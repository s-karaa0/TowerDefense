package org.example.towerdefense;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.example.towerdefense.Path.BuildSpots;
import org.example.towerdefense.Path.GamePath;
import org.example.towerdefense.Path.PathFactory;
import org.example.towerdefense.Wave.Wave;
import org.example.towerdefense.Wave.WaveLoader;
import org.example.towerdefense.Wave.WaveManager;

public class GameController {
    private final AnchorPane root = new AnchorPane();
    private GameApplication gameApp;
    private Game game;
    private Gameloop loop;
    private WaveManager waveManager;
    private boolean guiSetup = false;
    private boolean drag = false;
    private boolean endOfGame = false;
    private boolean conclusion; // true = won | false = lost

    //**************
    private HashSet<Enemy> enemyHashSet = new HashSet<>();
    //**************

    // FX:
    private ImageView backgroundimageView = new ImageView();
    private final Image buildSpotImg =
            new Image(getClass().getResourceAsStream("/images/buildspot.png"));

    private final Image tower1Img =
            new Image(getClass().getResourceAsStream("/images/towers/turm-lev1.png"));

    private final Image tower2Img =
            new Image(getClass().getResourceAsStream("/images/towers/turm-lev2.png"));

    private final Image tower3Img =
            new Image(getClass().getResourceAsStream("/images/towers/turm-lev3.png"));

    private final Image tower4Img =
            new Image(getClass().getResourceAsStream("/images/towers/turm-lev4.png"));


    private final ContextMenu buildMenu = new ContextMenu();
    private final ContextMenu aimMenu = new ContextMenu();
    private final HBox upgradeHBox = new HBox();
    private final ChoiceBox<String> towerChoice = new ChoiceBox<>();
    private final ChoiceBox<String> aimChoice = new ChoiceBox<>();
    private final Map<Point2D, Tower> towerMap = new HashMap<>();
    private Point2D selectedSpot;
    private Tower selectedTower;

    private final Button stopButton = new Button("Stop");
    private final Button resumeButton = new Button("Resume");
    private final Button restartButton = new Button("Restart");
    private final Button normalSpeedButton = new Button("1x");
    private final Button doubleSpeedButton = new Button("2x");
    private final Button quadroupleSpeedButton = new Button("4x");
    private final Button nextWaveButton = new Button("skip");
    private Label hp = new Label();
    private Label money = new Label();
    private final HBox hBox = new HBox();
    private String difficulty;
    private List<Point2D> buildSpots;
    private HashSet<Point2D> usedSpots = new HashSet<>();
    private GamePath path;

    public void setApplication(GameApplication gameApp) {
        this.gameApp = gameApp;
    }


    public GameController(String difficulty) {

        // Path
        path = PathFactory.createPath(difficulty);

        // Spots where you can place Buildings
        buildSpots = BuildSpots.createBuildSpots(difficulty);
        towerChoice.setItems(FXCollections.observableArrayList(
                towerOption("Tower1", 100),
                towerOption("Tower2", 150),
                towerOption("Tower3", 200),
                towerOption("Tower4", 300)
        ));

        aimChoice.setItems(FXCollections.observableArrayList("erster", "letzter", "staerkster", "schnellster"));
        CustomMenuItem aimItem = new CustomMenuItem(aimChoice);
        aimItem.setHideOnClick(false);
        CustomMenuItem towerItem = new CustomMenuItem(towerChoice);
        towerItem.setHideOnClick(false);
        buildMenu.getItems().add(towerItem);
        aimMenu.getItems().add(aimItem);

        towerChoice.setOnAction(e -> {
            String value = towerChoice.getValue();
            if (value == null || selectedSpot == null) return;
            String selected = value.split(" ")[0];
            String match = value.replaceAll(".*\\((\\d+)\\).*", "$1");
            int cost = Integer.parseInt(match);
            buildTower(cost, selected);

            Platform.runLater(() -> {
                buildMenu.hide();
                towerChoice.getSelectionModel().clearSelection();
            });
        });

        aimChoice.setOnAction(e -> {
            if (selectedTower == null) return;

            String selected = aimChoice.getValue();

            System.out.println("aim clicked");
            if (selected == null) return;
            selectedTower.getAim(selected);
            System.out.println("Target: " + selected);

        });

        // problems, problems, PROBLEMS EVERYWHERE.
        for (Point2D p : buildSpots) {

            Rectangle spot = new Rectangle(64, 64);
            spot.setFill(Color.BLACK);
            spot.setLayoutX(p.getX() - 32);
            spot.setLayoutY(p.getY() - 32);

            ImageView spotImage = new ImageView(buildSpotImg);
            spotImage.setFitWidth(64);
            spotImage.setFitHeight(64);
            spotImage.setLayoutX(p.getX() - 32);
            spotImage.setLayoutY(p.getY() - 32);
            spotImage.setMouseTransparent(true);
            spotImage.toFront();
            System.out.println(buildSpotImg);
            System.out.println(tower1Img);

            spot.setUserData(p);

            spot.setOnMouseClicked(e -> {
                System.out.println(drag);
                if (drag) {
                    return;
                }
                selectedSpot = (Point2D) spot.getUserData();
                Tower tower = towerMap.get(selectedSpot);

                if (e.getButton() == (javafx.scene.input.MouseButton.PRIMARY)) {

                    if (tower == null) {
                        towerChoice.setVisible(true);
                        buildMenu.show(spot, e.getScreenX(), e.getScreenY());
                    } else {
                        selectedTower = tower;
                        upgradeHboxToMousePosition(e.getSceneX(), e.getSceneY(), selectedTower);
                    }

                } else if (e.getButton() == (javafx.scene.input.MouseButton.SECONDARY)) {

                    if (tower != null) {
                        aimMenu.show(spot, e.getScreenX(), e.getScreenY());
                    }
                }

                if (tower != null) {
                    spot.setOnMouseEntered(c -> tower.getRangeCircle().setVisible(true));
                    spot.setOnMouseExited(c -> tower.getRangeCircle().setVisible(false));
                }

            });

            root.getChildren().add(spot);
            // root.getChildren().add(spotImage);
            // JavaFX cant handle some/many images and/or images are too big. Line above is proof.
        }

        // Game
        game = new Game(path);
        game.setOnProjectileSpawned(p -> root.getChildren().add(p.getView()));
        game.setOnProjectileRemoved(p -> root.getChildren().remove(p.getView()));
        createShop();

        // XML
        List<Wave> waves = WaveLoader.loadWaves();
        System.out.println("load waves: " + waves.size());

        // Wave manager
        waveManager = new WaveManager(game, waves);
        waveManager.startNextWave();

        // -------------------------------------------------
        // FXML \ Buttons \ onClicks
        stopButton.setOnAction(e -> {
            loop.pause();
            System.out.println("speed: " + loop.getSpeed());
            try {
                gameApp.switchScene("/settings-view.fxml", "settings");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        resumeButton.setOnAction(e -> {
            loop.resume();
        });
        restartButton.setOnAction(e -> {
            restartLoop();
        });

        nextWaveButton.setOnAction(e -> {
            waveManager.triggerNextWave();
        });

        // speedButtons to change GameLoop-Speed
        normalSpeedButton.setOnAction(e -> {
            loop.setSpeed(1);
        });
        doubleSpeedButton.setOnAction(e -> {
            loop.setSpeed(2);
        });
        quadroupleSpeedButton.setOnAction(e -> {
            loop.setSpeed(4);
        });
        root.setOnMouseClicked(e -> {
            if (e.getTarget() instanceof javafx.scene.shape.Rectangle) return;
            upgradeHBox.setVisible(false);
        });

        // --------------------------------------------------
        // Layout
        root.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                System.out.println("size changed");
                guiSetup = false;
            }
        });

        hBox.getChildren().addAll(stopButton, resumeButton, restartButton, normalSpeedButton, doubleSpeedButton, quadroupleSpeedButton);
        root.getChildren().addAll(hp, money);
        root.getChildren().addAll(hBox, upgradeHBox);
        root.getChildren().add(nextWaveButton);
        root.getChildren().addFirst(backgroundimageView);
        // ----------------------------------------------------


        // Looop, very important
        loop = new Gameloop(game, waveManager) {
            @Override
            public void handle(long now) {
                super.handle(now);
                sync();
                updateGUI();
            }
        };
    }

    public void updateGUI() {
        hp.setText(String.valueOf("Health Points: " + game.getHp()));
        money.setText("Gold: " + game.getMoney());
        if (!guiSetup) {
            double rootX = root.getLayoutBounds().getWidth();
            double rootY = root.getLayoutBounds().getHeight();

            double rootMaxX = root.getWidth();
            double rootMaxY = root.getHeight();

            System.out.println(rootX + " " + rootY);
            System.out.println(rootMaxX + " " + rootMaxY);

            nextWaveButton.setLayoutX(30);
            nextWaveButton.setLayoutY(500);

            hp.setLayoutX(rootMaxX - 160);
            hp.setFont(new Font("Arial", 20));
            hp.setTextAlignment(TextAlignment.LEFT);
            hp.setTextFill(Color.WHITE);

            money.setLayoutX(rootMaxX - 160 - 160);
            money.setFont(new Font("Arial", 20));
            money.setTextFill(Color.WHITE);

            upgradeHBox.setVisible(false);

            guiSetup = true;
        }

    }

    public void sync() {
        // add enemies, to the Set
        for (Enemy e : game.getEnemies()) {
            if (!enemyHashSet.contains(e)) {
                root.getChildren().add(e.getView());
                enemyHashSet.add(e);
                // System.out.println("enemy " + e.getId() + "|" + e.getGroupID() + " added");
            }
        }

        enemyHashSet.removeIf(e -> {
            if (!game.getEnemies().contains(e)) {
                root.getChildren().remove(e.getView());
                return true;
            }
            return false;
        });

        if (((waveManager.getEndOfAllWaves() && enemyHashSet.isEmpty()) || game.getHp() <= 0) && !endOfGame) {
            if (waveManager.getEndOfAllWaves()) {
                conclusion = true; // is a win
            } else if (game.getHp() <= 0) {
                conclusion = false; // is a loss
            }

            try {
                endOfGameCheck();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void endOfGameCheck() throws IOException {
        endOfGame = true;
        gameApp.setConclusion(conclusion);
        stopLoop();
        gameApp.switchScene("/conclusion-view.fxml", "conclusion");
    }

    private void showNoMoney() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cannot build tower");
        alert.setHeaderText(null);
        alert.setContentText("Not enough money!");

        alert.showAndWait();
    }

    public void deleteAllEnemies() {
        for (Enemy e : game.getEnemies()) {
            root.getChildren().remove(e.getView());
        }
    }

    public void createShop() {
        Image towerImage1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/towers/turm-lev1.png")));
        ImageView towerLev1 = new ImageView(towerImage1);
        towerLev1.setFitWidth(64);
        towerLev1.setFitHeight(64);
        towerLev1.setLayoutX(200);
        towerLev1.setLayoutY(720);

        Image towerImage2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/towers/turm-lev2.png")));
        ImageView towerLev2 = new ImageView(towerImage2);
        towerLev2.setFitWidth(64);
        towerLev2.setFitHeight(64);
        towerLev2.setLayoutX(400);
        towerLev2.setLayoutY(720);

        Image towerImage3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/towers/turm-lev3.png")));
        ImageView towerLev3 = new ImageView(towerImage3);
        towerLev3.setFitWidth(64);
        towerLev3.setFitHeight(64);
        towerLev3.setLayoutX(600);
        towerLev3.setLayoutY(720);

        Image towerImage4 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/towers/turm-lev4.png")));
        ImageView towerLev4 = new ImageView(towerImage4);
        towerLev4.setFitWidth(64);
        towerLev4.setFitHeight(64);
        towerLev4.setLayoutX(800);
        towerLev4.setLayoutY(720);

        root.getChildren().add(towerLev1);
        root.getChildren().add(towerLev2);
        root.getChildren().add(towerLev3);
        root.getChildren().add(towerLev4);

        towerLev1.setOnMouseClicked(event -> {
            spawnTower(event, towerImage1);
        });

        towerLev2.setOnMouseClicked(event -> {
            spawnTower(event, towerImage2);
        });

        towerLev3.setOnMouseClicked(event -> {
            spawnTower(event, towerImage3);
        });

        towerLev4.setOnMouseClicked(event -> {
            spawnTower(event, towerImage4);
        });


    }

    /**
     * private void addTowerMenu(ImageView view, Tower tower) {
     * <p>
     * ChoiceBox<Object> targetChoice = tower.getChoiceBox(); // dieselbe CB!
     * <p>
     * targetChoice.setItems(FXCollections.observableArrayList(
     * "erster", "letzter", "staerkster", "schnellster"));
     * <p>
     * ContextMenu targetMenu = new ContextMenu();
     * CustomMenuItem targetItem = new CustomMenuItem(targetChoice);
     * targetItem.setHideOnClick(false);
     * targetMenu.getItems().add(targetItem);
     * <p>
     * view.setOnContextMenuRequested(e ->
     * targetMenu.show(view, e.getScreenX(), e.getScreenY())
     * );
     * }
     **/

    public Pane getRoot() {
        return root;
    }

    public void stopLoop() {
        loop.stop();
    }

    public void startLoop() {
        loop.start();
    }

    public void resumeLoop() {
        loop.resume();
    }

    public void restartLoop() {
        loop.stop();
        deleteAllEnemies();
        game.resetGame();
        waveManager.resetAllWaves();
        waveManager.startNextWave();
        loop.start();
        loop.resume();
        upgradeHBox.setVisible(false);
    }

    public void getBackgroundImage(Image backgroundimg) {
        backgroundimageView.setImage(backgroundimg);
        backgroundimageView.setFitWidth(1000);
        backgroundimageView.setFitHeight(800);
    }

    private String towerOption(String name, int cost) {
        return name + " (" + cost + ")";
    }

    private String upgradeOption(String name, int cost) {
        return "+ " + name + " (" + cost + ")";
    }

    private void upgradeHboxToMousePosition(double x, double y, Tower tower) {
        upgradeHBox.getChildren().clear();

        Button dmg = new Button(upgradeOption("Damage +", tower.getUpgradeCost()));
        Button spd = new Button(upgradeOption("Speed +", tower.getUpgradeCost()));
        Button rng = new Button(upgradeOption("Range +", tower.getUpgradeCost()));

        dmg.setOnAction(e -> {
            tower.addUpgradeCost(25);
            if (game.getMoney() < 50) {
                showNoMoney();
                return;
            }
            game.addMoney(-50);
            tower.upgradeDamage();
            upgradeHBox.setVisible(true);
        });

        spd.setOnAction(e -> {
            tower.addUpgradeCost(25);
            if (game.getMoney() < 75) {
                showNoMoney();
                return;
            }
            game.addMoney(-75);
            tower.upgradeSpeed();
            upgradeHBox.setVisible(false);
        });

        rng.setOnAction(e -> {
            tower.addUpgradeCost(25);
            if (game.getMoney() < 100) {
                showNoMoney();
                return;
            }
            game.addMoney(-100);
            tower.upgradeRange();
            tower.getRangeCircle().setRadius(tower.getRangeCircle().getRadius() + 10);
            upgradeHBox.setVisible(false);
        });

        upgradeHBox.getChildren().addAll(dmg, spd, rng);
        upgradeHBox.setLayoutX(x - 100);
        upgradeHBox.setLayoutY(y);
        upgradeHBox.toFront();
        upgradeHBox.setVisible(true);
    }

    private void buildTower(int cost, String selected) {

        if (game.getMoney() < cost) {
            System.out.println("Not enough money!");
            showNoMoney();
            return;
        }

        game.addMoney(-cost);

        Image selectedImage = switch (selected) {
            case "Tower1" -> tower1Img;
            case "Tower2" -> tower2Img;
            case "Tower3" -> tower3Img;
            case "Tower4" -> tower4Img;
            default -> null;
        };

        if (selectedImage == null) return;

        Tower tower = new Tower(game, selectedImage);

        game.addTower(tower);
        ImageView view = tower.getView();
        view.setLayoutX(selectedSpot.getX() - 32);
        view.setLayoutY(selectedSpot.getY() - 32);

        towerMap.put(selectedSpot, tower);
        usedSpots.add(selectedSpot);

        root.getChildren().add(tower.getRangeCircle());

        root.getChildren().add(view);
        towerChoice.setVisible(false);
        towerChoice.hide();

        System.out.println("Tower gebaut: " + selected);
    }

    /*
    private void towerSelectionHBox(double x, double y, Tower tower) {
        HBox.getChildren().clear();

        Button dmg = new Button(upgradeOption("Damage +", tower.getUpgradeCost()));
        Button spd = new Button(upgradeOption("Speed +", tower.getUpgradeCost()));
        Button rng = new Button(upgradeOption("Range +", tower.getUpgradeCost()));

        dmg.setOnAction(e -> {
            tower.addUpgradeCost(25);
            if (game.getMoney() < 50) {
                showNoMoney();
                return;
            }
            game.addMoney(-50);
            tower.upgradeDamage();
            upgradeHBox.setVisible(true);
        });

        spd.setOnAction(e -> {
            tower.addUpgradeCost(25);
            if (game.getMoney() < 75) {
                showNoMoney();
                return;
            }
            game.addMoney(-75);
            tower.upgradeSpeed();
            upgradeHBox.setVisible(false);
        });

        rng.setOnAction(e -> {
            tower.addUpgradeCost(25);
            if (game.getMoney() < 100) {
                showNoMoney();
                return;
            }
            game.addMoney(-100);
            tower.upgradeRange();
            tower.getRangeCircle().setRadius(tower.getRangeCircle().getRadius() + 10);
            upgradeHBox.setVisible(false);
        });

        upgradeHBox.getChildren().addAll(dmg, spd, rng);
        upgradeHBox.setLayoutX(x - 100);
        upgradeHBox.setLayoutY(y);
        upgradeHBox.toFront();
        upgradeHBox.setVisible(true);
    }

     */


    private void spawnTower(MouseEvent event, Image selectedImage) {

        Tower tower = new Tower(game, selectedImage);
        ImageView view = tower.getView();
        root.getChildren().add(view);
        view.setLayoutX(event.getX() - 32);
        view.setLayoutY(event.getY() - 32);
        final boolean[] dragging = {true};

        root.setOnMouseMoved(e -> {
            if (dragging[0]) {
                view.setLayoutX(e.getX() - 32);
                view.setLayoutY(e.getY() - 32);
            }
        });

        root.setOnMouseClicked(e -> {
            if (drag) {
                dragging[0] = false;
                Point2D towerCenter = new Point2D(
                        view.getLayoutX() + 32,
                        view.getLayoutY() + 32
                );

                Point2D closestSpot = new Point2D(2, 2);
                double closestDistance = Double.MAX_VALUE;

                for (Point2D spot : buildSpots) {
                    double distance = towerCenter.distance(spot);
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestSpot = spot;
                        System.out.println("--");
                    }
                }

                selectedSpot = closestSpot;
                System.out.println(selectedSpot);

                if (closestSpot != null && closestDistance < 100 && !usedSpots.contains(closestSpot)) {
                    buildTower(100, "tower1");
                    view.setLayoutX(closestSpot.getX() - 32);
                    view.setLayoutY(closestSpot.getY() - 32);
                    view.toFront();
                    usedSpots.add(closestSpot);
                    root.setOnMouseMoved(null);
                    drag = false;
                    System.out.println("Tower placed");
                } else {
                    root.getChildren().remove(view);
                    root.setOnMouseMoved(null);
                    drag = false;
                    System.out.println("Invalid placement");
                }
            }
            drag = true;
        });
    }

}


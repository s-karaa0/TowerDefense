package org.example.towerdefense.Controllers;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Group;
import javafx.scene.transform.Scale;
import javafx.scene.layout.HBox;
import java.io.IOException;
import java.util.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.example.towerdefense.*;
import org.example.towerdefense.Path.BuildSpots;
import org.example.towerdefense.Path.GamePath;
import org.example.towerdefense.Path.PathFactory;
import org.example.towerdefense.Tower.Tower;
import org.example.towerdefense.Wave.Enemy;
import org.example.towerdefense.Wave.Wave;
import org.example.towerdefense.Wave.WaveLoader;
import org.example.towerdefense.Wave.WaveManager;

public class GameController {
    private final AnchorPane root = new AnchorPane();
    private final Group gameWorld = new Group();
    private final Scale worldScale = new Scale(1, 1, 0, 0);

    // Bildbereich rechts neben dem Spielfeld (Bild später setzen!!)
    private final ImageView sideImageView = new ImageView();
    private GameApplication gameApp;
    private Game game;
    private Gameloop loop;
    private WaveManager waveManager;
    private boolean guiSetup = false;
    private boolean endOfGame = false;
    private boolean conclusion; // true = won | false = lost

    //**************
    private HashSet<Enemy> enemyHashSet = new HashSet<>();
    //**************

    // FX:
    private ImageView backgroundimageView = new ImageView();
    private final Image buildSpotImg =
            new Image(getClass().getResourceAsStream("/images/buildspot_small.png"));

    private final Image tower1Img =
            new Image(getClass().getResourceAsStream("/images/towers/turm-lev1_small.png"));

    private final Image tower2Img =
            new Image(getClass().getResourceAsStream("/images/towers/turm-lev2_small.png"));

    private final Image tower3Img =
            new Image(getClass().getResourceAsStream("/images/towers/turm-lev3_small.png"));

    private final Image tower4Img =
            new Image(getClass().getResourceAsStream("/images/towers/turm-lev4_small.png"));


    private final ContextMenu buildMenu = new ContextMenu();
    private final ContextMenu aimMenu = new ContextMenu();
    private final HBox upgradeHBox = new HBox();
    private final ChoiceBox<String> towerChoice = new ChoiceBox<>();
    private final ChoiceBox<String> aimChoice = new ChoiceBox<>();
    private final Map<Point2D, Tower> towerMap = new HashMap<>();
    private Point2D selectedSpot;
    private Tower selectedTower;

    private final Button stopButton = new Button("⚙ Settings");
    //private final Button resumeButton = new Button("Resume");
    //private final Button restartButton = new Button("Restart");
    private final Button normalSpeedButton = new Button("1x");
    private final Button doubleSpeedButton = new Button("2x");
    private final Button quadroupleSpeedButton = new Button("4x");
    private final Button nextWaveButton = new Button("Nächste Welle");
    private final Label hp = new Label();
    private final ImageView healthHeart =
            new ImageView(new Image(getClass().getResourceAsStream("/images/hud/bright-red-pixel-heart-design-png.png")));
    private final HBox healthDisplay = new HBox(8);
    private final Label money = new Label();
    private final ImageView coinIcon =
            new ImageView(new Image(getClass().getResourceAsStream("/images/hud/coins.png")));
    private final HBox moneyDisplay = new HBox(8);
    private final HBox resourceBar = new HBox(18);
    private final HBox hBox = new HBox(6);
    private String difficulty;
    private List<Point2D> buildSpots;
    private HashSet<Point2D> usedSpots = new HashSet<>();
    private GamePath path;

    // show tower stats on hover
    private final VBox hoverStatsBox = new VBox(3); // Abstand 3px
    private final Label hoverDamageLabel = new Label();
    private final Label hoverSpeedLabel = new Label();

    private Map<Point2D, Rectangle> spotMap = new HashMap<>();
    private Map<Point2D, ImageView> spotImageMap = new HashMap<>();

    public void setApplication(GameApplication gameApp) {
        this.gameApp = gameApp;
    }


    public GameController(String difficulty) {
        root.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());

        // Alle Elemente des eigentlichen Spielfeldes werden gemeinsam skaliert.
        gameWorld.getTransforms().add(worldScale);
        gameWorld.getChildren().add(backgroundimageView);

        // Die Basisgroesse der Map ist 1000 x 800.
        root.heightProperty().addListener((obs, oldHeight, newHeight) -> updateWorldAndSideImage());
        root.widthProperty().addListener((obs, oldWidth, newWidth) -> updateWorldAndSideImage());

        sideImageView.setPreserveRatio(false);
        sideImageView.setSmooth(true);
        sideImageView.setMouseTransparent(true);

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

        String noBlue = "-fx-background-color: transparent;"
                + " -fx-padding: 0;"
                + "-fx-focus-color: transparent;"
                + " -fx-faint-focus-color: transparent;"
                + "-fx-selection-bar: transparent;"
                + " -fx-accent: transparent;";

        buildMenu.setStyle(noBlue);
        aimMenu.setStyle(noBlue);
        towerItem.setStyle(noBlue);
        aimItem.setStyle(noBlue);

        towerChoice.setPrefWidth(150);
        towerChoice.setMinWidth(150);
        aimChoice.setPrefWidth(150);
        aimChoice.setMinWidth(150);

        buildMenu.getItems().add(towerItem);
        aimMenu.getItems().add(aimItem);

        // init stat vbox
        hoverStatsBox.setId("hover-stats-box");
        hoverStatsBox.getChildren().addAll(hoverDamageLabel, hoverSpeedLabel);
        hoverStatsBox.setMouseTransparent(true);
        hoverStatsBox.setVisible(false);
        root.getChildren().add(hoverStatsBox);

        towerChoice.setOnAction(e -> {
            String value = towerChoice.getValue();
            if (value == null || selectedSpot == null) return;
            String selected = value.split(" ")[0];
            String match = value.replaceAll(".*\\((\\d+)\\).*", "$1");
            int cost = Integer.parseInt(match);
            buildTower(cost, selected);

            if (spotMap.get(selectedSpot) != null) spotMap.get(selectedSpot).setOpacity(0);
            if (spotImageMap.get(selectedSpot) != null) spotImageMap.get(selectedSpot).setOpacity(0);

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

            spot.setUserData(p);

            spotMap.put(p, spot);
            spotImageMap.put(p, spotImage);

            // Add BOTH to the same parent
            gameWorld.getChildren().addAll(spot, spotImage);

            spotImage.toFront();

            spot.setOnMouseClicked(e -> {

                selectedSpot = (Point2D) spot.getUserData();
                Tower tower = towerMap.get(selectedSpot);

                if (e.getButton() == MouseButton.PRIMARY) {

                    if (tower == null) {
                        aimMenu.hide();
                        buildMenu.hide();
                        towerChoice.hide();
                        towerChoice.setVisible(true);
                        buildMenu.show(spot, e.getScreenX(), e.getScreenY());
                    } else {
                        selectedTower = tower;
                        upgradeHboxToMousePosition(
                                e.getSceneX(),
                                e.getSceneY(),
                                selectedTower
                        );
                    }

                } else if (e.getButton() == MouseButton.SECONDARY) {

                    if (tower != null) {
                        buildMenu.hide();
                        aimMenu.hide();
                        aimChoice.hide();
                        aimMenu.show(spot, e.getScreenX(), e.getScreenY());
                    }
                }
            });

            spot.setOnMouseEntered(e -> {
                Tower tower = towerMap.get((Point2D) spot.getUserData());

                if (tower != null) {
                    tower.getRangeCircle().setVisible(true);
                }
            });

            spot.setOnMouseExited(e -> {
                Tower tower = towerMap.get((Point2D) spot.getUserData());

                if (tower != null) {
                    spot.setOnMouseEntered(c -> {
                        tower.getRangeCircle().setVisible(true);

                        // !!! show tower stats
                        hoverDamageLabel.setText("Damage: " + tower.getDamage());
                        hoverSpeedLabel.setText("Speed: " + String.format("%.2f", tower.getCooldown()));
                        // show at mouse pos
                        hoverStatsBox.setLayoutX(c.getSceneX());
                        hoverStatsBox.setLayoutY(c.getSceneY());
                        // move in front of the tower image
                        hoverStatsBox.toFront();
                        hoverStatsBox.setVisible(true);
                    });
                    spot.setOnMouseExited(c -> {
                        tower.getRangeCircle().setVisible(false);
                        hoverStatsBox.setVisible(false);
                    });
                }
            });

        }


        // Game
        game = new Game(path);
        game.setOnProjectileSpawned(p -> gameWorld.getChildren().add(p.getView()));
        game.setOnProjectileRemoved(p -> gameWorld.getChildren().remove(p.getView()));

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

//        testing side image with a Picture
//        Image img = new Image(getClass().getResourceAsStream("/images/hud/coins.png"));
//        setSideImage(img);

        hBox.getChildren().addAll(stopButton, normalSpeedButton, doubleSpeedButton, quadroupleSpeedButton);
        nextWaveButton.getStyleClass().add("next-wave-button");
        hBox.getStyleClass().add("game-toolbar");
        stopButton.getStyleClass().add("settings-game-button");
        aimChoice.getStyleClass().add("tower-choice-box");
        towerChoice.getStyleClass().add("tower-choice-box");
        towerChoice.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
        normalSpeedButton.getStyleClass().add("speed-button");
        doubleSpeedButton.getStyleClass().add("speed-button");
        quadroupleSpeedButton.getStyleClass().add("speed-button");
        resourceBar.getStyleClass().add("resource-bar");
        healthHeart.setFitWidth(28);
        healthHeart.setFitHeight(28);
        healthHeart.setPreserveRatio(true);
        healthDisplay.setAlignment(Pos.CENTER_LEFT);
        healthDisplay.getChildren().addAll(healthHeart, hp);
        coinIcon.setFitWidth(28);
        coinIcon.setFitHeight(28);
        coinIcon.setPreserveRatio(true);
        moneyDisplay.setAlignment(Pos.CENTER_LEFT);
        moneyDisplay.getChildren().addAll(coinIcon, money);
        resourceBar.setAlignment(Pos.CENTER_LEFT);
        resourceBar.getChildren().addAll(healthDisplay, moneyDisplay);
        AnchorPane.setLeftAnchor(resourceBar, 20.0);
        AnchorPane.setTopAnchor(resourceBar, 20.0);
        AnchorPane.setRightAnchor(hBox, 20.0);
        AnchorPane.setTopAnchor(hBox, 20.0);
        AnchorPane.setLeftAnchor(nextWaveButton, 30.0);
        AnchorPane.setBottomAnchor(nextWaveButton, 30.0);
        root.getChildren().addAll(gameWorld, sideImageView);
        root.getChildren().add(resourceBar);
        root.getChildren().addAll(hBox, upgradeHBox);
        root.getChildren().add(nextWaveButton);
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
        hp.setText(String.valueOf(game.getHp()));
        money.setText(String.valueOf(game.getMoney()));
        if (!guiSetup) {
            double rootX = root.getLayoutBounds().getWidth();
            double rootY = root.getLayoutBounds().getHeight();

            double rootMaxX = root.getWidth();
            double rootMaxY = root.getHeight();

            System.out.println(rootX + " " + rootY);
            System.out.println(rootMaxX + " " + rootMaxY);

            hp.setFont(new Font("Arial", 20));
            hp.setTextAlignment(TextAlignment.LEFT);
            hp.setTextFill(Color.WHITE);

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
                gameWorld.getChildren().add(e.getView());
                enemyHashSet.add(e);
                // System.out.println("enemy " + e.getId() + "|" + e.getGroupID() + " added");
            }
        }

        enemyHashSet.removeIf(e -> {
            if (!game.getEnemies().contains(e)) {
                gameWorld.getChildren().remove(e.getView());
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
            gameWorld.getChildren().remove(e.getView());
        }
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
        backgroundimageView.setPreserveRatio(false);
    }

    public void setSideImage(Image image) {
        sideImageView.setImage(image);
        updateWorldAndSideImage();
    }

    // Hält Spielfeld und rechtes Bild bei jeder Fenstergrößenänderung passend.
    private void updateWorldAndSideImage() {
        double height = root.getHeight();
        double width = root.getWidth();
        if (height <= 0) return;

        double scale = height / 800.0;
        worldScale.setX(scale);
        worldScale.setY(scale);

        double mapWidth = 1000.0 * scale;
        double remainingWidth = Math.max(0, width - mapWidth);

        sideImageView.setLayoutX(mapWidth);
        sideImageView.setLayoutY(0);
        sideImageView.setFitWidth(remainingWidth);
        sideImageView.setFitHeight(height);
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
        int id = switch (selected) {
            case "Tower2" -> 1;
            case "Tower3" -> 2;
            case "Tower4" -> 3;
            default -> 0; // Tower1 is also 0
        };

        // maybe move to conf
        Image selectedImage = switch (selected) {
            case "Tower1" -> tower1Img;
            case "Tower2" -> tower2Img;
            case "Tower3" -> tower3Img;
            case "Tower4" -> tower4Img;
            default -> null;
        };

        Color towerColor = switch (selected) {
            case "Tower1" -> Color.GREEN;
            case "Tower2" -> Color.BLUE;
            case "Tower3" -> Color.RED;
            case "Tower4" -> Color.WHITE;
            default -> null;
        };

        if (selectedImage == null) return;

        Tower tower = new Tower(game, selectedImage, towerColor);
        // load tower specific config
        tower.overwriteToType(id);

        if (game.getMoney() < tower.getBuildCost()) {
            System.out.println("Not enough money!");
            showNoMoney();
            return;
        }

        game.addMoney(-tower.getBuildCost());

        game.addTower(tower);
        ImageView view = tower.getView();
        view.setLayoutX(selectedSpot.getX() - 32);
        view.setLayoutY(selectedSpot.getY() - 32);
        view.setMouseTransparent(true);

        Circle range = tower.getRangeCircle();
        range.setStroke(Color.BLACK);
        range.setFill(tower.getDerivedColor());
        range.setStrokeWidth(2);
        range.toFront();

        PauseTransition pause = new javafx.animation.PauseTransition(
                javafx.util.Duration.seconds(1)
        );
        pause.setOnFinished(e -> range.setVisible(false));
        pause.play();


        towerMap.put(selectedSpot, tower);
        usedSpots.add(selectedSpot);

        gameWorld.getChildren().addAll(view, range);
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
}

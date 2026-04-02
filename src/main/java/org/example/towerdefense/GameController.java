package org.example.towerdefense;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.example.towerdefense.Path.GamePath;
import org.example.towerdefense.Wave.Wave;
import org.example.towerdefense.Wave.WaveLoader;
import org.example.towerdefense.Wave.WaveManager;

public class GameController {
    private final AnchorPane root = new AnchorPane();
    private final GameApplication gameApp = new GameApplication();
    private Game game;
    private Gameloop loop;
    private WaveManager waveManager;
    private boolean guiSetup = false;
    private ChoiceBox<Object> cb = new ChoiceBox<>();
    private boolean endOfGame = false;

    // FX:
    private final Button stopButton = new Button("Stop");
    private final Button resumeButton = new Button("Resume");
    private final Button restartButton = new Button("Restart");
    private final Button normalSpeedButton = new Button("1x");
    private final Button doubleSpeedButton = new Button("2x");
    private final Button quadroupleSpeedutton = new Button("4x");
    private final Button nextWaveButton = new Button("skip");
    private Label hp = new Label();
    private final HBox hBox = new HBox();
    private String difficulty;

    private HashSet<Enemy> enemyHashSet = new HashSet<>();
    private List<Point2D> buildSpots = List.of(
            new Point2D(200, 300),
            new Point2D(400, 250),
            new Point2D(600, 450),
            new Point2D(800, 300));
    private HashSet<Point2D> usedSpots = new HashSet<>();

    public GameController(String difficulty) {
        this.difficulty = difficulty;
        // Path
        List<GamePath.BezierSegment> segments = List.of(
                new GamePath.BezierSegment(
                        new Point2D(50, 400),
                        new Point2D(50, 400),
                        new Point2D(200, 400),
                        new Point2D(200, 400)
                ),
                new GamePath.BezierSegment(
                        new Point2D(200, 400),
                        new Point2D(400, 400),
                        new Point2D(200, 100),
                        new Point2D(400, 100)
                ),
                new GamePath.BezierSegment(
                        new Point2D(400, 100),
                        new Point2D(600, 100),
                        new Point2D(400, 700),
                        new Point2D(600, 700)
                ),
                new GamePath.BezierSegment(
                        new Point2D(600, 700),
                        new Point2D(600, 700),
                        new Point2D(800, 700),
                        new Point2D(800, 700)
                ),
                new GamePath.BezierSegment(
                        new Point2D(800, 700),
                        new Point2D(900, 700),
                        new Point2D(900, 700),
                        new Point2D(900, 500)
                ),
                new GamePath.BezierSegment(
                        new Point2D(900, 500),
                        new Point2D(900, 350),
                        new Point2D(700, 350),
                        new Point2D(700, 500)
                ),
                new GamePath.BezierSegment(
                        new Point2D(700, 500),
                        new Point2D(700, 620),
                        new Point2D(800, 600),
                        new Point2D(1000, 600)
                )
        );

        GamePath path = new GamePath(segments, 10024);

        // Game
        game = new Game(path);
        game.setOnProjectileSpawned(p -> root.getChildren().add(p.getView()));

// Und noch den remove-Callback in Game.java hinzufügen (wie vorhin erklärt)
        game.setOnProjectileRemoved(p -> root.getChildren().remove(p.getView()));
        createShop();

        for (Point2D p : buildSpots) {
            Image img = new Image(getClass().getResourceAsStream("/images/buildspot.png"));
            ImageView spot = new ImageView(img);

            spot.setFitWidth(64);
            spot.setFitHeight(64);
            spot.setOpacity(0.4);

            spot.setLayoutX(p.getX() - 32);
            spot.setLayoutY(p.getY() - 32);

            ChoiceBox<String> towerChoice = new ChoiceBox<>();
            towerChoice.setItems(FXCollections.observableArrayList("Tower1", "Tower2", "Tower3"));

            ContextMenu buildMenu = new ContextMenu();
            CustomMenuItem towerItem = new CustomMenuItem(towerChoice);
            towerItem.setHideOnClick(false);

            buildMenu.getItems().add(towerItem);

            spot.setOnContextMenuRequested(e ->
                    buildMenu.show(spot, e.getScreenX(), e.getScreenY())
            );

            towerChoice.setOnAction(e -> {

                String selected = towerChoice.getValue();

                Image selectedImage = null;

                switch (selected) {
                    case "Tower1":
                        selectedImage = new Image(getClass().getResourceAsStream("/images/towers/turm-lev1.png"));
                        break;
                    case "Tower2":
                        selectedImage = new Image(getClass().getResourceAsStream("/images/towers/turm-lev2.png"));
                        break;
                    case "Tower3":
                        selectedImage = new Image(getClass().getResourceAsStream("/images/towers/turm-lev3.png"));
                        break;
                }

                if (selected != null && !usedSpots.contains(p)) {

                    Tower tower = new Tower(10, game, selectedImage);
                    game.addTower(tower);

                    ImageView view = tower.getView();

                    view.setLayoutX(p.getX() - 32);
                    view.setLayoutY(p.getY() - 32);

                    root.getChildren().add(view);

                    usedSpots.add(p);

                    //addTowerMenu(view, tower);

                    System.out.println("Tower gebaut: " + selected);
                }
            });

            root.getChildren().add(spot);
        }

        // XML
        List<Wave> waves = WaveLoader.loadWaves();
        System.out.println("load waves: " + waves.size());

        // Wave manager
        waveManager = new WaveManager(game, WaveLoader.loadWaves());
        waveManager.startNextWave();

        // -------------------------------------------------
        // FXML \ Buttons \ onClicks
        root.setOnMouseClicked(game::handleMouseClicked);

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
        quadroupleSpeedutton.setOnAction(e -> {
            loop.setSpeed(4);
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

        hBox.getChildren().addAll(stopButton, resumeButton, restartButton, normalSpeedButton, doubleSpeedButton, quadroupleSpeedutton);
        root.getChildren().add(hp);
        root.getChildren().add(hBox);
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
        hp.setText(String.valueOf("Health Points: " + game.getHp()));
        if (!guiSetup) {
            double rootX = root.getLayoutBounds().getWidth();
            double rootY = root.getLayoutBounds().getHeight();

            double rootMaxX = root.getWidth();
            double rootMaxY = root.getHeight();
            
            System.out.println(rootX + " " + rootY);
            System.out.println(rootMaxX + " " + rootMaxY);

            nextWaveButton.setLayoutX(30);
            nextWaveButton.setLayoutY(500);

            hp.setLayoutX(rootMaxX - 200);
            hp.setLayoutY(100);

            guiSetup = true;
        }

    }

    public void sync() {
        // add enemies, to the Set
        for (Enemy e : game.getEnemies()) {
            if (!enemyHashSet.contains(e)) {
                root.getChildren().add(e.getView());
                enemyHashSet.add(e);

                // experimental:
                System.out.println("enemy " + e.getId() + "|" + e.getGroupID() + " added");
                // if (i == 5 || i == 10){loop.pause();}
            }
        }

        // rm enemies, via their Set
        enemyHashSet.removeIf(e -> {
            if (!game.getEnemies().contains(e)) {
                root.getChildren().remove(e.getView());
                return true;
            }
            return false;
        });

        if (game.getHp() <= 0 && !endOfGame) {
            try {
                endOfGameCheck();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void endOfGameCheck() throws IOException {
            endOfGame = true;
            stopLoop();
            gameApp.switchScene("/conclusion-view.fxml", "conclusion");
    }

    public void deleteAllEnemies() {
        for (Enemy e : game.getEnemies()) {
            root.getChildren().remove(e.getView());
            e.destroy(root);
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

    /**private void addTowerMenu(ImageView view, Tower tower) {

        ChoiceBox<Object> targetChoice = tower.getChoiceBox(); // dieselbe CB!

        targetChoice.setItems(FXCollections.observableArrayList(
                "erster", "letzter", "staerkster", "schnellster"));

        ContextMenu targetMenu = new ContextMenu();
        CustomMenuItem targetItem = new CustomMenuItem(targetChoice);
        targetItem.setHideOnClick(false);
        targetMenu.getItems().add(targetItem);

        view.setOnContextMenuRequested(e ->
                targetMenu.show(view, e.getScreenX(), e.getScreenY())
        );
    }**/

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
        game.resetEnemies();
        waveManager.resetAllWaves();
        waveManager.startNextWave();
        loop.start();
        loop.resume();
    }

    private void spawnTower(MouseEvent event, Image selectedImage) {

        Tower tower = new Tower(10, game, selectedImage);
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

        view.setOnMouseReleased(e -> {
            dragging[0] = false;

            Point2D towerCenter = new Point2D(
                    view.getLayoutX() + 32,
                    view.getLayoutY() + 32
            );

            Point2D closestSpot = null;
            double closestDistance = Double.MAX_VALUE;

            for (Point2D spot : buildSpots) {
                double distance = towerCenter.distance(spot);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestSpot = spot;
                }
            }

            if (closestSpot != null && closestDistance < 50 && !usedSpots.contains(closestSpot)) {

                view.setLayoutX(closestSpot.getX() - 32);
                view.setLayoutY(closestSpot.getY() - 32);

                usedSpots.add(closestSpot);

                root.setOnMouseMoved(null);

                System.out.println("Tower placed");

            } else {
                root.getChildren().remove(view);
                System.out.println("Invalid placement");
            }
        });
    }

}


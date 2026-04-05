package org.example.towerdefense;

import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.towerdefense.Path.GamePath;
import org.example.towerdefense.Wave.Wave;
import org.example.towerdefense.Wave.WaveLoader;
import org.example.towerdefense.Wave.WaveManager;

public class GameController {
    private final AnchorPane root = new AnchorPane();
    private final GameApplication gameApp = new GameApplication();
    private Game game;
    private Gameloop loop;
    private Image towerImage;
    private int lastEnemyCount = 0;
    private boolean guiSetup = false;
    ChoiceBox<Object> cb = new ChoiceBox<>();

    // FX:
    private final Button stopButton = new Button("Stop");
    private final Button resumeButton = new Button("Resume");
    private final Button restartButton = new Button("Restart");
    private final Button normalSpeedButton = new Button("1x");
    private final Button doubleSpeedButton = new Button("2x");
    private final Button quadroupleSpeedutton = new Button("4x");
    private final Button nextWaveButton = new Button("skip");
    private final Label hp = new Label();
    private final HBox hBox = new HBox();

    private HashSet<Enemy> enemyHashSet = new HashSet<>();
    private List<Point2D> buildSpots = List.of(
            new Point2D(200, 300),
            new Point2D(400, 250),
            new Point2D(600, 450),
            new Point2D(800, 300));
    private int i = 0;
    private HashSet<Point2D> usedSpots = new HashSet<>();

    public GameController() {
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
            ImageView spot = new ImageView(towerImage);

            spot.setFitWidth(64);
            spot.setFitHeight(64);
            spot.setOpacity(0.1);

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

                if (selected != null && !usedSpots.contains(p)) {

                    Tower tower = new Tower(10, game);
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
        WaveManager waveManager = new WaveManager(game, WaveLoader.loadWaves());
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
            loop.stop();
            deleteAllEnemies();
            i = 0;
            game.resetEnemies();
            waveManager.resetAllWaves();
            waveManager.startNextWave();
            loop.start();
            loop.resume();
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
            ;
            double rootY = root.getLayoutBounds().getHeight();

            double rootMaxX = root.getWidth();
            double rootMaxY = root.getHeight();


            System.out.println(rootX + " " + rootY);
            System.out.println(rootMaxX + " " + rootMaxY);

            nextWaveButton.setLayoutX(30);
            nextWaveButton.setLayoutY(300);
            hp.setLayoutX(rootX + 30);
            hp.setLayoutY(60);


            guiSetup = true;
        }

    }

    public void sync() {
        // add enemies, to the Set
        for (Enemy e : game.getEnemies()) {
            if (!enemyHashSet.contains(e)) {
                root.getChildren().add(e.getView());
                enemyHashSet.add(e);
                i++;

                // experimental:
                System.out.println("enemy " + i + " added");
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
    }

    public void deleteAllEnemies() {
        for (Enemy e : game.getEnemies()) {
            root.getChildren().remove(e.getView());
            e.destroy(root);
        }
    }

    public void createShop() {
        towerImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tower.png")));

        ImageView shopTower = new ImageView(towerImage);

        shopTower.setFitWidth(64);
        shopTower.setFitHeight(64);

        shopTower.setLayoutX(20);
        shopTower.setLayoutY(720);

        root.getChildren().add(shopTower);

        shopTower.setOnMouseClicked(event -> {
            Tower tower = new Tower(10,  game);
            game.addTower(tower);
            ImageView view = tower.getView();

            ChoiceBox<String> cb = new ChoiceBox<>();
            cb.setItems(FXCollections.observableArrayList(
                    "erster",
                    "letzter",
                    "staerkster",
                    "schnellster"
            ));

            ContextMenu contextMenu = new ContextMenu();
            CustomMenuItem choiceItem = new CustomMenuItem(cb);
            choiceItem.setHideOnClick(false);

            contextMenu.getItems().add(choiceItem);

            view.setOnContextMenuRequested(e ->
                    contextMenu.show(view, e.getScreenX(), e.getScreenY())
            );

            view.setLayoutX(event.getX() - 32);
            view.setLayoutY(event.getY() - 32);

            root.getChildren().add(view);

            root.setOnMouseMoved(e -> {
                view.setLayoutX(e.getX() - 32);
                view.setLayoutY(e.getY() - 32);
            });

            // DRAG
            view.setOnMouseDragged(e -> {
                view.setLayoutX(e.getX() - 32);
                view.setLayoutY(e.getY() - 32);
            });

            // DROP
            view.setOnMouseReleased(e -> {
                System.out.println("placed Tower: "
                        + view.getLayoutX() + " / "
                        + view.getLayoutY());
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
                    view.setImage(towerImage);

                    System.out.println("Tower placed");
                    root.setOnMouseMoved(null);
                    view.setOnMouseDragged(null);

                } else {
                    System.out.println("Invalid placement");
                }
            });
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
    public Game getGame() {
        return this.game;
    }
    public Parent getRoot() {
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
}


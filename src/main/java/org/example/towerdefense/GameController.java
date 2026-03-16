package org.example.towerdefense;

import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
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
    private final GameApplication  gameApp = new GameApplication();
    private Game game;
    private Gameloop loop;
    private Image towerImage;
    private int lastEnemyCount = 0;
    private final Button stopButton = new Button("Stop");
    private final Button resumeButton = new Button("Resume");
    private final Button restartButton = new Button("Restart");
    private final Button normalSpeedButton = new Button("1x");
    private final Button doubleSpeedButton = new Button("2x");
    private final Button quadroupleSpeedutton = new Button("4x");
    private final Button nextWaveButton = new Button("skip");
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

        GamePath path = new GamePath( segments,10024);

        // Game
        game = new Game(path);
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

                    addTowerMenu(view);

                    System.out.println("Tower gebaut: " + selected);
                }
            });

            root.getChildren().add(spot);
        }

        root.setOnMouseClicked(game::handleMouseClicked);

        // XML
        List<Wave> waves = WaveLoader.loadWaves();
        System.out.println("load waves: " + waves.size());

        // Wave manager
        WaveManager waveManager = new WaveManager(game, waves);
        waveManager.startNextWave();

        // FXML \ Buttons
        stopButton.setOnAction(e -> {
            loop.pause();
            System.out.println("speed: " + loop.getSpeed());
            try {
                gameApp.switchScene("/settings-view.fxml", "settings");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        resumeButton.setOnAction(e -> {loop.resume();});
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

        // speedButtons to change GameLoop-Speed
        normalSpeedButton.setOnAction(e -> {loop.setSpeed(1);});
        doubleSpeedButton.setOnAction(e -> {loop.setSpeed(2);});
        quadroupleSpeedutton.setOnAction(e -> {loop.setSpeed(4);});

        nextWaveButton.setLayoutX(30);
        nextWaveButton.setLayoutY(300);
        nextWaveButton.setOnAction(e -> {});

        hBox.getChildren().addAll(stopButton, resumeButton, restartButton, normalSpeedButton, doubleSpeedButton, quadroupleSpeedutton);
        root.getChildren().add(hBox);
        root.getChildren().add(nextWaveButton);

        // Looop
        loop = new Gameloop(game, waveManager) {
            @Override
            public void handle(long now) {
                super.handle(now);
                sync();
            }
        };
    }

    public void sync(){
        // add enemies, to the Set
        for (Enemy e : game.getEnemies()) {
            if (!enemyHashSet.contains(e)){
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
            if (!game.getEnemies().contains(e)){
                root.getChildren().remove(e.getView());
                return true;
            }
            return false;
        });
    }

    public void deleteAllEnemies(){
        for (Enemy e: game.getEnemies()){
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

        shopTower.setOnMouseClicked(event -> {

            // neuen Tower erstellen
            Tower tower = new Tower(10,  game);
            game.addTower(tower);
            ImageView view = tower.getView();
            addTowerMenu(view);

            ChoiceBox<String> towerOptions = new ChoiceBox<>();
            ContextMenu towerMenu = new ContextMenu();
            CustomMenuItem choiceMenu = new CustomMenuItem(towerOptions);
            choiceMenu.setHideOnClick(false);
            towerMenu.getItems().add(choiceMenu);

            view.setLayoutX(event.getX() - 32);
            view.setLayoutY(event.getY() - 32);

            root.getChildren().add(view);

            root.setOnMouseMoved(e -> {
                view.setLayoutX(e.getX() - 32);
                view.setLayoutY(e.getY() - 32);
            });

            view.setLayoutX(event.getSceneX() - 32);
            view.setLayoutY(event.getSceneY() - 32);
        });
    }

    private void addTowerMenu(ImageView view) {

        ChoiceBox<String> targetChoice = new ChoiceBox<>();

        targetChoice.setItems(FXCollections.observableArrayList("erster", "letzter", "staerkster", "schnellster"));

        ContextMenu targetMenu = new ContextMenu();

        CustomMenuItem targetItem = new CustomMenuItem(targetChoice);
        targetItem.setHideOnClick(false);

        targetMenu.getItems().add(targetItem);

        view.setOnContextMenuRequested(e ->
                targetMenu.show(view, e.getScreenX(), e.getScreenY())
        );
    }

    public Parent getRoot() {
        return root;
    }

    public void stopLoop(){
        loop.stop();
    }

    public void startLoop() {loop.start();}

    public void resumeLoop(){loop.resume();}
}

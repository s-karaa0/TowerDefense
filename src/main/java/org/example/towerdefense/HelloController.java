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

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HelloController {
    private final AnchorPane root = new AnchorPane();

    private Game game;
    private Gameloop loop;
    private Image towerImage;
    ChoiceBox<Object> cb = new ChoiceBox<>();
    private int lastEnemyCount = 0;

    private final Button stopButton = new Button("Stop");
    private final Button resumeButton = new Button("Resume");
    private final Button restartButton = new Button("Restart");
    private final HBox hBox = new HBox();

    private HashSet<Enemy> enemyHashSet = new HashSet<>();
    private List<Point2D> buildSpots = List.of(
            new Point2D(200, 300),
            new Point2D(400, 250),
            new Point2D(600, 450),
            new Point2D(800, 300));

    private int i = 0;

    public HelloController() {
        cb.setItems(FXCollections.observableArrayList("erster", "letzter", "staerkster", "schnellster"));

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

        GamePath path = new GamePath(segments, 320);

        // Game
        game = new Game(path);
        createShop();

        for (Point2D p : buildSpots) {
            ImageView spot = new ImageView(towerImage);

            spot.setFitWidth(64);
            spot.setFitHeight(64);
            spot.setOpacity(0.1);

            spot.setLayoutX(400);
            spot.setLayoutX(200);

            root.getChildren().add(spot);
        }

        root.setOnMouseClicked(game::handleMouseClicked);

        // XML
        List<Wave> waves = WaveLoader.loadWaves();
        System.out.println("load waves: " + waves.size());

        // Wave manager
        WaveManager waveManager = new WaveManager(game, waves);
        waveManager.startNextWave();

        // FXML \ Graphics

        stopButton.setOnAction(e -> {loop.pause();});
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

        hBox.getChildren().addAll(stopButton, resumeButton, restartButton);
        root.getChildren().add(hBox);

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
                System.out.println("enemy " + i + " added");
                if (i == 5 || i == 10){
                    loop.pause();
                }
            }
        }

        // rm enemies, via thei Set
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

        root.getChildren().add(shopTower);

        shopTower.setOnMouseClicked(event -> {

            // neuen Tower erstellen
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


            view.setLayoutX(event.getSceneX() - 32);
            view.setLayoutY(event.getSceneY() - 32);

            root.getChildren().add(view);

            root.setOnMouseMoved(e -> {
                view.setLayoutX(e.getSceneX() - 32);
                view.setLayoutY(e.getSceneY() - 32);
            });

            view.setLayoutX(event.getSceneX() - 32);
            view.setLayoutY(event.getSceneY() - 32);

            // Drag
            view.setOnMouseDragged(e -> {
                view.setLayoutX(e.getSceneX() - 32);
                view.setLayoutY(e.getSceneY() - 32);
            });

            // Drop
            view.setOnMouseReleased(e -> {
                System.out.println("placed Tower: "
                        + view.getLayoutX() + " / "
                        + view.getLayoutY());
            });
        });
    }

    public Parent getRoot() {
        return root;
    }

    public void start() {
        loop.start();
    }
}

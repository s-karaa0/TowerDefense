package org.example.towerdefense;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class HelloController {
    private final AnchorPane root = new AnchorPane();

    private Game game;
    private Gameloop loop;

    private int lastEnemyCount = 0;

    private int i = 0;

    public HelloController() {

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

        GamePath path = new GamePath(segments, 80);

        // Game
        game = new Game(path);

        // XML
        List<Wave> waves = WaveLoader.loadWaves();
        System.out.println("load waves: " + waves.size());

        // Wave manager
        WaveManager waveManager = new WaveManager(game, waves);
        waveManager.startNextWave();

        // Looop
        loop = new Gameloop(game, waveManager) {
            @Override
            public void handle(long now) {
                super.handle(now);

                List<Enemy> removed = game.getRemovedEnemies();

                // add enemies
                if (game.getEnemies().size() > lastEnemyCount) {
                    for (int i = lastEnemyCount; i < game.getEnemies().size(); i++) {
                        root.getChildren().add(game.getEnemies().get(i).getView());
                    }
                    lastEnemyCount = game.getEnemies().size();
                }

                // remove enemies
                if(!removed.isEmpty()) {
                    for (Enemy e : removed){
                        root.getChildren().remove(e.getView());
                        e.destroy(root);
                        System.out.println(removed.size() + "|" + game.getRemovedEnemies().size() + "|" + game.getEnemies().size());
                        i++;
                        System.out.println("iterations: " + i);
                    }
                }

                game.resetRemovedEnemies();
                removed.clear();

            }
        };
    }

    public Parent getRoot() {
        return root;
    }

    public void start() {
        loop.start();
    }
}

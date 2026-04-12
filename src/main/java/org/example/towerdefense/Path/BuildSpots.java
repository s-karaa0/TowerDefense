package org.example.towerdefense.Path;

import javafx.geometry.Point2D;

import java.util.List;

public class BuildSpots {

    public static List<Point2D> createBuildSpots(String difficulty) {
        switch (difficulty) {

            case "easy":
                return List.of(
                        new Point2D(500, 150),
                        new Point2D(930, 200),
                        new Point2D(200, 400),
                        new Point2D(500, 550),
                        new Point2D(700, 300),
                        new Point2D(200, 650)
                );


            case "normal":
                return List.of(
                        new Point2D(150, 300),
                        new Point2D(400, 200),
                        new Point2D(600, 400),
                        new Point2D(300, 500),
                        new Point2D(600, 600),
                        new Point2D(800, 500),
                        new Point2D(850, 300)
                );


            case "hard":
                return List.of(
                        new Point2D(400, 190),
                        new Point2D(600, 190),
                        new Point2D(500, 330),
                        new Point2D(700, 330),
                        new Point2D(400, 470),
                        new Point2D(600, 470),
                        new Point2D(500, 610),
                        new Point2D(700, 610)
                );


            default:
                return List.of(); // fallback
        }
    }
}

package org.example.towerdefense.Path;

import javafx.geometry.Point2D;

import java.util.List;

public class PathFactory {
    public static GamePath createPath(String difficulty) {

        List<GamePath.BezierSegment> segments;

        switch (difficulty) {

            case "easy":
                segments = List.of(
                        new GamePath.BezierSegment(
                                new Point2D(0, 460),
                                new Point2D(0, 460),
                                new Point2D(250, 460),
                                new Point2D(250, 460)
                        ),
                        new GamePath.BezierSegment(
                                new Point2D(250, 460),
                                new Point2D(280, 460),
                                new Point2D(280, 490),
                                new Point2D(280, 520)
                        ),

                        // runter
                        new GamePath.BezierSegment(
                                new Point2D(280, 520),
                                new Point2D(280, 520),
                                new Point2D(280, 590),
                                new Point2D(280, 590)
                        ),

                        // Kurve 2 (runter -> rechts)
                        new GamePath.BezierSegment(
                                new Point2D(280, 590),
                                new Point2D(280, 660),
                                new Point2D(310, 660),
                                new Point2D(340, 660)
                        ),

                        // unten nach rechts
                        new GamePath.BezierSegment(
                                new Point2D(340, 660),
                                new Point2D(340, 660),
                                new Point2D(550, 660),
                                new Point2D(550, 660)
                        ),

                        // Kurve 3 (rechts -> hoch)
                        new GamePath.BezierSegment(
                                new Point2D(550, 660),
                                new Point2D(580, 660),
                                new Point2D(580, 630),
                                new Point2D(580, 600)
                        ),

                        // hoch
                        new GamePath.BezierSegment(
                                new Point2D(580, 600),
                                new Point2D(580, 600),
                                new Point2D(580, 110),
                                new Point2D(580, 110)
                        ),

                        // Kurve 4 (hoch -> rechts)
                        new GamePath.BezierSegment(
                                new Point2D(580, 110),
                                new Point2D(580, 80),
                                new Point2D(710, 80),
                                new Point2D(710, 80)
                        ),

                        new GamePath.BezierSegment(
                                new Point2D(710, 80),
                                new Point2D(710, 80),
                                new Point2D(790, 80),
                                new Point2D(820, 80)
                        ),

                        new GamePath.BezierSegment(
                                new Point2D(820, 80),
                                new Point2D(820, 120),
                                new Point2D(820, 300),
                                new Point2D(820, 360)
                        ),

                        // oben raus
                        new GamePath.BezierSegment(
                                new Point2D(820, 360),
                                new Point2D(820, 360),
                                new Point2D(900, 360),
                                new Point2D(1000, 360)
                        )
                );
                break;

            case "normal":
                segments = List.of(
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
                        )
                );
                break;

            case "hard":
                segments = List.of(

                        // START
                        new GamePath.BezierSegment(
                                new Point2D(0, 100),
                                new Point2D(0, 100),
                                new Point2D(680, 100),
                                new Point2D(680, 100)
                        ),

                        // Kurve 1 (rechts -> runter)
                        new GamePath.BezierSegment(
                                new Point2D(680, 100),
                                new Point2D(710, 100),
                                new Point2D(710, 130),
                                new Point2D(710, 160)
                        ),

                        // runter
                        new GamePath.BezierSegment(
                                new Point2D(710, 160),
                                new Point2D(710, 160),
                                new Point2D(710, 210),
                                new Point2D(710, 210)
                        ),

                        // Kurve 2 (runter -> links)
                        new GamePath.BezierSegment(
                                new Point2D(710, 210),
                                new Point2D(710, 240),
                                new Point2D(680, 240),
                                new Point2D(650, 240)
                        ),

                        // nach links
                        new GamePath.BezierSegment(
                                new Point2D(650, 240),
                                new Point2D(650, 240),
                                new Point2D(280, 240),
                                new Point2D(280, 240)
                        ),

                        // Kurve 3 (links -> runter)
                        new GamePath.BezierSegment(
                                new Point2D(280, 240),
                                new Point2D(250, 240),
                                new Point2D(250, 270),
                                new Point2D(250, 300)
                        ),

                        // runter
                        new GamePath.BezierSegment(
                                new Point2D(250, 300),
                                new Point2D(250, 300),
                                new Point2D(250, 350),
                                new Point2D(250, 350)
                        ),

                        // Kurve 4 (runter -> rechts)
                        new GamePath.BezierSegment(
                                new Point2D(250, 350),
                                new Point2D(250, 380),
                                new Point2D(280, 380),
                                new Point2D(310, 380)
                        ),

                        // nach rechts
                        new GamePath.BezierSegment(
                                new Point2D(310, 380),
                                new Point2D(310, 380),
                                new Point2D(680, 380),
                                new Point2D(680, 380)
                        ),

                        // Kurve 5 (rechts -> runter)
                        new GamePath.BezierSegment(
                                new Point2D(680, 380),
                                new Point2D(710, 380),
                                new Point2D(710, 410),
                                new Point2D(710, 440)
                        ),

                        // runter
                        new GamePath.BezierSegment(
                                new Point2D(710, 440),
                                new Point2D(710, 440),
                                new Point2D(710, 490),
                                new Point2D(710, 490)
                        ),

                        // Kurve 6 (runter -> links)
                        new GamePath.BezierSegment(
                                new Point2D(710, 490),
                                new Point2D(710, 520),
                                new Point2D(680, 520),
                                new Point2D(650, 520)
                        ),

                        // links
                        new GamePath.BezierSegment(
                                new Point2D(650, 520),
                                new Point2D(650, 520),
                                new Point2D(280, 520),
                                new Point2D(280, 520)
                        ),

                        // Kurve 7 (links -> runter)
                        new GamePath.BezierSegment(
                                new Point2D(280, 520),
                                new Point2D(250, 520),
                                new Point2D(250, 550),
                                new Point2D(250, 580)
                        ),

                        // runter
                        new GamePath.BezierSegment(
                                new Point2D(250, 580),
                                new Point2D(250, 580),
                                new Point2D(250, 630),
                                new Point2D(250, 630)
                        ),

                        // Kurve 8 (runter -> rechts)
                        new GamePath.BezierSegment(
                                new Point2D(250, 630),
                                new Point2D(250, 660),
                                new Point2D(280, 660),
                                new Point2D(310, 660)
                        ),

                        // rechts
                        new GamePath.BezierSegment(
                                new Point2D(310, 660),
                                new Point2D(310, 660),
                                new Point2D(680, 660),
                                new Point2D(680, 660)
                        ),

                        // letzte Kurve (rechts -> raus)
                        new GamePath.BezierSegment(
                                new Point2D(680, 660),
                                new Point2D(800, 660),
                                new Point2D(900, 660),
                                new Point2D(1000, 660)
                        )
                );
                break;

            default:
                throw new IllegalArgumentException("Unknown difficulty: " + difficulty);
        }

        return new GamePath(segments, 200);
    }
}
package org.example.towerdefense.Path;


import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GamePath {
    private final List<Point2D> samples = new ArrayList<>();
    private final List<Double> lengths = new ArrayList<>();
    private final double totalLength;

    public GamePath(List<BezierSegment> segments, int resolution) {
        Point2D prev = null;
        double lenAcc = 0;
        for (BezierSegment seg : segments) {
            for (int i = 0; i <= resolution; i++) {
                double t = (double) i / resolution;
                Point2D p = seg.evaluate(t);
                if (prev != null) {
                    lenAcc += prev.distance(p);
                }
                samples.add(p);
                lengths.add(lenAcc);
                prev = p;
            }
        }
        totalLength = lenAcc;
    }

    public double getTotalLeanght(){
        return totalLength;
    }

    public Point2D getPointAt(double distance) {
        distance = ((distance % totalLength) + totalLength) % totalLength;
        int idx = Collections.binarySearch(lengths, distance);
        if (idx < 0) idx = -idx - 1;
        if (idx >= samples.size()) idx = samples.size() - 1;
        return samples.get(idx);
    }

    public static class BezierSegment {
        private final Point2D p0,p1,p2,p3;
        public BezierSegment(Point2D p0, Point2D p1, Point2D p2, Point2D p3){
            this.p0=p0; this.p1=p1; this.p2=p2; this.p3=p3;
        }
        public Point2D evaluate(double t){
            double u = 1-t;
            double x = u*u*u*p0.getX()+3*u*u*t*p1.getX()+3*u*t*t*p2.getX()+t*t*t*p3.getX();
            double y = u*u*u*p0.getY()+3*u*u*t*p1.getY()+3*u*t*t*p2.getY()+t*t*t*p3.getY();
            return new Point2D(x,y);
        }
    }
}

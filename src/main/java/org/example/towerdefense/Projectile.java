package org.example.towerdefense;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Projectile {
    private double x, y;
    private double speed;
    private Enemy target;
    private boolean active = true;
    private final double damage ;

    private final Circle view = new Circle(10, Color.RED);

    public Projectile(double x, double y, double speed, Enemy target, double damage) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.target = target;
        this.damage = damage;
        updateView();
    }

    public void update(double dt) {
// if projectile is not active or target is dead, stop
        if (!active || target == null || !target.getLifeStatus()) {
            active = false;
            return;
        }

        // get target's current position
        double targetX = target.getView().getX() + target.getView().getFitWidth()/2;
        double targetY = target.getView().getY() + target.getView().getFitHeight()/2;


        // calculate direction to target
        double dx = targetX - x;
        double dy = targetY - y;
        double distanceSquared = dx * dx + dy * dy;

        if (distanceSquared < (speed * dt + 5) * (speed * dt + 5)) {
            target.damageTaken((float) damage);
            active = false;
            return;
        }

        double distance = Math.sqrt(distanceSquared); // nur einmal wenn nötig

        // move projectile towards target
        x += (dx / distance) * speed * dt;
        y += (dy / distance) * speed * dt;

        updateView();
    }

    private void updateView() {
        view.setTranslateX(x);
        view.setTranslateY(y);
    }

    public Circle getView() { return view; }
    public boolean isActive() { return active; }
    public Enemy getTarget() { return target; }
}

package org.example.towerdefense;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Projectile {
    private double x, y;
    private double speed;
    private Enemy target;
    private boolean active = true;
    private final double damage = 10.0;

    private final Circle view = new Circle(10, Color.RED);

    public Projectile(double x, double y, double speed, Enemy target) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.target = target;
        updateView();
    }

    public void update(double dt) {
// if projectile is not active or target is dead, stop
        if (!active || target == null || !target.getLifeStatus()) {
            active = false;
            return;
        }

        // get target's current position
        double targetX = target.getView().getCenterX();
        double targetY = target.getView().getCenterY();

        // calculate direction to target
        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // if close enough to hit, deal damage and deactivate
        if (distance < speed * dt + 5) {
            target.damageTaken((float) damage);
            active = false;
            return;
        }

        // move projectile towards target
        x += (dx / distance) * speed * dt;
        y += (dy / distance) * speed * dt;

        updateView();
    }

    private void updateView() {
        view.setCenterX(x);
        view.setCenterY(y);
    }

    public Circle getView() { return view; }
    public boolean isActive() { return active; }
    public Enemy getTarget() { return target; }
}

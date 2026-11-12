package org.example.towerdefense.Tower;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.towerdefense.Wave.Enemy;

public class Projectile {
    private double x, y;
    private double speed;
    private Enemy target;
    private boolean active = true;
    private final double damage ;

    private final ImageView view;

    public Projectile(double x, double y, double speed, Enemy target, double damage, Image image) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.target = target;
        this.damage = damage;
        view = new ImageView(image);
        view.setFitWidth(24);
        view.setFitHeight(24);
        view.setPreserveRatio(true);
        view.setSmooth(false);
        view.setMouseTransparent(true);
        updateView();
    }

    public void update(double dt) {
// if projectile is not active or target is dead, stop
        if (!active || target == null || !target.getLifeStatus()) {
            active = false;
            return;
        }

        // get target's current position
        double targetX = target.getView().getLayoutX() + target.getView().getFitWidth()/2;
        double targetY = target.getView().getLayoutY() + target.getView().getFitHeight()/2;


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


    //rotation of projectile image to target
    private void updateView() {
        view.setTranslateX(x - view.getBoundsInLocal().getWidth() / 2);
        view.setTranslateY(y - view.getBoundsInLocal().getHeight() / 2);
        if (target != null) {
            double targetX = target.getView().getLayoutX() + target.getView().getFitWidth() / 2;
            double targetY = target.getView().getLayoutY() + target.getView().getFitHeight() / 2;
            view.setRotate(Math.toDegrees(Math.atan2(targetY - y, targetX - x)));
        }
    }

    public ImageView getView() { return view; }
    public boolean isActive() { return active; }
    public Enemy getTarget() { return target; }
}

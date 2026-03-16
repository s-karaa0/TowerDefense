package org.example.towerdefense;

import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import org.example.towerdefense.Enemy;
import org.example.towerdefense.Game;

import java.util.Comparator;
import java.util.List;

public class TowerShooter {
    private final Game game;
    private final ImageView towerView;
    private final double damage;
    private final ChoiceBox<Object> cb;

    private Enemy target;
    private double attackTimer = 0;
    private final double attackCooldown;
    private final double range;
    private final double projectileSpeed;

    public TowerShooter(Game game, ImageView towerView, double damage, ChoiceBox<Object> cb, double attackCooldown, double range,  double projectileSpeed) {
        this.game = game;
        this.towerView = towerView;
        this.damage = damage;
        this.cb = cb;
        this.attackCooldown = attackCooldown;
        this.range = range;
        this.projectileSpeed = projectileSpeed;
    }

    public void update(double dt) {
        attackTimer += dt;
        if (attackTimer >= attackCooldown) {
            setGoal();
            if (target != null && isInRange(target)) {
                shoot();
                attackTimer = 0;
            }
        }
    }

    private void setGoal() {
        List<Enemy> enemies = game.getEnemies();
        if (enemies.isEmpty()) { target = null; return; }

        String mode = (String) cb.getSelectionModel().getSelectedItem();
        if (mode == null) mode = "erster";

        target = switch (mode) {
            case "erster"      -> enemies.stream().max(Comparator.comparingDouble(Enemy::getDistance)).orElse(null);
            case "letzter"     -> enemies.stream().min(Comparator.comparingDouble(Enemy::getDistance)).orElse(null);
            case "staerkster"  -> enemies.stream().max(Comparator.comparingDouble(Enemy::getHealth)).orElse(null);
            case "schnellster" -> enemies.stream().max(Comparator.comparingDouble(Enemy::getSpeed)).orElse(null);
            default -> null;
        };
    }

    private void shoot() {
        double cx = towerView.getLayoutX() + 32;
        double cy = towerView.getLayoutY() + 32;
        game.spawnProjectile(new Projectile(cx, cy, projectileSpeed, target, damage));
    }

    private boolean isInRange(Enemy e) {
        double dx = e.getView().getCenterX() - (towerView.getLayoutX() + 32);
        double dy = e.getView().getCenterY() - (towerView.getLayoutY() + 32);
        return Math.sqrt(dx*dx + dy*dy) <= range;
    }
}
package org.example.towerdefense.Tower;

import javafx.scene.image.ImageView;
import org.example.towerdefense.Game;
import org.example.towerdefense.Wave.Enemy;

import java.util.Comparator;
import java.util.List;

public class TowerShooter {
    private final Game game;
    private final ImageView towerView;
    private double damage;
    private Tower tower;

    private Enemy target;
    private double attackTimer = 0;
    private double attackCooldown;
    private double range;
    private double projectileSpeed;

    public TowerShooter(Game game, ImageView towerView, double damage, Tower tower, double attackCooldown, double range,  double projectileSpeed) {
        this.game = game;
        this.towerView = towerView;
        this.damage = damage;
        this.tower = tower;
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
            } else {
                target = null; // reset wenn nicht in range!
            }
        }
    }

    private void setGoal() {
        List<Enemy> enemies = game.getEnemies().stream()
                .filter(this::isInRange) // nur Gegner in Range!
                .toList();

        if (enemies.isEmpty()) { target = null; return; }


        String mode = tower.setAim();

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
        double dx = (e.getView().getLayoutX() + e.getView().getFitWidth()/2) - (towerView.getLayoutX() + 32);
        double dy = (e.getView().getLayoutY() + e.getView().getFitHeight()/2) - (towerView.getLayoutY() + 32);
        return Math.sqrt(dx*dx + dy*dy) <= range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public void setAttackCooldown(double cooldown){
        this.attackCooldown = cooldown;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

}
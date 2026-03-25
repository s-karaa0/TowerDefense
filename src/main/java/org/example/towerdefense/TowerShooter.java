package org.example.towerdefense;

import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import org.example.towerdefense.Enemy;
import org.example.towerdefense.Game;

import java.net.URL;
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

    private AudioClip shootSound;

    public TowerShooter(Game game, ImageView towerView, double damage, ChoiceBox<Object> cb, double attackCooldown, double range, double projectileSpeed) {
        this.game = game;
        this.towerView = towerView;
        this.damage = damage;
        this.cb = cb;
        this.attackCooldown = attackCooldown;
        this.range = range;
        this.projectileSpeed = projectileSpeed;

        loadSound();
    }


    //loads shooting soundeffect
    private void loadSound() {
        try {

            URL soundUrl = getClass().getResource("/Soundeffects/pew sound effect.mp3"); // hier für Ändern des shoot sounds pfad ändern
            if (soundUrl != null) {
                shootSound = new AudioClip(soundUrl.toExternalForm());
                shootSound.setVolume(1.0);
            } else {
                System.err.println("Sounddatei pew sound effect wurde nicht gefunden!");
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Laden des Sounds: " + e.getMessage());
        }
    }

    public void update(double dt) {
        attackTimer += dt;
        if (attackTimer >= attackCooldown) {
            setGoal();
            if (target != null && isInRange(target)) {
                shoot();
                attackTimer = 0;
            } else {
                target = null; // Reset wenn nicht in Range!
            }
        }
    }

    private void setGoal() {
        List<Enemy> enemies = game.getEnemies().stream()
                .filter(this::isInRange) // Nur Gegner in Range!
                .toList();

        if (enemies.isEmpty()) {
            target = null;
            return;
        }

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


        if (shootSound != null) {
            shootSound.play();
        }
    }

    private boolean isInRange(Enemy e) {
        double dx = e.getView().getCenterX() - (towerView.getLayoutX() + 32);
        double dy = e.getView().getCenterY() - (towerView.getLayoutY() + 32);
        return Math.sqrt(dx * dx + dy * dy) <= range;
    }
}
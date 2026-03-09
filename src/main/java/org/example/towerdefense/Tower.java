package org.example.towerdefense;

import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Tower {
    private int damage;
    ChoiceBox<Object> cb = new ChoiceBox<>();
    private double mouseX;
    private double mouseY;
    private ImageView imageView;
    private Game game;
    private Enemy target;
    private double attackTimer = 0;

    public Tower(int damage, Game game) {
        this.damage = damage;
        this.game = game;

        // Choice-Box wird nur einmal erstellt/befüllt
        cb.setItems(FXCollections.observableArrayList("erster", "letzter", "staerkster", "schnellster"));

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tower.png")));
        imageView = new ImageView(image);

        imageView.setFitWidth(64);
        imageView.setFitHeight(64);
    }

    public void setGoal() {
        List<Enemy> enemies = game.getEnemies();

        if (enemies.isEmpty()) {
            target = null;
            return;
        }

        String mode = (String) cb.getSelectionModel().getSelectedItem();
        if (mode == null) {
            return;
        }

        for (Enemy e : enemies) {
            if (mode.equals("erster") && e.getDistance() > target.getDistance()) {
                target = e;
            } else if (mode.equals("letzter") && e.getDistance() > target.getDistance()) {
                target = e;
            } else if (mode.equals("staerkster") && e.getDistance() > target.getDistance()) {
                target = e;
            } else if (mode.equals("schnellster") && e.getDistance() > target.getDistance()) {
                target = e;
            }
        }


    }

    public ImageView getView() {
        return imageView;
    }

    public void update(double dt) {
        attackTimer += dt;

        setGoal();

        if (target != null) {
            target.damageTaken(damage);
        }
    }

}
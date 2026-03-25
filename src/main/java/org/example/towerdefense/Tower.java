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
    protected ImageView imageView;
    protected Game game;
    protected TowerShooter shooter;

    public ChoiceBox<Object> getChoiceBox() { return cb; }

    public Tower(int damage, Game game) {
        this.damage = damage;
        this.game = game;
        cb.setItems(FXCollections.observableArrayList("erster", "letzter", "staerkster", "schnellster"));

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/thunder-cat.jpeg")));
        imageView = new ImageView(image);
        imageView.setFitWidth(64);
        imageView.setFitHeight(64);

        shooter = createShooter(damage); // NEU: ausgelagert
    }

    protected TowerShooter createShooter(int damage) {
        return new TowerShooter(game, imageView, damage, cb, 1.0, 150.0, 300);
    }

    public void update(double dt) {
        shooter.update(dt); // NEU der Rest fällt weg
    }

    public ImageView getView() { return imageView; }
    public void setGoal() {} // könnte weg wegen der TowerShooter klasse
}
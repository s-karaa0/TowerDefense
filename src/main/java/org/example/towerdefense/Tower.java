package org.example.towerdefense;

import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Tower {
    private int damage = 5;
    private double range = 150.0;
    private double cooldown = 1.0;
    private int buildCost;
    private int upgradeCost;
    private String aim = "erster";
    protected ImageView imageView;
    protected Game game;
    protected TowerShooter shooter;
    private Circle rangeCircle;

    public Tower(Game game, Image image) {
        this.game = game;

        imageView = new ImageView(image);
        imageView.setFitWidth(64);
        imageView.setFitHeight(64);
        imageView.setMouseTransparent(true);

        rangeCircle = new Circle(range);
        rangeCircle.setFill(Color.rgb(0, 0, 255, 0.3)); // 30% opacity
        rangeCircle.setStroke(Color.BLUE);
        rangeCircle.setStrokeWidth(2);
        rangeCircle.setVisible(false);
        rangeCircle.setMouseTransparent(true);

        shooter = createShooter(damage, range, cooldown);
    }

    protected TowerShooter createShooter(int damage, double range, double cooldown) {
        return new TowerShooter(game, imageView, damage, aim, cooldown, range, 500);
    }

    public void update(double dt) {
        shooter.update(dt); //

        rangeCircle.setCenterX(imageView.getLayoutX() + 32);
        rangeCircle.setCenterY(imageView.getLayoutY() + 32);
    }

    public ImageView getView() { return imageView; }

    public void getTarget(String aim) {
        this.aim = aim;
    }

    public void upgradeDamage() {
        damage = damage + 10;
    }

    public void upgradeSpeed() {
        cooldown = Math.max(0.05, cooldown * 0.75); // maxx 0.05 weils sunst eh op is
    }

    public void upgradeRange() {
        range = range + 25;

        shooter.setRange(range);
        rangeCircle.setRadius(range);
    }

    public int getBuildCost() {
        return buildCost;
    }


    public Circle getRangeCircle() {
        return rangeCircle;
    }
}
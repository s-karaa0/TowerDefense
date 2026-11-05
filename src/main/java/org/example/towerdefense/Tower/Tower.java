package org.example.towerdefense.Tower;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.example.towerdefense.Game;

public class Tower {
    private int damage = 5;
    private double range = 150.0;
    private double cooldown = 1.0;
    private int buildCost;
    private int upgradeCost = 25;
    private String aim = "erster";
    protected ImageView imageView;
    protected Game game;
    protected TowerShooter shooter;
    private Circle rangeCircle;
    private Color towerColor;

    public Tower(Game game, Image image, Color color) {
        this.game = game;
        this.towerColor = color;

        imageView = new ImageView(image);
        imageView.setFitWidth(64);
        imageView.setFitHeight(64);
        imageView.setMouseTransparent(true);

        rangeCircle = new Circle(range);
        rangeCircle.setFill(towerColor);
        rangeCircle.setMouseTransparent(true);

        shooter = createShooter(damage, range, cooldown, color);
    }

    protected TowerShooter createShooter(int damage, double range, double cooldown, Color color) {
        return new TowerShooter(game, imageView, damage, this, cooldown, range, 500, color);
    }

    public void update(double dt) {
        shooter.update(dt); //

        rangeCircle.setCenterX(imageView.getLayoutX() + 32);
        rangeCircle.setCenterY(imageView.getLayoutY() + 32);
    }

    public ImageView getView() { return imageView; }

    public String setAim() {
        return aim;
    }

    public void getAim(String aim) {
        this.aim = aim;
    }

    public void upgradeDamage() {
        damage = damage + 10;
        shooter.setDamage(damage);
    }

    public void upgradeSpeed() {
        cooldown = cooldown / 1.2;
        shooter.setAttackCooldown(cooldown);
    }

    public void upgradeRange() {
        range = range + 25;

        shooter.setRange(range);
        rangeCircle.setRadius(range);
    }

    public int getBuildCost() {
        return buildCost;
    }

    public int getUpgradeCost() {
        return upgradeCost;
    }

    public void addUpgradeCost(int upgradeCost) {
        upgradeCost = upgradeCost + 25;
        this.upgradeCost = upgradeCost;
    }

    public Circle getRangeCircle() {
        return rangeCircle;
    }

    public Color getDerivedColor() {
        return towerColor.deriveColor(0, 1.0, 1.0, 0.3);
    }
}
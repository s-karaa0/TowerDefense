package org.example.towerdefense;

import javafx.scene.image.Image;

public class FastTower extends Tower {

    public FastTower(int damage, Game game, Image image) {
        super(damage, game, image);
    }

    @Override
    protected TowerShooter createShooter(int damage) {
        return new TowerShooter(game, imageView, damage, cb, 0.3, 150.0, 700);
    }
}

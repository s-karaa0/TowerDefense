package org.example.towerdefense;

public class FastTower extends Tower {

    public FastTower(int damage, Game game) {
        super(damage, game);
    }

    @Override
    protected TowerShooter createShooter(int damage) {
        return new TowerShooter(game, imageView, damage, cb, 0.3, 150.0, 700);
    }
}

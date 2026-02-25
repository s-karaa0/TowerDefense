package org.example.towerdefense;

import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Game {
    private final List<Enemy> enemies = new ArrayList<>();
    private final GamePath path;

    private List<Enemy> removedEnemies = new ArrayList<>();

    public Game(GamePath path) {
        this.path = path;
    }

    public void addEnemy(Enemy e) {
        enemies.add(e);
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Enemy> getRemovedEnemies() {
        return removedEnemies;
    }

    public void resetRemovedEnemies() {
        removedEnemies.clear();
    }

    public void resetEnemies() {
        enemies.clear();
    }

    public void update(double dt) {
        // enemy spawn
        for (Enemy e : enemies) {
            e.update(dt, path);

            if (e.getHealth() <= 0 || e.getFinished()) {
                removedEnemies.add(e);
            }
        }

        enemies.removeIf(e -> e.getHealth() <= 0 || e.getFinished());
    }


}





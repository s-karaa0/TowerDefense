package org.example.towerdefense;

import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class Game {
    private final List<Enemy> enemies = new ArrayList<>();
    private final GamePath path;
    private final List<Tower> towers = new ArrayList<>();
    private final List<Projectile> projectiles = new ArrayList<>();

    public Game(GamePath path) {
        this.path = path;
    }

    public void addEnemy(Enemy e) {
        enemies.add(e);
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void resetEnemies() {
        enemies.clear();
    }

    public boolean isEmpty(){
        return enemies.isEmpty();
    }


    public void update(double dt) {
        // enemy spawn with iterator
        Iterator<Enemy> iterator = enemies.iterator();

        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.update(dt, path);

            if (enemy.getHealth() <= 0 || enemy.getFinished()) {
                iterator.remove();
            }
        }
        enemies.removeIf(e -> e.getHealth() <= 0 || e.getFinished());
        enemies.removeIf(e -> !e.getLifeStatus());

        for (Tower t : towers) {
            t.update(dt);
        }

        for(Projectile p: projectiles) {
            p.update(dt);
        }
    }

    public void handleMouseClicked(MouseEvent e) {
        if(e.getButton() == MouseButton.PRIMARY){
            enemies.getFirst().setLifeStatus(false);
        }
    };

    public void addTower(Tower t) {
        towers.add(t);
    }
}





package org.example.towerdefense;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.example.towerdefense.Path.GamePath;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game {
    private final List<Enemy> enemies = new ArrayList<>();
    private final GamePath path;
    private final List<Tower> towers = new ArrayList<>();
    private final List<Projectile> projectiles = new ArrayList<>();
    private int hp = 20; // hp = health points


    public int getHp() {return hp;}

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

            if (enemy.getHealth() <= 0 || !enemy.getLifeStatus()) {
                iterator.remove();
            } else if (enemy.getFinished()) {
                enemy.setLifeStatus(false);
                float damage = enemy.getAttackOutput();
                hp = hp - Math.round(damage);
            }
        }

        for (Tower t : towers) {
            t.update(dt);
        }

        for (Projectile p: projectiles) {
            p.update(dt);
        }

    }




    // delete later
    public void handleMouseClicked(MouseEvent e) {
        if(e.getButton() == MouseButton.PRIMARY){
        }
    };

    public void addTower(Tower t) {
        towers.add(t);
    }

    public void spawnProjectile(Projectile p) {
        projectiles.add(p);
        if (onProjectileSpawned != null) {
            onProjectileSpawned.accept(p);
        }
    }

    private Consumer<Projectile> onProjectileSpawned; // Funktion die ein Projectile bekommt und etwas damit macht (kein Rückgabewert)

    public void setOnProjectileSpawned(Consumer<Projectile> callback) {
        this.onProjectileSpawned = callback;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }
}





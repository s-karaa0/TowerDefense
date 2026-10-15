package org.example.towerdefense;

import org.example.towerdefense.Path.GamePath;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class Game {
    private final List<Enemy> enemies = new ArrayList<>();
    private List<Enemy> deadEnemies = new ArrayList<>();
    private final GamePath path;
    private final List<Tower> towers = new ArrayList<>();
    private final List<Projectile> projectiles = new ArrayList<>();
    private int hp = 20; // hp = health pOiNTS weil des so kheat
    private int money = 500;
    private int numberOfSpawnedEnemies=0;

    public int getHp() {return hp;}
    public int getMoney() {return money;}
    public void addMoney(int money) {
        this.money += money;
    }

    public Game(GamePath path) {
        this.path = path;
    }

    public void addEnemy(Enemy e) {
        enemies.add(e);
        numberOfSpawnedEnemies=numberOfSpawnedEnemies+1;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
    public List<Enemy> getDeadEnemies() {return deadEnemies;}

    public void resetGame() {
        enemies.clear();
        money = 200;

    }

    public boolean isEmpty(){
        return enemies.isEmpty();
    }


    public void update(double dt) {
        // enemy loop
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.update(dt, path);

            if (enemy.getHealth() <= 0)   {
                iterator.remove();
                enemy.getView().setVisible(false);
                money = money + enemy.getMoneyDropped();
                deadEnemies.add(enemy);
            } else if (enemy.getFinished()) {
                enemy.setLifeStatus(false);
                enemy.getView().setVisible(false);
                float damage = enemy.getAttackOutput();
                hp = hp - Math.round(damage);

            }
        }

        for (Tower t : towers) {
            t.update(dt);
        }

        // projectile loop mit cleanup
        Iterator<Projectile> projIterator = projectiles.iterator();
        while (projIterator.hasNext()) {
            Projectile p = projIterator.next();
            p.update(dt);
            if (!p.isActive()) {
                if (onProjectileRemoved != null) onProjectileRemoved.accept(p);
                projIterator.remove();
            }
        }
    }

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
    private Consumer<Projectile> onProjectileRemoved;
    public void setOnProjectileSpawned(Consumer<Projectile> callback) {
        this.onProjectileSpawned = callback;
    }
    public void setOnProjectileRemoved(Consumer<Projectile> callback) {
        this.onProjectileRemoved = callback;
    }
    public List<Projectile> getProjectiles() {
        return projectiles;
    }


    public int getNumberOfSpawnedEnemies() {
        return numberOfSpawnedEnemies;
    }
    public void resetNumberOfSpawnedEnemies(){
        numberOfSpawnedEnemies=0;
    }
}





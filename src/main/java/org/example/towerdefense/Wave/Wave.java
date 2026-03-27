package org.example.towerdefense.Wave;

import org.example.towerdefense.Enemy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Wave {

    private final int group;
    private final int waveID;
    private final int enemyCount;
    private final double spawnInterval;
    private final float health;
    private final float speed;
    private final int money;
    private final float attack;
    private boolean isFinished = false;
    private int spawned = 0;
    private double spawnTimer = 0;
    private int activeEnemies;

    public Wave(int group, int waveID, int enemyCount, double spawnInterval,
                float health, float speed,
                int money, float attack) {

        this.group = group;
        this.waveID = waveID;
        this.enemyCount = enemyCount;
        this.spawnInterval = spawnInterval;
        this.health = health;
        this.speed = speed;
        this.money = money;
        this.attack = attack;
        this.activeEnemies = enemyCount;
    }


    public int getGroup() { return group; }
    public int getWaveID() {return waveID;}
    public int getEnemyCount() { return enemyCount; }
    public double getSpawnInterval() { return spawnInterval; }
    public float getHealth() { return health; }
    public float getSpeed() { return speed; }
    public int getMoney() { return money; }
    public float getAttack() { return attack; }
    public void setSpawned(int spawned) {this.spawned = spawned;}
    public void setSpawnTimer(double spawnTimer) {this.spawnTimer = spawnTimer;}
    public double getSpawnTimer() {return spawnTimer;}
    public int getSpawned() {return spawned;}

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
    public int getActiveEnemies() {return activeEnemies;}
    public void setActiveEnemies(int activeEnemies) {this.activeEnemies = activeEnemies;}
}

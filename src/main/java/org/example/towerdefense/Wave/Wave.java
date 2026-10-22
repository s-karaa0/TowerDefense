package org.example.towerdefense.Wave;

import javafx.scene.image.Image;

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
    private final String type;
    private Image image;

    public Wave(int group, int waveID, int enemyCount, double spawnInterval,
                float health, float speed,
                int money, float attack, String type) {

        this.group = group;
        this.waveID = waveID;
        this.enemyCount = enemyCount;
        this.spawnInterval = spawnInterval;
        this.health = health;
        this.speed = speed;
        this.money = money;
        this.attack = attack;
        this.activeEnemies = enemyCount;
        this.type = type;
    }

    // Tells which groups belong together - needed for pointing out enemy groups within a wave
    public int getWaveGroup() { return group; }
    public int getWaveID() {return waveID;}

    // for the amount of enemys that are going to get spawned
    public int getEnemyCount() { return enemyCount; }
    public double getSpawnInterval() { return spawnInterval; }
    public float getHealth() { return health; }
    public float getSpeed() { return speed; }

    public int getMoney() { return money; }
    // how much damage you get when he goes past the finish
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

    public String getType() {
        return type;
    }
    public Image getImage(){
        return image;
    }
    public void setImage(Image img){
        this.image = img;
    }
}

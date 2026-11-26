package org.example.towerdefense.Wave;

import javafx.scene.image.Image;

import java.util.Objects;

public class Wave {

    //they are individual stats set in the waves.xml file
    private final int group;
    private final int waveID;
    private final int enemyCount;
    private final double spawnInterval;

    // these are the stats based on the enemy type
    private final float health;
    private final float speed;
    private final int money;
    private final float attack;

    // is the wave completely gone?
    private boolean isFinished = false;

    // is connected to the spawn interval => when they are the same -> spawnTimer resets to 0 and enemy spawns
    private double spawnTimer = 0;
    private int spawned = 0;
    private int activeEnemies;



    private final Image image;

    private static Wave wave;

    public Wave(int group, int waveID, int enemyCount, double spawnInterval, String type, String bosstype) {

        this.group = group;
        this.waveID = waveID;
        this.enemyCount = enemyCount;
        this.spawnInterval = spawnInterval;
        this.activeEnemies = enemyCount; // these are the enemies on the screen


        switch (type) {
            case "NormalEnemy": //Stats for a normal enemy
                this.health = 100;
                this.speed = 65;
                this.money = 10;
                this.attack = 2;
                this.image = new Image(Objects.requireNonNull(WaveManager.class
                                .getResource("/images/Enemies/red_enemy.png"))
                        .toExternalForm());
                break;
            case "FastEnemy":   //stats for a fast enemy
                this.health = 100;
                this.speed = 90;
                this.attack = 3;
                this.money = 15;
                this.image = new Image(Objects.requireNonNull(WaveManager.class
                                .getResource("/images/Enemies/ice_enemy.png"))
                        .toExternalForm());
                break;
            case "StrongEnemy": //stats for a strong enemy
                this.health = 200;
                this.speed = 65;
                this.attack = 10;
                this.money = 15;
                this.image = new Image(Objects.requireNonNull(WaveManager.class
                                .getResource("/images/Enemies/enemy-lev4_small.png"))
                        .toExternalForm());
                break;
            case "BossEnemy":   //stats for a boss enemy
                switch (bosstype) { // there are multiple types of bosses
                    case "kiwi":
                        this.health = 850;
                        this.speed = 25;
                        this.attack = 50;
                        this.money = 250;
                        this.image = new Image(Objects.requireNonNull(WaveManager.class
                                        .getResource("/images/bosses/kiwi-boss.png"))
                                .toExternalForm());
                        break;

                    case "snow":
                        this.health = 1000;
                        this.speed = 30;
                        this.attack = 50;
                        this.money = 400;
                        this.image = new Image(Objects.requireNonNull(WaveManager.class
                                        .getResource("/images/bosses/snow-boss.png"))
                                .toExternalForm());
                        break;

                    case "slime":
                        this.health = 1000;
                        this.speed = 50;
                        this.attack = 50;
                        this.money = 400;
                        this.image = new Image(Objects.requireNonNull(WaveManager.class
                                        .getResource("/images/bosses/slime-boss.png"))
                                .toExternalForm());
                        break;

                    default:
                        this.health = 600;
                        this.speed = 50;
                        this.attack = 30;
                        this.money = 50;
                        this.image = new Image(Objects.requireNonNull(WaveManager.class
                                        .getResource("/images/Enemies/enemy-lev4_small.png"))
                                .toExternalForm());
                }
                break;

            default:    //when there is no valid enemy type -> exception
                throw new IllegalArgumentException("Invalid enemy type: " + type);
        }
    }

    public int getGroup() {
        return group;
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

    public Image getImage() {
        return image;
    }

}

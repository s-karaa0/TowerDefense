package org.example.towerdefense.Wave;

public class Wave {

    private final int enemyCount;
    private final double spawnInterval;
    private final float health;
    private final float speed;
    private final int money;
    private final float attack;
    private int enemyId = 0;

    private enum WaveState {
        SPAWNING,
        WAITING,
        COOLDOWN
    }

    public Wave(int enemyCount, double spawnInterval,
                float health, float speed,
                int money, float attack) {

        this.enemyCount = enemyCount;
        this.spawnInterval = spawnInterval;
        this.health = health;
        this.speed = speed;
        this.money = money;
        this.attack = attack;
    }

    public int getEnemyCount() { return enemyCount; }
    public double getSpawnInterval() { return spawnInterval; }
    public float getHealth() { return health; }
    public float getSpeed() { return speed; }
    public int getMoney() { return money; }
    public float getAttack() { return attack; }
    public int getEnemyId() {
        return enemyId;
    }
}

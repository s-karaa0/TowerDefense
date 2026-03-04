package org.example.towerdefense;

import java.util.List;

public class WaveManager {
    private final Game game;
    private final List<Wave> waves;

    private int currentWaveIndex = -1;
    private Wave currentWave;
    private boolean waveActive = false;

    private int spawned = 0;
    private double spawnTimer = 0;
    private int enemyId = 0;

    public WaveManager(Game game, List<Wave> waves) {
        this.game = game;
        this.waves = waves;
    }

    public void startNextWave() {
        currentWaveIndex++;

        if (currentWaveIndex >= waves.size()) {
            System.out.println("All waves finished");
            return;
        }

        currentWave = waves.get(currentWaveIndex);
        spawned = 0;
        spawnTimer = 0;
        waveActive = true;

        System.out.println("Starting wave " + (currentWaveIndex + 1));
    }

    public void update(double dt) {
        if (currentWave == null) return;
        if(waveActive) {
            spawnTimer += dt;

            while ((spawnTimer >= currentWave.getSpawnInterval()
                    && spawned < currentWave.getEnemyCount()) && waveActive) {

                spawnTimer -= currentWave.getSpawnInterval();
                spawnEnemy();
                spawned++;
            }

            if (spawned >= currentWave.getEnemyCount()
                    && game.getEnemies().isEmpty()) {
                waveActive = false;
                System.out.println("end of wave");
            }

        }
    }

    private void spawnEnemy() {
        Enemy enemy = new Enemy(
                currentWave.getHealth(),
                currentWave.getSpeed(),
                currentWave.getMoney(),
                currentWave.getAttack(),
                getEnemyId()
        );
        game.addEnemy(enemy);
        System.out.println("enemy added");
    }

    public int getEnemyId() {
        ++enemyId;
        return enemyId;
    }
}

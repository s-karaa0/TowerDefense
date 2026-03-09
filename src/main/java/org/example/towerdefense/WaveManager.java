package org.example.towerdefense;

import java.util.List;

public class WaveManager {
    private final Game game;
    private final List<Wave> waves;

    private int WaveIndex = -1;
    private Wave currentWave;
    private boolean endOfAllWaves = false;

    private int spawned = 0;
    private double spawnTimer = 0;
    private int enemyId = 0;

    public WaveManager(Game game, List<Wave> waves) {
        this.game = game;
        this.waves = waves;
    }

    public void startNextWave() {
        WaveIndex++;

        // if the Index of the next wave >= the list of all Waves
        if (WaveIndex >= waves.size()) {
            System.out.println("All waves finished");
            endOfAllWaves = true;
            return;
        }

        currentWave = waves.get(WaveIndex);
        spawned = 0;
        spawnTimer = 0;

        System.out.println("Starting wave " + (WaveIndex + 1));
    }

    public void update(double dt) {
        if(currentWave != null  && !endOfAllWaves) {
            spawnTimer += dt;

            while ((spawnTimer >= currentWave.getSpawnInterval()
                    && spawned < currentWave.getEnemyCount())) {
                spawnTimer -= currentWave.getSpawnInterval();
                spawnEnemy();
                spawned++;
            }

            if (spawned >= currentWave.getEnemyCount() && game.isEmpty())  {
                System.out.println("end of wave");
                System.out.println("-------");
                System.out.println(" ");

                // here the next wave is called. If all waves are over -> endOfAllWaves negates this ones "parent" if
                startNextWave();
            }
        }
    }

    public void resetAllWaves() {
        WaveIndex = -1;
        endOfAllWaves = false;
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
    }

    public int getEnemyId() {
        ++enemyId;
        return enemyId;
    }
}

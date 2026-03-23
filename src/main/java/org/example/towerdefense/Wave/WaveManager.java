package org.example.towerdefense.Wave;

import org.example.towerdefense.Enemy;
import org.example.towerdefense.Game;

import java.util.ArrayList;
import java.util.List;

public class WaveManager {
    private final Game game;
    private List<Wave> waves = new ArrayList<>();
    private List<Wave> activeWaves = new ArrayList<>();

    private int waveIndex = -1;
    private Wave currentWave;
    private boolean endOfAllWaves = false;
    private boolean nextWave = true;
    private int group;

    private int enemyId = 0;

    public WaveManager(Game game, List<Wave> waves) {
        this.game = game;
        this.waves = waves;
    }

    public void triggerNextWave(){
        nextWave = true;
        startNextWave();
        group++;
    }

    public void startNextWave() {
        waveIndex++;

        // if the Index of the next wave >= the list of all Waves
        if (waveIndex >= waves.size()) {
            System.out.println("All waves finished");
            endOfAllWaves = true;
            return;
        }

        activeWaves.add(waves.get(waveIndex));
        waves.get(waveIndex).setSpawned(0);
        waves.get(waveIndex).setSpawnTimer(0);

        System.out.println("Starting wave " + (waveIndex + 1));
    }

    public void update(double dt) {
        if(!endOfAllWaves) {
            for(int i=0; i<activeWaves.size(); i++) {
                Wave wave = activeWaves.get(i);

                wave.setSpawnTimer(wave.getSpawnTimer() + dt);
                // -> quasi: spawnTimer += dt;

                while ((wave.getSpawnTimer() >= wave.getSpawnInterval()
                        && wave.getSpawned() < wave.getEnemyCount())) {

                    wave.setSpawnTimer(wave.getSpawnTimer() - wave.getSpawnInterval());
                    
                    Enemy enemy = new Enemy(
                            wave.getHealth(),
                            wave.getSpeed(),
                            wave.getMoney(),
                            wave.getAttack(),
                            getEnemyId()
                    );
                    game.addEnemy(enemy);
                    wave.setSpawned(wave.getSpawned() + 1);
                }

                if (wave.getSpawned() >= wave.getEnemyCount() && wave.isFinished())  {
                    wave.setFinished(true);
                    activeWaves.remove(wave);

                    System.out.println("end of " + waveIndex + "th wave");
                    System.out.println("-------");
                    System.out.println(" ");
                    System.out.println(" ");

                    // here the next wave is called. If all waves are over
                    nextWave = false;
                    startNextWave();
                }
            }
        }
    }

    public void resetAllWaves() {
        waveIndex = -1;
        endOfAllWaves = false;
        activeWaves.clear();
        System.out.println(" ---------- reset ----------");
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

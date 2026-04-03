package org.example.towerdefense.Wave;

import org.example.towerdefense.Enemy;
import org.example.towerdefense.Game;
import org.example.towerdefense.e.FastEnemy;
import org.example.towerdefense.e.StrongEnemy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WaveManager {
    private final Game game;
    private List<Wave> waves = new ArrayList<>();
    private List<Wave> activeWaves = new ArrayList<>();

    private int waveIndex = 0;
    private int group = 0;
    private boolean endOfAllWaves = false;

    private int enemyId = 0;

    public WaveManager(Game game, List<Wave> waves) {
        this.game = game;
        this.waves = waves;
    }

    public void triggerNextWave(){
        startNextWave();
    }

    public void startNextWave() {
        group++;
        // if the Index of the current wave >= the list of all Waves
        if (waveIndex >= waves.size()) {
            System.out.println("All waves finished");
            endOfAllWaves = true;
            return;
        }

        // Iterate all waves, then add to active waves if same group
        for (Wave wave : waves) {
            if (wave.getGroup() == group) {
                activeWaves.add(wave);
                wave.setSpawned(0);
                wave.setSpawnTimer(0);
                // if its the last wave, then the if from above cancels any new one
                waveIndex = wave.getWaveID();

                wave.setFinished(false);
            }
        }

        System.out.println("----");
        System.out.println("Group: " + group);
    }

    public void update(double dt) {
        if(!endOfAllWaves) {
            for(int i=0; i<activeWaves.size(); i++) {
                Wave wave = activeWaves.get(i);

                wave.setSpawnTimer(wave.getSpawnTimer() + dt);
                // -> quasi: spawnTimer += dt;
                // spawning enemies in game
                if ((wave.getSpawnTimer() >= wave.getSpawnInterval()
                        && wave.getSpawned() < wave.getEnemyCount())) {
                    wave.setSpawnTimer(wave.getSpawnTimer() - wave.getSpawnInterval());
                    Enemy enemy;
                    switch (wave.getType()) {
                        case "FastEnemy" -> enemy = new FastEnemy(getEnemyId(), group, wave.getWaveID());
                        case "TankEnemy" -> enemy = new StrongEnemy(getEnemyId(), group, wave.getWaveID());
                        default -> enemy = new Enemy(
                                wave.getHealth(),
                                wave.getSpeed(),
                                wave.getMoney(),
                                wave.getAttack(),
                                getEnemyId(),
                                group,
                                wave.getWaveID(),
                                "/images/enemies/enemy-lev1.png"
                        );
                    }

                    game.addEnemy(enemy);

                    wave.setSpawned(wave.getSpawned() + 1);
                }

                // if the wave now has no enemies left, sets end of wave
                if (wave.getActiveEnemies() == 0) {
                    wave.setFinished(true);
                }

                if (activeWaves.isEmpty()) {
                    startNextWave();
                }
            }

            Iterator<Wave> iterator = activeWaves.iterator();
            while(iterator.hasNext()){
                Wave w = iterator.next();
                if (w.getSpawned() >= w.getEnemyCount() && w.isFinished()){
                    iterator.remove();
                    System.out.println("end of " + w.getWaveID() + "th wave");
                }
            }
        }
    }

    public void resetAllWaves() {
        waveIndex = 0;
        endOfAllWaves = false;
        activeWaves.clear();
        group = 0;
        enemyId = 0;
        for (Wave w : waves) {
            w.setSpawned(0);
            w.setSpawnTimer(0);
            w.setFinished(false);
            w.setActiveEnemies(w.getEnemyCount());
        }
        System.out.println(" ---------- reset ---------- ");
    }

    public int getEnemyId() {
        ++enemyId;
        return enemyId;
    }

    public void onEnemyKilled(Enemy e) {
        for (Wave w : activeWaves) {
            if (w.getWaveID() == e.getWaveID()) {
                w.setActiveEnemies(w.getActiveEnemies() - 1);
            }
        }
    }


}

package org.example.towerdefense.Wave;

import javafx.scene.image.Image;
import org.example.towerdefense.Game;

import java.util.ArrayList;
import java.util.List;

public class WaveManager {
    private final Game game;
    private List<Wave> waves = new ArrayList<>();
    private List<Wave> activeWaves = new ArrayList<>();

    private int waveIndex = 0;
    private int group = 0;
    private boolean endOfAllWaves = false;

    private static final Image NormalEnemy =
            new Image(WaveManager.class
                    .getResource("/images/Enemies/enemy-lev1_small.png")
                    .toExternalForm());

    private static final Image FastEnemy =
            new Image(WaveManager.class
                    .getResource("/images/Enemies/enemy-lev2_small.png")
                    .toExternalForm());

    private static final Image StrongEnemy =
            new Image(WaveManager.class
                    .getResource("/images/Enemies/enemy-lev3_small.png")
                    .toExternalForm());

    private static final Image BossEnemy =
            new Image(WaveManager.class
                    .getResource("/images/Enemies/enemy-lev4_small.png")
                    .toExternalForm());

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
            if (wave.getWaveGroup() == group) {
                activeWaves.add(wave);
                wave.setSpawned(0);
                wave.setSpawnTimer(0);
                // if its the last wave, then the if from above cancels any new one
                waveIndex = wave.getWaveID();

                switch (wave.getType()) {
                    case "NormalEnemy": {
                        wave.setImage(NormalEnemy);
                        break;
                    }

                    case "FastEnemy": {
                        wave.setImage(FastEnemy);
                        break;
                    }

                    case "StrongEnemy": {
                        wave.setImage(StrongEnemy);
                        break;
                    }

                    case "BossEnemy": {
                        wave.setImage(BossEnemy);
                        break;
                    }

                }

                wave.setFinished(false);
            }
        }
    }

    public void update(double dt) {
            if(!endOfAllWaves) {
                for(int i=0; i<activeWaves.size(); i++) {
                    Wave wave = activeWaves.get(i);
                    List<Enemy> enemyList = game.getEnemies();

                    wave.setSpawnTimer(wave.getSpawnTimer() + dt);
                    // -> quasi: spawnTimer += dt;
                    // spawning enemies in game
                    if ((wave.getSpawnTimer() >= wave.getSpawnInterval()
                            && wave.getSpawned() < wave.getEnemyCount())) {
                        wave.setSpawnTimer(wave.getSpawnTimer() - wave.getSpawnInterval());
                        Enemy enemy = new Enemy(
                                wave.getHealth(),
                                wave.getSpeed(),
                                wave.getMoney(),
                                wave.getAttack(),
                                getEnemyId(),
                                group,
                                wave.getWaveID()
                        );

                        enemy.setImageView(wave.getImage());
                        enemy.getView().setLayoutX(-100);
                        enemy.getView().setLayoutY(-100);

                        game.addEnemy(enemy);
                        enemy.getView().setLayoutX(0);
                        enemy.getView().setLayoutY(0);
                        wave.setSpawned(wave.getSpawned() + 1);
                    }

                    // iterates all existing enemies, and if those enemies are dead, gives the info to the wave
                    for (Enemy e : enemyList) {
                        if (e.getWaveID() == wave.getWaveID() && !e.getLifeStatus()) {
                            wave.setActiveEnemies(wave.getActiveEnemies() - 1);
                        }
                    }
                    // if the wave now has no enemies left, sets end of wave
                    if (wave.getActiveEnemies() == 0) {
                        wave.setFinished(true);
                    }

                    if (wave.getSpawned() >= wave.getEnemyCount() && wave.isFinished())  {
                        activeWaves.remove(wave);

                        System.out.println("end of " + wave.getWaveID() + "th wave");
                        System.out.println("---*---");
                        System.out.println(" ");
                        System.out.println(" ");
                    }

                    if (activeWaves.isEmpty()) {
                        startNextWave();
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


    public boolean getEndOfAllWaves() {
        return endOfAllWaves;
    }


}

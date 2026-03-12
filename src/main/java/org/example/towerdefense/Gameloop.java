package org.example.towerdefense;

import javafx.animation.AnimationTimer;
import org.example.towerdefense.Wave.WaveManager;

public class Gameloop extends AnimationTimer {
    private final Game game;
    private final WaveManager waveManager;
    private long lastTime = 0;
    private boolean running = true;

    public Gameloop(Game game, WaveManager waveManager) {
        this.game = game;
        this.waveManager = waveManager;
    }

    public void pause(){
        running = false;
    }

    public void resume(){
        lastTime = 0;
        running = true;
    }

    @Override
    public void handle(long now) {
        if (running) {

        if (lastTime == 0) {
            lastTime = now;
            return;
        }

        double dt = (now - lastTime) / 1_000_000_000.0;
        lastTime = now;

        game.update(dt);
        waveManager.update(dt);
        }
    }
}




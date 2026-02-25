package org.example.towerdefense;

import javafx.animation.AnimationTimer;

public class Gameloop extends AnimationTimer {
    private final Game game;
    private final WaveManager waveManager;
    private long lastTime = 0;

    public Gameloop(Game game, WaveManager waveManager) {
        this.game = game;
        this.waveManager = waveManager;
    }

    @Override
    public void handle(long now) {
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




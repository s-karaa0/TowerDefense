package org.example.towerdefense;

import javafx.animation.AnimationTimer;
import org.example.towerdefense.Wave.WaveManager;

public class Gameloop extends AnimationTimer {
    private final Game game;
    private final WaveManager waveManager;
    private long lastTime = 0;
    private boolean running = true;
    private static final double step = 1.0 / 120.0; // simulation ticks
    private double speed = 1.0; // 1x speed

    // fps display (weil es kuman sekunden pro frame)
    private int frames = 0;
    private long lastFpsTime = 0;
    private int fps = 0;

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

    public void stop(){running = false;}

    public void setSpeed(double speed){
        this.speed = speed;
    }

    public double getSpeed(){
        return this.speed;
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

        dt *= speed;

        game.update(dt);
        waveManager.update(dt);
        }

        // Framerate display in console
        /**
        frames++;
        if (now - lastFpsTime >= 1_000_000_000.0) {
            fps = frames;
            frames = 0;
            lastFpsTime = now;
            //System.out.print("----\n" + "Memory: " + Runtime.getRuntime().totalMemory()/1_000_000 + "MB" + "\nFPS: " + fps + "\n" + "----\n");
        }
         **/

    }
}




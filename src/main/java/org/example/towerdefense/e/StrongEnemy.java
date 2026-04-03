package org.example.towerdefense.e;

import org.example.towerdefense.Enemy;

public class StrongEnemy extends Enemy {
    public StrongEnemy(int id, int groupID, int waveID) {
        super(300, 30, 15, 50, id, groupID, waveID, "/images/enemies/enemy-lev3.png");
    }
}

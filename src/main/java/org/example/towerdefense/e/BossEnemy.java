package org.example.towerdefense.e;

import org.example.towerdefense.Enemy;

public class BossEnemy extends Enemy {
    public BossEnemy(int id, int groupID, int waveID) {
        super(600, 20, 30, 90, id, groupID, waveID, "/images/enemies/enemy-lev4.png");
    }
}

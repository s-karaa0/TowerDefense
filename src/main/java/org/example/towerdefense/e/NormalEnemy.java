package org.example.towerdefense.e;

import org.example.towerdefense.Enemy;

public class NormalEnemy extends Enemy {
    public NormalEnemy(int id, int groupID, int waveID) {
        super(50, 70, 5, 10, id, groupID, waveID, "/images/enemies/enemy-lev1.png"); // health, speed, money, attack
    }
}

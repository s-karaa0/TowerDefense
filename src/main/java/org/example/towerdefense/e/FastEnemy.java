package org.example.towerdefense.e;

import org.example.towerdefense.Enemy;

public class FastEnemy extends Enemy  {
    public FastEnemy(int id, int groupID, int waveID) {
        super(50, 150, 5, 10, id, groupID, waveID, "/images/enemies/enemy-lev2.png"); // health, speed, money, attack
    }
}

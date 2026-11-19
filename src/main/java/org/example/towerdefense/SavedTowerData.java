package org.example.towerdefense;

import java.io.Serializable;

//Helper class for saving the coordinates of the towers and their types & stats (damage, range, speed)
public class SavedTowerData implements Serializable {
    //UID for serialization
    private static final long serialVersionUID = 1L;

    private final double x;
    private final double y;
    private final int towerType;
    private final int dmg;
    private final double range;
    private final double speed;

    public SavedTowerData(double x, double y, int towerType, int dmg, double range, double speed) {
        this.x = x;
        this.y = y;
        this.towerType = towerType;
        this.dmg = dmg;
        this.range = range;
        this.speed = speed;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getTowerType() {
        return towerType;
    }

    public int getDmg() {
        return dmg;
    }

    public double getRange() {
        return range;
    }

    public double getSpeed() {
        return speed;
    }
}

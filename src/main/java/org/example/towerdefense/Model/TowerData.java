package org.example.towerdefense.Model;

// helper for TowerConfig
public class TowerData {
    private int id;
    private int damage;
    private int range;
    private double cooldown;
    private int buildCost;
    private int upgradeCost;
    private String aim;

    public int getId() { return id; }
    public int getDamage() { return damage; }
    public int getRange() { return range; }
    public double getCooldown() { return cooldown; }
    public int getBuildCost() { return buildCost; }
    public int getUpgradeCost() { return upgradeCost; }
    public String getAim() { return aim; }
}

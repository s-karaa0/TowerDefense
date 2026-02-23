package org.example.towerdefense;

public class Enemy {
    private float health;
    private float speed;
    private int moneyDropped;
    private float attackOutput;
    private boolean lifeStatus=true;
    public float getHealth() {
        return health;
    }
    public float getSpeed() {
        return speed;
    }
    public int getMoneyDropped() {
        return moneyDropped;
    }
    public void setHealth(float health) {
        this.health = health;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    public void setMoneyDropped(int moneyDropped) {
        this.moneyDropped = moneyDropped;
    }
    public void damageTaken(float damage){
        setHealth(getHealth() - damage);
        if(getHealth()<=0){
            setLifeStatus(false);
        }
    }
    public Enemy(float health, float speed, int moneyDropped, float attackOutput) {
        setHealth(health);
        setSpeed(speed);
        setMoneyDropped(moneyDropped);
        setAttackOutput(attackOutput);
    }

    public float getAttackOutput() {
        return attackOutput;
    }

    public void setAttackOutput(float attackOutput) {
        this.attackOutput = attackOutput;
    }

    public void setLifeStatus(boolean lifeStatus) {
        this.lifeStatus = lifeStatus;
    }
}

package org.example.towerdefense;

import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

public class Enemy {
    private float health;
    private float speed;
    private int moneyDropped;
    private float attackOutput;
    private boolean lifeStatus=true;
    private double distance = 0;
    private boolean finished = false;
    // for debugging ->
    private final Circle circle = new Circle(20);

    public boolean getFinished(){return finished;}
    public float getHealth() {
        return health;
    }
    public boolean isDead() {
        return !lifeStatus; // true, wenn lifeStatus false ist → Gegner tot
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

    public void update(double dt, GamePath path){
        if (!lifeStatus || finished){ return; }

        distance += dt * speed;
        if(distance >= path.getTotalLeanght()){
            distance = path.getTotalLeanght();
            finished = true;
        }

        Point2D point = path.getPointAt(distance);
        circle.setCenterX(point.getX());
        circle.setCenterY(point.getY());

    }

    public Circle getView(){
        return circle;
    }

    public void destroy(AnchorPane root) {
        root.getChildren().remove(circle);
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

        circle.setCenterX(0);
        circle.setCenterY(0);
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

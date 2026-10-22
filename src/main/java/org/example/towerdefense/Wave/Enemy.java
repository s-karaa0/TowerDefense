package org.example.towerdefense.Wave;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.example.towerdefense.Path.GamePath;

import java.util.HashMap;
import java.util.Map;

public class Enemy {
    private float health;
    private float speed;
    private int moneyDropped;
    private float attackOutput;
    private int id = 0;
    private int waveID;
    private int groupID;
    private boolean lifeStatus = true;
    private boolean init = false;
    private double distance = 0;
    private boolean finished = false;
    private ImageView view;
    //private static final Map<String, Image> imageCache = new HashMap<>();

    public boolean getFinished(){return finished;}
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
    private static final Image ENEMY_IMG =
            new Image(Enemy.class.getResourceAsStream("/images/enemies/enemy-lev1.png"));


    public void update(double dt, GamePath path){
        if (!init) {
            Point2D point = path.getPointAt(distance);
            view.setLayoutX(point.getX() -32 - view.getFitWidth() / 2);
            view.setLayoutY(point.getY() -32 - view.getFitHeight() / 2);
            view.setVisible(true);
            init = true;
            return;
        }
        if (!lifeStatus || finished) { return;}

        distance += dt * speed;
        if(distance >= path.getTotalLenght()){
            distance = path.getTotalLenght();
            finished = true;
        }

        Point2D point = path.getPointAt(distance);
        view.setLayoutX(point.getX());
        view.setLayoutY(point.getY());
    }

    public void destroy(AnchorPane root) {root.getChildren().remove(view);}

    public void damageTaken(float damage){
        health -= damage;
        if (health <= 0){
            lifeStatus = false;
        }
    }
    public Enemy(float health, float speed, int moneyDropped,
                 float attackOutput, int id, int groupID,
                 int waveID) {
        this.health = health;
        this.speed = speed;
        this.moneyDropped = moneyDropped;
        this.attackOutput = attackOutput;
        this.id = id;
        this.groupID = groupID;
        this.waveID = waveID;
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

    public boolean getLifeStatus() {
        return lifeStatus;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {return id;}

    public double getDistance() {
        return distance;
    }

    public int getGroupID() {return groupID;}
    public void setGroupID(int groupID) {this.groupID = groupID;}
    public int getWaveID() {return waveID;}
    public void setWaveID(int waveID) {this.waveID = waveID;}

    public ImageView getView() {return view;}
    public void setImageView(Image img) {
        view = new ImageView(img);
        view.setUserData("enemy");

        view.setFitWidth(40);
        view.setFitHeight(40);
        view.setVisible(false);
    }

}

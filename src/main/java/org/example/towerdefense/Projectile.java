package org.example.towerdefense;

public class Projectile {

    private double x, y;
    private double speed;
    private Enemy target;

    public Projectile(double x, double y, double speed, Enemy target) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.target = target;
    }

    public void move() {

        if (target == null) return; // No existing Target.Do nothing

        double dx = target.getX() - x; // vector from projectile to target on the x-axis
        double dy = target.getY() - y; // vector from projectile to target on the y-axis

        double length = Math.sqrt(dx * dx + dy * dy); // Distance between projectile and target

        if (length == 0) return; // Projectile is on enemy

        double dirX = dx / length; // Normalize the vector
        double dirY = dy / length;

        x += dirX * speed; //movement per frame
        y += dirY * speed;
    }

    public double getX() { return x; }
    public double getY() { return y; }
}
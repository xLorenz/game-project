package enemies;

import physics.*;
import player.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public abstract class Enemy extends PhysicsBall {

    public static PhysicsHandler handler;
    public static List<Enemy> enemies = new ArrayList<Enemy>();
    public static Player player;

    public Vector2 target = new Vector2();

    public int health;
    public int damage;

    public double jumpCooldown;
    public double jumpTimer = 0;

    public int maxJumpStrength;
    public int jumpAngleDegrees;
    public double jumpAngleRadians = 0.0;

    public Enemy(Vector2 pos, Color color, int radius) {
        super(radius, 0.8, 5.0, 0L);
        this.pos = pos;
        this.displayColor = color;

        handler.addObject(this);
        enemies.add(this);
    }

    public void setAngle(int angleDegrees) {
        jumpAngleDegrees = angleDegrees;
        jumpAngleRadians = Math.toRadians(angleDegrees);

    }

    @Override
    public void draw(Graphics2D g, Vector2 offset, double scale) {
        int x = (int) (pos.x - radius + offset.x);
        int y = (int) (pos.y - radius + offset.y);

        g.setColor(displayColor);
        g.fillOval((int) (x * scale), (int) (y * scale), (int) (radius * 2 * scale),
                (int) (((radius * 2) - Math.min(vel.y * 0.02, radius)) * scale));
    }

    @Override
    public void update(double dt) {

        updateTimers(dt);

        if (jumpTimer == 0 && supported) {

            jumpTowardsTarget(pathToPlayer());
            jumpTimer = jumpCooldown;
        }
    }

    public void updateTimers(double dt) {
        if (jumpTimer > 0) {
            if (supported)
                jumpTimer -= dt;
        } else {
            jumpTimer = 0;
        }
    }

    public double pathToPlayer() {
        target.set(player.pos);
        if (target.x < pos.x) {
            return Math.PI + jumpAngleRadians;
        } else {
            return -jumpAngleRadians;
        }
    }

    public void jumpTowardsTarget(double angleTowardsTargetRadians) {
        double dx = target.x - pos.x;
        double dy = target.y - pos.y;

        double cos = Math.cos(angleTowardsTargetRadians);
        double sin = Math.sin(angleTowardsTargetRadians);

        double difference = (dx * Math.tan(angleTowardsTargetRadians) - dy);
        double jumpVelocity = maxJumpStrength;

        if (difference != 0 && cos != 0) {
            // parabolic throw formula
            jumpVelocity = Math.sqrt(Math.abs((handler.gravity.y * dx * dx) / (2 * cos * cos * difference)));
        }
        // cap velocity
        jumpVelocity = jumpVelocity > maxJumpStrength ? maxJumpStrength : jumpVelocity;

        vel.set(cos, sin);
        vel.scaleLocal(jumpVelocity);
        supported = false;
    }

    public void damage(int damage) {

    }

    public void kill() {
        // rewards and remove()
    }

    public void remove() {
        // remove from Enemy list and handler objects
    }

}

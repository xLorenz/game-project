package src;

import physics.*;
import java.awt.Color;

public class Enemy extends PhysicsBall {

    public Color color;
    public int health;

    public Vector2 target = new Vector2();

    public Enemy(Vector2 pos, Color color, PhysicsHandler handler) {
        super(25, 0.8, 5.0, 0L);
        this.pos = pos;
        this.color = color;

        handler.addBall(this);

    }
}

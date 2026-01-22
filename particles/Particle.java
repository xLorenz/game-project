package particles;

import physics.Vector2;
import java.awt.Graphics2D;

public abstract class Particle {

    boolean active;

    final Vector2 pos = new Vector2();
    final Vector2 vel = new Vector2();

    double life;

    abstract void update(double dt);

    abstract void draw(Graphics2D g, Vector2 offset, double scale);

    void reset(Vector2 pos, Vector2 vel, double life) {
        this.pos.set(pos);
        this.vel.set(vel);
        this.life = life;
        this.active = true;
    }

    void deactivate() {
        active = false;
    }

}

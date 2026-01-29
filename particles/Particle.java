package particles;

import physics.Vector2;

public abstract class Particle {

    protected final Vector2 pos = new Vector2();
    protected final Vector2 vel = new Vector2();

    protected boolean alive;
    protected double life;
    protected double size;

    public abstract void draw(ParticleRenderer renderer);

    public abstract void free(); // Concrete classes MUST implement this so updater can return the instance to
                                 // the correct static pool when the particle dies.

    public void update(double dt) {
        updateLife(dt);
        pos.addLocal(vel.scale(dt));
    }

    public void updateLife(double dt) {
        life -= dt;
        if (life <= 0f)
            deactivate();
    }

    public void reset(Vector2 pos, Vector2 vel, double size, double life) {
        this.pos.set(pos);
        this.vel.set(vel);
        this.life = life;
        this.size = size;
        this.alive = true;
    }

    void deactivate() {
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

}

package particles.types;

import java.awt.Color;

import particles.Particle;
import particles.ParticlePool;
import particles.ParticleRenderer;
import physics.Vector2;

public class SimpleParticle extends Particle {

    // Per-class pool: tune initialSize and allowGrowth as needed.
    private static final ParticlePool<SimpleParticle> POOL = new ParticlePool<>(1500, SimpleParticle::new,
            false);

    private Color color = new Color(200, 200, 200, 220);
    private int radius = 10;

    private SimpleParticle() {
    }

    /** Static factory: obtains from the class pool and initializes it. */
    public static SimpleParticle obtain(Vector2 pos, Vector2 vel, double size, double life, Color color) {
        SimpleParticle p = POOL.obtain();
        if (p == null)
            return null; // pool exhausted and growth disallowed
        p.reset(pos, vel, size, life);
        p.color = color;
        return p;
    }

    @Override
    public void draw(ParticleRenderer renderer) {
        // fade out
        double lifeFrac = Math.max(0f, Math.min(1f, life / 2f));
        int alpha = (int) (255 * lifeFrac);

        renderer.setFill(color, alpha);
        renderer.drawCircle(pos, (int) (radius * size));
    }

    @Override
    public void free() {
        this.alive = false;
        POOL.free(this);
    }

    // helpers

    public static int poolFreeCount() {
        return POOL.freeCount();
    }

    public static int poolAllocations() {
        return POOL.allocations();
    }

    public static int poolDrops() {
        return POOL.drops();
    }
}

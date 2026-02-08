package particles.types;

import java.awt.Color;

import particles.Particle;
import particles.ParticlePool;
import physics.process.BatchRenderer;
import physics.structures.Vector2;

public class SimpleBackgroundParticle extends Particle {

    // Per-class pool: tune initialSize and allowGrowth as needed.
    private static final ParticlePool<SimpleBackgroundParticle> POOL = new ParticlePool<>(1500,
            SimpleBackgroundParticle::new,
            false);

    private static Color defColor = Color.darkGray;
    private static double defLife = 20.0;
    private static double defRotVel = 0.1;
    private static double rotFric = 1;

    private Color color = Color.darkGray;
    private int radius = 100;
    private double rotVel = defRotVel;
    private double rotAng = rand.nextInt(360);

    private Vector2[] points = { new Vector2(radius * size, 0).rotate(rotAng),
            new Vector2(radius * size, 0).rotate(120 + rotAng),
            new Vector2(radius * size, 0).rotate(240 + rotAng) };

    private SimpleBackgroundParticle() {
        this.background = true;
    }

    /** Static factory: obtains from the class pool and initializes it. */
    public static SimpleBackgroundParticle obtain(Vector2 pos, Vector2 vel, double size, double life, Color color) {
        SimpleBackgroundParticle p = POOL.obtain();
        if (p == null)
            return null; // pool exhausted and growth disallowed
        p.reset(pos, vel, size, life);
        p.color = color;
        p.rotVel = defRotVel;

        for (Vector2 v : p.points) {
            v.normalizeLocal().scaleLocal(size * p.radius);
        }
        return p;
    }

    public static void emit(Vector2 pos, Vector2 vel, double size, double life, Color color) {
        SimpleBackgroundParticle p = SimpleBackgroundParticle.obtain(pos, vel, size, life, color);
        handler.addParticle(p);
    }

    public static void emit(Vector2 pos) {
        Vector2 vel = Vector2.random(-20, 20, -20, 20);
        double life = defLife;
        double size = 1.0 + rand.nextInt(100) / 10;

        SimpleBackgroundParticle p = SimpleBackgroundParticle.obtain(pos, vel, size, life, defColor);
        handler.addParticle(p);
    }

    @Override
    public void draw(BatchRenderer renderer) {
        // fade out
        double lifeFrac = Math.max(0f, Math.min(1f, Math.sin(Math.PI * life / defLife)));
        int alpha = (int) (255 * lifeFrac / 2);

        renderer.setFill(color, alpha);

        Vector2[] ps = { points[0].add(pos), points[1].add(pos), points[2].add(pos) };

        renderer.drawPolygon(ps, 3);
    }

    @Override
    public void update(double dt) {
        super.update(dt);

        for (Vector2 v : points) {
            v.rotateLocal(rotVel);
        }
        rotVel *= rotFric;
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

package particles.types;

import java.awt.Color;

import particles.Particle;
import particles.ParticlePool;
import particles.BatchRenderer;
import physics.Vector2;

public class TriangleParticle extends Particle {

    // Per-class pool: tune initialSize and allowGrowth as needed.
    private static final ParticlePool<TriangleParticle> POOL = new ParticlePool<>(1500, TriangleParticle::new,
            false);

    private static Color defColor = Color.cyan;
    private static double defRotVel = 10;
    private static double rotFric = 0.99;

    private Color color = Color.cyan;
    private int radius = 10;
    private double rotVel = defRotVel;
    private double rotAng = rand.nextInt(360);

    private Vector2[] points = { new Vector2(radius * size, 0).rotate(rotAng),
            new Vector2(radius * size, 0).rotate(120 + rotAng),
            new Vector2(radius * size, 0).rotate(240 + rotAng) };

    private TriangleParticle() {
    }

    /** Static factory: obtains from the class pool and initializes it. */
    public static TriangleParticle obtain(Vector2 pos, Vector2 vel, double size, double life, Color color) {
        TriangleParticle p = POOL.obtain();
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
        TriangleParticle p = TriangleParticle.obtain(pos, vel, size, life, color);
        handler.addParticle(p);
    }

    public static void emit(Vector2 pos) {
        Vector2 vel = Vector2.random(-100, 100, -100, 100);
        double life = 2.0 + rand.nextInt(20) / 10;
        double size = 1.0 + rand.nextInt(20) / 10;

        TriangleParticle p = TriangleParticle.obtain(pos, vel, size, life, defColor);
        handler.addParticle(p);
    }

    @Override
    public void draw(BatchRenderer renderer) {
        // fade out
        double lifeFrac = Math.max(0f, Math.min(1f, life / 10f));
        int alpha = (int) (255 * lifeFrac);

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

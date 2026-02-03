package particles;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

public class ParticleUpdater implements Runnable {

    // runs on worker threads
    // Physics and lifecycle

    private static final float FIXED_DT = 1f / 120f; // physics rate
    private static final long NANOS_PER_UPDATE = (long) (1_000_000_000 * FIXED_DT);

    private final ParticleHandler handler;
    private volatile boolean running = true;

    public ParticleUpdater(ParticleHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        long previous = System.nanoTime();
        long accumulator = 0L;

        while (running) {
            long now = System.nanoTime();
            long frameTime = now - previous;
            previous = now;
            accumulator += frameTime;

            // Cap to avoid spiral of death after long pause
            if (accumulator > NANOS_PER_UPDATE * 16)
                accumulator = NANOS_PER_UPDATE * 16;

            while (accumulator >= NANOS_PER_UPDATE) {
                fixedUpdate();
                accumulator -= NANOS_PER_UPDATE;
            }

            handler.publishFrame(); // publish snapshot for rendering

            // sleep a tiny amount to reduce CPU usage (tunable)
            LockSupport.parkNanos(1_000_000L); // 1ms
        }
    }

    private void fixedUpdate() {
        List<Particle> particles = handler.getUpdateParticles();

        synchronized (particles) {
            Iterator<Particle> it = particles.iterator();
            while (it.hasNext()) {
                Particle p = it.next();
                if (p != null) {
                    p.update(FIXED_DT);
                    if (!p.isAlive()) {
                        it.remove();
                        p.free();
                    }
                }
            }
        }
    }

    public void stop() {
        running = false;
    }

}

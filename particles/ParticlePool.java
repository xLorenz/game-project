package particles;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Supplier;

public class ParticlePool<T extends Particle> {

    // owns reusable Particle instances

    private final ConcurrentLinkedDeque<T> free = new ConcurrentLinkedDeque<>();
    private final Supplier<T> factory;
    private final boolean allowGrowth;

    private static volatile int allocations = 0;
    private volatile int drops = 0;

    public ParticlePool(int initialSize, Supplier<T> factory, boolean allowGrowth) {
        this.factory = factory;
        this.allowGrowth = allowGrowth;
        for (int i = 0; i < initialSize; i++) {
            free.push(factory.get());
        }
    }

    public T obtain() {
        T t = free.poll();
        if (t != null)
            return t;
        if (allowGrowth) {
            allocations++;
            return factory.get();
        }
        drops++;
        return null;
    }

    public void free(T p) {
        if (p == null)
            return;
        p.alive = false;
        free.push(p);
    }

    // helpers
    public int freeCount() {
        return free.size();
    }

    public int allocations() {
        return allocations;
    }

    public int drops() {
        return drops;
    }
}

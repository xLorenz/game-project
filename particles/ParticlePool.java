package particles;

import java.util.ArrayDeque;
import java.util.function.Supplier;

public class ParticlePool<T extends Particle> {

    // owns reusable Particle instances

    private final ArrayDeque<T> free = new ArrayDeque<>();
    private final Supplier<T> factory;

    ParticlePool(int size, Supplier<T> factory) {
        this.factory = factory;
        for (int i = 0; i < size; i++) {
            free.push(factory.get());
        }
    }

    T obtain() {
        return free.isEmpty() ? factory.get() : free.pop();
    }

    void free(T p) {
        free.push(p);
    }
}

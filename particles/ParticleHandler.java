package particles;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ParticleHandler {

    // owns active particles and orcheswtration

    private ConcurrentLinkedQueue<Particle> activeParticles;
    private Long nextId = 0L;
    private ArrayList<ParticleGenerator> generators = new ArrayList<ParticleGenerator>();

    public void updateParticles(double dt) {
        particles.forEach((id, particle) -> particle.update(dt));
    }

}

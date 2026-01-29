package particles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParticleHandler {

    // owns active particles and orcheswtration

    private final List<Particle> updateParticles = new ArrayList<>(); // mutated by updater adn gen
    private volatile List<Particle> renderParticles = Collections.emptyList(); // volatile snapshot for renderer

    public ParticleHandler() {
    }

    public void swapBuffers() {
        synchronized (this) {
            List<Particle> temp = renderParticles;
            renderParticles.clear();
            renderParticles.addAll(updateParticles);

            updateParticles.clear();
            updateParticles.addAll(temp);
        }
    }

    // called by generator
    public void addParticle(Particle p) {
        synchronized (updateParticles) {
            updateParticles.add(p);
        }
    }

    public void publishFrame() {
        synchronized (updateParticles) {
            renderParticles = new ArrayList<>(updateParticles); // snapshot copy
        }
    }

    public ParticlePool<Particle> getPool() {
        return pool;
    }

    public List<Particle> getUpdateParticles() {
        return updateParticles;
    }

    public List<Particle> getRenderParticles() {
        return renderParticles; // EDT reads only
    }

}

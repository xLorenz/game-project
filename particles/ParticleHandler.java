package particles;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import physics.process.PhysicsHandler;
import physics.process.BatchRenderer;

public class ParticleHandler {

    // owns active particles and orcheswtration

    private final List<Particle> updateParticles = new ArrayList<>(); // mutated by updater and gen
    private volatile List<Particle> renderParticles = Collections.emptyList(); // volatile snapshot for renderer

    private final BatchRenderer renderer = new BatchRenderer();
    private final ParticleUpdater updater = new ParticleUpdater(this);

    public ParticleHandler(PhysicsHandler pHandler) {
        renderer.setDisplay(pHandler.display);
        Particle.setHandler(this);
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

    public void render(Graphics2D g) {
        renderer.setGraphics(g);
        for (Particle p : getRenderParticles()) {
            if (p != null)
                p.draw(renderer);
        }
    }

    public void renderFgParticles(Graphics2D g) {
        renderer.setGraphics(g);
        for (Particle p : getRenderParticles()) {
            if (p != null)
                if (!p.background)
                    p.draw(renderer);
        }
    }

    public void renderBgParticles(Graphics2D g) {
        renderer.setGraphics(g);
        for (Particle p : getRenderParticles()) {
            if (p != null)
                if (p.background)
                    p.draw(renderer);
        }
    }

    public List<Particle> getUpdateParticles() {
        return updateParticles;
    }

    public ParticleUpdater getUpdater() {
        return updater;
    }

    public List<Particle> getRenderParticles() {
        return renderParticles; // EDT reads only
    }

    public BatchRenderer getRenderer() {
        return renderer;
    }

}

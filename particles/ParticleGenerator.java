package particles;

import physics.Vector2;

public class ParticleGenerator {

    // requests particles from the pool

    private final ParticleHandler handler;

    public ParticleGenerator(ParticleHandler handler) {
        this.handler = handler;
    }

    public void emit(Vector2 pos, Vector2 vel, double size, double life) {
        Particle p = handler.getPool().obtain();
        p.reset(pos, vel, size, life);
        handler.addParticle(p);
    }

}

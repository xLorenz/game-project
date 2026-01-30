# Particles

Particle handler has all the particles as well as snapshots of them for the renderer.
Particles will update in ParticleUpdater's own thread, and render using BatchRenderer rendering. 

Each concrete Particle class has its own ParticlePool, as well as its own emit() and free() methods.
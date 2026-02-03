package player;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Random;

import particles.types.SimpleBackgroundParticle;
import particles.types.SimpleParticle;
import particles.types.TriangleParticle;
import physics.*;

public class Player extends PhysicsBall {

    public PhysicsHandler handler;
    private Random rand = new Random();

    public Color color;

    public HealthManager healthManager;

    public int baseSpeed = 55;
    public double baseSprintModifier = 1.5;
    public int baseMaxSpeed = 250;

    public int baseJumpStrength = 600;

    public Vector2 direction = new Vector2(1, 0);

    public Controller controller = new Controller();

    public double airBorneTimer = 0.0;

    public boolean invulnerable = false;
    public double invulnerableTimer = 0.0;

    public boolean sprinting = false;

    public Player(Vector2 pos, Color color, PhysicsHandler handler) {
        super(25, 0.1, 10.0, 0L);
        this.pos = pos;
        this.color = color;

        this.healthManager = new HealthManager(100, 1.0);

        this.forceAwake = true;
        this.friction = 0.0;

        handler.addObject(this);
        this.handler = handler;

    }

    @Override
    public void draw(Graphics2D g, Vector2 offset, double scale) {
        Polygon shape = new Polygon();

        Vector2 v1 = pos.add(direction.rotate(120 * 0).scale(radius * 1.5));
        Vector2 v2 = pos.add(direction.rotate(120 * 1).scale(radius * 1.5).sub(vel.scale(radius * 1.5 / 1000)));
        Vector2 v3 = pos.add(direction.rotate(120 * 2).scale(radius * 1.5).sub(vel.scale(radius * 1.5 / 1000)));

        shape.addPoint((int) ((v1.x + offset.x) * scale), (int) ((v1.y + offset.y) * scale));
        shape.addPoint((int) ((v2.x + offset.x) * scale), (int) ((v2.y + offset.y) * scale));
        shape.addPoint((int) ((v3.x + offset.x) * scale), (int) ((v3.y + offset.y) * scale));

        if (healthManager.vulnerable) {
            g.setColor(color);
        } else {
            g.setColor(color.darker());
        }
        g.fillPolygon(shape);
    }

    @Override
    public void update(double dt) {

        updateTimers(dt);

        if (supported) {
            vel.x *= 0.8;
        } else {
            // TriangleParticle.emit(pos);
        }

        direction.set((handler.getMapPos(controller.mouse.pos).sub(pos)));
        direction.normalizeLocal();

        if (rand.nextInt(200) == 1)
            SimpleBackgroundParticle.emit(pos.add(Vector2.random(-1000, 1000, -1000, 1000)));

    }

    public void handleInputs() {
        // jump
        boolean jump = (controller.keys.space.pressed || controller.keys.w.pressed);
        boolean allowed = (supported || airBorneTimer < 0.3);

        if (jump && allowed) {
            // must be on the ground or in coyote timer
            vel.set(new Vector2(vel.x, -baseJumpStrength));

            for (int i = 0; i < 20; i++) {
                SimpleParticle.emit(new Vector2(pos.x, pos.y + radius));
            }
            airBorneTimer = 0.3;
        }

        // walk left
        if (controller.keys.a.pressed) {
            if (vel.x > (sprinting ? -baseMaxSpeed * baseSprintModifier : -baseMaxSpeed)) {
                vel.x -= sprinting ? baseSpeed * baseSprintModifier : baseSpeed;
            }
            if (sprinting && vel.x < 0 && supported) {
                SimpleParticle.emit(new Vector2(pos.x, pos.y + radius),
                        Vector2.random(0, 50, 0, -50),
                        0.75 + rand.nextInt(20) / 10,
                        1,
                        color);
            }
        }
        // walk right
        if (controller.keys.d.pressed) {
            if (vel.x < (sprinting ? baseMaxSpeed * baseSprintModifier : baseMaxSpeed)) {
                vel.x += sprinting ? baseSpeed * baseSprintModifier : baseSpeed;
            }
            if (sprinting && vel.x > 0 && supported) {
                SimpleParticle.emit(new Vector2(pos.x, pos.y + radius),
                        Vector2.random(-50, 0, -50, 0),
                        0.75 + rand.nextInt(20) / 10,
                        1,
                        color);
            }
        }
        // sprint
        sprinting = controller.keys.control.pressed;
    }

    public void updateTimers(double dt) {
        if (!supported) {
            airBorneTimer += dt;
        } else {
            airBorneTimer = 0;
        }
        healthManager.updateTimers(dt);
    }

    public void damage(int ammount) {
        if (healthManager.damage(ammount)) {
            for (int i = 0; i < 10 * ammount; i++) {
                TriangleParticle.emit(pos);
            }
        }
    }

    @Override
    public Manifold collideWithRect(PhysicsRect rect) {
        // landing particles
        Manifold m = Collision.circleRect(this, rect); // get collision with rect (terrain)
        if (m != null) { // make sure it collided
            Boolean c = false;
            for (Vector2 con : m.contacts) {
                if (con.y > pos.y) // make sure it collided under player
                    c = true;
            }
            if (airBorneTimer > 0 && c) { // if the player has been falling
                for (int i = 0; i < airBorneTimer * 10; i++) {
                    SimpleParticle.emit(new Vector2(pos.x, pos.y + radius)); // emit particles
                }
            }
        }
        return m; // return Manifold
    }

}

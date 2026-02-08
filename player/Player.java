package player;

import java.awt.Color;
import java.util.Random;

import particles.types.*;
import physics.objects.PhysicsBall;
import physics.process.BatchRenderer;
import physics.process.PhysicsHandler;
import physics.structures.Vector2;
import physics.structures.Manifold;
import physics.objects.PhysicsRect;
import physics.collisions.Collision;
import player.skills.*;

public class Player extends PhysicsBall {

    public PhysicsHandler handler;
    private Random rand = new Random();

    public Color color;

    public HealthManager healthManager;
    public SkillsManager skillsManager;

    public int baseSpeed = 55;
    public double speedMultiplier = 1.0;
    public int baseMaxSpeed = 250;

    public int baseJumpStrength = 600;

    public Vector2 direction = new Vector2(1, 0);

    public Controller controller = new Controller();

    public double airBorneTimer = 0.0;

    public Player(Vector2 pos, Color color, PhysicsHandler handler) {
        super(25, 0.1, 10.0, 0L);
        this.pos = pos;
        this.color = color;

        this.healthManager = new HealthManager(100, 1.0);
        this.skillsManager = new SkillsManager(this);
        skillsManager.addSkill(new Sprint());
        skillsManager.addSkill(new DoubleJump());

        this.forceAwake = true;
        this.friction = 0.0;

        handler.addObject(this);
        this.handler = handler;

    }

    @Override
    public void draw(BatchRenderer renderer) {
        Vector2[] points = {
                pos.add(direction.rotate(120 * 0).scale(radius * 1.5)),
                pos.add(direction.rotate(120 * 1).scale(radius * 1.5).sub(vel.scale(radius * 1.5 / 1000))),
                pos.add(direction.rotate(120 * 2).scale(radius * 1.5).sub(vel.scale(radius * 1.5 / 1000))),
        };

        if (healthManager.vulnerable) {
            renderer.setFill(color, 255);
        } else {
            renderer.setFill(color.darker(), 255);
        }
        renderer.drawPolygon(points, 3);
    }

    @Override
    public void update(double dt) {

        updateTimers(dt);

        if (supported) {
            vel.x *= 0.8;
        } else {
            // TriangleParticle.emit(pos);
        }

        direction.set((handler.display.getMapPos(controller.mouse.pos).sub(pos)));
        direction.normalizeLocal();

        skillsManager.updateSkills(dt);

        // bg particles
        if (rand.nextInt(200) == 1)
            SimpleBackgroundParticle.emit(pos.add(Vector2.random(-1000, 1000, -1000, 1000)));

    }

    public void handleInputs() {
        // jump
        boolean jump = (controller.keys.space.pressed);
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
            if (vel.x > -baseMaxSpeed * speedMultiplier) {
                vel.x -= baseSpeed * speedMultiplier;
            }
        }
        // walk right
        if (controller.keys.d.pressed) {
            if (vel.x < baseMaxSpeed * speedMultiplier) {
                vel.x += baseSpeed * speedMultiplier;
            }
        }

        // skills
        skillsManager.handleInputs(controller);
    }

    public void updateTimers(double dt) {
        if (!supported) {
            airBorneTimer += dt;
        } else {
            airBorneTimer = 0;
        }
        healthManager.updateTimers(dt);
        skillsManager.updateTimers(dt);
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

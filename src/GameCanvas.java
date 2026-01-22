package src;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import player.*;
import enemies.*;
import physics.*;

public class GameCanvas extends Canvas implements Runnable {

    private int fps;
    private int frames;
    private long fpsTimer = System.nanoTime();

    private boolean running = true;
    private Dimension size = new Dimension(1000, 800);

    private Vector2 mousePos = new Vector2();

    private PhysicsHandler handler = new PhysicsHandler(size.width, size.height);
    private Player player = new Player(new Vector2(size.width / 2, 0), Color.cyan, handler);

    private Adapters adapters = new Adapters(player, handler, mousePos);

    private static final RenderingHints HINTS = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

    static {
        HINTS.put(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_NORMALIZE);
    }

    public GameCanvas() {

        this.setPreferredSize(size);
        this.setIgnoreRepaint(true);
        this.setBackground(new Color(12, 13, 20));
        this.setFocusable(true);

        this.addKeyListener(adapters.keyAdapter);
        this.addMouseListener(adapters.mouseAdapter);
        this.addMouseMotionListener(adapters.mouseMotionAdapter);

        setUpGame();
    }

    private void setUpGame() {
        handler.displayScale = 0.2;
        handler.anchorFollowRadius = 0;
        handler.anchorFollowVelocity = 1;
        handler.anchorFollowFriction = 0.95;
        handler.mainObject = player;
        Enemy.handler = handler;
        Enemy.player = player;

        // spawn test enemies
        for (int i = 0; i < 20; i++) {
            new Normie(new Vector2(size.width / 2, -100));
        }
        for (int i = 0; i < 20; i++) {
            new Speedster(new Vector2(size.width / 2, -120));
        }
        for (int i = 0; i < 20; i++) {
            new Jumper(new Vector2(size.width / 2, -140));
        }

        // add "terrain"
        handler.addRect(new Vector2(size.width / 2, size.height + 150), size.width * 10, 500); // bottom

        handler.addRect(new Vector2(-size.width * 4, 0), 20, size.height * 2); // walls
        handler.addRect(new Vector2(size.width * 5, 0), 20, size.height * 2);

        handler.addRect(new Vector2(size.width / 2 - 100, size.height - 200), 500, 50);
        handler.addRect(new Vector2(size.width / 2 + 150, size.height - 275), 100, 100);
        handler.addRect(new Vector2(size.width / 2 + 300, size.height - 375), 250, 100);
        handler.addRect(new Vector2(size.width / 2 - 300, size.height - 400), 300, 25);
        handler.addRect(new Vector2(size.width / 2 - 0, size.height - 712), 300, 25);

    }

    @Override
    public void run() {
        long last = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            float dt = (now - last) * 1e-9f;
            last = now;

            update(dt);
            render();

            frames++;
            if (now - fpsTimer >= 1_000_000_000L) {
                fps = frames;
                frames = 0;
                fpsTimer = now;

                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                frame.setTitle("Project | FPS: " + fps);
            }
        }
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null)
            return;

        do {
            do {
                Graphics2D g = (Graphics2D) bs.getDrawGraphics();
                try {
                    g.addRenderingHints(HINTS);

                    g.setColor(getBackground());
                    g.fillRect(0, 0, size.width, size.height);

                    // draw game

                    // handler.displayChunkBorders(g, size.width, size.height);
                    // handler.drawRecordedChunks(g);
                    handler.displayObjects(g);
                    // collision debug overlay
                    // handler.displayCollisionDebug(g);

                } finally {
                    g.dispose();
                }

            } while (bs.contentsRestored());

            bs.show();
            Toolkit.getDefaultToolkit().sync();

        } while (bs.contentsLost());

    }

    private void update(float dt) {
        handler.updatePhysics(dt);
        player.handleInputs();
    }

}

package particles;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import physics.PhysicsHandler;
import physics.Vector2;

import java.awt.Color;

public class BatchRenderer {

    // batch renderer

    private Graphics2D g;
    private PhysicsHandler pHandler;
    private final Ellipse2D.Float circle = new Ellipse2D.Float();

    public void setPhysicsHandler(PhysicsHandler p) {
        this.pHandler = p;
    }

    public void setGraphics(Graphics2D g) {
        this.g = g;
    }

    public void setFill(Color c, int alpha) {
        if (g == null)
            return;
        int a = Math.max(0, Math.min(255, alpha));
        Color color = new Color(c.getRed(), c.getGreen(), c.getBlue(), a);
        g.setComposite(AlphaComposite.SrcOver);
        g.setColor(color);
    }

    public void drawCircle(Vector2 pos, int radius) {
        if (g == null)
            return;

        int xi = (int) (pos.x + pHandler.mapAnchor.x) - radius;
        int yi = (int) (pos.y + pHandler.mapAnchor.y) - radius;
        int diam = radius * 2;

        circle.setFrame(xi * pHandler.displayScale, yi * pHandler.displayScale, diam * pHandler.displayScale,
                diam * pHandler.displayScale);

        g.fill((Shape) circle);

    }

    public void end() {
        this.g = null;
    }

}

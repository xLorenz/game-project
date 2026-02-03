package particles;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import physics.PhysicsHandler;
import physics.Vector2;

import java.awt.Color;

public class BatchRenderer {

    // batch renderer

    private Graphics2D g;
    private PhysicsHandler pHandler;
    private final Ellipse2D.Float circle = new Ellipse2D.Float();
    private final Path2D.Float polygon = new Path2D.Float();
    private final Rectangle2D.Float rect = new Rectangle2D.Float();

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

        double scale = pHandler.displayScale;
        double xi = ((pos.x + pHandler.mapAnchor.x) - radius) * scale;
        double yi = ((pos.y + pHandler.mapAnchor.y) - radius) * scale;
        double diam = radius * 2 * scale;

        circle.setFrame(xi, yi, diam, diam);

        g.fill((Shape) circle);

    }

    public void drawRect(Vector2 pos, double w, double h) {
        if (g == null)
            return;

        double scale = pHandler.displayScale;

        double xi = (pos.x + pHandler.mapAnchor.x) * scale;
        double yi = (pos.y + pHandler.mapAnchor.y) * scale;

        rect.setFrame(xi, yi, w * scale, h * scale);
        g.fill(rect);
    }

    public void drawSquare(Vector2 pos, double w) {
        if (g == null)
            return;

        double scale = pHandler.displayScale;

        double xi = (pos.x + pHandler.mapAnchor.x) * scale;
        double yi = (pos.y + pHandler.mapAnchor.y) * scale;

        rect.setFrame(xi, yi, w * scale, w * scale);
        g.fill(rect);
    }

    public void drawTriangle(Vector2 a, Vector2 b, Vector2 c) {
        if (g == null)
            return;

        double scale = pHandler.displayScale;

        double ax = (a.x + pHandler.mapAnchor.x) * scale;
        double ay = (a.y + pHandler.mapAnchor.y) * scale;

        double bx = (b.x + pHandler.mapAnchor.x) * scale;
        double by = (b.y + pHandler.mapAnchor.y) * scale;

        double cx = (c.x + pHandler.mapAnchor.x) * scale;
        double cy = (c.y + pHandler.mapAnchor.y) * scale;

        polygon.reset();
        polygon.moveTo(ax, ay);
        polygon.lineTo(bx, by);
        polygon.lineTo(cx, cy);
        polygon.closePath();

        g.fill(polygon);
    }

    public void drawPolygon(Vector2[] verts, int count) {
        if (g == null || count < 3)
            return;

        double scale = pHandler.displayScale;

        polygon.reset();

        double x0 = (verts[0].x + pHandler.mapAnchor.x) * scale;
        double y0 = (verts[0].y + pHandler.mapAnchor.y) * scale;
        polygon.moveTo(x0, y0);

        for (int i = 1; i < count; i++) {
            double xi = (verts[i].x + pHandler.mapAnchor.x) * scale;
            double yi = (verts[i].y + pHandler.mapAnchor.y) * scale;
            polygon.lineTo(xi, yi);
        }

        polygon.closePath();
        g.fill(polygon);
    }

    public void end() {
        this.g = null;
    }

}

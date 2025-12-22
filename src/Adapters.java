package src;

import java.awt.event.*;

import physics.PhysicsHandler;
import physics.Vector2;
import player.Player;

public class Adapters {
    public MyKeyAdapter keyAdapter;
    public MyMouseAdapter mouseAdapter;
    public MyMouseMotionAdapter mouseMotionAdapter;

    public Adapters(Player player, PhysicsHandler handler, Vector2 mousePos) {
        this.keyAdapter = new MyKeyAdapter(player, handler);
        this.mouseAdapter = new MyMouseAdapter(player);
        this.mouseMotionAdapter = new MyMouseMotionAdapter(player, mousePos);
    }

    public class MyKeyAdapter extends KeyAdapter {
        public Player player;
        public PhysicsHandler handler;

        public MyKeyAdapter(Player player, PhysicsHandler handler) {
            this.player = player;
            this.handler = handler;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player.controller.keyPress(e.getKeyCode());
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                handler.mapAnchorVelocity.y += handler.anchorFollowVelocity * 2;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                handler.mapAnchorVelocity.y -= handler.anchorFollowVelocity * 2;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                handler.mapAnchorVelocity.x += handler.anchorFollowVelocity * 2;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                handler.mapAnchorVelocity.x -= handler.anchorFollowVelocity * 2;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            player.controller.keyRelease(e.getKeyCode());
        }
    }

    public class MyMouseAdapter extends MouseAdapter {
        public Player player;

        public MyMouseAdapter(Player player) {
            this.player = player;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            player.controller.mousePress(e.getButton());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            player.controller.mouseRelease(e.getButton());
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                // System.out.println("Double click at " + e.getX() + "," + e.getY());
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // optional
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // optional: you may want to reset coords
        }
    }

    public class MyMouseMotionAdapter extends MouseMotionAdapter {
        public Player player;
        public Vector2 mousePos;

        public MyMouseMotionAdapter(Player player, Vector2 mousePos) {
            this.player = player;
            this.mousePos = mousePos;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mouseMovedOrDragged(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            mouseMovedOrDragged(e);
        }

        private void mouseMovedOrDragged(MouseEvent e) {

            mousePos.set(e.getX(), e.getY());
            player.controller.mouse.pos.set(mousePos);
            // Optionally get global screen position:
            // Point screenPoint = e.getLocationOnScreen();
            // System.out.println("Screen pos: " + screenPoint.x + "," + screenPoint.y);
        }
    }

}

package src;

import javax.swing.JFrame;

public class Frame extends JFrame {
    Frame() {
        GameCanvas canvas = new GameCanvas();
        this.add(canvas);
        this.setTitle("Project");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack(); // fits the frame around the components
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        canvas.createBufferStrategy(3);
        canvas.requestFocusInWindow();

        new Thread(canvas).start(); // start game thread
    }
}
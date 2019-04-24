package net.metzlar;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GUI extends JFrame {
    private BufferedImage image;
    private float exposure = 1;

    public GUI(Image image) {
        image.addListener(() -> {
            GUI.this.image = image.getBufferedImage(exposure);
            this.repaint();
        });

        this.setSize(Math.min(image.settings.imageWidth, 1024), Math.min(image.settings.imageHeight, 1024));

        this.setTitle("Render preview");

        this.setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.add(new DrawPanel());
    }

    class DrawPanel extends JPanel {

        DrawPanel() {
            super(true);
        }

        @Override
        public void paintComponent(Graphics gn) {
            super.paintComponent(gn);
            Graphics2D g = (Graphics2D) gn;

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            if (image != null) {
                Dimension scaledDimension = getScaledDimension(
                        new Dimension(image.getWidth(), image.getHeight()),
                        new Dimension(this.getWidth(), this.getHeight())
                );
                g.drawImage(image, 0, 0, scaledDimension.width, scaledDimension.height, null);
            }
        }
    }

    public Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }
}

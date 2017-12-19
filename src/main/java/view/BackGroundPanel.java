package view;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

public class BackGroundPanel extends JPanel {

    private Image image;
    private Image image2;

    BackGroundPanel(Image image, Image image2) {
        this();
        this.image = image;
        this.image2 = image2;
    }

    private BackGroundPanel() {
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.drawImage(image, 0, 0, getWidth(), 77, this);
        g2.drawImage(image2, 0, 77, getWidth(), getHeight()-77, this);
        g2.dispose();
        super.paintComponent(g);
    }

}

package view.dataCollect;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class TitlePanel extends JPanel {

	private Color color1, color2;

	public TitlePanel(Color color1, Color color2) {
		super();
		this.color1 = color1;
		this.color2 = color2;
	}

	public TitlePanel(Color color1, Color color2, LayoutManager layout) {
		super(layout);
		this.color1 = color1;
		this.color2 = color2;
	}

	public TitlePanel() {
		super();
	}

	public TitlePanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public TitlePanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public TitlePanel(LayoutManager layout) {
		super(layout);
	}

	static Color c1 = new Color(204, 229, 254), c2 = new Color(134, 194, 251),
			c3 = new Color(80, 167, 249), c4 = new Color(165, 209, 252);

	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		int h = getHeight(), w = getWidth(), middle;
		if (color1 != null && color2 != null) {
			middle = h / 3 * 2;
			g2.setPaint(new GradientPaint(0, 0, color1, 0, middle, color2));
			g2.fillRect(0, 0, w, middle);
			g2.setColor(color2);
		} else {
			middle = h / 2;
			g2.setPaint(new GradientPaint(0, 0, c1, 0, middle, c2));
			g2.fillRect(0, 0, w, middle);
			g2.setPaint(new GradientPaint(0, middle, c3, 0, h, c4));
		}
		g2.fillRect(0, middle, w, h);
		g2.dispose();
		super.paintComponent(g);
	}

	@Override
	public boolean isOpaque() {
		return false;
	}

}

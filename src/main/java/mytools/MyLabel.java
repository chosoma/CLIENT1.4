package mytools;

import java.awt.AlphaComposite;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;

/**
 * 有上下渐变色的label
 *
 * @author black
 *
 */
public class MyLabel extends JLabel {
	private Color c1, c2;
	private float alpha = 1.0f;

	public MyLabel(String text) {
		super(text);
		this.init();
	}

	public MyLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		this.init();
	}

	private void init() {
		// this.setOpaque(true);
		c1 = MyUtil.LABEL_BACKGROUNDS[0];
		c2 = MyUtil.LABEL_BACKGROUNDS[1];
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		AlphaComposite composite = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, alpha);
		g2.setComposite(composite);
		g2.setPaint(new GradientPaint(0, 0, c1, 0, getHeight(), c2));
		g2.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

}

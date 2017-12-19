package mytools;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * 软件窗体顶部面板
 *
 * @author black
 *
 */
public class MyFramePanel extends JPanel {

	private Image image = null;
	private float alpha = 1.0f;

	private Color c1 = Color.WHITE, c2 = null;

	public MyFramePanel() {
		init();
	}

	public MyFramePanel(Image image, float alpha) {
		this.image = image;
		this.alpha = alpha;
		init();
	}

	public MyFramePanel(String imageURL, float alpha) {
		try {
			image = ImageIO.read(new File(imageURL));
		} catch (IOException e) {
			// TODO Auto-generated method stub
			// System.out.println(imageURL+"文件无法找到！");
			image = null;
		}
		this.alpha = alpha;
		init();
	}

	public MyFramePanel(Color c1, Color c2) {
		this.c1 = c1;
		this.c2 = c2;
		init();
	}

	public MyFramePanel(Color[] colors) {
		this.c1 = colors[0];
		this.c2 = colors[1];
		init();
	}

	public MyFramePanel(Color c1, float alpha) {
		this.c1 = c1;
		this.alpha = alpha;
		init();
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	private void init() {
		this.setOpaque(false);// 很必要
		// 希望它大小是自我调整的
		int width, height;
		if (image != null) {
			width = image.getWidth(null);
			height = image.getHeight(null);
		} else {
			width = Toolkit.getDefaultToolkit().getScreenSize().width;
			height = Toolkit.getDefaultToolkit().getScreenSize().height;
		}
		this.setSize(width, height);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();

		if (image != null) {
			// if (image.getWidth(null) > getWidth()) {
			// g2.drawImage(image, 0, 0, image.getWidth(null), getHeight(),
			// this);
			// } else {
			// g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
			// }
			g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);

		} else if (c1 != null) {
			if (c2 != null) {
				g2.setPaint(new GradientPaint(0, 0, c1, getWidth(),
						getHeight(), c2));
			} else {
				g2.setColor(c1);
			}
			g2.fillRect(0, 0, getWidth(), getHeight());
		}
		g2.dispose();
		super.paintComponent(g);
	}

}

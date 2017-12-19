package mytools;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;

public class MyButton extends JButton {

	// 按钮各种状态的组合色
	private Color[] pressColors, hoverColors;
	// 按钮内外边框颜色
	private Color outer;
	// 透明
	private float alpha = 1f;

	public MyButton() {
		super();
		this.init();
	}

	public MyButton(Icon icon) {
		this(null, icon, null);
	}

	public MyButton(String text) {
		this(text, null, null);
	}

	public MyButton(String text, Icon icon) {
		this(text, icon, null);
	}

	public MyButton(Icon icon, String toolTipText) {
		this(null, icon, toolTipText);
	}

	public MyButton(String text, String toolTipText) {
		this(text, null, toolTipText);
	}

	public MyButton(String text, Icon icon, String toolTipText) {
		super(text, icon);
		this.setToolTipText(toolTipText);
		this.init();
	}

	private void init() {
		// TODO Auto-generated method stub
		outer = MyUtil.BUTTON_OUTER_BORDER;// 外边框颜色

		pressColors = MyUtil.PressColors;// 鼠标按下时色
		hoverColors = MyUtil.HoverColors;// 鼠标悬浮时色

		// 取消绘制按钮内容区域
		this.setContentAreaFilled(false);
		// 设置按钮按下后无虚线框
		this.setFocusPainted(false);
		this.setFocusable(false);

	}

	// 提供一个可以更改背景色的方法
	public void setBackGroundColors(Color[] hoverColors, Color[] pressColors) {
		this.pressColors = pressColors;
		this.hoverColors = hoverColors;
	}

	@Override
	protected void paintComponent(Graphics g) {

		if (MyButton.this.isEnabled()) {
			ButtonModel model = MyButton.this.getModel();
			Color[] colors = null;
			if (model.isPressed()) {
				colors = pressColors;
				alpha = 0.4f;
			} else if (model.isRollover()) {
				colors = hoverColors;
				alpha = 0.6f;
			}

			if (colors != null) {
				Graphics2D g2 = (Graphics2D) g.create();
				// 绘制边框
				g2.setColor(outer);
				g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

				AlphaComposite composite = AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, alpha);
				g2.setComposite(composite);

				g2.setPaint(new GradientPaint(1, 1, colors[0], 1,
						getHeight() / 2, colors[1]));
				g2.fillRect(1, 1, getWidth() - 1, getHeight() / 2 - 1);
				g2.setPaint(new GradientPaint(1, getHeight() / 2, colors[2], 1,
						getHeight() - 1, colors[3]));
				g2.fillRect(1, getHeight() / 2, getWidth() - 1, getHeight() / 2);
			}
		}
		super.paintComponent(g);
	}

}

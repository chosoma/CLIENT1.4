package mytools;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;

import view.Shell;

public class MyUI {
	public static AlphaComposite AC = AlphaComposite.getInstance(
			AlphaComposite.SRC_OVER, 1.0f);
	private static Border MyButtonBorder, MyWindowBorder;
	public static Border LineBoder = BorderFactory.createLineBorder(new Color(
			44, 46, 54));

	public static Border getMyButtonBorder() {
		if (MyButtonBorder == null) {
			synchronized (MyUI.class) {
				if (MyButtonBorder == null) {
					MyButtonBorder = new MyUI.ButtonBorder();
				}
			}
		}
		return MyButtonBorder;
	}

	public static void drawDisabledBorder(Graphics g, int x, int y, int w, int h) {
		g.translate(x, y);
		g.setColor(MyUtil.InactiveControlTextColor);
		g.drawRect(0, 0, w - 1, h - 1);
		g.translate(-x, -y);
	}

	static Insets ButtonInsets = new Insets(1, 1, 3, 3);
	static Color ControlDarkShadow = new Color(32, 100, 202);

	static class ButtonBorder extends AbstractBorder implements UIResource {
		public void paintBorder(Component c, Graphics g, int x, int y, int w,
								int h) {
			Color color;
			if (c.isEnabled()) {
				color = ControlDarkShadow;
			} else {
				color = MyUtil.InactiveControlTextColor;
			}
			Graphics2D g2 = (Graphics2D) g.create();
			MyUtil.DrawShadow(g2, color, x, y, w, h);
			g2.dispose();

		}

		public Insets getBorderInsets(Component c) {
			return ButtonInsets;
		}

		public Insets getBorderInsets(Component c, Insets newInsets) {
			return ButtonInsets;
		}
	}

	public static Border getMyWindowBorder() {
		if (MyWindowBorder == null) {
			synchronized (MyUI.class) {
				if (MyWindowBorder == null) {
					MyWindowBorder = new MyUI.WindowBorder();
				}
			}
		}
		return MyWindowBorder;
	}

	static Insets WindowInsets = new Insets(3, 5, 7, 5);
	static Image LeftTop, Left, LeftBottom, Top, RightTop, Right, RightBottom,
			Bottom;
	static {
		try {
			LeftTop = ImageIO.read(MyUI.class.getResource("image/leftTop.png"));
			Left = ImageIO.read(MyUI.class.getResource("image/left.png"));
			LeftBottom = ImageIO.read(MyUI.class
					.getResource("image/leftBottom.png"));
			Top = ImageIO.read(MyUI.class.getResource("image/top.png"));
			RightTop = ImageIO.read(MyUI.class
					.getResource("image/rightTop.png"));
			Right = ImageIO.read(MyUI.class.getResource("image/right.png"));
			RightBottom = ImageIO.read(MyUI.class
					.getResource("image/rightBottom.png"));
			Bottom = ImageIO.read(MyUI.class.getResource("image/bottom.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	static class WindowBorder extends AbstractBorder implements UIResource {

		public void paintBorder(Component c, Graphics g, int x, int y, int w,
								int h) {
			Window window = getWindowForComponent(c);
			if (window instanceof Shell) {
				if (Shell.ShellState != Frame.NORMAL) {
					g.setColor(new Color(80, 100, 120));
					g.drawRect(x, y, w - 1, h - 1);
					return;
				}
			}
			g.translate(x, y);
			g.drawImage(LeftTop, 0, 0, 9, 9, c);
			g.drawImage(Left, 0, 9, 5, h - 18, c);
			g.drawImage(LeftBottom, 0, h - 9, 9, 9, c);
			g.drawImage(Top, 9, 0, w - 18, 3, c);
			g.drawImage(RightTop, w - 9, 0, 9, 9, c);
			g.drawImage(Right, w - 5, 9, 5, h - 18, c);
			g.drawImage(RightBottom, w - 9, h - 9, 9, 9, c);
			g.drawImage(Bottom, 9, h - 7, w - 18, 7, c);
		}

		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, null);
		}

		public Insets getBorderInsets(Component c, Insets newInsets) {
			Window window = getWindowForComponent(c);
			if (window instanceof Shell) {
				if (Shell.ShellState == Frame.NORMAL) {
					return WindowInsets;
				}
				return new Insets(1, 1, 1, 1);
			} else {// dialog
				return WindowInsets;
			}
		}
	}

	// 获取组件的window
	public static Window getWindowForComponent(Component c) {
		if (c instanceof Frame || c instanceof Dialog)
			return (Window) c;
		return getWindowForComponent(c.getParent());
	}

}

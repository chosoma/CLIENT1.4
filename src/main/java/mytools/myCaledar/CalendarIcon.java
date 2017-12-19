package mytools.myCaledar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;

public class CalendarIcon implements Icon, Serializable {

	Color borderColor = new Color(127, 157, 185);
	Color pressedColor = new Color(77, 97, 133);
	Color rolloverColor = new Color(98, 164, 214);
	Color enabledColor = new Color(155, 185, 245);

	/**
	 * Paints the horizontal bars for the
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (c.isEnabled()) {
			ButtonModel model = ((JButton) c).getModel();
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setColor(borderColor);
			g2.drawLine(0, 0, c.getWidth(), 0);
			g2.drawLine(c.getWidth() - 1, 0, c.getWidth() - 1,
					c.getHeight() - 1);
			g2.drawLine(0, c.getHeight() - 1, c.getWidth() - 1,
					c.getHeight() - 1);
			g2.translate(x, y);
			if (model.isRollover()) {
				if (model.isPressed()) {
					g2.setColor(pressedColor);
					g2.translate(1, 1);
				} else {
					g2.setColor(rolloverColor);
				}
			} else {
				g2.setColor(enabledColor);
			}
			for (int line = 0; line < getIconHeight(); line++) {
				g2.drawLine(line, line, 9 - line, line);
			}
			g2.dispose();
		}
	}

	/**
	 * Created a stub to satisfy the interface.
	 */
	public int getIconWidth() {
		return 10;
	}

	/**
	 * Created a stub to satisfy the interface.
	 */
	public int getIconHeight() {
		return 5;
	}

}

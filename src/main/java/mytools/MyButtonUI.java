package mytools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicButtonListener;
import javax.swing.plaf.basic.BasicButtonUI;

import sun.awt.AppContext;
import sun.swing.SwingUtilities2;

public class MyButtonUI extends BasicButtonUI {
	// NOTE: These are not really needed, but at this point we can't pull
	// them. Their values are updated purely for historical reasons.
	protected Color focusColor;
	protected Color selectColor;
	protected Color disabledTextColor;

	private static final Object METAL_BUTTON_UI_KEY = new Object();

	// ********************************
	// Create PLAF
	// ********************************
	public static ComponentUI createUI(JComponent c) {
		AppContext appContext = AppContext.getAppContext();
		MyButtonUI myButtonUI = (MyButtonUI) appContext
				.get(METAL_BUTTON_UI_KEY);
		if (myButtonUI == null) {
			myButtonUI = new MyButtonUI();
			appContext.put(METAL_BUTTON_UI_KEY, myButtonUI);
		}
		return myButtonUI;
	}

	// ********************************
	// Default Accessors
	// ********************************
	protected Color getSelectColor() {
		selectColor = UIManager.getColor(getPropertyPrefix() + "select");
		return selectColor;
	}

	protected Color getDisabledTextColor() {
		disabledTextColor = UIManager.getColor(getPropertyPrefix()
				+ "disabledText");
		return disabledTextColor;
	}

	protected Color getFocusColor() {
		focusColor = UIManager.getColor(getPropertyPrefix() + "focus");
		return focusColor;
	}

	// ********************************
	// Paint
	// ********************************
	/**
	 * If necessary paints the background of the component, then invokes
	 * <code>paint</code>.
	 * 
	 * @param g
	 *            Graphics to paint to
	 * @param c
	 *            JComponent painting on
	 * @throws NullPointerException
	 *             if <code>g</code> or <code>c</code> is null
	 * @see ComponentUI#update
	 * @see ComponentUI#paint
	 * @since 1.5
	 */
	public void update(Graphics g, JComponent c) {
		AbstractButton button = (AbstractButton) c;
		if ((c.getBackground() instanceof UIResource)
				&& button.isContentAreaFilled() && c.isEnabled()) {
			ButtonModel model = button.getModel();
			if (!(c.getParent() instanceof JToolBar)) {
				if (!model.isArmed() && !model.isPressed()) {
					paint(g, c);
					return;
				}
			} else if (model.isRollover()) {
				paint(g, c);
				return;
			}
		}
		super.update(g, c);
	}

	protected void paintButtonPressed(Graphics g, AbstractButton b) {
		if (b.isContentAreaFilled()) {
			Dimension size = b.getSize();
			g.setColor(getSelectColor());
			g.fillRect(0, 0, size.width, size.height);
		}
	}

	protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect,
			Rectangle textRect, Rectangle iconRect) {
		super.paintFocus(g, b, viewRect, textRect, iconRect);
		g.setColor(getFocusColor());
		if (b.getBorder() != null) {
			g.drawRect(2, 2, b.getWidth() - 6, b.getHeight() - 6);
		} else {
			g.drawRect(1, 1, b.getWidth() - 3, b.getHeight() - 3);
		}
	}

	protected void paintText(Graphics g, JComponent c, Rectangle textRect,
			String text) {
		AbstractButton b = (AbstractButton) c;
		ButtonModel model = b.getModel();
		FontMetrics fm = SwingUtilities2.getFontMetrics(c, g);
		int mnemIndex = b.getDisplayedMnemonicIndex();

		/* Draw the Text */
		if (model.isEnabled()) {
			/*** paint the text normally */
			g.setColor(b.getForeground());
		} else {
			/*** paint the text disabled ***/
			g.setColor(getDisabledTextColor());
		}
		SwingUtilities2.drawStringUnderlineCharAt(c, g, text, mnemIndex,
				textRect.x, textRect.y + fm.getAscent());
	}

}

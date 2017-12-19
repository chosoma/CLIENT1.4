package mytools.myCaledar;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class MyCalendar extends JPanel {
	private CalendarBody cb;
	private Popup pop;
	private JSpinner invoker;
	private AWTEventListener eventListener;
	private JButton arrowButton;

	public MyCalendar() {
		initDefault(Calendar.getInstance().getTime());

	}

	public MyCalendar(Date date) {
		initDefault(date);
	}

	private void initDefault(Date date) {
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.setOpaque(false);
		cb = new CalendarBody(date);
		invoker = cb.getInvoker();
		this.add(new JLabel("日期 "));
		this.add(invoker);

		arrowButton = new JButton(new CalendarIcon());
		arrowButton.setBorder(null);
		// 取消绘制按钮内容区域
		arrowButton.setContentAreaFilled(false);
		// 设置按钮按下后无虚线框
		arrowButton.setFocusPainted(false);
		arrowButton.setPreferredSize(new Dimension(16, 24));
		arrowButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showPopup((JButton) e.getSource());
			}
		});
		arrowButton.addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent event) {

			}

			public void ancestorRemoved(AncestorEvent event) {

			}

			// 只要祖先组件一移动,马上就让popup消失
			public void ancestorMoved(AncestorEvent event) {
				hidePopup();
			}
		});
		this.add(arrowButton);
	}

	private void showPopup(Component owner) {
		if (pop == null) {
			Point show = new Point(0, owner.getHeight());
			SwingUtilities.convertPointToScreen(show, owner);
			Date date = (Date) invoker.getValue();
			cb.setDate(date);
			pop = PopupFactory.getSharedInstance().getPopup(owner, cb, show.x,
					show.y);
			pop.show();
			Toolkit.getDefaultToolkit().addAWTEventListener(
					createAWTEventListener(), AWTEvent.MOUSE_EVENT_MASK);
		} else {
			hidePopup();
		}
	}

	private void hidePopup() {
		if (pop != null) {
			// 隐藏前将月份JCombobox弹出菜单隐藏
			cb.hideMonthPopup();
			pop.hide();
			pop = null;
			Toolkit.getDefaultToolkit().removeAWTEventListener(
					createAWTEventListener());
		}
	}

	public Date getValue() {
		return (Date) invoker.getValue();
	}

	private AWTEventListener createAWTEventListener() {
		if (eventListener == null) {
			eventListener = new AWTEventListener() {
				@Override
				public void eventDispatched(AWTEvent event) {
					MouseEvent me = (MouseEvent) event;
					switch (me.getID()) {
						case MouseEvent.MOUSE_PRESSED:
							if (me.getSource() == arrowButton) {
								return;
							}
							Point point = me.getLocationOnScreen();
							// 将鼠标点击屏幕坐标转换到CalendarBody中坐标
							SwingUtilities.convertPointFromScreen(point, cb);
							Rectangle bounds = cb.getBounds();
							if (bounds.contains(point)) {
								return;
							}
							hidePopup();
							break;
					}
				};
			};
		}
		return eventListener;
	}
}

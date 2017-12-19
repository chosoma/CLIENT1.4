package mytools.myCaledar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DateEditor;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CalendarBody extends JPanel {
	JSpinner yearChooser, invoker;
	JComboBox monthChooser;
	JLabel weekShow;
	DecimalFormat weekFormat = new DecimalFormat("00周");
	JPanel calendarPane;
	private Calendar selectedCal;
	private boolean updateLock = false;

	public CalendarBody() {
		selectedCal = Calendar.getInstance();
		getDate(selectedCal);
		init();
	}

	public CalendarBody(JSpinner invoker) {
		this.invoker = invoker;
		selectedCal = Calendar.getInstance();
		getDate(selectedCal);
		init();
	}

	public CalendarBody(Date date) {
		selectedCal = Calendar.getInstance();
		selectedCal.setTime(date);
		getDate(selectedCal);
		init();
	}

	private void init() {
		if (invoker == null) {
			invoker = new JSpinner();
			initInvoker(invoker);
		}
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(218, 205));
		this.setBorder(BorderFactory.createLineBorder(new Color(158, 158, 158),
				1));
		this.setBackground(Color.WHITE);
		this.add(createHead(), BorderLayout.NORTH);
		updateCalendar();
		this.add(calendarPane, BorderLayout.CENTER);
	}

	private JPanel createHead() {

		JPanel headPane = new JPanel(new BorderLayout());
		headPane.setOpaque(false);

		JPanel controlPane = new JPanel();
		controlPane.setBackground(new Color(205, 48, 12));
		controlPane.setPreferredSize(new Dimension(controlPane.getWidth(), 36));
		headPane.add(controlPane, BorderLayout.NORTH);

		yearChooser = new JSpinner(new SpinnerNumberModel(Calendar
				.getInstance().get(Calendar.YEAR), 1900, 2050, 1));
		NumberEditor editor = new NumberEditor(yearChooser, "####");
		JFormattedTextField jtf = editor.getTextField();
		jtf.setBackground(Color.WHITE);
		jtf.setEditable(false);
		yearChooser.setEditor(editor);
		yearChooser.setPreferredSize(new Dimension(50, 20));
		yearChooser.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (updateLock) {
					return;
				}
				// 如果是2月29号
				if (selectedCal.get(Calendar.MONTH) == Calendar.FEBRUARY
						&& selectedCal.get(Calendar.DAY_OF_MONTH) == 29) {
					// 直接设置到 ****年03月0号
					selectedCal.set((Integer) yearChooser.getValue(),
							Calendar.MARCH, 0);
				} else {
					selectedCal.set(Calendar.YEAR,
							(Integer) yearChooser.getValue());
				}
				updateCalendar();
			}
		});
		controlPane.add(yearChooser);
		controlPane.add(Box.createHorizontalStrut(2));

		DecimalFormat monthFormat = new DecimalFormat("00月");
		String[] months = new String[12];
		for (int i = 0; i < months.length; i++) {
			months[i] = monthFormat.format(i + 1);
		}
		monthChooser = new JComboBox(new DefaultComboBoxModel(months));

		monthChooser.setPreferredSize(new Dimension(50, 20));
		monthChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (updateLock) {
					return;
				}
				int day = selectedCal.get(Calendar.DAY_OF_MONTH);
				if (day > 28) {
					int year = selectedCal.get(Calendar.YEAR);
					int month = monthChooser.getSelectedIndex();
					// 获取选择月份的最大天数
					selectedCal.set(year, month, 1);
					int maxDay = selectedCal
							.getActualMaximum(Calendar.DAY_OF_MONTH);
					// 取小
					selectedCal.set(year, month, Math.min(day, maxDay));
				} else {
					selectedCal.set(Calendar.MONTH,
							monthChooser.getSelectedIndex());
				}
				updateCalendar();
			}
		});
		controlPane.add(monthChooser);
		controlPane.add(Box.createHorizontalStrut(2));

		weekShow = new JLabel("", JLabel.CENTER);
		weekShow.setForeground(Color.WHITE);
		controlPane.add(weekShow);
		controlPane.add(Box.createHorizontalStrut(4));

		JButton today = new JButton(new ImageIcon(this.getClass().getResource(
				"returnToday.png"))) {
			@Override
			protected void paintComponent(Graphics g) {
				if (model.isPressed()) {
					g.translate(1, 1);
				}
				super.paintComponent(g);
				if (model.isPressed()) {
					g.translate(-1, -1);
				}
			}
		};
		today.setFocusable(false);
		// 无边框
		today.setBorder(null);
		// 取消绘制按钮内容区域
		today.setContentAreaFilled(false);
		// 设置按钮按下后无虚线框
		today.setFocusPainted(false);
		// today = new MyButton2("今天");
		today.setPreferredSize(new Dimension(46, 24));
		today.setToolTipText("返回今天");
		today.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				returnToday();
			}
		});
		controlPane.add(today);

		JPanel titlePane = new JPanel();
		titlePane.setPreferredSize(new Dimension(titlePane.getWidth(), 22));
		titlePane.setOpaque(false);
		titlePane.setLayout(new GridLayout(1, 7, 1, 1));// 面板布局
		String[] titles = new String[] { "日", "一", "二", "三", "四", "五", "六" };
		for (String title : titles) {
			JLabel label = new JLabel(title, JLabel.CENTER);
			label.setForeground(Color.BLACK);
			titlePane.add(label);

		}

		headPane.add(titlePane, BorderLayout.SOUTH);
		return headPane;
	}

	private void updateCalendar() {
		if (calendarPane == null) {
			calendarPane = new JPanel();
			// calendarPane.setOpaque(false);
			calendarPane.setBackground(new Color(191, 191, 191));
			calendarPane.setPreferredSize(new Dimension(216, 145));
			calendarPane.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
		}

		calendarPane.removeAll();
		int weeks = selectedCal.getActualMaximum(Calendar.WEEK_OF_MONTH);// 周数
		int selectYear = selectedCal.get(Calendar.YEAR);// 年
		int selectMonth = selectedCal.get(Calendar.MONTH);// 月
		updateLock = true;
		yearChooser.setValue(selectYear);
		monthChooser.setSelectedIndex(selectMonth);
		updateLock = false;
		weekShow.setText(weekFormat.format(selectedCal
				.get(Calendar.WEEK_OF_MONTH)));// 周
		calendarPane.setLayout(new GridLayout(weeks, 7, 1, 1));// 面板布局
		// 今天
		Calendar today = Calendar.getInstance();
		getDate(today);

		Calendar temp = new GregorianCalendar(selectYear, selectMonth, 1);
		int weekDay = temp.get(Calendar.DAY_OF_WEEK);// 1号对应的星期值
		temp.add(Calendar.DAY_OF_MONTH, 1 - weekDay);// 该页日历第一天
		for (int i = 0; i < weeks * 7; i++) {
			DateLabel dateLabel;
			int day = temp.get(Calendar.DAY_OF_MONTH);
			if (i < weekDay - 1) {// 前一个月的日期
				dateLabel = new DateLabel(-1, day);
			} else if (temp.get(Calendar.MONTH) == selectMonth) {// 本月
				dateLabel = new DateLabel(0, day);
				if (temp.equals(selectedCal)) {
					dateLabel.setSelected(true);
				}
			} else {// 次月
				dateLabel = new DateLabel(1, day);
			}
			if (temp.equals(today)) {
				dateLabel.setToday();
			}
			calendarPane.add(dateLabel);
			temp.add(Calendar.DAY_OF_MONTH, 1);
		}
		invoker.setValue(selectedCal.getTime());
		calendarPane.repaint();
		calendarPane.revalidate();
	}

	// 返回今天
	private void returnToday() {
		Calendar today = Calendar.getInstance();
		getDate(today);

		// 如果当前选中日期是今天
		if (selectedCal.equals(today)) {
			return;
		}
		// 如果当前选中日期和今天是同年、月,就不重新绘制页面
		if (selectedCal.get(Calendar.YEAR) == today.get(Calendar.YEAR)
				&& selectedCal.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
			// 选中日期不是今天，重新绘制之前选中日期
			selectedCal = today;
			for (Component c : calendarPane.getComponents()) {
				DateLabel label = (DateLabel) c;
				if (label.isSelected()) {
					label.setSelected(false);
					label.repaint();
				}
				if (label.isToday()) {
					label.setSelected(true);
				}
			}
			invoker.setValue(selectedCal.getTime());
			return;
		}
		// 不同年月则重新绘制日历页
		selectedCal = today;
		updateCalendar();
	}

	public JSpinner getInvoker() {
		return invoker;
	}

	public void setInvoker(JSpinner invoker) {
		this.invoker = invoker;
		initInvoker(invoker);
	}

	private void initInvoker(JSpinner invoker) {
		Calendar c = new GregorianCalendar(1900, Calendar.JANUARY, 1);
		Date startDate = c.getTime();
		c.set(2050, Calendar.DECEMBER, 30);
		Date endDate = c.getTime();
		SpinnerDateModel model = new SpinnerDateModel(selectedCal.getTime(),
				startDate, endDate, Calendar.DAY_OF_MONTH);
		invoker.setModel(model);
		DateEditor editor = new DateEditor(invoker, "yyyy-MM-dd");
		JFormattedTextField dateJTF = editor.getTextField();
		dateJTF.setEditable(false);
		invoker.setEditor(editor);
		invoker.setPreferredSize(new Dimension(85, 24));
	}

	public void setDate(Date date) {
		selectedCal.setTime(date);
		getDate(selectedCal);
		updateCalendar();
	}

	public Date getValue() {
		return selectedCal.getTime();
	}

	class DateLabel extends JLabel {
		private int type;
		private boolean mouseEntered = false, selected = false,
				isToday = false;

		public DateLabel(int type, int day) {
			this.type = type;
			this.setText(day + "");
			this.setHorizontalAlignment(JLabel.CENTER);
			this.setOpaque(true);
			MouseAdapter mouseAdapter = createMouseAdapter();
			this.addMouseListener(mouseAdapter);
			this.addMouseMotionListener(mouseAdapter);
		}

		protected void paintComponent(Graphics g) {
			Color foreground, background;
			if (isToday) {// 今天
				foreground = Color.WHITE;
				background = new Color(205, 48, 12);
			} else {
				Border border = null;

				if (type == 0) {// 本月

					foreground = Color.BLACK;
					background = Color.WHITE;
				} else {// 非本月
					foreground = new Color(154, 154, 154);
					background = Color.WHITE;
				}
				if (mouseEntered || selected) {// 鼠标进入
					border = BorderFactory.createLineBorder(new Color(72, 118,
							255), 3);
				}
				setBorder(border);

			}
			setForeground(foreground);
			setBackground(background);
			super.paintComponent(g);
		}

		public boolean isToday() {
			return isToday;
		}

		public void setToday() {
			this.isToday = true;
		}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		private MouseAdapter createMouseAdapter() {
			return new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					mouseEntered = true;
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					if (isToday || selected) {
						return;
					}
					repaint();
				}

				@Override
				public void mouseExited(MouseEvent e) {
					mouseEntered = false;
					if (isToday || selected) {
						return;
					}
					repaint();
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// 如果重复点击一天
					if (selected) {
						return;
					}

					if (type == 0) {// 本月
						for (Component c : calendarPane.getComponents()) {
							DateLabel label = (DateLabel) c;
							if (label.isSelected()) {
								label.setSelected(false);
								// 如果不是今天，重新绘制
								if (!label.isToday()) {
									label.repaint();
								}
							}
						}
						selected = true;
						// 跟新选中日
						selectedCal.set(Calendar.DAY_OF_MONTH,
								Integer.valueOf(getText()));
						weekShow.setText(weekFormat.format(selectedCal
								.get(Calendar.WEEK_OF_MONTH)));
						invoker.setValue(selectedCal.getTime());
						repaint();
					} else {
						selectedCal.add(Calendar.MONTH, type);
						// 跟新选中日
						selectedCal.set(Calendar.DAY_OF_MONTH,
								Integer.valueOf(getText()));
						updateCalendar();
					}
				}
			};
		}
	}

	private void getDate(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
	}

	public void hideMonthPopup() {
		monthChooser.setPopupVisible(false);
	}

}

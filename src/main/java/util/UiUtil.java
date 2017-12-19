package util;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DateEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import mytools.MyTCR;
import mytools.MyTCR_CheckBox;
import mytools.MyUtil;
import sun.swing.SwingUtilities2;

public class UiUtil {


	/**
	 * 获取数字Spinner（天、时、分）
	 *
	 * @param value
	 * @param minimum
	 * @param maximum
	 * @param stepSize
	 * @return
	 */
	public static JSpinner createNumSpinner(int value, int minimum,
											int maximum, int stepSize) {
		JSpinner jsp = new JSpinner(new SpinnerNumberModel(value, minimum,
				maximum, stepSize));
		JFormattedTextField dayJTF = ((JSpinner.DefaultEditor) jsp.getEditor())
				.getTextField();
		dayJTF.setEditable(false);
		return jsp;
	}

	static Date startDate = Timestamp.valueOf("1970-01-01 00:00:00");
	static Date endDate = Timestamp.valueOf("2099-01-01 00:00:00");

	public static JSpinner createDateSpinner() {
		return createDateSpinner(null);
	}

	/**
	 * 获取不可编辑日期Spinner
	 *
	 * @param date
	 * @return
	 */
	public static JSpinner createDateSpinner(Date date) {
		SpinnerDateModel datemodel = new SpinnerDateModel(date, startDate,
				endDate, Calendar.SECOND);
		JSpinner jsp = new JSpinner(datemodel);
		DateEditor dateEditor = new JSpinner.DateEditor(jsp,
				MyUtil.DATA_FORMAT_PATTERN_2);
		JFormattedTextField dateJTF = dateEditor.getTextField();
		dateJTF.setEditable(false);
		jsp.setEditor(dateEditor);
		return jsp;
	}


	/**
	 * 获取不可编辑JTextField
	 *
	 * @return
	 */
	public static JTextField CreateDisTextField() {
		JTextField tf = new JTextField();
		tf.setEditable(false);
		return tf;
	}

	public static JTable getTable() {
		return getTable(null);
	}

	public static JTable getTable(DefaultTableModel model) {
		JTable table;
		if (model != null) {
			table = new JTable(model);
		} else {
			table = new JTable();
		}
		MyTCR tcr = new MyTCR();
		table.setDefaultRenderer(String.class, tcr);
		table.setDefaultRenderer(Number.class, tcr);
		table.setDefaultRenderer(Float.class, tcr);
//		table.setDefaultRenderer(Long.class, tcr);
//		table.setDefaultRenderer(Double.class, tcr);
		table.setDefaultRenderer(Date.class, tcr);
		table.setDefaultRenderer(Boolean.class, new MyTCR_CheckBox());
		// 表头设置
		JTableHeader tableHeader = table.getTableHeader();
		// tableHeader.setDefaultRenderer(new MyTCR_Header());
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader
				.getDefaultRenderer();
		hr.setHorizontalAlignment(SwingConstants.CENTER);
		Dimension dimension = hr.getSize();
		dimension.height = 24;
		hr.setPreferredSize(dimension);// 设置表头高度
		tableHeader.setDefaultRenderer(hr);
		// 表头不可拖动
		tableHeader.setReorderingAllowed(false);
		// 列宽不可修改
		tableHeader.setResizingAllowed(false);
		// 自动排序
		// table.setAutoCreateRowSorter(autoCreateRowSorter);
		table.setRowHeight(22);// 设置行高
		// 设置单行选中
		// table.setSelectionMode(selectionMode);
		// 设置列宽不可自动调整，显示水平滚动条
		// table.setAutoResizeMode(autoResizeMode);

		return table;

	}

	public static void setColumnWidth(JTable table, String name, int width) {
		TableColumn tableColumn = table.getColumn(name);
		tableColumn.setMaxWidth(width);
		tableColumn.setMinWidth(width);
		tableColumn.setPreferredWidth(width);
	}
}

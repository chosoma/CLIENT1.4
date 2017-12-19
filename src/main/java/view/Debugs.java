package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import data.FormatTransfer;

import mytools.MyUtil;

public class Debugs extends JPanel {

	private JTextArea jta = null;
	private String send = " 发→→ : ", rece = " →→收 : ";
	private String lineFeed = "\r\n";
	private boolean isShow = false;
	private SimpleDateFormat timeFormat = MyUtil.getDateFormat();
	JPopupMenu pop;
	JMenuItem copy = null, cut = null, delete = null, clear = null;

	boolean key_ctrl = false, key_x = false, key_bs = false,
			key_delete = false;

	private static Debugs DBShow = new Debugs();

	private Debugs() {
		this.setLayout(new BorderLayout());
		this.setOpaque(false);
		init();
	}

	public static Debugs getInstance() {
		return DBShow;
	}

	private void init() {
		pop = new JPopupMenu();
		copy = new JMenuItem("复制");
		copy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jta.copy();
			}
		});
		pop.add(copy);

		cut = new JMenuItem("剪切");
		cut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cutText();
			}
		});
		pop.add(cut);

		delete = new JMenuItem("删除");
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteText();
			}
		});
		pop.add(delete);

		clear = new JMenuItem("清空");
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jta.setText("");
			}
		});
		pop.add(clear);

		jta = new JTextArea();
		jta.setLineWrap(true);// 自动换行
		jta.setWrapStyleWord(true);// 断行不断字
		jta.setFont(MyUtil.FONT_14);
		jta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int buttonKey = arg0.getButton();
				// 双击清屏
				if (buttonKey == MouseEvent.BUTTON1
						&& arg0.getClickCount() == 2) {
					jta.setText(null);
				}
				if (buttonKey == MouseEvent.BUTTON3) {
					boolean visible = isSeleted();
					copy.setEnabled(visible);
					cut.setEnabled(visible);
					delete.setEnabled(visible);
					visible = !(jta.getText().equals(""));
					clear.setEnabled(visible);
					pop.show(jta, arg0.getX(), arg0.getY());
				}
			}
		});
		jta.setDocument(new PlainDocument() {
			public void insertString(int offs, String str, AttributeSet a)
					throws BadLocationException {
				try {
					int off = jta.getLineEndOffset(jta.getLineCount() - 1000);
					remove(0, off);
					super.insertString(offs - off, str, a);
				} catch (BadLocationException e) {
					// e.printStackTrace();
					super.insertString(offs, str, a);
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(jta);
		this.add(scrollPane, BorderLayout.CENTER);
	}

	public synchronized void rec(byte[] data, int len, String msg) {
		if (!isShow) {
			return;
		}
		jta.append(timeFormat.format(Calendar.getInstance().getTime()) + msg
				+ rece + FormatTransfer.getBufHexStr(data, 0, len) + lineFeed);
	}

	public synchronized void rec(byte[] data, int len, Date time, String msg) {
		if (!isShow) {
			return;
		}
		jta.append(timeFormat.format(time) + msg + rece
				+ FormatTransfer.getBufHexStr(data, 0, len) + lineFeed);
	}

	public synchronized void rec(byte[] data, Date time, String msg) {
		if (!isShow) {
			return;
		}
		jta.append(timeFormat.format(time) + msg + rece
				+ FormatTransfer.getBufHexStr(data) + lineFeed);
	}

	public synchronized void send(byte[] data, int len, String dtu) {
		if (!isShow) {
			return;
		}
		jta.append(timeFormat.format(Calendar.getInstance().getTime()) + dtu
				+ send + FormatTransfer.getBufHexStr(data, 0, len) + lineFeed);
	}

	public synchronized void showData(boolean isSend, byte[] data, int len) {
		if (!isShow) {
			return;
		}
		if (isSend) {
			jta.append(timeFormat.format(Calendar.getInstance().getTime())
					+ send + FormatTransfer.getBufHexStr(data, 0, len)
					+ lineFeed);
		} else
			jta.append(timeFormat.format(Calendar.getInstance().getTime())
					+ rece + FormatTransfer.getBufHexStr(data, 0, len)
					+ lineFeed);
	}

	public synchronized void showMsg(String s) {
		if (!isShow) {
			return;
		}
		jta.append(timeFormat.format(Calendar.getInstance().getTime()) + s
				+ lineFeed);
	}

	public void setShow(boolean b) {
		isShow = b;
		if (!b) {
			jta.setText(null);
		}
	}

	// 光标是否选中
	public boolean isSeleted() {
		boolean b = false;
		if (jta.getSelectedText() != null) {
			b = true;
		}
		return b;
	}

	// 剪切
	private void cutText() {
		if (isSeleted()) {
			jta.copy();
			jta.replaceRange("", jta.getSelectionStart(), jta.getSelectionEnd());
		}
	}

	// 删除
	private void deleteText() {
		if (isSeleted()) {
			jta.replaceRange("", jta.getSelectionStart(), jta.getSelectionEnd());
		}
	}

}

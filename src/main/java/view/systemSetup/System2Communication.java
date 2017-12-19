package view.systemSetup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mytools.MyButton3;
import mytools.MyUtil;

public class System2Communication extends JPanel {

	private JComboBox<String> modeComboBox;
	private JButton apply, cancel, edit;
	private CommGPRS commGPRS;
	private boolean isModifying = false;
	private UpdateInfo currentComm;
	private JPanel content;

	public System2Communication() {

		this.setLayout(null);
		this.setPreferredSize(new Dimension(500, 360));
		this.setBorder(MyUtil.Component_Border);
		this.initialize();
		this.initContent();
		this.initToolbar();
		this.loadInfo();// 加载配置信息
	}

	private void initialize() {
		JLabel styeLabel = new JLabel("通讯方式:", JLabel.RIGHT);
		styeLabel.setBounds(150, 25, 60, 20);

		modeComboBox = new JComboBox<String>(new String[] { "DTU" });
		modeComboBox.setBounds(220, 25, 130, 20);
		modeComboBox.setName("modeComboBox");
		this.add(styeLabel);
		this.add(modeComboBox);

	}

	private void initContent() {
		content = new JPanel(new BorderLayout());
		content.setBounds(0, 70, 500, 250);
		content.setBorder(MyUtil.Component_Border);
		this.add(content);
		commGPRS = new CommGPRS();
		content.add(commGPRS, BorderLayout.CENTER);
	}

	// 初始化工具栏
	private void initToolbar() {
		JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER));
		toolbar.setBounds(1, 320, 498, 39);
		this.add(toolbar);

		Dimension buttonSize = new Dimension(60, 30);

		edit = new MyButton3("修改", new ImageIcon("images/edit.png"));
		edit.setToolTipText("修改连接设置");
		edit.setPreferredSize(buttonSize);
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentComm.isEditable();
				modeComboBox.setEnabled(false);
				edit.setEnabled(false);
				apply.setEnabled(true);
				cancel.setEnabled(true);
				isModifying = true;
			}
		});
		toolbar.add(edit);

		apply = new MyButton3("应用", new ImageIcon("images/apply.png"));
		apply.setToolTipText("保存修改，并应用");
		apply.setName("apply");
		apply.setPreferredSize(buttonSize);
		apply.setEnabled(false);
		apply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					currentComm.saveInfo();
					modeComboBox.setEnabled(true);
					edit.setEnabled(true);
					cancel.setEnabled(false);
					apply.setEnabled(false);
					isModifying = false;
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "错误",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		toolbar.add(apply);

		cancel = new MyButton3("取消", new ImageIcon("images/cancel.png"));
		cancel.setToolTipText("取消修改");
		cancel.setEnabled(false);
		cancel.setPreferredSize(buttonSize);
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentComm.cancel();
				modeComboBox.setEnabled(true);
				edit.setEnabled(true);
				cancel.setEnabled(false);
				apply.setEnabled(false);
				isModifying = false;
			}
		});
		toolbar.add(cancel);

	}

	// 加载配置信息 "通讯方式"
	// private UpdateInfo currentComm,centerCard,content
	private void loadInfo() {
		currentComm = commGPRS;
	}

	public boolean isEditable() {
		return isModifying;
	}

	public void setEditable(boolean b) {
		edit.setEnabled(b);
	}

}

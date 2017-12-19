package view.systemSetup;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mytools.MyUtil;

import com.MyConfigure;

public class CommGPRS extends JPanel implements UpdateInfo {

	private JTextField portInput, portInput2;

	public CommGPRS() {
		this.setLayout(null);
		this.initialize();
		this.loadInfo();
	}

	private void initialize() {

		JLabel portLabel = new JLabel("服务器IP:", JLabel.RIGHT);
		portLabel.setBounds(100, 70, 120, 30);
		portLabel.setFont(MyUtil.FONT_20);
//		this.add(portLabel);

		portInput = new JTextField();
		portInput.setToolTipText("");
		portInput.setBounds(230, 70, 150, 30);
		portInput.setEnabled(false);
		portInput.setFont(MyUtil.FONT_20);
//		this.add(portInput);

		JLabel portLabel2 = new JLabel("服务器端口:", JLabel.RIGHT);
		portLabel2.setBounds(100, 120, 120, 30);
		portLabel2.setFont(MyUtil.FONT_20);
		this.add(portLabel2);

		portInput2 = new JTextField();
		portInput2.setToolTipText("端口:(1025~65535)");
		portInput2.setBounds(230, 120, 150, 30);
		portInput2.setEnabled(false);
		portInput2.setFont(MyUtil.FONT_20);
		this.add(portInput2);

	}

	// 加载配置信息
	private void loadInfo() {
		portInput.setText(MyConfigure.getAddr());
		portInput2.setText(MyConfigure.getLocalPort());
	}

	// 保存配置信息
	@Override
	public void saveInfo() throws Exception {
		String s = portInput.getText().trim();
		String s2 = portInput2.getText().trim();
		int j = Integer.parseInt(s2);
		String ipreg = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
		if(!s.matches(ipreg)){
			throw new Exception("IP 输入有误");
		}
		if (j < 1025 || j > 65535) {
			throw new Exception("端口号必须位于介于[ 1025 -- 65535 ]之间!!!");
		}
		MyConfigure.setLocalPort(portInput.getText(), portInput2.getText());
		portInput.setEnabled(false);
		portInput2.setEnabled(false);
	}

	@Override
	public void isEditable() {
		portInput.setEnabled(true);
		portInput2.setEnabled(true);
	}

	@Override
	public void cancel() {
		portInput.setEnabled(false);
		portInput2.setEnabled(false);
		this.loadInfo();
	}

}

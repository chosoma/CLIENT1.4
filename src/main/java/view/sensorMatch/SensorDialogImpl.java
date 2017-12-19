package view.sensorMatch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import service.SysNetService;
import service.SysUnitService;
import util.MyUtils;
import domain.SensorAttr;
import domain.SensorBean;

public class SensorDialogImpl extends SensorDialog {

	private JComboBox<String> jcbType, jcbDw;
	private JComboBox<Integer> jcbNetID;
	private JComboBox<Byte> jcbUnitid, jcbA, jcbB, jcbC, jcbAp,
			jcbBp, jcbCp;
	private JTextField jtfSjbh, jtfSwjz, jtfAlarm, jtfBz;
	private JLabel jlbUnitid, jlbSwjz;
	private JPanel panelABC, panelPoint;

	public SensorDialogImpl() {
		this.initDefault(null);
	}

	public SensorDialogImpl(SensorBean sensor) {
		this.sensor = sensor;
		initDefault(null);
	}

	protected JPanel initContent() {

		JPanel centerPanel = new JPanel(null);
		centerPanel.setSize(285, 280);

		JLabel jlb01 = new JLabel("*测值类型:", JLabel.LEFT);
		jlb01.setBounds(10, 13, 65, 20);
		centerPanel.add(jlb01);

		jcbType = new JComboBox<String>(SensorAttr.Sensor_Type);
		jcbType.setBounds(75, 15, 90, 20);
		jcbType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String type = (String) jcbType.getSelectedItem();
				if (type.equals(SensorAttr.Sensor_SW)) {
					jlbUnitid.setVisible(true);
					jcbUnitid.setVisible(true);
					jlbSwjz.setVisible(true);
					jtfSwjz.setVisible(true);
					panelABC.setVisible(false);
					jcbDw.setEnabled(true);
					jcbDw.setModel(new DefaultComboBoxModel<String>(
							SensorAttr.UNITS_SW));
				} else if (type.equals(SensorAttr.Sensor_SSJ)) {
					jlbUnitid.setVisible(true);
					jcbUnitid.setVisible(true);
					jlbSwjz.setVisible(false);
					jtfSwjz.setVisible(false);
					panelABC.setVisible(false);
					jcbDw.setEnabled(false);
					// 油压和SF6共用网关
					type = SensorAttr.Sensor_SF6;
				} else {
					jcbDw.setEnabled(true);
					jlbUnitid.setVisible(false);
					jcbUnitid.setVisible(false);
					jlbSwjz.setVisible(false);
					jtfSwjz.setVisible(false);
					panelABC.setVisible(true);
					if (type.equals(SensorAttr.Sensor_WD)) {
						jcbDw.setModel(new DefaultComboBoxModel<String>(
								new String[] { SensorAttr.UNIT_WD }));
						panelPoint.setVisible(true);
					} else {
						jcbDw.setModel(new DefaultComboBoxModel<String>(
								new String[] { SensorAttr.UNIT_MPa }));
						panelPoint.setVisible(false);
					}
				}
				Vector<Integer> netIDs = SysNetService.getNetNumbers((byte) 4);
				jcbNetID.setModel(new DefaultComboBoxModel<Integer>(netIDs));
				jcbNetID.setSelectedIndex(0);
			}
		});
		centerPanel.add(jcbType);

		JLabel jlMKH = new JLabel("*网关编号:", JLabel.LEFT);
		jlMKH.setBounds(10, 43, 65, 20);
		centerPanel.add(jlMKH);

		Vector<Integer> netIDs = SysNetService.getNetNumbers((byte) 4);
		jcbNetID = new JComboBox<Integer>(netIDs);
		jcbNetID.setBounds(75, 45, 60, 20);
		jcbNetID.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String type = (String) jcbType.getSelectedItem();
				Byte netid = (Byte) jcbNetID.getSelectedItem();
				if (netid != null) {
					if (type.equals(SensorAttr.Sensor_SW)) {
						jcbUnitid.setModel(new DefaultComboBoxModel<Byte>(
								new Byte[] { netid }));
					} else {
						Vector<Byte> unitids = SysUnitService.getUnitIds(type,
								netid);
						if (type.equals(SensorAttr.Sensor_SSJ)) {
							jcbUnitid.setModel(new DefaultComboBoxModel<Byte>(
									unitids));
						} else {
							if (type.equals(SensorAttr.Sensor_SF6)) {
								unitids.add(0, null);
							}
							jcbA.setModel(new DefaultComboBoxModel<Byte>(
									unitids));
							jcbB.setModel(new DefaultComboBoxModel<Byte>(
									unitids));
							jcbC.setModel(new DefaultComboBoxModel<Byte>(
									unitids));
						}
					}
				}
			}
		});
		centerPanel.add(jcbNetID);

		JLabel jlSJBH = new JLabel("*设计编号:", JLabel.LEFT);
		jlSJBH.setBounds(10, 73, 65, 20);
		centerPanel.add(jlSJBH);

		jtfSjbh = new JTextField();
		jtfSjbh.setBounds(75, 75, 200, 20);
		centerPanel.add(jtfSjbh);

		JLabel jlbBz = new JLabel(" 备   注:", JLabel.LEFT);
		jlbBz.setBounds(10, 103, 65, 20);
		centerPanel.add(jlbBz);

		jtfBz = new JTextField();
		jtfBz.setBounds(75, 105, 200, 20);
		centerPanel.add(jtfBz);

		JLabel jlDW = new JLabel("*单   位:", JLabel.LEFT);
		jlDW.setBounds(10, 133, 65, 20);
		centerPanel.add(jlDW);

		jcbDw = new JComboBox<String>(new String[] { SensorAttr.UNIT_MPa });
		jcbDw.setBounds(75, 135, 60, 20);
		centerPanel.add(jcbDw);

		JLabel jlbYjz = new JLabel(" 预 警 值:", JLabel.LEFT);
		jlbYjz.setBounds(10, 163, 65, 20);
		centerPanel.add(jlbYjz);

		jtfAlarm = new JTextField();
		jtfAlarm.setBounds(75, 165, 60, 20);
		centerPanel.add(jtfAlarm);

		jlbUnitid = new JLabel("*单元编号:", JLabel.LEFT);
		jlbUnitid.setBounds(10, 193, 65, 20);
		jlbUnitid.setVisible(false);
		centerPanel.add(jlbUnitid);

		jcbUnitid = new JComboBox<Byte>();
		jcbUnitid.setBounds(75, 195, 60, 20);
		jcbUnitid.setVisible(false);
		centerPanel.add(jcbUnitid);

		jlbSwjz = new JLabel("*水位基值:", JLabel.LEFT);
		jlbSwjz.setBounds(10, 223, 65, 20);
		jlbSwjz.setVisible(false);
		centerPanel.add(jlbSwjz);

		jtfSwjz = new JTextField();
		jtfSwjz.setBounds(75, 225, 60, 20);
		jtfSwjz.setVisible(false);
		centerPanel.add(jtfSwjz);

		panelABC = new JPanel(null);
		panelABC.setBounds(0, 193, 285, 82);
		centerPanel.add(panelABC);

		panelPoint = new JPanel(null);
		panelPoint.setBounds(175, 0, 110, 82);
		panelPoint.setVisible(false);
		panelABC.add(panelPoint);

		JLabel jlbA = new JLabel("A相   单元编号:", JLabel.LEFT);
		jlbA.setBounds(10, 0, 90, 20);
		panelABC.add(jlbA);

		jcbA = new JComboBox<Byte>();
		jcbA.setBounds(100, 2, 60, 20);
		panelABC.add(jcbA);

		JLabel jlbAp = new JLabel("*点号:", JLabel.LEFT);
		jlbAp.setBounds(0, 0, 40, 20);
		panelPoint.add(jlbAp);

		jcbAp = new JComboBox<Byte>(new Byte[] { 0, 1, 2, 3 });
		jcbAp.setBounds(40, 2, 60, 20);
		panelPoint.add(jcbAp);

		JLabel jlbB = new JLabel("B相   单元编号:", JLabel.LEFT);
		jlbB.setBounds(10, 30, 90, 20);
		panelABC.add(jlbB);

		jcbB = new JComboBox<Byte>();
		jcbB.setBounds(100, 32, 60, 20);
		panelABC.add(jcbB);

		JLabel jlbBp = new JLabel("*点号:", JLabel.LEFT);
		jlbBp.setBounds(0, 30, 40, 20);
		panelPoint.add(jlbBp);

		jcbBp = new JComboBox<Byte>(new Byte[] { 0, 1, 2, 3 });
		jcbBp.setBounds(40, 32, 60, 20);
		panelPoint.add(jcbBp);

		JLabel jlbC = new JLabel("C相   单元编号:", JLabel.LEFT);
		jlbC.setBounds(10, 60, 90, 20);
		panelABC.add(jlbC);

		jcbC = new JComboBox<Byte>();
		jcbC.setBounds(100, 62, 60, 20);
		panelABC.add(jcbC);

		JLabel jlbCp = new JLabel("*点号:", JLabel.LEFT);
		jlbCp.setBounds(0, 60, 40, 20);
		panelPoint.add(jlbCp);

		jcbCp = new JComboBox<Byte>(new Byte[] { 0, 1, 2, 3 });
		jcbCp.setBounds(40, 62, 60, 20);
		panelPoint.add(jcbCp);

		return centerPanel;
	}

	@Override
	protected SensorBean getSensorInfo() throws Exception {
		SensorBean newSensor = new SensorBean();
		// 测值类型
		String type = (String) jcbType.getSelectedItem();
		newSensor.setType(type);
		// 网关
		newSensor.setNetid((Byte) jcbNetID.getSelectedItem());
		// 设计编号
		String sjbh = jtfSjbh.getText().trim();
		if (MyUtils.isNull(sjbh)) {
			throw new Exception("[设计编号]未填写");
		}
		newSensor.setSjbh(sjbh);
		// 备注
		String bz = jtfBz.getText().trim();
		if (!bz.equals("")) {
			newSensor.setBz(bz);
		}
		if (type.equals(SensorAttr.Sensor_SW)) {
			// 水位基值
			try {
				float swjz = Float.valueOf(jtfSwjz.getText().trim());
				newSensor.setSwjz(swjz);
			} catch (NumberFormatException e) {
				throw new Exception("[水位基值]格式错误");
			}
			// 单元号
			byte a = (Byte) jcbUnitid.getSelectedItem();
			newSensor.setA(a);
			// 单位
			newSensor.setDw(jcbDw.getSelectedItem() + "");
		} else if (type.equals(SensorAttr.Sensor_SSJ)) {
			// 单元号
			byte a = (Byte) jcbUnitid.getSelectedItem();
			newSensor.setA(a);
		} else {
			// A、B、C 三相对应的单元号
			byte a = (Byte) jcbA.getSelectedItem();
			newSensor.setA(a);
			byte b = (Byte) jcbB.getSelectedItem();
			newSensor.setB(b);
			byte c = (Byte) jcbC.getSelectedItem();
			newSensor.setC(c);
			// 温度点号
			if (type.equals(SensorAttr.Sensor_WD)) {
				Byte ap = (Byte) jcbAp.getSelectedItem();
				newSensor.setAp(ap);
				Byte bp = (Byte) jcbBp.getSelectedItem();
				newSensor.setBp(bp);
				Byte cp = (Byte) jcbCp.getSelectedItem();
				newSensor.setCp(cp);
				newSensor.setDw(SensorAttr.UNIT_WD);
			} else {
				newSensor.setDw(SensorAttr.UNIT_MPa);
			}
		}
		// 预警值
		String y = jtfAlarm.getText().trim();
		if (!y.equals("")) {
			try {
				float alarm = Float.valueOf(y);
				newSensor.setAlarm(alarm);
			} catch (NumberFormatException e) {
				throw new Exception("[预警值]填写格式不正确");
			}
		}
		return newSensor;
	}

	@Override
	protected void loadSensorInfo() {
		// 测值类型
		String type = sensor.getType();
		jcbType.setSelectedItem(type);
		// 网关
		jcbNetID.setSelectedItem(sensor.getNetid());
		// 设计编号
		jtfSjbh.setText(sensor.getSjbh());
		// 备注
		String bz = sensor.getBz();
		if (!MyUtils.isNull(bz)) {
			jtfBz.setText(bz);
		}
		if (type.equals(SensorAttr.Sensor_SW)) {
			// 单元号
			jcbUnitid.setSelectedItem(sensor.getA());
			// 水位基值
			jtfSwjz.setText(sensor.getSwjz() + "");
		} else if (type.equals(SensorAttr.Sensor_SSJ)) {
			// 单元号
			jcbUnitid.setSelectedItem(sensor.getA());
		} else {
			Byte a = sensor.getA();
			if (a != null) {
				jcbA.setSelectedItem(a);
			}
			Byte b = sensor.getB();
			if (b != null) {
				jcbB.setSelectedItem(b);
			}
			Byte c = sensor.getC();
			if (c != null) {
				jcbC.setSelectedItem(c);
			}
			if (type.equals(SensorAttr.Sensor_WD)) {
				Byte ap = sensor.getAp();
				if (ap != null) {
					jcbAp.setSelectedItem(ap);
				}
				Byte bp = sensor.getBp();
				if (bp != null) {
					jcbBp.setSelectedItem(bp);
				}
				Byte cp = sensor.getCp();
				if (cp != null) {
					jcbCp.setSelectedItem(cp);
				}
			}
		}
		// 单位
		String dw = sensor.getDw();
		if (dw != null) {
			jcbDw.setSelectedItem(dw);
		}
		// 预警值
		Float alarm = sensor.getAlarm();
		if (alarm != null) {
			jtfAlarm.setText(alarm + "");
		}
	}

	@Override
	protected void clearSensorInfo() {
		jtfSjbh.setText(null);
		jtfSwjz.setText(null);
		jtfBz.setText(null);
		jtfAlarm.setText(null);
	}

}

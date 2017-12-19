package view.sensorMatch;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mytools.MyUtil;
import util.MyUtils;
import domain.SensorAttr;
import domain.SensorBean;

public class SensorView extends AbstractView {

	private JTextField jcbType, jcbNetID, jcbUnitid, jcbA, jcbB, jcbC, jcbAp,
			jcbBp, jcbCp, jcbDw;
	private JTextField jtfSjbh, jtfBz, jtfAlarm, jtfSwjz;
	private JLabel jlbUnitid, jlbSwjz;
	private JPanel panelABC, panelPoint;

	public SensorView() {
		this.setOpaque(false);
		this.setLayout(null);
		this.initTop();
	}

	private void initTop() {

		JLabel jlb01 = new JLabel("*测值类型:", JLabel.LEFT);
		jlb01.setBounds(10, 13, 65, 20);
		this.add(jlb01);

		jcbType = MyUtil.CreateTextField();
		jcbType.setBounds(75, 15, 90, 20);
		this.add(jcbType);

		JLabel jlMKH = new JLabel("*网关编号:", JLabel.LEFT);
		jlMKH.setBounds(10, 43, 65, 20);
		this.add(jlMKH);

		jcbNetID = MyUtil.CreateTextField();
		jcbNetID.setBounds(75, 45, 60, 20);
		this.add(jcbNetID);

		JLabel jlSJBH = new JLabel("*设计编号:", JLabel.LEFT);
		jlSJBH.setBounds(10, 73, 65, 20);
		this.add(jlSJBH);

		jtfSjbh = MyUtil.CreateTextField();
		jtfSjbh.setBounds(75, 75, 200, 20);
		this.add(jtfSjbh);

		JLabel jlbBz = new JLabel(" 备   注:", JLabel.LEFT);
		jlbBz.setBounds(10, 103, 65, 20);
		this.add(jlbBz);

		jtfBz = MyUtil.CreateTextField();
		jtfBz.setBounds(75, 105, 200, 20);
		this.add(jtfBz);

		JLabel jlDW = new JLabel("*单   位:", JLabel.LEFT);
		jlDW.setBounds(10, 133, 65, 20);
		this.add(jlDW);

		jcbDw = MyUtil.CreateTextField();
		jcbDw.setBounds(75, 135, 60, 20);
		this.add(jcbDw);

		JLabel jlbYjz = new JLabel(" 预 警 值:", JLabel.LEFT);
		jlbYjz.setBounds(10, 163, 65, 20);
		this.add(jlbYjz);

		jtfAlarm = MyUtil.CreateTextField();
		jtfAlarm.setBounds(75, 165, 60, 20);
		this.add(jtfAlarm);

		jlbUnitid = new JLabel("*单元编号:", JLabel.LEFT);
		jlbUnitid.setBounds(10, 193, 65, 20);
		jlbUnitid.setVisible(false);
		this.add(jlbUnitid);

		jcbUnitid = MyUtil.CreateTextField();
		jcbUnitid.setBounds(75, 195, 60, 20);
		jcbUnitid.setVisible(false);
		this.add(jcbUnitid);

		jlbSwjz = new JLabel("*水位基值:", JLabel.LEFT);
		jlbSwjz.setBounds(10, 223, 65, 20);
		jlbSwjz.setVisible(false);
		this.add(jlbSwjz);

		jtfSwjz = MyUtil.CreateTextField();
		jtfSwjz.setBounds(75, 225, 60, 20);
		jtfSwjz.setVisible(false);
		this.add(jtfSwjz);

		panelABC = new JPanel(null);
		panelABC.setBounds(0, 193, 285, 82);
		this.add(panelABC);

		panelPoint = new JPanel(null);
		panelPoint.setBounds(175, 0, 110, 82);
		panelPoint.setVisible(false);
		panelABC.add(panelPoint);

		JLabel jlbA = new JLabel("A相   单元编号:", JLabel.LEFT);
		jlbA.setBounds(10, 0, 90, 20);
		panelABC.add(jlbA);

		jcbA = MyUtil.CreateTextField();
		jcbA.setBounds(100, 2, 60, 20);
		panelABC.add(jcbA);

		JLabel jlbAp = new JLabel("*点号:", JLabel.LEFT);
		jlbAp.setBounds(0, 0, 40, 20);
		panelPoint.add(jlbAp);

		jcbAp = MyUtil.CreateTextField();
		jcbAp.setBounds(40, 2, 60, 20);
		panelPoint.add(jcbAp);

		JLabel jlbB = new JLabel("B相   单元编号:", JLabel.LEFT);
		jlbB.setBounds(10, 30, 90, 20);
		panelABC.add(jlbB);

		jcbB = MyUtil.CreateTextField();
		jcbB.setBounds(100, 32, 60, 20);
		panelABC.add(jcbB);

		JLabel jlbBp = new JLabel("*点号:", JLabel.LEFT);
		jlbBp.setBounds(0, 30, 40, 20);
		panelPoint.add(jlbBp);

		jcbBp = MyUtil.CreateTextField();
		jcbBp.setBounds(40, 32, 60, 20);
		panelPoint.add(jcbBp);

		JLabel jlbC = new JLabel("C相   单元编号:", JLabel.LEFT);
		jlbC.setBounds(10, 60, 90, 20);
		panelABC.add(jlbC);

		jcbC = MyUtil.CreateTextField();
		jcbC.setBounds(100, 62, 60, 20);
		panelABC.add(jcbC);

		JLabel jlbCp = new JLabel("*点号:", JLabel.LEFT);
		jlbCp.setBounds(0, 60, 40, 20);
		panelPoint.add(jlbCp);

		jcbCp = MyUtil.CreateTextField();
		jcbCp.setBounds(40, 62, 60, 20);
		panelPoint.add(jcbCp);

	}

	@Override
	public void clearInfo() {
		jcbType.setText(null);
		jcbNetID.setText(null);
		jcbUnitid.setText(null);
		jcbA.setText(null);
		jcbB.setText(null);
		jcbC.setText(null);
		jcbAp.setText(null);
		jcbBp.setText(null);
		jcbCp.setText(null);
		jcbDw.setText(null);
		jtfSjbh.setText(null);
		jtfSwjz.setText(null);
		jtfAlarm.setText(null);
		jtfBz.setText(null);
	}

	@Override
	public void setSensor(SensorBean sensor) {
		this.sensor = sensor;
		// 测值类型
		String type = sensor.getType();
		jcbType.setText(type);
		// 网关
		jcbNetID.setText(sensor.getNetid() + "");
		// 设计编号
		jtfSjbh.setText(sensor.getSjbh());
		// 备注
		String bz = sensor.getBz();
		if (!MyUtils.isNull(bz)) {
			jtfBz.setText(bz);
		}
		if (type.equals(SensorAttr.Sensor_SW)) {
			jlbUnitid.setVisible(true);
			jcbUnitid.setVisible(true);
			jlbSwjz.setVisible(true);
			jtfSwjz.setVisible(true);
			panelABC.setVisible(false);
			// 单元号
			jcbUnitid.setText(sensor.getA() + "");
			// 水位基值
			jtfSwjz.setText(sensor.getSwjz() + "");
		} else if (type.equals(SensorAttr.Sensor_SSJ)) {
			// 单元号
			jcbUnitid.setText(sensor.getA() + "");
			jlbUnitid.setVisible(true);
			jcbUnitid.setVisible(true);
			jlbSwjz.setVisible(false);
			jtfSwjz.setVisible(false);
			panelABC.setVisible(false);
		} else {
			jlbUnitid.setVisible(false);
			jcbUnitid.setVisible(false);
			jlbSwjz.setVisible(false);
			jtfSwjz.setVisible(false);
			panelABC.setVisible(true);
			Byte a = sensor.getA();
			if (a != null) {
				jcbA.setText(a + "");
			}
			Byte b = sensor.getB();
			if (b != null) {
				jcbB.setText(b + "");
			}
			Byte c = sensor.getC();
			if (c != null) {
				jcbC.setText(c + "");
			}
			if (type.equals(SensorAttr.Sensor_WD)) {
				panelPoint.setVisible(true);
				Byte ap = sensor.getAp();
				if (ap != null) {
					jcbAp.setText(ap + "");
				}
				Byte bp = sensor.getBp();
				if (bp != null) {
					jcbBp.setText(bp + "");
				}
				Byte cp = sensor.getCp();
				if (cp != null) {
					jcbCp.setText(cp + "");
				}
			} else {
				panelPoint.setVisible(false);
			}
		}
		// 单位
		jcbDw.setText(sensor.getDw());
		// 预警值
		Float alarm = sensor.getAlarm();
		if (alarm != null) {
			jtfAlarm.setText(alarm + "");
		}

	}
}

package view.sensorMatch;

import javax.swing.JPanel;

import domain.SensorBean;

public abstract class AbstractView extends JPanel {
	protected SensorBean sensor;

	public SensorBean getUnit() {
		return sensor;
	}

	/**
	 * 清空传感器资料信息
	 */
	public abstract void clearInfo();

	/**
	 * 设置传感器资料信息
	 *
	 * @param sensor
	 */
	public abstract void setSensor(SensorBean sensor);

}

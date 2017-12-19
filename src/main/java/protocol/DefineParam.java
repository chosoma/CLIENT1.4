package protocol;

/**
 * 参数类型
 *
 * @author black
 *
 */
public class DefineParam {

	// 所有类型
	public final static byte ALL_PARAMETER = (byte) 0x00;

	// 设备类型(Char)
	public final static byte EQUIPMENT_TYPE = (byte) 0x04;
	// 设备编号(Short)
	public final static byte EQUIPMENT_NUM = (byte) 0x05;
	// 波特率(Short)
	public final static byte BAUD_RATE = (byte) 0x06;
	// 内部时钟(Long)
	public final static byte CLOCK = (byte) 0x07;
	// 电池电压(Float)
	public final static byte BATTERY_VOLTAGE = (byte) 0x08;
	// 存储器中数据条数(Long)
	public final static byte DATA_COUNT = (byte) 0x09;
	// 离线数据起始时间(Long)
	public final static byte FIRST_TIME = (byte) 0x0A;
	// 离线采集周期(Long)，单位为秒
	public final static byte PERIOD = (byte) 0x0B;
	// 自报开关(Char)，离线采集后是否主动上报数据(0 关闭，1 打开)
	public final static byte REPORT_SWITCH = (byte) 0x0C;
	// 通讯预热时间(Long)，单位为秒 0x0D
	public final static byte PREHEAT_TIME = (byte) 0x0D;

	// 温阻类型(Char)，3 → 3K，2 → 2K
	public final static byte THERMISTOR = (byte) 0x80;
	// 各通道温度电阻选择（Long），3 → 3K，2 → 2K
	public final static byte THERMISTORS = (byte) 0x81;
	// 各通道工作选择（Char），Bit0 --通道1；Bit1 --通道2；Bit2 --通道3；Bit3 --通道4；
	// 其它位不用；对应通道位=1工作，=0表示不工作。
	public final static byte SELECTION = (byte) 0x82;

	// 激励类型(Char)
	public final static byte EXCITATION = (byte) 0x90;
	// 各通道激励类型选择(Long)
	public final static byte EXCITATIONS = (byte) 0x92;
}

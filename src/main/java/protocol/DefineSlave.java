package protocol;

/**
 * 下位机(设备)对上位机功能码
 *
 * @author black
 *
 */
public class DefineSlave {

	// 系统上电 - 数据项为空
	public final static byte POWER_ON = (byte) 0x00;
	// 设备故障 - 数据项为空
	public final static byte BROKEN = (byte) 0x04;
	// 确认成功 - 数据项为空
	public final static byte SUCCESS = (byte) 0x05;
	// 设备忙 - 数据项为空
	public final static byte BUSY = (byte) 0x06;
	// 内存为空 - 数据项为空
	public final static byte EMPTY = (byte) 0x09;
	// 读取参数返回
	public final static byte PARAMETER = (byte) 0x0A;
	// 读取数据返回
	public final static byte DATA_READ = (byte) 0x0B;
	// 实时自报数据返回 - 雨量、水位
	public final static byte DATA_REPORT = (byte) 0x0D;
	// 手动采集数据返回
	public final static byte DATA_MANUAL = (byte) 0x0E;
	// 存储记录数据返回
	public final static byte DATA_STORAGE = (byte) 0x0F;
	// 离线自报数据返回 - 实时自报没有收到的新的数据记录(多用于雨量)
	public final static byte DATA_OFFLINE_REPORT = (byte) 0x10;
	// 定时自报数据返回 - 离线采集周期+自报开关打开
	public final static byte DATA_TIME_REPORT = (byte) 0x11;

//	// 王敏下位机休眠功能码 - 数据项为空（私有）
//	public final static byte OFFLINE = (byte) 0xF0;

}

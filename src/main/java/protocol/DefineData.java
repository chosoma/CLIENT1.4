package protocol;

/**
 * 数据类型
 *
 * @author black
 *
 */
public class DefineData {
	// 所有数据
	public final static byte ALL_DATA = (byte) 0x00;
	// 时间(Long)
	public final static byte Time = (byte) 0x01;
	// 离线数据起始时间(Long)
	public final static byte Start_Time = (byte) 0x02;
	// 电池电压(Float)
	public final static byte Battery = (byte) 0x03;

	// 电池电压(Float)
	public final static byte Index = (byte) 0x04;
	// 电池电压(Float)
	public final static byte End_Time = (byte) 0x05;

	/********************** 王敏485 ********************/

	// 蒸发
	public final static byte ZF = (byte) 0x01;
	// 风速，单位 m/s
	public final static byte FS = (byte) 0x12;
	public final static byte FX = (byte) 0x13;
	// 雨量，单位 mm或脉冲
	public final static byte YL = (byte) 0x14;
	// 温度，单位 ℃
	public final static byte WD = (byte) 0x15;
	// 磁尺量水堰，单位 mm
	public final static byte CCLSY = (byte) 0x17;

	// 硅压扬压力计
	public final static byte GY = (byte) 0x21;

	// 对应编号的硅压
	public final static byte GY_50 = (byte) 0x50;
	public final static byte GY_51 = (byte) 0x51;

	// 对应编号的温度
	public final static byte WD_60 = (byte) 0x60;
	public final static byte WD_61 = (byte) 0x61;
}

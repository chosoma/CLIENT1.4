package protocol;

/**
 * 设备类型
 *
 * @author black
 *
 */
public class DefineEqpt {

	// 所有设备
	public final static byte ALL = (byte) 0x00;

	// 单点振弦模块
	public final static byte GDA1800 = (byte) 0x60;
	// 4点振弦模块
	public final static byte GDA1804 = (byte) 0x61;
	// 单点电压电流
	public final static byte GDA1900 = (byte) 0x1C;

	public static boolean idGnEqpt(byte b) {
		return b == GDA1800 || b == GDA1804 || b == GDA1900;
	}
}

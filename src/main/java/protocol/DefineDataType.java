package protocol;

/**
 * 数据项(8 bits)
 * 8 bits —— 3 bits | 5 bits , 3 bits表示数据类型(DATA_CHAR~~DATA_STRING), 5 bits表示数据内容长度
 *
 * @author black
 *
 */
public class DefineDataType {
	// Chart(1 Bytes)，0-255 ；数据项为0x01
	public final static byte DATA_CHAR = (byte) 0x00;
	// Short(2 Bytes)，无符号短整型，16进制数表示，高位在前 ；数据项为0x22
	public final static byte DATA_SHORT = (byte) 0x01;
	// Long(4 Bytes)，无符号整型，16进制数表示，高位在前 ；数据项为0x44
	public final static byte DATA_LONG = (byte) 0x02;
	// Float(4 Bytes)，采用IEEE754标准表示 ；数据项为0x64
	public final static byte DATA_FLOAT = (byte) 0x03;
	// String(≤31 Bytes)，字符串类型
	public final static byte DATA_STRING = (byte) 0x04;

	// 数据项简化使用，主要针对Char、Short、Long、Float
	public final static byte CHAR = (byte) 0x01;
	public final static byte SHORT = (byte) 0x22;
	public final static byte LONG = (byte) 0x44;
	public final static byte FLOAT = (byte) 0x64;

}

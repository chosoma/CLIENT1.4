package protocol;

public class Protocol {
	// 数据包头标识
	public static byte HEAD_MARK = (byte) 0xFF;
	// 最小数据长度
	public static int MIN_LEN = 9, MAX_LEN = 254;
	// 数据包表示长度的起始地址
	public static int LEN_OFF = 4;

}

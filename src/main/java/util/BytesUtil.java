package util;

public class BytesUtil {
	final static char[] HexChars = new char[] { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private BytesUtil() {
	}

	/**
	 * 将HEX显示的字节码转换成字节数组
	 *
	 */
	public static byte[] hex2Bytes(String s) {
		int len = s.length();
		byte[] ret = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			ret[i / 2] = hex2Byte(s.substring(i, i + 2));
		}
		return ret;
	}

	/**
	 * 将HEX显示的单个字节码转换成字节
	 *
	 */
	public static byte hex2Byte(String s) {
		return (byte) Integer.parseInt(s, 16);
	}

	/**
	 * 将字节数组转换成HEX显示的字节码
	 *
	 */
	public static String getHexStr(byte[] raw) {
		return getHexStr(raw, 0, raw.length);
	}

	/**
	 * 将字节数组转换成HEX显示的字节码
	 *
	 * @param b
	 *            需要转换的字节数组
	 * @param off
	 *            偏移量
	 * @param len
	 *            需要转换字节数的长度
	 */
	public static String getHexStr(byte[] b, int off, int len) {
		if (b == null) {
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * len);
		for (int i = off; i < off + len; i++) {
			hex.append(HexChars[(b[i] & 0xF0) >> 4])
					.append(HexChars[b[i] & 0x0F]).append(' ');
		}
		return hex.toString();
	}

}

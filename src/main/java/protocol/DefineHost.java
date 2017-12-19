package protocol;

/**
 * 上位机对下位机(设备)功能码
 *
 * @author black
 *
 */
public class DefineHost {
	/**
	 * 读取参数 - 当读取单个参数值时为要读取的参数值的参数类型；（0x00 为读取全部参数值）；
	 *         - 当读取若干个参数值时为要读取的所有参数值的参数类型组成的字节数组
	 */
	public final static byte READ_PARAMETER = (byte) 0x80;

	/**
	 * 读取数据 - 当读取单个数据值时为要读取的数据值的数据类型；（0x00 为读取全部数据值）；
	 *         - 当读取若干个数据值时为要读取的所有数据值的数据类型组成的字节数组
	 */
	public final static byte READ_DATA = (byte) 0x81;

	/**
	 * 设置参数 - 多个参数同时设置时将单个参 数设置数据项组合起来即可
	 */
	public final static byte SET_PARAMETER = (byte) 0x82;

	/**
	 * 数据采集 - 当地址码为0x0000 时，下位机只执行采集，不返回确认。
	 *         - 1、数据项为0x00：表示采集所有测点数据，下位机立即返回“确认成功”数据包，功能码为0x05
	 *         - 2、数据项为单采对应数据类型的数据，下位机收到后不立即返回，待数据采集完成后返回“读取数据返回”数据包，功能码为0x0B
	 */
	public final static byte COLLECT_DATA = (byte) 0x83;

	/**
	 * 清空存储器 - 数据项为空
	 */
	public final static byte CLEAR_MEMORY = (byte) 0x84;

	/**
	 * 读取存储器 - 数据项为0x00 时读取全部，为整形时间时读取该时间之后的数据
	 */
	public final static byte READ_MEMORY = (byte) 0x85;

	/**
	 * 使模块进入深度睡眠模式 - 关闭外部通讯电源，模块进入深度休眠模式，直到下一次离线采集
	 *                      - 数据项为空
	 */
	public final static byte SLEEP = (byte) 0x86;

	/**
	 * 使模块进入待机模式 - 默认模式
	 *                  - 数据项为空
	 */
	public final static byte STANDBY = (byte) 0x87;

}

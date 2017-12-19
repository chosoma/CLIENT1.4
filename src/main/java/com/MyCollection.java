package com;

import java.io.IOException;
import java.util.Date;

public interface MyCollection {
	// 1 校准时钟，2 读取存储器 ，3 手动采集， 4 定时设置
	byte CLOCK = 1, READ_STORAGE = 2, CLEAR_STORAGE = 3,
			COLLECT = 4, SET_PARA = 5, READ_DATA = 6;

	/**
	 * 打开链接
	 *
	 * @throws Exception
	 */
	void openConnection() throws Exception;

	/**
	 * 关闭连接
	 */
	void closeConnection() throws IOException;

	/**
	 * 内部时钟校准
	 *
	 * @throws Exception
	 */
	void correctClock(Byte mkh) throws Exception;

	/**
	 * 手动采集
	 *
	 * @param mkh
	 * @throws Exception
	 */
	void collect(Byte mkh) throws Exception;

	/**
	 * 读取并清空存储器
	 *
	 * @param mkh
	 * @throws Exception
	 */
	void readStorage(Byte mkh, Date time) throws Exception;

	/**
	 * 离线定时采集
	 *
	 * @throws Exception
	 */
	void applyOffline(Byte mkh, long jg) throws Exception;

	void clearStorage(Byte mkh) throws Exception;

}

package data;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import protocol.Protocol;

public class DataBuffer {
	//
//	private DataFactory dataFactory = DataFactory.getInstance();
	// 字节数据缓冲区
	private MyDataBuffer dataBuffer;
	// 缓冲区添加数据和移除数据的线程锁
	private Object dataLock = new Object();
	// 数据包表示长度的起始地址,最小、最大数据长度
	int len_off = Protocol.LEN_OFF, minLen = Protocol.MIN_LEN,
			maxLen = Protocol.MAX_LEN;

	// 数据处理线程
	private Thread dataThread;

	private Lock lock;
	private Condition con;

	public DataBuffer() {
		dataBuffer = new MyDataBuffer();
		lock = new ReentrantLock();
		con = lock.newCondition();
		start();
	}

	/**
	 * 将有效长度为length的数据添加到数据缓冲区
	 *
	 * @param data
	 *            数据
	 * @param length
	 *            有效数据长度
	 */
	public void receDatas(byte[] data, int length) {
		synchronized (dataLock) {
			dataBuffer.addRange(data, length);
		}
		// 如果数据处理线程waiting中
		if (dataThread.getState() == Thread.State.WAITING) {
			lock.lock();
			con.signal();
			lock.unlock();
		}
	}

	// 数据处理线程是否等待
	// public boolean isRunnable() {
	// return dataThread.getState() == Thread.State.RUNNABLE;
	// }

	/**
	 * 数据处理 ：另起一个线程对数据缓冲区的数据进行处理
	 */
	private void start() {
		if (dataThread != null) {
			if (dataThread.isAlive()) {
				return;
			}
		}
		dataThread = new Thread(new DataRunnable());
		dataThread.start();
	}

	/**
	 * 丢弃不可用数据，寻找一个数据头
	 */
	private void drop() {
		synchronized (dataLock) {
			boolean find = false;
			for (int i = 1; i < dataBuffer.size(); i++) {
				if (dataBuffer.get(i) == Protocol.HEAD_MARK) {
					dataBuffer.removeRange(0, i);
					find = true;
					break;
				}
			}
			// 如果没有找到"FF"则全部丢弃
			if (!find) {
				dataBuffer.clear();
				return;
			}
		}
	}

	/**
	 * 从缓冲区移除长度为length的字节
	 *
	 * @param length
	 */
	private void removeData(int length) {
		synchronized (dataLock) {
			dataBuffer.removeRange(0, length);
		}
	}
	private boolean alive = true;
	class DataRunnable implements Runnable {
		@Override
		public void run() {// 如果flag标志为true，则继续循环
			lock.lock();
			try {
				while (alive) {
					// 判断缓冲区数据是否达到最小数据长度
					if (dataBuffer.size() < minLen) {
						Thread.sleep(50);// 等待50毫秒
						// 判断缓冲区数据是否达到最小数据长度,如果没有，则保存数据
						if (dataBuffer.size() < minLen) {
							Thread.sleep(50);// 等待50毫秒
							// 判断缓冲区数据是否达到最小数据长度,如果没有则线程休眠
							if (dataBuffer.size() < minLen) {
//								dataFactory.saveData();// 数据存储
								con.await();
							}
							continue;
						}
					}
					// 判断数据包是不是"FF"打头,不是就丢弃
					if (dataBuffer.get(0) != Protocol.HEAD_MARK) {
						drop();
						continue;
					}

					// 单条数据长度
					int dataLength = FormatTransfer.getDataLen(
							dataBuffer.get(len_off),
							dataBuffer.get(len_off + 1));
					// 如果数据长度最小长度和最大长度之外，则丢弃
					if (dataLength < minLen || dataLength > maxLen) {
						drop();
						continue;
					}
					// 如果数据长度大于缓冲区长度
					if (dataLength > dataBuffer.size()) {
						continue;
					}

					byte[] data = dataBuffer.toArray(0, dataLength);
					// CRC16校验
					if (FormatTransfer.checkCRC16(data)) {
						// // 此处写有效数据的处理
//						dataFactory.processData(data);
						// 移除已处理数据
						removeData(dataLength);
					} else {
						drop();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				// 数据线程出错
			} finally {
				lock.unlock();
			}
		}
	}

	public void close() {
		alive = false;
		// 如果数据处理线程waiting中
		if (dataThread.getState() == Thread.State.WAITING) {
			lock.lock();
			con.signal();
			lock.unlock();
		}
	}

}

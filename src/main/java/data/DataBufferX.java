package data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import protocol.ProtocolX;

import service.SysUnitService;
import util.MyDecodeUitl;

import domain.RawDataX;

public class DataBufferX {
    //
    private DataFactory factory = DataFactory.getInstance();
    // 字节数据缓冲区
    private List<RawDataX> buffer;
    // 缓冲区添加数据和移除数据的线程锁
    private Object dataLock = new Object();
    // 数据包表示长度的起始地址,最小、最大数据长度
    int len_off = 5, minLen = 22;

    // 数据处理线程
    private Thread dataThread;

    private Lock lock;
    private Condition con;

    public DataBufferX() {
        buffer = new ArrayList<RawDataX>();
        lock = new ReentrantLock();
        con = lock.newCondition();
        start();
    }

    /**
     * 将有效长度为length的数据添加到数据缓冲区
     *
     * @param //数据
     * @param //   有效数据长度
     */
    public void receDatas(RawDataX rawData) {
        synchronized (dataLock) {
            buffer.add(rawData);
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

    private boolean alive = true;

    class DataRunnable implements Runnable {
        @Override
        public void run() {// 如果flag标志为true，则继续循环
            lock.lock();
            try {
                while (alive) {
                    // 判断缓冲区数据是否达到最小数据长度
                    if (buffer.size() == 0) {
                        Thread.sleep(50);// 等待50毫秒
                        // 判断缓冲区数据是否达到最小数据长度,如果没有，则保存数据
                        if (buffer.size() == 0) {
                            Thread.sleep(50);// 等待50毫秒
                            // 判断缓冲区数据是否达到最小数据长度,如果没有则线程休眠
                            if (buffer.size() == 0) {
                                factory.saveData();// 数据存储
                                con.await();
                            }
                            continue;
                        }
                    }
                    RawDataX rawdata = buffer.remove(0);
                    byte[] data0 = rawdata.getData();
                    Date time = rawdata.getTime();
                    // 逆转义
                    byte[] data = MyDecodeUitl.Decrypt(data0);
                    // 判断数据包是不是"7E"打头,不是就丢弃
                    if (data[0] != ProtocolX.HEAD) {
                        continue;
                    }
                    // // 小于最小长度
                    // if (data.length < minLen) {
                    // continue;
                    // }

                    // 单条数据内容长度
                    int dataLength = FormatTransfer.getDataLen(data[len_off], data[len_off + 1]);

                    if ((dataLength == ProtocolX.LenHeartR1 || dataLength == ProtocolX.LenHeartR2) && data[3] == ProtocolX.cmdHeartR) {// 心跳包
                        if (data.length >= (11 * 2)) {
                            byte[] data2 = new byte[data.length - 11];
                            System.arraycopy(data, 11, data2, 0, data.length - 11);
                            buffer.add(0, new RawDataX(data2, time));
                        }
                        continue;
                    } else if ((dataLength == ProtocolX.LenSetR) && data[3] == ProtocolX.cmdSetR) {// 设置应答
                        if (data.length >= (13 + 11)) {
                            byte[] data2 = new byte[data.length - 13];
                            System.arraycopy(data, 13, data2, 0, data.length - 13);
                            buffer.add(0, new RawDataX(data2, time));
                        }
                        byte unitType = data[8];
                        byte unitId = data[9];
//                        try {
//                            SysUnitService.setTb(data[1], data[2], unitType, unitId);
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
                        continue;
                    } else if ((dataLength == ProtocolX.LenMsgR) && data[3] == ProtocolX.cmdMsgR) {// 手机短信应答
                        if (data.length >= (21 + 11)) {
                            byte[] data2 = new byte[data.length - 21];
                            System.arraycopy(data, 21, data2, 0,
                                    data.length - 21);
                            buffer.add(0, new RawDataX(data2, time));
                        }
                        // TODO 手机短信应答对应的操作
                        continue;
                    }
                    // 数据长度小于单条数据长度
                    if (dataLength + 10 < data.length) {
                        continue;
                    } else if (dataLength + 10 == data.length && data[data.length - 1] == ProtocolX.TAIL && data[3] == ProtocolX.cmdDataR) {// 长度相等
                        // CRC16校验
                        if (FormatTransfer.checkCRC16_X(data)) {
                            // // 此处写有效数据的处理
                            factory.processData_X(data, time);
                        } else {
                            System.out.println("校验位错误");
                        }
                    } else {// 数据长度大于单条数据长度
                        byte[] data1 = new byte[dataLength + 10];
                        System.arraycopy(data, 0, data1, 0, data1.length);
                        byte[] data2 = new byte[data.length - data1.length];
                        buffer.add(0, new RawDataX(data2, time));
                        if (data[data.length - 1] == ProtocolX.TAIL && data[3] == ProtocolX.cmdDataR) {
                            // CRC16校验
                            if (FormatTransfer.checkCRC16_X(data1)) {
                                // // 此处写有效数据的处理
                                factory.processData_X(data1, time);
                            }
                        }
                    }
                }
//            } catch (InterruptedException e) {
            } catch (Exception e) {
                e.printStackTrace();
                // 数据线程出错
            } finally {
//                lock.unlock();
                run();
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

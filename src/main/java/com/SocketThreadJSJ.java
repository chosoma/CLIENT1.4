//package com;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.Socket;
//import java.net.SocketException;
//import java.util.Date;
//
//import protocol.DefineEqpt;
//import protocol.Protocol;
//import service.SysUnitService;
//import view.Debugs;
//import data.DataBuffer;
//import data.OrderFactory;
//import domain.SensorAttr;
//import domain.UnitBean;
//
//public class SocketThreadJSJ implements Runnable {
//
//    private Socket socket;
//    private UnitBean mkBean;
//    private OutputStream out;// socket输出流
//
//    private boolean dataRece = false;// 数据接受标志位
//    private Debugs debugShow = Debugs.getInstance();
//    private DataBuffer dataBuffer;// 数据工厂
//
//    public SocketThreadJSJ(Socket socket) {
//        this.socket = socket;
//        dataBuffer = new DataBuffer();
//    }
//
//    @Override
//    public void run() {
//        String offlineMSG = "";
//        try {
//            InputStream in = socket.getInputStream();
//            out = socket.getOutputStream();
//            // 接受数据超时
//            socket.setSoTimeout(CollectWLANJSJ.timeout);
//            byte[] b = new byte[1024];
//            int num = -1;
//            while ((num = in.read(b)) != -1) {
//                debugShow.rec(b, num, " " + socket.getPort());
//                byte dtu = b[3];
//                // 如果数据长度小于最小数据长度
//                if (num < Protocol.MIN_LEN) {
//                    // 如果是心跳包
//                    if (b[0] == Protocol.HEAD_MARK && b[1] == Protocol.HEAD_MARK) {
//                        if (mkBean == null) {
////                            mkBean = SysUnitService.getUnitBean(SensorAttr.Sensor_SW, dtu);
//                            if (mkBean == null) {
//                                break;
//                            }
//                            CollectWLANJSJ.getInstance().addSocket(this, dtu);
//                            if (!mkBean.isTb() || mkBean.isClock()) {
//                                byte[] msg = OrderFactory.clock_Modify(
//                                        mkBean.getUnitNumber(),
//                                        mkBean.getJgInSecs());
//                                sendMSG(msg, MyCollection.CLOCK);
//                            }
//                        }
//                        continue;
//                    } else {
//                        break;
//                    }
//                }
//
//                // 如果mkBean未初始化，（先收到数据的情况）
//                if (mkBean == null) {
//                    if (b[0] == Protocol.HEAD_MARK && DefineEqpt.idGnEqpt(b[1])) {
////                        mkBean = SysUnitService.getUnitBean(SensorAttr.Sensor_SW, dtu);
//                        // 如果是葛南设备，但是又没有模块资料的，这里不用断开连接
//                        if (mkBean != null) {
//                            dataRece = true;
//                            dataBuffer.receDatas(b, num);
//                            CollectWLANJSJ.getInstance().addSocket(this, dtu);
//                            if (!mkBean.isTb() || mkBean.isClock()) {
//                                byte[] msg = OrderFactory.clock_Modify(
//                                        mkBean.getUnitNumber(),
//                                        mkBean.getJgInSecs());
//                                sendMSG(msg, MyCollection.CLOCK);
//                            }
//                        }
//                    } else {
//                        break;
//                    }
//                } else {
//                    dataRece = true;
//                    dataBuffer.receDatas(b, num);
//                }
//            }// while is over
//            offlineMSG = " " + socket.getPort() + " 已下线";
//        } catch (SocketException e) {
//            offlineMSG = " " + socket.getPort() + " 接受数据超时,连接中断,port: ";
//        } catch (IOException e) {
//            offlineMSG = " " + socket.getPort() + " 连接已被关闭,port: ";
//        } finally {
//            try {
//                this.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            dataBuffer.close();
//            // 先移除，后对界面进行修改
//            CollectWLANJSJ.getInstance().removeSocket(this);
//            debugShow.showMsg(offlineMSG);
//        }
//
//    }
//
//    // 如果socket没有关闭
//    public void close() throws IOException {
//        if (!socket.isClosed()) {
//            socket.close();
//        }
//    }
//
//    public boolean isUseful() {
//        return !socket.isClosed() && socket.isConnected();
//    }
//
//    // 是否关闭
//    public boolean isClosed() {
//        return socket.isClosed();
//    }
//
//    public boolean isConnected() {
//        return socket.isConnected();
//    }
//
//    // 群采
//    public void collect() throws IOException {
//        byte[] standOnLine = OrderFactory.StartOnLine;
//        this.sendMSG(standOnLine, MyCollection.COLLECT);
//    }
//
//    // 发送指令
//    private synchronized void sendMSG(byte[] msg, byte orderType)
//            throws IOException {
//        try {
//            out.write(msg);
//            out.flush();
//            debugShow.send(msg, msg.length, mkBean.getNetidString());
//            mkBean.setOrderType(orderType);
//        } catch (IOException e) {
//            try {
//                this.close();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//            throw new IOException(mkBean.getNetidString() + " 发送命令时连接中断");
//        }
//    }
//
//    // 发送指令,并等待返回
//    private synchronized boolean sendMSG(byte[] msg, boolean isReadStorage)
//            throws IOException, InterruptedException {
//        this.sendMSG(msg, MyCollection.READ_STORAGE);
//        return dataRece(isReadStorage);
//    }
//
//    // 接受数据扽等待
//    private boolean dataRece(boolean isReadStorage) throws InterruptedException {
//        boolean b = dataRece = false;
//        int num1 = CollectWLANJSJ.delayReply;
//        while (num1-- > 0) {
//            Thread.sleep(CollectWLANJSJ.unitTime);
//            if (dataRece) {// 如果收到数据
//                b = dataRece;
//                break;
//            }
//        }
//        // 如果是读取存储器
//        if (isReadStorage) {
//            while (dataRece) {// 判断数据接受
//                dataRece = false;
//                Thread.sleep(CollectWLANJSJ.unitTime);// 每unitTime毫秒判断一次
//                int count = CollectWLANJSJ.delaySpace;
//                // 如果没有收到数据
//                while (!dataRece) {
//                    Thread.sleep(CollectWLANJSJ.unitTime);// 每unitTime毫秒判断一次
//                    if (--count == 0) {
//                        break;
//                    }
//                }
//            }
//        }
//        return b;
//    }
//
//    // 校准时钟
//    public void correctClock() throws IOException, InterruptedException {
//        long period = mkBean.getJgInSecs();
//        byte[] msg = OrderFactory.clock_Modify(mkBean.getUnitNumber(), period);
//        this.sendMSG(msg, MyCollection.CLOCK);
//    }
//
//    // 读取采集数据
//    public void readData() throws IOException, InterruptedException {
//        byte[] msg = OrderFactory.readData(mkBean.getUnitNumber());
//        this.sendMSG(msg, MyCollection.READ_DATA);
//    }
//
//    // 读取并清空存储器
//    public void readStorage(Date time) throws IOException, InterruptedException {
//        byte[] msg = OrderFactory.readStorage(mkBean.getUnitNumber(), time);
//        this.sendMSG(msg, true);
//
//    }
//
//    public void clearStorage() throws IOException, InterruptedException {
//        byte[] clear = OrderFactory.clearStorage(mkBean.getUnitNumber());
//        this.sendMSG(clear, MyCollection.CLEAR_STORAGE);
//    }
//
//    public byte getDTU() {
//        return mkBean.getGatewaynumber();
//    }
//
//    public UnitBean getUnitBean() {
//        return mkBean;
//    }
//}

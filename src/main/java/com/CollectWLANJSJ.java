//package com;
//
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import javax.swing.JOptionPane;
//
//import service.SysUnitService;
//import view.dataCollect.CollectOperate;
//import domain.UnitBean;
//
///**
// * jsj
// */
//public class CollectWLANJSJ implements MyCollection {
//
//    private ServerSocket ss;//服务器链接
//    private List<SocketThreadJSJ> listST = new ArrayList<SocketThreadJSJ>();//链接线程集
//
//    private Thread operateThread;
//    private String spoTip;
//
//    static int delayWait, delayReply, delaySpace, unitTime = 250, timeout;// 采集后等待读取时间，毫秒
//
//    private static CollectWLANJSJ Collect = new CollectWLANJSJ();
//
//    private CollectWLANJSJ() {
//        DelayBean delay = MyConfigure.getDelayGPRS();
//        delayWait = delay.getWait() / unitTime;
//        delayReply = delay.getReply() / unitTime;
//        delaySpace = delay.getSpace() / unitTime;
//        timeout = delay.getTimeout();
//    }
//
//    public static CollectWLANJSJ getInstance() {
//        return Collect;
//    }
//
//    /**
//     * 启动服务
//     */
//    @Override
//    public void openConnection() throws IOException {
//        int localPort = Integer.valueOf(MyConfigure.getAddr());
//        ss = new ServerSocket(localPort);
//        listST.clear();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    while (true) {
//                        Socket s = ss.accept();
//                        new Thread(new SocketThreadJSJ(s)).start();
//                    }
//                } catch (IOException e) {
//                    // e.printStackTrace();
//                } finally {
//                    // 关闭serverSocket
//                    if (!ss.isClosed()) {
//                        try {
//                            ss.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }).start();
//    }
//
//    /**
//     * 关闭服务
//     */
//    @Override
//    public void closeConnection() {
//        if (operateThread != null && operateThread.isAlive()) {
//            operateThread.interrupt();
//        }
//        spoTip = null;
//        try {
//            ss.close();
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//        // 关闭socket
//        for (int i = listST.size() - 1; i >= 0; i--) {
//            SocketThreadJSJ st = listST.get(i);
//            try {
//                st.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        listST.clear();
//    }
//
//    /**
//     * 时钟校准
//     *
//     * @throws Exception
//     */
//    @Override
//    public void correctClock(Byte mkh) throws Exception {
//
//        if (operateThread != null && operateThread.isAlive()) {
//            throw new Exception("正在进行" + operateThread.getName() + "，请稍候再试");
//        }
//        if (spoTip != null) {
//            throw new Exception(spoTip);
//        }
//        operateThread = new Thread(new Opration(CLOCK, mkh), "校准时钟");
//        operateThread.start();
//    }
//
//    /**
//     * 启动在线采集
//     */
//    @Override
//    public void collect(Byte mkh) throws Exception {
//        if (operateThread != null && operateThread.isAlive()) {
//            throw new Exception("正在进行" + operateThread.getName() + "，请稍候再试");
//        }
//        if (spoTip != null) {
//            throw new Exception(spoTip);
//        }
//        operateThread = new Thread(new Opration(COLLECT, mkh), "数据采集");
//        operateThread.start();
//    }
//
//    @Override
//    public void readStorage(Byte mkh, Date time) throws Exception {
//        if (mkh == null) {
//            return;
//        }
//        if (operateThread != null && operateThread.isAlive()) {
//            throw new Exception("正在进行" + operateThread.getName() + "，请稍候再试");
//        }
//        if (spoTip != null) {
//            throw new Exception(spoTip);
//        }
//        operateThread = new Thread(new Opration(mkh, time), "读取存储器");
//        operateThread.start();
//    }
//
//    @Override
//    public void clearStorage(Byte mkh) throws Exception {
//        if (mkh == null) {
//            return;
//        }
//        if (operateThread != null && operateThread.isAlive()) {
//            throw new Exception("正在进行" + operateThread.getName() + "，请稍候再试");
//        }
//        if (spoTip != null) {
//            throw new Exception(spoTip);
//        }
//        operateThread = new Thread(new Opration(CLEAR_STORAGE, mkh), "清空存储器");
//        operateThread.start();
//    }
//
//    /**
//     * 离线采集的配置信息修改
//     */
//    @Override
//    public void applyOffline(Byte mkh, long jg) throws Exception {
//        if (!CollectOperate.getInstance().isOpened(false)) {
//            return;
//        }
//        if (operateThread != null && operateThread.isAlive()) {
//            throw new Exception("正在进行" + operateThread.getName() + "，请稍候再试");
//        }
//        if (spoTip != null) {
//            throw new Exception(spoTip);
//        }
//        operateThread = new Thread(new Opration(SET_PARA, mkh), "定时采集设置");
//        operateThread.start();
//    }
//
//    private class Opration implements Runnable {
//        Byte order, mkh;// order: 1 校准时钟，2 读取存储器 ，3 手动采集， 4 定时设置
//        Date startTime;
//
//        Opration(Byte mkh, Date time) {
//            this.order = READ_STORAGE;
//            this.mkh = mkh;
//            this.startTime = time;
//        }
//
//        Opration(byte order, Byte mkh) {
//            this.order = order;
//            this.mkh = mkh;
//        }
//
//        @Override
//        public void run() {
//            if (listST.size() == 0) {
//                JOptionPane.showMessageDialog(null, "没有可用的连接", "失败",
//                        JOptionPane.INFORMATION_MESSAGE);
//                return;
//            }
//            synchronized (CollectWLANJSJ.this) {
//                try {
//                    switch (order) {
//                        case CLOCK:// 1 校准时钟
//                            Date timeT0 = new Date();
//                            if (mkh == null) {// 校准全部时钟
//                                for (int i = 0; i < listST.size(); i++) {
//                                    SocketThreadJSJ st = listST.get(i);
//                                    if (st.isUseful()) {
//                                        try {
//                                            st.correctClock();
//                                        } catch (IOException e) {
//                                            // e.printStackTrace();
//                                            listST.remove(i);
//                                            i--;
//                                        }
//                                    } else {
//                                        listST.remove(i);
//                                        i--;
//                                    }
//                                }
//                                Thread.sleep(MyConfigure.getDelayGPRS().getReply());
//                                List<Byte> mkhs = SysUnitService.getSures(timeT0);
//                                if (mkhs.size() == 0) {
//                                    JOptionPane
//                                            .showMessageDialog(null, "模块校准失败",
//                                                    "校准时钟",
//                                                    JOptionPane.INFORMATION_MESSAGE);
//                                } else {
//                                    JOptionPane.showMessageDialog(null, "模块 "
//                                                    + mkhs + " 已校准", "校准时钟",
//                                            JOptionPane.INFORMATION_MESSAGE);
//                                }
//                                return;
//                            }
//                            UnitBean bean = null;
//                            for (int i = 0; i < listST.size(); i++) {
//                                SocketThreadJSJ st = listST.get(i);
//                                if (st.isUseful()) {
//                                    if (st.getDTU() == mkh) {
//                                        try {
//                                            st.correctClock();
//                                            bean = st.getUnitBean();
//                                            break;
//                                        } catch (IOException e) {
//                                            // e.printStackTrace();
//                                            listST.remove(i);
//                                            i--;
//                                        }
//                                    }
//                                } else {
//                                    listST.remove(i);
//                                    i--;
//                                }
//                            }
//                            if (bean == null) {// 如果该模块不在线
//                                JOptionPane.showMessageDialog(null, "模块[ " + mkh
//                                                + " ]不在线,操作失败", "校准时钟",
//                                        JOptionPane.INFORMATION_MESSAGE);
//                            } else {
//                                Thread.sleep(MyConfigure.getDelayGPRS().getReply());
//                                String tip = null;
//                                Date t = bean.getTime();
//                                if (t != null && t.after(timeT0)) {
//                                    tip = "时钟已校准";
//                                } else
//                                    tip = "时钟校准失败";
//                                JOptionPane.showMessageDialog(null, tip, "校准时钟",
//                                        JOptionPane.INFORMATION_MESSAGE);
//                            }
//                            break;
//                        case READ_STORAGE:// 2 读取存储器
//                            UnitBean bean1 = null;
//                            for (int i = 0; i < listST.size(); i++) {
//                                SocketThreadJSJ st = listST.get(i);
//                                if (st.isUseful()) {
//                                    if (st.getDTU() == mkh) {
//                                        try {
//                                            st.readStorage(startTime);
//                                            bean1 = st.getUnitBean();
//                                            break;
//                                        } catch (IOException e) {
//                                            // e.printStackTrace();
//                                            listST.remove(i);
//                                            i--;
//                                        }
//                                    }
//                                } else {
//                                    listST.remove(i);
//                                    i--;
//                                }
//                            }
//                            if (bean1 == null) {// 如果该模块不在线
//                                JOptionPane.showMessageDialog(null, "模块[ " + mkh
//                                                + " ]不在线,操作失败", "校准时钟",
//                                        JOptionPane.INFORMATION_MESSAGE);
//                            }
//                            break;
//                        case CLEAR_STORAGE:// 3 清空存储器
//                            UnitBean bean2 = null;
//                            for (int i = listST.size() - 1; i >= 0; i--) {
//                                SocketThreadJSJ st = listST.get(i);
//                                if (st.isUseful()) {
//                                    if (st.getDTU() == mkh) {
//                                        try {
//                                            st.clearStorage();
//                                            bean2 = st.getUnitBean();
//                                            break;
//                                        } catch (IOException e) {
//                                            // e.printStackTrace();
//                                            listST.remove(i);
//                                            i--;
//                                        }
//                                    }
//                                } else {
//                                    listST.remove(i);
//                                    i--;
//                                }
//                            }
//                            if (bean2 == null) {// 如果该模块不在线
//                                JOptionPane.showMessageDialog(null, "模块[ " + mkh
//                                                + " ]不在线,操作失败", "校准时钟",
//                                        JOptionPane.INFORMATION_MESSAGE);
//                            }
//                            break;
//                        case COLLECT:// 4 手动采集
//                            if (mkh == null) {// 全部采集
//                                for (int i = 0; i < listST.size(); i++) {
//                                    SocketThreadJSJ st = listST.get(i);
//                                    if (st.isUseful()) {
//                                        try {
//                                            st.collect();
//                                        } catch (IOException e) {
//                                            // e.printStackTrace();
//                                            listST.remove(i);
//                                            i--;
//                                        }
//                                    } else {
//                                        listST.remove(i);
//                                        i--;
//                                    }
//                                }
//
//                                Thread.sleep(MyConfigure.getDelayGPRS().getWait());
//                                for (int i = 0; i < listST.size(); i++) {
//                                    SocketThreadJSJ st = listST.get(i);
//                                    if (st.isUseful()) {
//                                        try {
//                                            st.readData();
//                                        } catch (IOException e) {
//                                            // e.printStackTrace();
//                                            listST.remove(i);
//                                            i--;
//                                        }
//                                    } else {
//                                        listST.remove(i);
//                                        i--;
//                                    }
//                                }
//                                return;
//                            }
//                            // 采集一个模块
//                            UnitBean bean3 = null;
//                            for (int i = listST.size() - 1; i >= 0; i--) {
//                                SocketThreadJSJ st = listST.get(i);
//                                if (st.isUseful()) {
//                                    if (st.getDTU() == mkh) {
//                                        try {
//                                            st.collect();
//                                            bean3 = st.getUnitBean();
//                                            Thread.sleep(MyConfigure.getDelayGPRS()
//                                                    .getWait());
//                                            st.readData();
//                                            break;
//                                        } catch (IOException e) {
//                                            // e.printStackTrace();
//                                            listST.remove(i);
//                                            i--;
//                                        }
//                                    }
//                                } else {
//                                    listST.remove(i);
//                                    i--;
//                                }
//                            }
//                            if (bean3 == null) {// 如果该模块不在线
//                                JOptionPane.showMessageDialog(null, "模块[ " + mkh
//                                                + " ]不在线,操作失败", "校准时钟",
//                                        JOptionPane.INFORMATION_MESSAGE);
//                            }
//                            break;
//                        case SET_PARA:// 5 定时设置
//                        default:
//                            if (mkh == null) {// 全部设置
//                                for (int i = 0; i < listST.size(); i++) {
//                                    SocketThreadJSJ st = listST.get(i);
//                                    if (st.isUseful()) {
//                                        try {
//                                            st.correctClock();
//                                        } catch (IOException e) {
//                                            // e.printStackTrace();
//                                            listST.remove(i);
//                                            i--;
//                                        }
//                                    } else {
//                                        listST.remove(i);
//                                        i--;
//                                    }
//                                }
//                                return;
//                            }
//                            // 设置一个模块
//                            UnitBean bean4 = null;
//                            for (int i = listST.size() - 1; i >= 0; i--) {
//                                SocketThreadJSJ st = listST.get(i);
//                                if (st.isUseful()) {
//                                    if (st.getDTU() == mkh) {
//                                        try {
//                                            st.correctClock();
//                                            bean4 = st.getUnitBean();
//                                            break;
//                                        } catch (IOException e) {
//                                            // e.printStackTrace();
//                                            listST.remove(i);
//                                            i--;
//                                        }
//                                    }
//                                } else {
//                                    listST.remove(i);
//                                    i--;
//                                }
//                            }
//                            if (bean4 == null) {// 如果该模块不在线
//                                JOptionPane.showMessageDialog(null, "模块[ " + mkh
//                                                + " ]不在线,操作失败", "校准时钟",
//                                        JOptionPane.INFORMATION_MESSAGE);
//                            }
//                            break;
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    // 移除Socket
//    public synchronized void removeSocket(SocketThreadJSJ st) {
//        listST.remove(st);
//        for (int i = 0; i < listST.size(); i++) {
//            SocketThreadJSJ stt = listST.get(i);
//            if (stt.isClosed() || !stt.isConnected()) {
//                listST.remove(i);
//                i--;
//            }
//        }
//    }
//
//    // 添加Socket，并移除已存在相同dtu编号的Socket
//    public synchronized void addSocket(SocketThreadJSJ st, byte dtu) {
//        listST.add(st);
//        for (int i = 0; i < listST.size(); i++) {
//            SocketThreadJSJ stt = listST.get(i);
//            if (stt.isClosed() || !stt.isConnected()) {
//                listST.remove(i);
//                i--;
//            }
//        }
//    }
//}

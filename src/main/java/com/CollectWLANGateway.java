package com;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import domain.NetBean;
import service.SysUnitService;
import domain.UnitBean;

/**
 * getway
 */

public class CollectWLANGateway {

    private ServerSocket ss;
    private List<SocketThreadGateway> listST = new ArrayList<SocketThreadGateway>();

    private Thread operateThread;

    static int delayWait, delayReply, delaySpace, unitTime = 250, timeout;// 采集后等待读取时间，毫秒

    private static CollectWLANGateway Collect = new CollectWLANGateway();

    private CollectWLANGateway() {
        DelayBean delay = MyConfigure.getDelayGPRS();
        delayWait = delay.getWait() / unitTime;
        delayReply = delay.getReply() / unitTime;
        delaySpace = delay.getSpace() / unitTime;
        timeout = delay.getTimeout();
    }

    public static CollectWLANGateway getInstance() {
        return Collect;
    }

    /**
     * 启动服务
     */
    Socket s;

    public void openConnection() throws IOException {

        int localPort = Integer.valueOf(MyConfigure.getLocalPort());
//        ss = new ServerSocket(localPort);
        listST.clear();


        String addr = MyConfigure.getAddr();
        s = new Socket(addr, localPort);
        System.out.println(s);
        System.out.println(s.isConnected());
        new Thread(new SocketThreadGateway(s)).start();


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    while (true) {
//                        Socket s = ss.accept();
//                        new Thread(new SocketThreadGateway(s)).start();
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
    }

    //
//    /**
//     * 关闭服务
//     */
    public void closeConnection() {
        if (operateThread != null && operateThread.isAlive()) {
            operateThread.interrupt();
        }
        try {
            s.close();
//            ss.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        // 关闭socket
        for (int i = listST.size() - 1; i >= 0; i--) {
            SocketThreadGateway st = listST.get(i);
            try {
                st.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        listST.clear();
    }

    // 移除Socket
    public synchronized void removeSocket(SocketThreadGateway st) {
        listST.remove(st);
        for (int i = 0; i < listST.size(); i++) {
            SocketThreadGateway s = listST.get(i);
            if (s.isClosed() || !s.isConnected()) {
                listST.remove(i);
                i--;
            }
        }
    }

    // 添加Socket，并移除关闭和未连接的
    public synchronized void addSocket(SocketThreadGateway st) {
        listST.add(st);
        for (int i = 0; i < listST.size(); i++) {
            SocketThreadGateway s = listST.get(i);
            if (s.isClosed() || !s.isConnected()) {
                listST.remove(i);
                i--;
            }
        }
    }

    public void setAlarm(NetBean net, byte time) {
        for (int i = 0; i < listST.size(); i++) {
            SocketThreadGateway s = listST.get(i);
            if (s.isUseful()) {
                if (net.getType() == s.getNetType() && net.getNumber() == s.getNetId()) {
                    try {
                        s.setAlarm(time);
                    } catch (IOException e) {
                        // e.printStackTrace();
                        listST.remove(i);
                        i--;
                    }
                }
            } else {
                listST.remove(i);
                i--;
            }
        }

    }

    public void applyOffline(String name, Byte unitnumber, byte jg) {
        if (unitnumber == null) {
            Vector<UnitBean> beans = SysUnitService.getUnitBeans(name);
            if (beans.size() <= 0) {
                return;
            }
            for (UnitBean unitBean : beans) {
                applyOffline(name, unitBean.getNumber(), jg);
            }
        } else {
            UnitBean unitBean = SysUnitService.getUnitBean(name, unitnumber);
            for (int i = 0; i < listST.size(); i++) {
                SocketThreadGateway s = listST.get(i);
                if (s.isUseful()) {
                    if (unitBean.getGatewaytype() == s.getNetType() && unitBean.getGatewaynumber() == s.getNetId()) {
                        try {
                            s.setJg(unitBean, jg);
                            // break;
                        } catch (IOException e) {
                            // e.printStackTrace();
                            listST.remove(i);
                            i--;
                        }
                    }
                } else {
                    listST.remove(i);
                    i--;
                }
            }
        }
    }
}

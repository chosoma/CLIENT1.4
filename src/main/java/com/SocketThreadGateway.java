package com;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

import domain.UnitBean;
import protocol.ProtocolX;
import service.SysNetService;
import util.MyDecodeUitl;
import view.Debugs;
import data.DataBufferX;
import data.FormatTransfer;
import domain.NetBean;
import domain.RawDataX;
import domain.WdPeriod;

import javax.swing.*;

public class SocketThreadGateway implements Runnable {

    private Socket socket;
    private InputStream in;// socket输入流
    private OutputStream out;// socket输出流

    private Debugs debugShow = Debugs.getInstance();
    private DataBufferX dataFactory;// 数据工厂

    private Byte netType, netID;
    private NetBean netBean;

    public SocketThreadGateway(Socket socket) {
        this.socket = socket;
        dataFactory = new DataBufferX();
    }

    @Override
    public void run() {
        String offlineMSG = "";
        try {
            offlineMSG = "连接成功";
            debugShow.showMsg(offlineMSG + socket.getPort());
            System.out.println("链接成功");
            in = socket.getInputStream();
            out = socket.getOutputStream();
            byte[] bytes = new byte[4];
            bytes[0] = 0x7d;
            bytes[3] = 0x7e;
            out.write(bytes);
            // 接受数据超时
            byte[] b = new byte[1024];
            int num = -1;
            while ((num = in.read(b)) != -1) {
                System.out.println("接受数据:" + num);
                Date time = Calendar.getInstance().getTime();
                debugShow.rec(b, num, time, " " + socket.getPort());
                if (b[0] == ProtocolX.HEAD && b[num - 1] == ProtocolX.TAIL) {
                    netType = b[1];
                    netID = b[2];
                    byte cmdType = b[3];
                    byte cmdID = b[4];
                    if (netBean == null) {
                        netBean = SysNetService.getNetBean(netType, netID);
                        if (netBean != null) {
                            CollectWLANGateway.getInstance().addSocket(this);
                        }
                    }
                    if (cmdType == ProtocolX.cmdHeartR) {// 心跳
                        byte[] msgH = getReply(ProtocolX.cmdHeartT, cmdID);
                        sendMSG(msgH);
//                        if (netBean == null) {
//                            netBean = SysNetService.getNetBean(netType, netID);
//                            if (netBean != null) {
//                                CollectWLANGateway.getInstance().addSocket(this);
//                            }
//                        }
                    } else if (cmdType == ProtocolX.cmdDataR) {// 数据
                        byte[] data = new byte[num];
                        System.arraycopy(b, 0, data, 0, num);
                        byte[] msgD = getReply(ProtocolX.cmdDataT, cmdID);
                        sendMSG(msgD);
                        dataFactory.receDatas(new RawDataX(data, time));
                    } else if (cmdType == ProtocolX.cmdSetR) {// 设置应答
                        JOptionPane.showMessageDialog(null, "设置成功");
                        System.out.println("设置应答");
//                        byte unitType = b[8];
//                        byte unitId = b[9];
//                        SysUnitService.setTb(netType, netID, unitType, unitId);
                    } else if (cmdType == ProtocolX.cmdMsgR) {// 短信应答
                        System.out.println("短信应答");
                    } else if (cmdType == 0x09) {
                        System.out.println("报警应答");
                    }
                }
            }
            offlineMSG = "已下线,port: ";
        } catch (SocketException e) {
//            CollectOperate.getInstance().getJbtOpen1().setSelected(false);
            e.printStackTrace();
            offlineMSG = "接受数据超时,port: ";
//        } catch (SQLException e) {
//            e.printStackTrace();
        } catch (IOException e) {
            offlineMSG = "连接已被关闭,port: ";
        } finally {
            try {
                this.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dataFactory.close();
            CollectWLANGateway.getInstance().removeSocket(this);
            debugShow.showMsg(offlineMSG + socket.getPort());
        }
    }

    // 如果socket没有关闭
    public void close() throws IOException {
        if (!socket.isClosed()) {
            socket.close();
        }
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    // 是否关闭
    public boolean isClosed() {
        return socket.isClosed();
    }

    public boolean isUseful() {
        return !socket.isClosed() && socket.isConnected();
    }

    public byte getNetId() {
        return netID;
    }

    public byte getNetType() {
        return netType;
    }

    // public NetBean getNetBean() {
    // return netBean;
    // }

    public boolean isMatch(byte netT, byte netI) {
        return netT == netType && netI == netID;
    }

    public void setJg(UnitBean unitBean, byte jg) throws IOException {
        System.out.println("设置参数");
        byte[] msg;
        if (unitBean.getType() == ProtocolX.UnitTypeWD) {
            msg = getSetJgWd(unitBean, jg);
        } else {
            msg = getSetJg(unitBean, jg);
        }
        sendMSG(msg);

    }

    public void setAlarm(byte time) throws IOException {
        System.out.println("设置报警");
        sendMSG(getAlarm(time));
    }


//    public void setJg(String type, Byte unitType, byte jg) throws IOException {
//        Vector<Byte> unitIds = SysUnitService.getUnitIds(type, netID);
//        if (unitType == ProtocolX.UnitTypeWD) {
//            for (Byte unitId : unitIds) {
//                byte[] msg = getSetJgWd(unitType, unitId, jg);
//                sendMSG(msg);
//            }
//        } else {
//            for (Byte unitId : unitIds) {
//                byte[] msg = getSetJg(unitType, unitId, jg);
//                sendMSG(msg);
//            }
//        }
//    }

    // 发送指令
    private synchronized void sendMSG(byte[] msg) throws IOException {
        try {
            out.write(msg);
            out.flush();
            debugShow.send(msg, msg.length, "");
        } catch (IOException e) {
            try {
                this.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            throw new IOException(" 发送命令时连接中断");
        }
    }

    byte[] getAlarm(byte time) {
        byte[] b = new byte[10];
        b[0] = netType;
        b[1] = netID;
        b[2] = 0x08;
        b[3] = (byte) 0xb4;
        b[4] = 0;
        b[5] = 2;
        b[6] = 0;
        b[7] = time;
        FormatTransfer.calcCRC16_X(b);// CRC16
        return MyDecodeUitl.Encryption(b);
    }

    byte[] getReply(byte cmdT, byte cmdI) {
        byte[] b = new byte[15];
        b[0] = netType;
        b[1] = netID;
        b[2] = cmdT;
        b[3] = cmdI;
        b[4] = 0x00;
        b[5] = ProtocolX.LenHeartT;
        b[6] = 0x00;
        Calendar cal = Calendar.getInstance();
        b[7] = (byte) (cal.get(Calendar.YEAR) % 100);
        b[8] = (byte) (cal.get(Calendar.MONTH) + 1);
        b[9] = (byte) cal.get(Calendar.DAY_OF_MONTH);
        b[10] = (byte) cal.get(Calendar.HOUR_OF_DAY);
        b[11] = (byte) cal.get(Calendar.MINUTE);
        b[12] = (byte) cal.get(Calendar.SECOND);
        FormatTransfer.calcCRC16_X(b);// CRC16
        return MyDecodeUitl.Encryption(b);
    }

    byte[] getSetJg(UnitBean unitBean, byte jg) {
        byte[] b = new byte[20];
        b[0] = netType;
        b[1] = netID;
        b[2] = ProtocolX.cmdSetT;
        b[3] = ProtocolX.cmdSetIDR;
        b[4] = 0x00;
        b[5] = ProtocolX.LenSetWDT;
        b[6] = 0x00;
        b[7] = unitBean.getType();
        b[8] = unitBean.getNumber();
        NetBean netBean = SysNetService.getNetBean(unitBean.getGatewaytype(), unitBean.getGatewaynumber());
        if (netBean != null) {
            b[9] = netBean.getChannel();
        }
        b[10] = jg;
        FormatTransfer.calcCRC16_X(b);// CRC16
        return MyDecodeUitl.Encryption(b);
    }

    byte[] getSetJgWd(UnitBean unitBean, byte jg) {
        // 7E 03 00 02 B1 00 0C 00 03 01 00 1E 1E 50 14 64 0A 00 00 7F 0A 7D
        byte[] b = new byte[20];
        b[0] = netType;
        b[1] = netID;
        b[2] = ProtocolX.cmdSetT;
        b[3] = ProtocolX.cmdSetIDR;
        b[4] = 0x00;
        b[5] = ProtocolX.LenSetWDT;
        b[6] = 0x00;
        b[7] = unitBean.getType();
        b[8] = unitBean.getNumber();
        NetBean netBean = SysNetService.getNetBean(unitBean.getGatewaytype(), unitBean.getGatewaynumber());
        if (netBean != null) {
            b[9] = netBean.getChannel();
        }
        b[10] = jg;
        b[11] = jg;
        WdPeriod wdPeriod = MyConfigure.getWdPeriod();
        b[12] = wdPeriod.getWd1();
        b[13] = wdPeriod.getJg1();
        b[14] = wdPeriod.getWd2();
        b[15] = wdPeriod.getJg2();
        b[16] = 0;
        b[17] = 0;
        FormatTransfer.calcCRC16_X(b);// CRC16
        return MyDecodeUitl.Encryption(b);
    }
}

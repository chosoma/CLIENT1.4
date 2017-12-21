package data;

import java.util.Calendar;
import java.util.Date;

import protocol.DefineData;
import protocol.DefineDataType;
import protocol.DefineEqpt;
import protocol.DefineHost;
import protocol.DefineParam;
import protocol.Protocol;

public class OrderFactory {

    public final static int CLOCK = 1;
    public final static int CLOCK_MODIFY = 2;
    public final static int CLOCK_STOP = 3;
    public final static int START_ONLINE = 4;
    public final static int READ_DATA = 5;
    public final static int READ_STORAGE = 6;
    public final static int CLEAR_STORAGE = 7;
    public final static int STANDBY = 8;
    public final static int SLEEP = 9;
    public final static int MODIFY = 10;
    public final static int STOP_OFFLINE = 11;
    public final static int START_OFFLINE = 12;
    public final static int JET_LAG = 28800000;

    // FF 00 00 00 00 0A 83 00 4E 3D
    public static byte[] StartOnLine = {(byte) 0xFF, DefineEqpt.ALL, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0A, DefineHost.COLLECT_DATA, (byte) 0x00, (byte) 0x4E, (byte) 0x3D};

    //
    public static byte[] collect(byte mkh) {
        byte[] period = initHead(mkh, 10);
        period[6] = DefineHost.COLLECT_DATA;
        period[7] = (byte) 0x00;
        FormatTransfer.calcCRC16(period);
        return period;
    }

    // 时钟校准,并修改起始时间和间隔（开始采集）
    public static byte[] clock_Modify(byte mkh, long period) {
        byte[] msg = initHead(mkh, 21);
        msg[6] = DefineHost.SET_PARAMETER;
        // 采集间隔
        msg[7] = DefineParam.PERIOD;
        msg[8] = DefineDataType.LONG;
        byte[] prd = null;
        if (period == 0) {
            prd = new byte[]{0x00, 0x00, 0x00, 0x00};
        } else {
            prd = FormatTransfer.long2Bytes(period);// 倪工硬件已秒为单位
        }
        System.arraycopy(prd, 0, msg, 9, 4);
        // 当前时间
        msg[13] = DefineParam.CLOCK;
        msg[14] = DefineDataType.LONG;
        long time = Calendar.getInstance().getTimeInMillis() + JET_LAG;
        byte[] t = FormatTransfer.long2Bytes(time / 1000);
        System.arraycopy(t, 0, msg, 15, 4);

        FormatTransfer.calcCRC16(msg);
        return msg;

    }

    public static byte[] modify(byte mkh, long period) {
        byte[] order = initHead(mkh, 15);
        order[6] = DefineHost.SET_PARAMETER;
        // 采集间隔
        order[7] = DefineParam.PERIOD;
        order[8] = DefineDataType.LONG;
        byte[] prd = null;
        if (period == 0) {
            prd = new byte[]{0x00, 0x00, 0x00, 0x00};
        } else {
            prd = FormatTransfer.long2Bytes(period);// 倪工硬件已秒为单位
        }
        System.arraycopy(prd, 0, order, 9, 4);
        FormatTransfer.calcCRC16(order);
        return order;
    }

    // 读取上次采集的全部数据 FF 00 00 0x 00 0A 81 00 xx xx
    public static byte[] readData(byte mkh) {
        byte[] order = initHead(mkh, 10);
        order[6] = DefineHost.READ_DATA;
        order[7] = DefineData.ALL_DATA;
        FormatTransfer.calcCRC16(order);
        return order;
    }

    // 读取存储器 FF 00 00 0x 00 0A 85 00 xx xx
    public static byte[] readStorage(byte mkh) {
        byte[] order = initHead(mkh, 10);
        order[6] = DefineHost.READ_MEMORY;
        order[7] = (byte) 0x00;
        FormatTransfer.calcCRC16(order);
        return order;

    }

    // 读取存储器 FF 00 00 0x 00 0A 85 ** ** ** ** xx xx
    public static byte[] readStorage(byte mkh, Date time) {
        if (time == null) {
            return readStorage(mkh);
        }
        byte[] order = initHead(mkh, 13);
        order[6] = DefineHost.READ_MEMORY;
        byte[] t = FormatTransfer.long2Bytes(time.getTime() / 1000);// 转换成4个长度的字节数组
        System.arraycopy(t, 0, order, 7, 4);
        FormatTransfer.calcCRC16(order);
        return order;
    }

    // 清空存储器 FF 00 00 0x 00 09 84 xx xx
    public static byte[] clearStorage(byte mkh) {
        byte[] order = initHead(mkh, 9);
        order[6] = DefineHost.CLEAR_MEMORY;
        FormatTransfer.calcCRC16(order);
        return order;
    }

    /**
     * 初始化通用指令表头部分
     *
     * @param num 设备编号,0表示该类型的所有设备
     * @param len 数据包长度
     * @return byte[]
     */
    private static byte[] initHead(byte num, int len) {
        byte[] msg = new byte[len];
        msg[0] = Protocol.HEAD_MARK;
        msg[1] = DefineEqpt.ALL;
        msg[2] = 0;
        msg[3] = num;
        byte[] length = FormatTransfer.short2Bytes(len);
        msg[4] = length[0];
        msg[5] = length[1];
        return msg;
    }
}

package data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class FormatTransfer {

    /**
     * 计算CRC16校验位
     *
     * @param buf - 字节数组
     * @return - 校验位值
     */
    private static int alex_crc16(byte[] buf, int len) {
        int crc = 0xFFFF;
        for (int i = 0; i < len; i++) {
            int c = buf[i] & 0x00FF;
            crc ^= c;
            for (int j = 0; j < 8; j++) {
                if ((crc & 0x0001) != 0) {
                    crc >>= 1;
                    crc ^= 0xA001;
                } else
                    crc >>= 1;
            }
        }
        return (crc);
    }

    /**
     * 将字节数组加上CRC16校验
     *
     * @param b
     * @return
     */
    public static void calcCRC16(byte[] b) {
        int crc16 = alex_crc16(b, b.length - 2);
        b[b.length - 2] = (byte) (crc16 & 0xFF);
        b[b.length - 1] = (byte) (crc16 >> 8 & 0xFF);
    }

    /**
     * 校验CRC16位
     *
     * @param bytes 需要校验的字节数组
     * @return
     */
    public static boolean checkCRC16(byte[] bytes) {
        int crc16 = alex_crc16(bytes, bytes.length - 2);
        return (bytes[bytes.length - 2] == (byte) (crc16 & 0xFF))
                && (bytes[bytes.length - 1] == (byte) (crc16 >> 8 & 0xFF));
    }

    /**
     * 校验CRC16位
     *
     * @param bytes 需要校验的字节数组
     * @return
     */
    public static boolean checkCRC16(byte[] bytes, int len) {
        if (len == 0) {
            return false;
        }
        int crc16 = alex_crc16(bytes, len - 2);
        return (bytes[len - 2] == (byte) (crc16 & 0xFF))
                && (bytes[len - 1] == (byte) (crc16 >> 8 & 0xFF));
    }

    /**
     * 响水涧CRC16检验
     *
     * @param message
     * @param len
     * @return
     */
    private static int cal_serv_crc(byte[] message, int off, int len) {
        int crc = 0x00;
        int polynomial = 0x1021;
        for (int index = off; index < off + len; index++) {
            byte b = message[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        return crc;
    }

    public static void calcCRC16_X(byte[] b) {
        int crc16 = cal_serv_crc(b, 0, b.length - 2);
        b[b.length - 1] = (byte) (crc16 & 0xFF);
        b[b.length - 2] = (byte) (crc16 >> 8 & 0xFF);
    }

    public static boolean checkCRC16_X(byte[] bytes) {
        int crc16 = cal_serv_crc(bytes, 1, bytes.length - 4);
        System.out.println(Integer.toHexString(crc16));
        return (bytes[bytes.length - 2] == (byte) (crc16 & 0xFF)) && (bytes[bytes.length - 3] == (byte) (crc16 >> 8 & 0xFF));
    }

    // /**
    // * 获取当前时间的字节数组 这里加上了8个时区
    // *
    // * @return time
    // */
    // public static byte[] getCurrentTime() {
    // Calendar calendar = Calendar.getInstance();
    // // calendar.add(Calendar.HOUR_OF_DAY, +8);
    // long timeLong = calendar.getTimeInMillis() / 1000;
    // return long2Bytes(timeLong);
    // }

    /**
     * 将时间的字节数组转换成格式化字符串
     *
     * @param time 时间的字节数组
     * @return
     */
    public String time2String(byte[] time) {
        long n = bytes2Long(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 王敏硬件以零时区为时间基准
        sdf.setTimeZone(TimeZone.getTimeZone("ETC/GMT"));
        Date dt3 = new Date(n * 1000);
        return sdf.format(dt3);
    }

    /**
     * 将高位在前的字节数组转换成短整型变量Short
     *
     * @param b 需要转换的字节数组
     * @return
     */
    public static short bytes2Short(byte... b) {
        return bytes2Short(b, 0, b.length);
    }

    /**
     * 将高位在前的字节数组转换成短整型变量Short
     *
     * @param b   需要转换的字节数组
     * @param off 偏移量
     * @param len 需要转换字节数的长度
     * @return
     */
    public static short bytes2Short(byte b[], int off, int len) {
        short temp = 0;
        for (int i = off; i < off + len; i++) {
            temp = (short) (temp << 8 | (b[i] & 0xFF));
        }
        return temp;
    }

    /**
     * 将低位在前的字节数组转换成短整型变量Short
     *
     * @param b
     * @param off
     * @param len
     * @return
     */
    public static short bytes2ShortL(byte b[], int off, int len) {
        short temp = 0;
        for (int i = off; i < off + len; i++) {
            temp = (short) (temp | (b[i] & 0xFF) << (8 * (i - off)));
        }
        return temp;
    }

    /**
     * 将高位在前的字节数组转换成整型变量Integer
     *
     * @param b 需要转换的字节数组
     * @return
     */
    public static int bytes2Int(byte b[]) {
        return bytes2Int(b, 0, b.length);
    }

    /**
     * 将高位在前的字节数组转换成整型变量Integer
     *
     * @param b   需要转换的字节数组
     * @param off 偏移量
     * @param len 需要转换字节数的长度
     * @return
     */
    public static int bytes2Int(byte b[], int off, int len) {
        // return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0]
        // & 0xFF) << 24 ;

        // return ((((b[0] & 0xFF) << 8 | (b[1] & 0xFF)) << 8) | (b[2] & 0xFF))
        // << 8
        // | (b[3] & 0xFF);
        int temp = 0;
        for (int i = off; i < off + len; i++) {
            temp = temp << 8 | (b[i] & 0xFF);
        }
        return temp;
    }

    public static int getDataLen(byte... b) {
        return bytes2Int(b);
    }

    /**
     * 将低字节在前的字节数组转换成浮点型Float
     *
     * @param b        需要转换的字节数组
     * @param newScale 要保留的小数位
     * @return
     */
    public static float bytesL2Float(byte[] b, int newScale) {
        return bytesL2Float(b, 0, b.length, newScale);
    }

    /**
     * 将低字节在前的字节数组转换成浮点型Float
     *
     * @param b        需要转换的字节数组
     * @param off      偏移量
     * @param len      需要转换字节数的长度
     * @param newScale 要保留的小数位
     * @return
     */
    public static float bytesL2Float(byte[] b, int off, int len, int newScale) {
        // int temp = ((((b[3] & 0xFF) << 8 | (b[2] & 0xFF)) << 8) | (b[1] &
        // 0xFF)) << 8
        // | (b[0] & 0xFF);
        int temp = 0;
        for (int i = off; i < off + len; i++) {
            temp = temp | (b[i] & 0xFF) << (8 * (i - off));
        }
        float f = Float.intBitsToFloat(temp);
        BigDecimal bd = new BigDecimal(f);
        return bd.setScale(newScale, BigDecimal.ROUND_HALF_UP).floatValue();
    }
    public static Float bytesL2Float3(byte[] b, int off, int len, int newScale) {
        int temp = 0;
        for (int i = off; i < off + len; i++) {
            temp = temp | (b[i] & 0xFF) << (8 * (i - off));
        }
        return (float) (temp * 1.0 / newScale);
    }
    public static Float bytesL2Float2(byte[] b, int off, int len, int newScale) {
        // int temp = ((((b[3] & 0xFF) << 8 | (b[2] & 0xFF)) << 8) | (b[1] &
        // 0xFF)) << 8
        // | (b[0] & 0xFF);
        int temp = 0;
        for (int i = off; i < off + len; i++) {
            temp = temp | (b[i] & 0xFF) << (8 * (i - off));
        }
        Float f = Float.intBitsToFloat(temp);
        if (Float.isNaN(f)) {
            return 0f;
        }
        BigDecimal bd = new BigDecimal(f);
        return bd.setScale(newScale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 将高位在前的字节数组转换成长整型变量Long
     *
     * @param b 需要转换的字节数组
     * @return
     */
    public static long bytes2Long(byte b[]) {
        // return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0]
        // & 0xFF) << 24 ;

        // return ((((b[0] & 0xFF) << 8 | (b[1] & 0xFF)) << 8) | (b[2] & 0xFF))
        // << 8
        // | (b[3] & 0xFF);
        return bytes2Long(b, 0, b.length);
    }

    /**
     * 将高位在前的字节数组转换成长整型变量Long
     *
     * @param b   需要转换的字节数组
     * @param off 偏移量
     * @param len 需要转换字节数的长度
     * @return
     */
    public static long bytes2Long(byte b[], int off, int len) {
        long temp = 0;
        for (int i = off; i < off + len; i++) {
            temp = temp << 8 | (b[i] & 0xFF);
        }
        return temp;
    }

    /**
     * 将高位在前的字节数组转换成时间变量Date
     *
     * @param b 需要转换的字节数组
     * @return
     */
    public static Date bytes2Date(byte b[]) {
        return bytes2Date(b, 0, b.length);
    }

    /**
     * 将高位在前的字节数组转换成时间变量Date
     *
     * @param b   需要转换的字节数组
     * @param off 偏移量
     * @param len 需要转换字节数的长度
     * @return
     */
    public static Date bytes2Date(byte b[], int off, int len) {
        long n = bytes2Long(b, off, len);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(n * 1000 - OrderFactory.JET_LAG);
        if (c.get(Calendar.SECOND) == 59) {
            c.add(Calendar.SECOND, 1);
        }
        return c.getTime();
    }

    public static Date bytes2DateRain(byte b[], int off, int len) {
        long n = bytes2Long(b, off, len);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(n * 1000 - OrderFactory.JET_LAG);
        return c.getTime();
    }

    /**
     * 将Short转换成高字节在前、低字节在后的byte数组
     *
     * @param s
     * @return
     */
    public static byte[] short2Bytes(short s) {
        byte[] b = new byte[2];
        b[0] = (byte) (s >> 8 & 0xFF);
        b[1] = (byte) (s & 0xFF);
        return b;
    }

    /**
     * 将整形表示的短整型变量转换成高字节在前、低字节在后的byte数组
     *
     * @param s
     * @return
     */
    public static byte[] short2Bytes(int s) {
        byte[] b = new byte[2];
        b[0] = (byte) (s >> 8 & 0xFF);
        b[1] = (byte) (s & 0xFF);
        return b;
    }

    /**
     * 将int转换成低字节在前、高字节在后的byte数组
     *
     * @param i
     * @return
     */
    private static byte[] int2BytesL(int i) {
        byte[] b = new byte[4];
        b[0] = (byte) (i & 0xFF);
        b[1] = (byte) (i >> 8 & 0xFF);
        b[2] = (byte) (i >> 16 & 0xFF);
        b[3] = (byte) (i >> 24 & 0xFF);
        return b;
    }

    /**
     * 将Float转换成低字节在前、高字节在后的byte数组
     *
     * @param f
     * @return
     */
    public static byte[] float2BytesL(Float f) {
        return int2BytesL(Float.floatToRawIntBits(f));
    }

    /**
     * 将long转为高字节在前，低字节在后的byte数组
     *
     * @param l 需要转换的长整形变量
     * @return
     */
    public static byte[] long2Bytes(long l) {
        byte[] b = new byte[4];
        b[3] = (byte) (l & 0xFF);
        b[2] = (byte) (l >> 8 & 0xFF);
        b[1] = (byte) (l >> 16 & 0xFF);
        b[0] = (byte) (l >> 24 & 0xFF);
        return b;
    }

    /**
     * 将字符串yyyy-MM-dd HH:mm:ss形式的时间转换成高位在前的长度为4的字节数组
     *
     * @param date
     * @return
     */
    public static byte[] date2Bytes(String date) {
        Date dateTime = Timestamp.valueOf(date);
        return date2Bytes(dateTime);
    }

    /**
     * 将时间转换成高位在前的长度为4的字节数组
     *
     * @param date
     * @return
     */
    public static byte[] date2Bytes(Date date) {
        long dateTime = date.getTime() / 1000;
        return long2Bytes(dateTime);
    }

    /**
     * 将c1、c2两字符表示的数合并成一个字节
     *
     * @param c1
     * @param c2
     * @return
     */
    private static byte uniteByte(char c1, char c2) {
        return (byte) (Byte.decode("0x" + c1) << 4 | Byte.decode("0x" + c2));
    }

    /**
     * 将HEX显示的字节码转换成字节数组
     *
     * @param s
     * @return
     */
    public static byte[] hexString2Bytes(String s) {
        int len = s.length();
        byte[] ret = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            ret[i / 2] = uniteByte(s.charAt(i), s.charAt(i + 1));
        }
        return ret;
    }

    /**
     * 将HEX显示的单个字节码转换成字节
     *
     * @param s
     * @return
     */
    public static byte hexString2Byte(String s) {
        return uniteByte(s.charAt(0), s.charAt(1));
    }

    private static final String HEXES = "0123456789ABCDEF";

    /**
     * 将字节数组转换成HEX显示的字节码
     *
     * @param raw
     * @return
     */
    public static String getBufHexStr(byte[] raw) {
        return getBufHexStr(raw, 0, raw.length);
    }

    /**
     * 将字节数组转换成HEX显示的字节码
     *
     * @param b   需要转换的字节数组
     * @param off 偏移量
     * @param len 需要转换字节数的长度
     * @return
     */
    public static String getBufHexStr(byte[] b, int off, int len) {
        if (b == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * len);
        for (int i = off; i < off + len; i++) {
            hex.append(HEXES.charAt((b[i] & 0xF0) >> 4))
                    .append(HEXES.charAt((b[i] & 0x0F))).append(" ");
        }
        return hex.toString();
    }

    /**
     * 得到某数据项的长度
     *
     * @param b
     * @return
     */
    public static int getDatalen(byte b) {
        return b & 0x1F;
    }

    public static boolean isSwitchOn(byte b) {
        return b != 0x00;
    }

    public static float newScale(float f1, float f2) {
        float f3 = f1 - f2;
        int i1 = Math.round(f3 * 10);
        return (float) (i1 / 10.0);
    }


}

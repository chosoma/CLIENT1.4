package util;

public class MyUtils {
    /**
     * 判断字符串是否为空或者空字符串
     */
    public static boolean isNull(String s) {
        return s == null || s.equals("");
    }

    public static void checkDouble(String test, String value) throws Exception {
        if (isNull(value)) {
            throw new Exception(test + "未填写");
        }
        try {
            Double.valueOf(value);
        } catch (NumberFormatException e) {
            throw new Exception(test + "填写格式不正确");
        }
    }

    /**
     * float四舍五入
     *
     * @param f     浮点数
     * @param scale 保留小数位数
     */
    public static float getFloat(float f, int scale) {
        double x = Math.pow(10, scale);
        return (float) ((Math.round(f * x)) / x);
    }

    /**
     * double四舍五入
     * <p>
     * 浮点数
     *
     * @param scale 保留小数位数
     */
    public static double getDouble(double d, int scale) {
        int x = ((Double) Math.pow(10, scale)).intValue();
        return (Math.round(d * x)) / x;
    }

    /**
     * 将地址码或者编号的hex字符串转成整形，以便存入数据库
     */
    public static int HexString2Int(String hex) {
        return Integer.parseInt(hex, 16);
    }

    /**
     * 将地址码或者编号转换成hex字符串，用于显示
     */
    public static String Int2HexString(int i) {
        char[] buf = new char[32];
        int charPos = 32;
        do {
            buf[--charPos] = BytesUtil.HexChars[i & 0x0f];
            i >>>= 4;
        } while (i != 0);

        return new String(buf, charPos, (32 - charPos));
    }

    /**
     * 将地址码或者编号对应的字节转成整数，以便在表中查询
     */
    public static int ByteUnsigned2Int(byte b) {
        return b & 0xff;
    }

    public static double getFS(double fj) {
        double fs;
        if (fj == 0) {
            fs = 0;
        } else if (fj == 1) {
            fs = 0.3;
        } else if (fj == 2) {
            fs = 1.6;
        } else if (fj == 3) {
            fs = 3.4;
        } else if (fj == 4) {
            fs = 5.5;
        } else if (fj == 5) {
            fs = 8.0;
        } else if (fj == 6) {
            fs = 10.8;
        } else if (fj == 7) {
            fs = 13.9;
        } else if (fj == 8) {
            fs = 17.2;
        } else if (fj == 9) {
            fs = 20.8;
        } else if (fj == 10) {
            fs = 24.5;
        } else if (fj == 11) {
            fs = 28.5;
        } else {
            fs = 32.6;
        }
        return fs;
    }

    public static long getPeriodLong(String period) {
        long p = 3600000;
        if (period == null || period.replaceAll(" ", "").equals("")) {
            return p;
        }

        int off = period.indexOf("minutes");
        int num, unit;
        if (off > 0) {
            unit = 60000;
            num = Integer.valueOf(period.substring(0, off));
            return num * unit;
        } else {
            return 3600000;
        }
    }

}

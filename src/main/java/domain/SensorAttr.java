package domain;

/**
 * 传感器相关参数
 */
public class SensorAttr {

    public static String Sensor_SW = "水位";
    public static String Sensor_WD = "温度";
    public static String Sensor_SF6 = "SF6";
    public static String Sensor_SSJ = "伸缩节";

    public static String Value_GY_N = "油压正常";
    public static String Value_GY_L = "低油压报警";
    public static String Value_GY_LL = "低低油压报警";

    public static String[] UnitNames = new String[]{"",Sensor_SF6,Sensor_SSJ,Sensor_WD};

    // 测值类型
    public static String[] Sensor_Type = new String[]{Sensor_SF6, Sensor_WD, Sensor_SSJ};
    // 网关类型
//    public static String[] Net_Type = new String[]{Sensor_SF6, Sensor_WD, Sensor_SW};
    public static Byte[] Net_Type = new Byte[]{4};

    public static String UNIT_CM = "cm", UNIT_MM = "mm", UNIT_M = "m";// 厘米、毫米、米
    public static String[] UNITS_SW = new String[]{UNIT_M, UNIT_CM, UNIT_MM};
    // 温度
    public static String UNIT_WD = "℃";

    public static String UNIT_MPa = "MPa";// 兆帕

}

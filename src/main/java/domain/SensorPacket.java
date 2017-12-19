//package domain;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import protocol.ProtocolX;
//import view.dataCollect.AbcUnitView;
//
///**
// * 传感器包(传感器所属网关集)
// */
//public class SensorPacket {
////    private List<SensorBean> sensors;
//    private short netType = ProtocolX.NetTypeSF6, netID;
//
////    public SensorPacket() {
////        sensors = new ArrayList<SensorBean>();
////    }
//
//    public SensorPacket(short netType) {
////        this();
//        this.netType = netType;
//    }
//
//    public SensorPacket(short netType, short netID) {
//        this(netType);
//        this.netID = netID;
//    }
//
//    public short getNetType() {
//        return netType;
//    }
//
//    public void setNetType(short netType) {
//        this.netType = netType;
//    }
//
//    public short getNetID() {
//        return netID;
//    }
//
//    public void setNetID(short netID) {
//        this.netID = netID;
//    }
//
//    /**
//     * 添加传感器  成功返回true 失败返回false
//     * @param sensor
//     * @return
//     */
//    public boolean addUnit(SensorBean sensor) {
//        short netType = sensor.getType().equals(SensorAttr.Sensor_WD) ? ProtocolX.NetTypeWD : ProtocolX.NetTypeSF6;//判断是温度传感器还是sf6传感器
//        if (match(netType, sensor.getNetid())) {
//            sensors.add(sensor);
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 判断网关类型和编号 完全匹配返回true 否则返回false
//     * @param netType
//     * @param netID
//     * @return
//     */
//    public boolean match(short netType, short netID) {
//        return this.netType == netType && this.netID == netID;
//    }
//
//
//    /**
//     * 通过设计编号获取传感器 未找到返回null
//     * @param sjbh
//     * @return
//     */
//    public SensorBean getUnit(String sjbh) {
//        for (SensorBean sensor : sensors) {
//            if (sensor.getSjbh().equals(sjbh)) {
//                return sensor;
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 通过单元id获取传感器 并设置相位
//     * @param unitid
//     * @return
//     */
//    public SensorBean getUnit(short unitid) {
//        for (SensorBean sensor : sensors) {
//            if (sensor.getA() != null && sensor.getA() == unitid) {
//                sensor.setXw("A");
//                return sensor;
//            } else if (sensor.getB() != null && sensor.getB() == unitid) {
//                sensor.setXw("B");
//                return sensor;
//            } else if (sensor.getC() != null && sensor.getC() == unitid) {
//                sensor.setXw("C");
//                return sensor;
//            }
//        }
//        return null;
//    }
//
//    public SensorBean getSensorGy(short unitid) {
//        for (SensorBean sensor : sensors) {
//            if (
////                    sensor.getType().equals(SensorAttr.Sensor_SSJ) &&
//                    sensor.getA() != null && sensor.getA() == unitid) {
//                sensor.setXw("A");
//                return sensor;
//            }
//        }
//        return null;
//    }
//
//    public SensorBean getUnit(short unitid, short dh) {
//        for (SensorBean sensor : sensors) {
//            if (sensor.getA() != null && sensor.getA() == unitid
//                    && sensor.getAp() != null && sensor.getAp() == dh) {
//                sensor.setXw("A");
//                return sensor;
//            } else if (sensor.getB() != null && sensor.getB() == unitid
//                    && sensor.getBp() != null && sensor.getBp() == dh) {
//                sensor.setXw("B");
//                return sensor;
//            } else if (sensor.getC() != null && sensor.getC() == unitid
//                    && sensor.getCp() != null && sensor.getCp() == dh) {
//                sensor.setXw("C");
//                return sensor;
//            }
//        }
//        return null;
//    }
//
//    public void clear() {
//        sensors.clear();
//    }
//
//    public List<SensorBean> getSensors() {
//        return sensors;
//    }
//
//    /**
//     * 将传感器集设置成相位单元试图
//     * @return
//     */
//    public List<AbcUnitView> getAbcUnitViews() {
//        List<AbcUnitView> views = new ArrayList<AbcUnitView>();
//        for (SensorBean sensor : sensors) {
//            views.add(new AbcUnitView(sensor));
//        }
//        return views;
//    }
//
//}

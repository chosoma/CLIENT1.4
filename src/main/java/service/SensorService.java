package service;

import java.sql.SQLException;
import java.util.*;

import domain.*;
import model.*;

import util.MyDbUtil;
import view.dataCollect.AbcUnitView;
import view.dataCollect.Line1800;


public class SensorService {
    private static String UnitTable = DataBaseAttr.UnitTable;
    private static String PointTable = DataBaseAttr.PointTable;
    private static List<UnitBean> units;
    private static List<UnitPacket> unitPackets;
    private static List<PointBean> points;

    static {
        unitPackets = new ArrayList<>();
        try {
            refreshSensor();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化单元资料
     *
     * @throws SQLException
     */
    public static void refreshSensor() throws SQLException {
        units = SysUnitService.getUnitList();
        points = SysPointService.getPointList();
        Collections.sort(units);
        refreshUnitPackts();//获取gateway

        for (PointBean point : points) {
            UnitPacket unitPacket = getUnitPacket(point.getGatewayType(), point.getGatewayNumber());
            if (unitPacket == null) {
                unitPacket = new UnitPacket(point.getGatewayType(), point.getGatewayNumber());
                unitPackets.add(unitPacket);
            }
            unitPacket.addPoint(point);
            for (UnitBean unitBean : units) {
                if (unitBean.getPoint() == point.getUnitType()) {
                    unitPacket.addUnit(unitBean);
                }
            }
        }
//        for (UnitBean unitBean : units) {
//            UnitPacket unitPacket = getUnitPacket(unitBean.getGatewaytype(), unitBean.getGatewaynumber());
//            if (unitPacket == null) {
//                unitPacket = new UnitPacket(unitBean.getGatewaytype(), unitBean.getGatewaynumber());
//                unitPacket.addUnit(unitBean);
//                unitPackets.add(unitPacket);
//            } else {
//                unitPacket.addUnit(unitBean);
//            }
//        }
        SensorModel.getInstance().setUnits(units);//系统设置
    }

    /**
     * 连接设置清空
     */
    public static void clearMk() {
        units.clear();
        SensorModel.getInstance().clearData();
    }


    /**
     * 清空传感器资料
     *
     * @throws SQLException
     */
    public static void clearUnits() throws SQLException {
        String sql = "delete from " + UnitTable;
        MyDbUtil.update(sql);
        clearMk();
    }

    public static UnitPacket getUnitPacket(byte netType, byte netID) {
        for (UnitPacket packet : unitPackets) {
            if (packet.match(netType, netID)) {
                return packet;
            }
        }
        return null;
    }

    public static List<AbcUnitView> getAbcUnitViews() {
        List<AbcUnitView> views = new ArrayList<AbcUnitView>();
        for (UnitPacket packet : unitPackets) {
            views.addAll(packet.getAbcUnitViews());
        }
        return views;
    }

    public static List<Line1800> getDdUnitViews() {
        List<Line1800> views = new ArrayList<Line1800>();
//        for (SensorBean sensor : sensorsSW) {
//            views.add(new Line1800(sensor));
//        }
        return views;
    }

    /**
     * 根据类型获取编号
     */
    public static Vector<Integer> getNumbers(String name) {
        Vector<Integer> numbers = new Vector<>();
        for (UnitBean unitBean : units) {
            if (name.equals(unitBean.getName())) {
                numbers.add(unitBean.getNumber() & 0xff);
            }
        }
        Collections.sort(numbers);
        return numbers;
    }

    public static void setPlace(PointBean pointBean, String place) {
        for (UnitBean unit : units) {
            if (unit.getPoint() == pointBean.getPoint()) {
                unit.setPlace(place);
            }
        }
    }

    public static Vector<String> getPlaces(String name) {
        Vector<String> places = new Vector<>();
        for (UnitBean unitBean : units) {
            if (name.equals(unitBean.getName())) {
                String place = unitBean.getPlace();
                if (place == null) {
                    place = "";
                }
                if (!places.contains(place)) {
                    places.add(place);
                }
            }
        }
        return places;
    }

    /**
     * 根据物理量获取表格数据模型
     *
     * @param type
     * @return
     */
    public static DataManageModel getDataModel(String type) {
        if (type.equals(SensorAttr.Sensor_WD)) {
            return DataModel_WD.getInstance();
        } else if (type.equals(SensorAttr.Sensor_SF6)) {
            return DataModel_SF6.getInstance();
        } else if (type.equals(SensorAttr.Sensor_SSJ)) {
            return DataModel_SSJ.getInstance();
        } else {
            return null;
//			return DataModel_GY.getInstance();

        }
    }

    private static void refreshUnitPackts() {
        int size = unitPackets.size();
        for (int i = size - 1; i >= 0; i--) {
            unitPackets.remove(i).clear();
        }
        for (NetBean netbean : SysNetService.getNetList()) {
            unitPackets.add(new UnitPacket(netbean));
        }
    }

    public static Object[] getTypes() {
        return SensorAttr.Sensor_Type;
    }

}

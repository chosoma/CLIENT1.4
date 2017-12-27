package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import model.SysUnitModel;
import protocol.ProtocolX;
import util.MyDbUtil;

import domain.DataBaseAttr;
import domain.SensorAttr;
import domain.UnitBean;

public class SysUnitService {
    public static String[] names = SensorAttr.UnitNames;
    private static String UnitTable = DataBaseAttr.UnitTable;
    private static String NetTable = DataBaseAttr.NetTable;
    private static String PointTable = DataBaseAttr.PointTable;
    private static List<UnitBean> unitList;

    public static List<UnitBean> getUnitList() {
        return unitList;
    }

    public static UnitBean getUnitBean(byte type, byte number) {
        for (UnitBean unitBean : unitList) {
            if (type == unitBean.getType() && number == unitBean.getNumber()) {
                return unitBean;
            }
        }
        return null;
    }

    public static void updatePlace(UnitBean unitBean) throws SQLException {
        String sql = " update " + UnitTable + " set \n"
                + " place = ? where point = ? ";
        MyDbUtil.update(sql, unitBean.getPlace(), unitBean.getPoint());
        init();
    }

    public static Vector<UnitBean> getUnitBeans(String name) {
        Vector<UnitBean> beans = new Vector<UnitBean>();
        for (UnitBean unitBean : unitList) {
            if (name.equals(unitBean.getName())) {
                beans.add(unitBean);
            }
        }
        return beans;
    }

    public static List<UnitBean> getUnit(String place, String xw) {
        List<UnitBean> units = new ArrayList<>();
        for (UnitBean unit : unitList) {
            if (xw.equals("")) {
                if (unit.getPlace().equals(place)) {
                    units.add(unit);
                }
            } else if (unit.getPlace().equals(place) && unit.getXw().equals(xw)) {
                units.add(unit);
            }
        }
        return units;
    }

    /**
     * 获取采集单元信息
     *
     * @return
     * @throws SQLException
     */
    public static void init() throws SQLException {
        String sql = "select " +
                "type , number , p.gatewaytype, p.gatewaynumber, " +
                "period, initvari, minvari, maxvari, minden, minden, maxper, minper, warntemp, xw, u.point, p.place " +
                "from " + UnitTable + " u , " + PointTable + " p " +
                "where u.point = p.point";
        unitList = MyDbUtil.queryBeanListData(sql, UnitBean.class);
        SysUnitModel.getInstance().addDatas(unitList);
    }

    /**
     * 根据类型获取单元号(采集操作界面调用)
     *
     * @param name
     * @return
     */
    public static Vector<Integer> getUnitIdsByType(String name) {
        Vector<Integer> ids = new Vector<>();
        for (UnitBean unit : unitList) {
            if (unit.getName().equals(name)) {
                ids.add(unit.getNumber() & 0xff);
            }
        }
        Collections.sort(ids);
        ids.add(0, null);// 需要添加null，以便设置一个类型的参数
        return ids;
    }

    /**
     * 根据测值类型和网关号获取单元号集合
     *
     * @param name  测值类型
     * @param netid 网关号
     * @return
     */
    public static Vector<Byte> getUnitIds(String name, byte netid) {
        Vector<Byte> ids = new Vector<Byte>();
        for (UnitBean unit : unitList) {
            if (unit.getName().equals(name) && unit.getGatewaynumber() == netid) {
                ids.add(unit.getNumber());
            }
        }
        return ids;
    }

    public static byte getNetType(String name) {
        if (name != null && !name.equals("")) {
            for (byte i = 0; i < names.length; i++) {
                if (name.equals(names[i])) {

                }
            }
        }
        return 0;
    }

    /**
     * 添加采集单元
     *
     * @param bean
     * @throws SQLException
     */
    public static void addUnit(UnitBean bean) throws SQLException {
        String sql = "insert into " + UnitTable + " (type,number,period,gatewaytype,gatewaynumber) values (?,?,?,?,?)";
        MyDbUtil.update(sql, getUnittype(bean.getName()), bean.getNumber(), bean.getPeriod(), bean.getGatewaytype(), bean.getGatewaynumber());
        unitList.add(bean);
        SysUnitModel.getInstance().addData(bean);
    }

    static byte getUnittype(String name) {
        switch (name) {
            case "SF6":
                return 1;
            case "伸缩节":
                return 2;
            case "温度":
                return 3;
            default:
                return 0;
        }
    }

    /**
     * 根据测值类型和unitid获取采集单元
     *
     * @param type
     * @param unitid
     * @return
     */
    public static UnitBean getUnitBean(String type, byte unitid) {
        for (UnitBean bean : unitList) {
            if (bean.getName().equals(type) && bean.getNumber() == unitid) {
                return bean;
            }
        }
        return null;
    }

    /**
     * 修改采集单元
     *
     * @param bean
     * @throws SQLException
     */
    public static void updateUnitBean(UnitBean bean, UnitBean oldbean) throws SQLException {
        String sql = "update "
                + UnitTable
                + " set type = ? , number = ? , period = ?  ,gatewaytype = ? , gatewaynumber = ? "
                + " where type = ? and number = ? ; ";
        MyDbUtil.update(sql, bean.getType(), bean.getNumber(), bean.getPeriod(), bean.getGatewaytype(), bean.getGatewaynumber(), oldbean.getType(), oldbean.getNumber());
        for (int i = 0; i < unitList.size(); i++) {
            UnitBean b = unitList.get(i);
            if (oldbean.getType() == b.getType() && oldbean.getNumber() == b.getNumber()) {
                unitList.remove(i);
                unitList.add(i, bean);
                break;
            }
        }
        SysUnitModel.getInstance().addDatas(unitList);
    }

    public static void updateInitvari(UnitBean bean) throws SQLException {
        String sql = "update "
                + UnitTable
                + " set initvari = ? "
                + " where type = ? and number = ? ; ";
        MyDbUtil.update(sql, bean.getInitvari(), bean.getType(), bean.getNumber());
        SysUnitModel.getInstance().addDatas(unitList);
    }

    public static void updateWarning(UnitBean bean) throws SQLException {
        String sql = "update "
                + UnitTable
                + " set maxden = ? ,"
                + "  minden = ? ,"
                + "  maxper = ? ,"
                + "  minper = ? ,"
                + "  maxvari = ? ,"
                + "  minvari = ? ,"
                + "  warntemp = ? "
                + " where type = ? and number = ? ; ";
        MyDbUtil.update(sql, bean.getMaxden(), bean.getMinden(), bean.getMaxper(), bean.getMinper(), bean.getMaxvari(), bean.getMinvari(), bean.getWarnTemp(), bean.getType(), bean.getNumber());
//        SysUnitModel.getInstance().addData(unitList);
    }


    /**
     * 删除采集单元
     *
     * @param name
     * @param unitnumber
     * @throws SQLException
     */
    public static void deleteUnitBean(String name, byte unitnumber) throws SQLException {
        String sql = "delete from "
                + UnitTable
                + " where type = ? and number= ? ";
        MyDbUtil.update(sql, getUnittype(name), unitnumber);
        for (int i = 0; i < unitList.size(); i++) {
            UnitBean bean = unitList.get(i);
            if (name.equals(bean.getName()) && unitnumber == bean.getNumber()) {
                unitList.remove(i);
                break;
            }
        }
        SysUnitModel.getInstance().deleteData(name, unitnumber);
    }

    /**
     * 清空采集单元
     *
     * @throws SQLException
     */
    public static void clearUnitBean() throws SQLException {
        String sql = "delete from " + UnitTable;
        MyDbUtil.update(sql);
        unitList.clear();
        SysUnitModel.getInstance().clearData();
    }

    /**
     * 从Excel导入模块信息
     *
     * @param datas
     * @throws SQLException
     */
    public static void import4Excel(ArrayList<String[]> datas)
            throws SQLException {
        String sql = "insert into " + UnitTable
                + " (type,netid,unitid,jg) values ( ?,?,?,? )";
        MyDbUtil.importData(sql, datas);
        init();
    }

    /**
     * 设置同步(单点)
     *
     * @param unitid
     * @throws SQLException
     */
//    public static void setTb(Date time, byte unitid) throws SQLException {
//        for (UnitBean bean : unitList) {
//            if (bean.getName().equals(SensorAttr.Sensor_SW)
//                    && bean.getNumber() == unitid) {
////                if (bean.getOrderType() == MyCollection.CLOCK) {
//                String sql = "update " + UnitTable
//                        + " set tb= ? where type= ? and unitid= ?";
//                MyDbUtil.update(sql, true, SensorAttr.Sensor_SW, unitid);
//                SysUnitModel.getInstance().setTb(SensorAttr.Sensor_SW,
//                        unitid);
////                }
//                break;
//            }
//        }
//    }

    /**
     * 设置同步
     *
     * @param netType
     * @param netId
     * @param unitType
     * @param unitid
     * @throws SQLException
     */
    public static void setTb(byte netType, byte netId, byte unitType, byte unitid) throws SQLException {
        String type;
        if (unitType == ProtocolX.UnitTypeWD) {
            type = SensorAttr.Sensor_WD;
        } else {
            if (unitType == ProtocolX.UnitTypeSSJ) {
                type = SensorAttr.Sensor_SSJ;
            } else {
                type = SensorAttr.Sensor_SF6;
            }
        }
        for (UnitBean bean : unitList) {
            if (bean.getGatewaynumber() == netId && bean.getNumber() == unitid && bean.getType() == unitType) {
//                String sql = "update " + UnitTable + " set tb where type= ? and number= ?";
//                MyDbUtil.update(sql, 1, bean.getType(), bean.getNumber());
//                SysUnitModel.getInstance().setTb(type, unitid);
                break;
            }
        }
    }


    /**
     * 根据类型设置采集间隔
     *
     * @param type
     * @param jg
     * @throws SQLException
     */
    public static void setJgs(String type, byte jg) throws SQLException {
        String sql = "update " + UnitTable + " set period = ? where type = ?";
        MyDbUtil.update(sql, jg, getUnittype(type));
        SysUnitModel.getInstance().setJgs(type, jg);
    }

    /**
     * 根据类型和unitid设置采集单元的间隔
     *
     * @param type
     * @param unitid
     * @param jg
     * @throws SQLException
     */
    public static void setJg(String type, Byte unitid, byte jg) throws SQLException {
        if (unitid == null) {
            setJgs(type, jg);
        } else {
            String sql = "update " + UnitTable
                    + " set period = ? where type= ? and number = ?";
            for (UnitBean bean : unitList) {
                if (bean.getName().equals(type) && bean.getNumber() == unitid) {
                    MyDbUtil.update(sql, jg, bean.getType(), bean.getNumber());
                    SysUnitModel.getInstance().setJg(type, unitid, jg);
                    break;
                }
            }
        }
    }

    /**
     * 获取采集单元的采集间隔（分钟）
     *
     * @param type
     * @param unitid
     * @return
     */
    public static byte getJgMin(String type, byte unitid) {
        for (UnitBean bean : unitList) {
            if (bean.getName().equals(type) && bean.getNumber() == unitid) {
                return bean.getPeriod();
            }
        }
        return 30;
    }

}

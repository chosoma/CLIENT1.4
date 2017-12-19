package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import protocol.ProtocolX;

import model.SysNetModel;
import model.SysUnitModel;
import util.MyDbUtil;
import domain.DataBaseAttr;
import domain.NetBean;
import domain.SensorAttr;

public class SysNetService {
    private static String netTable = DataBaseAttr.NetTable;
    private static List<NetBean> netList;

    public static List<NetBean> getNetList() {
        return netList;
    }

    /**
     * 获取采集单元信息
     *
     * @return
     * @throws SQLException
     */
    public static void init() throws SQLException {
        String sql = " select * from " + netTable + " group by type,number order by type ,number; ";
        netList = MyDbUtil.queryBeanListData(sql, NetBean.class);
        Collections.sort(netList);
        SysNetModel.getInstance().addDatas(netList);
    }

    public static Vector<Byte> getNetTypes() {
        Vector<Byte> netTypes = new Vector<>();
        for (NetBean netbean : netList) {
            if (!netTypes.contains(netbean.getType())) {
                netTypes.add(netbean.getType());
            }
        }
        return netTypes;
    }

    /**
     * 根据测值类型获取网关号集合
     *
     * @param type 测值类型
     * @return
     */
    public static Vector<Integer> getNetNumbers(byte type) {
        Vector<Integer> ids = new Vector<>();
        for (NetBean netBean : netList) {
            if (netBean.getType() == type) {
                ids.add(netBean.getNumber() & 0xff);
            }
        }
        return ids;
    }

    public static NetBean getNet(byte type, byte number) {
        for (NetBean netBean : netList) {
            if (netBean.getType() == type && netBean.getNumber() == number) {
                return netBean;
            }
        }
        return null;
    }


    /**
     * 添加网关
     *
     * @param bean
     * @throws SQLException
     */
    public static void addNet(NetBean bean) throws SQLException {
        String sql = "insert into " + netTable
                + " (type,number,sim,channel) values ( ?,?,?,? )";
        MyDbUtil.update(sql, bean.getType(), bean.getNumber(), bean.getSim(), bean.getChannel());
        init();
    }

    /**
     * 根据测值类型和netid获取采集单元
     *
     * @param type
     * @param netid
     * @return
     */
    public static NetBean getNetBean(byte type, byte netid) {
        for (NetBean bean : netList) {
            if (bean.getType() == type && bean.getNumber() == netid) {
                return bean;
            }
        }
        return null;
    }


    /**
     * 修改网关信息
     *
     * @param bean
     * @param oldbean
     * @throws SQLException
     */
    public static void updateNetBean(NetBean bean, NetBean oldbean) throws SQLException {
        String sql = "update " + netTable + " set type = ?,number= ?,sim= ? ,channel = ? where type = ? and number = ?;";
        MyDbUtil.update(sql, bean.getType(), bean.getNumber(), bean.getSim(), bean.getChannel(), oldbean.getType(), oldbean.getNumber());
        init();
    }

    /**
     * 删除网关
     *
     * @param type
     * @param number
     * @throws SQLException
     */
    public static void deleteNetBean(byte type, byte number)
            throws SQLException {
        String sql = " delete from " + netTable + " where type = ? and number = ? ";
        MyDbUtil.update(sql, type, number);
        init();
    }

    /**
     * 清空网关
     *
     * @throws SQLException
     */
    public static void clearNetBean() throws SQLException {
        String sql = "delete from " + netTable;
        MyDbUtil.update(sql);
        netList.clear();
        SysUnitModel.getInstance().clearData();
    }

    /**
     * 从Excel导入模块信息
     *
     * @param datas
     * @throws SQLException
     */
    public static void import4Excel(ArrayList<String[]> datas) throws SQLException {
        String sql = "insert into " + netTable + " (type,number,sim,channel) values ( ?,?,? )";
        MyDbUtil.importData(sql, datas);
        init();
    }

}

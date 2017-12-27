package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import util.MyDbUtil;
import domain.DataBaseAttr;
import domain.DataSearchPara;

public class DataManageService {
    private static String tableData = DataBaseAttr.DataTable,
            tableSensor = DataBaseAttr.SensorTable;

    public static List<Vector<Object>> getTableData(String sqlHead, DataSearchPara para) throws SQLException {
        String sql = sqlHead;
//        String sql = sqlHead + tableData + " d ," + tableSensor
//                + " s where d.sjbh=s.sjbh and s.type=? ";
        ArrayList<Object> p = new ArrayList<Object>();
//        p.add(para.getType());
//        if (para.getSjbh() != null) {
//            p.add(para.getSjbh());
//            sql += " and unitnumber = ? ";
//        }
        if (para.getPlace() != null) {
            p.add(para.getPlace());
            sql += " and p.place = ? ";
        }
        if (para.getXw() != null) {
            p.add(para.getXw());
            sql += " and u.xw = ? ";
        }
        if (para.getT1() != null) {
            p.add(para.getT1());
            if (para.getT2() != null) {
                p.add(para.getT2());
                sql += " and date between ? and ? ";
            } else {
                sql += " and date >= ? ";
            }
        } else {
            if (para.getT2() != null) {
                p.add(para.getT2());
                sql += " and date <= ? ";
            } else {
                // do nothing
            }
        }
        sql += " order by date ";
        return MyDbUtil.queryTableData(sql, p.toArray());
//        return MyDbUtil.queryTableData(sql);
    }

    /**
     * 根据物理量获取设计编号
     *
     * @return
     * @throws SQLException
     */
    public static Vector<Integer> getUnitNumbers(String name) {
        return SensorService.getNumbers(name);
    }

    public static Vector<String> getUnitPlaces(String name) {
        return SensorService.getPlaces(name);
    }

    public static void deleteData(int[] idss) throws SQLException {
        String sql = "delete from " + tableData + " where  ID in ";
        StringBuffer ids = new StringBuffer("(");
        for (long i : idss) {
            ids.append(i + ",");
        }
        // 末尾去掉最后一个逗号，添加右括号
        ids.deleteCharAt(ids.length() - 1);
        ids.append(")");
        sql += ids.toString();
        MyDbUtil.update(sql);
    }

    public static void clearData(int[] ids) throws SQLException {
        deleteData(ids);
    }
}

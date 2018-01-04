package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import domain.DataSearchPara;
import domain.UnitBean;
import util.MyDbUtil;
import domain.DataBaseAttr;
import domain.DataBean;

public class DataService {

    private static String sqlInsert;
    private static String sqlOrder;

    private DataService() {
    }

    static {
        String tableName = DataBaseAttr.DataTable;
        sqlInsert = "insert into " + tableName
                + " (unittype,unitnumber,den,pres,temp,vari,batlv,date)"
                + " values ( ?,?,?,?,?,?,?,? )";
        sqlOrder = "order by d.date desc , d.batlv desc , d.pres desc , d.den desc , d.temp desc , d.vari desc ";
    }

    public static List<DataBean> getBetween(UnitBean unitBean, DataSearchPara para) throws SQLException {
        String sql = "select * from ( select g.number gatewaynumber, u.type unittype, u.number unitnumber, period, channel, pres, temp, den, vari, batlv, date\n" +
                "from data d,unit u ,gateway g,point p\n" +
                "where p.point = u.point and d.unittype = u.type and d.unitnumber = u.number and p.gatewaytype = g.type and p.gatewaynumber = g.number and u.type = ? and u.number = ? \n";
        ArrayList<Object> p = new ArrayList<>();
        p.add(unitBean.getType());
        p.add(unitBean.getNumber());
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
            }
        }
        sql += sqlOrder;
        sql += " ) d group by date;\n";
        System.out.println(p.size());
        return MyDbUtil.queryBeanListData(sql, DataBean.class, p.toArray());
    }

    public static void saveCollData(List<DataBean> datas) throws SQLException {
        Vector<Vector<Object>> datasV = new Vector<Vector<Object>>();
        for (DataBean data : datas) {
            datasV.add(data.getSqlData());
        }
        MyDbUtil.batchData(sqlInsert, datasV);
    }

    public static void saveCollData(DataBean... datas) throws SQLException {
        Vector<Vector<Object>> datasV = new Vector<Vector<Object>>();
        for (DataBean data : datas) {
//			datasV.add(data.getSqlData());
        }
        MyDbUtil.batchData(sqlInsert, datasV);
    }

    public static void saveCollData(DataBean data) throws SQLException {
        MyDbUtil.update(sqlInsert, data.getSqlData().toArray());
    }

    public static List<DataBean> getOneUnitDatas(UnitBean unitBean) throws SQLException {
        String sql = "select * from ( select g.number gatewaynumber, u.type unittype, u.number unitnumber, period, channel, pres, temp, den, vari, batlv, date\n" +
                "from data d,unit u ,gateway g\n" +
                "where d.unittype = u.type and d.unitnumber = u.number and u.gatewaytype = g.type and u.gatewaynumber = g.number and u.type = ? and u.number = ? \n" +
                sqlOrder + " ) d group by date;\n";
//        String sql = "select g.number gatewaynumber, u.type unittype, u.number unitnumber, period, channel, pres, temp, den, vari, batlv, date\n" +
//                "from data d,unit u ,gateway g\n" +
//                "where d.unittype = u.type and d.unitnumber = u.number and u.gatewaytype = g.type and u.gatewaynumber = g.number and u.type = ? and u.number = ? \n" +
//                "order by d.date asc ;\n";
        return MyDbUtil.queryBeanListData(sql, DataBean.class, unitBean.getType(), unitBean.getNumber());
    }

    /**
     * 各点最后一条数据
     *
     * @return
     * @throws SQLException
     */
    public static List<DataBean> getLatestDatas() throws SQLException {
        // String sql =
        // "select s.type,a.sjbh,xw,unitid,dh,sj,dy,f1,f2,gcz,a.dw,yz from "
        // + DataBaseAttr.DataTable
        // + " a ,"
        // + DataBaseAttr.SensorTable
        // + " s where s.sjbh=a.sjbh and s.type!='"
        // + SensorAttr.Sensor_SW
        // +
        // "' and a.sj > NOW() - INTERVAL ? DAY and ? >=(select count(*) from "
        // + DataBaseAttr.DataTable
        // + " b where a.sjbh=b.sjbh and a.xw=b.xw and a.sj<=b.sj)";
        // List<DataBean> datas = MyDbUtil.queryBeanListData(sql,
        // DataBean.class,
        // 7, 1);

//        String sql = "select s.type, a.* from "
//                + DataBaseAttr.DataTable
//                + " a inner join(select sjbh,xw,max(sj) sj from "
//                + DataBaseAttr.DataTable
//                + " group by sjbh,xw) b on a.sjbh=b.sjbh and a.xw=b.xw and a.sj=b.sj,"
//                + DataBaseAttr.SensorTable
//                + " s where s.sjbh=a.sjbh and s.type!=? order by s.type,a.sjbh,a.xw";
        String sql = "(SELECT\n" +
                "  `d`.`gatewaynumber`,\n" +
                "  `d`.`unittype`,\n" +
                "  `d`.`unitnumber`,\n" +
                "  `d`.`period`,\n" +
                "  `d`.`channel`,\n" +
                "  `d`.`pres`,\n" +
                "  `d`.`temp`,\n" +
                "  `d`.`den`,\n" +
                "  `d`.`vari`,\n" +
                "  `d`.`batlv`,\n" +
                "  `d`.`date`,\n" +
                "  `d`.`xw`,\n" +
                "  `d`.`point`,\n" +
                "  `d`.`place`\n" +
                "FROM\n" +
                "  (\n" +
                "    (SELECT\n" +
                "      `g`.`number` AS 'gatewaynumber',\n" +
                "      `u`.`type` AS 'unittype',\n" +
                "      `u`.`number` AS 'unitnumber',\n" +
                "      `u`.`period`,\n" +
                "      `g`.`channel`,\n" +
                "      `d`.`Pres` AS 'pres',\n" +
                "      `d`.`Temp` AS 'temp',\n" +
                "      `d`.`Den` AS 'den',\n" +
                "      `d`.`Vari` AS 'vari',\n" +
                "      `d`.`BatLv` AS 'batlv',\n" +
                "      `d`.`date`,\n" +
                "      `u`.`xw`,\n" +
                "      `p`.`point`,\n" +
                "      `p`.`place`\n" +
                "    FROM\n" +
                "      (((`data` d\n" +
                "        JOIN `unit` u)\n" +
                "        JOIN `gateway` g)\n" +
                "        JOIN `point` p)\n" +
                "    WHERE\n" +
                "      ((`d`.`unittype` = `u`.`type`)\n" +
                "        AND (`d`.`unitnumber` = `u`.`number`)\n" +
                "        AND (`u`.`point` = `p`.`point`)\n" +
                "        AND (`p`.`gatewaytype` = `g`.`type`)\n" +
                "        AND (`p`.`gatewaynumber` = `g`.`number`))) d\n" +
                "      JOIN (SELECT\n" +
                "        `data`.`unittype`, `data`.`unitnumber`, MAX(`data`.`date`) AS 'date'\n" +
                "      FROM\n" +
                "        `data`\n" +
                "      GROUP BY\n" +
                "        `data`.`unittype`, `data`.`unitnumber`) da\n" +
                "  )\n" +
                "WHERE\n" +
                "  ((`d`.`unittype` = `da`.`unittype`) AND (`d`.`unitnumber` = `da`.`unitnumber`) AND (`d`.`date` = `da`.`date`))\n" +
                "GROUP BY\n" +
                "  `d`.`unittype`, `d`.`unitnumber`)";
        //        sql = "select s.type,a.sjbh,xw,unitid,dh,sj,dy,f1,f2,gcz,a.dw,yz from "
//                + DataBaseAttr.DataTable + " a ," + DataBaseAttr.SensorTable
//                + " s where s.sjbh=a.sjbh and s.type='"
////				+ SensorAttr.Sensor_SW
//                + "' and sj >=  NOW() - INTERVAL ? HOUR ";
//        List<DataBean> datasw = MyDbUtil.queryBeanListData(sql, DataBean.class,6);
//        if (datas != null) {
//            datas.addAll(datasw);
//        } else {
//            datas = datasw;
//        }
        return MyDbUtil.queryBeanListData(sql, DataBean.class);
    }
}

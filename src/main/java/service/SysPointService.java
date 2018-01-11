package service;

import domain.DataBaseAttr;
import domain.PointBean;
import util.MyDbUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class SysPointService {
    private static String pointTable = DataBaseAttr.PointTable;
    private static String unitTable = DataBaseAttr.UnitTable;
    private static List<PointBean> pointList;

    public static List<PointBean> getPointList() {
        return pointList;
    }

    public static void init() throws SQLException {
        String sql = " select p.point , place , x , y , gatewaytype , gatewaynumber , u.type as unittype\n" +
                " from " + pointTable + " p , " + unitTable + " u\n" +
                " where u.point = p.point group by p.point";
        pointList = MyDbUtil.queryBeanListData(sql, PointBean.class);
    }

    public static void updatePlace(PointBean pointBean) throws SQLException {
        String sql = " update " + pointTable + " set \n" + " place = ? where point = ? ";
        MyDbUtil.update(sql, pointBean.getPlace(), pointBean.getPoint());
        init();
    }

    public static Vector<String> getPlaces() {
        Vector<String> v = new Vector<>();
        for (PointBean point : pointList) {
            if (!v.contains(point.getPlace())) {
                v.add(point.getPlace());
            }
        }
        return v;
    }

}

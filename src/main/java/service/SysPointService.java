package service;

import domain.DataBaseAttr;
import domain.NetBean;
import domain.PointBean;
import domain.UnitBean;
import model.SysNetModel;
import util.MyDbUtil;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class SysPointService {
    private static String pointTable = DataBaseAttr.PointTable;
    private static List<PointBean> pointList;

    public static List<PointBean> getPointList() {
        return pointList;
    }

    public static void init() throws SQLException {
        String sql = " select * from " + pointTable;
        pointList = MyDbUtil.queryBeanListData(sql, PointBean.class);
    }

    public static void updatePlace(PointBean pointBean) throws SQLException {
        String sql = " update " + pointTable + " set \n" + " place = ? where point = ? ";
        MyDbUtil.update(sql, pointBean.getPlace(), pointBean.getPoint());
        init();
    }
}

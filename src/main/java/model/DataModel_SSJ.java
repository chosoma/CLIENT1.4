package model;

import java.util.Date;
import java.util.Vector;

public class DataModel_SSJ extends DataManageModel {

    private static model.DataModel_SSJ DM = null;

    public DataModel_SSJ() {
        initDefault();
    }

    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<String>();
        column.add("ID");
//        column.add("网关类型");
//        column.add("网关编号");
//        column.add("单元类型");
//        column.add("单元编号");
//        column.add("单元编号");
        column.add("监测点");
        column.add("测点相位");
        column.add("偏移量(mm)");
        column.add("电压(Ｖ)");
        column.add("时间");
        this.setDataVector(row, column);
    }

    public static model.DataModel_SSJ getInstance() {
        if (DM == null) {
            synchronized (model.DataModel_SSJ.class) {
                if (DM == null)
                    DM = new model.DataModel_SSJ();
            }
        }
        return DM;
    }

    /**
     * 重写getColumnClass方法，实现排序对列类型的区分
     * 这里根据数据库表中各个列类型，自定义返回每列的类型(用于解决数据库中NULL处理抛出异常)
     */
    @Override
    public Class<?> getColumnClass(int column) {
        // id,gt,gn,ut,un,vari,batlv
        if (column == 0) {
            return Integer.class;
        } else if (column == 1 || column == 2) {
            return String.class;
        } else if (column == 5) {
            return java.util.Date.class;
        } else {
            return Float.class;
        }
    }

    @Override
    public String getSelectSQL() {
        return "SELECT\n" +
                "  i.id as id,\n" +
//                "  g.type AS gt,\n" +
//                "  g.number AS gn,\n" +
//                "  u.type AS ut,\n" +
//                "  u.number AS un,\n" +
                "  p.place AS us,\n" +
                "  u.xw AS ux,\n" +
                "  i.Vari-u.initvari AS vari,\n" +
                "  i.BatLv AS batlv,\n" +
                "  i.date\n" +
                "FROM\n" +
                "  gateway g\n" +
                "    JOIN unit u\n" +
                "    JOIN data i\n" +
                "    JOIN point p\n" +
                "WHERE\n" +
                "  g.type = p.gatewaytype AND u.type = i.unittype AND u.type = 2 AND g.number = p.gatewaynumber AND u.number = i.unitnumber AND u.point = p.point";
    }
}


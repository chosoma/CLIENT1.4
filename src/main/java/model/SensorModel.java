package model;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

//import domain.SensorBean;
import domain.UnitBean;

public class SensorModel extends DefaultTableModel {

    private Vector<Vector<Object>> row;
    private static SensorModel MATCHMODEL = new SensorModel();

    private SensorModel() {
//        Vector<String> column = new Vector<String>();
//        column.add("类型");
//        column.add("设计编号");
//        column.add("备注");
//        column.add("网关");
//        column.add("A相单元");
//        column.add("A相点号");
//        column.add("B相单元");
//        column.add("B相点号");
//        column.add("C相单元");
//        column.add("C相点号");
//        column.add("水位基值");
//        column.add("单位");
//        column.add("预警值");
        row = new Vector<Vector<Object>>();
//        this.setDataVector(row, column);
    }

    public static SensorModel getInstance() {
        if (MATCHMODEL == null) {
            synchronized (SensorModel.class) {
                if (MATCHMODEL == null)
                    MATCHMODEL = new SensorModel();
            }
        }
        return MATCHMODEL;
    }

//    public void setSensors(List<SensorBean> sensors) {
//        row.clear();
//        for (SensorBean sensor : sensors) {
//            row.add(sensor.getTableData());
//        }
//        this.fireTableDataChanged();
//    }

    public void setUnits(List<UnitBean> units) {
        row.clear();
        for (UnitBean unitBean : units) {
            row.add(unitBean.getTableData());
        }
        this.fireTableDataChanged();
    }

//    public void addUnit(SensorBean sensor) {
//        this.addRow(sensor.getTableData());
//    }

    // 清空表中数据
    public void clearData() {
        row.clear();
        this.fireTableDataChanged();
    }

    // 根据数据所在行查询设计编号
    public String getSjbh(int row) {
        return (String) this.getValueAt(row, 1);
    }

    public Class<?> getColumnClass(int column) {
        // type,sjbh,bz,netid,a,ap,b,bp,c,cp,swjz,dw,alarm
        if (column >= 3 && column <= 9) {
            return Byte.class;
        } else if (column == 10 || column == 12) {
            return Float.class;
        } else {
            return String.class;
        }
    }

    // 设置表格不可修改
    public boolean isCellEditable(int row, int column) {
        return false;
    }

}

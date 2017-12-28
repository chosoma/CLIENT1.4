package model;

import java.util.Date;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import domain.DataBean;

public class Collect_DataModel extends DefaultTableModel {

    private Vector<Vector<Object>> row;
    private static Collect_DataModel Model = new Collect_DataModel();

    public static Collect_DataModel getInstance() {
        return Model;
    }

    private Collect_DataModel() {
        Vector<String> column = new Vector<String>();
//        column.add("类型");
        column.add("监测点");
        column.add("相位");
//        column.add("网关类型");
//        column.add("网关编号");
        column.add("密度");
        column.add("压力(Pa)");
        column.add("温度(℃)");
        column.add("位移(mm)");
        column.add("电压(Ｖ)");
        column.add("时间");
        row = new Vector<Vector<Object>>();
        this.setDataVector(row, column);
    }

    public synchronized void addData(DataBean data) {
        row.add(data.getTableData2());
        if (row.size() > 1000) {
            row.remove(0);
        }
        this.fireTableDataChanged();
    }

    /**
     * 设置JTable不可修改
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
     * 清空表格数据
     */
    public void clearData() {
        row.clear();
        this.fireTableDataChanged();
    }

    @Override
    public Class<?> getColumnClass(int column) {
        // type,sjbh,xw,unitid,dh,sj,dy,f1,f2,gcz,dw,yx
        if (column == 0 || column == 1 || column == 2) {
            return String.class;
//        } else if (column == 1) {
//            return Byte.class;
//        } else if (column == 2) {
//            return Integer.class;
        } else if (column == 8) {
            return Date.class;
        } else
            return Float.class;
    }

}

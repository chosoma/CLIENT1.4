package model;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import domain.UnitBean;

/**
 * 单元表模型
 */
public class SysUnitModel extends DefaultTableModel {

    private static SysUnitModel model = new SysUnitModel();

    public static SysUnitModel getInstance() {
        return model;
    }

    Vector<Vector<Object>> row;

    private SysUnitModel() {
        Vector<String> column = new Vector<String>();
        column.add("单元类型");
        column.add("单元编号");
//        column.add("网关类型");
        column.add("网关编号");
        column.add("周期");
        row = new Vector<Vector<Object>>();
        this.setDataVector(row, column);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if (column == 0) {
            return String.class;
        } else if (column == 1 || column == 3) {
            return Integer.class;
        } else {
            return Byte.class;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    private String getType(int row) {
        return (String) getValueAt(row, 0);
    }

    private Byte getUnitId(int row) {
        return (byte) (int) (Integer) getValueAt(row, 1);
    }

    private Byte getJg(int row) {
        return (Byte) getValueAt(row, 3);
    }

    private void setJg(int row, byte jg) {
        setValueAt(jg, row, 3);
    }

    public boolean isTb(int row) {
        return (Boolean) getValueAt(row, 4);
    }


    /**
     * 添加全部单元信息
     *
     * @param datas
     */
    public void addDatas(List<UnitBean> datas) {
        row.clear();
        Collections.sort(datas);
        for (UnitBean unit : datas) {
            row.add(unit.getTableRow());
        }
        this.fireTableDataChanged();
    }

    /**
     * 删除单元信息
     *
     * @param type
     * @param unitId
     */
    public void deleteData(String type, byte unitId) {
        for (int i = 0; i < getRowCount(); i++) {
            if (type.equals(getType(i)) && unitId == getUnitId(i)) {
                removeRow(i);
                break;
            }
        }
    }

    /**
     * 清空单元信息
     */
    public void clearData() {
        row.clear();
        this.fireTableDataChanged();
    }

    /**
     * 添加单元信息
     *
     * @param unitBean
     */
    public void addData(UnitBean unitBean) {
        addRow(unitBean.getTableRow());
    }


    /**
     * 设置间隔
     *
     * @param type
     * @param unitid
     * @param jg
     */
    public void setJg(String type, Byte unitid, byte jg) {
        for (int i = 0; i < getRowCount(); i++) {
            if (type.equals(getType(i)) && unitid == getUnitId(i)) {
                setJg(i, jg);
                break;
            }
        }
    }

    /**
     * 设置一种类型的间隔
     *
     * @param type
     * @param jg
     */
    public void setJgs(String type, byte jg) {
        for (int i = 0; i < getRowCount(); i++) {
            if (type.equals(getType(i))) {
                setJg(i, jg);
            }
        }
    }
}

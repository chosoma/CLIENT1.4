package model;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import domain.NetBean;
import domain.UnitBean;

/**
 * 网关表模型
 */
public class SysNetModel extends DefaultTableModel {

	private static SysNetModel model = new SysNetModel();

	public static SysNetModel getInstance() {
		return model;
	}

	Vector<Vector<Object>> row;

	private SysNetModel() {
		Vector<String> column = new Vector<>();
//		column.add("网关类型");
		column.add("网关编号");
		column.add("sim卡号");
		column.add("信道号");
		row = new Vector<>();
		this.setDataVector(row, column);
	}

	@Override
	public Class<?> getColumnClass(int column) {
		if (column == 1 || column == 3) {
			return Byte.class;
		} else {
			return String.class;
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public void addDatas(List<NetBean> datas) {
		row.clear();
		for (NetBean netBean : datas) {
			row.add(netBean.getTableRow());
		}
		this.fireTableDataChanged();
	}

	public void clearData() {
		row.clear();
		this.fireTableDataChanged();
	}

	public void addData(UnitBean mk) {
		row.add(mk.getTableRow());
		this.fireTableDataChanged();
	}

}

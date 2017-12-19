//package model;
//
//import java.util.Date;
//import java.util.Vector;
//
//public class DataModel_SW extends DataManageModel {
//
//	private static DataModel_SW DM = null;
//
//	private DataModel_SW() {
//		initDefault();
//	}
//
//	@Override
//	protected void initDefault() {
//		super.initDefault();
//		Vector<String> column = new Vector<String>();
//		column.add("ID");
//		column.add("类型");
//		column.add("设计编号");
//		column.add("模块号");
//		column.add("时间");
//		column.add("电压");
//		column.add("f1");
//		column.add("温度");
//		column.add("水位");
//		column.add("单位");
//		column.add("越限");
//		this.setDataVector(row, column);
//	}
//
//	public static DataModel_SW getInstance() {
//		if (DM == null) {
//			synchronized (DataModel_SW.class) {
//				if (DM == null)
//					DM = new DataModel_SW();
//			}
//		}
//		return DM;
//	}
//
//	/**
//	 * 重写getColumnClass方法，实现排序对列类型的区分
//	 * 这里根据数据库表中各个列类型，自定义返回每列的类型(用于解决数据库中NULL处理抛出异常)
//	 */
//	@Override
//	public Class<?> getColumnClass(int column) {
//		// id,type,sjbh,unitid,sj,dy,f1,f2,gcz,dw,yx
//		if (column == 0) {
//			return Integer.class;
//		} else if (column == 3) {
//			return Byte.class;
//		} else if (column == 4) {
//			return Date.class;
//		} else if (column == 5 || column == 6 || column == 7) {
//			return Float.class;
//		} else if (column == 8) {
//			return Double.class;
//		} else {
//			return String.class;
//		}
//	}
//
//	@Override
//	public String getSelectSQL() {
//		return "select id,s.type,d.sjbh,unitid,sj,dy,f1,f2,gcz,d.dw,yz from ";
//	}
//}

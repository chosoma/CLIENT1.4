//package model;
//
//import java.util.Date;
//import java.util.Vector;
//
///**
// * 硅油表模型
// */
//public class DataModel_GY extends DataManageModel {
//
//	private static DataModel_GY DM = null;
//
//	private DataModel_GY() {
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
//		column.add("相位");
//		column.add("单元");
//		column.add("时间");
//		column.add("电压");
//		column.add("f1");
//		column.add("油压");
//		this.setDataVector(row, column);
//	}
//
//	public static DataModel_GY getInstance() {
//		if (DM == null) {
//			synchronized (DataModel_GY.class) {
//				if (DM == null)
//					DM = new DataModel_GY();
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
//		// id,type,sjbh,xw,unitid,sj,dy,f1,yx
//		if (column == 0) {
//			return Integer.class;
//		} else if (column == 4) {
//			return Byte.class;
//		} else if (column == 5) {
//			return Date.class;
//		} else if (column == 6 || column == 7) {
//			return Float.class;
//		} else {
//			return String.class;
//		}
//	}
//
//	@Override
//	public String getSelectSQL() {
//		return "select id,s.type,d.sjbh,xw,unitid,sj,dy,f1,yz from ";
//	}
//}

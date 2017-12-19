//package domain;
//
//import java.util.Date;
//import java.util.Vector;
//
//public class DataBeanX {
//
//	private String type, sjbh, xw = "", dw, yz; //类型,设计编号,相位,单位,yz不明(应该是越限)
//	private float dy;//电压
//	private byte unitid;//单元id
//	private Byte dh;//点号
//	private Date sj;//时间
//	private Float f1, f2;//
//	private Double gcz;//工程值
//
//	public DataBeanX newInstance(String xw) {
//		DataBeanX db = new DataBeanX();
//		db.type = type;//类型
//		db.sjbh = sjbh;//设计编号
//		db.unitid = unitid;//单元
//		db.xw = xw;//相位
//		db.sj = sj;//时间
//		db.dw = dw;//单位
//		db.dy = dy;//电压
//		return db;
//	}
//
//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}
//
//	public String getSjbh() {
//		return sjbh;
//	}
//
//	public void setSjbh(String sjbh) {
//		this.sjbh = sjbh;
//	}
//
//	public byte getUnitid() {
//		return unitid;
//	}
//
//	public void setUnitid(byte unitid) {
//		this.unitid = unitid;
//	}
//
//	public Byte getDh() {
//		return dh;
//	}
//
//	public void setDh(Byte dh) {
//		this.dh = dh;
//	}
//
//	public String getXw() {
//		return xw;
//	}
//
//	public void setXw(String xw) {
//		this.xw = xw;
//	}
//
//	public String getDw() {
//		return dw;
//	}
//
//	public void setDw(String dw) {
//		this.dw = dw;
//	}
//
//	public String getYz() {
//		return yz;
//	}
//
//	public void setYz(String yz) {
//		this.yz = yz;
//	}
//
//	public float getDy() {
//		return dy;
//	}
//
//	public void setDy(float dy) {
//		this.dy = dy;
//	}
//
//	public Date getSj() {
//		return sj;
//	}
//
//	public void setSj(Date sj) {
//		this.sj = sj;
//	}
//
//	public Float getF1() {
//		return f1;
//	}
//
//	public void setF1(Float f1) {
//		this.f1 = f1;
//	}
//
//	public Float getF2() {
//		return f2;
//	}
//
//	public void setF2(Float f2) {
//		this.f2 = f2;
//	}
//
//	public Double getGcz() {
//		return gcz;
//	}
//
//	public void setGcz(Double gcz) {
//		this.gcz = gcz;
//	}
//
//	@Override
//	public String toString() {
//		// TODO Auto-generated method stub
//		return type + " " + sjbh + " " + xw + " " + unitid + " " + dh + " "
//				+ sj.toLocaleString() + " " + dy + " " + f1 + " " + f2 + " "
//				+ gcz + " " + dw + " " + yz;
//	}
//
//	public Vector<Object> getTableData() {
//		Vector<Object> data = new Vector<Object>();
//		data.add(type);
//		data.add(sjbh);
//		data.add(xw);
//		data.add(unitid);
//		data.add(dh);
//		data.add(sj);
//		data.add(dy);
//		data.add(f1);
//		data.add(f2);
//		data.add(gcz);
//		data.add(dw);
//		data.add(yz);
//		return data;
//	}
//
//	public Vector<Object> getSqlData() {
//		Vector<Object> data = new Vector<Object>();
//		data.add(sjbh);
//		data.add(xw);
//		data.add(unitid);
//		data.add(dh);
//		data.add(sj);
//		data.add(dy);
//		data.add(f1);
//		data.add(f2);
//		data.add(gcz);
//		data.add(dw);
//		data.add(yz);
//		return data;
//	}
//
//}

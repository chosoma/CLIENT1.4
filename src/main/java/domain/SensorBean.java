package domain;

import java.util.Vector;

/**
 * 传感器实体类
 */
public class SensorBean {
	private String type, sjbh, bz, dw, xw;
	private byte netid;
	private Byte a, ap, b, bp, c, cp;
	private Float swjz, alarm;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSjbh() {
		return sjbh;
	}

	public void setSjbh(String sjbh) {
		this.sjbh = sjbh;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getDw() {
		return dw;
	}

	public void setDw(String dw) {
		this.dw = dw;
	}

	public byte getNetid() {
		return netid;
	}

	public void setNetid(byte netid) {
		this.netid = netid;
	}

	public Byte getA() {
		return a;
	}

	public void setA(Byte a) {
		if (a != null && a > 0) {
			this.a = a;
		}
	}

	public Byte getAp() {
		return ap;
	}

	public void setAp(Byte ap) {
		if (ap != null) {
			this.ap = ap;
		}
	}

	public Byte getB() {
		return b;
	}

	public void setB(Byte b) {
		if (b != null && b > 0) {
			this.b = b;
		}
	}

	public Byte getBp() {
		return bp;
	}

	public void setBp(Byte bp) {
		if (bp != null) {
			this.bp = bp;
		}
	}

	public Byte getC() {
		return c;
	}

	public void setC(Byte c) {
		if (c != null && c > 0) {
			this.c = c;
		}
	}

	public Byte getCp() {
		return cp;
	}

	public void setCp(Byte cp) {
		if (cp != null) {
			this.cp = cp;
		}
	}

	public Float getSwjz() {
		return swjz;
	}

	public void setSwjz(Float swjz) {
		this.swjz = swjz;
	}

	public Float getAlarm() {
		return alarm;
	}

	public void setAlarm(Float alarm) {
		this.alarm = alarm;
	}

	public String getXw() {
		return xw;
	}

	public void setXw(String xw) {
		this.xw = xw;
	}

	public Vector<Object> getTableData() {
		Vector<Object> data = new Vector<Object>();
		data.add(type);
		data.add(sjbh);
		data.add(bz);
		data.add(netid);
		data.add(a);
		data.add(ap);
		data.add(b);
		data.add(bp);
		data.add(c);
		data.add(cp);
		data.add(swjz);
		data.add(dw);
		data.add(alarm);
		return data;
	}

	@Override
	public String toString() {
		return type + " " + sjbh + " " + bz + " " + netid + " " + a + " " + ap
				+ " " + b + " " + bp + " " + c + " " + cp + " " + swjz + " "
				+ dw + " " + alarm;
	}
}

package domain;

import java.util.Date;

public class RawDataX {
	private byte[] data;
	private Date time;

	public RawDataX(byte[] data, Date time) {
		this.data = data;
		this.time = time;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}

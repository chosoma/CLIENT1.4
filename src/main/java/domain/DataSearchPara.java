package domain;

import java.util.Date;

public class DataSearchPara {

    private String type, place, xw;//类型
    private Byte sjbh;//编号
    private Date t1, t2;

    public Byte getSjbh() {
        return sjbh;
    }

    public void setSjbh(Byte sjbh) {
        this.sjbh = sjbh;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getT1() {
        return t1;
    }

    public void setT1(Date t1) {
        this.t1 = t1;
    }

    public Date getT2() {
        return t2;
    }

    public void setT2(Date t2) {
        this.t2 = t2;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getXw() {
        return xw;
    }

    public void setXw(String xw) {
        this.xw = xw;
    }
}

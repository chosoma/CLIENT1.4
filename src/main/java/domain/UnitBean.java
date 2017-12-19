package domain;

import java.util.Vector;

/**
 * 单元实体类
 */
public class UnitBean implements Comparable<UnitBean> {

    private String name;

    private byte gatewaytype, gatewaynumber, type, number, period;
    private float initvari;
    private Float maxden;
    private Float minden;
    private Float maxper;
    private Float minper;
    private Float warnTemp;

    @Override
    public String toString() {
        return "UnitBean{" +
                "name='" + name + '\'' +
                ", gatewaytype=" + gatewaytype +
                ", gatewaynumber=" + gatewaynumber +
                ", type=" + type +
                ", number=" + number +
                ", period=" + period +
                ", initvari=" + initvari +
                ", maxden=" + maxden +
                ", minden=" + minden +
                ", maxper=" + maxper +
                ", minper=" + minper +
                ", warnTemp=" + warnTemp +
                ", minvari=" + minvari +
                ", maxvari=" + maxvari +
                ", x=" + x +
                ", y=" + y +
                ", point=" + point +
                ", xw='" + xw + '\'' +
                '}';
    }

    private Float minvari;
    private Float maxvari;
    private int x, y, point;
    private String xw,place;
    public UnitBean() {
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public UnitBean(byte gatewaytype, byte gatewaynumber, byte type, byte number) {
        setGatewaytype(gatewaytype);
        setGatewaynumber(gatewaynumber);
        setType(type);
        setNumber(number);
    }

    public byte getGatewaytype() {
        return gatewaytype;
    }

    public void setGatewaytype(byte gatewaytype) {
        this.gatewaytype = gatewaytype;
    }

    public byte getGatewaynumber() {
        return gatewaynumber;
    }

    public void setGatewaynumber(byte gatewaynumber) {
        this.gatewaynumber = gatewaynumber;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        String[] names = new String[]{"", "SF6", "伸缩节", "温度"};
        switch (type) {
            case 1:
            case 2:
            case 3:
                name = names[type];
                break;
            default:
                name = "";
        }
        this.type = type;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public byte getNumber() {
        return number;
    }

    public void setNumber(byte number) {
        this.number = number;
    }

    public byte getPeriod() {
        return period;
    }

    public void setPeriod(byte period) {
        this.period = period;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        switch (name) {
            case "SF6":
                type = 1;
                break;
            case "伸缩节":
                type = 2;
                break;
            case "温度":
                type = 3;
                break;
            default:
                type = 0;
        }
        this.name = name;
    }

    public float getInitvari() {
        return initvari;
    }

    public void setInitvari(float initvari) {
        this.initvari = initvari;
    }

    public Float getMinvari() {
        return minvari;
    }

    public void setMinvari(Float minvari) {
        this.minvari = minvari;
    }

    public Float getMaxvari() {
        return maxvari;
    }

    public void setMaxvari(Float maxvari) {
        this.maxvari = maxvari;
    }

    public Float getMaxden() {
        return maxden;
    }

    public void setMaxden(Float maxden) {
        this.maxden = maxden;
    }

    public Float getMinden() {
        return minden;
    }

    public void setMinden(Float minden) {
        this.minden = minden;
    }

    public Float getMaxper() {
        return maxper;
    }

    public void setMaxper(Float maxper) {
        this.maxper = maxper;
    }

    public Float getMinper() {
        return minper;
    }

    public void setMinper(Float minper) {
        this.minper = minper;
    }

    public Float getWarnTemp() {
        return warnTemp;
    }

    public void setWarnTemp(Float warnTemp) {
        this.warnTemp = warnTemp;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    //    public String getNetidString() {
//        return String.format("DTU<%03d>", gatewayid);
//    }


    public String getXw() {
        return xw;
    }

    public void setXw(String xw) {
        this.xw = xw;
    }


//    public void changeJg(byte period) {
//        this.period = period;
//        this.tb = false;
//    }

    /**
     * 获取间隔（秒）
     *
     * @return
     */
    public long getJgInSecs() {
        return period * 60;
    }

    public Vector<Object> getTableData() {
        Vector<Object> data = new Vector<Object>();
        data.add(name);
        data.add(gatewaytype);
        data.add(gatewaynumber);
        return data;
    }

    public Vector<Object> getTableRow() {
        Vector<Object> row = new Vector<Object>();
        row.add(name);
        row.add(number & 0xff);
//        row.add(gatewaytype);
        row.add(gatewaynumber & 0xff);
        row.add(period);
        return row;
    }

    @Override
    public int compareTo(UnitBean o) {
        if (type == o.type) {
            int n1 = number;
            int n2 = o.number;
            if (n1 < 0) {
                n1 += 256;
            }
            if (n2 < 0) {
                n2 += 256;
            }
            return n1 - n2;
        } else {
            return type - o.type;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitBean unitBean = (UnitBean) o;

        return type == unitBean.type && number == unitBean.number;
    }

    @Override
    public int hashCode() {
        int result = (int) type;
        result = 31 * result + (int) number;
        return result;
    }
}

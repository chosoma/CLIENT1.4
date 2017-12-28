package domain;

import data.FormatTransfer;
import service.SysUnitService;

import java.util.Date;
import java.util.Vector;

public class DataBean implements Comparable {
    private int id;
    private String name, place, xw;
    private byte gatewayType, gatewayNumber, unitType, unitNumber;
    private float pres, temp, den, vari, batlv;
    private Date date;
    private int point;

    public String getXw() {
        return xw;
    }

    public void setXw(String xw) {
        this.xw = xw;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(byte gatewayType) {
        this.gatewayType = gatewayType;
    }

    public byte getGatewayNumber() {
        return gatewayNumber;
    }

    public void setGatewayNumber(byte gatewayNumber) {
        this.gatewayNumber = gatewayNumber;
    }

    public byte getUnitType() {
        return unitType;
    }

    public void setUnitType(byte unitType) {
        String[] names = new String[]{"", "SF6", "伸缩节", "温度"};
        switch (unitType) {
            case 1:
            case 2:
            case 3:
                name = names[unitType];
                break;
            default:
                name = "";
        }
        this.unitType = unitType;
    }

    public byte getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(byte unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public float getPres() {
        return pres;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gatewayType=" + gatewayType +
                ", gatewayNumber=" + gatewayNumber +
                ", unitType=" + unitType +
                ", unitNumber=" + unitNumber +
                ", pres=" + pres +
                ", temp=" + temp +
                ", den=" + den +
                ", vari=" + vari +
                ", batlv=" + batlv +
                ", date=" + date +
                '}';
    }

    public void setPres(float pres) {
        this.pres = pres;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getDen() {
        return den;
    }

    public void setDen(float den) {
        this.den = den;
    }

    public float getVari() {
        return vari;
    }

    public void setVari(float vari) {
        this.vari = vari;
    }

    public float getBatlv() {
        return batlv;
    }

    public void setBatlv(float batlv) {
        this.batlv = batlv;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Vector<Object> getTableData() {

        Vector<Object> data = new Vector<Object>();
        data.add(name);
        data.add(unitNumber);
        if (name.equals("SF6")) {
            data.add(den);
            data.add(pres);
            data.add(temp);
        } else if (name.equals("伸缩节")) {
            data.add(vari);
        } else {//温度
            data.add(temp);
        }
        data.add(batlv);
        data.add(date);
        return data;
    }

    public Vector<Object> getTableData2() {

        Vector<Object> data = new Vector<Object>();
//        data.add(name);
        data.add(place);
        data.add(xw);
//        data.add(unitNumber);
//        data.add(gatewayType);
//        data.add(gatewayNumber);
        if (name.equals("SF6")) {
            data.add(den);
            data.add(pres);
            data.add(temp);
            data.add(null);
        } else if (name.equals("伸缩节")) {
            data.add(null);
            data.add(null);
            data.add(null);
            UnitBean unit = SysUnitService.getUnitBean(unitType, unitNumber);
            float vari;
            if (unit == null) {
                vari = this.vari;
            } else {
                vari = FormatTransfer.newScale(this.vari, unit.getInitvari());
            }
            data.add(vari);
        } else {//温度
            data.add(null);
            data.add(null);
            data.add(temp);
            data.add(null);
        }
        data.add(batlv);
        data.add(date);
        return data;
    }

    public Vector<Object> getSqlData() {
        Vector<Object> data = new Vector<Object>();
        data.add(unitType);
        data.add(unitNumber);
        if (unitType == 1) {
            data.add(den);
            data.add(pres);
            data.add(temp);
            data.add(null);
        } else if (unitType == 2) {
            data.add(null);
            data.add(null);
            data.add(null);
            data.add(vari);
        } else {//温度
            data.add(null);
            data.add(null);
            data.add(temp);
            data.add(null);
        }
        data.add(batlv);
        data.add(date);
        return data;
    }


    @Override
    public int compareTo(Object o) {
        if (getDate().getTime() == ((DataBean) o).getDate().getTime()) {
            if (getBatlv() == ((DataBean) o).getBatlv()) {
                switch (unitType) {
                    case 1:
                        if (pres == ((DataBean) o).pres) {
                            if (den == ((DataBean) o).den) {
                                return (int) Math.floor(temp - ((DataBean) o).temp);
                            }
                            return (int) Math.floor(den - ((DataBean) o).den);
                        }
                        return (int) Math.floor(pres - ((DataBean) o).pres);
                    case 2:
                        return (int) Math.floor(vari - ((DataBean) o).vari);
                    case 3:
                        return (int) Math.floor(temp - ((DataBean) o).temp);
                }
            }
            return (int) Math.floor(batlv - ((DataBean) o).batlv);
        }
        return (int) (date.getTime() - ((DataBean) o).date.getTime());
    }
}

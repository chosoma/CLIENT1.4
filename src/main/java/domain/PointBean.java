package domain;

public class PointBean {
    private int id;
    private int point;
    private String place;
    private byte unitType;
    private byte gatewayType, gatewayNumber;

    public byte getUnitType() {
        return unitType;
    }

    public void setUnitType(byte unitType) {
        this.unitType = unitType;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public PointBean() {

    }
}

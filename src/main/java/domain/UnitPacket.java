package domain;

import protocol.ProtocolX;
import service.SysUnitService;
import view.dataCollect.AbcUnitView;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.List;

/**
 * 单元包
 */
public class UnitPacket {
    private List<UnitBean> units;
    private List<PointBean> points;
    private byte gatewaytype;
    private byte gatewaynumber;

    public UnitPacket() {
        units = new ArrayList<>();
        points = new ArrayList<>();
    }

    public UnitPacket(byte gt, byte gn) {
        this();
        this.gatewaytype = gt;
        this.gatewaynumber = gn;
    }

    public UnitPacket(NetBean netBean) {
        this();
        this.gatewaytype = netBean.getType();
        this.gatewaynumber = netBean.getNumber();
    }

    /**
     * 添加单元  成功返回true 失败返回false
     */
    public boolean addUnit(UnitBean unit) {
        if (units.contains(unit)) {
            units.remove(unit);
        }
        return units.add(unit);
    }

    public boolean addPoint(PointBean point) {
        if (points.contains(point)) {
            points.remove(point);
        }
        return points.add(point);
    }

    /**
     * 判断网关类型和编号 完全匹配返回true 否则返回false
     *
     * @param netType
     * @param netID
     * @return
     */
    public boolean match(byte netType, byte netID) {
        return this.gatewaytype == netType && this.gatewaynumber == netID;
    }


    public UnitBean getUnit(byte type, byte number) {
        for (UnitBean unitBean : units) {
            if (unitBean.getType() == type && unitBean.getNumber() == number) {
                return unitBean;
            }
        }
        return null;
    }

    public UnitBean getUnit(String name, byte number) {
        switch (name) {
            case "SF6":
                return getUnit((byte) 1, number);
            case "伸缩节":
                return getUnit((byte) 2, number);
            case "温度":
                return getUnit((byte) 3, number);
            default:
                return null;
        }
    }

    public void removeUnit(String name, byte number) {
        UnitBean unitBean = getUnit(name, number);
        if (unitBean == null) {
            return;
        }
        units.remove(unitBean);
    }

    public UnitBean getUnit(short unitid, short dh) {
        for (UnitBean sensor : units) {

        }
        return null;
    }

    public void clear() {
        units.clear();
    }

    public List<UnitBean> getUnits() {
        return units;
    }

    /**
     * 将传感器集设置成相位单元试图
     *
     * @return
     */
    public List<AbcUnitView> getAbcUnitViews() {
        List<AbcUnitView> views = new ArrayList<AbcUnitView>();

        for (PointBean point : points) {
            List<UnitBean> units = new ArrayList<>();
            for (UnitBean unit : SysUnitService.getUnitList()) {
                if (unit.getType() == point.getUnitType()) {
                    units.add(unit);
                }
            }
            views.add(new AbcUnitView(point, units));
        }

//        for (UnitBean unitBean : units) {
//            views.add(new AbcUnitView(unitBean));
//        }
        return views;
    }

    @Override
    public String toString() {
        return "UnitPacket{" +
                "gatewaytype=" + gatewaytype +
                ", gatewaynumber=" + gatewaynumber +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitPacket that = (UnitPacket) o;

        return gatewaytype == that.gatewaytype && gatewaynumber == that.gatewaynumber;
    }

    @Override
    public int hashCode() {
        int result = (int) gatewaytype;
        result = 31 * result + (int) gatewaynumber;
        return result;
    }
}

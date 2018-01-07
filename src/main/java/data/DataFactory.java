package data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import domain.UnitBean;
import domain.UnitPacket;
import protocol.ProtocolX;
import service.DataService;
import service.SensorService;
import view.dataCollect.AbcUnitView;
import view.dataCollect.ChartView;
import view.dataCollect.CollectShow;
import domain.DataBean;
import domain.SensorAttr;

public class DataFactory {
    private List<DataBean> dataList = new ArrayList<DataBean>();
    private CollectShow show = CollectShow.getInstance();

    private DataFactory() {

    }

    private static DataFactory DB = new DataFactory();

    public static DataFactory getInstance() {
        return DB;
    }

    // 将数据集合中的数据保存到数据库中
    public synchronized void saveData() {
        if (dataList.size() > 0) {
            try {
                DataService.saveCollData(dataList);
                dataList.clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void processData_X(byte[] data, Date time) {
        byte gatewayType = data[1];// 网关类型
        byte gatewaynumber = data[2];// 网关ID
        UnitPacket unitPacket = SensorService.getUnitPacket(gatewayType, gatewaynumber);
        if (unitPacket == null) {
            System.out.println("未找到网关");
            return;
        }

        int length = FormatTransfer.getDataLen(data[5], data[6]);// 总数据长度

        int dataCount = data[7];// 数据条数
        int off = 7;
        for (int i = 0; i < dataCount; i++) {
            DataBean databean = new DataBean();
            databean.setGatewayType(gatewayType);
            databean.setGatewayNumber(gatewaynumber);
            databean.setDate(time);// 时间
            byte unitType = data[++off];// 单元类型
            byte unitNumber = data[++off];// 单元ID
            databean.setUnitType(unitType);
            databean.setUnitNumber(unitNumber);
            UnitBean unit = unitPacket.getUnit(unitType, unitNumber);
            if (unit == null) {
                System.out.println("单元不存在");
                return;
//                UnitBean unitBean = new UnitBean(gatewayType, gatewaynumber, unitType, unitNumber);
//                unitPacket.addUnit(unitBean);
//                ChartView.getInstance().addNewUnit(new AbcUnitView(unitBean));
            }
            try {
                off++;
                byte[] bytes1 = new byte[4];
                System.arraycopy(data, off, bytes1, 0, 4);
                off += 4;
                if (off > length + 7) {
                    return;
                }
                byte[] bytes2 = new byte[4];
                System.arraycopy(data, off, bytes2, 0, 4);
                off += 4;
                if (off > length + 7) {
                    return;
                }
                byte[] bytes3 = new byte[4];
                System.arraycopy(data, off, bytes3, 0, 4);
                off += 4;
                if (off > length + 7) {
                    return;
                }
                float dy = data[off] / 10.0f;
                databean.setBatlv(dy);// 电量
                if (unitType == ProtocolX.UnitTypeSF6) {// ----SF6单元
                    Float f1 = FormatTransfer.bytesL2Float3(bytes1, 0, 4, 10000);
                    Float f2 = FormatTransfer.bytesL2Float3(bytes2, 0, 4, 100);
//                    Float f3 = FormatTransfer.bytesL2Float3(bytes1, 0, 4, 1);
                    databean.setPres(f1);
                    databean.setDen(f1);
                    databean.setTemp(f2);// F1
                    if (bytes3[0] == 1) {
                        databean.setLowPres(true);
                    }
                    if (bytes3[1] == 1) {
                        databean.setLowLock(true);
                    }
                    databean.setName(SensorAttr.Sensor_SF6);// 类型 SF6
                } else if (unitType == ProtocolX.UnitTypeSSJ) {
                    Float f3 = FormatTransfer.bytesL2Float2(bytes3, 0, 4, 1);
                    databean.setVari(f3);
                    databean.setName(SensorAttr.Sensor_SSJ);// 类型 伸缩节
                } else {//温度
                    Float f3 = FormatTransfer.bytesL2Float2(bytes3, 0, 4, 1);
                    databean.setTemp(f3);
                    databean.setName(SensorAttr.Sensor_WD);// 类型 伸缩节
                }
                databean.setPlace(unit.getPlace());
                databean.setXw(unit.getXw());
                show.receData(databean);
                DataService.saveCollData(databean);
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                System.out.println("数据长度不足");
                aioobe.printStackTrace();
            } catch (SQLException e) {
                System.out.println("数据库连接异常");
                e.printStackTrace();
            }
        }

    }

}

package service;


import javax.swing.JOptionPane;

//import view.sensorMatch.SensorMatch;
import domain.NetBean;
import view.systemSetup.SystemSetup;

import com.CollectWLANGateway;


public class CollectService {

    /**
     * 是否编辑状态
     *
     * @return
     * @throws Exception
     */
    public static boolean isEditable() {
        return SystemSetup.getInstance().isEditable();
    }

    /**
     * 设置编辑状态
     *
     * @param b
     */
    public static void setEditable(boolean b) {
        SystemSetup.getInstance().setEditable(b);
//		SensorMatch.getInstance().setEditable(b);
    }


    /**
     * 关闭服务
     */
    public static void CloseColl1() {
        CollectWLANGateway.getInstance().closeConnection();
    }

    /**
     * 启动服务
     *
     * @throws Exception
     */
    public static void OpenColl1() throws Exception {
        CollectWLANGateway.getInstance().openConnection();
    }


    /**
     * 设置采集间隔
     *
     * @param jg
     */
    public static void setPeriod(String type, Byte unitid, byte jg) {
        // TODO
        try {
            SysUnitService.setJg(type, unitid, jg);
            CollectWLANGateway.getInstance().applyOffline(type, unitid, jg);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void setAlarm(NetBean net, byte time) {
        CollectWLANGateway.getInstance().setAlarm(net, time);
    }


}

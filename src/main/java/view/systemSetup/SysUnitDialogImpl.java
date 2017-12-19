package view.systemSetup;

import javax.swing.*;

import service.SysNetService;
import domain.SensorAttr;
import domain.UnitBean;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SysUnitDialogImpl extends SysUnitDialog {
    private JComboBox<String> jcbType;
    private JComboBox<Integer> jcbNetNumber;
    private JComboBox<Byte> jcbNetType;
    private JComboBox<Integer> jcbUnitNumber;
    private JComboBox<Byte> jcbZq;

    public SysUnitDialogImpl() {
        this.initDefault(null);
    }

    public SysUnitDialogImpl(UnitBean mkbean) {
        this.bean = mkbean;
        this.initDefault(null);
    }

    @Override
    protected JPanel initContent() {
        JPanel centerPanel = new JPanel(null);
        int y = 13;
        JLabel jlbType = new JLabel("单元类型:", JLabel.LEFT);
        jlbType.setBounds(40, y, 65, 20);
        centerPanel.add(jlbType);

        jcbType = new JComboBox<>(SensorAttr.Sensor_Type);
        jcbType.setBounds(105, y, 60, 20);
        jcbType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        centerPanel.add(jcbType);
        y += 30;
//        JLabel jlbNetType = new JLabel("网关类型:", JLabel.LEFT);
//        jlbNetType.setBounds(40, 43, 65, 20);
//        centerPanel.add(jlbNetType);
//
//        jcbNetType = new JComboBox<>((new DefaultComboBoxModel<>(SysNetService.getNetTypes())));
//        jcbNetType.setBounds(105, 45, 60, 20);
//        jcbNetType.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                byte nettype = (byte) jcbNetType.getSelectedItem();
//                jcbNetNumber.setModel(new DefaultComboBoxModel<>(SysNetService.getNetNumbers(nettype)));
//            }
//        });
//        centerPanel.add(jcbNetType);

        JLabel jlbNetID = new JLabel("网关编号:", JLabel.LEFT);
        jlbNetID.setBounds(40, y, 65, 20);
        centerPanel.add(jlbNetID);

        jcbNetNumber = new JComboBox<>(new DefaultComboBoxModel<>(SysNetService.getNetNumbers((byte) 4)));
        jcbNetNumber.setBounds(105, y, 60, 20);
        jcbNetNumber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        centerPanel.add(jcbNetNumber);
        y += 30;
        JLabel jlbUnitId = new JLabel("单元编号:", JLabel.LEFT);
        jlbUnitId.setBounds(40, y, 65, 20);
        centerPanel.add(jlbUnitId);

        Integer[] unitnumbers = new Integer[255];
        for (int i = 0; i < unitnumbers.length; ) {
            unitnumbers[i] = ++i;
        }

        jcbUnitNumber = new JComboBox<>(unitnumbers);
        jcbUnitNumber.setBounds(105, y, 60, 20);
        centerPanel.add(jcbUnitNumber);
        y += 30;
        JLabel jlbZq = new JLabel("周期:", JLabel.LEFT);
        jlbZq.setBounds(40, y, 65, 20);
        centerPanel.add(jlbZq);

        jcbZq = new JComboBox<>(new Byte[]{5, 10, 15, 20, 30, 40, 60});
        jcbZq.setBounds(105, y, 60, 20);
        centerPanel.add(jcbZq);


        centerPanel.setSize(200, y + 30);

        return centerPanel;
    }

    @Override
    protected UnitBean getUnitInfo() throws Exception {
        UnitBean bean = new UnitBean();
        bean.setName((String) jcbType.getSelectedItem());
//        bean.setGatewaytype((byte) jcbNetType.getSelectedItem());
        bean.setGatewaytype((byte) 4);
        bean.setGatewaynumber((byte) ((int) jcbNetNumber.getSelectedItem()));
        bean.setNumber((byte) (int) jcbUnitNumber.getSelectedItem());
        bean.setPeriod((byte) jcbZq.getSelectedItem());

        return bean;
    }

    @Override
    protected void loadUnitInfo() {
        jcbType.setSelectedItem(bean.getName());
//        jcbNetType.setSelectedItem(bean.getGatewaytype());
        jcbNetNumber.setSelectedItem(bean.getGatewaynumber() & 0xff);
        jcbUnitNumber.setSelectedItem(bean.getNumber() & 0xff);
        jcbZq.setSelectedItem(bean.getPeriod());
    }

    @Override
    protected void clearUnitInfo() {
        jcbType.setSelectedIndex(0);
        jcbNetNumber.setSelectedIndex(0);
//        jcbNetType.setSelectedIndex(0);
        jcbUnitNumber.setSelectedIndex(0);
        jcbZq.setSelectedIndex(0);
    }

}

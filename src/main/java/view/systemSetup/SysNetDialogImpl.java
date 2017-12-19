package view.systemSetup;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sun.management.Sensor;
import util.MyUtils;
import domain.NetBean;
import domain.SensorAttr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SysNetDialogImpl extends SysNetDialog {
    private JComboBox<Byte> jcbType;
    private JComboBox<Integer> jcbNumber;
    private JTextField jtfSim, jcbXdh;
    private Integer[] numbers;

    SysNetDialogImpl() {
        this.initDefault(null);
    }

    SysNetDialogImpl(NetBean netbean) {
        this.bean = netbean;
        this.initDefault(null);
    }

    @Override
    protected JPanel initContent() {
        numbers = new Integer[255];
        for (int i = 0; i < numbers.length; ) {
            numbers[i] = ++i;
        }

        JPanel centerPanel = new JPanel(null);

        int y = 13;
//        JLabel jlbType = new JLabel("网关类型:", JLabel.LEFT);
//        jlbType.setBounds(40, y, 65, 20);
//        centerPanel.add(jlbType);
//
//        jcbType = new JComboBox<>(SensorAttr.Net_Type);
//        jcbType.setBounds(105, y, 60, 20);
//        jcbType.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                jcbNumber.setModel(new DefaultComboBoxModel<>(numbers));
//            }
//        });
//        centerPanel.add(jcbType);

//        y += 30;
        JLabel jlbNetID = new JLabel("网关编号:", JLabel.LEFT);
        jlbNetID.setBounds(40, y, 65, 20);
        centerPanel.add(jlbNetID);

        jcbNumber = new JComboBox<>(numbers);
        jcbNumber.setBounds(105, y, 60, 20);
        centerPanel.add(jcbNumber);

        y += 30;
        JLabel jlbSim = new JLabel("SIM 卡号:", JLabel.LEFT);
        jlbSim.setBounds(40, y, 65, 20);
        centerPanel.add(jlbSim);

        jtfSim = new JTextField();
        jtfSim.setBounds(105, y, 60, 20);
        centerPanel.add(jtfSim);

        y += 30;
        JLabel jlbZq = new JLabel("信 道 号:", JLabel.LEFT);
        jlbZq.setBounds(40, y, 65, 20);
        centerPanel.add(jlbZq);

        jcbXdh = new JTextField();
//        jcbXdh = new JComboBox<Integer>(new Integer[] { null, 0, 1, 2, 3, 4, 5, 6, 7,
//                8, 9 });
        jcbXdh.setBounds(105, y, 60, 20);
        centerPanel.add(jcbXdh);

        y += 20;
        centerPanel.setSize(200, y);
        return centerPanel;
    }

    @Override
    protected NetBean getNetInfo() throws Exception {
        NetBean bean = new NetBean();
//        bean.setType((byte) jcbType.getSelectedItem());
        bean.setType((byte) 4);
        bean.setNumber((byte) ((int) jcbNumber.getSelectedItem()));
        String sim = jtfSim.getText().trim();
        if (!sim.equals("")) {
            bean.setSim(sim);
        }
        String xdh = jcbXdh.getText();
        if (xdh == null || xdh.equals("")) {
            bean.setChannel((byte) 0);
        } else {
            int channel = Integer.parseInt(jcbXdh.getText());
            if (channel > 255 || channel < 0) {
                throw new NumberFormatException();
            }
            bean.setChannel((byte) channel);
        }
        return bean;
    }

    @Override
    protected void loadNetInfo() {
//        jcbType.setSelectedItem(bean.getType());
        jcbNumber.setSelectedItem(bean.getNumber() & 0xff);
        String sim = bean.getSim();
        if (!MyUtils.isNull(sim)) {
            jtfSim.setText(sim);
        }
        jcbXdh.setText(String.valueOf(bean.getChannel()));

    }

    @Override
    protected void clearNetInfo() {
//        jcbType.setSelectedIndex(0);
        jcbNumber.setSelectedIndex(0);
        jtfSim.setText(null);
        jcbXdh.setText(null);
    }

}

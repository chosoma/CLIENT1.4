package view.dataCollect;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;

import com.PlayWAV;
import domain.SensorAttr;
import domain.UnitBean;
import mytools.MyButton;
import mytools.MyButton2;
import mytools.MyUtil;
import service.SysUnitService;
import domain.DataBean;
//import domain.SensorAttr;
//import domain.SensorBean;

/**
 * 单元视图
 */
public class AbcUnitView extends JPanel implements Comparable<AbcUnitView> {
    private UnitBean unitBean;
    private DataBean dataBean;

    public UnitBean getUnitBean() {
        return unitBean;
    }

    String name, type;
    JLabel jlwd, jlmd, jldy, jlyl, jlwy;
    MyButton2 jbsetinit;

    public AbcUnitView(UnitBean unitBean) {
        this.unitBean = unitBean;
        init();
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public boolean matchData(DataBean data) {
        return unitBean.getNumber() == data.getUnitNumber();
    }

    static Color colorWarn = new Color(255, 80, 0);
    static Color colorB = new Color(255, 255, 255);

    public void addData(DataBean data) {
        if (data != null) {
            dataBean = data;
        }
        if (dataBean == null) {
            return;
        }
        List<Boolean> flags = new ArrayList<Boolean>();
        String name = dataBean.getName();
        if (name.equals(SensorAttr.Sensor_SF6)) {
            jlwd.setText(String.valueOf(dataBean.getTemp()));
            if (unitBean.getWarnTemp() != null && dataBean.getTemp() > unitBean.getWarnTemp()) {
                jlwd.setBackground(colorWarn);
                flags.add(true);
            } else {
                jlwd.setBackground(colorB);
            }
            jlmd.setText(String.valueOf(dataBean.getDen()));
            if (unitBean.getMaxden() != null && unitBean.getMinden() != null && (dataBean.getDen() > unitBean.getMaxden() || dataBean.getDen() < unitBean.getMinden())) {
                jlmd.setBackground(colorWarn);
                flags.add(true);
            } else {
                jlmd.setBackground(colorB);
            }
            jlyl.setText(String.valueOf(dataBean.getPres()));
            if (unitBean.getMaxper() != null && unitBean.getMinper() != null && (dataBean.getPres() > unitBean.getMaxper() || dataBean.getPres() < unitBean.getMinper())) {
                jlyl.setBackground(colorWarn);
                flags.add(true);
            } else {
                jlyl.setBackground(colorB);
            }
        } else if (name.equals(SensorAttr.Sensor_SSJ)) {
            jlwy.setText(String.valueOf(((int) (dataBean.getVari() - unitBean.getInitvari()) * 10) / 10.0));
            if (unitBean.getMaxvari() != null && unitBean.getMinvari() != null && (dataBean.getVari() - unitBean.getInitvari() > unitBean.getMaxvari() || dataBean.getVari() - unitBean.getInitvari() < unitBean.getMinvari())) {
                jlwy.setBackground(colorWarn);
                flags.add(true);
            } else {
                jlwy.setBackground(colorB);
            }
        } else {
            jlwd.setText(String.valueOf(dataBean.getTemp()));
            if (unitBean.getWarnTemp() != null && dataBean.getTemp() > unitBean.getWarnTemp()) {
                jlwd.setBackground(colorWarn);
                flags.add(true);
            } else {
                jlwd.setBackground(colorB);
            }
        }
        jldy.setText(String.valueOf(dataBean.getBatlv()));
        for (boolean flag : flags) {
            if (flag) {
                JPanel warnPanel = CollectShow.getWarnPanel();
                if (!warnPanel.isVisible()) warnPanel.setVisible(true);
                PlayWAV.getInstance().play();//报警
                break;
            }
        }

    }

    public void refresh(UnitBean unitBean) {
        this.unitBean = unitBean;
        addData(dataBean);
    }

    static Border border = MyUtil.Component_Border;


    static Dimension size = new Dimension(170, 120);
    JLabel jlbSjbh;

    public void setTitle(String title) {
        jlbSjbh.setText("监测点:" + title);
    }

    private void init() {
        this.setLayout(null);
        this.setPreferredSize(size);

        Color colorTitle, colorSubTitle;
        colorTitle = colorTitle3;
        colorSubTitle = colorSubTitle3;

        jlbSjbh = new JLabel("监测点:" + unitBean.getPlace(), JLabel.CENTER);
        jlbSjbh.setBounds(0, 0, 121, 21);
        jlbSjbh.setBorder(border);
        jlbSjbh.setBackground(colorTitle);
        jlbSjbh.setOpaque(true);
        this.add(jlbSjbh);

        MyButton2 jbreset = new MyButton2("修改");
        jbreset.setBounds(120, 0, 41, 23);
        jbreset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                new SetTitleDialog(null, unitBean);
            }
        });
        this.add(jbreset);

        JLabel jlbBz = new JLabel("监测相位:" + unitBean.getXw(), JLabel.CENTER);
        jlbBz.setBounds(0, 20, 160, 21);
        jlbBz.setBorder(border);
        jlbBz.setBackground(colorSubTitle);
        jlbBz.setOpaque(true);
        this.add(jlbBz);

        JLabel jlbwd = new JLabel("温度", JLabel.CENTER);
        jlbwd.setBorder(border);
        jlbwd.setBackground(colorB);
        jlbwd.setOpaque(true);

        JLabel jlbmd = new JLabel("密度", JLabel.CENTER);
        jlbmd.setBorder(border);
        jlbmd.setBackground(colorB);
        jlbmd.setOpaque(true);


        JLabel jlbyl = new JLabel("压力", JLabel.CENTER);
        jlbyl.setBorder(border);
        jlbyl.setBackground(colorB);
        jlbyl.setOpaque(true);

        JLabel jlbdy = new JLabel("电压", JLabel.CENTER);
        jlbdy.setBorder(border);
        jlbdy.setBackground(colorB);
        jlbdy.setOpaque(true);

        final JLabel jlbwy = new JLabel("偏移量", JLabel.CENTER);
        jlbwy.setBorder(border);
        jlbwy.setBackground(colorB);
        jlbwy.setOpaque(true);

        jlwd = new JLabel("", JLabel.CENTER);
        jlwd.setBorder(border);
        jlwd.setOpaque(true);

        jlmd = new JLabel("", JLabel.CENTER);
        jlmd.setBorder(border);
        jlmd.setOpaque(true);

        jlyl = new JLabel("", JLabel.CENTER);
        jlyl.setBorder(border);

        jldy = new JLabel("", JLabel.CENTER);
        jldy.setBorder(border);
        jldy.setOpaque(true);

        jlwy = new JLabel("", JLabel.CENTER);
        jlwy.setBorder(border);
        jlwy.setOpaque(true);
        String name = unitBean.getName();
        if (name.equals(SensorAttr.Sensor_SF6)) {
            jlbwd.setBounds(0, 40, 41, 21);
            jlbmd.setBounds(40, 40, 41, 21);
            jlbyl.setBounds(80, 40, 41, 21);
            jlbdy.setBounds(120, 40, 40, 21);
            this.add(jlbwd);
            this.add(jlbmd);
            this.add(jlbyl);
            this.add(jlbdy);
            jlwd.setBounds(0, 60, 41, 21);
            jlmd.setBounds(40, 60, 41, 21);
            jlyl.setBounds(80, 60, 41, 21);
            jldy.setBounds(120, 60, 40, 21);
            this.add(jlwd);
            this.add(jlmd);
            this.add(jlyl);
            this.add(jldy);
        } else {
            if (name.equals(SensorAttr.Sensor_SSJ)) {
                jlbwy.setBounds(0, 40, 61, 21);
                this.add(jlbwy);
                jlwy.setBounds(0, 60, 61, 21);
                this.add(jlwy);
                jlbdy.setBounds(60, 40, 61, 21);
                this.add(jlbdy);
                jldy.setBounds(60, 60, 61, 21);
                this.add(jldy);
                jbsetinit = new MyButton2("校零");
                jbsetinit.setBounds(120, 40, 42, 43);
                this.add(jbsetinit);
            } else {
                jlbwd.setBounds(0, 40, 81, 21);
                this.add(jlbwd);
                jlwd.setBounds(0, 60, 81, 21);
                this.add(jlwd);
                jlbdy.setBounds(80, 40, 80, 21);
                this.add(jlbdy);
                jldy.setBounds(80, 60, 80, 21);
                this.add(jldy);
            }

        }
        if (jbsetinit != null) {
            jbsetinit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    String valistr = jlwy.getText();

                    if (valistr == null || valistr.equals("")) {
                        JOptionPane.showMessageDialog(null, "请先获得初始值", "错误", JOptionPane.WARNING_MESSAGE);
                    } else {
                        if (unitBean.getInitvari() != 0.0f) {
                            int flag = JOptionPane.showConfirmDialog(null, "初始值已存在是否覆盖", "提示", JOptionPane.OK_CANCEL_OPTION);
                            if (flag != JOptionPane.OK_OPTION) {
                                return;
                            }
                        }
                        try {
                            UnitBean unit = SysUnitService.getUnitBean(unitBean.getType(), unitBean.getNumber());
                            if (unit == null) {
                                JOptionPane.showMessageDialog(null, "单元不存在,请先添加单元!", "设置失败", JOptionPane.WARNING_MESSAGE);
                                return;
                            } else {
                                unit.setInitvari(dataBean.getVari());
                            }
                            SysUnitService.updateInitvari(unit);
                            unitBean.setInitvari(dataBean.getVari());
                            JOptionPane.showMessageDialog(null, "设置成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                            jlwy.setText("0.0");
                            jlwy.setBackground(colorB);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "存储失败,请稍后重试", "设置失败", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            });
        }

    }

    static Color colorTitle3 = new Color(182, 216, 245);// 138, 191, 237
    static Color colorSubTitle3 = new Color(232, 242, 254);// 182, 216, 245

//    void init2() {
//        this.setLayout(new GridLayout(6, 1, 1, 1));
//        this.setBackground(MyUtil.Component_Border_Color);
//        this.setBorder(border);
//
//        JLabel jlbTitle = new JLabel(name, JLabel.CENTER);
//        jlbTitle.setFont(MyUtil.FONT_20);
//        // jlbTitle.setBorder(border);
//        jlbTitle.setOpaque(true);
//        jlbTitle.setBackground(colorTitle3);
//        this.add(jlbTitle);
//
//        JLabel jlbSubTitle = new JLabel(sjbh, JLabel.CENTER);
//        jlbSubTitle.setFont(MyUtil.FONT_18);
//        // jlbSubTitle.setBorder(border);
//        jlbSubTitle.setBackground(colorSubTitle3);
//        jlbSubTitle.setOpaque(true);
//        this.add(jlbSubTitle);
//
//        JPanel pane1 = new JPanel(new GridLayout(1, 3, 1, 0));
//        pane1.setOpaque(false);
//        this.add(pane1);
//        JPanel pane2 = new JPanel(new GridLayout(1, 3, 1, 0));
//        pane2.setOpaque(false);
//        this.add(pane2);
//        JPanel pane3 = new JPanel(new GridLayout(1, 3, 1, 0));
//        pane3.setOpaque(false);
//        this.add(pane3);
//        JPanel pane4 = new JPanel(new GridLayout(1, 3, 1, 0));
//        pane4.setOpaque(false);
//        this.add(pane4);
//
//        JLabel jlbxw = new JLabel("相位", JLabel.CENTER);
//        jlbxw.setFont(MyUtil.FONT_18);
//        // jlbxw.setBorder(border);
//        jlbxw.setBackground(colorB);
//        jlbxw.setOpaque(true);
//        pane1.add(jlbxw);
//
//        JLabel jlbtype = new JLabel(type, JLabel.CENTER);
//        jlbtype.setFont(MyUtil.FONT_18);
//        // jlbtype.setBorder(border);
//        jlbtype.setBackground(colorB);
//        jlbtype.setOpaque(true);
//        pane1.add(jlbtype);
//
//        JLabel jlbdl = new JLabel("电量", JLabel.CENTER);
//        jlbdl.setFont(MyUtil.FONT_18);
//        // jlbdl.setBorder(border);
//        jlbdl.setBackground(colorB);
//        jlbdl.setOpaque(true);
//        pane1.add(jlbdl);
//
//        JLabel jlwd = new JLabel("A", JLabel.CENTER);
//        jlwd.setFont(MyUtil.FONT_18);
//        // jlwd.setBorder(border);
//        jlwd.setBackground(colorB);
//        jlwd.setOpaque(true);
//        pane2.add(jlwd);
//
//        jlmd = new JLabel("", JLabel.CENTER);
//        jlmd.setFont(MyUtil.FONT_18);
//        // jlmd.setBorder(border);
//        jlmd.setOpaque(true);
//        pane2.add(jlmd);
//
//        jldy = new JLabel("", JLabel.CENTER);
//        jldy.setFont(MyUtil.FONT_18);
//        // jldy.setBorder(border);
//        jldy.setOpaque(true);
//        pane2.add(jldy);
//
//        JLabel jlb2 = new JLabel("B", JLabel.CENTER);
//        jlb2.setFont(MyUtil.FONT_18);
//        // jlb2.setBorder(border);
//        jlb2.setBackground(colorB);
//        jlb2.setOpaque(true);
//        pane3.add(jlb2);
//
//        jlbB = new JLabel("", JLabel.CENTER);
//        jlbB.setFont(MyUtil.FONT_18);
//        // jlbB.setBorder(border);
//        jlbB.setOpaque(true);
//        pane3.add(jlbB);
//
//        jlbDyB = new JLabel("", JLabel.CENTER);
//        jlbDyB.setFont(MyUtil.FONT_18);
//        // jlbDyB.setBorder(border);
//        jlbDyB.setOpaque(true);
//        pane3.add(jlbDyB);
//
//        JLabel jlb3 = new JLabel("C", JLabel.CENTER);
//        jlb3.setFont(MyUtil.FONT_18);
//        // jlb3.setBorder(border);
//        jlb3.setBackground(colorB);
//        jlb3.setOpaque(true);
//        pane4.add(jlb3);
//
//        jlbC = new JLabel("", JLabel.CENTER);
//        jlbC.setFont(MyUtil.FONT_18);
//        // jlbC.setBorder(border);
//        jlbC.setOpaque(true);
//        pane4.add(jlbC);
//
//        jlbDyC = new JLabel("", JLabel.CENTER);
//        jlbDyC.setFont(MyUtil.FONT_18);
//        // jlbDyC.setBorder(border);
//        jlbDyC.setOpaque(true);
//        pane4.add(jlbDyC);
//
//    }

    @Override
    public int compareTo(AbcUnitView o) {

        int u1 = unitBean.getNumber();
        int u2 = o.unitBean.getNumber();
        if (u1 < 0) {
            u1 += 256;
        }
        if (u2 < 0) {
            u2 += 256;
        }
        return u1 - u2;
    }


}

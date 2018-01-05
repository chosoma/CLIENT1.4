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
import data.FormatTransfer;
import domain.PointBean;
import domain.SensorAttr;
import domain.UnitBean;
import mytools.MyButton2;
import mytools.MyUtil;
import service.SysUnitService;
import domain.DataBean;

/**
 * 单元视图
 */
public class AbcUnitView extends JPanel
//        implements Comparable<AbcUnitView>
{
    //    private UnitBean unitBean;
//    private DataBean dataBean;
    private List<UnitBean> units;

//    public UnitBean getUnitBean() {
//        return unitBean;
//    }

    private String type;
    private JLabel jlwda, jlmda, jldya, jlyla, jlwya;
    private JLabel jlwdb, jlmdb, jldyb, jlylb, jlwyb;
    private JLabel jlwdc, jlmdc, jldyc, jlylc, jlwyc;
    private MyButton2 jbsetinita, jbsetinitb, jbsetinitc;

    private PointBean pointBean;

    public PointBean getPointBean() {
        return pointBean;
    }

//    public AbcUnitView(UnitBean unitBean) {
//        this.unitBean = unitBean;
//        init();
//    }

    public AbcUnitView(PointBean pointBean, List<UnitBean> units) {
        this.pointBean = pointBean;
        this.units = units;
        flags = new ArrayList<Boolean>();
        init();
    }


    public String getType() {
        return type;
    }

    public boolean matchData(DataBean data) {

        for (UnitBean unit : units) {
            System.out.println(unit);
            if (unit.getNumber() == data.getUnitNumber()) {
                return true;
            }
        }
        return false;
    }

    static Color colorWarn = new Color(255, 80, 0);
    static Color colorB = new Color(255, 255, 255);

    public void addData(DataBean data) {
        flags.clear();
        byte unitnumber = data.getUnitNumber();
        UnitBean unit = getUnitBean(unitnumber);
        if (unit == null) {
            return;
        }
        switch (unit.getXw()) {
            case "A":
                addDataA(unit, data);
                break;
            case "B":
                addDataB(unit, data);
                break;
            case "C":
                addDataC(unit, data);
                break;
        }
        for (boolean flag : flags) {
            if (flag) {
                JPanel warnPanel = CollectShow.getInstance().getWarnPanel();
                CollectShow.getInstance().setPlace(pointBean.getPlace() + ":" + unit.getXw());
                if (!warnPanel.isVisible()) warnPanel.setVisible(true);
                PlayWAV.getInstance().play();//报警
                break;
            }
        }
//        if (data != null) {
//            dataBean = data;
//        }
//        if (dataBean == null) {
//            return;
//        }

    }

//    public void refresh(UnitBean unitBean) {
//        this.unitBean = unitBean;
//        addData(dataBean);
//    }

    static Border border = MyUtil.Component_Border;


    static Dimension size = new Dimension(210, 120);
    JLabel jlbSjbh;

    public void setTitle(String title) {
        jlbSjbh.setText(title);
    }

    private void init() {
        this.setLayout(null);
        this.setPreferredSize(size);

        Color colorTitle, colorSubTitle;
        colorTitle = colorTitle3;
        colorSubTitle = colorSubTitle3;

        jlbSjbh = new JLabel(pointBean.getPlace(), JLabel.CENTER);
//        jlbSjbh = new JLabel("监测点:" + unitBean.getPlace(), JLabel.CENTER);
        jlbSjbh.setBounds(0, 0, 161, 21);
        jlbSjbh.setBorder(border);
        jlbSjbh.setBackground(colorTitle);
        jlbSjbh.setOpaque(true);
        this.add(jlbSjbh);

        MyButton2 jbreset = new MyButton2("修改");
        jbreset.setBounds(160, 0, 41, 23);
        jbreset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                new SetTitleDialog(null, pointBean);
            }
        });
        this.add(jbreset);


        JLabel jlbwd = new JLabel("温度", JLabel.CENTER);
        jlbwd.setBorder(border);
        jlbwd.setBackground(colorSubTitle3);
        jlbwd.setOpaque(true);

        JLabel jlbmd = new JLabel("密度", JLabel.CENTER);
        jlbmd.setBorder(border);
        jlbmd.setBackground(colorSubTitle3);
        jlbmd.setOpaque(true);


        JLabel jlbyl = new JLabel("压力", JLabel.CENTER);
        jlbyl.setBorder(border);
        jlbyl.setBackground(colorSubTitle3);
        jlbyl.setOpaque(true);

        JLabel jlbdy = new JLabel("电压", JLabel.CENTER);
        jlbdy.setBorder(border);
        jlbdy.setBackground(colorSubTitle3);
        jlbdy.setOpaque(true);

        JLabel jlbwy = new JLabel("偏移量", JLabel.CENTER);
        jlbwy.setBorder(border);
        jlbwy.setBackground(colorSubTitle3);
        jlbwy.setOpaque(true);

        jlwda = new JLabel("", JLabel.CENTER);
        jlwda.setBorder(border);
        jlwda.setOpaque(true);

        jlmda = new JLabel("", JLabel.CENTER);
        jlmda.setBorder(border);
        jlmda.setOpaque(true);

        jlyla = new JLabel("", JLabel.CENTER);
        jlyla.setBorder(border);
        jlyla.setOpaque(true);

        jldya = new JLabel("", JLabel.CENTER);
        jldya.setBorder(border);
        jldya.setOpaque(true);

        jlwya = new JLabel("", JLabel.CENTER);
        jlwya.setBorder(border);
        jlwya.setOpaque(true);

        jlwdb = new JLabel("", JLabel.CENTER);
        jlwdb.setBorder(border);
        jlwdb.setOpaque(true);

        jlmdb = new JLabel("", JLabel.CENTER);
        jlmdb.setBorder(border);
        jlmdb.setOpaque(true);

        jlylb = new JLabel("", JLabel.CENTER);
        jlylb.setBorder(border);
        jlylb.setOpaque(true);

        jldyb = new JLabel("", JLabel.CENTER);
        jldyb.setBorder(border);
        jldyb.setOpaque(true);

        jlwyb = new JLabel("", JLabel.CENTER);
        jlwyb.setBorder(border);
        jlwyb.setOpaque(true);

        jlwdc = new JLabel("", JLabel.CENTER);
        jlwdc.setBorder(border);
        jlwdc.setOpaque(true);

        jlmdc = new JLabel("", JLabel.CENTER);
        jlmdc.setBorder(border);
        jlmdc.setOpaque(true);

        jlylc = new JLabel("", JLabel.CENTER);
        jlylc.setBorder(border);
        jlylc.setOpaque(true);

        jldyc = new JLabel("", JLabel.CENTER);
        jldyc.setBorder(border);
        jldyc.setOpaque(true);

        jlwyc = new JLabel("", JLabel.CENTER);
        jlwyc.setBorder(border);
        jlwyc.setOpaque(true);

        addXwA(40);
        addXwB(60);
        addXwC(80);
        int x = 0;
        int y = 20;
        JLabel jlbxw = new JLabel("相位", JLabel.CENTER);
        jlbxw.setBorder(border);
        jlbxw.setBackground(colorSubTitle3);
        jlbxw.setOpaque(true);
        jlbxw.setBounds(x, y, 41, 21);
        x += 40;
        this.add(jlbxw);
        if (this.pointBean.getUnitType() == 1) {
            jlbwd.setBounds(x, y, 41, 21);
            x += 40;
            jlbmd.setBounds(x, y, 41, 21);
            x += 40;
            jlbyl.setBounds(x, y, 41, 21);
            x += 40;
            jlbdy.setBounds(x, y, 40, 21);
            x += 40;
            this.add(jlbwd);
            this.add(jlbmd);
            this.add(jlbyl);
            this.add(jlbdy);
        } else {
            if (this.pointBean.getUnitType() == 2) {
                jlbwy.setBounds(x, y, 61, 21);
                x += 60;
                jlbdy.setBounds(x, y, 61, 21);
                x += 60;
                JLabel jLabel = new JLabel("");
                jLabel.setBorder(border);
                jLabel.setOpaque(true);
                jLabel.setBackground(colorSubTitle);
                jLabel.setBounds(x, y, 40, 21);
                this.add(jLabel);
                this.add(jlbwy);
                this.add(jlbdy);
            } else {
                jlbwd.setBounds(x, y, 81, 21);
                x += 80;
                jlbdy.setBounds(x, y, 80, 21);
                x += 80;
                this.add(jlbwd);
                this.add(jlbdy);
            }
        }

    }

    private static Color colorTitle3 = new Color(182, 216, 245);// 138, 191, 237
    private static Color colorSubTitle3 = new Color(232, 242, 254);// 182, 216, 245

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

    private void addXwA(int y) {
        JLabel jlba = new JLabel("A", JLabel.CENTER);
        jlba.setBorder(border);
        jlba.setOpaque(true);
        jlba.setBackground(colorSubTitle3);
        int x = 0;
        jlba.setBounds(x, y, 41, 21);
        this.add(jlba);
        x += 40;

        if (this.pointBean.getUnitType() == 1) {
            jlwda.setBounds(x, y, 41, 21);
            x += 40;
            jlmda.setBounds(x, y, 41, 21);
            x += 40;
            jlyla.setBounds(x, y, 41, 21);
            x += 40;
            jldya.setBounds(x, y, 40, 21);
            x += 40;
            this.add(jlwda);
            this.add(jlmda);
            this.add(jlyla);
            this.add(jldya);
        } else {
            if (this.pointBean.getUnitType() == 2) {
                jlwya.setBounds(x, y, 61, 21);
                x += 60;
                jldya.setBounds(x, y, 61, 21);
                x += 60;
                this.add(jlwya);
                this.add(jldya);
                jbsetinita = new MyButton2("校零");
                jbsetinita.setBounds(x, y, 42, 23);
                x += 40;
                this.add(jbsetinita);
            } else {
                jlwda.setBounds(x, y, 81, 21);
                x += 80;
                jldya.setBounds(x, y, 80, 21);
                x += 80;
                this.add(jlwda);
                this.add(jldya);
            }

        }
        if (jbsetinita != null) {
            jbsetinita.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    String valistr = jlwya.getText();
                    if (valistr == null || valistr.equals("")) {
                        JOptionPane.showMessageDialog(null, "请先获得初始值", "错误", JOptionPane.WARNING_MESSAGE);
                    } else {
                        UnitBean unitBean = getUnitBean("A");
                        if (unitBean == null) {
                            return;
                        }
                        updateUnit(unitBean, "A");
                    }
                }
            });
        }
    }

    private void addXwB(int y) {
        JLabel jlba = new JLabel("B", JLabel.CENTER);
        jlba.setBorder(border);
        jlba.setOpaque(true);
        jlba.setBackground(colorSubTitle3);
        int x = 0;
        jlba.setBounds(x, y, 41, 21);
        this.add(jlba);
        x += 40;
        if (this.pointBean.getUnitType() == 1) {
            jlwdb.setBounds(x, y, 41, 21);
            x += 40;
            jlmdb.setBounds(x, y, 41, 21);
            x += 40;
            jlylb.setBounds(x, y, 41, 21);
            x += 40;
            jldyb.setBounds(x, y, 40, 21);
            x += 40;
            this.add(jlwdb);
            this.add(jlmdb);
            this.add(jlylb);
            this.add(jldyb);
        } else {
            if (this.pointBean.getUnitType() == 2) {
                jlwyb.setBounds(x, y, 61, 21);
                x += 60;
                jldyb.setBounds(x, y, 61, 21);
                x += 60;
                this.add(jlwyb);
                this.add(jldyb);
                jbsetinitb = new MyButton2("校零");
                jbsetinitb.setBounds(x, y, 42, 23);
                x += 40;
                this.add(jbsetinitb);
            } else {
                jlwdb.setBounds(x, y, 81, 21);
                x += 80;
                jldyb.setBounds(x, y, 80, 21);
                x += 80;
                this.add(jlwdb);
                this.add(jldyb);
            }
        }
        if (jbsetinitb != null) {
            jbsetinitb.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    String valistr = jlwyb.getText();

                    if (valistr == null || valistr.equals("")) {
                        JOptionPane.showMessageDialog(null, "请先获得初始值", "错误", JOptionPane.WARNING_MESSAGE);
                    } else {
                        UnitBean unitBean = getUnitBean("B");
                        if (unitBean == null) {
                            return;
                        }
                        updateUnit(unitBean, "B");
                    }
                }
            });
        }
    }

    private void addXwC(int y) {
        JLabel jlba = new JLabel("C", JLabel.CENTER);
        jlba.setBorder(border);
        jlba.setOpaque(true);
        jlba.setBackground(colorSubTitle3);
        int x = 0;
        jlba.setBounds(x, y, 41, 21);
        this.add(jlba);
        x += 40;
        if (this.pointBean.getUnitType() == 1) {
            jlwdc.setBounds(x, y, 41, 21);
            x += 40;
            jlmdc.setBounds(x, y, 41, 21);
            x += 40;
            jlylc.setBounds(x, y, 41, 21);
            x += 40;
            jldyc.setBounds(x, y, 40, 21);
            x += 40;
            this.add(jlwdc);
            this.add(jlmdc);
            this.add(jlylc);
            this.add(jldyc);
        } else {
            if (this.pointBean.getUnitType() == 2) {
                jlwyc.setBounds(x, y, 61, 21);
                x += 60;
                jldyc.setBounds(x, y, 61, 21);
                x += 60;
                this.add(jlwyc);
                this.add(jldyc);
                jbsetinitc = new MyButton2("校零");
                jbsetinitc.setBounds(x, y, 42, 23);
                x += 40;
                this.add(jbsetinitc);
            } else {
                jlwdc.setBounds(x, y, 81, 21);
                x += 80;
                this.add(jlwdc);
                jldyc.setBounds(x, y, 80, 21);
                x += 80;
                this.add(jldyc);
            }

        }
        if (jbsetinitc != null) {
            jbsetinitc.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    String valistr = jlwyc.getText();
                    if (valistr == null || valistr.equals("")) {
                        JOptionPane.showMessageDialog(null, "请先获得初始值", "错误", JOptionPane.WARNING_MESSAGE);
                    } else {
                        UnitBean unitBean = getUnitBean("C");
                        if (unitBean == null) {
                            return;
                        }
                        updateUnit(unitBean, "C");
                    }
                }
            });
        }
    }

    private List<Boolean> flags;

    private void addDataA(UnitBean unitBean, DataBean dataBean) {
        byte type = dataBean.getUnitType();
        switch (type) {
            case 1:
                jlwda.setText(String.valueOf(dataBean.getTemp()));
                if (unitBean.getWarnTemp() != null && dataBean.getTemp() > unitBean.getWarnTemp()) {
                    jlwda.setBackground(colorWarn);
                    flags.add(true);
                } else {
                    jlwda.setBackground(colorB);
                }
                jlmda.setText(String.valueOf(dataBean.getDen()));
                if (unitBean.getMaxden() != null && unitBean.getMinden() != null && (dataBean.getDen() > unitBean.getMaxden() || dataBean.getDen() < unitBean.getMinden())) {
                    jlmda.setBackground(colorWarn);
                    flags.add(true);
                } else {
                    jlmda.setBackground(colorB);
                }
                jlyla.setText(String.valueOf(dataBean.getPres()));
                if (unitBean.getMaxper() != null && unitBean.getMinper() != null && (dataBean.getPres() > unitBean.getMaxper() || dataBean.getPres() < unitBean.getMinper())) {
                    jlyla.setBackground(colorWarn);
                    flags.add(true);
                } else {
                    jlyla.setBackground(colorB);
                }
                break;
            case 2:
                float vari = FormatTransfer.newScale(dataBean.getVari(), unitBean.getInitvari());
                if (dataBean.getVari() >= 65535) {
                    jlwya.setText("断连");
                } else {
                    jlwya.setText(String.valueOf(vari));
                }
                if (unitBean.getMaxvari() != null && unitBean.getMinvari() != null && (vari > unitBean.getMaxvari() || vari < unitBean.getMinvari())) {
                    jlwya.setBackground(colorWarn);
                    flags.add(true);
                } else {
                    jlwya.setBackground(colorB);
                }
                break;
            case 3:
                jlwda.setText(String.valueOf(dataBean.getTemp()));
                if (unitBean.getWarnTemp() != null && dataBean.getTemp() > unitBean.getWarnTemp()) {
                    jlwda.setBackground(colorWarn);
                    flags.add(true);
                } else {
                    jlwda.setBackground(colorB);
                }
                break;
        }
        jldya.setText(String.valueOf(dataBean.getBatlv()));

    }

    private void addDataB(UnitBean unitBean, DataBean dataBean) {
        byte type = dataBean.getUnitType();
        switch (type) {
            case 1:
                jlwdb.setText(String.valueOf(dataBean.getTemp()));
                if (unitBean.getWarnTemp() != null && dataBean.getTemp() > unitBean.getWarnTemp()) {
                    jlwdb.setBackground(colorWarn);
                    flags.add(true);
                } else {
                    jlwdb.setBackground(colorB);
                }
                jlmdb.setText(String.valueOf(dataBean.getDen()));
                if (unitBean.getMaxden() != null && unitBean.getMinden() != null && (dataBean.getDen() > unitBean.getMaxden() || dataBean.getDen() < unitBean.getMinden())) {
                    jlmdb.setBackground(colorWarn);
                    flags.add(true);
                } else {
                    jlmdb.setBackground(colorB);
                }
                jlylb.setText(String.valueOf(dataBean.getPres()));
                if (unitBean.getMaxper() != null && unitBean.getMinper() != null && (dataBean.getPres() > unitBean.getMaxper() || dataBean.getPres() < unitBean.getMinper())) {
                    jlylb.setBackground(colorWarn);
                    flags.add(true);
                } else {
                    jlylb.setBackground(colorB);
                }
                break;
            case 2:
                float vari = FormatTransfer.newScale(dataBean.getVari(), unitBean.getInitvari());
                if (dataBean.getVari() >= 65535) {
                    jlwyb.setText("断连");
                } else {
                    jlwyb.setText(String.valueOf(vari));
                }
                if (unitBean.getMaxvari() != null && unitBean.getMinvari() != null && (vari > unitBean.getMaxvari() || vari < unitBean.getMinvari())) {
                    jlwyb.setBackground(colorWarn);
                    flags.add(true);
                } else {
                    jlwyb.setBackground(colorB);
                }
                break;
            case 3:
                jlwdb.setText(String.valueOf(dataBean.getTemp()));
                if (unitBean.getWarnTemp() != null && dataBean.getTemp() > unitBean.getWarnTemp()) {
                    jlwdb.setBackground(colorWarn);
                    flags.add(true);
                } else {
                    jlwdb.setBackground(colorB);
                }
                break;
        }
        jldyb.setText(String.valueOf(dataBean.getBatlv()));
    }

    private void addDataC(UnitBean unitBean, DataBean dataBean) {
        byte type = dataBean.getUnitType();
        switch (type) {
            case 1:
                jlwdc.setText(String.valueOf(dataBean.getTemp()));
                if (unitBean.getWarnTemp() != null && dataBean.getTemp() > unitBean.getWarnTemp()) {
                    jlwdc.setBackground(colorWarn);
                    flags.add(true);
                } else {
                    jlwdc.setBackground(colorB);
                }
                jlmdc.setText(String.valueOf(dataBean.getDen()));
                if (unitBean.getMaxden() != null && unitBean.getMinden() != null && (dataBean.getDen() > unitBean.getMaxden() || dataBean.getDen() < unitBean.getMinden())) {
                    jlmdc.setBackground(colorWarn);
                    flags.add(true);
                } else {
                    jlmdc.setBackground(colorB);
                }
                jlylc.setText(String.valueOf(dataBean.getPres()));
                if (unitBean.getMaxper() != null && unitBean.getMinper() != null && (dataBean.getPres() > unitBean.getMaxper() || dataBean.getPres() < unitBean.getMinper())) {
                    jlylc.setBackground(colorWarn);
                    flags.add(true);
                } else {
                    jlylc.setBackground(colorB);
                }
                break;
            case 2:
                float vari = FormatTransfer.newScale(dataBean.getVari(), unitBean.getInitvari());
                if (dataBean.getVari() >= 65535) {
                    jlwyc.setText("断连");
                } else {
                    jlwyc.setText(String.valueOf(vari));
                }
                if (unitBean.getMaxvari() != null && unitBean.getMinvari() != null && (vari > unitBean.getMaxvari() || vari < unitBean.getMinvari())) {
                    jlwyc.setBackground(colorWarn);
                    flags.add(true);
                } else {
                    jlwyc.setBackground(colorB);
                }
                break;
            case 3:
                jlwdc.setText(String.valueOf(dataBean.getTemp()));
                if (unitBean.getWarnTemp() != null && dataBean.getTemp() > unitBean.getWarnTemp()) {
                    jlwdc.setBackground(colorWarn);
                    flags.add(true);
                } else {
                    jlwdc.setBackground(colorB);
                }
                break;
        }
        jldyc.setText(String.valueOf(dataBean.getBatlv()));
    }

//    @Override
//    public int compareTo(AbcUnitView o) {
//
//        int u1 = unitBean.getNumber();
//        int u2 = o.unitBean.getNumber();
//        if (u1 < 0) {
//            u1 += 256;
//        }
//        if (u2 < 0) {
//            u2 += 256;
//        }
//        return u1 - u2;
//    }

    private UnitBean getUnitBean(String xw) {
        for (UnitBean unit : units) {
            if (unit.getXw().equals(xw)) {
                return unit;
            }
        }
        return null;
    }

    private UnitBean getUnitBean(byte number) {
        for (UnitBean unit : units) {
            if (unit.getNumber() == number) {
                return unit;
            }
        }
        return null;
    }

    private void updateUnit(UnitBean unitBean, String xw) {
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
            }
            switch (xw) {
                case "A":
                    unit.setInitvari(Float.parseFloat(jlwya.getText()));
                    break;
                case "B":
                    unit.setInitvari(Float.parseFloat(jlwyb.getText()));
                    break;
                case "C":
                    unit.setInitvari(Float.parseFloat(jlwyc.getText()));
                    break;
            }
            SysUnitService.updateInitvari(unit);
            ChartView.getInstance().alignZero(unitBean);
            switch (xw) {
                case "A":
                    jlwya.setText("0.0");
                    jlwya.setBackground(colorB);
                    break;
                case "B":
                    jlwyb.setText("0.0");
                    jlwyb.setBackground(colorB);
                    break;
                case "C":
                    jlwyc.setText("0.0");
                    jlwyc.setBackground(colorB);
                    break;
            }
            JOptionPane.showMessageDialog(null, "设置成功", "成功", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "存储失败,请稍后重试", "设置失败", JOptionPane.WARNING_MESSAGE);
        }
    }

}

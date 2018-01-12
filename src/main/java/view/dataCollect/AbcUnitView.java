package view.dataCollect;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;

import com.PlayWAV;
import data.FormatTransfer;
import domain.PointBean;
import domain.UnitBean;
import mytools.MyButton2;
import mytools.MyUtil;
import service.SysUnitService;
import domain.DataBean;
import view.Shell;

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
    private JLabel jlwda, jlmda, jldya, jlyla, jlwya, jljza;
    private JLabel[] jlas = new JLabel[6]; //0 温度 1 密度 2 压力 3 电压 4 位移 5 校准
    private JLabel jlwdb, jlmdb, jldyb, jlylb, jlwyb, jljzb;
    private JLabel[] jlbs = new JLabel[6]; //0 温度 1 密度 2 压力 3 电压 4 位移 5 校准
    private JLabel jlwdc, jlmdc, jldyc, jlylc, jlwyc, jljzc;
    private JLabel[] jlcs = new JLabel[6]; //0 温度 1 密度 2 压力 3 电压 4 位移 5 校准
    private MyButton2 jbsetinita, jbsetinitb, jbsetinitc;
    private MyButton2[] jbsetinits = new MyButton2[3];
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
        this.setLayout(null);
        this.setPreferredSize(size);
        init2();
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

    private DataBean dataBeana, dataBeanb, dataBeanc;
    private DataBean[] dataBeans = new DataBean[3];
    static Color colorWarn = new Color(255, 80, 0);
    static Color colorB = new Color(255, 255, 255);

    public void addData2(DataBean data) {
        flags.clear();
        byte unitnumber = data.getUnitNumber();
        UnitBean unit = getUnitBean(unitnumber);
        if (unit == null) {
            return;
        }
        switch (unit.getXw()) {
            case "A":
                dataBeana = data;
                addDataA(unit, data);
                break;
            case "B":
                dataBeanb = data;
                addDataB(unit, data);
                break;
            case "C":
                dataBeanc = data;
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

    private static Border border = MyUtil.Component_Border;


    private static Dimension size = new Dimension(210, 120);
    private JLabel jlbSjbh;

    public void setTitle(String title) {
        switch (pointBean.getUnitType()) {
            case 1:
                jlbSjbh.setText("SF6:" + title);
                break;
            case 2:
                jlbSjbh.setText("伸缩节:" + title);
                break;
            case 3:
                jlbSjbh.setText("温度:" + title);
                break;
        }

    }

    private void init() {


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

        JLabel jlbjz = new JLabel("基准值", JLabel.CENTER);
        jlbjz.setBorder(border);
        jlbjz.setBackground(colorSubTitle3);
        jlbjz.setOpaque(true);

        JLabel jlbwy = new JLabel("偏移量", JLabel.CENTER);
        jlbwy.setBorder(border);
        jlbwy.setBackground(colorSubTitle3);
        jlbwy.setOpaque(true);

        jljza = new JLabel("", JLabel.CENTER);
        jljzb = new JLabel("", JLabel.CENTER);
        jljzc = new JLabel("", JLabel.CENTER);

        jljza.setBorder(border);
        jljzb.setBorder(border);
        jljzc.setBorder(border);

        jljza.setOpaque(true);
        jljzb.setOpaque(true);
        jljzc.setOpaque(true);

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
                jlbjz.setBounds(x, y, 41, 21);
                x += 40;
                jlbwy.setBounds(x, y, 41, 21);
                x += 40;
                jlbdy.setBounds(x, y, 41, 21);
                x += 40;
                JLabel jLabel = new JLabel("");
                jLabel.setBorder(border);
                jLabel.setOpaque(true);
                jLabel.setBackground(colorSubTitle);
                jLabel.setBounds(x, y, 40, 21);
                this.add(jlbjz);
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
        setInit();
    }

    private void setInit() {
        if (pointBean.getPoint() != 0) {
            return;
        }
        for (UnitBean unit : units) {
            in:
            switch (unit.getXw()) {
                case "A":
                    jljza.setText(String.valueOf(unit.getInitvari()));
                    break in;
                case "B":
                    jljzb.setText(String.valueOf(unit.getInitvari()));
                    break in;
                case "C":
                    jljzc.setText(String.valueOf(unit.getInitvari()));
                    break in;
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
                jljza.setBounds(x, y, 41, 21);
                x += 40;
                jlwya.setBounds(x, y, 41, 21);
                x += 40;
                jldya.setBounds(x, y, 41, 21);
                x += 40;
                this.add(jljza);
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
                jljzb.setBounds(x, y, 41, 21);
                x += 40;
                jlwyb.setBounds(x, y, 41, 21);
                x += 40;
                jldyb.setBounds(x, y, 41, 21);
                x += 40;
                this.add(jljzb);
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
                jljzc.setBounds(x, y, 41, 21);
                x += 40;
                jlwyc.setBounds(x, y, 41, 21);
                x += 40;
                jldyc.setBounds(x, y, 41, 21);
                x += 40;
                this.add(jljzc);
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
                if (dataBean.isLowPres()) {
                    jlmda.setBackground(colorWarn);
                    flags.add(true);
                    jlmda.setText("低压");
                    break;
                } else {
                    jlmda.setText(String.valueOf(dataBean.getDen()));
                }
                if (unitBean.getMaxden() != null && unitBean.getMinden() != null && (dataBean.getDen() > unitBean.getMaxden() || dataBean.getDen() < unitBean.getMinden())) {
                    jlmda.setBackground(colorWarn);
                    flags.add(true);
                } else {
                    jlmda.setBackground(colorB);
                }
                if (dataBean.isLowLock()) {
                    jlyla.setText("闭锁");
                    jlyla.setBackground(colorWarn);
                    flags.add(true);
                    break;
                } else {
                    jlyla.setText(String.valueOf(dataBean.getPres()));
                }
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
                if (dataBean.isLowPres()) {
                    jlmdb.setBackground(colorWarn);
                    flags.add(true);
                    jlmdb.setText("低压");
                    break;
                } else {
                    jlmdb.setText(String.valueOf(dataBean.getDen()));
                }
                if (unitBean.getMaxden() != null && unitBean.getMinden() != null && (dataBean.getDen() > unitBean.getMaxden() || dataBean.getDen() < unitBean.getMinden())) {
                    jlmdb.setBackground(colorWarn);
                    flags.add(true);
                } else {
                    jlmdb.setBackground(colorB);
                }
                if (dataBean.isLowLock()) {
                    jlylb.setText("闭锁");
                    jlylb.setBackground(colorWarn);
                    flags.add(true);
                    break;
                } else {
                    jlylb.setText(String.valueOf(dataBean.getPres()));
                }
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
                if (dataBean.isLowPres()) {
                    jlmdc.setBackground(colorWarn);
                    flags.add(true);
                    jlmdc.setText("低压");
                    break;
                } else {
                    jlmdc.setText(String.valueOf(dataBean.getDen()));
                }
                if (unitBean.getMaxden() != null && unitBean.getMinden() != null && (dataBean.getDen() > unitBean.getMaxden() || dataBean.getDen() < unitBean.getMinden())) {
                    jlmdc.setBackground(colorWarn);
                    flags.add(true);
                } else {
                    jlmdc.setBackground(colorB);
                }
                if (dataBean.isLowLock()) {
                    jlylc.setText("闭锁");
                    jlylc.setBackground(colorWarn);
                    flags.add(true);
                    break;
                } else {
                    jlylc.setText(String.valueOf(dataBean.getPres()));
                }
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

    public void addData(DataBean data) {
        flags.clear();
        warningstr.delete(0, warningstr.length());
        initWarning();
        byte unitnumber = data.getUnitNumber();
        UnitBean unit = getUnitBean(unitnumber);
        if (unit == null) {
            return;
        }
        addData(unit, data);
        checkWarning(unit, data);
        switch (unit.getXw()) {
            case "A":
                dataBeans[0] = data;
                break;
            case "B":
                dataBeans[1] = data;
                break;
            case "C":
                dataBeans[2] = data;
                break;
        }
        if (flags.size() > 0) {
            Shell.getInstance().showPanel(unit.getType());
            JPanel warnPanel = CollectShow.getInstance().getWarnPanel();
            warningstr.append("\n--");
            warningstr.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(data.getDate()));
            CollectShow.getInstance().setPlace(warningstr.toString());
            if (!warnPanel.isVisible()) warnPanel.setVisible(true);
            PlayWAV.getInstance().play();//报警
        }

    }


    private void init2() {
//        initWarning();
        initTitle();
        int y = 40;
        for (int i = 0; i < jlas.length; i++) {
            jlas[i] = new JLabel("", JLabel.CENTER);
            jlas[i].setBorder(border);
            jlas[i].setOpaque(true);
            this.add(jlas[i]);
        }
        initValueLabel(jlas, y);
        y += 20;
        for (int i = 0; i < jlbs.length; i++) {
            jlbs[i] = new JLabel("", JLabel.CENTER);
            jlbs[i].setBorder(border);
            jlbs[i].setOpaque(true);
            this.add(jlbs[i]);
        }
        initValueLabel(jlbs, y);
        y += 20;
        for (int i = 0; i < jlcs.length; i++) {
            jlcs[i] = new JLabel("", JLabel.CENTER);
            jlcs[i].setBorder(border);
            jlcs[i].setOpaque(true);
            this.add(jlcs[i]);
        }
        initValueLabel(jlcs, y);
        if (pointBean.getUnitType() == 2) {
            setInitVari();
            y = 40;
            for (int i = 0, x = 160; i < jbsetinits.length; i++, y += 20) {
                jbsetinits[i] = new MyButton2("校零");
                jbsetinits[i].setBounds(x, y, 42, 23);
                this.add(jbsetinits[i]);
                final int index = i;
                jbsetinits[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        if (dataBeans[index] == null) {
                            JOptionPane.showMessageDialog(null, "请先获得初始值", "错误", JOptionPane.WARNING_MESSAGE);
                        } else if (dataBeans[index].getVari() > 125 || dataBeans[index].getVari() < 0) {
                            JOptionPane.showMessageDialog(null, "该数据无效,请重新获取", "错误", JOptionPane.WARNING_MESSAGE);
                        } else {
                            UnitBean unitBean = getUnitBean(index);
                            if (unitBean == null) {
                                return;
                            }
                            updateUnit(unitBean, index);
                        }
                    }
                });
            }
        }
    }

    private StringBuilder warningstr = new StringBuilder();

    private void initWarning() {
        switch (pointBean.getUnitType()) {
            case 1:
                warningstr.append("监测类型:SF6,");
                break;
            case 2:
                warningstr.append("监测类型:伸缩节,");
                break;
            case 3:
                warningstr.append("监测类型:温度,");
                break;
        }
        warningstr.append("监测点:");
        warningstr.append(pointBean.getPlace());
        warningstr.append(",相位:");
    }


    private void initTitle() {


        jlbSjbh = new JLabel("", JLabel.CENTER);
        setTitle(pointBean.getPlace());
//        jlbSjbh = new JLabel("监测点:" + unitBean.getPlace(), JLabel.CENTER);
        jlbSjbh.setBounds(0, 0, 161, 21);
        jlbSjbh.setBorder(border);
        jlbSjbh.setBackground(colorTitle3);
        jlbSjbh.setOpaque(true);
        this.add(jlbSjbh);

        MyButton2 jbreset = new MyButton2("修改");
        jbreset.setBounds(160, 0, 42, 23);
        jbreset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                new SetTitleDialog(null, pointBean);
            }
        });
        this.add(jbreset);
        JLabel[] jltitles = new JLabel[6];//0 温度 1 密度 2 压力 3 电压 4 位移 5 校准
        for (int i = 0; i < jltitles.length; i++) {
            String str = "";
            switch (i) {
                case 0:
                    str = "温度";
                    break;
                case 1:
                    str = "密度";
                    break;
                case 2:
                    str = "压力";
                    break;
                case 3:
                    str = "电压";
                    break;
                case 4:
                    str = "偏移量";
                    break;
                case 5:
                    str = "基准值";
                    break;
            }
            jltitles[i] = new JLabel(str, JLabel.CENTER);
            jltitles[i].setBorder(border);
            jltitles[i].setBackground(colorSubTitle3);
            jltitles[i].setOpaque(true);
        }
        int x = 0;
        int y = 20;
        int width = 41;
        int height = 21;
        JLabel jlbxw = new JLabel("相位", JLabel.CENTER);
        jlbxw.setBorder(border);
        jlbxw.setBackground(colorSubTitle3);
        jlbxw.setOpaque(true);
        jlbxw.setBounds(x, y, width, height);
        x += 40;
        this.add(jlbxw);
        switch (this.pointBean.getUnitType()) {
            case 1:
                for (int i = 0; i <= 3; i++, x += 40) {
                    jltitles[i].setBounds(x, y, width, height);
                    this.add(jltitles[i]);
                }
                break;
            case 2:
                for (int i = 5; i >= 3; i--, x += 40) {
                    jltitles[i].setBounds(x, y, width, height);
                    this.add(jltitles[i]);
                }
                JLabel jLabel = new JLabel("");
                jLabel.setBorder(border);
                jLabel.setOpaque(true);
                jLabel.setBackground(colorSubTitle3);
                jLabel.setBounds(x, y, width, height);
                this.add(jLabel);
                break;
            case 3:
                width = 81;
                jltitles[0].setBounds(x, y, width, height);
                x += width - 1;
                jltitles[3].setBounds(x, y, width, height);
                this.add(jltitles[0]);
                this.add(jltitles[3]);
                break;
        }

        JLabel[] jlxws = new JLabel[3];
        int xwx = 0;
        int xwy = 40;
        for (int i = 0; i < jlxws.length; i++, xwy += 20) {
            String str = "";
            switch (i) {
                case 0:
                    str = "A";
                    break;
                case 1:
                    str = "B";
                    break;
                case 2:
                    str = "C";
                    break;
            }
            jlxws[i] = new JLabel(str, JLabel.CENTER);
            jlxws[i].setBorder(border);
            jlxws[i].setOpaque(true);
            jlxws[i].setBackground(colorSubTitle3);
            jlxws[i].setBounds(xwx, xwy, 41, 21);
            this.add(jlxws[i]);
        }
    }

    private void initValueLabel(JLabel[] jLabels, int y) {
        int x = 40;
        int width = 41;
        int height = 21;
        switch (this.pointBean.getUnitType()) {
            case 1:
                for (int j = 0; j <= 3; j++, x += 40) {
                    jLabels[j].setBounds(x, y, width, height);
                    this.add(jLabels[j]);
                }
                break;
            case 2:
                for (int j = 5; j >= 3; j--, x += 40) {
                    jLabels[j].setBounds(x, y, width, height);
                    this.add(jLabels[j]);
                }
                break;
            case 3:
                width = 81;
                jLabels[0].setBounds(x, y, width, height);
                x += width - 1;
                jLabels[3].setBounds(x, y, width, height);
                this.add(jLabels[0]);
                this.add(jLabels[3]);
                break;
        }
    }

    private void setInitVari() {
        if (pointBean.getPoint() != 0) {
            return;
        }
        for (UnitBean unit : units) {
            getInitLabel(unit).setText(String.valueOf(unit.getInitvari()));
        }
    }

    private UnitBean getUnitBean(int index) {
        switch (index) {
            case 0:
                return getUnitBean("A");
            case 1:
                return getUnitBean("B");
            case 2:
                return getUnitBean("C");
        }
        return null;
    }

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

    private void updateUnit(UnitBean unitBean, int index) {
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
            unit.setInitvari(dataBeans[index].getVari());
            SysUnitService.updateInitvari(unit);
            ChartView.getInstance().alignZero(unitBean);
            JOptionPane.showMessageDialog(null, "设置成功", "成功", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "设置失败,请稍后重试", "失败", JOptionPane.WARNING_MESSAGE);
            return;
        }
        getInitLabel(unitBean).setText(String.valueOf(unitBean.getInitvari()));
        getVariLabel(unitBean).setText("0.0");
        getVariLabel(unitBean).setBackground(colorB);
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
                    unit.setInitvari(dataBeana.getVari());
                    jljza.setText(String.valueOf(unit.getInitvari()));
                    break;
                case "B":
                    unit.setInitvari(dataBeanb.getVari());
                    jljzb.setText(String.valueOf(unit.getInitvari()));
                    break;
                case "C":
                    unit.setInitvari(dataBeanc.getVari());
                    jljzc.setText(String.valueOf(unit.getInitvari()));
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

    private void checkWarning(UnitBean unit, DataBean data) {
        warningstr.append(unit.getXw());
        warningstr.append("\t");
        switch (unit.getType()) {
            case 1:
                SF6CheckWarning(unit, data);
                break;
            case 2:
                VariCheckWarning(unit, data);
                break;
            case 3:
                TempCheckWarning(unit, data);
                break;
        }
    }

    private void SF6CheckWarning(UnitBean unit, DataBean data) {
        if (data.isLowPres()) {
            warningstr.append("\n--低压报警");
        }
        if (data.isLowLock()) {
            warningstr.append("\n--低压闭锁");
        }

        boolean[] flags = new boolean[3];

        if (data.isDisconnect() || data.getTemp() < -273) {
            flags[0] = true;
            warningstr.append("\n--温度值无效");
        } else if (unit.getWarnTemp() != null && data.getTemp() > unit.getWarnTemp()) {
            flags[0] = true;
            warningstr.append("\n--温度过高");
        }
        if (data.isDisconnect() || data.getDen() < 0) {
            flags[1] = true;
            warningstr.append("\n--密度值无效");
        } else if (unit.getMaxden() != null && unit.getMinden() != null) {
            if (data.getDen() > unit.getMaxden() || data.getDen() < unit.getMinden()) {
                flags[1] = true;
                if (unit.getMaxden() != null && unit.getMinden() != null) {
                    if (data.getDen() > unit.getMaxden()) {
                        warningstr.append("\n--密度过高");
                    } else if (data.getDen() < unit.getMinden()) {
                        warningstr.append("\n--密度过低");
                    }
                }
            }
        }
        if (data.isDisconnect() || data.getPres() < 0) {
            flags[2] = true;
            warningstr.append("\n--压力值无效");
        } else if (unit.getMaxper() != null && unit.getMinper() != null) {
            if (data.getPres() > unit.getMaxper() || data.getPres() < unit.getMinper()) {
                flags[2] = true;
                if (unit.getMaxper() != null && unit.getMinper() != null) {
                    if (data.getPres() > unit.getMaxper()) {
                        warningstr.append("\n--压力过高");
                    } else if (data.getPres() < unit.getMinper()) {
                        warningstr.append("\n--压力过低");
                    }
                }
            }
        }
        for (int i = 0; i < flags.length; i++) { // 0 temp 1 den 2 per
            if (flags[i]) {
                this.flags.add(true);
                switch (unit.getXw()) {
                    case "A":
                        jlas[i].setBackground(colorWarn);
                        break;
                    case "B":
                        jlbs[i].setBackground(colorWarn);
                        break;
                    case "C":
                        jlcs[i].setBackground(colorWarn);
                        break;
                }
            } else {
                switch (unit.getXw()) {
                    case "A":
                        jlas[i].setBackground(colorB);
                        break;
                    case "B":
                        jlbs[i].setBackground(colorB);
                        break;
                    case "C":
                        jlcs[i].setBackground(colorB);
                        break;
                }
            }
        }
    }

    private void TempCheckWarning(UnitBean unit, DataBean data) {
        boolean flag = false;
//        if (data.isDisconnect() || data.getTemp() < -273) {
//            flag = true;
//            warningstr.append("\n--温度值无效");
//        } else
        if (unit.getWarnTemp() != null && data.getTemp() > unit.getWarnTemp()) {
            flag = true;
            warningstr.append("\n--温度过高");
        }
        if (flag) {
            flags.add(true);
            switch (unit.getXw()) {
                case "A":
                    jlas[0].setBackground(colorWarn);
                    break;
                case "B":
                    jlbs[0].setBackground(colorWarn);
                    break;
                case "C":
                    jlcs[0].setBackground(colorWarn);
                    break;
            }
        } else {
            switch (unit.getXw()) {
                case "A":
                    jlas[0].setBackground(colorB);
                    break;
                case "B":
                    jlbs[0].setBackground(colorB);
                    break;
                case "C":
                    jlcs[0].setBackground(colorB);
                    break;
            }
        }
    }

    private void VariCheckWarning(UnitBean unit, DataBean data) {
        boolean flag = false;
        float vari = FormatTransfer.newScale(data.getVari(), unit.getInitvari());
        if (data.isDisconnect() || data.getVari() < 0 || data.getVari() > 125) {
            flag = true;
            warningstr.append("\n--偏移量无效");
        } else if (unit.getMaxvari() != null && unit.getMinvari() != null) {
            if (vari > unit.getMaxvari() || vari < unit.getMinvari()) {
                flag = true;
                warningstr.append("\n--伸缩节超出范围");
            }
        }
        JLabel jLabel = getVariLabel(unit);
        if (jLabel == null) {
            return;
        }
        if (flag) {
            flags.add(true);
            jLabel.setBackground(colorWarn);
        } else {
            jLabel.setBackground(colorB);
        }
    }

    private JLabel getTempLabel(UnitBean unit) {
        return getLabel(unit, 0);
    }

    private JLabel getDenLabel(UnitBean unit) {
        return getLabel(unit, 1);
    }

    private JLabel getPresLabel(UnitBean unit) {
        return getLabel(unit, 2);
    }

    private JLabel getBatlvLabel(UnitBean unit) {
        return getLabel(unit, 3);
    }

    private JLabel getVariLabel(UnitBean unit) {
        return getLabel(unit, 4);
    }

    private JLabel getInitLabel(UnitBean unit) {
        return getLabel(unit, 5);
    }

    private JLabel getLabel(UnitBean unit, int index) {
        switch (unit.getXw()) {
            case "A":
                return jlas[index];
            case "B":
                return jlbs[index];
            case "C":
                return jlcs[index];
        }
        return null;
    }

    private void addData(UnitBean unit, DataBean data) {

        switch (data.getUnitType()) {
            case 1:
                if (data.getTemp() <= -273) {
                    getTempLabel(unit).setText("××");
                } else {
                    getTempLabel(unit).setText(String.valueOf(data.getTemp()));
                }
                if (data.isDisconnect() || data.getPres() < 0) {
                    getPresLabel(unit).setText("××");
                } else {
                    getPresLabel(unit).setText(String.valueOf(data.getPres()));
                    if (data.isLowPres()) {
//                        getPresLabel(unit).setText("低压");
//                        getPresLabel(unit).setBackground(colorWarn);
                        flags.add(true);
                    }
                }
                if (data.isDisconnect() || data.getDen() < 0) {
                    getDenLabel(unit).setText("××");

                } else {
                    getDenLabel(unit).setText(String.valueOf(data.getDen()));
                    if (data.isLowLock()) {
//                        getDenLabel(unit).setText("闭锁");
//                        getDenLabel(unit).setBackground(colorWarn);
                        flags.add(true);
                    }
                }
                break;
            case 2:
                if (data.isDisconnect() || data.getVari() < 0) {
                    getVariLabel(unit).setText("××");
                } else {
                    float vari = data.getVari();
                    if (unit.getInitvari() != 0) {
                        vari = FormatTransfer.newScale(data.getVari(), unit.getInitvari());
                    }
                    getVariLabel(unit).setText(String.valueOf(vari));
                }
                break;
            case 3:
                getTempLabel(unit).setText(String.valueOf(data.getTemp()));
                break;
            default:
                return;
        }
        getBatlvLabel(unit).setText(String.valueOf(data.getBatlv()));
    }

    public void clearData() {
        for (int i = 0; i <= 4; i++) {
            jlas[i].setText("");
            jlas[i].setBackground(colorB);
            jlbs[i].setText("");
            jlbs[i].setBackground(colorB);
            jlcs[i].setText("");
            jlcs[i].setBackground(colorB);
        }
        for (DataBean dataBean : dataBeans) {
            dataBean = null;
        }
    }
}

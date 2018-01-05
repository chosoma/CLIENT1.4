package view.dataCollect;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import domain.NetBean;
import domain.UnitBean;
import kotlin.Unit;
import mytools.MyButton2;
import mytools.MyFoldPanel;
import mytools.MyUtil;
import service.*;
import view.ModifiedFlowLayout;
import domain.DataBean;
import domain.SensorAttr;

public class CollectOperate {

    private JPanel center;
    JComponent operatePane;

    private static CollectOperate CO = new CollectOperate();

    public static CollectOperate getInstance() {
        return CO;
    }

    public JComponent getOperatePane() {
        return operatePane;
    }

    private CollectOperate() {
        operatePane = new JPanel(new BorderLayout());
        operatePane.setBackground(new Color(218, 236, 250));

        center = new JPanel(new ModifiedFlowLayout());
        center.setOpaque(false);
        JScrollPane jsp = new JScrollPane(center);
        jsp.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        jsp.setPreferredSize(new Dimension(268, operatePane.getHeight()));
        jsp.setOpaque(false);
        jsp.getViewport().setOpaque(false);
        JScrollBar scrollBar = jsp.getVerticalScrollBar();
        scrollBar.setUnitIncrement(10);
        scrollBar.setOpaque(false);
        operatePane.add(jsp);
        this.initFirst();
//		this.initSecond();
        this.initThird();
//        this.initFourth();
        this.initFifth();
    }

    JComboBox<String> jcbUnitType1;
    JComboBox<String> jcbUnitNumber1;
    JComboBox<Byte> jcbPeriod1;
    MyButton2 jbtOpen1, jbtSet1;
    JLabel jlbOpen1, jlbWait1;

    public MyButton2 getJbtOpen1() {
        return jbtOpen1;
    }

    private void initFirst() {
        JPanel pane = new JPanel(null);
        pane.setPreferredSize(new Dimension(248, 200));
        center.add(new MyFoldPanel("SF6、温度、伸缩节", false, pane));

        jbtOpen1 = new MyButton2("启动服务");
        jbtOpen1.setBounds(75, 10, 100, 30);
        jbtOpen1.setFont(MyUtil.FONT_15);
        pane.add(jbtOpen1);

        jlbOpen1 = new JLabel("服务未启动");
        jlbOpen1.setHorizontalAlignment(JLabel.LEFT);
        jlbOpen1.setBounds(75, 40, 80, 20);
        jbtOpen1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isOpen = jbtOpen1.isSelected();
                if (isOpen) {
                    CollectService.CloseColl1();
                    jbtOpen1.setSelected(false);
                    jlbOpen1.setText("服务已关闭");
                    jbtOpen1.setText("启动服务");
                    jcbUnitType1.setEnabled(false);// 类型
                    jcbUnitNumber1.setEnabled(false);// 单元号
                    jbtSet1.setEnabled(false);// 周期
                    jcbPeriod1.setEnabled(false);// 周期选项
//                    jcbNetId4.setEnabled(false);
//                    jcbTimes4.setEnabled(false);
//                    setAlarm4.setEnabled(false);
//                    jcbUnitType5.setEnabled(false);
//                    jcbUnitNumber5.setEnabled(false);
//                    jbtSet5.setEnabled(false);
                    CollectService.setEditable(true);
                } else {
                    boolean isEditable = CollectService.isEditable();// 资料是否可编辑
                    if (!isEditable) {
                        jbtOpen1.setEnabled(false);
                        jlbOpen1.setEnabled(false);
                        jlbWait1.setVisible(true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    CollectService.OpenColl1();
                                    jbtOpen1.setSelected(true);
                                    jbtOpen1.setText("关闭服务");
                                    jlbOpen1.setText("服务已启动");
                                    jcbUnitType1.setEnabled(true);// 类型
                                    jcbUnitType1.setSelectedIndex(0);
                                    jcbUnitNumber1.setEnabled(true);// 单元号
                                    jbtSet1.setEnabled(true);// 周期
                                    jcbPeriod1.setEnabled(true);// 周期选项
//                                    jcbNetId4.setEnabled(true);
//                                    jcbTimes4.setEnabled(true);
//                                    setAlarm4.setEnabled(true);
//                                    jcbUnitType5.setEnabled(true);
//                                    jcbUnitType5.setSelectedIndex(0);
//                                    jcbUnitNumber5.setEnabled(true);
//                                    jbtSet5.setEnabled(true);
                                    CollectService.setEditable(false);
                                } catch (Exception e) {
//                                    jlbOpen2.setText("服务无法启动");
                                    e.printStackTrace();
                                    JOptionPane.showMessageDialog(null,
                                            e.getMessage(), "服务启动失败",
                                            JOptionPane.ERROR_MESSAGE);
                                } finally {
//                                    jcbNetId4.setEnabled(true);
//                                    jcbTimes4.setEnabled(true);
//                                    setAlarm4.setEnabled(true);
                                    jlbOpen1.setEnabled(true);
                                    jbtOpen1.setEnabled(true);
                                    jlbWait1.setVisible(false);
                                }
                            }
                        }).start();
                    } else {
                        JOptionPane.showMessageDialog(null, "通讯方式处于编辑状态，请注意保存",
                                "服务启动失败", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        pane.add(jlbOpen1);

        jlbWait1 = new JLabel(new ImageIcon("images/progress.gif"));
        jlbWait1.setBounds(155, 40, 20, 20);
        jlbWait1.setVisible(false);
        pane.add(jlbWait1);

        JLabel jlbType = new JLabel("监测点:", JLabel.RIGHT);
        jlbType.setBounds(25, 70, 50, 20);
        pane.add(jlbType);

        jcbUnitType1 = new JComboBox<String>(SysPointService.getPlaces());
        jcbUnitType1.setBounds(75, 70, 100, 20);
        jcbUnitType1.setEnabled(false);
        jcbUnitType1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                String type = (String) jcbUnitType1.getSelectedItem();
//                jcbUnitNumber1.setModel(new DefaultComboBoxModel<>(SysUnitService.getUnitIdsByType(type)));
            }
        });
        pane.add(jcbUnitType1);

        JLabel jlbMkh = new JLabel("相位:", JLabel.RIGHT);
        jlbMkh.setBounds(25, 100, 50, 20);
        pane.add(jlbMkh);

        jcbUnitNumber1 = new JComboBox<>(new String[]{"A", "B", "C"});
        jcbUnitNumber1.setBounds(75, 100, 100, 20);
        jcbUnitNumber1.setEnabled(false);
        jcbUnitNumber1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                String type = (String) jcbUnitType1.getSelectedItem();
//                Integer unitidint = (Integer) jcbUnitNumber1.getSelectedItem();
//                if (unitidint != null) {
//                    Byte unitid = (byte) (int) unitidint;
//                    Byte jg = SysUnitService.getJgMin(type, unitid);
//                    jcbPeriod1.setSelectedItem(jg);
//                }
            }
        });
        pane.add(jcbUnitNumber1);

        JPanel jp2 = new JPanel(null);
        jp2.setOpaque(false);
        jp2.setBorder(new TitledBorder("采样周期"));
        jp2.setBounds(5, 140, 240, 50);
        pane.add(jp2);

        jcbPeriod1 = new JComboBox<Byte>(
                new Byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 30, 40, 60});
        jcbPeriod1.setBounds(45, 20, 60, 20);
        jcbPeriod1.setEnabled(false);
        jp2.add(jcbPeriod1);

        jbtSet1 = new MyButton2("设置");
        jbtSet1.setName("set1");
        jbtSet1.setBounds(130, 17, 80, 26);
        jbtSet1.setEnabled(false);
        jbtSet1.setFont(MyUtil.FONT_14);
        jbtSet1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String place = (String) jcbUnitType1.getSelectedItem();
                String xw = (String) jcbUnitNumber1.getSelectedItem();
                List<UnitBean> unitBeans = SysUnitService.getUnit(place, xw);


//                String type = (String) jcbUnitType1.getSelectedItem();
//                Integer unitidint = (Integer) jcbUnitNumber1.getSelectedItem();
//                Byte unitid;
//                if (unitidint != null) {
//                    unitid = (byte) (int) unitidint;
//                } else {
//                    unitid = null;
//                }
                byte jg = (byte) jcbPeriod1.getSelectedItem();
//                CollectService.setPeriod(type, unitid, jg);
                for (UnitBean unit : unitBeans) {
                    CollectService.setPeriod(unit.getName(), unit.getNumber(), jg);
                }
            }
        });
        jp2.add(jbtSet1);

    }

    // 开始采集、停止采集 按钮
    private JComboBox<Integer> jcbMkh;
    private JComboBox<Byte> jcbPeriod2;
    private JButton jbtOpen2, jbtSet2;
    private JLabel jlbOpen2, jlbWait2;
    private JButton jbtCorrect, jbtColl, jbtRread, jbtClear;
    private JSpinner jspRead;
    private JCheckBox jcbRead;

    private void initSecond() {
        JPanel pane = new JPanel(null);
        pane.setPreferredSize(new Dimension(248, 300));
        center.add(new MyFoldPanel("集水井", false, pane));

        jbtOpen2 = new MyButton2("启动服务");
        jbtOpen2.setBounds(75, 10, 100, 30);
        jbtOpen2.setFont(MyUtil.FONT_15);
        jbtOpen2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isOpen = jbtOpen2.isSelected();
                if (isOpen) {
//					CollectService.CloseCollDtu();
                    jbtOpen2.setSelected(false);
                    jlbOpen2.setText("服务已关闭");
                    jbtOpen2.setText("启动服务");
                    jcbMkh.setEnabled(false);// 模块号
                    jbtCorrect.setEnabled(false);// 校准时钟
                    jbtColl.setEnabled(false);// 实时采集
                    jbtSet2.setEnabled(false);// 周期
                    jcbPeriod2.setEnabled(false);// 周期选项
                    jbtRread.setEnabled(false);// 读取存储器
                    jcbRead.setEnabled(false);// 读取时间
                    jspRead.setEnabled(false);// 读取时间选项
                    jbtClear.setEnabled(false);// 清空存储器
                    if (!jbtOpen1.isSelected()) {
                        CollectService.setEditable(true);
                    }
                } else {
                    boolean isEditable = CollectService.isEditable();// 资料是否可编辑
                    if (!isEditable) {
                        jbtOpen2.setEnabled(false);
                        jlbOpen2.setEnabled(false);
                        jlbWait2.setVisible(true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
//									CollectService.OpenCollDtu();
                                    jbtOpen2.setSelected(true);
                                    jbtOpen2.setText("关闭服务");
                                    jlbOpen2.setText("服务已启动");
                                    jcbMkh.setEnabled(true);// 模块号
                                    jbtCorrect.setEnabled(true);// 校准时钟
                                    jbtColl.setEnabled(true);// 实时采集
                                    jbtSet2.setEnabled(true);// 周期
                                    jcbPeriod2.setEnabled(true);// 周期选项
                                    // jbtRread.setEnabled(true);// 读取存储器
                                    // jcbRead.setEnabled(true);// 读取时间
                                    // jspRead.setEnabled(jcbRead.isSelected());//
                                    // 读取时间选项
                                    // jbtClear.setEnabled(true);// 清空存储器
                                    // 添加模块号集合
                                    jcbMkh.setModel(new DefaultComboBoxModel<Integer>(
                                            SysUnitService
                                                    .getUnitIdsByType(SensorAttr.Sensor_SW)));
                                    CollectService.setEditable(false);
                                } catch (Exception e) {
                                    jlbOpen2.setText("服务无法启动");
                                    JOptionPane.showMessageDialog(null,
                                            e.getMessage(), "服务启动失败",
                                            JOptionPane.ERROR_MESSAGE);
                                } finally {
                                    jlbOpen2.setEnabled(true);
                                    jbtOpen2.setEnabled(true);
                                    jlbWait2.setVisible(false);
                                }
                            }
                        }).start();
                    } else {
                        JOptionPane.showMessageDialog(null, "通讯方式处于编辑状态，请注意保存",
                                "服务启动失败", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });
        pane.add(jbtOpen2);

        jlbOpen2 = new JLabel("服务未启动");
        jlbOpen2.setHorizontalAlignment(JLabel.LEFT);
        jlbOpen2.setBounds(75, 40, 80, 20);
        pane.add(jlbOpen2);

        jlbWait2 = new JLabel(new ImageIcon("images/progress.gif"));
        jlbWait2.setBounds(155, 40, 20, 20);
        jlbWait2.setVisible(false);
        pane.add(jlbWait2);

        JLabel jlbMkh = new JLabel("模块号:", JLabel.RIGHT);
        jlbMkh.setBounds(50, 70, 60, 20);
        pane.add(jlbMkh);

        jcbMkh = new JComboBox<Integer>();
        jcbMkh.setBounds(110, 70, 60, 20);
        jcbMkh.setEnabled(false);
        jcbMkh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Byte mkh = (Byte) jcbMkh.getSelectedItem();
                boolean isSingle = (mkh != null);
                if (isSingle) {
                    Byte jg = SysUnitService
                            .getJgMin(SensorAttr.Sensor_SW, mkh);
                    jcbPeriod2.setSelectedItem(jg);
                }
                jbtRread.setEnabled(isSingle);
                jcbRead.setEnabled(isSingle);
                jbtClear.setEnabled(isSingle);
                jspRead.setEnabled(isSingle);
            }
        });
        pane.add(jcbMkh);

        jbtCorrect = new MyButton2("校准时钟");
        jbtCorrect.setBounds(15, 100, 100, 26);
        jbtCorrect.setEnabled(false);
        jbtCorrect.setFont(MyUtil.FONT_14);
        jbtCorrect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Byte mkh = (Byte) jcbMkh.getSelectedItem();
//				CollectService.clock(mkh);
            }
        });
        pane.add(jbtCorrect);

        jbtColl = new MyButton2("实时采集");
        jbtColl.setName("collect");
        jbtColl.setBounds(125, 100, 100, 26);
        jbtColl.setEnabled(false);
        jbtColl.setFont(MyUtil.FONT_14);
        jbtColl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Byte mkh = (Byte) jcbMkh.getSelectedItem();
//				CollectService.collectOnline(mkh);
            }
        });
        pane.add(jbtColl);

        JPanel jp2 = new JPanel(null);
        jp2.setOpaque(false);
        jp2.setBorder(new TitledBorder("采样周期"));
        jp2.setBounds(5, 135, 240, 50);
        pane.add(jp2);

        jcbPeriod2 = new JComboBox<Byte>(
                new Byte[]{5, 10, 15, 20, 30, 40, 60});
        jcbPeriod2.setBounds(45, 20, 60, 20);
        jcbPeriod2.setEnabled(false);
        jp2.add(jcbPeriod2);

        jbtSet2 = new MyButton2("设置");
        jbtSet2.setName("set2");
        jbtSet2.setBounds(130, 17, 80, 26);
        jbtSet2.setEnabled(false);
        jbtSet2.setFont(MyUtil.FONT_14);
        jbtSet2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Byte mkh = (Byte) jcbMkh.getSelectedItem();
                byte jg = (Byte) jcbPeriod2.getSelectedItem();
//				CollectService.setPeriodSW(mkh, jg);
            }
        });
        jp2.add(jbtSet2);

        JPanel jp3 = new JPanel(null);
        jp3.setOpaque(false);
        jp3.setBorder(new TitledBorder("存储器"));
        jp3.setBounds(5, 190, 240, 110);
        pane.add(jp3);

        jbtRread = new MyButton2("读取记录");
        jbtRread.setName("readStorage");
        jbtRread.setBounds(70, 20, 100, 26);
        jbtRread.setEnabled(false);
        jbtRread.setFont(MyUtil.FONT_14);
        jbtRread.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Byte mkh = (Byte) jcbMkh.getSelectedItem();
                Date time = null;
                if (jcbRead.isSelected()) {
                    time = (Date) jspRead.getValue();
                }
//				CollectService.readStorage(mkh, time);
            }
        });
        jp3.add(jbtRread);

        jcbRead = new JCheckBox("时间:");
        jcbRead.setBounds(35, 50, 55, 20);
        jcbRead.setEnabled(false);
        jcbRead.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jspRead.setEnabled(jcbRead.isSelected());
            }
        });
        jp3.add(jcbRead);

        jspRead = MyUtil.createSpinner(new Date());
        jspRead.setBounds(90, 50, 120, 20);
        jspRead.setEnabled(false);
        jp3.add(jspRead);

        jbtClear = new MyButton2("清空记录");
        jbtClear.setName("clearStorage");
        jbtClear.setBounds(70, 75, 100, 26);
        jbtClear.setEnabled(false);
        jbtClear.setFont(MyUtil.FONT_14);
        jbtClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Byte mkh = (Byte) jcbMkh.getSelectedItem();
//				CollectService.clearStorage(mkh);
            }
        });
        jp3.add(jbtClear);

    }

    private void initThird() {
        JPanel pane1 = new JPanel(null);
        pane1.setPreferredSize(new Dimension(248, 105));

        JButton addData = new MyButton2("加载数据");
        addData.setFont(MyUtil.FONT_14);
        addData.setBounds(75, 10, 100, 26);
        addData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    long start = System.currentTimeMillis();
                    List<DataBean> datas = DataService.getLatestDatas();
                    CollectShow.getInstance().receDatas(datas);
                    long end = System.currentTimeMillis();
                    System.out.println(end - start);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        pane1.add(addData);

        JButton clearData = new MyButton2("清空数据");
        clearData.setFont(MyUtil.FONT_14);
        clearData.setBounds(75, 42, 100, 26);
        clearData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CollectShow.getInstance().clearData();
            }
        });
        pane1.add(clearData);

        JButton show = new MyButton2("表格显示");
        show.setFont(MyUtil.FONT_14);
        show.setBounds(75, 72, 100, 26);
        show.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JButton jb = (JButton) e.getSource();
                boolean b = jb.isSelected();
                if (b) {
                    jb.setText("表格显示");
                } else {
                    jb.setText("图形显示");
                }
                CollectShow.getInstance().showTable(!b);
                jb.setSelected(!b);
            }
        });
        pane1.add(show);

        JPanel c = new MyFoldPanel("数据区", false, pane1);
        center.add(c);

    }

    JComboBox<Integer> jcbNetId4;
    JComboBox<Integer> jcbTimes4;
    JButton setAlarm4;

    private void initFourth() {
        JPanel pane = new JPanel(null);
        pane.setPreferredSize(new Dimension(248, 100));
        center.add(new MyFoldPanel("网关报警", false, pane));

        JLabel jlNetId = new JLabel("网关编号:", JLabel.RIGHT);
        jlNetId.setBounds(10, 20, 70, 20);
        pane.add(jlNetId);

        jcbNetId4 = new JComboBox<>(SysNetService.getNetNumbers((byte) 4));
        jcbNetId4.setBounds(80, 20, 50, 20);
        jcbNetId4.setEnabled(false);
        pane.add(jcbNetId4);

        JLabel jlTime = new JLabel("时间:", JLabel.RIGHT);
        jlTime.setBounds(130, 20, 50, 20);
        pane.add(jlTime);

        Integer[] times = new Integer[255];
        for (int i = 0; i < times.length; ) {
            times[i] = ++i;
        }
        jcbTimes4 = new JComboBox<Integer>(times);
        jcbTimes4.setBounds(180, 20, 50, 20);
        jcbTimes4.setEnabled(false);
        pane.add(jcbTimes4);

        setAlarm4 = new MyButton2("设置");
        setAlarm4.setBounds(84, 60, 80, 26);
        setAlarm4.setEnabled(false);
        setAlarm4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(1);
                NetBean netBean = new NetBean();
                netBean.setType((byte) 4);
                netBean.setNumber((byte) (int) (Integer) jcbNetId4.getSelectedItem());
                byte time = (byte) (int) (Integer) jcbTimes4.getSelectedItem();
                CollectService.setAlarm(netBean, time);
            }
        });
        pane.add(setAlarm4);


    }

    public void setPlace() {
        jcbUnitType1.setModel(new DefaultComboBoxModel<>(SysPointService.getPlaces()));
        jcbUnitType5.setModel(new DefaultComboBoxModel<>(SysPointService.getPlaces()));
    }

    JComboBox<String> jcbUnitType5;
    JComboBox<String> jcbUnitNumber5;
    JButton jbtSet5;

    private void initFifth() {
        JPanel pane = new JPanel(null);
        pane.setPreferredSize(new Dimension(248, 116));
        center.add(new MyFoldPanel("报警值设置", false, pane));

        JLabel jltype = new JLabel("类型:", JLabel.RIGHT);
        jltype.setBounds(25, 10, 50, 20);
        pane.add(jltype);

        jcbUnitType5 = new JComboBox<>(SysPointService.getPlaces());
        jcbUnitType5.setBounds(75, 10, 100, 20);
//        jcbUnitType5.setEnabled(false);
        jcbUnitType5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                String type = (String) jcbUnitType5.getSelectedItem();
//                jcbUnitNumber5.setModel(new DefaultComboBoxModel<>(SysUnitService.getUnitIdsByType(type)));
            }
        });
        pane.add(jcbUnitType5);

        JLabel jlnumber = new JLabel("单元号:", JLabel.RIGHT);
        jlnumber.setBounds(25, 40, 50, 20);
        pane.add(jlnumber);

        jcbUnitNumber5 = new JComboBox<>(new String[]{"A", "B", "C"});
        jcbUnitNumber5.setBounds(75, 40, 100, 20);
//        jcbUnitNumber5.setEnabled(false);
        pane.add(jcbUnitNumber5);

        jbtSet5 = new MyButton2("设置");
        jbtSet5.setBounds(84, 80, 80, 26);
//        jbtSet5.setEnabled(false);
        jbtSet5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String place = (String) jcbUnitType5.getSelectedItem();
                if (place != null && place.equals("")) {
                    JOptionPane.showMessageDialog(null, "请选择监测点", "提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                String xw = (String) jcbUnitNumber5.getSelectedItem();
//                System.out.println("xw:" + xw);
                List<UnitBean> unitBeans = SysUnitService.getUnit(place, xw);
//                System.out.println(unitBeans);

                for (UnitBean unit : unitBeans) {
                    new SetWarnDialog(unit);
                }

//                UnitBean unit = unitBeans.get(0);
//                UnitBean unitBean;
//                if (xw.equals("")) {
//                    unitBean = new UnitBean();
//                }
//                String name = (String) jcbUnitType5.getSelectedItem();
//                Integer numberint = (Integer) jcbUnitNumber5.getSelectedItem();
//                Byte number;
//                if (numberint != null) {
//                    number = (byte) (int) numberint;
//                } else {
//                    number = null;
//                }
//                new SetWarnDialog(name, number);
//                System.out.println(unitBean);
//                new SetWarnDialog(unitBean);
            }
        });
        jcbUnitType5.setSelectedIndex(0);
        pane.add(jbtSet5);


    }

    /**
     * 是否有服务在启动
     *
     * @param isSP
     * @return
     */
    public boolean isOpened(boolean isSP) {
        if (isSP)
            return jbtOpen1.isSelected();
        else
            return jbtOpen2.isSelected();
    }

    /**
     * 关闭资源
     */
    public void closeResource() {
        if (jbtOpen1.isSelected()) {
            jbtOpen1.doClick();
        }
//		if (jbtOpen2.isSelected()) {
//			jbtOpen2.doClick();
//		}
    }

}

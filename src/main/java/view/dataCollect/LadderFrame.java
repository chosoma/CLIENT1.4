package view.dataCollect;

import com.sun.awt.AWTUtilities;
import domain.DataBean;
import domain.DataSearchPara;
import domain.UnitBean;
import model.DataManageModel;
import mytools.Check2SPinner;
import mytools.MouseInputHandler;
import mytools.MyButton2;
import mytools.MyUtil;
import service.DataManageService;
import service.DataService;
import service.SensorService;
import view.icon.CloseIcon;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class LadderFrame extends JFrame {
    private static LadderFrame instance;

    public static LadderFrame getInstance() {
        if (instance == null) {
            synchronized (LadderFrame.class) {
                if (instance == null) {
                    instance = new LadderFrame();
                }
            }
        }
        return instance;
    }

    private java.util.List<UnitBean> unitBeanList;

    public void setUnitBeanList(List<UnitBean> unitBeanList) {
        this.unitBeanList = unitBeanList;
    }

    private Point lastPoint;
    private static Color HeadC1 = new Color(115, 168, 240),
            HeadC2 = new Color(136, 186, 205);
    private LadderPanel jPanel;

    public LadderPanel getjPanel() {
        return jPanel;
    }

    private java.util.List<DataBean> datas;

    JLabel title;

    JComboBox<String> jcbxw;

    public LadderFrame() {
//        setModal(true);// 设置对话框模式
        setUndecorated(true);// 去除JDialog边框
        // 设置JDialog背景透明
        AWTUtilities.setWindowOpaque(this, false);
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createLineBorder(new Color(44, 46, 54)));
        this.setContentPane(contentPane);

        JPanel headPane = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, HeadC1, 0, getHeight() - 1, HeadC2));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }

            public boolean isOpaque() {
                return false;
            }
        };

        headPane.setPreferredSize(new Dimension(headPane.getWidth(), 32));
        contentPane.add(headPane, BorderLayout.NORTH);
        headPane.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent arg0) {
                lastPoint = arg0.getLocationOnScreen();// 记录鼠标坐标
            }
        });
        headPane.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point tempPonit = e.getLocationOnScreen();
                Point location = getLocation();
                setLocation(tempPonit.x - lastPoint.x + location.x, tempPonit.y - lastPoint.y + location.y);
                lastPoint = tempPonit;
            }
        });

        title = new JLabel("", new ImageIcon("images/main/chart_24.png"), JLabel.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("宋体", Font.BOLD, 14));

        headPane.add(title, BorderLayout.WEST);

        JPanel headRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        headRight.setOpaque(false);
        headPane.add(headRight, BorderLayout.EAST);

        JButton close = new JButton(new CloseIcon());
        close.setToolTipText("关闭");
        close.setFocusable(false);
        // 无边框
        close.setBorder(null);
        // 取消绘制按钮内容区域
        close.setContentAreaFilled(false);
        // 设置按钮按下后无虚线框
        close.setFocusPainted(false);
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        headRight.add(close);


        jPanel = new LadderPanel();
        jPanel.setPreferredSize(getSize());
        contentPane.add(jPanel, BorderLayout.CENTER);

        this.setSize(500, 300);
        this.setMinimumSize(new Dimension(500, 300));
        this.setLocationRelativeTo(null);
        MouseInputListener listener = new MouseInputHandler(this);
        addMouseListener(listener);
        addMouseMotionListener(listener);

        JPanel toolBarL = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 2));
        toolBarL.setOpaque(false);
//        contentPane.add(toolBarL);
        contentPane.add(toolBarL, BorderLayout.SOUTH);

        toolBarL.add(Box.createHorizontalStrut(5));
        JTextArea jta4 = new JTextArea("起始\n时间");
        jta4.setOpaque(false);
        jta4.setEditable(false);
        toolBarL.add(jta4);

        Calendar ca = Calendar.getInstance();
        Date date2 = ca.getTime();
        ca.add(Calendar.DAY_OF_MONTH, -1);
        Date date = ca.getTime();
        // 起始时间
        c2s1 = new Check2SPinner(false, date);
        c2s1.setMaximumSize(new Dimension(165, 17));
        toolBarL.add(c2s1);

        toolBarL.add(Box.createHorizontalStrut(5));

        JTextArea jta5 = new JTextArea("终止\n时间");
        jta5.setOpaque(false);
        jta5.setEditable(false);
        toolBarL.add(jta5);

        // 终止时间
        c2s2 = new Check2SPinner(false, date2);
        c2s2.setMaximumSize(new Dimension(165, 17));
        toolBarL.add(c2s2);
        String[] xws = new String[]{"A", "B", "C"};
        jcbxw = new JComboBox<>(xws);
        toolBarL.add(jcbxw);
        toolBarL.add(Box.createHorizontalStrut(5));
        JButton search = new MyButton2("查询", new ImageIcon(
                "images/search_24.png"));
        search.setFont(MyUtil.FONT_14);
        search.setPreferredSize(new Dimension(65, 32));
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UnitBean unit = getUnit();

                    DataSearchPara para = getSearchConditon();
                    if (para.getT1() == null && para.getT2() == null) {
                        JOptionPane.showMessageDialog(null, "请选择日期", "提示", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    List<DataBean> dataList = DataService.getBetween(unit, para);
                    if (dataList == null || dataList.size() == 0) {
                        JOptionPane.showMessageDialog(null, "时间段没有数据", "提示", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    LadderFrame.this.jPanel.setUnit(unit);
                    LadderFrame.this.jPanel.setDatas(dataList);
                    LadderFrame.this.jPanel.repaint();

                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "提示",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        toolBarL.add(search);

//        JButton restore = new MyButton2("还原", new ImageIcon(
//                "images/search_24.png"));
//        restore.setFont(MyUtil.FONT_14);
//        restore.setPreferredSize(new Dimension(65, 32));
//        restore.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//
//                    LadderFrame.this.jPanel.setFlag(true);
//                    LadderFrame.this.jPanel.repaint();
//
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                    JOptionPane.showMessageDialog(null, e1.getMessage(), "提示",
//                            JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//        toolBarL.add(restore);
    }

    public DataSearchPara getSearchConditon() throws Exception {

        UnitBean unit = getUnit();
        DataSearchPara para = new DataSearchPara();
        para.setType(unit.getName());
        para.setSjbh(unit.getNumber());
        Date startT = c2s1.getTime(), endT = c2s2.getTime();
        if (startT == null) {// 起始时间未选中
            if (endT == null) {// 终止时间未选中
            } else {// 终止时间选中
                para.setT2(endT);
            }
        } else {// 起始时间选中
            para.setT1(startT);
            if (endT == null) {// 终止时间未选中
                // do nothing
            } else {// 终止时间选中
                if (startT.after(endT)) {
                    throw new Exception("起始时间应位于终止时间之前");
                }
                para.setT2(endT);
            }
        }
        return para;
    }

    private UnitBean getUnit() {
        String xw = (String) jcbxw.getSelectedItem();
        UnitBean unitBean = null;
        for (UnitBean unit : unitBeanList) {
            if (unit.getXw().equals(xw)) {
                unitBean = unit;
                break;
            }
        }
        return unitBean;
    }

    private Check2SPinner c2s1, c2s2;


    public void setHeadTitle(String title) {
        this.title.setText(title);
    }
}

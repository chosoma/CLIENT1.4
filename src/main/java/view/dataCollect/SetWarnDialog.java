package view.dataCollect;

import domain.NetBean;
import domain.SensorAttr;
import domain.UnitBean;
import mytools.MyButton2;
import service.SysUnitService;
import view.icon.CloseIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class SetWarnDialog extends JDialog {

    UnitBean unitBean;
    String name;
    private Point lastPoint;
    private static Color HeadC1 = new Color(240, 62, 20), HeadC2 = new Color(205, 49, 13);

    public SetWarnDialog(String name, Byte number) {
        if (number != null) {
            unitBean = SysUnitService.getUnitBean(name, number);
        }
        this.name = name;
        initDefault(null);
    }

    private void initDefault(Window owner) {

        setModal(true);// 设置对话框模式
        setUndecorated(true);// 去除JDialog边框
        // 设置JDialog背景透明
        // AWTUtilities.setWindowOpaque(this, false);
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createLineBorder(new Color(44, 46,
                54)));
        this.setContentPane(contentPane);

        JPanel headPane = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, HeadC1, 0, getHeight() - 1,
                        HeadC2));
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
                setLocation(tempPonit.x - lastPoint.x + location.x, tempPonit.y
                        - lastPoint.y + location.y);
                lastPoint = tempPonit;
            }
        });

        JLabel title = new JLabel("报警值设置", new ImageIcon(
                "images/main/sensor_24.png"), JLabel.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("微软雅黑", Font.BOLD, 14));

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
                dispose();
            }
        });
        headRight.add(close);

        JPanel centerPane = initContent();
        Dimension size = centerPane.getSize();
        contentPane.add(centerPane, BorderLayout.CENTER);

        JPanel bottomPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 2));
        bottomPane.setBackground(new Color(240, 240, 240));
        contentPane.add(bottomPane, BorderLayout.SOUTH);

        JButton buttonSave = new MyButton2("保存", new ImageIcon(
                "images/apply.png"));
        buttonSave.setToolTipText("保存修改");
        buttonSave.setPreferredSize(new Dimension(75, 28));
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setPerameter();
                    JOptionPane.showMessageDialog(null, "设置成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    JOptionPane.showMessageDialog(null, "设置失败!", "失败", JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "输入有误!", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                dispose();
            }
        });
        bottomPane.add(buttonSave);

        bottomPane.add(Box.createVerticalStrut(36));

        JButton buttonCancel = new MyButton2("取消", new ImageIcon(
                "images/cancel.png"));
        buttonCancel.setToolTipText("取消添加，并退出该窗口");
        buttonCancel.setPreferredSize(new Dimension(75, 28));
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        bottomPane.add(buttonCancel);

        this.setSize(size.width + 10, size.height + headPane.getPreferredSize().height + bottomPane.getPreferredSize().height + 10);
        setLocationRelativeTo(owner);// 居中
        this.setVisible(true);

    }

    protected NetBean getNetInfo() throws Exception {
        return null;
    }

    JLabel jlbTypes;
    JLabel jlbNumbers;
    JTextField jtftemp;
    JTextField jtfdenmax;
    JTextField jtfdenmin;
    JTextField jtfpermax;
    JTextField jtfpermin;
    JTextField jtfvarimax;
    JTextField jtfvarimin;


    protected JPanel initContent() {
        JPanel centerPanel = new JPanel(null);
        int y = 13;
        JLabel jlbType = new JLabel("单元类型:", JLabel.RIGHT);
        jlbType.setBounds(30, y, 75, 20);
        centerPanel.add(jlbType);

        jlbTypes = new JLabel(name);
        jlbTypes.setBounds(105, y, 70, 20);
        centerPanel.add(jlbTypes);

        y += 30;
        if (unitBean != null) {
            JLabel jlbNumber = new JLabel("单元编号:", JLabel.RIGHT);
            jlbNumber.setBounds(30, y, 75, 20);
            centerPanel.add(jlbNumber);
            jlbNumbers = new JLabel(String.valueOf(unitBean.getNumber()));
            jlbNumbers.setBounds(105, y, 70, 20);
            centerPanel.add(jlbNumbers);
            y += 30;
        }

        if (name.equals(SensorAttr.Sensor_SF6)) {
            JLabel jlbdenmax = new JLabel("密度最大值:", JLabel.RIGHT);
            jlbdenmax.setBounds(30, y, 75, 20);
            centerPanel.add(jlbdenmax);


            jtfdenmax = new JTextField();
            jtfdenmax.setBounds(105, y, 70, 20);
            centerPanel.add(jtfdenmax);
            if (unitBean != null && unitBean.getMaxden() != null) {
                jtfdenmax.setText(String.valueOf(unitBean.getMaxden()));
            }
            y += 30;

            JLabel jlbdenmin = new JLabel("密度最小值:", JLabel.RIGHT);
            jlbdenmin.setBounds(30, y, 75, 20);
            centerPanel.add(jlbdenmin);

            jtfdenmin = new JTextField();
            jtfdenmin.setBounds(105, y, 70, 20);
            centerPanel.add(jtfdenmin);
            if (unitBean != null && unitBean.getMinden() != null) {
                jtfdenmin.setText(String.valueOf(unitBean.getMinden()));
            }
            y += 30;

            JLabel jlbpermax = new JLabel("压力最大值:", JLabel.RIGHT);
            jlbpermax.setBounds(30, y, 75, 20);
            centerPanel.add(jlbpermax);

            jtfpermax = new JTextField();
            jtfpermax.setBounds(105, y, 70, 20);
            centerPanel.add(jtfpermax);
            if (unitBean != null && unitBean.getMaxper() != null) {
                jtfpermax.setText(String.valueOf(unitBean.getMaxper()));
            }
            y += 30;

            JLabel jlbpermin = new JLabel("压力最小值:", JLabel.RIGHT);
            jlbpermin.setBounds(30, y, 75, 20);
            centerPanel.add(jlbpermin);

            jtfpermin = new JTextField();
            jtfpermin.setBounds(105, y, 70, 20);
            centerPanel.add(jtfpermin);
            if (unitBean != null && unitBean.getMinper() != null) {
                jtfpermin.setText(String.valueOf(unitBean.getMinper()));
            }
            y += 30;

            JLabel jlbtemp = new JLabel("温度:", JLabel.RIGHT);
            jlbtemp.setBounds(30, y, 75, 20);
            centerPanel.add(jlbtemp);

            jtftemp = new JTextField();
            jtftemp.setBounds(105, y, 70, 20);
            centerPanel.add(jtftemp);
            if (unitBean != null && unitBean.getWarnTemp() != null) {
                jtftemp.setText(String.valueOf(unitBean.getWarnTemp()));
            }
        } else if (name.equals(SensorAttr.Sensor_WD)) {
            JLabel jlbtemp = new JLabel("温度:", JLabel.RIGHT);
            jlbtemp.setBounds(30, y, 75, 20);
            centerPanel.add(jlbtemp);

            jtftemp = new JTextField();
            jtftemp.setBounds(105, y, 70, 20);
            centerPanel.add(jtftemp);
            if (unitBean != null && unitBean.getWarnTemp() != null) {
                jtftemp.setText(String.valueOf(unitBean.getWarnTemp()));
            }
        } else {
            JLabel jlbvarimax = new JLabel("移动最大值:", JLabel.RIGHT);
            jlbvarimax.setBounds(30, y, 75, 20);
            centerPanel.add(jlbvarimax);

            jtfvarimax = new JTextField();
            jtfvarimax.setBounds(105, y, 70, 20);
            centerPanel.add(jtfvarimax);
            if (unitBean != null && unitBean.getMaxvari() != null) {
                jtfvarimax.setText(String.valueOf(unitBean.getMaxvari()));
            }
            y += 30;

            JLabel jlbvarimin = new JLabel("移动最小值:", JLabel.RIGHT);
            jlbvarimin.setBounds(30, y, 75, 20);
            centerPanel.add(jlbvarimin);

            jtfvarimin = new JTextField();
            jtfvarimin.setBounds(105, y, 70, 20);
            centerPanel.add(jtfvarimin);
            if (unitBean != null && unitBean.getMinvari() != null) {
                jtfvarimin.setText(String.valueOf(unitBean.getMinvari()));
            }
        }

        centerPanel.setSize(200, y + 30);
        return centerPanel;
    }

    void setPerameter() throws NumberFormatException, SQLException {
//        UnitBean unitBean = new UnitBean();
//        String name = jlbTypes.getText();
//        unitBean.setName(name);
        for (UnitBean unit : SysUnitService.getUnitList()) {
            boolean flag = false;
            if (this.unitBean != null) {
                if (unit.equals(this.unitBean)) {
                    flag = true;
                }
                if (!flag) {
                    continue;
                }
            }
            if (!unit.getName().equals(name)) {
                continue;
            }
            switch (unit.getName()) {
                case "SF6":
                    if (jtfdenmax != null) {
                        float denmax = Float.parseFloat(jtfdenmax.getText());
                        unit.setMaxden(denmax);
                    }
                    if (jtfdenmin != null) {
                        float denmin = Float.parseFloat(jtfdenmin.getText());
                        unit.setMinden(denmin);
                    }
                    if (jtfpermax != null) {
                        float permax = Float.parseFloat(jtfpermax.getText());
                        unit.setMaxper(permax);
                    }
                    if (jtfpermin != null) {
                        float permin = Float.parseFloat(jtfpermin.getText());
                        unit.setMinper(permin);
                    }
                    if (jtftemp != null) {
                        float temp = Float.parseFloat(jtftemp.getText());
                        unit.setWarnTemp(temp);
                    }
                    if (unit.getMaxden() < unit.getMinden()) {
                        throw new NumberFormatException();
                    }
                    if (unit.getMaxper() < unit.getMinper()) {
                        throw new NumberFormatException();
                    }
                    break;
                case "温度":
                    if (jtftemp != null) {
                        float temp = Float.parseFloat(jtftemp.getText());
                        unit.setWarnTemp(temp);
                    }
                    break;
                case "伸缩节":
                    if (jtfvarimax != null) {
                        float varimax = Float.parseFloat(jtfvarimax.getText());
                        unit.setMaxvari(varimax);
                    }
                    if (jtfvarimin != null) {
                        float varimin = Float.parseFloat(jtfvarimin.getText());
                        unit.setMinvari(varimin);
                    }
                    if (unit.getMaxvari() < unit.getMinvari() || unit.getMaxvari() > 125 || unit.getMaxvari() < -125 || unit.getMinvari() > 125 || unit.getMinvari() < -125) {
                        throw new NumberFormatException();
                    }
                    break;
            }
            SysUnitService.updateWarning(unit);
//            ChartView.getInstance().refresh(unit);
            if (flag) {
                break;
            }
        }
    }


}

package view.systemSetup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import domain.UnitPacket;
import mytools.MyButton2;
import service.SensorService;
import service.SysNetService;
import service.SysUnitService;
import view.dataCollect.ChartView;
import view.icon.CloseIcon;
import domain.UnitBean;

public abstract class SysUnitDialog extends JDialog {
    protected UnitBean bean = null;
    private Point lastPoint;
    static Color HeadC1 = new Color(240, 62, 20), HeadC2 = new Color(205, 49,
            13);

    protected void initDefault(Window owner) {
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
                setLocation(tempPonit.x - lastPoint.x + location.x, tempPonit.y
                        - lastPoint.y + location.y);
                lastPoint = tempPonit;
            }
        });

        JLabel title = new JLabel("单元设置", new ImageIcon(
                "images/main/sensor_24.png"), JLabel.LEFT);
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
            public void actionPerformed(ActionEvent actionEvente) {
                try {
                    UnitBean newUnitBean = getUnitInfo();
                    boolean contains = SysUnitService.getUnitList().contains(newUnitBean);
                    if (bean != null) {
                        if (!bean.equals(newUnitBean) && contains) {
                            JOptionPane.showMessageDialog(null, "单元已存在", "提示", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        SysUnitService.updateUnitBean(newUnitBean, bean);
                        JOptionPane.showMessageDialog(null, "资料修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        if (contains) {
                            JOptionPane.showMessageDialog(null, "单元已存在", "提示", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        SysUnitService.addUnit(newUnitBean);
                        UnitPacket unitPacket = SensorService.getUnitPacket(newUnitBean.getGatewaytype(), newUnitBean.getGatewaynumber());
                        unitPacket.addUnit(newUnitBean);
                        ChartView.getInstance().loadSensor();
                        int flag = JOptionPane.showConfirmDialog(null, "资料添加成功，是否需要继续添加？", "提示", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (flag == JOptionPane.OK_OPTION) {
                            clearUnitInfo();
                        } else {
                            dispose();
                        }
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "请先添加网关", "失败", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "资料保存失败", "失败", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
                }
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
        if (bean != null) {
            this.loadUnitInfo();
        }
        this.setVisible(true);
    }

    /**
     * 初始化表单
     *
     * @return
     */
    protected abstract JPanel initContent();

    /**
     * 获取需要提交的信息
     *
     * @return
     * @throws Exception
     */
    protected abstract UnitBean getUnitInfo() throws Exception;

    /**
     * 加载信息
     */
    protected abstract void loadUnitInfo();

    /**
     * 清空表单
     */
    protected abstract void clearUnitInfo();

}

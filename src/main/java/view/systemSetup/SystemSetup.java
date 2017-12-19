package view.systemSetup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import mytools.MyPanel;
import mytools.MyUtil;
import service.UserService;

public class SystemSetup extends JPanel {

    private static SystemSetup SS = null;
    private JPanel leftPanel, rightPanel;
    private System2Communication sys2comm;
    private System2Net sys2Net;
    private System2Unit sys2Unit;
    private System2WdSet sys2Wd;
    private System2PassWord sys2pw;
    private System2User sys2u;
    // 用户权限
    private JSplitPane splitPane;
    private JLabel jLL1, jLL2, jLL3, jLL4, jLL5, jLL6, titleLabel;
    private ImageIcon icon10, icon11, icon20, icon21, icon30, icon31, icon40,
            icon41, icon50, icon51, icon60, icon61;
    private boolean isInitialize = false;// 初始化标志，用于首次打开显示时用

    private int dividerLocation = 250;// 分割条位置

    private SystemSetup() {
        this.setLayout(new BorderLayout());
        sys2comm = new System2Communication();
        sys2Unit = new System2Unit();
        sys2Net = new System2Net();
        sys2pw = new System2PassWord();
        sys2u = new System2User();
        sys2Wd = new System2WdSet();
        init();
        this.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                if (!isInitialize) {
                    isInitialize = true;
                    boolean admin = UserService.getInstance().isAdmin();
                    if (admin) {
                        sys2u.loadUsers();
                    } else {
                        jLL4.setVisible(false);
                    }
                }
            }
        });
    }

    public static SystemSetup getInstance() {
        if (SS == null) {
            synchronized (SystemSetup.class) {
                if (SS == null)
                    SS = new SystemSetup();
            }
        }
        return SS;
    }

    private void init() {
        // 左面板
        leftPanel = new MyPanel(MyUtil.LEFT_PANE_COLOR, 1.0f);
        leftPanel.setLayout(new FlowLayout(FlowLayout.CENTER, dividerLocation,
                30));

        // 通讯设置
        icon10 = new ImageIcon("images/sysComm_32.png");
        icon11 = new ImageIcon("images/sysComm_48.png");
        jLL1 = new JLabel("通讯设置", icon10, 0);
        jLL1.setFont(MyUtil.FONT_16);
        jLL1.setPreferredSize(new Dimension(140, 50));
        jLL1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (rightPanel.getComponent(1) == sys2comm) {
                    return;
                }
                titleLabel.setText("通 讯 设 置");
                rightPanel.remove(1);
                rightPanel.add(sys2comm, 1);
                rightPanel.repaint();
                rightPanel.revalidate();
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
                jLL1.setForeground(Color.WHITE);
                jLL1.setIcon(icon11);
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
                jLL1.setForeground(Color.BLACK);
                jLL1.setIcon(icon10);
            }
        });
        leftPanel.add(jLL1);

        // 网关配置
        icon50 = new ImageIcon("images/sysNet_32.png");
        icon51 = new ImageIcon("images/sysNet_48.png");
        jLL5 = new JLabel("网关配置", icon50, 0);
        jLL5.setFont(MyUtil.FONT_16);
        jLL5.setPreferredSize(new Dimension(140, 50));
        jLL5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (rightPanel.getComponent(1) == sys2Net) {
                    return;
                }
                titleLabel.setText("网 关 配 置");
                rightPanel.remove(1);
                rightPanel.add(sys2Net, 1);
                rightPanel.repaint();
                rightPanel.revalidate();
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
                jLL5.setForeground(Color.WHITE);
                jLL5.setIcon(icon51);
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
                jLL5.setForeground(Color.BLACK);
                jLL5.setIcon(icon50);
            }
        });
//		leftPanel.add(jLL5);

        // 单元配置
        icon20 = new ImageIcon("images/sysUnit_32.png");
        icon21 = new ImageIcon("images/sysUnit_48.png");
        jLL2 = new JLabel("单元配置", icon20, 0);
        jLL2.setFont(MyUtil.FONT_16);
        jLL2.setPreferredSize(new Dimension(140, 50));
        jLL2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (rightPanel.getComponent(1) == sys2Unit) {
                    return;
                }
                titleLabel.setText("单 元 配 置");
                rightPanel.remove(1);
                rightPanel.add(sys2Unit, 1);
                rightPanel.repaint();
                rightPanel.revalidate();
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
                jLL2.setForeground(Color.WHITE);
                jLL2.setIcon(icon21);
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
                jLL2.setForeground(Color.BLACK);
                jLL2.setIcon(icon20);
            }
        });
//		leftPanel.add(jLL2);

        // 单元配置
        icon60 = new ImageIcon("images/sysTemp_32.png");
        icon61 = new ImageIcon("images/sysTemp_48.png");
        jLL6 = new JLabel("温度频率", icon60, 0);
        jLL6.setFont(MyUtil.FONT_16);
        jLL6.setPreferredSize(new Dimension(140, 50));
        jLL6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (rightPanel.getComponent(1) == sys2Wd) {
                    return;
                }
                titleLabel.setText("温 度 频 率");
                rightPanel.remove(1);
                rightPanel.add(sys2Wd, 1);
                rightPanel.repaint();
                rightPanel.revalidate();
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
                jLL6.setForeground(Color.WHITE);
                jLL6.setIcon(icon61);
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
                jLL6.setForeground(Color.BLACK);
                jLL6.setIcon(icon60);
            }
        });
        leftPanel.add(jLL6);

        // 修改密码
        icon30 = new ImageIcon("images/sysPass_32.png");
        icon31 = new ImageIcon("images/sysPass_48.png");
        jLL3 = new JLabel("修改密码", icon30, 0);
        jLL3.setFont(MyUtil.FONT_16);
        jLL3.setPreferredSize(new Dimension(140, 50));
        jLL3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (rightPanel.getComponent(1) == sys2pw) {
                    return;
                }
                titleLabel.setText("修 改 密 码");
                rightPanel.remove(1);
                rightPanel.add(sys2pw, 1);
                rightPanel.repaint();
                rightPanel.revalidate();
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
                jLL3.setForeground(Color.WHITE);
                jLL3.setIcon(icon31);
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
                jLL3.setForeground(Color.BLACK);
                jLL3.setIcon(icon30);
            }
        });
        leftPanel.add(jLL3);

        // 用户管理
        icon40 = new ImageIcon("images/sysUser_32.png");
        icon41 = new ImageIcon("images/sysUser_48.png");
        jLL4 = new JLabel("用户管理", icon40, 0);
        jLL4.setFont(MyUtil.FONT_16);
        jLL4.setPreferredSize(new Dimension(140, 50));
        jLL4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (rightPanel.getComponent(1) == sys2u) {
                    return;
                }
                titleLabel.setText("用 户 管 理");
                rightPanel.remove(1);
                rightPanel.add(sys2u, 1);
                rightPanel.repaint();
                rightPanel.revalidate();
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
                jLL4.setForeground(Color.WHITE);
                jLL4.setIcon(icon41);
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
                jLL4.setForeground(Color.BLACK);
                jLL4.setIcon(icon40);
            }
        });
        leftPanel.add(jLL4);

        // 右面板
        titleLabel = new JLabel("通 讯 设 置", JLabel.CENTER);
        titleLabel.setFont(MyUtil.FONT_36);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));// 绘制空白边框
        rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5000, 10));
        rightPanel.setOpaque(false);
        rightPanel.add(titleLabel, 0);
        rightPanel.add(sys2comm, 1);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, leftPanel, rightPanel);
        // 指定左边的面板占多大的像素
        splitPane.setDividerLocation(dividerLocation);
        // 把拆分边界线设为0，即不显示
        splitPane.setDividerSize(0);
        // 设置jSplitPane不可拖动
        splitPane.setEnabled(false);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);
        this.add(splitPane, BorderLayout.CENTER);

    }

    // 设置用户管理可见
    public void hideUserMan() {
        jLL4.setVisible(false);
    }

    public System2Communication getSys2comm() {
        return sys2comm;
    }

    public System2Unit getsys2conn() {
        return sys2Unit;
    }

    public System2PassWord getSys2pw() {
        return sys2pw;
    }

    public System2User getSys2u() {
        return sys2u;
    }

    public void setEditable(boolean isEditable) {
        sys2Unit.setEditable(isEditable);
        sys2Net.setEditable(isEditable);
        sys2comm.setEditable(isEditable);
    }

    public boolean isEditable() {
        return sys2comm.isEditable();
    }

}

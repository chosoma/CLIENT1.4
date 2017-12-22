package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Random;


import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import mytools.*;
import view.dataCollect.ChartView;
import view.dataCollect.CollectOperate;
import view.icon.CloseIcon;
import view.icon.MaxIcon;
import view.icon.MinIcon;
import view.icon.MyIconFactory;
import view.icon.SetIcon;

import com.DefaultUIManager;
import com.MyConfigure;
import com.MyLgoInfo;

public class Shell extends JFrame implements ActionListener {

    // 虚线框
    private MyDashedBorder myDashedBorder;
    private Point lastPoint;
    private boolean isMaximized = false;
    private JPopupMenu pop;
    private JButton btnMax;
    private boolean isDragged = false;
    private JPanel contentPane, normalpanel, toolBar, centerPanel;
    private CardLayout contentCard, centerCard;// 卡片布局
    public static int ShellState = Frame.NORMAL;

    public CardLayout getCenterCard() {
        return centerCard;
    }

    public JPanel getCenterPanel() {
        return centerPanel;
    }

    public static Dimension dimension = new Dimension(1000, 600);

    private static Shell SHELL = null;

    private Shell() {

        this.initWindow();

        this.initTop();

        this.initCenter();

    }

    public static Shell getInstance() {
        if (SHELL == null) {
            synchronized (Shell.class) {
                if (SHELL == null)
                    SHELL = new Shell();
            }
        }
        return SHELL;
    }

    private void initWindow() {

        this.setIconImages(DefaultUIManager.icons);
        this.setTitle(MyLgoInfo.SoftName);// 标题

        this.setUndecorated(true);// 去除边框修饰
        // AWTUtilities.setWindowOpaque(this, false);// 设置透明
        this.setSize(dimension);
        this.setLocationRelativeTo(null);

        contentCard = new CardLayout();
        contentPane = new JPanel(contentCard);
        contentPane.setBorder(BorderFactory.createLineBorder(new Color(44, 46,
                54)));
        setContentPane(contentPane);

        try {
            Image image = ImageIO.read(this.getClass().getResource("backGround.png"));
            Image image2 = ImageIO.read(this.getClass().getResource("backGround.jpg"));
            normalpanel = new BackGroundPanel(image, image2);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        contentPane.add(normalpanel, "normal");

        myDashedBorder = new MyDashedBorder();
        myDashedBorder.setBounds(this.getBounds());
        // 窗体关闭弹出对话框提示："确定"、"取消"
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Shell.this.exitSys();
            }
        });
//        MouseInputListener listener = new MouseInputHandler(this);
//        addMouseListener(listener);
//        addMouseMotionListener(listener);
    }

    private void initTop() {
        // 顶部面板：放置标题面板和功能面板
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        normalpanel.add(topPanel, BorderLayout.NORTH);
        topPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    btnMax.doClick();
                }
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                if (!isMaximized) {
                    lastPoint = arg0.getLocationOnScreen();// 记录鼠标坐标
                    myDashedBorder.setVisible(true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isMaximized & isDragged) {
                    isDragged = false;
                    Shell.this.setLocation(myDashedBorder.getLocation());
                }
                myDashedBorder.setVisible(false);
            }
        });
        topPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!isMaximized) {
                    isDragged = true;
                    Point location = myDashedBorder.getLocation();
                    Point tempPonit = e.getLocationOnScreen();
                    myDashedBorder.setLocation(location.x + tempPonit.x - lastPoint.x, location.y + tempPonit.y - lastPoint.y);
                    lastPoint = tempPonit;
                }
            }
        });

        // 标题面板：放置logo和窗口工具
        JPanel tiltlePanel = new JPanel(new BorderLayout());
        tiltlePanel.setOpaque(false);
        topPanel.add(tiltlePanel, BorderLayout.NORTH);

        JLabel log = new JLabel(" " + MyLgoInfo.SoftName);
        log.setForeground(Color.WHITE);
        log.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        tiltlePanel.add(log, BorderLayout.WEST);

        // 窗口操作面板，“最小化”、“最大化”、关闭
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        tiltlePanel.add(right, BorderLayout.EAST);

        initSetPop();

        JButton btnSet = new JButton(new SetIcon());
        btnSet.setToolTipText("主菜单");
        btnSet.setFocusable(false);
        // 无边框
        btnSet.setBorder(null);
        // 取消绘制按钮内容区域
        btnSet.setContentAreaFilled(false);
        // 设置按钮按下后无虚线框
        btnSet.setFocusPainted(false);
        btnSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton jb = (JButton) e.getSource();
                // Point show = new Point(0, jb.getHeight());
                // SwingUtilities.convertPointToScreen(show, jb);
                // if (show.x < 0) {
                // show.x = 0;
                // } else {
                // Dimension size = Toolkit.getDefaultToolkit()
                // .getScreenSize();
                // Dimension popSize = pop.getSize();
                // if (popSize.width + show.x > size.width) {
                // show.x = size.width - popSize.width;
                // }
                // }
                // SwingUtilities.convertPointFromScreen(show, jb);
                // pop.show(jb, show.x, show.y);
                pop.show(jb, 0, jb.getHeight());
            }
        });
        right.add(btnSet);

        JButton btnMin = new JButton(new MinIcon());
        btnMin.setToolTipText("最小化");
        btnMin.setFocusable(false);
        // 无边框
        btnMin.setBorder(null);
        // 取消绘制按钮内容区域
        btnMin.setContentAreaFilled(false);
        // 设置按钮按下后无虚线框
        btnMin.setFocusPainted(false);
        right.add(btnMin);
        btnMin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Shell.this.setExtendedState(JFrame.ICONIFIED);
                // 此处只能是Frame.setStatr(state),否则在最大化模式下最小化后，
                // 再点击状态栏图标就不能还原最大化,只能显示JFrame.NORMAL状态
                Shell.this.setState(Frame.ICONIFIED);
            }
        });

        btnMax = new JButton(new MaxIcon());
        btnMax.setToolTipText("最大化");
        btnMax.setFocusable(false);
        // 无边框
        btnMax.setBorder(null);
        // 取消绘制按钮内容区域
        btnMax.setContentAreaFilled(false);
        // 设置按钮按下后无虚线框
        btnMax.setFocusPainted(false);
        right.add(btnMax);
        btnMax.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isMaximized) {
                    // Shell.this.setExtendedState(Frame.NORMAL);
                    Shell.this.setBounds(myDashedBorder.getBounds());
                    ShellState = Frame.NORMAL;
                    btnMax.setToolTipText("最大化");
//                    ChartView.getInstance().getPanelPhoto().repaint();
                } else {
                    // Shell.this.setExtendedState(Frame.MAXIMIZED_BOTH);
                    Shell.this.setBounds(getMaxBounds());
                    ShellState = Frame.MAXIMIZED_BOTH;
                    btnMax.setToolTipText("向下还原");
                }
                isMaximized = !isMaximized;
                btnMax.setSelected(isMaximized);
            }
        });

        JButton btnClose = new JButton(new CloseIcon());
        btnClose.setToolTipText("关闭");
        btnClose.setFocusable(false);
        // 无边框
        btnClose.setBorder(null);
        // 取消绘制按钮内容区域
        btnClose.setContentAreaFilled(false);
        // 设置按钮按下后无虚线框
        btnClose.setFocusPainted(false);
        right.add(btnClose);
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Shell.this.exitSys();
            }
        });

        // 功能面板，放置“主界面”、“数据采集”······
        JPanel funcionPanel = new JPanel(new BorderLayout());
        funcionPanel.setOpaque(false);
        topPanel.add(funcionPanel, BorderLayout.CENTER);

        toolBar = new JPanel();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 2));
        toolBar.setOpaque(false);
        funcionPanel.add(toolBar, BorderLayout.WEST);

        JPanel toolBarRight = new JPanel(new BorderLayout());
        toolBarRight.setOpaque(false);
        funcionPanel.add(toolBarRight, BorderLayout.EAST);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));
        buttonPanel.setOpaque(false);
        toolBarRight.add(buttonPanel, BorderLayout.SOUTH);

        Dimension buttonsize = new Dimension(60, 24);

        JButton jbSF6 = new MyButton4("SF6");
        jbSF6.setPreferredSize(buttonsize);
        jbSF6.setSelected(true);
        jbSF6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myButtonGroup(e, "SF6");
            }
        });
        buttonPanel.add(jbSF6);

        JButton jbWd = new MyButton4("温度");
        jbWd.setPreferredSize(buttonsize);
        jbWd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myButtonGroup(e, "WD");
            }
        });
        buttonPanel.add(jbWd);

        JButton jbGy = new MyButton4("伸缩节");
        jbGy.setPreferredSize(buttonsize);
        jbGy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myButtonGroup(e, "SSJ");
            }
        });
        buttonPanel.add(jbGy);

        JButton jbSw = new MyButton4("图形");
        jbSw.setPreferredSize(buttonsize);
        jbSw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myButtonGroup(e, "TX");
            }
        });
        buttonPanel.add(jbSw);

    }

    JPanel buttonPanel;

    MyTitleButton debugs;

    private void initSetPop() {
        pop = new JPopupMenu();
        // JMenuItem changePSW = new JMenuItem("更改密码", new ImageIcon(
        // "images/main/changePSW_16.png"));
        // pop.add(changePSW);
        // userManage = new JMenuItem("用户管理", new ImageIcon(
        // "images/main/userManage_16.png"));
        // pop.add(userManage);

        JCheckBoxMenuItem show = new JCheckBoxMenuItem("调试界面",
                MyIconFactory.getShowDebugIcon());
        show.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean b = ((JCheckBoxMenuItem) e.getSource()).getState();
                if (debugs == null) {
                    debugs = (MyTitleButton) toolBar.getComponent(toolBar
                            .getComponentCount() - 1);
                }
                if (!b && debugs.isSelected()) {
                    ((MyTitleButton) toolBar.getComponent(toolBar
                            .getComponentCount() - 2)).doClick();

                }
                debugs.setVisible(b);
                Debugs.getInstance().setShow(b);
            }
        });
        pop.add(show);

        JCheckBoxMenuItem voiceAlarm = new JCheckBoxMenuItem("声音报警",
                MyIconFactory.getVoiceWarnIcon());
        voiceAlarm.setSelected(MyConfigure.isVioceWarn());
        voiceAlarm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MyConfigure.setVoiceWarn(((JCheckBoxMenuItem) e.getSource())
                        .getState());
            }
        });
        pop.add(voiceAlarm);

//        JMenuItem help = new JMenuItem("帮 助 ", new ImageIcon(
//                "images/main/help_16.png"));
//        help.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    // 文件路径空格解决办法***.replace(" ", "\" \"")
//                    Runtime.getRuntime().exec("cmd /c start " + "help.chm");
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        });
//        pop.add(help);
    }

    private Rectangle getMaxBounds() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle bounds = new Rectangle(screenSize);
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());
        bounds.x += insets.left;
        bounds.y += insets.top;
        bounds.width -= insets.left + insets.right;
        bounds.height -= insets.top + insets.bottom;
        return bounds;
    }


    // 初始化中间面板
    private void initCenter() {
        centerCard = new CardLayout();
        centerPanel = new JPanel(centerCard);
        centerPanel.setOpaque(false);
        normalpanel.add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * 退出程序
     */
    private void exitSys() {
        int flag = JOptionPane.showConfirmDialog(null, "您确定要退出系统？", "关闭",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (flag == JOptionPane.OK_OPTION) {
            CollectOperate collect = CollectOperate.getInstance();
            collect.closeResource();
            Shell.this.setVisible(false);
            Shell.this.dispose();
            System.exit(0);
        } else
            return;
    }

    public void addItem(JComponent component, Icon icon, String text) {
        this.addItem(component, icon, text, true);
    }

    public void addItem(JComponent component, Icon icon, String text, boolean isVisible) {
        // 按钮
        MyTitleButton mtb = new MyTitleButton(text, icon);
        mtb.setVisible(isVisible);
        mtb.addActionListener(this);
        toolBar.add(mtb);
        // 内容
        centerPanel.add(component, text);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof MyTitleButton) {
            MyTitleButton temp = (MyTitleButton) e.getSource();
            if (!temp.isSelected()) {
                temp.setSelected(true);
                for (Component b : toolBar.getComponents()) {
                    if (b != temp && ((MyTitleButton) b).isSelected()) {
                        ((MyTitleButton) b).setSelected(false);
                    }
                }
                centerCard.show(centerPanel, temp.getText());
            }
        } else if (e.getSource() instanceof MySkipButton) {
            centerCard.show(centerPanel, "数据采集");
            MySkipButton temp = (MySkipButton) e.getSource();

            String str = "";
            switch (temp.getType()) {
                case 0:
                    str = "SF6";
                    break;
                case 1:
                    str = "WD";
                    break;
                case 2:
                    str = "SSJ";
                    break;
                case 3:
                    str = "TX";
                    break;
            }
            ChartView.getInstance().showPane(str);
            for (Component b : toolBar.getComponents()) {
                if (b instanceof MyTitleButton) {
                    MyTitleButton myTitleButton = (MyTitleButton) b;
                    if (myTitleButton.getText().equals("数据采集")) {
                        myTitleButton.setSelected(true);
                    } else {
                        myTitleButton.setSelected(false);
                    }
                }

            }
            MyButton4 jb = (MyButton4) buttonPanel.getComponent(temp.getType());
            myButtonGroup(jb, str);
        } else if (e.getSource() instanceof MyOutButton) {
            MyOutButton temp = (MyOutButton) e.getSource();
            String libstring = "";
            switch (temp.getType()) {
                case 0:
                    libstring = MyConfigure.getGzstr();
                    break;
                case 1:
                    libstring = MyConfigure.getJfstr();
                    break;
            }
            try {
                File file = new File(libstring);
                if (file.exists()) {
                    if (file.canWrite()) {
                        Runtime.getRuntime().exec(libstring);
                    }
                } else {
                    throw new FileNotFoundException();
                }
            } catch (FileNotFoundException e1) {
                JOptionPane.showMessageDialog(null, "文件不存在", "错误", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null, "文件无法打开", "错误", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        }
    }

    public void showButton(boolean istable) {
        buttonPanel.setVisible(istable);
    }

    private void myButtonGroup(ActionEvent e, String name) {
        MyButton4 jb = (MyButton4) e.getSource();
        myButtonGroup(jb, name);
    }

    private void myButtonGroup(MyButton4 jb, String name) {
        if (!jb.isSelected()) {
            jb.setSelected(true);
            for (Component b : buttonPanel.getComponents()) {
                if (b != jb && ((MyButton4) b).isSelected()) {
                    ((MyButton4) b).setSelected(false);
                }
            }
            ChartView.getInstance().showPane(name);
        }
    }

    public boolean isMaximized() {
        return isMaximized;
    }
}

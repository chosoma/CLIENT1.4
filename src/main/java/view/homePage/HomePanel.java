package view.homePage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import mytools.MyOutButton;
import mytools.MySkipButton;
import view.dataCollect.LadderFrame;
import view.icon.*;

public class HomePanel extends GlassPanel {

    private static HomePanel homePanel;
    static java.util.List<LadderFrame> ladders;

    Dimension size;

//    public static List<LadderFrame> getLadders() {
//        if (ladders == null) {
//            ladders = new ArrayList<>();
//        }
//        return ladders;
//    }

//    public static boolean contains(UnitBean unit) {
//        LadderFrame ladderFrame = getLadder(unit);
//        if (ladderFrame != null) {
//            return true;
//        } else {
//            return false;
//        }
//    }

//    public static LadderFrame getLadder(UnitBean unit) {
//        for (LadderFrame ladder : getLadders()) {
//            if (ladder.getUnit().equals(unit)) {
//                return ladder;
//            }
//        }
//        return null;
//    }

    public static HomePanel getInstance() {
        if (homePanel == null) {
            homePanel = new HomePanel();
        }
        return homePanel;
    }

    public HomePanel() {
        this.init();
        this.init2();
    }

    private void init2() {
//        java.util.List<UnitBean> units = SysUnitService.getUnitList();
//        for (UnitBean unit : units) {
//            UnitPanel unitPanel = new UnitPanel(unit);
//            this.add(unitPanel);
//            this.getUnitPanels().add(unitPanel);
//            try {
//                getLadders().add(new LadderFrame(unit, DataService.getOneUnitDatas(unit)));
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        this.addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                paintComponents(getGraphics());
//            }
//        });
    }

//    private java.util.List<UnitPanel> unitPanels;
//
//    public List<UnitPanel> getUnitPanels() {
//        if (unitPanels == null) {
//            unitPanels = new ArrayList<>();
//        }
//        return unitPanels;
//    }


//    public void addData(DataBean dataBean) {
//        UnitPanel unitPanel = getUnitPanel(dataBean.getUnitType(), dataBean.getUnitNumber());
//        if (unitPanel != null) {
//            unitPanel.addData(dataBean);
//        }
//        LadderFrame ladderFrame = getLadder(dataBean.getUnitType(), dataBean.getUnitNumber());
//        if (ladderFrame != null) {
//            ladderFrame.getjPanel().addData(dataBean);
//        }
//    }

//    public UnitPanel getUnitPanel(byte type, byte number) {
//        for (UnitPanel unit : unitPanels) {
//            if (unit.getUnitBean().getType() == type && unit.getUnitBean().getNumber() == number) {
//                return unit;
//            }
//        }
//        return null;
//    }

    JLabel title, companyName;

    MySkipButton mtbSF6, mtbTemp, mtbVari, mtbOther1, mtbOther2, mtbLadder;

    MyOutButton mobGZ, mobJF;

    private void init() {
        this.setOpaque(false);
        this.setLayout(new GridLayout(2, 3,50,50));
//        JPanel jPanel1 = new JPanel();
//        jPanel1.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));
//        jPanel1.setBackground(new Color(134, 73, 128));
//        jPanel1.setOpaque(false);
//        this.add(jPanel1,BorderLayout.NORTH);
        this.mtbSF6 = new MySkipButton("SF6", new SF6Icon(), 0);
        this.mtbTemp = new MySkipButton("温度", new TempIcon(), 1);
        this.mtbVari = new MySkipButton("伸缩节", new VariIcon(), 2);
//        jPanel1.add(mtbSF6);
//        jPanel1.add(mtbTemp);
//        jPanel1.add(mtbVari);

        this.mobGZ = new MyOutButton("故障定位", new UnKnownIcon(), 0);
        this.mobJF = new MyOutButton("局放检测", new UnKnownIcon(), 1);
//        JPanel jPanel2 = new JPanel();
//        jPanel2.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));
//        jPanel2.setBackground(new Color(152, 114, 110));
//        jPanel2.setOpaque(false);
//        this.add(jPanel2,BorderLayout.SOUTH);
        this.mtbLadder = new MySkipButton("图形", new LadderIcon(), 3);
//        jPanel2.add(mtbLadder);
//        this.addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                jPanel1.setPreferredSize(new Dimension(HomePanel.this.getWidth(),HomePanel.this.getHeight()/2));
//                jPanel2.setPreferredSize(new Dimension(HomePanel.this.getWidth(),HomePanel.this.getHeight()/2));
//            }
//        });

        this.add(mtbSF6);
        this.add(mtbTemp);
        this.add(mtbVari);
        this.add(mobGZ);
        this.add(mobJF);
        this.add(mtbLadder);

//        ladders = new ArrayList<>();
//        JPanel top = new JPanel(new BorderLayout());
//        top.setOpaque(false);
//        this.add(top, BorderLayout.NORTH);
//        top.add(Box.createVerticalStrut(40), BorderLayout.NORTH);
//        top.add(Box.createHorizontalStrut(40), BorderLayout.EAST);
//		title = new JLabel(MyLgoInfo.SoftName, JLabel.RIGHT);
//		title.setFont(new Font("楷体", Font.PLAIN, 48));
//		top.add(title, BorderLayout.CENTER);
        // this.add(new JLabel(), BorderLayout.CENTER);


//        JPanel bottom = new GlassPanel(new BorderLayout(), 0.3f);

//        companyName = new JLabel("© CopyRight " + MyLgoInfo.CopyrightName, JLabel.CENTER);
//        companyName.setFont(new Font("微软雅黑", Font.PLAIN, 12));
//        bottom.add(companyName, BorderLayout.NORTH);

//        this.add(bottom, BorderLayout.SOUTH);
    }

//    @Override
//    public void paintComponents(Graphics g) {
//        if (size == null) {
//            size = getSize();
//        }
//        for (UnitPanel unitPanel : unitPanels) {
//            if (Shell.getInstance().isMaximized()) {
//                int homewidth1 = size.width;
//                int homeheight1 = size.height;
//                int x1 = unitPanel.getX() + unitPanel.getWidth() / 2;
//                int y1 = unitPanel.getY() + unitPanel.getHeight() / 2;
//                double dx = (x1 * 1.0) / homewidth1;
//                double dy = (y1 * 1.0) / homeheight1;
//                int x2 = (int) (dx * getWidth() - unitPanel.getWidth() / 2);
//                int y2 = (int) (dy * getHeight() - unitPanel.getHeight() / 2);
//                unitPanel.setBounds(x2, y2, unitPanel.getWidth(), unitPanel.getHeight());
//            } else {
//                unitPanel.setBounds(unitPanel.getUnitBean().getX(), unitPanel.getUnitBean().getY(), unitPanel.getWidth(), unitPanel.getHeight());
//            }
//        }
//        size = getSize();
//    }


//    public synchronized static void closeLadderDialog() {
//        for (int i = ladders.size() - 1; i > 0; i--) {
//            LadderFrame ladderFrame = ladders.get(i);
//            if (ladderFrame.isVisible()) {
//                ladderFrame.setVisible(true);
//                return;
//            }
//        }
//    }


//    public LadderFrame getLadder(byte unittype, byte unitnumber) {
//        UnitBean unitBean = new UnitBean();
//        unitBean.setType(unittype);
//        unitBean.setNumber(unitnumber);
//        return getLadder(unitBean);
//    }
}
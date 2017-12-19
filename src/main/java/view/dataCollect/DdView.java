package view.dataCollect;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import domain.DataBean;
import domain.UnitBean;
import service.DataService;
import service.SysUnitService;
import view.Shell;
import view.icon.*;

public class DdView extends JPanel {


    Image backgroud;

    public DdView() {
        this.setLayout(null);
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        try {
            backgroud = ImageIO.read(this.getClass().getResource("indexGroud.png"));
        } catch (IOException ignored) {

        }
        init2();
        init3();

    }

    private void init3() {

    }

    static java.util.List<LadderFrame> ladders;

    Dimension size;

    public static List<LadderFrame> getLadders() {
        if (ladders == null) {
            ladders = new ArrayList<>();
        }
        return ladders;
    }


    java.util.List<UnitBean> units;

    private void init2() {
        units = SysUnitService.getUnitList();

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                paintComponents(getGraphics());
            }
        });
        showButtons = new ShowButton[12];

        showButtons[0] = new ShowButton(new VariIconMIN());
        for (int i = 1; i < 10; i++) {
            showButtons[i] = new ShowButton(new SF6IconMIN());
        }
        for (int i = 10; i < showButtons.length; i++) {
            showButtons[i] = new ShowButton(new TempIconMIN());
        }
        rs = new Rectangle[showButtons.length];
        rs[0] = new Rectangle(430, 280,35,35);
        rs[1] = new Rectangle(16, 227,35,35);
        rs[2] = new Rectangle(150, 210,35,35);
        rs[3] = new Rectangle(258, 128,35,35);
        rs[4] = new Rectangle(350, 170,35,35);
        rs[5] = new Rectangle(430, 205,35,35);
        rs[6] = new Rectangle(510, 240,35,35);
        rs[7] = new Rectangle(608, 244,35,35);
        rs[8] = new Rectangle(689, 285,35,35);
        rs[9] = new Rectangle(510, 310,35,35);
        rs[10] = new Rectangle(256, 197,35,35);
        rs[11] = new Rectangle(350, 240,35,35);
        for (int i = 0; i < showButtons.length; i++) {
            showButtons[i].setBounds(rs[i]);
            this.add(showButtons[i]);
        }
        for (UnitBean unitBean : units) {
            int i = unitBean.getPoint();
            showButtons[i].addUnit(unitBean);
        }
        for (ShowButton show : showButtons) {
            this.add(show.getjPanel());
        }
    }

    Rectangle[] rs;


    ShowButton[] showButtons;

    public void addData(DataBean dataBean) {
        UnitBean unitBean = getUnit(dataBean);
        if (unitBean != null) {
            showButtons[unitBean.getPoint()].addData(dataBean);
        }
    }


    public UnitBean getUnit(DataBean dataBean) {
        UnitBean unit = null;
        for (UnitBean unitBean : units) {
            if (dataBean.getUnitType() == unitBean.getType() && dataBean.getUnitNumber() == unitBean.getNumber()) {
                unit = unitBean;
                break;
            }
        }
        return unit;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroud != null) {
            g.drawImage(backgroud, 0, 0, getWidth(), getHeight(), this);
        }
        if (size == null) {
            size = getSize();
        }

        for (ShowButton showButton : showButtons) {
            int homewidth1 = size.width;
            int homeheight1 = size.height;
            int x1 = showButton.getX();
            int y1 = showButton.getY();
            double dx = (x1 * 1.0) / homewidth1;
            double dy = (y1 * 1.0) / homeheight1;
            int x2 = (int) (dx * getWidth());
            int y2 = (int) (dy * getHeight());
            showButton.setBounds(x2, y2, showButton.getWidth(), showButton.getHeight());
        }
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
        size = getSize();
    }
}

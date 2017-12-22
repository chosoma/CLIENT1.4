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
        setBounds();

        for (UnitBean unitBean : units) {
            int i = unitBean.getPoint();
            showButtons[i].addUnit(unitBean);
        }
        for (ShowButton show : showButtons) {
            this.add(show);
            this.add(show.getjPanel());
        }
    }

    private void setBounds() {
        Rectangle[] rs;
        rs = new Rectangle[showButtons.length];
        int width = getWidth();
        int height = getHeight();
        rs[0] = new Rectangle((int) (width / 1.71), (int) (height / 1.90), 35, 35);
        rs[1] = new Rectangle((int) (width / 40.0), (int) (height / 2.35), 35, 35);
        rs[2] = new Rectangle((int) (width / 4.71), (int) (height / 2.5), 35, 35);
        rs[3] = new Rectangle((int) (width / 2.81), (int) (height / 4.3), 35, 35);
        rs[4] = new Rectangle((int) (width / 2.06), (int) (height / 3.2), 35, 35);
        rs[5] = new Rectangle((int) (width / 1.71), (int) (height / 2.61), 35, 35);
        rs[6] = new Rectangle((int) (width / 1.42), (int) (height / 2.22), 35, 35);
        rs[7] = new Rectangle((int) (width / 1.19), (int) (height / 2.17), 35, 35);
        rs[8] = new Rectangle((int) (width / 1.06), (int) (height / 1.88), 35, 35);
        rs[9] = new Rectangle((int) (width / 1.42), (int) (height / 1.72), 35, 35);
        rs[10] = new Rectangle((int) (width / 2.81), (int) (height / 2.73), 35, 35);
        rs[11] = new Rectangle((int) (width / 2.06), (int) (height / 2.22), 35, 35);
        for (int i = 0; i < showButtons.length; i++) {
            showButtons[i].setBounds(rs[i]);
            JPanel jPanel = showButtons[i].getjPanel();
            int x = showButtons[i].getX();
            int y = showButtons[i].getY();
            switch (i) {
                case 0:
                case 1:
                case 2:
                case 9:
                case 10:
                case 11:
                    y += 35;
                    break;
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                    x -= getWidth() / 22;
                    y -= 100;
                    break;
            }
            jPanel.setBounds(x, y, getWidth() / 11, 100);
            jPanel.updateUI();
        }
    }


    public void setTitle(int point, String title) {
        showButtons[point].setTitle(title);
    }


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
        if (backgroud != null) {
            g.drawImage(backgroud, 0, 0, getWidth(), getHeight(), this);
        }
        setBounds();
    }
}

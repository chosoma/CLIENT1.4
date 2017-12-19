package view.dataCollect;


import domain.DataBean;
import domain.UnitBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

public class ShowButton extends JButton {

    java.util.List<UnitBean> unitList;
    JPanel jPanel;

    public ShowButton() {
        this(null, null);
    }

    public ShowButton(Icon icon) {
        this(null, icon);
    }

    public ShowButton(String text) {
        this(text, null);
    }

    JLabel titleLabel;

    JLabel jlA, jlB, jlC, jlsub1, jlsub2;

    JPanel center;

    LadderFrame ladderFrame;

    public JPanel getjPanel() {
        return jPanel;
    }

    public ShowButton(String text, Icon icon) {
        super(text, icon);
        unitList = new ArrayList<>();
        jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        jPanel.setLayout(new BorderLayout());
        jPanel.setBounds(0, 0, 100, 50);
        jPanel.setVisible(false);

        titleLabel = new JLabel("位置名称", JLabel.CENTER);
        jPanel.add(titleLabel, BorderLayout.NORTH);

        jlA = new JLabel("A", JLabel.CENTER);
        jlB = new JLabel("B", JLabel.CENTER);
        jlC = new JLabel("C", JLabel.CENTER);
        jlsub1 = new JLabel("--", JLabel.CENTER);
        jlsub2 = new JLabel("--", JLabel.CENTER);

        center = new JPanel();
        center.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        center.add(jlA);
        center.add(jlsub1);
        center.add(jlB);
        center.add(jlsub2);
        center.add(jlC);

        jPanel.add(center, BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                ladderFrame.getjPanel().setFlag(true);
                ladderFrame.getjPanel().clear();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ladderFrame.setHeadTitle(titleLabel.getText());
                ladderFrame.setUnitBeanList(unitList);
                ladderFrame.getjPanel().setFlag(false);
                ladderFrame.setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                jPanel.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                jPanel.setVisible(false);
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }
        });

        ladderFrame = LadderFrame.getInstance();
    }


    void addUnit(UnitBean unitBean) {
        unitList.add(unitBean);
        if (unitBean.getPlace() != null && !unitBean.getPlace().equals("")) {
            titleLabel.setText(unitBean.getPlace());
        }
    }

    void addData(DataBean dataBean) {
        String str = "";
        switch (dataBean.getUnitType()) {
            case 1:
                str = String.valueOf(dataBean.getPres());
                break;
            case 2:
                str = String.valueOf(dataBean.getVari());
                break;
            case 3:
                str = String.valueOf(dataBean.getTemp());
                break;
        }

        String xw = getXw(dataBean);
        switch (xw) {
            case "A":
                jlA.setText(str);
                break;
            case "B":
                jlB.setText(str);
                break;
            case "C":
                jlC.setText(str);
                break;
        }
    }

    private String getXw(DataBean dataBean) {
        String xw = "";
        for (UnitBean unit : unitList) {
            if (dataBean.getUnitType() == unit.getType() && dataBean.getUnitNumber() == unit.getNumber()) {
                xw = unit.getXw();
            }
        }
        return xw;
    }

}

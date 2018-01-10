package view.dataCollect;


import data.FormatTransfer;
import domain.DataBean;
import domain.PointBean;
import domain.UnitBean;
import view.icon.SF6IconMIN;
import view.icon.TempIconMIN;
import view.icon.VariIconMIN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

public class ShowButton extends JButton {

    private java.util.List<UnitBean> unitList;
    private JPanel jPanel;
    private PointBean pointBean;


    private JLabel titleLabel;

    public PointBean getPointBean() {
        return pointBean;
    }

    private JLabel jlA;
    private JLabel jlB;
    private JLabel jlC;
    private JLabel jldw;

    private JPanel center;

    private LadderFrame ladderFrame;

    JPanel getjPanel() {
        return jPanel;
    }

    public ShowButton(final PointBean pointBean) {
        this.pointBean = pointBean;
        switch (pointBean.getPoint()) {
            case 0:
                this.setIcon(new VariIconMIN());
                break;
            case 10:
            case 11:
                this.setIcon(new TempIconMIN());
                break;
            default:
                this.setIcon(new SF6IconMIN());
        }
        Font font = new Font(null, Font.PLAIN, 15);
        unitList = new ArrayList<>();
        jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        jPanel.setLayout(new BorderLayout());
        jPanel.setBounds(0, 0, 70, 70);
        jPanel.setVisible(false);

//        titleLabel = new JLabel(pointBean.getPlace(), JLabel.CENTER);
//        titleLabel.setFont(font);
//        jPanel.add(titleLabel, BorderLayout.NORTH);

        jlA = new JLabel("A", JLabel.CENTER);
        jlA.setFont(font);
        jlA.setForeground(new Color(105, 42, 42));
        jlB = new JLabel("B", JLabel.CENTER);
        jlB.setFont(font);
        jlB.setForeground(new Color(42, 105, 42));
        jlC = new JLabel("C", JLabel.CENTER);
        jlC.setFont(font);
        jlC.setForeground(new Color(42, 42, 105));
//        JLabel jlsub1 = new JLabel("--", JLabel.CENTER);
//        jlsub1.setFont(font);
//        JLabel jlsub2 = new JLabel("--", JLabel.CENTER);
//        jlsub2.setFont(font);
        jldw = new JLabel("", JLabel.CENTER);
        jldw.setFont(font);


        center = new JPanel();
        center.setLayout(new GridLayout(3, 1));
        center.add(jlA);
//        center.add(jlsub1);
        center.add(jlB);
//        center.add(jlsub2);
        center.add(jlC);

        jPanel.add(center, BorderLayout.CENTER);
//        jPanel.add(jldw, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
//                ladderFrame.getjPanel().setFlag(true);
//                ladderFrame.getjPanel().clear();
                ladderFrame.clear();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ladderFrame.setHeadTitle(ShowButton.this.pointBean.getPlace());
                ladderFrame.setUnitBeanList(unitList);
//                ladderFrame.getjPanel().setFlag(false);
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
//        if (unitBean.getPlace() != null && !unitBean.getPlace().equals("")) {
//            titleLabel.setText(unitBean.getPlace());
//        }
//        switch (unitBean.getType()) {
//            case 1:
//                jldw.setText("单位:MPa");
//                break;
//            case 2:
//                jldw.setText("单位:mm");
//                break;
//            case 3:
//                jldw.setText("单位:℃");
//                break;
//        }
    }

    void alignZero(String xw) {
        switch (xw) {
            case "A":
                jlA.setText("A:0.0");
                break;
            case "B":
                jlB.setText("B:0.0");
                break;
            case "C":
                jlC.setText("C:0.0");
                break;
        }
    }

    void addData(DataBean dataBean) {
        String str = "××";
        switch (dataBean.getUnitType()) {
            case 1:
                if (dataBean.isLowPres() || dataBean.isLowLock()) {
                    break;
                }
                if (dataBean.getPres() >= 0) {
                    str = String.valueOf(dataBean.getPres());
                }
                break;
            case 2:
                float vari = dataBean.getVari();
                if (vari < 0) {
                    break;
                }
                UnitBean unit = getUnit(dataBean);
                if (unit != null) {
                    vari = FormatTransfer.newScale(vari, unit.getInitvari());
                }
                str = String.valueOf(vari);
                break;
            case 3:
                str = String.valueOf(dataBean.getTemp());
                break;
        }

        String xw = getXw(dataBean);
        switch (xw) {
            case "A":
                jlA.setText("A:" + str);
                break;
            case "B":
                jlB.setText("B:" + str);
                break;
            case "C":
                jlC.setText("C:" + str);
                break;
        }
    }

    private String getXw(DataBean dataBean) {
        String xw = "";
        UnitBean unit = getUnit(dataBean);
        if (unit != null) {
            xw = unit.getXw();
        }
        return xw;
    }

    private UnitBean getUnit(DataBean dataBean) {
        for (UnitBean unit : unitList) {
            if (dataBean.getUnitType() == unit.getType() && dataBean.getUnitNumber() == unit.getNumber()) {
                return unit;
            }
        }
        return null;
    }

    public void setTitle(String title) {
        this.titleLabel.setText(title);
    }
}

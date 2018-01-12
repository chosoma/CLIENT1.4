package view.dataCollect;

import domain.DataBean;
import domain.PointBean;
import domain.UnitBean;
import service.SysUnitService;
import view.icon.SF6IconMIN;
import view.icon.TempIconMIN;
import view.icon.VariIconMIN;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HitchView extends JPanel {
    Image backgroud;

    public HitchView() {
        this.setLayout(null);
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        try {
            backgroud = ImageIO.read(this.getClass().getResource("indexGroud.png"));
        } catch (IOException ignored) {

        }
        init();

    }

    private java.util.List<PointBean> pointBeans;
    private java.util.List<UnitBean> units;
    private void init() {

    }



    @Override
    protected void paintComponent(Graphics g) {
        if (backgroud != null) {
            g.drawImage(backgroud, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

package view.dataCollect;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import domain.UnitBean;
import service.SensorService;
import domain.DataBean;
import domain.SensorAttr;

public class ChartView extends JPanel {

    private CardLayout centerCard;// 卡片布局
    private static ChartView CV = new ChartView();
    private AbcView panelSF6, panelWd, panelSSJ;
    private DdView panelSw;

    public CardLayout getCenterCard() {
        return centerCard;
    }

    private ChartView() {
        init();
        loadSensor();
    }

    public void loadSensor() {
        List<AbcUnitView> views = SensorService.getAbcUnitViews();
        panelSF6.clearAbcUnits();
        panelSSJ.clearAbcUnits();
        panelWd.clearAbcUnits();
        for (AbcUnitView view : views) {
            byte type = view.getUnitBean().getType();
            switch (type) {
                case 1:
                    panelSF6.addAbcUnit(view);
                    break;
                case 2:
                    panelSSJ.addAbcUnit(view);
                    break;
                case 3:
                    panelWd.addAbcUnit(view);
                    break;
            }
        }
        panelSF6.sort();
        panelWd.sort();
        panelSSJ.sort();
//        List<Line1800> ddviews = SensorService.getDdUnitViews();
//        panelSw.setViews(ddviews);
    }

    public static ChartView getInstance() {
        return CV;
    }

    private void init() {
        centerCard = new CardLayout();
        this.setLayout(centerCard);

        panelSF6 = new AbcView(SensorAttr.Sensor_SF6);
        this.add(panelSF6, "SF6");
        panelWd = new AbcView(SensorAttr.Sensor_WD);
        this.add(panelWd, "WD");
        panelSSJ = new AbcView(SensorAttr.Sensor_SSJ);
        this.add(panelSSJ, "SSJ");
        panelSw = new DdView();
        this.add(panelSw, "TX");

    }

    public void receDatas(DataBean... datas) {
        List<DataBean> dataBeans = new ArrayList<DataBean>();
        dataBeans.addAll(Arrays.asList(datas));
        receDatas(dataBeans);
    }

    public void receDatas(List<DataBean> dataList) {
        for (DataBean data : dataList) {
            byte type = data.getUnitType();
            switch (type) {
                case 1:
                    panelSF6.addData(data);
                    break;
                case 2:
                    panelSSJ.addData(data);
                    break;
                case 3:
                    panelWd.addData(data);
                    break;
            }
            panelSw.addData(data);
        }
    }

    public void refresh(UnitBean unit) {
        switch (unit.getType()) {
            case 1:
                panelSF6.refresh(unit);
                break;
            case 2:
                panelSSJ.refresh(unit);
                break;
            case 3:
                panelWd.refresh(unit);
                break;
        }
    }

    public void addNewUnit(AbcUnitView abcUnitView) {
        switch (abcUnitView.getUnitBean().getType()) {
            case 1:
                panelSF6.addAbcUnit(abcUnitView);
                break;
            case 2:
                panelSSJ.addAbcUnit(abcUnitView);
                break;
            case 3:
                panelWd.addAbcUnit(abcUnitView);
                break;
            default:
        }
    }


    public void showPane(String name) {
        centerCard.show(this, name);
    }
}

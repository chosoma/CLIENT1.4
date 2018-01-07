package view.dataCollect;

import data.FormatTransfer;
import domain.DataBean;
import domain.UnitBean;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

import javax.swing.*;
import java.awt.*;

public class LineLadder {
    private static LineLadder instance;

    public static LineLadder getInstance() {
        if (instance == null) {
            synchronized (LineLadder.class) {
                if (instance == null) {
                    instance = new LineLadder();
                }
            }
        }
        return instance;
    }

    public JPanel getJPanel(UnitBean unitBean, java.util.List<DataBean> dataBeans, Dimension dimension) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());

        switch (unitBean.getType()) {
            case 1:
//                jPanel.setLayout(new GridLayout(3, 1));
//                jPanel.add(getChart(unitBean, dataBeans, "MPa", dimension));
                jPanel.add(getChart(unitBean, dataBeans, dimension), BorderLayout.CENTER);
//                jPanel.add(getChart(unitBean, dataBeans, "kg/m³", dimension));
//                jPanel.add(getChart(unitBean, dataBeans, "℃", dimension));
                break;
            case 2:
//                jPanel.setLayout(new BorderLayout());
                jPanel.add(getChart(unitBean, dataBeans, dimension), BorderLayout.CENTER);
                break;
            case 3:
//                jPanel.setLayout(new BorderLayout());
                jPanel.add(getChart(unitBean, dataBeans, dimension), BorderLayout.CENTER);
                break;
        }
        return jPanel;
    }

    private ChartPanel getChart(UnitBean unitBean, java.util.List<DataBean> dataBeans, Dimension dimension) {
        TimeSeriesCollection lineDataset = new TimeSeriesCollection();

        switch (unitBean.getType()) {
            case 1:
                TimeSeries timeSeries1 = new TimeSeries("压力(MPa)");
                for (DataBean data : dataBeans) {
                    timeSeries1.add(new Second(data.getDate()), data.getPres());
                }
                TimeSeries timeSeries2 = new TimeSeries("密度(MPa)");
                for (DataBean data : dataBeans) {
                    timeSeries2.add(new Second(data.getDate()), data.getDen());
                }
                TimeSeries timeSeries3 = new TimeSeries("温度(℃)");
                for (DataBean data : dataBeans) {
                    timeSeries3.add(new Second(data.getDate()), data.getTemp());
                }
                lineDataset.addSeries(timeSeries1);
                lineDataset.addSeries(timeSeries2);
                lineDataset.addSeries(timeSeries3);
                break;
            case 2:
                TimeSeries timeSeries22 = new TimeSeries("位移(mm)");
                for (DataBean data : dataBeans) {
                    float vari = data.getVari();
                    if (unitBean.getInitvari() != 0) {
                        vari = FormatTransfer.newScale(data.getVari(), unitBean.getInitvari());
                    }
                    timeSeries22.add(new Second(data.getDate()), vari);
                }
                lineDataset.addSeries(timeSeries22);
                break;
            case 3:
                TimeSeries timeSeries33 = new TimeSeries("温度(℃)");
                for (DataBean data : dataBeans) {
                    timeSeries33.add(new Second(data.getDate()), data.getTemp());
                }
                lineDataset.addSeries(timeSeries33);
                break;
        }
        JFreeChart chart = ChartFactory.createTimeSeriesChart(null,
                null,
                null,
                lineDataset,
                true,
                true,
                true);
        chart.setTextAntiAlias(false);
//        chart.setTitle(new TextTitle("", new Font(null, Font.PLAIN, 15)));
        chart.setAntiAlias(true);//设置抗锯齿
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setAxisOffset(new RectangleInsets(10, 10, 10, 10));//图片区与坐标轴的距离
        plot.setOutlinePaint(Color.PINK);
        plot.setInsets(new RectangleInsets(15, 15, 15, 15));//坐标轴与最外延的距离
//        AxisSpace as = new AxisSpace();
//        as.setLeft(25);
//        as.setRight(25);
//        plot.setFixedRangeAxisSpace(as);
        chart.setPadding(new RectangleInsets(5, 5, 5, 5));
        chart.setNotify(true);
        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) plot.getRenderer();
        xylineandshaperenderer.setBaseShapesVisible(true);
        XYItemRenderer xyitem = plot.getRenderer();
        xyitem.setBaseItemLabelsVisible(true);//设置值显示 true 显示 false 不显示
        xyitem.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.INSIDE10, TextAnchor.BASELINE_LEFT));
        xyitem.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
        xyitem.setBaseItemLabelFont(new Font("Dialog", Font.BOLD, 14));
        plot.setRenderer(xyitem);
        ChartPanel chartPanel = new ChartPanel(chart);
//        switch (unitBean.getType()) {
//            case 1:
//                chartPanel.setSize(new Dimension((int) dimension.getWidth() / 3, (int) dimension.getHeight() / 3));
//                break;
//            default:
//                chartPanel.setSize(dimension);
//        }
        return chartPanel;
    }

}

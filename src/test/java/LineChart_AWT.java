import domain.DataBean;
import domain.DataSearchPara;
import domain.UnitBean;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import service.DataService;

import java.sql.SQLException;
import java.util.Date;

public class LineChart_AWT extends ApplicationFrame {
    public LineChart_AWT(String applicationTitle, String chartTitle) {
        super(applicationTitle);
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle,
                "Years", "Number of Schools",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        setContentPane(chartPanel);
    }

    private DefaultCategoryDataset createDataset() {
        UnitBean unit = new UnitBean();
        unit.setType((byte) 2);
        unit.setNumber((byte) 1);
        DataSearchPara dsp = new DataSearchPara();
        dsp.setT2(new Date());
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            java.util.List<DataBean> dataBeans = DataService.getBetween(unit, dsp);
            for (DataBean data : dataBeans) {
                dataset.addValue(data.getVari(), "压力", data.getDate());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        dataset.addValue( 15 , "schools" , "1970" );
//        dataset.addValue( 30 , "schools" , "1980" );
//        dataset.addValue( 60 , "schools" ,  "1990" );
//        dataset.addValue( 120 , "schools" , "2000" );
//        dataset.addValue( 240 , "schools" , "2010" );
//        dataset.addValue( 300 , "schools" , "2014" );
        return dataset;
    }

    public static void main(String[] args) {
        LineChart_AWT chart = new LineChart_AWT(
                "School Vs Years",
                "Numer of Schools vs years");

        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }
}

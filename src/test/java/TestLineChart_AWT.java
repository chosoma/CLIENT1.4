import domain.DataBean;
import domain.DataSearchPara;
import domain.UnitBean;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import service.DataService;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Date;

public class TestLineChart_AWT {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        JFreeChart lineChart = ChartFactory.createLineChart(
                "",
                "Years", "Number of Schools",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        jFrame.setContentPane(chartPanel);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    private static CategoryDataset createDataset() {
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

}

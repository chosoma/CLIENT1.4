package view.dataCollect;

import java.util.Date;

import domain.UnitBean;
import mytools.ChartUtil;
import mytools.MyUtil;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleEdge;

import domain.DataBean;
//import domain.SensorBean;

public class Line1800 extends ChartContainer {

	private TimeSeries timeseries1, timeseries2;
	private XYLineAndShapeRenderer renderer1, renderer2;
	private TextTitle subTitle;
	private String yqmc, sjbh, dw;
	Float exceedUp;

//	public Line1800(SensorBean sensor) {
//		yqmc = sensor.getType();
//		sjbh = sensor.getSjbh();
//		dw = sensor.getDw();
//		exceedUp = sensor.getAlarm();
//		this.setUsername(sjbh);
//		JFreeChart chart = createJFreeChart();
//		ChartPanel chartPanel = ChartUtil.getChartPanel(chart);
//		super.initialize(chartPanel);
//		setTitle(yqmc + ": " + sjbh + " 过程线");
//	}
	public Line1800(UnitBean sensor) {
		yqmc = sensor.getName();
		sjbh = sensor.getName();
//		dw = sensor.getDw();
//		exceedUp = sensor.getAlarm();
		this.setName(sjbh);
		JFreeChart chart = createJFreeChart();
		ChartPanel chartPanel = ChartUtil.getChartPanel(chart);
		super.initialize(chartPanel);
		setTitle(yqmc + ": " + sjbh + " 过程线");
	}

	private JFreeChart createJFreeChart() {
		timeseries1 = new TimeSeries("工程值");
		timeseries1.setMaximumItemCount(ChartUtil.MaximumItemCount);// 设置折线显示最大点数
		TimeSeriesCollection datasets1 = new TimeSeriesCollection(timeseries1);

		timeseries2 = new TimeSeries("温度");
		timeseries2.setMaximumItemCount(ChartUtil.MaximumItemCount);// 设置折线显示最大点数
		TimeSeriesCollection datasets2 = new TimeSeriesCollection(timeseries2);
		// 创建图表
		JFreeChart chart = ChartUtil.create1800Chart(null, null, "工程值" + "("
				+ dw + ")", "温度(℃)", datasets1, datasets2, false, true);

		subTitle = new TextTitle("", MyUtil.FONT_12);
		chart.addSubtitle(subTitle);
		// 设置图例位置
		LegendTitle legendTitle = new LegendTitle(chart.getPlot());
		legendTitle.setItemPaint(ChartUtil.FontColor);
		legendTitle.setPosition(RectangleEdge.TOP);
		legendTitle.setHorizontalAlignment(HorizontalAlignment.LEFT);
		chart.addSubtitle(legendTitle);

		// 数据区设置
		XYPlot xyPlot = chart.getXYPlot();
		// 设置数据区无边框
		// xyPlot.setOutlineVisible(false);
		this.setExceedMarker(xyPlot);

		// 设置Y轴 工程值
		NumberAxis rangeAxis1 = (NumberAxis) xyPlot.getRangeAxis(0);
		// 设置工程值刻度可调整集合
		rangeAxis1.setStandardTickUnits(ChartUtil.myNumTickUnits(2));

		return chart;
	}

	// 设置极限值警戒线
	private void setExceedMarker(XYPlot xyPlot) {
		if (exceedUp != null) {
			Marker markerH = ChartUtil.getMarker("Max", exceedUp);
			xyPlot.addRangeMarker(0, markerH, Layer.BACKGROUND, false);
		}
	}

	/**
	 * 清空数据
	 */
	public void clearData(String date) {
		timeseries1.clear();
		timeseries2.clear();
		subTitle.setText(date);
	}

	// 添加数据：时间、工程值 、温度<yqlx,sjbh, mkh,dh, time, F1, F2, GCZ1,GCZ2,isExceed>
	@Override
	public boolean addData(DataBean data) {
		// try 抓住异常，不绘制点，返回添加下一个点
//		Double gcz = data.getGcz();
//		Date time = data.getSj();
//		if (gcz != null) {
//			try {
//				timeseries1.add(new Second(time), (Number) gcz);
				subTitle.setText(ChartUtil.getSubTitle(timeseries1));
//			} catch (SeriesException s) {
//				 do nothing 重复时间的数据
//				return false;
//			}
//		}
//		Float wd = ((DataBean) data).getF2();
//		if (wd != null) {
//			try {
//				timeseries2.add(new Second(time), (Number) wd);
//			} catch (SeriesException s) {
//				 do nothing 重复时间的数据
//				return false;
//			}
//		}
		return true;

	}

	@Override
	public void setSeriesShapesVisible(int series, boolean visible) {
		renderer1.setSeriesShapesVisible(series, visible);
		renderer2.setSeriesShapesVisible(series, visible);
	}

	public boolean matchData(DataBean data) {
//		return data.getSjbh().equals(sjbh);
		return true;
	}
}

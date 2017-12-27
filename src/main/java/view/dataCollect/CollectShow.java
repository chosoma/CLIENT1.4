package view.dataCollect;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import model.Collect_DataModel;
import mytools.MyButton3;
import mytools.MyTCR;
import mytools.MyUtil;
import view.Shell;

import com.PlayWAV;

import domain.DataBean;
import domain.SensorAttr;
import view.homePage.HomePanel;

public class CollectShow extends JPanel {

    private static CollectShow CS = new CollectShow();
    private JTable dataTable;
    private Collect_DataModel dataModel = Collect_DataModel.getInstance();
    private JLabel jlbWarn;
    private boolean isNeedRoll = true;

    private JPanel warnPanel;

    public JPanel getWarnPanel() {
        return warnPanel;
    }

    public void setPlace(String place) {
        jlbWarn.setText(place);
    }

    private CollectShow() {
        this.init();
    }

    public static CollectShow getInstance() {
        return CS;
    }

    public void init() {

        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.initTable();
        this.loadView();
    }

    @Override
    public boolean isVisible() {
        return super.isVisible();
    }

    /**
     * 初始化数据表格
     */
    private void initTable() {
        dataTable = new JTable(dataModel);
        MyTCR tcr = new MyTCR();
        dataTable.setDefaultRenderer(String.class, tcr);
        dataTable.setDefaultRenderer(Number.class, tcr);
        dataTable.setDefaultRenderer(Float.class, tcr);
        dataTable.setDefaultRenderer(Double.class, tcr);
        dataTable.setDefaultRenderer(Date.class, tcr);
        dataTable.setDefaultRenderer(Object.class, tcr);
        // 设置表头居中
        JTableHeader tableHeader = dataTable.getTableHeader();
        DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader
                .getDefaultRenderer();
        hr.setHorizontalAlignment(SwingConstants.CENTER);
        Dimension dimension = hr.getSize();
        dimension.height = MyUtil.HeadHeight;
        hr.setPreferredSize(dimension);// 设置表头高度
        tableHeader.setDefaultRenderer(hr);
        // 表头不可拖动
        tableHeader.setReorderingAllowed(false);
        // 列宽不可修改
        tableHeader.setResizingAllowed(false);

        // 自动排序
        dataTable.setAutoCreateRowSorter(true);
        dataTable.setRowHeight(MyUtil.RowHeight);// 设置行高
        // 设置单行选中
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        dataTable.getColumn("单元ID").setPreferredWidth(35);
        dataTable.getColumn("类型").setPreferredWidth(35);
        dataTable.getColumn("单元编号").setPreferredWidth(50);
//        dataTable.getColumn("网关类型").setPreferredWidth(50);
        dataTable.getColumn("网关编号").setPreferredWidth(50);
        dataTable.getColumn("密度").setPreferredWidth(50);
        dataTable.getColumn("压力(Pa)").setPreferredWidth(50);
        dataTable.getColumn("温度(℃)").setPreferredWidth(50);
        dataTable.getColumn("位移(mm)").setPreferredWidth(50);
        dataTable.getColumn("电压(Ｖ)").setPreferredWidth(50);
        dataTable.getColumn("时间").setPreferredWidth(120);
    }

    private CardLayout centerCard;// 卡片布局
    private JPanel show;
    ChartView chartView;

    /**
     * 初始化数据采集显示界面
     */
    private void loadView() {

        centerCard = new CardLayout();
        show = new JPanel(centerCard);
        show.setOpaque(false);
        this.add(show, BorderLayout.CENTER);

        chartView = ChartView.getInstance();
        show.add(chartView, "chart");

        JScrollPane jspTable = new JScrollPane(dataTable);
        jspTable.setBorder(null);
        show.add(jspTable, "table");

        warnPanel = new JPanel(new BorderLayout());
        warnPanel.setOpaque(true);
        warnPanel.setVisible(false);
        warnPanel.setBackground(AbcUnitView.colorWarn);
        this.add(warnPanel, BorderLayout.EAST);

        jlbWarn = new JLabel("", JLabel.CENTER);
        jlbWarn.setForeground(Color.BLUE);
        jlbWarn.setFont(MyUtil.FONT_16);
        warnPanel.add(jlbWarn, BorderLayout.CENTER);

        JButton jb = new MyButton3("解除警报", new ImageIcon("images/delete.png"));
//        jb.setToolTipText("解除报警");
        jb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PlayWAV.getInstance().stop();
                jlbWarn.setText(null);
            }
        });
        warnPanel.add(jb, BorderLayout.EAST);

    }

    /**
     * 接受数据
     */
    // MKH,BH,YQXH,YQMC,SJBH,SJ,F1,F2,GCZ,DW,YX
    public synchronized void receData(DataBean data) {
//        HomePanel.getInstance().addData(data);
        dataModel.addData(data);
        chartView.receDatas(data);
//        if (data.getYz() != null) {
//            String warm = data.getType() + "：" + data.getSjbh() + (data.getXw() == null ? " " : (" " + data.getXw() + "相")) + " 值" + data.getGcz() + " 预警";
//            jlbWarn.setText(warm);
//        if(!warnPanel.isVisible())warnPanel.setVisible(true);
//        PlayWAV.getInstance().play();//报警
//        }
    }

    public synchronized void receDatas(List<DataBean> dataList) {
        dataModel.clearData();
        for (DataBean data : dataList) {
            dataModel.addData(data);
        }
        chartView.receDatas(dataList);
    }

    /**
     * 接受油压数据
     *
     * @param datas
     */
    public synchronized void receDatas(DataBean... datas) {
        chartView.receDatas(datas);
        for (DataBean data : datas) {
            dataModel.addData(data);
//            if (data.getYz() != null && !data.getYz().equals(SensorAttr.Value_GY_N)) {
//                String warm = data.getType() + "：" + data.getSjbh() + " " + data.getXw() + " 相 " + data.getYz();
//                jlbWarn.setText(warm);
            warnPanel.setVisible(true);
            PlayWAV.getInstance().play();
//            }
        }
    }

    public synchronized void clearData() {
        dataModel.clearData();
    }

    public boolean isNeedRoll() {
        return isNeedRoll;
    }

    public void showTable(boolean istable) {
        isNeedRoll = !istable;
        Shell.getInstance().showButton(isNeedRoll);
        if (istable) {
            centerCard.show(show, "table");
        } else {
            centerCard.show(show, "chart");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int w = getWidth(), h = getHeight();
        Color c1 = new Color(242, 242, 242), c2 = Color.WHITE;
        g2.setPaint(new GradientPaint(0, 0, c1, 0, 250, c2));
        g2.fillRect(0, 0, w, 250);
        g2.setColor(c2);
        g2.fillRect(0, 250, w, h);
        g2.dispose();
        super.paintComponent(g);
    }
}

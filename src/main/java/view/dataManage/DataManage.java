package view.dataManage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import model.DataManageModel;
import mytools.Check2SPinner;
import mytools.MyButton2;
import mytools.MyButton3;
import mytools.MyTCR;
import mytools.MyToolPanel;
import mytools.MyUtil;
import service.DataManageService;
import service.SensorService;
import util.MyExcelUtil;
import view.icon.MyIconFactory;
import domain.DataSearchPara;

public class DataManage extends JPanel {

    private JComboBox jcbType, jcbSJBH, jcbXW;
    JTextArea jtaXW;
    private Check2SPinner c2s1, c2s2;
    private JTable table;
    private static DataManage DM = null;

    private DataManage() {

        this.setLayout(new BorderLayout());
        // 加载工具栏
        this.initToolPane();
        // 加载表格
        this.initTable();
    }

    // 懒汉式
    public static DataManage getInstance() {
        if (DM == null) {
            synchronized (DataManage.class) {
                if (DM == null)
                    DM = new DataManage();
            }
        }
        return DM;
    }

    /**
     * 初始化顶部工具栏面板 jToolPane：顶部工具栏面板有jtb1、jtb2左右两个工具栏，分别加载左边和右边的工具按钮
     */
    private void initToolPane() {

        /**
         * 工具栏面板
         */
        JPanel toolPane = new MyToolPanel(new BorderLayout());
        toolPane.setPreferredSize(new Dimension(toolPane.getWidth(), 36));
        this.add(toolPane, BorderLayout.NORTH);

        /**
         * 工具栏左半边
         */
        JPanel toolBarL = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 2));
        toolBarL.setOpaque(false);
        toolPane.add(toolBarL, BorderLayout.WEST);

        toolBarL.add(Box.createHorizontalStrut(5));

        // 设置的文本设置为html代码即可实现JLabel多行显示
        JTextArea jta1 = new JTextArea("单元\n类型");
        jta1.setOpaque(false);
        jta1.setEditable(false);
        toolBarL.add(jta1);

        // 仪器类型
        jcbType = new JComboBox(new DefaultComboBoxModel(SensorService.getTypes()));
        jcbType.setSelectedItem(null);
        jcbType.setMaximumSize(new Dimension(80, 20));
        jcbType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String type = (String) jcbType.getSelectedItem();
//                if (type.equals(SensorAttr.Sensor_SW)) {
//                    jtaXW.setVisible(false);
//                    jcbXW.setVisible(false);
//                } else {
//                    jtaXW.setVisible(true);
//                    jcbXW.setVisible(true);
//                }
                refreshSJBH(type);
            }
        });
        toolBarL.add(jcbType);

        toolBarL.add(Box.createHorizontalStrut(5));

        JTextArea jta3 = new JTextArea("监测\n位置");
        jta3.setOpaque(false);
        jta3.setEditable(false);
        toolBarL.add(jta3);

        jcbSJBH = new JComboBox();
        jcbSJBH.setMaximumSize(new Dimension(100, 20));
        toolBarL.add(jcbSJBH);
        JButton refresh = MyUtil.CreateButton(MyIconFactory.getReFreshIcon(),
                "刷新设计编号");
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String type = (String) jcbType.getSelectedItem();
                if (type == null || type.equals("")) {
                    JOptionPane.showMessageDialog(null, "请先选择单元类型", "提示",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    refreshSJBH(type);
                }
            }
        });
        toolBarL.add(refresh);
        toolBarL.add(Box.createHorizontalStrut(5));

        jtaXW = new JTextArea("测点\n相位");
        jtaXW.setOpaque(false);
        jtaXW.setEditable(false);
        toolBarL.add(jtaXW);

        jcbXW = new JComboBox<>(new String[]{"全部", "A", "B", "C"});
        toolBarL.add(jcbXW);
        toolBarL.add(Box.createHorizontalStrut(5));

        JTextArea jta4 = new JTextArea("起始\n时间");
        jta4.setOpaque(false);
        jta4.setEditable(false);
        toolBarL.add(jta4);

        Calendar ca = Calendar.getInstance();
        Date date2 = ca.getTime();
        ca.add(Calendar.DAY_OF_MONTH, -1);
        Date date = ca.getTime();
        // 起始时间
        c2s1 = new Check2SPinner(false, date);
        c2s1.setMaximumSize(new Dimension(165, 17));
        toolBarL.add(c2s1);

        toolBarL.add(Box.createHorizontalStrut(5));

        JTextArea jta5 = new JTextArea("终止\n时间");
        jta5.setOpaque(false);
        jta5.setEditable(false);
        toolBarL.add(jta5);

        // 终止时间
        c2s2 = new Check2SPinner(false, date2);
        c2s2.setMaximumSize(new Dimension(165, 17));
        toolBarL.add(c2s2);

        toolBarL.add(Box.createHorizontalStrut(5));
        JButton search = new MyButton2("查询", new ImageIcon(
                "images/search_24.png"));
        search.setFont(MyUtil.FONT_14);
        search.setPreferredSize(new Dimension(65, 32));
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (jcbType.getSelectedIndex() < 0) {
                        JOptionPane.showMessageDialog(null, "请先选择设备类型", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    DataSearchPara para = getSearchConditon();
                    DataManageModel model = SensorService.getDataModel(para.getType());
                    // 如果表格模型没有发生变化，则返回
                    if (!table.getModel().equals(model)) {
                        table.setModel((TableModel) model);
                        TableColumn column = table.getColumn("时间");
                        column.setMinWidth(135);
                        column.setPreferredWidth(140);
                        column.setMaxWidth(180);
                    }
                    String sqlHead = model.getSelectSQL();
//                    if (jcbSJBH.getSelectedIndex() != 0) {
//                        sqlHead += " u.place = '" + jcbSJBH.getSelectedItem() + "'\n";
//                    }
//                    if (jcbXW.getSelectedIndex() != 0) {
//                        sqlHead += " u.xw = '" + jcbXW.getSelectedItem() + "'\n";
//                    }

                    List<Vector<Object>> datas = DataManageService.getTableData(sqlHead, para);
                    model.addDatas(datas);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "提示",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        toolBarL.add(search);

        /**
         * 工具栏右半边
         */
        JPanel toolBarR = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 2));
        toolBarR.setOpaque(false);
        toolPane.add(toolBarR, BorderLayout.EAST);

        Dimension buttonSize = new Dimension(34, 32);

        JButton delete = new MyButton3(new ImageIcon("images/delete.png"));
        delete.setToolTipText("删除选中的数据");
        delete.setPreferredSize(buttonSize);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 得到鼠标选中行
                int[] selRows = table.getSelectedRows();
                // 做个判断，如果没有选中行，则弹出提示
                if (selRows.length <= 0) {
                    JOptionPane.showMessageDialog(null, "请选中要删除的数据行", "提示",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 提示"是"、"否"、"取消"删除操作
                int flag = JOptionPane.showConfirmDialog(null, "确定要删除选中数据？",
                        "删除", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (flag != JOptionPane.OK_OPTION)
                    return;
                int[] ids = new int[selRows.length];
                for (int i = 0; i < selRows.length; i++) {
                    ids[i] = (Integer) table.getValueAt(selRows[i], 0);
                }
                try {
                    DataManageService.deleteData(ids);
                    JOptionPane.showMessageDialog(null, "数据已成功删除", "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                    int[] rows = new int[selRows.length];
                    int j = 0;
                    for (int i : selRows) {
                        rows[j] = table.convertRowIndexToModel(i);
                        j++;
                    }
                    Arrays.sort(rows);// rows升序排序
                    DataManageModel model = (DataManageModel) table.getModel();
                    model.removeRows(rows);
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null,
                            "数据删除失败:" + e1.getMessage(), "提示",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        toolBarR.add(delete);

        JButton clear = new MyButton3(new ImageIcon("images/clear_24.png"));
        clear.setToolTipText("清空表中数据");
        clear.setPreferredSize(buttonSize);
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 做个判断，如果没有选中行，则弹出提示
                int rowCount = table.getRowCount();
                if (rowCount == 0) {
                    JOptionPane.showMessageDialog(null, "当前表中无数据，或数据已清空", "提示",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 提示"是"、"否"、"取消"删除操作
                int flag = JOptionPane.showConfirmDialog(null, "确定要清空表中数据？",
                        "清空", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (flag != JOptionPane.OK_OPTION)
                    return;
                int[] ids = new int[rowCount];
                for (int i = 0; i < rowCount; i++) {
                    ids[i] = (Integer) table.getValueAt(i, 0);
                }
                try {
                    DataManageService.deleteData(ids);
                    JOptionPane.showMessageDialog(null, "数据已成功清空", "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                    DataManageModel model = (DataManageModel) table.getModel();
                    model.clearData();
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null,
                            "数据清空失败：" + e1.getMessage(), "提示",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        toolBarR.add(clear);

        JButton export = new MyButton3(new ImageIcon(
                "images/database_download_24.png"));
        export.setToolTipText("将表中数据下载到Excel");
        export.setPreferredSize(buttonSize);
        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "当前表中无数据，或数据已清空", "提示",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        MyExcelUtil.Export2Excel(table, "数据");
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(null,
                                "导出失败：" + e1.getMessage(), "失败",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });
        toolBarR.add(export);

        JButton print = new MyButton3(new ImageIcon("images/printer_24.png"));
        print.setToolTipText("打印表格");
        print.setPreferredSize(buttonSize);
        print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (table.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "当前表中无数据，或数据已清空", "提示",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    try {
                        table.print();
                    } catch (PrinterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        toolBarR.add(print);
    }

    /**
     * 初始化中间表格部分
     */
    private void initTable() {
        // 中部JTable
        table = new JTable();
        this.initializeTable();// 初始化表格

        // 将JTable添加到滚动面板中
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        this.add(scrollPane, BorderLayout.CENTER);
    }

    // 初始化表格
    private void initializeTable() {
        MyTCR tcr = new MyTCR();
        table.setDefaultRenderer(String.class, tcr);
        table.setDefaultRenderer(Number.class, tcr);
        table.setDefaultRenderer(Float.class, tcr);
        table.setDefaultRenderer(Double.class, tcr);
        table.setDefaultRenderer(Date.class, tcr);

        // 表头设置
        JTableHeader tableHeader = table.getTableHeader();
        DefaultTableCellRenderer dtcr = (DefaultTableCellRenderer) tableHeader
                .getDefaultRenderer();
        dtcr.setHorizontalAlignment(SwingConstants.CENTER);// 表头居中
        Dimension dimension = dtcr.getSize();
        dimension.height = MyUtil.HeadHeight;
        dtcr.setPreferredSize(dimension);// 设置表头高度
        tableHeader.setDefaultRenderer(dtcr);
        // 表头不可拖动
        tableHeader.setReorderingAllowed(false);
        // 列宽不可修改
        tableHeader.setResizingAllowed(false);

        // 自动排序
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(MyUtil.RowHeight);// 设置行高

    }

    /**
     * 获取查询条件
     *
     * @return
     * @throws Exception
     */
    public DataSearchPara getSearchConditon() throws Exception {
        DataSearchPara para = new DataSearchPara();
        // 物理量
        String type = (String) jcbType.getSelectedItem();
        para.setType(type);
//        if (!type.equals(SensorAttr.Sensor_SW)) {//             相位
//            String xw = (String) jcbXW.getSelectedItem();
//        }

        int sjbhIdenx = jcbSJBH.getSelectedIndex();
        if (sjbhIdenx < 0) {
            throw new Exception("监测位置不存在");
        } else if (sjbhIdenx == 0) {// 全部
            // do nothing
        } else {
            String sjbh = (String) jcbSJBH.getSelectedItem();// 得到"设计编号"
            para.setPlace(sjbh);
        }
        sjbhIdenx = jcbXW.getSelectedIndex();
        if (sjbhIdenx < 0) {
            throw new Exception("相位不存在");
        } else if (sjbhIdenx == 0) {// 全部
            // do nothing
        } else {
            String xw = (String) jcbXW.getSelectedItem();// 得到"设计编号"
            para.setXw(xw);
        }
        Date startT = c2s1.getTime(), endT = c2s2.getTime();
        if (startT == null) {// 起始时间未选中
            if (endT == null) {// 终止时间未选中
                // do nothing
            } else {// 终止时间选中
                para.setT2(endT);
            }
        } else {// 起始时间选中
            para.setT1(startT);
            if (endT == null) {// 终止时间未选中
                // do nothing
            } else {// 终止时间选中
                if (startT.after(endT)) {
                    throw new Exception("起始时间应位于终止时间之前");
                }
                para.setT2(endT);
            }
        }
        return para;

    }

    protected void refreshSJBH(String type) {
//        Vector<Integer> sjbhs = DataManageService.getUnitNumbers(type);
        Vector<String> sjbhs = DataManageService.getUnitPlaces(type);
        jcbSJBH.removeAllItems();
        if (sjbhs.size() > 0) {
            jcbSJBH.addItem("全部");
            for (String sjbh : sjbhs) {
                jcbSJBH.addItem(sjbh);
            }
        }
        jcbXW.setSelectedIndex(0);
    }

}

package view.sensorMatch;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

import model.SensorModel;
import mytools.MyButton3;
import mytools.MyLabel;
import mytools.MyToolPanel;
import mytools.MyUtil;
import service.SensorService;
import util.MyExcelUtil;
import util.UiUtil;
import domain.SensorBean;

public class SensorMatch extends JPanel {

    private static SensorMatch SM = null;
    private JButton add, modify, delete, clear, in4Excel;
    protected JTable table;
    protected SensorModel matchModel;
    protected AbstractView sensorView;
    JPanel leftPane;

    private SensorMatch() {
        this.setLayout(new BorderLayout());
        this.init();
    }

    public static SensorMatch getInstance() {
        if (SM == null) {
            synchronized (SensorMatch.class) {
                if (SM == null)
                    SM = new SensorMatch();
            }
        }
        return SM;
    }

    private void init() {

        initToolBar();
        initCenter();
    }

    private void initToolBar() {

        // 工具栏面板
        JPanel toolPane = new MyToolPanel(new BorderLayout());
        this.add(toolPane, BorderLayout.NORTH, 0);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorderPainted(false);
        menuBar.setOpaque(false);
        toolPane.add(menuBar, BorderLayout.WEST);

        // 工具栏右
        JPanel toolBarR = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 2));
        toolBarR.setOpaque(false);
        toolPane.add(toolBarR, BorderLayout.EAST);

        Dimension buttonSize = new Dimension(34, 32);

        add = new MyButton3(new ImageIcon("images/add.png"));
        add.setToolTipText("添加");
        add.setPreferredSize(buttonSize);
        toolBarR.add(add);
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                new SensorDialogImpl();
            }
        });

        modify = new MyButton3(new ImageIcon("images/edit.png"));
        modify.setToolTipText("修改");
        modify.setPreferredSize(buttonSize);
        toolBarR.add(modify);
        modify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                SensorBean sensor = sensorView.getUnit();
                if (sensor != null) {
//                    SensorService.showUpdateDialog(sensor);
                } else {
                    JOptionPane.showMessageDialog(null, "请选中要修改的数据", "提示",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        delete = new MyButton3(new ImageIcon("images/delete.png"));
        delete.setToolTipText("删除");
        delete.setPreferredSize(buttonSize);
        toolBarR.add(delete);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // 得到鼠标选中行
                int selRow = table.getSelectedRow();
                if (selRow < 0) {
                    JOptionPane.showMessageDialog(null, "请选中要删除的数据", "提示",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // 提示"是"、"否"、"取消"删除操作
                int flag = JOptionPane.showConfirmDialog(null, "确定要删除选中数据？",
                        "删除", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (flag != JOptionPane.OK_OPTION) {
                    return;
                }

//                try {
//                    SensorBean sensor = sensorView.getUnit();
////                    SensorService.deleteSensor(sensor);
//                    JOptionPane.showMessageDialog(null, "传感器信息已成功删除", "提示",
//                            JOptionPane.INFORMATION_MESSAGE);
//                    table.clearSelection();
//                } catch (SQLException e) {
//                    JOptionPane.showMessageDialog(null,
//                            "传感器信息删除失败:\n" + e.getMessage(), "提示",
//                            JOptionPane.ERROR_MESSAGE);
//                }

            }
        });

        clear = new MyButton3(new ImageIcon("images/clear_24.png"));
        clear.setToolTipText("清空");
        clear.setPreferredSize(buttonSize);
        toolBarR.add(clear);
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // 判断表中是否有数据，或已清空
                int rowCount = table.getRowCount();
                if (rowCount == 0) {
                    JOptionPane.showMessageDialog(null, "当前表中无数据，或数据已清空", "提示",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int flag = JOptionPane.showConfirmDialog(null, "确定要清空表中所有数据？",
                        "清空", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (flag != JOptionPane.OK_OPTION) {
                    return;
                }

//                try {
////                    SensorService.clearSensors();
//                    JOptionPane.showMessageDialog(null, "传感器信息已清空", "提示",
//                            JOptionPane.INFORMATION_MESSAGE);
//                    // 清空表格数据模型中的值
//                    table.clearSelection();
//                }
//                catch (SQLException e) {
//                    JOptionPane.showMessageDialog(null,
//                            "传感器信息清空失败:\n" + e.getMessage(), "提示",
//                            JOptionPane.ERROR_MESSAGE);
//                }
            }
        });

        JButton out2Excel = new MyButton3(new ImageIcon(
                "images/database_download_24.png"));
        out2Excel.setToolTipText("将传感器资料保存到Excel表格");
        out2Excel.setPreferredSize(buttonSize);
        toolBarR.add(out2Excel);
        out2Excel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (table.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "当前表中无数据，或数据已清空", "提示",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    MyExcelUtil.Export2Excel(table, "传感器资料");
                } catch (IOException e) {
                    // e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "导出失败：" + e.getMessage(), "失败",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        in4Excel = new MyButton3(new ImageIcon("images/database_upload_24.png"));
        in4Excel.setToolTipText("将Excel表格中传感器资料导入到采集系统中");
        in4Excel.setPreferredSize(buttonSize);
        toolBarR.add(in4Excel);
        in4Excel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (table.getRowCount() != 0) {
                    JOptionPane.showMessageDialog(null,
                            "为确保成功导入传感器资料，请先清空传感器列表", "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                try {
                    ArrayList<String[]> paras = MyExcelUtil.Import4Excel();
                    if (paras != null) {
//                        SensorService.import4Excel(paras);
                        JOptionPane.showMessageDialog(null, "导入成功", "成功",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException e) {
                    // e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "导入失败:" + e.getMessage(), "失败",
                            JOptionPane.ERROR_MESSAGE);
                }
//                catch (SQLException e) {
//                    // e.printStackTrace();
//                    JOptionPane.showMessageDialog(null,
//                            "导入失败:请严格检查传感器资料是否填写规范", "失败",
//                            JOptionPane.ERROR_MESSAGE);
//                }
            }
        });

        JButton print = new MyButton3(new ImageIcon("images/printer_24.png"));
        print.setToolTipText("打印传感器资料");
        print.setPreferredSize(buttonSize);
        toolBarR.add(print);
        print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    table.print();
                } catch (PrinterException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initCenter() {
        // 初始化左边"配置预览"面板
        leftPane = new JPanel(new BorderLayout());
        JLabel label = new MyLabel("传感器信息预览", JLabel.CENTER);
        label.setFont(MyUtil.FONT_16);
        label.setPreferredSize(new Dimension(label.getWidth(), 26));
        leftPane.add(label, BorderLayout.NORTH);
        sensorView = new SensorView();
        leftPane.add(sensorView, BorderLayout.CENTER);

        // 右边表格
        matchModel = SensorModel.getInstance();
        // 表格模型的数据监听
        matchModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
            }
        });
        table = UiUtil.getTable(matchModel);
        table.setAutoCreateRowSorter(true);// 自动排序
        // 设置列宽不可自动调整，显示水平滚动条
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 单行选中
        TableColumn column = table.getColumn("设计编号");
        column.setMaxWidth(140);
        column.setMinWidth(140);
        column = table.getColumn("备注");
        column.setMaxWidth(120);
        column.setMinWidth(120);
        // 表格选中行监听事件
        table.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    // 这里鼠标点击和释放都会触发事件
                    @Override
                    public void valueChanged(ListSelectionEvent arg0) {
                        // getValueIsAdjusting()==ture为鼠标点击，false为鼠标释放
                        // 键盘选择只触发一次
                        if (!arg0.getValueIsAdjusting()) {
                            int selRow = table.getSelectedRow();
                            if (selRow < 0) {
                                sensorView.clearInfo();
                            } else {
                                int row = table.convertRowIndexToModel(selRow);
                                String sjbh = ((SensorModel) table.getModel()).getSjbh(row);
//                                SensorBean sensor = SensorService.getUnit(sjbh);
//                                if (sensor != null) {
//                                    // TODO 刷新显示
//                                    sensorView.setSensor(sensor);
//                                }
                            }
                        }
                    }
                });
        // 将JTable添加到滚动面板中
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(MyUtil.Component_Border);

        JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
                leftPane, scrollPane);
        jsp.setDividerLocation(285);
        jsp.setOneTouchExpandable(true);
        // jsp.setOpaque(false);
        jsp.setEnabled(false);
        jsp.setBorder(null);
        this.add(jsp, BorderLayout.CENTER);
    }

    /**
     * 当打开连接时设置传感器配置不可修改；关闭连接时恢复传感器配置界面
     */
    public void setEditable(boolean b) {
        add.setEnabled(b);
        modify.setEnabled(b);
        delete.setEnabled(b);
        clear.setEnabled(b);
        in4Excel.setEnabled(b);
    }
}

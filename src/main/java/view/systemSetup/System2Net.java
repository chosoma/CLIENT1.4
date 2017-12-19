package view.systemSetup;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import model.SysNetModel;
import mytools.MyButton3;
import mytools.MyTCR;
import mytools.MyTCR_CheckBox;
import mytools.MyUtil;
import service.SysNetService;
import util.MyExcelUtil;
import domain.NetBean;

public class System2Net extends JPanel {

    private JTable table;
    private SysNetModel tableModel;
    private JButton add, edit, delete, clear, in4Excel;

    public System2Net() {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(500, 360));
        this.initialize();
    }

    private void initialize() {

        tableModel = SysNetModel.getInstance();
        table = new JTable(tableModel);
        // 表格设置
        this.setTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 0, 500, 321);
        this.add(scrollPane);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        toolbar.setBounds(0, 320, 500, 40);
        toolbar.setBorder(MyUtil.Component_Border);
        this.add(toolbar);

        Dimension buttonSize = new Dimension(60, 30);

        add = new MyButton3("添加", new ImageIcon("images/add.png"));
        add.setToolTipText("添加单元采集模块");
        add.setPreferredSize(buttonSize);
        toolbar.add(add);
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                new SysNetDialogImpl();
            }
        });

        edit = new MyButton3("修改", new ImageIcon("images/edit.png"));
        edit.setToolTipText("修改单元采集模块");
        edit.setPreferredSize(buttonSize);
        toolbar.add(edit);
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int selRow = table.getSelectedRow();
                if (selRow >= 0) {
//                    byte type = (byte) table.getValueAt(selRow, 0);
                    int netid = (Integer) table.getValueAt(selRow, 0);
                    NetBean netbean = SysNetService.getNetBean((byte) 4, (byte) netid);
                    if (netbean != null) {
                        new SysNetDialogImpl(netbean);
                    } else {
                        new SysNetDialogImpl();
                    }
                }

            }
        });

        delete = new MyButton3("删除", new ImageIcon("images/delete.png"));
        delete.setToolTipText("删除单元采集模块");
        delete.setPreferredSize(buttonSize);
        toolbar.add(delete);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int selRow = table.getSelectedRow();
                if (selRow < 0) {
                    JOptionPane.showMessageDialog(null, "请选中要删除的数据", "提示",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int flag = JOptionPane.showConfirmDialog(null, "确定要删除选中数据？",
                        "删除", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (flag != JOptionPane.OK_OPTION) {
                    return;
                }

                try {
                    byte type = 4;
//                    byte type = (Byte) table.getValueAt(selRow, 0);
                    byte number = (byte) (int) table.getValueAt(selRow, 0);
                    SysNetService.deleteNetBean(type, number);
                    JOptionPane.showMessageDialog(null, "信息已成功删除", "提示", JOptionPane.INFORMATION_MESSAGE);
//                    int selectedRow = table.convertRowIndexToModel(selRow);
//                    tableModel.removeRow(selectedRow);
                    table.clearSelection();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null,
                            "传感器信息删除失败:\n" + e.getMessage(), "提示",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        clear = new MyButton3("清空", new ImageIcon("images/clear_24.png"));
        clear.setToolTipText("清空单元采集模块");
        clear.setPreferredSize(buttonSize);
        toolbar.add(clear);
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
                try {
                    SysNetService.clearNetBean();
                    JOptionPane.showMessageDialog(null, "表格数据已清空", "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                    // 清空表格数据模型中的值
                     tableModel.clearData();
                    // table.clearSelection();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null,
                            "表格数据清空失败:\n" + e.getMessage(), "提示",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        JButton out2Excel = new MyButton3("导出", new ImageIcon(
                "images/database_download_24.png"));
        out2Excel.setToolTipText("将单元采集模块资料保存到Excel表格");
        out2Excel.setPreferredSize(buttonSize);
        toolbar.add(out2Excel);
        out2Excel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (table.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "当前表中无数据，或数据已清空", "提示",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    MyExcelUtil.Export2Excel(table, "连接信息");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,
                            "导出失败：" + e.getMessage(), "失败",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        in4Excel = new MyButton3("导入", new ImageIcon(
                "images/database_upload_24.png"));
        in4Excel.setToolTipText("将Excel表格中单元采集模块资料导入到采集系统中");
        in4Excel.setPreferredSize(buttonSize);
        toolbar.add(in4Excel);
        in4Excel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (table.getRowCount() != 0) {
                    JOptionPane.showMessageDialog(null, "为确保成功导入资料，请先清空表格数据",
                            "提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                try {
                    ArrayList<String[]> paras = MyExcelUtil.Import4Excel();
                    if (paras != null) {
                        SysNetService.import4Excel(paras);
                        JOptionPane.showMessageDialog(null, "导入成功", "成功",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,
                            "导入失败:" + e.getMessage(), "失败",
                            JOptionPane.ERROR_MESSAGE);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "导入失败:请严格检查资料是否填写规范",
                            "失败", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * 表格设置
     */
    private void setTable() {
        MyTCR tcr = new MyTCR();
        table.setDefaultRenderer(String.class, tcr);
        table.setDefaultRenderer(Number.class, tcr);

        // 设置表头居中
        JTableHeader tableHeader = table.getTableHeader();
        // 表头不可拖动
        tableHeader.setReorderingAllowed(false);
        // 列宽不可修改
        tableHeader.setResizingAllowed(false);

        DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader
                .getDefaultRenderer();
        hr.setHorizontalAlignment(SwingConstants.CENTER);
        Dimension dimension = hr.getSize();
        dimension.height = MyUtil.HeadHeight;
        hr.setPreferredSize(dimension);// 设置表头高度
        tableHeader.setDefaultRenderer(hr);

        table.setRowHeight(MyUtil.RowHeight);// 设置行高
        // 设置单行选中
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // TableColumn column = table.getColumn("模块号");
        // column.setMaxWidth(60);
        // column.setMinWidth(60);
        // column = table.getColumn("SIM卡号");
        // column.setMaxWidth(80);
        // column.setMinWidth(80);

    }

    /**
     * 当打开连接时将"修改"设置为不可用状态；关闭连接时恢复"修改"的可使用状态
     */
    public void setEditable(boolean b) {
        add.setEnabled(b);
        edit.setEnabled(b);
        delete.setEnabled(b);
        clear.setEnabled(b);
        in4Excel.setEnabled(b);
    }

}

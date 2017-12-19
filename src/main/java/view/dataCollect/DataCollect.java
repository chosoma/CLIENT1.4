package view.dataCollect;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.swing.*;

import view.Shell;

public class DataCollect {
    private static DataCollect DC = new DataCollect();
    private JComponent contentPane = null;
    CollectShow collectShow;

    private DataCollect() {

    }

    public static DataCollect getInstance() {
        return DC;
    }

    public JComponent CreatContentPanel() {
        if (contentPane == null) {
            synchronized (DataCollect.class) {
                if (contentPane == null) {
                    this.init();
                }
            }
        }
        return contentPane;
    }

    ScheduledThreadPoolExecutor scheduler = null;
    RunnableScheduledFuture<?> rsfRoll;

    private void init() {
        scheduler = new ScheduledThreadPoolExecutor(1);
        JComponent operatePane = CollectOperate.getInstance().getOperatePane();
        collectShow = CollectShow.getInstance();

        contentPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
                operatePane, collectShow);
        ((JSplitPane) contentPane).setDividerLocation(268);
        // ((JSplitPane) contentPane).setDividerSize(0);
        ((JSplitPane) contentPane).setOneTouchExpandable(true);
        // contentPane.setBackground(new Color(215, 215, 215));
        contentPane.setEnabled(false);
        contentPane.setBorder(null);
        contentPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                if (collectShow.isNeedRoll()) {
                    // TODO Auto-generated method stub
                    Shell.getInstance().showButton(true);
                }
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                // TODO Auto-generated method stub
                Shell.getInstance().showButton(false);
            }
        });

//		 contentPane = new JPanel(new BorderLayout());
//		 contentPane.add(operatePane, BorderLayout.WEST);
//		 contentPane.add(collectShow, BorderLayout.CENTER);
    }

}

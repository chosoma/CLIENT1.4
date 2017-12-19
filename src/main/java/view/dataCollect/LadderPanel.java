package view.dataCollect;

import domain.DataBean;
import domain.UnitBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class LadderPanel extends JPanel {
    private java.util.List<java.util.List<Float>> infos;
    private java.util.List<java.util.Date> dates;
    private java.util.List<DataBean> datas;
    private java.util.List<DataBean> historydatas;
    private boolean flag;
    private UnitBean unit;
    private double oneHourSpace;

    private int MaxPres;

    private int MinPres;

    private int MaxDen;

    private int MinDen;

    private int MaxTemp;

    private int MinTemp;

    private int MaxVari;

    private int MinVari;

    private int MaxBat;

    private int MinBat;

    public LadderPanel() {
//        this.flag = true;
        this.infos = new ArrayList<>();
        this.dates = new ArrayList<>();

        this.hour = 24;


        getInfos();
    }


    public LadderPanel(UnitBean unit) {
        this.unit = unit;
    }

    public void addData(DataBean data) {
        this.datas.add(data);
        Graphics g = LadderPanel.this.getGraphics();
        if (g != null) {
            g.setColor(new Color(255, 255, 255));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        repaint();
    }

    public void clear() {
        if (historydatas != null) {
            historydatas.clear();
        }
        repaint();
    }

    public LadderPanel(UnitBean unit, java.util.List<DataBean> datas) {
        this(unit);
        this.datas = datas;
        switch (unit.getType()) {
            case 1:
                MaxTemp = 150;//150
                MinTemp = -770;//-80  +3
                MaxPres = 400;//200 +1
                MinPres = -400;//0 +2
                MaxDen = 300;//100 +2
                MinDen = -100;//0 +1
                MaxBat = 400;//100 +3
                MinBat = 0;//0
                break;
            case 2:
                MaxVari = 125;
                MinVari = -375;
                MaxBat = 200;
                MinBat = 0;
                break;
            case 3:
                MaxTemp = 150;
                MinTemp = -310;
                MaxBat = 200;
                MinBat = 0;
                break;
        }


//        addMouseWheelListener(new MouseWheelListener() {
//            @Override
//            public void mouseWheelMoved(MouseWheelEvent e) {
//                if (e.getWheelRotation() > 0) {
//                    if (++hour > 24) {
//                        hour = 24;
//                        return;
//                    }
//                } else {
//                    if (--hour < 6) {
//                        hour = 6;
//                        return;
//                    }
//                }
//                Graphics g = LadderPanel.this.getGraphics();
//                g.setColor(new Color(255, 255, 255));
//                g.fillRect(0, 0, getWidth(), getHeight());
//                LadderPanel.this.repaint();
//
//                int mousex = e.getX();
//                int mousey = e.getY();
//
//                int width = e.getComponent().getWidth();
//                int height = e.getComponent().getHeight();
//
//
//            }
//        });
    }

    private int hour;
    int x = 30;

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, getWidth(), getHeight());
//        System.out.println(flag);
//        if (flag) {
//            return;
//        }
        getInfos();
        if (dates.size() == 0) {
            return;
        }
        // 原点x坐标
//        int x = (int) (getWidth() * 0.05);
        // 原点y坐标
        int y = getHeight() - 20;

//        g.setColor(new Color(0x000000));
        // 画x轴
//        g.drawLine(x, y, getWidth() - 5, y);
        // 画y轴
//        g.drawLine(x, 10, x, y);


        g.setColor(new Color(174, 174, 174));


        java.util.List<Integer> hours = new ArrayList<>();

//        if (flag) {

//            hour = 24;
//            oneHourSpace = (getWidth() - x * 2.0) / hour;
//            Calendar startCalendar = Calendar.getInstance();
//            startCalendar.add(Calendar.HOUR_OF_DAY, 1 - hour);
//            for (int i = startCalendar.get(Calendar.HOUR_OF_DAY), h = 0; h <= hour; i++, h++) {
//                hours.add(i % 24);
//            }
//        } else {

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(dates.get(0));
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(dates.get(dates.size() - 1));
        endCalendar.add(Calendar.HOUR_OF_DAY, 1);
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);

        hour = (int) ((endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis()) / oneHour);
        oneHourSpace = (getWidth() - x * 2.0) / hour;

        for (int i = startCalendar.get(Calendar.HOUR_OF_DAY), h = 0; h <= hour; i++, h++) {
            hours.add(i % 24);
        }

//        }
        for (int j = 0; j < hours.size(); j++) { //Y轴
//            if (!flag) {
            if (j == 0) {
                String datestr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dates.get(0));
                g.drawString(datestr, x + 10, 20);
            }
            if (j == hours.size() - 1) {
                String datestr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dates.get(dates.size() - 1));
                g.drawString(datestr, getWidth() - 150, 20);
            }
//            }
            if (j == 0 || j == hours.size() - 1) {

            } else if (getWidth() > 1000) {
                if (hours.size() < 24) {
                } else if (hours.size() < 48) {
                    if (j % 2 != 0) {
                        continue;
                    }
                } else if (hours.size() < 96) {
                    if (j % 4 != 0) {
                        continue;
                    }
                } else {
                    continue;
                }
            } else if (getWidth() > 500) {
                if (hours.size() < 12) {
                } else if (hours.size() < 24) {
                    if (j % 2 != 0) {
                        continue;
                    }
                } else {
                    continue;
                }
            } else {
                if (hours.size() < 6) {
                } else if (hours.size() < 12) {
                    if (j % 2 != 0) {
                        continue;
                    }
                } else {
                    continue;
                }
            }
//            if (j == hours.size() - 1) {
//            } else if (getWidth() < 500) {
//                if (hour > 20 && j % 4 != 0) {
//                    continue;
//                } else if (hour > 10 && j % 2 != 0) {
//                    continue;
//                }
//            } else if (getWidth() < 1000) {
//                if (hour > 18 && j % 2 != 0) {
//                    continue;
//                }
//            }
            int y_xmove = 0;
            if (hours.get(j) < 10) {
                y_xmove = 22;
            } else {
                y_xmove = 16;
            }
            g.drawString(hours.get(j) + ":00", (int) (j * oneHourSpace + y_xmove), y + 15);
            g.drawLine((int) (j * oneHourSpace + x), 10, (int) (j * oneHourSpace + x), y - 1);

        }
        paintHour(g);
        paintOneType(g);

    }

    private void paintHour(Graphics g) {


    }

    private void paintOneType(Graphics g) {
        for (int i = 0; i < infos.size(); i++) {
            java.util.List<Point> points = new ArrayList<>();
            Float max = null;
            Float min = null;
            if (infos.get(i).size() > 0) {
                max = Collections.max(infos.get(i));
                min = Collections.min(infos.get(i));
            }
            boolean flagmax = true;
            boolean flagmin = true;
            g.setColor(getColor(i));
            for (int j = 0; j < infos.get(i).size(); j++) {
                Point p = new Point();
                p.x = getPointX(dates.get(j));
                p.y = getPointY(i, infos.get(i).get(j));
                points.add(p);
                if (max != null && infos.get(i).get(j).equals(max) && flagmax) {
                    g.drawString(String.valueOf(infos.get(i).get(j)), p.x, p.y);
                    flagmax = false;
                }
                if (min != null && infos.get(i).get(j).equals(min) && flagmin) {
                    g.drawString(String.valueOf(infos.get(i).get(j)), p.x, p.y);
                    flagmin = false;
                }
                if (j == 0 || j == infos.get(i).size() - 1) {
                    g.drawString(String.valueOf(infos.get(i).get(j)), p.x, p.y);
                }
            }
            paintOneLadder(g, points, i);
        }
    }

    private void paintOneLadder(Graphics g, List<Point> points, int index) {
        String str = "";
        switch (unit.getType()) {
            case 1:
                switch (index) {
                    case 0:
                        str = "温度";
                        break;
                    case 1:
                        str = "压力";
                        break;
                    case 2:
                        str = "密度";
                        break;
                    case 3:
                        str = "电压";
                        break;
                }
                break;
            case 2:
                switch (index) {
                    case 0:
                        str = "位移";
                        break;
                    case 1:
                        str = "电压";
                        break;
                }
                break;
            case 3:
                switch (index) {
                    case 0:
                        str = "温度";
                        break;
                    case 1:
                        str = "电压";
                        break;
                }
        }

        g.drawLine(x, getPointY(index, 0.0f), getWidth() - x, getPointY(index, 0.0f));
        g.drawString("0", 23, getPointY(index, 0.0f));
        g.drawString(str, 5, getPointY(index, 0.0f) - 11);

        for (int i = 0; i < points.size(); i++) {
            //        g.drawLine(x, y, getWidth() - 5, y);
            if (i == points.size() - 1) {
                g.drawLine(points.get(i).x, points.get(i).y, points.get(i).x, points.get(i).y);
            } else {
                g.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y);
            }
//            if (i == points.size() - 2) {
//                g.drawString(str, points.get(i + 1).x + 5, points.get(i + 1).y + 5);
//            }
        }
    }


    private void getInfos() {
        infos.clear();
        dates.clear();

        Calendar calendar = Calendar.getInstance();
        long thenlong = calendar.getTimeInMillis();

        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        long then = calendar.getTimeInMillis();


        if (unit == null) {
            return;
        }
        switch (unit.getType()) {
            case 1:
                java.util.List<Float> pres = new ArrayList<>();
                java.util.List<Float> temps = new ArrayList<>();
                java.util.List<Float> dens = new ArrayList<>();
//                if (flag) {
//                    for (DataBean databean : datas) {
//                        if ((then - databean.getDate().getTime()) / oneHour < hour - 1) {
//                            if (thenlong < databean.getDate().getTime()) {
//                                continue;
//                            }
//                            temps.add(databean.getTemp());
//                            pres.add(databean.getPres());
//                            dens.add(databean.getDen());
//                            dates.add(databean.getDate());
//                        }
//                    }
//                } else {
                for (DataBean databean : historydatas) {
                    temps.add(databean.getTemp());
                    pres.add(databean.getPres());
                    dens.add(databean.getDen());
                    dates.add(databean.getDate());
                }
//                }
                infos.add(temps);
                infos.add(pres);
                infos.add(dens);
                break;
            case 2:
                java.util.List<Float> varis = new ArrayList<>();
//                if (flag) {
//                    for (DataBean databean : datas) {
//                        if ((then - databean.getDate().getTime()) / oneHour < hour - 1) {
//                            if (thenlong < databean.getDate().getTime()) {
//                                continue;
//                            }
//                            varis.add((float) ((int) ((databean.getVari() - unit.getInitvari()) * 10.0) / 10.0));
//                            dates.add(databean.getDate());
//                        }
//                    }
//                } else {
                for (DataBean databean : historydatas) {
                    varis.add((float) ((int) ((databean.getVari() - unit.getInitvari()) * 10.0) / 10.0));
                    dates.add(databean.getDate());
                }
//                }
                infos.add(varis);
                break;
            case 3:
                java.util.List<Float> temps2 = new ArrayList<>();
//                if (flag) {
//                    for (DataBean databean : datas) {
//
//                        if ((then - databean.getDate().getTime()) / oneHour < hour - 1) {
//                            if (thenlong < databean.getDate().getTime()) {
//                                continue;
//                            }
//                            temps2.add(databean.getTemp());
//                            dates.add(databean.getDate());
//                        }
//
//                    }
//                } else {
                for (DataBean databean : historydatas) {
                    temps2.add(databean.getTemp());
                    dates.add(databean.getDate());
                }
//                }
                infos.add(temps2);
                break;
        }
//        java.util.List<Float> bat = new ArrayList<>();
//        for (DataBean databean : datas) {
//            if ((then - databean.getDate().getTime()) / oneHour < hour - 1) {
//                if (thenlong < databean.getDate().getTime()) {
//                    continue;
//                }
//                bat.add(databean.getBatlv());
//                dates.add(databean.getDate());
//            }
//        }
//        infos.add(bat);
    }

    private static long oneSecond = 1000;

    private static long oneMinute = oneSecond * 60;

    private static long oneHour = oneMinute * 60;

    private static long oneDay = oneHour * 24;

    private int getPointX(Date date) {
        Calendar datacalendar = Calendar.getInstance();
        datacalendar.setTime(date);
        int minute = datacalendar.get(Calendar.MINUTE);
        datacalendar.set(Calendar.MINUTE, 0);
        datacalendar.set(Calendar.SECOND, 0);
        long datahour = datacalendar.getTimeInMillis();
        long thenhour;
//        if (flag) {
//            thenhour = System.currentTimeMillis();
//        } else {
        thenhour = dates.get(dates.size() - 1).getTime();
//        }
        double start = hour - 1 - Math.floor((thenhour - datahour) / (oneHour * 1.0));
        double oneMinuteSpace = oneHourSpace / 60;
        return (int) (oneMinuteSpace * minute + start * oneHourSpace + x);
    }

    private int getPointY(int index, Float value) {

        double y = Integer.MAX_VALUE;
        if (value == null) {
            return (int) y;
        }
        double yspace = 0.0;
        switch (unit.getType()) {
            case 1://SF6
                switch (index) {
                    case 0: //pres
                        yspace = (getHeight() - 40.0) / (MaxTemp - MinTemp);
                        y = MaxTemp - value;
                        break;
                    case 1: //temp
                        yspace = (getHeight() - 40.0) / (MaxPres - MinPres);
                        y = MaxPres - value;
                        break;
                    case 2: //den
                        yspace = (getHeight() - 40.0) / (MaxDen - MinDen);
                        y = MaxDen - value;
                        break;
                    case 3: //batlv
                        yspace = (getHeight() - 40.0) / (MaxBat - MinBat);
                        y = MaxBat - value;
                        break;
                }
                break;
            case 2://伸缩节
                switch (index) {
                    case 0: //vari
                        yspace = (getHeight() - 40.0) / (MaxVari - MinVari);
                        y = MaxVari - value;
                        break;
                    case 1:
                        yspace = (getHeight() - 40.0) / (MaxBat - MinBat);
                        y = MaxBat - value;
                        break;
                }
                break;
            case 3://温度
                switch (index) {
                    case 0: //temp
                        yspace = (getHeight() - 40.0) / (MaxTemp - MinTemp);
                        y = MaxTemp - value;
                        break;
                    case 1:
                        yspace = (getHeight() - 40.0) / (MaxBat - MinBat);
                        y = MaxBat - value;
                        break;
                }
                break;
        }
        y *= yspace;
        y += 20;
        return (int) y;
    }

    private Color getColor(int index) {
        int r = 0;
        int g = 0;
        int b = 0;
        switch (index) {
            case 0:
                switch (unit.getType()) {
                    case 1:
                        r = 55;
                        g = 129;
                        b = 167;
                        break;
                    case 2:
                        r = 73;
                        g = 149;
                        b = 57;
                        break;
                    case 3:
                        r = 161;
                        g = 125;
                        b = 34;
                        break;
                }
                break;
            case 1:
                switch (unit.getType()) {
                    case 1:
                        r = 161;
                        g = 125;
                        b = 34;
                        break;
                    default:
                        r = 120;
                        g = 120;
                        b = 120;
                }
                break;
            case 2:
                r = 56;
                g = 106;
                b = 59;
                break;
            case 3:
                r = 120;
                g = 120;
                b = 120;
                break;
        }

        return new Color(r, g, b);
    }

    public void setDatas(List<DataBean> datas) {
        this.historydatas = datas;
    }

    public void setUnit(UnitBean unit) {
        this.unit = unit;
        switch (unit.getType()) {
            case 1:
                MaxTemp = 150;//150
                MinTemp = -770;//-80  +3
                MaxPres = 400;//200 +1
                MinPres = -400;//0 +2
                MaxDen = 300;//100 +2
                MinDen = -100;//0 +1
                MaxBat = 400;//100 +3
                MinBat = 0;//0
                break;
            case 2:
                MaxVari = 125;
                MinVari = -375;
                MaxBat = 200;
                MinBat = 0;
                break;
            case 3:
                MaxTemp = 150;
                MinTemp = -310;
                MaxBat = 200;
                MinBat = 0;
                break;
        }
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}

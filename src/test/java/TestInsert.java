import domain.DataBean;
import service.DataService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestInsert {
    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2018);
//        c.set(Calendar.MONTH, Calendar.DECEMBER);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DAY_OF_MONTH, 7);
        c.set(Calendar.HOUR_OF_DAY,12);
        c.set(Calendar.MINUTE,24);
        c.set(Calendar.SECOND,14);
        Date date = c.getTime();
//        Date date = new Date();

        List<DataBean> datas = new ArrayList<>();

        for (byte i = 1; i <= 27; i++) {
            DataBean dataBean = new DataBean();
            dataBean.setUnitType((byte) 1);
            dataBean.setUnitNumber(i);
            float f = (float) Math.random();
            dataBean.setPres(f);
            dataBean.setTemp((float) (Math.random() * 10 + 10));
            dataBean.setDen(f);
            dataBean.setBatlv((float) 3.8);
            dataBean.setDate(date);
            datas.add(dataBean);
        }
        for (byte i = 1; i < 6; i++) {
            DataBean dataBean = new DataBean();
            dataBean.setUnitType((byte) 3);
            dataBean.setUnitNumber(i);
            dataBean.setTemp((float) (Math.random() * 10 + 10));
            dataBean.setBatlv((float) 3.8);
            dataBean.setDate(date);
            datas.add(dataBean);
        }
        for (byte i = 1; i < 3; i++) {
            DataBean dataBean = new DataBean();
            dataBean.setUnitType((byte) 2);
            dataBean.setUnitNumber(i);
            dataBean.setVari((float) (Math.random() * 20 + 20));
            dataBean.setBatlv((float) 3.8);
            dataBean.setDate(date);
            datas.add(dataBean);
        }
        DataBean dataBean2 = new DataBean();
        dataBean2.setUnitType((byte) 2);
        dataBean2.setUnitNumber((byte) 34);
        dataBean2.setVari((float) (Math.random() * 20 + 20));
        dataBean2.setBatlv((float) 3.8);
        dataBean2.setDate(date);
        datas.add(dataBean2);
        DataBean dataBean3 = new DataBean();
        dataBean3.setUnitType((byte) 3);
        dataBean3.setUnitNumber((byte) 51);
        dataBean3.setTemp((float) (Math.random() * 10 + 10));
        dataBean3.setBatlv((float) 3.8);
        dataBean3.setDate(date);
        datas.add(dataBean3);
        try {
            DataService.saveCollData(datas);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

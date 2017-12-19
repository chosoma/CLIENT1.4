import domain.DataBean;
import org.jfree.data.general.Dataset;
import util.MyDbUtil;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

public class TestData {
    public static void main(String[] args) {
        String sql = "select * from data ";
        try {
            List<DataBean> datas = MyDbUtil.queryBeanListData(sql, DataBean.class);
//            System.out.println(datas);
            int i = 0;
            for (DataBean data : datas) {
                Calendar c = Calendar.getInstance();
                Calendar c1 = Calendar.getInstance();
                c1.setTime(data.getDate());
                c1.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));
                c1.set(Calendar.MONTH, c.get(Calendar.MONTH));
                if (i % 2 == 0) {
                    c1.add(Calendar.HOUR_OF_DAY, -12);
                }
                i++;
                String sql1 = "update data set date = ? where unittype = ? and unitnumber = ? ";
                MyDbUtil.update(sql1, c1.getTime(), data.getUnitType(), data.getUnitNumber());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

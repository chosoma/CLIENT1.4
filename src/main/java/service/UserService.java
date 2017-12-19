package service;

import java.sql.SQLException;
import java.util.List;

import util.MyDbUtil;
import domain.DataBaseAttr;
import domain.UserBean;

public class UserService {

    private static String tableName = DataBaseAttr.UserTable;
    public static String ADMIN = "管理员";
    public static String USER = "普通用户";
    public static int AP = 1, UP = 2;
    static UserBean UserLogin;

    private static UserService MODEL;

    private UserService() {

    }

    public static UserService getInstance() {
        if (MODEL == null) {
            synchronized (UserService.class) {
                if (MODEL == null) {
                    MODEL = new UserService();
                }
            }
        }
        return MODEL;
    }

    /**
     * 登录校验
     *
     * @param paras
     * @return
     * @throws SQLException
     */
    public static UserBean checkUser(String[] paras) throws SQLException {
        String sql = "select * from " + tableName
                + " where username = ? and password = ? ";
        UserLogin = MyDbUtil.queryBeanData(sql, UserBean.class, paras[0],
                paras[1]);
        return UserLogin;
    }

    /**
     * 检验密码
     *
     * @param p
     * @return
     */
    public static boolean checkPassWord(String p) {
        return UserLogin.getPassword().equals(p);
    }

    /**
     * 修改密码
     *
     * @throws SQLException
     */
    public static void changePassword(String password) throws SQLException {
        String sql = "update " + tableName
                + "  set password=? where username=?";
        MyDbUtil.update(sql, password, UserLogin.getName());
        UserLogin.setPassword(password);
    }

    /**
     * 获取全部用户
     *
     * @return
     * @throws SQLException
     */
    public static List<UserBean> query() throws SQLException {
        String sql = "select username as name , authority from " + tableName
                + " where authority >= ? order by authority , username";
        List<UserBean> users = MyDbUtil.queryBeanListData(sql, UserBean.class, 1);
        return users;
    }



    /**
     * 更改用户权限
     *
     * @param user
     * @throws SQLException
     */
    public static void changeRole(UserBean user) throws SQLException {
        String sql = "update " + tableName
                + "  set authority = ? where username = ? ";
        MyDbUtil.update(sql, user.getAuthority(), user.getName());
    }

    /**
     * 删除用户
     *
     * @param user
     * @throws SQLException
     */
    public static void deleteUser(UserBean user) throws SQLException {
        String sql = "delete from " + tableName + "  where username = ? ";
        MyDbUtil.update(sql, user.getName());
    }

    /**
     * 添加用户
     *
     * @param user
     * @throws SQLException
     */
    public static void addUser(UserBean user) throws SQLException {
        String sql = "insert into " + tableName
                + " (username,password,authority) values (?,123456,?)";
        MyDbUtil.update(sql, user.getName(), user.getAuthority());
    }

    /**
     * 获取当前用户名
     *
     * @return
     */
    public String getUserName() {
        return UserLogin.getName();
    }

    /**
     * 判断当前登录用户是否为管理员
     *
     * @return
     */
    public boolean isAdmin() {
        return UserLogin.getAuthority() <= 1;
    }

}

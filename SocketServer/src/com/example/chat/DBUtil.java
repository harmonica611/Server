package com.example.chat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBUtil {

    public static Connection getConn() throws Exception {
        String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dbURL = "jdbc:sqlserver://localhost:1433;DatabaseName=QQChat";
        String userName = "wifi";
        String userPwd = "123456";
        Class.forName(driverName);
        Connection dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
        return dbConn;
    }

    /** 查看所有用户信息 */
    public static ResultSet showAllUsers(Connection conn) throws Exception {
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("select * from [User]");
        return rs;
    }

    /** 比对用户名密码是否一致 */
    public static boolean loginAccount(Connection conn, String uid, String password) throws Exception {
        ResultSet rs = showAllUsers(conn);
        while (rs.next()) {
            if (uid.equals(rs.getString(1).trim()) && password.equals(rs.getString(2).trim())) {
                conn.close();
                return true;
            }
        }
        conn.close();
        return false;
    }

    /** 判断是否已有账号UID */
    public static boolean hasAccount(Connection conn, String uid) throws Exception {
        ResultSet rs = showAllUsers(conn);
        while (rs.next()) {
            if (uid == rs.getString(1)) {
                return true;
            }
        }
        return false;
    }

    /** 创建新的账户 */
    public static boolean createAccount(Connection conn, String uid, String password) throws Exception {
        Statement stat = conn.createStatement();
        stat.execute("insert into [User] values ('" + uid + "','" + password + "')");
        stat.close();
        conn.close();
        return true;   
    }

    /** 更新用户名和性别、信息 */
    public static boolean updateAccount(Connection conn, String uid, String name, String gender) throws Exception {
        if (!hasAccount(conn, uid)) {
            conn.close();
            return false;
        }
        Statement stat = conn.createStatement();
        stat.executeUpdate("update [User] set name='" + name + "',gender='" + gender + "' where uid=" + uid);
        stat.close();
        conn.close();
        return true;
    }

    public static boolean addFriend(Connection conn, String my_uid, String his_uid, String friend_group) throws Exception {
        Statement stat = conn.createStatement();
        stat.executeQuery("insert into [Friend] values (" + my_uid + "," + his_uid + ",'" + friend_group + "')");
        stat.close();
        conn.close();
        return true;
    }

    public static boolean allMyFriend(Connection conn, String uid) throws Exception {
        Statement stat = conn.createStatement();
        stat.executeQuery("select friend_uid,friend_group from [Friend] where uid=" + uid);
        stat.close();
        conn.close();
        return true;
    }

}

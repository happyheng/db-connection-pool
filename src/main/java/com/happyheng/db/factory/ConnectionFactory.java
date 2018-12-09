package com.happyheng.db.factory;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 数据库连接工厂类
 * Created by happyheng on 2018/12/5.
 */
public class ConnectionFactory {


    private String driver;
    private String dbUrl;
    private String userName;
    private String password;

    public ConnectionFactory(String driver, String dbUrl, String userName, String password) {
        this.driver = driver;
        this.dbUrl = dbUrl;
        this.userName = userName;
        this.password = password;
    }

    public Connection makeConnection() {

        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(dbUrl, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

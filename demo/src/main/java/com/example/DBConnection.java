package com.example;

import java.io.File;
import java.sql.*;

public class DBConnection {
    static {
        try {
            // 显式注册驱动（新版本JDBC需要）
            DriverManager.registerDriver(new org.sqlite.JDBC());
        } catch (SQLException e) {
            System.err.println("SQLite驱动注册失败");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        String dbPath = System.getProperty("user.dir") + "/CSCI7785_database.db";
        System.out.println("数据库路径: " + dbPath);
        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }
}
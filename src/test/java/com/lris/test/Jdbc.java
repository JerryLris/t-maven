package com.lris.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Jdbc {

	public static void main(String[] args) {
		Connection conn = Jdbc.getConn();
        System.out.println(conn);
        closeConn(conn);
    }
    public static Connection getConn(){
        Connection c = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://118.24.105.89/?characterEncoding=utf8","lris", "lris");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;   
    }
    public static  void closeConn(Connection c){
        if(c!=null){
            try {
                c.close();
                System.out.println("关闭连接！");
            } catch (SQLException e) {
                // TODO Auto-generated catch block
            	System.out.println("关闭连接失败！");
                e.printStackTrace();
            }
        }
    }   
}

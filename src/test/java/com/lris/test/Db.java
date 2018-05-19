package com.lris.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Db {
	
	private static String className = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://118.24.105.89/?characterEncoding=utf8";
	private static String user = "lris";
	private static String password = "lris";


	/**
	 * 给调用者提供一个数据库连接通道
	 * @returns
	 */
	public static Connection getConnection(){
		try {
			// 1.注册和加载数据库驱动程序
			Class.forName(className);
			return DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 关闭数据库相关的资源
	 * @param conn
	 * @param ps
	 */
	public static void close(Connection conn , PreparedStatement ps){
		if(ps != null){
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	/**
	 * 关闭数据库相关的资源
	 * @param conn
	 * @param ps
	 * @param rs
	 */
	public static void close(Connection conn , PreparedStatement ps,ResultSet rs){
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		if(ps != null){
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}	
}

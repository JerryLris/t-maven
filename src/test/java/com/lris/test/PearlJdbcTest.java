package com.lris.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PearlJdbcTest {

	
	public static void main(String[] args) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			 conn = Db.getConnection();
			String sql = "select count(1) as num from pearl.pearl where id >?";
			statement = conn.prepareStatement(sql);
			statement.setInt(1, 1);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				System.out.println(resultSet.getInt("num"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Db.close(conn, statement, resultSet);
		}
	}
	
	public void insert() {
		
	}
	
}

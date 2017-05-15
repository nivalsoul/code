package com.nivalsoul.code;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBOperation {

	public static void main(String[] args) {
		String driverName = "com.mysql.jdbc.Driver";
    	String url = "jdbc:mysql://localhost:3306/test";
    	String username = "root";
    	String password = "root";
		try {
			Class.forName(driverName);
			Connection con = DriverManager.getConnection(url, username, password);
			Statement stmt = con.createStatement();
			
			//获取主键
			DatabaseMetaData dbmd= con.getMetaData();
			ResultSet rs0 = dbmd.getPrimaryKeys(null,null,"kk");
			while (rs0.next()) {
				String keyname = rs0.getString( "PK_NAME" );
		        String col_name = rs0.getString( "COLUMN_NAME" );
		        int m = rs0.getInt("KEY_SEQ");
				System.out.println(keyname+"=="+col_name+"=="+m);
			}
			rs0.close();
			
			String sql = "show create table kk";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
			    String createSQL = rs.getString(2);
			    System.out.println(createSQL);
			}
			
			rs = stmt.executeQuery("select * from kk where id=0");
			if(rs.next()){
			    String createSQL = rs.getString(2);
			    System.out.println(createSQL);
			}
			
			stmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

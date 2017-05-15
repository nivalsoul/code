package com.nivalsoul.code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.net.aso.a;

public class JDBCTest {

	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://172.16.50.21:3306/orchestra";
        Connection con = DriverManager.getConnection(url, "root", "654321");
        Statement st = con.createStatement();
        System.out.println("连接成功！");
        
        /*ResultSet result = st.executeQuery("SELECT SUM(CAST(records AS UNSIGNED)) as  x FROM job_history");
        while (result.next()) {
        	Object s = result.getObject(1);
        	System.out.println(s);
		}*/
        
        ResultSet rs = con.getMetaData().getColumns(null, null, "job_history", "createDate");
		if (rs.getMetaData().getColumnCount() > 18) {
          // Should only be one match
          if (rs.next()) {
            System.out.println(11);
          }
          String ss = rs.getString(18);
          System.out.println(ss.equals("YES"));
        }
	}

}

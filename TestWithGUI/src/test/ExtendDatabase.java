package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import controller.Controller;
import controller.Edit;

public class ExtendDatabase {
	
	public static void main(String args[]) {
		
		try (Connection con = DriverManager.getConnection(Controller.url); 
				Statement stmt = con.createStatement();) {
			
//			for (int i = 0; i < 1000; i++) {
//				Edit.INSTANCE.setArticle(176);
//				Edit.INSTANCE.getArticle().setId(0);
//				Edit.INSTANCE.markAsNewArticle();
//				Edit.INSTANCE.saveArticle(); 
//			}
			
//			PerformanceTest.printMemoryUsage();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}

package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import controller.Controller;

public class ModifyDatabase {
	
	public static void main(String args[]) {
		try (Connection con = DriverManager.getConnection(Controller.url); 
				Statement stmt = con.createStatement();) {
			
//			String sql = "SELECT * FROM ((SELECT * FROM (SELECT TOP 40 * FROM (SELECT TOP 40 * FROM t_fit_article WHERE "
//					+ "i_id IN (SELECT t_fit_article.i_id FROM t_fit_article WHERE (b_export_ready = false OR d_application_deadline >= #2017-01-29#))) "
//					+ "ORDER BY i_id DESC) ORDER BY i_id ASC) articles LEFT OUTER JOIN (SELECT i_fit_article, i_order_nr, i_id AS i_element_id, s_code, "
//					+ "NULL AS s_name, NULL AS s_value, NULL AS e_n_format, NULL AS s_content, NULL AS s_url, NULL AS s_info, 1 AS i_element_type FROM fit_article_code) codes "
//					+ "ON articles.i_id=codes.i_fit_article) "; 
//			
//			String sql2 = "SELECT * FROM ((SELECT * FROM (SELECT TOP 40 * FROM (SELECT TOP 40 * FROM t_fit_article WHERE i_id IN "
//					+ "(SELECT t_fit_article.i_id FROM t_fit_article WHERE (b_export_ready = false OR d_application_deadline >= #2017-01-29#))) "
//					+ "ORDER BY i_id DESC) ORDER BY i_id ASC) articlesInfos LEFT OUTER JOIN (SELECT i_fit_article, i_order_nr, i_id AS i_element_id, "
//					+ "NULL AS s_code, s_name, s_value, NULL AS e_n_format, NULL AS s_content, NULL AS s_url, NULL AS s_info, 2 AS i_element_type "
//					+ "FROM t_fit_article_info_text) infos ON articlesInfos.i_id=infos.i_fit_article)"; 
//			String sql = "SELECT * FROM (t_fit_article JOIN ) WHERE "; 
			
			String sql = "SELECT * FROM t_fit_article_text WHERE i_fit_article=201"; 
			
//			String sql = "UPDATE t_fit_article SET d_recurrent_check_from = DateAdd(\"m\", 4, d_recurrent_check_from), b_recurrent = TRUE "
//					+ "WHERE d_recurrent_check_from IS NOT NULL"; 
//			String sql = "SELECT * FROM t_fit_article_text WHERE i_fit_article >= 504"; 
//			String sql = "DELETE FROM t_fit_article WHERE i_id >= 504"; 
//			String sql = "SELECT * FROM t_fit_article WHERE i_id > 480"; 
//			stmt.executeUpdate(sql); 
			ResultSet rs = stmt.executeQuery(sql); 
//			System.out.println("" + rs.getMetaData().getColumnCount()); 
			while (rs.next()) {
				System.out.println(rs.getString("s_content"));				
			}
//			while (rs.next()) {
//				System.out.println(rs.getInt("i_fit_article") + ": " + rs.getString("s_content"));
//				System.out.println(rs.getInt("i_id"));
//			}
//			rs.next(); 
//			System.out.println(rs.getBoolean("b_child_article_present"));
//			System.out.println(rs.getInt("i_child_article"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

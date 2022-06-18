package main.java;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

public class audit_log_push {
	
	public static void push_to_audit(String date,String time,String uid,String msg) throws ParseException {
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql;
		boolean present = false;
		try {
			sql = "INSERT INTO audit_log(date,time,uid,msg,note) VALUES(?,?,?,?,?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, date);
			ps.setString(2, time);
			ps.setString(3, uid);
			ps.setString(4, msg);
			ps.setString(5, "");
			ps.executeUpdate();
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, "");
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e1) {
				System.out.println("");
			}
		}
	}
}

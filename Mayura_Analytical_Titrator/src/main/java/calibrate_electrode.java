package main.java;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.management.modelmbean.ModelMBean;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.FloatDimension;

import com.fazecast.jSerialComm.SerialPort;

public class calibrate_electrode extends JPanel implements ItemListener {

	static int wid = 0, hei = 0;
	static JFrame frame1 = new JFrame();
	static JPanel p = new JPanel();
	static String user_name = "";
	static JLabel text_mV,text_ph;
	static JButton button_calibrate;

	public calibrate_electrode() {
		setLayout(null);
		text_mV = new JLabel("<html>mV <br/>0 mV</html>");
		text_mV.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.016 * wid)));
		text_mV.setBounds((int) Math.round(0.03 * wid), (int) Math.round(0.001 * hei),
				(int) Math.round(0.4 * wid), (int) Math.round(0.10 * hei));
		add(text_mV);
		
		text_ph = new JLabel("<html>pH <br/> 0</html>");
		text_ph.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.016 * wid)));
		text_ph.setBounds((int) Math.round(0.2 * wid), (int) Math.round(0.001 * hei),
				(int) Math.round(0.4 * wid), (int) Math.round(0.10 * hei));
		add(text_ph);
		

		button_calibrate = new JButton("Calibrate");
		button_calibrate.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.012 * wid)));
		button_calibrate.setBounds((int) Math.round(0.1 * wid), (int) Math.round(0.135 * hei),
				(int) Math.round(0.1 * wid), (int) Math.round(0.05 * hei));

		add(button_calibrate);
		button_calibrate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		initialize();
	}

	@SuppressWarnings("removal")
	public static void initialize() {
		frame1.getContentPane().invalidate();
		frame1.getContentPane().validate();
		frame1.getContentPane().repaint();
	}
	
	public static void update_calibrate_mv(String msg) {
		System.out.println("Calibrate = "+msg);
		msg = msg.replaceAll("\\\n", "");
		msg = msg.replaceAll("\\\t", "");
		String[] temp = new String[2];
		if (msg.contains("T")) {
			temp = msg.split("T");
		} else if (msg.contains("N")) {
			temp = msg.split("N");
		}
		String mv_val_str = "";
		int start;
		int end;
		String[] temp_p = temp[1].split("");
		for (int i = 0; i < 4; i++) {
			mv_val_str = mv_val_str + temp_p[i];
		}
		int int_temp_mv = Integer.parseInt(mv_val_str);
		if (msg.contains("T")) {
			text_mV.setText("<html>mV <br/>"+int_temp_mv+" mV</html>");
			text_ph.setText("<html>pH <br/>"+(float)(int_temp_mv/54)+" mV</html>");
		} else if (msg.contains("N")) {
			text_mV.setText("<html>mV <br/>- "+int_temp_mv+" mV</html>");
			text_ph.setText("<html>pH <br/>-"+(float)(int_temp_mv/54)+" mV</html>");
		}
		
	}



	public static void calibrate_home() {
		String aa[] = new String[1];
		aa[0] = "aa";
		menubar.main(aa);
		ReformatBuffer.current_exp = "main";
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name, "Returning to Home from KF");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		frame1.dispose();
		frame1 = new JFrame();
		p = new JPanel();
		p.invalidate();
		p.revalidate();
		p.repaint();
	}




	public static String get_date() {
		DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		String date_time = dateFormat2.format(new Date()).toString();
		return date_time;
	}

	public static String get_time() {
		DateFormat dateFormat2 = new SimpleDateFormat("hh:mm:ss aa");
		String date_time = dateFormat2.format(new Date()).toString();
		return date_time;
	}

	public static void get_value_from_db() {
		boolean temp_result = false;
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql;
		sql = "SELECT details FROM kf WHERE report_name = '" + user_name + "'";
		try {
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			String details = rs.getString("details");
			if (details.matches("")) {
				try {
					sql = "DELETE FROM kf WHERE report_name = ?";
					ps = con.prepareStatement(sql);
					ps.setString(1, user_name);
					ps.executeUpdate();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1);
				}
			}
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, e1);
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e1) {
				System.out.println(e1.toString());
			}
		}

	}

	public static void update_value_to_db() {
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql;
		boolean present = false;
		try {
			sql = "UPDATE kf SET moisture_trials = ? , " + "moisture_result = ? , " + "parameters = ? , "
					+ "details = ? " + "WHERE report_name = ?";
			ps = con.prepareStatement(sql);
			//ps.setString(1, db_moisture_trials);
		
			ps.executeUpdate();
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, e1);
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e1) {
				System.out.println(e1.toString());
			}
		}
	}

	public static void insert_value_to_db() {
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql;
		boolean present = false;
		try {
			sql = "INSERT INTO kf(report_name,date,parameters,details,kff_trials,kff_result,moisture_trials,moisture_result,remarks) VALUES(?,?,?,?,?,?,?,?,?)";
			ps = con.prepareStatement(sql);
	//		ps.setString(1, db_report_name);
			
			ps.executeUpdate();
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, e1);
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e1) {
				System.out.println(e1.toString());
			}
		}
	}


	public static void main(String[] args) {
		if(args.length > 0) {
			user_name = args[0];
		}
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame1.getGraphicsConfiguration());
		int taskHeight = screenInsets.bottom;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		hei = d.height - taskHeight;
		wid = d.width;
		frame1.setBounds(0, 0, (int)(0.3*wid), (int)(0.25*hei));
		frame1.add(p);
		frame1.setLocationRelativeTo(null);
		frame1.getContentPane().add(new calibrate_electrode());
		frame1.setResizable(false);
		frame1.setVisible(true);
		frame1.repaint();
		ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
		frame1.setIconImage(img.getImage());
		frame1.setTitle("Calibrate Electrode");
		frame1 = new JFrame();
		p = new JPanel();
		p.invalidate();
		p.revalidate();
		p.repaint();
		
		frame1.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				System.out.println("Closing Calibrate");
				ReformatBuffer.current_exp = "main";
				frame1.dispose();
				frame1 = new JFrame();
				p = new JPanel();
				p.invalidate();
				p.revalidate();
				p.repaint();
			}
		});
			
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}

}

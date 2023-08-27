package main.java;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class calibrate_electrode extends JPanel {
	static JFrame frame = new JFrame();
	static String user_name = "";
	static JLabel text_mV, text_ph,previous_value;
	static JButton button_calibrate;
	static int int_temp_mv ;
	static int wid = 0, hei = 0;
	static boolean stop_updating = false; 
	static int e_calib = 0;
	
	static JPanel p = new JPanel();
	public calibrate_electrode() {
		setLayout(null);
		initialize();
		get_data();
	}

	@SuppressWarnings("removal")
	public static void initialize() {
		frame.getContentPane().invalidate();
		frame.getContentPane().validate();
		frame.getContentPane().repaint();
		
		text_mV = new JLabel("<html>mV <br/>0 mV</html>");
		text_mV.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.016 * wid)));
		text_mV.setBounds((int) Math.round(0.03 * wid), (int) Math.round(0.001 * hei), (int) Math.round(0.4 * wid),
				(int) Math.round(0.10 * hei));
		frame.getContentPane().add(text_mV);

		text_ph = new JLabel("<html>pH <br/> 0</html>");
		text_ph.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.016 * wid)));
		text_ph.setBounds((int) Math.round(0.2 * wid), (int) Math.round(0.001 * hei), (int) Math.round(0.4 * wid),
				(int) Math.round(0.10 * hei));
		frame.getContentPane().add(text_ph);

		button_calibrate = new JButton("Calibrate");
		button_calibrate.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.012 * wid)));
		button_calibrate.setBounds((int) Math.round(0.05 * wid), (int) Math.round(0.135 * hei),
				(int) Math.round(0.2 * wid), (int) Math.round(0.05 * hei));

		frame.getContentPane().add(button_calibrate);
		button_calibrate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(button_calibrate.getText().toString().contains("4")) {
					button_calibrate.setText("Check completed for pH 9.2");
					JOptionPane.showMessageDialog(null, "Place the electrode in pH 9.2 solution");
				}
				else if(button_calibrate.getText().toString().contains("9")) {
					close_page();
				}
				else {
					update_electrode_calibration();
				}
			}
		});
		previous_value = new JLabel("Previous value");
		previous_value.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.012 * wid)));
		previous_value.setBounds((int) Math.round(0.03 * wid), (int) Math.round(0.001 * hei), (int) Math.round(0.4 * wid),
				(int) Math.round(0.45 * hei));
		frame.getContentPane().add(previous_value);
	}
	public static void get_data() {
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql;
		sql = "SELECT * FROM config_param WHERE cnfg_param_group = 'electrodeFactor' and cnfg_param_name = 'electrodeFactor'";
		try {
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(null != rs.getString("cnfg_param_value")) {
				previous_value.setText("Previous value = "+rs.getString("cnfg_param_value")+" mV");
				e_calib = Integer.parseInt(rs.getString("cnfg_param_value"));
			}
			else
				previous_value.setText("Previous value = 0");	
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
	public static void update_calibrate_mv(String msg) {
		// System.out.println("Calibrate = "+msg);
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
		int_temp_mv = Integer.parseInt(mv_val_str);
		if(stop_updating == false) {
			if (msg.contains("T")) {
				text_mV.setText("<html>mV <br/>" + (int_temp_mv) + " mV</html>");
				text_ph.setText("<html>pH <br/>" +String.format("%.2f", (7-((float)(int_temp_mv-e_calib) / 54)))+ "</html>");
			} else if (msg.contains("N")) {
				text_mV.setText("<html>mV <br/> " + ((int_temp_mv*(-1)))+ " mV</html>");
				text_ph.setText("<html>pH <br/> " +String.format("%.2f", (7-((float)(int_temp_mv-e_calib) / 54)))+ "</html>");
				int_temp_mv = int_temp_mv*(-1);
			}
		}
	}
	
	public static void update_electrode_calibration() {
		System.out.println("check details Electrode");
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
	
		try {
			String sql = "UPDATE config_param SET cnfg_param_value = ? WHERE cnfg_param_group =?  and cnfg_param_name = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, String.valueOf((int_temp_mv)));
			ps.setString(2, "electrodeFactor");
			ps.setString(3, "electrodeFactor");

			ps.executeUpdate();
		} 
		catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, e1);
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e1) {
				System.out.println(e1.toString());
			}
		}
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), menubar.user_name, "Electrode Calibrated to "+int_temp_mv);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		e_calib = (e_calib + (int_temp_mv-e_calib));
		menubar.e_calibration = e_calib;
		text_mV.setText("<html>mV <br/>0</html>");
		text_ph.setText("<html>pH <br/>7</html>");
		previous_value.setText("Current value = "+int_temp_mv+" mV");	
		JOptionPane.showMessageDialog(null, "Electrode calibrated to "+int_temp_mv);
		button_calibrate.setText("Check completed for pH 4.0");
		JOptionPane.showMessageDialog(null, "Place the electrode in pH 4.0 solution");

	}
	public static void close_page() {
		ReformatBuffer.current_exp = "main";
		frame.dispose();
		
		frame = new JFrame();
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
	public static void main(String[] args) {

		if (args.length != 0) {
			user_name = args[0];
		}

		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
		int taskHeight = screenInsets.bottom;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int height = d.height - taskHeight;
		int width = d.width;
		wid = (int) d.getWidth();
		hei = (int) (d.getHeight() - taskHeight);

		int wid1 = (int) Math.round(0.3 * wid);
		int hei1 = (int) Math.round(0.3 * hei);

		System.out.println(wid + "   dfvdvdv " + hei);

		// frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setBounds(0, 0, wid1, hei1);
		frame.add(p);
		frame.getContentPane().add(new calibrate_electrode());
		frame.setLocationRelativeTo(null);

		frame.setResizable(true);
		frame.setVisible(true);
		frame.repaint();
		frame.setTitle("Open Potentiometry Results");
		ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
		frame.setIconImage(img.getImage());

		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				frame.dispose();
				frame = new JFrame();
				p = new JPanel();
				p.revalidate();
				p.repaint();
			}
		});
		JOptionPane.showMessageDialog(null, "Place the electrode in pH 7 solution");

	}

}

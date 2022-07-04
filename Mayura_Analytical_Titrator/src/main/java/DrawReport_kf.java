package main.java;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import com.itextpdf.text.DocumentException;

public class DrawReport_kf extends JPanel {

	static int wid = 0, hei = 0;

	static JFrame frame1 = new JFrame();
	static JTable table1;
	static JTable table2 = new JTable();
	static JRadioButton b, b1;
	static JPanel p = new JPanel();
	static JLabel kf_mv_display, result_header, result;
	static JFrame frame2 = new JFrame();

	private JTextField textField;
	static JTable table11, table12;

	static JLabel vol_filled;
	static JLabel vol_dosed;
	static JLabel delay_timer;
	public static String variables[] = new String[17];
	public static String metd_name, metd_data;

	int j = 1;
	static int row = 0;

	static DefaultTableModel model, model2;
	static JTextArea display;
	static JPanel panel_result;

	static JLabel experiment_performing;
	static JRadioButton radio_btn_kff, radio_btn_moisture;

	static JScrollPane scrollPane = new JScrollPane();
	static JScrollPane scrollPane2 = new JScrollPane();
	static JTextField remarks_input = new JTextField();

	static String[] params = { "Analyzed by", "Date", "Time", "Method Name", "Delay", "Stir Time", " Max Vol",
			"Blank Vol", "Burette Factor", "Density", "KF Factor", "End Point", "Dosage Rate", "Result Unit",
			"No of Trials", "SOP", "AR No", "Batch No", "Sample Name", "Report Name", "Reagent Name", "Std. Technique",
			"Certified by" };

	static String report_name = "";
	static String parameter = "";
	static String details = "";
	static String kff_trials = "";
	static String kff_results = "";
	static String moisture_trials = "";
	static String moisture_results = "";
	static String remarks = "";

	static JLabel rsd_header, rsd_header1, result_header1;

	static boolean certify = false;
	static boolean certify_now = false;

	static String user_name = "",permission_report = "";

	static JCheckBox checkBox1;

	
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
	
	public DrawReport_kf() {
		setLayout(null);
		System.out.println("Contructor draw report");

		JLabel experiment = new JLabel("KF");
		experiment.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.013 * wid)));
		experiment.setBounds((int) Math.round(0.013 * wid), (int) Math.round(0.02 * hei), (int) Math.round(0.05 * wid),
				(int) Math.round(0.03 * hei));
		add(experiment);

		JLabel Method_param = new JLabel("Method Parameters");
		Method_param.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.013 * wid)));
		Method_param.setBounds((int) Math.round(0.1 * wid), (int) Math.round(0.02 * hei), (int) Math.round(0.225 * wid),
				(int) Math.round(0.0306 * hei));
		add(Method_param);

		JLabel exp_details = new JLabel("Experiment Details");
		exp_details.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.013 * wid)));
		exp_details.setBounds((int) Math.round(0.4 * wid), (int) Math.round(0.02 * hei), (int) Math.round(0.195 * wid),
				(int) Math.round(0.0392 * hei));
		add(exp_details);

		radio_btn_kff = new JRadioButton("KFF");
		radio_btn_kff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				five_column_kff();
			}
		});
		radio_btn_kff.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0125 * wid)));
		radio_btn_kff.setBounds((int) Math.round(0.7 * wid), (int) Math.round(0.02 * hei), (int) Math.round(0.06 * wid),
				(int) Math.round(0.036 * hei));
		add(radio_btn_kff);

		radio_btn_moisture = new JRadioButton("Sample");
		radio_btn_moisture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				five_column_moisture();
			}
		});
		radio_btn_moisture.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0125 * wid)));
		radio_btn_moisture.setBounds((int) Math.round(0.85 * wid), (int) Math.round(0.02 * hei),
				(int) Math.round(0.1 * wid), (int) Math.round(0.036 * hei));
		add(radio_btn_moisture);
		ButtonGroup bg1 = new ButtonGroup();
		bg1.add(radio_btn_kff);
		bg1.add(radio_btn_moisture);
		radio_btn_kff.setSelected(true);

		display = new JTextArea();
		display.setEditable(false);
		display.setFont(display.getFont().deriveFont(15f));
		JScrollPane scroll = new JScrollPane(display);
		scroll.setBounds((int) Math.round(0.4 * wid), (int) Math.round(0.098 * hei), (int) Math.round(0.58 * wid),
				(int) Math.round(0.4044 * hei));
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(scroll);

		JButton btn_print_kf_result = new JButton("Print");
		btn_print_kf_result.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		btn_print_kf_result.setBounds((int) Math.round(0.902 * wid), (int) Math.round(0.87 * hei),
				(int) Math.round(0.07 * wid), (int) Math.round(0.0392 * hei));
		add(btn_print_kf_result);
		btn_print_kf_result.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				print_kf_result();
			}
		});

		JButton btn_update_kf_remarks = new JButton("Update");
		btn_update_kf_remarks.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		btn_update_kf_remarks.setBounds((int) Math.round(0.815 * wid), (int) Math.round(0.87 * hei),
				(int) Math.round(0.07 * wid), (int) Math.round(0.0392 * hei));
		add(btn_update_kf_remarks);
		btn_update_kf_remarks.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				update_kf_remarks();
			}
		});

		result_header = new JLabel("KFF_Result :");
		result_header.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.013 * wid)));
		result_header.setBounds((int) Math.round(0.4 * wid), (int) Math.round(0.8 * hei), (int) Math.round(0.195 * wid),
				(int) Math.round(0.0392 * hei));
		add(result_header);

		rsd_header = new JLabel("KFF_RSD :");
		rsd_header.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.013 * wid)));
		rsd_header.setBounds((int) Math.round(0.55 * wid), (int) Math.round(0.8 * hei), (int) Math.round(0.195 * wid),
				(int) Math.round(0.0392 * hei));
		add(rsd_header);

		result_header1 = new JLabel("M_Result :");
		result_header1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.013 * wid)));
		result_header1.setBounds((int) Math.round(0.75 * wid), (int) Math.round(0.8 * hei),
				(int) Math.round(0.195 * wid), (int) Math.round(0.0392 * hei));
		add(result_header1);

		rsd_header1 = new JLabel("M_RSD :");
		rsd_header1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.013 * wid)));
		rsd_header1.setBounds((int) Math.round(0.90 * wid), (int) Math.round(0.8 * hei), (int) Math.round(0.195 * wid),
				(int) Math.round(0.0392 * hei));
		add(rsd_header1);

		JLabel Remarks = new JLabel("Remarks : ");
		Remarks.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.013 * wid)));
		Remarks.setBounds((int) Math.round(0.4 * wid), (int) Math.round(0.87 * hei), (int) Math.round(0.8 * wid),
				(int) Math.round(0.03 * hei));
		add(Remarks);

		remarks_input = new JTextField(remarks);
		remarks_input.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.013 * wid)));
		remarks_input.setBounds((int) Math.round(0.48 * wid), (int) Math.round(0.87 * hei), (int) Math.round(0.2 * wid),
				(int) Math.round(0.04 * hei));
		add(remarks_input);
		if (certify == true) {
			checkBox1 = new JCheckBox();
			checkBox1.setText("Certify");
			checkBox1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.012 * wid)));
			checkBox1.setBounds((int) Math.round(0.7 * wid), (int) Math.round(0.87 * hei), (int) Math.round(0.08 * wid),
					(int) Math.round(0.04 * hei));
			add(checkBox1);
			checkBox1.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == 1) {
						certify_now = true;
					} else {
						certify_now = false;
					}
				}
			});
		}		
		initialize();
	}

	public static void update_kf_remarks() {
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql = null;
		String temp_remarks = "";
		String user_text = "";
		try {
			if (certify_now == true) {
				String update_params = "";
				String[] temp_param = parameter.split(",");

				for (int i = 0; i < 23; i++) {
					if (i == 0) {
						update_params = update_params + temp_param[i];
					} else {
						if (i == 22) {
							update_params = update_params + "," + user_name;
						} else {
							update_params = update_params + "," + temp_param[i];
						}
					}
				}
				if (remarks_input.getText().toString().matches("")) {
					temp_remarks = "No Remarks";
				} else {
					temp_remarks = remarks_input.getText().toString();
				}
				sql = "UPDATE kf SET remarks = ?,parameters=? WHERE report_name = ?";
				ps = con.prepareStatement(sql);
				ps.setString(1, temp_remarks);
				ps.setString(2, update_params);
				ps.setString(3, report_name);
				
				user_text = report_name +" Certified By: "+user_name;
			} else {
				String update_params = "";
				String[] temp_param = parameter.split(",");

				for (int i = 0; i < 23; i++) {
					if (i == 0) {
						update_params = update_params + temp_param[i];
					} else {
						if (i == 22) {
							update_params = update_params + "," + "Not Certified";
						} else {
							update_params = update_params + "," + temp_param[i];
						}
					}
				}
				if (remarks_input.getText().toString().matches("")) {
					temp_remarks = "No Remarks";
				} else {
					temp_remarks = remarks_input.getText().toString();
				}
				sql = "UPDATE kf SET remarks = ?,parameters=? WHERE report_name = ?";
				ps = con.prepareStatement(sql);
				ps.setString(1, temp_remarks);
				ps.setString(2, update_params);
				ps.setString(3, report_name);
				user_text = user_text +" Remarks Updated";
			}
			ps.executeUpdate();
			JOptionPane.showMessageDialog(null, "KF Remarks Updated Successfully!");
			frame1.dispose();
			frame1 = new JFrame();
			p = new JPanel();
			p.invalidate();
			p.revalidate();
			p.repaint();
	
				
		} catch (SQLException e1) {
			System.out.println(e1.toString());
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e1) {
				System.out.println(e1.toString());
			}
		}
		
		try {
			audit_log_push.push_to_audit(get_date(), get_time() ,user_name,user_text);
		} catch (ParseException e1) {e1.printStackTrace();}
		
		String[] aa = {report_name,user_name,permission_report};			
		DrawReport_kf.main(aa);		
	}
	
	public static void print_kf_result() {
		
		String company_details = get_company_details();
		String parameter = "",header = "",value = "",trials="",result="",rsd="",instrument_id = "";
		String[] company_arr = company_details.split(">");
		
		String company_details_temp = company_arr[1]+">"+company_arr[2];
		
		if(radio_btn_kff.isSelected()) {
			parameter = parameter+ "Standardisation Report";
			header = "Trial No,Volume Dosed (mL),Weight in mg or microlitre,KFF";
			value = kff_trials;
			String[] temparr = kff_results.split(",");
			result = temparr[0];	
			rsd = temparr[1]+"%";
			parameter = parameter +",Rea.Mfgr : "+table2.getValueAt(20, 1);
			parameter = parameter +",Date : "+table2.getValueAt(1, 1);
			parameter = parameter +",Batch No : "+table2.getValueAt(17, 1);
			parameter = parameter +",Time : "+table2.getValueAt(2, 1);
			parameter = parameter +",Std. Tech : "+table2.getValueAt(21, 1);
			parameter = parameter +",Method File : "+table2.getValueAt(3, 1);
			parameter = parameter +",End Point : "+table2.getValueAt(11, 1);
			parameter = parameter +",Instrument ID : "+company_arr[0];
			parameter = parameter +",AR No : "+table2.getValueAt(16, 1);
			parameter = parameter +",User Name : "+table2.getValueAt(0, 1);
			parameter = parameter +",Titration Speed : "+table2.getValueAt(12, 1);
			parameter = parameter+",Report Name : "+report_name;
			parameter = parameter+","+report_name;
			parameter = parameter+","+table2.getValueAt(0, 1);
			parameter = parameter+","+table2.getValueAt(22, 1);
			try {
				audit_log_push.push_to_audit(get_date(), get_time() ,user_name,"Printing "+report_name+" - Standardization Report");
			} catch (ParseException e1) {e1.printStackTrace();}

		}
		
		else if(radio_btn_moisture.isSelected()){
			parameter = parameter+ "Moisture Estimation Report";	
			header = "Trial No,Volume Dosed (mL),Weight in mg or microlitre,Moisture";
			value = moisture_trials;
			String[] temparr = moisture_results.split(",");
			result = temparr[0];	
			rsd = temparr[1]+"%";	
			parameter = parameter +",Sample Name : "+table2.getValueAt(18, 1);
			parameter = parameter +",Date-Time : "+table2.getValueAt(1, 1)+"-"+table2.getValueAt(2, 1);
			parameter = parameter +",Batch No : "+table2.getValueAt(17, 1);
			parameter = parameter +",KF Factor : "+table2.getValueAt(10, 1);
			parameter = parameter +",Density : "+table2.getValueAt(9, 1);
			parameter = parameter +",Method File : "+table2.getValueAt(3, 1);
			parameter = parameter +",Blank Vol : "+table2.getValueAt(7, 1);
			parameter = parameter +",Instrument ID : "+company_arr[0];
			parameter = parameter +",AR No : "+table2.getValueAt(16, 1);
			parameter = parameter +",User Name : "+table2.getValueAt(0, 1);
			parameter = parameter +",Titration Speed : "+table2.getValueAt(12, 1);
			parameter = parameter+",Report Name : "+report_name;
			parameter = parameter+","+report_name;
			parameter = parameter+","+table2.getValueAt(0, 1);
			parameter = parameter+","+table2.getValueAt(22, 1);
			try {
				audit_log_push.push_to_audit(get_date(), get_time() ,user_name,"Printing "+report_name+" - Analysis Report");
			} catch (ParseException e1) {e1.printStackTrace();}
		}
		
		String remarks = remarks_input.getText().toString();
		

		
		try {
			report.generate_report(company_details_temp, parameter, header, value, false, trials, result, rsd,remarks);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		/        generate_report(company_details,parameters,headers,values,graph,trials,result,rsd);

		
	}
	
	public static String get_company_details() {
		String data = "";
			Connection con = DbConnection.connect();
			PreparedStatement ps = null;
			ResultSet rs = null;
			String sql;
			try {
				sql = "SELECT * FROM company_data";
				ps = con.prepareStatement(sql);
				rs = ps.executeQuery();
				data = data + rs.getString("instrument_id");
				data = data + ">" + rs.getString("company_name");
				data = data + ">" + rs.getString("company_address");						
			} catch (SQLException e1) {
				// JOptionPane.showMessageDialog(null,e1);
			} finally {
				try {
					ps.close();
					con.close();
				} catch (SQLException e1) {
					System.out.println(e1.toString());
				}
			}
			return data;
	}
	
	@SuppressWarnings("removal")
	public static void initialize() {
		frame1.getContentPane().invalidate();
		frame1.getContentPane().validate();
		frame1.getContentPane().repaint();
		get_data_kf();
		two_column();
		try {
			String[] params_data = parameter.split(",");
			for (int i = 0; i < 23; i++) {
				add_row_to_two_column(i, params[i], params_data[i]);
			}
			if (!params_data[22].matches("Not Certified")) {
				checkBox1.setSelected(true);
				certify_now = true;
				checkBox1.setEnabled(false);
			}
			String[] details_data = details.split(",");
			String temp_details = "";
			for (int i = 0; i < details_data.length; i++) {
				temp_details = temp_details + details_data[i] + "\n\n";
				display.setText(temp_details);
				//System.out.println(temp_details);
			}
		} catch (NullPointerException njh) {

		} catch (ArrayIndexOutOfBoundsException fd) {
			System.out.println("");
		}
		five_column_kff();
		
		try {
			audit_log_push.push_to_audit(get_date(), get_time() ,user_name,"KF Report - "+report_name+" Opened");
		} catch (ParseException e1) {e1.printStackTrace();}
	}

	public static void get_data_kf() {
		System.out.println("get_data_kf");
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		try {
			sql = "SELECT * FROM kf WHERE report_name = '" + report_name + "'";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			// report_name = rs.getString("report_name");
			parameter = rs.getString("parameters");
			details = rs.getString("details");
			kff_trials = rs.getString("kff_trials");
			kff_results = rs.getString("kff_result");
			moisture_trials = rs.getString("moisture_trials");
			moisture_results = rs.getString("moisture_result");
			remarks = rs.getString("remarks");
			System.out.println("report_name sdfsdfsd = " + parameter);
		} catch (SQLException e1) {
			System.out.println(e1.toString());
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e1) {
				System.out.println(e1.toString());
			}
		}
	}
	public static void add_row_to_five_column(int r, String v1, String w_g, String kff) {
		model.addRow(new Object[0]);
		model.setValueAt(r + 1, r, 0);
		model.setValueAt(v1, r, 1);
		model.setValueAt(w_g, r, 2);
		model.setValueAt(kff, r, 3);
		model.fireTableDataChanged();
	}

	public static void add_row_to_two_column(int r, String param, String val) {
		model2.addRow(new Object[0]);
		model2.setValueAt(param, r, 0);
		model2.setValueAt(val, r, 1);
		model2.fireTableDataChanged();
	}

	public static void five_column_kff() {
		scrollPane.setBounds((int) Math.round(0.4 * wid), (int) Math.round(0.55 * hei), (int) Math.round(0.58 * wid),
				(int) Math.round(0.23 * hei));
		frame1.getContentPane().add(scrollPane);
		table1 = new JTable();
		table1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0097 * wid)));
		model = new DefaultTableModel() {
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return Integer.class;
				case 1:
					return String.class;
				case 2:
					return String.class;
				default:
					return String.class;
				}
			}
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table1.setModel(model);
		model.addColumn("Trial No");
		model.addColumn("Volume Dosed (mL)");
		model.addColumn("Weight in mg or microlitre");
		model.addColumn("KFF");
		table1.setRowHeight((int) Math.round(0.04 * hei));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table1.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table1.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		table1.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		table1.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		scrollPane.setViewportView(table1);
		try {

			String[] kff_trial_data = kff_trials.split(":");
			for (int i = 0; i < kff_trial_data.length; i++) {
				String[] kff_trial_indi = kff_trial_data[i].split(",");
				add_row_to_five_column(i, kff_trial_indi[0], kff_trial_indi[1], kff_trial_indi[2]);
			}
		} catch (ArrayIndexOutOfBoundsException k) {}
		try {
			String[] kff_result = kff_results.split(",");
			result_header.setText("KFF_Result : " + kff_result[0]);
			rsd_header.setText("KFF_RSD : " + kff_result[1]);
		} catch (ArrayIndexOutOfBoundsException k) {}
		try {
			String[] moisture_result2 = moisture_results.split(",");
			result_header1.setText("M_Result : " + moisture_result2[0]);
			rsd_header1.setText("M_RSD : " + moisture_result2[1]);
		} catch (ArrayIndexOutOfBoundsException k) {}
		
		remarks_input.setText(remarks);
		if(!remarks.matches("No Remarks"))
			remarks_input.setEnabled(false);
	}

	public static void five_column_moisture() {
		scrollPane.setBounds((int) Math.round(0.4 * wid), (int) Math.round(0.55 * hei), (int) Math.round(0.58 * wid),
				(int) Math.round(0.23 * hei));
		frame1.getContentPane().add(scrollPane);
		table1 = new JTable();
		table1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0097 * wid)));
		model = new DefaultTableModel() {
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return Integer.class;
				case 1:
					return String.class;
				case 2:
					return String.class;
				default:
					return String.class;
				}
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table1.setModel(model);
		model.addColumn("Trial No");
		model.addColumn("Volume Dosed (mL)");
		model.addColumn("Weight in mg or microlitre");
		model.addColumn("Moisture");
		table1.setRowHeight((int) Math.round(0.04 * hei));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table1.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table1.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		table1.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		table1.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

		scrollPane.setViewportView(table1);

		try {
			String[] kff_trial_data = moisture_trials.split(":");
			for (int i = 0; i < kff_trial_data.length; i++) {
				String[] kff_trial_indi = kff_trial_data[i].split(",");
				add_row_to_five_column(i, kff_trial_indi[0], kff_trial_indi[1], kff_trial_indi[2]);
			}
		} catch (ArrayIndexOutOfBoundsException jdd) {
		}

	}

	public static void two_column() {
		scrollPane2.setBounds((int) Math.round(0.013 * wid), (int) Math.round(0.06 * hei), (int) Math.round(0.35 * wid),
				(int) Math.round(0.87 * hei));
		frame1.getContentPane().add(scrollPane2);

		table2 = new JTable();
		table2.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0097 * wid)));

		model2 = new DefaultTableModel() {
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return Integer.class;
				default:
					return String.class;
				}
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table2.setModel(model2);
		model2.addColumn("Parameters");
		model2.addColumn("Value");
		table2.setRowHeight((int) Math.round(0.0447 * hei));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table2.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table2.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

		scrollPane2.setViewportView(table2);

	}

	public static void main(String[] args) {

		if (args.length != 0) {
			report_name = args[0];
			user_name = args[1];
			permission_report = args[2];

			if (args[2].contains("Certify")) {
				certify = true;
			}

		}

		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame1.getGraphicsConfiguration());
		int taskHeight = screenInsets.bottom;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		hei = d.height - taskHeight;
		wid = d.width;

//	    hei = 720;
//	    wid = 1280;

		System.out.println(wid + "   dfvdvdv " + hei);
		frame1.setBounds(0, 0, wid, hei);
		frame1.add(p);
		frame1.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame1.getContentPane().add(new DrawReport_kf());
		ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
		frame1.setIconImage(img.getImage());
		frame1.setResizable(true);
		frame1.setVisible(true);
		frame1.repaint();
		ReformatBuffer.current_exp = "kf";
		frame1.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				frame1.dispose();
				frame1 = new JFrame();
				p = new JPanel();
				p.invalidate();
				p.revalidate();
				p.repaint();
			}
		});
	}
}

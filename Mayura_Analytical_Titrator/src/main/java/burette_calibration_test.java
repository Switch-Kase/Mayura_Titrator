package main.java;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import java.text.DecimalFormat;
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
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.AbstractCategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.FloatDimension;

import com.fazecast.jSerialComm.SerialPort;
import com.itextpdf.text.DocumentException;

public class burette_calibration_test extends JPanel implements ItemListener {
	static JFrame frame = new JFrame();
	static JTable table1;
	static JTable table2 = new JTable();
	static JPanel p = new JPanel();
	private static int HEADER_HEIGHT = 60;

	static DefaultTableModel model;
	static double wid, hei;
	static String f_date, t_date, c_dat;
	static PrintWriter output_bc;
	static SerialPort sp1;
	static JLabel vol_fill, vol_dose, burette_factor;
	static ScheduledExecutorService exec_bc_fill, exec_bc_dose;
	static int row_cnt = 0, arrow = 1;
	static double dose = 0, dose_counter = 0, b_factor = 0;
	static JButton btn_print, btn_test_buretter_calibration, btn_update_buretter_factor, btn_done_buretter_calibration,
			btn_brtest_report;
	static boolean test = true;
	static JScrollPane scrollPane, scrollPane1;
	static DecimalFormat df = new DecimalFormat("0.000");
	
	static String str_format = "%.4f", double_format = "#.####";

	public burette_calibration_test() {
		setLayout(null);
		initialize();
	}

	public static void port_setup_bct(SerialPort sp) {
		try {
			output_bc = new PrintWriter(sp.getOutputStream());
			sp1 = sp;
			try {
				Thread.sleep(250);
				output_bc.print("<8888>DOSR,006*");
				output_bc.flush();
			} catch (InterruptedException ex) {
			} catch (NullPointerException ee) {
				JOptionPane.showMessageDialog(null, "Please select the ComPort!");
			}
			ReformatBuffer.current_state = "bct_dosr";
			
		} catch (NullPointerException np) {
			JOptionPane.showMessageDialog(null, "Please select the ComPort!");
			frame.dispose();
			frame = new JFrame();
			p = new JPanel();
			p.revalidate();
			p.repaint();
		}
	}

	
	public static void send_cvop() {
		System.out.println("Inside send CVOP");
		try {
			Thread.sleep(250);
			output_bc.print("<8888>CVOP*");
			output_bc.flush();
			ReformatBuffer.current_state = "dg_bct_cvop";
		} catch (InterruptedException ex) {
		} catch (NullPointerException ee) {
			JOptionPane.showMessageDialog(null, "Please select the ComPort!");
		}
	}
	
	public static void cvop_ok_received() {
		send_afil();
	}
	
	
	public static void send_afil() {
		System.out.println("Inside send AFILL bc");
		if (row_cnt <= 2) {
			try {
				Thread.sleep(500);
				output_bc.print("<8888>AFIL*");
				output_bc.flush();
				ReformatBuffer.current_state = "bct_afil";
			} catch (InterruptedException ex) {
			} catch (NullPointerException ee) {
				JOptionPane.showMessageDialog(null, "Please select the ComPort!");
			}
		} else{
			btn_brtest_report.setEnabled(true);
		}
	}

	public static void bct_afill_ok_received() {
		System.out.println("Inside AFIL OK");

		exec_bc_fill = Executors.newSingleThreadScheduledExecutor();
		exec_bc_fill.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (arrow == 1) {
					vol_fill.setText("Filling ");
				}
				if (arrow == 2) {
					vol_fill.setText("Filling >");
				}
				if (arrow == 3) {
					vol_fill.setText("Filling >>");
				}
				if (arrow == 4) {
					vol_fill.setText("Filling >>>");
				}
				if (arrow == 5) {
					vol_fill.setText("Filling >>>>");
				}
				if (arrow == 6) {
					vol_fill.setText("Filling >>>>>");
				}
				if (arrow == 7) {
					vol_fill.setText("Filling >>>>>>");
				}
				if (arrow == 8) {
					vol_fill.setText("Filling >>>>>>>");
				}
				if (arrow == 9) {
					vol_fill.setText("Filling >>>>>>>>");
				}
				if (arrow == 10) {
					vol_fill.setText("Filling >>>>>>>>>");
					arrow = 0;
				}
				arrow++;
			}
		}, 0, 50, TimeUnit.MILLISECONDS);
	}

	public static void bct_afill_end_received() {
		exec_bc_fill.shutdown();
		input_popup();
	}
	
	public static void input_popup() {
		String result = (String) JOptionPane.showInputDialog(frame, "Enter the weight W1 and then PRESS OK!",
				"Enter W1", JOptionPane.PLAIN_MESSAGE, null, null, "");
		try {
			double w1 = Double.parseDouble(result);
			model.setValueAt(w1, row_cnt, 2);
			model.fireTableDataChanged();
			if (row_cnt == 0 ) {
				dose = 5;
			}
			if (row_cnt == 1) {
				dose = 10;
			}
			if (row_cnt == 2) {
				dose = 15;
			}
			send_dose();
		} catch (NullPointerException ne) {
			JOptionPane.showMessageDialog(null, "Please enter a value!");
			input_popup();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Please enter a valid value!");
			input_popup();
		}
	}

	public static void send_dose() {
		try {
			Thread.sleep(500);
			output_bc.print("<8888>DOSE*");
			output_bc.flush();
			ReformatBuffer.current_state = "bct_dose";
		} catch (InterruptedException ex) {
		} catch (NullPointerException ee) {
			JOptionPane.showMessageDialog(null, "Please select the ComPort!");
		}
	}

	public static void bct_dose_ok_received() {
		dose_counter = 0;
		exec_bc_dose = Executors.newSingleThreadScheduledExecutor();
		exec_bc_dose.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (dose_counter < dose) {
					dose_counter = dose_counter + (((6 / 60.0) / 10)*b_factor);
					vol_dose.setText("Volume Dosed : " + String.format("%.1f", dose_counter) + " mL");
				} else {
					dose_counter = 0;
					exec_bc_dose.shutdown();
					try {
						Thread.sleep(300);
						output_bc.print("<8888>ESCP*");
						output_bc.flush();
						ReformatBuffer.current_state = "bct_stpm";
					} catch (NullPointerException ex) {
						JOptionPane.showMessageDialog(null, "Please connect to ComPort!");
					} catch (InterruptedException jb) {
					}
				}
			}
		}, 0, 100, TimeUnit.MILLISECONDS);
	}

	public static void bct_dosr_ok_received() {
		//JOptionPane.showMessageDialog(null, "Dose Rate at 6 ml/min");
	}
	
	

	public static void bct_stpm_ok_received() {
		String result = (String) JOptionPane.showInputDialog(frame, "Enter the weight W2 and then PRESS OK!",
				"Enter W2", JOptionPane.PLAIN_MESSAGE, null, null, "");
		try {
			DecimalFormat df = new DecimalFormat(double_format);
			double w2 = Double.parseDouble(result);
			w2 = Double.valueOf(df.format(w2));
			model.setValueAt(w2, row_cnt, 3);
			
			Double w3 = w2 - Double.parseDouble(model.getValueAt(row_cnt, 2).toString());
			w3 = Double.valueOf(df.format(w3));
			model.setValueAt(w3, row_cnt, 4);		
			model.fireTableDataChanged();
			
			row_cnt++;
			if(row_cnt<=3)
				send_cvop();
		} catch (NullPointerException ne) {
			JOptionPane.showMessageDialog(null, "Please enter a value!");
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Please enter a valid value!");
		}
	}
	
	public static double get_burette_factor() {
		double temp_bf = 0;
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql;

		sql = "SELECT * FROM config_param WHERE cnfg_param_group = 'buretteFactor' and cnfg_param_name = 'buretteFactor'";

		try {
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			temp_bf = Double.parseDouble(rs.getString("cnfg_param_value"));
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

		return temp_bf;
	}

	@SuppressWarnings("removal")
	public static void initialize() {
		frame.getContentPane().invalidate();
		frame.getContentPane().validate();
		frame.getContentPane().repaint();

		test_four_column();

		vol_fill = new JLabel("Filling");
		vol_fill.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.012 * wid)));
		vol_fill.setBounds((int) Math.round(0.013 * wid), (int) Math.round(0.04 * hei), (int) Math.round(0.15 * wid),
				(int) Math.round(0.0428 * hei));
		frame.getContentPane().add(vol_fill);

		vol_dose = new JLabel("Volume Dosed : ");
		vol_dose.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.012 * wid)));
		vol_dose.setBounds((int) Math.round(0.22 * wid), (int) Math.round(0.04 * hei), (int) Math.round(0.2 * wid),
				(int) Math.round(0.0428 * hei));
		frame.getContentPane().add(vol_dose);

		JButton btn_start = new JButton("Start >>>");
		btn_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btn_start.setEnabled(false);
				send_cvop();
			}
		});
		btn_start.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.009 * wid)));
		btn_start.setBounds((int) Math.round(0.013 * wid), (int) Math.round(0.88 * hei), (int) Math.round(0.0976 * wid),
				(int) Math.round(0.0428 * hei));
		frame.getContentPane().add(btn_start);

		btn_brtest_report = new JButton("Print Report");
		btn_brtest_report.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				print_burette_calibration_report();
			}
		});
		btn_brtest_report.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.009 * wid)));
		btn_brtest_report.setBounds((int) Math.round(0.8 * wid), (int) Math.round(0.88 * hei),
				(int) Math.round(0.0976 * wid), (int) Math.round(0.0428 * hei));
		btn_brtest_report.setEnabled(false);
		frame.getContentPane().add(btn_brtest_report);
		
		b_factor = get_burette_factor();
		System.out.println("BF = "+b_factor);
	}

	public static void print_burette_calibration_report() {

		String company_details = "";
		String parameter = "", header = "", value = "", trials = "", result = "", rsd = "", instrument_id = "",
				analyzed_by = "",result_kff="";
		trials = "1,5," + table1.getValueAt(0, 4).toString()+",";
		result_kff = String.valueOf((Double.parseDouble(df.format(((Double.parseDouble(table1.getValueAt(0, 4).toString())-5)/5)*100))));
		trials = trials+result_kff+":";
		trials = trials+"2,10," + table1.getValueAt(1, 4).toString()+",";
		result_kff = String.valueOf((Double.parseDouble(df.format(((Double.parseDouble(table1.getValueAt(1, 4).toString())-10)/10)*100))));
		trials = trials+result_kff+":";
		trials = trials+"3,15," + table1.getValueAt(2, 4).toString()+",";
		result_kff = String.valueOf((Double.parseDouble(df.format(((Double.parseDouble(table1.getValueAt(2, 4).toString())-15)/15)*100))));
		trials = trials+result_kff;
		


		company_details = get_company_details();
		String[] company_arr = company_details.split(">");
		parameter = "Burette Calibration Report,Date: " + get_date() + ",Time: " + get_time() + ",Instrument ID: "
				+ company_arr[0] + ",User Name: " + menubar.user_name;
		header = "Sl No,Volume displayed(mL),Actual Volume dispensed(mL),Deviation %";

		analyzed_by = menubar.user_name;
		instrument_id = company_arr[0];

		String company_details_temp = company_arr[1] + ">" + company_arr[2];
		try {
			report.generate_report_burette_calib(company_details_temp, parameter, header, trials, analyzed_by,
					instrument_id);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
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

	public static void add_row_to_four_column(int r, String v1, String v2, String v3, String v4) {
		model.addRow(new Object[0]);
		model.setValueAt(r + 1, r, 0);
		model.setValueAt(v1, r, 1);
		model.setValueAt(v2, r, 2);
		model.setValueAt(v3, r, 3);
		model.setValueAt(v4, r, 4);
		model.fireTableDataChanged();
	}

	public static void test_four_column() {
		scrollPane1 = new JScrollPane();
		scrollPane1.setBounds((int) Math.round(0.013 * wid), (int) Math.round(0.1 * hei), (int) Math.round(0.95 * wid),
				(int) Math.round(0.75 * hei));
		frame.getContentPane().add(scrollPane1);
		table1 = new JTable();
		table1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.012 * wid)));

		model = new DefaultTableModel() {
			public Class<?> getColumnClass(int column) {
				switch (column) {

				case 0:
					return Integer.class;
				case 1:
					return String.class;
				case 2:
					return String.class;
				case 3:
					return String.class;
				default:
					return String.class;
				}
			}
		};
		table1.setModel(model);
		table1.setDefaultEditor(Object.class, null);
		model.addColumn("Trials");
		model.addColumn("Displayed Volume in mL");
		model.addColumn("Weight of Empty Flask(gms){W1}");
		model.addColumn("Weight of Flask with H20(gms){W2}");
		model.addColumn("Weight of Actual Vol of H2O dispensed(gms){W2-W1}");

		table1.setRowHeight((int) Math.round(0.076 * hei));

		JTableHeader header = table1.getTableHeader();
		header.setPreferredSize(new Dimension((int) Math.round(0.0612 * hei), (int) Math.round(0.0612 * hei)));
		header.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.009765 * wid)));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table1.setDefaultRenderer(String.class, centerRenderer);
		header.setDefaultRenderer(centerRenderer);

		table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table1.getColumnModel().getColumn(0).setPreferredWidth((int) Math.round(0.0651 * wid));
		table1.getColumnModel().getColumn(1).setPreferredWidth((int) Math.round(0.1302 * wid));
		table1.getColumnModel().getColumn(2).setPreferredWidth((int) Math.round(0.1953 * wid));
		table1.getColumnModel().getColumn(3).setPreferredWidth((int) Math.round(0.1953 * wid));
		table1.getColumnModel().getColumn(4).setPreferredWidth((int) Math.round(0.37 * wid));
		table1.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table1.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		table1.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		table1.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		table1.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

		scrollPane1.setViewportView(table1);

		add_row_to_four_column(0, "5", "", "", "");
		add_row_to_four_column(1, "10", "", "", "");
		add_row_to_four_column(2, "15", "", "", "");

	}

	public void itemStateChanged(ItemEvent e) {

	}

	public static void main(String[] args) {
		
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
		int taskHeight = screenInsets.bottom;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int height = d.height - taskHeight;
		int width = d.width;
		wid = d.getWidth();
		hei = d.getHeight() - taskHeight;


		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setBounds(0, 0, (int) wid, (int) hei);
		frame.add(p);
		frame.getContentPane().add(new burette_calibration_test());
		frame.setResizable(true);
		frame.setVisible(true);
		frame.repaint();
		frame.setTitle("Burette Calibration");
		ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
		frame.setIconImage(img.getImage());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				try {
					Thread.sleep(500);
					output_bc.print("<8888>ESCP*");
					output_bc.flush();
					Thread.sleep(200);
				} catch (NullPointerException ne) {
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					sp1.closePort();
				} catch (NullPointerException ne) {
				}
				frame.dispose();
				frame = new JFrame();
				p = new JPanel();
				p.revalidate();
				p.repaint();
			}
		});
	}
}

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

public class burette_calibration extends JPanel {
	static JFrame frame = new JFrame();
	static JTable table1;
	static JTable table2 = new JTable();
	static JPanel p = new JPanel();
	private static int HEADER_HEIGHT = 60;

	static DefaultTableModel model;
	static double wid, hei;
	static String f_date, t_date, c_dat;
	static PrintWriter output_bc;
	static SerialPort serialPort_bc;
	static JLabel vol_fill, vol_dose, burette_factor, legend_w1, legend_w2;
	static ScheduledExecutorService exec_bc_fill, exec_bc_dose;
	static int row_cnt = 0, arrow = 1;
	static double dose = 5, dose_counter = 0, b_factor = 0;
	static JButton btn_test_buretter_calibration, btn_update_buretter_factor;

	static float prev_burette_factor = 0;

	static JScrollPane scrollPane, scrollPane1;
	static String raw_burette_factor = "";
	
	static String str_format = "%.4f";
	static String double_format = "#.####";
	static JRadioButton radioButton_5ml,radioButton_10ml,radioButton_15ml;    

	public burette_calibration() {
		setLayout(null);
		initialize();
	}

	public static void port_setup_bc(SerialPort sp) {
		try {
			output_bc = new PrintWriter(sp.getOutputStream());
			serialPort_bc = sp;
			try {
				Thread.sleep(250);
				output_bc.print("<8888>DOSR,006*");
				output_bc.flush();
			} catch (InterruptedException ex) {
			} catch (NullPointerException ee) {
				JOptionPane.showMessageDialog(null, "Please select the ComPort!");
			}
			ReformatBuffer.current_state = "bc_dosr";
			
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
			Thread.sleep(500);
			output_bc.print("<8888>CVOP*");
			output_bc.flush();
			ReformatBuffer.current_state = "dg_bc_cvop";
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
				ReformatBuffer.current_state = "bc_afil";
			} catch (InterruptedException ex) {
			} catch (NullPointerException ee) {
				JOptionPane.showMessageDialog(null, "Please select the ComPort!");
			}
		} else{
			b_factor = 0;
			for (int i = 0; i < 3; i++) { 
				double temp_bfactor = (double) model.getValueAt(i, 5);
				DecimalFormat df = new DecimalFormat(double_format);  
				temp_bfactor = Double.valueOf(df.format(temp_bfactor));
				b_factor = b_factor+temp_bfactor;
			}			
			b_factor = ((b_factor) / 3);
			burette_factor.setText("Burette Factor : " + String.format(str_format, b_factor));
			btn_test_buretter_calibration.setEnabled(true);
			btn_update_buretter_factor.setEnabled(true);
		}
	}

	public static void bc_afill_ok_received() {
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

	public static void bc_afill_end_received() {
		exec_bc_fill.shutdown();
		input_popup();
	}
	
	public static void input_popup() {
		String result = (String) JOptionPane.showInputDialog(frame, "Enter the weight W1 (Empty Weight)",
				"Enter W1", JOptionPane.PLAIN_MESSAGE, null, null, "");
		try {
			double w1 = Double.parseDouble(result);
			model.setValueAt(w1, row_cnt, 2);
			model.fireTableDataChanged();
//			if (row_cnt == 0 || row_cnt == 1 || row_cnt == 2) {
//				dose = 5;
//			}
//			if (row_cnt == 3 || row_cnt == 4 || row_cnt == 5) {
//				dose = 10;
//			}
//			if (row_cnt == 6 || row_cnt == 7 || row_cnt == 8) {
//				dose = 15;
//			}
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
			ReformatBuffer.current_state = "bc_dose";
		} catch (InterruptedException ex) {
		} catch (NullPointerException ee) {
			JOptionPane.showMessageDialog(null, "Please select the ComPort!");
		}
	}

	public static void bc_dose_ok_received() {
		dose_counter = 0;
		exec_bc_dose = Executors.newSingleThreadScheduledExecutor();
		exec_bc_dose.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (dose_counter < dose) {
					dose_counter = dose_counter + ((6 / 60.0) / 10);
					vol_dose.setText("Volume Dosed : " + String.format("%.1f", dose_counter) + " mL");
				} else {
					dose_counter = 0;
					exec_bc_dose.shutdown();
					try {
						Thread.sleep(300);
						output_bc.print("<8888>ESCP*");
						output_bc.flush();
						ReformatBuffer.current_state = "bc_stpm";
					} catch (NullPointerException ex) {
						JOptionPane.showMessageDialog(null, "Please connect to ComPort!");
					} catch (InterruptedException jb) {
					}
				}
			}
		}, 0, 100, TimeUnit.MILLISECONDS);
	}

	public static void bc_dosr_ok_received() {
		JOptionPane.showMessageDialog(null, "Dose Rate at 6 ml/min");
	}
	
	

	public static void bc_stpm_ok_received() {
		String result = (String) JOptionPane.showInputDialog(frame, "Enter the weight W2 (Weight with Water)",
				"Enter W2", JOptionPane.PLAIN_MESSAGE, null, null, "");
		try {
			DecimalFormat df = new DecimalFormat(double_format);  

			double w2 = Double.parseDouble(result);
			w2 = Double.valueOf(df.format(w2));
			model.setValueAt(w2, row_cnt, 3);
			
			Double w3 = w2 - Double.parseDouble(model.getValueAt(row_cnt, 2).toString());
			w3 = Double.valueOf(df.format(w3));
			model.setValueAt(w3, row_cnt, 4);
			
			Double temp_burette_factor = (w3 / Double.parseDouble(model.getValueAt(row_cnt, 1).toString()));
			temp_burette_factor = Double.valueOf(df.format(temp_burette_factor));
			model.setValueAt(temp_burette_factor, row_cnt, 5);			
			model.fireTableDataChanged();
			
			row_cnt++;
			if(row_cnt<=3)
				send_cvop();
		} catch (NullPointerException ne) {
			JOptionPane.showMessageDialog(null, "Please enter a value!");
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Please enter a valid value!");
		}
			if(row_cnt<3)
				send_afil();
	}

	public static void update_burette_factor() {
		System.out.println("NEW BF = "+ String.format(str_format, b_factor));
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		try {
			String sql = null;
			String update_data = "";
			sql = "UPDATE config_param SET cnfg_param_value = ? WHERE cnfg_param_group =?  and cnfg_param_name = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, String.format(str_format, b_factor));
			ps.setString(2, "buretteFactor");
			ps.setString(3, "buretteFactor");
			ps.executeUpdate();
			JOptionPane.showMessageDialog(null, "Burette Factor Updated Successfully!");
		} catch (SQLException e1) {
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e1) {}
		}
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), menubar.user_name, "Burette Factor updated to "+String.format("%.2f", b_factor));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}

	@SuppressWarnings("removal")
	public static void initialize() {
		frame.getContentPane().invalidate();
		frame.getContentPane().validate();
		frame.getContentPane().repaint();
		four_column();

		
		legend_w1 = new JLabel("W1 - Weight of Empty Beaker");
		legend_w1.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.01 * wid)));
		legend_w1.setBounds((int) Math.round(0.12 * wid), (int) Math.round(0.45 * hei), (int) Math.round(0.35 * wid),
				(int) Math.round(0.0428 * hei));
		frame.getContentPane().add(legend_w1);
		
		legend_w2 = new JLabel("W2 - Weight of Beaker with dosed Water");
		legend_w2.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.01 * wid)));
		legend_w2.setBounds((int) Math.round(0.12 * wid), (int) Math.round(0.5 * hei), (int) Math.round(0.35 * wid),
				(int) Math.round(0.0428 * hei));
		frame.getContentPane().add(legend_w2);
		
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

		burette_factor = new JLabel("Burette Factor : ");
		burette_factor.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.012 * wid)));
		burette_factor.setBounds((int) Math.round(0.55 * wid), (int) Math.round(0.04 * hei),
				(int) Math.round(0.2 * wid), (int) Math.round(0.0428 * hei));
		frame.getContentPane().add(burette_factor);
		
		radioButton_5ml=new JRadioButton("5 mL");    
		radioButton_5ml.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.012 * wid)));
		radioButton_5ml.setBounds((int) Math.round(0.75 * wid), (int) Math.round(0.04 * hei),
				(int) Math.round(0.05 * wid), (int) Math.round(0.0428 * hei));
		radioButton_5ml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//JOptionPane.showMessageDialog(null, "Selected 5 mL");
				dose = 5;
				for(int row_count =0; row_count<3; row_count++) {
					model.setValueAt("5", row_count, 1);			
					model.fireTableDataChanged();
				}
			}
		});
		
		radioButton_10ml=new JRadioButton("10 mL");   
		radioButton_10ml.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.012 * wid)));
		radioButton_10ml.setBounds((int) Math.round(0.825 * wid), (int) Math.round(0.04 * hei),
				(int) Math.round(0.05 * wid), (int) Math.round(0.0428 * hei));
		radioButton_10ml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//JOptionPane.showMessageDialog(null, "Selected 10 mL");
				dose = 10;
				for(int row_count =0; row_count<3; row_count++) {
					model.setValueAt("10", row_count, 1);			
					model.fireTableDataChanged();
				}
			}
		});
		
		radioButton_15ml=new JRadioButton("15 mL");   
		radioButton_15ml.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.012 * wid)));
		radioButton_15ml.setBounds((int) Math.round(0.9 * wid), (int) Math.round(0.04 * hei),
				(int) Math.round(0.1 * wid), (int) Math.round(0.0428 * hei)); 
		radioButton_15ml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//JOptionPane.showMessageDialog(null, "Selected 15 mL");
				dose = 15;
				for(int row_count =0; row_count<3; row_count++) {
					model.setValueAt("15", row_count, 1);			
					model.fireTableDataChanged();
				}
			}
		});
		
		ButtonGroup bg=new ButtonGroup();    
		bg.add(radioButton_5ml);
		bg.add(radioButton_10ml); 
		bg.add(radioButton_15ml); 
		
		radioButton_5ml.setSelected(true);
		
		
		frame.getContentPane().add(radioButton_5ml);
		frame.getContentPane().add(radioButton_10ml);
		frame.getContentPane().add(radioButton_15ml);


		JButton btn_start = new JButton("Start >>>");
		btn_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				radioButton_5ml.setEnabled(false);
				radioButton_10ml.setEnabled(false);
				radioButton_15ml.setEnabled(false);
				btn_start.setEnabled(false);
				send_cvop();
			}
		});
		btn_start.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.009 * wid)));
		btn_start.setBounds((int) Math.round(0.013 * wid), (int) Math.round(0.88 * hei), (int) Math.round(0.0976 * wid),
				(int) Math.round(0.0428 * hei));
		frame.getContentPane().add(btn_start);

		btn_update_buretter_factor = new JButton("Update Burette Correction Factor");
		btn_update_buretter_factor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update_burette_factor();
			}
		});
		btn_update_buretter_factor.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.009 * wid)));
		btn_update_buretter_factor.setBounds((int) Math.round(0.35 * wid), (int) Math.round(0.88 * hei),
				(int) Math.round(0.22 * wid), (int) Math.round(0.0428 * hei));
		frame.getContentPane().add(btn_update_buretter_factor);
		btn_update_buretter_factor.setEnabled(false);

		btn_test_buretter_calibration = new JButton("Test Burette Calibration");
		btn_test_buretter_calibration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] aa = {};
				
				if (serialPort_bc != null) {
					try {
						Thread.sleep(200);
						output_bc.print("<8888>ESCP*");
						output_bc.flush();
					} catch (InterruptedException jkb) {}
				}
				burette_calibration_test.main(aa);
				burette_calibration_test.port_setup_bct(serialPort_bc);
				frame.dispose();
				frame = new JFrame();
				p = new JPanel();
				p.revalidate();
				p.repaint();
			}
		});
		btn_test_buretter_calibration.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.009 * wid)));
		btn_test_buretter_calibration.setBounds((int) Math.round(0.615 * wid), (int) Math.round(0.88 * hei),
				(int) Math.round(0.22 * wid), (int) Math.round(0.0428 * hei));
		frame.getContentPane().add(btn_test_buretter_calibration);
		get_data();
	}

	public static void add_row_to_four_column( int r, String v1, String v2, String v3, String v4, String v5) {
		model.addRow(new Object[0]);
		model.setValueAt(r + 1, r, 0);
		model.setValueAt(v1, r, 1);
		model.setValueAt(v2, r, 2);
		model.setValueAt(v3, r, 3);
		model.setValueAt(v4, r, 4);
		model.setValueAt(v5, r, 5);
		model.fireTableDataChanged();
	}

	public static void four_column() {
		scrollPane = new JScrollPane();
		scrollPane.setBounds((int) Math.round(0.013 * wid), (int) Math.round(0.1 * hei), (int) Math.round(0.95 * wid),
				(int) Math.round(0.294 * hei));
		frame.getContentPane().add(scrollPane);
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
			@Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
		};
		table1.setModel(model);
		table1.setDefaultEditor(Object.class, null);
		model.addColumn("Trials");
		model.addColumn("Volume");
		model.addColumn("W1");
		model.addColumn("W2");
		model.addColumn("W2-W1");
		model.addColumn("Burrete Factor");

		table1.setRowHeight((int) Math.round(0.076 * hei));

		JTableHeader header = table1.getTableHeader();
		header.setPreferredSize(new Dimension((int) Math.round(0.0612 * hei), (int) Math.round(0.0612 * hei)));
		header.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.008 * wid)));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table1.setDefaultRenderer(String.class, centerRenderer);
		header.setDefaultRenderer(centerRenderer);

		table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table1.getColumnModel().getColumn(0).setPreferredWidth((int) Math.round(0.0651 * wid));
		table1.getColumnModel().getColumn(1).setPreferredWidth((int) Math.round(0.1302 * wid));
		table1.getColumnModel().getColumn(2).setPreferredWidth((int) Math.round(0.1953 * wid));
		table1.getColumnModel().getColumn(3).setPreferredWidth((int) Math.round(0.1953 * wid));
		table1.getColumnModel().getColumn(4).setPreferredWidth((int) Math.round(0.24 * wid));
		table1.getColumnModel().getColumn(5).setPreferredWidth((int) Math.round(0.122 * wid));
		table1.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table1.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		table1.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		table1.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		table1.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		table1.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

		scrollPane.setViewportView(table1);

		add_row_to_four_column( 0, "5", "", "", "","");
		add_row_to_four_column( 1, "5", "", "", "","");
		add_row_to_four_column( 2, "5", "", "", "","");
	
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
		frame.getContentPane().add(new burette_calibration());
		frame.setResizable(true);
		frame.setVisible(true);
		frame.repaint();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Burette Calibration");
		ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
		frame.setIconImage(img.getImage());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
//					try {
//						Thread.sleep(500);
//						output_bc.print("<8888>ESCP*");
//						output_bc.flush();
//						Thread.sleep(200);
//					} catch (NullPointerException ne) {
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
				try {
					serialPort_bc.closePort();
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
	
	public static void get_data() {
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql;
		sql = "SELECT * FROM config_param WHERE cnfg_param_group = 'buretteFactor' and cnfg_param_name = 'buretteFactor'";
		try {
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			raw_burette_factor = rs.getString("cnfg_param_value");
			burette_factor.setText("Burette Factor : "+raw_burette_factor);	
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, e1);
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e1) {
			}
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
}

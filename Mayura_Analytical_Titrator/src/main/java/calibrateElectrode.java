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

import com.itextpdf.text.DocumentException;

public class calibrateElectrode extends JPanel {
	static JFrame frame = new JFrame();
	static DefaultTableModel model;

	static String user_name = "";
	static JLabel text_mV, text_ph,previous_value;
	static JButton button_calibrate, button_enterpH, button_print;
	static int int_temp_mv ;
	static int wid = 0, hei = 0;
	static boolean stop_updating = false; 
	static int e_calib = 0;
	static JTable table1 = new JTable();
	static JScrollPane scrollPane, scrollPane1;

	static int current_ph_row = 1;
	static boolean update_table_pH = true;
	static DecimalFormat df = new DecimalFormat("0.000");

	
	static JPanel p = new JPanel();
	public calibrateElectrode() {
		setLayout(null);
		initialize();
	}

	@SuppressWarnings("removal")
	public static void initialize() {
		frame.getContentPane().invalidate();
		frame.getContentPane().validate();
		frame.getContentPane().repaint();
		
		test_four_column();

		button_calibrate = new JButton("Calibrate");
		button_calibrate.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.01 * wid)));
		button_calibrate.setBounds((int) Math.round(0.01 * wid), (int) Math.round(0.19 * hei),
				(int) Math.round(0.08 * wid), (int) Math.round(0.04 * hei));

		frame.getContentPane().add(button_calibrate);
		button_calibrate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				double temp_pH = Double.parseDouble(model.getValueAt(current_ph_row, 1).toString()) - Double.parseDouble(model.getValueAt(current_ph_row, 0).toString());
				DecimalFormat df = new DecimalFormat("#.#");
			    String tempPh = df.format(temp_pH);
				
				if(current_ph_row == 1) {
					model.setValueAt(tempPh, current_ph_row, 2);		
					model.fireTableDataChanged();
					current_ph_row = 0;
					input_popup("Enter pH value between 7 - 14");
				}
				else if(current_ph_row == 0) {
					model.setValueAt(tempPh, current_ph_row, 2);		
					model.fireTableDataChanged();
					current_ph_row = 2;
					input_popup("Enter pH value between 0 - 7");
				}
				else if(current_ph_row == 2) {
					model.setValueAt(tempPh, current_ph_row, 2);		
					model.fireTableDataChanged();
					button_calibrate.setEnabled(false);
					button_print.setEnabled(true);
					try {
						audit_log_push.push_to_audit(get_date(), get_time(), menubar.user_name, "Electrode Calibrated");
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					current_ph_row = 3;
					update_table_pH = false;
					JOptionPane.showMessageDialog(null, "Electrode Calibration Complete");

				}
			}
		});
		

		button_print = new JButton("Print");
		button_print.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.01 * wid)));
		button_print.setBounds((int) Math.round(0.2 * wid), (int) Math.round(0.19 * hei),
				(int) Math.round(0.08 * wid), (int) Math.round(0.04 * hei));

		frame.getContentPane().add(button_print);
		button_print.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				print_electrode_calibration_report();
			}
		});
		button_print.setEnabled(false);

	}
	
	public static void print_electrode_calibration_report() {

		String company_details = "";
		String parameter = "", header = "", value = "", trials = "", result = "", rsd = "", instrument_id = "",
				analyzed_by = "",result_kff="";
		trials = model.getValueAt(0, 0).toString()+","+model.getValueAt(0, 1).toString()+","+model.getValueAt(0, 2).toString()+":";
		trials = trials+model.getValueAt(1, 0).toString()+","+model.getValueAt(1, 1).toString()+","+model.getValueAt(1, 2).toString()+":";
		trials = trials+model.getValueAt(2, 0).toString()+","+model.getValueAt(2, 1).toString()+","+model.getValueAt(2, 2).toString();


		company_details = get_company_details();
		String[] company_arr = company_details.split(">");
		parameter = "Electrode Calibration Report,Date: " + get_date() + ",Time: " + get_time() + ",Instrument ID: "
				+ company_arr[0] + ",User Name: " + menubar.user_name;
		header = "Expected pH ,Actual pH, Correction";

		analyzed_by = menubar.user_name;
		instrument_id = company_arr[0];

		String company_details_temp = company_arr[1] + ">" + company_arr[2];
		try {
			report.generate_report_electrode_calib(company_details_temp, parameter, header, trials, analyzed_by,
					instrument_id);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
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
	
	public static void input_popup(String question) {
		String result = (String) JOptionPane.showInputDialog(frame, question,
				"Enter W1", JOptionPane.PLAIN_MESSAGE, null, null, "");
		try {
			float pH = Float.parseFloat(result);

			if(question.contains("14")) {
				if(pH <= 14 && pH >= 7) {
					model.setValueAt(pH, current_ph_row, 0);
					model.fireTableDataChanged();
				}
				else {
					input_popup(question);
				}
			}
			else if(question.contains("0")){
				if(pH <= 7 && pH >= 0) {
					model.setValueAt(pH, current_ph_row, 0);
					model.fireTableDataChanged();
				}
				else {
					input_popup(question);
				}
			}
			
		} catch (NullPointerException ne) {
			JOptionPane.showMessageDialog(null, "Please enter a value!");
			input_popup(question);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Please enter a valid value!");
			input_popup(question);
		}
	}
	
	public static void test_four_column() {
		scrollPane1 = new JScrollPane();
		scrollPane1.setBounds((int) Math.round(0.01 * wid), (int) Math.round(0.02 * hei),
				(int) Math.round(0.272* wid), (int) Math.round(0.15 * hei));
		frame.getContentPane().add(scrollPane1);
		table1 = new JTable();
		table1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.01 * wid)));

		model = new DefaultTableModel() {
			public Class<?> getColumnClass(int column) {
				switch (column) {

				case 0:
					return Integer.class;
				case 1:
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
		model.addColumn("pH");
		model.addColumn("Actual ph");
		model.addColumn("Correction");

		table1.setRowHeight((int) Math.round(0.038 * hei));

		JTableHeader header = table1.getTableHeader();
		header.setPreferredSize(new Dimension((int) Math.round(0.03 * hei), (int) Math.round(0.03 * hei)));
		header.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.009765 * wid)));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table1.setDefaultRenderer(String.class, centerRenderer);
		header.setDefaultRenderer(centerRenderer);

		table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table1.getColumnModel().getColumn(0).setPreferredWidth((int) Math.round(0.125 * wid));
		table1.getColumnModel().getColumn(1).setPreferredWidth((int) Math.round(0.072 * wid));
		table1.getColumnModel().getColumn(2).setPreferredWidth((int) Math.round(0.072 * wid));

		table1.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table1.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		table1.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);


		scrollPane1.setViewportView(table1);
		

		add_row_to_four_column(0, "ph greater than 7", "","");
		add_row_to_four_column(1, "7", "","");
		add_row_to_four_column(2, "ph lesser than 7", "","");

	}
	
	
	public static void add_row_to_four_column(int r, String v1, String v2, String v3) {
		model.addRow(new Object[0]);
		model.setValueAt(v1, r, 0);
		model.setValueAt(v2, r, 1);
		model.setValueAt(v3, r, 2);
		model.fireTableDataChanged();
	}
	
	public static void update_calibrate_mv(String msg) {
		
		if (update_table_pH == true){
			
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
			
			if(msg.contains("N")) {
				int_temp_mv = -int_temp_mv;
			}
			
			float current_ph = (7-((float)(int_temp_mv) / 54));
			model.setValueAt(Math.round(Math.abs(current_ph) * 10.0) / 10.0, current_ph_row, 1);		
			model.fireTableDataChanged();
		}
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
		current_ph_row = 1;
		update_table_pH = true;

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
		frame.getContentPane().add(new calibrateElectrode());
		frame.setLocationRelativeTo(null);

		frame.setResizable(true);
		frame.setVisible(true);
		frame.repaint();
		frame.setTitle("Calibrate Electrode");
		ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
		frame.setIconImage(img.getImage());

		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				ReformatBuffer.current_exp = "main";
				frame.dispose();
				frame = new JFrame();
				p = new JPanel();
				p.revalidate();
				p.repaint();
			}
		});
		//JOptionPane.showMessageDialog(null, "Place the electrode in pH 7 solution");

	}

}

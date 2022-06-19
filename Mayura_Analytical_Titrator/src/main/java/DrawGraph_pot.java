package main.java;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.IllegalFormatConversionException;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.AbstractCategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.FloatDimension;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import com.fazecast.jSerialComm.SerialPort;

public class DrawGraph_pot extends JPanel implements ItemListener {

	static int hei, wid, j = 0, temp_j = 0, row = 0, size = 0, end_point_no = 0, typ, ee = 0, m1, d_flag, end_count = 0,
			i, thershold = 200, factor = 1, graph_mul1 = 1, data_array_size = 0, balance = 0, cur_trial = 1,
			trial_cnt = 0, report_saved = 0, failed_count = 0, current_cloumn_count = 0, formula_no, no_of_trials;
	final int PAD = 20;

	static double end_line = 0, total_points = 0, k = 0, ky, delta = 0.6, t, k1, k2, c2, newdata1, diff0, diff1, diff2,
			slope, corres_fact, ep_continue1, ep_continue2, ep_continue3, dossage_speed, fill, dose, predose,
			moisture = 0, normality = 0, sample_weight = 0, end_point1 = 0, end_point2 = 0, end_point3 = 0,
			titrant_normality = 0, bvolume = 0, res_unit = 0, user_vol2 = 0, pre_dose, stir_time, max_vol, blank_vol,
			burette_factor, threshold_val, filter_val, doserate_val, factor1, factor2, factor3, factor4, ep_select,
			tendency, result_unit, sop_val, end_point_1 = 0, end_point_2 = 0, end_point_3 = 0, new_blank_volume = 0,
			filter = 2;

	static boolean draw_graph_state = true, update_front_end = false, select_column = false,
			blank_run_conducted = false, blank_volume_running = false, trials_completed = false, got_mg = false,
			refreshing = false, continue_data1 = false, res_flag, flag_stop = true, experiment_started = false,
			afill_first = false;

	static String dos_speed, metd_name, method_name, method_data, ar, batch, sample_name, normality_val, moisture_val,
			report_name, titrant_name, dg_current_process = "", math, math2, math3, db_report_name = "",
			db_parameters = "", db_details = "", db_graph_x = "", db_graph_y = "", db_header = "", db_trial_data = "",
			db_results = "", user_name = "", user_permissions = "", db_remarks = "No remarks", variables_string = "",
			result_timings = "";

	static float dose_speed = 0, popup_blank_weight = 0, blank_volume = 0;;

	int[] data = { 25, 60, 42, 75 };
	static double[] d;
	static double[] cur_val = new double[20];
	static double[] end_point_mv = new double[2000];
	static double[] end_position = new double[2000];
	static double[] End_Point = new double[2000];
	static double[] slp_diff = new double[2000];
	static double[] diff = new double[20];
	static double[] data1;

	static FileWriter fw;

	static JFrame frame1 = new JFrame();
	static JFrame frame2 = new JFrame();
	static JPanel p = new JPanel();
	static JPanel p_formula, p_formula2, p_formula3;
	static JTable table2 = new JTable();
	static JTable table1, table11;
	static DefaultTableModel method_model;
	static JRadioButton b, b1;
	static JTextField tf_threshold, tf_filter, textField;;
	static JButton button_dvdl, button_exit, button_generate_result, viewReport, button_saveReport, button_stop,
			button_continue, button_start, button_update_blank_vol, blankRun, button_refresh;
	static JLabel mv_display_pot, vol_filled, vol_pre_dosed, vol_dosed, l_formula, l_formula2, l_formula3,
			formula_header, info;;
	static JTextArea display;

	private static final XYSeries series = new XYSeries("mL,mV");
	static DefaultTableModel model, model2;

	static SerialPort sp1;
	static ChartPanel chartPanel;
	static JFreeChart chart;
	static TeXFormula formula, formula2, formula3;
	static TeXIcon ti_formula, ti_formula2, ti_formula3;
	static BufferedImage b_formula, b_formula2, b_formula3;
	static XYPlot plot, plot1, plot2;
	
	static BufferedReader br1;

	static PrintWriter output_dg;

	static ScheduledExecutorService exec_dg,exec_dg_predose;
	
	static ArrayList<String> data_X = new ArrayList<String>();
	static ArrayList<String> data_Y = new ArrayList<String>();
	static String[] trial_timings = new String[5];
	static Stack<String> threshold_array = new Stack<String>();
	static int[] int_arr = new int[5000];
	static ArrayList<Double> data_array = new ArrayList<Double>();
	public static String variables[] = new String[17];


	@SuppressWarnings("removal")
	public static void initialize() {
		frame1.getContentPane().invalidate();
		frame1.getContentPane().validate();
		frame1.getContentPane().repaint();
	}

	public static void update_mv_pot(String msg) {
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
		int int_mv_val = Integer.parseInt(mv_val_str);
		double double_mv_val = Double.parseDouble(mv_val_str);

		if (msg.contains("N"))
			int_mv_val = -int_mv_val;

		mv_display_pot.setText(int_mv_val + " mV");

		update_front_end = true;

		if (dg_current_process.matches("blank_run_started_dose") || dg_current_process.matches("trial_started_dose")) {
			double temp_val;
			if (data_array.size() < 2) {
				temp_val = double_mv_val;
			} else {
				temp_val = (data_array.get(data_array.size() - 1) * 0.5) + (double_mv_val * 0.5);
			}
			data_array.add(temp_val);
			float f = (float) dose;

			series.addOrUpdate(f, int_mv_val);

			DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
			String date_time = dateFormat2.format(new Date()).toString();

//			try {
//				 fw_temp.append(int_mv_val+"------------"+date_time+"\n");
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}

		}
	}

	public static void port_setup(SerialPort sp) {
		output_dg = new PrintWriter(sp.getOutputStream());
		sp1 = sp;
		int dr = (int) doserate_val;
		// System.out.println("Dosage rate = " + dr);
		if (dr == 0) {
			// System.out.println("Inside dr = 0.5555555555555555");
			try {
				Thread.sleep(500);
				output_dg.print("<8888>DOSR,017*");
				output_dg.flush();
			} catch (InterruptedException ex) {
			} catch (NullPointerException ee) {
				JOptionPane.showMessageDialog(null, "Please select the ComPort!");
			}
			ReformatBuffer.current_state = "dg_pot_dosr";
		} else {
			try {
				Thread.sleep(500);
				String s = String.format("%03d", dr);
				// System.out.println("Dosage rate 2 = " + s);
				output_dg.print("<8888>DOSR," + s + "*");
				output_dg.flush();
				ReformatBuffer.current_state = "dg_pot_dosr";
			} catch (InterruptedException ex) {
			} catch (NullPointerException ee) {
				JOptionPane.showMessageDialog(null, "Please select the ComPort!");
			}
		}
	}

	public static void dosr_ok_recieved() {
		System.out.println("Dose rate ok recieved");
		JOptionPane.showMessageDialog(null, "Equipment Ready!");
	}

	public static void send_cvop() {
		System.out.println("Inside send CVOP");
		try {
			Thread.sleep(500);
			output_dg.print("<8888>CVOP*");
			output_dg.flush();
			ReformatBuffer.current_state = "dg_pot_cvop";
		} catch (InterruptedException ex) {
		} catch (NullPointerException ee) {
			JOptionPane.showMessageDialog(null, "Please select the ComPort!");
		}
	}

	public static void cvop_ok_recieved() {
		if (dg_current_process.matches("blank_run_started")) {
			send_afil();
		}
		if (dg_current_process.matches("trial_started")) {
			System.out.println("AFILL Trial Started Line 321 cvop ok recieved");
			send_afil();
		}
	}

	public static void send_stop() {
		dg_current_process = "";
		System.out.println("Inside send stop");

		try {
			Thread.sleep(500);
			output_dg.print("<8888>STOP*");
			output_dg.flush();
			ReformatBuffer.current_state = "dg_pot_stop";
		} catch (InterruptedException ex) {
		} catch (NullPointerException ee) {
			JOptionPane.showMessageDialog(null, "Please select the ComPort!");
		}
		got_mg = false;
	}

	public static void send_afil() {
		System.out.println("Inside send AFIL method");

		try {
			Thread.sleep(500);
			output_dg.print("<8888>AFIL*");
			output_dg.flush();
			ReformatBuffer.current_state = "dg_pot_afil";
		} catch (InterruptedException ex) {
		} catch (NullPointerException ee) {
			JOptionPane.showMessageDialog(null, "Please select the ComPort!");
		}
	}

	public static void afil_ok_recieved() {
		if (dg_current_process.matches("blank_run_started") || dg_current_process.matches("blank_run_started_dose")) {
			dg_current_process = "blank_run_started_afill";
		} else if (dg_current_process.matches("trial_started") || dg_current_process.matches("trial_started_dose")) {
			dg_current_process = "trial_started_afill";
		} else if (dg_current_process.matches("pre_dose_started")) {
			dg_current_process = "pre_dose_started";
		}

		exec_dg = Executors.newSingleThreadScheduledExecutor();
		exec_dg.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				fill = fill + ((20.0 / 60.0) / 10.0);

				vol_filled.setText("Volume filled : " + String.format("%.3f", fill) + "mL");

			}
		}, 0, 100, TimeUnit.MILLISECONDS);
	}

	public static void afil_end_recieved() {
		exec_dg.shutdown();
		if (got_mg == false)
			get_mg();

		if (afill_first == false) {
			afill_first = true;
			open_timer();
			fill = 0;
		} else if (dg_current_process.matches("trial_started_afill")) {
			send_dose_command();
		} else if (dg_current_process.matches("pre_dose") || dg_current_process.matches("pre_dose_started")) {
			send_pre_dose_command();
		} else {
			send_dose_command();
		}

		got_mg = true;
	}

	public static void open_timer() {
		if ((int) stir_time > 0) {
			String aa[] = { String.valueOf((int) stir_time), "pot" };
			popup_stirtime.main(aa);
		} else if (dg_current_process.matches("blank_run_started_afill")) {
			send_dose_command();
		} else if (dg_current_process.matches("trial_started_afill")) {
			if (pre_dose > 0) {
				send_pre_dose_command();
				dg_current_process = "pre_dose_started";
			} else {
				send_dose_command();
			}
		}

//		else if(dg_current_process.matches("experiment")) {
//			if (pre_dose > 0) {
//				dg_current_process = "pre_dose";
//				send_pre_dose_command();
//			}
//			else {
//				send_dose_command();
//			}
//		}
//		else {
//			send_dose_command();
//		}
	}

	public static void timer_completed() {
		if (dg_current_process.matches("blank_run_started_afill")) {
			send_dose_command();
		} else if (dg_current_process.matches("trial_started_afill")) {
			if (pre_dose > 0) {
				System.out.println("Timer completed : dg_CP = " + dg_current_process + " pre_dose = " + pre_dose);
				send_pre_dose_command();
				dg_current_process = "pre_dose_started";
			} else {
				send_dose_command();
			}
		}
	}

	public static void send_pre_dose_command() {

		try {
			Thread.sleep(500);
			output_dg.print("<8888>PRDS*");
			output_dg.flush();
			ReformatBuffer.current_state = "dg_pot_predose";
		} catch (InterruptedException ex) {
		} catch (NullPointerException ee) {
			JOptionPane.showMessageDialog(null, "Please select the ComPort!");
		}
	}

	public static void pre_dose_ok_recieved() {
		System.out.println("Predose OK");
		exec_dg_predose = Executors.newSingleThreadScheduledExecutor();
		exec_dg_predose.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				// do stuff
				// predose = predose+((16.0/60.0)/10.0);

				dose = dose + ((20.0 / 60.0) / 4);

				vol_pre_dosed.setText("Volume Pre-dosed : " + String.format("%.3f", dose) + "mL");

				if ((dose) >= pre_dose) {
					exec_dg_predose.shutdown();
					try {
						Thread.sleep(100);
						output_dg.print("<8888>STPM*");
						output_dg.flush();
						ReformatBuffer.current_state = "dg_pot_stmp_predose";
					} catch (InterruptedException ex) {
					} catch (NullPointerException ee) {
						JOptionPane.showMessageDialog(null, "Please select the ComPort!");
					}
				}
			}
		}, 0, 250, TimeUnit.MILLISECONDS);
	}

	public static void pre_dose_end_recieved() {
		System.out.println("AFILL Predose end recieved Line 480");
		send_afil();
	}

	public static void pre_dose_stmp_end_recieved() {
		String s = "";
		int dr = (int) doserate_val;
		if (dr == 0) {
			try {
				Thread.sleep(100);
				output_dg.print("<8888>DOSR,017*");
				output_dg.flush();
				ReformatBuffer.current_state = "dg_pot_pre_dose_then_dosr";

			} catch (InterruptedException ex) {
			} catch (NullPointerException ee) {
				JOptionPane.showMessageDialog(null, "Please select the ComPort!");
			}
		} else {
			try {
				Thread.sleep(100);
				s = String.format("%03d", dr);
				output_dg.print("<8888>DOSR," + s + "*");
				output_dg.flush();
				ReformatBuffer.current_state = "dg_pot_pre_dose_then_dosr";
			} catch (InterruptedException ex) {
			} catch (NullPointerException ee) {
				JOptionPane.showMessageDialog(null, "Please select the ComPort!");
			}
		}
		System.out.println(s);
	}

	public static void pre_dose_dosr_end_recieved() {
		try {
			Thread.sleep(500);
			output_dg.print("<8888>DOSE*");
			output_dg.flush();
			ReformatBuffer.current_state = "dg_pot_pre_dose_then_dose";
		} catch (InterruptedException ex) {
		} catch (NullPointerException ee) {
			JOptionPane.showMessageDialog(null, "Please select the ComPort!");
		}
		// dg_current_process = "experiment";
	}

	public static void send_dose_command() {
		try {
			Thread.sleep(500);
			output_dg.print("<8888>DOSE*");
			output_dg.flush();
			ReformatBuffer.current_state = "dg_pot_dose";
		} catch (InterruptedException ex) {
		} catch (NullPointerException ee) {
			JOptionPane.showMessageDialog(null, "Please select the ComPort!");
		}
	}

	public static void dose_ok_recieved() {
		System.out.println("Dose counter OK recieved");

		if (dg_current_process.matches("blank_run_started_afill")) {
			dg_current_process = "blank_run_started_dose";
		} else if (dg_current_process.matches("pre_dose_started")
				|| dg_current_process.matches("trial_started_afill")) {
			dg_current_process = "trial_started_dose";
		}

		exec_dg = Executors.newSingleThreadScheduledExecutor();
		exec_dg.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				System.out.println("Dose counter EXEC_DG1111 = "+dose);

				dose = dose + ((doserate_val / 60.0) / 4.0);

				update_front_end = false;
				vol_dosed.setText("Volume dosed : " + String.format("%.3f", dose) + "mL");

				if ((dose) > max_vol) {
					try {
						Thread.sleep(100);
						output_dg.print("<8888>STPM*");
						output_dg.flush();
						ReformatBuffer.current_state = "";
					} catch (InterruptedException ex) {
					} catch (NullPointerException ee) {
						JOptionPane.showMessageDialog(null, "Please select the ComPort!");
					}
					exec_dg.shutdown();
					JOptionPane.showMessageDialog(null, "Max Volume Reached!");
					dg_current_process = "";	
				}
			}
		}, 0, 250, TimeUnit.MILLISECONDS);
	}

	public static void dose_end_recieved() {
		exec_dg.shutdown();
		if (dg_current_process.matches("blank_run_started_dose")) {
			System.out.println("AFILL Trial Started dose_end_recieved Line 542 DOSE END");
			send_afil();
		} else if (dg_current_process.matches("trial_started_dose")) {
			System.out.println("AFILL Trial Started trial Line 545 Dose END");
			send_afil();
		}
	}

	public static void pot_home_dosr() {
		try {
			Thread.sleep(100);
			output_dg.print("<8888>ESCP*");
			output_dg.flush();
			ReformatBuffer.current_state = "dg_pot_home_escp";
		} catch (InterruptedException ex) {
		} catch (NullPointerException ee) {
			JOptionPane.showMessageDialog(null, "Please select the ComPort!");
		}
	}

	public static void pot_home_escp() {
		String aa[] = new String[1];
		aa[0] = "aa";
		menubar.main(aa);
		ReformatBuffer.current_exp = "main";
		menubar.enable_all(true);
		menubar.send_cvol_kfpot();
		menubar.frame1.setTitle("Mayura Analytical      Logged in as - " + user_name + "      Connected to Comport : "
				+ sp1.getDescriptivePortName());
		// sent_cvop = false;
		data_X.clear();
		data_Y.clear();
		db_report_name = "";
		db_parameters = "";
		db_details = "";
		db_graph_x = "";
		db_graph_y = "";
		db_header = "";
		db_trial_data = "";
		db_results = "";
		user_name = "";
		user_permissions = "";
		trial_cnt = 0;
		cur_trial = 1;
		data_array.clear();
		series.clear();

		fill = 0;
		dose = 0;
		predose = 0;
		afill_first = false;
		experiment_started = true;

			for (int x = 0; x < d.length; x++)
				d[x] = 0;
			cur_val = new double[20];
			end_point_mv = new double[2000];
			end_position = new double[2000];
			End_Point = new double[2000];
			slp_diff = new double[2000];
			diff = new double[20];
			for (int x = 0; x < data1.length; x++)
				data1[x] = 0;
			size = 0;
			end_line = 0;
			total_points = 0;
			end_point_no = 0;
			flag_stop = true;
			typ = 0;
			ee = 0;
			k = 0;
			m1 = 0;
			ky = 0;
			d_flag = 0;
			end_count = 0;
			delta = 0.6;
			t = 0;
			k1 = 0;
			k2 = 0;
			c2 = 0;
			newdata1 = 0;
			i = 0;
			graph_mul1 = 1;
			diff0 = 0;
			diff1 = 0;
			diff2 = 0;
			slope = 0;
			corres_fact = 0;
			ep_continue1 = 0;
			ep_continue2 = 0;
			ep_continue3 = 0;
			dossage_speed = 0;
			db_remarks = "No Remarks";
			threshold_array.clear();
			threshold_array = new Stack<String>();
		

		if (cur_trial != no_of_trials) {
			try {
				audit_log_push.push_to_audit(get_date(), get_time(), user_name,
						(cur_trial - 1) + " trial completed out of " + no_of_trials + " trial");
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name, "Returning to Home from Potentimetry");
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

	public static void update_blank_volume_method() {
		if (model2.getRowCount() > 0) {
			for (int i = model2.getRowCount() - 1; i > -1; i--) {
				model2.removeRow(i);
			}
		}

		bvolume = new_blank_volume;
		new_blank_volume = 0;
		method_model = new DefaultTableModel(
				new Object[][] { { "Metd Name", ":", metd_name, "" }, { "Pre dose", ":", variables[0], "ml" },
						{ "Stir time", ":", variables[1], "sec" }, { "Max vol", ":", variables[2], "ml" },
						{ "Dose Rate", ":", variables[7], "ml/min" }, { "Result Unit", ":", variables[16], "" },
						{ "E.P Select", ":", variables[13], "" }, { "Formula No	", ":", variables[14], "" },
						{ "Blk_Vol", ":", String.format("%.3f", bvolume), "" }, },
				new String[] { "Methods", "Parameters", "", "" });

		table11.setModel(method_model);
		method_model.fireTableDataChanged();

		try {

			table11.setRowHeight((int) Math.round(0.034 * hei));
		} catch (IllegalArgumentException kjhb) {
		}
		table11.setEnabled(false);
		table11.getColumnModel().getColumn(1).setPreferredWidth((int) Math.round(0.00651 * wid));
		table11.getColumnModel().getColumn(2).setPreferredWidth((int) Math.round(0.01953 * wid));

		db_details = db_details + "[ " + get_time() + " ]  Blank Run Started ,";
		db_details = db_details + "[ " + get_time() + " ]  New Blank Volume = " + String.format("%.3f", bvolume) + " ,";
		db_details = db_details + "[ " + get_time() + " ]  Blank Volume Updated ,";

		model2 = new DefaultTableModel(
				new Object[][] { { "Burette Factor", ":", variables[4], "" }, { "Trials", ":", variables[8], "" },
						{ "Filter", ":", "", "" }, { "Threshold", ":", "", "" }, { "factor1", ":", variables[9], "" },
						{ "factor2", ":", variables[10], "" }, { "factor3", ":", variables[11], "" },
						{ "factor4", ":", variables[12], "" }, { "Tendency", ":", variables[15], "" }, },
				new String[] { "Methods", "Parameters", "", "" });

		table2.setModel(model2);
		model2.fireTableDataChanged();

		table2.getColumnModel().getColumn(1).setPreferredWidth((int) Math.round(0.00651 * wid));
		table2.getColumnModel().getColumn(2).setPreferredWidth((int) Math.round(0.01953 * wid));
		try {
			table2.setRowHeight((int) Math.round(0.034 * hei));
		} catch (IllegalArgumentException ikju) {

		}

		tf_threshold.setText(variables[5]);
		tf_filter.setText(variables[6]);
		info.setText("( Threshold and filter values btw 1 and 20 )");

		String[] temp_ar = variables_string.split(",");
		String temp_update = "";

		for (int i = 0; i < temp_ar.length; i++) {
			if (i == 3) {
				temp_update = temp_update + "," + String.format("%.4f", bvolume);
			} else {
				if (i == 0) {
					temp_update = temp_update + temp_ar[i];
				} else {
					temp_update = temp_update + "," + temp_ar[i];
				}
			}
		}

		String[] temp_params = db_parameters.split(",");
		String temp_db_params = "";

		for (int i = 0; i < temp_params.length; i++) {
			if (i == 0) {
				temp_db_params = temp_db_params + temp_params[0];
			} else if (i == 7) {
				temp_db_params = temp_db_params + "," + String.format("%.4f", bvolume);
			} else {
				temp_db_params = temp_db_params + "," + temp_params[i];
			}
		}
		db_parameters = temp_db_params;

		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		try {
			String sql = null;
			sql = "UPDATE pot_method SET Value = ? WHERE Trial_name = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, temp_update);
			ps.setString(2, metd_name);
			ps.executeUpdate();
			JOptionPane.showMessageDialog(null, "Blank Vol Updated Successfully to method file!");
			update_result_scroll("\nBlank Volume Updated to Method File\n ");
			db_details = db_details + "[ " + get_time() + " ]  Blank Volume Updated to Method File ,";
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

	public static void update_data() {

		System.out.println("UUUPPPDDAATTEE  DATAA DB TRial DATAA : " + db_trial_data);

		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql;
		boolean present = false;
		try {
			sql = "UPDATE potentiometry SET details = ? ," + "graph_dataX = ? , " + "graph_dataY = ?, "
					+ "trial_data = ?, " + "parameters = ?, " + "results = ?, " + "remarks = ?"
					+ "WHERE report_name = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, db_details);
			ps.setString(2, db_graph_x);
			ps.setString(3, db_graph_y);
			ps.setString(4, db_trial_data);
			ps.setString(5, db_parameters);
			ps.setString(6, db_results);
			ps.setString(7, db_remarks);
			ps.setString(8, db_report_name);

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

	public static void formula1(boolean pass) {
		// res = normality
		System.out.println("Current Triasl = " + cur_trial);
		if (pass == true) {
			double result1 = (sample_weight * (1 - (moisture / 100)) * factor3 * factor4)
					/ ((end_point_1 - bvolume) * factor1 * factor2);
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_five_column(cur_trial - 1, String.format("%.3f", end_point_1), String.valueOf(sample_weight),
					String.valueOf(moisture), String.format("%.3f", result1));
			update_result_scroll("Trial " + cur_trial + " : normality = " + String.format("%.3f", result1) + "\n");

		} else {
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_five_column(cur_trial - 1, "NA", String.valueOf(sample_weight), String.valueOf(moisture), "NA");
			update_result_scroll("Trial " + cur_trial + " : Result = Failed\n");
		}
		if (cur_trial == no_of_trials && select_column == false) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}

			trials_completed = true;
			button_continue.setEnabled(false);
			report_saved = 1;

		} else if (cur_trial == no_of_trials) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}

			button_continue.setEnabled(false);
			report_saved = 1;
		}
		cur_trial++;
	}

	public static void formula1_non_select_column() {
		double res_normality = 0;
		ArrayList<Double> temp_res = new ArrayList<Double>();
		for (int i = 0; i < no_of_trials; i++) {
			if (!model.getValueAt(i, 4).toString().matches("NA")) {
				res_normality = res_normality + Double.parseDouble(model.getValueAt(i, 4).toString());
				temp_res.add(Double.parseDouble(model.getValueAt(i, 4).toString()));
			}
			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 4).toString() + ",";
			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 4).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			if (i == 0)
				db_trial_data = db_trial_data + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString();
			else
				db_trial_data = db_trial_data + ":" + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString();
			db_remarks = db_remarks + "," + threshold_array.get(i);
		}
		res_normality = res_normality / temp_res.size();
		double rsd_1 = SD(temp_res);
		update_result_scroll("\nAverage Normality = " + String.format("%.4f", res_normality) + "\n");
		db_details = db_details + "[ " + get_time() + " ]  Result = " + String.format("%.4f", res_normality) + ",";
		update_result_scroll("Normality RSD = " + String.format("%.4f", rsd_1) + " %\n");
		db_details = db_details + "[ " + get_time() + " ]  Result  RSD = " + String.format("%.4f", rsd_1) + ",";
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Normality: " + String.format("%.4f", res_normality) + ", RSD: " + String.format("%.4f", rsd_1));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		db_results = String.format("%.4f", res_normality) + "," + String.format("%.4f", rsd_1);

		for (int i = 0; i < data_X.size(); i++) {
			if (i == 0) {
				db_graph_x = db_graph_x + data_X.get(i);
				db_graph_y = db_graph_y + data_Y.get(i);
			} else {
				db_graph_x = db_graph_x + ":" + data_X.get(i);
				db_graph_y = db_graph_y + ":" + data_Y.get(i);
			}
		}
		update_data();
		blankRun.setEnabled(false);
		viewReport.setEnabled(true);
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Report - " + db_report_name + " Saved Successfully");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
		button_saveReport.setEnabled(false);
		button_generate_result.setEnabled(false);
	}

	public static void formula2(boolean pass) {
		// res=analyte
		if (pass == true) {
			double result1 = (end_point_1 * titrant_normality * factor1 * factor2 * res_unit)
					/ (sample_weight * factor3 * factor4);
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_six_column(cur_trial - 1, String.format("%.3f", end_point_1), String.valueOf(sample_weight),
					String.valueOf(titrant_normality), String.valueOf(moisture), String.format("%.3f", result1));
			update_result_scroll("Trial " + cur_trial + " : analyte = " + String.format("%.3f", result1) + "\n");

		} else {
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_six_column(cur_trial - 1, "NA", String.valueOf(sample_weight), String.valueOf(titrant_normality),
					String.valueOf(moisture), "NA");
			update_result_scroll("Trial " + cur_trial + " : Result = Failed\n");
		}
		if (cur_trial == no_of_trials && select_column == false) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			trials_completed = true;
			button_continue.setEnabled(false);
			report_saved = 1;

		} else if (cur_trial == no_of_trials) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			button_continue.setEnabled(false);
			report_saved = 1;
		}
		// else
		cur_trial++;
	}

	public static void formula2_non_select_column() {
		double res_normality = 0;
		ArrayList<Double> temp_res = new ArrayList<Double>();
		for (int i = 0; i < no_of_trials; i++) {
			if (!model.getValueAt(i, 5).toString().matches("NA")) {
				res_normality = res_normality + Double.parseDouble(model.getValueAt(i, 5).toString());
				temp_res.add(Double.parseDouble(model.getValueAt(i, 5).toString()));
			}
			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 5).toString() + ",";
			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 5).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			if (i == 0) {
				db_trial_data = db_trial_data + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString();
			} else {
				db_trial_data = db_trial_data + ":" + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString();
			}
			db_remarks = db_remarks + "," + threshold_array.get(i);

		}
		res_normality = res_normality / temp_res.size();
		double rsd_1 = SD(temp_res);
		update_result_scroll("\nAverage Analyte = " + String.format("%.4f", res_normality) + "\n");
		db_details = db_details + "[ " + get_time() + " ]  Result = " + String.format("%.4f", res_normality) + ",";
		update_result_scroll("Analyte RSD = " + String.format("%.4f", rsd_1) + " %\n");
		db_details = db_details + "[ " + get_time() + " ]  Result  RSD = " + String.format("%.4f", rsd_1) + ",";
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Analyte: " + String.format("%.4f", res_normality) + ", RSD: " + String.format("%.4f", rsd_1));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		db_results = String.format("%.4f", res_normality) + "," + String.format("%.4f", rsd_1);

		for (int i = 0; i < data_X.size(); i++) {
			if (i == 0) {
				db_graph_x = db_graph_x + data_X.get(i);
				db_graph_y = db_graph_y + data_Y.get(i);
			} else {
				db_graph_x = db_graph_x + ":" + data_X.get(i);
				db_graph_y = db_graph_y + ":" + data_Y.get(i);
			}
		}
		update_data();
		blankRun.setEnabled(false);
		viewReport.setEnabled(true);
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Report - " + db_report_name + " Saved Successfully");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
		button_saveReport.setEnabled(false);
		button_generate_result.setEnabled(false);
	}

	public static void formula3(boolean pass) {
		// res = analyte
		if (pass == true) {
			double result1 = ((end_point_1 - bvolume) * titrant_normality * factor1 * factor2 * res_unit)
					/ (sample_weight * (1 - (moisture / 100)) * factor3 * factor4);
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_six_column(cur_trial - 1, String.format("%.3f", end_point_1), String.valueOf(sample_weight),
					String.valueOf(titrant_normality), String.valueOf(moisture), String.format("%.3f", result1));
			update_result_scroll("Trial " + cur_trial + " : analyte = " + String.format("%.3f", result1) + "\n");

		} else {
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_six_column(cur_trial - 1, "NA", String.valueOf(sample_weight), String.valueOf(titrant_normality),
					String.valueOf(moisture), "NA");
			update_result_scroll("Trial " + cur_trial + " : Result = Failed\n");

		}
		if (cur_trial == no_of_trials && select_column == false) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			trials_completed = true;
			button_continue.setEnabled(false);
			report_saved = 1;
		} else if (cur_trial == no_of_trials) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			button_continue.setEnabled(false);
			report_saved = 1;
		}
		cur_trial++;
	}

	public static void formula3_non_select_column() {
		double res_normality = 0;
		ArrayList<Double> temp_res = new ArrayList<Double>();
		for (int i = 0; i < no_of_trials; i++) {
			if (!model.getValueAt(i, 5).toString().matches("NA")) {
				res_normality = res_normality + Double.parseDouble(model.getValueAt(i, 5).toString());
				temp_res.add(Double.parseDouble(model.getValueAt(i, 5).toString()));
			}
			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 5).toString() + ",";
			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 5).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			if (i == 0) {
				db_trial_data = db_trial_data + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString();
			} else {
				db_trial_data = db_trial_data + ":" + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString();
			}
			db_remarks = db_remarks + "," + threshold_array.get(i);

		}
		res_normality = res_normality / temp_res.size();
		double rsd_1 = SD(temp_res);
		update_result_scroll("\nAverage Analyte = " + String.format("%.4f", res_normality) + "\n");
		db_details = db_details + "[ " + get_time() + " ]  Result = " + String.format("%.4f", res_normality) + ",";
		update_result_scroll("Analyte RSD = " + String.format("%.4f", rsd_1) + " %\n");
		db_details = db_details + "[ " + get_time() + " ]  Result  RSD = " + String.format("%.4f", rsd_1) + ",";

		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Analyte: " + String.format("%.4f", res_normality) + ", RSD: " + String.format("%.4f", rsd_1));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		db_results = String.format("%.4f", res_normality) + "," + String.format("%.4f", rsd_1);

		for (int i = 0; i < data_X.size(); i++) {
			if (i == 0) {
				db_graph_x = db_graph_x + data_X.get(i);
				db_graph_y = db_graph_y + data_Y.get(i);
			} else {
				db_graph_x = db_graph_x + ":" + data_X.get(i);
				db_graph_y = db_graph_y + ":" + data_Y.get(i);
			}
		}
		update_data();
		blankRun.setEnabled(false);
		viewReport.setEnabled(true);
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Report - " + db_report_name + " Saved Successfully");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
		button_saveReport.setEnabled(false);
		button_generate_result.setEnabled(false);
	}

	public static void formula4(boolean pass) {
		// res = normality
		if (pass == true) {
			double result1 = (sample_weight * (1 - (moisture / 100)) * factor3 * factor4)
					/ ((end_point_2 - end_point_1) * factor1 * factor2);
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_six_column(cur_trial - 1, String.format("%.3f", end_point_1), String.format("%.3f", end_point_2),
					String.valueOf(sample_weight), String.valueOf(moisture), String.format("%.3f", result1));
			update_result_scroll("Trial " + cur_trial + " : normality = " + String.format("%.3f", result1) + "\n");

		} else {
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_six_column(cur_trial - 1, "NA", "NA", String.valueOf(sample_weight), String.valueOf(moisture),
					"NA");
			update_result_scroll("Trial " + cur_trial + " : Result = Failed\n");
		}
		if (cur_trial == no_of_trials && select_column == false) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			trials_completed = true;
			button_continue.setEnabled(false);
			report_saved = 1;
		} else if (cur_trial == no_of_trials) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			button_continue.setEnabled(false);
			report_saved = 1;
		}
		cur_trial++;
	}

	public static void formula4_non_select_column() {
		double res_normality = 0;
		ArrayList<Double> temp_res = new ArrayList<Double>();
		for (int i = 0; i < no_of_trials; i++) {
			if (!model.getValueAt(i, 5).toString().matches("NA")) {
				res_normality = res_normality + Double.parseDouble(model.getValueAt(i, 5).toString());
				temp_res.add(Double.parseDouble(model.getValueAt(i, 5).toString()));
			}
			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 5).toString() + ",";
			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 5).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			if (i == 0) {
				db_trial_data = db_trial_data + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString();
			} else {
				db_trial_data = db_trial_data + ":" + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString();
			}
			db_remarks = db_remarks + "," + threshold_array.get(i);

		}
		res_normality = res_normality / temp_res.size();
		double rsd_1 = SD(temp_res);
		update_result_scroll("\nAverage Normality = " + String.format("%.4f", res_normality) + "\n");
		db_details = db_details + "[ " + get_time() + " ]  Result = " + String.format("%.4f", res_normality) + ",";
		update_result_scroll("Analyte RSD = " + String.format("%.4f", rsd_1) + " %\n");
		db_details = db_details + "[ " + get_time() + " ]  Result  RSD = " + String.format("%.4f", rsd_1) + ",";

		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Normality: " + String.format("%.4f", res_normality) + ", RSD: " + String.format("%.4f", rsd_1));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		db_results = String.format("%.4f", res_normality) + "," + String.format("%.4f", rsd_1);

		for (int i = 0; i < data_X.size(); i++) {
			System.out.println("SIZEEEEEEEEEEEEEE = " + data_X.size() + "   iiiiiiiiii    = " + i);
			if (i == 0) {
				db_graph_x = db_graph_x + data_X.get(i);
				db_graph_y = db_graph_y + data_Y.get(i);
			} else {
				db_graph_x = db_graph_x + ":" + data_X.get(i);
				db_graph_y = db_graph_y + ":" + data_Y.get(i);
			}
		}
		update_data();
		blankRun.setEnabled(false);
		viewReport.setEnabled(true);
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Report - " + db_report_name + " Saved Successfully");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
		button_saveReport.setEnabled(false);
		button_generate_result.setEnabled(false);
	}

	public static void formula5(boolean pass) {
		// res = normality
		if (pass == true) {
			double result1 = ((end_point_2 - end_point_1) * titrant_normality * factor1 * factor2 * res_unit)
					/ (sample_weight * (1 - (moisture / 100)) * factor3 * factor4);
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_seven_column(cur_trial - 1, String.format("%.3f", end_point_1),
					String.format("%.3f", end_point_2), String.valueOf(sample_weight),
					String.valueOf(titrant_normality), String.valueOf(moisture), String.format("%.3f", result1));
			update_result_scroll("Trial " + cur_trial + " : normality = " + String.format("%.3f", result1) + "\n");

		} else {
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_seven_column(cur_trial - 1, "NA", "NA", String.valueOf(sample_weight),
					String.valueOf(titrant_normality), String.valueOf(moisture), "NA");
			update_result_scroll("Trial " + cur_trial + " : Result = Failed\n");

		}
		if (cur_trial == no_of_trials && select_column == false) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			trials_completed = true;
			button_continue.setEnabled(false);
			report_saved = 1;
		} else if (cur_trial == no_of_trials) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			button_continue.setEnabled(false);
			report_saved = 1;
		}
		cur_trial++;
	}

	public static void formula5_non_select_column() {
		double res_normality = 0;
		ArrayList<Double> temp_res = new ArrayList<Double>();
		for (int i = 0; i < no_of_trials; i++) {
			if (!model.getValueAt(i, 6).toString().matches("NA")) {
				res_normality = res_normality + Double.parseDouble(model.getValueAt(i, 6).toString());
				temp_res.add(Double.parseDouble(model.getValueAt(i, 6).toString()));
			}
			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 6).toString() + ",";
			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 6).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			if (i == 0) {
				db_trial_data = db_trial_data + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString();
			} else {
				db_trial_data = db_trial_data + ":" + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString();
			}
			db_remarks = db_remarks + "," + threshold_array.get(i);

		}
		res_normality = res_normality / temp_res.size();
		double rsd_1 = SD(temp_res);
		update_result_scroll("\nAverage Analyte = " + String.format("%.4f", res_normality) + "\n");
		db_details = db_details + "[ " + get_time() + " ]  Result = " + String.format("%.4f", res_normality) + ",";
		update_result_scroll("Analyte RSD = " + String.format("%.4f", rsd_1) + " %\n");
		db_details = db_details + "[ " + get_time() + " ]  Result  RSD = " + String.format("%.4f", rsd_1) + ",";

		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Analyte: " + String.format("%.4f", res_normality) + ", RSD: " + String.format("%.4f", rsd_1));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		db_results = String.format("%.4f", res_normality) + "," + String.format("%.4f", rsd_1);

		for (int i = 0; i < data_X.size(); i++) {
			System.out.println("SIZEEEEEEEEEEEEEE = " + data_X.size() + "   iiiiiiiiii    = " + i);
			if (i == 0) {
				db_graph_x = db_graph_x + data_X.get(i);
				db_graph_y = db_graph_y + data_Y.get(i);
			} else {
				db_graph_x = db_graph_x + ":" + data_X.get(i);
				db_graph_y = db_graph_y + ":" + data_Y.get(i);
			}
		}
		update_data();
		blankRun.setEnabled(false);
		viewReport.setEnabled(true);
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Report - " + db_report_name + " Saved Successfully");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
		button_saveReport.setEnabled(false);
		button_generate_result.setEnabled(false);
	}

	public static void formula6(boolean pass) {
		// analyte A and analyte B
		if (pass == true) {
			double result1 = (end_point_1 * titrant_normality * factor1 * res_unit) / (sample_weight * factor3);
			double result2 = ((end_point_2 - end_point_1) * titrant_normality * factor2 * res_unit)
					/ (sample_weight * factor4);
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			System.out.println("CUUUURRRRRRRRRR Triallllllll ==== " + cur_trial);
			add_row_to_eight_column(cur_trial - 1, String.format("%.3f", end_point_1),
					String.format("%.3f", end_point_2), String.valueOf(sample_weight),
					String.valueOf(titrant_normality), String.valueOf(moisture), String.format("%.3f", result1),
					String.format("%.3f", result2));
			update_result_scroll("Trial " + cur_trial + " : Analyte A = " + String.format("%.3f", result1) + "\n");
			update_result_scroll("Trial " + cur_trial + " : Analyte B = " + String.format("%.3f", result2) + "\n");

		} else {
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_eight_column(cur_trial - 1, "NA", "NA", String.valueOf(sample_weight),
					String.valueOf(titrant_normality), String.valueOf(moisture), "NA", "NA");
			update_result_scroll("Trial " + cur_trial + " : Result = Failed\n");

		}
		if (cur_trial == no_of_trials && select_column == false) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			trials_completed = true;
			button_continue.setEnabled(false);
			report_saved = 1;
		} else if (cur_trial == no_of_trials) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			button_continue.setEnabled(false);
			report_saved = 1;
		}
		cur_trial++;
	}

	public static void formula6_non_select_column() {
		double res_analyteA = 0, res_analyteB = 0;
		ArrayList<Double> temp_res = new ArrayList<Double>();
		ArrayList<Double> temp_resB = new ArrayList<Double>();

		for (int i = 0; i < no_of_trials; i++) {
			if (!model.getValueAt(i, 6).toString().matches("NA")) {
				res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 6).toString());
				res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 7).toString());

				temp_res.add(Double.parseDouble(model.getValueAt(i, 6).toString()));
				temp_resB.add(Double.parseDouble(model.getValueAt(i, 7).toString()));
			}

			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 6).toString() + ",";
			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 7).toString() + ",";
			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 6).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 7).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			if (i == 0) {
				db_trial_data = db_trial_data + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
						+ model.getValueAt(i, 7).toString();
			} else {
				db_trial_data = db_trial_data + ":" + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
						+ model.getValueAt(i, 7).toString();
			}
			db_remarks = db_remarks + "," + threshold_array.get(i);

		}

		res_analyteA = res_analyteA / temp_res.size();
		double rsd_1 = SD(temp_res);

		res_analyteB = res_analyteB / temp_res.size();
		double rsd_2 = SD(temp_resB);

		update_result_scroll("\nAverage AnalyteA = " + String.format("%.4f", res_analyteA) + "\n");
		update_result_scroll("Average AnalyteB = " + String.format("%.4f", res_analyteB) + "\n");

		db_details = db_details + "[ " + get_time() + " ]  Analyte A = " + String.format("%.4f", res_analyteA) + ",";
		update_result_scroll("Analyte RSD1 = " + String.format("%.4f", rsd_1) + " %\n");

		db_details = db_details + "[ " + get_time() + " ]  Analyte B = " + String.format("%.4f", res_analyteB) + ",";
		update_result_scroll("Analyte RSD2 = " + String.format("%.4f", rsd_2) + " %\n");

		db_details = db_details + "[ " + get_time() + " ]  Result  RSD 1 = " + String.format("%.4f", rsd_1) + ",";
		db_details = db_details + "[ " + get_time() + " ]  Result  RSD 2 = " + String.format("%.4f", rsd_2) + ",";

		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Analyte A: " + String.format("%.4f", res_analyteA) + ", RSD: " + String.format("%.4f", rsd_1));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Analyte B: " + String.format("%.4f", res_analyteB) + ", RSD: " + String.format("%.4f", rsd_2));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		db_results = String.format("%.4f", res_analyteA) + "," + String.format("%.4f", rsd_1) + ":"
				+ String.format("%.4f", res_analyteB) + "," + String.format("%.4f", rsd_2);

		for (int i = 0; i < data_X.size(); i++) {
			System.out.println("SIZEEEEEEEEEEEEEE = " + data_X.size() + "   iiiiiiiiii    = " + i);
			if (i == 0) {
				db_graph_x = db_graph_x + data_X.get(i);
				db_graph_y = db_graph_y + data_Y.get(i);
			} else {
				db_graph_x = db_graph_x + ":" + data_X.get(i);
				db_graph_y = db_graph_y + ":" + data_Y.get(i);
			}
		}
		update_data();
		blankRun.setEnabled(false);
		viewReport.setEnabled(true);
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Report - " + db_report_name + " Saved Successfully");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
		button_saveReport.setEnabled(false);
		button_generate_result.setEnabled(false);
	}

	public static void formula7(boolean pass) {
		// analyte A and analyte B and analyte C
		if (pass == true) {
			double result1 = (end_point_1 * titrant_normality * factor1 * factor2 * res_unit)
					/ (sample_weight * factor3 * factor4);
			double result2 = ((end_point_2 - end_point_1) * titrant_normality * factor1 * factor2 * res_unit)
					/ (sample_weight * factor3 * factor4);
			double result3 = ((end_point_3 - end_point_2) * titrant_normality * factor1 * factor2 * res_unit)
					/ (sample_weight * factor3 * factor4);
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_ten_column(cur_trial - 1, String.format("%.3f", end_point_1), String.format("%.3f", end_point_2),
					String.format("%.3f", end_point_3), String.valueOf(sample_weight),
					String.valueOf(titrant_normality), String.valueOf(moisture), String.format("%.3f", result1),
					String.format("%.3f", result2), String.format("%.3f", result3));
			update_result_scroll("Trial " + cur_trial + " : Analyte A = " + String.format("%.3f", result1) + "\n");
			update_result_scroll("Trial " + cur_trial + " : Analyte B = " + String.format("%.3f", result2) + "\n");
			update_result_scroll("Trial " + cur_trial + " : Analyte C = " + String.format("%.3f", result3) + "\n");
		} else {
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_ten_column(cur_trial - 1, "NA", "NA", "NA", String.valueOf(sample_weight),
					String.valueOf(titrant_normality), String.valueOf(moisture), "NA", "NA", "NA");
			update_result_scroll("Trial " + cur_trial + " : Result = Failed\n");
		}
		if (cur_trial == no_of_trials && select_column == false) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			trials_completed = true;
			button_continue.setEnabled(false);
			report_saved = 1;
		} else if (cur_trial == no_of_trials) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			button_continue.setEnabled(false);
			report_saved = 1;
		}
		cur_trial++;
	}

	public static void formula7_non_select_column() {
		double res_analyteA = 0, res_analyteB = 0, res_analyteC = 0;
		ArrayList<Double> temp_res = new ArrayList<Double>();
		ArrayList<Double> temp_resB = new ArrayList<Double>();
		ArrayList<Double> temp_resC = new ArrayList<Double>();

		for (int i = 0; i < no_of_trials; i++) {
			if (!model.getValueAt(i, 7).toString().matches("NA")) {
				res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 7).toString());
				res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 8).toString());
				res_analyteC = res_analyteC + Double.parseDouble(model.getValueAt(i, 9).toString());

				temp_res.add(Double.parseDouble(model.getValueAt(i, 7).toString()));
				temp_resB.add(Double.parseDouble(model.getValueAt(i, 8).toString()));
				temp_resC.add(Double.parseDouble(model.getValueAt(i, 9).toString()));
			}

			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 7).toString() + ",";
			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 8).toString() + ",";
			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 9).toString() + ",";
			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 7).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 8).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 9).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			if (i == 0) {
				db_trial_data = db_trial_data + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
						+ model.getValueAt(i, 7).toString() + "," + model.getValueAt(i, 8).toString() + ","
						+ model.getValueAt(i, 9).toString();
			} else {
				db_trial_data = db_trial_data + ":" + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
						+ model.getValueAt(i, 7).toString() + "," + model.getValueAt(i, 8).toString() + ","
						+ model.getValueAt(i, 9).toString();
			}
			db_remarks = db_remarks + "," + threshold_array.get(i);

		}

		res_analyteA = res_analyteA / temp_res.size();
		double rsd_1 = SD(temp_res);

		res_analyteB = res_analyteB / temp_res.size();
		double rsd_2 = SD(temp_resB);

		res_analyteC = res_analyteC / temp_res.size();
		double rsd_3 = SD(temp_resC);

		update_result_scroll("\nAverage Analyte A = " + String.format("%.4f", res_analyteA) + "\n");
		update_result_scroll("Average Analyte B = " + String.format("%.4f", res_analyteB) + "\n");
		update_result_scroll("Average Analyte C = " + String.format("%.4f", res_analyteC) + "\n");

		db_details = db_details + "[ " + get_time() + " ]  Analyte A = " + String.format("%.4f", res_analyteA) + ",";
		update_result_scroll("Analyte RSD 1 = " + String.format("%.4f", rsd_1) + " %\n");

		db_details = db_details + "[ " + get_time() + " ]  Analyte B = " + String.format("%.4f", res_analyteB) + ",";
		update_result_scroll("Analyte RSD 2 = " + String.format("%.4f", rsd_2) + " %\n");

		db_details = db_details + "[ " + get_time() + " ]  Analyte C = " + String.format("%.4f", res_analyteC) + ",";
		update_result_scroll("Analyte RSD 3 = " + String.format("%.4f", rsd_3) + " %\n");

		db_details = db_details + "[ " + get_time() + " ]  Result  RSD 1 = " + String.format("%.4f", rsd_1) + ",";
		db_details = db_details + "[ " + get_time() + " ]  Result  RSD 2 = " + String.format("%.4f", rsd_2) + ",";
		db_details = db_details + "[ " + get_time() + " ]  Result  RSD 3 = " + String.format("%.4f", rsd_3) + ",";

		db_results = String.format("%.4f", res_analyteA) + "," + String.format("%.4f", rsd_1) + ":"
				+ String.format("%.4f", res_analyteB) + "," + String.format("%.4f", rsd_2) + ":"
				+ String.format("%.4f", res_analyteC) + "," + String.format("%.4f", rsd_3);

		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Analyte A: " + String.format("%.4f", res_analyteA) + ", RSD: " + String.format("%.4f", rsd_1));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Analyte B: " + String.format("%.4f", res_analyteB) + ", RSD: " + String.format("%.4f", rsd_2));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Analyte C: " + String.format("%.4f", res_analyteC) + ", RSD: " + String.format("%.4f", rsd_3));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		for (int i = 0; i < data_X.size(); i++) {
			System.out.println("SIZEEEEEEEEEEEEEE = " + data_X.size() + "   iiiiiiiiii    = " + i);
			if (i == 0) {
				db_graph_x = db_graph_x + data_X.get(i);
				db_graph_y = db_graph_y + data_Y.get(i);
			} else {
				db_graph_x = db_graph_x + ":" + data_X.get(i);
				db_graph_y = db_graph_y + ":" + data_Y.get(i);
			}
		}
		update_data();
		blankRun.setEnabled(false);
		viewReport.setEnabled(true);
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Report - " + db_report_name + " Saved Successfully");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
		button_saveReport.setEnabled(false);
		button_generate_result.setEnabled(false);
	}

	public static void formula8(boolean pass) {
		if (pass == true) {
			double result1 = (end_point_1 * titrant_normality * factor1 * res_unit) / (sample_weight * factor3);
			double result2 = ((user_vol2 - end_point_1) * titrant_normality * factor2 * res_unit)
					/ (sample_weight * factor4);
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_eight_column(cur_trial - 1, String.format("%.3f", end_point_1),
					String.format("%.3f", end_point_2), String.valueOf(sample_weight),
					String.valueOf(titrant_normality), String.valueOf(moisture), String.format("%.3f", result1),
					String.format("%.3f", result2));
			update_result_scroll("Trial " + cur_trial + " : Analyte A = " + String.format("%.3f", result1) + "\n");
			update_result_scroll("Trial " + cur_trial + " : Analyte B = " + String.format("%.3f", result2) + "\n");
		} else {
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_eight_column(cur_trial - 1, "NA", "NA", String.valueOf(sample_weight),
					String.valueOf(titrant_normality), String.valueOf(moisture), "NA", "NA");
			update_result_scroll("Trial " + cur_trial + " : Result = Failed\n");
		}
		if (cur_trial == no_of_trials && select_column == false) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			trials_completed = true;
			button_continue.setEnabled(false);
			report_saved = 1;
		} else if (cur_trial == no_of_trials) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			button_continue.setEnabled(false);
			report_saved = 1;
		}
		cur_trial++;
	}

	public static void formula8_non_select_column() {
		double res_analyteA = 0, res_analyteB = 0;
		ArrayList<Double> temp_res = new ArrayList<Double>();
		ArrayList<Double> temp_resB = new ArrayList<Double>();

		for (int i = 0; i < no_of_trials; i++) {
			if (!model.getValueAt(i, 6).toString().matches("NA")) {
				res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 6).toString());
				res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 7).toString());

				temp_res.add(Double.parseDouble(model.getValueAt(i, 6).toString()));
				temp_resB.add(Double.parseDouble(model.getValueAt(i, 7).toString()));
			}

			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 6).toString() + ",";
			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 7).toString() + ",";
			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 6).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 7).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			if (i == 0) {
				db_trial_data = db_trial_data + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
						+ model.getValueAt(i, 7).toString();
			} else {
				db_trial_data = db_trial_data + ":" + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
						+ model.getValueAt(i, 7).toString();
			}
			db_remarks = db_remarks + "," + threshold_array.get(i);

		}

		res_analyteA = res_analyteA / temp_res.size();
		double rsd_1 = SD(temp_res);

		res_analyteB = res_analyteB / temp_res.size();
		double rsd_2 = SD(temp_resB);

		update_result_scroll("\nAverage AnalyteA = " + String.format("%.4f", res_analyteA) + "\n");
		update_result_scroll("Average AnalyteB = " + String.format("%.4f", res_analyteB) + "\n");

		db_details = db_details + "[ " + get_time() + " ]  Analyte A = " + String.format("%.4f", res_analyteA) + ",";
		update_result_scroll("Analyte RSD1 = " + String.format("%.4f", rsd_1) + " %\n");

		db_details = db_details + "[ " + get_time() + " ]  Analyte B = " + String.format("%.4f", res_analyteB) + ",";
		update_result_scroll("Analyte RSD2 = " + String.format("%.4f", rsd_2) + " %\n");

		db_details = db_details + "[ " + get_time() + " ]  Result  RSD 1 = " + String.format("%.4f", rsd_1) + ",";
		db_details = db_details + "[ " + get_time() + " ]  Result  RSD 2 = " + String.format("%.4f", rsd_2) + ",";

		db_results = String.format("%.4f", res_analyteA) + "," + String.format("%.4f", rsd_1) + ":"
				+ String.format("%.4f", res_analyteB) + "," + String.format("%.4f", rsd_2);

		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Analyte A: " + String.format("%.4f", res_analyteA) + ", RSD: " + String.format("%.4f", rsd_1));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Analyte B: " + String.format("%.4f", res_analyteB) + ", RSD: " + String.format("%.4f", rsd_2));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		for (int i = 0; i < data_X.size(); i++) {
			System.out.println("SIZEEEEEEEEEEEEEE = " + data_X.size() + "   iiiiiiiiii    = " + i);
			if (i == 0) {
				db_graph_x = db_graph_x + data_X.get(i);
				db_graph_y = db_graph_y + data_Y.get(i);
			} else {
				db_graph_x = db_graph_x + ":" + data_X.get(i);
				db_graph_y = db_graph_y + ":" + data_Y.get(i);
			}
		}
		update_data();
		blankRun.setEnabled(false);
		viewReport.setEnabled(true);
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Report - " + db_report_name + " Saved Successfully");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
		button_saveReport.setEnabled(false);
		button_generate_result.setEnabled(false);
	}

	public static void formula9(boolean pass) {
		// carbonate and alkali
		if (pass == true) {

			double result1 = ((end_point_2 - (2 * end_point_1)) * titrant_normality * factor1 * res_unit)
					/ (sample_weight * factor3);
			double result2 = ((2 * (end_point_2 - end_point_1)) * titrant_normality * factor2 * res_unit)
					/ (sample_weight * factor4);
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_eight_column(cur_trial - 1, String.format("%.3f", end_point_1),
					String.format("%.3f", end_point_2), String.valueOf(sample_weight),
					String.valueOf(titrant_normality), String.valueOf(moisture), String.format("%.3f", result1),
					String.format("%.3f", result2));
			update_result_scroll("Trial " + cur_trial + " : Carbonate = " + String.format("%.3f", result1) + "\n");
			update_result_scroll("Trial " + cur_trial + " : Alkali = " + String.format("%.3f", result2) + "\n");
		} else {
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_eight_column(cur_trial - 1, "NA", "NA", String.valueOf(sample_weight),
					String.valueOf(titrant_normality), String.valueOf(moisture), "NA", "NA");
			update_result_scroll("Trial " + cur_trial + " : Result = Failed\n");

		}
		if (cur_trial == no_of_trials && select_column == false) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			trials_completed = true;
			button_continue.setEnabled(false);
			report_saved = 1;
		} else if (cur_trial == no_of_trials) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			button_continue.setEnabled(false);
		}
		cur_trial++;
	}

	public static void formula9_non_select_column() {
		double res_analyteA = 0, res_analyteB = 0;
		ArrayList<Double> temp_res = new ArrayList<Double>();
		ArrayList<Double> temp_resB = new ArrayList<Double>();

		for (int i = 0; i < no_of_trials; i++) {
			if (!model.getValueAt(i, 6).toString().matches("NA")) {
				res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 6).toString());
				res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 7).toString());

				temp_res.add(Double.parseDouble(model.getValueAt(i, 6).toString()));
				temp_resB.add(Double.parseDouble(model.getValueAt(i, 7).toString()));
			}

			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 6).toString() + ",";
			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 7).toString() + ",";
			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 6).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 7).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			if (i == 0) {
				db_trial_data = db_trial_data + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
						+ model.getValueAt(i, 7).toString();
			} else {
				db_trial_data = db_trial_data + ":" + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
						+ model.getValueAt(i, 7).toString();
			}
			db_remarks = db_remarks + "," + threshold_array.get(i);

		}

		res_analyteA = res_analyteA / temp_res.size();
		double rsd_1 = SD(temp_res);

		res_analyteB = res_analyteB / temp_res.size();
		double rsd_2 = SD(temp_resB);

		update_result_scroll("\nAverage Carbonate = " + String.format("%.4f", res_analyteA) + "\n");
		update_result_scroll("Average Alkali = " + String.format("%.4f", res_analyteB) + "\n");

		db_details = db_details + "[ " + get_time() + " ]  Carbonate = " + String.format("%.4f", res_analyteA) + ",";
		update_result_scroll("Analyte RSD1 = " + String.format("%.4f", rsd_1) + " %\n");

		db_details = db_details + "[ " + get_time() + " ]  Alkali = " + String.format("%.4f", res_analyteB) + ",";
		update_result_scroll("Analyte RSD2 = " + String.format("%.4f", rsd_2) + " %\n");

		db_details = db_details + "[ " + get_time() + " ]  Result  RSD 1 = " + String.format("%.4f", rsd_1) + ",";
		db_details = db_details + "[ " + get_time() + " ]  Result  RSD 2 = " + String.format("%.4f", rsd_2) + ",";

		db_results = String.format("%.4f", res_analyteA) + "," + String.format("%.4f", rsd_1) + ":"
				+ String.format("%.4f", res_analyteB) + "," + String.format("%.4f", rsd_2);
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Carbonate: " + String.format("%.4f", res_analyteA) + ", RSD: " + String.format("%.4f", rsd_1));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Alkali: " + String.format("%.4f", res_analyteB) + ", RSD: " + String.format("%.4f", rsd_2));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		for (int i = 0; i < data_X.size(); i++) {
			System.out.println("SIZEEEEEEEEEEEEEE = " + data_X.size() + "   iiiiiiiiii    = " + i);
			if (i == 0) {
				db_graph_x = db_graph_x + data_X.get(i);
				db_graph_y = db_graph_y + data_Y.get(i);
			} else {
				db_graph_x = db_graph_x + ":" + data_X.get(i);
				db_graph_y = db_graph_y + ":" + data_Y.get(i);
			}
		}
		update_data();
		blankRun.setEnabled(false);
		viewReport.setEnabled(true);
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Report - " + db_report_name + " Saved Successfully");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
		button_saveReport.setEnabled(false);
		button_generate_result.setEnabled(false);
	}

	public static void formula10(boolean pass) {
		// bicarbonate and carbonate and alkali
		if (pass == true) {
			double result1 = (end_point_1 * titrant_normality * factor1 * factor2 * res_unit)
					/ (sample_weight * factor3 * factor4);
			double result2 = ((end_point_2 - (2 * end_point_1)) * titrant_normality * factor1 * factor2 * res_unit)
					/ (sample_weight * factor3 * factor4);
			double result3 = ((2 * (end_point_3 - end_point_2)) * titrant_normality * factor1 * factor2 * res_unit)
					/ (sample_weight * factor3 * factor4);
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_ten_column(cur_trial - 1, String.format("%.3f", end_point_1), String.format("%.3f", end_point_2),
					String.format("%.3f", end_point_3), String.valueOf(sample_weight),
					String.valueOf(titrant_normality), String.valueOf(moisture), String.format("%.3f", result1),
					String.format("%.3f", result2), String.format("%.3f", result3));
			update_result_scroll("Trial " + cur_trial + " : Bicarbonate = " + String.format("%.3f", result1) + "\n");
			update_result_scroll("Trial " + cur_trial + " : Carbonate = " + String.format("%.3f", result2) + "\n");
			update_result_scroll("Trial " + cur_trial + " : Alkali = " + String.format("%.3f", result3) + "\n");
		} else {
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_ten_column(cur_trial - 1, "NA", "NA", "NA", String.valueOf(sample_weight),
					String.valueOf(titrant_normality), String.valueOf(moisture), "NA", "NA", "NA");
			update_result_scroll("Trial " + cur_trial + " : Result = Failed\n");

		}
		if (cur_trial == no_of_trials && select_column == false) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			trials_completed = true;
			button_continue.setEnabled(false);
			report_saved = 1;
		} else if (cur_trial == no_of_trials) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			button_continue.setEnabled(false);
			report_saved = 1;
		}
		cur_trial++;
	}

	public static void formula10_non_select_column() {
		double res_analyteA = 0, res_analyteB = 0, res_analyteC = 0;
		ArrayList<Double> temp_res = new ArrayList<Double>();
		ArrayList<Double> temp_resB = new ArrayList<Double>();
		ArrayList<Double> temp_resC = new ArrayList<Double>();

		for (int i = 0; i < no_of_trials; i++) {
			if (!model.getValueAt(i, 7).toString().matches("NA")) {
				res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 7).toString());
				res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 8).toString());
				res_analyteC = res_analyteC + Double.parseDouble(model.getValueAt(i, 9).toString());

				temp_res.add(Double.parseDouble(model.getValueAt(i, 7).toString()));
				temp_resB.add(Double.parseDouble(model.getValueAt(i, 8).toString()));
				temp_resC.add(Double.parseDouble(model.getValueAt(i, 9).toString()));
			}

			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 7).toString() + ",";
			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 8).toString() + ",";
			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 9).toString() + ",";

			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 7).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 8).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 9).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			if (i == 0) {
				db_trial_data = db_trial_data + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
						+ model.getValueAt(i, 7).toString() + "," + model.getValueAt(i, 8).toString() + ","
						+ model.getValueAt(i, 9).toString();
			} else {
				db_trial_data = db_trial_data + ":" + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
						+ model.getValueAt(i, 7).toString() + "," + model.getValueAt(i, 8).toString() + ","
						+ model.getValueAt(i, 9).toString();
			}
			db_remarks = db_remarks + "," + threshold_array.get(i);

		}

		res_analyteA = res_analyteA / temp_res.size();
		double rsd_1 = SD(temp_res);

		res_analyteB = res_analyteB / temp_res.size();
		double rsd_2 = SD(temp_resB);

		res_analyteC = res_analyteC / temp_res.size();
		double rsd_3 = SD(temp_resC);

		update_result_scroll("\nAverage BiCarbonate = " + String.format("%.4f", res_analyteA) + "\n");
		update_result_scroll("Average Carbonate = " + String.format("%.4f", res_analyteB) + "\n");
		update_result_scroll("Average Alkali = " + String.format("%.4f", res_analyteC) + "\n");

		db_details = db_details + "[ " + get_time() + " ]  BiCarbonate = " + String.format("%.4f", res_analyteA) + ",";
		update_result_scroll("Analyte RSD 1 = " + String.format("%.4f", rsd_1) + " %\n");

		db_details = db_details + "[ " + get_time() + " ]  Carbonate = " + String.format("%.4f", res_analyteB) + ",";
		update_result_scroll("Analyte RSD 2 = " + String.format("%.4f", rsd_2) + " %\n");

		db_details = db_details + "[ " + get_time() + " ]  Alkali = " + String.format("%.4f", res_analyteC) + ",";
		update_result_scroll("Analyte RSD 3 = " + String.format("%.4f", rsd_3) + " %\n");

		db_details = db_details + "[ " + get_time() + " ]  Result  RSD 1 = " + String.format("%.4f", rsd_1) + ",";
		db_details = db_details + "[ " + get_time() + " ]  Result  RSD 2 = " + String.format("%.4f", rsd_2) + ",";
		db_details = db_details + "[ " + get_time() + " ]  Result  RSD 3 = " + String.format("%.4f", rsd_3) + ",";

		db_results = String.format("%.4f", res_analyteA) + "," + String.format("%.4f", rsd_1) + ":"
				+ String.format("%.4f", res_analyteB) + "," + String.format("%.4f", rsd_2) + ":"
				+ String.format("%.4f", res_analyteC) + "," + String.format("%.4f", rsd_3);

		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"BiCarbonate: " + String.format("%.4f", res_analyteA) + ", RSD: " + String.format("%.4f", rsd_1));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Carbonate: " + String.format("%.4f", res_analyteB) + ", RSD: " + String.format("%.4f", rsd_2));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Alkali: " + String.format("%.4f", res_analyteC) + ", RSD: " + String.format("%.4f", rsd_3));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		for (int i = 0; i < data_X.size(); i++) {
			System.out.println("SIZEEEEEEEEEEEEEE = " + data_X.size() + "   iiiiiiiiii    = " + i);
			if (i == 0) {
				db_graph_x = db_graph_x + data_X.get(i);
				db_graph_y = db_graph_y + data_Y.get(i);
			} else {
				db_graph_x = db_graph_x + ":" + data_X.get(i);
				db_graph_y = db_graph_y + ":" + data_Y.get(i);
			}
		}
		update_data();
		blankRun.setEnabled(false);
		viewReport.setEnabled(true);
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Report - " + db_report_name + " Saved Successfully");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
		button_saveReport.setEnabled(false);
		button_generate_result.setEnabled(false);
	}

	public static void formula11(boolean pass) {
		// bicarbonate and carbonate
		if (pass == true) {
			double result1 = (end_point_1 * titrant_normality * factor1 * res_unit) / (sample_weight * factor3);
			double result2 = ((end_point_2 - (2 * end_point_1)) * titrant_normality * factor2 * res_unit)
					/ (sample_weight * factor4);
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_eight_column(cur_trial - 1, String.format("%.3f", end_point_1),
					String.format("%.3f", end_point_2), String.valueOf(sample_weight),
					String.valueOf(titrant_normality), String.valueOf(moisture), String.format("%.3f", result1),
					String.format("%.3f", result2));
			update_result_scroll("Trial " + cur_trial + " : Bicarbonate = " + String.format("%.3f", result1) + "\n");
			update_result_scroll("Trial " + cur_trial + " : Carbonate = " + String.format("%.3f", result2) + "\n");
		} else {
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_eight_column(cur_trial - 1, "NA", "NA", String.valueOf(sample_weight),
					String.valueOf(titrant_normality), String.valueOf(moisture), "NA", "NA");
			update_result_scroll("Trial " + cur_trial + " : Result = Failed\n");

		}
		if (cur_trial == no_of_trials && select_column == false) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			trials_completed = true;
			button_continue.setEnabled(false);
			report_saved = 1;

		} else if (cur_trial == no_of_trials) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			button_continue.setEnabled(false);
			report_saved = 1;
		}
		cur_trial++;
	}

	public static void formula11_non_select_column() {
		double res_analyteA = 0, res_analyteB = 0;
		ArrayList<Double> temp_res = new ArrayList<Double>();
		ArrayList<Double> temp_resB = new ArrayList<Double>();

		for (int i = 0; i < no_of_trials; i++) {
			if (!model.getValueAt(i, 6).toString().matches("NA")) {
				res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 6).toString());
				res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 7).toString());

				temp_res.add(Double.parseDouble(model.getValueAt(i, 6).toString()));
				temp_resB.add(Double.parseDouble(model.getValueAt(i, 7).toString()));
			}

			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 6).toString() + ",";
			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 7).toString() + ",";

			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 6).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 7).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			if (i == 0) {
				db_trial_data = db_trial_data + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
						+ model.getValueAt(i, 7).toString();
			} else {
				db_trial_data = db_trial_data + ":" + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
						+ model.getValueAt(i, 7).toString();
			}
			db_remarks = db_remarks + "," + threshold_array.get(i);

		}

		res_analyteA = res_analyteA / temp_res.size();
		double rsd_1 = SD(temp_res);

		res_analyteB = res_analyteB / temp_res.size();
		double rsd_2 = SD(temp_resB);

		update_result_scroll("\nAverage BiCarbonate = " + String.format("%.4f", res_analyteA) + "\n");
		update_result_scroll("Average Carbonate = " + String.format("%.4f", res_analyteB) + "\n");

		db_details = db_details + "[ " + get_time() + " ]  BiCarbonate = " + String.format("%.4f", res_analyteA) + ",";
		update_result_scroll("Analyte RSD1 = " + String.format("%.4f", rsd_1) + " %\n");

		db_details = db_details + "[ " + get_time() + " ]  Carbonate = " + String.format("%.4f", res_analyteB) + ",";
		update_result_scroll("Analyte RSD2 = " + String.format("%.4f", rsd_2) + " %\n");

		db_details = db_details + "[ " + get_time() + " ]  Result  RSD 1 = " + String.format("%.4f", rsd_1) + ",";
		db_details = db_details + "[ " + get_time() + " ]  Result  RSD 2 = " + String.format("%.4f", rsd_2) + ",";

		db_results = String.format("%.4f", res_analyteA) + "," + String.format("%.4f", rsd_1) + ":"
				+ String.format("%.4f", res_analyteB) + "," + String.format("%.4f", rsd_2);

		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"BiCarbonate: " + String.format("%.4f", res_analyteA) + ", RSD: " + String.format("%.4f", rsd_1));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Carbonate: " + String.format("%.4f", res_analyteB) + ", RSD: " + String.format("%.4f", rsd_2));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		for (int i = 0; i < data_X.size(); i++) {
			System.out.println("SIZEEEEEEEEEEEEEE = " + data_X.size() + "   iiiiiiiiii    = " + i);
			if (i == 0) {
				db_graph_x = db_graph_x + data_X.get(i);
				db_graph_y = db_graph_y + data_Y.get(i);
			} else {
				db_graph_x = db_graph_x + ":" + data_X.get(i);
				db_graph_y = db_graph_y + ":" + data_Y.get(i);
			}
		}
		update_data();
		blankRun.setEnabled(false);
		viewReport.setEnabled(true);
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Report - " + db_report_name + " Saved Successfully");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
		button_saveReport.setEnabled(false);
		button_generate_result.setEnabled(false);
	}

	public static void formula12(boolean pass) {
		// analyte A and analyte B
		if (pass == true) {
			double result1 = (end_point_1 * titrant_normality * factor1 * res_unit) / (sample_weight * factor3);
			double result2 = ((end_point_2 - end_point_1) * titrant_normality * factor2 * res_unit)
					/ (sample_weight * factor4);
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_eight_column(cur_trial - 1, String.format("%.3f", end_point_1),
					String.format("%.3f", end_point_2), String.valueOf(sample_weight),
					String.valueOf(titrant_normality), String.valueOf(moisture), String.format("%.3f", result1),
					String.format("%.3f", result2));
			update_result_scroll("Trial " + cur_trial + " : Analyte A = " + String.format("%.3f", result1) + "\n");
			update_result_scroll("Trial " + cur_trial + " : Analyte B = " + String.format("%.3f", result2) + "\n");
		} else {
			if (model.getRowCount() == cur_trial)
				model.removeRow(cur_trial - 1);
			add_row_to_eight_column(cur_trial - 1, "NA", "NA", String.valueOf(sample_weight),
					String.valueOf(titrant_normality), String.valueOf(moisture), "NA", "NA");
			update_result_scroll("Trial " + cur_trial + " : Result = Failed\n");

		}
		if (cur_trial == no_of_trials && select_column == false) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			trials_completed = true;
			button_continue.setEnabled(false);
			report_saved = 1;
		} else if (cur_trial == no_of_trials) {
//			try {
//				audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Experiment Completed");
//			} catch (ParseException e1) {e1.printStackTrace();}
			button_continue.setEnabled(false);
			report_saved = 1;
		}
		cur_trial++;
	}

	public static void formula12_non_select_column() {
		double res_analyteA = 0, res_analyteB = 0;
		ArrayList<Double> temp_res = new ArrayList<Double>();
		ArrayList<Double> temp_resB = new ArrayList<Double>();

		for (int i = 0; i < no_of_trials; i++) {
			if (!model.getValueAt(i, 6).toString().matches("NA")) {
				res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 6).toString());
				res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 7).toString());

				temp_res.add(Double.parseDouble(model.getValueAt(i, 6).toString()));
				temp_resB.add(Double.parseDouble(model.getValueAt(i, 7).toString()));
			}

			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 6).toString() + ",";
			db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + (i + 1) + " = "
					+ model.getValueAt(i, 7).toString() + ",";
			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 6).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			try {
				audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
						"Trial " + (i + 1) + " = " + model.getValueAt(i, 7).toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			if (i == 0) {
				db_trial_data = db_trial_data + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
						+ model.getValueAt(i, 7).toString();
			} else {
				db_trial_data = db_trial_data + ":" + model.getValueAt(i, 0).toString() + ","
						+ model.getValueAt(i, 1).toString() + "," + model.getValueAt(i, 2).toString() + ","
						+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
						+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
						+ model.getValueAt(i, 7).toString();
			}
			db_remarks = db_remarks + "," + threshold_array.get(i);

		}

		res_analyteA = res_analyteA / temp_res.size();
		double rsd_1 = SD(temp_res);

		res_analyteB = res_analyteB / temp_res.size();
		double rsd_2 = SD(temp_resB);

		update_result_scroll("\nAverage AnalyteA = " + String.format("%.4f", res_analyteA) + "\n");
		update_result_scroll("Average AnalyteB = " + String.format("%.4f", res_analyteB) + "\n");

		db_details = db_details + "[ " + get_time() + " ]  Analyte A = " + String.format("%.4f", res_analyteA) + ",";
		update_result_scroll("Analyte RSD1 = " + String.format("%.4f", rsd_1) + " %\n");

		db_details = db_details + "[ " + get_time() + " ]  Analyte B = " + String.format("%.4f", res_analyteB) + ",";
		update_result_scroll("Analyte RSD2 = " + String.format("%.4f", rsd_2) + " %\n");

		db_details = db_details + "[ " + get_time() + " ]  Result  RSD 1 = " + String.format("%.4f", rsd_1) + ",";
		db_details = db_details + "[ " + get_time() + " ]  Result  RSD 2 = " + String.format("%.4f", rsd_2) + ",";

		db_results = String.format("%.4f", res_analyteA) + "," + String.format("%.4f", rsd_1) + ":"
				+ String.format("%.4f", res_analyteB) + "," + String.format("%.4f", rsd_2);

		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"AnalyteA: " + String.format("%.4f", res_analyteA) + ", RSD: " + String.format("%.4f", rsd_1));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"AnalyteB: " + String.format("%.4f", res_analyteB) + ", RSD: " + String.format("%.4f", rsd_2));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		for (int i = 0; i < data_X.size(); i++) {
			// System.out.println("SIZEEEEEEEEEEEEEE = " + data_X.size() + " iiiiiiiiii = "
			// + i);
			if (i == 0) {
				db_graph_x = db_graph_x + data_X.get(i);
				db_graph_y = db_graph_y + data_Y.get(i);
			} else {
				db_graph_x = db_graph_x + ":" + data_X.get(i);
				db_graph_y = db_graph_y + ":" + data_Y.get(i);
			}
		}
		update_data();
		blankRun.setEnabled(false);
		viewReport.setEnabled(true);
		try {
			audit_log_push.push_to_audit(get_date(), get_time(), user_name,
					"Report - " + db_report_name + " Saved Successfully");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
		button_saveReport.setEnabled(false);
		button_generate_result.setEnabled(false);
	}

	public static double SD(ArrayList<Double> arr) {
		double sum = 0.0;
		double standardDeviation = 0.0;
		double mean = 0.0;
		double res = 0.0;
		double sq = 0.0;

		int size1 = arr.size();

		for (int i = 0; i < size1; i++) {
			System.out.println("Cur_Trial = " + cur_trial + "  -   " + arr.get(i));
			sum = sum + arr.get(i);
		}

		mean = sum / (size1);

		// System.out.println("MMMEEEAAANNNN : " + mean);

		for (int i = 0; i < size1; i++) {
			standardDeviation = standardDeviation + Math.abs(Math.pow((arr.get(i) - mean), 2));
		}

		sq = standardDeviation / (size1 - 1);
		res = Math.sqrt(sq);
		res = res / mean;
		// System.out.println("MMMEEEAAANNNN RESSS : " + res);

		return (res * 100);
	}

	public static void call_formula(boolean check) {
		switch (formula_no) {
		case 1:
			formula1(check);
			break;
		case 2:
			formula2(check);
			break;
		case 3:
			formula3(check);
			break;
		case 4:
			formula4(check);
			break;
		case 5:
			formula5(check);
			break;
		case 6:
			formula6(check);
			break;
		case 7:
			formula7(check);
			break;
		case 8:
			formula8(check);
			break;
		case 9:
			formula9(check);
			break;
		case 10:
			formula10(check);
			break;
		case 11:
			formula11(check);
			break;
		case 12:
			formula12(check);
			break;
		}
	}

	public static void call_formula_report() {
		switch (formula_no) {
		case 1:
			formula1_non_select_column();
			break;
		case 2:
			formula2_non_select_column();
			break;
		case 3:
			formula3_non_select_column();
			break;
		case 4:
			formula4_non_select_column();
			break;
		case 5:
			formula5_non_select_column();
			break;
		case 6:
			formula6_non_select_column();
			break;
		case 7:
			formula7_non_select_column();
			break;
		case 8:
			formula8_non_select_column();
			break;
		case 9:
			formula9_non_select_column();
			break;
		case 10:
			formula10_non_select_column();
			break;
		case 11:
			formula11_non_select_column();
			break;
		case 12:
			formula12_non_select_column();
			break;
		}
	}

	public void arr() {
		System.out.println(" Arr_method ");

		for (i = 0; i <= (end_point_no - 1); i++) {
			for (int j = i + 1; j <= (end_point_no - 1); j++) {
				if (Math.abs(slp_diff[i]) < Math.abs(slp_diff[j])) {
					c2 = End_Point[i];
					End_Point[i] = End_Point[j];
					End_Point[j] = c2;
					c2 = slp_diff[i];
					slp_diff[i] = slp_diff[j];
					slp_diff[j] = c2;
					c2 = end_position[i];
					end_position[i] = end_position[j];
					end_position[j] = c2;
					c2 = end_point_mv[i];
					end_point_mv[i] = end_point_mv[j];
					end_point_mv[j] = c2;
				}
			}
		}
		set_dose_speed();
		res_flag = true;
//		System.out.println("dossage_speed = " + dossage_speed);

//		System.out.println("cur_val[6] = " + cur_val[6]);
//		System.out.println("cur_val[7] = " + cur_val[7]);
//		System.out.println("cur_val[17] = " + burette_factor);
//		System.out.println("dosage speed = " + dossage_speed);
//		System.out.println("ep1 = " + End_Point[0]);
//		System.out.println("ep2 = " + End_Point[1]);
//		System.out.println("ep2 = " + End_Point[2]);

		try {

			if (blank_volume_running == true) {
				if (end_point_no > 0) {
					end_point_1 = ((((End_Point[0] * dossage_speed / burette_factor) / 1000)) + pre_dose);
					ValueMarker marker = new ValueMarker(end_point_1);
					marker.setPaint(Color.blue);
					plot = (XYPlot) chart.getPlot();
					plot.addDomainMarker(marker);
					update_result_scroll("Blank Vol = " + String.format("%.5f", end_point_1) + " ml\n");
					new_blank_volume = end_point_1;
					failed_count = 0;
					button_update_blank_vol.setEnabled(true);
				} else {
					JOptionPane.showMessageDialog(null, "Blank Volume not detected");
				}
			}

			else if (math.contains("V_3") || math2.contains("V_3") || math3.contains("V_3")) {
				if (end_point_no > 2) {
					end_point_1 = (((End_Point[0] * dossage_speed / burette_factor) / 1000)) + pre_dose;
					end_point_2 = (((End_Point[1] * dossage_speed / burette_factor) / 1000)) + pre_dose;
					end_point_3 = (((End_Point[2] * dossage_speed / burette_factor) / 1000)) + pre_dose;
					ValueMarker marker = new ValueMarker(end_point_1);
					marker.setPaint(Color.blue);
					plot = (XYPlot) chart.getPlot();
					plot.addDomainMarker(marker);
					ValueMarker marker1 = new ValueMarker(end_point_2);
					marker1.setPaint(Color.blue);
					plot1 = (XYPlot) chart.getPlot();
					plot1.addDomainMarker(marker1);
					ValueMarker marker2 = new ValueMarker(end_point_3);
					marker2.setPaint(Color.blue);
					plot2 = (XYPlot) chart.getPlot();
					plot2.addDomainMarker(marker2);
					ArrayList<Double> list = new ArrayList<Double>();
					list.add(end_point_1);
					list.add(end_point_2);
					list.add(end_point_3);
					Collections.sort(list);
					end_point_1 = list.get(0);
					end_point_2 = list.get(1);
					end_point_3 = list.get(2);
					update_result_scroll("End Point 1 = " + end_point_1 + "\n");
					update_result_scroll("End Point 2 = " + end_point_2 + "\n");
					update_result_scroll("End Point 3 = " + end_point_3 + "\n");
					String x = "", y = "";
					for (int i = 0; i < series.getItemCount(); i++) {
						if (i == 0) {
							x = x + series.getX(i);
							y = y + series.getY(i);
						} else {
							x = x + "," + series.getX(i);
							y = y + "," + series.getY(i);
						}
					}
					data_X.add(x);
					data_Y.add(y);
					call_formula(true);
				} else {
					if (failed_count == 2) {
						String x = "", y = "";
						for (int i = 0; i < series.getItemCount(); i++) {
							if (i == 0) {
								x = x + series.getX(i);
								y = y + series.getY(i);
							} else {
								x = x + "," + series.getX(i);
								y = y + "," + series.getY(i);
							}
						}
						data_X.add(x);
						data_Y.add(y);
						failed_count = 0;
						call_formula(false);
						JOptionPane.showMessageDialog(null, "Trial " + (cur_trial - 1) + " Failed");
					} else {
						String[] buttons = { "Retry the trial", "Try with different Threshold and Filter" };
						int rc = JOptionPane.showOptionDialog(null,
								"Please choose as no End-Point detected for Trial " + cur_trial,
								"No End-Point Detected", JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[1]);
						if (rc == 0) {
							if (model.getRowCount() == cur_trial)
								model.removeRow(cur_trial - 1);
							failed_count++;
							try {
								threshold_array.pop();
							} catch (Exception ee) {
							}
							continue_trial();
						} else if (rc == 1) {
							call_formula(false);
						}
					}
				}
			} else if (math.contains("V_2") || math2.contains("V_2") || math3.contains("V_2")) {
				if (end_point_no > 1) {
					end_point_1 = (((End_Point[0] * dossage_speed / burette_factor) / 1000)) + pre_dose;
					end_point_2 = (((End_Point[1] * dossage_speed / burette_factor) / 1000)) + pre_dose;
					ValueMarker marker = new ValueMarker(end_point_1);
					marker.setPaint(Color.blue);
					plot = (XYPlot) chart.getPlot();
					plot.addDomainMarker(marker);
					ValueMarker marker1 = new ValueMarker(end_point_2);
					marker1.setPaint(Color.blue);
					plot1 = (XYPlot) chart.getPlot();
					plot1.addDomainMarker(marker1);
					ArrayList<Double> list = new ArrayList<Double>();
					list.add(end_point_1);
					list.add(end_point_2);
					Collections.sort(list);
					end_point_1 = list.get(0);
					end_point_2 = list.get(1);
					update_result_scroll("End Point 1 = " + end_point_1 + "\n");
					update_result_scroll("End Point 2 = " + end_point_2 + "\n");
					String x = "", y = "";
					for (int i = 0; i < series.getItemCount(); i++) {
						if (i == 0) {
							x = x + series.getX(i);
							y = y + series.getY(i);
						} else {
							x = x + "," + series.getX(i);
							y = y + "," + series.getY(i);
						}
					}
					data_X.add(x);
					data_Y.add(y);
					call_formula(true);
					// System.out.println("AADDDDIIIIIINNNNGGGGGGG");
				} else {
					if (failed_count == 2) {
						String x = "", y = "";
						for (int i = 0; i < series.getItemCount(); i++) {
							if (i == 0) {
								x = x + series.getX(i);
								y = y + series.getY(i);
							} else {
								x = x + "," + series.getX(i);
								y = y + "," + series.getY(i);
							}
						}
						data_X.add(x);
						data_Y.add(y);
						failed_count = 0;
						call_formula(false);
						JOptionPane.showMessageDialog(null, "Trial " + (cur_trial - 1) + " Failed");
					} else {
						String[] buttons = { "Retry the trial", "Try with different Threshold and Filter" };
						int rc = JOptionPane.showOptionDialog(null,
								"Please choose as no End-Point detected for Trial " + cur_trial,
								"No End-Point Detected ARR", JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[1]);
						if (rc == 0) {
							if (model.getRowCount() == cur_trial)
								model.removeRow(cur_trial - 1);
							failed_count++;
							try {
								threshold_array.pop();
							} catch (Exception ee) {
							}
							continue_trial();
						} else if (rc == 1) {
							call_formula(false);
						}
					}
				}
			}

			else if (math.contains("V_1") || math2.contains("V_1") || math3.contains("V_1")) {
				if (end_point_no > 0) {
					end_point_1 = ((((End_Point[0] * dossage_speed / burette_factor) / 1000)) + pre_dose);
					ValueMarker marker = new ValueMarker(end_point_1);
					marker.setPaint(Color.blue);
					plot = (XYPlot) chart.getPlot();
					plot.addDomainMarker(marker);
					update_result_scroll("End Point = " + end_point_1 + "\n");
					String x = "", y = "";
					for (int i = 0; i < series.getItemCount(); i++) {
						if (i == 0) {
							x = x + series.getX(i);
							y = y + series.getY(i);
						} else {
							x = x + "," + series.getX(i);
							y = y + "," + series.getY(i);
						}
					}
					data_X.add(x);
					data_Y.add(y);
					call_formula(true);
					failed_count = 0;
				} else {
					if (failed_count == 2) {
						String x = "", y = "";
						for (int i = 0; i < series.getItemCount(); i++) {
							if (i == 0) {
								x = x + series.getX(i);
								y = y + series.getY(i);
							} else {
								x = x + "," + series.getX(i);
								y = y + "," + series.getY(i);
							}
						}
						data_X.add(x);
						data_Y.add(y);
						failed_count = 0;
						call_formula(false);
						JOptionPane.showMessageDialog(null, "Trial " + (cur_trial - 1) + " Failed");
					} else {
						String[] buttons = { "Retry the trial", "Try with different Threshold and Filter" };
						int rc = JOptionPane.showOptionDialog(null,
								"Please choose as no End-Point detected for Trial " + cur_trial,
								"No End-Point Detected", JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[1]);
						if (rc == 0) {
							if (model.getRowCount() == cur_trial)
								model.removeRow(cur_trial - 1);
							try {
								threshold_array.pop();
							} catch (Exception ee) {
							}
							failed_count++;
							continue_trial();
						} else if (rc == 1) {
							call_formula(false);
						}
					}
				}
			}

			else {
				System.out.println("Not if");
			}
		} catch (NullPointerException npp) {
		}
	}

	public void set_dose_speed() {
		System.out.println(" setdose_speed ");
		if (dose_speed == 0.5)
			dossage_speed = 1.8;
		else if (dose_speed == 1)
			dossage_speed = 3.6;
		else if (dose_speed == 2)
			dossage_speed = 7.2;
		else if (dose_speed == 3)
			dossage_speed = 10.8;
		else if (dose_speed == 4)
			dossage_speed = 14.4;
		else if (dose_speed == 5)
			dossage_speed = 18;
		else if (dose_speed == 6)
			dossage_speed = 21.6;
		else if (dose_speed == 8)
			dossage_speed = 28.8;
		else if (dose_speed == 10)
			dossage_speed = 36;
		else if (dose_speed == 12)
			dossage_speed = 43.2;
		else if (dose_speed == 14)
			dossage_speed = 50.4;
		else if (dose_speed == 16)
			dossage_speed = 57.6;
	}

	public boolean xxx(ArrayList<Double> temp_d) {

		d = new double[temp_d.size()];
		// System.out.println("Filterrrrr : " + filter_val);

		// System.out.println("Thresholdddd : " + threshold_val);

		try {
			fw = new FileWriter("C:\\SQLite\\RAW_DATA.csv");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i = 0; i < temp_d.size(); i++) {
			try {
				fw.append(temp_d.get(i) + ",\n");
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		try {
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < temp_d.size(); i++) {
			d[i] = (temp_d.get(i) / 4);
		}

		ee = d.length;

		threshold_val = Double.parseDouble(tf_threshold.getText().toString());
		filter_val = Double.parseDouble(tf_filter.getText().toString());

		// System.out.println("Size = " + ee);

		if (flag_stop == false) {
			return false;
		}
		if (typ == 1 || typ == 3) {
			return false;
		}
		data1 = new double[ee];

		t = (filter_val / 5) * delta;
		k1 = delta / (t + delta);
		k2 = t / (t + delta);
		data1 = new double[ee];

		try {
			if (ee > 10) {
				for (int k = 0; k <= 10; k++) {
					data1[k] = d[k];
					// System.out.println("initial for loop " + data1[k] + " " + d[k]);
				}
			} else {
				data1[5] = 0;
				data1[10] = 0;
			}

			diff[1] = 0;
			c2 = 0;
			diff[0] = 0;
			diff[1] = 0;
			diff[2] = 0;
			diff[3] = 0;
			diff[4] = (data1[10] - data1[5]) * 20;
			// System.out.println("diff[4] = " + diff[4]);
			diff[5] = diff[4];
			diff[6] = diff[4];
			diff[7] = diff[4];
			diff[8] = diff[4];
			diff[9] = diff[4];
			diff[10] = diff[4];
			newdata1 = diff[4];
			c2 = 1;
			i = 0;
			while (i < 10) {
				c2 = c2 + 0.2 * factor;
				i = i + 1;
			}
			// System.out.println("C2 = " + c2);
			// Change K here
			k = 4;
			m1 = 0;

			Arrays.fill(diff, 0);

			// main while

			for (int z = 10; z < ee; z++) {
				k = k + 1;
				data1[10] = d[z];

				for (int y = 1; y <= 9; y++) {
					diff[y] = diff[y + 1];
				}
				diff[10] = (data1[10] - data1[5]) * 60;
				delta = 0.6;
				t = filter_val * delta * 5;
				k1 = delta / (t + delta);
				k2 = t / (t + delta);
				diff[10] = diff[9] * k2 + diff[10] * k1;
				data1[5] = data1[6];
				data1[6] = data1[7];
				data1[7] = data1[8];
				data1[8] = data1[9];
				data1[9] = data1[10];
				diff0 = diff[7];
				diff1 = diff[8];
				diff2 = diff[9];

				if (diff0 < 0) {
					diff0 = -diff0;
				}
				if (diff1 < 0) {
					diff1 = -diff1;
				}
				if (diff2 < 0) {
					diff2 = -diff2;
				}
				if ((diff1 > threshold_val) && (diff0 < diff1) && (diff1 > diff2)) {
					// System.out.println("indise if " + diff1);
					if (slope < Math.abs(diff[8])) {
						end_count = (int) c2;
						slope = diff[8];
						ky = k;
						d_flag = 1;
					}
				} else if (d_flag == 1) {
					if (Math.abs(slope) > Math.abs(diff[8]) / 1) {
						// System.out.println("indise abs-slope if " + Math.abs(slope));

						end_position[end_point_no] = end_count;
						End_Point[end_point_no] = (float) ky; // End_Point[end_point_no] = Format((ky), "###0.0###");
						slp_diff[end_point_no] = slope;
						if (dose_speed == 0.5) {
							corres_fact = 0.032;
						} else if (dose_speed == 1) {
							corres_fact = 0.017;
						} else if (dose_speed == 2) {
							corres_fact = -0.018;
						} else if (dose_speed == 3) {
							corres_fact = -0.037;
						} else if (dose_speed == 4) {
							corres_fact = -0.068;
						} else if (dose_speed == 5) {
							corres_fact = -0.054;
						} else if (dose_speed == 6) {
							corres_fact = -0.094;
						} else if (dose_speed == 8) {
							corres_fact = -0.147;
						} else if (dose_speed == 10) {
							corres_fact = -0.164;
						} else if (dose_speed == 12) {
							corres_fact = -0.22;
						} else if (dose_speed == 14) {
							corres_fact = -0.238;
						} else if (dose_speed == 16) {
							corres_fact = -0.245;
						}
						end_point_mv[end_point_no] = (data1[10]);
						// System.out.println(end_point_mv[end_point_no]);

						end_point_no = end_point_no + 1;
						total_points = end_point_no;
						d_flag = 0;
						slope = 0;
					}
				}
				c2 = c2 + 0.2 * factor * graph_mul1;
			}
			arr();
		} catch (ArrayIndexOutOfBoundsException dfv) {
			if (failed_count == 2) {
				call_formula(false);
				failed_count = 0;
				JOptionPane.showMessageDialog(null, "Trial " + (cur_trial) + " Failed");
			} else {
				String[] buttons = { "Retry the trial", "Try with different Threshold and Filter" };
				int rc = JOptionPane.showOptionDialog(null,
						"Please choose as no End-Point detected for Trial " + cur_trial, "No End-Point Detected",
						JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[1]);
				if (rc == 0) {
					failed_count++;
					try {
						threshold_array.pop();
					} catch (Exception ee) {
					}
					continue_trial();
				} else if (rc == 1) {
					// check here
				}
			}
		}

		return false;
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

	public static void continue_trial() {
		System.out.println("CurTrial = " + cur_trial + " : trial_Count = " + trial_cnt);

		if (cur_trial <= trial_cnt) {
			if (refreshing == false)
				trial_timings[cur_trial - 1] = get_time();
			button_stop.setEnabled(true);
			System.out.println("Cur_trial = " + cur_trial + " Trial_cnt = " + trial_cnt);
			JOptionPane.showMessageDialog(null, "Start Trial " + cur_trial);
			fill = 0;
			dose = 0;
			predose = 0;
			afill_first = false;
			experiment_started = true;

			if (cur_trial != 1) {
				for (int x = 0; x < d.length; x++)
					d[x] = 0;
				cur_val = new double[20];
				end_point_mv = new double[2000];
				end_position = new double[2000];
				End_Point = new double[2000];
				slp_diff = new double[2000];
				diff = new double[20];
				for (int x = 0; x < data1.length; x++)
					data1[x] = 0;
				size = 0;
				end_line = 0;
				total_points = 0;
				end_point_no = 0;
				flag_stop = true;
				typ = 0;
				ee = 0;
				k = 0;
				m1 = 0;
				ky = 0;
				d_flag = 0;
				end_count = 0;
				delta = 0.6;
				t = 0;
				k1 = 0;
				k2 = 0;
				c2 = 0;
				newdata1 = 0;
				i = 0;
//				thershold = 200;
//				factor = 1;
				graph_mul1 = 1;

				diff0 = 0;
				diff1 = 0;
				diff2 = 0;
				slope = 0;
				corres_fact = 0;
				ep_continue1 = 0;
				ep_continue2 = 0;
				ep_continue3 = 0;
				dossage_speed = 0;

			}

			dg_current_process = "trial_started";
			update_result_scroll("\nTrial " + cur_trial + " started\n");
			button_start.setEnabled(false);
			button_dvdl.setEnabled(false);
			data_array.clear();
//			data_array.add(10.0);
			vol_filled.setText("Volume filled: 00.000 ml");
			button_continue.setEnabled(false);
			vol_pre_dosed.setText("Volume pre-dosed: 00.000 ml");
			vol_dosed.setText("Volume dosed: 00.000 ml");
			series.clear();
			try {
				plot.clearDomainMarkers();
				plot1.clearDomainMarkers();
				plot2.clearDomainMarkers();
			} catch (NullPointerException npe) {
			}
			// get_mg();
			send_cvop();
		} else {
			JOptionPane.showMessageDialog(null, "Trials Completed!");
		}
	}

	public DrawGraph_pot() {
		setLayout(null);

		display = new JTextArea();
		display.setEditable(false);
		display.setFont(display.getFont().deriveFont(15f));

		JScrollPane scroll = new JScrollPane(display);
		scroll.setBounds((int) Math.round(0.54 * wid), (int) Math.round(0.392 * hei), (int) Math.round(0.235 * wid),
				(int) Math.round(0.214 * hei));

		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(scroll);

		JLabel Method_param = new JLabel("Method Parameters");
		Method_param.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.03 * hei)));
		Method_param.setBounds((int) Math.round(0.013 * wid), (int) Math.round(0.60 * hei),
				(int) Math.round(0.225 * wid), (int) Math.round(0.03 * hei));

		add(Method_param);

		dos_speed = variables[3];

		table11 = new JTable();
		table11.setDefaultEditor(Object.class, null);
		table11.setFont(new Font("Times New Roman", Font.PLAIN, (int) Math.round(0.01953 * hei)));
		// System.out.println("Constructorrrrrr = " + metd_name);
		method_model = new DefaultTableModel(
				new Object[][] { { "Metd Name", ":", metd_name, "" }, { "Pre dose", ":", variables[0], "ml" },
						{ "Stir time", ":", variables[1], "sec" }, { "Max vol", ":", variables[2], "ml" },
						{ "Dose Rate", ":", variables[7], "ml/min" }, { "Result Unit", ":", variables[16], "" },
						{ "E.P Select", ":", variables[13], "" }, { "Formula No	", ":", variables[14], "" },
						{ "Blk_Vol", ":", String.format("%.3f", bvolume), "" }, },
				new String[] { "Methods", "Parameters", "", "" });

		table11.setModel(method_model);
		method_model.fireTableDataChanged();
		table11.setBounds((int) Math.round(0.013 * wid), (int) Math.round(0.64 * hei), (int) Math.round(0.16 * wid),
				(int) Math.round(0.31 * hei));

		table11.setShowGrid(false);
		table11.setOpaque(false);

		try {

			table11.setRowHeight((int) Math.round(0.034 * hei));
		} catch (IllegalArgumentException kjhb) {
		}
		table11.setEnabled(false);
		table11.getColumnModel().getColumn(1).setPreferredWidth((int) Math.round(0.00651 * wid));
		table11.getColumnModel().getColumn(2).setPreferredWidth((int) Math.round(0.01953 * wid));

		((DefaultTableCellRenderer) table11.getDefaultRenderer(Object.class)).setOpaque(false);
		add(table11);

		table2 = new JTable();
		table2.setDefaultEditor(Object.class, null);

		table2.setFont(new Font("Times New Roman", Font.PLAIN, (int) Math.round(0.01953 * hei)));
		model2 = new DefaultTableModel(
				new Object[][] { { "Burette Factor", ":", variables[4], "" }, { "Trials", ":", variables[8], "" },
						{ "Filter", ":", "", "" }, { "Threshold", ":", "", "" }, { "factor1", ":", variables[9], "" },
						{ "factor2", ":", variables[10], "" }, { "factor3", ":", variables[11], "" },
						{ "factor4", ":", variables[12], "" }, { "Tendency", ":", variables[15], "" }, },
				new String[] { "Methods", "Parameters", "", "" });

		table2.setModel(model2);
		model2.fireTableDataChanged();
		table2.setBounds((int) Math.round(0.211 * wid), (int) Math.round(0.637 * hei), (int) Math.round(0.16 * wid),
				(int) Math.round(0.428 * hei));
		table2.getColumnModel().getColumn(1).setPreferredWidth((int) Math.round(0.00651 * wid));
		table2.getColumnModel().getColumn(2).setPreferredWidth((int) Math.round(0.01953 * wid));
		table2.setEnabled(false);
		table2.setShowGrid(false);
		table2.setOpaque(false);
		try {
			table2.setRowHeight((int) Math.round(0.034 * hei));
		} catch (IllegalArgumentException ikju) {

		}
		((DefaultTableCellRenderer) table2.getDefaultRenderer(Object.class)).setOpaque(false);
		Dimension dim1 = new Dimension(10, 1);
		table2.setIntercellSpacing(dim1);
		add(table2);

		tf_threshold = new JTextField();
		tf_threshold.setText(variables[5]);
		tf_threshold.setBounds((int) Math.round(0.3 * wid), (int) Math.round(0.745 * hei),
				(int) Math.round(0.0625 * wid), (int) Math.round(0.022 * hei));
		frame1.getContentPane().add(tf_threshold);

		info = new JLabel("( Threshold and filter values btw 1-1000 & 1-20 )");
		info.setBounds((int) Math.round(0.37 * wid), (int) Math.round(0.716 * hei), (int) Math.round(0.162 * wid),
				(int) Math.round(0.022 * hei));
		frame1.getContentPane().add(info);

		tf_filter = new JTextField();
		tf_filter.setText(variables[6]);
		tf_filter.setBounds((int) Math.round(0.3 * wid), (int) Math.round(0.713 * hei), (int) Math.round(0.0625 * wid),
				(int) Math.round(0.022 * hei));

		frame1.getContentPane().add(tf_filter);

		blankRun = new JButton("Blank Run");
		blankRun.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0079 * wid)));
		blankRun.setBounds((int) Math.round(0.833 * wid), (int) Math.round(0.443 * hei), (int) Math.round(0.091 * wid),
				(int) Math.round(0.0392 * hei));
		add(blankRun);
		blankRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (blankRun.getText().toString().matches("Blank Run")) {

					blank_volume_running = true;
					fill = 0;
					dose = 0;
					predose = 0;
					afill_first = false;
					experiment_started = true;

					if (cur_trial != 1) {
						for (int x = 0; x < d.length; x++)
							d[x] = 0;
						cur_val = new double[20];
						end_point_mv = new double[2000];
						end_position = new double[2000];
						End_Point = new double[2000];
						slp_diff = new double[2000];
						diff = new double[20];
						for (int x = 0; x < data1.length; x++)
							data1[x] = 0;
						size = 0;
						end_line = 0;
						total_points = 0;
						end_point_no = 0;
						flag_stop = true;
						typ = 0;
						ee = 0;
						k = 0;
						m1 = 0;
						ky = 0;
						d_flag = 0;
						end_count = 0;
						delta = 0.6;
						t = 0;
						k1 = 0;
						k2 = 0;
						c2 = 0;
						newdata1 = 0;
						i = 0;
//					thershold = 200;
//					factor = 1;
						graph_mul1 = 1;

						diff0 = 0;
						diff1 = 0;
						diff2 = 0;
						slope = 0;
						corres_fact = 0;
						ep_continue1 = 0;
						ep_continue2 = 0;
						ep_continue3 = 0;
						dossage_speed = 0;
					}
					dg_current_process = "trial_started";
					JOptionPane.showMessageDialog(null, "Start Blank Run");
					update_result_scroll("Blank run started \n");
					data_array.clear();
//					data_array.add(10.0);
					series.clear();
					vol_filled.setText("Volume filled: 00.000 ml");
					vol_pre_dosed.setText("Volume pre-dosed: 00.000 ml");
					vol_dosed.setText("Volume dosed: 00.000 ml");
					try {
						plot.clearDomainMarkers();
						plot1.clearDomainMarkers();
						plot2.clearDomainMarkers();
					} catch (NullPointerException npe) {
					}
					blankRun.setText("Stop Blank Run");
					send_cvop();
				} else if (blankRun.getText().toString().matches("Stop Blank Run")) {
					afill_first = false;
					dg_current_process = "";
					try {
						exec_dg.shutdown();
					} catch (NullPointerException npp) {

					}
					blankRun.setText("Check Blank Vol");
					send_stop();
				} else if (blankRun.getText().toString().matches("Check Blank Vol")) {
					for (double dd : data_array) {
						System.out.println("Data Array Val = " + dd);
					}
					button_stop.setEnabled(false);
					button_dvdl.setEnabled(false);
					button_continue.setEnabled(true);
					xxx(data_array);
					blankRun.setText("Blank Run");
				}
			}
		});

		button_update_blank_vol = new JButton("Update Blank Vol");
		button_update_blank_vol.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0079 * wid)));
		button_update_blank_vol.setBounds((int) Math.round(0.833 * wid), (int) Math.round(0.503 * hei),
				(int) Math.round(0.091 * wid), (int) Math.round(0.0392 * hei));
		add(button_update_blank_vol);
		button_update_blank_vol.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				blank_volume_running = false;
				update_blank_volume_method();
				button_update_blank_vol.setEnabled(false);
			}
		});
		button_update_blank_vol.setEnabled(false);

		button_generate_result = new JButton("Generate Result");
		button_generate_result.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0079 * wid)));
		button_generate_result.setBounds((int) Math.round(0.833 * wid), (int) Math.round(0.563 * hei),
				(int) Math.round(0.091 * wid), (int) Math.round(0.0392 * hei));
		add(button_generate_result);
		button_generate_result.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean check_trial = false;
				for (int i = 0; i < table1.getRowCount(); i++) {
					if (Boolean.valueOf(table1.getValueAt(i, 0).toString())) {
						check_trial = true;
					}
				}
				if (check_trial) {
					generate_result();
					result_timings = get_time();
					button_saveReport.setEnabled(true);
				} else
					JOptionPane.showMessageDialog(null, "Please select atleast 1 trial!");
			}

		});
		button_generate_result.setVisible(false);

		button_saveReport = new JButton("Save Report");
		button_saveReport.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		button_saveReport.setBounds((int) Math.round(0.54 * wid), (int) Math.round(0.637 * hei),
				(int) Math.round(0.0781 * wid), (int) Math.round(0.0392 * hei));
		add(button_saveReport);
		button_saveReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < threshold_array.size(); i++)
					System.out.println("Threshold Array [" + i + "] = " + threshold_array.get(i));
				int result = JOptionPane.showConfirmDialog(frame1, "Save the result? You won't be able to revert!",
						"Swing Tester", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (result == JOptionPane.YES_OPTION) {
					if (select_column == true)
						save_report();
					else {
						call_formula_report();
					}
					report_saved = 2;
				} else if (result == JOptionPane.NO_OPTION) {
				} else {
				}
			}
		});
		button_saveReport.setEnabled(false);

		viewReport = new JButton("View Report");
		viewReport.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		viewReport.setBounds((int) Math.round(0.62 * wid), (int) Math.round(0.637 * hei),
				(int) Math.round(0.0781 * wid), (int) Math.round(0.0392 * hei));

		add(viewReport);
		viewReport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String[] aa = { db_report_name, user_name, user_permissions };
				DrawReport_pot.main(aa);
			}
		});

		viewReport.setEnabled(false);

//		JButton traillReport = new JButton("Trial Report");
//		traillReport.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
//
//		traillReport.setBounds((int) Math.round(0.7 * wid), (int) Math.round(0.637 * hei),
//				(int) Math.round(0.0781 * wid), (int) Math.round(0.0392 * hei));
//
//		add(traillReport);

		JButton btnNewButton = new JButton("SOP");
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		btnNewButton.setBounds((int) Math.round(0.0846 * wid), (int) Math.round(0.539 * hei),
				(int) Math.round(0.0455 * wid), (int) Math.round(0.0392 * hei));
		add(btnNewButton);

		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// variables[17]
				System.out.println("SOP = " + variables[17]);
				if (!variables[17].matches("Not Selected")) {

					try {
						File file = new File("C:\\SQLite\\SOP\\" + variables[17]);
						if (!Desktop.isDesktopSupported()) {
							System.out.println("not supported");
							return;
						}
						Desktop desktop = Desktop.getDesktop();
						if (file.exists())
							desktop.open(file);
					} catch (Exception ee) {
						ee.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "No SOP is set for this Method file!");
				}
			}
		});

		button_start = new JButton("Start");
		button_start.setBounds((int) Math.round(0.129 * wid), (int) Math.round(0.539 * hei),
				(int) Math.round(0.0455 * wid), (int) Math.round(0.0392 * hei));
		button_start.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		add(button_start);
		button_start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				db_details = db_details + "[ " + get_time() + " ]  " + "Experiment Started,";

				trial_timings[cur_trial - 1] = get_time();
				fill = 0;
				dose = 0;
				predose = 0;
				afill_first = false;
				experiment_started = true;

				if (cur_trial != 1) {
					for (int x = 0; x < d.length; x++)
						d[x] = 0;
					cur_val = new double[20];
					end_point_mv = new double[2000];
					end_position = new double[2000];
					End_Point = new double[2000];
					slp_diff = new double[2000];
					diff = new double[20];
					for (int x = 0; x < data1.length; x++)
						data1[x] = 0;
					size = 0;
					end_line = 0;
					total_points = 0;
					end_point_no = 0;
					flag_stop = true;
					typ = 0;
					ee = 0;
					k = 0;
					m1 = 0;
					ky = 0;
					d_flag = 0;
					end_count = 0;
					delta = 0.6;
					t = 0;
					k1 = 0;
					k2 = 0;
					c2 = 0;
					newdata1 = 0;
					i = 0;
//					thershold = 200;
//					factor = 1;
					graph_mul1 = 1;

					diff0 = 0;
					diff1 = 0;
					diff2 = 0;
					slope = 0;
					corres_fact = 0;
					ep_continue1 = 0;
					ep_continue2 = 0;
					ep_continue3 = 0;
					dossage_speed = 0;

				}

				dg_current_process = "trial_started";
				JOptionPane.showMessageDialog(null, "Start Trial " + cur_trial);
				update_result_scroll("Experiment Started \n");
				update_result_scroll("\nTrial 1 started\n");

				button_start.setEnabled(false);
				button_stop.setEnabled(true);
				data_array.clear();
//				data_array.add(10.0);
				series.clear();
				vol_filled.setText("Volume filled: 00.000 ml");
				vol_pre_dosed.setText("Volume pre-dosed: 00.000 ml");
				vol_dosed.setText("Volume dosed: 00.000 ml");
				try {
					plot.clearDomainMarkers();
					plot1.clearDomainMarkers();
					plot2.clearDomainMarkers();
				} catch (NullPointerException npe) {
				}
				send_cvop();
			}
		});

		button_stop = new JButton("Stop");
		button_stop.setBounds((int) Math.round(0.171 * wid), (int) Math.round(0.539 * hei),
				(int) Math.round(0.052 * wid), (int) Math.round(0.0392 * hei));
		button_stop.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));

		add(button_stop);
		button_stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				button_dvdl.setEnabled(true);
				button_stop.setEnabled(false);
				afill_first = false;
				dg_current_process = "";
				try {
					exec_dg.shutdown();
				} catch (NullPointerException npp) {

				}
				send_stop();

			}
		});
		button_stop.setEnabled(false);
//		JButton btnNewButton_3 = new JButton("Pause");
//		btnNewButton_3.setBounds((int) Math.round(0.222 * wid), (int) Math.round(0.539 * hei),
//				(int) Math.round(0.048 * wid), (int) Math.round(0.0392 * hei));
//		btnNewButton_3.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0079 * wid)));
//
//		add(btnNewButton_3);

		button_dvdl = new JButton("dv/dl");
		button_dvdl.setBounds((int) Math.round(0.265 * wid), (int) Math.round(0.539 * hei),
				(int) Math.round(0.052 * wid), (int) Math.round(0.0392 * hei));
		button_dvdl.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0079 * wid)));

		add(button_dvdl);
		button_dvdl.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//					for (double dd : data_array) {
//						System.out.println("Data Array Val = " + dd);
//					}
				button_stop.setEnabled(false);
				button_dvdl.setEnabled(false);
				button_continue.setEnabled(true);
				// if(threshold_array.size() == 0) {
				threshold_array.push(tf_threshold.getText().toString());
				System.out.println("Setting Threshold arr = " + tf_threshold.getText().toString());
//				}
//			    else {
//			    	threshold_array[model.getRowCount()] = tf_threshold.getText().toString();	
//					System.out.println("Setting Threshold arr ["+(model.getRowCount()-1)+"] = "+tf_threshold.getText().toString());
//			    }
				xxx(data_array);
				button_refresh.setEnabled(true);

			}
		});
		button_dvdl.setEnabled(false);

		button_continue = new JButton("Cont");
		button_continue.setBounds((int) Math.round(0.315 * wid), (int) Math.round(0.539 * hei),
				(int) Math.round(0.052 * wid), (int) Math.round(0.0392 * hei));
		button_continue.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));

		add(button_continue);
		button_continue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tf_threshold.setText(variables[5]);
				tf_filter.setText(variables[6]);
				refreshing = false;
				button_continue.setEnabled(false);
				button_refresh.setEnabled(false);
				continue_trial();
			}
		});
		button_continue.setEnabled(false);

		button_exit = new JButton("ESC");
		button_exit.setBounds((int) Math.round(0.366 * wid), (int) Math.round(0.539 * hei),
				(int) Math.round(0.052 * wid), (int) Math.round(0.0392 * hei));
		button_exit.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));

		add(button_exit);

		button_exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dg_current_process = "";
				try {
					exec_dg.shutdown();
				} catch (NullPointerException np) {
				}
				try {
					exec_dg_predose.shutdown();
				} catch (NullPointerException np) {
				}
				button_stop.setEnabled(false);
				button_generate_result.setEnabled(false);
				button_update_blank_vol.setEnabled(false);
				blankRun.setEnabled(false);	

				afill_first = false;

				try {
					Thread.sleep(100);
					output_dg.print("<8888>ESCP*");
					output_dg.flush();
					ReformatBuffer.current_state = "dg_kf_escp";
				} catch (InterruptedException ex) {
				} catch (NullPointerException ee) {
					JOptionPane.showMessageDialog(null, "Please select the ComPort!");
				}
			}
		});

		button_refresh = new JButton("Refresh");
		button_refresh.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0078 * wid)));

		button_refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int thresh = Integer.parseInt(tf_threshold.getText());
					int filt = Integer.parseInt(tf_filter.getText());
					if (thresh > 1000 || filt > 20 || thresh < 1 || filt < 1) {
						JOptionPane.showMessageDialog(null, "Enter value between 1 to 20 !");
					} else {
						refreshing = true;
						threshold_array.pop();
						threshold_array.push(tf_threshold.getText().toString());

						cur_trial = model.getRowCount();
						try {
							plot.clearDomainMarkers();
							plot1.clearDomainMarkers();
							plot2.clearDomainMarkers();
						} catch (NullPointerException npe) {
						}
						cur_val = new double[20];
						end_point_mv = new double[2000];
						end_position = new double[2000];
						End_Point = new double[2000];
						slp_diff = new double[2000];
						diff = new double[20];
						for (int x = 0; x < data1.length; x++)
							data1[x] = 0;
						end_line = 0;
						total_points = 0;
						end_point_no = 0;
						flag_stop = true;
						typ = 0;
						ee = 0;
						k = 0;
						m1 = 0;
						ky = 0;
						d_flag = 0;
						end_count = 0;
						delta = 0.6;
						t = 0;
						k1 = 0;
						k2 = 0;
						c2 = 0;
						newdata1 = 0;
						i = 0;
						graph_mul1 = 1;
						diff0 = 0;
						diff1 = 0;
						diff2 = 0;
						slope = 0;
						corres_fact = 0;
						ep_continue1 = 0;
						ep_continue2 = 0;
						ep_continue3 = 0;
						dossage_speed = 0;
						xxx(data_array);
						// codee
					}
				} catch (NumberFormatException nn) {
					JOptionPane.showMessageDialog(null, "Enter correct value!");
				}
			}
		});
		button_refresh.setBounds((int) Math.round(0.423 * wid), (int) Math.round(0.539 * hei),
				(int) Math.round(0.055 * wid), (int) Math.round(0.0392 * hei));
		add(button_refresh);
		button_refresh.setEnabled(false);

		JButton button_home = new JButton("Home");
		button_home.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));

		button_home.setBounds((int) Math.round(0.013 * wid), (int) Math.round(0.539 * hei),
				(int) Math.round(0.055 * wid), (int) Math.round(0.0392 * hei));

		add(button_home);
		button_home.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					Thread.sleep(100);
					output_dg.print("<8888>DOSR,020*");
					output_dg.flush();
				} catch (InterruptedException ex) {
				} catch (NullPointerException ee) {
					JOptionPane.showMessageDialog(null, "Please select the ComPort!");
				}
				ReformatBuffer.current_state = "dg_pot_home_dosr";
			}
		});

		JLabel lblNewLabel_2 = new JLabel("mV Display");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.03 * hei)));
		lblNewLabel_2.setBounds((int) Math.round(0.846 * wid), (int) Math.round(0.0367 * hei),
				(int) Math.round(0.13 * wid), (int) Math.round(0.0392 * hei));
		add(lblNewLabel_2);

		mv_display_pot = new JLabel();
		mv_display_pot.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.026 * hei)));
		mv_display_pot.setBounds((int) Math.round(0.846 * wid), (int) Math.round(0.067 * hei),
				(int) Math.round(0.055 * wid), (int) Math.round(0.0563 * hei));
		mv_display_pot.setText("0 mV");
		add(mv_display_pot);

		JLabel formula_header = new JLabel("Formula");
		formula_header.setFont(new Font("Times New Roman", Font.PLAIN, (int) Math.round(0.013 * wid)));
		formula_header.setBounds((int) Math.round(0.846 * wid), (int) Math.round(0.147 * hei),
				(int) Math.round(0.13 * wid), (int) Math.round(0.039 * hei));
		add(formula_header);

		l_formula = new JLabel();
		l_formula2 = new JLabel();
		l_formula3 = new JLabel();

		p_formula = new JPanel();
		p_formula2 = new JPanel();
		p_formula3 = new JPanel();

		formula = new TeXFormula(math);
		ti_formula = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, (int) Math.round(0.0183 * hei));
		b_formula = new BufferedImage(ti_formula.getIconWidth(), ti_formula.getIconHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);
		ti_formula.paintIcon(new JLabel(), b_formula.getGraphics(), 0, 0);

		formula2 = new TeXFormula(math2);
		ti_formula2 = formula2.createTeXIcon(TeXConstants.STYLE_DISPLAY, (int) Math.round(0.0183 * hei));
		b_formula2 = new BufferedImage(ti_formula2.getIconWidth(), ti_formula2.getIconHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);
		ti_formula2.paintIcon(new JLabel(), b_formula2.getGraphics(), 0, 0);

		formula3 = new TeXFormula(math3);
		ti_formula3 = formula3.createTeXIcon(TeXConstants.STYLE_DISPLAY, (int) Math.round(0.0183 * hei));
		b_formula3 = new BufferedImage(ti_formula3.getIconWidth(), ti_formula3.getIconHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);
		ti_formula3.paintIcon(new JLabel(), b_formula3.getGraphics(), 0, 0);

		p_formula.setBounds((int) Math.round(0.755 * wid), (int) Math.round(0.2 * hei), (int) Math.round(0.234 * wid),
				(int) Math.round(0.0694 * hei));
		p_formula2.setBounds((int) Math.round(0.755 * wid), (int) Math.round(0.26 * hei), (int) Math.round(0.234 * wid),
				(int) Math.round(0.055 * hei));
		p_formula3.setBounds((int) Math.round(0.755 * wid), (int) Math.round(0.32 * hei), (int) Math.round(0.234 * wid),
				(int) Math.round(0.055 * hei));

		l_formula.setIcon(ti_formula);
		l_formula2.setIcon(ti_formula2);
		l_formula3.setIcon(ti_formula3);

		p_formula.add(l_formula);
		p_formula2.add(l_formula2);
		p_formula3.add(l_formula3);

		add(p_formula);
		add(p_formula2);
		add(p_formula3);

		vol_filled = new JLabel("Volume filled: 00.000 ml");
		vol_filled.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.016 * wid)));
		vol_filled.setBounds((int) Math.round(0.54 * wid), (int) Math.round(0.061 * hei), (int) Math.round(0.227 * wid),
				(int) Math.round(0.0612 * hei));
		add(vol_filled);

		vol_pre_dosed = new JLabel("Volume pre-dosed: 0.000 ml");
		vol_pre_dosed.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.016 * wid)));
		vol_pre_dosed.setBounds((int) Math.round(0.54 * wid), (int) Math.round(0.14 * hei),
				(int) Math.round(0.227 * wid), (int) Math.round(0.056 * hei));
		add(vol_pre_dosed);

		vol_dosed = new JLabel("Volume dosed: 0.000 ml");
		vol_dosed.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.016 * wid)));
		vol_dosed.setBounds((int) Math.round(0.54 * wid), (int) Math.round(0.232 * hei), (int) Math.round(0.227 * wid),
				(int) Math.round(0.039 * hei));

		add(vol_dosed);
//
//		b = new JRadioButton("with graph");
//		b.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.013 * wid)));
//		b1 = new JRadioButton("without graph");
//		b1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.013 * wid)));
//
//		ButtonGroup bg = new ButtonGroup();
//		b.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				System.out.println("with graph selected");
//			}
//		});
//		b1.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				System.out.println("without graph selected");
//			}
//		});
//
//		bg.add(b);
//		bg.add(b1);
//		b.setSelected(true);
//
//		p.setBounds((int) Math.round(0.53 * wid), (int) Math.round(0.69 * hei), (int) Math.round(0.26 * wid),
//				(int) Math.round(0.049 * hei));
//		p.add(b);
//		p.add(b1);

		chartPanel = new ChartPanel(createChart(createDataset())) {

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(640, 360);
			}
		};
		chartPanel.setBounds((int) Math.round(0.013 * wid), (int) Math.round(0.024 * hei),
				(int) Math.round(0.455 * wid), (int) Math.round(0.49 * hei)); // (int) Math.round(0.45 * hei)
		add(chartPanel);

//		System.out.println("Math = " + math);
//		System.out.println("Math2 = " + math2);
//		System.out.println("Math3 = " + math3);

		try {
			if (math.contains("V_3") || math2.contains("V_3") || math3.contains("V_3")) {
				if (math2.matches("") && math3.matches("")) {
					String[] math_header = math.split("=");
					if (math_header[0].matches("N")) {
						seven_column("Trial No", "V1 mL", "V2 mL", "V3 mL", "Wt/gm", "Moisture", "Normality");
						db_header = "Trial No,V1 mL,V2 mL,V3 mL,Wt/gm,Moisture,Normality";
					} else if (!math_header[0].contains("N")) {
						eight_column("Trial No", "V1 mL", "V2 mL", "V3 mL", "Wt/gm", "Normality", "Moisture",
								math_header[0]);
						db_header = "Trial No,V1 mL,V2 mL,V3 mL,Wt/gm,Normality,Moisture";
					}
				} else if (!math2.matches("") && math3.matches("")) {
					String[] math_header = math.split("=");
					String[] math_header2 = math2.split("=");
					nine_column("Trial No", "V1 mL", "V2 mL", "V3 mL", "Wt/gm", "Normality", "Moisture", math_header[0],
							math_header2[0]);
					db_header = "Trial No,V1 mL,V2 mL,V3 mL,Wt/gm,Normality,Moisture," + math_header[0] + ","
							+ math_header2[0];
				} else if (!math2.matches("") && !math3.matches("")) {
					String[] math_header = math.split("=");
					String[] math_header2 = math2.split("=");
					String[] math_header3 = math3.split("=");
					ten_column("Trial No", "V1 mL", "V2 mL", "V3 mL", "Wt/gm", "Normality", "Moisture", math_header[0],
							math_header2[0], math_header3[0]);
					db_header = "Trial No,V1 mL,V2 mL,V3 mL,Wt/gm,Normality,Moisture," + math_header[0] + ","
							+ math_header2[0] + "," + math_header3[0];
				}
			} else if (math.contains("V_2") || math2.contains("V_2") || math3.contains("V_2")) {

				if (math2.matches("") && math3.matches("")) {
					String[] math_header = math.split("=");
					if (math_header[0].matches("N")) {
						six_column("Trial No", "V1 mL", "V2 mL", "Wt/gm", "Moisture", "Normality");
						db_header = "Trial No,V1 mL,V2 mL,Wt/gm,Moisture,Normality";
					} else if (!math_header[0].contains("N")) {
						seven_column("Trial No", "V1 mL", "V2 mL", "Wt/gm", "Normality", "Moisture", math_header[0]);
						db_header = "Trial No,V1 mL,V2 mL,Wt/gm,Normality,Moisture," + math_header[0];
					}
				} else if (!math2.matches("") && math3.matches("")) {
					String[] math_header = math.split("=");
					String[] math_header2 = math2.split("=");
					eight_column("Trial No", "V1 mL", "V2 mL", "Wt/gm", "Normality", "Moisture", math_header[0],
							math_header2[0]);
					db_header = "Trial No,V1 mL,V2 mL,Wt/gm,Normality,Moisture," + math_header[0] + ","
							+ math_header2[0];
				} else if (!math2.matches("") && !math3.matches("")) {
					String[] math_header = math.split("=");
					String[] math_header2 = math2.split("=");
					String[] math_header3 = math3.split("=");
					nine_column("Trial No", "V1 mL", "V2 mL", "Wt/gm", "Normality", "Moisture", math_header[0],
							math_header2[0], math_header3[0]);
					db_header = "Trial No,V1 mL,V2 mL,Wt/gm,Normality,Moisture," + math_header[0] + ","
							+ math_header2[0] + "," + math_header3[0];
				}
			} else if (math.contains("V_1") || math2.contains("V_1") || math3.contains("V_1")) {

				if (math2.matches("") && math3.matches("")) {
					String[] math_header = math.split("=");
					if (math_header[0].matches("N")) {
						five_column("Trial No", "V1 mL", "Wt/gm", "Moisture", "Normality");
						db_header = "Trial No,V1 mL,Wt/gm,Moisture,Normality";
					} else if (!math_header[0].contains("N")) {
						six_column("Trial No", "V1 mL", "Wt/gm", "Normality", "Moisture", math_header[0]);
						db_header = "Trial No,V1 mL,Wt/gm,Normality,Moisture," + math_header[0];
					}
				} else if (!math2.matches("") && math3.matches("")) {
					String[] math_header = math.split("=");
					String[] math_header2 = math2.split("=");
					seven_column("Trial No", "V1 mL", "Wt/gm", "Normality", "Moisture", math_header[0],
							math_header2[0]);
					db_header = "Trial No,V1 mL,Wt/gm,Normality,Moisture," + math_header[0] + "," + math_header2[0];
				} else if (!math2.matches("") && !math3.matches("")) {
					String[] math_header = math.split("=");
					String[] math_header2 = math2.split("=");
					String[] math_header3 = math3.split("=");
					eight_column("Trial No", "V1 mL", "Wt/gm", "Normality", "Moisture", math_header[0], math_header2[0],
							math_header3[0]);
					db_header = "Trial No,V1 mL,Wt/gm,Normality,Moisture," + math_header[0] + "," + math_header2[0]
							+ "," + math_header3[0];
				}
			}
		} catch (NullPointerException npp) {
		}
		// System.out.println("Select Potentiometry = " + select_column);
		initialize();
		add_to_db();
	}

	private static void save_report() {
		if (formula_no == 1) {
			trials_completed = true;
			button_continue.setEnabled(false);
			double res_normality = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			int k = 1;
			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 5).toString().matches("NA")) {
						res_normality = res_normality + Double.parseDouble(model.getValueAt(i, 5).toString());
						temp_res.add(Double.parseDouble(model.getValueAt(i, 5).toString()));
					}
					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 5).toString() + ",";
					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 5).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}

					if (k == 1) {
						db_trial_data = db_trial_data + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString();
						db_graph_x = db_graph_x + data_X.get(i);
						db_graph_y = db_graph_y + data_Y.get(i);
					} else {
						db_trial_data = db_trial_data + ":" + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString();
						db_graph_x = db_graph_x + ":" + data_X.get(i);
						db_graph_y = db_graph_y + ":" + data_Y.get(i);
					}
					k++;
					db_remarks = db_remarks + "," + threshold_array.get(i);
				}
			}
			if (k != 1) {
				res_normality = res_normality / temp_res.size();
				double rsd_1 = SD(temp_res);
				update_result_scroll("\nAverage Normality = " + String.format("%.4f", res_normality) + "\n");
				db_details = db_details + "[ " + result_timings + " ]  Result = " + String.format("%.4f", res_normality)
						+ ",";
				update_result_scroll("Normality RSD = " + String.format("%.4f", rsd_1) + " %\n");
				db_details = db_details + "[ " + result_timings + " ]  Result  RSD = " + String.format("%.4f", rsd_1)
						+ ",";
				db_results = String.format("%.4f", res_normality) + "," + String.format("%.4f", rsd_1);

				try {
					audit_log_push.push_to_audit(get_date(), result_timings, user_name, "Normality: "
							+ String.format("%.4f", res_normality) + ", RSD: " + String.format("%.4f", rsd_1));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				String[] temp_params = db_parameters.split(",");
				String temp_db_params = "";

				for (int i = 0; i < temp_params.length; i++) {
					if (i == 0) {
						temp_db_params = temp_db_params + temp_params[0];
					} else if (i == 12) {
						temp_db_params = temp_db_params + "," + (k - 1);
					} else {
						temp_db_params = temp_db_params + "," + temp_params[i];
					}
				}
				db_parameters = temp_db_params;

				update_data();
				blankRun.setEnabled(false);
				viewReport.setEnabled(true);
				try {
					audit_log_push.push_to_audit(get_date(), get_time(), user_name,
							"Report - " + db_report_name + " Saved Successfully");
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
			} else {
				JOptionPane.showMessageDialog(null, "Select atleast one trial!");
			}
		}
		if (formula_no == 2) {
			trials_completed = true;
			button_continue.setEnabled(false);
			double res_normality = 0;
			int k = 1;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 6).toString().matches("NA")) {
						res_normality = res_normality + Double.parseDouble(model.getValueAt(i, 6).toString());
						temp_res.add(Double.parseDouble(model.getValueAt(i, 6).toString()));
					}
					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 6).toString() + ",";
					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 6).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}

					if (k == 1) {
						db_trial_data = db_trial_data + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString();
						db_graph_x = db_graph_x + data_X.get(i);
						db_graph_y = db_graph_y + data_Y.get(i);
					} else {
						db_trial_data = db_trial_data + ":" + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString();
						db_graph_x = db_graph_x + ":" + data_X.get(i);
						db_graph_y = db_graph_y + ":" + data_Y.get(i);
					}
					k++;
					db_remarks = db_remarks + "," + threshold_array.get(i);

				}
			}

			if (k != 1) {
				res_normality = res_normality / temp_res.size();
				double rsd_1 = SD(temp_res);
				update_result_scroll("\nAverage Analyte = " + String.format("%.4f", res_normality) + "\n");
				db_details = db_details + "[ " + result_timings + " ]  Result = " + String.format("%.4f", res_normality)
						+ ",";
				update_result_scroll("Analyte RSD = " + String.format("%.4f", rsd_1) + " %\n");
				db_details = db_details + "[ " + result_timings + " ]  Result  RSD = " + String.format("%.4f", rsd_1)
						+ ",";
				db_results = String.format("%.4f", res_normality) + "," + String.format("%.4f", rsd_1);
				try {
					audit_log_push.push_to_audit(get_date(), result_timings, user_name, "Analyte: "
							+ String.format("%.4f", res_normality) + ", RSD: " + String.format("%.4f", rsd_1));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				String[] temp_params = db_parameters.split(",");
				String temp_db_params = "";

				for (int i = 0; i < temp_params.length; i++) {
					if (i == 0) {
						temp_db_params = temp_db_params + temp_params[0];
					} else if (i == 12) {
						temp_db_params = temp_db_params + "," + (k - 1);
					} else {
						temp_db_params = temp_db_params + "," + temp_params[i];
					}
				}
				db_parameters = temp_db_params;

				update_data();
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
				blankRun.setEnabled(false);
				viewReport.setEnabled(true);
				try {
					audit_log_push.push_to_audit(get_date(), get_time(), user_name,
							"Report - " + db_report_name + " Saved Successfully");
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
			} else {
				JOptionPane.showMessageDialog(null, "Select atleast one trial!");
			}
		}
		if (formula_no == 3) {
			trials_completed = true;
			button_continue.setEnabled(false);
			double res_normality = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			int k = 1;
			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 6).toString().matches("NA")) {
						res_normality = res_normality + Double.parseDouble(model.getValueAt(i, 6).toString());
						temp_res.add(Double.parseDouble(model.getValueAt(i, 6).toString()));
					}
					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 6).toString() + ",";
					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 6).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					if (k == 1) {
						db_trial_data = db_trial_data + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString();
						db_graph_x = db_graph_x + data_X.get(i);
						db_graph_y = db_graph_y + data_Y.get(i);
					} else {
						db_trial_data = db_trial_data + ":" + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString();
						db_graph_x = db_graph_x + ":" + data_X.get(i);
						db_graph_y = db_graph_y + ":" + data_Y.get(i);
					}
					k++;
					db_remarks = db_remarks + "," + threshold_array.get(i);

				}
			}
			if (k != 1) {

				res_normality = res_normality / temp_res.size();
				double rsd_1 = SD(temp_res);
				update_result_scroll("\nAverage Analyte = " + String.format("%.4f", res_normality) + "\n");
				db_details = db_details + "[ " + result_timings + " ]  Result = " + String.format("%.4f", res_normality)
						+ ",";
				update_result_scroll("Analyte RSD = " + String.format("%.4f", rsd_1) + " %\n");
				db_details = db_details + "[ " + result_timings + " ]  Result  RSD = " + String.format("%.4f", rsd_1)
						+ ",";
				db_results = String.format("%.4f", res_normality) + "," + String.format("%.4f", rsd_1);

				try {
					audit_log_push.push_to_audit(get_date(), result_timings, user_name, "Analyte: "
							+ String.format("%.4f", res_normality) + ", RSD: " + String.format("%.4f", rsd_1));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				String[] temp_params = db_parameters.split(",");
				String temp_db_params = "";

				for (int i = 0; i < temp_params.length; i++) {
					if (i == 0) {
						temp_db_params = temp_db_params + temp_params[0];
					} else if (i == 12) {
						temp_db_params = temp_db_params + "," + (k - 1);
					} else {
						temp_db_params = temp_db_params + "," + temp_params[i];
					}
				}
				db_parameters = temp_db_params;

				update_data();
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
				blankRun.setEnabled(false);
				viewReport.setEnabled(true);
				try {
					audit_log_push.push_to_audit(get_date(), get_time(), user_name,
							"Report - " + db_report_name + " Saved Successfully");
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
			} else {
				JOptionPane.showMessageDialog(null, "Select atleast one trial!");
			}
		}
		if (formula_no == 4) {
			trials_completed = true;
			button_continue.setEnabled(false);
			double res_normality = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			int k = 1;
			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 6).toString().matches("NA")) {
						res_normality = res_normality + Double.parseDouble(model.getValueAt(i, 6).toString());
						temp_res.add(Double.parseDouble(model.getValueAt(i, 6).toString()));
					}
					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 6).toString() + ",";
					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 6).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					if (k == 1) {
						db_trial_data = db_trial_data + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString();
						db_graph_x = db_graph_x + data_X.get(i);
						db_graph_y = db_graph_y + data_Y.get(i);
					} else {
						db_trial_data = db_trial_data + ":" + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString();
						db_graph_x = db_graph_x + ":" + data_X.get(i);
						db_graph_y = db_graph_y + ":" + data_Y.get(i);
					}
					k++;
					db_remarks = db_remarks + "," + threshold_array.get(i);

				}
			}
			if (k != 1) {
				res_normality = res_normality / temp_res.size();
				double rsd_1 = SD(temp_res);
				update_result_scroll("\nAverage Analyte = " + String.format("%.4f", res_normality) + "\n");
				db_details = db_details + "[ " + result_timings + " ]  Result = " + String.format("%.4f", res_normality)
						+ ",";
				update_result_scroll("Analyte RSD = " + String.format("%.4f", rsd_1) + " %\n");
				db_details = db_details + "[ " + result_timings + " ]  Result  RSD = " + String.format("%.4f", rsd_1)
						+ ",";
				db_results = String.format("%.4f", res_normality) + "," + String.format("%.4f", rsd_1);

				try {
					audit_log_push.push_to_audit(get_date(), result_timings, user_name, "Analyte: "
							+ String.format("%.4f", res_normality) + ", RSD: " + String.format("%.4f", rsd_1));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				String[] temp_params = db_parameters.split(",");
				String temp_db_params = "";

				for (int i = 0; i < temp_params.length; i++) {
					if (i == 0) {
						temp_db_params = temp_db_params + temp_params[0];
					} else if (i == 12) {
						temp_db_params = temp_db_params + "," + (k - 1);
					} else {
						temp_db_params = temp_db_params + "," + temp_params[i];
					}
				}
				db_parameters = temp_db_params;

				update_data();
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
				blankRun.setEnabled(false);
				viewReport.setEnabled(true);
				try {
					audit_log_push.push_to_audit(get_date(), get_time(), user_name,
							"Report - " + db_report_name + " Saved Successfully");
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
			} else {
				JOptionPane.showMessageDialog(null, "Select atleast one trial!");
			}
		}
		if (formula_no == 5) {
			trials_completed = true;
			button_continue.setEnabled(false);
			double res_normality = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			int k = 1;
			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 7).toString().matches("NA")) {
						res_normality = res_normality + Double.parseDouble(model.getValueAt(i, 7).toString());
						temp_res.add(Double.parseDouble(model.getValueAt(i, 7).toString()));
					}
					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 7).toString() + ",";
					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 7).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					if (k == 1) {
						db_trial_data = db_trial_data + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
								+ model.getValueAt(i, 7).toString();
						db_graph_x = db_graph_x + data_X.get(i);
						db_graph_y = db_graph_y + data_Y.get(i);
					} else {
						db_trial_data = db_trial_data + ":" + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
								+ model.getValueAt(i, 7).toString();
						db_graph_x = db_graph_x + ":" + data_X.get(i);
						db_graph_y = db_graph_y + ":" + data_Y.get(i);
					}
					k++;
					db_remarks = db_remarks + "," + threshold_array.get(i);

				}
			}
			if (k != 1) {
				res_normality = res_normality / temp_res.size();
				double rsd_1 = SD(temp_res);
				update_result_scroll("\nAverage Analyte = " + String.format("%.4f", res_normality) + "\n");
				db_details = db_details + "[ " + result_timings + " ]  Result = " + String.format("%.4f", res_normality)
						+ ",";
				update_result_scroll("Analyte RSD = " + String.format("%.4f", rsd_1) + " %\n");
				db_details = db_details + "[ " + result_timings + " ]  Result  RSD = " + String.format("%.4f", rsd_1)
						+ ",";
				db_results = String.format("%.4f", res_normality) + "," + String.format("%.4f", rsd_1);

				try {
					audit_log_push.push_to_audit(get_date(), result_timings, user_name, "Analyte: "
							+ String.format("%.4f", res_normality) + ", RSD: " + String.format("%.4f", rsd_1));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				String[] temp_params = db_parameters.split(",");
				String temp_db_params = "";

				for (int i = 0; i < temp_params.length; i++) {
					if (i == 0) {
						temp_db_params = temp_db_params + temp_params[0];
					} else if (i == 12) {
						temp_db_params = temp_db_params + "," + (k - 1);
					} else {
						temp_db_params = temp_db_params + "," + temp_params[i];
					}
				}
				db_parameters = temp_db_params;

				update_data();
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
				blankRun.setEnabled(false);
				viewReport.setEnabled(true);
				try {
					audit_log_push.push_to_audit(get_date(), get_time(), user_name,
							"Report - " + db_report_name + " Saved Successfully");
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
			} else {
				JOptionPane.showMessageDialog(null, "Select atleast one trial!");
			}
		}
		if (formula_no == 6) {
			trials_completed = true;
			button_continue.setEnabled(false);
			double res_analyteA = 0, res_analyteB = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			ArrayList<Double> temp_resB = new ArrayList<Double>();

			int k = 1;
			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 7).toString().matches("NA")) {
						res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 7).toString());
						res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 8).toString());

						temp_res.add(Double.parseDouble(model.getValueAt(i, 7).toString()));
						temp_resB.add(Double.parseDouble(model.getValueAt(i, 8).toString()));
					}

					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 7).toString() + ",";
					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 8).toString() + ",";

					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 7).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 8).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}

					if (k == 1) {
						db_trial_data = db_trial_data + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
								+ model.getValueAt(i, 7).toString() + "," + model.getValueAt(i, 8).toString();
						db_graph_x = db_graph_x + data_X.get(i);
						db_graph_y = db_graph_y + data_Y.get(i);
					} else {
						db_trial_data = db_trial_data + ":" + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
								+ model.getValueAt(i, 7).toString() + "," + model.getValueAt(i, 8).toString();
						db_graph_x = db_graph_x + ":" + data_X.get(i);
						db_graph_y = db_graph_y + ":" + data_Y.get(i);
					}
					k++;
					db_remarks = db_remarks + "," + threshold_array.get(i);

				}
			}
			if (k != 1) {
				res_analyteA = res_analyteA / temp_res.size();
				double rsd_1 = SD(temp_res);

				res_analyteB = res_analyteB / temp_res.size();
				double rsd_2 = SD(temp_resB);

				update_result_scroll("\nAverage AnalyteA = " + String.format("%.4f", res_analyteA) + "\n");
				update_result_scroll("Average AnalyteB = " + String.format("%.4f", res_analyteB) + "\n");

				db_details = db_details + "[ " + result_timings + " ]  Analyte A = "
						+ String.format("%.4f", res_analyteA) + ",";
				update_result_scroll("Analyte RSD1 = " + String.format("%.4f", rsd_1) + " %\n");

				db_details = db_details + "[ " + result_timings + " ]  Analyte B = "
						+ String.format("%.4f", res_analyteB) + ",";
				update_result_scroll("Analyte RSD2 = " + String.format("%.4f", rsd_2) + " %\n");

				db_details = db_details + "[ " + result_timings + " ]  Result  RSD 1 = " + String.format("%.4f", rsd_1)
						+ ",";
				db_details = db_details + "[ " + result_timings + " ]  Result  RSD 2 = " + String.format("%.4f", rsd_2)
						+ ",";

				db_results = String.format("%.4f", res_analyteA) + "," + String.format("%.4f", rsd_1) + ":"
						+ String.format("%.4f", res_analyteB) + "," + String.format("%.4f", rsd_2);

				try {
					audit_log_push.push_to_audit(get_date(), result_timings, user_name, "AnalyteA: "
							+ String.format("%.4f", res_analyteA) + ", RSD: " + String.format("%.4f", rsd_1));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				try {
					audit_log_push.push_to_audit(get_date(), result_timings, user_name, "AnalyteB: "
							+ String.format("%.4f", res_analyteB) + ", RSD: " + String.format("%.4f", rsd_2));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				String[] temp_params = db_parameters.split(",");
				String temp_db_params = "";

				for (int i = 0; i < temp_params.length; i++) {
					if (i == 0) {
						temp_db_params = temp_db_params + temp_params[0];
					} else if (i == 12) {
						temp_db_params = temp_db_params + "," + (k - 1);
					} else {
						temp_db_params = temp_db_params + "," + temp_params[i];
					}
				}
				db_parameters = temp_db_params;

				update_data();
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
				blankRun.setEnabled(false);
				viewReport.setEnabled(true);
				try {
					audit_log_push.push_to_audit(get_date(), get_time(), user_name,
							"Report - " + db_report_name + " Saved Successfully");
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
			} else {
				JOptionPane.showMessageDialog(null, "Select atleast one trial!");
			}
		}
		if (formula_no == 7) {
			trials_completed = true;
			button_continue.setEnabled(false);
			double res_analyteA = 0, res_analyteB = 0, res_analyteC = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			ArrayList<Double> temp_resB = new ArrayList<Double>();
			ArrayList<Double> temp_resC = new ArrayList<Double>();

			int k = 1;
			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 8).toString().matches("NA")) {

						res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 8).toString());
						res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 9).toString());
						res_analyteC = res_analyteC + Double.parseDouble(model.getValueAt(i, 10).toString());

						temp_res.add(Double.parseDouble(model.getValueAt(i, 8).toString()));
						temp_resB.add(Double.parseDouble(model.getValueAt(i, 9).toString()));
						temp_resC.add(Double.parseDouble(model.getValueAt(i, 10).toString()));
					}

					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 8).toString() + ",";
					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 9).toString() + ",";
					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 10).toString() + ",";

					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 8).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 9).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 10).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}

					if (k == 1) {
						db_trial_data = db_trial_data + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
								+ model.getValueAt(i, 7).toString() + "," + model.getValueAt(i, 8).toString() + ","
								+ model.getValueAt(i, 9).toString() + "," + model.getValueAt(i, 10).toString();
						db_graph_x = db_graph_x + data_X.get(i);
						db_graph_y = db_graph_y + data_Y.get(i);
					} else {
						db_trial_data = db_trial_data + ":" + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
								+ model.getValueAt(i, 7).toString() + "," + model.getValueAt(i, 8).toString() + ","
								+ model.getValueAt(i, 9).toString() + "," + model.getValueAt(i, 10).toString();
						db_graph_x = db_graph_x + ":" + data_X.get(i);
						db_graph_y = db_graph_y + ":" + data_Y.get(i);
					}
					k++;
					db_remarks = db_remarks + "," + threshold_array.get(i);

				}
			}
			if (k != 1) {
				res_analyteA = res_analyteA / temp_res.size();
				double rsd_1 = SD(temp_res);

				res_analyteB = res_analyteB / temp_res.size();
				double rsd_2 = SD(temp_resB);

				res_analyteC = res_analyteC / temp_res.size();
				double rsd_3 = SD(temp_resC);

				update_result_scroll("\nAverage Analyte A = " + String.format("%.4f", res_analyteA) + "\n");
				update_result_scroll("Average Analyte B = " + String.format("%.4f", res_analyteB) + "\n");
				update_result_scroll("Average Analyte C = " + String.format("%.4f", res_analyteC) + "\n");

				db_details = db_details + "[ " + result_timings + " ]  Analyte A = "
						+ String.format("%.4f", res_analyteA) + ",";
				update_result_scroll("Analyte RSD 1 = " + String.format("%.4f", rsd_1) + " %\n");

				db_details = db_details + "[ " + result_timings + " ]  Analyte B = "
						+ String.format("%.4f", res_analyteB) + ",";
				update_result_scroll("Analyte RSD 2 = " + String.format("%.4f", rsd_2) + " %\n");

				db_details = db_details + "[ " + result_timings + " ]  Analyte C = "
						+ String.format("%.4f", res_analyteC) + ",";
				update_result_scroll("Analyte RSD 3 = " + String.format("%.4f", rsd_3) + " %\n");

				db_details = db_details + "[ " + result_timings + " ]  Result  RSD 1 = " + String.format("%.4f", rsd_1)
						+ ",";
				db_details = db_details + "[ " + result_timings + " ]  Result  RSD 2 = " + String.format("%.4f", rsd_2)
						+ ",";
				db_details = db_details + "[ " + result_timings + " ]  Result  RSD 3 = " + String.format("%.4f", rsd_3)
						+ ",";

				db_results = String.format("%.4f", res_analyteA) + "," + String.format("%.4f", rsd_1) + ":"
						+ String.format("%.4f", res_analyteB) + "," + String.format("%.4f", rsd_2) + ":"
						+ String.format("%.4f", res_analyteC) + "," + String.format("%.4f", rsd_3);

				try {
					audit_log_push.push_to_audit(get_date(), result_timings, user_name, "AnalyteA: "
							+ String.format("%.4f", res_analyteA) + ", RSD: " + String.format("%.4f", rsd_1));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				try {
					audit_log_push.push_to_audit(get_date(), result_timings, user_name, "AnalyteB: "
							+ String.format("%.4f", res_analyteB) + ", RSD: " + String.format("%.4f", rsd_2));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				String[] temp_params = db_parameters.split(",");
				String temp_db_params = "";

				for (int i = 0; i < temp_params.length; i++) {
					if (i == 0) {
						temp_db_params = temp_db_params + temp_params[0];
					} else if (i == 12) {
						temp_db_params = temp_db_params + "," + (k - 1);
					} else {
						temp_db_params = temp_db_params + "," + temp_params[i];
					}
				}
				db_parameters = temp_db_params;

				update_data();
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
				blankRun.setEnabled(false);
				viewReport.setEnabled(true);
				try {
					audit_log_push.push_to_audit(get_date(), get_time(), user_name,
							"Report - " + db_report_name + " Saved Successfully");
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
			} else {
				JOptionPane.showMessageDialog(null, "Select atleast one trial!");
			}
		}
		if (formula_no == 8) {
			trials_completed = true;
			button_continue.setEnabled(false);
			double res_analyteA = 0, res_analyteB = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			ArrayList<Double> temp_resB = new ArrayList<Double>();

			int k = 1;
			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 7).toString().matches("NA")) {
						res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 7).toString());
						res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 8).toString());

						temp_res.add(Double.parseDouble(model.getValueAt(i, 7).toString()));
						temp_resB.add(Double.parseDouble(model.getValueAt(i, 8).toString()));
					}

					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 7).toString() + ",";
					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 8).toString() + ",";
					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 7).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 8).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}

					if (k == 1) {
						db_trial_data = db_trial_data + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
								+ model.getValueAt(i, 7).toString() + "," + model.getValueAt(i, 8).toString();
						db_graph_x = db_graph_x + data_X.get(i);
						db_graph_y = db_graph_y + data_Y.get(i);
					} else {
						db_trial_data = db_trial_data + ":" + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
								+ model.getValueAt(i, 7).toString() + "," + model.getValueAt(i, 8).toString();
						db_graph_x = db_graph_x + ":" + data_X.get(i);
						db_graph_y = db_graph_y + ":" + data_Y.get(i);
					}
					k++;
					db_remarks = db_remarks + "," + threshold_array.get(i);

				}
			}
			if (k != 1) {

				res_analyteA = res_analyteA / temp_res.size();
				double rsd_1 = SD(temp_res);

				res_analyteB = res_analyteB / temp_res.size();
				double rsd_2 = SD(temp_resB);

				update_result_scroll("\nAverage AnalyteA = " + String.format("%.4f", res_analyteA) + "\n");
				update_result_scroll("Average AnalyteB = " + String.format("%.4f", res_analyteB) + "\n");

				db_details = db_details + "[ " + result_timings + " ]  Analyte A = "
						+ String.format("%.4f", res_analyteA) + ",";
				update_result_scroll("Analyte RSD1 = " + String.format("%.4f", rsd_1) + " %\n");

				db_details = db_details + "[ " + result_timings + " ]  Analyte B = "
						+ String.format("%.4f", res_analyteB) + ",";
				update_result_scroll("Analyte RSD2 = " + String.format("%.4f", rsd_2) + " %\n");

				db_details = db_details + "[ " + result_timings + " ]  Result  RSD 1 = " + String.format("%.4f", rsd_1)
						+ ",";
				db_details = db_details + "[ " + result_timings + " ]  Result  RSD 2 = " + String.format("%.4f", rsd_2)
						+ ",";

				db_results = String.format("%.4f", res_analyteA) + "," + String.format("%.4f", rsd_1) + ":"
						+ String.format("%.4f", res_analyteB) + "," + String.format("%.4f", rsd_2);

				try {
					audit_log_push.push_to_audit(get_date(), result_timings, user_name, "AnalyteA: "
							+ String.format("%.4f", res_analyteA) + ", RSD: " + String.format("%.4f", rsd_1));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				try {
					audit_log_push.push_to_audit(get_date(), result_timings, user_name, "AnalyteB: "
							+ String.format("%.4f", res_analyteB) + ", RSD: " + String.format("%.4f", rsd_2));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				String[] temp_params = db_parameters.split(",");
				String temp_db_params = "";

				for (int i = 0; i < temp_params.length; i++) {
					if (i == 0) {
						temp_db_params = temp_db_params + temp_params[0];
					} else if (i == 12) {
						temp_db_params = temp_db_params + "," + (k - 1);
					} else {
						temp_db_params = temp_db_params + "," + temp_params[i];
					}
				}
				db_parameters = temp_db_params;

				update_data();
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
				blankRun.setEnabled(false);
				viewReport.setEnabled(true);
				try {
					audit_log_push.push_to_audit(get_date(), get_time(), user_name,
							"Report - " + db_report_name + " Saved Successfully");
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
			} else {
				JOptionPane.showMessageDialog(null, "Select atleast one trial!");
			}
		}
		if (formula_no == 9) {
			trials_completed = true;
			button_continue.setEnabled(false);
			double res_analyteA = 0, res_analyteB = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			ArrayList<Double> temp_resB = new ArrayList<Double>();

			int k = 1;
			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 7).toString().matches("NA")) {
						res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 7).toString());
						res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 8).toString());

						temp_res.add(Double.parseDouble(model.getValueAt(i, 7).toString()));
						temp_resB.add(Double.parseDouble(model.getValueAt(i, 8).toString()));
					}

					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 7).toString() + ",";
					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 8).toString() + ",";
					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 7).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 8).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}

					if (k == 1) {
						db_trial_data = db_trial_data + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
								+ model.getValueAt(i, 7).toString() + "," + model.getValueAt(i, 8).toString();
						db_graph_x = db_graph_x + data_X.get(i);
						db_graph_y = db_graph_y + data_Y.get(i);
					} else {
						db_trial_data = db_trial_data + ":" + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
								+ model.getValueAt(i, 7).toString() + "," + model.getValueAt(i, 8).toString();
						db_graph_x = db_graph_x + ":" + data_X.get(i);
						db_graph_y = db_graph_y + ":" + data_Y.get(i);
					}
					k++;
					db_remarks = db_remarks + "," + threshold_array.get(i);

				}
			}
			if (k != 1) {
				res_analyteA = res_analyteA / temp_res.size();
				double rsd_1 = SD(temp_res);

				res_analyteB = res_analyteB / temp_res.size();
				double rsd_2 = SD(temp_resB);

				update_result_scroll("\nAverage Carbonate = " + String.format("%.4f", res_analyteA) + "\n");
				update_result_scroll("Average Alkali = " + String.format("%.4f", res_analyteB) + "\n");

				db_details = db_details + "[ " + result_timings + " ]  Carbonate = "
						+ String.format("%.4f", res_analyteA) + ",";
				update_result_scroll("Analyte RSD1 = " + String.format("%.4f", rsd_1) + " %\n");

				db_details = db_details + "[ " + result_timings + " ]  Alkali = " + String.format("%.4f", res_analyteB)
						+ ",";
				update_result_scroll("Analyte RSD2 = " + String.format("%.4f", rsd_2) + " %\n");

				db_details = db_details + "[ " + result_timings + " ]  Result  RSD 1 = " + String.format("%.4f", rsd_1)
						+ ",";
				db_details = db_details + "[ " + result_timings + " ]  Result  RSD 2 = " + String.format("%.4f", rsd_2)
						+ ",";

				db_results = String.format("%.4f", res_analyteA) + "," + String.format("%.4f", rsd_1) + ":"
						+ String.format("%.4f", res_analyteB) + "," + String.format("%.4f", rsd_2);

				try {
					audit_log_push.push_to_audit(get_date(), result_timings, user_name, "Carbonate: "
							+ String.format("%.4f", res_analyteA) + ", RSD: " + String.format("%.4f", rsd_1));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				try {
					audit_log_push.push_to_audit(get_date(), result_timings, user_name, "Alkali: "
							+ String.format("%.4f", res_analyteB) + ", RSD: " + String.format("%.4f", rsd_2));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				String[] temp_params = db_parameters.split(",");
				String temp_db_params = "";

				for (int i = 0; i < temp_params.length; i++) {
					if (i == 0) {
						temp_db_params = temp_db_params + temp_params[0];
					} else if (i == 12) {
						temp_db_params = temp_db_params + "," + (k - 1);
					} else {
						temp_db_params = temp_db_params + "," + temp_params[i];
					}
				}
				db_parameters = temp_db_params;

				update_data();
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
				blankRun.setEnabled(false);
				viewReport.setEnabled(true);
				try {
					audit_log_push.push_to_audit(get_date(), get_time(), user_name,
							"Report - " + db_report_name + " Saved Successfully");
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
			} else {
				JOptionPane.showMessageDialog(null, "Select atleast one trial!");
			}
		}
		if (formula_no == 10) {
			trials_completed = true;
			button_continue.setEnabled(false);
			double res_analyteA = 0, res_analyteB = 0, res_analyteC = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			ArrayList<Double> temp_resB = new ArrayList<Double>();
			ArrayList<Double> temp_resC = new ArrayList<Double>();

			int k = 1;
			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 8).toString().matches("NA")) {

						res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 8).toString());
						res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 9).toString());
						res_analyteC = res_analyteC + Double.parseDouble(model.getValueAt(i, 10).toString());

						temp_res.add(Double.parseDouble(model.getValueAt(i, 8).toString()));
						temp_resB.add(Double.parseDouble(model.getValueAt(i, 9).toString()));
						temp_resC.add(Double.parseDouble(model.getValueAt(i, 10).toString()));
					}

					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 8).toString() + ",";
					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 9).toString() + ",";
					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 10).toString() + ",";

					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 8).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 9).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 10).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}

					if (k == 1) {
						db_trial_data = db_trial_data + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
								+ model.getValueAt(i, 7).toString() + "," + model.getValueAt(i, 8).toString() + ","
								+ model.getValueAt(i, 9).toString() + "," + model.getValueAt(i, 10).toString();
						db_graph_x = db_graph_x + data_X.get(i);
						db_graph_y = db_graph_y + data_Y.get(i);
					} else {
						db_trial_data = db_trial_data + ":" + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
								+ model.getValueAt(i, 7).toString() + "," + model.getValueAt(i, 8).toString() + ","
								+ model.getValueAt(i, 9).toString() + "," + model.getValueAt(i, 10).toString();
						db_graph_x = db_graph_x + data_X.get(i);
						db_graph_y = db_graph_y + data_Y.get(i);
					}
					k++;
					db_remarks = db_remarks + "," + threshold_array.get(i);

				}
			}
			if (k != 1) {
				res_analyteA = res_analyteA / temp_res.size();
				double rsd_1 = SD(temp_res);

				res_analyteB = res_analyteB / temp_res.size();
				double rsd_2 = SD(temp_resB);

				res_analyteC = res_analyteC / temp_res.size();
				double rsd_3 = SD(temp_resC);

				update_result_scroll("\nAverage BiCarbonate = " + String.format("%.4f", res_analyteA) + "\n");
				update_result_scroll("Average Carbonate = " + String.format("%.4f", res_analyteB) + "\n");
				update_result_scroll("Average Alkali = " + String.format("%.4f", res_analyteC) + "\n");

				db_details = db_details + "[ " + result_timings + " ]  BiCarbonate = "
						+ String.format("%.4f", res_analyteA) + ",";
				update_result_scroll("Analyte RSD 1 = " + String.format("%.4f", rsd_1) + " %\n");

				db_details = db_details + "[ " + result_timings + " ]  Carbonate = "
						+ String.format("%.4f", res_analyteB) + ",";
				update_result_scroll("Analyte RSD 2 = " + String.format("%.4f", rsd_2) + " %\n");

				db_details = db_details + "[ " + result_timings + " ]  Alkali = " + String.format("%.4f", res_analyteC)
						+ ",";
				update_result_scroll("Analyte RSD 3 = " + String.format("%.4f", rsd_3) + " %\n");

				db_details = db_details + "[ " + result_timings + " ]  Result  RSD 1 = " + String.format("%.4f", rsd_1)
						+ ",";
				db_details = db_details + "[ " + result_timings + " ]  Result  RSD 2 = " + String.format("%.4f", rsd_2)
						+ ",";
				db_details = db_details + "[ " + result_timings + " ]  Result  RSD 3 = " + String.format("%.4f", rsd_3)
						+ ",";

				db_results = String.format("%.4f", res_analyteA) + "," + String.format("%.4f", rsd_1) + ":"
						+ String.format("%.4f", res_analyteB) + "," + String.format("%.4f", rsd_2) + ":"
						+ String.format("%.4f", res_analyteC) + "," + String.format("%.4f", rsd_3);
				try {
					audit_log_push.push_to_audit(get_date(), result_timings, user_name, "BiCarbonate: "
							+ String.format("%.4f", res_analyteA) + ", RSD: " + String.format("%.4f", rsd_1));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				try {
					audit_log_push.push_to_audit(get_date(), result_timings, user_name, "Carbonate: "
							+ String.format("%.4f", res_analyteB) + ", RSD: " + String.format("%.4f", rsd_2));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				try {
					audit_log_push.push_to_audit(get_date(), result_timings, user_name, "Alkali: "
							+ String.format("%.4f", res_analyteC) + ", RSD: " + String.format("%.4f", rsd_3));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				String[] temp_params = db_parameters.split(",");
				String temp_db_params = "";

				for (int i = 0; i < temp_params.length; i++) {
					if (i == 0) {
						temp_db_params = temp_db_params + temp_params[0];
					} else if (i == 12) {
						temp_db_params = temp_db_params + "," + (k - 1);
					} else {
						temp_db_params = temp_db_params + "," + temp_params[i];
					}
				}
				db_parameters = temp_db_params;

				update_data();
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
				blankRun.setEnabled(false);
				viewReport.setEnabled(true);
				try {
					audit_log_push.push_to_audit(get_date(), get_time(), user_name,
							"Report - " + db_report_name + " Saved Successfully");
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
			} else {
				JOptionPane.showMessageDialog(null, "Select atleast one trial!");

			}
		}
		if (formula_no == 11) {
			trials_completed = true;
			button_continue.setEnabled(false);
			double res_analyteA = 0, res_analyteB = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			ArrayList<Double> temp_resB = new ArrayList<Double>();

			int k = 1;
			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 7).toString().matches("NA")) {
						res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 7).toString());
						res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 8).toString());

						temp_res.add(Double.parseDouble(model.getValueAt(i, 7).toString()));
						temp_resB.add(Double.parseDouble(model.getValueAt(i, 8).toString()));
					}

					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 7).toString() + ",";
					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 8).toString() + ",";
					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 7).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 8).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}

					if (k == 1) {
						db_trial_data = db_trial_data + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
								+ model.getValueAt(i, 7).toString() + "," + model.getValueAt(i, 8).toString();
					} else {
						db_trial_data = db_trial_data + ":" + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
								+ model.getValueAt(i, 7).toString() + "," + model.getValueAt(i, 8).toString();
					}
					k++;
					db_remarks = db_remarks + "," + threshold_array.get(i);

				}
			}
			if (k != 1) {
				res_analyteA = res_analyteA / temp_res.size();
				double rsd_1 = SD(temp_res);

				res_analyteB = res_analyteB / temp_res.size();
				double rsd_2 = SD(temp_resB);

				update_result_scroll("\nAverage BiCarbonate = " + String.format("%.4f", res_analyteA) + "\n");
				update_result_scroll("Average Carbonate = " + String.format("%.4f", res_analyteB) + "\n");

				db_details = db_details + "[ " + result_timings + " ]  BiCarbonate = "
						+ String.format("%.4f", res_analyteA) + ",";
				update_result_scroll("Analyte RSD1 = " + String.format("%.4f", rsd_1) + " %\n");

				db_details = db_details + "[ " + result_timings + " ]  Carbonate = "
						+ String.format("%.4f", res_analyteB) + ",";
				update_result_scroll("Analyte RSD2 = " + String.format("%.4f", rsd_2) + " %\n");

				db_details = db_details + "[ " + result_timings + " ]  Result  RSD 1 = " + String.format("%.4f", rsd_1)
						+ ",";
				db_details = db_details + "[ " + result_timings + " ]  Result  RSD 2 = " + String.format("%.4f", rsd_2)
						+ ",";

				db_results = String.format("%.4f", res_analyteA) + "," + String.format("%.4f", rsd_1) + ":"
						+ String.format("%.4f", res_analyteB) + "," + String.format("%.4f", rsd_2);

				try {
					audit_log_push.push_to_audit(get_date(), result_timings, user_name, "BiCarbonate: "
							+ String.format("%.4f", res_analyteA) + ", RSD: " + String.format("%.4f", rsd_1));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				try {
					audit_log_push.push_to_audit(get_date(), result_timings, user_name, "Carbonate: "
							+ String.format("%.4f", res_analyteB) + ", RSD: " + String.format("%.4f", rsd_2));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				String[] temp_params = db_parameters.split(",");
				String temp_db_params = "";

				for (int i = 0; i < temp_params.length; i++) {
					if (i == 0) {
						temp_db_params = temp_db_params + temp_params[0];
					} else if (i == 12) {
						temp_db_params = temp_db_params + "," + (k - 1);
					} else {
						temp_db_params = temp_db_params + "," + temp_params[i];
					}
				}
				db_parameters = temp_db_params;

				update_data();
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
				blankRun.setEnabled(false);
				try {
					audit_log_push.push_to_audit(get_date(), get_time(), user_name,
							"Report - " + db_report_name + " Saved Successfully");
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
			} else {
				JOptionPane.showMessageDialog(null, "Select atleast one trial!");
			}
		}
		if (formula_no == 12) {
			trials_completed = true;
			button_continue.setEnabled(false);
			double res_analyteA = 0, res_analyteB = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			ArrayList<Double> temp_resB = new ArrayList<Double>();

			int k = 1;
			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 7).toString().matches("NA")) {
						res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 7).toString());
						res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 8).toString());

						temp_res.add(Double.parseDouble(model.getValueAt(i, 7).toString()));
						temp_resB.add(Double.parseDouble(model.getValueAt(i, 8).toString()));
					}

					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 7).toString() + ",";
					db_details = db_details + "[ " + trial_timings[i] + " ]  Trial " + k + " = "
							+ model.getValueAt(i, 8).toString() + ",";
					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 7).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					try {
						audit_log_push.push_to_audit(get_date(), trial_timings[i], user_name,
								"Trial " + k + " = " + model.getValueAt(i, 8).toString());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}

					if (k == 1) {
						db_trial_data = db_trial_data + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
								+ model.getValueAt(i, 7).toString() + "," + model.getValueAt(i, 8).toString();
						db_graph_x = db_graph_x + data_X.get(i);
						db_graph_y = db_graph_y + data_Y.get(i);
					} else {
						db_trial_data = db_trial_data + ":" + k + "," + model.getValueAt(i, 2).toString() + ","
								+ model.getValueAt(i, 3).toString() + "," + model.getValueAt(i, 4).toString() + ","
								+ model.getValueAt(i, 5).toString() + "," + model.getValueAt(i, 6).toString() + ","
								+ model.getValueAt(i, 7).toString() + "," + model.getValueAt(i, 8).toString();
						db_graph_x = db_graph_x + ":" + data_X.get(i);
						db_graph_y = db_graph_y + ":" + data_Y.get(i);
					}
					k++;
					db_remarks = db_remarks + "," + threshold_array.get(i);

				}
			}
			if (k != 1) {
				res_analyteA = res_analyteA / temp_res.size();
				double rsd_1 = SD(temp_res);

				res_analyteB = res_analyteB / temp_res.size();
				double rsd_2 = SD(temp_resB);

				update_result_scroll("\nAverage AnalyteA = " + String.format("%.4f", res_analyteA) + "\n");
				update_result_scroll("Average AnalyteB = " + String.format("%.4f", res_analyteB) + "\n");

				db_details = db_details + "[ " + result_timings + " ]  Analyte A = "
						+ String.format("%.4f", res_analyteA) + ",";
				update_result_scroll("Analyte RSD1 = " + String.format("%.4f", rsd_1) + " %\n");

				db_details = db_details + "[ " + result_timings + " ]  Analyte B = "
						+ String.format("%.4f", res_analyteB) + ",";
				update_result_scroll("Analyte RSD2 = " + String.format("%.4f", rsd_2) + " %\n");

				db_details = db_details + "[ " + result_timings + " ]  Result  RSD 1 = " + String.format("%.4f", rsd_1)
						+ ",";
				db_details = db_details + "[ " + result_timings + " ]  Result  RSD 2 = " + String.format("%.4f", rsd_2)
						+ ",";

				db_results = String.format("%.4f", res_analyteA) + "," + String.format("%.4f", rsd_1) + ":"
						+ String.format("%.4f", res_analyteB) + "," + String.format("%.4f", rsd_2);

				try {
					audit_log_push.push_to_audit(get_date(), result_timings, user_name, "AnalyteA: "
							+ String.format("%.4f", res_analyteA) + ", RSD: " + String.format("%.4f", rsd_1));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				try {
					audit_log_push.push_to_audit(get_date(), result_timings, user_name, "AnalyteB: "
							+ String.format("%.4f", res_analyteB) + ", RSD: " + String.format("%.4f", rsd_2));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				String[] temp_params = db_parameters.split(",");
				String temp_db_params = "";

				for (int i = 0; i < temp_params.length; i++) {
					if (i == 0) {
						temp_db_params = temp_db_params + temp_params[0];
					} else if (i == 12) {
						temp_db_params = temp_db_params + "," + (k - 1);
					} else {
						temp_db_params = temp_db_params + "," + temp_params[i];
					}
				}
				db_parameters = temp_db_params;

				update_data();
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);
				blankRun.setEnabled(false);
				viewReport.setEnabled(true);
				try {
					audit_log_push.push_to_audit(get_date(), get_time(), user_name,
							"Report - " + db_report_name + " Saved Successfully");
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Report Saved Successfully!");
				button_saveReport.setEnabled(false);
				button_generate_result.setEnabled(false);

			} else {
				JOptionPane.showMessageDialog(null, "Select atleast one trial!");
			}
		}
	}

	private static void generate_result() {
		if (formula_no == 1) {
			double res_normality = 0;
			button_continue.setEnabled(false);
			ArrayList<Double> temp_res = new ArrayList<Double>();
			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 5).toString().matches("NA")) {
						res_normality = res_normality + Double.parseDouble(model.getValueAt(i, 5).toString());
						temp_res.add(Double.parseDouble(model.getValueAt(i, 5).toString()));
					}
				}
			}
			res_normality = res_normality / temp_res.size();
			double rsd_1 = SD(temp_res);
			update_result_scroll("\nAverage Normality = " + String.format("%.4f", res_normality) + "\n");
			update_result_scroll("Normality RSD = " + String.format("%.4f", rsd_1) + " %\n");
		}

		if (formula_no == 2) {
			double res_normality = 0;
			button_continue.setEnabled(false);
			ArrayList<Double> temp_res = new ArrayList<Double>();
			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 6).toString().matches("NA")) {
						res_normality = res_normality + Double.parseDouble(model.getValueAt(i, 6).toString());
						temp_res.add(Double.parseDouble(model.getValueAt(i, 6).toString()));
					}
				}
			}
			res_normality = res_normality / temp_res.size();
			double rsd_1 = SD(temp_res);
			update_result_scroll("\nAverage Analyte = " + String.format("%.4f", res_normality) + "\n");
			update_result_scroll("Analyte RSD = " + String.format("%.4f", rsd_1) + " %\n");
		}
		if (formula_no == 3) {
			button_continue.setEnabled(false);
			double res_normality = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 6).toString().matches("NA")) {
						res_normality = res_normality + Double.parseDouble(model.getValueAt(i, 6).toString());
						temp_res.add(Double.parseDouble(model.getValueAt(i, 6).toString()));
					}
				}
			}
			res_normality = res_normality / temp_res.size();
			double rsd_1 = SD(temp_res);
			update_result_scroll("\nAverage Analyte = " + String.format("%.4f", res_normality) + "\n");
			update_result_scroll("Analyte RSD = " + String.format("%.4f", rsd_1) + " %\n");
		}
		if (formula_no == 4) {
			button_continue.setEnabled(false);
			double res_normality = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 6).toString().matches("NA")) {
						res_normality = res_normality + Double.parseDouble(model.getValueAt(i, 6).toString());
						temp_res.add(Double.parseDouble(model.getValueAt(i, 6).toString()));
					}
				}
			}
			res_normality = res_normality / temp_res.size();
			double rsd_1 = SD(temp_res);
			update_result_scroll("\nAverage Analyte = " + String.format("%.4f", res_normality) + "\n");
			update_result_scroll("Analyte RSD = " + String.format("%.4f", rsd_1) + " %\n");
		}
		if (formula_no == 5) {
			button_continue.setEnabled(false);
			double res_normality = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 7).toString().matches("NA")) {
						res_normality = res_normality + Double.parseDouble(model.getValueAt(i, 7).toString());
						temp_res.add(Double.parseDouble(model.getValueAt(i, 7).toString()));
					}
				}
			}
			res_normality = res_normality / temp_res.size();
			double rsd_1 = SD(temp_res);
			update_result_scroll("\nAverage Analyte = " + String.format("%.4f", res_normality) + "\n");
			update_result_scroll("Analyte RSD = " + String.format("%.4f", rsd_1) + " %\n");
		}
		if (formula_no == 6) {
			button_continue.setEnabled(false);
			double res_analyteA = 0, res_analyteB = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			ArrayList<Double> temp_resB = new ArrayList<Double>();

			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 7).toString().matches("NA")) {
						res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 7).toString());
						res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 8).toString());

						temp_res.add(Double.parseDouble(model.getValueAt(i, 7).toString()));
						temp_resB.add(Double.parseDouble(model.getValueAt(i, 8).toString()));
					}
				}
			}

			res_analyteA = res_analyteA / temp_res.size();
			double rsd_1 = SD(temp_res);

			res_analyteB = res_analyteB / temp_res.size();
			double rsd_2 = SD(temp_resB);

			update_result_scroll("\nAverage AnalyteA = " + String.format("%.4f", res_analyteA) + "\n");
			update_result_scroll("Average AnalyteB = " + String.format("%.4f", res_analyteB) + "\n");
			update_result_scroll("Analyte RSD1 = " + String.format("%.4f", rsd_1) + " %\n");
			update_result_scroll("Analyte RSD2 = " + String.format("%.4f", rsd_2) + " %\n");
		}
		if (formula_no == 7) {
			button_continue.setEnabled(false);
			double res_analyteA = 0, res_analyteB = 0, res_analyteC = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			ArrayList<Double> temp_resB = new ArrayList<Double>();
			ArrayList<Double> temp_resC = new ArrayList<Double>();

			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 8).toString().matches("NA")) {
						res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 8).toString());
						res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 9).toString());
						res_analyteC = res_analyteC + Double.parseDouble(model.getValueAt(i, 10).toString());

						temp_res.add(Double.parseDouble(model.getValueAt(i, 8).toString()));
						temp_resB.add(Double.parseDouble(model.getValueAt(i, 9).toString()));
						temp_resC.add(Double.parseDouble(model.getValueAt(i, 10).toString()));
					}
				}
			}

			res_analyteA = res_analyteA / temp_res.size();
			double rsd_1 = SD(temp_res);
			res_analyteB = res_analyteB / temp_res.size();
			double rsd_2 = SD(temp_resB);
			res_analyteC = res_analyteC / temp_res.size();
			double rsd_3 = SD(temp_resC);

			update_result_scroll("\nAverage Analyte A = " + String.format("%.4f", res_analyteA) + "\n");
			update_result_scroll("Average Analyte B = " + String.format("%.4f", res_analyteB) + "\n");
			update_result_scroll("Average Analyte C = " + String.format("%.4f", res_analyteC) + "\n");
			update_result_scroll("Analyte RSD 1 = " + String.format("%.4f", rsd_1) + " %\n");
			update_result_scroll("Analyte RSD 2 = " + String.format("%.4f", rsd_2) + " %\n");
			update_result_scroll("Analyte RSD 3 = " + String.format("%.4f", rsd_3) + " %\n");
		}
		if (formula_no == 8) {
			button_continue.setEnabled(false);
			double res_analyteA = 0, res_analyteB = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			ArrayList<Double> temp_resB = new ArrayList<Double>();

			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 7).toString().matches("NA")) {
						res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 7).toString());
						res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 8).toString());

						temp_res.add(Double.parseDouble(model.getValueAt(i, 7).toString()));
						temp_resB.add(Double.parseDouble(model.getValueAt(i, 8).toString()));
					}
				}
			}

			res_analyteA = res_analyteA / temp_res.size();
			double rsd_1 = SD(temp_res);

			res_analyteB = res_analyteB / temp_res.size();
			double rsd_2 = SD(temp_resB);

			update_result_scroll("\nAverage AnalyteA = " + String.format("%.4f", res_analyteA) + "\n");
			update_result_scroll("Average AnalyteB = " + String.format("%.4f", res_analyteB) + "\n");
			update_result_scroll("Analyte RSD1 = " + String.format("%.4f", rsd_1) + " %\n");
			update_result_scroll("Analyte RSD2 = " + String.format("%.4f", rsd_2) + " %\n");
		}
		if (formula_no == 9) {
			button_continue.setEnabled(false);
			double res_analyteA = 0, res_analyteB = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			ArrayList<Double> temp_resB = new ArrayList<Double>();

			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 7).toString().matches("NA")) {
						res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 7).toString());
						res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 8).toString());

						temp_res.add(Double.parseDouble(model.getValueAt(i, 7).toString()));
						temp_resB.add(Double.parseDouble(model.getValueAt(i, 8).toString()));
					}
				}
			}

			res_analyteA = res_analyteA / temp_res.size();
			double rsd_1 = SD(temp_res);

			res_analyteB = res_analyteB / temp_res.size();
			double rsd_2 = SD(temp_resB);

			update_result_scroll("\nAverage Carbonate = " + String.format("%.4f", res_analyteA) + "\n");
			update_result_scroll("Average Alkali = " + String.format("%.4f", res_analyteB) + "\n");
			update_result_scroll("Analyte RSD1 = " + String.format("%.4f", rsd_1) + " %\n");
			update_result_scroll("Analyte RSD2 = " + String.format("%.4f", rsd_2) + " %\n");
		}
		if (formula_no == 10) {
			button_continue.setEnabled(false);
			double res_analyteA = 0, res_analyteB = 0, res_analyteC = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			ArrayList<Double> temp_resB = new ArrayList<Double>();
			ArrayList<Double> temp_resC = new ArrayList<Double>();

			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 8).toString().matches("NA")) {
						res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 8).toString());
						res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 9).toString());
						res_analyteC = res_analyteC + Double.parseDouble(model.getValueAt(i, 10).toString());

						temp_res.add(Double.parseDouble(model.getValueAt(i, 8).toString()));
						temp_resB.add(Double.parseDouble(model.getValueAt(i, 9).toString()));
						temp_resC.add(Double.parseDouble(model.getValueAt(i, 10).toString()));
					}
				}
			}

			res_analyteA = res_analyteA / temp_res.size();
			double rsd_1 = SD(temp_res);

			res_analyteB = res_analyteB / temp_res.size();
			double rsd_2 = SD(temp_resB);

			res_analyteC = res_analyteC / temp_res.size();
			double rsd_3 = SD(temp_resC);

			update_result_scroll("\nAverage BiCarbonate = " + String.format("%.4f", res_analyteA) + "\n");
			update_result_scroll("Average Carbonate = " + String.format("%.4f", res_analyteB) + "\n");
			update_result_scroll("Average Alkali = " + String.format("%.4f", res_analyteC) + "\n");
			update_result_scroll("Analyte RSD 1 = " + String.format("%.4f", rsd_1) + " %\n");
			update_result_scroll("Analyte RSD 2 = " + String.format("%.4f", rsd_2) + " %\n");
			update_result_scroll("Analyte RSD 3 = " + String.format("%.4f", rsd_3) + " %\n");
		}
		if (formula_no == 11) {
			button_continue.setEnabled(false);
			double res_analyteA = 0, res_analyteB = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			ArrayList<Double> temp_resB = new ArrayList<Double>();

			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 7).toString().matches("NA")) {
						res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 7).toString());
						res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 8).toString());

						temp_res.add(Double.parseDouble(model.getValueAt(i, 7).toString()));
						temp_resB.add(Double.parseDouble(model.getValueAt(i, 8).toString()));
					}
				}
			}

			res_analyteA = res_analyteA / temp_res.size();
			double rsd_1 = SD(temp_res);

			res_analyteB = res_analyteB / temp_res.size();
			double rsd_2 = SD(temp_resB);

			update_result_scroll("\nAverage BiCarbonate = " + String.format("%.4f", res_analyteA) + "\n");
			update_result_scroll("Average Carbonate = " + String.format("%.4f", res_analyteB) + "\n");
			update_result_scroll("Analyte RSD1 = " + String.format("%.4f", rsd_1) + " %\n");
			update_result_scroll("Analyte RSD2 = " + String.format("%.4f", rsd_2) + " %\n");
		}
		if (formula_no == 12) {
			button_continue.setEnabled(false);
			double res_analyteA = 0, res_analyteB = 0;
			ArrayList<Double> temp_res = new ArrayList<Double>();
			ArrayList<Double> temp_resB = new ArrayList<Double>();

			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked == true) {
					if (!model.getValueAt(i, 7).toString().matches("NA")) {
						res_analyteA = res_analyteA + Double.parseDouble(model.getValueAt(i, 7).toString());
						res_analyteB = res_analyteB + Double.parseDouble(model.getValueAt(i, 8).toString());

						temp_res.add(Double.parseDouble(model.getValueAt(i, 7).toString()));
						temp_resB.add(Double.parseDouble(model.getValueAt(i, 8).toString()));
					}
				}
			}

			res_analyteA = res_analyteA / temp_res.size();
			double rsd_1 = SD(temp_res);

			res_analyteB = res_analyteB / temp_res.size();
			double rsd_2 = SD(temp_resB);

			update_result_scroll("\nAverage AnalyteA = " + String.format("%.4f", res_analyteA) + "\n");
			update_result_scroll("Average AnalyteB = " + String.format("%.4f", res_analyteB) + "\n");
			update_result_scroll("Analyte RSD1 = " + String.format("%.4f", rsd_1) + " %\n");
			update_result_scroll("Analyte RSD2 = " + String.format("%.4f", rsd_2) + " %\n");
		}
	}

	public static void get_mg() {
		try {
			String result = (String) JOptionPane.showInputDialog(frame1,
					"Enter the weight of the sample in mg or microlitre. Add sample and then PRESS OK!", "User Input",
					JOptionPane.PLAIN_MESSAGE, null, null, "");
			sample_weight = Double.parseDouble(result);
		} catch (NullPointerException ne) {
			JOptionPane.showMessageDialog(null, "Please enter a value!");
			get_mg();

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Please enter a valid value!");
			get_mg();
		}
	}

	public static void add_row_to_five_column(int one, String two, String three, String four, String five) {
		model.addRow(new Object[0]);
		if (select_column) {
			model.setValueAt(true, one, 0);
			model.setValueAt(String.valueOf(one + 1), one, 1);
			model.setValueAt(two, one, 2);
			model.setValueAt(three, one, 3);
			model.setValueAt(four, one, 4);
			model.setValueAt(five, one, 5);
		} else {
			model.setValueAt(String.valueOf(one + 1), one, 0);
			model.setValueAt(two, one, 1);
			model.setValueAt(three, one, 2);
			model.setValueAt(four, one, 3);
			model.setValueAt(five, one, 4);
		}

		model.fireTableDataChanged();
	}

	public static void add_row_to_six_column(int one, String two, String three, String four, String five, String six) {
		model.addRow(new Object[0]);
		if (select_column) {
			model.setValueAt(true, one, 0);
			model.setValueAt(String.valueOf(one + 1), one, 1);
			model.setValueAt(two, one, 2);
			model.setValueAt(three, one, 3);
			model.setValueAt(four, one, 4);
			model.setValueAt(five, one, 5);
			model.setValueAt(six, one, 6);
		} else {
			model.setValueAt(String.valueOf(one + 1), one, 0);
			model.setValueAt(two, one, 1);
			model.setValueAt(three, one, 2);
			model.setValueAt(four, one, 3);
			model.setValueAt(five, one, 4);
			model.setValueAt(six, one, 5);
		}
		model.fireTableDataChanged();
	}

	public static void add_row_to_seven_column(int one, String two, String three, String four, String five, String six,
			String seven) {
		model.addRow(new Object[0]);
		if (select_column) {
			model.setValueAt(true, one, 0);
			model.setValueAt(String.valueOf(one + 1), one, 1);
			model.setValueAt(two, one, 2);
			model.setValueAt(three, one, 3);
			model.setValueAt(four, one, 4);
			model.setValueAt(five, one, 5);
			model.setValueAt(six, one, 6);
			model.setValueAt(seven, one, 7);

		} else {
			model.setValueAt(String.valueOf(one + 1), one, 0);
			model.setValueAt(two, one, 1);
			model.setValueAt(three, one, 2);
			model.setValueAt(four, one, 3);
			model.setValueAt(five, one, 4);
			model.setValueAt(six, one, 5);
			model.setValueAt(seven, one, 6);
		}
		model.fireTableDataChanged();
	}

	public static void add_row_to_eight_column(int one, String two, String three, String four, String five, String six,
			String seven, String eight) {
		model.addRow(new Object[0]);
		if (select_column) {
			model.setValueAt(true, one, 0);
			model.setValueAt(String.valueOf(one + 1), one, 1);
			model.setValueAt(two, one, 2);
			model.setValueAt(three, one, 3);
			model.setValueAt(four, one, 4);
			model.setValueAt(five, one, 5);
			model.setValueAt(six, one, 6);
			model.setValueAt(seven, one, 7);
			model.setValueAt(eight, one, 8);
		} else {
			model.setValueAt(String.valueOf(one + 1), one, 0);
			model.setValueAt(two, one, 1);
			model.setValueAt(three, one, 2);
			model.setValueAt(four, one, 3);
			model.setValueAt(five, one, 4);
			model.setValueAt(six, one, 5);
			model.setValueAt(seven, one, 6);
			model.setValueAt(eight, one, 7);
		}
		model.fireTableDataChanged();
	}

	public static void add_row_to_nine_column(int one, String two, String three, String four, String five, String six,
			String seven, String eight, String nine) {
		model.addRow(new Object[0]);
		if (select_column) {
			model.setValueAt(true, one, 0);
			model.setValueAt(String.valueOf(one + 1), one, 1);
			model.setValueAt(two, one, 2);
			model.setValueAt(three, one, 3);
			model.setValueAt(four, one, 4);
			model.setValueAt(five, one, 5);
			model.setValueAt(six, one, 6);
			model.setValueAt(seven, one, 7);
			model.setValueAt(eight, one, 8);
			model.setValueAt(nine, one, 9);

		} else {
			model.setValueAt(String.valueOf(one + 1), one, 0);
			model.setValueAt(two, one, 1);
			model.setValueAt(three, one, 2);
			model.setValueAt(four, one, 3);
			model.setValueAt(five, one, 4);
			model.setValueAt(six, one, 5);
			model.setValueAt(seven, one, 6);
			model.setValueAt(eight, one, 7);
			model.setValueAt(nine, one, 8);

		}
		model.fireTableDataChanged();
	}

	public static void add_row_to_ten_column(int one, String two, String three, String four, String five, String six,
			String seven, String eight, String nine, String ten) {
		model.addRow(new Object[0]);
		if (select_column) {
			model.setValueAt(true, one, 0);
			model.setValueAt(String.valueOf(one + 1), one, 1);
			model.setValueAt(two, one, 2);
			model.setValueAt(three, one, 3);
			model.setValueAt(four, one, 4);
			model.setValueAt(five, one, 5);
			model.setValueAt(six, one, 6);
			model.setValueAt(seven, one, 7);
			model.setValueAt(eight, one, 8);
			model.setValueAt(nine, one, 9);
			model.setValueAt(ten, one, 10);
		} else {
			model.setValueAt(String.valueOf(one + 1), one, 0);
			model.setValueAt(two, one, 1);
			model.setValueAt(three, one, 2);
			model.setValueAt(four, one, 3);
			model.setValueAt(five, one, 4);
			model.setValueAt(six, one, 5);
			model.setValueAt(seven, one, 6);
			model.setValueAt(eight, one, 7);
			model.setValueAt(nine, one, 8);
			model.setValueAt(ten, one, 9);
		}
		model.fireTableDataChanged();
	}

	public static void five_column(String col1, String col2, String col3, String col4, String col5) {
		select_column = get_permission();
		current_cloumn_count = 5;

		JScrollPane scrollPane = new JScrollPane();
		table1 = new JTable();
		scrollPane.setBounds((int) Math.round(0.39 * wid), (int) Math.round(0.759 * hei), (int) Math.round(0.585 * wid),
				(int) Math.round(0.181 * hei)); // (int) Math.round(0.45 * hei)
		table1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0183 * hei)));
		frame1.getContentPane().add(scrollPane);

		if (select_column) {
			model = new DefaultTableModel() {
				public Class<?> getColumnClass(int column) {
					switch (column) {
					case 0:
						return Boolean.class;
					case 1:
						return String.class;
					case 2:
						return String.class;
					case 3:
						return String.class;
					case 4:
						return String.class;
					default:
						return String.class;
					}
				}
			};
			table1.setModel(model);
			model.addColumn("Select");
			model.addColumn(col1);
			model.addColumn(col2);
			model.addColumn(col3);
			model.addColumn(col4);
			model.addColumn(col5);
			button_generate_result.setVisible(true);
			button_saveReport.setVisible(true);
		} else {
			model = new DefaultTableModel() {

	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 0:
			return String.class;
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

	};table1.setModel(model);model.addColumn(col1);model.addColumn(col2);model.addColumn(col3);model.addColumn(col4);model.addColumn(col5);button_saveReport.setEnabled(true);

	}table1.setRowHeight((int)Math.round(0.0306*hei));table1.setDefaultEditor(Object.class,null);

	scrollPane.setViewportView(table1);}

	public static void six_column(String col1, String col2, String col3, String col4, String col5, String col6) {
		select_column = get_permission();
		current_cloumn_count = 6;

		JScrollPane scrollPane = new JScrollPane();
		table1 = new JTable();
		scrollPane.setBounds((int) Math.round(0.39 * wid), (int) Math.round(0.759 * hei), (int) Math.round(0.585 * wid),
				(int) Math.round(0.181 * hei)); // (int) Math.round(0.45 * hei)
		table1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0183 * hei)));
		frame1.getContentPane().add(scrollPane);

		if (select_column) {
			model = new DefaultTableModel() {
				public Class<?> getColumnClass(int column) {
					switch (column) {
					case 0:
						return Boolean.class;
					case 1:
						return String.class;
					case 2:
						return String.class;
					case 3:
						return String.class;
					case 4:
						return String.class;
					case 5:
						return String.class;
					default:
						return String.class;
					}
				}
			};
			table1.setModel(model);
			model.addColumn("Select");
			model.addColumn(col1);
			model.addColumn(col2);
			model.addColumn(col3);
			model.addColumn(col4);
			model.addColumn(col5);
			model.addColumn(col6);
			button_generate_result.setVisible(true);
			button_saveReport.setVisible(true);


		} else {
			model = new DefaultTableModel() {

	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		case 4:
			return String.class;
		default:
			return String.class;
		}
	}

	};table1.setModel(model);model.addColumn(col1);model.addColumn(col2);model.addColumn(col3);model.addColumn(col4);model.addColumn(col5);model.addColumn(col6);button_saveReport.setEnabled(true);

	}table1.setRowHeight((int)Math.round(0.0306*hei));table1.setDefaultEditor(Object.class,null);scrollPane.setViewportView(table1);}

	public static void seven_column(String col1, String col2, String col3, String col4, String col5, String col6,
			String col7) {
		select_column = get_permission();
		current_cloumn_count = 7;

		JScrollPane scrollPane = new JScrollPane();
		table1 = new JTable();
		scrollPane.setBounds((int) Math.round(0.39 * wid), (int) Math.round(0.759 * hei), (int) Math.round(0.585 * wid),
				(int) Math.round(0.181 * hei)); // (int) Math.round(0.45 * hei)
		table1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0183 * hei)));
		frame1.getContentPane().add(scrollPane);

		if (select_column) {
			model = new DefaultTableModel() {
				public Class<?> getColumnClass(int column) {
					switch (column) {
					case 0:
						return Boolean.class;
					case 1:
						return String.class;
					case 2:
						return String.class;
					case 3:
						return String.class;
					case 4:
						return String.class;
					case 5:
						return String.class;
					case 6:
						return String.class;
					default:
						return String.class;
					}
				}
			};
			table1.setModel(model);
			model.addColumn("Select");
			model.addColumn(col1);
			model.addColumn(col2);
			model.addColumn(col3);
			model.addColumn(col4);
			model.addColumn(col5);
			model.addColumn(col6);
			model.addColumn(col7);
			button_generate_result.setVisible(true);
			button_saveReport.setVisible(true);


		} else {
			model = new DefaultTableModel() {

	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		case 4:
			return String.class;
		case 5:
			return String.class;
		default:
			return String.class;
		}
	}

	};table1.setModel(model);model.addColumn(col1);model.addColumn(col2);model.addColumn(col3);model.addColumn(col4);model.addColumn(col5);model.addColumn(col6);model.addColumn(col7);button_saveReport.setEnabled(true);}table1.setRowHeight((int)Math.round(0.0306*hei));table1.setDefaultEditor(Object.class,null);scrollPane.setViewportView(table1);}

	public static void eight_column(String col1, String col2, String col3, String col4, String col5, String col6,
			String col7, String col8) {
		select_column = get_permission();
		current_cloumn_count = 8;

		JScrollPane scrollPane = new JScrollPane();
		table1 = new JTable();
		scrollPane.setBounds((int) Math.round(0.39 * wid), (int) Math.round(0.759 * hei), (int) Math.round(0.585 * wid),
				(int) Math.round(0.181 * hei)); // (int) Math.round(0.45 * hei)
		table1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0183 * hei)));
		frame1.getContentPane().add(scrollPane);

		if (select_column) {
			model = new DefaultTableModel() {
				public Class<?> getColumnClass(int column) {
					switch (column) {
					case 0:
						return Boolean.class;
					case 1:
						return String.class;
					case 2:
						return String.class;
					case 3:
						return String.class;
					case 4:
						return String.class;
					case 5:
						return String.class;
					case 6:
						return String.class;
					case 7:
						return String.class;
					default:
						return String.class;
					}
				}
			};
			table1.setModel(model);
			model.addColumn("Select");
			model.addColumn(col1);
			model.addColumn(col2);
			model.addColumn(col3);
			model.addColumn(col4);
			model.addColumn(col5);
			model.addColumn(col6);
			model.addColumn(col7);
			model.addColumn(col8);
			button_generate_result.setVisible(true);
			button_saveReport.setVisible(true);

		} else {
			model = new DefaultTableModel() {

	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		case 4:
			return String.class;
		case 5:
			return String.class;
		case 6:
			return String.class;
		default:
			return String.class;
		}
	}

	};table1.setModel(model);model.addColumn(col1);model.addColumn(col2);model.addColumn(col3);model.addColumn(col4);model.addColumn(col5);model.addColumn(col6);model.addColumn(col7);model.addColumn(col8);button_saveReport.setEnabled(true);

	}table1.setRowHeight((int)Math.round(0.0306*hei));table1.setDefaultEditor(Object.class,null);scrollPane.setViewportView(table1);}

	public static void nine_column(String col1, String col2, String col3, String col4, String col5, String col6,
			String col7, String col8, String col9) {
		select_column = get_permission();
		current_cloumn_count = 9;

		JScrollPane scrollPane = new JScrollPane();
		table1 = new JTable();
		scrollPane.setBounds((int) Math.round(0.39 * wid), (int) Math.round(0.759 * hei), (int) Math.round(0.585 * wid),
				(int) Math.round(0.181 * hei)); // (int) Math.round(0.45 * hei)
		table1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0183 * hei)));
		frame1.getContentPane().add(scrollPane);

		if (select_column) {
			model = new DefaultTableModel() {
				public Class<?> getColumnClass(int column) {
					switch (column) {
					case 0:
						return Boolean.class;
					case 1:
						return String.class;
					case 2:
						return String.class;
					case 3:
						return String.class;
					case 4:
						return String.class;
					case 5:
						return String.class;
					case 6:
						return String.class;
					case 7:
						return String.class;
					case 8:
						return String.class;
					default:
						return String.class;
					}
				}
			};
			table1.setModel(model);
			model.addColumn("Select");
			model.addColumn(col1);
			model.addColumn(col2);
			model.addColumn(col3);
			model.addColumn(col4);
			model.addColumn(col5);
			model.addColumn(col6);
			model.addColumn(col7);
			model.addColumn(col8);
			model.addColumn(col9);
			button_generate_result.setVisible(true);
			button_saveReport.setVisible(true);

		} else {
			model = new DefaultTableModel() {

	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		case 4:
			return String.class;
		case 5:
			return String.class;
		case 6:
			return String.class;
		case 7:
			return String.class;
		default:
			return String.class;
		}
	}

	};table1.setModel(model);model.addColumn(col1);model.addColumn(col2);model.addColumn(col3);model.addColumn(col4);model.addColumn(col5);model.addColumn(col6);model.addColumn(col7);model.addColumn(col8);model.addColumn(col9);button_saveReport.setEnabled(true);

	}table1.setRowHeight((int)Math.round(0.0306*hei));table1.setDefaultEditor(Object.class,null);scrollPane.setViewportView(table1);}

	public static void ten_column(String col1, String col2, String col3, String col4, String col5, String col6,
			String col7, String col8, String col9, String col10) {
		select_column = get_permission();
		current_cloumn_count = 10;

		JScrollPane scrollPane = new JScrollPane();
		table1 = new JTable();
		scrollPane.setBounds((int) Math.round(0.39 * wid), (int) Math.round(0.759 * hei), (int) Math.round(0.585 * wid),
				(int) Math.round(0.181 * hei));
		table1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0183 * hei)));
		frame1.getContentPane().add(scrollPane);

		if (select_column) {
			model = new DefaultTableModel() {
				public Class<?> getColumnClass(int column) {
					switch (column) {
					case 0:
						return Boolean.class;
					case 1:
						return String.class;
					case 2:
						return String.class;
					case 3:
						return String.class;
					case 4:
						return String.class;
					case 5:
						return String.class;
					case 6:
						return String.class;
					case 7:
						return String.class;
					case 8:
						return String.class;
					case 9:
						return String.class;
					default:
						return String.class;
					}
				}
			};
			table1.setModel(model);
			model.addColumn("Select");
			model.addColumn(col1);
			model.addColumn(col2);
			model.addColumn(col3);
			model.addColumn(col4);
			model.addColumn(col5);
			model.addColumn(col6);
			model.addColumn(col7);
			model.addColumn(col8);
			model.addColumn(col9);
			model.addColumn(col10);
			button_generate_result.setVisible(true);
			button_saveReport.setVisible(true);

		} else {
			model = new DefaultTableModel() {

	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		case 4:
			return String.class;
		case 5:
			return String.class;
		case 6:
			return String.class;
		case 7:
			return String.class;
		case 8:
			return String.class;
		default:
			return String.class;
		}
	}

	};table1.setModel(model);model.addColumn(col1);model.addColumn(col2);model.addColumn(col3);model.addColumn(col4);model.addColumn(col5);model.addColumn(col6);model.addColumn(col7);model.addColumn(col8);model.addColumn(col9);model.addColumn(col10);button_saveReport.setEnabled(true);

	}table1.setRowHeight((int)Math.round(0.0306*hei));table1.setDefaultEditor(Object.class,null);scrollPane.setViewportView(table1);}

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == b) {
			if (e.getStateChange() == 1) {
			}
		} else {
			if (e.getStateChange() == 1) {
			}
		}
	}

	private XYDataset createDataset() {
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		return dataset;
	}

	private JFreeChart createChart(final XYDataset dataset) {
		chart = ChartFactory.createXYLineChart("Potentiometry", // chart title
				"mL", // x axis label
				"mV", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, false, // include legend
				true, // tooltips
				false // urls
		);
		return chart;
	}

	public static void update_result_scroll(String msg) {
		display.setText(display.getText().toString() + msg);
	}

	public static void main(String[] args) {

		// System.out.println("MAAAIIINNNNN DRAWWW GRAPHHHH");

		if (args.length != 0) {

//			System.out.println("method_name22 = " + args[0]);
//			System.out.println("method_data22 = " + args[1]);
//			System.out.println("ar22 = " + args[2]);
//			System.out.println("batch22 = " + args[3]);
//			System.out.println("sample_name22 = " + args[4]);
//			System.out.println("normality_val22 = " + args[5]);
//			System.out.println("moisture_val22 = " + args[6]);
//			System.out.println("Report Name = " + args[7]);
//			System.out.println("titrant_name22 = " + args[8]);

			moisture = Double.valueOf(args[6]);
			titrant_normality = Double.valueOf(args[5]);

			metd_name = "";
			metd_name = args[0];
			db_report_name = args[7];
			variables_string = args[1];
			variables = args[1].split(",");

			pre_dose = Double.valueOf(variables[0]);
			stir_time = Double.valueOf(variables[1]);
			max_vol = Double.valueOf(variables[2]);
			bvolume = Double.valueOf(variables[3]);
			burette_factor = Double.valueOf(variables[4]);
			threshold_val = Double.valueOf(variables[5]);
			filter_val = Double.valueOf(variables[6]);
			doserate_val = Double.valueOf(variables[7]);
			dose_speed = (float) doserate_val;
			no_of_trials = Integer.parseInt(variables[8]);
			trial_cnt = Integer.parseInt(variables[8]);
			factor1 = Double.valueOf(variables[9]);
			factor2 = Double.valueOf(variables[10]);
			factor3 = Double.valueOf(variables[11]);
			factor4 = Double.valueOf(variables[12]);
			String ep_select1 = (variables[13]);
			formula_no = Integer.parseInt(variables[14]);
			String tendency1 = variables[15];
			String result_unit1 = variables[16];

			// res_unit

			if (result_unit1.matches("%")) {
				res_unit = 100;
			} else if (result_unit1.toLowerCase().matches("ppm")) {
				res_unit = 1000000;
			} else if (result_unit1.toLowerCase().matches("Normality")) {
				res_unit = 1;
			}

			String sop_val1 = variables[17];

			math = args[9];
			math2 = args[10];
			math3 = args[11];
			System.out.println("No of trials = " + no_of_trials);
//			System.out.println("Math2 = " + math2);
//			System.out.println("Math3 = " + math3);
//			
			user_name = args[12];
			user_permissions = args[13];

			db_parameters = args[12] + "," + get_date() + "," + get_time() + "," + args[0] + "," + pre_dose + ","
					+ stir_time + "," + max_vol + "," + bvolume + "," + burette_factor + "," + threshold_val + ","
					+ filter_val + "," + doserate_val + "," + no_of_trials + "," + factor1 + "," + factor2 + ","
					+ factor3 + "," + factor4 + "," + ep_select1 + "," + formula_no + "," + tendency1 + ","
					+ result_unit1 + "," + sop_val1 + "," + args[2] + "," + args[3] + "," + args[4] + "," + args[5]
					+ "," + args[6] + "," + args[7] + "," + args[8] + "," + "Not Certified";
		}

		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame1.getGraphicsConfiguration());
		int taskHeight = screenInsets.bottom;
		Dimension dh = Toolkit.getDefaultToolkit().getScreenSize();
		hei = dh.height - taskHeight;
		wid = dh.width;

//        hei = 720;
//        wid = 1280;

		// System.out.println(wid + " dfvdvdv " + hei);
		frame1.setBounds(0, 0, wid, hei);
		// frame1.setBounds(0,0,1280,720);

		frame1.add(p);
		frame1.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame1.getContentPane().add(new DrawGraph_pot());
		frame1.setResizable(true);
		frame1.setVisible(true);
		frame1.repaint();
		ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
		frame1.setIconImage(img.getImage());
		ReformatBuffer.current_exp = "pot";

		frame1.setTitle("Potentiometry        Logged in as " + user_name);

		frame1.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				int rc, checked = 0;

				if (report_saved == 1) {
					int result = JOptionPane.showConfirmDialog(null, "Save report ? ", "Report not Saved!",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						if (select_column == true)
							save_report();
						else
							call_formula_report();
						report_saved = 2;
						checked = 0;
					} else if (result == JOptionPane.NO_OPTION) {

					} else {

					}
				}
				try {
					Thread.sleep(400);
					output_dg.print("<8888>ESCP*");
					output_dg.flush();
				} catch (InterruptedException ex) {
				} catch (NullPointerException ee) {
					// JOptionPane.showMessageDialog(null, "Please select the ComPort!");
				}
				try {
					sp1.closePort();
					exec_dg.shutdown();
				} catch (NullPointerException npe) {

				}
				// delete_from_db();
				check_details_from_db();

				data_X.clear();
				data_Y.clear();
				db_report_name = "";
				db_parameters = "";
				db_details = "";
				db_graph_x = "";
				db_graph_y = "";
				db_header = "";
				db_trial_data = "";
				db_results = "";
				user_name = "";
				user_permissions = "";
				trial_cnt = 0;
				cur_trial = 1;
				data_array.clear();
				series.clear();

				fill = 0;
				dose = 0;
				predose = 0;
				afill_first = false;
				experiment_started = true;

				if (cur_trial != 1) {
					for (int x = 0; x < d.length; x++)
						d[x] = 0;
					cur_val = new double[20];
					end_point_mv = new double[2000];
					end_position = new double[2000];
					End_Point = new double[2000];
					slp_diff = new double[2000];
					diff = new double[20];
					for (int x = 0; x < data1.length; x++)
						data1[x] = 0;
					size = 0;
					end_line = 0;
					total_points = 0;
					end_point_no = 0;
					flag_stop = true;
					typ = 0;
					ee = 0;
					k = 0;
					m1 = 0;
					ky = 0;
					d_flag = 0;
					end_count = 0;
					delta = 0.6;
					t = 0;
					k1 = 0;
					k2 = 0;
					c2 = 0;
					newdata1 = 0;
					i = 0;
					graph_mul1 = 1;
					diff0 = 0;
					diff1 = 0;
					diff2 = 0;
					slope = 0;
					corres_fact = 0;
					ep_continue1 = 0;
					ep_continue2 = 0;
					ep_continue3 = 0;
					dossage_speed = 0;
					db_remarks = "No Remarks";
					threshold_array.clear();
					threshold_array = new Stack<String>();
				}

				if ((cur_trial - 1 != no_of_trials)) {
					try {
						audit_log_push.push_to_audit(get_date(), get_time(), user_name,
								(cur_trial - 1) + " trial completed out of " + no_of_trials + " trial");
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				} else if ((no_of_trials == 1 && (cur_trial == no_of_trials))) {
					try {
						audit_log_push.push_to_audit(get_date(), get_time(), user_name,
								0 + " trial completed out of " + no_of_trials + " trial");
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}
				try {
					audit_log_push.push_to_audit(get_date(), get_time(), user_name, "Potentiometry closed!");
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				frame1.dispose();
				frame1 = new JFrame();
				p = new JPanel();
				p.revalidate();
				p.repaint();

			}
		});
	}

	public static void add_to_db() {
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql;
		boolean present = false;
		try {
			sql = "INSERT INTO potentiometry(report_name,date,parameters,details,graph_dataX,graph_dataY,header,trial_data,results,remarks) VALUES(?,?,?,?,?,?,?,?,?,?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, db_report_name);
			ps.setString(2, get_date());
			ps.setString(3, db_parameters);
			ps.setString(4, db_details);
			ps.setString(5, "");
			ps.setString(6, "");
			ps.setString(7, db_header);
			ps.setString(8, "");
			ps.setString(9, "");
			ps.setString(10, "No Remarks");
			ps.executeUpdate();
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, "Report Name Exists!");
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e1) {
				System.out.println("Report Name Exists!");
			}
		}
	}

	public static void check_details_from_db() {

		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql;
		sql = "SELECT details FROM potentiometry WHERE report_name = '" + db_report_name + "'";
		try {
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			String details = rs.getString("details");
			if (details.matches("")) {
				try {
					sql = "DELETE FROM potentiometry WHERE report_name = ?";
					ps = con.prepareStatement(sql);
					ps.setString(1, db_report_name);
					ps.executeUpdate();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1);
				}
			}
			// delete_from_db();
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

	public static void delete_from_db() {

		// if (select_column == false && trials_completed == false) {
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql;
		boolean present = false;
		try {
			sql = "DELETE FROM potentiometry WHERE report_name = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, db_report_name);
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
		// }
	}

	public static boolean get_permission() {
		boolean temp_result = true;
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql;
		sql = "SELECT permision FROM burette_factor WHERE SlNo = '1'";
		try {
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			temp_result = ((rs.getString("permision").matches("true")) ? true : false);
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
		return temp_result;
	}
}
//try {
//OutputStream out = new FileOutputStream("C:\\sqlite\\chart\\chart.png"); 
//ChartUtilities.writeChartAsPNG(out, chart, chartPanel.getWidth(), chartPanel.getHeight()); 
//}
//catch (IOException ex){}

//for(int i=0;i<table1.getRowCount();i++ ) {
//	Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
//	String aa=table1.getValueAt(i, 1).toString();
//	System.out.println("checked = "+checked+ " value = "+aa);
//}

//db_details = db_details + "[ " + get_time() + " ]  Std. by H2O - RSD = " + String.format("%.4f", rsd) + " %,";

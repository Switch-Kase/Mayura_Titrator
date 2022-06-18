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

public class DrawGraph_kf extends JPanel implements ItemListener {

	static int wid = 0, hei = 0;
	static JFrame frame1 = new JFrame();
	static JPanel p = new JPanel();
	static JPanel panel_result;
	static JTable table1, table11, table2;
	static JLabel kf_mv_display, result_header, result, vol_filled, vol_dosed, delay_timer, experiment_performing;
	static JTextField textField;
	static JTextArea display;
	static JRadioButton b, b1;
	static JButton button_prerun, button_blankrun, button_std_by_h2o, button_std_by_sodium, button_analysis,
			button_home, button_sop, button_esc, update_analyze, viewReport, saveReport, update_kf_factor,
			update_blankvol;
	static String[] variables = new String[17];
	static String temp_status = "", mv_check_state = "", user_name = "", permission_items = "",roles_list = "", db_report_name = "",
			db_parameters = "", db_details = "", db_kff_trials = "", db_kff_results = "", db_moisture_trials = "",
			db_moisture_results = "", metd_name, metd_data, delay_val, stir_time, max_vol, blank_vol, burette_factor,
			density, kf_factor, end_point, dosage_rate, result_unit, no_of_trials, sop, current_process = "",
			prev_process = "";
	static DefaultTableModel model, model3;
	static PrintWriter output_dg;
	static ScheduledExecutorService exec_dg_kf_fill, exec_dg_kf_one, exec_dg_kf_dosr;
	static ScheduledExecutorService exec_dg_kf_timer = Executors.newSingleThreadScheduledExecutor();
	static int j = 1, row = 0, int_mv_val = 0, time = 15, trial_cnt = 0, cur_trial = 0, v;
	static double fill, dose, predose, double_mv_val, sample_weight = 0;
	static boolean start_checking = true, pre_run_completed = false, sent_cvok = false, pre_run_middle = false,
			blank_run_conducted = false, std_h20_conducted = false, std_disodium_conducted = false,
			analysis_conducted = false, kf_done = false, select_column = true;
	static double[][] kff_arr = new double[5][3];
	static double[][] moisture_arr = new double[5][3];
	static SerialPort sp1;
	
	static String[] kf_trial_timings = new String[5];
	static String[] moisture_trial_timings = new String[5];

	static String result_timings = "";
	
	public static void reset() {
		variables = new String[17];
		temp_status = ""; mv_check_state = ""; user_name = ""; permission_items = "";roles_list = ""; db_report_name = "";
				db_parameters = ""; db_details = ""; db_kff_trials = ""; db_kff_results = ""; db_moisture_trials = "";
				db_moisture_results = "";current_process = "";
				prev_process = "";
		while (model.getRowCount()>0)
		{
			model.removeRow(0);
		}while (model3.getRowCount()>0)
		{
			model3.removeRow(0);
		}
		j = 1; row = 0; int_mv_val = 0; time = 15; trial_cnt = 0; cur_trial = 0;
		sample_weight = 0;
		start_checking = true; pre_run_completed = false; sent_cvok = false; pre_run_middle = false;
		blank_run_conducted = false; std_h20_conducted = false; std_disodium_conducted = false;
		analysis_conducted = false; kf_done = false; select_column = true;
		kff_arr = new double[5][3];
		moisture_arr = new double[5][3];
		
		kf_trial_timings = new String[5];
		moisture_trial_timings = new String[5];

		result_timings = "";
		
	}

	public static void port_setup(SerialPort sp) {
		output_dg = new PrintWriter(sp.getOutputStream());
		sp1 = sp;
		Float aa = Float.valueOf(dosage_rate);
		int dr = Math.round(aa);
		if (aa == 0.5) {
			try {
				Thread.sleep(500);
				output_dg.print("<8888>DOSR,017*");
				output_dg.flush();
			} catch (InterruptedException ex) {
			} catch (NullPointerException ee) {
				JOptionPane.showMessageDialog(null, "Please select the ComPort!");
			}
			ReformatBuffer.current_state = "dg_kf_dosr";
		} else {
			try {
				Thread.sleep(500);
				String s = String.format("%03d", dr);
				System.out.println("Dosage rate 2 = " + s);
				output_dg.print("<8888>DOSR," + s + "*");
				output_dg.flush();
				ReformatBuffer.current_state = "dg_kf_dosr";
			} catch (InterruptedException ex) {
			} catch (NullPointerException ee) {
				JOptionPane.showMessageDialog(null, "Please select the ComPort!");
			}
		}
	}

	public static void dosr_ok_received() {
		JOptionPane.showMessageDialog(null, "Equipment Ready!");
		send_cvok();
	}

	public static void update_mv_kf(String msg) {
		msg = msg.replaceAll("\\\n", "");
		msg = msg.replaceAll("\\\t", "");
		String[] temp = new String[2];
		if (msg.contains("T")) {
			temp = msg.split("T");
		} else if (msg.contains("N")) {
			temp = msg.split("N");
		}
		String mv_val_str = "";
		int start, end;
		String[] temp_p = temp[1].split("");
		for (int i = 0; i < 4; i++) {
			mv_val_str = mv_val_str + temp_p[i];
		}
		int_mv_val = Integer.parseInt(mv_val_str);
		double_mv_val = Double.parseDouble(mv_val_str);
		if (msg.contains("N"))
			int_mv_val = -int_mv_val;
		kf_mv_display.setText(int_mv_val + " mV");
		if (current_process.matches("pre_run_started") || current_process.matches("blank_run_started")) {
			check_mv(int_mv_val);
		}
	}

	public static void send_cvok() {
		if (sent_cvok == false) {
			try {
				Thread.sleep(500);
				output_dg.print("<8888>CVOK*");
				output_dg.flush();
				ReformatBuffer.current_state = "dg_kf_cvok";
			} catch (InterruptedException ex) {
			} catch (NullPointerException ee) {
				JOptionPane.showMessageDialog(null, "Please select the ComPort!");
			}
		}
		else {
			send_afil();
		}
	}

	public static void cvok_ok_received() {
		sent_cvok = true;
		//send_afil();
	}

	public static void send_afil() {
		try {
			Thread.sleep(500);
			output_dg.print("<8888>AFIL*");
			output_dg.flush();
			ReformatBuffer.current_state = "dg_kf_afil";
		} catch (InterruptedException ex) {
		} catch (NullPointerException ee) {
			JOptionPane.showMessageDialog(null, "Please select the ComPort!");
		}
	}

	public static void afill_ok_received() {
		System.out.println("KKKKFFFF  AFILLL OK Recieved");
		exec_dg_kf_fill = Executors.newSingleThreadScheduledExecutor();
		exec_dg_kf_fill.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				// do stuff
				fill = fill + ((16 / 60.0) / 10.0);
				vol_filled.setText("Volume filled : " + String.format("%.3f", fill) + "mL");
			}
		}, 0, 100, TimeUnit.MILLISECONDS);
	}

	public static void afill_end_received() {
		exec_dg_kf_fill.shutdown();
		if (current_process.matches("pre_run") || current_process.matches("pre_run_started_afill")) {
			if ((int) double_mv_val < Integer.parseInt(end_point))// && pre_run_completed == true
			{
				JOptionPane.showMessageDialog(null, "Pre-Run cannot continue if mV < End-Point. Please Check!");
				button_prerun.setEnabled(true);
			} else {
				send_dose();
			}
		} else if (current_process.matches("blank_run") && (int) double_mv_val < Integer.parseInt(end_point)) {
			get_mg();
		} else if (current_process.matches("blank_run_started_afill")) {
			send_dose();
		} else {
			JOptionPane.showMessageDialog(null, "Pre-Run Required!");
			button_prerun.setEnabled(false);
			if (temp_status.matches("a")) {
				temp_status = "am";
			} else if (temp_status.matches("h")) {
				temp_status = "hm";
			} else if (temp_status.matches("t")) {
				temp_status = "tm";
			} else if (temp_status.matches("b")) {
				temp_status = "bm";
			}

			start_checking = true;
			dose = 0;
			fill = 0;
			send_cvok();
			current_process = "pre_run";
			experiment_performing.setText("Experiment Performing : PreRun");
		}
	}

	public static void send_dose() {
		try {
			Thread.sleep(50);
			output_dg.print("<8888>DOSE*");
			output_dg.flush();
			ReformatBuffer.current_state = "dg_kf_dose";
		} catch (InterruptedException ex) {
		} catch (NullPointerException ee) {
			JOptionPane.showMessageDialog(null, "Please select the ComPort!");
		}
	}

	public static void dose_ok_received() {
		if (current_process.matches("pre_run")) {
			current_process = "pre_run_started";
		} else if (current_process.matches("pre_run_started_afill")) {
			current_process = "pre_run_started";
			mv_check_state = "";
		} else if (current_process.matches("blank_run")) {
			current_process = "blank_run_started";
		} else if (current_process.matches("blank_run_started_afill")) {
			current_process = "blank_run_started";
			mv_check_state = "";
		}
	}

	public static void dose_end_received() {
		if (current_process.matches("pre_run_started")) {
			if (mv_check_state.matches("g_hundred")) {
				exec_dg_kf_dosr.shutdown();
			}
			if (mv_check_state.matches("g_one")) {
				exec_dg_kf_one.shutdown();
			}
			current_process = "pre_run_started_afill";
			send_afil();
		} else if (current_process.matches("blank_run_started")) {
			if (mv_check_state.matches("g_hundred")) {
				exec_dg_kf_dosr.shutdown();
			}
			if (mv_check_state.matches("g_one")) {
				exec_dg_kf_one.shutdown();
			}
			current_process = "blank_run_started_afill";
			send_afil();
		}
	}

	public static void send_dose_timer() {
		try {
			Thread.sleep(50);
			output_dg.print("<8888>DOSE*");
			output_dg.flush();
			ReformatBuffer.current_state = "dg_kf_dose_timer";
		} catch (InterruptedException ex) {
		} catch (NullPointerException ee) {
			JOptionPane.showMessageDialog(null, "Please select the ComPort!");
		}
	}

	public static void dose_ok_timer_received() {
		check_mv(int_mv_val);
		prev_process = "";
	}

	public static void check_mv(int mv) {
		int ep = Integer.parseInt(end_point);
		if (start_checking == true) {
			if (mv <= 100 && mv >= ep) {
				if (mv_check_state.matches("g_hundred")) {
					exec_dg_kf_dosr.shutdown();
				}
				if (mv_check_state.matches("timer")) {
					exec_dg_kf_timer.shutdown();
					delay_timer.setText("Delay Timer : " + 0 + " sec");
					prev_process = current_process;
					mv_check_state = "";
					send_dose_timer();
				}
				if (mv_check_state.matches("g_hundred") || mv_check_state.matches("")) {
					mv_check_state = "g_one";
					exec_dg_kf_one = Executors.newSingleThreadScheduledExecutor();
					exec_dg_kf_one.scheduleAtFixedRate(new Runnable() {
						@Override
						public void run() {
							dose = dose + ((1 / 60.0) / 10.0);
							vol_dosed.setText("Volume dosed : " + String.format("%.3f", dose) + "mL");
						}
					}, 0, 100, TimeUnit.MILLISECONDS);
				}
			} else if (mv > 100) {
				if (mv_check_state.matches("timer")) {
					exec_dg_kf_timer.shutdown();
					delay_timer.setText("Delay Timer : " + 0 + " sec");
					prev_process = current_process;
					mv_check_state = "";
					send_dose_timer();
				}
				if (mv_check_state.matches("g_one")) {
					System.out.println("From One to hundred");
					exec_dg_kf_one.shutdown();
				}
				if (mv_check_state.matches("g_one") || mv_check_state.matches("")) {
					mv_check_state = "g_hundred";
					exec_dg_kf_dosr = Executors.newSingleThreadScheduledExecutor();
					exec_dg_kf_dosr.scheduleAtFixedRate(new Runnable() {
						@Override
						public void run() {
							Double dos_rate = Double.parseDouble(dosage_rate);
							dose = dose + ((dos_rate / 60.0) / 10.0);
							vol_dosed.setText("Volume dosed : " + String.format("%.3f", dose) + "mL");
						}
					}, 0, 100, TimeUnit.MILLISECONDS);
				}
			} else {
				if (mv_check_state.matches("g_hundred")) {
					exec_dg_kf_dosr.shutdown();
					delay_timer.setText("Delay Timer : " + 0 + " sec");
				}
				if (mv_check_state.matches("g_one")) {
					System.out.println("From One to hundred");
					delay_timer.setText("Delay Timer : " + 0 + " sec");
					exec_dg_kf_one.shutdown();
				}
				if (mv_check_state.matches("g_one") || mv_check_state.matches("g_hundred")
						|| mv_check_state.matches("")) {
					mv_check_state = "timer";
					try {
						//Thread.sleep(100);
						output_dg.print("<8888>STPM*");
						output_dg.flush();
						ReformatBuffer.current_state = "dg_kf_stmp";
					} //catch (InterruptedException ex) {
					//}
					catch (NullPointerException ee) {
						JOptionPane.showMessageDialog(null, "Please select the ComPort!");
					}
				}
			}
		}
	}

	public static void stmp_ok_received() {
		System.out.println("STPM OK RECEIVED KF");
		start_delay_timer();
	}

	public static void start_delay_timer() {
		time = Integer.parseInt(delay_val);
		exec_dg_kf_timer = Executors.newSingleThreadScheduledExecutor();
		exec_dg_kf_timer.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				delay_timer.setText("Delay Timer : " + time + "sec");
				if (time == 0) {
					exec_dg_kf_timer.shutdown();
					delay_timer.setText("Delay Timer : " + 0 + "sec");

					if (current_process.matches("pre_run_started")) {
						dose = 0;
						fill = 0;
						current_process = "";
						mv_check_state = "";
						vol_dosed.setText("Volume dosed : 0.00 mL");
						vol_filled.setText("Volume filled : 0.00 mL");
						delay_timer.setText("Delay Timer : " + 0 + " sec");
						update_result_scroll("\nPre-Run Completed!");
						db_details = db_details + "[ " + get_time() + " ]  Pre-Run Completed,";
						update_result_text("Pre-Run Completed!");
						JOptionPane.showMessageDialog(null, "Pre-Run Completed!");
						try {
							audit_log_push.push_to_audit(get_date(), get_time() ,user_name,"Pre-Run completed");
						} catch (ParseException e1) {e1.printStackTrace();}
						button_prerun.setEnabled(true);
						pre_run_completed = true;
						if (temp_status.matches("bm")) {
							temp_status = "b";
							dose = 0;
							fill = 0;
							if (sent_cvok == false) {
								send_cvok();
							} else {
								send_afil();
							}
							current_process = "blank_run";
							experiment_performing.setText("Experiment Performing : Blank Run");
							start_checking = true;
						}
						if (temp_status.matches("am")) {
							temp_status = "a";
							JOptionPane.showMessageDialog(null, "Continue to Trial " + (cur_trial));
							dose = 0;
							fill = 0;
							if (sent_cvok == false) {
								send_cvok();
							} else {
								send_afil();
							}
							current_process = "blank_run";
							experiment_performing.setText("Experiment Performing : Analysis");
							start_checking = true;
						}
						if (temp_status.matches("hm")) {
							temp_status = "h";
							JOptionPane.showMessageDialog(null, "Continue to Trial " + (cur_trial));
							dose = 0;
							fill = 0;
							if (sent_cvok == false) {
								send_cvok();
							} else {
								send_afil();
							}
							current_process = "blank_run";
							experiment_performing.setText("Experiment Performing : Std. by H2O ");
							start_checking = true;
						}
						if (temp_status.matches("tm")) {
							temp_status = "t";
							JOptionPane.showMessageDialog(null, "Continue to Trial " + (cur_trial));
							dose = 0;
							fill = 0;
							if (sent_cvok == false) {
								send_cvok();
							} else {
								send_afil();
							}
							current_process = "blank_run";
							experiment_performing.setText("Experiment Performing : Std. by DiSodium Tartarate ");
							start_checking = true;
						}
					} else if (current_process.matches("blank_run_started")) {
						Double temp_dose = dose;
						mv_check_state = "";
						vol_dosed.setText("Volume dosed : 0.00 mL");
						vol_filled.setText("Volume filled : 0.00 mL");
						delay_timer.setText("Delay Timer : " + 0 + " sec");
						if (temp_status.matches("b")) {
							blank_vol = String.valueOf(dose);
							current_process = "";
							update_result_text("Blank Vol: " + String.format("%.4f", temp_dose) + " mL");
							update_result_scroll("\nBlank run Completed!\n");
							db_details = db_details + "[ " + get_time() + " ]  Blank run Completed,";
							update_result_scroll("Blank Volume = " + String.format("%.4f", temp_dose) + " mL \n");
							db_details = db_details + "[ " + get_time() + " ]  Blank Volume = "
									+ String.format("%.4f", temp_dose) + " mL,";
							JOptionPane.showMessageDialog(null, "Blank-Run Completed!");
							// button_blankrun.setEnabled(false);
							update_blankvol.setEnabled(true);
							blank_run_conducted = true;

						} else if (temp_status.matches("h")) {
							start_checking = false;
							// JOptionPane.showMessageDialog(null, "Std. by H2O Completed!");
							std_h2o_completed(temp_dose);
						} else if (temp_status.matches("t")) {
							start_checking = false;
							std_disodium_completed(temp_dose);
							// JOptionPane.showMessageDialog(null, "Std. by Disodium Tartarate Completed!");
						} else if (temp_status.matches("a")) {
							start_checking = false;
							analysis_completed(temp_dose);
							// JOptionPane.showMessageDialog(null, "Analysis Completed!");
						}
					}
				}
				time--;
			}
		}, 0, 1000, TimeUnit.MILLISECONDS);
	}

	public static void open_timer() {
		if (Integer.parseInt(stir_time) > 0) {
			System.out.println("afill_end received if 2 -- If method");
			String aa[] = { String.valueOf(Integer.parseInt(stir_time)), "kf" };
			popup_stirtime.main(aa);
		} else {
			send_dose();
		}
	}

	public static void timer_completed() {
		send_dose();
	}

	public static void get_mg() {
		try {
			String result = (String) JOptionPane.showInputDialog(frame1,
					"Enter the weight of the sample in mg or microlitre. Add sample and then PRESS OK!", "User Input",
					JOptionPane.PLAIN_MESSAGE, null, null, "");
			sample_weight = Double.parseDouble(result);

			try {
				v = 0;
				ScheduledExecutorService exec_tempp = Executors.newSingleThreadScheduledExecutor();
				exec_tempp.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						v++;
						if (v == 10) {
							v = 0;
							if ((int) double_mv_val < Integer.parseInt(end_point))// && pre_run_completed == true
							{
								exec_tempp.shutdown();
								JOptionPane.showMessageDialog(null,
										"mV less than end-point Experiment cannot continue. Please Check!");
								get_mg();
							} else {
								exec_tempp.shutdown();
								open_timer();
								// send_dose();
							}
						}

					}
				}, 0, 100, TimeUnit.MILLISECONDS);

			} catch (Exception mnmnm) {

			}
		} catch (NullPointerException ne) {
			JOptionPane.showMessageDialog(null, "Please enter a value!");
			get_mg();

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Please enter a valid value!");
			get_mg();
		}
	}

	public static void update_blank_vol_metd(Double dose) {
		variables[3] = String.format("%.2f", dose);
		System.out.println("Inside Update Blank Volume Metd");
		if (model3.getRowCount() > 0) {
			for (int i = model3.getRowCount() - 1; i > -1; i--) {
				model3.removeRow(i);
			}
		}
		model3 = new DefaultTableModel(
				new Object[][] { { "Metd Name", ":", metd_name, "" }, { "Delay", ":", variables[0], "sec" },
						{ "Stir time", ":", variables[1], "sec" }, { "Max vol", ":", variables[2], "mL" },
						{ "Blank vol", ":", String.format("%.2f", dose), "mL" },
						{ "Buretter Factor", ":", variables[4], "" }, { "Density", ":", variables[5], "" },
						{ "KF Factor	", ":", variables[6], "" }, },
				new String[] { "Methods", "Parameters", "", "" });
		table11.setModel(model3);
		model3.fireTableRowsUpdated(0, 8);
		// metd_data
		blank_vol = String.format("%.5f", dose);
		String[] temp_ar = metd_data.split(",");
		String temp_update = "";

		for (int i = 0; i < temp_ar.length; i++) {
			if (i == 3) {
				temp_update = temp_update + "," + String.format("%.5f", dose);
			} else {
				if (i == 0) {
					temp_update = temp_update + temp_ar[i];
				} else {
					temp_update = temp_update + "," + temp_ar[i];
				}
			}
		}

		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		try {
			String sql = null;
			sql = "UPDATE kf_method SET Value = ? WHERE Trial_name = ?";
			System.out.println("Checking");
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
		try {
			audit_log_push.push_to_audit(get_date(), get_time() ,user_name,"Blank Volume: "+String.format("%.3f", dose)+" - Updated to Method File");
		} catch (ParseException e1) {e1.printStackTrace();}
	}

	public static void update_kff_metd(Double kff) {

		System.out.println("Inside Update Blank Volume Metd");
		if (model3.getRowCount() > 0) {
			for (int i = model3.getRowCount() - 1; i > -1; i--) {
				model3.removeRow(i);
			}
		}
		model3 = new DefaultTableModel(
				new Object[][] { { "Metd Name", ":", metd_name, "" }, { "Delay", ":", variables[0], "sec" },
						{ "Stir time", ":", variables[1], "sec" }, { "Max vol", ":", variables[2], "mL" },
						{ "Blank vol", ":", variables[3], "mL" }, { "Buretter Factor", ":", variables[4], "" },
						{ "Density", ":", variables[5], "" }, { "KF Factor	", ":", String.format("%.2f", kff), "" },

				}, new String[] { "Methods", "Parameters", "", "" });
		table11.setModel(model3);
		model3.fireTableRowsUpdated(0, 8);

		String[] temp_ar = metd_data.split(",");
		String temp_update = "";

		for (int i = 0; i < temp_ar.length; i++) {
			if (i == 6) {
				temp_update = temp_update + "," + String.format("%.2f", kff);
			} else {
				if (i == 0) {
					temp_update = temp_update + temp_ar[i];
				} else {
					temp_update = temp_update + "," + temp_ar[i];
				}
			}
		}

		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		try {
			String sql = "UPDATE kf_method SET Value = ? WHERE Trial_name = ?";

			ps = con.prepareStatement(sql);
			ps.setString(1, temp_update);
			ps.setString(2, metd_name);
			ps.executeUpdate();
			JOptionPane.showMessageDialog(null, "KF Factor Updated Successfully to method file!");
			update_result_scroll("\nKFF Updated to Method File\n ");
			db_details = db_details + "[ " + get_time() + " ]  KFF Updated to Method File ,";
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
			audit_log_push.push_to_audit(get_date(), get_time() ,user_name,"KF Factor: "+String.format("%.2f", kff)+" Updated Successfully to method file");
		} catch (ParseException e1) {e1.printStackTrace();}
	}

	public static void update_result_scroll(String msg) {
		display.setText(display.getText().toString() + msg);
	}

	public static void update_result_text(String msg) {
		result.setText(msg);
	}

	public static void std_h2o_completed(double dosage) {
		JOptionPane.showMessageDialog(null, "Std. by H2O Trial " + cur_trial + " completed");

		if (trial_cnt > 0) {
			double result_kff = 0;
			result_kff = (sample_weight / dosage);
			System.out.println("Inside trial_cnt>0 Sample weight = " + sample_weight + " --dosage = " + dosage
					+ "  --result = " + result_kff);

			add_row_to_five_column(cur_trial - 1, String.format("%.4f", dosage), String.valueOf(sample_weight),
					String.format("%.4f", result_kff));
			update_result_scroll(
					"\nStd. by H2O - Trial " + cur_trial + " : KFF = " + String.format("%.4f", result_kff));

			update_result_text("KFF = " + String.format("%.4f", result_kff));
			kff_arr[cur_trial - 1][0] = dosage;
			kff_arr[cur_trial - 1][1] = sample_weight;
			kff_arr[cur_trial - 1][2] = result_kff;

			if (trial_cnt == 1) {
				std_h20_conducted = true;
				current_process = "";
				update_kf_factor.setEnabled(true);
				double[] tempp_arr = new double[5];

				double avg_kff = 0;
				for (int i = 0; i < table1.getRowCount(); i++) {
					avg_kff = avg_kff + kff_arr[i][2];
					tempp_arr[i] = kff_arr[i][2];
					System.out.println("Tempp_arr [" + i + "] = " + tempp_arr[i]);
				}
				double rsd = SD(tempp_arr, table1.getRowCount()) * 100;
				avg_kff = avg_kff / cur_trial;
				update_result_text("<html>Avg. KFF = " + String.format("%.4f", avg_kff)+"<br/>RSD = "+String.format("%.2f",rsd)+"</html>");
				JOptionPane.showMessageDialog(null, "STD. BY H20 COMPLETED");
				button_std_by_h2o.setEnabled(false);
				
			} else {

				JOptionPane.showMessageDialog(null, "Continue to Trial " + (cur_trial + 1));
				System.out.println("KF_Timing_Trial [" + (cur_trial)+" ] = "+get_time());
				kf_trial_timings[cur_trial] = get_time();

				dose = 0;
				fill = 0;
				if (sent_cvok == false) {
					send_cvok();
				} else {
					send_afil();
				}
				current_process = "blank_run";
				start_checking = true;
			}
			cur_trial++;
			trial_cnt--;
		}
	}

	public static void std_h20_result() {

		double[] tempp_arr = new double[5];
		cur_trial--;
		double avg_kff = 0;
		double rsd = 0;
		if (select_column == true) {
			int g = 0;
			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked) {
					String aa = table1.getValueAt(i, 1).toString();
					avg_kff = avg_kff + kff_arr[i][2];
					tempp_arr[g] = kff_arr[i][2];
					System.out.println("checked = " + checked + " value = " + aa);
					System.out.println("Tempp_arr [" + i + "] = " + tempp_arr[i]+" timings = "+kf_trial_timings[i]);
					g++;
					db_details = db_details + "[ " + kf_trial_timings[i] + " ]  Std. by H2O - Trial " + g + " : KFF = "
							+ String.format("%.4f", kff_arr[i][2]) + ",";
					try {
						audit_log_push.push_to_audit(get_date(), kf_trial_timings[i] ,user_name,"Std. by H2O - Trial " + g + ": KFF =  "+ String.format("%.4f", kff_arr[i][2]));
					} catch (ParseException e1) {e1.printStackTrace();}
					
					if (g == 1)
						db_kff_trials = db_kff_trials + table1.getValueAt(i, 2).toString() + ","
								+ table1.getValueAt(i, 3).toString() + "," + table1.getValueAt(i, 4).toString();
					else
						db_kff_trials = db_kff_trials + ":" + table1.getValueAt(i, 2).toString() + ","
								+ table1.getValueAt(i, 3).toString() + "," + table1.getValueAt(i, 4).toString();
				}
			}
			rsd = SD(tempp_arr, g) * 100;
			avg_kff = avg_kff / g;
			db_kff_results = "";
			double rsd1 = (rsd / avg_kff) * 100;
			db_kff_results = db_kff_results + String.format("%.4f", avg_kff) + "," + String.format("%.2f", rsd);
			kf_factor = String.format("%.3f", avg_kff);
			
		} 
		else {
			int g = 0;
			for (int i = 0; i < table1.getRowCount(); i++) {
				avg_kff = avg_kff + kff_arr[i][2];
				tempp_arr[i] = kff_arr[i][2];
				System.out.println("Tempp_arr [" + i + "] = " + tempp_arr[i]+" timings = "+kf_trial_timings[i]);
				g++;
				db_details = db_details + "[ " + kf_trial_timings[i] + " ]  Std. by H2O - Trial " + g + " : KFF = "
						+ String.format("%.4f", kff_arr[i][2]) + ",";
				try {
					audit_log_push.push_to_audit(get_date(), kf_trial_timings[i] ,user_name,"Std. by H2O - Trial " + g + ": KFF =  "+ String.format("%.4f", kff_arr[i][2]));
				} catch (ParseException e1) {e1.printStackTrace();}
				if (g == 1)
					db_kff_trials = db_kff_trials + table1.getValueAt(i, 1).toString() + ","
							+ table1.getValueAt(i, 2).toString() + "," + table1.getValueAt(i, 3).toString();
				else
					db_kff_trials = db_kff_trials + ":" + table1.getValueAt(i, 1).toString() + ","
							+ table1.getValueAt(i, 2).toString() + "," + table1.getValueAt(i, 3).toString();
			}
			rsd = SD(tempp_arr, g) * 100;
			avg_kff = avg_kff / g;
			double rsd1 = (rsd / avg_kff) * 100;
			db_kff_results = db_kff_results + String.format("%.4f", avg_kff) + "," + String.format("%.2f", rsd);
			kf_factor = String.format("%.3f", avg_kff);

		}

		update_result_scroll("\n\nStd. by H2O - Result KFF = " + String.format("%.4f", avg_kff));
		db_details = db_details + "[ " + get_time() + " ]  Std. by H2O - Result KFF = " + String.format("%.4f", avg_kff)
				+ " mL,";
		update_result_scroll("\nStd. by H2O - RSD = " + String.format("%.4f", rsd) + " % \n");
		db_details = db_details + "[ " + get_time() + " ]  Std. by H2O - RSD = " + String.format("%.4f", rsd) + " %,";
		
		try {
			audit_log_push.push_to_audit(get_date(), get_time() ,user_name,"Std. by H2O - Result KFF =  "+ String.format("%.4f", avg_kff)
			+ " mL   RSD = "+String.format("%.4f", rsd) + " %");
		} catch (ParseException e1) {e1.printStackTrace();}
		try {
			audit_log_push.push_to_audit(get_date(), get_time() ,user_name,"Std. Report: "+ db_report_name+" - Updated to DB");
		} catch (ParseException e1) {e1.printStackTrace();}
		try {
			audit_log_push.push_to_audit(get_date(), get_time() ,user_name,"Std. By H2O Completed");
		} catch (ParseException e1) {e1.printStackTrace();}
		button_std_by_sodium.setEnabled(false);
		button_std_by_h2o.setEnabled(false);
		update_result_text("<html>Avg. KFF = " + String.format("%.4f", avg_kff)+"<br/>RSD = "+String.format("%.2f",rsd)+"</html>");
		kf_factor = String.format("%.5f", avg_kff);
		update_kff_metd(avg_kff);
		add_kff_to_db("Std. by H2O");

	}

	public static void std_disodium_completed(double dosage) {
		JOptionPane.showMessageDialog(null, "Std. by DST Trial " + cur_trial + " completed");

		if (trial_cnt > 0) {
			double result_kff = 0;
			result_kff = ((sample_weight * 0.1566) / dosage);
			add_row_to_five_column(cur_trial - 1, String.format("%.4f", dosage), String.valueOf(sample_weight),
					String.format("%.4f", result_kff));
			update_result_scroll("\nStd. by DST - Trial " + cur_trial + " : KFF = "
					+ String.format("%.4f", result_kff));

			update_result_text("KFF = " + String.format("%.4f", result_kff));
			kff_arr[cur_trial - 1][0] = dosage;
			kff_arr[cur_trial - 1][1] = sample_weight;
			kff_arr[cur_trial - 1][2] = result_kff;
			if (trial_cnt == 1) {
				current_process = "";
				std_disodium_conducted = true;

				update_kf_factor.setEnabled(true);

				double[] tempp_arr = new double[5];

				double avg_kff = 0;
				for (int i = 0; i < table1.getRowCount(); i++) {
					avg_kff = avg_kff + kff_arr[i][2];
					tempp_arr[i] = kff_arr[i][2];
				}
				double rsd = SD(tempp_arr, table1.getRowCount()) * 100;
				avg_kff = avg_kff / cur_trial;
				update_result_text("<html>Avg. KFF = " + String.format("%.4f", avg_kff)+"<br/>RSD = "+String.format("%.2f",rsd)+"</html>");

				JOptionPane.showMessageDialog(null, "STD. BY DST COMPLETED");
				button_std_by_sodium.setEnabled(false);

			} else {
				JOptionPane.showMessageDialog(null, "Continue to Trial " + (cur_trial + 1));
				kf_trial_timings[cur_trial] = get_time();

				dose = 0;
				fill = 0;
				if (sent_cvok == false) {
					send_cvok();
				} else {
					send_afil();
				}
				current_process = "blank_run";
				start_checking = true;

			}
			cur_trial++;
			trial_cnt--;
		}
	}

	public static void std_disodium_result() {
		double[] tempp_arr = new double[5];
		cur_trial--;
		double rsd = 0;
		double avg_kff = 0;
		if (select_column == true) {
			int g = 0;
			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked) {
					String aa = table1.getValueAt(i, 1).toString();
					avg_kff = avg_kff + kff_arr[i][2];
					tempp_arr[g] = kff_arr[i][2];
					System.out.println("checked = " + checked + " value = " + aa);
					System.out.println("Tempp_arr [" + i + "] = " + tempp_arr[i]);
					g++;
					db_details = db_details + "[ " + kf_trial_timings[i] + " ]  Std. by DST - Trial " + g
							+ " : KFF = " + String.format("%.4f", kff_arr[i][2]) + ",";
					try {
						audit_log_push.push_to_audit(get_date(), kf_trial_timings[i] ,user_name,"Std. by DST - Trial " + g + ": KFF =  "+ String.format("%.4f", kff_arr[i][2]));
					} catch (ParseException e1) {e1.printStackTrace();}
					if (g == 1)
						db_kff_trials = db_kff_trials + table1.getValueAt(i, 2).toString() + ","
								+ table1.getValueAt(i, 3).toString() + "," + table1.getValueAt(i, 4).toString();
					else
						db_kff_trials = db_kff_trials + ":" + table1.getValueAt(i, 2).toString() + ","
								+ table1.getValueAt(i, 3).toString() + "," + table1.getValueAt(i, 4).toString();
				}
			}
			rsd = SD(tempp_arr, g) * 100;
			avg_kff = avg_kff / g;
			double rsd1 = (rsd / avg_kff) * 100;
			db_kff_results = db_kff_results + String.format("%.4f", avg_kff) + "," + String.format("%.2f", rsd);
			kf_factor = String.format("%.3f", avg_kff);


		} else {
			int g = 0;
			for (int i = 0; i < table1.getRowCount(); i++) {
				avg_kff = avg_kff + kff_arr[i][2];
				tempp_arr[i] = kff_arr[i][2];
				g++;
				db_details = db_details + "[ " + kf_trial_timings[i] + " ]  Std. by DST - Trial " + g + " : KFF = "
						+ String.format("%.4f", kff_arr[i][2]) + ",";
				try {
					audit_log_push.push_to_audit(get_date(), kf_trial_timings[i] ,user_name,"Std. by DST - Trial " + g + ": KFF =  "+ String.format("%.4f", kff_arr[i][2]));
				} catch (ParseException e1) {e1.printStackTrace();}
				if (g == 1)
					db_kff_trials = db_kff_trials + table1.getValueAt(i, 1).toString() + ","
							+ table1.getValueAt(i, 2).toString() + "," + table1.getValueAt(i, 3).toString();
				else
					db_kff_trials = db_kff_trials + ":" + table1.getValueAt(i, 1).toString() + ","
							+ table1.getValueAt(i, 2).toString() + "," + table1.getValueAt(i, 3).toString();
			}
			rsd = SD(tempp_arr, g) * 100;
			avg_kff = avg_kff / g;
			double rsd1 = (rsd / avg_kff) * 100;
			db_kff_results = db_kff_results + String.format("%.4f", avg_kff) + "," + String.format("%.2f", rsd);
			kf_factor = String.format("%.3f", avg_kff);


		}

		update_result_scroll("\n\nStd. by DST - Result KFF = " + String.format("%.4f", avg_kff));
		db_details = db_details + "[ " + get_time() + " ]  Std. by DST - Result KFF = "
				+ String.format("%.4f", avg_kff) + ",";
		update_result_scroll("\nStd. by DST - RSD = " + String.format("%.4f", rsd) + " % \n");
		db_details = db_details + "[ " + get_time() + " ]  Std. by DST - RSD = "
				+ String.format("%.4f", rsd) + " %,";
		try {
			audit_log_push.push_to_audit(get_date(), get_time() ,user_name,"Std. by DST - Result KFF =  "+ String.format("%.4f", avg_kff)
			+ " mL   RSD = "+String.format("%.4f", rsd) + " %");
		} catch (ParseException e1) {e1.printStackTrace();}
		try {
			audit_log_push.push_to_audit(get_date(), get_time() ,user_name,"Std. Report: "+ db_report_name+" - Updated to DB");
		} catch (ParseException e1) {e1.printStackTrace();}
		try {
			audit_log_push.push_to_audit(get_date(), get_time() ,user_name,"Std. By DST Completed");
		} catch (ParseException e1) {e1.printStackTrace();}
		button_std_by_sodium.setEnabled(false);
		button_std_by_h2o.setEnabled(false);
		update_result_text("<html>Avg. KFF = " + String.format("%.4f", avg_kff)+"<br/>RSD = "+String.format("%.2f",rsd)+"</html>");
		kf_factor = String.format("%.5f", avg_kff);
		update_kff_metd(avg_kff);
		add_kff_to_db("Std. by DST");
	}

	public static void analysis_completed(double dosage) {
		JOptionPane.showMessageDialog(null, "Analysis : Trial =  " + cur_trial + " Completed");

		if (trial_cnt > 0) {
			double result_moisture = 0;
			int temp_factor = 0;
			if (result_unit.matches("%")) {
				temp_factor = 100;
			} else if (result_unit.toLowerCase().matches("ppm")) {
				temp_factor = 1000000;
			}

			result_moisture = (((dosage - Double.parseDouble(blank_vol)) / sample_weight))
					* (Double.parseDouble(kf_factor)) * (temp_factor);
			add_row_to_five_column(cur_trial - 1, String.format("%.4f", dosage), String.valueOf(sample_weight),
					String.format("%.4f", result_moisture));
			update_result_scroll(
					"\nAnalysis - Trial " + cur_trial + " : Moisture = " + String.format("%.4f", result_moisture));

			update_result_text("Moisture = " + String.format("%.4f", result_moisture));
			moisture_arr[cur_trial - 1][0] = dosage;
			moisture_arr[cur_trial - 1][1] = sample_weight;
			moisture_arr[cur_trial - 1][2] = result_moisture;
			if (trial_cnt == 1) {
				current_process = "";
				update_analyze.setEnabled(true);
				analysis_conducted = true;

				double[] tempp_arr = new double[5];
				double avg_moisture = 0;
				for (int i = 0; i < table1.getRowCount(); i++) {
					avg_moisture = avg_moisture + moisture_arr[i][2];
					tempp_arr[i] = moisture_arr[i][2];
				}
				double rsd = SD(tempp_arr, table1.getRowCount()) * 100;
				avg_moisture = avg_moisture / cur_trial;

				update_result_text("<html>Avg. Moisture = " + String.format("%.4f", avg_moisture)+"<br/>RSD = "+String.format("%.2f",rsd)+"</html>");
				JOptionPane.showMessageDialog(null, "ANALYSIS COMPLETED");
				button_analysis.setEnabled(false);
			} else {
				JOptionPane.showMessageDialog(null, "Continue to Trial " + (cur_trial + 1));
				System.out.println("Analysis_Timing_Trial [" + (cur_trial)+" ] = "+get_time());

				moisture_trial_timings[cur_trial] = get_time();

				dose = 0;
				fill = 0;
				if (sent_cvok == false) {
					send_cvok();
				} else {
					send_afil();
				}
				current_process = "blank_run";
				start_checking = true;
			}
			cur_trial++;
			trial_cnt--;
		}
	}

	public static void analysis_result_update() {
		cur_trial--;
		double[] tempp_arr = new double[5];
		double avg_moisture = 0;
		double rsd = 0;
		if (select_column == true) {
			int g = 0;
			for (int i = 0; i < table1.getRowCount(); i++) {
				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
				if (checked) {
					String aa = table1.getValueAt(i, 1).toString();
					avg_moisture = avg_moisture + moisture_arr[i][2];
					tempp_arr[g] = moisture_arr[i][2];
					
					db_details = db_details + "[ " + moisture_trial_timings[i] + " ]  Analysis - Trial " + (g+1) + " : Moisture = "
							+ String.format("%.4f", moisture_arr[i][2]) + ",";
					try {
						audit_log_push.push_to_audit(get_date(), moisture_trial_timings[i] ,user_name,"Analysis - Trial " + (g+1) + ": Moisture = "+ String.format("%.4f", moisture_arr[i][2]));
					} catch (ParseException e1) {e1.printStackTrace();}
					if (g == 0)
						db_moisture_trials = db_moisture_trials + table1.getValueAt(i , 2).toString() + ","
								+ table1.getValueAt(i , 3).toString() + "," + table1.getValueAt(i , 4).toString();
					else
						db_moisture_trials = db_moisture_trials + ":" + table1.getValueAt(i , 2).toString() + ","
								+ table1.getValueAt(i , 3).toString() + "," + table1.getValueAt(i , 4).toString();
					g++;
				}
			}
			rsd = SD(tempp_arr, g) * 100;
			avg_moisture = avg_moisture / g;
			double rsd1 = (rsd / avg_moisture) * 100;
			db_moisture_results = db_moisture_results + String.format("%.4f", avg_moisture) + ","+ String.format("%.2f", rsd);
		} else {
			int g = 0;
			for (int i = 0; i < table1.getRowCount(); i++) {
				avg_moisture = avg_moisture + moisture_arr[i][2];
				tempp_arr[i] = moisture_arr[i][2];
				g++;
				db_details = db_details + "[ " + moisture_trial_timings[i] + " ]  Analysis - Trial " + g + " : Moisture = "
						+ String.format("%.4f", moisture_arr[i][2]) + ",";
				try {
					audit_log_push.push_to_audit(get_date(), moisture_trial_timings[i] ,user_name,"Analysis - Trial " + (g) + ": Moisture = "+ String.format("%.4f", moisture_arr[i][2]));
				} catch (ParseException e1) {e1.printStackTrace();}
				if (g == 1)
					db_moisture_trials = db_moisture_trials + table1.getValueAt(g - 1, 1).toString() + ","
							+ table1.getValueAt(g - 1, 2).toString() + "," + table1.getValueAt(g - 1, 3).toString();
				else
					db_moisture_trials = db_moisture_trials + ":" + table1.getValueAt(g - 1, 1).toString() + ","
							+ table1.getValueAt(g - 1, 2).toString() + "," + table1.getValueAt(g - 1, 3).toString();
			}
			rsd = SD(tempp_arr, g) * 100;
			avg_moisture = avg_moisture / g;
			double rsd1 = (rsd / avg_moisture) * 100;
			db_moisture_results = db_moisture_results + String.format("%.4f", avg_moisture) + ","
					+ String.format("%.2f", rsd);
		}

		update_result_scroll("\n\nAnalysis - Result Moisture = " + String.format("%.4f", avg_moisture));
		db_details = db_details + "[ " + get_time() + " ]  Analysis - Result Moisture = "
				+ String.format("%.4f", avg_moisture) + ",";
		update_result_scroll("\nAnalysis - RSD = " + String.format("%.4f", rsd) + " %");
		db_details = db_details + "[ " + get_time() + " ]  Analysis - RSD = " + String.format("%.4f", rsd) + " %,";

		update_result_text("<html>Avg. Moisture = " + String.format("%.4f", avg_moisture)+"<br/>RSD = "+String.format("%.2f",rsd)+"</html>");

		try {
			audit_log_push.push_to_audit(get_date(), get_time() ,user_name,"Analysis - Result Moisture = "+ String.format("%.4f", avg_moisture)+"    RSD = "+String.format("%.4f", rsd) + " %");
		} catch (ParseException e1) {e1.printStackTrace();}
		try {
			audit_log_push.push_to_audit(get_date(), get_time() ,user_name,"Analysis Report: "+ db_report_name+" - Updated to DB");
		} catch (ParseException e1) {e1.printStackTrace();}
		try {
			audit_log_push.push_to_audit(get_date(), get_time() ,user_name,"Analyis Completed");
		} catch (ParseException e1) {e1.printStackTrace();}
		String[] temp_print = db_details.split(",");

		for (String ddd : temp_print) {
			System.out.println("D =   " + ddd);
		}
		System.out.println("UUUUU KFF Trial data = " + db_kff_trials);
		System.out.println("UUUUU KFF Result data = " + db_kff_results);
		System.out.println("UUUUU Moisture Trial data = " + db_moisture_trials);
		System.out.println("UUUUU Moisture Result data = " + db_moisture_results);
		
		String[] temp_trial_cnt = db_moisture_trials.split(":");
		
		String[] temp_params = db_parameters.split(",");
		String temp_db_params = "";
		
		for(int i=0;i<temp_params.length;i++) {
			if(i == 0) {
				temp_db_params = temp_db_params + temp_params[0];
			}else if(i == 14) {
				temp_db_params = temp_db_params +","+temp_trial_cnt.length;
			}else {
				temp_db_params = temp_db_params +","+temp_params[i];
			}
		}
		db_parameters = temp_db_params;
		
		add_moisture_to_db();
	}

	public static double SD(double arr[], int size) {
		double sum = 0.0;
		double standardDeviation = 0.0;
		double mean = 0.0;
		double res = 0.0;
		double sq = 0.0;

		int n = cur_trial;

		for (int i = 0; i < size; i++) {
			System.out.println("Cur_Trial = " + cur_trial + "  -   " + arr[i]);
			sum = sum + arr[i];
		}

		mean = sum / (size);

		System.out.println("MMMEEEAAANNNN : " + mean);

		for (int i = 0; i < size; i++) {
			standardDeviation = standardDeviation + Math.abs(Math.pow((arr[i] - mean), 2));
		}

		sq = standardDeviation / (size-1);
		res = Math.sqrt(sq);
		res = res / mean;
		System.out.println("MMMEEEAAANNNN RESSS : " + res);

		return res;
	}

	public static void update_data_to_result_file() {

	}

	public DrawGraph_kf() {
		setLayout(null);
		//System.out.println("Contructor draw graph");

		display = new JTextArea();
		display.setEditable(false);
		display.setFont(display.getFont().deriveFont(15f));
		JScrollPane scroll = new JScrollPane(display);
		scroll.setBounds((int) Math.round(0.013 * wid), (int) Math.round(0.098 * hei), (int) Math.round(0.455 * wid),
				(int) Math.round(0.4044 * hei));
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(scroll);

		// result text view
		Border blackline = BorderFactory.createLineBorder(Color.DARK_GRAY);
		panel_result = new JPanel();
		panel_result.setLayout(new GridBagLayout());
		add(panel_result);
		panel_result.setBorder(blackline);
		result = new JLabel("");
		result.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.014 * wid)));
		panel_result.setBounds((int) Math.round(0.585 * wid), (int) Math.round(0.355 * hei),
				(int) Math.round(0.221 * wid), (int) Math.round(0.245 * hei));

		panel_result.add(result);

		JLabel experiment = new JLabel("KF");
		experiment.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.016 * wid)));
		experiment.setBounds((int) Math.round(0.013 * wid), (int) Math.round(0.03 * hei), (int) Math.round(0.225 * wid),
				(int) Math.round(0.03 * hei));
		add(experiment);

		experiment_performing = new JLabel("Experiment Performing : ");
		experiment_performing.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.012 * wid)));
		experiment_performing.setBounds((int) Math.round(0.13 * wid), (int) Math.round(0.03 * hei),
				(int) Math.round(0.4 * wid), (int) Math.round(0.036 * hei));

		add(experiment_performing);

		button_home = new JButton("Home");
		button_home.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		button_home.setBounds((int) Math.round(0.013 * wid), (int) Math.round(0.5392 * hei),
				(int) Math.round(0.052 * wid), (int) Math.round(0.0392 * hei));

		add(button_home);
		button_home.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (mv_check_state.matches("g_hundred")) {
					exec_dg_kf_dosr.shutdown();
					//exec_dg_kf_dosr.shutdownNow();
				}
				if (mv_check_state.matches("g_one")) {
					exec_dg_kf_one.shutdown();
				}
				if (mv_check_state.matches("timer")) {
					exec_dg_kf_timer.shutdown();
				} 
				if (mv_check_state.matches("")) {
					exec_dg_kf_fill.shutdown();
				}
				current_process = "";
				
				 
				try {
					Thread.sleep(100);
					output_dg.print("<8888>DOSR,020*");
					output_dg.flush();
				} catch (InterruptedException ex) {
				} catch (NullPointerException ee) {
					JOptionPane.showMessageDialog(null, "Please select the ComPort!");
				}
				ReformatBuffer.current_state = "dg_kf_home_dosr";

				
			}
		});

		button_sop = new JButton("SOP");
		button_sop.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		button_sop.setBounds((int) Math.round(0.068 * wid), (int) Math.round(0.5392 * hei),
				(int) Math.round(0.052 * wid), (int) Math.round(0.0392 * hei));

		add(button_sop);
		button_sop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("SOP = " + variables[17]);
				if (!sop.matches("Not Selected")) {
					try {
						File file = new File("C:\\SQLite\\SOP\\" + sop);
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

		button_prerun = new JButton("Pre Run");
		button_prerun.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		button_prerun.setBounds((int) Math.round(0.123 * wid), (int) Math.round(0.5392 * hei),
				(int) Math.round(0.052 * wid), (int) Math.round(0.0392 * hei));
		add(button_prerun);
		button_prerun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				start_checking = true;

				temp_status = "p";
				dose = 0;
				fill = 0;
				send_cvok();
				current_process = "pre_run";

				experiment_performing.setText("Experiment Performing : PreRun");
				db_details = db_details + "[ " + get_time() + " ]  Pre-Run Started,";
				button_prerun.setEnabled(false);
				try {
					audit_log_push.push_to_audit(get_date(), get_time() ,user_name,"Pre-Run Started");
				} catch (ParseException e1) {e1.printStackTrace();}
				
			}
		});

		button_blankrun = new JButton("Blank Run");
		button_blankrun.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		button_blankrun.setBounds((int) Math.round(0.179 * wid), (int) Math.round(0.5392 * hei),
				(int) Math.round(0.0651 * wid), (int) Math.round(0.0392 * hei));

		add(button_blankrun);

		button_blankrun.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				start_checking = true;

				temp_status = "b";
				dose = 0;
				fill = 0;
				if (sent_cvok == false) {
					send_cvok();
				} else {
					send_afil();
				}
				current_process = "blank_run";
				experiment_performing.setText("Experiment Performing : Blank Run");
				db_details = db_details + "[ " + get_time() + " ]  Blank-Run Started,";
				try {
					audit_log_push.push_to_audit(get_date(), get_time() ,user_name,"Blank-Run Started");
				} catch (ParseException e1) {e1.printStackTrace();}
			}
		});
		button_std_by_h2o = new JButton("Std. by H2O");
		button_std_by_h2o.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		button_std_by_h2o.setBounds((int) Math.round(0.247 * wid), (int) Math.round(0.5392 * hei),
				(int) Math.round(0.0716 * wid), (int) Math.round(0.0392 * hei));

		add(button_std_by_h2o);
		button_std_by_h2o.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				viewReport.setEnabled(false);
				saveReport.setEnabled(false);
				button_std_by_sodium.setEnabled(false);
				if (blank_run_conducted == true) {
					int result = JOptionPane.showConfirmDialog(null, "Update Blank Volume ? ", "Blank Volume Update",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						Double temp_dose = dose;
						update_blank_vol_metd(temp_dose);
						update_blankvol.setEnabled(false);
						blank_run_conducted = false;
					} else if (result == JOptionPane.NO_OPTION) {
						update_blankvol.setEnabled(false);
						blank_run_conducted = false;
					} else {
						update_blankvol.setEnabled(false);
						blank_run_conducted = false;
					}
				}
				TableColumnModel colMod = table1.getColumnModel();
				TableColumn tabCol;
				if (select_column == true) {
					tabCol = colMod.getColumn(4);
				} else {
					tabCol = colMod.getColumn(3);
				}
				tabCol.setHeaderValue("KFF");
				table1.getTableHeader().repaint();
				temp_status = "h";
				start_checking = true;

				trial_cnt = Integer.valueOf(no_of_trials);
				cur_trial = 1;
				dose = 0;
				fill = 0;
				if (sent_cvok == false) {
					send_cvok();
				} else {
					send_afil();
				}
				current_process = "blank_run";
				experiment_performing.setText("Experiment Performing : Std. by H2O");
				db_details = db_details + "[ " + get_time() + " ]  Std. by H2O Started,";
				kf_trial_timings[0] = get_time();
			}
		});

		button_std_by_sodium = new JButton("Std. by DST");
		button_std_by_sodium.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		
		button_std_by_sodium.setBounds((int) Math.round(0.32 * wid), (int) Math.round(0.5392 * hei),
				(int) Math.round(0.0716 * wid), (int) Math.round(0.0392 * hei));

		add(button_std_by_sodium);
		button_std_by_sodium.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				viewReport.setEnabled(false);
				saveReport.setEnabled(false);
				button_std_by_h2o.setEnabled(false);
				if (blank_run_conducted == true) {
					int result = JOptionPane.showConfirmDialog(null, "Update Blank Volume ? ", "Blank Volume Update",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						Double temp_dose = dose;
						update_blank_vol_metd(temp_dose);
						update_blankvol.setEnabled(false);
						blank_run_conducted = false;
					} else if (result == JOptionPane.NO_OPTION) {
						update_blankvol.setEnabled(false);
						blank_run_conducted = false;
					} else {
						update_blankvol.setEnabled(false);
						blank_run_conducted = false;
					}
				}
				TableColumnModel colMod = table1.getColumnModel();
				TableColumn tabCol;
				if (select_column == true) {
					tabCol = colMod.getColumn(4);
				} else {
					tabCol = colMod.getColumn(3);
				}
				tabCol.setHeaderValue("KFF");
				table1.getTableHeader().repaint();
				temp_status = "t";
				trial_cnt = Integer.valueOf(no_of_trials);
				cur_trial = 1;
				start_checking = true;
				dose = 0;
				fill = 0;
				if (sent_cvok == false) {
					send_cvok();
				} else {
					send_afil();
				}
				current_process = "blank_run";
				experiment_performing.setText("Experiment Performing : Std. by DST");
				db_details = db_details + "[ " + get_time() + " ]  Std. by DST Started,";
				kf_trial_timings[0] = get_time();

			}
		});

		button_analysis = new JButton("Analysis");
		button_analysis.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		button_analysis.setBounds((int) Math.round(0.395 * wid), (int) Math.round(0.5392 * hei),
				(int) Math.round(0.052 * wid), (int) Math.round(0.0392 * hei));

		add(button_analysis);
		button_analysis.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				viewReport.setEnabled(false);
				saveReport.setEnabled(false);
				update_kf_factor.setEnabled(false);
				update_blankvol.setEnabled(false);
				button_std_by_sodium.setEnabled(false);
				button_std_by_h2o.setEnabled(false);

				if (blank_run_conducted == true) {
					int result = JOptionPane.showConfirmDialog(null, "Update Blank Volume ? ", "Blank Volume Update",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						Double temp_dose = dose;
						update_blank_vol_metd(temp_dose);
						update_blankvol.setEnabled(false);
						blank_run_conducted = false; 
					} else if (result == JOptionPane.NO_OPTION) {
						update_blankvol.setEnabled(false);
						blank_run_conducted = false;
					} else {
						update_blankvol.setEnabled(false);
						blank_run_conducted = false;
					}
				}

				if (std_h20_conducted == true || std_disodium_conducted == true) {
					int result = JOptionPane.showConfirmDialog(null, "Update KF Factor ? ", "KF Factor Update",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						if (std_h20_conducted == true) {
							std_h20_result();
						} else if (std_disodium_conducted == true) {
							std_disodium_result();
						}
						update_kf_factor.setEnabled(false);
						viewReport.setEnabled(true);
						saveReport.setEnabled(true);
						kf_done = true;
						std_disodium_conducted = false;
						std_h20_conducted = false;
					} else if (result == JOptionPane.NO_OPTION) {
						update_kf_factor.setEnabled(false);
						std_h20_conducted = false;
						std_disodium_conducted = false;

					} else {
						update_kf_factor.setEnabled(false);
						std_h20_conducted = false;
						std_disodium_conducted = false;
					}
				}
				trial_cnt = Integer.valueOf(no_of_trials);
				if (model.getRowCount() > 0) {
					for (int i = model.getRowCount() - 1; i > -1; i--) {
						model.removeRow(i);
					}
				}
				model.fireTableDataChanged();
				TableColumnModel colMod = table1.getColumnModel();
				TableColumn tabCol;
				if (select_column == true) {
					tabCol = colMod.getColumn(4);
				} else {
					tabCol = colMod.getColumn(3);
				}
				tabCol.setHeaderValue("Moisture  (" + result_unit + ")");
				table1.getTableHeader().repaint();
				start_checking = true;
				temp_status = "a";
				trial_cnt = Integer.valueOf(no_of_trials);
				cur_trial = 1;
				dose = 0;
				fill = 0;
				if (sent_cvok == false) {
					send_cvok();
				} else {
					send_afil();
				}
				current_process = "blank_run";
				experiment_performing.setText("Experiment Performing : Analysis");
				db_details = db_details + "[ " + get_time() + " ]  Analysis Started,";
				moisture_trial_timings[0] = get_time();

			}
		});

		button_esc = new JButton("ESC");
		button_esc.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		button_esc.setBounds((int) Math.round(0.45 * wid), (int) Math.round(0.5392 * hei),
				(int) Math.round(0.052 * wid), (int) Math.round(0.0392 * hei));
		add(button_esc);
		button_esc.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (mv_check_state.matches("g_hundred")) {
					exec_dg_kf_dosr.shutdown();
				}
				if (mv_check_state.matches("g_one")) {
					exec_dg_kf_one.shutdown();
				}
				if (mv_check_state.matches("timer")) {
					exec_dg_kf_timer.shutdown();
				}
				if (mv_check_state.matches("")) {
					exec_dg_kf_fill.shutdown();
				}
				current_process = "";
				try {
					Thread.sleep(100);
					output_dg.print("<8888>ESCP*");
					output_dg.flush();
					ReformatBuffer.current_state = "dg_kf_escp";
				} catch (InterruptedException ex) {
				} catch (NullPointerException ee) {
					JOptionPane.showMessageDialog(null, "Please select the ComPort!");
				}
				current_process = "";
				experiment_performing.setText("Experiment Performing : Escaped");
			}
		});

		JLabel Method_param = new JLabel("Method Parameters");
		Method_param.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0162 * wid)));
		Method_param.setBounds((int) Math.round(0.0162 * wid), (int) Math.round(0.6 * hei),
				(int) Math.round(0.225 * wid), (int) Math.round(0.0306 * hei));
		add(Method_param);

		table11 = new JTable();
		table11.setDefaultEditor(Object.class, null);
		table11.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0104 * wid)));
		table11.setBounds((int) Math.round(0.0195 * wid), (int) Math.round(0.637 * hei), (int) Math.round(0.214 * wid),
				(int) Math.round(0.318 * hei));

		//System.out.println("Constructorrrrrr = " + metd_name);
		model3 = new DefaultTableModel(
				new Object[][] { { "Metd Name", ":", metd_name, "" }, { "Delay", ":", variables[0], "sec" },
						{ "Stir time", ":", variables[1], "sec" }, { "Max vol", ":", variables[2], "mL" },
						{ "Blank vol", ":", variables[3], "mL" }, { "Burette Factor", ":", variables[4], "" },
						{ "Density", ":", variables[5], "" }, { "KF Factor	", ":", variables[6], "" }, },
				new String[] { "Methods", "Parameters", "", "" });
		table11.setModel(model3);
		model3.fireTableDataChanged();
		table11.setShowGrid(false);
		table11.setOpaque(false);
		table11.setRowHeight((int) Math.round(0.0367 * hei));
		table11.setEnabled(false);
		table11.getColumnModel().getColumn(1).setPreferredWidth(10);
		table11.getColumnModel().getColumn(2).setPreferredWidth(30);

		((DefaultTableCellRenderer) table11.getDefaultRenderer(Object.class)).setOpaque(false);
		add(table11);

		table2 = new JTable();
		table2.setDefaultEditor(Object.class, null);
		table2.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0104 * wid)));
		table2.setBounds((int) Math.round(0.211 * wid), (int) Math.round(0.637 * hei), (int) Math.round(0.214 * wid),
				(int) Math.round(0.428 * hei));

		DefaultTableModel model1 = new DefaultTableModel(
				new Object[][] { { "Dosage Rate", ":", variables[8], "" }, { "Result Unit", ":", variables[9], "" },
						{ "No of Trials", ":", variables[10], "" }, { "End Point", ":", variables[7], "mV" }, },
				new String[] { "Methods", "Parameters", "", "" });
		table2.setModel(model1);
		model1.fireTableDataChanged();

		table2.setShowGrid(false);
		table2.setOpaque(false);
		table2.setRowHeight((int) Math.round(0.0367 * hei));
		table2.setEnabled(false);
		table2.getColumnModel().getColumn(1).setPreferredWidth(10);
		table2.getColumnModel().getColumn(2).setPreferredWidth(30);

		((DefaultTableCellRenderer) table2.getDefaultRenderer(Object.class)).setOpaque(false);
		add(table2);

		update_kf_factor = new JButton("Update KF Factor");
		update_kf_factor.setBounds(1280, 361, 140, 32);
		update_kf_factor.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		update_kf_factor.setBounds((int) Math.round(0.833 * wid), (int) Math.round(0.442 * hei),
				(int) Math.round(0.0911 * wid), (int) Math.round(0.0392 * hei));
		add(update_kf_factor);
		update_kf_factor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (std_h20_conducted == true) {
					std_h20_result();
					std_h20_conducted = false;
				} else if (std_disodium_conducted == true) {
					std_disodium_result();
					std_disodium_conducted = false;
				}
				update_kf_factor.setEnabled(false);
				viewReport.setEnabled(true);
				saveReport.setEnabled(true);
				kf_done = true;
			}
		});
		update_kf_factor.setEnabled(false);

		update_blankvol = new JButton("Update Blank Vol");
		update_blankvol.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		update_blankvol.setBounds((int) Math.round(0.833 * wid), (int) Math.round(0.381 * hei),
				(int) Math.round(0.0911 * wid), (int) Math.round(0.0392 * hei));
		add(update_blankvol);
		update_blankvol.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Double temp_dose = dose;
				update_blank_vol_metd(temp_dose);
				update_blankvol.setEnabled(false);
				blank_run_conducted = false;
			}
		});
		update_blankvol.setEnabled(false);

		update_analyze = new JButton("Result");
		update_analyze.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		update_analyze.setBounds((int) Math.round(0.833 * wid), (int) Math.round(0.503 * hei),
				(int) Math.round(0.0911 * wid), (int) Math.round(0.0392 * hei));
		add(update_analyze);
		update_analyze.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				analysis_result_update();
				update_analyze.setEnabled(false);
				viewReport.setEnabled(true);
				saveReport.setEnabled(true);
				analysis_conducted = false;
			}
		});
		update_analyze.setEnabled(false);

		saveReport = new JButton("Save Report");
		saveReport.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		saveReport.setBounds((int) Math.round(0.585 * wid), (int) Math.round(0.637 * hei),
				(int) Math.round(0.0781 * wid), (int) Math.round(0.0392 * hei));
	//	add(saveReport);
		saveReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ffff
			}
		});
		saveReport.setEnabled(false);

		viewReport = new JButton("View Report");
		viewReport.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		viewReport.setBounds((int) Math.round(0.67 * wid), (int) Math.round(0.637 * hei),
				(int) Math.round(0.0781 * wid), (int) Math.round(0.0392 * hei));
		add(viewReport);
		viewReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] aa = { db_report_name, user_name, permission_items };
				DrawReport_kf.main(aa);
			}
		});
		viewReport.setEnabled(false);

		JLabel lblNewLabel_2 = new JLabel("mV Display");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.016 * wid)));
		lblNewLabel_2.setBounds((int) Math.round(0.846 * wid), (int) Math.round(0.0612 * hei),
				(int) Math.round(0.13 * wid), (int) Math.round(0.0392 * hei));
		add(lblNewLabel_2);

		kf_mv_display = new JLabel();
		kf_mv_display.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0143 * wid)));
		kf_mv_display.setBounds((int) Math.round(0.846 * wid), (int) Math.round(0.0919 * hei),
				(int) Math.round(0.055 * wid), (int) Math.round(0.0563 * hei));
		kf_mv_display.setText("0 mV");
		add(kf_mv_display);

		vol_filled = new JLabel("Volume filled: 00.000 ml");
		vol_filled.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.016 * wid)));
		vol_filled.setBounds((int) Math.round(0.58 * wid), (int) Math.round(0.061 * hei), (int) Math.round(0.227 * wid),
				(int) Math.round(0.0612 * hei));
		add(vol_filled);

		vol_dosed = new JLabel("Volume Dosed: 0.000 ml");
		vol_dosed.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.016 * wid)));
		vol_dosed.setBounds((int) Math.round(0.579 * wid), (int) Math.round(0.147 * hei), (int) Math.round(0.227 * wid),
				(int) Math.round(0.0563 * hei));
		add(vol_dosed);

		delay_timer = new JLabel("Delay Timer : 0 s.");
		delay_timer.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.016 * wid)));
		delay_timer.setBounds((int) Math.round(0.579 * wid), (int) Math.round(0.232 * hei),
				(int) Math.round(0.195 * wid), (int) Math.round(0.0392 * hei));
		add(delay_timer);

		result_header = new JLabel("Results :");
		result_header.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.013 * wid)));
		result_header.setBounds((int) Math.round(0.585 * wid), (int) Math.round(0.318 * hei),
				(int) Math.round(0.195 * wid), (int) Math.round(0.0392 * hei));
		add(result_header);
		select_column = get_permission();
		System.out.println("Select Column : " + select_column);
		initialize();
	}

	@SuppressWarnings("removal")
	public static void initialize() {
		frame1.getContentPane().invalidate();
		frame1.getContentPane().validate();
		frame1.getContentPane().repaint();
		five_column();
		add_to_db();
	}

	public static void kf_home_dosr() {
		try {
			Thread.sleep(100);
			output_dg.print("<8888>ESCP*");
			output_dg.flush();
			ReformatBuffer.current_state = "dg_kf_home_escp";
		} catch (InterruptedException ex) {
		} catch (NullPointerException ee) {
			JOptionPane.showMessageDialog(null, "Please select the ComPort!");
		}
	}
	public static void kf_home_escp() {
		String aa[] = new String[1];
		aa[0] = "aa";
		menubar.main(aa);
		ReformatBuffer.current_exp = "main";
		menubar.enable_all(true);
		menubar.send_cvol_kfpot();
		menubar.frame1.setTitle("Mayura Analytical      Logged in as - "+user_name+"      Connected to Comport : "+sp1.getDescriptivePortName());
		sent_cvok = false;
		
		try {
			audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Returning to Home from KF");
		} catch (ParseException e1) {e1.printStackTrace();}

		reset();
		
		frame1.dispose();
		frame1 = new JFrame();
		p = new JPanel();
		p.invalidate();
		p.revalidate();
		p.repaint();
	}
	
	
	public static void add_row_to_five_column(int r, String v1, String w_g, String kff) {
		if (select_column == true) {
			model.addRow(new Object[0]);
			model.setValueAt(true, r, 0);
			model.setValueAt(r + 1, r, 1);
			model.setValueAt(v1, r, 2);
			model.setValueAt(w_g, r, 3);
			model.setValueAt(kff, r, 4);
			model.fireTableDataChanged();
		} else {
			model.addRow(new Object[0]);
			model.setValueAt(r + 1, r, 0);
			model.setValueAt(v1, r, 1);
			model.setValueAt(w_g, r, 2);
			model.setValueAt(kff, r, 3);
			model.fireTableDataChanged();
		}
	}

	public static void five_column() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds((int) Math.round(0.39 * wid), (int) Math.round(0.74 * hei), (int) Math.round(0.585 * wid),
				(int) Math.round(0.181 * hei));
		frame1.getContentPane().add(scrollPane);
		table1 = new JTable();
		table1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0097 * wid)));
		if (select_column == true) {
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
					default:
						return String.class;
					}
				}
			};
			table1.setModel(model);
			table1.setDefaultEditor(Object.class, null);
			model.addColumn("Select");
			model.addColumn("Trial No");
			model.addColumn("Volume Dosed (mL)");
			model.addColumn("Weight in mg or microlitre");
			model.addColumn("KFF");
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
					default:
						return String.class;
					}
				}
			};
			table1.setModel(model);
			table1.setDefaultEditor(Object.class, null);
			model.addColumn("Trial No");
			model.addColumn("Volume Dosed (mL)");
			model.addColumn("Weight in mg or microlitre");
			model.addColumn("KFF");
		}
		table1.setRowHeight(25);
		scrollPane.setViewportView(table1);
		
		table1.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent event) {
	        	System.out.println("Clicked clicked");
	        	if(select_column == true) {
	        	//	if (trial_cnt > 0) { 
	        			double[] temp_double = new double[5]; 
	        			int r=0;
	        			double temp_res = 0;
	        			for (int i = 0; i < table1.getRowCount(); i++) {
	        				Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
	        	        	System.out.println("Clicked clicked checked = "+i);
	        				if (checked) {
	        					String aa = table1.getValueAt(i, 4).toString();
	        					temp_double[r] = Double.parseDouble(aa);
	        					temp_res+=Double.parseDouble(aa);
	        					r++;
	        				}
	        			}
	        			temp_res = temp_res/r;
    					update_result_text("<html>Avg. KFF = " + String.format("%.4f", temp_res)+"<br/>RSD = "+String.format("%.2f",(SD(temp_double, r)*100))+"</html>");
	        		//codecode}
	        	}
	        }
	    });
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

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == b) {
			if (e.getStateChange() == 1) {
			}
		} else {

			if (e.getStateChange() == 1) {
			}
		}
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

	public static void add_kff_to_db(String std) {
		
		String[] temp_trial_cnt = db_kff_trials.split(":");

		
		String temp_params = "";
		String[] temp_arr = db_parameters.split(",");
		for (int i = 0; i < 23; i++) {
			if (i == 0) {
				temp_params = temp_params + temp_arr[i];
			} else if (i == 21) {
				temp_params = temp_params + "," + std;
			}else if (i == 14) {
				temp_params = temp_params + "," + temp_trial_cnt.length;
			}
			else if(i == 10) {
				temp_params = temp_params + "," + kf_factor;
			}
			else {
				temp_params = temp_params + "," + temp_arr[i];
			}
		}

		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql;
		boolean present = false;
		try {
			sql = "UPDATE kf SET parameters = ? ," + "kff_trials = ? , " + "kff_result = ?, " + "details = ? "
					+ "WHERE report_name = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, temp_params);
			ps.setString(2, db_kff_trials);
			ps.setString(3, db_kff_results);
			ps.setString(4, db_details);
			ps.setString(5, db_report_name);
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

	public static void add_moisture_to_db() {
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql;
		boolean present = false;
		try {
			sql = "UPDATE kf SET moisture_trials = ? , " + "moisture_result = ? , " +"parameters = ? , " + "details = ? "
					+ "WHERE report_name = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, db_moisture_trials);
			ps.setString(2, db_moisture_results);
			ps.setString(3, db_parameters);
			ps.setString(4, db_details);
			ps.setString(5, db_report_name);
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

	public static void add_to_db() {
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql;
		boolean present = false;
		try {
			sql = "INSERT INTO kf(report_name,date,parameters,details,kff_trials,kff_result,moisture_trials,moisture_result,remarks) VALUES(?,?,?,?,?,?,?,?,?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, db_report_name);
			ps.setString(2, get_date());
			ps.setString(3, db_parameters);
			ps.setString(4, db_details);
			ps.setString(5, db_kff_trials);
			ps.setString(6, db_kff_results);
			ps.setString(7, db_moisture_trials);
			ps.setString(8, db_moisture_results);
			ps.setString(9, "No Remarks");
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

	public static void delete_from_db() {
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql;
		boolean present = false;
		try {
			sql = "DELETE FROM kf WHERE report_name = ?";
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
	}

	public static void main(String[] args) {
		if (args.length != 0) {
			metd_name = args[0];
			metd_data = args[1];
			variables = args[1].split(",");
			delay_val = variables[0];
			stir_time = variables[1];
			max_vol = variables[2];
			blank_vol = variables[3];
			burette_factor = variables[4];
			density = variables[5];
			kf_factor = variables[6];
			end_point = variables[7];
			dosage_rate = variables[8];
			result_unit = variables[9];
			no_of_trials = variables[10];
			sop = variables[11];
			trial_cnt = Integer.valueOf(no_of_trials);
			db_parameters = args[args.length - 3] + "," + get_date() + "," + get_time() + "," + args[0] + ","
					+ delay_val + "," + stir_time + "," + max_vol + "," + blank_vol + "," + burette_factor + ","
					+ density + "," + kf_factor + "," + end_point + "," + dosage_rate + "," + result_unit + ","
					+ no_of_trials + "," + sop + "," + args[2] + "," + args[3] + "," + args[4] + "," + args[5] + ","
					+ args[6] + "," + "NA" + "," + "Not Certified";
			//System.out.println("PARAMETERS : " + args[8]);
			db_report_name = args[5];
			user_name = args[7];
			permission_items = args[8];
			roles_list = args[9];
		}
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame1.getGraphicsConfiguration());
		int taskHeight = screenInsets.bottom;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		hei = d.height - taskHeight;
		wid = d.width;
	//	System.out.println(wid + "   dfvdvdv " + hei);
		frame1.setBounds(0, 0, wid, hei);
		frame1.add(p);
		frame1.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame1.getContentPane().add(new DrawGraph_kf());
		frame1.setResizable(true);
		frame1.setVisible(true);
		frame1.repaint();
		ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
		frame1.setIconImage(img.getImage());
		frame1.setTitle("KF        Logged in as "+user_name);

		
		ReformatBuffer.current_exp = "kf";
		frame1.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				try {
					Thread.sleep(500);
					output_dg.print("<8888>ESCP*");
					output_dg.flush();
				} catch (InterruptedException ex) {
				} catch (NullPointerException ee) {
					JOptionPane.showMessageDialog(null, "Please select the ComPort!");
				}
				try {
					sp1.closePort();
				} catch (NullPointerException npn) {
				}
				if (blank_run_conducted == true) {
					int result = JOptionPane.showConfirmDialog(null, "Update Blank Volume ? ", "Blank Volume Update",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						Double temp_dose = dose;
						update_blank_vol_metd(temp_dose);
						update_blankvol.setEnabled(false);
						blank_run_conducted = false;
					} else if (result == JOptionPane.NO_OPTION) {
						update_blankvol.setEnabled(false);
						blank_run_conducted = false;
						delete_from_db();
					} else {
						update_blankvol.setEnabled(false);
						blank_run_conducted = false;
						delete_from_db();
					}
				}
				if (std_h20_conducted == true || std_disodium_conducted == true) {
					int result = JOptionPane.showConfirmDialog(null, "Update KF Factor ? ", "KF Factor Update",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						if (std_h20_conducted == true) {
							std_h20_result();
						} else if (std_disodium_conducted == true) {
							std_disodium_result();
						}
						update_kf_factor.setEnabled(false);
						viewReport.setEnabled(true);
						saveReport.setEnabled(true);
						kf_done = true;
						std_disodium_conducted = false;
						std_h20_conducted = false;
					} else if (result == JOptionPane.NO_OPTION) {
						update_kf_factor.setEnabled(false);
						std_h20_conducted = false;
						std_disodium_conducted = false;
						delete_from_db();
					} else {
						update_kf_factor.setEnabled(false);
						std_h20_conducted = false;
						std_disodium_conducted = false;
						delete_from_db();
					}
				}
				if (analysis_conducted == true) {
					int result = JOptionPane.showConfirmDialog(null, "Analyze ? ", "Analysis",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						analysis_result_update();
						update_analyze.setEnabled(false);
						viewReport.setEnabled(true);
						saveReport.setEnabled(true);

					} else if (result == JOptionPane.NO_OPTION) {
						delete_from_db();
						analysis_conducted = false;
					} else {
						delete_from_db();
						analysis_conducted = false;
					}
				}
				
				check_details_from_db();
					
				try {
					audit_log_push.push_to_audit(get_date(), get_time(),user_name,"KFF closed!");
				} catch (ParseException e1) {e1.printStackTrace();}
				reset();

				frame1.dispose();
				frame1 = new JFrame();
				p = new JPanel();
				p.invalidate();
				p.revalidate();
				p.repaint();
			}
		});
	}
	
	public static void check_details_from_db() {
		boolean temp_result = false;
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql;
		sql = "SELECT details FROM kf WHERE report_name = '"+db_report_name+"'";
		try {
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			String details = rs.getString("details");			
			if (details.matches("")) {
				try {
					sql = "DELETE FROM kf WHERE report_name = ?";
					ps = con.prepareStatement(sql);
					ps.setString(1, db_report_name);
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
	
	
}

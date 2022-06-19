package main.java;

import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import com.fazecast.jSerialComm.SerialPort;
import com.sun.jdi.connect.Connector.SelectedArgument;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.SystemColor;
import java.awt.Toolkit;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JLayeredPane;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.util.regex.Pattern;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class menubar extends JPanel implements ItemListener {

	static boolean checked_vaidity = false;

	static float dose_rate_variable = 20;
	static JFrame frame1 = new JFrame();
	static JPanel p = new JPanel();

	public static SerialPort serial_port1;
	public static PrintWriter output;
	public static SerialPort[] ports;
	SerialPort firstAvailableComPort;
	String data;

	static JLabel jlabel_mv_Value, jlabel_ml_Value, lblEpSelect, text_blank_vol, lblNewLabel_pot_tendency,
			lblNewLabel_21, lblNewLabel_22;

	private static JTextField textField_11, textField_12;
	public static String selected_experiment = "", selected_methodfile = null;

	static JComboBox comboBox_filter, comboBox_threshold, comboBox_formula, comboBox_epselect, comboBox_unit,
			comboBox_dosageRate, comboBox_pot_tendency;
	BufferedReader br;
	static int saved = 0;
	public static boolean saved_file = false;
	public static String saved_file_name = null, st;

	static JPanel panel_pot1, panel_pot2, panel_pot3, panel_pot4, panel_pot5;
	static JLabel pot_predose, pot_stirtime, pot_maxvol, pot_blankvol, pot_burette, pot_threshold, pot_filter,
			pot_no_of_trials, pot_tendency, pot_f1, pot_f2, pot_f3, pot_f4, pot_epselect, pot_formulaNo, pot_dosagerate,
			pot_resultunit, pot_units_predose, pot_units_stirtime, pot_units_maxvol, pot_units_blankvol,
			pot_units_dosagerate, pot_units_threshold, pot_sop;
	static JTextField pot_tf_predose, pot_tf_stirtime, pot_tf_maxvol, pot_tf_blankvol, pot_tf_burette, pot_tf_factor1,
			pot_tf_factor2, pot_tf_factor3, pot_tf_factor4, pot_tf_sop_value;
	static JComboBox pot_cb_threshold, pot_cb_filter, pot_cb_dosagerate, pot_cb_nooftrials, pot_cb_epselect,
			pot_cb_formula, pot_cb_tendency, pot_cb_resultunit;

	static JPanel panel_kf1, panel_kf2, panel_kf3, panel_kf4, panel_kf5;
	static JLabel kf_stirtime, kf_delay, kf_maxvol, kf_blankvol, kf_burette, kf_density, kf_factor, kf_no_of_trials,
			kf_endpoint, kf_dosagerate, kf_resultunit, kf_units_stir, kf_units_delay, kf_units_maxvol,
			kf_units_blankvol, kf_units_dosagerate, kf_units_endpoint, kf_sop;
	static JTextField kf_tf_delay, kf_tf_stirtime, kf_tf_maxvol, kf_tf_blankvol, kf_tf_burette, kf_tf_density,
			kf_tf_factor, kf_tf_endpoint, kf_tf_sop_value;
	static JComboBox kf_cb_dosagerate, kf_cb_nooftrials, kf_cb_resultunit;

	static JPanel panel_ph1, panel_ph2, panel_ph3, panel_ph4, panel_ph5;
	static JLabel ph_stirtime, ph_delay, ph_predose, ph_maxvol, ph_blankvol, ph_burette, ph_endpoint, ph_no_of_trials,
			ph_f1, ph_f2, ph_f3, ph_f4, ph_formulaNo, ph_dosagerate, ph_tendency, ph_resultunit, ph_calibrate,
			ph_slope1, ph_slope2, ph_units_stirtime, ph_units_delay, ph_units_predose, ph_units_maxvol,
			ph_units_blankvol, ph_units_dosagerate, ph_units_endpoint, ph_sop;
	static JTextField ph_tf_stirtime, ph_tf_delay, ph_tf_predose, ph_tf_maxvol, ph_tf_blankvol, ph_tf_burette,
			ph_tf_endpoint, ph_tf_factor1, ph_tf_factor2, ph_tf_factor3, ph_tf_factor4, ph_tf_slope1, ph_tf_slope2,
			ph_tf_sop_value;
	static JComboBox ph_cb_tendency, ph_cb_dosagerate, ph_cb_nooftrials, ph_cb_calibrate, ph_cb_formula,
			ph_cb_resultunit;

	static JPanel panel_amp1, panel_amp2, panel_amp3, panel_amp4, panel_amp5;
	static JLabel amp_stirtime, amp_delay, amp_predose, amp_maxvol, amp_blankvol, amp_burette, amp_endpoint,
			amp_no_of_trials, amp_f1, amp_f2, amp_f3, amp_f4, amp_formulaNo, amp_dosagerate, amp_filter, amp_resultunit,
			amp_units_stirtime, amp_units_delay, amp_units_predose, amp_units_maxvol, amp_units_blankvol,
			amp_units_dosagerate, amp_units_endpoint, amp_sop;
	static JTextField amp_tf_stirtime, amp_tf_delay, amp_tf_predose, amp_tf_maxvol, amp_tf_blankvol, amp_tf_burette,
			amp_tf_endpoint, amp_tf_factor1, amp_tf_factor2, amp_tf_factor3, amp_tf_factor4, amp_tf_sop_value;
	static JComboBox amp_cb_filter, amp_cb_dosagerate, amp_cb_nooftrials, amp_cb_formula, amp_cb_resultunit;

	static int counter = 0;
	static String res;
	static JLabel metd_header;
	boolean vol_counter = false;
	public static int wash_counter = 0;
	public static int rinse_counter = 0;
	static double fill_dose = 0;
	static int wash_rinse = 1;

	static ScheduledExecutorService exec1, exec2, exec3, exec4;

	static JLabel lblNewLabel_vol;
	static JButton btn_fill_mb, btn_dose_mb, btn_wash_mb, btn_rinse_mb;
	static JButton btn_open_mb, btn_run_mb, btn_save_mb, btn_refresh_mb, btn_esc_mb;

	static JMenuItem mnNewMenu_7, menuItem_burette, menu_item_log, menu_item_print_report, menu_item_print_method,
			menu_item_print_log, menu_item_exit, menu_item_mv_display, menu_item_measure, menu_item_calibrate,
			menu_item_pot, menu_item_kf, menu_item_ph, menu_item_amp, menu_item_login, menu_item_sa_login,
			menu_item_view_reports, menu_item_comport, menu_item_logout, menuItem_add_sop, menuItem_custom_formula,
			menuItem_device_data;
	static JRadioButton rdbtnNewRadioButton, rdbtnNewRadioButton_1, rdbtnNewRadioButton_2, rdbtnNewRadioButton_3;
	static int u = 0;
	static int v = 0;
	static Double volume;
	static String mb_cur_state = "";
	static String math = "", math2 = "", math3 = "";
	static TeXFormula formula, formula2, formula3;
	static TeXIcon ti_formula, ti_formula2, ti_formula3;
	static BufferedImage b_formula, b_formula2, b_formula3;
	static JPanel p_formula, p_formula2, p_formula3;
	static JLabel l_formula, l_formula2, l_formula3, formula_header;
	int formula_cnt = 0;
	static String user_name = "testing", roles_list = "testing", role_items = "testing", admin_user_name = "testing";

	static boolean no_update = false;

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame1.getGraphicsConfiguration());
		int taskHeight = screenInsets.bottom;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int height = d.height - taskHeight;
		int width = d.width;
		double wid = d.getWidth();
		double hei = d.getHeight() - taskHeight;
		// system.out.println(width + " dfvdvdv " + hei);
		frame1.setTitle("Mayura Analytical Titrator");

		frame1.setBounds(0, 0, width, (int) hei);
		// frame1.setBounds(0,0,1280,720);

		frame1.add(p);
		frame1.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame1.getContentPane().add(new menubar());
		ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
		frame1.setIconImage(img.getImage());

		frame1.setResizable(true);
		frame1.setVisible(true);
		frame1.repaint();

		frame1.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				try {
					Thread.sleep(500);
					output.print("<8888>ESCP*");
					output.flush();
					Thread.sleep(200);
				} catch (NullPointerException ne) {
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					serial_port1.closePort();
				} catch (NullPointerException ne) {
				}
				
				try {
					audit_log_push.push_to_audit(get_date(), get_time(),user_name,"Software closed!");
				} catch (ParseException e1) {e1.printStackTrace();}
				
				frame1.dispose();
				frame1 = new JFrame();
				p = new JPanel();
				p.revalidate();
				p.repaint();
			}
		});
	}

	@SuppressWarnings("removal")
	public static void initialize() {
		frame1.getContentPane().invalidate();
		frame1.getContentPane().validate();
		frame1.getContentPane().repaint();
	}

	public static void open_draw_graph(String[] aa) {

		// aa =
		// {method_name,method_data,ar,batch,sample_name,normality_val,moisture_val,report_name,titrant_name};

		if (serial_port1 != null) {
			// hiieee
			try {
				Thread.sleep(200);
				output.print("<8888>ESCP*");
				output.flush();
			} catch (InterruptedException jkb) {
			}

			metd_header.setText("");
			saved_file = false;

			String b[] = new String[aa.length + 5];
			for (int i = 0; i < aa.length; i++)
				b[i] = aa[i];

			b[aa.length] = math;
			b[aa.length + 1] = math2;
			b[aa.length + 2] = math3;
			b[aa.length + 3] = user_name;
			b[aa.length + 4] = role_items;

			DrawGraph_pot.main(b);
			DrawGraph_pot.port_setup(serial_port1);
			frame1.dispose();
			frame1 = new JFrame();
			p = new JPanel();
			p.revalidate();
			p.repaint();
		} else {
			JOptionPane.showMessageDialog(null, "Please select a ComPort!");
		}
	}

	public static void open_draw_graph_kf(String[] aa) {

		if (serial_port1 != null) {
			output.print("<8888>ESCP*");
			output.flush();
			metd_header.setText("");
			saved_file = false;
			int f = aa.length;
			String[] temp_array = new String[f + 3];
			for (int i = 0; i < f; i++) {
				temp_array[i] = aa[i];
			}
			temp_array[f] = user_name;
			temp_array[f + 1] = role_items;
			temp_array[f + 2] = roles_list;

			DrawGraph_kf.main(temp_array);
			DrawGraph_kf.port_setup(serial_port1);
			frame1.dispose();
			frame1 = new JFrame();
			p = new JPanel();
			p.invalidate();
			p.revalidate();
			p.repaint();
		} else {
			JOptionPane.showMessageDialog(null, "Please select a ComPort!");
		}
	}

	public static void escape() {
		if (wash_counter == 0 && rinse_counter == 0) {
			exec1.shutdown();
			exec2.shutdown();
			exec3.shutdown();
		}

		lblNewLabel_vol.setText("Volume Filled");
		jlabel_ml_Value.setText("0.0");
		btn_fill_mb.setEnabled(true);
		btn_dose_mb.setEnabled(true);
		btn_wash_mb.setEnabled(true);
		btn_rinse_mb.setEnabled(true);
		btn_open_mb.setEnabled(true);
		btn_run_mb.setEnabled(true);
		btn_save_mb.setEnabled(true);
		btn_refresh_mb.setEnabled(true);
		fill_dose = 0;
		wash_rinse = 1;
	}

	public static void wash_cycle_count(String wash_count) {
		//// system.out.println("washeddd11:" + wash_count);
		wash_counter = Integer.parseInt(wash_count);
		send_wash_signal();
	}

	public static void rinse_cycle_count(String rinse_count) {
		// getting the count from popup :- open_rinse_counter.java
		//// system.out.println("rinseddd11:" + rinse_count);
		rinse_counter = Integer.parseInt(rinse_count);
		send_rinse_signal();
	}

	public static void update_wash_cycle() {
		if (wash_counter > 1) {
			wash_counter--;
			wash_rinse++;
			try {
				Thread.sleep(500);
				send_wash_signal();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		} else {
			wash_rinse = 1;

			lblNewLabel_vol.setText("Volume Filled ");
			jlabel_ml_Value.setText("0.0 mL");

			JOptionPane.showMessageDialog(null, "Wash Cycles Completed!");

			btn_fill_mb.setEnabled(true);
			btn_dose_mb.setEnabled(true);
			btn_wash_mb.setEnabled(true);
			btn_rinse_mb.setEnabled(true);
			btn_open_mb.setEnabled(true);
			btn_run_mb.setEnabled(true);
			btn_save_mb.setEnabled(true);
			btn_refresh_mb.setEnabled(true);
		}
	}

	public static void update_rinse_cycle() {

		if (rinse_counter > 1) {
			rinse_counter--;
			wash_rinse++;
			try {
				Thread.sleep(500);
				send_rinse_signal();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		} else {
			//// system.out.println(" rinse counter inside else rinse_counter = " +
			//// rinse_counter);
			wash_rinse = 1;
			lblNewLabel_vol.setText("Volume Filled ");
			jlabel_ml_Value.setText("0.0 mL");
			JOptionPane.showMessageDialog(null, "Rinse Cycles Completed!");

			btn_fill_mb.setEnabled(true);
			btn_dose_mb.setEnabled(true);
			btn_wash_mb.setEnabled(true);
			btn_rinse_mb.setEnabled(true);
			btn_open_mb.setEnabled(true);
			btn_run_mb.setEnabled(true);
			btn_save_mb.setEnabled(true);
			btn_refresh_mb.setEnabled(true);
		}
	}

	public static void wash_started() {
		//// system.out.println("rinse_counter = " + rinse_counter);
		lblNewLabel_vol.setText("Wash Cycle " + wash_rinse);
		jlabel_ml_Value.setText("");
	}

	public static void rinse_started() {
		lblNewLabel_vol.setText("Rinse Cycle " + wash_rinse);
		jlabel_ml_Value.setText("");
	}

	public static void send_wash_signal() {
		try {
			//// system.out.println("Sending Wash Signal = counter = " + wash_counter);

			output.print("<8888>WASH*");
			output.flush();
			ReformatBuffer.current_state = "menubar_wash";
			btn_fill_mb.setEnabled(false);
			btn_dose_mb.setEnabled(false);
			btn_wash_mb.setEnabled(false);
			btn_rinse_mb.setEnabled(false);
			btn_open_mb.setEnabled(false);
			btn_run_mb.setEnabled(false);
			btn_save_mb.setEnabled(false);
			btn_refresh_mb.setEnabled(false);
		} catch (NullPointerException ee) {
			JOptionPane.showMessageDialog(null, "Please select the ComPort!");
		}
	}

	public static void send_rinse_signal() {
		try {
			//// system.out.println("Sending Rinse Signal = counter = " + rinse_counter);
			output.print("<8888>RINS*");
			output.flush();
			ReformatBuffer.current_state = "menubar_rinse";
			btn_fill_mb.setEnabled(false);
			btn_dose_mb.setEnabled(false);
			btn_wash_mb.setEnabled(false);
			btn_rinse_mb.setEnabled(false);
			btn_open_mb.setEnabled(false);
			btn_run_mb.setEnabled(false);
			btn_save_mb.setEnabled(false);
			btn_refresh_mb.setEnabled(false);
		} catch (NullPointerException ee) {
			JOptionPane.showMessageDialog(null, "Please select the ComPort!");
		}
	}

	public static void start_volume_counter() {
		if (mb_cur_state.matches("filling")) {
			lblNewLabel_vol.setText("Volume Filled");
			jlabel_ml_Value.setText("0.0");

			btn_fill_mb.setEnabled(false);
			btn_dose_mb.setEnabled(false);
			btn_wash_mb.setEnabled(false);
			btn_rinse_mb.setEnabled(false);
			btn_open_mb.setEnabled(false);
			btn_run_mb.setEnabled(false);
			btn_save_mb.setEnabled(false);
			btn_refresh_mb.setEnabled(false);

			exec1 = Executors.newSingleThreadScheduledExecutor();
			exec1.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					if (fill_dose < volume) {
						fill_dose = fill_dose + ((dose_rate_variable / 60.0) / 10.0);
						jlabel_ml_Value.setText(String.format("%.3f", fill_dose) + "mL");
					} else {
						try {
							Thread.sleep(300);
							output.print("<8888>ESCP*");
							output.flush();
							stop_volume_counter();
							mb_cur_state = "";
							ReformatBuffer.current_state = "menubar_stpm";
						} catch (NullPointerException ex) {
							JOptionPane.showMessageDialog(null, "Please connect to ComPort!");
						} catch (InterruptedException jb) {
						}
					}
				}
			}, 0, 100, TimeUnit.MILLISECONDS);
		} else if (mb_cur_state.matches("dosing")) {
			lblNewLabel_vol.setText("Filling in process...");
			try {
				exec1.shutdown();
			} catch (NullPointerException dfj) {
			}
			try {
				exec2.shutdown();
			} catch (NullPointerException df) {
			}
			try {
				exec3.shutdown();
			} catch (NullPointerException ervf) {
			}
			try {
				exec4.shutdown();
			} catch (NullPointerException ervf) {
			}
		}
	}

	public static void stop_volume_counter() {
		// fill_dose=0;
		mb_cur_state = "";
		try {
			exec1.shutdown();
		} catch (NullPointerException dfj) {
		}
		try {
			exec2.shutdown();
		} catch (NullPointerException df) {
		}
		try {
			exec3.shutdown();
		} catch (NullPointerException ervf) {
		}
		try {
			exec4.shutdown();
		} catch (NullPointerException ervf) {
		}
		JOptionPane.showMessageDialog(null, "Filling Complete!");

		btn_fill_mb.setEnabled(true);
		btn_dose_mb.setEnabled(true);
		btn_wash_mb.setEnabled(true);
		btn_rinse_mb.setEnabled(true);
		btn_open_mb.setEnabled(true);
		btn_run_mb.setEnabled(true);
		btn_save_mb.setEnabled(true);
		btn_refresh_mb.setEnabled(true);
	}

	public static void mb_fill_end_received() {
		if (mb_cur_state.matches("filling")) {
			//// system.out.println("Inside mb_fill end");
			try {
				Thread.sleep(300);
				output.print("<8888>ESCP*");
				output.flush();
				stop_volume_counter();
				mb_cur_state = "";
				ReformatBuffer.current_state = "menubar_stpm";
			} catch (NullPointerException ex) {
				JOptionPane.showMessageDialog(null, "Please connect to ComPort!");
			} catch (InterruptedException jb) {
			}
		}
		if (mb_cur_state.matches("dosing")) {
			if (fill_dose < volume) {
				try {
					Thread.sleep(300);
					output.print("<8888>DOSE*");
					output.flush();
					lblNewLabel_vol.setText("Volume Dosed");
					ReformatBuffer.current_state = "menubar_dose";
				} catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(null, "Please connect to ComPort!");
				} catch (InterruptedException tt) {

				}
			}
		}
	}

	public static void start_dose_counter() {
		if (!mb_cur_state.matches("filling")) {
			lblNewLabel_vol.setText("Volume Dosed");
			jlabel_ml_Value.setText("0.0");
			btn_fill_mb.setEnabled(false);
			btn_dose_mb.setEnabled(false);
			btn_wash_mb.setEnabled(false);
			btn_rinse_mb.setEnabled(false);
			btn_open_mb.setEnabled(false);
			btn_run_mb.setEnabled(false);
			btn_save_mb.setEnabled(false);
			btn_refresh_mb.setEnabled(false);
			exec4 = Executors.newSingleThreadScheduledExecutor();
			exec4.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					// do stuff
					if (fill_dose < volume) {

						////// system.out.println("Dose Value Main = "+);
						fill_dose = fill_dose + ((dose_rate_variable / 60.0) / 10.0);
						jlabel_ml_Value.setText(String.format("%.3f", fill_dose) + "mL");
					} else {
						System.out.println("Dosing Full Value Reached!");
						try {
							exec1.shutdown();
						} catch (NullPointerException dfj) {
						}
						try {
							exec2.shutdown();
						} catch (NullPointerException df) {
						}
						try {
							exec3.shutdown();
						} catch (NullPointerException ervf) {
						}
						try {
							exec4.shutdown();
						} catch (NullPointerException ervf) {
						}

						try {
							Thread.sleep(300);
							output.print("<8888>ESCP*");
							output.flush();
							stop_dose_counter();
							mb_cur_state = "";
							ReformatBuffer.current_state = "menubar_stpm";
						} catch (NullPointerException ex) {
							JOptionPane.showMessageDialog(null, "Please connect to ComPort!");
						} catch (InterruptedException wed) {

						}
					}

				}
			}, 0, 100, TimeUnit.MILLISECONDS);
		}
	}

	public static void mb_dose_end_received() {
		try {
			exec1.shutdown();
		} catch (NullPointerException dfj) {
		}
		try {
			exec2.shutdown();
		} catch (NullPointerException df) {
		}
		try {
			exec3.shutdown();
		} catch (NullPointerException ervf) {
		}
		try {
			exec4.shutdown();
		} catch (NullPointerException ervf) {
		}
		if (mb_cur_state.matches("filling")) {
			if (fill_dose < volume) {
				try {
					Thread.sleep(300);
					output.print("<8888>FILL*");
					output.flush();
					lblNewLabel_vol.setText("Volume Filled");
					ReformatBuffer.current_state = "menubar_fill";
				}

				catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(null, "Please connect to ComPort!");
				} catch (InterruptedException tt) {

				}
			}
		} else if (mb_cur_state.matches("dosing")) {
			if (fill_dose < volume) {
				try {
					Thread.sleep(300);
					output.print("<8888>FILL*");
					output.flush();
					try {
						exec1.shutdown();
					} catch (NullPointerException dfj) {
					}
					try {
						exec2.shutdown();
					} catch (NullPointerException df) {
					}
					try {
						exec3.shutdown();
					} catch (NullPointerException ervf) {
					}
					try {
						exec4.shutdown();
					} catch (NullPointerException ervf) {
					}
					lblNewLabel_vol.setText("Filling in Process...");
					ReformatBuffer.current_state = "menubar_fill";
				} catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(null, "Please connect to ComPort!");
				} catch (InterruptedException exd) {
				}
			}
		}
	}

	public static void stop_dose_counter() {
		try {
			exec1.shutdown();
		} catch (NullPointerException dfj) {
		}
		try {
			exec2.shutdown();
		} catch (NullPointerException df) {
		}
		try {
			exec3.shutdown();
		} catch (NullPointerException ervf) {
		}
		try {
			exec4.shutdown();
		} catch (NullPointerException ervf) {
		}
		JOptionPane.showMessageDialog(null, "Dosing Complete!");

		mb_cur_state = "";
		btn_fill_mb.setEnabled(true);
		btn_dose_mb.setEnabled(true);
		btn_wash_mb.setEnabled(true);
		btn_rinse_mb.setEnabled(true);
		btn_open_mb.setEnabled(true);
		btn_run_mb.setEnabled(true);
		btn_save_mb.setEnabled(true);
		btn_refresh_mb.setEnabled(true);
	}

	public static void update_mv_main(String msg) {
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm:ss:SSS");  
//		LocalDateTime now = LocalDateTime.now();  
//		////system.out.println("Update MV Main = " + msg+"  Time  = "+dtf.format(now));
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
			jlabel_mv_Value.setText(int_temp_mv + " mV");
		} else if (msg.contains("N")) {
			jlabel_mv_Value.setText("-" + int_temp_mv + " mV");
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

	public static void refresh_items() {
		pot_tf_predose.setText("0");
		pot_tf_stirtime.setText("10");
		pot_tf_maxvol.setText("100");
		pot_tf_blankvol.setText("0");
		pot_tf_burette.setText("1");
		pot_cb_threshold.setSelectedItem("1");
		pot_cb_filter.setSelectedItem("1");
		pot_cb_dosagerate.setSelectedItem("0.5");
		pot_cb_nooftrials.setSelectedItem("1");
		pot_tf_factor1.setText("1");
		pot_tf_factor2.setText("1");
		pot_tf_factor3.setText("1");
		pot_tf_factor4.setText("1");
		pot_cb_epselect.setSelectedItem("Auto");
		pot_cb_formula.setSelectedItem("1");
		//pot_cb_tendency.setSelectedItem("Up");
		pot_cb_resultunit.setSelectedItem("%");
		pot_tf_sop_value.setText("Not Selected");

		ph_tf_delay.setText("0");
		ph_tf_predose.setText("0");
		ph_tf_stirtime.setText("10");
		ph_tf_maxvol.setText("100");
		ph_tf_blankvol.setText("0");
		ph_tf_burette.setText("1");
		ph_tf_endpoint.setText("10.0");
		ph_cb_dosagerate.setSelectedItem("0.5");
		ph_cb_formula.setSelectedItem("2");
		ph_cb_nooftrials.setSelectedItem("1");
		ph_tf_factor1.setText("1");
		ph_tf_factor2.setText("1");
		ph_tf_factor3.setText("1");
		ph_tf_factor4.setText("1");
		ph_cb_tendency.setSelectedItem("Up");
		ph_cb_resultunit.setSelectedItem("%");
		ph_cb_calibrate.setSelectedItem("3 Point CUST");
		ph_tf_slope1.setText("1");
		ph_tf_slope2.setText("1");
		ph_tf_sop_value.setText("Not Selected");

		amp_tf_delay.setText("0");
		amp_tf_predose.setText("0");
		amp_tf_stirtime.setText("10");
		amp_tf_maxvol.setText("100");
		amp_tf_blankvol.setText("0");
		amp_tf_burette.setText("1");
		amp_tf_endpoint.setText("10.0");
		amp_cb_dosagerate.setSelectedItem("0.5");
		amp_cb_formula.setSelectedItem("2");
		amp_cb_nooftrials.setSelectedItem("1");
		amp_tf_factor1.setText("1");
		amp_tf_factor2.setText("1");
		amp_tf_factor3.setText("1");
		amp_tf_factor4.setText("1");
		amp_cb_filter.setSelectedItem("1");
		amp_cb_resultunit.setSelectedItem("%");
		amp_tf_sop_value.setText("Not Selected");

		kf_tf_delay.setText("0");
		kf_tf_stirtime.setText("0");
		kf_tf_maxvol.setText("100");
		kf_tf_blankvol.setText("0");
		kf_tf_burette.setText("1");
		kf_tf_density.setText("1");
		kf_tf_factor.setText("1");
		kf_tf_endpoint.setText("30");
		kf_cb_dosagerate.setSelectedItem("0.5");
		kf_cb_resultunit.setSelectedItem("%");
		kf_cb_nooftrials.setSelectedItem("1");
		kf_tf_sop_value.setText("Not Selected");
	}

	public static void update_data(String data_got) {
		saved_file = false;
		selected_methodfile = null;
		res = "";
		selected_methodfile = data_got;
		metd_header.setText("Method Name : " + selected_methodfile);

		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String[] data_arr = null;

		String sql;
		res = "";
		try {
			if (selected_experiment.matches("potentiometry")) {
				sql = "SELECT Value FROM pot_method where (Trial_name  = '" + data_got + "')";
			} else if (selected_experiment.matches("phstat")) {
				sql = "SELECT Value FROM ph_method where (Trial_name  = '" + data_got + "')";
			} else if (selected_experiment.matches("amperometry")) {
				sql = "SELECT Value FROM amp_method where (Trial_name  = '" + data_got + "')";
			} else {
				sql = "SELECT Value FROM kf_method where (Trial_name  = '" + data_got + "')";
			}
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			// ////system.out.println("got got got" + rs.getString("Value"));
			res = rs.getString("Value");
			data_arr = res.split(",");
			if (selected_experiment.matches("potentiometry")) {
				// //system.out.println("inside if temp = " + res);

				pot_tf_predose.setText(data_arr[0]);
				pot_tf_stirtime.setText(data_arr[1]);
				pot_tf_maxvol.setText(data_arr[2]);
				pot_tf_blankvol.setText(data_arr[3]);
				pot_tf_burette.setText(data_arr[4]);
				pot_cb_threshold.setSelectedItem(data_arr[5]);
				pot_cb_filter.setSelectedItem(data_arr[6]);
				pot_cb_dosagerate.setSelectedItem(data_arr[7]);
				pot_cb_nooftrials.setSelectedItem(data_arr[8]);
				pot_tf_factor1.setText(data_arr[9]);
				pot_tf_factor2.setText(data_arr[10]);
				pot_tf_factor3.setText(data_arr[11]);
				pot_tf_factor4.setText(data_arr[12]);
				pot_cb_epselect.setSelectedItem(data_arr[13]);
				// pot_cb_formula.setSelectedItem(data_arr[14]);
				//pot_cb_tendency.setSelectedItem(data_arr[15]);
				pot_cb_resultunit.setSelectedItem(data_arr[16]);
				pot_tf_sop_value.setText(data_arr[17]);

			} else if (selected_experiment.matches("phstat")) {
				ph_tf_stirtime.setText(data_arr[0]);
				ph_tf_delay.setText(data_arr[1]);
				ph_tf_predose.setText(data_arr[2]);
				ph_tf_maxvol.setText(data_arr[3]);
				ph_tf_blankvol.setText(data_arr[4]);
				ph_tf_burette.setText(data_arr[5]);
				ph_tf_endpoint.setText(data_arr[6]);
				ph_cb_dosagerate.setSelectedItem(data_arr[7]);
				ph_cb_formula.setSelectedItem(data_arr[8]);
				ph_cb_nooftrials.setSelectedItem(data_arr[9]);
				ph_tf_factor1.setText(data_arr[10]);
				ph_tf_factor2.setText(data_arr[11]);
				ph_tf_factor3.setText(data_arr[12]);
				ph_tf_factor4.setText(data_arr[13]);
				ph_cb_tendency.setSelectedItem(data_arr[14]);
				ph_cb_resultunit.setSelectedItem(data_arr[15]);
				ph_cb_calibrate.setSelectedItem(data_arr[16]);
				ph_tf_slope1.setText(data_arr[17]);
				ph_tf_slope2.setText(data_arr[18]);
				ph_tf_sop_value.setText(data_arr[19]);

			} else if (selected_experiment.matches("amperometry")) {
				amp_tf_stirtime.setText(data_arr[0]);
				amp_tf_delay.setText(data_arr[1]);
				amp_tf_predose.setText(data_arr[2]);
				amp_tf_maxvol.setText(data_arr[3]);
				amp_tf_blankvol.setText(data_arr[4]);
				amp_tf_burette.setText(data_arr[5]);
				amp_tf_endpoint.setText(data_arr[6]);
				amp_cb_dosagerate.setSelectedItem(data_arr[7]);
				amp_cb_formula.setSelectedItem(data_arr[8]);
				amp_cb_nooftrials.setSelectedItem(data_arr[9]);
				amp_tf_factor1.setText(data_arr[10]);
				amp_tf_factor2.setText(data_arr[11]);
				amp_tf_factor3.setText(data_arr[12]);
				amp_tf_factor4.setText(data_arr[13]);
				amp_cb_filter.setSelectedItem(data_arr[14]);
				amp_cb_resultunit.setSelectedItem(data_arr[15]);
				amp_tf_sop_value.setText(data_arr[16]);
			}

			else if (selected_experiment.matches("karl")) {
				kf_tf_delay.setText(data_arr[0]);
				kf_tf_stirtime.setText(data_arr[1]);
				kf_tf_maxvol.setText(data_arr[2]);
				kf_tf_blankvol.setText(data_arr[3]);
				kf_tf_burette.setText(data_arr[4]);
				kf_tf_density.setText(data_arr[5]);
				kf_tf_factor.setText(data_arr[6]);
				kf_tf_endpoint.setText(data_arr[7]);
				kf_cb_dosagerate.setSelectedItem(data_arr[8]);
				kf_cb_resultunit.setSelectedItem(data_arr[9]);
				kf_cb_nooftrials.setSelectedItem(data_arr[10]);
				kf_tf_sop_value.setText(data_arr[11]);

			}

		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, e1);
		} finally {
			try {
				ps.close();
				con.close();
				if (selected_experiment.matches("potentiometry")) {
					pot_cb_formula.setSelectedItem(data_arr[14]);
				}
			} catch (SQLException e1) {
				// system.out.println(e1.toString());
			}
		}
	}

	public static void open_port(SerialPort selected_port) {
		serial_port1 = selected_port;

		selected_port.openPort();
		selected_port.setBaudRate(38400);

		MyComPortListener listenerObject = new MyComPortListener();
		selected_port.addDataListener(listenerObject);
		output = new PrintWriter(selected_port.getOutputStream());
		// system.out.println("mensaje consola2 : " + selected_port);
		output.print("<8888>STRT*");
		output.flush();

		frame1.setTitle("Mayura Analytical             Logged in as - " + user_name
				+ "               Conected to ComPort : " + selected_port.getDescriptivePortName());

		menu_item_comport.setEnabled(false);
		ReformatBuffer.current_state = "mb_start";

		// //system.out.println(" portport =
		// "+serial_port1.getDescriptivePortName().toString());
	}

	public static void comport_success() {

		JOptionPane.showMessageDialog(null, "ComPort Connected Succesfully!");

		try {
			ReformatBuffer.current_exp = "main";
			output.print("<8888>CVOL*");
			output.flush();
		} catch (NullPointerException ee) {
			// system.out.println(" portport_clickclick = ");
		}
	}

	private menubar() {
		setLayout(null);

		File directoryPath = new File("C:\\");
		String contents[] = directoryPath.list();
		List<String> folders = Arrays.asList(contents);

		if (!folders.contains("SQLite")) {
			new File("C:\\SQLite").mkdir();
			new File("C:\\SQLite\\Report").mkdir();
			new File("C:\\SQLite\\chart").mkdir();
			new File("C:\\SQLite\\SOP").mkdir();
			new File("C:\\SQLite\\logo").mkdir();
			new File("C:\\SQLite\\company_logo").mkdir();
			new File("C:\\SQLite\\audit_log_report").mkdir();

			String separator = "\\";

			String[] str_arr = System.getProperty("user.dir").replaceAll(Pattern.quote(separator), "\\\\")
					.split("\\\\");
			String path_final = "";
			for (int i = 0; i < (str_arr.length - 1); i++) {
				if (i == (str_arr.length - 2))
					path_final = path_final + str_arr[i];
				else
					path_final = path_final + str_arr[i] + "\\\\";
			}

			// system.out.println(System.getProperty("user.dir"));
			// system.out.println("pathh : " + path_final);
			File source = new File(path_final + "\\sqldiff.exe");
			File dest = new File("C:\\SQLite\\sqldiff.exe");
			File source2 = new File(path_final + "\\sqlite3.def");
			File dest2 = new File("C:\\SQLite\\sqlite3.def");
			File source3 = new File(path_final + "\\sqlite3.dll");
			File dest3 = new File("C:\\SQLite\\sqlite3.dll");
			File source4 = new File(path_final + "\\sqlite3.exe");
			File dest4 = new File("C:\\SQLite\\sqlite3.exe");
			File source5 = new File(path_final + "\\sqlite3_analyzer.exe");
			File dest5 = new File("C:\\SQLite\\sqlite3_analyzer.exe");
			File source6 = new File(path_final + "\\logo.png");
			File dest6 = new File("C:\\SQLite\\logo\\logo.png");

			try {
				Files.copy(source.toPath(), dest.toPath());
				Files.copy(source2.toPath(), dest2.toPath());
				Files.copy(source3.toPath(), dest3.toPath());
				Files.copy(source4.toPath(), dest4.toPath());
				Files.copy(source5.toPath(), dest5.toPath());
				Files.copy(source6.toPath(), dest6.toPath());

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		formula_cnt = get_formula_cnt();
		panel_pot1 = new JPanel();
		panel_pot2 = new JPanel();
		panel_pot3 = new JPanel();
		panel_pot4 = new JPanel();
		panel_pot5 = new JPanel();

		panel_kf1 = new JPanel();
		panel_kf2 = new JPanel();
		panel_kf3 = new JPanel();
		panel_kf4 = new JPanel();
		panel_kf5 = new JPanel();

		panel_ph1 = new JPanel();
		panel_ph2 = new JPanel();
		panel_ph3 = new JPanel();
		panel_ph4 = new JPanel();
		panel_ph5 = new JPanel();

		panel_amp1 = new JPanel();
		panel_amp2 = new JPanel();
		panel_amp3 = new JPanel();
		panel_amp4 = new JPanel();
		panel_amp5 = new JPanel();

		Border blackline = BorderFactory.createLineBorder(Color.black);

		panel_pot1.setBounds(47, 184, 150, 350);
		panel_pot1.setLayout(new BoxLayout(panel_pot1, BoxLayout.Y_AXIS));
		add(panel_pot1);

		panel_pot2.setBounds(200, 184, 130, 350);
		panel_pot2.setLayout(new BoxLayout(panel_pot2, BoxLayout.Y_AXIS));
		add(panel_pot2);

		panel_pot3.setBounds(335, 184, 75, 350);
		panel_pot3.setLayout(new BoxLayout(panel_pot3, BoxLayout.Y_AXIS));
		add(panel_pot3);

		panel_pot4.setBounds(440, 184, 150, 350);
		panel_pot4.setLayout(new BoxLayout(panel_pot4, BoxLayout.Y_AXIS));
		add(panel_pot4);

		panel_pot5.setBounds(593, 184, 130, 343);
		panel_pot5.setLayout(new BoxLayout(panel_pot5, BoxLayout.Y_AXIS));
		add(panel_pot5);

		panel_kf1.setBounds(47, 184, 150, 350);
		panel_kf1.setLayout(new BoxLayout(panel_kf1, BoxLayout.Y_AXIS));
		add(panel_kf1);
		panel_kf1.setVisible(false);

		panel_kf2.setBounds(200, 175, 130, 320);
		panel_kf2.setLayout(new BoxLayout(panel_kf2, BoxLayout.Y_AXIS));
		add(panel_kf2);
		panel_kf2.setVisible(false);

		panel_kf3.setBounds(335, 184, 75, 350);
		panel_kf3.setLayout(new BoxLayout(panel_kf3, BoxLayout.Y_AXIS));
		add(panel_kf3);
		panel_kf3.setVisible(false);

		panel_kf4.setBounds(440, 184, 150, 335);
		panel_kf4.setLayout(new BoxLayout(panel_kf4, BoxLayout.Y_AXIS));
		add(panel_kf4);
		panel_kf4.setVisible(false);

		panel_kf5.setBounds(593, 184, 130, 145);
		panel_kf5.setLayout(new BoxLayout(panel_kf5, BoxLayout.Y_AXIS));
		add(panel_kf5);
		panel_kf5.setVisible(false);

		panel_ph1.setBounds(47, 184, 150, 375);
		panel_ph1.setLayout(new BoxLayout(panel_ph1, BoxLayout.Y_AXIS));
		add(panel_ph1);
		panel_ph1.setVisible(false);

		panel_ph2.setBounds(200, 184, 130, 375);
		panel_ph2.setLayout(new BoxLayout(panel_ph2, BoxLayout.Y_AXIS));
		add(panel_ph2);
		panel_ph2.setVisible(false);

		panel_ph3.setBounds(335, 184, 75, 350);
		panel_ph3.setLayout(new BoxLayout(panel_ph3, BoxLayout.Y_AXIS));
		add(panel_ph3);
		panel_ph3.setVisible(false);

		panel_ph4.setBounds(440, 184, 150, 375);
		panel_ph4.setLayout(new BoxLayout(panel_ph4, BoxLayout.Y_AXIS));
		add(panel_ph4);
		panel_ph4.setVisible(false);

		panel_ph5.setBounds(593, 184, 130, 390);
		panel_ph5.setLayout(new BoxLayout(panel_ph5, BoxLayout.Y_AXIS));
		add(panel_ph5);
		panel_ph5.setVisible(false);

		panel_amp1.setBounds(47, 184, 150, 375);
		panel_amp1.setLayout(new BoxLayout(panel_amp1, BoxLayout.Y_AXIS));
		add(panel_amp1);
		panel_amp1.setVisible(false);

		panel_amp2.setBounds(200, 184, 130, 375);
		panel_amp2.setLayout(new BoxLayout(panel_amp2, BoxLayout.Y_AXIS));
		add(panel_amp2);
		panel_amp2.setVisible(false);

		panel_amp3.setBounds(335, 184, 75, 350);
		panel_amp3.setLayout(new BoxLayout(panel_amp3, BoxLayout.Y_AXIS));
		add(panel_amp3);
		panel_amp3.setVisible(false);

		panel_amp4.setBounds(440, 184, 150, 365);
		panel_amp4.setLayout(new BoxLayout(panel_amp4, BoxLayout.Y_AXIS));
		add(panel_amp4);
		panel_amp4.setVisible(false);

		panel_amp5.setBounds(593, 184, 130, 275);
		panel_amp5.setLayout(new BoxLayout(panel_amp5, BoxLayout.Y_AXIS));
		add(panel_amp5);
		panel_amp5.setVisible(false);

		pot_predose = new JLabel("Pre Dose :");
		pot_predose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_stirtime = new JLabel("Stir Time :");
		pot_stirtime.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_maxvol = new JLabel("Max Vol :");
		pot_maxvol.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_blankvol = new JLabel("Blank Vol :");
		pot_blankvol.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_burette = new JLabel("Burette Factor :");
		pot_burette.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_threshold = new JLabel("Threshold :");
		pot_threshold.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_filter = new JLabel("Filter :");
		pot_filter.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_dosagerate = new JLabel("Dosage Rate :");
		pot_dosagerate.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_no_of_trials = new JLabel("No of Trials :");
		pot_no_of_trials.setFont(new Font("Times New Roman", Font.BOLD, 20));

		pot_tf_predose = new JTextField();
		pot_tf_predose.setText("0");
		pot_tf_predose.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!pot_tf_predose.getText().toString().matches("")) {
					try {
						if (Float.parseFloat(pot_tf_predose.getText().toString()) < 0
								|| Float.parseFloat(pot_tf_predose.getText().toString()) > 100) {
							JOptionPane.showMessageDialog(null, "Pre Dose must be between 0 to 100!");
							pot_tf_predose.setText("0");
						}
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(null, "Pre Dose must be between 0 to 100!");
						pot_tf_predose.setText("0");
					}
				}

			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		pot_tf_stirtime = new JTextField();
		pot_tf_stirtime.setText("0");
		pot_tf_stirtime.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!pot_tf_predose.getText().toString().matches("")) {
					try {
						if (Integer.parseInt(pot_tf_stirtime.getText().toString()) < 0
								|| Integer.parseInt(pot_tf_stirtime.getText().toString()) > 100) {
							JOptionPane.showMessageDialog(null, "Stir Time must be between 0 to 100!");
							pot_tf_stirtime.setText("0");
						}
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(null, "Stir Time must be between 0 to 100!");
						pot_tf_stirtime.setText("0");
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		pot_tf_maxvol = new JTextField();
		pot_tf_maxvol.setText("100");
		pot_tf_maxvol.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!pot_tf_maxvol.getText().toString().matches("")) {
					try {
						if (Float.parseFloat(pot_tf_maxvol.getText().toString()) < 0
								|| Float.parseFloat(pot_tf_maxvol.getText().toString()) > 200) {
							JOptionPane.showMessageDialog(null, "Stir Time must be between 0 to 100!");
							pot_tf_maxvol.setText("100");
						}
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(null, "Stir Time must be between 0 to 100!");
						pot_tf_maxvol.setText("100");
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		pot_tf_blankvol = new JTextField();
		pot_tf_blankvol.setText("0");
		pot_tf_burette = new JTextField();
		pot_tf_burette.setText("1");

		String[] pot_threshold_arr = new String[1000];
		for (int i = 0; i < 1000; i++) {
			pot_threshold_arr[i] = String.valueOf(i + 1);
		}
		pot_cb_threshold = new JComboBox(pot_threshold_arr);

		pot_cb_threshold.setSelectedItem("100");

		String[] pot_filter_arr = new String[20];
		for (int i = 0; i < 20; i++) {
			pot_filter_arr[i] = String.valueOf(i + 1);
		}
		pot_cb_filter = new JComboBox(pot_filter_arr);

		String[] pot_dosage_arr = { "0.5", "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "8.0", "10.0", "12.0", "14.0",
				"16.0" };
		pot_cb_dosagerate = new JComboBox(pot_dosage_arr);
		pot_cb_dosagerate.setSelectedItem("4.0");

		String[] pot_trials_arr = new String[5];
		for (int i = 0; i < 5; i++) {
			pot_trials_arr[i] = String.valueOf(i + 1);
		}
		pot_cb_nooftrials = new JComboBox(pot_trials_arr);

		pot_units_predose = new JLabel("mL");
		pot_units_predose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_units_stirtime = new JLabel("Sec");
		pot_units_stirtime.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_units_maxvol = new JLabel("mL");
		pot_units_maxvol.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_units_blankvol = new JLabel("mL");
		pot_units_blankvol.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_units_dosagerate = new JLabel("mL/min");
		pot_units_dosagerate.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_units_threshold = new JLabel("mV/mL");
		pot_units_threshold.setFont(new Font("Times New Roman", Font.BOLD, 20));

		pot_f1 = new JLabel("Factor 1 :");
		pot_f1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_f2 = new JLabel("Factor 2 :");
		pot_f2.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_f3 = new JLabel("Factor 3 :");
		pot_f3.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_f4 = new JLabel("Factor 4 :");
		pot_f4.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_epselect = new JLabel("EP Select :");
		pot_epselect.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_formulaNo = new JLabel("Formula No :");
		pot_formulaNo.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_tendency = new JLabel("Tendency :");
		pot_tendency.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_resultunit = new JLabel("Result Unit :");
		pot_resultunit.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pot_sop = new JLabel("SOP :");
		pot_sop.setFont(new Font("Times New Roman", Font.BOLD, 20));

		pot_tf_factor1 = new JTextField();
		pot_tf_factor1.setText("1");
		pot_tf_factor2 = new JTextField();
		pot_tf_factor2.setText("1");
		pot_tf_factor3 = new JTextField();
		pot_tf_factor3.setText("1");
		pot_tf_factor4 = new JTextField();
		pot_tf_factor4.setText("1");
		pot_tf_sop_value = new JTextField();
		pot_tf_sop_value.setText("Not Selected");
		pot_tf_sop_value.setEditable(false);
		pot_tf_sop_value.setHorizontalAlignment(SwingConstants.LEFT);

		String[] pot_epselect_arr = { "Auto", "Manual", "Autostat" };
		pot_cb_epselect = new JComboBox(pot_epselect_arr);

		String[] pot_formula_arr = new String[12];
		for (int i = 0; i < formula_cnt; i++) {
			pot_formula_arr[i] = String.valueOf(i + 1);
		}
		pot_cb_formula = new JComboBox(pot_formula_arr);

		pot_cb_formula.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				set_formula(pot_cb_formula.getSelectedItem().toString());
			}
		});

		String[] pot_tendency_arr = { "Up", "Down" };
		pot_cb_tendency = new JComboBox(pot_tendency_arr);

		String[] pot_resultunit_arr = { "%", "PPM", "Normality" };// "mL", "mg/gm",
		pot_cb_resultunit = new JComboBox(pot_resultunit_arr);

		panel_pot1.add(pot_predose);
		panel_pot1.add(Box.createVerticalStrut(20));
		panel_pot1.add(pot_stirtime);
		panel_pot1.add(Box.createVerticalStrut(20));
		panel_pot1.add(pot_maxvol);
		panel_pot1.add(Box.createVerticalStrut(20));
		panel_pot1.add(pot_blankvol);
		panel_pot1.add(Box.createVerticalStrut(15));
		panel_pot1.add(pot_burette);
		panel_pot1.add(Box.createVerticalStrut(15));
		panel_pot1.add(pot_threshold);
		panel_pot1.add(Box.createVerticalStrut(12));
		panel_pot1.add(pot_filter);
		panel_pot1.add(Box.createVerticalStrut(12));
		panel_pot1.add(pot_dosagerate);
		panel_pot1.add(Box.createVerticalStrut(10));
		panel_pot1.add(pot_no_of_trials);
		panel_pot1.add(Box.createVerticalStrut(7));

		panel_pot2.add(pot_tf_predose);
		panel_pot2.add(Box.createVerticalStrut(10));
		panel_pot2.add(pot_tf_stirtime);
		panel_pot2.add(Box.createVerticalStrut(10));
		panel_pot2.add(pot_tf_maxvol);
		panel_pot2.add(Box.createVerticalStrut(10));
		panel_pot2.add(pot_tf_blankvol);
		panel_pot2.add(Box.createVerticalStrut(10));
		panel_pot2.add(pot_tf_burette);
		panel_pot2.add(Box.createVerticalStrut(10));
		panel_pot2.add(pot_cb_threshold);
		panel_pot2.add(Box.createVerticalStrut(18));
		panel_pot2.add(pot_cb_filter);
		panel_pot2.add(Box.createVerticalStrut(18));
		panel_pot2.add(pot_cb_dosagerate);
		panel_pot2.add(Box.createVerticalStrut(18));
		panel_pot2.add(pot_cb_nooftrials);
		panel_pot2.add(Box.createVerticalStrut(5));

		panel_pot3.add(pot_units_predose);
		panel_pot3.add(Box.createVerticalStrut(20));
		panel_pot3.add(pot_units_stirtime);
		panel_pot3.add(Box.createVerticalStrut(15));
		panel_pot3.add(pot_units_maxvol);
		panel_pot3.add(Box.createVerticalStrut(20));
		panel_pot3.add(pot_units_blankvol);
		panel_pot3.add(Box.createVerticalStrut(55));
		panel_pot3.add(pot_units_threshold);
		panel_pot3.add(Box.createVerticalStrut(52));
		panel_pot3.add(pot_units_dosagerate);

		panel_pot4.add(pot_f1);
		panel_pot4.add(Box.createVerticalStrut(15));
		panel_pot4.add(pot_f2);
		panel_pot4.add(Box.createVerticalStrut(15));
		panel_pot4.add(pot_f3);
		panel_pot4.add(Box.createVerticalStrut(15));
		panel_pot4.add(pot_f4);
		panel_pot4.add(Box.createVerticalStrut(15));
		panel_pot4.add(pot_epselect);
		panel_pot4.add(Box.createVerticalStrut(15));
		panel_pot4.add(pot_formulaNo);
		panel_pot4.add(Box.createVerticalStrut(15));
		panel_pot4.add(pot_tendency);
		panel_pot4.add(Box.createVerticalStrut(15));
		panel_pot4.add(pot_resultunit);
		panel_pot4.add(Box.createVerticalStrut(15));
		panel_pot4.add(pot_sop);

		panel_pot5.add(pot_tf_factor1);
		panel_pot5.add(Box.createVerticalStrut(8));
		panel_pot5.add(pot_tf_factor2);
		panel_pot5.add(Box.createVerticalStrut(8));
		panel_pot5.add(pot_tf_factor3);
		panel_pot5.add(Box.createVerticalStrut(8));
		panel_pot5.add(pot_tf_factor4);
		panel_pot5.add(Box.createVerticalStrut(8));
		panel_pot5.add(pot_cb_epselect);
		panel_pot5.add(Box.createVerticalStrut(15));
		panel_pot5.add(pot_cb_formula);
		panel_pot5.add(Box.createVerticalStrut(15));
		panel_pot5.add(pot_cb_tendency);
		panel_pot5.add(Box.createVerticalStrut(15));
		panel_pot5.add(pot_cb_resultunit);
		panel_pot5.add(Box.createVerticalStrut(10));
		panel_pot5.add(pot_tf_sop_value);

		kf_delay = new JLabel("Delay :");
		kf_delay.setFont(new Font("Times New Roman", Font.BOLD, 20));
		kf_stirtime = new JLabel("Stir Time :");
		kf_stirtime.setFont(new Font("Times New Roman", Font.BOLD, 20));
		kf_maxvol = new JLabel("Max Vol :");
		kf_maxvol.setFont(new Font("Times New Roman", Font.BOLD, 20));
		kf_blankvol = new JLabel("Blank Vol :");
		kf_blankvol.setFont(new Font("Times New Roman", Font.BOLD, 20));
		kf_burette = new JLabel("Burette Factor :");
		kf_burette.setFont(new Font("Times New Roman", Font.BOLD, 20));
		kf_density = new JLabel("Density :");
		kf_density.setFont(new Font("Times New Roman", Font.BOLD, 20));
		kf_factor = new JLabel("KF Factor :");
		kf_factor.setFont(new Font("Times New Roman", Font.BOLD, 20));
		kf_dosagerate = new JLabel("Dosage Rate :");
		kf_dosagerate.setFont(new Font("Times New Roman", Font.BOLD, 20));
		kf_no_of_trials = new JLabel("No of Trials :");
		kf_no_of_trials.setFont(new Font("Times New Roman", Font.BOLD, 20));
		kf_endpoint = new JLabel("End Point :");
		kf_endpoint.setFont(new Font("Times New Roman", Font.BOLD, 20));
		kf_resultunit = new JLabel("Result Unit :");
		kf_resultunit.setFont(new Font("Times New Roman", Font.BOLD, 20));
		kf_sop = new JLabel("SOP :");
		kf_sop.setFont(new Font("Times New Roman", Font.BOLD, 20));

		kf_tf_delay = new JTextField();
		kf_tf_delay.setText("15");
		kf_tf_delay.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!kf_tf_delay.getText().toString().matches("")) {
					try {
						if (Integer.parseInt(kf_tf_delay.getText().toString()) < 0
								|| Integer.parseInt(kf_tf_delay.getText().toString()) > 50) {
							JOptionPane.showMessageDialog(null, "Delay Time must be between 0 to 50!");
							kf_tf_delay.setText("50");
						}
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(null, "Delay Time must be between 0 to 50!");
						kf_tf_delay.setText("50");
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		kf_tf_stirtime = new JTextField();
		kf_tf_stirtime.setText("10");
		kf_tf_stirtime.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!kf_tf_stirtime.getText().toString().matches("")) {
					try {
						if (Integer.parseInt(kf_tf_stirtime.getText().toString()) < 0
								|| Integer.parseInt(kf_tf_stirtime.getText().toString()) > 500) {
							JOptionPane.showMessageDialog(null, "Stir Time must be between 0 to 500!");
							kf_tf_stirtime.setText("50");
						}
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(null, "Stir Time must be between 0 to 500!");
						kf_tf_stirtime.setText("50");
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		kf_tf_maxvol = new JTextField();
		kf_tf_maxvol.setText("100");
		kf_tf_maxvol.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!kf_tf_maxvol.getText().toString().matches("")) {
					try {
						if (Float.parseFloat(kf_tf_maxvol.getText().toString()) < 0
								|| Float.parseFloat(kf_tf_maxvol.getText().toString()) > 200) {
							JOptionPane.showMessageDialog(null, "Max Volume must be between 0 to 200!");
							kf_tf_maxvol.setText("50");
						}
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(null, "Max Volume must be between 0 to 500!");
						kf_tf_maxvol.setText("50");
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		kf_tf_blankvol = new JTextField();
		kf_tf_blankvol.setText("0");
		kf_tf_blankvol.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!kf_tf_blankvol.getText().toString().matches("")) {
					try {
						if (Float.parseFloat(kf_tf_blankvol.getText().toString()) < 0
								|| Float.parseFloat(kf_tf_blankvol.getText().toString()) > 200) {
							JOptionPane.showMessageDialog(null, "Blank Volume must be between 0 to 200!");
							kf_tf_blankvol.setText("50");
						}
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(null, "Blank Volume must be between 0 to 500!");
						kf_tf_blankvol.setText("50");
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		kf_tf_burette = new JTextField();
		kf_tf_burette.setText("1");
		kf_tf_density = new JTextField();
		kf_tf_density.setText("1");
		kf_tf_factor = new JTextField();
		kf_tf_factor.setText("5");
		kf_tf_endpoint = new JTextField();
		kf_tf_endpoint.setText("30");
		kf_tf_sop_value = new JTextField();
		kf_tf_sop_value.setText("Not Selected");
		kf_tf_sop_value.setEditable(false);

		String[] kf_dosage_arr = { "0.5", "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "8.0", "10.0", "12.0", "14.0",
				"16.0" };
		kf_cb_dosagerate = new JComboBox(kf_dosage_arr);
		kf_cb_dosagerate.setSelectedItem("4.0");
		String[] kf_trials_arr = new String[5];
		for (int i = 0; i < 5; i++) {
			kf_trials_arr[i] = String.valueOf(i + 1);
		}
		kf_cb_nooftrials = new JComboBox(kf_trials_arr);

		String[] kf_resultunit_arr = { "%", "PPM" };
		kf_cb_resultunit = new JComboBox(kf_resultunit_arr);

		kf_units_stir = new JLabel("Sec");
		kf_units_stir.setFont(new Font("Times New Roman", Font.BOLD, 20));
		kf_units_delay = new JLabel("Sec");
		kf_units_delay.setFont(new Font("Times New Roman", Font.BOLD, 20));
		kf_units_maxvol = new JLabel("mL");
		kf_units_maxvol.setFont(new Font("Times New Roman", Font.BOLD, 20));
		kf_units_blankvol = new JLabel("mL");
		kf_units_blankvol.setFont(new Font("Times New Roman", Font.BOLD, 20));
		kf_units_dosagerate = new JLabel("mL/min");
		kf_units_dosagerate.setFont(new Font("Times New Roman", Font.BOLD, 20));
		kf_units_endpoint = new JLabel("mV");
		kf_units_endpoint.setFont(new Font("Times New Roman", Font.BOLD, 20));

		panel_kf1.add(kf_delay);
		panel_kf1.add(Box.createVerticalStrut(15));
		panel_kf1.add(kf_stirtime);
		panel_kf1.add(Box.createVerticalStrut(15));
		panel_kf1.add(kf_maxvol);
		panel_kf1.add(Box.createVerticalStrut(15));
		panel_kf1.add(kf_blankvol);
		panel_kf1.add(Box.createVerticalStrut(15));
		panel_kf1.add(kf_burette);
		panel_kf1.add(Box.createVerticalStrut(15));
		panel_kf1.add(kf_density);
		panel_kf1.add(Box.createVerticalStrut(15));
		panel_kf1.add(kf_factor);
		panel_kf1.add(Box.createVerticalStrut(15));
		panel_kf1.add(kf_endpoint);

		panel_kf2.add(kf_tf_delay);
		panel_kf2.add(Box.createVerticalStrut(5));
		panel_kf2.add(kf_tf_stirtime);
		panel_kf2.add(Box.createVerticalStrut(5));
		panel_kf2.add(kf_tf_maxvol);
		panel_kf2.add(Box.createVerticalStrut(5));
		panel_kf2.add(kf_tf_blankvol);
		panel_kf2.add(Box.createVerticalStrut(5));
		panel_kf2.add(kf_tf_burette);
		panel_kf2.add(Box.createVerticalStrut(5));
		panel_kf2.add(kf_tf_density);
		panel_kf2.add(Box.createVerticalStrut(5));
		panel_kf2.add(kf_tf_factor);
		panel_kf2.add(Box.createVerticalStrut(5));
		panel_kf2.add(kf_tf_endpoint);

		panel_kf3.add(kf_units_delay);
		panel_kf3.add(Box.createVerticalStrut(15));
		panel_kf3.add(kf_units_stir);
		panel_kf3.add(Box.createVerticalStrut(15));
		panel_kf3.add(kf_units_maxvol);
		panel_kf3.add(Box.createVerticalStrut(15));
		panel_kf3.add(kf_units_blankvol);
		panel_kf3.add(Box.createVerticalStrut(132));
		panel_kf3.add(kf_units_endpoint);

		panel_kf4.add(kf_dosagerate);
		panel_kf4.add(Box.createVerticalStrut(15));
		panel_kf4.add(kf_resultunit);
		panel_kf4.add(Box.createVerticalStrut(15));
		panel_kf4.add(kf_no_of_trials);
		panel_kf4.add(Box.createVerticalStrut(15));
		panel_kf4.add(kf_sop);

		panel_kf5.add(kf_cb_dosagerate);
		panel_kf5.add(Box.createVerticalStrut(15));
		panel_kf5.add(kf_cb_resultunit);
		panel_kf5.add(Box.createVerticalStrut(15));
		panel_kf5.add(kf_cb_nooftrials);
		panel_kf5.add(Box.createVerticalStrut(15));
		panel_kf5.add(kf_tf_sop_value);

		// PH

		ph_stirtime = new JLabel("Stir Time :");
		ph_stirtime.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_delay = new JLabel("Delay :");
		ph_delay.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_predose = new JLabel("PreDose :");
		ph_predose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_maxvol = new JLabel("Max Vol :");
		ph_maxvol.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_blankvol = new JLabel("Blank Vol :");
		ph_blankvol.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_burette = new JLabel("Burette Factor :");
		ph_burette.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_endpoint = new JLabel("End Point :");
		ph_endpoint.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_dosagerate = new JLabel("Dosage Rate :");
		ph_dosagerate.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_no_of_trials = new JLabel("No of Trials :");
		ph_no_of_trials.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_f1 = new JLabel("Factor 1 :");
		ph_f1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_f2 = new JLabel("Factor 2 :");
		ph_f2.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_f3 = new JLabel("Factor 3 :");
		ph_f3.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_f4 = new JLabel("Factor 4 :");
		ph_f4.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_calibrate = new JLabel("Calibrate :");
		ph_calibrate.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_slope1 = new JLabel("Slope 1 :");
		ph_slope1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_slope2 = new JLabel("Slope 2 :");
		ph_slope2.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_formulaNo = new JLabel("Formula No :");
		ph_formulaNo.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_tendency = new JLabel("Tendency :");
		ph_tendency.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_resultunit = new JLabel("Result Unit :");
		ph_resultunit.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_sop = new JLabel("SOP : ");
		ph_sop.setFont(new Font("Times New Roman", Font.BOLD, 20));

		ph_tf_stirtime = new JTextField();
		ph_tf_stirtime.setText("10");
		ph_tf_delay = new JTextField();
		ph_tf_delay.setText("0");
		ph_tf_predose = new JTextField();
		ph_tf_predose.setText("0");
		ph_tf_maxvol = new JTextField();
		ph_tf_maxvol.setText("100");
		ph_tf_blankvol = new JTextField();
		ph_tf_blankvol.setText("0");
		ph_tf_burette = new JTextField();
		ph_tf_burette.setText("1");
		ph_tf_endpoint = new JTextField();
		ph_tf_endpoint.setText("0.0");
		ph_tf_factor1 = new JTextField();
		ph_tf_factor1.setText("1");
		ph_tf_factor2 = new JTextField();
		ph_tf_factor2.setText("1");
		ph_tf_factor3 = new JTextField();
		ph_tf_factor3.setText("1");
		ph_tf_factor4 = new JTextField();
		ph_tf_factor4.setText("1");
		ph_tf_slope1 = new JTextField();
		ph_tf_slope1.setText("1");
		ph_tf_slope2 = new JTextField();
		ph_tf_slope2.setText("1");
		ph_tf_sop_value = new JTextField();
		ph_tf_sop_value.setText("Not Selected");
		ph_tf_sop_value.setEditable(false);

		String[] ph_tendency_arr = { "Up", "Down" };
		ph_cb_tendency = new JComboBox(ph_tendency_arr);

		String[] ph_dosage_arr = { "0.5", "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "8.0", "10.0", "12.0", "14.0",
				"16.0" };
		ph_cb_dosagerate = new JComboBox(ph_dosage_arr);

		String[] ph_nooftrials_arr = new String[5];
		for (int i = 0; i < 5; i++) {
			ph_nooftrials_arr[i] = String.valueOf(i + 1);
		}
		ph_cb_nooftrials = new JComboBox(ph_nooftrials_arr);

		String[] ph_calibrate_arr = { "3 Point NIST", "5 Point NIST", "3 Point CUST", "5 Point CUST" };
		ph_cb_calibrate = new JComboBox(ph_calibrate_arr);

		String[] ph_resultunit_arr = { "%", "PPM", "mL", "mg/gm", "Normality" };
		ph_cb_resultunit = new JComboBox(ph_resultunit_arr);

		String[] ph_fomula_arr = { "2" };
		ph_cb_formula = new JComboBox(ph_fomula_arr);

		ph_units_stirtime = new JLabel("Sec");
		ph_units_stirtime.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_units_delay = new JLabel("Sec");
		ph_units_delay.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_units_predose = new JLabel("mL");
		ph_units_predose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_units_maxvol = new JLabel("mL");
		ph_units_maxvol.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_units_blankvol = new JLabel("mL");
		ph_units_blankvol.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_units_dosagerate = new JLabel("mL/min");
		ph_units_dosagerate.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ph_units_endpoint = new JLabel("pH");
		ph_units_endpoint.setFont(new Font("Times New Roman", Font.BOLD, 20));

		panel_ph1.add(ph_stirtime);
		panel_ph1.add(Box.createVerticalStrut(15));
		panel_ph1.add(ph_delay);
		panel_ph1.add(Box.createVerticalStrut(15));
		panel_ph1.add(ph_predose);
		panel_ph1.add(Box.createVerticalStrut(15));
		panel_ph1.add(ph_maxvol);
		panel_ph1.add(Box.createVerticalStrut(15));
		panel_ph1.add(ph_blankvol);
		panel_ph1.add(Box.createVerticalStrut(15));
		panel_ph1.add(ph_burette);
		panel_ph1.add(Box.createVerticalStrut(15));
		panel_ph1.add(ph_endpoint);
		panel_ph1.add(Box.createVerticalStrut(15));
		panel_ph1.add(ph_dosagerate);
		panel_ph1.add(Box.createVerticalStrut(15));
		panel_ph1.add(ph_formulaNo);
		panel_ph1.add(Box.createVerticalStrut(15));
		panel_ph1.add(ph_no_of_trials);

		panel_ph2.add(ph_tf_stirtime);
		panel_ph2.add(Box.createVerticalStrut(5));
		panel_ph2.add(ph_tf_delay);
		panel_ph2.add(Box.createVerticalStrut(5));
		panel_ph2.add(ph_tf_predose);
		panel_ph2.add(Box.createVerticalStrut(5));
		panel_ph2.add(ph_tf_maxvol);
		panel_ph2.add(Box.createVerticalStrut(5));
		panel_ph2.add(ph_tf_blankvol);
		panel_ph2.add(Box.createVerticalStrut(5));
		panel_ph2.add(ph_tf_burette);
		panel_ph2.add(Box.createVerticalStrut(5));
		panel_ph2.add(ph_tf_endpoint);
		panel_ph2.add(Box.createVerticalStrut(15));
		panel_ph2.add(ph_cb_dosagerate);
		panel_ph2.add(Box.createVerticalStrut(15));
		panel_ph2.add(ph_cb_formula);
		panel_ph2.add(Box.createVerticalStrut(15));
		panel_ph2.add(ph_cb_nooftrials);

		panel_ph3.add(ph_units_stirtime);
		panel_ph3.add(Box.createVerticalStrut(5));
		panel_ph3.add(ph_units_delay);
		panel_ph3.add(Box.createVerticalStrut(5));
		panel_ph3.add(ph_units_predose);
		panel_ph3.add(Box.createVerticalStrut(5));
		panel_ph3.add(ph_units_maxvol);
		panel_ph3.add(Box.createVerticalStrut(5));
		panel_ph3.add(ph_units_blankvol);
		panel_ph3.add(Box.createVerticalStrut(85));
		panel_ph3.add(ph_units_endpoint);
		panel_ph3.add(Box.createVerticalStrut(18));
		panel_ph3.add(ph_units_dosagerate);

		panel_ph4.add(ph_f1);
		panel_ph4.add(Box.createVerticalStrut(15));
		panel_ph4.add(ph_f2);
		panel_ph4.add(Box.createVerticalStrut(15));
		panel_ph4.add(ph_f3);
		panel_ph4.add(Box.createVerticalStrut(15));
		panel_ph4.add(ph_f4);
		panel_ph4.add(Box.createVerticalStrut(15));
		panel_ph4.add(ph_tendency);
		panel_ph4.add(Box.createVerticalStrut(15));
		panel_ph4.add(ph_resultunit);
		panel_ph4.add(Box.createVerticalStrut(15));
		panel_ph4.add(ph_calibrate);
		panel_ph4.add(Box.createVerticalStrut(15));
		panel_ph4.add(ph_slope1);
		panel_ph4.add(Box.createVerticalStrut(15));
		panel_ph4.add(ph_slope2);
		panel_ph4.add(Box.createVerticalStrut(15));
		panel_ph4.add(ph_sop);

		panel_ph5.add(ph_tf_factor1);
		panel_ph5.add(Box.createVerticalStrut(10));
		panel_ph5.add(ph_tf_factor2);
		panel_ph5.add(Box.createVerticalStrut(10));
		panel_ph5.add(ph_tf_factor3);
		panel_ph5.add(Box.createVerticalStrut(10));
		panel_ph5.add(ph_tf_factor4);
		panel_ph5.add(Box.createVerticalStrut(10));
		panel_ph5.add(ph_cb_tendency);
		panel_ph5.add(Box.createVerticalStrut(10));
		panel_ph5.add(ph_cb_resultunit);
		panel_ph5.add(Box.createVerticalStrut(10));
		panel_ph5.add(ph_cb_calibrate);
		panel_ph5.add(Box.createVerticalStrut(10));
		panel_ph5.add(ph_tf_slope1);
		panel_ph5.add(Box.createVerticalStrut(10));
		panel_ph5.add(ph_tf_slope2);
		panel_ph5.add(Box.createVerticalStrut(10));
		panel_ph5.add(ph_tf_sop_value);
		panel_ph5.add(Box.createVerticalStrut(10));

		amp_stirtime = new JLabel("Stir Time :");
		amp_stirtime.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_delay = new JLabel("Delay :");
		amp_delay.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_predose = new JLabel("PreDose :");
		amp_predose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_maxvol = new JLabel("Max Vol :");
		amp_maxvol.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_blankvol = new JLabel("Blank Vol :");
		amp_blankvol.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_burette = new JLabel("Burette Factor :");
		amp_burette.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_endpoint = new JLabel("End Point :");
		amp_endpoint.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_dosagerate = new JLabel("Dosage Rate :");
		amp_dosagerate.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_no_of_trials = new JLabel("No of Trials :");
		amp_no_of_trials.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_f1 = new JLabel("Factor 1 :");
		amp_f1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_f2 = new JLabel("Factor 2 :");
		amp_f2.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_f3 = new JLabel("Factor 3 :");
		amp_f3.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_f4 = new JLabel("Factor 4 :");
		amp_f4.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_formulaNo = new JLabel("Formula No :");
		amp_formulaNo.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_filter = new JLabel("Filter :");
		amp_filter.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_resultunit = new JLabel("Result Unit :");
		amp_resultunit.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_sop = new JLabel("SOP :");
		amp_sop.setFont(new Font("Times New Roman", Font.BOLD, 20));

		amp_tf_stirtime = new JTextField();
		amp_tf_stirtime.setText("10");
		amp_tf_delay = new JTextField();
		amp_tf_delay.setText("0");
		amp_tf_predose = new JTextField();
		amp_tf_predose.setText("0");
		amp_tf_maxvol = new JTextField();
		amp_tf_maxvol.setText("100");
		amp_tf_blankvol = new JTextField();
		amp_tf_blankvol.setText("0");
		amp_tf_burette = new JTextField();
		amp_tf_burette.setText("1");
		amp_tf_endpoint = new JTextField();
		amp_tf_endpoint.setText("0.0");
		amp_tf_factor1 = new JTextField();
		amp_tf_factor1.setText("1");
		amp_tf_factor2 = new JTextField();
		amp_tf_factor2.setText("1");
		amp_tf_factor3 = new JTextField();
		amp_tf_factor3.setText("1");
		amp_tf_factor4 = new JTextField();
		amp_tf_factor4.setText("1");
		amp_tf_sop_value = new JTextField();
		amp_tf_sop_value.setText("Not Selected");
		amp_tf_sop_value.setEditable(false);

		String[] amp_tendency_arr = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
				"16", "17", "18", "19", "20" };
		amp_cb_filter = new JComboBox(amp_tendency_arr);

		String[] amp_dosage_arr = { "0.5", "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "8.0", "10.0", "12.0", "14.0",
				"16.0" };
		amp_cb_dosagerate = new JComboBox(amp_dosage_arr);

		String[] amp_nooftrials_arr = new String[5];
		for (int i = 0; i < 5; i++) {
			amp_nooftrials_arr[i] = String.valueOf(i + 1);
		}
		amp_cb_nooftrials = new JComboBox(amp_nooftrials_arr);

		String[] amp_resultunit_arr = { "%", "PPM", "mL", "mg/gm", "Normality" };
		amp_cb_resultunit = new JComboBox(amp_resultunit_arr);

		String[] amp_fomula_arr = { "2" };
		amp_cb_formula = new JComboBox(amp_fomula_arr);

		amp_units_stirtime = new JLabel("Sec");
		amp_units_stirtime.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_units_delay = new JLabel("Sec");
		amp_units_delay.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_units_predose = new JLabel("mL");
		amp_units_predose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_units_maxvol = new JLabel("mL");
		amp_units_maxvol.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_units_blankvol = new JLabel("mL");
		amp_units_blankvol.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_units_dosagerate = new JLabel("mL/min");
		amp_units_dosagerate.setFont(new Font("Times New Roman", Font.BOLD, 20));
		amp_units_endpoint = new JLabel("mV");
		amp_units_endpoint.setFont(new Font("Times New Roman", Font.BOLD, 20));

		panel_amp1.add(amp_stirtime);
		panel_amp1.add(Box.createVerticalStrut(15));
		panel_amp1.add(amp_delay);
		panel_amp1.add(Box.createVerticalStrut(15));
		panel_amp1.add(amp_predose);
		panel_amp1.add(Box.createVerticalStrut(15));
		panel_amp1.add(amp_maxvol);
		panel_amp1.add(Box.createVerticalStrut(15));
		panel_amp1.add(amp_blankvol);
		panel_amp1.add(Box.createVerticalStrut(15));
		panel_amp1.add(amp_burette);
		panel_amp1.add(Box.createVerticalStrut(15));
		panel_amp1.add(amp_endpoint);
		panel_amp1.add(Box.createVerticalStrut(15));
		panel_amp1.add(amp_dosagerate);
		panel_amp1.add(Box.createVerticalStrut(15));
		panel_amp1.add(amp_formulaNo);
		panel_amp1.add(Box.createVerticalStrut(15));
		panel_amp1.add(amp_no_of_trials);

		panel_amp2.add(amp_tf_stirtime);
		panel_amp2.add(Box.createVerticalStrut(5));
		panel_amp2.add(amp_tf_delay);
		panel_amp2.add(Box.createVerticalStrut(5));
		panel_amp2.add(amp_tf_predose);
		panel_amp2.add(Box.createVerticalStrut(5));
		panel_amp2.add(amp_tf_maxvol);
		panel_amp2.add(Box.createVerticalStrut(5));
		panel_amp2.add(amp_tf_blankvol);
		panel_amp2.add(Box.createVerticalStrut(5));
		panel_amp2.add(amp_tf_burette);
		panel_amp2.add(Box.createVerticalStrut(5));
		panel_amp2.add(amp_tf_endpoint);
		panel_amp2.add(Box.createVerticalStrut(15));
		panel_amp2.add(amp_cb_dosagerate);
		panel_amp2.add(Box.createVerticalStrut(15));
		panel_amp2.add(amp_cb_formula);
		panel_amp2.add(Box.createVerticalStrut(15));
		panel_amp2.add(amp_cb_nooftrials);

		panel_amp3.add(amp_units_stirtime);
		panel_amp3.add(Box.createVerticalStrut(5));
		panel_amp3.add(amp_units_delay);
		panel_amp3.add(Box.createVerticalStrut(5));
		panel_amp3.add(amp_units_predose);
		panel_amp3.add(Box.createVerticalStrut(5));
		panel_amp3.add(amp_units_maxvol);
		panel_amp3.add(Box.createVerticalStrut(5));
		panel_amp3.add(amp_units_blankvol);
		panel_amp3.add(Box.createVerticalStrut(85));
		panel_amp3.add(amp_units_endpoint);
		panel_amp3.add(Box.createVerticalStrut(18));
		panel_amp3.add(amp_units_dosagerate);

		panel_amp4.add(amp_f1);
		panel_amp4.add(Box.createVerticalStrut(15));
		panel_amp4.add(amp_f2);
		panel_amp4.add(Box.createVerticalStrut(15));
		panel_amp4.add(amp_f3);
		panel_amp4.add(Box.createVerticalStrut(15));
		panel_amp4.add(amp_f4);
		panel_amp4.add(Box.createVerticalStrut(15));
		panel_amp4.add(amp_filter);
		panel_amp4.add(Box.createVerticalStrut(15));
		panel_amp4.add(amp_resultunit);
		panel_amp4.add(Box.createVerticalStrut(15));
		panel_amp4.add(amp_sop);
		panel_amp4.add(Box.createVerticalStrut(15));

		panel_amp5.add(amp_tf_factor1);
		panel_amp5.add(Box.createVerticalStrut(10));
		panel_amp5.add(amp_tf_factor2);
		panel_amp5.add(Box.createVerticalStrut(10));
		panel_amp5.add(amp_tf_factor3);
		panel_amp5.add(Box.createVerticalStrut(10));
		panel_amp5.add(amp_tf_factor4);
		panel_amp5.add(Box.createVerticalStrut(10));
		panel_amp5.add(amp_cb_filter);
		panel_amp5.add(Box.createVerticalStrut(10));
		panel_amp5.add(amp_cb_resultunit);
		panel_amp5.add(Box.createVerticalStrut(10));
		panel_amp5.add(amp_tf_sop_value);
		panel_amp5.add(Box.createVerticalStrut(10));

		btn_open_mb = new JButton("Open");
		btn_open_mb.setBounds(158, 600, 112, 33);
		btn_open_mb.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btn_open_mb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// work here 14091999
				// "potentiometry", "phstat","amperometry","karl",

				if (selected_experiment.matches("potentiometry")) {

					open_method open_method1 = new open_method();
					String[] temp_arr = { "pot" };
					open_method1.main(temp_arr);
				} else if (selected_experiment.matches("phstat")) {
					open_method open_method1 = new open_method();
					String[] temp_arr = { "ph" };
					open_method1.main(temp_arr);
				} else if (selected_experiment.matches("amperometry")) {
					//
					open_method open_method1 = new open_method();
					String[] temp_arr = { "amp" };
					open_method1.main(temp_arr);
				} else if (selected_experiment.matches("karl")) {
					//
					open_method open_method1 = new open_method();
					String[] temp_arr = { "karl" };
					open_method1.main(temp_arr);

				}
			}
		});
		add(btn_open_mb);

		btn_run_mb = new JButton("Run");
		btn_run_mb.setBounds(271, 600, 121, 34);
		btn_run_mb.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btn_run_mb.setBackground(SystemColor.text);
		btn_run_mb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (saved_file == true) {
					//// system.out.println(" Inside If If");
					if (selected_experiment.matches("potentiometry")) {
						String data = pot_tf_predose.getText().toString() + "," + pot_tf_stirtime.getText().toString()
								+ "," + pot_tf_maxvol.getText().toString() + "," + pot_tf_blankvol.getText().toString()
								+ "," + pot_tf_burette.getText().toString() + ","
								+ pot_cb_threshold.getSelectedItem().toString() + ","
								+ pot_cb_filter.getSelectedItem().toString() + ","
								+ pot_cb_dosagerate.getSelectedItem().toString() + ","
								+ pot_cb_nooftrials.getSelectedItem().toString() + ","
								+ pot_tf_factor1.getText().toString() + "," + pot_tf_factor2.getText().toString() + ","
								+ pot_tf_factor3.getText().toString() + "," + pot_tf_factor4.getText().toString() + ","
								+ pot_cb_epselect.getSelectedItem().toString() + ","
								+ pot_cb_formula.getSelectedItem().toString() + ","
								+ pot_cb_tendency.getSelectedItem().toString() + ","
								+ pot_cb_resultunit.getSelectedItem().toString() + ","
								+ pot_tf_sop_value.getText().toString();// www
						popup_input dg = new popup_input();
						String[] temp = { data, saved_file_name, "false", "pot" };
						dg.main(temp);
					} else if (selected_experiment.matches("phstat")) {
						String data = ph_tf_stirtime.getText().toString() + "," + ph_tf_delay.getText().toString() + ","
								+ ph_tf_predose.getText().toString() + "," + ph_tf_maxvol.getText().toString() + ","
								+ ph_tf_blankvol.getText().toString() + "," + ph_tf_burette.getText().toString() + ","
								+ ph_tf_endpoint.getText().toString() + ","
								+ ph_cb_dosagerate.getSelectedItem().toString() + ","
								+ ph_cb_formula.getSelectedItem().toString() + ","
								+ ph_cb_nooftrials.getSelectedItem().toString() + ","
								+ ph_tf_factor1.getText().toString() + "," + ph_tf_factor2.getText().toString() + ","
								+ ph_tf_factor3.getText().toString() + "," + ph_tf_factor4.getText().toString() + ","
								+ ph_cb_tendency.getSelectedItem().toString() + ","
								+ ph_cb_resultunit.getSelectedItem().toString() + ","
								+ ph_cb_calibrate.getSelectedItem().toString() + "," + ph_tf_slope1.getText().toString()
								+ "," + ph_tf_slope2.getText().toString() + "," + ph_tf_sop_value.getText().toString();// www
//						popup_input_ph dg = new popup_input_ph();
//						String[] temp = { data, saved_file_name, "false", "ph" };
//						dg.main(temp);
					} else if (selected_experiment.matches("amperometry")) {
						String data = amp_tf_stirtime.getText().toString() + "," + amp_tf_delay.getText().toString()
								+ "," + amp_tf_predose.getText().toString() + "," + amp_tf_maxvol.getText().toString()
								+ "," + amp_tf_blankvol.getText().toString() + "," + amp_tf_burette.getText().toString()
								+ "," + amp_tf_endpoint.getText().toString() + ","
								+ amp_cb_dosagerate.getSelectedItem().toString() + ","
								+ amp_cb_formula.getSelectedItem().toString() + ","
								+ amp_cb_nooftrials.getSelectedItem().toString() + ","
								+ amp_tf_factor1.getText().toString() + "," + amp_tf_factor2.getText().toString() + ","
								+ amp_tf_factor3.getText().toString() + "," + amp_tf_factor4.getText().toString() + ","
								+ amp_cb_filter.getSelectedItem().toString() + ","
								+ amp_cb_resultunit.getSelectedItem().toString() + ","
								+ amp_tf_sop_value.getText().toString();// www
//						popup_input_amp dg = new popup_input_amp();
//						String[] temp = { data, saved_file_name, "false", "amp" };
//						dg.main(temp);
					}

					else if (selected_experiment.matches("karl")) {
						String data = kf_tf_delay.getText().toString() + "," + kf_tf_stirtime.getText().toString() + ","
								+ kf_tf_maxvol.getText().toString() + "," + kf_tf_blankvol.getText().toString() + ","
								+ kf_tf_burette.getText().toString() + "," + kf_tf_density.getText().toString() + ","
								+ kf_tf_factor.getText().toString() + "," + kf_tf_endpoint.getText().toString() + ","
								+ kf_cb_dosagerate.getSelectedItem().toString() + ","
								+ kf_cb_resultunit.getSelectedItem().toString() + ","
								+ kf_cb_nooftrials.getSelectedItem().toString() + ","
								+ kf_tf_sop_value.getText().toString();// www
						popup_input_kf dg = new popup_input_kf();
						String[] temp = { data, saved_file_name, "false", "kf" };
						dg.main(temp);
					}
				} else {
					// system.out.println("Inside Inside else");

					if (selected_experiment.matches("potentiometry")) {
						String data = pot_tf_predose.getText().toString() + "," + pot_tf_stirtime.getText().toString()
								+ "," + pot_tf_maxvol.getText().toString() + "," + pot_tf_blankvol.getText().toString()
								+ "," + pot_tf_burette.getText().toString() + ","
								+ pot_cb_threshold.getSelectedItem().toString() + ","
								+ pot_cb_filter.getSelectedItem().toString() + ","
								+ pot_cb_dosagerate.getSelectedItem().toString() + ","
								+ pot_cb_nooftrials.getSelectedItem().toString() + ","
								+ pot_tf_factor1.getText().toString() + "," + pot_tf_factor2.getText().toString() + ","
								+ pot_tf_factor3.getText().toString() + "," + pot_tf_factor4.getText().toString() + ","
								+ pot_cb_epselect.getSelectedItem().toString() + ","
								+ pot_cb_formula.getSelectedItem().toString() + ","
								+ pot_cb_tendency.getSelectedItem().toString() + ","
								+ pot_cb_resultunit.getSelectedItem().toString() + ","
								+ pot_tf_sop_value.getText().toString();// www
						if (selected_methodfile != null) {
							if (res.matches(data)) {

								if (no_update == true) {
									popup_input dg = new popup_input();
									String[] temp = { data, selected_methodfile, "false", "pot" };
									dg.main(temp);
								} else {
									popup_input dg = new popup_input();
									String[] temp = { data, selected_methodfile, "true", "pot" };
									dg.main(temp);
								}

							} else {
								save_method save_method1 = new save_method();
								String[] temp_arr = { data, selected_methodfile, "pot" };
								save_method1.main(temp_arr);
								saved = 0;
							}
						} else {
							save_method save_method1 = new save_method();
							String[] temp_arr = { data, "", "pot" };
							save_method1.main(temp_arr);
							saved = 0;
						}
					}

					else if (selected_experiment.matches("phstat")) {
						String data = ph_tf_stirtime.getText().toString() + "," + ph_tf_delay.getText().toString() + ","
								+ ph_tf_predose.getText().toString() + "," + ph_tf_maxvol.getText().toString() + ","
								+ ph_tf_blankvol.getText().toString() + "," + ph_tf_burette.getText().toString() + ","
								+ ph_tf_endpoint.getText().toString() + ","
								+ ph_cb_dosagerate.getSelectedItem().toString() + ","
								+ ph_cb_formula.getSelectedItem().toString() + ","
								+ ph_cb_nooftrials.getSelectedItem().toString() + ","
								+ ph_tf_factor1.getText().toString() + "," + ph_tf_factor2.getText().toString() + ","
								+ ph_tf_factor3.getText().toString() + "," + ph_tf_factor4.getText().toString() + ","
								+ ph_cb_tendency.getSelectedItem().toString() + ","
								+ ph_cb_resultunit.getSelectedItem().toString() + ","
								+ ph_cb_calibrate.getSelectedItem().toString() + "," + ph_tf_slope1.getText().toString()
								+ "," + ph_tf_slope2.getText().toString() + "," + ph_tf_sop_value.getText().toString();// www
						if (selected_methodfile != null) {
							if (res.matches(data)) {

//								if (no_update == true) {
//									popup_input_ph dg = new popup_input_ph();
//									String[] temp = { data, selected_methodfile, "false", "ph" };
//									dg.main(temp);
//								} else {
//									popup_input_ph dg = new popup_input_ph();
//									String[] temp = { data, selected_methodfile, "true", "ph" };
//									dg.main(temp);
//								}
							} else {
								save_method save_method1 = new save_method();
								String[] temp_arr = { data, selected_methodfile, "ph" };
								save_method1.main(temp_arr);
								saved = 0;
							}
						} else {
							save_method save_method1 = new save_method();
							String[] temp_arr = { data, "", "ph" };
							save_method1.main(temp_arr);
							saved = 0;
						}

					} else if (selected_experiment.matches("amperometry")) {
						String data = amp_tf_stirtime.getText().toString() + "," + amp_tf_delay.getText().toString()
								+ "," + amp_tf_predose.getText().toString() + "," + amp_tf_maxvol.getText().toString()
								+ "," + amp_tf_blankvol.getText().toString() + "," + amp_tf_burette.getText().toString()
								+ "," + amp_tf_endpoint.getText().toString() + ","
								+ amp_cb_dosagerate.getSelectedItem().toString() + ","
								+ amp_cb_formula.getSelectedItem().toString() + ","
								+ amp_cb_nooftrials.getSelectedItem().toString() + ","
								+ amp_tf_factor1.getText().toString() + "," + amp_tf_factor2.getText().toString() + ","
								+ amp_tf_factor3.getText().toString() + "," + amp_tf_factor4.getText().toString() + ","
								+ amp_cb_filter.getSelectedItem().toString() + ","
								+ amp_cb_resultunit.getSelectedItem().toString() + ","
								+ amp_tf_sop_value.getText().toString();// www
						if (selected_methodfile != null) {
							if (res.matches(data)) {

//								if (no_update == true) {
//									popup_input_amp dg = new popup_input_amp();
//									String[] temp = { data, selected_methodfile, "false", "amp" };
//									dg.main(temp);
//								} else {
//									popup_input_amp dg = new popup_input_amp();
//									String[] temp = { data, selected_methodfile, "true", "amp" };
//									dg.main(temp);
//								}
							} else {
								save_method save_method1 = new save_method();
								String[] temp_arr = { data, selected_methodfile, "amp" };
								save_method1.main(temp_arr);
								saved = 0;
							}
						} else {
							save_method save_method1 = new save_method();
							String[] temp_arr = { data, "", "amp" };
							save_method1.main(temp_arr);
							saved = 0;
						}
					}

					else if (selected_experiment.matches("karl")) {
						String data = kf_tf_delay.getText().toString() + "," + kf_tf_stirtime.getText().toString() + ","
								+ kf_tf_maxvol.getText().toString() + "," + kf_tf_blankvol.getText().toString() + ","
								+ kf_tf_burette.getText().toString() + "," + kf_tf_density.getText().toString() + ","
								+ kf_tf_factor.getText().toString() + "," + kf_tf_endpoint.getText().toString() + ","
								+ kf_cb_dosagerate.getSelectedItem().toString() + ","
								+ kf_cb_resultunit.getSelectedItem().toString() + ","
								+ kf_cb_nooftrials.getSelectedItem().toString() + ","
								+ kf_tf_sop_value.getText().toString();// www
						// //system.out.println("Data ELse If KF = "+data);
						if (selected_methodfile != null) {
							// //system.out.println("Data ELse first If KF = "+selected_methodfile);
							if (res.matches(data)) {
								if (no_update == true) {
									popup_input_kf dg = new popup_input_kf();
									String[] temp = { data, selected_methodfile, "false", "kf" };
									dg.main(temp);
								} else {
									popup_input_kf dg = new popup_input_kf();
									String[] temp = { data, selected_methodfile, "true", "kf" };
									dg.main(temp);
								}
							} else {
								/// //system.out.println("Data ELse first If else KF = "+res);
								save_method save_method1 = new save_method();
								String[] temp_arr = { data, selected_methodfile, "kf" };
								save_method1.main(temp_arr);
								saved = 0;
							}
						} else {
							// //system.out.println("Data ELse first else KF = "+res);

							save_method save_method1 = new save_method();
							String[] temp_arr = { data, "", "kf" };
							save_method1.main(temp_arr);
							saved = 0;
						}
					}
				}
			}
		});
		add(btn_run_mb);

		btn_save_mb = new JButton("Save");
		btn_save_mb.setBackground(SystemColor.window);
		btn_save_mb.setBounds(391, 600, 113, 34);
		btn_save_mb.setFont(new Font("Times New Roman", Font.BOLD, 20));
		add(btn_save_mb);
		btn_save_mb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selected_experiment.matches("potentiometry")) {
					String data = pot_tf_predose.getText().toString() + "," + pot_tf_stirtime.getText().toString() + ","
							+ pot_tf_maxvol.getText().toString() + "," + pot_tf_blankvol.getText().toString() + ","
							+ pot_tf_burette.getText().toString() + "," + pot_cb_threshold.getSelectedItem().toString()
							+ "," + pot_cb_filter.getSelectedItem().toString() + ","
							+ pot_cb_dosagerate.getSelectedItem().toString() + ","
							+ pot_cb_nooftrials.getSelectedItem().toString() + "," + pot_tf_factor1.getText().toString()
							+ "," + pot_tf_factor2.getText().toString() + "," + pot_tf_factor3.getText().toString()
							+ "," + pot_tf_factor4.getText().toString() + ","
							+ pot_cb_epselect.getSelectedItem().toString() + ","
							+ pot_cb_formula.getSelectedItem().toString() + ","
							+ pot_cb_tendency.getSelectedItem().toString() + ","
							+ pot_cb_resultunit.getSelectedItem().toString() + ","
							+ pot_tf_sop_value.getText().toString();// www
					if (selected_methodfile != null) {
						save_method save_method1 = new save_method();
						String[] temp_arr = { data, selected_methodfile, "pot" };
						save_method1.main(temp_arr);
						saved = 0;
					} else {
						save_method save_method1 = new save_method();
						String[] temp_arr = { data, "", "pot" };
						save_method1.main(temp_arr);
						saved = 0;
					}

				}

				else if (selected_experiment.matches("phstat")) {
					String data = ph_tf_stirtime.getText().toString() + "," + ph_tf_delay.getText().toString() + ","
							+ ph_tf_predose.getText().toString() + "," + ph_tf_maxvol.getText().toString() + ","
							+ ph_tf_blankvol.getText().toString() + "," + ph_tf_burette.getText().toString() + ","
							+ ph_tf_endpoint.getText().toString() + "," + ph_cb_dosagerate.getSelectedItem().toString()
							+ "," + ph_cb_formula.getSelectedItem().toString() + ","
							+ ph_cb_nooftrials.getSelectedItem().toString() + "," + ph_tf_factor1.getText().toString()
							+ "," + ph_tf_factor2.getText().toString() + "," + ph_tf_factor3.getText().toString() + ","
							+ ph_tf_factor4.getText().toString() + "," + ph_cb_tendency.getSelectedItem().toString()
							+ "," + ph_cb_resultunit.getSelectedItem().toString() + ","
							+ ph_cb_calibrate.getSelectedItem().toString() + "," + ph_tf_slope1.getText().toString()
							+ "," + ph_tf_slope2.getText().toString() + "," + ph_tf_sop_value.getText().toString();// www
					if (selected_methodfile != null) {
						save_method save_method1 = new save_method();
						String[] temp_arr = { data, selected_methodfile, "ph" };
						save_method1.main(temp_arr);
						saved = 0;
					} else {
						save_method save_method1 = new save_method();
						String[] temp_arr = { data, "", "ph" };
						save_method1.main(temp_arr);
						saved = 0;
					}

				} else if (selected_experiment.matches("amperometry")) {
					String data = amp_tf_stirtime.getText().toString() + "," + amp_tf_delay.getText().toString() + ","
							+ amp_tf_predose.getText().toString() + "," + amp_tf_maxvol.getText().toString() + ","
							+ amp_tf_blankvol.getText().toString() + "," + amp_tf_burette.getText().toString() + ","
							+ amp_tf_endpoint.getText().toString() + ","
							+ amp_cb_dosagerate.getSelectedItem().toString() + ","
							+ amp_cb_formula.getSelectedItem().toString() + ","
							+ amp_cb_nooftrials.getSelectedItem().toString() + "," + amp_tf_factor1.getText().toString()
							+ "," + amp_tf_factor2.getText().toString() + "," + amp_tf_factor3.getText().toString()
							+ "," + amp_tf_factor4.getText().toString() + ","
							+ amp_cb_filter.getSelectedItem().toString() + ","
							+ amp_cb_resultunit.getSelectedItem().toString() + ","
							+ amp_tf_sop_value.getText().toString();// www
					if (selected_methodfile != null) {
						save_method save_method1 = new save_method();
						String[] temp_arr = { data, selected_methodfile, "amp" };
						save_method1.main(temp_arr);
						saved = 0;
					} else {
						save_method save_method1 = new save_method();
						String[] temp_arr = { data, "", "amp" };
						save_method1.main(temp_arr);
						saved = 0;
					}
				}

				else if (selected_experiment.matches("karl")) {
					String data = kf_tf_delay.getText().toString() + "," + kf_tf_stirtime.getText().toString() + ","
							+ kf_tf_maxvol.getText().toString() + "," + kf_tf_blankvol.getText().toString() + ","
							+ kf_tf_burette.getText().toString() + "," + kf_tf_density.getText().toString() + ","
							+ kf_tf_factor.getText().toString() + "," + kf_tf_endpoint.getText().toString() + ","
							+ kf_cb_dosagerate.getSelectedItem().toString() + ","
							+ kf_cb_resultunit.getSelectedItem().toString() + ","
							+ kf_cb_nooftrials.getSelectedItem().toString() + ","
							+ kf_tf_sop_value.getText().toString();// www
					if (selected_methodfile != null) {
						save_method save_method1 = new save_method();
						String[] temp_arr = { data, selected_methodfile, "kf" };
						save_method1.main(temp_arr);
						saved = 0;
					} else {
						save_method save_method1 = new save_method();
						String[] temp_arr = { data, "", "kf" };
						save_method1.main(temp_arr);
						saved = 0;
					}
				}
			}
		});
		btn_refresh_mb = new JButton("Refresh");
		btn_refresh_mb.setBackground(SystemColor.window);
		btn_refresh_mb.setBounds(504, 600, 113, 34);
		btn_refresh_mb.setFont(new Font("Times New Roman", Font.BOLD, 20));
		add(btn_refresh_mb);
		btn_refresh_mb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				saved_file = false;
				selected_methodfile = null;
				res = "";
				metd_header.setText("");
				if (selected_experiment.matches("potentiometry")) {
					pot_tf_predose.setText("0");
					pot_tf_stirtime.setText("10");
					pot_tf_maxvol.setText("100");
					pot_tf_blankvol.setText("0");
					pot_tf_burette.setText("1");
					pot_cb_threshold.setSelectedItem("1");
					pot_cb_filter.setSelectedItem("1");
					pot_cb_dosagerate.setSelectedItem("0.5");
					pot_cb_nooftrials.setSelectedItem("1");
					pot_tf_factor1.setText("1");
					pot_tf_factor2.setText("1");
					pot_tf_factor3.setText("1");
					pot_tf_factor4.setText("1");
					pot_cb_epselect.setSelectedItem("Auto");
					pot_cb_formula.setSelectedItem("1");
					pot_cb_tendency.setSelectedItem("Up");
					pot_cb_resultunit.setSelectedItem("%");
					pot_tf_sop_value.setText("Not Selected");
				} else if (selected_experiment.matches("phstat")) {
					ph_tf_delay.setText("0");
					ph_tf_predose.setText("0");
					ph_tf_stirtime.setText("10");
					ph_tf_maxvol.setText("100");
					ph_tf_blankvol.setText("0");
					ph_tf_burette.setText("1");
					ph_tf_endpoint.setText("10.0");
					ph_cb_dosagerate.setSelectedItem("0.5");
					ph_cb_formula.setSelectedItem("2");
					ph_cb_nooftrials.setSelectedItem("1");
					ph_tf_factor1.setText("1");
					ph_tf_factor2.setText("1");
					ph_tf_factor3.setText("1");
					ph_tf_factor4.setText("1");
					ph_cb_tendency.setSelectedItem("Up");
					ph_cb_resultunit.setSelectedItem("%");
					ph_cb_calibrate.setSelectedItem("3 Point CUST");
					ph_tf_slope1.setText("1");
					ph_tf_slope2.setText("1");
					ph_tf_sop_value.setText("Not Selected");
				} else if (selected_experiment.matches("amperometry")) {
					amp_tf_delay.setText("0");
					amp_tf_predose.setText("0");
					amp_tf_stirtime.setText("10");
					amp_tf_maxvol.setText("100");
					amp_tf_blankvol.setText("0");
					amp_tf_burette.setText("1");
					amp_tf_endpoint.setText("10.0");
					amp_cb_dosagerate.setSelectedItem("0.5");
					amp_cb_formula.setSelectedItem("2");
					amp_cb_nooftrials.setSelectedItem("1");
					amp_tf_factor1.setText("1");
					amp_tf_factor2.setText("1");
					amp_tf_factor3.setText("1");
					amp_tf_factor4.setText("1");
					amp_cb_filter.setSelectedItem("1");
					amp_cb_resultunit.setSelectedItem("%");
					amp_tf_sop_value.setText("Not Selected");
				} else if (selected_experiment.matches("karl")) {
					kf_tf_delay.setText("0");
					kf_tf_stirtime.setText("0");
					kf_tf_maxvol.setText("100");
					kf_tf_blankvol.setText("0");
					kf_tf_burette.setText("1");
					kf_tf_density.setText("1");
					kf_tf_factor.setText("1");
					kf_tf_endpoint.setText("30");
					kf_cb_dosagerate.setSelectedItem("0.5");
					kf_cb_resultunit.setSelectedItem("%");
					kf_cb_nooftrials.setSelectedItem("1");
					kf_tf_sop_value.setText("Not Selected");
				}
			}
		});

		btn_esc_mb = new JButton("ESC");
		btn_esc_mb.setBackground(SystemColor.window);
		btn_esc_mb.setBounds(618, 600, 113, 34);
		btn_esc_mb.setFont(new Font("Times New Roman", Font.BOLD, 20));
		add(btn_esc_mb);
		btn_esc_mb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					exec1.shutdown();
				} catch (NullPointerException dfj) {
				}
				try {
					exec2.shutdown();
				} catch (NullPointerException df) {
				}
				try {
					exec3.shutdown();
				} catch (NullPointerException ervf) {
				}
				try {
					exec4.shutdown();
				} catch (NullPointerException ervf) {}

				try {
					Thread.sleep(300);
					output.print("<8888>ESCP*");
					output.flush();
					fill_dose = 0;
				//	stop_dose_counter();
					mb_cur_state = "";
					btn_fill_mb.setEnabled(true);
					btn_dose_mb.setEnabled(true);
					btn_wash_mb.setEnabled(true);
					btn_rinse_mb.setEnabled(true);
					btn_open_mb.setEnabled(true);
					btn_run_mb.setEnabled(true);
					btn_save_mb.setEnabled(true);
					btn_refresh_mb.setEnabled(true);
					ReformatBuffer.current_state = "menubar_stpm";
				} catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(null, "Please connect to ComPort!");
				} catch (InterruptedException wed) {}
			}
		});

		rdbtnNewRadioButton = new JRadioButton("Potentiometry");
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saved_file = false;
				selected_methodfile = null;
				res = "";
				metd_header.setText("");
				selected_experiment = "potentiometry";
				refresh_items();
				panel_pot1.setVisible(true);
				panel_pot2.setVisible(true);
				panel_pot3.setVisible(true);
				panel_pot4.setVisible(true);
				panel_pot5.setVisible(true);
				panel_kf1.setVisible(false);
				panel_kf2.setVisible(false);
				panel_kf3.setVisible(false);
				panel_kf4.setVisible(false);
				panel_kf5.setVisible(false);
				panel_ph1.setVisible(false);
				panel_ph2.setVisible(false);
				panel_ph3.setVisible(false);
				panel_ph4.setVisible(false);
				panel_ph5.setVisible(false);
				panel_amp1.setVisible(false);
				panel_amp2.setVisible(false);
				panel_amp3.setVisible(false);
				panel_amp4.setVisible(false);
				panel_amp5.setVisible(false);
				p_formula.setVisible(true);
				p_formula2.setVisible(true);
				p_formula3.setVisible(true);

				formula_header.setVisible(true);
				p_formula.setBounds(790, 340, 360, 60);
				p_formula2.setBounds(790, 410, 360, 60);
				p_formula3.setBounds(790, 480, 360, 60);

				formula = new TeXFormula("N = \\frac {W*(1-\\frac{M}{100})*F_3*F_4}{(V_1-V_B_L_K)*F_1*F_2}");
				ti_formula = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 18);
				b_formula = new BufferedImage(ti_formula.getIconWidth(), ti_formula.getIconHeight(),
						BufferedImage.TYPE_4BYTE_ABGR);
				ti_formula.paintIcon(new JLabel(), b_formula.getGraphics(), 0, 0);

				formula2 = new TeXFormula("");
				ti_formula2 = formula2.createTeXIcon(TeXConstants.STYLE_DISPLAY, 18);
				b_formula2 = new BufferedImage(ti_formula2.getIconWidth(), ti_formula2.getIconHeight(),
						BufferedImage.TYPE_4BYTE_ABGR);
				ti_formula2.paintIcon(new JLabel(), b_formula2.getGraphics(), 0, 0);

				formula3 = new TeXFormula("");
				ti_formula3 = formula3.createTeXIcon(TeXConstants.STYLE_DISPLAY, 18);
				b_formula3 = new BufferedImage(ti_formula3.getIconWidth(), ti_formula3.getIconHeight(),
						BufferedImage.TYPE_4BYTE_ABGR);
				ti_formula3.paintIcon(new JLabel(), b_formula3.getGraphics(), 0, 0);

				l_formula.setIcon(ti_formula);
				l_formula2.setIcon(ti_formula2);
				l_formula3.setIcon(ti_formula3);
			}
		});
		rdbtnNewRadioButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
		rdbtnNewRadioButton.setBounds(133, 39, 150, 21);
		rdbtnNewRadioButton.setSelected(true);
		add(rdbtnNewRadioButton);

		rdbtnNewRadioButton_2 = new JRadioButton("pH Stat");
		rdbtnNewRadioButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saved_file = false;
				selected_methodfile = null;
				res = "";
				metd_header.setText("");
				selected_experiment = "phstat";
				refresh_items();
				panel_pot1.setVisible(false);
				panel_pot2.setVisible(false);
				panel_pot3.setVisible(false);
				panel_pot4.setVisible(false);
				panel_pot5.setVisible(false);
				panel_kf1.setVisible(false);
				panel_kf2.setVisible(false);
				panel_kf3.setVisible(false);
				panel_kf4.setVisible(false);
				panel_kf5.setVisible(false);
				panel_amp1.setVisible(false);
				panel_amp2.setVisible(false);
				panel_amp3.setVisible(false);
				panel_amp4.setVisible(false);
				panel_amp5.setVisible(false);
				panel_ph1.setVisible(true);
				panel_ph2.setVisible(true);
				panel_ph3.setVisible(true);
				panel_ph4.setVisible(true);
				panel_ph5.setVisible(true);
				p_formula.setVisible(true);
				p_formula2.setVisible(true);
				p_formula3.setVisible(true);

				formula_header.setVisible(false);

				p_formula.setBounds(790, 340, 360, 60);
				p_formula2.setBounds(740, 410, 360, 60);
				p_formula3.setBounds(740, 480, 360, 60);

				formula = new TeXFormula("");
				ti_formula = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 18);
				b_formula = new BufferedImage(ti_formula.getIconWidth(), ti_formula.getIconHeight(),
						BufferedImage.TYPE_4BYTE_ABGR);
				ti_formula.paintIcon(new JLabel(), b_formula.getGraphics(), 0, 0);

				formula2 = new TeXFormula("");
				ti_formula2 = formula2.createTeXIcon(TeXConstants.STYLE_DISPLAY, 18);
				b_formula2 = new BufferedImage(ti_formula2.getIconWidth(), ti_formula2.getIconHeight(),
						BufferedImage.TYPE_4BYTE_ABGR);
				ti_formula2.paintIcon(new JLabel(), b_formula2.getGraphics(), 0, 0);

				formula3 = new TeXFormula("");
				ti_formula3 = formula3.createTeXIcon(TeXConstants.STYLE_DISPLAY, 18);
				b_formula3 = new BufferedImage(ti_formula3.getIconWidth(), ti_formula3.getIconHeight(),
						BufferedImage.TYPE_4BYTE_ABGR);
				ti_formula3.paintIcon(new JLabel(), b_formula3.getGraphics(), 0, 0);

				l_formula.setIcon(ti_formula);
				l_formula2.setIcon(ti_formula2);
				l_formula3.setIcon(ti_formula3);
			}
		});

		rdbtnNewRadioButton_2.setFont(new Font("Times New Roman", Font.BOLD, 18));
		rdbtnNewRadioButton_2.setBounds(451, 39, 126, 21);
		// add(rdbtnNewRadioButton_2);

		rdbtnNewRadioButton_1 = new JRadioButton("Amperometry");
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selected_experiment = "amperometry";
				saved_file = false;
				selected_methodfile = null;
				metd_header.setText("");
				res = "";
				refresh_items();
				panel_pot1.setVisible(false);
				panel_pot2.setVisible(false);
				panel_pot3.setVisible(false);
				panel_pot4.setVisible(false);
				panel_pot5.setVisible(false);
				panel_kf1.setVisible(false);
				panel_kf2.setVisible(false);
				panel_kf3.setVisible(false);
				panel_kf4.setVisible(false);
				panel_kf5.setVisible(false);
				panel_ph1.setVisible(false);
				panel_ph2.setVisible(false);
				panel_ph3.setVisible(false);
				panel_ph4.setVisible(false);
				panel_ph5.setVisible(false);
				panel_amp1.setVisible(true);
				panel_amp2.setVisible(true);
				panel_amp3.setVisible(true);
				panel_amp4.setVisible(true);
				panel_amp5.setVisible(true);
				p_formula.setVisible(true);
				p_formula2.setVisible(true);
				p_formula3.setVisible(true);
				p_formula.setBounds(790, 340, 360, 60);
				p_formula2.setBounds(740, 410, 360, 60);
				p_formula3.setBounds(740, 480, 360, 60);

				formula_header.setVisible(false);

				formula = new TeXFormula("");
				ti_formula = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 18);
				b_formula = new BufferedImage(ti_formula.getIconWidth(), ti_formula.getIconHeight(),
						BufferedImage.TYPE_4BYTE_ABGR);
				ti_formula.paintIcon(new JLabel(), b_formula.getGraphics(), 0, 0);

				formula2 = new TeXFormula("");
				ti_formula2 = formula2.createTeXIcon(TeXConstants.STYLE_DISPLAY, 18);
				b_formula2 = new BufferedImage(ti_formula2.getIconWidth(), ti_formula2.getIconHeight(),
						BufferedImage.TYPE_4BYTE_ABGR);
				ti_formula2.paintIcon(new JLabel(), b_formula2.getGraphics(), 0, 0);

				formula3 = new TeXFormula("");
				ti_formula3 = formula3.createTeXIcon(TeXConstants.STYLE_DISPLAY, 18);
				b_formula3 = new BufferedImage(ti_formula3.getIconWidth(), ti_formula3.getIconHeight(),
						BufferedImage.TYPE_4BYTE_ABGR);
				ti_formula3.paintIcon(new JLabel(), b_formula3.getGraphics(), 0, 0);

				l_formula.setIcon(ti_formula);
				l_formula2.setIcon(ti_formula2);
				l_formula3.setIcon(ti_formula3);
			}
		});

		rdbtnNewRadioButton_1.setFont(new Font("Times New Roman", Font.BOLD, 18));
		rdbtnNewRadioButton_1.setBounds(133, 85, 150, 21);
		// add(rdbtnNewRadioButton_1);

		rdbtnNewRadioButton_3 = new JRadioButton("Karl Fischer");
		rdbtnNewRadioButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selected_experiment = "karl";
				saved_file = false;
				selected_methodfile = null;
				res = "";
				metd_header.setText("");
				refresh_items();
				panel_pot1.setVisible(false);
				panel_pot2.setVisible(false);
				panel_pot3.setVisible(false);
				panel_pot4.setVisible(false);
				panel_pot5.setVisible(false);
				panel_kf1.setVisible(true);
				panel_kf2.setVisible(true);
				panel_kf3.setVisible(true);
				panel_kf4.setVisible(true);
				panel_kf5.setVisible(true);
				panel_ph1.setVisible(false);
				panel_ph2.setVisible(false);
				panel_ph3.setVisible(false);
				panel_ph4.setVisible(false);
				panel_ph5.setVisible(false);
				panel_amp1.setVisible(false);
				panel_amp2.setVisible(false);
				panel_amp3.setVisible(false);
				panel_amp4.setVisible(false);
				panel_amp5.setVisible(false);

				p_formula.setVisible(true);
				p_formula2.setVisible(true);
				p_formula3.setVisible(true);
				formula_header.setVisible(true);

				p_formula.setBounds(740, 340, 420, 60);
				p_formula2.setBounds(700, 410, 530, 60);
				p_formula3.setBounds(700, 480, 530, 60);

				formula = new TeXFormula(
						"KFF (H2O) = \\frac {mg\\ or\\ microlitre\\ of\\ H2O\\ injected}{Vol\\ of\\ KF\\ Reagent\\ consumed\\ (ml)}");
				ti_formula = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 15);
				b_formula = new BufferedImage(ti_formula.getIconWidth(), ti_formula.getIconHeight(),
						BufferedImage.TYPE_4BYTE_ABGR);
				ti_formula.paintIcon(new JLabel(), b_formula.getGraphics(), 0, 0);

				formula2 = new TeXFormula(
						"KFF (DST) = \\frac {mg\\ of\\ DST\\ *0.1566}{Vol\\ of\\ KF\\ Reagent\\ consumed\\ (ml)}");
				ti_formula2 = formula2.createTeXIcon(TeXConstants.STYLE_DISPLAY, 15);
				b_formula2 = new BufferedImage(ti_formula2.getIconWidth(), ti_formula2.getIconHeight(),
						BufferedImage.TYPE_4BYTE_ABGR);
				ti_formula2.paintIcon(new JLabel(), b_formula2.getGraphics(), 0, 0);

				formula3 = new TeXFormula(
						"Moisture = \\frac {(Vol\\ of\\ KF\\ reagent\\ consumed-V_b_l_k)*KFF}{Sample\\ weight\\ or\\ (Vol\\ *\\ Density)}");
				ti_formula3 = formula3.createTeXIcon(TeXConstants.STYLE_DISPLAY, 15);
				b_formula3 = new BufferedImage(ti_formula3.getIconWidth(), ti_formula3.getIconHeight(),
						BufferedImage.TYPE_4BYTE_ABGR);
				ti_formula3.paintIcon(new JLabel(), b_formula3.getGraphics(), 0, 0);

				l_formula.setIcon(ti_formula);
				l_formula2.setIcon(ti_formula2);
				l_formula3.setIcon(ti_formula3);

			}
		});

		rdbtnNewRadioButton_3.setFont(new Font("Times New Roman", Font.BOLD, 18));
		rdbtnNewRadioButton_3.setBounds(451, 39, 126, 21);
		add(rdbtnNewRadioButton_3);

		ButtonGroup bg1 = new ButtonGroup();
		bg1.add(rdbtnNewRadioButton_3);
		bg1.add(rdbtnNewRadioButton_2);
		bg1.add(rdbtnNewRadioButton_1);
		bg1.add(rdbtnNewRadioButton);

		JLabel lblNewLabel_19 = new JLabel("Titration");
		lblNewLabel_19.setBounds(23, 10, 82, 28);
		add(lblNewLabel_19);
		lblNewLabel_19.setFont(new Font("Times New Roman", Font.BOLD, 18));

		metd_header = new JLabel();
		metd_header.setBounds(23, 130, 1500, 28);
		add(metd_header);
		metd_header.setFont(new Font("Times New Roman", Font.BOLD, 18));

		JLayeredPane layeredPane_1 = new JLayeredPane();
		layeredPane_1.setBounds(10, 140, 740, 483);
		add(layeredPane_1);

		JLabel lblNewLabel_20 = new JLabel("pH/mV Display");
		lblNewLabel_20.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_20.setBounds(819, 20, 150, 22);
		add(lblNewLabel_20);

		jlabel_mv_Value = new JLabel("0 mV");
		jlabel_mv_Value.setFont(new Font("Times New Roman", Font.BOLD, 20));
		jlabel_mv_Value.setBounds(819, 50, 150, 22);
		add(jlabel_mv_Value);

		lblNewLabel_vol = new JLabel("Volume Filled");
		lblNewLabel_vol.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_vol.setBounds(819, 100, 200, 22);
		add(lblNewLabel_vol);

		formula_header = new JLabel("Formula", SwingConstants.CENTER);
		formula_header.setFont(new Font("Times New Roman", Font.BOLD, 20));
		formula_header.setBounds(819, 300, 220, 22);
		add(formula_header);

		p_formula = new JPanel();
		p_formula2 = new JPanel();
		p_formula3 = new JPanel();

		math = "N = \\frac {W*(1-\\frac{M}{100})*F_3*F_4}{(V_1-V_B_L_K)*F_1*F_2}";

		formula = new TeXFormula(math);
		ti_formula = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 18);
		b_formula = new BufferedImage(ti_formula.getIconWidth(), ti_formula.getIconHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);
		ti_formula.paintIcon(new JLabel(), b_formula.getGraphics(), 0, 0);

		formula2 = new TeXFormula(math2);
		ti_formula2 = formula2.createTeXIcon(TeXConstants.STYLE_DISPLAY, 18);
		b_formula2 = new BufferedImage(ti_formula2.getIconWidth(), ti_formula2.getIconHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);
		ti_formula2.paintIcon(new JLabel(), b_formula2.getGraphics(), 0, 0);

		formula3 = new TeXFormula(math3);
		ti_formula3 = formula3.createTeXIcon(TeXConstants.STYLE_DISPLAY, 18);
		b_formula3 = new BufferedImage(ti_formula3.getIconWidth(), ti_formula3.getIconHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);
		ti_formula3.paintIcon(new JLabel(), b_formula3.getGraphics(), 0, 0);

		l_formula = new JLabel();
		l_formula2 = new JLabel();
		l_formula3 = new JLabel();

		p_formula.setBounds(790, 340, 360, 60);
		p_formula2.setBounds(790, 410, 360, 60);
		p_formula3.setBounds(790, 480, 360, 60);

		l_formula.setIcon(ti_formula);
		l_formula2.setIcon(ti_formula2);
		l_formula3.setIcon(ti_formula3);

		p_formula.add(l_formula);
		p_formula2.add(l_formula2);
		p_formula3.add(l_formula3);

		add(p_formula);
		add(p_formula2);
		add(p_formula3);

		jlabel_ml_Value = new JLabel("0.0 mL");
		jlabel_ml_Value.setFont(new Font("Times New Roman", Font.BOLD, 20));
		jlabel_ml_Value.setBounds(819, 128, 150, 22);
		add(jlabel_ml_Value);

		JLayeredPane layeredPane_2 = new JLayeredPane();
		layeredPane_2.setBounds(763, 10, 330, 120);
		add(layeredPane_2);

		btn_fill_mb = new JButton("Fill");
		btn_fill_mb.setBackground(SystemColor.window);
		btn_fill_mb.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btn_fill_mb.setBounds(819, 180, 101, 34);
		add(btn_fill_mb);
		btn_fill_mb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fill_dose = 0;
				volume = (double) 0;
				try {
					String result = (String) JOptionPane.showInputDialog(frame1, "Enter the Fill Volume", "User Input",
							JOptionPane.PLAIN_MESSAGE, null, null, "");
					volume = Double.parseDouble(result);
					if (volume > 0) {

						try {

							output.print("<8888>FILL*");
							output.flush();
							mb_cur_state = "filling";

							ReformatBuffer.current_state = "menubar_fill";
						} catch (NullPointerException ee) {
							JOptionPane.showMessageDialog(null, "Please select the ComPort!");
						}
					}
				} catch (NullPointerException ne) {
					JOptionPane.showMessageDialog(null, "Please enter a value!");

				} catch (NumberFormatException ddd) {
					JOptionPane.showMessageDialog(null, "Please enter a valid value!");
				}
			}
		});

		btn_dose_mb = new JButton("Dose");
		btn_dose_mb.setBackground(Color.WHITE);
		btn_dose_mb.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btn_dose_mb.setBounds(957, 180, 101, 34);
		add(btn_dose_mb);
		btn_dose_mb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fill_dose = 0;
				volume = (double) 0;

				try {
					String result = (String) JOptionPane.showInputDialog(frame1, "Enter the Dose Volume", "User Input",
							JOptionPane.PLAIN_MESSAGE, null, null, "");
					volume = Double.parseDouble(result);
					if (volume > 0) {
						try {
							output.print("<8888>DOSE*");
							output.flush();
							mb_cur_state = "dosing";

							ReformatBuffer.current_state = "menubar_dose";
						} catch (NullPointerException ee) {
							JOptionPane.showMessageDialog(null, "Please select the ComPort!");
						}
					}
				} catch (NullPointerException ne) {
					JOptionPane.showMessageDialog(null, "Please enter a value!");

				} catch (NumberFormatException ddd) {
					JOptionPane.showMessageDialog(null, "Please enter a valid value!");
				}
			}
		});

		btn_wash_mb = new JButton("Wash");
		btn_wash_mb.setBackground(Color.WHITE);
		btn_wash_mb.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btn_wash_mb.setBounds(819, 230, 101, 34);
		add(btn_wash_mb);
		btn_wash_mb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 14/09/1999
				open_wash_counter dg = new open_wash_counter();
				String[] a = new String[1];
				a[0] = "a";
				dg.main(a);
			}
		});

		btn_rinse_mb = new JButton("Rinse");
		btn_rinse_mb.setBackground(Color.WHITE);
		btn_rinse_mb.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btn_rinse_mb.setBounds(957, 230, 101, 35);
		add(btn_rinse_mb);
		btn_rinse_mb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				open_rinse_counter dg = new open_rinse_counter();
				String[] a = new String[1];
				a[0] = "a";
				dg.main(a);
			}
		});

		JLayeredPane layeredPane_3 = new JLayeredPane();
		layeredPane_3.setBounds(763, 140, 330, 483);
		add(layeredPane_3);

		JMenuBar menubar1 = new JMenuBar();
		frame1.setJMenuBar(menubar1);

		JMenu menu_file = new JMenu("File");
		menu_file.setFont(new Font("Times New Roman", Font.BOLD, 18));
		menubar1.add(menu_file);

		menu_item_log = new JMenuItem("Audit Log");
		menu_file.add(menu_item_log);
		menu_item_log.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				log_viewer objsda = new log_viewer();
				String[] a = {};
				objsda.main(a);
			}
		});

		menu_item_view_reports = new JMenuItem("View Reports");
		menu_item_view_reports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String[] aa = { user_name, role_items };
				open_report.main(aa);

			}
		});
		menu_file.add(menu_item_view_reports);

		mnNewMenu_7 = new JMenuItem("Print Method");
	//	menu_file.add(mnNewMenu_7);

		menu_item_exit = new JMenuItem("Exit");
		menu_file.add(menu_item_exit);
		menu_item_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Thread.sleep(500);
					output.print("<8888>ESCP*");
					output.flush();
					Thread.sleep(200);
				} catch (NullPointerException ne) {
				} catch (InterruptedException sdce) {
					sdce.printStackTrace();
				}
				try {
					serial_port1.closePort();
				} catch (NullPointerException ne) {
				}
				
				try {
					audit_log_push.push_to_audit(get_date(), get_time(),"","Software closed!");
				} catch (ParseException e1) {e1.printStackTrace();}
				
				frame1.dispose();
				frame1 = new JFrame();
				p = new JPanel();
				p.revalidate();
				p.repaint();
			}
		});
		

		JMenu menu_mv_display = new JMenu("mV Display");
		menu_mv_display.setFont(new Font("Segoe UI", Font.BOLD, 15));
		menubar1.add(menu_mv_display);

		menu_item_mv_display = new JMenuItem("mV Display");
		menu_item_mv_display.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ReformatBuffer.current_exp = "main";
					output.print("<8888>CVOL*");
					output.flush();
				} catch (NullPointerException ee) {
					// system.out.println(" portport_clickclick = ");
				}
			}
		});
		menu_mv_display.add(menu_item_mv_display);

		JMenu mnNewMenu_4 = new JMenu("Results");
		mnNewMenu_4.setFont(new Font("Segoe UI", Font.BOLD, 15));
		menubar1.add(mnNewMenu_4);

		menu_item_pot = new JMenuItem("Recall Potentiometry");
		mnNewMenu_4.add(menu_item_pot);
		menu_item_pot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] aa = { user_name, role_items };
				open_pot_result.main(aa);
			}
		});

		menu_item_kf = new JMenuItem("Recall Karl Fisher");
		mnNewMenu_4.add(menu_item_kf);
		menu_item_kf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] aa = { user_name, role_items };
				open_kf_result.main(aa);
			}
		});

		menu_item_ph = new JMenuItem("Recall PH");
		// mnNewMenu_4.add(menu_item_ph);
		menu_item_ph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] aa = { user_name, role_items };
				// open_kf_result.main(aa);
			}
		});

		menu_item_amp = new JMenuItem("Recall Amperometry");
		// mnNewMenu_4.add(menu_item_amp);
		menu_item_amp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] aa = { user_name, role_items };
				// open_kf_result.main(aa);
			}
		});

		JMenu mnNewMenu_5 = new JMenu("Settings");
		mnNewMenu_5.setFont(new Font("Segoe UI", Font.BOLD, 15));
		menubar1.add(mnNewMenu_5);

		menu_item_login = new JMenuItem("Login");
		menu_item_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminLogin al = new AdminLogin();
				if (checked_vaidity == true) {
					AdminLogin.check_validity = true;
				} else {
					AdminLogin.check_validity = false;
				}
				al.setVisible(true);
			}
		});
		mnNewMenu_5.add(menu_item_login);

		menu_item_sa_login = new JMenuItem("Super admin login");
		menu_item_sa_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SuperAdminLogin objsda = new SuperAdminLogin();
				objsda.setVisible(true);
				// frame.dispose();
			}
		});
		mnNewMenu_5.add(menu_item_sa_login);

		menuItem_add_sop = new JMenuItem("Add SOP");
		menuItem_add_sop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a SOP file");
				int userSelection = fileChooser.showOpenDialog(null);
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToSave = fileChooser.getSelectedFile();
					String separator = "\\";
					String[] str_arr = fileToSave.getAbsolutePath().replaceAll(Pattern.quote(separator), "\\\\")
							.split("\\\\");
					// system.out.println("file name = " + str_arr[str_arr.length - 1]);
					File source = new File(fileToSave.getAbsolutePath());
					File dest = new File("C:\\SQLite\\SOP\\" + str_arr[str_arr.length - 1]);
					try {
						try {
							Files.copy(source.toPath(), dest.toPath());
							JOptionPane.showMessageDialog(null, "SOP added successfully!");
						} catch (FileAlreadyExistsException dv) {
							JOptionPane.showMessageDialog(null, "SOP Exists!");
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		mnNewMenu_5.add(menuItem_add_sop);

		menuItem_custom_formula = new JMenuItem("Custom Formula");
		menuItem_custom_formula.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String a[] = new String[1];
				// system.out.println("Formula Count = " + formula_cnt);
				a[0] = String.valueOf(formula_cnt);
				new_formula.main(a);
			}
		});
	//	mnNewMenu_5.add(menuItem_custom_formula);

		menuItem_device_data = new JMenuItem("Device Data");
		menuItem_device_data.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				user_device_data scom = new user_device_data();
				scom.setVisible(true);
			}
		});
		mnNewMenu_5.add(menuItem_device_data);

		menuItem_burette = new JMenuItem("Burette Calibration");
		menuItem_burette.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] temp_aa = { "" };
				burette_calibration.main(temp_aa);
				burette_calibration.port_setup_bc(serial_port1);
			}
		});
		mnNewMenu_5.add(menuItem_burette);

		menu_item_comport = new JMenuItem("ComPort");
		menu_item_comport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				select_comport scom = new select_comport();
				scom.setVisible(true);
			}
		});
		mnNewMenu_5.add(menu_item_comport);

		menu_item_logout = new JMenuItem("Log Out");
		menu_item_logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					audit_log_push.push_to_audit(get_date(), get_time(),user_name,frame1.getTitle().toString().toLowerCase().contains("superadmin")? user_name+" - SuperAdmimn - Logged out ":user_name+" - Logged out" );
				} catch (ParseException e1) {e1.printStackTrace();}
				
				enable_all(false);
				enable_table_items(false);
				user_name = "";
				roles_list = "";
				role_items = "";
				menu_item_login.setEnabled(true);
				menu_item_sa_login.setEnabled(true);
				menu_item_logout.setEnabled(false);
				AdminLogin al = new AdminLogin();
				if (checked_vaidity == true) {
					AdminLogin.check_validity = true;
				} else {
					AdminLogin.check_validity = false;
				}
				al.setVisible(true);
				if (frame1.getTitle().toString().contains("ComPort")) {
					frame1.setTitle(
							"Mayura Analytical       Conected to ComPort : " + serial_port1.getDescriptivePortName());
				} else {
					frame1.setTitle("Mayura Analytical");
				}
			}
		});
		mnNewMenu_5.add(menu_item_logout);

		JMenu mnNewMenu_6 = new JMenu("Help");
		mnNewMenu_6.setFont(new Font("Segoe UI", Font.BOLD, 15));
	//	menubar1.add(mnNewMenu_6);
		selected_experiment = "potentiometry";

		initialize();
		enable_all(false);

		if (check_validity()) {
			ScheduledExecutorService exec_temp1 = Executors.newSingleThreadScheduledExecutor();
			exec_temp1.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					if (v == 1) {
						AdminLogin.main(null);
						if (checked_vaidity == true) {
							AdminLogin.check_validity = true;
						} else {
							AdminLogin.check_validity = false;
						}
						exec_temp1.shutdown();
					}
					v++;
				}
			}, 0, 100, TimeUnit.MILLISECONDS);
		} else {
			JOptionPane.showMessageDialog(null,
					"Software's Maximum Validity Limit Reached. Contact Mayura Analytical for more information");
			menu_item_sa_login.setEnabled(false);
		}
	}

	public static boolean check_validity() {
		boolean temp_result = false;
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql;
		sql = "SELECT start_date,validity FROM company_data WHERE Slno = '1'";
		try {
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			String start_date = rs.getString("start_date");
			int valid = Integer.parseInt(rs.getString("validity"));
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			final LocalDate firstDate = LocalDate.parse(rs.getString("start_date"), formatter);
			final LocalDate secondDate = LocalDate.parse(get_date(), formatter);
			final long days = ChronoUnit.DAYS.between(firstDate, secondDate);
			if (days <= valid) {
				temp_result = true;
				checked_vaidity = true;
			} else {
				temp_result = false;
				checked_vaidity = false;

			}
		} catch (SQLException e1) {
			// JOptionPane.showMessageDialog(null, e1);
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e1) {
				// system.out.println(e1.toString());
			}
		}
		try {
			Thread.sleep(50);
		}
		catch (InterruptedException tt) {}
		return temp_result;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
	}

	public static int get_formula_cnt() {
		int count = 0;
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql;
		String res_formula = "";

		try {
			sql = "SELECT * FROM formulas";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				count++;
			}
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, e1);
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e1) {
			}
		}
		return count;
	}

	public static void set_formula(String formula_no) {
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql;
		String res_formula = "";
		try {
			sql = "SELECT formula FROM formulas where (no  = '" + formula_no + "')";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			res_formula = rs.getString("formula");
			String[] res_arr = res_formula.split(",");
			if (res_arr.length == 1) {
				math = res_arr[0];
				math2 = "";
				math3 = "";
			} else if (res_arr.length == 2) {
				math = res_arr[0];
				math2 = res_arr[1];
				math3 = "";
			} else if (res_arr.length == 3) {
				math = res_arr[0];
				math2 = res_arr[1];
				math3 = res_arr[2];
			}
			formula = new TeXFormula(math);
			ti_formula = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 18);
			b_formula = new BufferedImage(ti_formula.getIconWidth(), ti_formula.getIconHeight(),
					BufferedImage.TYPE_4BYTE_ABGR);
			ti_formula.paintIcon(new JLabel(), b_formula.getGraphics(), 0, 0);

			formula2 = new TeXFormula(math2);
			ti_formula2 = formula2.createTeXIcon(TeXConstants.STYLE_DISPLAY, 18);
			b_formula2 = new BufferedImage(ti_formula2.getIconWidth(), ti_formula2.getIconHeight(),
					BufferedImage.TYPE_4BYTE_ABGR);
			ti_formula2.paintIcon(new JLabel(), b_formula2.getGraphics(), 0, 0);

			formula3 = new TeXFormula(math3);
			ti_formula3 = formula3.createTeXIcon(TeXConstants.STYLE_DISPLAY, 18);
			b_formula3 = new BufferedImage(ti_formula3.getIconWidth(), ti_formula3.getIconHeight(),
					BufferedImage.TYPE_4BYTE_ABGR);
			ti_formula3.paintIcon(new JLabel(), b_formula3.getGraphics(), 0, 0);

			l_formula.setIcon(ti_formula);
			l_formula2.setIcon(ti_formula2);
			l_formula3.setIcon(ti_formula3);
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

	public static void enable_all(boolean condition) {
		btn_run_mb.setEnabled(condition);
		btn_open_mb.setEnabled(condition);
		btn_save_mb.setEnabled(condition);
		btn_refresh_mb.setEnabled(condition);
		btn_esc_mb.setEnabled(condition);
		btn_fill_mb.setEnabled(condition);
		btn_dose_mb.setEnabled(condition);
		btn_wash_mb.setEnabled(condition);
		btn_rinse_mb.setEnabled(condition);
		rdbtnNewRadioButton.setEnabled(condition);
		rdbtnNewRadioButton_1.setEnabled(condition);
		rdbtnNewRadioButton_2.setEnabled(condition);
		rdbtnNewRadioButton_3.setEnabled(condition);
		menu_item_log.setEnabled(condition);
		mnNewMenu_7.setEnabled(condition);
		menu_item_view_reports.setEnabled(condition);
		menu_item_exit.setEnabled(condition);
		menu_item_mv_display.setEnabled(condition);
		menu_item_pot.setEnabled(condition);
		menu_item_kf.setEnabled(condition);
		menu_item_ph.setEnabled(condition);
		menu_item_amp.setEnabled(condition);
		menuItem_add_sop.setEnabled(condition);
		menuItem_custom_formula.setEnabled(condition);
		menuItem_device_data.setEnabled(condition);
		menuItem_burette.setEnabled(condition);
		menu_item_comport.setEnabled(condition);
		enable_table_items(condition);
	}

	public static void enable_table_items(boolean condition) {
		pot_tf_predose.setEnabled(condition);
		pot_tf_stirtime.setEnabled(condition);
		pot_tf_maxvol.setEnabled(condition);
		pot_tf_blankvol.setEnabled(condition);
		pot_tf_burette.setEnabled(condition);
		pot_tf_factor1.setEnabled(condition);
		pot_tf_factor2.setEnabled(condition);
		pot_tf_factor3.setEnabled(condition);
		pot_tf_factor4.setEnabled(condition);
		pot_tf_sop_value.setEnabled(condition);
		pot_cb_threshold.setEnabled(condition);
		pot_cb_filter.setEnabled(condition);
		pot_cb_dosagerate.setEnabled(condition);
		pot_cb_nooftrials.setEnabled(condition);
		pot_cb_epselect.setEnabled(condition);
		pot_cb_formula.setEnabled(condition);
		pot_cb_tendency.setEnabled(false);
		pot_cb_resultunit.setEnabled(condition);

		kf_tf_delay.setEnabled(condition);
		kf_tf_stirtime.setEnabled(condition);
		kf_tf_maxvol.setEnabled(condition);
		kf_tf_blankvol.setEnabled(condition);
		kf_tf_burette.setEnabled(condition);
		kf_tf_density.setEnabled(condition);
		kf_tf_factor.setEnabled(condition);
		kf_tf_endpoint.setEnabled(condition);
		kf_tf_sop_value.setEnabled(condition);
		kf_cb_dosagerate.setEnabled(condition);
		kf_cb_nooftrials.setEnabled(condition);
		kf_cb_resultunit.setEnabled(condition);

		ph_tf_stirtime.setEnabled(condition);
		ph_tf_delay.setEnabled(condition);
		ph_tf_predose.setEnabled(condition);
		ph_tf_maxvol.setEnabled(condition);
		ph_tf_blankvol.setEnabled(condition);
		ph_tf_burette.setEnabled(condition);
		ph_tf_endpoint.setEnabled(condition);
		ph_tf_factor1.setEnabled(condition);
		ph_tf_factor2.setEnabled(condition);
		ph_tf_factor3.setEnabled(condition);
		ph_tf_factor4.setEnabled(condition);
		ph_tf_slope1.setEnabled(condition);
		ph_tf_slope2.setEnabled(condition);
		ph_tf_sop_value.setEnabled(condition);
		ph_cb_tendency.setEnabled(condition);
		ph_cb_dosagerate.setEnabled(condition);
		ph_cb_nooftrials.setEnabled(condition);
		ph_cb_calibrate.setEnabled(condition);
		ph_cb_formula.setEnabled(condition);
		ph_cb_resultunit.setEnabled(condition);

		amp_tf_stirtime.setEnabled(condition);
		amp_tf_delay.setEnabled(condition);
		amp_tf_predose.setEnabled(condition);
		amp_tf_maxvol.setEnabled(condition);
		amp_tf_blankvol.setEnabled(condition);
		amp_tf_burette.setEnabled(condition);
		amp_tf_endpoint.setEnabled(condition);
		amp_tf_factor1.setEnabled(condition);
		amp_tf_factor2.setEnabled(condition);
		amp_tf_factor3.setEnabled(condition);
		amp_tf_factor4.setEnabled(condition);
		amp_tf_sop_value.setEnabled(condition);
		amp_cb_filter.setEnabled(condition);
		amp_cb_dosagerate.setEnabled(condition);
		amp_cb_nooftrials.setEnabled(condition);
		amp_cb_formula.setEnabled(condition);
		amp_cb_resultunit.setEnabled(condition);
		btn_save_mb.setEnabled(condition);

	}

	public static void setRole(String u_name, String roles, String items) {

		System.out.println("SETTTT ROLEEEE");
		ReformatBuffer.current_exp = "main";
		user_name = u_name;
		roles_list = roles;
		role_items = items;

		if (frame1.getTitle().toString().contains("ComPort")) {
			frame1.setTitle("Mayura Analytical    Logged in as - " + u_name + "     Conected to ComPort : "
					+ serial_port1.getDescriptivePortName());
		} else {
			frame1.setTitle("Mayura Analytical    Logged in as - " + u_name);
		}

		if (!items.contains("Recall")) {
			menu_item_pot.setEnabled(false);
			menu_item_kf.setEnabled(false);
			menu_item_ph.setEnabled(false);
			menu_item_amp.setEnabled(false);
		}
		if (!items.contains("SOP")) {
			menuItem_add_sop.setEnabled(false);
		}
		if (!items.contains("Burette")) {
			menuItem_burette.setEnabled(false);
		}
		if (!items.contains("Custom")) {
			menuItem_custom_formula.setEnabled(false);
		}
		if (!items.contains("Audit")) {
			menu_item_log.setEnabled(false);
		}
		if (!items.contains("Device")) {
			menuItem_device_data.setEnabled(false);
		}
		if (!items.contains("Method")) {
			enable_table_items(false);
			no_update = true;
		}
		if (!items.contains("Analysis")) {
			enable_table_items(false);
			btn_open_mb.setEnabled(false);
			btn_run_mb.setEnabled(false);
			btn_save_mb.setEnabled(false);
		}
	}

	public static void admin_setRole(String u_name) {
		admin_user_name = u_name;
	}

	public static void send_cvol_kfpot() {
		try {
			Thread.sleep(500);
			ReformatBuffer.current_exp = "main";
			output.print("<8888>CVOL*");
			output.flush();
		} catch (NullPointerException ee) {
		} catch (InterruptedException jb) {
		}
	}
}
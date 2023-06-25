package main.java;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class user_device_data extends JFrame {

	private JPanel contentPane;

	private JLabel instrument_id_heading;
	private JLabel company_logo;
	private JLabel company_name_heading;
	private JLabel company_address_heading;
	private JLabel validity_heading,validity_left_heading;

	private JTextField instrument_id_tf;
	private JTextField company_logo_tf;
	private JTextField company_name_tf;
	private JTextField company_address_tf;
	private JTextField validity_tf;

	private JButton btn_update, btn_browse,btn_create_superAdmin;

	public static String method_data;
	public static String method_name;

	static user_device_data frame;
	static String status = "false";
	static String exp = "";
	static boolean update = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new user_device_data();
					frame.setVisible(true);
					ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
					frame.setIconImage(img.getImage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public user_device_data() {
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		System.out.println("constructor == ");

		setBounds(100, 200, 520, 500);
		setTitle("Input Data");
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel heading = new JLabel("");
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setFont(new Font("Times New Roman", Font.BOLD, 22));
		heading.setBounds(0, 30, 520, 20);
		contentPane.add(heading);

		instrument_id_heading = new JLabel("Instrument ID :");
		instrument_id_heading.setFont(new Font("Times New Roman", Font.BOLD, 18));
		instrument_id_heading.setBounds(15, 80, 139, 20);
		contentPane.add(instrument_id_heading);

		instrument_id_tf = new JTextField();
		instrument_id_tf.setBounds(190, 80, 290, 30);
		contentPane.add(instrument_id_tf);

		company_logo = new JLabel("Company Logo :");
		company_logo.setFont(new Font("Times New Roman", Font.BOLD, 18));
		company_logo.setBounds(15, 130, 139, 20);
		contentPane.add(company_logo);

		company_logo_tf = new JTextField();
		company_logo_tf.setBounds(190, 130, 290, 30);
		contentPane.add(company_logo_tf);

		company_name_heading = new JLabel("Company Name :");
		company_name_heading.setFont(new Font("Times New Roman", Font.BOLD, 18));
		company_name_heading.setBounds(15, 230, 160, 21);
		contentPane.add(company_name_heading);

		company_name_tf = new JTextField();
		company_name_tf.setBounds(190, 230, 290, 30);
		contentPane.add(company_name_tf);

		company_address_heading = new JLabel("Company Address :");
		company_address_heading.setFont(new Font("Times New Roman", Font.BOLD, 18));
		company_address_heading.setBounds(15, 280, 180, 21);
		contentPane.add(company_address_heading);

		company_address_tf = new JTextField();
		company_address_tf.setBounds(190, 280, 290, 30);
		contentPane.add(company_address_tf);

		validity_left_heading = new JLabel("Days left :");
		validity_left_heading.setFont(new Font("Times New Roman", Font.BOLD, 17));
		validity_left_heading.setBounds(190, 335, 139, 21);
	//	contentPane.add(validity_left_heading);
		
		btn_browse = new JButton("Browse");
		btn_browse.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btn_browse.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn_browse.setBounds(280, 180, 100, 30);
		contentPane.add(btn_browse);

		btn_browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");

				int userSelection = fileChooser.showOpenDialog(null);

				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToSave = fileChooser.getSelectedFile();

					try {
						company_logo_tf.setText(fileToSave.getAbsolutePath());
						System.out.println("Save as file: " + fileToSave.getAbsolutePath());
					} catch (NullPointerException eee) {
					}
				}
			}
		});


		
		
		btn_update = new JButton("");
		btn_update.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btn_update.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn_update.setBounds(220, 410, 100, 32);
		contentPane.add(btn_update);
		

		btn_update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String inst_id = instrument_id_tf.getText();
				String comp_logo = company_logo_tf.getText();
				String comp_name = company_name_tf.getText();
				String comp_address = company_address_tf.getText();

				if (inst_id.matches("") || comp_logo.matches("") || comp_name.matches("") || comp_address.matches("")
						) {
					JOptionPane.showMessageDialog(null, "Fill all the data!");
				} else {
					try {
//						int val = Integer.parseInt(valid);
//						if (val <= 0 || val > 20000) {
//							JOptionPane.showMessageDialog(null, "Validity must be in days and between 1 to 20000!");
//
//						} else {

							File source17 = new File(company_logo_tf.getText().toString());
							File dest17 = new File("C:\\SQLite\\company_logo\\logo.png");

							try {

								Files.copy(source17.toPath(), dest17.toPath());
							} catch (FileAlreadyExistsException ds) {
								dest17.delete();
								try {
									Files.copy(source17.toPath(), dest17.toPath());
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							} catch (IOException e1) {
								e1.printStackTrace();
							}

							Connection con = DbConnection.connect();
							PreparedStatement ps = null;
							try {
								String sql = null;
								if (update == false) {
									sql = "INSERT INTO company_data(Slno,instrument_id, company_logo,company_name,company_address,start_date,validity) VALUES(?,?,?,?,?,?,?)";
									ps = con.prepareStatement(sql);
									ps.setString(1, "1");
									ps.setString(2, inst_id);
									ps.setString(3, "C:\\SQLite\\company_logo\\logo.png");
									ps.setString(4, comp_name);
									ps.setString(5, comp_address);
									ps.setString(6, get_date());
									ps.execute();
								} else {
									sql = "UPDATE company_data SET instrument_id = ?,company_logo = ?,company_name=?,company_address=? WHERE Slno = ?";

									ps = con.prepareStatement(sql);
									ps.setString(1, inst_id);
									ps.setString(2, "C:\\SQLite\\company_logo\\logo.png");
									ps.setString(3, comp_name);
									ps.setString(4, comp_address);
									ps.setString(5, "1");
									ps.executeUpdate();
								}
								dispose();
								JOptionPane.showMessageDialog(null, "Saved Successfully");
							} catch (SQLException e1) {
								System.out.println(e1.toString());
							} finally {
								try {
									ps.close();
									con.close();
								} catch (SQLException e1) {
									System.out.println(e1.toString());
								}
							//}

							dispose();
						}
					} catch (NumberFormatException nff) {
						JOptionPane.showMessageDialog(null, "Validity must be in days (number)!");

					}

				}
			}
		});

		String[] data_arr = get_data().split(">");
		if (data_arr.length > 1) {
			instrument_id_tf.setText(data_arr[0]);
			company_logo_tf.setText(data_arr[1]);
			company_name_tf.setText(data_arr[2]);
			company_address_tf.setText(data_arr[3]);
			int temp = Integer.parseInt(data_arr[4]) - Integer.parseInt(data_arr[5]);
			//validity_left_heading.setText("Days left : "+temp);
			update = true;
			btn_update.setText("Update");
			heading.setText("Update Company Data");
		} else {
			btn_update.setText("Create");
			heading.setText("Create Company Data");
		}
	}

	public static String get_date() {
		DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		String date_time = dateFormat2.format(new Date()).toString();
		return date_time;
	}

	public static String get_data() {
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
			data = data + ">" + rs.getString("company_logo");
			data = data + ">" + rs.getString("company_name");
			data = data + ">" + rs.getString("company_address");
			data = data + ">" + rs.getString("validity");
			
			int valid = Integer.parseInt(rs.getString("validity"));
			
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			final LocalDate firstDate = LocalDate.parse(rs.getString("start_date"), formatter);
			final LocalDate secondDate = LocalDate.parse(get_date(), formatter);
			final long days = ChronoUnit.DAYS.between(firstDate, secondDate);
			data = data + ">" + days;

			
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
	public static String get_permission() {
    	String temp_result = "";
    	Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql ;
		sql = "SELECT * FROM config_param WHERE cnfg_param_group = 'trials_altering' and cnfg_param_name = 'permission_to_alter_trial'";
				try {
					ps = con.prepareStatement(sql);
					ResultSet rs = ps.executeQuery();
					temp_result = rs.getString("cnfg_param_value");
				}
				catch(SQLException e1) {
					JOptionPane.showMessageDialog(null,e1);
				}
				finally {
				    try{
				    ps.close();
				    con.close();
				    } catch(SQLException e1) {
				      System.out.println(e1.toString());
				    }
				}
    	return temp_result;
    }
}
package main.java;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.regex.Pattern;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Matcher;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

//import Login.trial;

//import login.trial;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.HeadlessException;

import javax.swing.JTextField;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class AdminLogin extends JFrame implements ActionListener, KeyListener {

	private JPanel contentPane;
	private JTextField user;
	private JTextField password;
	static AdminLogin frame;

	static boolean check_validity = false;
	
	static String audit_user="",audit_text="";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new AdminLogin();
					frame.setVisible(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		if (s.equals("Click here")) {
			JOptionPane.showMessageDialog(null, "Your form has been sent");
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			JOptionPane.showMessageDialog(null, "Your form has been sent");
		}
	}

	@Override
	public void keyReleased(KeyEvent arg) {
	}

	@Override
	public void keyTyped(KeyEvent arg) {
	}

	public AdminLogin() {
		setBounds(100, 200, 400, 220);
		setTitle("Login");
		setLocationRelativeTo(null);
		
		ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
		setIconImage(img.getImage());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Username   :");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel.setBounds(20, 25, 139, 21);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Password   :");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel_1.setBounds(20, 70, 139, 13);
		contentPane.add(lblNewLabel_1);

		user = new JTextField();
		user.setBounds(150, 25, 200, 20);
		contentPane.add(user);
		user.setColumns(10);

		password = new JPasswordField();
		password.setBounds(150, 70, 200, 20);
		contentPane.add(password);
		password.setColumns(10);

		JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String un = user.getText();
				String pas = password.getText();
				// try {
				if (un.equals("serviceengineer") && pas.matches("mayura123#")) {
					audit_user = "Service Engineer";
					audit_text = "Service Engineer Logged-In";
					
					menubar.enable_all(true);
					menubar.user_name = "Service Engineer";
					menubar.menu_item_login.setEnabled(false);
					menubar.menu_item_sa_login.setEnabled(false);
					menubar.menu_item_logout.setEnabled(true);
					
					if (menubar.frame1.getTitle().toString().contains("ComPort")) {
						menubar.frame1.setTitle("Mayura Analytical    Logged in as - Service Engineer    Conected to ComPort : "
								+ menubar.serial_port1.getDescriptivePortName());
					} else {
						menubar.frame1.setTitle("Mayura Analytical    Logged in as - Service Engineer");
					}
					
					device_data.main(null);
					dispose();
				} else if (check_validity == false) {
					JOptionPane.showMessageDialog(null,
							"Software's Maximum Validity Limit Reached. Contact Mayura Analytical for more information");
					audit_user = "";
					audit_text = "Software's Maximum Validity Limit Reached";
				} else if (check_validity == true) {
					Connection con = DbConnection.connect();
					PreparedStatement ps = null;
					ResultSet rs = null;
					String det_items = "";

					String Pass = password.getText();
					try {
						String sql = "SELECT Password,created_date,validity,Roles FROM UserLogin where Username='"
								+ user.getText() + "'";
						ps = con.prepareStatement(sql);
						rs = ps.executeQuery();

						if (Pass.equals(rs.getString("Password"))) {

							final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
							final LocalDate firstDate = LocalDate.parse(rs.getString("created_date"), formatter);
							final LocalDate secondDate = LocalDate.parse(get_date(), formatter);
							final long days = ChronoUnit.DAYS.between(firstDate, secondDate);
							System.out.println("Days between: " + days);

							if (days <= Integer.valueOf(rs.getString("validity"))) {

								String[] temp_arr = rs.getString("Roles").split(",");
								for (int i = 0; i < temp_arr.length; i++) {
									sql = "SELECT Items FROM Roles where RoleName ='" + temp_arr[i] + "'";
									ps = con.prepareStatement(sql);
									ResultSet rs1 = ps.executeQuery();

									if (i == 0) {
										det_items = det_items + rs1.getString("Items");
									} else {
										det_items = det_items + "," + rs1.getString("Items");
									}
								}
								audit_user = user.getText();
								audit_text = user.getText()+" - Logged-In";
								menubar.menu_item_login.setEnabled(false);
								menubar.menu_item_sa_login.setEnabled(false);
								menubar.menu_item_logout.setEnabled(true);

								menubar.enable_all(true);
								menubar.setRole(user.getText().toString(), rs.getString("Roles"), det_items);
								menubar.menu_item_sa_login.setEnabled(true);

								dispose();
							} else {
								audit_user = user.getText();
								audit_text = user.getText()+" - User Validity Expired";
								JOptionPane.showMessageDialog(null, "User Validity Expired!. Kindly contact Admin.");
							}
						} else {
							JOptionPane.showMessageDialog(null, "Invalid Username or password");
							audit_user = user.getText();
							audit_text = user.getText()+" - Failed Login";
						}

					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null, "Login UnSuccessful!");
						audit_user = user.getText();
						audit_text = user.getText()+" - Failed Login";
					} finally {
						try {
							ps.close();
							con.close();
						} catch (SQLException e1) {
							System.out.println(e1.toString());
						}
					}
				}

				// }
//				} catch (InvalidKeyException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (HeadlessException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (NoSuchAlgorithmException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (NoSuchPaddingException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (IllegalBlockSizeException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (BadPaddingException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				
				try {
					audit_log_push.push_to_audit(get_date(), get_time(),audit_user,audit_text);
				} catch (ParseException e1) {e1.printStackTrace();}
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnNewButton.setBounds(150, 120, 104, 37);
		contentPane.add(btnNewButton);
	}

	public static boolean check_password_dev(String pass) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		String dec_output = decrypt(pass, "Dfs4#6PtBN");
		if (dec_output.matches("Mayura12#")) {
			return true;
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

	public static String decrypt(String encryptedtext, String key) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		byte[] KeyData = key.getBytes();
		SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
		byte[] ecryptedtexttobytes = Base64.getDecoder().decode(encryptedtext);
		Cipher cipher = Cipher.getInstance("Blowfish");
		cipher.init(Cipher.DECRYPT_MODE, KS);
		byte[] decrypted = cipher.doFinal(ecryptedtexttobytes);
		String decryptedString = new String(decrypted, Charset.forName("UTF-8"));
		return decryptedString;

	}
}
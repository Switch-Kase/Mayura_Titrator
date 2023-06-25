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
import javax.swing.JDialog;

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

public class SuperAdminLogin extends JDialog{

	private JPanel contentPane;
	private JTextField user;
	private JTextField password;
	static SuperAdminLogin frame;
	boolean auth = false;
	
	static String audit_user="",audit_text="";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new SuperAdminLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void actionPerformed(ActionEvent e) 
    {
        String s = e.getActionCommand(); 
        if(s.equals("Click here")){
            JOptionPane.showMessageDialog(null , "Your form has been sent");
        } 
    } 
	  public void keyPressed(KeyEvent e) {
	    if (e.getKeyCode()==KeyEvent.VK_ENTER){
	      JOptionPane.showMessageDialog(null , "Your form has been sent");
	    }
	  }

	public SuperAdminLogin() {
		setBounds(100, 200, 400, 220);
		setTitle("SuperAdmin Login");
		setLocationRelativeTo(null);
		setResizable(false);

		setModal(true);

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
		
		password= new JPasswordField();
		password.setBounds(150, 70, 200, 20);
		contentPane.add(password);
		password.setColumns(10);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Connection con = DbConnection.connect();
				PreparedStatement ps = null;
				ResultSet rs = null;
				String Pass = password.getText();
				try {
					String sql = "SELECT Username, Password FROM SuperAdminLogin where Username='"+ user.getText()+ "'";
					ps = con.prepareStatement(sql);
					rs = ps.executeQuery();
					
					if(Pass.equals(rs.getString("Password"))) {
						menubar.user_name = user.getText().toString();
						menubar.admin_setRole(user.getText().toString());
						menubar.enable_all(true);
						menubar.menu_item_login.setEnabled(false);
						menubar.menu_item_logout.setEnabled(true);
						menubar.menu_item_sa_login.setEnabled(false);
						if(menubar.frame1.getTitle().toString().contains("ComPort"))
							menubar.frame1.setTitle("Mayura Analytical   Logged in as SuperAdmin : "+user.getText()+ "     Conected to ComPort : "+ menubar.serial_port1.getDescriptivePortName());
						else
							menubar.frame1.setTitle("Mayura Analytical   Logged in as SuperAdmin : "+user.getText());
						dispose();
						auth = true;
						audit_user = user.getText().toString();audit_text = user.getText().toString()+" - SuperAdmin - Logged-In";
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Invalid Username or password");
						audit_user = user.getText().toString();audit_text = user.getText().toString()+" - SuperAdmin - Log-In Failed";
						//dispose();
						auth = false;
					}
					ps.close();
					rs.close();
					if(auth) {
						users_roles.main(null);
					}
				}
				catch(SQLException e1) {
					JOptionPane.showMessageDialog(null,"Login Unsuccessful!");
					audit_user = user.getText().toString();audit_text = "SuperAdmin - Log-In Unsuccessful";

				}
				try {
					audit_log_push.push_to_audit(get_date(), get_time(),audit_user,audit_text);
				} catch (ParseException e1) {e1.printStackTrace();}
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnNewButton.setBounds(150, 120, 104, 37);
		contentPane.add(btnNewButton);
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
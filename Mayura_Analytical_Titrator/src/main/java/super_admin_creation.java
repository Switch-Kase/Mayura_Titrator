package main.java;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.regex.Pattern;
import java.util.Base64;
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
import javax.swing.JCheckBox;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class super_admin_creation extends JDialog{

	private JPanel contentPane;
	private JTextField user;
	private JTextField password;
	private JTextField confirm_password;
	static super_admin_creation frame;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new super_admin_creation();
					frame.setVisible(true);
					ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
					frame.setIconImage(img.getImage());

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
	  
	public super_admin_creation() {
		setBounds(100, 200, 400, 280);
		setTitle("Create Super Admin");
		setLocationRelativeTo(null);
		setResizable(false);
		setModal(true);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Username   :");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblNewLabel.setBounds(20, 25, 139, 21);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Password   :");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblNewLabel_1.setBounds(20, 70, 139, 13);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Confirm Password   :");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblNewLabel_2.setBounds(20, 115, 180, 13);
		contentPane.add(lblNewLabel_2);
		
		user = new JTextField();
		user.setBounds(170, 25, 180, 20);
		contentPane.add(user);
		user.setColumns(10);
		
		password= new JPasswordField();
		password.setBounds(170, 70, 180, 20);
		contentPane.add(password);
		password.setColumns(10);
		
		confirm_password= new JPasswordField();
		confirm_password.setBounds(170, 115, 180, 20);
		contentPane.add(confirm_password);
		confirm_password.setColumns(10);
		
		
		 
		JButton btnNewButton = new JButton("Create");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String un=user.getText();
				String pas=password.getText();
				String confirm_pas=confirm_password.getText();
				if(check_superadmin_name(un)) {
					if(!un.matches("") && !pas.matches("") && !confirm_pas.matches("")) {
						if(pas.matches(confirm_pas) ) {
							Connection con = DbConnection.connect();
							PreparedStatement ps = null;
							try {
								String sql = "INSERT INTO SuperAdminLogin(Username, Password) VALUES(?,?)";
								System.out.println("Checking");
								ps = con.prepareStatement(sql);
								ps.setString(1, un);
								ps.setString(2, pas);
								ps.execute();
								
								System.out.println("Data Inserted!");
							}
							catch(SQLException eh) {System.out.println(eh.toString());}
							finally {
							    try{
							    ps.close();
							    con.close();
								JOptionPane.showMessageDialog(null, "SuperAdmin Successfully Created!");
								frame.dispose();
							    } catch(SQLException eugy) {System.out.println(eugy.toString());}
							}
						}
						else{JOptionPane.showMessageDialog(null, "Password doesn't match!");}
					}
					else {JOptionPane.showMessageDialog(null, "Enter Username or Password!");}
				}
				else {
					JOptionPane.showMessageDialog(null, "Username Exists!");
				}
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnNewButton.setBounds(150, 195, 104, 30);
		contentPane.add(btnNewButton);
	}
	public boolean check_superadmin_name(String name) {
		boolean unique = true;
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql ;
		try {
			sql = "SELECT Username FROM SuperAdminLogin";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) 
			{
				if(name.matches(rs.getString("Username"))) {
					unique = false;
					break;
				}
			}						
		}
		catch(SQLException e1) {
			JOptionPane.showMessageDialog(null,e1);
		}
		finally {
		    try{
		    ps.close();
		    con.close();
		    } catch(SQLException e1) {
		    }
		}
		return unique;
	}
	
}
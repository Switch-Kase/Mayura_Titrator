package main.java;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

//import Login.trial;

//import login.trial;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionEvent;
import java.awt.Desktop;  


public class rename_metd_name extends JFrame {

	private static JPanel contentPane;
	private JTextField method;
	static rename_metd_name frame;
	static String method_name="";
	static String data="";
	static String exp="";
	static JButton btn_save,btn_open;
	static String[] data_arr;
	boolean exists = false;
	static String[] exists_arr;
	ResultSet rs1;
	String final_name="";
	JLabel lblNewLabel2;
	public static void main(String[] args) {
		
		if(args.length != 0) {
		exp=args[0];
		method_name=args[1];
		}
		System.out.println("data = "+data+" -- method_name = "+method_name+" -- exp = "+exp);
	
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new rename_metd_name();
					frame.setVisible(true);
					ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
					frame.setIconImage(img.getImage());

					//frame.dispose();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});		
	}

	/**
	 * Create the frame for Developer Login
	 */
	
	public void keyPressed(KeyEvent e) {
	    if (e.getKeyCode()==KeyEvent.VK_ENTER){
	        System.out.println("Hello");
	        JOptionPane.showMessageDialog(null , "You've Submitted the name ");
	    }

	}
	
	public rename_metd_name() {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 200, 520, 280);
		setTitle("Enter the Method Name");
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	
		JLabel lblNewLabel1 = new JLabel("Current Method Name   :");
		lblNewLabel1.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel1.setBounds(20, 40, 250, 21);
		contentPane.add(lblNewLabel1);
		
		lblNewLabel2 = new JLabel();
		lblNewLabel2.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel2.setBounds(250, 40, 139, 21);
		contentPane.add(lblNewLabel2);
		lblNewLabel2.setText(method_name);
		
		JLabel lblNewLabel = new JLabel("New Method Name   :");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel.setBounds(20, 110, 200, 21);
		contentPane.add(lblNewLabel);
		
		method = new JTextField();
		method.setBounds(220, 110, 280, 30);
		contentPane.add(method);
		method.setColumns(10);
		if(method_name != null) {
			method.setText(method_name);
		}

		btn_save = new JButton("Update Method Name");
		btn_save.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btn_save.setBounds(120, 180, 250, 37);
		contentPane.add(btn_save);
		btn_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String mn=method.getText();
				System.out.println("Method Name="+mn+".....");
				if(!mn.matches(""))
				{
					
				Connection con = DbConnection.connect();
				PreparedStatement ps = null;
				try 
				{
					  
					String sql = null;
					if(exp.matches("Potentiometry")) 
					{
						sql = "UPDATE pot_method SET Trial_name = ? WHERE Trial_name = ?";
					}
					else if(exp.matches("Amperometry"))
					{
						sql = "UPDATE amp_method SET Trial_name = ? WHERE Trial_name = ?";
					}
					else if(exp.matches("Ph"))
					{
						sql = "UPDATE ph_method SET Trial_name = ? WHERE Trial_name = ?";
					}
					else {
						sql = "UPDATE kf_method SET Trial_name = ? WHERE Trial_name = ?";
					}
					
					System.out.println("Checking");
					ps = con.prepareStatement(sql);
					ps.setString(1, mn);
					ps.setString(2, method_name);
					ps.executeUpdate();
					menubar.metd_header.setText("Method Name : "+mn);
					dispose();
				}
				catch(SQLException e1) {
					JOptionPane.showMessageDialog(null,e1);
				}
				finally {
				    try{
				    ps.close();
				    con.close();
				    } 
				    catch(SQLException e1) 
				    {
				      System.out.println(e1.toString());
				    }
				}
			
				}
				else {
					JOptionPane.showMessageDialog(null, "Please enter the new method name!");
				}
				
			}
		});
		
		
		
		
	}
}
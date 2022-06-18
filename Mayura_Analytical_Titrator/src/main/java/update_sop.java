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


public class update_sop extends JFrame {

	private static JPanel contentPane;
	static JLabel sop;
	static update_sop frame;
	static String method_name="";
	static String exp="";
	static JButton btn_save,btn_open;
	static JComboBox cb_sop;
	static String[] data_arr;
	boolean exists = false;
	static String[] exists_arr;
	ResultSet rs1;
	String final_name="",res="";
	static JLabel exp_name_header,menthod_name_header,exp_name,menthod_name;
	
	
	
	public static void main(String[] args) {
		
		if(args.length != 0) {
		exp=args[0];
		method_name=args[1];
		}
	//	System.out.println("data = "+data+" -- method_name = "+method_name+" -- exp = "+exp);
	
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new update_sop();
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
	
	public update_sop() {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 200, 520, 450);
		setTitle("Update SOP");
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	
		exp_name_header = new JLabel("Experiment :");
		exp_name_header.setFont(new Font("Times New Roman", Font.BOLD, 18));
		exp_name_header.setBounds(20, 20, 139, 21);
		contentPane.add(exp_name_header);
		
		menthod_name_header = new JLabel("Method Name   :");
		menthod_name_header.setFont(new Font("Times New Roman", Font.BOLD, 18));
		menthod_name_header.setBounds(180, 20, 139, 21);
		contentPane.add(menthod_name_header);
		
		exp_name = new JLabel(exp);
		exp_name.setFont(new Font("Times New Roman", Font.BOLD, 18));
		exp_name.setBounds(20, 60, 139, 21);
		contentPane.add(exp_name);
		
		menthod_name = new JLabel(method_name);
		menthod_name.setFont(new Font("Times New Roman", Font.BOLD, 18));
		menthod_name.setBounds(180, 60, 139, 21);
		contentPane.add(menthod_name);
		
		sop = new JLabel("Standard Operating Procedure - SOP");
		sop.setFont(new Font("Times New Roman", Font.BOLD, 18));
		sop.setBounds(95, 100, 375, 21);
		contentPane.add(sop);
		
		
		int k=1;
		
		File folder = new File("C:\\SQLite\\SOP");
		File[] listOfFiles = folder.listFiles();

		String[] sop_files_arr=new String[listOfFiles.length+1];
		sop_files_arr[0] = "Not Selected";
		
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile())
		  {
			sop_files_arr[k] = listOfFiles[i].getName();  
			k++;
		   // System.out.println(listOfFiles[i].getName());
		  } 
		  else if (listOfFiles[i].isDirectory())
		  {
		   // System.out.println("Directory " + listOfFiles[i].getName());
		  }
		}
		
		cb_sop = new JComboBox(sop_files_arr);
		cb_sop.setBounds(20, 150, 465, 30);
		contentPane.add(cb_sop);
		try {
		cb_sop.setSelectedItem(data_arr[data_arr.length-1]);
		}
		catch(NullPointerException e) {
			
		}
		
		btn_save = new JButton("Update");
		btn_save.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btn_save.setBounds(100, 350, 104, 37);
		contentPane.add(btn_save);
		btn_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Connection con1 = DbConnection.connect();
				PreparedStatement ps1 = null;
				ResultSet rs1 = null;
				String sql1 ;
				res="";
				try {
					if(exp.matches("Potentiometry")) {
						sql1 = "SELECT Value FROM pot_method where (Trial_name  = '"+method_name+"')";
					}
					else if(exp.matches("Ph")) {
						sql1 = "SELECT Value FROM ph_method where (Trial_name  = '"+method_name+"')";
					}
					else if(exp.matches("Amperometry")) {
						sql1 = "SELECT Value FROM amp_method where (Trial_name  = '"+method_name+"')";
					}
					else {
						sql1 = "SELECT Value FROM kf_method where (Trial_name  = '"+method_name+"')";
					}
					ps1 = con1.prepareStatement(sql1);
					rs1 = ps1.executeQuery();
					
					System.out.println("got got got"+rs1.getString("Value"));
					res=rs1.getString("Value");
				}
					catch(SQLException e1) {
						JOptionPane.showMessageDialog(null,e1);
					}
					finally {
					    try{
					    ps1.close();
					    con1.close();
					    } catch(SQLException e1) {
					      System.out.println(e1.toString());
					    }
					 }	 
				
				String[] arr_temp = res.split(",");
				
				for(int i=0;i<(arr_temp.length-1);i++) {
					final_name = final_name+arr_temp[i]+",";
				}
				
				final_name = final_name+cb_sop.getSelectedItem().toString();
				
					
				
				Connection con = DbConnection.connect();
				PreparedStatement ps = null;
				try 
				{
					String sql = null;
					if(exp.matches("Potentiometry")) 
					{
						//sql = "INSERT INTO pot_method(Value) VALUES(?)";
						sql = "UPDATE pot_method SET Value = ? WHERE Trial_name = ?";
					}
					else if(exp.matches("Amperometry"))
					{
						//sql = "INSERT INTO amp_method(Value) VALUES(?)";
					    //sql = "UPDATE amp_method SET Value = ? , "+ "WHERE Trial_name = ?";
						sql = "UPDATE amp_method SET Value = ? WHERE Trial_name = ?";
					}
					else if(exp.matches("Ph"))
					{
						//sql = "INSERT INTO ph_method(Value) VALUES(?)";
						//sql = "UPDATE ph_method SET Value = ? , "+ "WHERE Trial_name = ?";
						sql = "UPDATE ph_method SET Value = ? WHERE Trial_name = ?";
					}
					else {
						//sql = "INSERT INTO kf_method(Value) VALUES(?)";
						//sql = "UPDATE kf_method SET Value = ? , "+ "WHERE Trial_name = ?";
						sql = "UPDATE kf_method SET Value = ? WHERE Trial_name = ?";
					}
							
					System.out.println("Checking");
					ps = con.prepareStatement(sql);
					ps.setString(1, final_name);
					ps.setString(2, method_name);
					
					ps.executeUpdate();
					
					menubar.res = final_name;
					JOptionPane.showMessageDialog(null, "Updated Successfully");
					
					
					if(exp.matches("Potentiometry")) {
						menubar.pot_tf_sop_value.setText(cb_sop.getSelectedItem().toString());
					}
					else if(exp.matches("Amperometry")) {
						menubar.amp_tf_sop_value.setText(cb_sop.getSelectedItem().toString());
					}
					else if(exp.matches("Ph")) {
						menubar.ph_tf_sop_value.setText(cb_sop.getSelectedItem().toString());
					}
					else {
						menubar.kf_tf_sop_value.setText(cb_sop.getSelectedItem().toString());
					}
					System.out.println("Data Inserted!");
					dispose();

				}
				catch(SQLException e1) {
					System.out.println(e1.toString());
				}//always remember to close database connections
				finally {
				    try{
				    ps.close();
				    con.close();
				    } catch(SQLException e1) {
				      System.out.println(e1.toString());
				    }
				}		
			}
		});
		
		btn_open = new JButton("Open SOP");
		btn_open.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btn_open.setBounds(300, 350, 130, 37);
		contentPane.add(btn_open);
		btn_open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//cb_sop.getSelectedItem().toString()
				try  
				{
				File file = new File("C:\\SQLite\\SOP\\"+cb_sop.getSelectedItem().toString());   
					if(!Desktop.isDesktopSupported())
					{  
					System.out.println("not supported");  
					return;  
					}  
				Desktop desktop = Desktop.getDesktop();  
				if(file.exists())         
				desktop.open(file); 
				}  
				catch(Exception ee)  
				{  
				ee.printStackTrace();  
				} 
				
				
			}
		});
		
		
	}
}
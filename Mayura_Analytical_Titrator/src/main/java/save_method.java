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


public class save_method extends JFrame {

	private static JPanel contentPane;
	private JTextField method;
	static JLabel sop;
	static save_method frame;
	static String method_name=null;
	static String data="";
	static String exp="";
	static JButton btn_save,btn_open;
	static JComboBox cb_sop;
	static String[] data_arr;
	boolean exists = false;
	static String[] exists_arr;
	ResultSet rs1;
	String final_name="";
	
	public static void main(String[] args) {
		
		if(args.length != 0) {
		data=args[0];
		data_arr = data.split(",");
		method_name=args[1];
		exp=args[2];
		}
		//System.out.println("data = "+data+" -- method_name = "+method_name+" -- exp = "+exp);
	
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new save_method();
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
	
	public save_method() {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//System.out.println("MMMMMMMMethod Name  = "+method_name);
		setBounds(100, 200, 520, 450);
		setTitle("Enter the Method Name");
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	
		JLabel lblNewLabel = new JLabel("Method Name   :");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel.setBounds(20, 40, 139, 21);
		contentPane.add(lblNewLabel);
		
		method = new JTextField();
		method.setBounds(180, 40, 300, 30);
		contentPane.add(method);
		method.setColumns(10);
		
		
		if(method_name != null ) {
			try {
			if(!method_name.matches("")) {
			method.setText(method_name);
			
			JButton btn_update_data = new JButton("Update Method");
			btn_update_data.setFont(new Font("Times New Roman", Font.BOLD, 18));
			btn_update_data.setBounds(155, 350, 180, 37);
			contentPane.add(btn_update_data);
			btn_update_data.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					//data_arr
					String final_name1="";
					
				    for(int i=0;i<(data_arr.length-1);i++) {
						final_name1 = final_name1.concat(data_arr[i]);
						final_name1 = final_name1.concat(",");
					}
					final_name1 = final_name1.concat(cb_sop.getSelectedItem().toString());
					
					Connection con = DbConnection.connect();
					PreparedStatement ps = null;
					try 
					{
						String sql = null;
						if(exp.matches("pot")) 
						{
							sql = "UPDATE pot_method SET Value = ? WHERE Trial_name = ?";
						}
						else if(exp.matches("amp"))
						{
							sql = "UPDATE amp_method SET Value = ? WHERE Trial_name = ?";
						}
						else if(exp.matches("ph"))
						{
							sql = "UPDATE ph_method SET Value = ? WHERE Trial_name = ?";
						}
						else {
							sql = "UPDATE kf_method SET Value = ? WHERE Trial_name = ?";
						}
								
						System.out.println("Checking");
						ps = con.prepareStatement(sql);
						ps.setString(1, final_name1);
						ps.setString(2, method_name);
						
						ps.executeUpdate();
						
						if(exp.matches("pot")) 
						{
							menubar.pot_tf_sop_value.setText(cb_sop.getSelectedItem().toString());
						}
						else if(exp.matches("amp"))
						{
							menubar.amp_tf_sop_value.setText(cb_sop.getSelectedItem().toString());
						}
						else if(exp.matches("ph"))
						{
							menubar.ph_tf_sop_value.setText(cb_sop.getSelectedItem().toString());
						}
						else {
							menubar.kf_tf_sop_value.setText(cb_sop.getSelectedItem().toString());
						}
						
						menubar.res = final_name;
						
						JOptionPane.showMessageDialog(null, "Updated Successfully");
						menubar.metd_header.setText("Method Name : "+method_name);
						//menubar.saved_file=true;
						menubar.saved_file_name = method_name;
						menubar.res = final_name1;
						dispose();
					}
					catch(SQLException e1) {
						System.out.println(e1.toString());
					}
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
		}
			}
			catch(NullPointerException eee) {
				
			}
			
			
		}
		
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
		
		btn_save = new JButton("Save");
		btn_save.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btn_save.setBounds(25, 350, 104, 37);
		contentPane.add(btn_save);
		btn_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String mn=method.getText();
				System.out.println("Method Name="+mn+".....");
				if(!mn.matches(""))
				{
					
					Connection con1 = DbConnection.connect();
					PreparedStatement ps1 = null;
					rs1 = null;
					String sql1 ;
					try {
						if(exp.matches("pot")) {
							sql1 = "SELECT Trial_name FROM pot_method";
						}
						else if(exp.matches("amp")) {
							sql1 = "SELECT Trial_name FROM amp_method";
						}
						else if(exp.matches("ph")){
							sql1 = "SELECT Trial_name FROM ph_method";
						}
						else {
							sql1 = "SELECT Trial_name FROM kf_method";
						}
						ps1 = con1.prepareStatement(sql1);
						rs1 = ps1.executeQuery();

						int i=0;
						 while (rs1.next()) {
							// exists_arr[i] = rs1.getString("Trial_name");
							 System.out.println("Inside = "+rs1.getString("Trial_name") );//+ " ==arr== "+exists_arr[i]
							 i++;
							 final_name = final_name+rs1.getString("Trial_name") +",";
						 }
						 
					}
					catch(SQLException e1) {
						JOptionPane.showMessageDialog(null,e1);
					}
					finally {
					    try{
					    ps1.close();
					    con1.close();
					    } 
					    catch(SQLException e1) 
					    {
					      System.out.println(e1.toString());
					    }
					}
					
					System.out.println("Final Name"+final_name);
					
					exists_arr = final_name.split(","); 
					
					for(int i=0;i<exists_arr.length;i++) {
						System.out.println("MN = "+mn);
						if(mn.matches(exists_arr[i]))
						{
							exists = true;
							break;
						}
					}
					
				if(exists == false) {	
				Connection con = DbConnection.connect();
				PreparedStatement ps = null;
				try 
				{
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
					LocalDateTime now = LocalDateTime.now();  
					System.out.println(dtf.format(now));  
					String sql = null;
					if(exp.matches("pot")) 
					{
						sql = "INSERT INTO pot_method(Trial_name, Date,Value) VALUES(?,?,?)";
					}
					else if(exp.matches("amp"))
					{
						sql = "INSERT INTO amp_method(Trial_name, Date,Value) VALUES(?,?,?)";
					}
					else if(exp.matches("ph"))
					{
						sql = "INSERT INTO ph_method(Trial_name, Date,Value) VALUES(?,?,?)";
					}
					else {
						sql = "INSERT INTO kf_method(Trial_name, Date,Value) VALUES(?,?,?)";
					}
										
					String data_final="";
					
					for(int j=0;j<(data_arr.length-1);j++) {
						data_final = data_final.concat(data_arr[j]);
						data_final = data_final.concat(",");
					}
					
					data_final = data_final+cb_sop.getSelectedItem().toString();
					
					System.out.println("datafinal = "+data_final);
					
					System.out.println("Checking");
					ps = con.prepareStatement(sql);
					ps.setString(1, mn);
					ps.setString(2, String.valueOf(dtf.format(now)));
					ps.setString(3, data_final);
					ps.execute();
					menubar.metd_header.setText("Method Name : "+mn);
					dispose();
					//JOptionPane.showOptionDialog(null, "Hello","Empty?", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null);
					JOptionPane.showMessageDialog(null, "Saved Successfully");
					menubar.saved_file=true;
					menubar.saved_file_name = mn;
					if(exp.matches("pot")) {
						menubar.pot_tf_sop_value.setText(cb_sop.getSelectedItem().toString());
					}
					else if(exp.matches("amp")) {
						menubar.amp_tf_sop_value.setText(cb_sop.getSelectedItem().toString());
					}
					else if(exp.matches("ph")) {
						menubar.ph_tf_sop_value.setText(cb_sop.getSelectedItem().toString());
					}
					else {
						menubar.kf_tf_sop_value.setText(cb_sop.getSelectedItem().toString());
					}
					System.out.println("Data Inserted!");
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
		      else
		      {
				exists = false;
				JOptionPane.showMessageDialog(null, "Method already exists!");
			  }
			}
				
			else 
			{
				JOptionPane.showMessageDialog(null, "Please enter the method name!");
			}
				
				
			}
		});
		
		btn_open = new JButton("Open SOP");
		btn_open.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btn_open.setBounds(365, 350, 120, 37);
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
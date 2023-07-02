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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.Desktop;  


public class save_method extends JFrame {

	private static JPanel contentPane;
	private JTextField method;
	static JLabel sop;
	static save_method frame;
	static String method_name=null;
	//static String data="";
	static String exp="";
	static JButton btn_save,btn_open;
	static JComboBox cb_sop;
	static String[] data_arr;
	static String[] exists_arr;
	ResultSet rs1;
	String final_name="";
	static potentiometry potentiometry_obj = null ;
	static karl_fischer kf_obj = null ;

	
	public static void main(String[] args) {
		
		if(args.length != 0) {
		//data=args[0];
		//data_arr = data.split(",");
		//method_name=args[1];
		exp=args[0];
		}
	
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
		
		
		if(null != method.getText()) {
			
			try {
			if(!method_name.matches("")) {
				
			method.setText(method_name);
			
			JButton btn_update_data = new JButton("Update Method");
			btn_update_data.setFont(new Font("Times New Roman", Font.BOLD, 18));
			btn_update_data.setBounds(155, 350, 180, 37);
			contentPane.add(btn_update_data);
			btn_update_data.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					if(null!= method_name && method.getText().matches(method_name)) {
						
						
							String sql = null;
							if(exp.matches("pot")) 
							{
								update_pot_method();
								menubar.pot_tf_sop_value.setText(cb_sop.getSelectedItem().toString());
								menubar.setPotentimetryObject(potentiometry_obj);
								menubar.metd_header.setText("Method Name : "+method_name);
								menubar.saved_file=true;
								menubar.saved_file_name = method_name;
								potentiometry_obj =null;

							}
							else if(exp.matches("karl")) {
								update_kf_method();
								menubar.kf_tf_sop_value.setText(cb_sop.getSelectedItem().toString());
								menubar.setKFObject(kf_obj);
								menubar.metd_header.setText("Method Name : "+method_name);
								menubar.saved_file=true;
								menubar.saved_file_name = method_name;
								kf_obj =null;

							}
							dispose();
						
						}
					
					else {
						JOptionPane.showMessageDialog(null, "Please Save it as a new Method!");
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
		  } 
		}
		
		cb_sop = new JComboBox(sop_files_arr);
		cb_sop.setBounds(20, 150, 465, 30);
		contentPane.add(cb_sop);
		try {
			if(null!= exp && exp.matches("pot"))
				cb_sop.setSelectedItem(potentiometry_obj.getSop());
			else if(null!= exp && exp.matches("karl"))
				cb_sop.setSelectedItem(kf_obj.getSop());
		}
		catch(NullPointerException e) {
			
		}
		
		btn_save = new JButton("Save");
		btn_save.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btn_save.setBounds(25, 350, 104, 37);
		contentPane.add(btn_save);
		btn_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String method_name = method.getText();
				if(!method_name.matches(""))
				{
					if(!methodName_exists(method_name)) {	
						System.out.println("Experiment = "+exp);
						if(exp.matches("pot")) {
							potentiometry_obj.setMethod_name(method_name);
							insert_new_pot_method();
							potentiometry_obj =null;

						}
						else if(exp.matches("karl")) {
							kf_obj.setMethod_name(method_name);
							insert_new_kf_method();
							kf_obj =null;

						}
					}
				
				    else{
						JOptionPane.showMessageDialog(null, "Method name already exists!");
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
	
	private boolean methodName_exists(String cur_method_name) {
		Connection con1 = DbConnection.connect();
		PreparedStatement ps1 = null;
		rs1 = null;
		String sql1 =null ;
		try {
			if(exp.matches("pot")) {
				sql1 = "SELECT * FROM potentiometry_methods where method_name = '"+cur_method_name+"'";
			}
			else if(exp.matches("karl")) {
				sql1 = "SELECT * FROM kf_methods where method_name = '"+cur_method_name+"'";
			}
			ps1 = con1.prepareStatement(sql1);
			rs1 = ps1.executeQuery();

			if(null!= rs1 && rs1.getRow()>0)
				return true;
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
		return false;
	}
	
	
	 public static void insert_new_pot_method() {
		 
		 if(null != potentiometry_obj) {
			 
			Connection con = DbConnection.connect();
			PreparedStatement ps = null;
			try 
			{
				String sql = "INSERT INTO potentiometry_methods ( method_name, created_by, created_date, updated_date, updated_by, pre_dose, stir_time, max_vol, blank_vol, burette_factor, threshold, filter, dosage_rate, no_of_trials, factor1, factor2, factor3, factor4, ep_select, formula_no, result_unit, sop) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";				
				
				if(!potentiometry_obj.getSop().matches(cb_sop.getSelectedItem().toString()))
					potentiometry_obj.setSop(cb_sop.getSelectedItem().toString());
				
				ps = con.prepareStatement(sql);
				ps.setString(1, potentiometry_obj.getMethod_name());
				ps.setString(2, potentiometry_obj.getCreated_by());
				ps.setString(3, potentiometry_obj.getCreated_date());
				ps.setString(4, potentiometry_obj.getUpdated_date());
				ps.setString(5, potentiometry_obj.getUpdated_by());
				ps.setString(6, potentiometry_obj.getPre_dose());
				ps.setString(7, potentiometry_obj.getStir_time());
				ps.setString(8, potentiometry_obj.getMax_vol());
				ps.setString(9, potentiometry_obj.getBlank_vol());
				ps.setString(10, potentiometry_obj.getBurette_factor());
				ps.setString(11, potentiometry_obj.getThreshold());
				ps.setString(12, potentiometry_obj.getFilter());
				ps.setString(13, potentiometry_obj.getDosage_rate());
				ps.setString(14, potentiometry_obj.getNo_of_trials());
				ps.setString(15, potentiometry_obj.getFactor1());
				ps.setString(16, potentiometry_obj.getFactor2());
				ps.setString(17, potentiometry_obj.getFactor3());
				ps.setString(18, potentiometry_obj.getFactor4());
				ps.setString(19, potentiometry_obj.getEp_select());
				ps.setString(20, potentiometry_obj.getFormula_no());
				ps.setString(21, potentiometry_obj.getResult_unit());
				ps.setString(22, cb_sop.getSelectedItem().toString());
				

				ps.execute();
				menubar.metd_header.setText("Method Name : "+potentiometry_obj.getMethod_name());
				frame.dispose();
				JOptionPane.showMessageDialog(null, "Saved Successfully");
				menubar.saved_file=true;
				menubar.saved_file_name = potentiometry_obj.getMethod_name();
				menubar.pot_tf_sop_value.setText(potentiometry_obj.getSop());
				menubar.setPotentimetryObject(potentiometry_obj);
				potentiometry_obj = null;
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
	 }
	 
	 public static void update_pot_method() {
		 
		 if(null != potentiometry_obj) {
			 
			Connection con = DbConnection.connect();
			PreparedStatement ps = null;
			try 
			{
				String sql = "Update potentiometry_methods set  updated_date = ? , updated_by = ? , pre_dose = ? , stir_time = ? , max_vol = ? , blank_vol = ? , burette_factor = ? , threshold = ? , filter = ? , dosage_rate = ? , no_of_trials = ? , factor1 = ? , factor2 = ? , factor3 = ? , factor4 = ? , ep_select = ? , formula_no = ? , result_unit = ? , sop = ?  WHERE method_name = ?";				
				
				if(!potentiometry_obj.getSop().matches(cb_sop.getSelectedItem().toString()))
					potentiometry_obj.setSop(cb_sop.getSelectedItem().toString());
				
				ps = con.prepareStatement(sql);

				ps.setString(1, potentiometry_obj.getUpdated_date());
				ps.setString(2, potentiometry_obj.getUpdated_by());
				ps.setString(3, potentiometry_obj.getPre_dose());
				ps.setString(4, potentiometry_obj.getStir_time());
				ps.setString(5, potentiometry_obj.getMax_vol());
				ps.setString(6, potentiometry_obj.getBlank_vol());
				ps.setString(7, potentiometry_obj.getBurette_factor());
				ps.setString(8, potentiometry_obj.getThreshold());
				ps.setString(9, potentiometry_obj.getFilter());
				ps.setString(10, potentiometry_obj.getDosage_rate());
				ps.setString(11, potentiometry_obj.getNo_of_trials());
				ps.setString(12, potentiometry_obj.getFactor1());
				ps.setString(13, potentiometry_obj.getFactor2());
				ps.setString(14, potentiometry_obj.getFactor3());
				ps.setString(15, potentiometry_obj.getFactor4());
				ps.setString(16, potentiometry_obj.getEp_select());
				ps.setString(17, potentiometry_obj.getFormula_no());
				ps.setString(18, potentiometry_obj.getResult_unit());
				ps.setString(19, potentiometry_obj.getSop());
				ps.setString(20, potentiometry_obj.getMethod_name());

				ps.execute();
				menubar.metd_header.setText("Method Name : "+potentiometry_obj.getMethod_name());
				frame.dispose();
				JOptionPane.showMessageDialog(null, "Saved Successfully");
				menubar.saved_file=true;
				menubar.saved_file_name = potentiometry_obj.getMethod_name();
				menubar.pot_tf_sop_value.setText(potentiometry_obj.getSop());

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
	 }
	 
	 public static void insert_new_kf_method() {
		 
		 if(null != kf_obj) {
			 
			Connection con = DbConnection.connect();
			PreparedStatement ps = null;
			try 
			{
				String sql = "INSERT INTO kf_methods ( method_name, created_by, created_date, updated_by, updated_date, delay, stir_time, max_vol, blank_vol, burette_factor, density, kf_factor, end_point, dosage_rate, result_unit, no_of_trials, sop) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";				
				
				if(!kf_obj.getSop().matches(cb_sop.getSelectedItem().toString()))
					kf_obj.setSop(cb_sop.getSelectedItem().toString());
				
				ps = con.prepareStatement(sql);
				ps.setString(1, kf_obj.getMethod_name());
				ps.setString(2, kf_obj.getCreated_by());
				ps.setString(3, kf_obj.getCreated_date());
				ps.setString(4, kf_obj.getUpdated_by());
				ps.setString(5, kf_obj.getUpdated_date());
				ps.setString(6, kf_obj.getDelay());
				ps.setString(7, kf_obj.getStir_time());
				ps.setString(8, kf_obj.getMax_vol());
				ps.setString(9, kf_obj.getBlank_vol());
				ps.setString(10, kf_obj.getBurette_factor());
				ps.setString(11, kf_obj.getDensity());
				ps.setString(12, kf_obj.getKf_factor());
				ps.setString(13, kf_obj.getEnd_point());
				ps.setString(14, kf_obj.getDosage_rate());
				ps.setString(15, kf_obj.getResult_unit());
				ps.setString(16, kf_obj.getNo_of_trials());
				ps.setString(17, kf_obj.getSop());
				
				ps.execute();
				menubar.metd_header.setText("Method Name : "+kf_obj.getMethod_name());
				frame.dispose();
				JOptionPane.showMessageDialog(null, "Saved Successfully");
				menubar.saved_file=true;
				menubar.saved_file_name = kf_obj.getMethod_name();
				menubar.kf_tf_sop_value.setText(kf_obj.getSop());
				menubar.setKFObject(kf_obj);
				kf_obj =null;

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
	 }
	 
	 public static void update_kf_method() {
		 
		 if(null != kf_obj) {
			 
			Connection con = DbConnection.connect();
			PreparedStatement ps = null;
			try 
			{
				String sql = "Update kf_methods set updated_by = ?, updated_date = ?, delay = ?, stir_time = ?, max_vol = ?, blank_vol = ?, burette_factor = ?, density = ?, kf_factor = ?, end_point = ?, dosage_rate = ?, result_unit = ?, no_of_trials = ?, sop = ? where method_name = ?";				
				
				if(!kf_obj.getSop().matches(cb_sop.getSelectedItem().toString()))
					kf_obj.setSop(cb_sop.getSelectedItem().toString());
				
				ps = con.prepareStatement(sql);
			
				ps.setString(1, kf_obj.getUpdated_by());
				ps.setString(2, kf_obj.getUpdated_date());
				ps.setString(3, kf_obj.getDelay());
				ps.setString(4, kf_obj.getStir_time());
				ps.setString(5, kf_obj.getMax_vol());
				ps.setString(6, kf_obj.getBlank_vol());
				ps.setString(7, kf_obj.getBurette_factor());
				ps.setString(8, kf_obj.getDensity());
				ps.setString(9, kf_obj.getKf_factor());
				ps.setString(10, kf_obj.getEnd_point());
				ps.setString(11, kf_obj.getDosage_rate());
				ps.setString(12, kf_obj.getResult_unit());
				ps.setString(13, kf_obj.getNo_of_trials());
				ps.setString(14, kf_obj.getSop());
				ps.setString(15, kf_obj.getMethod_name());

				
				ps.execute();
				menubar.metd_header.setText("Method Name : "+kf_obj.getMethod_name());
				frame.dispose();
				JOptionPane.showMessageDialog(null, "Saved Successfully");
				menubar.saved_file=true;
				menubar.saved_file_name = kf_obj.getMethod_name();
				menubar.kf_tf_sop_value.setText(kf_obj.getSop());

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
	 }
	 
	 public static String get_date() {
			DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
			String date_time = dateFormat2.format(new Date()).toString();
			return date_time;
	}
	
	public static void setPotentimetryObject(potentiometry pot) {
		potentiometry_obj  = new potentiometry(pot);
		method_name = potentiometry_obj.getMethod_name();
	}
	public static void setKFObject(karl_fischer kf) {
		kf_obj  = new karl_fischer(kf);
		method_name = kf_obj.getMethod_name();
	}
}
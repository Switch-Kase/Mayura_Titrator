package main.java;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class popup_input_kf extends JFrame {

	private JPanel contentPane;
	
	private JLabel ar_no_label ;
	private JLabel batch_no_label ;
	private JLabel sample_label ;
	private JLabel report_label ;
	private JLabel titrant_label ;
	private JLabel reagent_label ;

	private JTextField ar_no;
	private JTextField batch_no;
	private JTextField sample ;
	private JTextField report ;
	private JTextField titrant ;
	private JTextField reagent ;

	
	private static String method_data;
	private static String method_name;

	static popup_input_kf frame;
	private JButton btnNewButton,btn_sop,btn_rename; 
	static String status = "false";
	
	static boolean unique = true;
	static karl_fischer kf_obj = null ;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		if(args.length !=0) {
		status = args[0];
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					frame = new popup_input_kf();
					frame.setVisible(true);
					ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
					frame.setIconImage(img.getImage());

					//frame.dispose();
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame for Developer Login
	 */
	public popup_input_kf() {
		setBounds(100, 200, 520, 430);
		setTitle("Input Data KF");
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JLabel heading = new JLabel("Provide Input Details");
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setFont(new Font("Times New Roman", Font.BOLD, 22));
		heading.setBounds(0, 30, 520, 20);
		contentPane.add(heading);
		
		ar_no_label = new JLabel("AR Number   :");
		ar_no_label.setFont(new Font("Times New Roman", Font.BOLD, 18));
		ar_no_label.setBounds(120, 80, 139, 21);
		contentPane.add(ar_no_label);
		
		ar_no = new JTextField();
		ar_no.setBounds(255, 80, 139, 19);
		contentPane.add(ar_no);
		
		batch_no_label = new JLabel("Batch Number :");
		batch_no_label.setFont(new Font("Times New Roman", Font.BOLD, 18));
		batch_no_label.setBounds(120, 130, 139, 13);
		contentPane.add(batch_no_label);
		
		batch_no= new JTextField();
		batch_no.setBounds(255, 130, 139, 19);
		contentPane.add(batch_no);
		
		sample_label = new JLabel("Sample Name :");
		sample_label.setFont(new Font("Times New Roman", Font.BOLD, 18));
		sample_label.setBounds(120, 180, 139, 21);
		contentPane.add(sample_label);
		
		sample = new JTextField();
		sample.setBounds(255, 180, 139, 19);
		contentPane.add(sample);
		
		report_label = new JLabel("Report Name :");
		report_label.setFont(new Font("Times New Roman", Font.BOLD, 18));
		report_label.setBounds(120, 230, 139, 21);
		contentPane.add(report_label);
		
		report = new JTextField();
		report.setBounds(255, 230, 139, 19);
		contentPane.add(report);
		
		reagent_label = new JLabel("Reagent Mfg :");
		reagent_label.setFont(new Font("Times New Roman", Font.BOLD, 18));
		reagent_label.setBounds(120, 280, 139, 21);
		contentPane.add(reagent_label);
		
		reagent = new JTextField();
		reagent.setBounds(255, 280, 139, 19);
		contentPane.add(reagent);
		
		
		//remove -- Temperary
//		
		ar_no.setText("5");
		batch_no.setText("5");
		sample.setText("5");
		report.setText("5");
		reagent.setText("5");
//		
		if(status.matches("true")) 
		{
			btnNewButton = new JButton("Finish");
			btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
			btnNewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			btnNewButton.setBounds(320, 330, 100, 37);
			contentPane.add(btnNewButton);
			
			btn_rename = new JButton("Rename Metd File");
			btn_rename.setFont(new Font("Times New Roman", Font.BOLD, 18));
			btn_rename.setAlignmentX(Component.CENTER_ALIGNMENT);
			btn_rename.setBounds(80, 330, 200, 37);
			contentPane.add(btn_rename);
			
			btn_rename.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					rename_metd_name usop=new rename_metd_name();
					String[] temp= {"KF",method_name};
					usop.main(temp);
					dispose();
				}
			});
		}
		else 
		{
			btnNewButton = new JButton("Finish");
			btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
			btnNewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			btnNewButton.setBounds(200, 330, 100, 37);
			contentPane.add(btnNewButton);
		}
		

		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
				
				String ar=(ar_no.getText());
				String batch=(batch_no.getText());
				
				
				String sample_name=sample.getText().toString();
				String report_name = report.getText().toString();
				//System.out.println("Method name in input popUp = "+method_name);
				
				
				if(ar_no.getText().matches("") || batch_no.getText().matches("") || sample_name.matches("") || reagent.getText().toString().matches("")){
					JOptionPane.showMessageDialog(null, "Fill all the data!");
				}
				else 
				{
					Connection con = DbConnection.connect();
					PreparedStatement ps = null;
					ResultSet rs = null;
					String sql ;
					
					try {

						sql = "SELECT report_name FROM kf";
						ps = con.prepareStatement(sql);
						rs = ps.executeQuery();
						
						while(rs.next()) 
						{
							//System.out.println(rs.getString("report_name") + " actual = "+report_name);

							if(report_name.matches(rs.getString("report_name"))) {
								//System.out.println("if  = "+rs.getString("report_name"));
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
					
					if(unique == true) {
					//	System.out.println("Unique");
						//System.out.println("Method name in input popUp = "+method_name);
						
						try {
							audit_log_push.push_to_audit(get_date(), get_time(),menubar.user_name,"KF Experiment Started");
						} catch (ParseException e1) {e1.printStackTrace();}
						
						String[] aa= {method_name,method_data,String.valueOf(ar),String.valueOf(batch),sample_name,report.getText().toString(),reagent.getText().toString()};
						menubar.open_draw_graph_kf(aa);								
						dispose();
					}
					else {
						JOptionPane.showMessageDialog(null, "Report Name Exists! Please choose unique name!");
						unique = true;
					}
				  }
				}
				catch(NumberFormatException rgfb) {
					JOptionPane.showMessageDialog(null, "Enter Number for AR or Batch No!");

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
	
	public static void setKFObject(karl_fischer kf) {
		kf_obj  = new karl_fischer(kf);
		method_name = kf_obj.getMethod_name();
		method_data = kf_obj.getDelay() + "," + kf_obj.getStir_time() + ","
				+ kf_obj.getMax_vol() + "," + kf_obj.getBlank_vol() + ","
				+ kf_obj.getBurette_factor() + "," + kf_obj.getDensity() + ","
				+ kf_obj.getKf_factor()+ "," + kf_obj.getEnd_point() + ","
				+ kf_obj.getDosage_rate() + ","
				+ kf_obj.getResult_unit() + ","
				+ kf_obj.getNo_of_trials() + ","
				+ kf_obj.getSop();
	}
}
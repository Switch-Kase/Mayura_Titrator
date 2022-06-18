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

public class popup_input extends JFrame {

	private JPanel contentPane;
	
	private JLabel ar_no_label ;
	private JLabel batch_no_label ;
	private JLabel sample_label ;
	private JLabel normality_label ;
	private JLabel moisture_label ;
	private JLabel report_label ;
	private JLabel titrant_label ;
	
	private JTextField ar_no;
	private JTextField batch_no;
	private JTextField sample ;
	private JTextField normality ;
	private JTextField moisture ;
	private JTextField report ;
	private JTextField titrant ;
	private JButton btnNewButton,btn_sop,btn_rename; 

	public static String method_data;
	public static String method_name;

	static popup_input frame;
	static String status = "false";
	static String exp = "";
	static boolean unique = true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if(args.length >0) {
		method_data = args[0];
		method_name = args[1];
		status = args[2];
		exp = args[3];
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new popup_input();
					frame.setVisible(true);
					

					//frame.dispose();
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame for Developer Login
	 */
	public popup_input() {
		
		setBounds(100, 200, 520, 550);
		setTitle("Input Data");
		setLocationRelativeTo(null);
		
		ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
		setIconImage(img.getImage());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JLabel heading = new JLabel("Provide Titration Input Details");
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setFont(new Font("Times New Roman", Font.BOLD, 22));
		heading.setBounds(0, 30, 520, 20);
		contentPane.add(heading);
		
		ar_no_label = new JLabel("AR Number   :");
		ar_no_label.setFont(new Font("Times New Roman", Font.BOLD, 18));
		ar_no_label.setBounds(120, 80, 139, 21);
		contentPane.add(ar_no_label);
		
		ar_no = new JTextField();
		ar_no.setBounds(255, 80, 180, 30);
		contentPane.add(ar_no);
		
		batch_no_label = new JLabel("Batch Number :");
		batch_no_label.setFont(new Font("Times New Roman", Font.BOLD, 18));
		batch_no_label.setBounds(120, 130, 139, 13);
		contentPane.add(batch_no_label);
		
		batch_no= new JTextField();
		batch_no.setBounds(255, 130, 180, 30);
		contentPane.add(batch_no);
		
		sample_label = new JLabel("Sample Name :");
		sample_label.setFont(new Font("Times New Roman", Font.BOLD, 18));
		sample_label.setBounds(120, 180, 139, 21);
		contentPane.add(sample_label);
		
		sample = new JTextField();
		sample.setBounds(255, 180, 180, 30);
		contentPane.add(sample);
		
		normality_label = new JLabel("Normality :");
		normality_label.setFont(new Font("Times New Roman", Font.BOLD, 18));
		normality_label.setBounds(120, 230, 139, 21);
		contentPane.add(normality_label);
		
		normality = new JTextField();
		normality.setBounds(255, 230, 180, 30);
		contentPane.add(normality);
		
		moisture_label = new JLabel("Moisture(%) :");
		moisture_label.setFont(new Font("Times New Roman", Font.BOLD, 18));
		moisture_label.setBounds(120, 280, 139, 21);
		contentPane.add(moisture_label);
		
		moisture = new JTextField();
		moisture.setBounds(255, 280, 180, 30);
		contentPane.add(moisture);
		
		report_label = new JLabel("Report :");
		report_label.setFont(new Font("Times New Roman", Font.BOLD, 18));
		report_label.setBounds(120, 330, 139, 21);
		contentPane.add(report_label);
		
		report = new JTextField();
		report.setBounds(255, 330, 180, 30);
		contentPane.add(report);
		
		titrant_label = new JLabel("Titrant Name :");
		titrant_label.setFont(new Font("Times New Roman", Font.BOLD, 18));
		titrant_label.setBounds(120, 380, 139, 21);
		contentPane.add(titrant_label);
		
		titrant = new JTextField();
		titrant.setBounds(255, 380, 180, 30);
		contentPane.add(titrant);
		
		ar_no.setText("1");
		batch_no.setText("1");
		sample.setText("1");
		normality.setText("1");
		moisture.setText("1");
		report.setText("1");
		titrant.setText("1");
		
		
		
		if(status.matches("true")) 
		{
			btnNewButton = new JButton("Finish");
			btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
			btnNewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			btnNewButton.setBounds(330, 450, 100, 37);
			contentPane.add(btnNewButton);
			
			btn_rename = new JButton("Rename Metd File");
			btn_rename.setFont(new Font("Times New Roman", Font.BOLD, 18));
			btn_rename.setAlignmentX(Component.CENTER_ALIGNMENT);
			btn_rename.setBounds(80, 450, 200, 37);
			contentPane.add(btn_rename);
			
			btn_rename.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					rename_metd_name usop=new rename_metd_name();
					String[] temp= {"Potentiometry",method_name};
					usop.main(temp);
					dispose();
				}
			});
			
		}
		else {
			btnNewButton = new JButton("Finish");
			btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
			btnNewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			btnNewButton.setBounds(220, 450, 100, 37);
			contentPane.add(btnNewButton);
		}
		
		
	
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String ar=ar_no.getText().toString();
					String batch=batch_no.getText().toString();
					String sample_name=sample.getText();
					float normality_val=Float.parseFloat(normality.getText().toString());
					float moisture_val=Float.parseFloat(moisture.getText().toString());
					String report_name=report.getText();
					String titrant_name=titrant.getText();
					
					if(ar_no.getText().matches("") || batch_no.getText().matches("") || sample_name.matches("")|| normality.getText().matches("") || moisture.getText().matches("") || report_name.matches("") || titrant_name.matches("")){
						JOptionPane.showMessageDialog(null, "Fill all the data!");
					}
					else 
					{
						
						Connection con = DbConnection.connect();
						PreparedStatement ps = null;
						ResultSet rs = null;
						String sql ;
						
						try {

							sql = "SELECT report_name FROM potentiometry";
							ps = con.prepareStatement(sql);
							rs = ps.executeQuery();
							
							while(rs.next()) 
							{
								//System.out.println(rs.getString("report_name") + " actual = "+report_name);
								if(report_name.matches(rs.getString("report_name"))) {
								//	System.out.println("if  = "+rs.getString("report_name"));
									unique = false;
									break;
								}
							}						
						}
						catch(SQLException e1) {
							JOptionPane.showMessageDialog(null,"Report Name Exists. Please enter a unique name!");
							//dispose();
						}
						finally {
						    try{
						    ps.close();
						    con.close();
						    } catch(SQLException e1) {
								JOptionPane.showMessageDialog(null,"Report Name Exists. Please enter a unique name!");
						    }
						 }	
						if(unique == true) {
							//System.out.println("Unique = ");
						//	System.out.println("Method name in input popUp = "+method_name);
							try {
								audit_log_push.push_to_audit(get_date(), get_time(),menubar.user_name,"Potentiometry Experiment Started");
							} catch (ParseException e1) {e1.printStackTrace();}
							
							String[] aa= {method_name,method_data,ar,batch,sample_name,String.valueOf(normality_val),String.valueOf(moisture_val),report_name,titrant_name};
							menubar.open_draw_graph(aa);
							dispose();
						}
						else {
							JOptionPane.showMessageDialog(null, "Report Name Exists! Please choose unique name!");
							unique = true;
						}
					}
				}
				catch(NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null, "Enter Number for AR or Batch No or normality or moisture!");
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
}
package main.java;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.regex.Pattern;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

//import Login.trial;

//import login.trial;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField.AbstractFormatter;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class open_method extends JDialog{

	private JPanel contentPane;
	private JTextField user;
	private JTextField password;
	static open_method frame;
	static JTable table1= new JTable();
	static String exp="";
	DefaultTableModel model;
	static int wid=0,hei=0;


	public static void main(String[] args) {
		if(args.length > 0)
		exp=args[0];
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new open_method();
					frame.setVisible(true);
					ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
					frame.setIconImage(img.getImage());
					frame.setResizable(false);

					//frame.dispose();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public open_method() {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 200, 720, 525);
		setLocationRelativeTo(null);
		
		setResizable(false);
		setModal(true);
		

		
		setTitle("Open Method File");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("From Date");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel.setBounds(54, 25, 139, 21);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("To Date");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel_1.setBounds(275, 25, 139, 13);
		contentPane.add(lblNewLabel_1);
		UtilDateModel model1 = new UtilDateModel();
		UtilDateModel model2 = new UtilDateModel();

		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model1,p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel,new DateLabelFormatter());
		datePicker.setBounds(54, 50, 139, 40);
		contentPane.add(datePicker);
		
		JDatePanelImpl datePanel1 = new JDatePanelImpl(model2,p);
		JDatePickerImpl datePicker1 = new JDatePickerImpl(datePanel1,new DateLabelFormatter());
		datePicker1.setBounds(275, 50, 139, 40);
		contentPane.add(datePicker1);
		
		JButton btnNewButton = new JButton("Open");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				int column = 0;
				int row = table1.getSelectedRow();
				String value = table1.getModel().getValueAt(row, column).toString();
				//system.out.println("Selected Row = "+value);
				menubar.update_data(value);
				menubar.saved_file=true;
				dispose();
				}
				catch(ArrayIndexOutOfBoundsException e2) {
					JOptionPane.showMessageDialog(null, "Please Select A Method File");
				} 
			}
		});
		
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btnNewButton.setBounds(325, 415, 104, 37);
		contentPane.add(btnNewButton);
		
		JButton btn_apply_filter = new JButton("Apply Filter");
		btn_apply_filter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				int column = 0;
				int row = table1.getSelectedRow();

				Date datee = (Date) datePicker.getModel().getValue();	
				
				String from_month = ((model1.getMonth()+1) < 10 ? "0" : "") + (model1.getMonth()+1);
				String to_month = ((model2.getMonth()+1) < 10 ? "0" : "") + (model2.getMonth()+1);
				String from_day = (model1.getDay() < 10 ? "0" : "") + (model1.getDay());
				String to_day = (model2.getDay() < 10 ? "0" : "") + (model2.getDay());
				
				String from_date = model1.getYear()+"-"+from_month+"-"+from_day;
				String to_date = model2.getYear()+"-"+to_month+"-"+to_day;
				
				
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
				LocalDateTime now = LocalDateTime.now();  
				String cur_date = String.valueOf(dtf.format(now));
				if((!from_date.matches(cur_date) && !to_date.matches(cur_date))  || (!from_date.matches(cur_date) && to_date.matches(cur_date)) ) {
					model.getDataVector().removeAllElements();
					model.fireTableDataChanged();
					Connection con = DbConnection.connect();
					PreparedStatement ps = null;
					ResultSet rs = null;
					String sql ;
					try {
						if(exp.matches("pot")) {
							sql = "SELECT method_name,created_date FROM potentiometry_methods where (created_date BETWEEN '"+from_date+"' AND '"+to_date+"')"; //
						}
//						else if(exp.matches("amp")) {
//							sql = "SELECT Trial_name,Date,Value FROM amp_method where (Date BETWEEN '"+from_date+"' AND '"+to_date+"')";
//						}
//						else if(exp.matches("ph")){
//							sql = "SELECT Trial_name,Date,Value FROM ph_method where (Date BETWEEN '"+from_date+"' AND '"+to_date+"')";
//						}
						else {
							sql = "SELECT method_name,created_date FROM kf_methods where (created_date BETWEEN '"+from_date+"' AND '"+to_date+"')";
						}
						ps = con.prepareStatement(sql);
						rs = ps.executeQuery();
						
						int i=0;
						 while (rs.next()) {
							 model.addRow(new Object[0]);
							 model.setValueAt(rs.getString("method_name"), i, 0);			
							 model.setValueAt(rs.getString("created_date"), i, 1);
							 i++;
						 }
						model.fireTableDataChanged();

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
				}							
				}
				catch(ArrayIndexOutOfBoundsException e2) {
					JOptionPane.showMessageDialog(null, "Please Select A Method File");
				} 
			}
		});
		
		btn_apply_filter.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btn_apply_filter.setBounds(500, 45, 150, 32);
		contentPane.add(btn_apply_filter);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(54, 130, 600, 250);
		contentPane .add(scrollPane);

		table1 = new JTable();
		table1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		table1.setRowHeight(25);

		model = new DefaultTableModel() {
			public Class <?> getColumnClass(int column){
				switch(column) {
				case 0:
					return String.class;
				default:
					return String.class;			
				}	
			}
		};
		table1.setModel(model);
		table1.setDefaultEditor(Object.class, null);

		model.addColumn("Method File");
		model.addColumn("Date");
		
		
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql ;
		try {
			if(exp.matches("pot")) {
				sql = "SELECT method_name,created_date FROM potentiometry_methods";
			}
//			else if(exp.matches("amp")) {
//				sql = "SELECT Trial_name,Date,Value FROM amp_method";
//			}
//			else if(exp.matches("ph")){
//				sql = "SELECT Trial_name,Date,Value FROM ph_method";
//			}
			else  {
				sql = "SELECT method_name,created_date FROM kf_methods";
			}
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			int i=0;
			 while (rs.next()) {
				 model.addRow(new Object[0]);
				 model.setValueAt(rs.getString("method_name"), i, 0);			
				 model.setValueAt(rs.getString("created_date"), i, 1);
				 i++;
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
		      System.out.println(e1.toString());
		    }
		}
		
		scrollPane.setViewportView(table1);
	}
}

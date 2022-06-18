package main.java;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.AbstractCategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.FloatDimension;

import com.fazecast.jSerialComm.SerialPort;
  
public class new_user extends JPanel 
{
	static JFrame frame1 = new JFrame();
	
	static new_user frame;
	static JTable table1;

	static String exp="";
	static DefaultTableModel model1;
	static int wid=0,hei=0;
	static JPanel p = new JPanel();
	static JTextField user_name,password,validity;
	public new_user() 
	{
		setLayout(null);
		initialize();
	}

	

	@SuppressWarnings("removal")
	public static void initialize() {
		System.out.println("INITIALIZE");
		
		frame1.getContentPane().invalidate();
		frame1.getContentPane().validate();
		frame1.getContentPane().repaint();		
		
		JLabel user_name_header = new JLabel("User Name : ");
		user_name_header.setBounds((int) Math.round(0.07637 * wid), (int) Math.round(0.02 * hei), (int) Math.round(0.25 * wid), (int) Math.round(0.06 * hei));
		user_name_header.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.03 * wid)));
		frame1.getContentPane().add(user_name_header);

		user_name = new JTextField();
		user_name.setBounds((int) Math.round(0.4 * wid), (int) Math.round(0.03 * hei), (int) Math.round(0.515 * wid), (int) Math.round(0.06 * hei));
		user_name.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.03 * wid)));
		frame1.getContentPane().add(user_name);
		
		JLabel user_pass_header = new JLabel("Password: ");
		user_pass_header.setBounds((int) Math.round(0.07637 * wid), (int) Math.round(0.12 * hei), (int) Math.round(0.25 * wid), (int) Math.round(0.06 * hei));
		user_pass_header.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.03 * wid)));
		frame1.getContentPane().add(user_pass_header);
				
		password = new JTextField();
		password.setBounds((int) Math.round(0.4 * wid), (int) Math.round(0.12 * hei), (int) Math.round(0.515 * wid), (int) Math.round(0.06 * hei));
		password.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.03 * wid)));
		frame1.getContentPane().add(password);
		
		JLabel validity_header = new JLabel("Validity: ");
		validity_header.setBounds((int) Math.round(0.07637 * wid), (int) Math.round(0.21 * hei), (int) Math.round(0.25 * wid), (int) Math.round(0.06 * hei));
		validity_header.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.03 * wid)));
		frame1.getContentPane().add(validity_header);
		
		validity = new JTextField();
		validity.setBounds((int) Math.round(0.4 * wid), (int) Math.round(0.21 * hei), (int) Math.round(0.515 * wid), (int) Math.round(0.06 * hei));
		validity.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.03 * wid)));
		frame1.getContentPane().add(validity);
		validity.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!validity.getText().toString().matches("")) {
					try {
						int a  = Integer.parseInt(validity.getText().toString());
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(null, "Validity must be a valid value!");
						validity.setText("1");
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		
		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBounds((int) Math.round(0.07637 * wid), (int) Math.round(0.34 * hei), (int) Math.round(0.8486 * wid), (int) Math.round(0.4 * hei));
		frame1.getContentPane().add(scrollPane1);
		table1 = new JTable();
		table1.setRowHeight((int) Math.round(0.06 * hei));
		table1.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.024 * wid)));
		model1 = new DefaultTableModel() {
			public Class <?> getColumnClass(int column){
				switch(column) {
				case 0:
					return Boolean.class;
				default:
					return String.class;			
				}	
			}
		};
		table1.setModel(model1);
		table1.setDefaultEditor(Object.class, null);
		model1.addColumn("Select");
		model1.addColumn("Items");
		scrollPane1.setViewportView(table1);
		
		
		
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql ;
		boolean present = false;
		try {
			
			sql = "SELECT RoleName FROM Roles"; 

			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			int i=0;
			 while (rs.next()) {			
				model1.addRow(new Object[0]);
				model1.setValueAt(false, i, 0);			
				model1.setValueAt(rs.getString("RoleName"), i, 1);
				i++;
			 }
				model1.fireTableDataChanged();

		}
		catch(SQLException e1) {
			JOptionPane.showMessageDialog(null,e1);
		}
		finally {
		    try{
		    ps.close();
		    con.close();
		    } 
		    catch(SQLException e1) {
		      System.out.println(e1.toString());
		    }
		}
		
		
		JButton btn_new = new JButton("Add User");
		btn_new.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String items = "";
				int j = 1;
				for(int i=0;i<table1.getRowCount();i++ ) {
					Boolean checked = Boolean.valueOf(table1.getValueAt(i, 0).toString());
					String aa=table1.getValueAt(i, 1).toString();
					if(checked) {
						if(j==1)
						{
							items = items+aa;
							j++;
						}
						else {
							items = items+","+aa;
						}
					}
			    }
				
				if(!user_name.getText().toString().matches("") && !password.getText().toString().matches("") && !validity.getText().toString().matches("")) {
				
				Connection con = DbConnection.connect();
				PreparedStatement ps = null;
				String sql ;
				boolean present = false;
				
				try {
					
					sql = "SELECT Username FROM UserLogin"; 

					ps = con.prepareStatement(sql);
					ResultSet rs = ps.executeQuery();
					
					 while (rs.next()) {
						 System.out.println("User Names = "+rs.getString("Username"));
						if(rs.getString("Username").matches(user_name.getText().toString())) {
							present  = true;
						}
					 }
					
					 if(!present) {
						 if(!items.matches("")) {
							sql = "INSERT INTO UserLogin(Username,Password,created_date,validity,Roles) VALUES(?,?,?,?,?)"; 
							ps = con.prepareStatement(sql);
							ps.setString(1, user_name.getText().toString());
					        ps.setString(2, password.getText().toString());
					        ps.setString(3, get_date());
					        ps.setString(4, validity.getText().toString());
					        ps.setString(5, items);
							ps.executeUpdate();
							    frame1.dispose();
					            frame1 = new JFrame();
					            p=new JPanel();	
					            p.revalidate();
					            p.repaint();
							users_roles.main(null);
						 }
						 else {
								JOptionPane.showMessageDialog(null,"Select Role Items!");
						 }
					}
				    else
					{
						JOptionPane.showMessageDialog(null,"User Name exists. Please check again!");
					}
					   
				}
				catch(SQLException e1) {
					JOptionPane.showMessageDialog(null,e1);
				}
				finally {
				    try{
				    ps.close();
				    con.close();
				    } 
				    catch(SQLException e1) {
				      System.out.println(e1.toString());
				    }
				}
			}
				else
				{
					JOptionPane.showMessageDialog(null,"Enter Username , Password & Validity!");

				}
			}
		});
		
		btn_new.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.025 * wid)));
		btn_new.setBounds((int) Math.round(0.38 * wid), (int) Math.round(0.8 * hei), (int) Math.round(0.25 * wid), (int) Math.round(0.08 * hei));
		frame1.getContentPane().add(btn_new);
	}
	
	public static String get_date() {
		DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		String date_time = dateFormat2.format(new Date()).toString();
		return date_time;
	}
	
    public static void main(String[] args) {    
    	
    	Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame1.getGraphicsConfiguration());
        int taskHeight=screenInsets.bottom;
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int height=d.height-taskHeight;
        int width=d.width;
        wid = (int)d.getWidth();
        hei = (int)(d.getHeight()-taskHeight);
        
        wid = (int) Math.round(0.3 * wid);
        hei = (int) Math.round(0.55 * hei);
        
        System.out.println(wid + "   dfvdvdv " + hei);
        
		frame1.setBounds(0,0,wid, hei);
		frame1.add(p);
   		frame1.getContentPane().add(new new_user());
		frame1.setLocationRelativeTo(null);
		frame1.setResizable(true);
		frame1.setVisible(true);
		frame1.repaint();
		frame1.setTitle("New User");
		ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
		frame1.setIconImage(img.getImage());

			frame1.addWindowListener(new java.awt.event.WindowAdapter() {
			    @Override
			    public void windowClosing(java.awt.event.WindowEvent windowEvent)
			    {
			    	frame1.dispose();
		            frame1 = new JFrame();
		            p=new JPanel();	
		            p.revalidate();
		            p.repaint();
				    users_roles.main(null);
			    }
			});
    	}
  
}


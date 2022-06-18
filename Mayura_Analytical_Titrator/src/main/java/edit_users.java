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
import java.util.ArrayList;
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
  
public class edit_users extends JPanel 
{
	static JFrame frame1 = new JFrame();
	
	static edit_users frame;
	static JTable table1;

	static String exp="";
	static DefaultTableModel model1;
	static int wid=0,hei=0;
	static JPanel p = new JPanel();
	static String user_name = "",valid="",days_left = "";
	static ArrayList<String> role_item = new ArrayList<>(); 
	static JTextField validity,tf_new_password,tf_confirm_new_password;
	static JLabel days_header;

	public edit_users() 
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
		
		JLabel user_name_header = new JLabel("User Name : "+user_name);
		user_name_header.setBounds((int) Math.round(0.07637 * wid), (int) Math.round(0.01 * hei), (int) Math.round(0.7 * wid), (int) Math.round(0.1 * hei));
		user_name_header.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.03 * wid)));
		frame1.getContentPane().add(user_name_header);
		
		JLabel validity_header = new JLabel("Validity :");
		validity_header.setBounds((int) Math.round(0.07637 * wid), (int) Math.round(0.08 * hei), (int) Math.round(0.2 * wid), (int) Math.round(0.1 * hei));
		validity_header.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.03 * wid)));
		frame1.getContentPane().add(validity_header);
		
		JLabel new_password = new JLabel("New Password :");
		new_password.setBounds((int) Math.round(0.07637 * wid), (int) Math.round(0.18 * hei), (int) Math.round(0.4 * wid), (int) Math.round(0.1 * hei));
		new_password.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.03 * wid)));
		frame1.getContentPane().add(new_password);
		
		JLabel confirm_new_password = new JLabel("Confirm New Password :");
		confirm_new_password.setBounds((int) Math.round(0.07637 * wid), (int) Math.round(0.26 * hei), (int) Math.round(0.4 * wid), (int) Math.round(0.1 * hei));
		confirm_new_password.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.03 * wid)));
		frame1.getContentPane().add(confirm_new_password);
		
		tf_new_password = new JTextField();
		tf_new_password.setBounds((int) Math.round(0.5 * wid), (int) Math.round(0.21 * hei), (int) Math.round(0.4 * wid), (int) Math.round(0.05 * hei));
		tf_new_password.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.03 * wid)));
		frame1.getContentPane().add(tf_new_password);
		
		tf_confirm_new_password = new JTextField();
		tf_confirm_new_password.setBounds((int) Math.round(0.5 * wid), (int) Math.round(0.29 * hei), (int) Math.round(0.4 * wid), (int) Math.round(0.05 * hei));
		tf_confirm_new_password.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.03 * wid)));
		frame1.getContentPane().add(tf_confirm_new_password);
		
		
		validity = new JTextField();
		validity.setBounds((int) Math.round(0.25 * wid), (int) Math.round(0.11 * hei), (int) Math.round(0.25 * wid), (int) Math.round(0.05 * hei));
		validity.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.03 * wid)));
		frame1.getContentPane().add(validity);
		validity.setText(valid);
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
						validity.setText(valid);
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		
		days_header = new JLabel("Days Left : "+days_left);
		days_header.setBounds((int) Math.round(0.55 * wid), (int) Math.round(0.1 * hei), (int) Math.round(0.25 * wid), (int) Math.round(0.06 * hei));
		days_header.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.03 * wid)));
		frame1.getContentPane().add(days_header);

		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBounds((int) Math.round(0.07637 * wid), (int) Math.round(0.4 * hei), (int) Math.round(0.8486 * wid), (int) Math.round(0.35 * hei));
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
				model1.setValueAt(role_item.contains(rs.getString("RoleName")), i, 0);			
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
		
		JButton btn_new = new JButton("Update Role");
		btn_new.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String items = "";
				int j=1;
				int validity_days = 0;
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
				System.out.println(items);
				
				if(!tf_new_password.getText().toString().matches(""))
				{
					if(tf_new_password.getText().toString().matches(tf_confirm_new_password.getText().toString())) 
					{	
						if(validity.getText().toString().matches("")) {
						    validity_days = 0;
						}
						else {
							 validity_days = Integer.parseInt(validity.getText().toString());
						}
						Connection con = DbConnection.connect();
						PreparedStatement ps = null;
						String sql ;
						try {
							sql = "UPDATE UserLogin SET Roles = ?,validity = ?,password = ?,created_date = ? WHERE Username = ?";
		
							ps = con.prepareStatement(sql);
							ps.setString(1, items);
							ps.setString(2, String.valueOf(validity_days));
							ps.setString(3, tf_new_password.getText().toString());
							ps.setString(4, get_date());							
							ps.setString(5, user_name);
							ps.executeUpdate();
						}
						catch(SQLException e1) {
							JOptionPane.showMessageDialog(null,e1);
						}
						finally {
						    try{
						    ps.close();
						    con.close();
						    frame1.dispose();
				            frame1 = new JFrame();
				            p=new JPanel();	
				            p.revalidate();
				            p.repaint();
						    users_roles.main(null);
						    } catch(SQLException e1) {
						      System.out.println(e1.toString());
						    }
						}						
					}
					else {
						JOptionPane.showMessageDialog(null,"Password does not match!");
					}
				}
				
				else 
				{
					if(validity.getText().toString().matches("")) {
					    validity_days = 0;
					}
					else {
						 validity_days = Integer.parseInt(validity.getText().toString());
					}
					Connection con = DbConnection.connect();
					PreparedStatement ps = null;
					String sql ;
					try {
						sql = "UPDATE UserLogin SET Roles = ?,validity = ? WHERE Username = ?";
	
						ps = con.prepareStatement(sql);
						ps.setString(1, items);
						ps.setString(2, String.valueOf(validity_days));
						ps.setString(3, user_name);
						ps.executeUpdate();
						
					}
					catch(SQLException e1) {
						JOptionPane.showMessageDialog(null,e1);
					}
					finally {
					    try{
					    ps.close();
					    con.close();
					    frame1.dispose();
			            frame1 = new JFrame();
			            p=new JPanel();	
			            p.revalidate();
			            p.repaint();
					    users_roles.main(null);
					    } catch(SQLException e1) {
					      System.out.println(e1.toString());
					    }
					}		
				}
			}
		});
		
		btn_new.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.025 * wid)));
		btn_new.setBounds((int) Math.round(0.38 * wid), (int) Math.round(0.83 * hei), (int) Math.round(0.25 * wid), (int) Math.round(0.07 * hei));
		frame1.getContentPane().add(btn_new);
	}
	public static String get_date() {
		DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		String date_time = dateFormat2.format(new Date()).toString();
		return date_time;
	}
	
		
    public static void main(String[] args) {    
    	
    	role_item.clear();
    	
    	if(args.length != 0) {
    		user_name = args[0];
    		valid = args[1];
    		days_left = args[2];
    		String[] temp_items = args[3].split(",");
    		for(int i=0;i<temp_items.length;i++)
    		{
    			role_item.add(temp_items[i]);
    		}
    	}
    	
    	Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame1.getGraphicsConfiguration());
        int taskHeight=screenInsets.bottom;
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int height=d.height-taskHeight;
        int width=d.width;
        wid = (int)d.getWidth();
        hei = (int)(d.getHeight()-taskHeight);
        
        wid = (int) Math.round(0.3 * wid);
        hei = (int) Math.round(0.6 * hei);
        
        System.out.println(wid + "   dfvdvdv " + hei);
        
		frame1.setBounds(0,0,wid, hei);
		frame1.add(p);
   		frame1.getContentPane().add(new edit_users());
		frame1.setLocationRelativeTo(null);
		frame1.setResizable(true);
		frame1.setVisible(true);
		frame1.repaint();
		ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
		frame1.setIconImage(img.getImage());

		frame1.setTitle("Update User");
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


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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
  
public class users_roles extends JPanel 
{
	static JFrame frame1 = new JFrame();
	private JTextField user;
	private JTextField password;
	static users_roles frame;
	static JTable table1= new JTable();
	static JTable table2= new JTable();

	static String exp="";
	static DefaultTableModel model1,model2;
	static int wid=0,hei=0;
	private static JTextField tf_search;
	static ResultSet rs = null;
	static boolean apply_filter = false;
	static String f_date,t_date,c_date;
	static JPanel p = new JPanel();


	
	public users_roles() 
	{
		setLayout(null);
		initialize();
	}

	

	@SuppressWarnings("removal")
	public static void initialize() {
		frame1.getContentPane().invalidate();
		frame1.getContentPane().validate();
		frame1.getContentPane().repaint();		
		
		
		JButton btn_users_new = new JButton("New");
		btn_users_new.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 frame1.dispose();
		         frame1 = new JFrame();
		         p=new JPanel();	
		         p.revalidate();
		         p.repaint();
				 new_user.main(null);
			}
		});
		btn_users_new.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.025 * wid)));
		btn_users_new.setBounds((int) Math.round(0.38 * wid), (int) Math.round(0.35 * hei), (int) Math.round(0.1471 * wid), (int) Math.round(0.05 * hei));
		frame1.getContentPane().add(btn_users_new);
		
		JButton btn_users_edit = new JButton("Edit");
		btn_users_edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int column = 0;
					int row1 = table1.getSelectedRow();
					String value1 = table1.getModel().getValueAt(row1, 0).toString();
					String value2 = table1.getModel().getValueAt(row1, 1).toString();
					String value3 = table1.getModel().getValueAt(row1, 2).toString();
					String value4 = table1.getModel().getValueAt(row1, 3).toString();

					String[] aa = {value1,value2,value3,value4};
					frame1.dispose();
		            frame1 = new JFrame();
		            p=new JPanel();	
		            p.revalidate();
		            p.repaint();
					edit_users.main(aa);
				}
				catch(ArrayIndexOutOfBoundsException fde) {
					JOptionPane.showMessageDialog(null,"Select a User!");
				}
				
				
			}
		});
		btn_users_edit.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.025 * wid)));
		btn_users_edit.setBounds((int) Math.round(0.55 * wid), (int) Math.round(0.35 * hei), (int) Math.round(0.1471 * wid), (int) Math.round(0.05 * hei));
		frame1.getContentPane().add(btn_users_edit);
		
		JButton btn_users_delete = new JButton("Delete");
		btn_users_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				int column = 0;
				int row = table1.getSelectedRow();
				String value = table1.getModel().getValueAt(row, column).toString();
				
				Connection con = DbConnection.connect();
				PreparedStatement ps = null;
				String sql ;
				try {
					sql ="DELETE FROM UserLogin WHERE Username = '"+value+"'";

					ps = con.prepareStatement(sql);
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
				catch(ArrayIndexOutOfBoundsException sd) {					
					JOptionPane.showMessageDialog(null,"Select a User!");
				}
			}
		});
		btn_users_delete.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.025 * wid)));
		btn_users_delete.setBounds((int) Math.round(0.72 * wid), (int) Math.round(0.35 * hei), (int) Math.round(0.2 * wid), (int) Math.round(0.05 * hei));
		frame1.getContentPane().add(btn_users_delete);
		
		JButton btn_roles_new = new JButton("New");
		btn_roles_new.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("HTHTHTH");
				new_role.main(null);
				frame1.dispose();
				frame1 = new JFrame();
		        p=new JPanel();	
		        p.revalidate();
		        p.repaint();
			}
		});
		btn_roles_new.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.025 * wid)));
		btn_roles_new.setBounds((int) Math.round(0.38 * wid), (int) Math.round(0.82 * hei), (int) Math.round(0.1471 * wid), (int) Math.round(0.05 * hei));
		frame1.getContentPane().add(btn_roles_new);
		
		
		
		JButton btn_roles_edit = new JButton("Edit");
		btn_roles_edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int column = 0;
					int row = table2.getSelectedRow();
					String value1 = table2.getModel().getValueAt(row, 0).toString();
					String value2 = table2.getModel().getValueAt(row, 1).toString();

					String[] aa = {value1,value2};
					frame1.dispose();
		            frame1 = new JFrame();
		            p=new JPanel();	
		            p.revalidate();
		            p.repaint();
					edit_roles.main(aa);
				}
				catch(ArrayIndexOutOfBoundsException fde) {
					JOptionPane.showMessageDialog(null,"Select a Role!");
				}
				
			}
		});
		btn_roles_edit.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.025 * wid)));
		btn_roles_edit.setBounds((int) Math.round(0.55 * wid), (int) Math.round(0.82 * hei), (int) Math.round(0.1471 * wid), (int) Math.round(0.05 * hei));
		frame1.getContentPane().add(btn_roles_edit);
		
		
		
		JButton btn_roles_delete = new JButton("Delete");
		btn_roles_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int column = 0;
				int row = table2.getSelectedRow();
				
				try {
				
				
				String value = table2.getModel().getValueAt(row, column).toString();
				
				Connection con = DbConnection.connect();
				PreparedStatement ps = null;
				String sql ;
				try {
					sql ="DELETE FROM Roles WHERE RoleName = '"+value+"'";

					ps = con.prepareStatement(sql);
					ps.executeUpdate();
					
				}
				catch(SQLException e1) {
					JOptionPane.showMessageDialog(null,e1);
				}
				finally {
				    try
				    {
				    	try {
							sql = "SELECT Username,Roles FROM UserLogin"; 

							ps = con.prepareStatement(sql);
							rs = ps.executeQuery();
							
							 while (rs.next()) {
								String u_name  = rs.getString("Username");			
								String role = rs.getString("Roles");
								
								if(role.contains(value)) {
									String[] temp = role.split(",");
									String temp_roles = "";
									int j=0;
									for(int i=0;i<temp.length;i++)
									{
										if(!temp[i].matches(value)) {
											if(j==0) 
											{
												temp_roles = temp_roles+temp[i];
											}
											else
											{
												temp_roles = temp_roles+","+temp[i];
											}
											j++;										
										}
									}
									sql = "UPDATE UserLogin SET Roles = ? WHERE Username = ?";
									ps = con.prepareStatement(sql);
									ps.setString(1, temp_roles);
									ps.setString(2, u_name);
									ps.executeUpdate();									
								}
							 }
							 model1.fireTableDataChanged();
						}
						catch(SQLException e1) {
							JOptionPane.showMessageDialog(null,e1);
						}
				    	
					    ps.close();
					    con.close();
					    frame1.dispose();
			            frame1 = new JFrame();
			            p=new JPanel();	
			            p.revalidate();
			            p.repaint();
					    users_roles.main(null);
				    } 
				    catch(SQLException e1) {
				      System.out.println(e1.toString());
				    }
				}		
				}
				catch(ArrayIndexOutOfBoundsException aiofb) {
					JOptionPane.showMessageDialog(null,"Select a Role!");					
				}
			}
		});
		btn_roles_delete.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.025 * wid)));
		btn_roles_delete.setBounds((int) Math.round(0.72 * wid), (int) Math.round(0.82 * hei), (int) Math.round(0.2 * wid), (int) Math.round(0.05 * hei));
		frame1.getContentPane().add(btn_roles_delete);
		
		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBounds((int) Math.round(0.07637 * wid), (int) Math.round(0.02 * hei), (int) Math.round(0.8486 * wid), (int) Math.round(0.3 * hei));
		frame1.getContentPane().add(scrollPane1);
		table1 = new JTable();
		table1.setRowHeight((int) Math.round(0.06 * hei));
		table1.setFont(new Font("Segoe UI Variable", Font.PLAIN, (int) Math.round(0.024 * wid)));
		model1 = new DefaultTableModel() {
			public Class <?> getColumnClass(int column){
				switch(column) {
				case 0:
					return String.class;
				case 1:
					return String.class;
				case 2:
					return String.class;
				default:
					return String.class;			
				}	
			}
		};
		table1.setModel(model1);
		table1.setDefaultEditor(Object.class, null);
		model1.addColumn("User");
		model1.addColumn("Validity (Days)");
		model1.addColumn("Days Left");
		model1.addColumn("Roles");
		scrollPane1.setViewportView(table1);
		
		table1.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent mouseEvent) {
		        JTable table1 =(JTable) mouseEvent.getSource();
		        Point point = mouseEvent.getPoint();
		        int row = table1.rowAtPoint(point);
		        if (mouseEvent.getClickCount() == 2 && table1.getSelectedRow() != -1) {
		            // your valueChanged overridden method
					int column = 0;
					int row1 = table1.getSelectedRow();
					String value1 = table1.getModel().getValueAt(row1, 0).toString();
					String value2 = table1.getModel().getValueAt(row1, 1).toString();
					String value3 = table1.getModel().getValueAt(row1, 2).toString();
					String value4 = table1.getModel().getValueAt(row1, 3).toString();

					String[] aa = {value1,value2,value3,value4};
					frame1.dispose();
		            frame1 = new JFrame();
		            p=new JPanel();	
		            p.revalidate();
		            p.repaint();
					edit_users.main(aa);
					
					
		        }
		    }
		});
		
		
		
		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setBounds((int) Math.round(0.07637 * wid), (int) Math.round(0.47 * hei), (int) Math.round(0.8486 * wid), (int) Math.round(0.3 * hei));
		frame1.getContentPane().add(scrollPane2);
		table2 = new JTable();
		table2.setRowHeight((int) Math.round(0.06 * hei));
		table2.setFont(new Font("Segoe UI Variable", Font.PLAIN, (int) Math.round(0.024 * wid)));
		model2 = new DefaultTableModel() {
			public Class <?> getColumnClass(int column){
				switch(column) {
				case 0:
					return String.class;
				default:
					return String.class;			
				}	
			}
		};
		table2.setModel(model2);
		table2.setDefaultEditor(Object.class, null);
		model2.addColumn("Role");
		model2.addColumn("Items");
		scrollPane2.setViewportView(table2);
		
		table2.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent mouseEvent) {
		        JTable table2 =(JTable) mouseEvent.getSource();
		        Point point = mouseEvent.getPoint();
		        int row = table2.rowAtPoint(point);
		        if (mouseEvent.getClickCount() == 2 && table2.getSelectedRow() != -1) {
		            // your valueChanged overridden method
					int column = 0;
					int row1 = table2.getSelectedRow();
					String value1 = table2.getModel().getValueAt(row1, 0).toString();
					String value2 = table2.getModel().getValueAt(row1, 1).toString();

					String[] aa = {value1,value2};
					frame1.dispose();
		            frame1 = new JFrame();
		            p=new JPanel();	
		            p.revalidate();
		            p.repaint();
					edit_roles.main(aa);
					
					
		        }
		    }
		});
		
		
		
		refresh_users();
	    refresh_roles();

				
	}
	
	public static void refresh_users() {
		
		for(int i=0;i<model1.getRowCount();i++) {
			model1.removeRow(i);
		}
		model1.fireTableDataChanged();
		
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql ;
		try {
			sql = "SELECT Username,Roles,validity,created_date FROM UserLogin"; 

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			int i=0;
			 while (rs.next()) {
				 model1.addRow(new Object[0]);
				 model1.setValueAt(rs.getString("Username"), i, 0);		
				 model1.setValueAt(rs.getString("validity"), i, 1);
				 
				 final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			     final LocalDate firstDate = LocalDate.parse(rs.getString("created_date"), formatter);
			     final LocalDate secondDate = LocalDate.parse(get_date(), formatter);
			     final long days = ChronoUnit.DAYS.between(firstDate, secondDate);
			     //System.out.println("Days between: " + days);
			     
			     int diff_days = (int) (Integer.parseInt(rs.getString("validity")) - days);
			     
				 model1.setValueAt(String.valueOf(diff_days), i, 2);
				 model1.setValueAt(rs.getString("Roles"), i, 3);
				 
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
		    } catch(SQLException e1) {
		      System.out.println(e1.toString());
		    }
		}
	}
	
	public static void refresh_roles() {
		for(int i=0;i<model2.getRowCount();i++) {
			model2.removeRow(i);
		}
		model2.fireTableDataChanged();
		
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql ;
		try {
			sql = "SELECT RoleName,Items FROM Roles"; 

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			int i=0;
			 while (rs.next()) {
				 if(!rs.getString("RoleName").matches("")) {
				 model2.addRow(new Object[0]);
				 model2.setValueAt(rs.getString("RoleName"), i, 0);			
				 model2.setValueAt(rs.getString("Items"), i, 1);
				 i++;
				 }
			 }
			 model2.fireTableDataChanged();
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
		
    public static void main(String[] args) {    
    	
    	Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame1.getGraphicsConfiguration());
        int taskHeight=screenInsets.bottom;
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int height=d.height-taskHeight;
        int width=d.width;
        wid = (int)d.getWidth();
        hei = (int)(d.getHeight()-taskHeight);
        
        wid = (int) Math.round(0.3 * wid);
        hei = (int) Math.round(0.60 * hei);
        
        System.out.println(wid + "   dfvdvdv " + hei);
        
		frame1.setBounds(0,0,wid, hei);
		frame1.add(p);
   		frame1.getContentPane().add(new users_roles());
		frame1.setLocationRelativeTo(null);
		frame1.setResizable(false);
		frame1.setVisible(true);
		frame1.repaint();
		ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
		frame1.setIconImage(img.getImage());

		frame1.setTitle("Users and Roles");
			frame1.addWindowListener(new java.awt.event.WindowAdapter() {
			    @Override
			    public void windowClosing(java.awt.event.WindowEvent windowEvent)
			    {
			            frame1.dispose();
			            frame1 = new JFrame();
			            p=new JPanel();	
			            p.revalidate();
			            p.repaint();
			    }
			});
    	}
    public static String get_date() {
		DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		String date_time = dateFormat2.format(new Date()).toString();
		return date_time;
	}
	
}


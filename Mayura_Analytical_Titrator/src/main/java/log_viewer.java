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
  
public class log_viewer extends JPanel implements ItemListener
{
	static JFrame frame = new JFrame();
	static JTable table1;
	static JTable table2= new JTable();
	static JPanel p = new JPanel();


    static DefaultTableModel model ;
    static boolean apply_filter = false;
 
	static double wid,hei;
	static String f_date,t_date,c_date;
	
	public log_viewer() 
	{
		setLayout(null);
		initialize();
	}

	

	@SuppressWarnings("removal")
	public static void initialize() {
		frame.getContentPane().invalidate();
		frame.getContentPane().validate();
		frame.getContentPane().repaint();
		four_column();
		UtilDateModel model1 = new UtilDateModel();
		UtilDateModel model2 = new UtilDateModel();
		
		JLabel lblNewLabel = new JLabel("From Date");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.009 * wid)));
		lblNewLabel.setBounds((int) Math.round(0.0325* wid), (int) Math.round(0.0245 * hei), (int) Math.round(0.09114 * wid), (int) Math.round(0.02573 * hei));
		
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("To Date");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.009 * wid)));
		lblNewLabel_1.setBounds((int) Math.round(0.2604* wid), (int) Math.round(0.0245 * hei), (int) Math.round(0.09114 * wid), (int) Math.round(0.0159 * hei));
		frame.getContentPane().add(lblNewLabel_1);
		
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		
		
		JDatePanelImpl datePanel = new JDatePanelImpl(model1,p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel,new DateLabelFormatter());
		datePicker.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.009 * wid)));
		datePicker.setBounds((int) Math.round(0.0976 * wid), (int) Math.round(0.0245 * hei), (int) Math.round(0.09114 * wid), (int) Math.round(0.0490 * hei));
		
		frame.getContentPane().add(datePicker);
		
		
		JDatePanelImpl datePanel1 = new JDatePanelImpl(model2,p);
		JDatePickerImpl datePicker1 = new JDatePickerImpl(datePanel1,new DateLabelFormatter());
		datePicker1.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.009 * wid)));
		datePicker1.setBounds((int) Math.round(0.3125 * wid), (int) Math.round(0.0245 * hei), (int) Math.round(0.09114 * wid), (int) Math.round(0.0490 * hei));
		
		frame.getContentPane().add(datePicker1);
		
		insert_data();
		
		JButton btn_apply_filter = new JButton("Apply Filter");
		btn_apply_filter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int column = 0;
				int row = table1.getSelectedRow();

				Date datee = (Date) datePicker.getModel().getValue();	
				
				String from_month = ((model1.getMonth()+1) < 10 ? "0" : "") + (model1.getMonth()+1);
				String to_month = ((model2.getMonth()+1) < 10 ? "0" : "") + (model2.getMonth()+1);
				String from_day = (model1.getDay() < 10 ? "0" : "") + (model1.getDay());
				String to_day = (model2.getDay() < 10 ? "0" : "") + (model2.getDay());
				
				String from_date = model1.getYear()+"-"+from_month+"-"+from_day;
				String to_date = model2.getYear()+"-"+to_month+"-"+to_day;
			//	System.out.println("From Date = "+from_date);
			//	System.out.println("To Date = "+to_date);
				
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
				LocalDateTime now = LocalDateTime.now();  
			//	System.out.println("Todays Date = "+dtf.format(now)); 
				String cur_date = String.valueOf(dtf.format(now));
				filtered_data(from_date,to_date,cur_date );
			}
		});
		btn_apply_filter.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.009 * wid)));
		btn_apply_filter.setBounds((int) Math.round(0.455 * wid), (int) Math.round(0.0245 * hei), (int) Math.round(0.0976 * wid), (int) Math.round(0.0428 * hei));
		
		
		frame.getContentPane().add(btn_apply_filter);
		
		JButton btn_clear_filter = new JButton("Clear Filter");
		btn_clear_filter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insert_data();
			}
		});
		btn_clear_filter.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.009 * wid)));
		btn_clear_filter.setBounds((int) Math.round(0.585 * wid), (int) Math.round(0.0245 * hei), (int) Math.round(0.0976 * wid), (int) Math.round(0.0428 * hei));
		frame.getContentPane().add(btn_clear_filter);
		
		JButton btn_print = new JButton("Print");
		btn_print.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				print_audit_log();
			}
		});
		btn_print.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.009 * wid)));
		btn_print.setBounds((int) Math.round(0.895 * wid), (int) Math.round(0.0245 * hei), (int) Math.round(0.065 * wid), (int) Math.round(0.0428 * hei));

		//frame.getContentPane().add(btn_print);
		
    }
	public static void filtered_data(String from_date,String to_date,String cur_date) {
		try {
		if((!from_date.matches(cur_date) && !to_date.matches(cur_date))  || (!from_date.matches(cur_date) && to_date.matches(cur_date)) ) {
			model.getDataVector().removeAllElements();
			model.fireTableDataChanged();
			Connection con = DbConnection.connect();
			PreparedStatement ps = null;
			ResultSet rs = null;
			String sql ;
			try {

				sql = "SELECT date,time,uid,msg,note FROM audit_log where (date BETWEEN '"+from_date+"' AND '"+to_date+"')";
				ps = con.prepareStatement(sql);
				rs = ps.executeQuery();
				
				int i=0;
				 while (rs.next()) {
					// System.out.println(rs.getString("Trial_name") +  "\t" + rs.getString("Date") + "\t" +rs.getString("Value"));
					 model.addRow(new Object[0]);
					 model.setValueAt(rs.getString("date"), i, 0);			
					 model.setValueAt(rs.getString("time"), i, 1);
					 model.setValueAt(rs.getString("uid"), i, 2);			
					 model.setValueAt(rs.getString("msg"), i, 3);
					 model.setValueAt(rs.getString("note"), i, 4);			
					 i++;
				 }
				model.fireTableDataChanged();
				apply_filter = true;
				f_date = from_date;
				t_date = to_date;
				c_date = cur_date;
				
			}
			catch(SQLException e1) {
				JOptionPane.showMessageDialog(null,e1);
			}
			catch(NullPointerException ne) {
				JOptionPane.showMessageDialog(null,"No Data");
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
			JOptionPane.showMessageDialog(null, "Please select a proper date!");
		} 
	}
	
	public static void print_audit_log() {
		//twins
	}

	public static void insert_data() {
		try {
		model.getDataVector().removeAllElements();
		model.fireTableDataChanged();
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql ;
		try {

			sql = "SELECT * FROM audit_log";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			int i=0;
			 while (rs.next()) {
		//		 System.out.println(rs.getString("date") +  "\t" + rs.getString("time") + "\t" +rs.getString("uid"));
				 model.addRow(new Object[0]);
				 model.setValueAt(rs.getString("date"), i, 0);			
				 model.setValueAt(rs.getString("time"), i, 1);
				 model.setValueAt(rs.getString("uid"), i, 2);			
				 model.setValueAt(rs.getString("msg"), i, 3);
				 model.setValueAt(rs.getString("note"), i, 4);			
				 i++;
			 }
			model.fireTableDataChanged();
			apply_filter = false;
		}
		catch(SQLException e1) {
			JOptionPane.showMessageDialog(null,e1);
		}
		catch(NullPointerException ne) {
			JOptionPane.showMessageDialog(null,"No Data");
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
	catch(ArrayIndexOutOfBoundsException e2) {
		JOptionPane.showMessageDialog(null, "Please select a proper date!");
	} 
	}
	public static void add_row_to_four_column(int r ,String date,String uid,String msg,String note) {
		model.addRow(new Object[0]);
		model.setValueAt(date, r, 0);	
		model.setValueAt(uid, r, 1);			
		model.setValueAt(msg, r, 2);
		model.setValueAt(note, r, 3);	
		model.fireTableDataChanged();
	}
	
	public static void four_column() {
		
		
		JScrollPane scrollPane = new JScrollPane();
		//scrollPane.setBounds(20,80,((int)wid-40),((int)hei-160));
		scrollPane.setBounds((int) Math.round(0.013 * wid), (int) Math.round(0.098 * hei), (int) Math.round(0.95 * wid), (int) Math.round(0.803 * hei));

		
		frame.getContentPane().add(scrollPane);

		table1 = new JTable();
		table1.setFont(new Font("Times New Roman", Font.CENTER_BASELINE,17));
		table1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.011 * wid)));

		model = new DefaultTableModel() {
			public Class <?> getColumnClass(int column){
				switch(column) {
				case 0:
					return String.class;
				case 1:
					return String.class;
				case 2:
					return String.class;
				case 3:
					return String.class;
				default:
					return String.class;		
				}
			}
			 @Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }
		};
		table1.setModel(model);
		model.addColumn("Date");
		model.addColumn("Time");
		model.addColumn("UserId");
		model.addColumn("Message");
		model.addColumn("User Note");
		try {
		
		table1.setRowHeight((int) Math.round(0.0428* hei));
		}
		catch(IllegalArgumentException dsf) {}
		
	scrollPane.setViewportView(table1);
	
	
	table1.addMouseListener(new MouseAdapter() {
	    public void mousePressed(MouseEvent mouseEvent) {
	        JTable table =(JTable) mouseEvent.getSource();
	        Point point = mouseEvent.getPoint();
	        int row = table.rowAtPoint(point);
	        if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
	            // your valueChanged overridden method
				String note = table1.getModel().getValueAt(row, 4).toString();
				if(note.matches("")) {
					try {
				    	String result = (String)JOptionPane.showInputDialog(
					               frame,
					               "Enter the Note!", 
					               "Note",            
					               JOptionPane.PLAIN_MESSAGE,
					               null,            
					               null, 
					               ""
					            );
				    		 String temp_val = (result);
				    		 
				    		 Connection con = DbConnection.connect();
								PreparedStatement ps = null;
								try 
								{
									String sql = null;
									sql = "UPDATE audit_log SET note = ? WHERE date = ? AND time = ? AND uid = ? AND msg = ?";
									
									System.out.println("Checking");
									String date = table1.getModel().getValueAt(row, 0).toString();
									String time = table1.getModel().getValueAt(row, 1).toString();
									String uid = table1.getModel().getValueAt(row, 2).toString();
									String msg = table1.getModel().getValueAt(row, 3).toString();
									ps = con.prepareStatement(sql);
									ps.setString(1, temp_val);
									ps.setString(2, date);
									ps.setString(3, time);
									ps.setString(4, uid);
									ps.setString(5, msg);
									ps.executeUpdate();
									
									JOptionPane.showMessageDialog(null, "Updated Successfully");									
									System.out.println("Data Inserted!");
									if(apply_filter == false) {
										insert_data();
									}
									else {
										filtered_data(f_date, t_date, c_date);
									}
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
				    	catch(NullPointerException ne) {
					        JOptionPane.showMessageDialog(null , "Please enter a value!");
				    	}
				}
				else {
					JOptionPane.showMessageDialog(null, "Note already exists!");
				}

	        }
	    }
	});
	}
	
	
	
	public void itemStateChanged(ItemEvent e)
	    {
	       
	    }
    public static void main(String[] args) {    
    	
    	if(args.length != 0 )
    	{
    		//method_name,method_data,ar,batch,sample_name,normality_val,moisture_val,report_name,titrant_name
    		
    	}
    	
    	Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
        int taskHeight=screenInsets.bottom;
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int height=d.height-taskHeight;
        int width=d.width;
        wid = d.getWidth();
        hei = d.getHeight()-taskHeight;
        
        System.out.println(width + "   dfvdvdv " + hei);
        
        

        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setBounds(0,0,(int)wid,(int)hei);
		frame.add(p);
   		frame.getContentPane().add(new log_viewer());
		frame.setResizable(true);
		frame.setVisible(true);
		frame.repaint();
		frame.setTitle("Audit Log");
		ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
		frame.setIconImage(img.getImage());

			frame.addWindowListener(new java.awt.event.WindowAdapter() {
			    @Override
			    public void windowClosing(java.awt.event.WindowEvent windowEvent)
			    {
			            frame.dispose();
			            frame = new JFrame();
			            p=new JPanel();	
			            p.revalidate();
			            p.repaint();
			    }
			});
    	}
  
}


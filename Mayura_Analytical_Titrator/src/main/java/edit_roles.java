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
  
public class edit_roles extends JPanel 
{
	static JFrame frame = new JFrame();
	
	static JTable table1;

	static String exp="";
	static DefaultTableModel model1;
	static int wid=0,hei=0;
	static JPanel p = new JPanel();
	static String role_name = "";
	static ArrayList<String> role_item = new ArrayList<>(); 
	
	
	public edit_roles() 
	{
		setLayout(null);
		initialize();
	}

	

	@SuppressWarnings("removal")
	public static void initialize() {
		System.out.println("INITIALIZE");
		
		frame.getContentPane().invalidate();
		frame.getContentPane().validate();
		frame.getContentPane().repaint();		
		
		JLabel role_name_header = new JLabel("Role Name : "+role_name);
		role_name_header.setBounds((int) Math.round(0.07637 * wid), (int) Math.round(0.05 * hei), (int) Math.round(0.7 * wid), (int) Math.round(0.1 * hei));
		role_name_header.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.03 * wid)));
		frame.getContentPane().add(role_name_header);

		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBounds((int) Math.round(0.07637 * wid), (int) Math.round(0.2 * hei), (int) Math.round(0.8486 * wid), (int) Math.round(0.5 * hei));
		frame.getContentPane().add(scrollPane1);
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
		
		model1.addRow(new Object[0]);
		model1.setValueAt(role_item.contains("Method File (Create/Update/Delete)"), 0, 0);			
		model1.setValueAt("Method File (Create/Update/Delete)", 0, 1);
		
		model1.addRow(new Object[0]);
		model1.setValueAt(role_item.contains("Certify Reports"), 1, 0);			
		model1.setValueAt("Certify Reports", 1, 1);
		
		model1.addRow(new Object[0]);
		model1.setValueAt(role_item.contains("Analysis"), 2, 0);			
		model1.setValueAt("Analysis", 2, 1);
		
		model1.addRow(new Object[0]);
		model1.setValueAt(role_item.contains("Audit Trail"), 3, 0);			
		model1.setValueAt("Audit Trail", 3, 1);
		
		model1.addRow(new Object[0]);
		model1.setValueAt(role_item.contains("Recall Result"),4, 0);			
		model1.setValueAt("Recall Result", 4, 1);
		
		model1.addRow(new Object[0]);
		model1.setValueAt(role_item.contains("Burette Calibration"), 5, 0);			
		model1.setValueAt("Burette Calibration", 5, 1);
		
		model1.addRow(new Object[0]);
		model1.setValueAt(role_item.contains("SOP Upload"),6, 0);			
		model1.setValueAt("SOP Upload", 6, 1);
		
		model1.addRow(new Object[0]);
		model1.setValueAt(role_item.contains("Custom Formula"), 7, 0);			
		model1.setValueAt("Custom Formula", 7, 1);
		
		model1.addRow(new Object[0]);
		model1.setValueAt(role_item.contains("Device Data"), 8, 0);			
		model1.setValueAt("Device Data", 8, 1);
		
		model1.fireTableDataChanged();
		
		JButton btn_new = new JButton("Update Role");
		btn_new.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String items = "";
				int j=1;
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
				
				Connection con = DbConnection.connect();
				PreparedStatement ps = null;
				String sql ;
				try {
					sql = "UPDATE Roles SET Items = ? WHERE RoleName = ?";

					ps = con.prepareStatement(sql);
					ps.setString(1, items);
					ps.setString(2, role_name);
					ps.executeUpdate();
					
				}
				catch(SQLException e1) {
					JOptionPane.showMessageDialog(null,e1);
				}
				finally {
				    try{
				    ps.close();
				    con.close();
				    frame.dispose();
		            frame = new JFrame();
		            p=new JPanel();	
		            p.revalidate();
		            p.repaint();
				    users_roles.main(null);
				    } catch(SQLException e1) {
				      System.out.println(e1.toString());
				    }
				}		
				
				
			}
		});
		
		btn_new.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.025 * wid)));
		btn_new.setBounds((int) Math.round(0.38 * wid), (int) Math.round(0.75 * hei), (int) Math.round(0.25 * wid), (int) Math.round(0.1 * hei));
		frame.getContentPane().add(btn_new);
	}
	
		
    public static void main(String[] args) {    
    	
    	role_item.clear();
    	
    	if(args.length != 0) {
    		role_name = args[0];
    		String[] temp_items = args[1].split(",");
    		for(int i=0;i<temp_items.length;i++)
    		{
    			role_item.add(temp_items[i]);
    		}
    		
    	}
    	
    	Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
        int taskHeight=screenInsets.bottom;
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int height=d.height-taskHeight;
        int width=d.width;
        wid = (int)d.getWidth();
        hei = (int)(d.getHeight()-taskHeight);
        
        wid = (int) Math.round(0.3 * wid);
        hei = (int) Math.round(0.45 * hei);
        
        System.out.println(wid + "   dfvdvdv " + hei);
        
		frame.setBounds(0,0,wid, hei);
		frame.add(p);
   		frame.getContentPane().add(new edit_roles());
		frame.setLocationRelativeTo(null);
		frame.setResizable(true);
		frame.setVisible(true);
		frame.repaint();
		frame.setTitle("Update Role");	
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
				    users_roles.main(null);
			    }
			});
    	}
  
}


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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
  
public class open_report extends JPanel 
{
	static JFrame frame = new JFrame();
	//static open_report frame;
	static JTable table1= new JTable();
	static String exp="";
	static DefaultTableModel model;
	static int wid=0,hei=0;
	private static JTextField tf_search;
	static ResultSet rs = null;
	static boolean apply_filter = false;
	static String f_date,t_date,c_date,user_name,permission;
	static JPanel p = new JPanel();


	
	public open_report() 
	{
		setLayout(null);
		initialize();
	}

	

	@SuppressWarnings("removal")
	public static void initialize() {
		frame.getContentPane().invalidate();
		frame.getContentPane().validate();
		frame.getContentPane().repaint();
		JLabel lblNewLabel = new JLabel("From Date");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.02 * wid)));
		lblNewLabel.setBounds((int) Math.round(0.07637 * wid), (int) Math.round(0.021 * hei), (int) Math.round(0.198 * wid), (int) Math.round(0.028 * hei));
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("To Date");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.02 * wid)));
		lblNewLabel_1.setBounds((int) Math.round(0.3818 * wid), (int) Math.round(0.021 * hei), (int) Math.round(0.198 * wid), (int) Math.round(0.028 * hei));
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_4 = new JLabel("Search");
		lblNewLabel_4.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.0254 * wid)));
		lblNewLabel_4.setBounds((int) Math.round(0.5304 * wid), (int) Math.round(0.1836 * hei), (int) Math.round(0.198 * wid), (int) Math.round(0.0265 * hei));
		frame.getContentPane().add(lblNewLabel_4);
		
		UtilDateModel model1 = new UtilDateModel();
		UtilDateModel model2 = new UtilDateModel();

		Properties pp = new Properties();
		pp.put("text.today", "Today");
		pp.put("text.month", "Month");
		pp.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model1,pp);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel,new DateLabelFormatter());		
		datePicker.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.02 * wid)));
		datePicker.setBounds((int) Math.round(0.0763 * wid), (int) Math.round(0.0571 * hei), (int) Math.round(0.198 * wid), (int) Math.round(0.08163 * hei));		
		frame.getContentPane().add(datePicker);
		
		JDatePanelImpl datePanel1 = new JDatePanelImpl(model2,pp);
		JDatePickerImpl datePicker1 = new JDatePickerImpl(datePanel1,new DateLabelFormatter());
		datePicker1.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.02 * wid)));
		datePicker1.setBounds((int) Math.round(0.3889 * wid), (int) Math.round(0.0571 * hei), (int) Math.round(0.198 * wid), (int) Math.round(0.08163 * hei));
		frame.getContentPane().add(datePicker1);
		
		
		tf_search = new JTextField();
		tf_search.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.02 * wid)));
		tf_search.setBounds((int) Math.round(0.6364 * wid), (int) Math.round(0.1632 * hei), (int) Math.round(0.2828 * wid), (int) Math.round(0.06122 * hei));
		
		frame.getContentPane().add(tf_search);
		
		tf_search.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                //System.out.println("keyTyped");
            }

            @Override
            public void keyReleased(KeyEvent e) {
          	
            	try {
					int column = 0;
					int row = table1.getSelectedRow();
	
					Date datee = (Date) datePicker.getModel().getValue();	
					
					String from_month = ((model1.getMonth()+1) < 10 ? "0" : "") + (model1.getMonth()+1);
					String to_month = ((model2.getMonth()+1) < 10 ? "0" : "") + (model2.getMonth()+1);
					String from_day = (model1.getDay() < 10 ? "0" : "") + (model1.getDay());
					String to_day = (model2.getDay() < 10 ? "0" : "") + (model2.getDay());
					
					String from_date = from_day+"-"+from_month+"-"+model1.getYear();
					String to_date = to_day+"-"+to_month+"-"+model2.getYear();
					
					
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");  
					LocalDateTime now = LocalDateTime.now();  
					String cur_date = String.valueOf(dtf.format(now));
					
					if((!from_date.matches(cur_date) && !to_date.matches(cur_date))  || (!from_date.matches(cur_date) && to_date.matches(cur_date)) ) {
						model.getDataVector().removeAllElements();
						model.fireTableDataChanged();
						
						File directoryPath = new File("C:\\SQLite\\Report\\");
						String contents[] = directoryPath.list();
						List<String> reports = Arrays.asList(contents);
						
						int j = 0;
						for (int i=0;i<reports.size();i++) 
						{
							Path path = Paths.get("C:\\SQLite\\Report\\"+reports.get(i));
						    BasicFileAttributes view = null;
							try {
								view = Files.getFileAttributeView(path, BasicFileAttributeView.class).readAttributes();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
							
							Date file_date = format.parse(String.valueOf(format.format(new Date(view.lastModifiedTime().toMillis()))));
							Date fromDate = format.parse(from_date);
							Date toDate = format.parse(to_date);
							System.out.println("From Date = "+from_date+"    :     From date = file_date.compareTo(fromDate) >= 0   : "+file_date.compareTo(fromDate));
							System.out.println("To date = file_date.compareTo(toDate) >= 0   : "+file_date.compareTo(toDate));

							if((file_date.compareTo(fromDate) >= 0) && (file_date.compareTo(toDate) <= 0) && reports.get(i).toLowerCase().contains(tf_search.getText().toLowerCase().toString()) )
							{
								model.addRow(new Object[0]);
								model.setValueAt(reports.get(i), j, 0);
								format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
								model.setValueAt(format.format(new Date(view.lastModifiedTime().toMillis())), j, 1);
								j++;
							}
						}
					}
					else if(from_date.matches(cur_date) && to_date.matches(cur_date)){
						model.getDataVector().removeAllElements();
						model.fireTableDataChanged();
						
						File directoryPath = new File("C:\\SQLite\\Report\\");
						String contents[] = directoryPath.list();
						List<String> reports = Arrays.asList(contents);
						
						int j = 0;
						for (int i=0;i<reports.size();i++) 
						{
							Path path = Paths.get("C:\\SQLite\\Report\\"+reports.get(i));
						    BasicFileAttributes view = null;
							try {
								view = Files.getFileAttributeView(path, BasicFileAttributeView.class).readAttributes();
							} catch (IOException e1) {
								e1.printStackTrace();
							}							
							
							if(reports.get(i).toLowerCase().contains(tf_search.getText().toLowerCase().toString()))
							{
								model.addRow(new Object[0]);
								model.setValueAt(reports.get(i), j, 0);
								SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
								model.setValueAt(format.format(new Date(view.lastModifiedTime().toMillis())), j, 1);
								j++;
							}
						}
						
					}
					
					model.fireTableDataChanged();

				}
				catch(ArrayIndexOutOfBoundsException e2) {
					JOptionPane.showMessageDialog(null, "Please Select A Result File");
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                //System.out.println("keyPressed");
            }
        }); 
		
		JButton btnNewButton = new JButton("Open");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				int column = 0;
				int row = table1.getSelectedRow();
				String value = table1.getModel().getValueAt(row, column).toString();
				System.out.println("Selected Row = "+value);
				try  
				{  
					File file = new File("C:\\SQLite\\Report\\"+value);   
					if(!Desktop.isDesktopSupported())
					{  
						JOptionPane.showMessageDialog(null, "System does not support Auto-Opening of files. Please open the files manually from Reports Folder!");
						return;  
					} 
					else {
						Desktop desktop = Desktop.getDesktop();  
						if(file.exists())         
						desktop.open(file); 
					}
					
				}  
				catch(Exception ej)  
				{  
					ej.printStackTrace();  
				} 
				
				frame.dispose();
				frame = new JFrame();
		        p=new JPanel();	
		        p.invalidate();
		        p.revalidate();
		        p.repaint();

				}
				catch(ArrayIndexOutOfBoundsException e2) {
					JOptionPane.showMessageDialog(null, "Please Select A Result File");
				} 
			}
		});
		
		btnNewButton.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.02 * wid)));
		btnNewButton.setBounds((int) Math.round(0.4 * wid), (int) Math.round(0.82 * hei), (int) Math.round(0.1471 * wid), (int) Math.round(0.0755 * hei));
		frame.getContentPane().add(btnNewButton);
		
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
					
					String from_date = from_day+"-"+from_month+"-"+model1.getYear();
					String to_date = to_day+"-"+to_month+"-"+model2.getYear();
					
					
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");  
					LocalDateTime now = LocalDateTime.now();  
					String cur_date = String.valueOf(dtf.format(now));
					
					if((!from_date.matches(cur_date) && !to_date.matches(cur_date))  || (!from_date.matches(cur_date) && to_date.matches(cur_date)) ) {
						model.getDataVector().removeAllElements();
						model.fireTableDataChanged();
						
						File directoryPath = new File("C:\\SQLite\\Report\\");
						String contents[] = directoryPath.list();
						List<String> reports = Arrays.asList(contents);
						
						
						int j = 0;
						for (int i=0;i<reports.size();i++) 
						{
							Path path = Paths.get("C:\\SQLite\\Report\\"+reports.get(i));
						    BasicFileAttributes view = null;
							try {
								view = Files.getFileAttributeView(path, BasicFileAttributeView.class).readAttributes();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
							
							Date file_date = format.parse(String.valueOf(format.format(new Date(view.lastModifiedTime().toMillis()))));
							Date fromDate = format.parse(from_date);
							Date toDate = format.parse(to_date);
							System.out.println("From Date = "+from_date+"    :     From date = file_date.compareTo(fromDate) >= 0   : "+file_date.compareTo(fromDate));
							System.out.println("To date = file_date.compareTo(toDate) >= 0   : "+file_date.compareTo(toDate));

							if((file_date.compareTo(fromDate) >= 0) && (file_date.compareTo(toDate) <= 0))
							{
								model.addRow(new Object[0]);
								model.setValueAt(reports.get(i), j, 0);
								format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
								model.setValueAt(format.format(new Date(view.lastModifiedTime().toMillis())), j, 1);
								j++;
							}
						}
					}
					model.fireTableDataChanged();

				}
				catch(ArrayIndexOutOfBoundsException e2) {
					JOptionPane.showMessageDialog(null, "Please Select A Result File");
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		});
		
		btn_apply_filter.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.02 * wid)));
		btn_apply_filter.setBounds((int) Math.round(0.7072 * wid), (int) Math.round(0.04897 * hei), (int) Math.round(0.2121 * wid), (int) Math.round(0.06530 * hei));
		frame.getContentPane().add(btn_apply_filter);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds((int) Math.round(0.07637 * wid), (int) Math.round(0.2653 * hei), (int) Math.round(0.8486 * wid), (int) Math.round(0.5102 * hei));
		frame.getContentPane().add(scrollPane);
		table1 = new JTable();
		table1.setRowHeight((int) Math.round(0.06 * hei));
		table1.setFont(new Font("Arial", Font.BOLD, (int) Math.round(0.02 * wid)));


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

		model.addColumn("Report Name");
		model.addColumn("Date");
	
		File directoryPath = new File("C:\\SQLite\\Report\\");
		String contents[] = directoryPath.list();
		List<String> reports = Arrays.asList(contents);
		
		for (int i=0;i<reports.size();i++) 
		{
			System.out.println(reports.get(i));
		}
		
		for (int i=0;i<reports.size();i++) 
		{
			Path path = Paths.get("C:\\SQLite\\Report\\"+reports.get(i));
		    BasicFileAttributes view = null;
			try {
				view = Files.getFileAttributeView(path, BasicFileAttributeView.class).readAttributes();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			model.addRow(new Object[0]);
			model.setValueAt(reports.get(i), i, 0);
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			model.setValueAt(format.format(new Date(view.lastModifiedTime().toMillis())), i, 1);
		}
		
		scrollPane.setViewportView(table1);
	}
		
    public static void main(String[] args) {    
    	
//    	if(args.length != 0 )
//    	{
//    		System.out.println("User Name = "+args[0]);
//    		System.out.println("permissions = "+args[1]);
//    		user_name = args[0];
//    		permission = args[1];
//    	}
    	
    	Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
        int taskHeight=screenInsets.bottom;
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int height=d.height-taskHeight;
        int width=d.width;
        wid = (int)d.getWidth();
        hei = (int)(d.getHeight()-taskHeight);
        
        wid = (int) Math.round(0.46 * wid);
        hei = (int) Math.round(0.60 * hei);
        
        System.out.println(wid + "   dfvdvdv " + hei);
        
       // frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setBounds(0,0,wid, hei);
		frame.add(p);
   		frame.getContentPane().add(new open_report());
		frame.setLocationRelativeTo(null);

		frame.setResizable(true);
		frame.setVisible(true);
		frame.repaint();
		frame.setTitle("Open Reports");
		ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
		frame.setIconImage(img.getImage());

			frame.addWindowListener(new java.awt.event.WindowAdapter() {
			    @Override
			    public void windowClosing(java.awt.event.WindowEvent windowEvent)
			    {
			    		System.out.println("Closinggg opn KF result");
			            frame.dispose();
			            frame = new JFrame();
			            p=new JPanel();	
			            p.revalidate();
			            p.repaint();
			    }
			});
    	}
  
}


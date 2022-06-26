package main.java;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.management.modelmbean.ModelMBean;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.FloatDimension;

import com.fazecast.jSerialComm.SerialPort;
import com.itextpdf.text.DocumentException;

  
public class DrawReport_pot extends JPanel{
	static XYPlot plot;
	static XYPlot plot1;
	static XYPlot plot2;
	static int wid=0,hei=0;
	static JFrame frame1 = new JFrame();
	static JTable table1;
	static JTable table2= new JTable();
    static JRadioButton b, b1;
	static JPanel p = new JPanel();
	static JLabel kf_mv_display,result;
	static JFrame frame2 = new JFrame();
    private JTextField textField;
    static JTable table11,table12 ;
    static DefaultTableModel model,model2 ;
    static JTextArea display;
    static JPanel panel_result;
    static JLabel experiment_performing ;
	static JScrollPane scrollPane = new JScrollPane();
	static JScrollPane scrollPane2 = new JScrollPane();
	static JTextField remarks_input_pot = new JTextField();
    static String[] params = {"User Name","Date","Time","Method Name","Pre Dose","Stir Time","Max Vol","Blank Vol","Burette Factor","Threshold","Filter","Dosage Rate","No of Trials","Factor 1","Factor 2","Factor 3","Factor 4","EP Select","Formula No","Tendency","Result Unit","SOP","AR No","Batch No","Sample Name","Normality","Moisture%","Report Name","Titrant Name","Certified By"};
    static String report_name = "";
    static String parameter = "";
    static String details = "";
    static String graphX = "";
    static String graphY = "";
    static String header = "";
    static String trial_data = "";
    static String results = "";
    static String remarks = "";
    static String[] threshold = new String[5];
    static String[] timings = new String[5];

    static JLabel result_header1,result_header2,result_header3,rsd1,rsd2,rsd3;
    static ChartPanel chartPanel ;
	static JFreeChart chart ;
	private static final XYSeries series = new XYSeries("mL,mV");
	static int cur=0,prev=0;
	static JRadioButton radio_btn_with_graph,radio_btn_without_graph;
	static String[] tempx,tempy;
	static JCheckBox checkBox1;
	static boolean certify = false;
	static boolean certify_now = false;
	static String user_name = "",u_permissions = "";
	static boolean already_certified = false,already_remarked = false;
	static JButton btn_update_pot_remarks;

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

	public DrawReport_pot() {
		setLayout(null);		
		JLabel experiment = new JLabel("Potentiometry");
		experiment.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.013 * wid)));
		experiment.setBounds((int) Math.round(0.013 * wid), (int) Math.round(0.02 * hei), (int) Math.round(0.15 * wid), (int) Math.round(0.03 * hei));
		add(experiment);
		
		JLabel Method_param = new JLabel("Method Parameters");
		Method_param.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.013 * wid)));
		Method_param.setBounds((int) Math.round(0.16 * wid), (int) Math.round(0.02 * hei), (int) Math.round(0.225 * wid), (int) Math.round(0.0306 * hei));
		add(Method_param);
		
		JLabel exp_details = new JLabel("Experiment Details");
		exp_details.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.013 * wid)));
		exp_details.setBounds((int) Math.round(0.013 * wid), (int) Math.round(0.52 * hei), (int) Math.round(0.195 * wid), (int) Math.round(0.0392 * hei));
		add(exp_details);
		
		display = new JTextArea();
		display.setEditable(false); 
		display.setFont(display.getFont().deriveFont(15f));
		JScrollPane scroll = new JScrollPane(display);
		scroll.setBounds((int) Math.round(0.013 * wid), (int) Math.round(0.58 * hei), (int) Math.round(0.35 * wid), (int) Math.round(0.35 * hei));	
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(scroll);
		
		result_header1 = new JLabel("Result1 :");
		result_header1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.011 * wid)));
		result_header1.setBounds((int) Math.round(0.4 * wid), (int) Math.round(0.78 * hei), (int) Math.round(0.195 * wid), (int) Math.round(0.0392 * hei));
		add(result_header1);
		rsd1 = new JLabel("Rsd1 :");
		rsd1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.011 * wid)));
		rsd1.setBounds((int) Math.round(0.4 * wid), (int) Math.round(0.81 * hei), (int) Math.round(0.195 * wid), (int) Math.round(0.0392 * hei));
		add(rsd1);
		
		result_header2 = new JLabel("Result2 :");
		result_header2.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.011 * wid)));
		result_header2.setBounds((int) Math.round(0.6 * wid), (int) Math.round(0.78 * hei), (int) Math.round(0.195 * wid), (int) Math.round(0.0392 * hei));
		add(result_header2);
		rsd2 = new JLabel("Rsd2 :");
		rsd2.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.011 * wid)));
		rsd2.setBounds((int) Math.round(0.6 * wid), (int) Math.round(0.81 * hei), (int) Math.round(0.195 * wid), (int) Math.round(0.0392 * hei));
		add(rsd2);
		
		result_header3 = new JLabel("Result3 :");
		result_header3.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.011 * wid)));
		result_header3.setBounds((int) Math.round(0.8 * wid), (int) Math.round(0.78 * hei), (int) Math.round(0.195 * wid), (int) Math.round(0.0392 * hei));
		add(result_header3);
		rsd3 = new JLabel("Rsd3 :");
		rsd3.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.011 * wid)));
		rsd3.setBounds((int) Math.round(0.8 * wid), (int) Math.round(0.81 * hei), (int) Math.round(0.195 * wid), (int) Math.round(0.0392 * hei));
		add(rsd3);

		radio_btn_with_graph = new JRadioButton("With Graph");
		radio_btn_with_graph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//four_column_kff();			
			}
		});
		radio_btn_with_graph.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.01 * wid)));
		radio_btn_with_graph.setBounds((int) Math.round(0.5 * wid), (int) Math.round(0.85 * hei), (int) Math.round(0.1 * wid),(int) Math.round(0.036 * hei));
		add(radio_btn_with_graph);

		radio_btn_without_graph = new JRadioButton("Without Graph");
		radio_btn_without_graph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		radio_btn_without_graph.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.01 * wid)));
		radio_btn_without_graph.setBounds((int) Math.round(0.65 * wid), (int) Math.round(0.85 * hei), (int) Math.round(0.1 * wid),(int) Math.round(0.036 * hei));
		add(radio_btn_without_graph);
		
		ButtonGroup bg1 = new ButtonGroup();
		bg1.add(radio_btn_with_graph);
		bg1.add(radio_btn_without_graph);
		
		radio_btn_with_graph.setSelected(true);
		
		JLabel Remarks = new JLabel("Remarks : ");
		Remarks.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.013 * wid)));
		Remarks.setBounds((int) Math.round(0.4 * wid), (int) Math.round(0.9 * hei), (int) Math.round(0.1 * wid), (int) Math.round(0.03 * hei));
		add(Remarks);
		
		remarks_input_pot = new JTextField(remarks);
		remarks_input_pot.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.013 * wid)));
		remarks_input_pot.setBounds((int) Math.round(0.48 * wid), (int) Math.round(0.9 * hei), (int) Math.round(0.2 * wid),
				(int) Math.round(0.04 * hei));
		add(remarks_input_pot);
		
		remarks_input_pot.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (remarks_input_pot.getText().toString().contains(",")) {
					StringBuffer sb= new StringBuffer(remarks_input_pot.getText().toString());  
					sb.deleteCharAt(sb.length()-1); 
					JOptionPane.showMessageDialog(null, "Remarks connot contain comma(',')!");
					remarks_input_pot.setText(sb.toString());
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		
		if (certify == true) {
			checkBox1 = new JCheckBox();
			checkBox1.setText("Certify");
			checkBox1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.012 * wid)));
			checkBox1.setBounds((int) Math.round(0.7 * wid), (int) Math.round(0.9 * hei), (int) Math.round(0.08 * wid),
					(int) Math.round(0.04 * hei));
			add(checkBox1);
			checkBox1.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == 1) {
						certify_now = true;
					} else {
						certify_now = false;
					}
				}
			});
		}		
		
		JButton btn_print_kf_result = new JButton("Print");
		btn_print_kf_result.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		btn_print_kf_result.setBounds((int) Math.round(0.902 * wid), (int) Math.round(0.9 * hei), (int) Math.round(0.07 * wid), (int) Math.round(0.0392 * hei));
		add(btn_print_kf_result);
		btn_print_kf_result.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				print_pot_result();
			}
		});
		
		btn_update_pot_remarks = new JButton("Update");
		btn_update_pot_remarks.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0082 * wid)));
		btn_update_pot_remarks.setBounds((int) Math.round(0.815 * wid), (int) Math.round(0.9 * hei), (int) Math.round(0.07 * wid), (int) Math.round(0.0392 * hei));
		add(btn_update_pot_remarks);
		btn_update_pot_remarks.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				update_pot_remarks();
			}
		});
		  chartPanel = new ChartPanel(createChart(createDataset())) {

	            @Override
	            public Dimension getPreferredSize() {
	                return new Dimension(640, 360);
	            }
	        };
	        chartPanel.setBounds((int) Math.round(0.4 * wid), (int) Math.round(0.024 * hei), (int) Math.round(0.58 * wid), (int) Math.round(0.49 * hei)); // (int) Math.round(0.45 * hei)
	        add(chartPanel);
	        
		initialize();
	}
	
    private XYDataset createDataset() { 	
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }

    private JFreeChart createChart(final XYDataset dataset) {
        chart = ChartFactory.createXYLineChart(
            "Potentiometry", // chart title
            "mL", // x axis label
            "mV", // y axis label
            dataset, // data
            PlotOrientation.VERTICAL,
            false, // include legend
            true, // tooltips
            false // urls
        );
        return chart;
    }
    
//	public static void update_pot_remarks() {
//		Connection con = DbConnection.connect();
//		PreparedStatement ps = null;
//		try 
//		{
//			String sql = "UPDATE potentiometry SET remarks = ? WHERE report_name = ?";
//				
//			ps = con.prepareStatement(sql);
//			ps.setString(1, remarks_input_pot.getText().toString());
//			ps.setString(2, report_name);
//			ps.executeUpdate();
//			JOptionPane.showMessageDialog(null, "KF Remarks Updated Successfully!");
//		}
//		catch(SQLException e1) {
//			System.out.println(e1.toString());
//		}
//		finally {
//		    try{
//		    ps.close();
//		    con.close();
//		    } catch(SQLException e1) {
//		      System.out.println(e1.toString());
//		    }
//		}	
//	}
    
    
    public static void update_pot_remarks() {
    	String user_text="";
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql = null;
		String temp_remarks = "";
		try {
			System.out.println("Certify Now : "+certify_now+"   already Certifed :"+ already_certified);
			if (certify_now == true && already_certified == false) {
				String update_params = "";
				String[] temp_param = parameter.split(",");

				for (int i = 0; i < 30; i++) {
					if (i == 0) {
						update_params = update_params + temp_param[i];
					} else {
						if (i == 29) {
							update_params = update_params + "," + user_name;
						} else {
							update_params = update_params + "," + temp_param[i];
						}
					}
				}
				if (remarks_input_pot.getText().toString().matches("")) {
					temp_remarks = "No Remarks";
				} else {
					temp_remarks = remarks_input_pot.getText().toString();
				}
				String push_remarks = "";
				String[] temp_remarks_arr = remarks.split(",");
				
				for(int i=0;i<temp_remarks_arr.length;i++) {
					if(i==0) {
						push_remarks = temp_remarks;
					}
					else {
						push_remarks = push_remarks +","+temp_remarks_arr[i];
					}
				}
				
				sql = "UPDATE potentiometry SET remarks = ?,parameters=? WHERE report_name = ?";
				ps = con.prepareStatement(sql);
				ps.setString(1, push_remarks);
				ps.setString(2, update_params);
				ps.setString(3, report_name);
				
				user_text = report_name +" Certified By: "+user_name;
			} else if(already_certified == true) {
				
				
				if (remarks_input_pot.getText().toString().matches("")) {
					temp_remarks = "No Remarks";
				} else {
					temp_remarks = remarks_input_pot.getText().toString();
				}
				
				String push_remarks = "";
				String[] temp_remarks_arr = remarks.split(",");
				
				for(int i=0;i<temp_remarks_arr.length;i++) {
					if(i==0) {
						push_remarks = temp_remarks;
					}
					else {
						push_remarks = push_remarks +","+temp_remarks_arr[i];
					}
				}
				sql = "UPDATE potentiometry SET remarks = ? WHERE report_name = ?";
				ps = con.prepareStatement(sql);
				ps.setString(1, push_remarks);
				ps.setString(2, report_name);
				user_text = user_text+" Remarks Updated";
			}
			ps.executeUpdate();
			JOptionPane.showMessageDialog(null, "Pot Remarks Updated Successfully!");
			frame1.dispose();
			frame1 = new JFrame();
			p = new JPanel();
			p.invalidate();
			p.revalidate();
			p.repaint();
		} catch (SQLException e1) {
			System.out.println(e1.toString());
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e1) {
				System.out.println(e1.toString());
			}
		}
		
		try {
			audit_log_push.push_to_audit(get_date(), get_time() ,user_name,user_text);
		} catch (ParseException e1) {e1.printStackTrace();}
		
		String[] aa = { report_name,user_name,u_permissions};
		DrawReport_pot.main(aa);

	}
    
    public static String get_company_details() {
		String data = "";
			Connection con = DbConnection.connect();
			PreparedStatement ps = null;
			ResultSet rs = null;
			String sql;
			try {
				sql = "SELECT * FROM company_data";
				ps = con.prepareStatement(sql);
				rs = ps.executeQuery();
				data = data + rs.getString("instrument_id");
				data = data + ">" + rs.getString("company_name");
				data = data + ">" + rs.getString("company_address");						
			} catch (SQLException e1) {
				// JOptionPane.showMessageDialog(null,e1);
			} finally {
				try {
					ps.close();
					con.close();
				} catch (SQLException e1) {
					System.out.println(e1.toString());
				}
			}
			return data;
	}
	
	public static void print_pot_result() {
		
		String company_details = get_company_details();
		String parameter = "",value = "",trials="",trial_results="",trial_rsds="",instrument_id = "";
		String[] company_arr = company_details.split(">");		
		String company_details_temp = company_arr[1]+">"+company_arr[2];
		boolean print_graph = false;
		
		if(radio_btn_with_graph.isSelected()) {
			print_graph = true;
			File dir = new File("C:\\SQLite\\chart");
			String contents[] = dir.list();
			for(File file: dir.listFiles()) {
			    if (!file.isDirectory()) 
			        file.delete();
			}
			save_graphs();
		} 
		
		parameter = parameter+"Potentiometry Report";
		//value = header;
		
		parameter = parameter +",User Name : "+table2.getValueAt(0, 1);
		parameter = parameter +",Instrument Id : "+company_arr[0];
		parameter = parameter +",Date : "+table2.getValueAt(1, 1);
		parameter = parameter +",Time : "+table2.getValueAt(2, 1);
		parameter = parameter +",Method File : "+table2.getValueAt(3, 1);
		parameter = parameter +",Batch Number : "+table2.getValueAt(23, 1);
		parameter = parameter +", AR Number : "+table2.getValueAt(22, 1);
		parameter = parameter +", Sample Name: "+table2.getValueAt(24, 1);
		parameter = parameter +",Dosage Speed : "+table2.getValueAt(11, 1);
		parameter = parameter +",EP Select : "+table2.getValueAt(17, 1);
		parameter = parameter +",No. of Trials : "+table2.getValueAt(12, 1);
		parameter = parameter+",Titrant Name : "+table2.getValueAt(28, 1);
		parameter = parameter+",Normality : "+table2.getValueAt(25, 1);
		parameter = parameter+",Formula No. : "+table2.getValueAt(18, 1);
		parameter = parameter+","+table2.getValueAt(0, 1);
		parameter = parameter+","+table2.getValueAt(29, 1);
		String[] remarks_final = remarks.split(","); 
		try {
			boolean graph_print = false;
			
			if(radio_btn_with_graph.isSelected()) {
				try {
					audit_log_push.push_to_audit(get_date(), get_time() ,user_name,"Report: "+report_name+" Printed - With Graph");
				} catch (ParseException e1) {e1.printStackTrace();}
				graph_print = true;
			}
			else {
				try {
					audit_log_push.push_to_audit(get_date(), get_time() ,user_name,"Report: "+report_name+" Printed - Without Graph");
				} catch (ParseException e1) {e1.printStackTrace();}
			}
			report.generate_report_pot(company_details_temp, parameter, header, trial_data,graph_print,results,remarks_final[0],
					threshold,timings,table2.getValueAt(5, 1).toString(),table2.getValueAt(4, 1).toString(),
					table2.getValueAt(3, 1).toString(),table2.getValueAt(11, 1).toString(),table2.getValueAt(17, 1).toString(),table2.getValueAt(27, 1).toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void save_graphs() {
		
		String[] trial_arr = trial_data.split(":");
		for(int i=0;i<trial_arr.length;i++) {
		
		  String [] params_data = parameter.split(",");
		  String dx[] = tempx[i].split(",");
		  String dy[] = tempy[i].split(",");
		  series.clear();
		  
		  for(int j=0;j<dx.length;j++) {
			       series.addOrUpdate(Double.parseDouble(dx[j]),Double.parseDouble(dy[j]));
		  }
		  
		  try {
			  plot.clearDomainMarkers();
			  plot1.clearDomainMarkers();
			  plot2.clearDomainMarkers();
		  }
		  catch(NullPointerException ds) {}
		  
		  if(params_data[18].matches("1")) {
			  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
			  }
			  catch(NumberFormatException dcw){}
		  }
		  if(params_data[18].matches("2")) {
			  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
			  }
			  catch(NumberFormatException dcw){}
		  }
		  if(params_data[18].matches("3")) {
			  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
			  }
		  catch(NumberFormatException dcw){}
		  }
		  if(params_data[18].matches("4")) {
			  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
				  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 2).toString()));
				  marker1.setPaint(Color.blue);
				  plot1 = (XYPlot) chart.getPlot();
				  plot1.addDomainMarker(marker1);
			  }
		  catch(NumberFormatException dcw){}
		  }
		  if(params_data[18].matches("5")) {
			  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
				  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 2).toString()));
				  marker1.setPaint(Color.blue);
				  plot1 = (XYPlot) chart.getPlot();
				  plot1.addDomainMarker(marker1);
			  }
		  catch(NumberFormatException dcw){}
		  }
		  if(params_data[18].matches("6")) {
			  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
				  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 2).toString()));
				  marker1.setPaint(Color.blue);
				  plot1 = (XYPlot) chart.getPlot();
				  plot1.addDomainMarker(marker1);
			  }
		  catch(NumberFormatException dcw){}
		  }
		  if(params_data[18].matches("7")) {
			  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 1).toString()));
			  marker.setPaint(Color.blue);
			  plot = (XYPlot) chart.getPlot();
			  plot.addDomainMarker(marker);
			  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 2).toString()));
			  marker1.setPaint(Color.blue);
			  plot1 = (XYPlot) chart.getPlot();
			  plot1.addDomainMarker(marker1);
			  ValueMarker marker2 = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 3).toString()));
			  marker2.setPaint(Color.blue);
			  plot2 = (XYPlot) chart.getPlot();
			  plot2.addDomainMarker(marker2);
			  System.out.println("Seevennnnnnnn "+marker.getValue());
		  }
		  if(params_data[18].matches("8")) {
			  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
				  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 2).toString()));
				  marker1.setPaint(Color.blue);
				  plot1 = (XYPlot) chart.getPlot();
				  plot1.addDomainMarker(marker1);
			  }
		  catch(NumberFormatException dcw){}
		  }
		  if(params_data[18].matches("9")) {
			  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
				  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 2).toString()));
				  marker1.setPaint(Color.blue);
				  plot1 = (XYPlot) chart.getPlot();
				  plot1.addDomainMarker(marker1);
			  }
		  catch(NumberFormatException dcw){}
		  }
		  if(params_data[18].matches("10")) {
			  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 1).toString()));
			  marker.setPaint(Color.blue);
			  plot = (XYPlot) chart.getPlot();
			  plot.addDomainMarker(marker);
			  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 2).toString()));
			  marker1.setPaint(Color.blue);
			  plot1 = (XYPlot) chart.getPlot();
			  plot1.addDomainMarker(marker1);
			  ValueMarker marker2 = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 3).toString()));
			  marker2.setPaint(Color.blue);
			  plot2 = (XYPlot) chart.getPlot();
			  plot2.addDomainMarker(marker2);
		  }
		  if(params_data[18].matches("11")) {
			  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
				  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 2).toString()));
				  marker1.setPaint(Color.blue);
				  plot1 = (XYPlot) chart.getPlot();
				  plot1.addDomainMarker(marker1);
			  }
		  catch(NumberFormatException dcw){}
		  }
		  if(params_data[18].matches("12")) {
			  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
				  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(i, 2).toString()));
				  marker1.setPaint(Color.blue);
				  plot1 = (XYPlot) chart.getPlot();
				  plot1.addDomainMarker(marker1);
			  }
		  catch(NumberFormatException dcw){}
		  }
		  
		  
		try {
		OutputStream out = new FileOutputStream("C:\\sqlite\\chart\\chart"+i+".png"); 
		ChartUtilities.writeChartAsPNG(out, chart, chartPanel.getWidth(), chartPanel.getHeight()); 
		}
		catch (IOException ex){}
		table1.setRowSelectionInterval(i, i);
		}
	 
	}
	
	@SuppressWarnings("removal")
	public static void initialize() {
		frame1.getContentPane().invalidate();
		frame1.getContentPane().validate();
		frame1.getContentPane().repaint();
		two_column();
		get_data_pot();	
		
		try {
			String[] params_data = parameter.split(",");
			
			if (!params_data[29].matches("Not Certified")) {
				System.out.println("IF Not certified");
				checkBox1.setSelected(true);
				certify_now = true;
				checkBox1.setEnabled(false);
				already_certified = true;
			}
			String[] details_data = details.split(",");
			String temp_details = "";
			for (int i = 0; i < details_data.length; i++) {
				temp_details = temp_details + details_data[i] + "\n\n";
				display.setText(temp_details);
				//System.out.println(temp_details);
			}
			
			String[] temp_remarks_arr = remarks.split(",");
			remarks_input_pot.setText(temp_remarks_arr[0]);
			if(!temp_remarks_arr[0].toLowerCase().contains("no remarks")) {
				remarks_input_pot.setEnabled(false);
				already_remarked = true;
			}
			
			if(already_certified == true && already_remarked == true) {
				btn_update_pot_remarks.setEnabled(false);
			}
			
			
		} catch (NullPointerException njh) {

		} catch (ArrayIndexOutOfBoundsException fd) {
			System.out.println("");
		}
		
		try {
			audit_log_push.push_to_audit(get_date(), get_time() ,user_name,"Pot Report: "+report_name+" Opened");
		} catch (ParseException e1) {e1.printStackTrace();}
		
    }
	
	public static void get_data_pot() {
		System.out.println("get_data_pot");
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		try 
		{
			sql = "SELECT * FROM potentiometry WHERE report_name = '"+report_name+"'";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
		    parameter = rs.getString("parameters");
		    details = rs.getString("details");
		    graphX =rs.getString("graph_dataX");
		    graphY = rs.getString("graph_dataY");
		    header = rs.getString("header");
		    trial_data = rs.getString("trial_data");
		    results = rs.getString("results");
		    remarks = rs.getString("remarks");
		    
		    String[] temp_remarks = remarks.split(",");
		    
		    for(int i=1;i<temp_remarks.length;i++) {
		    	threshold[i-1] = temp_remarks[i];
		    }
		    
		    int v=0;
		    String[] temp_param_timings = details.split(",");
			for(int f=0;f<temp_param_timings.length;f++) {
		    	if(temp_param_timings[f].contains("Trial "+f)) {
		    		
		    		System.out.println("Checking for Trial "+f);
		    		
		    		String str = temp_param_timings[f];
		    		Stack<Integer> dels = new Stack<Integer>();
		    	    for(int i = 0; i < str.length(); i++)
		    	    {
		    	        if (str.charAt(i) == '[')
		    	        {
		    	            dels.add(i);
		    	        }
		    	        else if (str.charAt(i) == ']' && !dels.isEmpty())
		    	        {
		    	            int pos = dels.peek();
		    	            dels.pop();
		    	            int len = i - 1 - pos;
		    	            String ans = str.substring( pos + 1, pos + 1 + len);
		    	            System.out.print(ans + "\n");
		    	            timings[v] = ans;
		    	        }
		    	    }
		    	    v++;
		    	}
		    }

		    System.out.println(header);
		   
			String [] params_data = parameter.split(",");
			
			for(int i=0;i<30;i++) {
				add_row_to_two_column(i,params[i],params_data[i]);
			}
				
		    String [] details_data = details.split(",");

			String temp_details = "";
				for(int i=0;i<details_data.length;i++) 
				{
					temp_details = temp_details+ details_data[i]+"\n\n";
					display.setText(temp_details);
				}
				
				String[] header_arr = header.split(",");
				if(header_arr.length == 5 ) {
					five_column_kff();
				}
				else if(header_arr.length == 6 ) {
					six_column_kff();
				}
				else if(header_arr.length == 7 ) {
					seven_column_kff();
				}
				else if(header_arr.length == 8 ) {
					eight_column_kff();
				}
				else if(header_arr.length == 9 ) {
					nine_column_kff();
				}
				else if(header_arr.length == 10 ) {
					ten_column_kff();
				}
				try {
					String[] trial_arr = trial_data.split(":");
					for(int i=0;i<trial_arr.length;i++) {
						String[] temp_arr = trial_arr[i].split(",");
						System.out.println("Trial Data : "+trial_arr[i]);
						if(header_arr.length == 5 ) {
							add_row_to_five_column(i, temp_arr[1], temp_arr[2], temp_arr[3], temp_arr[4]);
						}
						else if(header_arr.length == 6 ) {
							add_row_to_six_column(i, temp_arr[1], temp_arr[2], temp_arr[3], temp_arr[4],temp_arr[5]);
						}
						else if(header_arr.length == 7 ) {
							add_row_to_seven_column(i, temp_arr[1], temp_arr[2], temp_arr[3], temp_arr[4],temp_arr[5],temp_arr[6]);
						}
						else if(header_arr.length == 8 ) {
							add_row_to_eight_column(i, temp_arr[1], temp_arr[2], temp_arr[3], temp_arr[4],temp_arr[5],temp_arr[6],temp_arr[7]);
						}
						else if(header_arr.length == 9 ) {
							add_row_to_nine_column(i, temp_arr[1], temp_arr[2], temp_arr[3], temp_arr[4],temp_arr[5],temp_arr[6],temp_arr[7],temp_arr[8]);
						}
						else if(header_arr.length == 10 ) {
							add_row_to_ten_column(i, temp_arr[1], temp_arr[2], temp_arr[3], temp_arr[4],temp_arr[5],temp_arr[6],temp_arr[7],temp_arr[8],temp_arr[9]);
						}
					}
				
				
			  String[] result_arr = results.split(":");
			  for(int i=0;i<result_arr.length;i++) {
					String[] temp_arr = result_arr[i].split(",");
					if(i==0) {
						result_header1.setText("Result1 : "+temp_arr[0]);
						rsd1.setText("RSD1 : "+temp_arr[1]+" %");
						result_header2.setVisible(false);
						rsd2.setVisible(false);
						result_header3.setVisible(false);
						rsd3.setVisible(false);
					}
					if(i==1) {
						result_header2.setVisible(true);
						rsd2.setVisible(true);
						result_header2.setText("Result2 : "+temp_arr[0]);
						rsd2.setText("RSD2 : "+temp_arr[1]+" %");
					}
					if(i==2) {
						result_header3.setVisible(true);
						rsd3.setVisible(true);
					
						result_header3.setText("Result3 : "+temp_arr[0]);
						rsd3.setText("RSD3 : "+temp_arr[1]+" %");
					}
			  }
			  
			  tempx= graphX.split(":");
			  tempy = graphY.split(":");
			  String dx[] = tempx[0].split(",");
			  String dy[] = tempy[0].split(",");
			  for(int j=0;j<dx.length;j++) {
				//System.out.println("X = "+dx[j]+" : Y = "+dy[j]);
				       series.addOrUpdate(Double.parseDouble(dx[j]),Double.parseDouble(dy[j]));
			  }
			  
			}
				catch(ArrayIndexOutOfBoundsException ex) {
					return;
				}
			  
			  if(params_data[18].matches("1")) {
				  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
				  }
				  catch(NumberFormatException f) {
					  
				  }
			  }
			  if(params_data[18].matches("2")) {
				  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
				  }
				  catch(NumberFormatException f) {
					  
				  }
			  }
			  if(params_data[18].matches("3")) {
				  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
				  }
			  catch(NumberFormatException f) {
				  
			  }
			  }
			  if(params_data[18].matches("4")) {
				  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
				  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 2).toString()));
				  marker1.setPaint(Color.blue);
				  plot1 = (XYPlot) chart.getPlot();
				  plot1.addDomainMarker(marker1);
				  }
				  catch(NumberFormatException f) {
					  
				  }
			  } 	
			  if(params_data[18].matches("5")) {
				  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
				  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 2).toString()));
				  marker1.setPaint(Color.blue);
				  plot1 = (XYPlot) chart.getPlot();
				  plot1.addDomainMarker(marker1);
				  }
				  catch(NumberFormatException f) {
					  
				  }
			  } 	
			  if(params_data[18].matches("6")) {
				  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
				  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 2).toString()));
				  marker1.setPaint(Color.blue);
				  plot1 = (XYPlot) chart.getPlot();
				  plot1.addDomainMarker(marker1);
				  }
				  catch(NumberFormatException f) {
					  
				  }
			  } 
			  if(params_data[18].matches("7")) {
				  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
				  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 2).toString()));
				  marker1.setPaint(Color.blue);
				  plot1 = (XYPlot) chart.getPlot();
				  plot1.addDomainMarker(marker1);
				  ValueMarker marker2 = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 3).toString()));
				  marker2.setPaint(Color.blue);
				  plot2 = (XYPlot) chart.getPlot();
				  plot2.addDomainMarker(marker2);}
				  catch(NumberFormatException f) {
					  
				  }
			  } 
			  if(params_data[18].matches("8")) {
				  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
				  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 2).toString()));
				  marker1.setPaint(Color.blue);
				  plot1 = (XYPlot) chart.getPlot();
				  plot1.addDomainMarker(marker1);
				  }
				  catch(NumberFormatException f) {
					  
				  }
			  } 
			  if(params_data[18].matches("9")) {
				  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
				  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 2).toString()));
				  marker1.setPaint(Color.blue);
				  plot1 = (XYPlot) chart.getPlot();
				  plot1.addDomainMarker(marker1);
				  }
				  catch(NumberFormatException f) {
					  
				  }
			  } 
			  if(params_data[18].matches("10")) {
				  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
				  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 2).toString()));
				  marker1.setPaint(Color.blue);
				  plot1 = (XYPlot) chart.getPlot();
				  plot1.addDomainMarker(marker1);
				  ValueMarker marker2 = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 3).toString()));
				  marker2.setPaint(Color.blue);
				  plot2 = (XYPlot) chart.getPlot();
				  plot2.addDomainMarker(marker2);
				  }
				  catch(NumberFormatException f) {
					  
				  }
			  } 
			  if(params_data[18].matches("11")) {
				  try {
					  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 1).toString()));
					  marker.setPaint(Color.blue);
					  plot = (XYPlot) chart.getPlot();
					  plot.addDomainMarker(marker);
					  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 2).toString()));
					  marker1.setPaint(Color.blue);
					  plot1 = (XYPlot) chart.getPlot();
					  plot1.addDomainMarker(marker1);
				  }
				  catch(NumberFormatException f) {}
			  } 
			  if(params_data[18].matches("12")) {
				  try {
				  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 1).toString()));
				  marker.setPaint(Color.blue);
				  plot = (XYPlot) chart.getPlot();
				  plot.addDomainMarker(marker);
				  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(0, 2).toString()));
				  marker1.setPaint(Color.blue);
				  plot1 = (XYPlot) chart.getPlot();
				  plot1.addDomainMarker(marker1);
				  }
				  catch(NumberFormatException f) {
					  
				  }
			  } 
			table1.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		    public void valueChanged(ListSelectionEvent event) {
		    	 cur = Integer.parseInt(table1.getValueAt(table1.getSelectedRow(), 0).toString());
		    	 if(cur != prev)
		    	 {
		    		  prev = cur;
		    		  String dx[] = tempx[cur-1].split(",");
					  String dy[] = tempy[cur-1].split(",");
					  series.clear();
					  
					  
					  for(int j=0;j<dx.length;j++) {
						       series.addOrUpdate(Double.parseDouble(dx[j]),Double.parseDouble(dy[j]));
					  }
					  
					  
					  try {
						  plot.clearDomainMarkers();
							plot1.clearDomainMarkers();
							plot2.clearDomainMarkers();
						 
					  }
					  catch(NullPointerException ds) {}
					  
					  if(params_data[18].matches("1")) {
						  try {
							  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 1).toString()));
							  marker.setPaint(Color.blue);
							  plot = (XYPlot) chart.getPlot();
							  plot.addDomainMarker(marker);
						  }
						  catch(NumberFormatException dcw){}
					  }
					  if(params_data[18].matches("2")) {
						  try {
							  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 1).toString()));
							  marker.setPaint(Color.blue);
							  plot = (XYPlot) chart.getPlot();
							  plot.addDomainMarker(marker);
						  }
						  catch(NumberFormatException dcw){}
					  }
					  if(params_data[18].matches("3")) {
						  try {
							  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 1).toString()));
							  marker.setPaint(Color.blue);
							  plot = (XYPlot) chart.getPlot();
							  plot.addDomainMarker(marker);
						  }
					  catch(NumberFormatException dcw){}
					  }
					  if(params_data[18].matches("4")) {
						  try {
							  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 1).toString()));
							  marker.setPaint(Color.blue);
							  plot = (XYPlot) chart.getPlot();
							  plot.addDomainMarker(marker);
							  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 2).toString()));
							  marker1.setPaint(Color.blue);
							  plot1 = (XYPlot) chart.getPlot();
							  plot1.addDomainMarker(marker1);
						  }
					  catch(NumberFormatException dcw){}
					  }
					  if(params_data[18].matches("5")) {
						  try {
							  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 1).toString()));
							  marker.setPaint(Color.blue);
							  plot = (XYPlot) chart.getPlot();
							  plot.addDomainMarker(marker);
							  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 2).toString()));
							  marker1.setPaint(Color.blue);
							  plot1 = (XYPlot) chart.getPlot();
							  plot1.addDomainMarker(marker1);
						  }
					  catch(NumberFormatException dcw){}
					  }
					  if(params_data[18].matches("6")) {
						  try {
							  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 1).toString()));
							  marker.setPaint(Color.blue);
							  plot = (XYPlot) chart.getPlot();
							  plot.addDomainMarker(marker);
							  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 2).toString()));
							  marker1.setPaint(Color.blue);
							  plot1 = (XYPlot) chart.getPlot();
							  plot1.addDomainMarker(marker1);
						  }
					  catch(NumberFormatException dcw){}
					  }
					  if(params_data[18].matches("7")) {
						  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 1).toString()));
						  marker.setPaint(Color.blue);
						  plot = (XYPlot) chart.getPlot();
						  plot.addDomainMarker(marker);
						  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 2).toString()));
						  marker1.setPaint(Color.blue);
						  plot1 = (XYPlot) chart.getPlot();
						  plot1.addDomainMarker(marker1);
						  ValueMarker marker2 = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 3).toString()));
						  marker2.setPaint(Color.blue);
						  plot2 = (XYPlot) chart.getPlot();
						  plot2.addDomainMarker(marker2);
						  System.out.println("Seevennnnnnnn "+marker.getValue());
					  }
					  if(params_data[18].matches("8")) {
						  try {
							  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 1).toString()));
							  marker.setPaint(Color.blue);
							  plot = (XYPlot) chart.getPlot();
							  plot.addDomainMarker(marker);
							  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 2).toString()));
							  marker1.setPaint(Color.blue);
							  plot1 = (XYPlot) chart.getPlot();
							  plot1.addDomainMarker(marker1);
						  }
					  catch(NumberFormatException dcw){}
					  }
					  if(params_data[18].matches("9")) {
						  try {
							  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 1).toString()));
							  marker.setPaint(Color.blue);
							  plot = (XYPlot) chart.getPlot();
							  plot.addDomainMarker(marker);
							  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 2).toString()));
							  marker1.setPaint(Color.blue);
							  plot1 = (XYPlot) chart.getPlot();
							  plot1.addDomainMarker(marker1);
						  }
					  catch(NumberFormatException dcw){}
					  }
					  if(params_data[18].matches("10")) {
						  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 1).toString()));
						  marker.setPaint(Color.blue);
						  plot = (XYPlot) chart.getPlot();
						  plot.addDomainMarker(marker);
						  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 2).toString()));
						  marker1.setPaint(Color.blue);
						  plot1 = (XYPlot) chart.getPlot();
						  plot1.addDomainMarker(marker1);
						  ValueMarker marker2 = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 3).toString()));
						  marker2.setPaint(Color.blue);
						  plot2 = (XYPlot) chart.getPlot();
						  plot2.addDomainMarker(marker2);
					  }
					  if(params_data[18].matches("11")) {
						  try {
							  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 1).toString()));
							  marker.setPaint(Color.blue);
							  plot = (XYPlot) chart.getPlot();
							  plot.addDomainMarker(marker);
							  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 2).toString()));
							  marker1.setPaint(Color.blue);
							  plot1 = (XYPlot) chart.getPlot();
							  plot1.addDomainMarker(marker1);
						  }
					  catch(NumberFormatException dcw){}
					  }
					  if(params_data[18].matches("12")) {
						  try {
							  ValueMarker marker = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 1).toString()));
							  marker.setPaint(Color.blue);
							  plot = (XYPlot) chart.getPlot();
							  plot.addDomainMarker(marker);
							  ValueMarker marker1 = new ValueMarker(Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 2).toString()));
							  marker1.setPaint(Color.blue);
							  plot1 = (XYPlot) chart.getPlot();
							  plot1.addDomainMarker(marker1);
						  }
					  catch(NumberFormatException dcw){}
					  }
		    	 }
		        System.out.println(table1.getValueAt(table1.getSelectedRow(), 0).toString());
		    }
			});
			
		}
		catch(SQLException e1) {
			System.out.println(e1.toString());
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

	public static void two_column() {
		scrollPane2.setBounds((int) Math.round(0.013 * wid), (int) Math.round(0.06 * hei), (int) Math.round(0.35 * wid), (int) Math.round(0.45* hei));
		frame1.getContentPane().add(scrollPane2);
		table2 = new JTable();
		table2.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0097 * wid)));
		model2 = new DefaultTableModel() {
			public Class <?> getColumnClass(int column){
				switch(column) {
				case 0:
					return Integer.class;
				default:
					return String.class;
				}
			}
		};
		table2.setModel(model2);
		model2.addColumn("Parameters");
		model2.addColumn("Value");
		table2.setRowHeight((int) Math.round(0.0447 * hei));
		table2.setDefaultEditor(Object.class, null);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		table2.getColumnModel().getColumn(0).setCellRenderer( centerRenderer);
		table2.getColumnModel().getColumn(1).setCellRenderer( centerRenderer);
		scrollPane2.setViewportView(table2);
	}
	
	public static void add_row_to_two_column(int r,String param,String val) {
		model2.addRow(new Object[0]);
		model2.setValueAt(param, r, 0);	
		model2.setValueAt(val, r, 1);			
		model2.fireTableDataChanged();
	}
	
	public static void five_column_kff() {
		String[] header_arr = header.split(",");
		scrollPane.setBounds((int) Math.round(0.4 * wid), (int) Math.round(0.53 * hei), (int) Math.round(0.58 * wid), (int) Math.round(0.23 * hei));
		frame1.getContentPane().add(scrollPane);
		table1 = new JTable();
		table1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0097 * wid)));
		model = new DefaultTableModel() {
			public Class <?> getColumnClass(int column){
				switch(column) {
				case 0:
					return Integer.class;
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
		};
		table1.setModel(model);
		model.addColumn(header_arr[0]);
		model.addColumn(header_arr[1]);
		model.addColumn(header_arr[2]);
		model.addColumn(header_arr[3]);
		model.addColumn(header_arr[4]);	
		table1.setRowHeight((int) Math.round(0.04 * hei));
		table1.setDefaultEditor(Object.class, null);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		table1.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(4).setCellRenderer( centerRenderer );
		scrollPane.setViewportView(table1);

	}
	
	public static void six_column_kff() {
		String[] header_arr = header.split(",");
		scrollPane.setBounds((int) Math.round(0.4 * wid), (int) Math.round(0.53 * hei), (int) Math.round(0.58 * wid), (int) Math.round(0.23 * hei));
		frame1.getContentPane().add(scrollPane);
		table1 = new JTable();
		table1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0097 * wid)));
		model = new DefaultTableModel() {
			public Class <?> getColumnClass(int column){
				switch(column) {
				case 0:
					return Integer.class;
				case 1:
					return String.class;
				case 2:
					return String.class;
				case 3:
					return String.class;
				case 4:
					return String.class;
				default:
					return String.class;
				}
			}
		};
		table1.setModel(model);
		model.addColumn(header_arr[0]);
		model.addColumn(header_arr[1]);
		model.addColumn(header_arr[2]);
		model.addColumn(header_arr[3]);
		model.addColumn(header_arr[4]);	
		model.addColumn(header_arr[5]);	
		table1.setRowHeight((int) Math.round(0.04 * hei));
		table1.setDefaultEditor(Object.class, null);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		table1.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(4).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(5).setCellRenderer( centerRenderer );
		scrollPane.setViewportView(table1);

	}
	public static void seven_column_kff() {
		System.out.println("Inside seven column");
		String[] header_arr = header.split(",");
		System.out.println("Inside seven column len = "+header_arr.length);

		scrollPane.setBounds((int) Math.round(0.4 * wid), (int) Math.round(0.53 * hei), (int) Math.round(0.58 * wid), (int) Math.round(0.23 * hei));
		frame1.getContentPane().add(scrollPane);
		table1 = new JTable();
		table1.setDefaultEditor(Object.class, null);

		table1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0097 * wid)));
		model = new DefaultTableModel() 
		{
			public Class <?> getColumnClass(int column){
				switch(column) {
				case 0:
					return Integer.class;
				case 1:
					return String.class;
				case 2:
					return String.class;
				case 3:
					return String.class;
				case 4:
					return String.class;
				case 5:
					return String.class;
				default:
					return String.class;
				}
			}
		};
		table1.setModel(model);
		model.addColumn(header_arr[0]);
		model.addColumn(header_arr[1]);
		model.addColumn(header_arr[2]);
		model.addColumn(header_arr[3]);
		model.addColumn(header_arr[4]);	
		model.addColumn(header_arr[5]);	
		model.addColumn(header_arr[6]);	

		table1.setRowHeight((int) Math.round(0.04 * hei));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		table1.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(4).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(5).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(6).setCellRenderer( centerRenderer );
		scrollPane.setViewportView(table1);
	}
	
	public static void eight_column_kff() {
		String[] header_arr = header.split(",");
		scrollPane.setBounds((int) Math.round(0.4 * wid), (int) Math.round(0.53 * hei), (int) Math.round(0.58 * wid), (int) Math.round(0.23 * hei));
		frame1.getContentPane().add(scrollPane);
		table1 = new JTable();
		table1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0097 * wid)));
		table1.setDefaultEditor(Object.class, null);

		model = new DefaultTableModel() {
			public Class <?> getColumnClass(int column){
				switch(column) {
				case 0:
					return Integer.class;
				case 1:
					return String.class;
				case 2:
					return String.class;
				case 3:
					return String.class;
				case 4:
					return String.class;
				case 5:
					return String.class;
				case 6:
					return String.class;
				default:
					return String.class;
				}
			}
		};
		table1.setModel(model);
		model.addColumn(header_arr[0]);
		model.addColumn(header_arr[1]);
		model.addColumn(header_arr[2]);
		model.addColumn(header_arr[3]);
		model.addColumn(header_arr[4]);	
		model.addColumn(header_arr[5]);	
		model.addColumn(header_arr[6]);	
		model.addColumn(header_arr[7]);	

		table1.setRowHeight((int) Math.round(0.04 * hei));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		table1.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(4).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(5).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(6).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(7).setCellRenderer( centerRenderer );
		scrollPane.setViewportView(table1);
	}
	
	public static void nine_column_kff() {
		String[] header_arr = header.split(",");
		scrollPane.setBounds((int) Math.round(0.4 * wid), (int) Math.round(0.53 * hei), (int) Math.round(0.58 * wid), (int) Math.round(0.23 * hei));
		frame1.getContentPane().add(scrollPane);
		table1 = new JTable();
		table1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0097 * wid)));
		table1.setDefaultEditor(Object.class, null);

		model = new DefaultTableModel() {
			public Class <?> getColumnClass(int column){
				switch(column) {
				case 0:
					return Integer.class;
				case 1:
					return String.class;
				case 2:
					return String.class;
				case 3:
					return String.class;
				case 4:
					return String.class;
				case 5:
					return String.class;
				case 6:
					return String.class;
				case 7:
					return String.class;
				default:
					return String.class;
				}
			}
		};
		table1.setModel(model);
		model.addColumn(header_arr[0]);
		model.addColumn(header_arr[1]);
		model.addColumn(header_arr[2]);
		model.addColumn(header_arr[3]);
		model.addColumn(header_arr[4]);	
		model.addColumn(header_arr[5]);	
		model.addColumn(header_arr[6]);	
		model.addColumn(header_arr[7]);	
		model.addColumn(header_arr[8]);	

		table1.setRowHeight((int) Math.round(0.04 * hei));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		table1.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(4).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(5).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(6).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(7).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(8).setCellRenderer( centerRenderer );
		scrollPane.setViewportView(table1);
	}
	
	public static void ten_column_kff() {
		String[] header_arr = header.split(",");
		scrollPane.setBounds((int) Math.round(0.4 * wid), (int) Math.round(0.53 * hei), (int) Math.round(0.58 * wid), (int) Math.round(0.23 * hei));
		frame1.getContentPane().add(scrollPane);
		table1 = new JTable();
		table1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.0097 * wid)));
		table1.setDefaultEditor(Object.class, null);

		model = new DefaultTableModel() {
			public Class <?> getColumnClass(int column){
				switch(column) {
				case 0:
					return Integer.class;
				case 1:
					return String.class;
				case 2:
					return String.class;
				case 3:
					return String.class;
				case 4:
					return String.class;
				case 5:
					return String.class;
				case 6:
					return String.class;
				case 7:
					return String.class;
				case 8:
					return String.class;
				default:
					return String.class;
				}
			}
		};
		table1.setModel(model);
		model.addColumn(header_arr[0]);
		model.addColumn(header_arr[1]);
		model.addColumn(header_arr[2]);
		model.addColumn(header_arr[3]);
		model.addColumn(header_arr[4]);	
		model.addColumn(header_arr[5]);	
		model.addColumn(header_arr[6]);	
		model.addColumn(header_arr[7]);	
		model.addColumn(header_arr[8]);	
		model.addColumn(header_arr[9]);	

		table1.setRowHeight((int) Math.round(0.04 * hei));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		table1.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(4).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(5).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(6).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(7).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(8).setCellRenderer( centerRenderer );
		table1.getColumnModel().getColumn(9).setCellRenderer( centerRenderer );
		scrollPane.setViewportView(table1);
	}
	
	public static void add_row_to_five_column(int r,String a1,String a2,String a3,String a4) {
		model.addRow(new Object[0]);
		model.setValueAt(r+1, r, 0);	
		model.setValueAt(a1, r, 1);			
		model.setValueAt(a2, r, 2);		
		model.setValueAt(a3, r, 3);	
		model.setValueAt(a4, r, 4);			
		model.fireTableDataChanged();
	}
	
	public static void add_row_to_six_column(int r,String a1,String a2,String a3,String a4, String a5) {
		model.addRow(new Object[0]);
		model.setValueAt(r+1, r, 0);	
		model.setValueAt(a1, r, 1);			
		model.setValueAt(a2, r, 2);		
		model.setValueAt(a3, r, 3);	
		model.setValueAt(a4, r, 4);			
		model.setValueAt(a5, r, 5);			
		model.fireTableDataChanged();
	}
	public static void add_row_to_seven_column(int r,String a1,String a2,String a3,String a4, String a5, String a6) {
		model.addRow(new Object[0]);
		model.setValueAt(r+1, r, 0);	
		model.setValueAt(a1, r, 1);			
		model.setValueAt(a2, r, 2);		
		model.setValueAt(a3, r, 3);	
		model.setValueAt(a4, r, 4);			
		model.setValueAt(a5, r, 5);	
		model.setValueAt(a6, r, 6);			
		model.fireTableDataChanged();
	}
	public static void add_row_to_eight_column(int r,String a1,String a2,String a3,String a4, String a5, String a6, String a7) {
		model.addRow(new Object[0]);
		model.setValueAt(r+1, r, 0);	
		model.setValueAt(a1, r, 1);			
		model.setValueAt(a2, r, 2);		
		model.setValueAt(a3, r, 3);	
		model.setValueAt(a4, r, 4);			
		model.setValueAt(a5, r, 5);	
		model.setValueAt(a6, r, 6);
		model.setValueAt(a7, r, 7);			
		model.fireTableDataChanged();
	}
	public static void add_row_to_nine_column(int r,String a1,String a2,String a3,String a4, String a5, String a6, String a7,String a8) {
		model.addRow(new Object[0]);
		model.setValueAt(r+1, r, 0);	
		model.setValueAt(a1, r, 1);			
		model.setValueAt(a2, r, 2);		
		model.setValueAt(a3, r, 3);	
		model.setValueAt(a4, r, 4);			
		model.setValueAt(a5, r, 5);	
		model.setValueAt(a6, r, 6);
		model.setValueAt(a7, r, 7);	
		model.setValueAt(a8, r, 8);	
		model.fireTableDataChanged();
	}
	public static void add_row_to_ten_column(int r,String a1,String a2,String a3,String a4, String a5, String a6, String a7,String a8,String a9) {
		model.addRow(new Object[0]);
		model.setValueAt(r+1, r, 0);	
		model.setValueAt(a1, r, 1);			
		model.setValueAt(a2, r, 2);		
		model.setValueAt(a3, r, 3);	
		model.setValueAt(a4, r, 4);			
		model.setValueAt(a5, r, 5);	
		model.setValueAt(a6, r, 6);
		model.setValueAt(a7, r, 7);	
		model.setValueAt(a8, r, 8);	
		model.setValueAt(a9, r, 9);	
		model.fireTableDataChanged();
	}
	
    public static void main(String[] args) {    
    	
    	if(args.length != 0 )
    	{    
    		report_name = args[0];
    		user_name = args[1];
    		u_permissions= args[2];
    		if (args[2].contains("Certify")) {
				certify = true;
			}
    	}
    	
    	Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame1.getGraphicsConfiguration());
        int taskHeight=screenInsets.bottom;
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        hei=d.height-taskHeight;
        wid=d.width;
        System.out.println(wid + "   dfvdvdv " + hei);
		frame1.setBounds(0,0,wid,hei);
		frame1.add(p);
	    frame1.setExtendedState(Frame.MAXIMIZED_BOTH);
   		frame1.getContentPane().add(new DrawReport_pot());
		frame1.setResizable(true);
		frame1.setVisible(true);
		frame1.repaint();
		ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
		frame1.setIconImage(img.getImage());

		ReformatBuffer.current_exp = "kf";
			frame1.addWindowListener(new java.awt.event.WindowAdapter() {
			    @Override
			    public void windowClosing(java.awt.event.WindowEvent windowEvent)
			    {
			    	series.clear();
			    	frame1.dispose();
					frame1 = new JFrame();
			        p=new JPanel();	
			        p.invalidate();
			        p.revalidate();
			        p.repaint();
			    }
			});
    }
}
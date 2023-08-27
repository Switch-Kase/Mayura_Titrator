package main.java;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

//import Login.trial;

//import login.trial;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Desktop;  


public class popup_stirtime extends JDialog{

	private static JPanel contentPane;
	static popup_stirtime frame;
	static JLabel lblNewLabel;
	static int timer = 15;
	static String exp = "";

	 static ScheduledExecutorService exec_timer;

	public static void main(String[] args) {
		try {
		timer = Integer.parseInt(args[0]);
		System.out.println(" MAINN STIR TIME = "+ timer);
		exp=args[1];
		}
		catch(ArrayIndexOutOfBoundsException npe) {
			
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new popup_stirtime();
					frame.setVisible(true);
					

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});		
	}

	
	public void keyPressed(KeyEvent e) {
	    if (e.getKeyCode()==KeyEvent.VK_ENTER){
	        System.out.println("Hello");
	        JOptionPane.showMessageDialog(null , "You've Submitted the name ");
	    }

	}
	
	public popup_stirtime() {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 200, 350, 200);
		setTitle("Stirring in Progress");
		setLocationRelativeTo(null);
		setModal(true);
		
		ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
		setIconImage(img.getImage());

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);		
		lblNewLabel = new JLabel("Stir Time : ");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel.setBounds(100, 60, 350, 21);
		contentPane.add(lblNewLabel);
		
		 exec_timer = Executors.newSingleThreadScheduledExecutor();
		exec_timer.scheduleAtFixedRate(new Runnable() {
		  @Override
		  public void run() {
			  if(timer>0) {
			  lblNewLabel.setText("Stir Time : "+timer + " Sec");
			  timer--;
			  }
			  else {
				  exec_timer.shutdown();
				  if(exp.matches("pot")) {
					 DrawGraph_pot.timer_completed();
				  }
				  else if(exp.matches("kf")) {
					  DrawGraph_kf.timer_completed();
				  }
				  else if(exp.matches("ph")) {
					 // DrawGraph_kf.timer_completed();
				  }
				  else if(exp.matches("amp")) {
					  //DrawGraph_kf.timer_completed();
				  }
				  dispose();
			  }
			  
		  }
		}, 0, 1000, TimeUnit.MILLISECONDS);
		
		
	}
}
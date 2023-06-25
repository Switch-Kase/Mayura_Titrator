package main.java;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.fazecast.jSerialComm.SerialPort;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class select_comport extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
    JComboBox comboBox_port;
    static select_comport frame;
    int hp77_port =0;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new select_comport();
					frame.setVisible(true);
					ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
					frame.setIconImage(img.getImage());


				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public select_comport() {
		
		setBounds(300, 200, 500, 300);
		setTitle("Select ComPort");
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel UserName = new JLabel("Comport");
		UserName.setFont(new Font("Times New Roman", Font.BOLD, 18));
		UserName.setBounds(10, 59, 100, 21);
		contentPane.add(UserName);
		SerialPort[] ports = SerialPort.getCommPorts();
		ArrayList<String> available_ports = new ArrayList<String>(); // Create an ArrayList object


        for(SerialPort temp_port : ports) {
           if(temp_port.getDescriptivePortName().contains("Silicon Labs CP210x USB to UART Bridge")) { //7  
        	 if(null != temp_port.getDescriptivePortName().split(" ")[7] && temp_port.getDescriptivePortName().split(" ")[7].contains("COM")) {
        		 available_ports.add("Mayura Analytical 21CFR_HP77 "+temp_port.getDescriptivePortName().split(" ")[7]);
        	 }
           }
           else {
        	   available_ports.add(temp_port.getDescriptivePortName());
           }
        }
        
        
        for(int port=0;port<available_ports.size();port++) {
        	if(null!= available_ports.get(port) && available_ports.get(port).contains("Mayura Analytical 21CFR_HP77")) {
        		hp77_port = port;
        		String temp_port = available_ports.get(0);
        		available_ports.set(0,available_ports.get(port));
        		available_ports.set(port,temp_port);        		
        	}
        }
		
		comboBox_port = new JComboBox(available_ports.toArray());
		comboBox_port.setBounds(100, 59,335, 21);
		comboBox_port.setSelectedIndex(0);		
		contentPane.add(comboBox_port);
		
		JButton btnNewButton = new JButton("Open");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBox_port.getSelectedItem().toString().contains("Mayura Analytical 21CFR_HP77")) {
	                SerialPort firstAvailableComPort = ports[hp77_port];
	                menubar.open_port(firstAvailableComPort);
	                dispose();
				}
				else {
					JOptionPane.showMessageDialog(null, "Please select the 21CFR-HP77 ComPort!");
				}
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnNewButton.setBounds(160, 180, 170, 37);
		contentPane.add(btnNewButton);
	}
}

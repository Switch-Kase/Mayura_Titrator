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
import java.awt.event.ActionEvent;

public class select_comport extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	public static SerialPort firstAvailableComPort;
    public static SerialPort[] ports;
    static DefaultListModel<String> l1;
    String[] port_arr ;
    JComboBox comboBox_port;
    static select_comport frame;
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

	/**
	 * Create the frame.
	 */
	public select_comport() {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		ports = SerialPort.getCommPorts();

		port_arr = new String[ports.length];

        for(int i=0;i<ports.length;i++) {
           port_arr[i]=(ports[i].getDescriptivePortName());
           System.out.println(ports[i].getDescriptivePortName());
        }
		
		comboBox_port = new JComboBox(port_arr);
		comboBox_port.setBounds(100, 59,335, 21);
		contentPane.add(comboBox_port);
		
		JButton btnNewButton = new JButton("Open");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selected_port = comboBox_port.getSelectedItem().toString();
				int chosenPort = comboBox_port.getSelectedIndex();
                System.out.println(chosenPort);
                firstAvailableComPort = ports[chosenPort];
                menubar.open_port(firstAvailableComPort);

				dispose();
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnNewButton.setBounds(160, 180, 170, 37);
		contentPane.add(btnNewButton);

        
//        int chosenPort = list.getSelectedIndex();
//        System.out.println(chosenPort);
//        firstAvailableComPort = ports[chosenPort];
//        firstAvailableComPort.openPort();

	}
}

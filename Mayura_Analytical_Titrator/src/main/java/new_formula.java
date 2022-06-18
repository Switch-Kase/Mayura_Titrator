package main.java;

import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import com.fazecast.jSerialComm.SerialPort;
import com.sun.jdi.connect.Connector.SelectedArgument;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JLayeredPane;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
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
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.util.regex.Pattern;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

// firstAvailableComPort = ports[chosenPort];
//firstAvailableComPort.openPort();
//
//
//
//System.out.println("Opened serial port: " + firstAvailableComPort.getDescriptivePortName());
//MyComPortListener listenerObject = new MyComPortListener();
//firstAvailableComPort.addDataListener(listenerObject);

public class new_formula extends JPanel {
	// cur_val[3] = dose speed
	// cur_val[6] = EPSELECT
	// cur_val[7] = FORMULA No
	// cur_val[17] = burette factor

	static JFrame frame1 = new JFrame();
	static JPanel p = new JPanel();
	static int height, hei;
	static int width, wid;
	static int formula_no = -1;
	static JTextField name1;
	static JLabel formula_name,v1,v2,v3,n,w,m,vblk,f1,f2,f3,f4,user_factor_header,formula1_header,formula2_header,formula3_header,formula1,formula2,formula3;
    static JButton btn_v1,btn_v2,btn_v3,btn_n,btn_del,btn_w,btn_m,btn_vblk,btn_f1,btn_f2,btn_f3,btn_f4,btn_plus,btn_minus,btn_mul,btn_div,btn_lb,btn_rb,btn_ru,btn_refresh,btn_addformula,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn0,btndot;
    static JRadioButton radio_btn1,radio_btn2,radio_btn3;
    static ArrayList<String> formula1_arr = new ArrayList<String>();
    static ArrayList<String> formula2_arr = new ArrayList<String>();
    static ArrayList<String> formula3_arr = new ArrayList<String>();

	public static void main(String[] args) {
		
		if(args.length != 0) {
			formula_no = Integer.parseInt(args[0]);
			formula_no++;
		}
		
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame1.getGraphicsConfiguration());
        int taskHeight=screenInsets.bottom;
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        height=d.height-taskHeight;
        width=d.width;
      wid = (int) Math.round(0.6*width);
      hei = (int) Math.round(0.7*height);
//        wid = (int) Math.round(0.6*1280);
//        hei = (int) Math.round(0.75*720);
        System.out.println(width + "   FULL " + height);
        System.out.println(wid + "   Current " + hei);

        frame1.setTitle("Custom Formula Generator");
        
		frame1.setBounds(0,0,wid,hei);
		frame1.add(p);
   		frame1.getContentPane().add(new new_formula());
   		ImageIcon img = new ImageIcon(("C:\\SQLite\\logo\\logo.png"));
   		frame1.setIconImage(img.getImage());
		frame1.setLocationRelativeTo(null);

		frame1.setResizable(true);
		frame1.setVisible(true);
		frame1.repaint();
		
		formula1_arr.clear();
		formula2_arr.clear();
		formula3_arr.clear();

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

	@SuppressWarnings("removal")
	public static void initialize() {
		frame1.getContentPane().invalidate();
		frame1.getContentPane().validate();
		frame1.getContentPane().repaint();
	}

	public new_formula() {
		setLayout(null);

		radio_btn1 = new JRadioButton("Formula 1");
		radio_btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				name1.setText(formula1_header.getText().toString());				
			}
		});
		radio_btn1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.02 * wid)));
		radio_btn1.setBounds((int) Math.round(0.03 * wid), (int) Math.round(0.03 * hei), (int) Math.round(0.16 * wid),(int) Math.round(0.036 * hei));
		add(radio_btn1);

		radio_btn2 = new JRadioButton("Formula 2");
		radio_btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				name1.setText(formula2_header.getText().toString());				

			}
		});
		radio_btn1.setSelected(true);
		
		radio_btn2.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.02 * wid)));
		radio_btn2.setBounds((int) Math.round(0.35 * wid), (int) Math.round(0.03 * hei), (int) Math.round(0.16 * wid),(int) Math.round(0.036 * hei));
		add(radio_btn2);

		radio_btn3 = new JRadioButton("Formula 3");
		radio_btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				name1.setText(formula3_header.getText().toString());				

			}
		});
		radio_btn3.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.02 * wid)));
		radio_btn3.setBounds((int) Math.round(0.7 * wid), (int) Math.round(0.03 * hei), (int) Math.round(0.16 * wid),(int) Math.round(0.036 * hei));
		add(radio_btn3);

		ButtonGroup bg1 = new ButtonGroup();
		bg1.add(radio_btn1);
		bg1.add(radio_btn2);
		bg1.add(radio_btn3);
		
		formula_name = new JLabel("Formula Number : "+formula_no+"           Formula Name : ");
		formula_name.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.02 * wid)));
		formula_name.setBounds((int) Math.round(0.03 * wid), (int) Math.round(0.15 * hei), (int) Math.round(0.5 * wid), (int) Math.round(0.036 * hei));
		add(formula_name);
		
		name1 = new JTextField();
		name1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.02 * wid)));
		name1.setBounds((int) Math.round(0.46 * wid), (int) Math.round(0.15 * hei), (int) Math.round(0.5 * wid), (int) Math.round(0.05 * hei));
		add(name1);
		
		name1.addKeyListener(new KeyListener() {

             @Override
             public void keyTyped(KeyEvent e) {
                 // TODO Auto-generated method stub
                 //System.out.println("keyTyped");
             }

             @Override
             public void keyReleased(KeyEvent e) {
                 // TODO Auto-generated method stub  formula1_header
            //     System.out.println("keyReleased = "+name1.getText());
                 
                 if(radio_btn1.isSelected()) {
                	 	formula1_header.setText(name1.getText().toString()+" : ");
 					}
 				else if(radio_btn2.isSelected()) {
 						formula2_header.setText(name1.getText().toString()+" : ");
 					}
	 			else if(radio_btn3.isSelected()) {
	 					formula3_header.setText(name1.getText().toString()+" : ");
	 				}
             }

             @Override
             public void keyPressed(KeyEvent e) {
                 // TODO Auto-generated method stub
                 //System.out.println("keyPressed");
             }
         }); 
		
		
		
		btn_v1 = new JButton("V1 : First EP");
		btn_v1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_v1.setBounds((int) Math.round(0.03 * wid), (int) Math.round(0.25 * hei), (int) Math.round(0.16 * wid),(int) Math.round(0.05 * hei));
		add(btn_v1);
		btn_v1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("V1");
			}
		});
		
		
		btn_v2 = new JButton("V2 : Second EP");
		btn_v2.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_v2.setBounds((int) Math.round(0.22 * wid), (int) Math.round(0.25 * hei), (int) Math.round(0.18 * wid),(int) Math.round(0.05 * hei));
		add(btn_v2);
		btn_v2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("V2");
			}
		});
		
		
		btn_v3 = new JButton("V3 : Third EP");
		btn_v3.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_v3.setBounds((int) Math.round(0.43 * wid), (int) Math.round(0.25 * hei), (int) Math.round(0.16 * wid),(int) Math.round(0.05 * hei));
		add(btn_v3);
		btn_v3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("V3");
			}
		});
		
		
		btn_n = new JButton("N : Normality");
		btn_n.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_n.setBounds((int) Math.round(0.62 * wid), (int) Math.round(0.25 * hei), (int) Math.round(0.16 * wid),(int) Math.round(0.05 * hei));
		add(btn_n);
		btn_n.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("N");
			}
		});
		
		btn_del = new JButton("Delete");
		btn_del.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_del.setBounds((int) Math.round(0.80 * wid), (int) Math.round(0.25 * hei), (int) Math.round(0.17 * wid),(int) Math.round(0.05 * hei));
		add(btn_del);
		btn_del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(radio_btn1.isSelected()) {
					try {
						if(formula1_arr.size() != 0) {
							formula1_arr.remove(formula1_arr.size()-1);
							formula1.setText(String.join("", formula1_arr));
						}
					}
					catch(ArrayIndexOutOfBoundsException h) {}
				}
				
				else if(radio_btn2.isSelected()) {
					try {
						if(formula2_arr.size() != 0) {
							formula2_arr.remove(formula2_arr.size()-1);
							formula2.setText(String.join("", formula2_arr));
						}
					}
					catch(ArrayIndexOutOfBoundsException h) {}
				}
				else if(radio_btn3.isSelected()) {
					try {
						if(formula3_arr.size() != 0) {
							formula3_arr.remove(formula3_arr.size()-1);
							formula3.setText(String.join("", formula3_arr));
						}
					}
					catch(ArrayIndexOutOfBoundsException h) {}
				}
				
				
			}
		});
		
		btn_m = new JButton("M : % Moisture");
		btn_m.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_m.setBounds((int) Math.round(0.03 * wid), (int) Math.round(0.35 * hei), (int) Math.round(0.18 * wid),(int) Math.round(0.05 * hei));
		add(btn_m);
		btn_m.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("M");
			}
		});

		btn_vblk = new JButton("V_blk : Blank Vol");
		btn_vblk.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_vblk.setBounds((int) Math.round(0.22 * wid), (int) Math.round(0.35 * hei), (int) Math.round(0.2 * wid),(int) Math.round(0.05 * hei));
		add(btn_vblk);
		btn_vblk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("V_blk");
			}
		});
		
		btn_w = new JButton("W : Sample wt (g/mL)");
		btn_w.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_w.setBounds((int) Math.round(0.43 * wid), (int) Math.round(0.35 * hei), (int) Math.round(0.23 * wid),(int) Math.round(0.05 * hei));
		add(btn_w);
		btn_w.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("W");
			}
		});
		
		btn_ru = new JButton("R.Unit");
		btn_ru.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_ru.setBounds((int) Math.round(0.67 * wid), (int) Math.round(0.35 * hei), (int) Math.round(0.14 * wid),(int) Math.round(0.05 * hei));
		add(btn_ru);
		btn_ru.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("R.U");
			}
		});
		
		btn_lb = new JButton("(");
		btn_lb.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_lb.setBounds((int) Math.round(0.82 * wid), (int) Math.round(0.35 * hei), (int) Math.round(0.072 * wid),(int) Math.round(0.05 * hei));
		add(btn_lb);
		btn_lb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("( ");
			}
		});
		
		
		btn_rb = new JButton(")");
		btn_rb.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_rb.setBounds((int) Math.round(0.90 * wid), (int) Math.round(0.35 * hei), (int) Math.round(0.072 * wid),(int) Math.round(0.05 * hei));
		add(btn_rb);
		btn_rb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula(" )");
			}
		});
		
		user_factor_header = new JLabel("User Factors : ");
		user_factor_header.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		user_factor_header.setBounds((int) Math.round(0.03 * wid), (int) Math.round(0.45 * hei), (int) Math.round(0.18 * wid),(int) Math.round(0.05 * hei));
		add(user_factor_header);
		
		btn_f1 = new JButton("F1");
		btn_f1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_f1.setBounds((int) Math.round(0.18 * wid), (int) Math.round(0.45 * hei), (int) Math.round(0.1 * wid),(int) Math.round(0.05 * hei));
		add(btn_f1);
		btn_f1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("F1");
			}
		});
		
		
		btn_f2 = new JButton("F2");
		btn_f2.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_f2.setBounds((int) Math.round(0.285 * wid), (int) Math.round(0.45 * hei), (int) Math.round(0.1 * wid),(int) Math.round(0.05 * hei));
		add(btn_f2);
		btn_f2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("F2");
			}
		});
		
		btn_f3 = new JButton("F3");
		btn_f3.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_f3.setBounds((int) Math.round(0.39 * wid), (int) Math.round(0.45 * hei), (int) Math.round(0.1 * wid),(int) Math.round(0.05 * hei));
		add(btn_f3);
		btn_f3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("F3");
			}
		});
		
		btn_f4 = new JButton("F4");
		btn_f4.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_f4.setBounds((int) Math.round(0.495 * wid), (int) Math.round(0.45 * hei), (int) Math.round(0.1 * wid),(int) Math.round(0.05 * hei));
		add(btn_f4);
		btn_f4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("F4");
			}
		});

		btn_plus = new JButton("+");
		btn_plus.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_plus.setBounds((int) Math.round(0.60 * wid), (int) Math.round(0.45 * hei), (int) Math.round(0.07 * wid),(int) Math.round(0.05 * hei));
		add(btn_plus);
		btn_plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula(" + ");
			}
		});
		
		btn_minus = new JButton("-");
		btn_minus.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_minus.setBounds((int) Math.round(0.676 * wid), (int) Math.round(0.45 * hei), (int) Math.round(0.07 * wid),(int) Math.round(0.05 * hei));
		add(btn_minus);
		btn_minus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula(" - ");
			}
		});
		
		btn_mul = new JButton("*");
		btn_mul.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_mul.setBounds((int) Math.round(0.753 * wid), (int) Math.round(0.45 * hei), (int) Math.round(0.07 * wid),(int) Math.round(0.05 * hei));
		add(btn_mul);
		btn_mul.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula(" * ");
			}
		});
		
		btn_div = new JButton("/");
		btn_div.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_div.setBounds((int) Math.round(0.83 * wid), (int) Math.round(0.45 * hei), (int) Math.round(0.07 * wid),(int) Math.round(0.05 * hei));
		add(btn_div);
		btn_div.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula(" / ");
			}
		});
		
		btndot = new JButton(".");
		btndot.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btndot.setBounds((int) Math.round(0.906 * wid), (int) Math.round(0.45 * hei), (int) Math.round(0.065 * wid),(int) Math.round(0.05 * hei));
		add(btndot);
		btndot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula(" . ");
			}
		});
		
		btn0 = new JButton("0");
		btn0.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn0.setBounds((int) Math.round(0.18 * wid), (int) Math.round(0.53 * hei), (int) Math.round(0.07 * wid),(int) Math.round(0.05 * hei));
		add(btn0);
		btn0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("0");
			}
		});
		
		btn1 = new JButton("1");
		btn1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn1.setBounds((int) Math.round(0.26 * wid), (int) Math.round(0.53 * hei), (int) Math.round(0.07 * wid),(int) Math.round(0.05 * hei));
		add(btn1);
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("1");
			}
		});
		
		btn2 = new JButton("2");
		btn2.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn2.setBounds((int) Math.round(0.34 * wid), (int) Math.round(0.53 * hei), (int) Math.round(0.07 * wid),(int) Math.round(0.05 * hei));
		add(btn2);
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("2");
			}
		});
		
		btn3 = new JButton("3");
		btn3.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn3.setBounds((int) Math.round(0.42 * wid), (int) Math.round(0.53 * hei), (int) Math.round(0.07 * wid),(int) Math.round(0.05 * hei));
		add(btn3);
		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("3");
			}
		});
		
		btn4 = new JButton("4");
		btn4.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn4.setBounds((int) Math.round(0.50 * wid), (int) Math.round(0.53 * hei), (int) Math.round(0.07 * wid),(int) Math.round(0.05 * hei));
		add(btn4);
		btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("4");
			}
		});
		
		btn5 = new JButton("5");
		btn5.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn5.setBounds((int) Math.round(0.58 * wid), (int) Math.round(0.53 * hei), (int) Math.round(0.07 * wid),(int) Math.round(0.05 * hei));
		add(btn5);
		btn5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("5");
			}
		});
		
		btn6 = new JButton("6");
		btn6.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn6.setBounds((int) Math.round(0.66 * wid), (int) Math.round(0.53 * hei), (int) Math.round(0.07 * wid),(int) Math.round(0.05 * hei));
		add(btn6);
		btn6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("6");
			}
		});
		
		btn7 = new JButton("7");
		btn7.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn7.setBounds((int) Math.round(0.74 * wid), (int) Math.round(0.53 * hei), (int) Math.round(0.07 * wid),(int) Math.round(0.05 * hei));
		add(btn7);
		btn7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("7");
			}
		});
		
		btn8 = new JButton("8");
		btn8.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn8.setBounds((int) Math.round(0.82 * wid), (int) Math.round(0.53 * hei), (int) Math.round(0.07 * wid),(int) Math.round(0.05 * hei));
		add(btn8);
		btn8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("8");
			}
		});
		
		btn9 = new JButton("9");
		btn9.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn9.setBounds((int) Math.round(0.90 * wid), (int) Math.round(0.53 * hei), (int) Math.round(0.07 * wid),(int) Math.round(0.05 * hei));
		add(btn9);
		btn9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula("9");
			}
		});
		
		formula1_header = new JLabel("Formula 1 : ");
		formula1_header.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.025 * wid)));
		formula1_header.setBounds((int) Math.round(0.03 * wid), (int) Math.round(0.62 * hei), (int) Math.round(0.16 * wid), (int) Math.round(0.036 * hei));
		add(formula1_header);
		
		formula1 = new JLabel();
		formula1.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.025 * wid)));
		formula1.setBounds((int) Math.round(0.18 * wid), (int) Math.round(0.62 * hei), (int) Math.round(0.82 * wid), (int) Math.round(0.04 * hei));
		add(formula1);
		
		formula2_header = new JLabel("Formula 2 : ");
		formula2_header.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.025 * wid)));
		formula2_header.setBounds((int) Math.round(0.03 * wid), (int) Math.round(0.69 * hei), (int) Math.round(0.16 * wid), (int) Math.round(0.036 * hei));
		add(formula2_header);
		
		formula2 = new JLabel();
		formula2.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.025 * wid)));
		formula2.setBounds((int) Math.round(0.18 * wid), (int) Math.round(0.69 * hei), (int) Math.round(0.82 * wid), (int) Math.round(0.04 * hei));
		add(formula2);
		
		formula3_header = new JLabel("Formula 3 : ");
		formula3_header.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.025 * wid)));
		formula3_header.setBounds((int) Math.round(0.03 * wid), (int) Math.round(0.77 * hei), (int) Math.round(0.16 * wid), (int) Math.round(0.036 * hei));
		add(formula3_header);
		
		formula3 = new JLabel();
		formula3.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.025 * wid)));
		formula3.setBounds((int) Math.round(0.18 * wid), (int) Math.round(0.77 * hei), (int) Math.round(0.82 * wid), (int) Math.round(0.04 * hei));
		add(formula3);
		
		btn_refresh = new JButton("Refresh");
		btn_refresh.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_refresh.setBounds((int) Math.round(0.25 * wid), (int) Math.round(0.85 * hei), (int) Math.round(0.2 * wid),(int) Math.round(0.060 * hei));
		add(btn_refresh);
		btn_refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				radio_btn1.setSelected(true);
				formula1.setText("");
				formula2.setText("");
				formula3.setText("");
				formula1_arr.clear();
				formula2_arr.clear();
				formula3_arr.clear();
				formula1_header.setText("Formula 1 : ");
				formula2_header.setText("Formula 2 : ");
				formula3_header.setText("Formula 3 : ");
				name1.setText(formula1_header.getText().toString());

			}
		});
		
		btn_addformula = new JButton("Add Formula");
		btn_addformula.setFont(new Font("Times New Roman", Font.BOLD, (int) Math.round(0.018 * wid)));
		btn_addformula.setBounds((int) Math.round(0.55 * wid), (int) Math.round(0.85 * hei), (int) Math.round(0.2 * wid),(int) Math.round(0.060 * hei));
		add(btn_addformula);
		btn_addformula.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		initialize();
	}
	
	public static void formula(String msg) {
		if(radio_btn1.isSelected()) {
			formula1_arr.add(msg);
			formula1.setText(String.join("", formula1_arr));
		}
		else if(radio_btn2.isSelected()) {
			formula2_arr.add(msg);
			formula2.setText(String.join("", formula2_arr));
		}
		else if(radio_btn3.isSelected()) {
			formula3_arr.add(msg);
			formula3.setText(String.join("", formula3_arr));
		}
		
	}

}
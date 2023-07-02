package main.java;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

public class ReformatBuffer {

    public String outputString;
    static int cutoffASCII = 10; // ASCII code of the character used for cut-off between received messages
    static String bufferReadToString = ""; // empty, but not null
    static String data="";
    public static String current_state="";
    public static String current_exp="";

    public static void parseByteArray(byte[] readBuffer) {
    	
    	
        String s = new String(readBuffer);
//         bufferReadToString = bufferReadToString.concat(s);
     //   System.out.println("ReformatBuffer = "+readBuffer);
       //   DrawGraph.temp_graph(s);
        
//        DateFormat dateFormat244 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
//		String date_time244 = dateFormat244.format(new Date()).toString();
		//System.out.println("Dattaaaaa = "+s+"   -   Time = "+ date_time244);

          
        if(s.contains("*")){
        	bufferReadToString+=s;
           
        	bufferReadToString =bufferReadToString.replaceAll("\\\n","");
        	bufferReadToString =bufferReadToString.replaceAll("\\\t","");;

        	
        	if(!bufferReadToString.contains("T") && !bufferReadToString.contains("N")) {
        	
        	
        	if(bufferReadToString.contains("T")) {
         		//System.out.println("Contains T ");

        		if(current_exp.matches("main")) {
        			menubar.update_mv_main(bufferReadToString);
        		}
        		else if(current_exp.matches("pot")) {
        			DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
        			String date_time = dateFormat2.format(new Date()).toString();
        		//	System.out.println(bufferReadToString +"-------------"+date_time);

        			DrawGraph_pot.update_mv_pot(bufferReadToString);
        		}
        		else if(current_exp.matches("kf")) {
        			DrawGraph_kf.update_mv_kf(bufferReadToString); 
        		}
        		else if(current_exp.matches("calibrate")) {
        			calibrate_electrode.update_calibrate_mv(bufferReadToString);
        			menubar.update_mv_main(bufferReadToString);
        		}
        	}
        	
        	if(bufferReadToString.contains("N") && !bufferReadToString.contains("END")) {
        		if(current_exp.matches("main")) {
        			menubar.update_mv_main(bufferReadToString);
        		}
        		else if(current_exp.matches("pot")) {
        			DrawGraph_pot.update_mv_pot(bufferReadToString);
        		}
        		else if(current_exp.matches("kf")) {
        			DrawGraph_kf.update_mv_kf(bufferReadToString); 
        		}  
        		else if(current_exp.matches("calibrate")) {
        			calibrate_electrode.update_calibrate_mv(bufferReadToString);
        			menubar.update_mv_main(bufferReadToString);
        		}
        	}
        	
        	if(current_state.matches("mb_start") && bufferReadToString.contains("END")) {
            	menubar.comport_success();
            } 
        	else if(current_state.matches("menubar_rate") && bufferReadToString.contains("OK")) {
				JOptionPane.showMessageDialog(null, "Data Rate configured successfully!");
				current_state = "";
            } 
        	
        	else if(current_state.matches("menubar_fill") && bufferReadToString.contains("OK")) {
            //	menubar.start_volume_counter();
            	current_state = "menubar_fill_waiting_ok2";
            }
        	
            else if(current_state.matches("menubar_fill") && bufferReadToString.contains("END")) {
            	//current_state = "menubar_fill_waiting_ok2";
            }

            else if(current_state.matches("menubar_fill_waiting_ok2") && bufferReadToString.contains("OK2")) {
            	menubar.start_volume_counter();
            }
        	
            else if(current_state.matches("menubar_fill_waiting_ok2") && bufferReadToString.contains("END")) {
            	//System.out.println("Insidee menubar fill end recieved!");
            	menubar.mb_fill_end_received();
            }
            
            else if(current_state.matches("menubar_dose") && bufferReadToString.contains("OK")) {
            	//menubar.start_dose_counter();
            	current_state = "menubar_dose_waiting_ok2";
            }
            else if(current_state.matches("menubar_dose") && bufferReadToString.contains("END")) {
                //current_state = "menubar_dose_waiting_ok2";
            }
            
            else if(current_state.matches("menubar_dose_waiting_ok2") && bufferReadToString.contains("OK2")) {
            	menubar.start_dose_counter();
            }
            else if(current_state.matches("menubar_dose_waiting_ok2") && bufferReadToString.contains("END")) {
				// System.out.println("Insidee menubar dose end received!");
            	menubar.mb_dose_end_received();
            }
            else if(current_state.matches("menubar_stpm") && bufferReadToString.contains("OK")) {
//            	System.out.println("Insidee menubar fill end recieved!");
//            	menubar.stop_volume_counter();
            	current_state = "";
            }
        	
            else if(current_state.matches("menubar_wash") && bufferReadToString.contains("OK")) {
            	menubar.wash_started();
            }
            else if(current_state.matches("menubar_wash") && bufferReadToString.contains("END")) {
            	menubar.update_wash_cycle();
            }
            
            else if(current_state.matches("menubar_rinse") && bufferReadToString.contains("OK")) {
            	menubar.rinse_started();
            }
            else if(current_state.matches("menubar_rinse") && bufferReadToString.contains("END")) {
            	menubar.update_rinse_cycle();
            }

            else if(current_state.matches("menubar_esc") && bufferReadToString.contains("END")) {
            	menubar.escape();
            }
            else if(current_state.matches("dg_pot_dosr") && bufferReadToString.contains("OK")) {
            	DrawGraph_pot.dosr_ok_recieved();
            }
            else if(current_state.matches("dg_pot_cvop") && bufferReadToString.contains("OK")) {
            	DrawGraph_pot.cvop_ok_recieved();
            }
            else if(current_state.matches("dg_pot_afil") && bufferReadToString.contains("OK")) {
            	current_state = "dg_pot_afil_waiting_ok2";
            }  
            else if(current_state.matches("dg_pot_afil_waiting_ok2") && bufferReadToString.contains("OK2")) {
            	DrawGraph_pot.afil_ok_recieved(); 
            }
            else if(current_state.matches("dg_pot_afil_waiting_ok2") && bufferReadToString.contains("END")) {
            	DrawGraph_pot.afil_end_recieved();
            }
            else if(current_state.matches("dg_pot_pre_dose_then_dose") && bufferReadToString.contains("OK2")) {
            	DrawGraph_pot.dose_ok_recieved();
            }
            else if(current_state.matches("dg_pot_dose") && bufferReadToString.contains("OK")) {
            	//DrawGraph_pot.dose_ok_recieved();
            	current_state = "dg_pot_dose_waiting_ok2";
            }
            else if(current_state.matches("dg_pot_dose") && bufferReadToString.contains("END")) {
            	//current_state = "dg_pot_dose_waiting_ok2";
            }
            else if(current_state.matches("dg_pot_dose_waiting_ok2") && bufferReadToString.contains("OK2")) {
            	DrawGraph_pot.dose_ok_recieved();
            }
            else if(current_state.matches("dg_pot_dose_waiting_ok2") && bufferReadToString.contains("END")) {
            	DrawGraph_pot.dose_end_recieved();
            }
            else if(current_state.matches("dg_pot_predose") && bufferReadToString.contains("OK")) {
            	//DrawGraph_pot.pre_dose_ok_recieved();
            	current_state = "dg_pot_predose_waiting_ok2";
            }
            else if(current_state.matches("dg_pot_predose") && bufferReadToString.contains("END")) {
            	//current_state = "dg_pot_predose_waiting_ok2";
            }
            else if(current_state.matches("dg_pot_predose_waiting_ok2") && bufferReadToString.contains("OK2")) {
            	DrawGraph_pot.pre_dose_ok_recieved();
            }
            else if(current_state.matches("dg_pot_predose_waiting_ok2") && bufferReadToString.contains("END")) {
            	DrawGraph_pot.pre_dose_end_recieved();
            }
            else if(current_state.matches("dg_pot_stmp_predose") && bufferReadToString.contains("OK")) {
            	DrawGraph_pot.pre_dose_stmp_end_recieved();
            }
            else if(current_state.matches("dg_pot_pre_dose_then_dosr") && bufferReadToString.contains("OK")) {
            	DrawGraph_pot.pre_dose_dosr_end_recieved();
            }
            
        	
            else if(current_state.matches("dg_pot_home_dosr") && bufferReadToString.contains("OK")) {
            	DrawGraph_pot.pot_home_dosr();
            }
            else if(current_state.matches("dg_pot_home_escp") && bufferReadToString.contains("OK")) {
            	DrawGraph_pot.pot_home_escp();
            	current_state="";
            }
            else if(current_state.matches("dg_kf_cvok") && bufferReadToString.contains("OK")) {
            	DrawGraph_kf.cvok_ok_received();
            }
            else if(current_state.matches("dg_kf_afil") && bufferReadToString.contains("OK")) {
            	//DrawGraph_kf.afill_ok_received();
            	current_state = "dg_kf_afil_waiting_ok2";
            }
            else if(current_state.matches("dg_kf_afil") && bufferReadToString.contains("END")) {
            	//current_state = "dg_kf_afil_waiting_ok2";
            }
            else if(current_state.matches("dg_kf_afil_waiting_ok2") && bufferReadToString.contains("OK2")) {
            	DrawGraph_kf.afill_ok_received();
            }
            else if(current_state.matches("dg_kf_afil_waiting_ok2") && bufferReadToString.contains("END")) {
            	DrawGraph_kf.afill_end_received();
            }
            else if(current_state.matches("dg_kf_dose") && bufferReadToString.contains("OK")) {
            	//DrawGraph_kf.dose_ok_received();
            	current_state = "dg_kf_dose_waiting_ok2";
            }            
            else if(current_state.matches("dg_kf_dose") && bufferReadToString.contains("END")) {
            	//current_state = "dg_kf_dose_waiting_ok2";
            }		
            else if(current_state.matches("dg_kf_dose_waiting_ok2") && bufferReadToString.contains("OK2")) {
            	DrawGraph_kf.dose_ok_received();
            }            
            else if(current_state.matches("dg_kf_dose_waiting_ok2") && bufferReadToString.contains("END")) {
            	DrawGraph_kf.dose_end_received();
            }	
            else if(current_state.matches("dg_kf_stmp") && bufferReadToString.contains("OK")) {
            	DrawGraph_kf.stmp_ok_received();
            }
            else if(current_state.matches("dg_kf_dosr") && bufferReadToString.contains("OK")) {
            	DrawGraph_kf.dosr_ok_received();
            }
            else if(current_state.matches("dg_kf_dose_timer") && bufferReadToString.contains("OK")) {
            	DrawGraph_kf.dose_ok_timer_received();
            }
            else if(current_state.matches("dg_kf_dose_timer") && bufferReadToString.contains("END")) {
            	DrawGraph_kf.dose_end_received();
            }
            else if(current_state.matches("dg_kf_home_dosr") && bufferReadToString.contains("OK")) {
            	DrawGraph_kf.kf_home_dosr();
            }
            else if(current_state.matches("dg_kf_home_escp") && bufferReadToString.contains("OK")) {
            	DrawGraph_kf.kf_home_escp();
            	current_state="";
            }
        	
            else if(current_state.matches("bc_dosr") && bufferReadToString.contains("OK")) {
            	burette_calibration.bc_dosr_ok_received();
            }
            else if(current_state.matches("dg_bc_cvop") && bufferReadToString.contains("OK")) {
            	burette_calibration.cvop_ok_received();
            }
            else if(current_state.matches("bc_afil") && bufferReadToString.contains("OK")) {
            	current_state = "bc_afil_waiting_ok2";
            }  
            else if(current_state.matches("bc_afil_waiting_ok2") && bufferReadToString.contains("OK2")) {
            	burette_calibration.bc_afill_ok_received();
            }
            else if(current_state.matches("bc_afil_waiting_ok2") && bufferReadToString.contains("END")) {
            	burette_calibration.bc_afill_end_received();
            }
            else if(current_state.matches("bc_dose") && bufferReadToString.contains("OK")) {
            	//burette_calibration.bc_dose_ok_received();
            	current_state = "bc_dose_waiting_ok2";
            } 
            else if(current_state.matches("bc_dose_waiting_ok2") && bufferReadToString.contains("OK2")) {
            	burette_calibration.bc_dose_ok_received();
            }
            else if(current_state.matches("bc_stpm") && bufferReadToString.contains("END")) {
            	burette_calibration.bc_stpm_ok_received();
            } 
        	
        	//burette_calibration_test
        	
            else if(current_state.matches("bct_dosr") && bufferReadToString.contains("OK")) {
            	burette_calibration_test.bct_dosr_ok_received();
            }
            else if(current_state.matches("dg_bct_cvop") && bufferReadToString.contains("OK")) {
            	burette_calibration_test.cvop_ok_received();
            }
            else if(current_state.matches("bct_afil") && bufferReadToString.contains("OK")) {
            	current_state = "bct_afil_waiting_ok2";
            }  
            else if(current_state.matches("bct_afil_waiting_ok2") && bufferReadToString.contains("OK2")) {
            	burette_calibration_test.bct_afill_ok_received();
            }
            else if(current_state.matches("bct_afil_waiting_ok2") && bufferReadToString.contains("END")) {
            	burette_calibration_test.bct_afill_end_received();
            }
            else if(current_state.matches("bct_dose") && bufferReadToString.contains("OK")) {
            	//burette_calibration.bct_dose_ok_received();
            	current_state = "bct_dose_waiting_ok2";
            } 
            else if(current_state.matches("bct_dose_waiting_ok2") && bufferReadToString.contains("OK2")) {
            	burette_calibration_test.bct_dose_ok_received();
            }
            else if(current_state.matches("bct_stpm") && bufferReadToString.contains("END")) {
            	burette_calibration_test.bct_stpm_ok_received();
            } 
        	
            bufferReadToString="";

        }
        else
        {
        	// System.out.println("elseelse "+s);
             bufferReadToString = bufferReadToString.concat(s);
        }   
    }
    public void set_msg_to_null() {
    	bufferReadToString=null;   	
    }
    
   
    
//  System.out.println("GGGGG "+s);
//bufferReadToString = bufferReadToString.concat(s);
//System.out.println(bufferReadToString);
//if((bufferReadToString.indexOf(cutoffASCII) + 1) > 0)
//{
//String outputString = bufferReadToString.substring(0, bufferReadToString.indexOf(cutoffASCII) + 1);
//bufferReadToString = bufferReadToString.substring(bufferReadToString.indexOf(cutoffASCII) + 1); // adjust as needed to accommodate the CRLF convention ("\n\r"), ASCII 10 & 13
//Main m=new Main();
//System.out.print("FFFF : "+outputString);
//m.recieved_message(outputString);
//}
    
    
    
    
    
    ////
    ////
    ////
    //
//  bufferReadToString = bufferReadToString.concat(s);
//  System.out.println(bufferReadToString);
//  if((bufferReadToString.indexOf(cutoffASCII) + 1) > 0)
//  {
//  String outputString = bufferReadToString.substring(0, bufferReadToString.indexOf(cutoffASCII) + 1);
//  bufferReadToString = bufferReadToString.substring(bufferReadToString.indexOf(cutoffASCII) + 1); // adjust as needed to accommodate the CRLF convention ("\n\r"), ASCII 10 & 13
//  Main m=new Main();
//  System.out.print("FFFF : "+outputString);
//  m.recieved_message(outputString);
//  }
    /////
    ////
    ////
    
//    bufferReadToString = bufferReadToString.concat(s);
//        System.out.println(bufferReadToString);
//        if((bufferReadToString.indexOf(cutoffASCII) + 1) > 0)
//    {
//        String outputString = bufferReadToString.substring(0, bufferReadToString.indexOf(cutoffASCII) + 1);
//        bufferReadToString = bufferReadToString.substring(bufferReadToString.indexOf(cutoffASCII) + 1); // adjust as needed to accommodate the CRLF convention ("\n\r"), ASCII 10 & 13
//        Main m=new Main();
//        System.out.print("FFFF : "+outputString);
//        m.recieved_message(outputString);
//    }
//    public static void appendStrToFile(String data)
//    {
//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
//        String Date = formatter.format(date);
//        String path="C:\\Mayura Analytical LLP\\"+Date+".txt";
//        File fileName2=new File(path);
//
//        try {
//            BufferedWriter out = new BufferedWriter(new FileWriter(fileName2, true));
//            out.write(data);
//            out.newLine();
//            out.close();
//        }
//        catch (IOException e) {
//            System.out.println("exception occured" + e);
//        }
//    }
}
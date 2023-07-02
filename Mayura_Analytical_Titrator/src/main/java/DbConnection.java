package main.java;
/*import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
public class DbConnection {
	public static Connection connect() {
		Connection con = null;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:Login.db");
			System.out.println("Connected");
		}
		catch(ClassNotFoundException | SQLException e) {
			System.out.println(e + "");
		}
		return con;
	}
}*/

import java.sql.Connection;  
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;  

import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;  
   
public class DbConnection {  
     
    public static Connection connect() {  
        Connection con = null;  
        try {  
            // db parameters  
            String url = "jdbc:sqlite:C:/sqlite/Login.db";  
            // create a connection to the database  
            con = DriverManager.getConnection(url);  
            //con = DriverManager.getConnection("jdbc:sqlite:Login.db");
          //  System.out.println("Connected");
            createNewTable();
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        } 
        catch(NullPointerException ppp) {
        	
        }
        /*finally {  
            try {  
                if (con != null) {  
                    con.close();  
                }  
            } catch (SQLException ex) {  
                System.out.println(ex.getMessage());  
            }  
        }*/
        
		return con;  
    }  
    public static String get_date() {
    	DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		String date_time = dateFormat2.format(new Date()).toString();
		return date_time; 
	}
    
    public static void createNewTable() {  
        // SQLite connection string  
        String url = "jdbc:sqlite:C:/sqlite/Login.db";   
        
        String create_SuperAdminLogin_table = "CREATE TABLE IF NOT EXISTS SuperAdminLogin (\n"  
                + " Username text NOT NULL PRIMARY KEY,\n"  
                + " Password text NOT NULL\n"
                + ");";  
        String create_UserLogin_table = "CREATE TABLE IF NOT EXISTS UserLogin (\n"  
                + " Username TEXT NOT NULL PRIMARY KEY,\n"  
                + " Password text NOT NULL,\n"
                + " created_date text NOT NULL,\n"
                + " validity text NOT NULL,\n"
                + "Roles TEXT NOT NULL\n"
                + ");"; 
        String create_Roles_table = "CREATE TABLE IF NOT EXISTS Roles (\n"  
                + " RoleName TEXT ,\n"  
                + " Items text \n"
                + ");";  
        String create_pot_methods_table = "CREATE TABLE IF NOT EXISTS potentiometry_methods (\n"  
                + " method_name text ,\n"
                + " created_by text, \n"
                + " created_date date, \n"
                + " updated_by text, \n"
                + " updated_date date, \n"
                + " pre_dose text, \n"
                + " stir_time text, \n"
                + " max_vol text, \n"
                + " blank_vol text, \n"
                + " burette_factor text, \n"
                + " threshold text, \n"
                + " filter text, \n"
                + " dosage_rate text, \n"
                + " no_of_trials text, \n"
                + " factor1 text, \n"
                + " factor2 text, \n"
                + " factor3 text, \n"
                + " factor4 text, \n"
                + " ep_select text, \n"  
                + " formula_no text, \n"  
                + " result_unit text, \n"  
                + " sop text \n"
                + ");"; 

        String create_kf_methods_table = "CREATE TABLE IF NOT EXISTS kf_methods (\n"  
        		 + " method_name text ,\n"
                 + " created_by text, \n"
                 + " created_date date, \n"
                 + " updated_by text, \n"
                 + " updated_date date, \n"
                 + " delay text, \n"
                 + " stir_time text, \n"
                 + " max_vol text, \n"
                 + " blank_vol text, \n"
                 + " burette_factor text, \n"
                 + " density text, \n"
                 + " kf_factor text, \n"
                 + " end_point text, \n"
                 + " dosage_rate text, \n"
                 + " result_unit text, \n"
                 + " no_of_trials text, \n"
                 + " sop text \n"
                + ");"; 
        
        String create_config_param_table = "CREATE TABLE IF NOT EXISTS config_param (\n"  
       		    + " cnfg_param_group text,\n"
                + " cnfg_param_name text NOT NULL UNIQUE, \n"
                + " cnfg_param_value text \n"
               + ");"; 
        
        String create_company_data_table = "CREATE TABLE IF NOT EXISTS company_data (\n"  
                + " Slno text NOT NULL UNIQUE,\n"  
                + " instrument_id text ,\n"  
                + " company_logo text, \n"
                + " company_name text, \n"
                + " company_address text, \n"
                + " start_date text, \n"
                + " validity text \n"
                + ");"; 
        String create_audit_log_table = "CREATE TABLE IF NOT EXISTS audit_log (\n"  
                + " date date ,\n"
        		+ " time text ,\n"
                + " uid text, \n"
                + " msg text, \n"
                + " note text \n"
                + ");";      
        String create_kf_table = "CREATE TABLE IF NOT EXISTS kf (\n"  
                + " report_name text NOT NULL UNIQUE, \n"
                + " date date , \n"
                + " parameters text, \n"
                + " details text, \n"
                + " kff_trials text, \n"
                + " kff_result text, \n"
                + " moisture_trials text, \n"
                + " moisture_result text, \n"
                + " remarks text \n"
                + ");"; 
        String create_potentiometry_table = "CREATE TABLE IF NOT EXISTS potentiometry (\n"  
                + " report_name text NOT NULL UNIQUE, \n"
                + " date date , \n"
                + " parameters text, \n"
                + " details text, \n"
                + " graph_dataX text, \n"
                + " graph_dataY text, \n"
                + " header text, \n"
                + " trial_data text, \n"
                + " results text, \n"
                + " remarks text \n"
                + ");";
        
		String formula1 = "INSERT OR IGNORE INTO config_param(cnfg_param_group,cnfg_param_name,cnfg_param_value) VALUES('formulas','1','N= \\frac {W*(1-\\frac{M}{100})*F_3*F_4}{(V_1-V_B_L_K)*F_1*F_2}')";
		String formula2 = "INSERT OR IGNORE INTO config_param(cnfg_param_group,cnfg_param_name,cnfg_param_value) VALUES('formulas','2','Analyte= \\frac {V_1*N*F_1*F_2*Unit}{W*F_3*F_4}')";
		String formula3 = "INSERT OR IGNORE INTO config_param(cnfg_param_group,cnfg_param_name,cnfg_param_value) VALUES('formulas','3','Analyte= \\frac {(V_1-V_B_L_K)*N*F_1*F_2*Unit}{W*(1-\\frac {M}{1000})*F_3*F_4}')";
		String formula4 = "INSERT OR IGNORE INTO config_param(cnfg_param_group,cnfg_param_name,cnfg_param_value) VALUES('formulas','4','Analyte= \\frac {(V_2-V_1)*N*F_1*F_2*Unit}{W*(1-\\frac {M}{1000})*F_3*F_4}')";
		String formula5 = "INSERT OR IGNORE INTO config_param(cnfg_param_group,cnfg_param_name,cnfg_param_value) VALUES('formulas','5','Analyte A= \\frac {V_1*N*F_1*Unit}{W*F_3},Analyte B= \\frac {(V_2-V_1)*N*F_2*Unit}{W*F_4}')";
		String formula6 = "INSERT OR IGNORE INTO config_param(cnfg_param_group,cnfg_param_name,cnfg_param_value) VALUES('formulas','6','Analyte A= \\frac {V_1*N*F_1*F_2*Unit}{W*F_3*F_4},Analyte B= \\frac {(V_2-V_1)*N*F_1*F_2*Unit}{W*F_3*F_4},Analyte C= \\frac {(V_3-V_2)*N*F_1*F_2*Unit}{W*F_3*F_4}')";
		String formula7 = "INSERT OR IGNORE INTO config_param(cnfg_param_group,cnfg_param_name,cnfg_param_value) VALUES('formulas','7','Carbonate= \\frac {(V_2-2V_1)*N*F_1*Unit}{W*F_3},Alkali= \\frac {2(V_2-V_1)*N*F_2*Unit}{W*F_4}')";
		String formula8 = "INSERT OR IGNORE INTO config_param(cnfg_param_group,cnfg_param_name,cnfg_param_value) VALUES('formulas','8','BiCarbonate= \\frac {V_1*N*F_1*F_2*Unit}{W*F_3*F_4},Carbonate= \\frac {(V_2-2V_1)*N*F_1*F_2*Unit}{W*F_3*F_4},Alkali= \\frac {2(V_3-V_2)*N*F_1*F_2*Unit}{W*F_3*F_4}')";
		String formula9 = "INSERT OR IGNORE INTO config_param(cnfg_param_group,cnfg_param_name,cnfg_param_value) VALUES('formulas','9','BiCarbonate= \\frac {V_1*N*F_1*Unit}{W*F_3},Carbonate= \\frac {(V_2-2V_1)*N*F_2*Unit}{W*F_4}')";
		String formula10 = "INSERT OR IGNORE INTO config_param(cnfg_param_group,cnfg_param_name,cnfg_param_value) VALUES('formulas','10','Analyte A= \\frac {V_1*N*F_1*Unit}{W*F_3},Analyte B= \\frac {V_2*N*F_2*Unit}{W*F_4}')";

		String set_bf = "INSERT OR IGNORE INTO config_param(cnfg_param_group,cnfg_param_name,cnfg_param_value) VALUES('buretteFactor','buretteFactor','1')";
		String set_electrode_factor = "INSERT OR IGNORE INTO config_param(cnfg_param_group,cnfg_param_name,cnfg_param_value) VALUES('electrodeFactor','electrodeFactor','0')";
		String set_trial_alter_prmission = "INSERT OR IGNORE INTO config_param(cnfg_param_group,cnfg_param_name,cnfg_param_value) VALUES('trials_altering','permission_to_alter_trial','false')";

		String set_company_details = "INSERT OR IGNORE INTO company_data(Slno,instrument_id,company_logo,company_name,company_address,start_date,validity) VALUES('1','HP77','C:\\SQLite\\logo\\logo.png','Mayura Analytical','Bangalore','"+get_date()+"','100000')";

		try{  
            Connection con = DriverManager.getConnection(url);  
            Statement sql = con.createStatement();  
            sql.execute(create_SuperAdminLogin_table);  
            sql.execute(create_UserLogin_table);
            sql.execute(create_Roles_table);
            sql.execute(create_pot_methods_table);
            sql.execute(create_kf_methods_table);
            sql.execute(create_config_param_table);
            sql.execute(create_company_data_table);
            sql.execute(create_audit_log_table);
            sql.execute(create_kf_table);
            sql.execute(create_potentiometry_table);
            sql.execute(formula1);
            sql.execute(formula2);
            sql.execute(formula3);
            sql.execute(formula4);
            sql.execute(formula5);
            sql.execute(formula6);
            sql.execute(formula7);
            sql.execute(formula8);
            sql.execute(formula9);
            sql.execute(formula10);
            sql.execute(set_bf);
            sql.execute(set_electrode_factor);
            sql.execute(set_trial_alter_prmission);
            sql.execute(set_company_details);

            
        } 
		catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
    }  
    
    public static void main(String[] args) {  
        connect();  
        //createNewTable(); 
    }  
}  

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
    
    public static void createNewTable() {  
        // SQLite connection string  
        String url = "jdbc:sqlite:C:/sqlite/Login.db";   
        
        String SuperAdmin = "CREATE TABLE IF NOT EXISTS SuperAdminLogin (\n"  
                + " Username text NOT NULL PRIMARY KEY,\n"  
                + " Password text NOT NULL\n"
                + ");";  
        String Users = "CREATE TABLE IF NOT EXISTS UserLogin (\n"  
                + " Username TEXT NOT NULL PRIMARY KEY,\n"  
                + " Password text NOT NULL,\n"
                + " created_date text NOT NULL,\n"
                + " validity text NOT NULL,\n"
                + "Roles TEXT NOT NULL\n"
                + ");"; 
        String Roles = "CREATE TABLE IF NOT EXISTS Roles (\n"  
                + " RoleName TEXT ,\n"  
                + " Items text \n"
                + ");";  
        String Trials = "CREATE TABLE IF NOT EXISTS pot_method (\n"  
                + " Trial_name text ,\n"  
                + " Date date, \n"
                + " Value text \n"
                + ");"; 
        String Trials1 = "CREATE TABLE IF NOT EXISTS amp_method (\n"  
                + " Trial_name text ,\n"  
                + " Date date, \n"
                + " Value text \n"
                + ");"; 
        String Trials2 = "CREATE TABLE IF NOT EXISTS ph_method (\n"  
                + " Trial_name text ,\n"  
                + " Date date, \n"
                + " Value text \n"
                + ");"; 
        String Trials3 = "CREATE TABLE IF NOT EXISTS kf_method (\n"  
                + " Trial_name text ,\n"  
                + " Date date, \n"
                + " Value text \n"
                + ");"; 
        String Trials4 = "CREATE TABLE IF NOT EXISTS company_data (\n"  
                + " Slno text NOT NULL UNIQUE,\n"  
                + " instrument_id text ,\n"  
                + " company_logo text, \n"
                + " company_name text, \n"
                + " company_address text, \n"
                + " start_date text, \n"
                + " validity text \n"
                + ");"; 
        String Trials5 = "CREATE TABLE IF NOT EXISTS audit_log (\n"  
                + " date date ,\n"
        		+ " time text ,\n"
                + " uid text, \n"
                + " msg text, \n"
                + " note text \n"
                + ");"; 
        String Trials6 = "CREATE TABLE IF NOT EXISTS formulas (\n"  
                + " no text NOT NULL UNIQUE, \n"
                + " formula text \n"
                + ");";       
        String Trials7 = "CREATE TABLE IF NOT EXISTS kf (\n"  
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
        String Trials8 = "CREATE TABLE IF NOT EXISTS potentiometry (\n"  
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
        String Trials9 = "CREATE TABLE IF NOT EXISTS burette_factor (\n"  
                + " SlNo text NOT NULL UNIQUE, \n"
                + " b_factor text, \n"
                + " permision text \n"
                + ");";
        
		String formula1 = "INSERT OR IGNORE INTO formulas(no,formula) VALUES('1','N= \\frac {W*(1-\\frac{M}{100})*F_3*F_4}{(V_1-V_B_L_K)*F_1*F_2}')";
		String formula2 = "INSERT OR IGNORE INTO formulas(no,formula) VALUES('2','Analyte= \\frac {V_1*N*F_1*F_2*Unit}{W*F_3*F_4}')";
		String formula3 = "INSERT OR IGNORE INTO formulas(no,formula) VALUES('3','Analyte= \\frac {(V_1-V_B_L_K)*N*F_1*F_2*Unit}{W*(1-\\frac {M}{1000})*F_3*F_4}')";
		String formula4 = "INSERT OR IGNORE INTO formulas(no,formula) VALUES('4','N= \\frac {W*(1-\\frac{M}{100})*F_3*F_4}{(V_2-V_1)*F_1*F_2}')";
		String formula5 = "INSERT OR IGNORE INTO formulas(no,formula) VALUES('5','Analyte= \\frac {(V_2-V_1)*N*F_1*F_2*Unit}{W*(1-\\frac {M}{1000})*F_3*F_4}')";
		String formula6 = "INSERT OR IGNORE INTO formulas(no,formula) VALUES('6','Analyte A= \\frac {V_1*N*F_1*Unit}{W*F_3},Analyte B= \\frac {(V_2-V_1)*N*F_2*Unit}{W*F_4}')";
		String formula7 = "INSERT OR IGNORE INTO formulas(no,formula) VALUES('7','Analyte A= \\frac {V_1*N*F_1*F_2*Unit}{W*F_3*F_4},Analyte B= \\frac {(V_2-V_1)*N*F_1*F_2*Unit}{W*F_3*F_4},Analyte C= \\frac {(V_3-V_2)*N*F_1*F_2*Unit}{W*F_3*F_4}')";
		String formula8 = "INSERT OR IGNORE INTO formulas(no,formula) VALUES('8','Analyte A= \\frac {V_1*N*F_1*Unit}{W*F_3},Analyte B= \\frac {(V_2-V_1)*N*F_2*Unit}{W*F_4}')";
		String formula9 = "INSERT OR IGNORE INTO formulas(no,formula) VALUES('9','Carbonate= \\frac {(V_2-2V_1)*N*F_1*Unit}{W*F_3},Alkali= \\frac {2(V_2-V_1)*N*F_2*Unit}{W*F_4}')";
		String formula10 = "INSERT OR IGNORE INTO formulas(no,formula) VALUES('10','BiCarbonate= \\frac {V_1*N*F_1*F_2*Unit}{W*F_3*F_4},Carbonate= \\frac {(V_2-2V_1)*N*F_1*F_2*Unit}{W*F_3*F_4},Alkali= \\frac {2(V_3-V_2)*N*F_1*F_2*Unit}{W*F_3*F_4}')";
		String formula11 = "INSERT OR IGNORE INTO formulas(no,formula) VALUES('11','BiCarbonate= \\frac {V_1*N*F_1*Unit}{W*F_3},Carbonate= \\frac {(V_2-2V_1)*N*F_2*Unit}{W*F_4}')";
		String formula12 = "INSERT OR IGNORE INTO formulas(no,formula) VALUES('12','Analyte A= \\frac {V_1*N*F_1*Unit}{W*F_3},Analyte B= \\frac {V_2*N*F_2*Unit}{W*F_4}')";

		String set_bf = "INSERT OR IGNORE INTO burette_factor(SlNo,b_factor,permision) VALUES('1','1,0','true')";
		String set_company_details = "INSERT OR IGNORE INTO company_data(Slno,instrument_id,company_logo,company_name,company_address,start_date,validity) VALUES('1','HP77','C:\\SQLite\\logo\\logo.png','Mayura Analytical','Bangalore','2022-06-08','100000')";


		try{  
            Connection con = DriverManager.getConnection(url);  
            Statement sql = con.createStatement();  
            sql.execute(SuperAdmin);  
            sql.execute(Users);
            sql.execute(Roles);
            sql.execute(Trials);
            sql.execute(Trials1);
            sql.execute(Trials2);
            sql.execute(Trials3);
            sql.execute(Trials4);
            sql.execute(Trials5);
            sql.execute(Trials6);
            sql.execute(Trials7);
            sql.execute(Trials8);
            sql.execute(Trials9);
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
            sql.execute(formula11);
            sql.execute(formula12);
            sql.execute(set_bf);
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

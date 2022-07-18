# Mayura_Titrator

AdminLogin.java (Window/Screen): UserLogin screen
audit_log_push.java: Used to puh data to audit log
burette_calibration_test.java (Window/Screen): Testing burette Calibration screen
burette_calibration.java (Window/Screen): Burette Calilbration Page
calibrate_electrode.java (Window/Screen): Electrode Calibration pop-up
close_all.java: used to close all window from home page when user clicks default close button on top right 
DateLabelFormatter.java: Used for selecting the date in a format from calender in multiple page
db_controller.java: all db queries will be present here
DBConnection.java: Used for creatng connection to local DataBase(DB) and initial creation of DB
device_data.java (Window/Screen): used to take Service Enginner (Engineer from Mayura Analytical Side) input regarding the Client company details (Setting the validation days and trials selection option from this page)
DrawGraph_kf.java (Window/Screen): KF experiment page
DrawGraph_pot.java (Window/Screen): Potentiometry experiment page
DrawReport_kf.java (Window/Screen): KF view report page (Printing of report only done from View Report page)
DrawReport_pot (Window/Screen): Potentiometry view report page (Printing of report only done from View Report page)
edit_roles.java (Window/Screen): Editing of roles page
edit_user.java (Window/Screen): Editing user details page (If the user forgets his/her password. Client side superadmin can reset their password and also increase their user-validity)
encrypt_decrypt.java: Encryption and Decryption code is available in this class
log_viewer.java (Window/Screen): Used fror viewing Audit Logs

menubar.java (Window/Screen): Main Home Page

MyComPortListener.java: Creates a connnection with the ComPort and cretaes a listener where it records the input data from the device and pushes to ReformatBuffer.java for buffer storage.
new_formula.java (Window/Screen): New Formula Screen
new_role.java (Window/Screen): Creating new Role and adding the rules for the rules (Choose the Certify Option from this page)
new_user.java (Window/Screen): Creating new user
open_kf_result.java (Window/Screen): Used to recall the KF Experiments conducted
open_method.java (Window/Screen): Used to open the method list
open_pot_result.java (Window/Screen): used to recall the Potentimetry Experiments conducted
open_report.java (Window/Screen): used to open the generated report list (All reports mixed)
open_rinse_counter.java (Window/Screen): used to keep the rinse cycle count
open_wash_counter.java (Window/Screen): used to keep the wash cycle count
pop_input_kf.java (Window/Screen): Input screen where user enters the ARNo,etc., before the KF experiment starts
pop_input.java (Window/Screen): Input screen where user enters the ARNo,etc., before the Potentiometry experiment starts
popup_stirtime.java (Window/Screen): Used to display the stir time popup
ReformatBuffer.java: Buffer storage for incomming data from the device and proper execution of commands to the device is maintained
rename_metd_name.java (Window/Screen): Used to rename method name screen
report.java: used for generating Report PDFs'
save_method.java (Window/Screen): Used to save new method
select_comport.java (Window/Screen): Comport Selection screen
super_admin_creation.java (Window/Screen): Used to create Super Admin
SuperAdminLogin.java (Window/Screen): Used for creating SuperAdmin by the service Engineer(resetting of SuperAdmin Password cannot be done but passwor is lost then the client should contact back Mayura Analytical for Password or Mayura Analytical can create multiple SuperAdmins)
update_sop.java (Window/Screen): used to upload a new SOP (Standard Operating Procedure)
user_device_data.java (Window/Screen): fetches device data during installation
users_roles.java (Window/Screen): used to fetch user roles for editing


package main.java;

public class close_all {
	public static void close_windows() {
		try {
			if(AdminLogin.frame != null) {
				AdminLogin.frame.dispose();
			}
		}catch(NullPointerException np){}
		try {
			if(SuperAdminLogin.frame != null) {
				SuperAdminLogin.frame.dispose();
			}
		}catch(NullPointerException np){}
		try {
			if(open_pot_result.frame != null) {
				open_pot_result.frame.dispose();
			}
		}catch(NullPointerException np){}
		try {
			if(open_kf_result.frame != null) {
				open_kf_result.frame.dispose();
			}
		}catch(NullPointerException np){}
		try {
			if(open_method.frame != null) {
				open_method.frame.dispose();
			}
		}catch(NullPointerException np){}
		try {
			if(save_method.frame != null) {
				save_method.frame.dispose();
			}
		}catch(NullPointerException np){}
		try {
			if(select_comport.frame != null) {
				select_comport.frame.dispose();
			}
		}catch(NullPointerException np){}
		try {
			if(popup_input.frame != null) {
				popup_input.frame.dispose();
			}
		}catch(NullPointerException np){}
		try {
			if(popup_input_kf.frame != null) {
				popup_input_kf.frame.dispose();
			}
		}catch(NullPointerException np){}
		try {
			if(log_viewer.frame != null) {
				log_viewer.frame.dispose();
			}
		}catch(NullPointerException np){}
		try {
			if(open_report.frame != null) {
				open_report.frame.dispose();
			}
		}catch(NullPointerException np){}
		try {
			if(burette_calibration.frame != null) {
				burette_calibration.frame.dispose();
			}
		}catch(NullPointerException np){}
		try {
			if(burette_calibration_test.frame != null) {
				burette_calibration_test.frame.dispose();
			}
		}catch(NullPointerException np){}
		try {
			if(calibrate_electrode.frame != null) {
				calibrate_electrode.frame.dispose();
			}
		}catch(NullPointerException np){}
		try {
			if(device_data.frame != null) {
				device_data.frame.dispose();
			}
		}catch(NullPointerException np){}
		try {
			if(edit_roles.frame != null) {
				edit_roles.frame.dispose();
			}
		}catch(NullPointerException np){}
		try {
			if(edit_users.frame != null) {
				edit_users.frame.dispose();
			}
		}catch(NullPointerException np){}
		try {
			if(new_role.frame != null) {
				new_role.frame.dispose();
			}
		}catch(NullPointerException np){}
		try {
			if(new_user.frame != null) {
				new_user.frame.dispose();
			}
		}catch(NullPointerException np){}
		
		
	}
}

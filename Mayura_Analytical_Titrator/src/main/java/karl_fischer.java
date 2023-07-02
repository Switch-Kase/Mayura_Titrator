package main.java;

public class karl_fischer {
	
	String method_name;
	String created_by;
	String created_date;
	String updated_by;
	String updated_date;
	String delay;
	String stir_time;
	String max_vol;
	String blank_vol;
	String burette_factor;
	String density;
	String kf_factor;
	String end_point;
	String dosage_rate;
	String result_unit;
	String no_of_trials;
	String sop;
	
	public karl_fischer() {}
	
	@Override 
	public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null
            || this.getClass() != obj.getClass())
            return false;
 
        karl_fischer p1 = (karl_fischer)obj; 
        
        return  this.method_name.equals(p1.method_name) &&
        		this.created_by.equals(p1.created_by) &&
        		this.created_date.equals(p1.created_date) &&
        		this.updated_by.equals(p1.updated_by) &&
        		this.updated_date.equals(p1.updated_date) &&
        		this.delay.equals(p1.delay) &&
        		this.stir_time.equals(p1.stir_time) &&
        		this.max_vol.equals(p1.max_vol) &&
        		this.blank_vol.equals(p1.blank_vol) &&
        		this.burette_factor .equals(p1.burette_factor) &&
        		this.density .equals(p1.density) &&
        		this.kf_factor .equals(p1.kf_factor) &&
        		this.end_point .equals(p1.end_point) &&
        		this.dosage_rate .equals(p1.dosage_rate) &&
        		this.result_unit .equals(p1.result_unit) &&
        		this.no_of_trials .equals(p1.no_of_trials) &&
        		this.sop .equals(p1.sop);
    }
	
	public karl_fischer(String method_name, String created_by, String created_date, String updated_by, String updated_date, String delay, String stir_time, String max_vol, String blank_vol, String burette_factor, String density, String kf_factor, String end_point, String dosage_rate, String result_unit, String no_of_trials, String sop)
    {
		this.method_name=method_name;
		this.created_by=created_by;
		this.created_date= created_date;
		this.updated_by=updated_by;
		this.updated_date= updated_date;
		this.delay=delay;
		this.stir_time=stir_time;
		this.max_vol=max_vol;
		this.blank_vol=blank_vol;
		this.burette_factor = burette_factor;
		this.density = density;
		this.kf_factor = kf_factor;
		this.end_point = end_point;
		this.dosage_rate = dosage_rate;
		this.result_unit = result_unit;
		this.no_of_trials = no_of_trials;
		this.sop = sop;
    }
	
	public karl_fischer(karl_fischer kf)   {
		this.method_name=kf.method_name;
		this.created_by=kf.created_by;
		this.created_date=kf.created_date;
		this.updated_by=kf.updated_by;
		this.updated_date=kf.updated_date;
		this.delay=kf.delay;
		this.stir_time=kf.stir_time;
		this.max_vol=kf.max_vol;
		this.blank_vol=kf.blank_vol;
		this.burette_factor =kf.burette_factor;
		this.density =kf.density;
		this.kf_factor =kf.kf_factor;
		this.end_point =kf.end_point;
		this.dosage_rate =kf.dosage_rate;
		this.result_unit =kf.result_unit;
		this.no_of_trials =kf.no_of_trials;
		this.sop =kf.sop;
    }	
	
	public String getMethod_name() {
		return method_name;
	}

	public void setMethod_name(String method_name) {
		this.method_name = method_name;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

	public String getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(String updated_date) {
		this.updated_date = updated_date;
	}

	public String getDelay() {
		return delay;
	}

	public void setDelay(String delay) {
		this.delay = delay;
	}

	public String getStir_time() {
		return stir_time;
	}

	public void setStir_time(String stir_time) {
		this.stir_time = stir_time;
	}

	public String getMax_vol() {
		return max_vol;
	}

	public void setMax_vol(String max_vol) {
		this.max_vol = max_vol;
	}

	public String getBlank_vol() {
		return blank_vol;
	}

	public void setBlank_vol(String blank_vol) {
		this.blank_vol = blank_vol;
	}

	public String getBurette_factor() {
		return burette_factor;
	}

	public void setBurette_factor(String burette_factor) {
		this.burette_factor = burette_factor;
	}

	public String getDensity() {
		return density;
	}

	public void setDensity(String density) {
		this.density = density;
	}

	public String getKf_factor() {
		return kf_factor;
	}

	public void setKf_factor(String kf_factor) {
		this.kf_factor = kf_factor;
	}

	public String getEnd_point() {
		return end_point;
	}

	public void setEnd_point(String end_point) {
		this.end_point = end_point;
	}

	public String getDosage_rate() {
		return dosage_rate;
	}

	public void setDosage_rate(String dosage_rate) {
		this.dosage_rate = dosage_rate;
	}

	public String getResult_unit() {
		return result_unit;
	}

	public void setResult_unit(String result_unit) {
		this.result_unit = result_unit;
	}

	public String getNo_of_trials() {
		return no_of_trials;
	}

	public void setNo_of_trials(String no_of_trials) {
		this.no_of_trials = no_of_trials;
	}

	public String getSop() {
		return sop;
	}

	public void setSop(String sop) {
		this.sop = sop;
	}
}

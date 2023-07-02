package main.java;

public class potentiometry {
	String method_name;
	String created_by;
	String created_date;
	String updated_date;
	String updated_by;
	String pre_dose;
	String stir_time;
	String max_vol;
	String blank_vol;
	String burette_factor;
	String threshold;
	String filter;
	String dosage_rate;
	String no_of_trials;
	String factor1;
	String factor2;
	String factor3;
	String factor4;
	String ep_select;
	String formula_no;
	String result_unit;
	String sop;
	
	@Override 
	public boolean equals(Object obj)
    {
 
        // checking if the two objects
        // pointing to same object
        if (this == obj)
            return true;
 
        // checking for two condition:
        // 1) object is pointing to null
        // 2) if the objects belong to
        // same class or not
        if (obj == null || this.getClass() != obj.getClass())
            return false;
 
        potentiometry p1 = (potentiometry)obj; // type casting object to the
                           // intended class type
 
        // checking if the two
        // objects share all the same values
        return  this.method_name.equals(p1.method_name) &&
        		this.created_by.equals(p1.created_by) &&
        		this.created_date.equals(p1.created_date) &&
        		this.updated_date.equals(p1.updated_date) &&
        		this.updated_by.equals(p1.updated_by) &&
        		this.pre_dose.equals(p1.pre_dose) &&
        		this.stir_time.equals(p1.stir_time) &&
        		this.max_vol.equals(p1.max_vol) &&
        		this.blank_vol.equals(p1.blank_vol) &&
        		this.burette_factor.equals(p1.burette_factor) &&
        		this.threshold.equals(p1.threshold) &&
        		this.filter.equals(p1.filter) &&
        		this.dosage_rate.equals(p1.dosage_rate) &&
        		this.no_of_trials.equals(p1.no_of_trials) &&
        		this.factor1.equals(p1.factor1) &&
        		this.factor2.equals(p1.factor2) &&
        		this.factor3.equals(p1.factor3) &&
        		this.factor4.equals(p1.factor4) &&
        		this.ep_select.equals(p1.ep_select) &&
        		this.formula_no.equals(p1.formula_no) &&
        		this.result_unit.equals(p1.result_unit) &&
        		this.sop.equals(p1.sop);
    }
	
	public potentiometry() {}
	
	public potentiometry(String method_name,String created_by,String created_date,String updated_date,String updated_by,String pre_dose,String stir_time,String max_vol,String blank_vol,String burette_factor,String threshold,String filter,String dosage_rate,String no_of_trials,String factor1,String factor2,String factor3,String factor4,String ep_select,String formula_no,String result_unit,	String sop)
    {
		this.method_name = method_name;
		this.created_by = created_by;
		this.created_date = created_date;
		this.updated_date = updated_date;
		this.updated_by = updated_by;
		this.pre_dose = pre_dose;
		this.stir_time = stir_time;
		this.max_vol = 	max_vol;
		this.blank_vol = blank_vol;
		this.burette_factor = burette_factor;
		this.threshold = threshold;
		this.filter = filter;
		this.dosage_rate = dosage_rate;
		this.no_of_trials = no_of_trials;
		this.factor1 = factor1;
		this.factor2 = factor2;
		this.factor3 = factor3;
		this.factor4 = 	factor4;
		this.ep_select = ep_select;
		this.formula_no = formula_no;
		this.result_unit = result_unit;
		this.sop = sop;
    }
	
	public potentiometry(potentiometry pot)   {
		this.method_name = pot.method_name;
		this.created_by =  pot.created_by;
		this.created_date =  pot.created_date;
		this.updated_date =  pot.updated_date;
		this.updated_by =  pot.updated_by;
		this.pre_dose =  pot.pre_dose;
		this.stir_time =  pot.stir_time;
		this.max_vol = 	 pot.max_vol;
		this.blank_vol = pot.blank_vol;
		this.burette_factor =  pot.burette_factor;
		this.threshold =  pot.threshold;
		this.filter =  pot.filter;
		this.dosage_rate =  pot.dosage_rate;
		this.no_of_trials =  pot.no_of_trials;
		this.factor1 =  pot.factor1;
		this.factor2 =  pot.factor2;
		this.factor3 =  pot.factor3;
		this.factor4 = 	 pot.factor4;
		this.ep_select =  pot.ep_select;
		this.formula_no =  pot.formula_no;
		this.result_unit =  pot.result_unit;
		this.sop =  pot.sop;
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
	public String getUpdated_date() {
		return updated_date;
	}
	public void setUpdated_date(String updated_date) {
		this.updated_date = updated_date;
	}
	public String getUpdated_by() {
		return updated_by;
	}
	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}
	
	public String getPre_dose() {
		return pre_dose;
	}
	public void setPre_dose(String pre_dose) {
		this.pre_dose = pre_dose;
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
	public String getThreshold() {
		return threshold;
	}
	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getDosage_rate() {
		return dosage_rate;
	}
	public void setDosage_rate(String dosage_rate) {
		this.dosage_rate = dosage_rate;
	}
	public String getNo_of_trials() {
		return no_of_trials;
	}
	public void setNo_of_trials(String no_of_trials) {
		this.no_of_trials = no_of_trials;
	}
	public String getFactor1() {
		return factor1;
	}
	public void setFactor1(String factor1) {
		this.factor1 = factor1;
	}
	public String getFactor2() {
		return factor2;
	}
	public void setFactor2(String factor2) {
		this.factor2 = factor2;
	}
	public String getFactor3() {
		return factor3;
	}
	public void setFactor3(String factor3) {
		this.factor3 = factor3;
	}
	public String getFactor4() {
		return factor4;
	}
	public void setFactor4(String factor4) {
		this.factor4 = factor4;
	}
	public String getEp_select() {
		return ep_select;
	}
	public void setEp_select(String ep_select) {
		this.ep_select = ep_select;
	}
	public String getFormula_no() {
		return formula_no;
	}
	public void setFormula_no(String formula_no) {
		this.formula_no = formula_no;
	}
	public String getResult_unit() {
		return result_unit;
	}
	public void setResult_unit(String result_unit) {
		this.result_unit = result_unit;
	}
	public String getSop() {
		return sop;
	}
	public void setSop(String sop) {
		this.sop = sop;
	}
	
	
}

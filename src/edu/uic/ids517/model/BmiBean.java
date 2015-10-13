package edu.uic.ids517.model;

import java.io.Serializable;

public class BmiBean implements Serializable, Cloneable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String getReturnCode() {
		return returnCode;
	}
	//input variables
	private String bmiUser;
	private String units;
	public void setBmi(double bmi) {
		this.bmi = bmi;
	}
	public void setBmiPrime(double bmiPrime) {
		this.bmiPrime = bmiPrime;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	private String weight;
	private String height;
	//output variables
	private double bmi;
	private double bmiPrime;
	private String category;
	private String returnCode;
	public static double categoryLimit [] =
		{0.0, 15.0, 16.0, 18.5, 25.0, 30.0, 35.0, 40.0};
	public static String categoryDescription [] =
		{	"Error", 
			"Very severely underweight", 
			"Severely underweight", 
			"Underweight", 
			"Normal (healthy weight)", 
			"Overweight", 
			"Obese Class I (Moderately obese)",
			"Obese Class II (Severely obese)",
			"Obese Class III (Very severely obese)"};
	//messages
	private String errorMessage;
	private int errorCode;
	public BmiBean()
	{
		//consructor
		units="SI";
		category="";
		errorMessage="";
		weight="0.0"; height="0.0";
		
	}
	public BmiBean clone() throws CloneNotSupportedException {
		BmiBean cloned =(BmiBean) super.clone();
		return cloned;
	}
	
	public String computeBMI()
	{
		String status="fail";
		errorMessage = "";
	
		boolean isHeightNonZero = true;
		try{
			Double wt = Double.parseDouble(weight);
			Double ht = Double.parseDouble(height);
			
		if (ht<=0.0 || wt <=0.0)
		{
			isHeightNonZero = false;
			errorCode = 1;
			bmi=0.0; bmiPrime=0.0; category="";
		}
				
		if(units.equals("SI") && isHeightNonZero){
			//kg/m
			status="success";
				errorCode = 0;
				bmi = wt / (ht*ht);
				bmiPrime = bmi/25.0;
				bmi = Math.round(bmi *100.0)/100.0;
				bmiPrime=Math.round(bmiPrime * 100.0)/100.0;
				
				category = 
						categoryDescription[categoryDescription.length-1];
				
				for(int i = 0; i < categoryLimit.length; i++) {
					if(bmi <= categoryLimit[i]) {
						category = categoryDescription[i];
						break;
					}	// end if
				}	// end for
			
		}
		else if(units.equals("English/Imperial") && isHeightNonZero)
				{
			status = "success";
			//lb/in
			errorCode = 0;	
			bmi=703.0*wt/(ht*ht);
				bmiPrime=bmi/25.0;
				bmi=Math.round(bmi*100.0)/100.0;
				bmiPrime=Math.round(bmiPrime * 100.0)/100.0;
				
				category = 
						categoryDescription[categoryDescription.length-1];
				
				for(int i = 0; i < categoryLimit.length; i++) {
					if(bmi <= categoryLimit[i]) {
						category = categoryDescription[i];
						break;
					}	// end if
				}	// end for
				return status;
				
				
				}
				
				else {
			errorMessage = "Positive values for height and weight only";
					}
		
		
		return status;
		}
		catch (Exception e) {this.setErrorMessage("Only Numeric Values Allowed");
		errorCode = 1; bmi=0.0; bmiPrime=0.0; category=""; 

		return status;
		}
	}
	public double getBmi()
	{
		return bmi;
	}
	public double getBmiPrime()
	{
		return bmiPrime; 
	}
	public String getCategory()
	{
		return category;
	}
	public String getErrorMessage()
	{
		return errorMessage;
	}
	public int getErrorCode()
	{
		return errorCode;
	}
	public String getUnits()
	{
		return units;
	}
	public void setUnits(String units)
	{
		this.units=units;
	}
	
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}

	
	public String getHeight()
	{
		return height;
	}
	public void setHeight(String height)
	{
		this.height=height;
	}
	public String getBmiUser() {
		return bmiUser;
	}
	public void setBmiUser(String bmiUser) {
		this.bmiUser = bmiUser;
	}
}

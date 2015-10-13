package edu.uic.ids517.model;

import java.io.Serializable;

public class LoanBean implements Serializable, Cloneable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String getReturnCode() {
		return getReturnCode();  //CHECK
	}
	//input variables
	private String loanUser;
	private String purchaseprice;
	private String downPayment;
	private String interestRate;
	private String term;
	
	private double monthlyPayment;
	private double totalPayment;
	private double totalWithDownPayment;
	private double totalInterest;

	private String errorMessage;
	private int errorCode;
	
	public LoanBean()
	{
		//constructor
		purchaseprice="0.0"; downPayment="0.0"; interestRate="0.0"; term="0.0"; 
		errorMessage="";
	}
	
	public LoanBean clone() throws CloneNotSupportedException {
		LoanBean cloned =(LoanBean) super.clone();
		return cloned;
	}
	
	public String computeLoan()
	{
		String status="success";
		try {
			Double pp = Double.parseDouble(purchaseprice);
			Double dp = Double.parseDouble(downPayment);
			Double ir = Double.parseDouble(interestRate);
			Double t = Double.parseDouble(term);

		if (pp > 0 && dp > -1 && ir > 0 && t > 0) {
			status = "success";
			errorMessage =""; errorCode = 0;
			
		double monthlypaymentdecimal = ((pp-dp)*((ir/100)/12))/((1.0)-((1.0)/Math.pow(1.0+((ir/100)/12),(t*12))));
		monthlyPayment = Math.round(monthlypaymentdecimal*100.0)/100.0;
		double totalpaymentdecimal = (monthlypaymentdecimal * (t*12));
		totalPayment = Math.round(totalpaymentdecimal*100.0)/100.0;
		double totalpaymentwithdowndecimal = totalPayment + dp;
		totalWithDownPayment = Math.round(totalpaymentwithdowndecimal*100.0)/100.0;
		double totalinterestdecimal = totalPayment - (pp - dp);
		totalInterest = Math.round(totalinterestdecimal*100.0)/100.0; 
		}
		
		
		
		else if (pp <= 0 || dp < 0 || ir < 0 || t <= 0){
			errorMessage = "Positive Values Only";
			errorCode = 1;
		}
		
		
		return status;
		}
		catch (Exception e) {this.setErrorMessage("Only Numeric Values Allowed");
		errorCode = 1; 

		return status;
		}
		
	}

	public String getPurchaseprice() {
		return purchaseprice;
	} 

	public void setPurchaseprice(String purchaseprice) {
		this.purchaseprice = purchaseprice;
	}

	public String getDownPayment() {
		return downPayment;
	}

	public void setDownPayment(String downPayment) {
		this.downPayment = downPayment;
	}

	public String getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}
	
	 public double getMonthlyPayment() {
		return monthlyPayment;
	}

	public void setMonthlyPayment(double monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
	}

	public double getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(double totalPayment) {
		this.totalPayment = totalPayment;
	}

	public double getTotalWithDownPayment() {
		return totalWithDownPayment;
	}

	public void setTotalWithDownPayment(double totalWithDownPayment) {
		this.totalWithDownPayment = totalWithDownPayment;
	}

	public double getTotalInterest() {
		return totalInterest;
	}

	public void setTotalInterest(double totalInterest) {
		this.totalInterest = totalInterest;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getLoanUser() {
		return loanUser;
	}

	public void setLoanUser(String loanUser) {
		this.loanUser = loanUser;
	}
	
	
}
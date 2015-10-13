package edu.uic.ids517.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ActionBeanLoan {

	private DbaseAccessBean dbaseAccessBean;
	//private DbaseBean dbaseBean;
	private LoanBean loanBean;
	//maintains list of previous runs
	private List <LoanBean> loanBeanList;
	private boolean renderList;
	
	public ActionBeanLoan() {
			
		renderList = false;
		
		setLoanBeanList(new ArrayList <LoanBean> ());
	
	}

	public String computeLoan() 
		throws CloneNotSupportedException {
		String status = loanBean.computeLoan();
		LoanBean x = loanBean.clone();
		
		if(loanBean.getErrorCode()==0)
		{
			loanBeanList.add(x);
			renderList = true;
		}
		
		return status;
	}

	public String reset() {
		loanBeanList.clear();
		renderList = false;
		loanBean.setPurchaseprice("0.0");
		loanBean.setDownPayment("0.0");
		loanBean.setInterestRate("0.0");
		loanBean.setTerm("0.0");
		loanBean.setMonthlyPayment(0.0);
		loanBean.setTotalPayment(0.0);
		loanBean.setTotalWithDownPayment(0.0);
		loanBean.setTotalInterest(0.0);
		loanBean.setErrorMessage("");
		return "SUCCESS";
	}
	
	public LoanBean getLoanBean() {
		return loanBean;
	}

	public void setLoanBean(LoanBean loanBean) {
		this.loanBean = loanBean;
	}

	public List<LoanBean> getLoanBeanList() {
		return loanBeanList;
	}

	public void setLoanBeanList(List<LoanBean> loanBeanList) {
		this.loanBeanList = loanBeanList;
	}

	public boolean isRenderList() {
		return renderList;
	}

	public void setRenderList(boolean renderList) {
		this.renderList = renderList;
	}

	public DbaseAccessBean getDbaseAccessBean() {
		return dbaseAccessBean;
	}

	public void setDbaseAccessBean(DbaseAccessBean dbaseAccessBean) {
		this.dbaseAccessBean = dbaseAccessBean;
	}
	
	public void save() {
		Iterator<LoanBean> loanIterator = loanBeanList.iterator();
		while (loanIterator.hasNext()) {
			LoanBean loan = loanIterator.next();
				
			// save to database	
			String sqlQuery = "INSERT INTO loanTable (loanUser, purchasePrice, downPayment, interestRate, term, monthlyPayment, totalPayment, totalWithDownPayment, totalInterest) values ('" 
					+ loan.getLoanUser() + "', "+ loan.getPurchaseprice() + ", " + loan.getDownPayment() + ", " + loan.getInterestRate() + ", " + loan.getTerm() 
					+ ", " + loan.getMonthlyPayment() + ", " + loan.getTotalPayment() + ", " + loan.getTotalWithDownPayment() + ", " + loan.getTotalInterest() +")"; 
			dbaseAccessBean.setQueryType("INSERT");
			dbaseAccessBean.executeSQL(sqlQuery);	

		}
	}
	
}

		
		
		
		
		
		
		
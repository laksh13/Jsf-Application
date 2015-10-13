package edu.uic.ids517.model;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;

import org.apache.commons.lang.StringUtils;

import edu.uic.ids517.model.DbaseAccessBean;
import edu.uic.ids517.model.MessageBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped

public class ActionBeanDatabaseAccess {

	private DbaseAccessBean dbaseAccessBean;
	private MessageBean messageBean;

	public String createTables() {
		messageBean.clearMessage();
		
		dbaseAccessBean.createAllTables();
		
		/*
		messageBean.setMessage("");
		messageBean.setErrorMessage("");
		dbaseAccessBean.setQueryType("CREATE");
		dbaseAccessBean
				.executeSQL("CREATE TABLE bmiTable (id INT NOT NULL AUTO_INCREMENT, bmiUser VARCHAR(50) NULL, units VARCHAR(20) NULL, weight DOUBLE NULL, height DOUBLE NULL, bmi DOUBLE NULL, bmiPrime DOUBLE NULL, bmiCategory VARCHAR(100) NULL, PRIMARY KEY (id))");
		dbaseAccessBean
				.executeSQL("CREATE TABLE loanTable (id INT NOT NULL AUTO_INCREMENT, loanUser VARCHAR(50) NULL, purchasePrice DOUBLE NULL, downPayment DOUBLE NULL, interestRate DOUBLE NULL, term DOUBLE NULL, monthlyPayment DOUBLE NULL, totalPayment DOUBLE NULL, totalWithDownPayment DOUBLE NULL, totalInterest DOUBLE NULL, PRIMARY KEY (id))");
		*/
		
		return "SUCCESS";
	}

	public String dropTables() {
		messageBean.setMessage("");
		messageBean.setErrorMessage("");
		dbaseAccessBean.setQueryType("DROP");
		String sqlQuery = "DROP TABLE IF EXISTS bmiTable, loanTable";
		dbaseAccessBean.executeSQL(sqlQuery);		
		dbaseAccessBean.reset();
		dbaseAccessBean.setSqlQuery(sqlQuery);
		return "SUCCESS";
	}

	public String displayTables() {
		messageBean.setMessage("");
		messageBean.setErrorMessage("");
		dbaseAccessBean.setQueryType("SELECT");
		String sqlQuery = "SELECT * FROM " + dbaseAccessBean.getTableName();
		dbaseAccessBean.executeSQL(sqlQuery);
		dbaseAccessBean.generateResult();
		dbaseAccessBean.setSqlQuery(sqlQuery);
		return "SUCCESS";
	}

	/**
	 * @return the dbase
	 */
	public DbaseAccessBean getDbaseAccessBean() {
		return dbaseAccessBean;
	}

	/**
	 * @param dbase
	 *            the dbase to set
	 */
	public void setDbaseAccessBean(DbaseAccessBean dbase) {
		this.dbaseAccessBean = dbase;
	}

	/**
	 * @return the message
	 */
	public MessageBean getMessageBean() {
		return messageBean;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessageBean(MessageBean message) {
		this.messageBean = message;
	}
	
	public void listTables() {
		dbaseAccessBean.tableList();
	}

	public void listColumns() {		
		dbaseAccessBean.columnList(dbaseAccessBean.getTableName());		
	}
	
	public void updateListColumns(ValueChangeEvent event) {
		String tableName = event.getNewValue().toString();
		dbaseAccessBean.columnList(tableName);
		dbaseAccessBean.setTableName(tableName);
	}
	
	public void updateSelectedColumns(ValueChangeEvent event) {
		dbaseAccessBean.setSelectedColumnNames((ArrayList<String>) event.getNewValue());
		dbaseAccessBean.columnList(dbaseAccessBean.getTableName());
	}
	
	public void updateSqlQuery(ValueChangeEvent event) {
		String sqlQuery = event.getNewValue().toString();
		dbaseAccessBean.setSqlQuery(sqlQuery);
	}
	
	

	public void displaySelectedColumns() {
		messageBean.setMessage("");
		messageBean.setErrorMessage("");
		dbaseAccessBean.setQueryType("SELECT");	
		String tableName = dbaseAccessBean.getTableName();
		ArrayList <String> selectedColumnsList = dbaseAccessBean.getSelectedColumnNames();
		ArrayList <String> columnsList = dbaseAccessBean.getColumnNames();
		String sqlQuery = "";
		if (selectedColumnsList != null) {
			sqlQuery = "SELECT " + StringUtils.join(selectedColumnsList, ',') + " FROM " + tableName;			
			
		} else {
			sqlQuery = "SELECT * FROM " + tableName;			
		}		
		dbaseAccessBean.executeSQL(sqlQuery);
		dbaseAccessBean.generateResult();
		dbaseAccessBean.setSqlQuery(sqlQuery);
	}

	public void processSqlQuery() {
		messageBean.setMessage("");
		messageBean.setErrorMessage("");
		String sqlQuery = dbaseAccessBean.getSqlQuery();
		dbaseAccessBean.executeSQL(sqlQuery);
		dbaseAccessBean.generateResult();
		dbaseAccessBean.setSqlQuery(sqlQuery);
		
	}	

}

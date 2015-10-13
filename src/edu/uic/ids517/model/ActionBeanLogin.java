package edu.uic.ids517.model;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.context.FacesContext;

import edu.uic.ids517.model.MessageBean;
import edu.uic.ids517.model.MessageConstants;
import edu.uic.ids517.model.DbaseAccessBean;
import edu.uic.ids517.model.DbUserInfo;

public class ActionBeanLogin {

	private DbaseAccessBean dbaseAccessBean;
	private DbUserInfo dbUserInfo;
	private MessageBean messageBean;
	private String result = "fail";
	private String errorMessage = "";
	private String logoutMessage = "";

	public String getLogoutMessage() {
		return logoutMessage;
	}

	public void setLogoutMessage(String logoutMessage) {
		this.logoutMessage = logoutMessage;
	}

	public String processLogin() {

		boolean connectStatus = false;
		String result = "SUCCESS";
		messageBean.setIsMessage(true);

		try {
			connectStatus = dbaseAccessBean.connect();
		} catch (SQLException e) {
			connectStatus = false;
			errorMessage = "Failed to Login - check Username and/or Password";
			e.printStackTrace();
		}

		if (connectStatus) {
			messageBean.clearMessage();
			
			// createTables();
			dbaseAccessBean.createAllTables();

			// track login
			this.trackLogin();

			result = "SUCCESS";
		} else {
			errorMessage = "Failed to Login - check Username and/or Password";
			messageBean.setMessage(MessageConstants.LOGIN_FAIL);
			result = "Fail";
		}

		return result;

	}

	public String processLogout() throws SQLException {
		// get id from session
				FacesContext facesContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
				int id = (int) session.getAttribute("idUser");
		
		// update userTrackerTable data
		String sqlQuery = "UPDATE `userTrackerTable` SET `end_time`=now() WHERE `id`='" + id + "'";
		dbaseAccessBean.setQueryType("SELECT");
		dbaseAccessBean.executeSQL(sqlQuery);
		
		messageBean.setIsMessage(true);

		boolean connectStatus = false;
		connectStatus = dbaseAccessBean.closeConnect();

		FacesContext context = FacesContext.getCurrentInstance();

		context.getExternalContext().invalidateSession();
		messageBean.setMessage(MessageConstants.LOGOUT_SUCCESS);
		logoutMessage = "Logged Out";
		
		
	    return "/login.jsp?faces-redirect=true";
		
	    //return "SUCCESS";

	}

	public String processUserLogin() throws ClassNotFoundException {

		errorMessage = "Failed to Login - check Username and/or Password";
		messageBean.setIsMessage(true);
		boolean connectStatus = true;
		String result = null;

		if (connectStatus) {
			result = "SUCCESS";
		} else {
			errorMessage = "Failed to Login - check Username and/or Password";
			messageBean.setMessage(MessageConstants.LOGIN_FAIL);
			result = "Fail";
		}

		return result;

	}

	public DbaseAccessBean getDbaseBean() {
		return dbaseAccessBean;
	}

	public void setDbaseAccessBean(DbaseAccessBean dbaseAccessBean) {
		this.dbaseAccessBean = dbaseAccessBean;
	}

	public DbUserInfo getDbUserInfo() {
		return dbUserInfo;
	}

	public void setDbUserInfo(DbUserInfo dbUserInfo) {
		this.dbUserInfo = dbUserInfo;
	}

	public MessageBean getMessageBean() {
		return messageBean;
	}

	public void setMessageBean(MessageBean messageBean) {
		this.messageBean = messageBean;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	private void trackLogin() {
		// save to database
		String sqlQuery = "INSERT INTO userTrackerTable (user_name, start_time, end_time, ip_address) values ('"
				+ dbUserInfo.getUserName()
				+ "', "
				+ "now()"
				+ ", "
				+ "null"
				+ ", "
				+ "'" 
				+ this.getUserIpAddress()
				+ "'"
				+ ")";
		dbaseAccessBean.setQueryType("INSERT");
		dbaseAccessBean.executeSQL(sqlQuery);
		
		// get latest insert id to save in session
		String idSqlQuery = "SELECT MAX(id) as id FROM userTrackerTable";
		dbaseAccessBean.setQueryType("SELECT");
		dbaseAccessBean.executeSQL(idSqlQuery);
		
		int id = 0;
		try {
			ResultSet rs = dbaseAccessBean.getResultSet();
			rs.next();
			id = rs.getInt("id");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// save id to session to updated from logout user
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
		session.setAttribute("idUser", id);
		
	}
	
	private String getUserIpAddress() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
		    ipAddress = request.getRemoteAddr();
		}
		
		return ipAddress;
	}

	/*
	 * public String createTables() { messageBean.setMessage("");
	 * messageBean.setErrorMessage(""); dbaseAccessBean.setQueryType("CREATE");
	 * dbaseAccessBean .executeSQL(
	 * "CREATE TABLE bmiTable (id INT NOT NULL AUTO_INCREMENT, bmiUser VARCHAR(50) NULL, units VARCHAR(20) NULL, weight DOUBLE NULL, height DOUBLE NULL, bmi DOUBLE NULL, bmiPrime DOUBLE NULL, bmiCategory VARCHAR(100) NULL, PRIMARY KEY (id))"
	 * ); dbaseAccessBean .executeSQL(
	 * "CREATE TABLE loanTable (id INT NOT NULL AUTO_INCREMENT, loanUser VARCHAR(50) NULL, purchasePrice DOUBLE NULL, downPayment DOUBLE NULL, interestRate DOUBLE NULL, term DOUBLE NULL, monthlyPayment DOUBLE NULL, totalPayment DOUBLE NULL, totalWithDownPayment DOUBLE NULL, totalInterest DOUBLE NULL, PRIMARY KEY (id))"
	 * );
	 * 
	 * return "SUCCESS"; }
	 */

}

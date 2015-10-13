package edu.uic.ids517.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped


public class DbUserInfo {

	//enter the user and password you use to connect to MySql on your machine
	private String userName;
	private String password;
	private String dbms;
	private String dbmsHost;
	String databaseSchema;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDbms() {
		return dbms;
	}
	public void setDbms(String dbms) {
		this.dbms = dbms;
	}
	public String getDbmsHost() {
		return dbmsHost;
	}
	public void setDbmsHost(String dbmsHost) {
		this.dbmsHost = dbmsHost;
	}
	public String getDatabaseSchema() {
		return databaseSchema;
	}
	public void setDatabaseSchema(String databaseSchema) {
		this.databaseSchema = databaseSchema;
	}
	
	
	
}

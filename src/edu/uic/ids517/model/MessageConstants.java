package edu.uic.ids517.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped

public class MessageConstants {


		
	public static final String LOGIN_SUCCESS= "LOGIN SUCCESSFUL";
	public static final String  LOGIN_FAIL="FAILED TO LOGIN";
	public static final String REGISTRATION_SUCCESS= "REGISTRATION SUCCESSFUL";
	public static final String  REGISTRATION_FAIL="FAILED TO REGISTER";
	public static final String LOGOUT_SUCCESS= "LOGOUT SUCCESSFUL";
	public static final String  LOGOUT_FAIL="FAILED TO LOGOUT";
	public static final String  JDBCDriver="->  could not load JDBC Driver";

	
}


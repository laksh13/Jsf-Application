package edu.uic.ids517.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped

public class MessageBean {

	private String message;
	private String errorMessage;
	private String queryType;
	private Boolean isMessage;
	
	// to decide message will be showed up or not
	private Boolean showMessage;
	private Boolean showErrorMessage;
	
	public void clearMessage() {
		this.setErrorMessage("");
		this.setMessage("");
		this.setIsMessage(false);
		this.setShowErrorMessage(false);
		this.setShowMessage(false);
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/**
	 * @return the queryType
	 */
	public String getQueryType() {
		return queryType;
	}
	/**
	 * @param queryType the queryType to set
	 */
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	/**
	 * @return the isMessage
	 */
	public Boolean getIsMessage() {
		return isMessage;
	}
	/**
	 * @param isMessage the isMessage to set
	 */
	public void setIsMessage(Boolean isMessage) {
		this.isMessage = isMessage;
	}
	public Boolean getShowMessage() {
		return showMessage;
	}
	public void setShowMessage(Boolean showMessage) {
		this.showMessage = showMessage;
	}
	public Boolean getShowErrorMessage() {
		return showErrorMessage;
	}
	public void setShowErrorMessage(Boolean showErrorMessage) {
		this.showErrorMessage = showErrorMessage;
	}
	
	
	
}

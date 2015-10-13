<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">
H4 { background-color:#00FF00;}
body{ background-color:#FF9900;}
</style>
<title>Project 5 Group 10 Login</title>
</head>
<body>
<f:view>

<f:verbatim>
	<h4>IDS517 s15g110 P 05 Login Page</h4>
	
</f:verbatim>

<h:form> 
	<h:panelGrid columns="2"> 
		<h:outputText value="User Name:"/>
		<h:inputText id="userName" value="#{dbUserInfo.userName }" />
		<h:outputText value="Password:"/>
		<h:inputSecret id="password" value="#{dbUserInfo.password }" /> 
		<h:outputText value=""/>
		<h:outputText value=""/>
		<h:outputText value="DBMS Host:"/>   
		<h:selectOneListbox value="#{dbUserInfo.dbmsHost }" size="1">
			<f:selectItem itemValue ="localhost" />
			<f:selectItem itemValue ="131.193.209.57" itemLabel="server-57" />
			<f:selectItem itemValue ="131.193.209.54" itemLabel="server-54" />		
		</h:selectOneListbox>
		<h:outputText value=""/>
		<h:outputText value=""/>
		<h:outputText value="RDBMS:"/>
		<h:selectOneListbox value="#{dbUserInfo.dbms }" size="1">
			<f:selectItem itemValue="MySQL"/>
			<f:selectItem itemValue="DB2"/>
			<f:selectItem itemValue="Oracle"/>
		</h:selectOneListbox>
		<h:outputText value=""/>
		<h:outputText value=""/>
		<h:outputText value="database schema:" />
		<h:inputText id="databaseSchema" value="#{dbUserInfo.databaseSchema}" />
		<h:outputText value="" />
		<h:outputText value="" />
		<h:outputText value="#{messageBean.errorMessage}" />
		<h:outputText value="" />
		<h:outputText value="" />
		
		<h:commandButton type="submit" value="Login" action="#{actionBeanLogin.processLogin}"> </h:commandButton>
		
		<h:outputText value =""/>
 		<h:outputText id="errorMessage" value="#{actionBeanLogin.errorMessage}"/>		
 		</h:panelGrid>
</h:form>

</f:view>
</body>
</html>
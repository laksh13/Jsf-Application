<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">
H4 { background-color:#00FF00;}
body { background-color:#F8F8F8;}
</style>
<title>Project 5 Main Menu</title>
</head>
<body>
<f:view>
<f:verbatim>
	<h4>IDS517 s15g110 P 05 Main Menu</h4>
	
</f:verbatim>

<center>
<h:form>
			
				<a href="index.jsp">Home</a>&nbsp;&nbsp;&nbsp;
				<a href="fileImport.jsp">Data Analysis</a>&nbsp;&nbsp;&nbsp;
				<a href="databaseAccess.jsp">Database Access</a>&nbsp;&nbsp;&nbsp;
				<h:commandLink id="logout" value="Logout" action="#{actionBeanLogin.processLogout}" />
				
			<br />
			<hr />
			<br />
</h:form>
</center>

</f:view>
</body>
</html>
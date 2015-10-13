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
body background-color:#F8F8F8;
</style>
<title>Database Access</title>
</head>
<body>

<f:view> 

	<center>
<h:form>
			<h4>IDS517 s15g110 P 05 Main Menu</h4>
			<hr />
				<a href="index.jsp">Home</a>&nbsp;&nbsp;&nbsp;
				<a href="fileImport.jsp">Data Analysis</a>&nbsp;&nbsp;&nbsp;
				<a href="databaseAccess.jsp">Database Access</a>&nbsp;&nbsp;&nbsp;
				<h:commandLink id="logout" value="Logout" action="#{actionBeanLogin.processLogout}" />
				
			<br />
			<hr />
			<br />
</h:form>
</center>

	
		 <center>
			<h:form>
				<h:commandButton type="submit" value="Table List"
					action="#{actionBeanDatabaseAccess.listTables}" />
				<h:commandButton type="submit" value="Column List"
					action="#{actionBeanDatabaseAccess.listColumns}" />
				<h:commandButton type="submit" value="Display Tables"
					action="#{actionBeanDatabaseAccess.displayTables}" />
				<h:commandButton type="submit" value="Display Selected Column"
					action="#{actionBeanDatabaseAccess.displaySelectedColumns}" />
				<h:commandButton type="submit" value="Process SQL Query"
					action="#{actionBeanDatabaseAccess.processSqlQuery}" />	
				<h:commandButton type="submit" value="Create Tables"
					action="#{actionBeanDatabaseAccess.createTables}" />
				<h:commandButton type="submit" value="Drop Tables"
					action="#{actionBeanDatabaseAccess.dropTables}" />
				
					
			</h:form>
		</center>
		
		<center>
			<h:form>
				<h:selectOneListbox size="10" value="#{dbaseAccessBean.tableName}" onchange="submit()" 
				valueChangeListener="#{actionBeanDatabaseAccess.updateListColumns}">
					<f:selectItems value="#{dbaseAccessBean.tableListNames}"/>
					
				</h:selectOneListbox>
				
				<h:selectManyListbox size="10" value="#{dbaseAccessBean.selectedColumnNames}" onchange="submit()" 
				valueChangeListener="#{actionBeanDatabaseAccess.updateSelectedColumns}">
					<f:selectItems value="#{dbaseAccessBean.columnNames}"/>
				</h:selectManyListbox>
				
				<h:inputTextarea rows="10" cols="30" value="#{dbaseAccessBean.sqlQuery}" onblur="submit()" 
				valueChangeListener="#{actionBeanDatabaseAccess.updateSqlQuery}" />
 
			</h:form>
		</center>

		<br />
		<hr />
		<br />

		<h:outputText value="#{dbaseAccessBean.statusMessage}" />
		<h:outputText value="#{dbaseAccessBean.errorMessage}" />

		<h:panelGrid columns="2" rendered="#{dbaseAccessBean.renderResult}">
			<h:outputText value="SQL query:" />
			<h:outputText value="#{dbaseAccessBean.sqlQuery}" />
			<h:outputText value="Column names:" />
			<h:outputText value="#{dbaseAccessBean.columnNames}" />
			<h:outputText value="Number of columns:" />
			<h:outputText value="#{dbaseAccessBean.numberColumns}" />
			<h:outputText value="Number of rows:" />
			<h:outputText value="#{dbaseAccessBean.numberRows}" />
		</h:panelGrid>
		
		<div style="background-attachment: scroll; overflow: auto; height: 400px; background-repeat: repeat">

			<t:dataTable value="#{dbaseAccessBean.result}" var="row"
				rendered="#{dbaseAccessBean.renderResult}" border="1" cellspacing="0"
				cellpadding="1" columnClasses="columnClass1 border"
				headerClass="headerClass" footerClass="footerClass"
				rowClasses="rowClass2" styleClass="dataTableEx" width="900">

				<t:columns var="col" value="#{dbaseAccessBean.columnNames}">

					<f:facet name="header">
						<t:outputText styleClass="outputHeader" value="#{col}" />
					</f:facet>

					<t:outputText styleClass="outputText" value="#{row[col]}" />
				</t:columns>

			</t:dataTable>			
			

		</div>

		<f:verbatim>
			<br />
			<hr />
			<br />
		</f:verbatim>
			

</f:view>
	</body>
</html>
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
<title>JSF Loan Calculator</title>
</head>
<body>
<f:view>

<f:verbatim>
	<h3>ids517 s15g10 Loan Calculator</h3>
	<a href="index.jsp">Home</a>
</f:verbatim>

<h:form>
	<h:panelGrid columns="2">
	 
	 	<h:outputText value="Loan User"/>
		<h:inputText id="loanUser" value="#{loanBean.loanUser}"></h:inputText>
	 
		<h:outputText value="Purchase Price"/>
		<h:inputText id="purchaseprice" value="#{loanBean.purchaseprice}"></h:inputText>
		
		<h:outputText value="Down Payment"/>
		<h:inputText id="downPayment" value="#{loanBean.downPayment}"></h:inputText>
		
		<h:outputText value="Interest Rate"/>
		<h:inputText id="interestRate" value="#{loanBean.interestRate}"></h:inputText>
		
		<h:outputText value="Term in Years"/>
		<h:inputText id="term" value="#{loanBean.term}"></h:inputText>
		
		<h:outputText value=""/>
		<h:outputText value=""/>
		
		<h:commandButton type="submit" value="Calculate" action="#{actionBeanLoan.computeLoan }"/>
		<h:commandButton type="submit" value="Reset" action="#{actionBeanLoan.reset }"/>
		
		<h:outputText value=""/>
		<h:outputText value=""/>
		
		<h:outputText value="Monthly Payment"/>
		<h:outputText id="monthlyPayment" value="#{loanBean.monthlyPayment}" />
		
		<h:outputText value="Total Payment"/>
		<h:outputText id="totalPayment" value="#{loanBean.totalPayment}" />
		
		<h:outputText value="Total with Down Payment"/>
		<h:outputText id="totalWithDownPayment" value="#{loanBean.totalWithDownPayment}" />
		
		<h:outputText value="Total Interest"/>
		<h:outputText id="totalInterest" value="#{loanBean.totalInterest}" />
		
		<h:outputText value ="Error Message (if any)"/>
		<h:outputText id="errorMessage" value="#{loanBean.errorMessage}"/>
		
	</h:panelGrid>
</h:form>

<t:dataTable
		value="#{actionBeanLoan.loanBeanList }"
		var="rowNumber"
		rendered = "#{actionBeanLoan.renderList}"
		border = "1" cellspacing = "0" cellpadding="1"
		columnClasses="columnClass1 border"
		headerClass="headerClass"
		footerClass ="footerClass"
		rowClasses="rowClass2"
		styleClass="dataTableEx"
		width="800" >
		
		<h:column>
			<f:facet name="header">
				<h:outputText>Loan User</h:outputText>
			</f:facet>
			<h:outputText value="#{rowNumber.loanUser }"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText>Purchase Price</h:outputText>
			</f:facet>
			<h:outputText value="#{rowNumber.purchaseprice }"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText>Down Payment</h:outputText>
			</f:facet>
			<h:outputText value="#{rowNumber.downPayment }"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText>Interest Rate</h:outputText>
			</f:facet>
			<h:outputText value="#{rowNumber.interestRate }"/>
		</h:column>
				<h:column>
			<f:facet name="header">
				<h:outputText>Term</h:outputText>
			</f:facet>
			<h:outputText value="#{rowNumber.term }"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText>Monthly Payment</h:outputText>
			</f:facet>
			<h:outputText value="#{rowNumber.monthlyPayment }"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText>Total Payment</h:outputText>
			</f:facet>
			<h:outputText value="#{rowNumber.totalPayment }"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText>Total with Down Payment</h:outputText>
			</f:facet>
			<h:outputText value="#{rowNumber.totalWithDownPayment }"/>
		</h:column>
				<h:column>
			<f:facet name="header">
				<h:outputText>Total Interest</h:outputText>
			</f:facet>
			<h:outputText value="#{rowNumber.totalInterest }"/>
		</h:column>
		
		</t:dataTable>
		
		<h:form>
			<h:commandButton type="submit" value="Save"
					action="#{actionBeanLoan.save}" />
		</h:form>
		

</f:view>
</body>
</html>


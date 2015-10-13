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
<title>JSF BMI Calculator</title>
</head>
<body>
<f:view>  

<f:verbatim>
	<h3>ids517 s15g10 BMI Calculator (JSF)</h3>
	<a href="index.jsp">Home</a>
</f:verbatim>

<h:form>
	<h:panelGrid columns="2">
		<h:outputText value="Bmi User"/>
		<h:inputText id="bmiUser" value="#{bmiBean.bmiUser}"></h:inputText>
		
		<h:outputText value="Units"/>
		<h:selectOneListbox value="#{bmiBean.units}" size="2">
			<f:selectItem itemValue="English/Imperial"/>
			<f:selectItem itemValue="SI"/>
		</h:selectOneListbox>
		
		<h:outputText value="Weight"/>
		<h:inputText id="weight" value="#{bmiBean.weight}"></h:inputText>
		
		<h:outputText value="Height"/>
		<h:inputText id="height" value="#{bmiBean.height}"></h:inputText>
		
		<h:outputText value=""/>
		<h:outputText value=""/>
		
		<h:commandButton type="submit" value="Calculate" action="#{actionBeanBmi.computeBMI }"/>
		<h:commandButton type="submit" value="Reset" action="#{actionBeanBmi.reset }"/>
		
		<h:outputText value=""/>
		<h:outputText value=""/>
		
		<h:outputText value="BMI"/>
		<h:outputText id="bmi" value="#{bmiBean.bmi}" />
		
		<h:outputText value="BMI Prime"/>
		<h:outputText id="bmiPrime" value="#{bmiBean.bmiPrime}" />
		
		<h:outputText value="BMI Category"/>
		<h:outputText id="category" value="#{bmiBean.category}" />
		
		<h:outputText value ="Error Message (if any)"/>
		<h:outputText id="errorMessage" value="#{bmiBean.errorMessage}"/>
		
	</h:panelGrid>
</h:form>

	<t:dataTable
		
		value="#{actionBeanBmi.bmiBeanList }"
		var="rowNumber"
		rendered = "#{actionBeanBmi.renderList}"
		border = "1" cellspacing = "0" cellpadding="1"
		columnClasses="columnClass1 border"
		headerClass="headerClass"
		footerClass ="footerClass"
		rowClasses="rowClass2"
		styleClass="dataTableEx"
		width="800" >
	
		<h:column>
			<f:facet name="header">
				<h:outputText>User</h:outputText>
			</f:facet>
			<h:outputText value="#{rowNumber.bmiUser }"/>
		</h:column>
		
		<h:column>
			<f:facet name="header">
				<h:outputText>units</h:outputText>
			</f:facet>
			<h:outputText value="#{rowNumber.units }"/>
		</h:column>
		
				<h:column>
			<f:facet name="header">
				<h:outputText>weight</h:outputText>
			</f:facet>
			<h:outputText value="#{rowNumber.weight }"/>
		</h:column>
				<h:column>
			<f:facet name="header">
				<h:outputText>height</h:outputText>
			</f:facet>
			<h:outputText value="#{rowNumber.height }"/>
		</h:column>
				<h:column>
			<f:facet name="header">
				<h:outputText>BMI</h:outputText>
			</f:facet>
			<h:outputText value="#{rowNumber.bmi }"/>
		</h:column>
				<h:column>
			<f:facet name="header">
				<h:outputText>BMI Prime</h:outputText>
			</f:facet>
			<h:outputText value="#{rowNumber.bmiPrime }"/>
		</h:column>				
		<h:column>
			<f:facet name="header">
				<h:outputText>BMI Category</h:outputText>
			</f:facet>
			<h:outputText value="#{rowNumber.category }"/>
		</h:column>
	</t:dataTable>
	<h:form>
	<h:commandButton type="submit" value="Save"
					action="#{actionBeanBmi.save}" />
	</h:form>

</f:view>
</body>
</html>
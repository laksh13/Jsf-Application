<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">
H4 { background-color:#00FF00;}
body background-color:#F8F8F8;
</style>
<title>Data Upload and Analysis</title>
<script type="text/javascript">
	function displayTable() {
		document.getElementById('inputForm:display_table').click();
	}
</script>
<style>
.rowClass {width:100px}
</style>

</head>
<body>
	<f:view>

		<center>
<h:form>
			<h4>IDS517 s15g110 P 05 Data Analysis</h4>
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

		<br />
		<hr>

		<h:form>
			<h:outputLabel rendered="#{messageBean.showMessage}"
				value="Message: " />
			<h:outputText rendered="#{messageBean.showMessage}"
				value="#{messageBean.message}" />

			<h:outputLabel rendered="#{messageBean.showErrorMessage}"
				value="Error Message: " />
			<h:outputText rendered="#{messageBean.showErrorMessage}"
				value="#{messageBean.errorMessage}" />
		</h:form>

		<br />

		<h:form id="inputForm" enctype="multipart/form-data">

			<h:panelGrid columns="2"
				style="background-color:Beige;
						border-bottom-style:solid;
						border-top-style:solid;
						border-left-style:solid;
						border-right-style:solid">

				<h:outputLabel value="select file to upload:" for="fileUpload" />
				<t:inputFileUpload id="fileUpload" label="File to upload"
					storage="default" value="#{actionBeanFile.uploadedFile}" size="60" />

				<h:outputLabel value="filelabel:" for="fileLabel" />
				<h:inputText id="fileLabel" value="#{actionBeanFile.fileLabel}"
					size="60" />

				<h:outputLabel value="Upload" for="upload" />

				<h:commandButton id="upload" type="submit" value="Submit"
					action="#{actionBeanFile.processFileUpload}" />

				<!-- 
				<h:outputLabel value="Existing Table" for="display_table" />				
				<h:commandButton id="display_table" type="submit" value="Display Table"
					action="#{actionBeanFile.processShowData}" />		
				 -->

			</h:panelGrid>

			<h:outputLabel rendered="#{actionBeanFile.fileImport }"
				value="Number of records imported: " />
			<h:outputText rendered="#{actionBeanFile.fileImport }"
				value="#{actionBeanFile.numberRows }" />

		</h:form>

		<br />
		<h:form>
			<table border="0">
				<tr>
					<td>Dataset</td>
					<td>Column</td>
					<td>X Regression</td>
					<td>Y Regression</td>
					<td>Command</td>
				</tr>
				<tr>
					<td>
					<h:selectOneListbox id="datasetArrayList" size="10"
							value="#{dataBean.selectedDataset}" onchange="submit()"
							valueChangeListener="#{actionBeanAnalysis.updateListColumns}">
							<f:selectItems value="#{dataBean.datasetArrayList}" />
						</h:selectOneListbox>
					</td>
					
					<td>
					<h:selectManyListbox id="columnListNames" size="10"
					value="#{dataBean.selectedColumnNamesArrayList}" onchange="submit()"
					valueChangeListener="#{actionBeanAnalysis.updateSelectedColumns}">
					<f:selectItems value="#{dataBean.columnNamesArrayList}" />
					</h:selectManyListbox>
					</td>	
									
					<td>
					<h:selectOneListbox id="xRegression" size="10"
					value="#{dataBean.xRegression}" onchange="submit()"
					valueChangeListener="#{actionBeanAnalysis.updateXRegression}">
					<f:selectItems value="#{dataBean.selectedColumnNamesArrayList}" />
					</h:selectOneListbox>
					</td>
					
					<td>
					<h:selectOneListbox id="yRegression" size="10"
					value="#{dataBean.yRegression}" onchange="submit()"
					valueChangeListener="#{actionBeanAnalysis.updateYRegression}">
					<f:selectItems value="#{dataBean.selectedColumnNamesArrayList}" />
					</h:selectOneListbox>
					</td>
					
					<td>
									
					<h:commandButton id="getDataset" type="submit"
					value="Get Dataset" action="#{actionBeanAnalysis.getDataset}" />
					
					<h:commandButton id="displaySelectedColumn" type="submit"
					value="Display Selected Column" action="#{actionBeanAnalysis.displaySelectedColumns}" />
					
					<h:commandButton id="getStatistics" type="submit"
					value="Get Statistics" action="#{actionBeanAnalysis.getStatistics}" />
					
					<h:commandButton id="getRegression" type="submit"
					value="Get Regression" action="#{actionBeanAnalysis.getRegression}" />
					
					<h:commandButton id="downloadCsvFile" type="submit"
					value="Download CSV File"
					action="#{actionBeanFile.processFileDownload}" />
					
					<h:commandButton id="downloadXmlFile" type="submit"
					value="Download XML File"
					action="#{actionBeanFile.processFileDownloadXml}" />
					
					<h:commandButton id="deleteDataset" type="submit"
					value="Delete Dataset" action="#{actionBeanAnalysis.deleteDataset}" />
					
					</td>
				</tr>
			</table>
		</h:form>

		
		<h:form>
			<center></center>
		</h:form>

		<br />
		<hr />
		<br />
				
		<h:panelGrid columns="2" rendered="#{dbaseAccessBean.renderResult}">
			<h:outputText value="SQL query:" />
			<h:outputText value="#{dbaseAccessBean.sqlQuery}" />
			<h:outputText value="Column names:" />
			<h:outputText value="#{dataBean.selectedColumnNamesArrayList}" />
			<h:outputText value="Number of columns:" />
			<h:outputText value="#{dbaseAccessBean.numberColumns}" />
			<h:outputText value="Number of rows:" />
			<h:outputText value="#{dbaseAccessBean.numberRows}" />
		</h:panelGrid>

		<div
			style="background-attachment: scroll; overflow: auto; height: 400px; background-repeat: repeat">

			<t:dataTable
				rendered="#{dbaseAccessBean.renderResult}" border="1"
				cellspacing="0" cellpadding="1" columnClasses="columnClass1 border"
				headerClass="rowClass" footerClass="footerClass"
				rowClasses="rowClass" styleClass="dataTableEx" width="900">
				
			<t:columns var="realcol" value="#{dataBean.selectedColumnNamesArrayList}" >
					<f:facet name="header">
						<t:outputText styleClass="outputHeader" value="#{realcol}"/>
					</f:facet>
					
				</t:columns>
			</t:dataTable>

			<t:dataTable value="#{dbaseAccessBean.result}" var="row"
				rendered="#{dbaseAccessBean.renderResult}" border="1"
				cellspacing="0" cellpadding="1" columnClasses="columnClass1 border"
				headerClass="rowClass" footerClass="footerClass"
				rowClasses="rowClass" styleClass="dataTableEx" width="900">				
				
				<t:columns var="col" value="#{dbaseAccessBean.selectedColumnNames}">
					
						<t:outputText styleClass="outputHeader" value="#{row[col]}"/>
					
					
				</t:columns>

			</t:dataTable>


		</div>


		<f:verbatim>
			<br />
			<hr />
			<br />
		</f:verbatim>
		
		<h:panelGroup rendered="#{actionBeanAnalysis.renderStatistics}">
		
		<h1>Statistics Data</h1>
		<t:dataTable value="#{actionBeanAnalysis.dataStatisticsBeanList }"
			var="stats" rendered="#{actionBeanAnalysis.renderStatistics}" border="1"
			cellspacing="0" cellpadding="1" columnClasses="columnClass1 border"
			headerClass="headerClass" footerClass="footerClass"
			rowClasses="rowClass2" styleClass="dataTableEx" width="800">

			<h:column>
				<f:facet name="header">
					<h:outputText>Column</h:outputText>
				</f:facet>
				<h:outputText value="#{stats.columnNames }" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText>Min Value</h:outputText>
				</f:facet>
				<h:outputText value="#{stats.minValue }" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText>Max Value</h:outputText>
				</f:facet>
				<h:outputText value="#{stats.maxValue }" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText>Mean</h:outputText>
				</f:facet>
				<h:outputText value="#{stats.mean }" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText>Variance</h:outputText>
				</f:facet>
				<h:outputText value="#{stats.variance }" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText>STD</h:outputText>
				</f:facet>
				<h:outputText value="#{stats.std }" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText>Median</h:outputText>
				</f:facet>
				<h:outputText value="#{stats.median }" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText>Q1</h:outputText>
				</f:facet>
				<h:outputText value="#{stats.q1 }" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText>Q3</h:outputText>
				</f:facet>
				<h:outputText value="#{stats.q3 }" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText>IQR</h:outputText>
				</f:facet>
				<h:outputText value="#{stats.iqr }" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText>Range</h:outputText>
				</f:facet>
				<h:outputText value="#{stats.range }" />
			</h:column>

		</t:dataTable>
		</h:panelGroup>

		<h:panelGroup rendered="#{actionBeanAnalysis.renderChart}">
		<h1>Graphic Chart</h1>

		<t:dataTable value="#{actionBeanAnalysis.dataStatisticsBeanList }"
			var="stats" rendered="#{actionBeanAnalysis.renderChart}" border="1"
			cellspacing="0" cellpadding="1" columnClasses="columnClass1 border"
			headerClass="headerClass" footerClass="footerClass"
			rowClasses="rowClass2" styleClass="dataTableEx" width="800">

			<h:column>
				<f:facet name="header">
					<h:outputText>Column</h:outputText>
				</f:facet>
				<h:outputText value="#{stats.columnNames }" />
			</h:column>

			<h:column>
				<f:facet name="header">
					<h:outputText>Chart</h:outputText>
				</f:facet>
				<h:graphicImage value="#{stats.xySeriesChartFile}" height="450"
					width="600" rendered="#{actionBeanAnalysis.renderChart}"
					alt="xySeriesChart" />
			</h:column>

		</t:dataTable>

		<h2>Single Chart for All Selected Columns</h2>
		<h:graphicImage value="#{actionBeanAnalysis.xySeriesChartFile}"
			height="450" width="600" rendered="#{actionBeanAnalysis.renderChart}"
			alt="xySeriesChart" />
			
		</h:panelGroup>

		<h:panelGroup rendered="#{actionBeanAnalysis.renderRegression}">
		<h1>Regression Data</h1>

		<h:panelGrid columns="2"
			rendered="#{actionBeanAnalysis.renderRegression}">
			<h:outputText value="Error Message:" />
			<h:outputText value="#{messageBean.errorMessage}" />
			<h:outputText value="X Column Names:" />
			<h:outputText value="#{dataRegressionBean.xColumnNames}" />
			<h:outputText value="Y Column Names:" />
			<h:outputText value="#{dataRegressionBean.yColumnNames}" />
			<h:outputText value="Intercept:" />
			<h:outputText value="#{dataRegressionBean.intercept}" />
			<h:outputText value="Slope:" />
			<h:outputText value="#{dataRegressionBean.slope}" />
			<h:outputText value="RSquare:" />
			<h:outputText value="#{dataRegressionBean.rSquare}" />
			<h:outputText value="Significance:" />
			<h:outputText value="#{dataRegressionBean.significance}" />
			<h:outputText value="Intercept Std Err" />
			<h:outputText value="#{dataRegressionBean.interceptStdErr}" />
			<h:outputText value="Slope std Err:" />
			<h:outputText value="#{dataRegressionBean.slopeStdErr}" />
		</h:panelGrid>
		
		</h:panelGroup>

	</f:view>
</body>
</html>
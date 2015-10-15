package edu.uic.ids517.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.jsp.jstl.sql.Result;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

public class ActionBeanAnalysis {

	private MessageBean messageBean;
	private DbaseAccessBean dbaseAccessBean;
	private DataRegressionBean dataRegressionBean;
	private String xySeriesChartFile;
	private DataStatisticsBean dataStatisticsBean;
	private ArrayList<DataStatisticsBean> dataStatisticsBeanList;
	private DataBean dataBean;

	private boolean renderList;
	private boolean renderStatistics;
	private boolean renderChart;
	private boolean renderRegression;

	public ActionBeanAnalysis() {
		renderList = false;
		setDataStatisticsBean(new DataStatisticsBean());
		setDataStatisticsBeanList(new ArrayList<DataStatisticsBean>());

	}

	public void updateListColumns(ValueChangeEvent event) throws SQLException {
		
		String dataset;
		
		if(event.getNewValue() != null) {
			dataset = event.getNewValue().toString();
		} else {
			dataset = "";
		}
		
		String  tableName = "csvMetadataTable";	
		String columnNames = "";
		String sqlQuery = "SELECT `column_names` from " + tableName + " WHERE `table_name`='" + dataset + "'";
		
		dbaseAccessBean.executeSQL(sqlQuery);
		dbaseAccessBean.generateResult();
		
		ResultSet rs = dbaseAccessBean.getResultSet();
		
		rs.beforeFirst();
		while (rs.next()) {
			columnNames = rs.getString("column_names");
		}
		
		// split to become array string
		String[] arrColumnNamesTemp = columnNames.split(",");
		
		String[] arrColumnNames = new String[arrColumnNamesTemp.length-1];
		for(int j =0; j<arrColumnNamesTemp.length-1; j++)
		{
			arrColumnNames[j] = arrColumnNamesTemp[j+1];
		}

		ArrayList<String> columnNamesArrayList = new ArrayList<String>( Arrays.asList( arrColumnNames ) );
		
		dataBean.setColumnNamesArrayList(columnNamesArrayList);	

	}

	public void updateSelectedColumns(ValueChangeEvent event) {
		dataBean.setSelectedColumnNamesArrayList((ArrayList<String>) event.getNewValue());
		
	}
	
	public void updateXRegression(ValueChangeEvent event) {
		String xRegression = "";
		
		if(event.getNewValue() != null) {
			xRegression = event.getNewValue().toString();
		} else {
			xRegression = "";
		}
		
		dataBean.setxRegression(xRegression);
	}
	
	public void updateYRegression(ValueChangeEvent event) {
		String yRegression = "";
		
		if(event.getNewValue() != null) {
			yRegression = event.getNewValue().toString();
		} else {
			yRegression = "";
		}
		
		dataBean.setyRegression(yRegression);
	}
	

	public void getStatistics() throws CloneNotSupportedException {
		messageBean.clearMessage();
		clearRenderer();
		
		// check for selected dataset and columns
		if (! this.isDatasetAndColumnNamesSelected()) {
			return;
		}

		// emptying list
		dataStatisticsBeanList.clear();

		// perform sqlquery based on selected columns
		displaySelectedColumns();

		// get data from selected columns
		ArrayList<String> selectedColumnNames = dbaseAccessBean
				.getSelectedColumnNames();

		// get data from result query
		Result result = dbaseAccessBean.getResult();
		Object[][] columnsData = result.getRowsByIndex(); // sData[indexRow][indexColumn]
		int numRow = result.getRowCount();

		int columnIndex = 0;
		while (selectedColumnNames.size() > columnIndex) {

			// clone new object to add on dataStatisticsBeanList
			// DataStatisticsBean newDataStatistics =
			// dataStatisticsBean.clone(); // error null pointer

			// create new object
			DataStatisticsBean newDataStatisticsBean = new DataStatisticsBean();

			// set nama column
			String column = selectedColumnNames.get(columnIndex);
			// newDataStatisticsBean.setColumnNames(column);

			// set value from column
			double[] values = new double[numRow];
			for (int i = 0; i < numRow; i++) {
				values[i] = (double) columnsData[i][columnIndex];
			}

			// newDataStatisticsBean.setValues(values);

			// perform statistics operation to dataStatisticsBean values
			newDataStatisticsBean.doStatisticsOperation(dataBean.getAliasColumn( column ), values);

			// create graphics cart for every column
			newDataStatisticsBean.processGraphicQuery(dataBean.getAliasColumn( column ), values);

			// add to dataStatisticsBeanList
			dataStatisticsBeanList.add(newDataStatisticsBean);

			columnIndex++;
		}

		// create graphics chart for all selected columns
		this.processGraphicQuery();

		renderList = true;
		renderStatistics = true;
		renderChart = true;
		

	}

	public void processGraphicQuery() {
		messageBean.clearMessage();
		clearRenderer();

		// get data from selected columns
		// ArrayList<String> selectedColumnNames = dbaseAccessBean.getSelectedColumnNames();
		ArrayList<String> selectedColumnNames = dataBean.getSelectedColumnNamesArrayList();

		// get data from result query
		Result result = dbaseAccessBean.getResult();
		Object[][] columnsData = result.getRowsByIndex(); // sData[indexRow][indexColumn]
		int numRow = result.getRowCount();

		JFreeChart chart;

		FacesContext context = FacesContext.getCurrentInstance();

		String path = context.getExternalContext().getRealPath("/temp");

		File out = null;

		ByteArrayOutputStream s = new ByteArrayOutputStream();

		String pathWebContent = "/temp/";

		try {
			out = new File(path + "/all_selected_columns_chart.png");
			ChartBean chartBean = new ChartBean();
			chart = chartBean.createIndexTimeSeriesChart(
					"Grapics for all selected columns", selectedColumnNames,
					columnsData);
			ChartUtilities.saveChartAsPNG(out, chart, 600, 450);

			String fileLocation = pathWebContent + out.getName();
			setXySeriesChartFile(fileLocation);
			
			this.setRenderChart(true);

		} catch (Exception e) {
			e.printStackTrace();
			
			this.setRenderChart(false);
		}

	}

	public boolean getRegression() {	
		messageBean.clearMessage();
		clearRenderer();
		
		// check for selected dataset and columns
		if (! this.isDatasetAndXYRegressionSelected()) {
			return false;
		}
		
		boolean status = false;
		
		// perform sqlquery based on selected columns
		displaySelectedColumns();
		
		// get data from selected columns
		ArrayList<String> selectedColumnNames = dbaseAccessBean.getSelectedColumnNames();

		// get data from result query
		Result result = dbaseAccessBean.getResult();
		Object[][] columnsData = result.getRowsByIndex(); // sData[indexRow][indexColumn]
		int numRow = result.getRowCount();
		
		if ( selectedColumnNames.size() != 2 ) {
			messageBean.setErrorMessage("Please select two columns to perform regressions");
			messageBean.setShowErrorMessage(true);
			
			this.setRenderRegression(false);
			
			status = false;
			return status;
			
		}
		else {
			
			// build array values from columns data
			String xRegression = dataBean.getxRegression();
			String yRegression = dataBean.getyRegression();
			int xDataIndex = dataBean.getSelectedColumnNamesArrayList().indexOf(xRegression);//0;
			int yDataIndex = dataBean.getSelectedColumnNamesArrayList().indexOf(yRegression);//1;
			int rowIndex;
			
			// x data values
			rowIndex = 0;
			double[] xValues = new double[numRow];
			while (rowIndex < numRow) {
				xValues[rowIndex] =  (double) columnsData[rowIndex][xDataIndex];
						
			    rowIndex++;
			}
			
			String xColumn = selectedColumnNames.get(xDataIndex);
			
			// y data			
			rowIndex = 0;
			double[] yValues = new double[numRow];
			while (rowIndex < numRow) {
				yValues[rowIndex] =  (double) columnsData[rowIndex][yDataIndex];
						
			    rowIndex++;
			}
			
			String yColumn = selectedColumnNames.get(yDataIndex);
			
			// perform regression
			dataRegressionBean.performRegression(dataBean.getAliasColumn( xColumn ), xValues, dataBean.getAliasColumn( yColumn ), yValues);
			this.setRenderRegression(true);
			
			status = true;			
		}
		
		return status;
	}

	public void displaySelectedColumns() {
		messageBean.clearMessage();
		this.clearRenderer();
		
		// check for selected dataset and columns
		if (! this.isDatasetAndColumnNamesSelected()) {
			return;
		}
		
		dbaseAccessBean.setQueryType("SELECT");
		String tableName = "csvTable";
		dbaseAccessBean.setTableName(tableName);
		
		ArrayList<String> mSelectedColumnsList = dataBean.getCsvTableSelectedColumnNames();
		ArrayList<String> mColumnsList = dataBean.getCsvTableColumnNames();
		String selectedDataset = dataBean.getSelectedDataset();
		
		String sqlQuery = "";
		if (mSelectedColumnsList != null) {
			sqlQuery = "SELECT " + StringUtils.join(mSelectedColumnsList, ',')
					+ " FROM " + tableName 
					+ " WHERE `table_name`='" + selectedDataset + "'";

		} else {
			sqlQuery = "SELECT " + StringUtils.join(mColumnsList, ',')
					+ " FROM " + tableName
					+ " WHERE `table_name`='" + selectedDataset + "'";
		}
		
		dbaseAccessBean.executeSQL(sqlQuery);
		dbaseAccessBean.generateResult();
		dbaseAccessBean.setSqlQuery(sqlQuery);
		
		// set other properties of dbaseAccessBean for getStatistics operation
		//dbaseAccessBean.setColumnNames(mColumnsList);
		dbaseAccessBean.setSelectedColumnNames(mSelectedColumnsList);
		dbaseAccessBean.setTableName(tableName);
		
		// copy data result set dan result to dataBean
		dataBean.setResultSet(dbaseAccessBean.getResultSet());
		dataBean.setResult(dbaseAccessBean.getResult());
		dataBean.setRenderResult(true);
		
	}
	
	public void getDataset() throws SQLException {	
		
		String  tableName = "csvMetadataTable";
		
		String sqlQuery = "SELECT `table_name` from " + tableName + "";
		
		dbaseAccessBean.setQueryType("SELECT");
		dbaseAccessBean.executeSQL(sqlQuery);
		dbaseAccessBean.generateResult();
		
		// build dataset
		ResultSet rs = dbaseAccessBean.getResultSet();
		
		int numRows = rs.getRow();
		ArrayList<String> datasetArrayList = new ArrayList<String>(numRows);
		
		rs.beforeFirst();
		
		while (rs.next()) {
			datasetArrayList.add(rs.getString("table_name"));			
		}
		
		dataBean.setDatasetArrayList(datasetArrayList);	
		
	}
	
	public void deleteDataset() throws SQLException {	
		messageBean.clearMessage();
		
		// check dataset must be selected	
		if (! this.isDatasetSelected()) {
			return;
		}
		
		// get selected dataset
		String selectedDataset = dataBean.getSelectedDataset();
		
		// get table name
		String csvTable = "csvTable";
		String  csvMetadataTable = "csvMetadataTable";
		String sqlQuery = "";
		// delete all row in csvTable where dataset is selectedDataset
		sqlQuery = "DELETE from " + csvTable + " WHERE `table_name`='" + selectedDataset + "'";
		dbaseAccessBean.setQueryType("DELETE");
		dbaseAccessBean.executeSQL(sqlQuery);
		
		
		// delete all row in csvMetaDataTable where dataset is selectedDataset 
		sqlQuery = "DELETE from " + csvMetadataTable + " WHERE `table_name`='" + selectedDataset + "'";
		dbaseAccessBean.setQueryType("DELETE");
		dbaseAccessBean.executeSQL(sqlQuery);
		
		// set message for success delete dataset
		messageBean.setMessage(selectedDataset + " dataset deleted successfully.");
		
		// get new dataset	
		getDataset();		
			
	}
	
	public void clearRenderer() {
		this.setRenderChart(false);
		this.setRenderRegression(false);
		this.setRenderStatistics(false);
	}

	/*
	 * getter and setter
	 */

	public DataRegressionBean getDataRegressionBean() {
		return dataRegressionBean;
	}

	public void setDataRegressionBean(DataRegressionBean dataRegressionBean) {
		this.dataRegressionBean = dataRegressionBean;
	}

	public boolean isRenderRegression() {
		return renderRegression;
	}

	public void setRenderRegression(boolean renderRegression) {
		this.renderRegression = renderRegression;
	}

	public boolean isRenderChart() {
		return renderChart;
	}

	public void setRenderChart(boolean renderChart) {
		this.renderChart = renderChart;
	}

	public String getXySeriesChartFile() {
		return xySeriesChartFile;
	}

	public MessageBean getMessageBean() {
		return messageBean;
	}

	public void setMessageBean(MessageBean messageBean) {
		this.messageBean = messageBean;
	}

	public DbaseAccessBean getDbaseAccessBean() {
		return dbaseAccessBean;
	}

	public void setDbaseAccessBean(DbaseAccessBean dbaseAccessBean) {
		this.dbaseAccessBean = dbaseAccessBean;
	}

	public void setXySeriesChartFile(String xySeriesChartFile) {
		this.xySeriesChartFile = xySeriesChartFile;
	}

	public DataStatisticsBean getDataStatisticsBean() {
		return dataStatisticsBean;
	}

	public void setDataStatisticsBean(DataStatisticsBean dataStatisticsBean) {
		dataStatisticsBean = dataStatisticsBean;
	}

	public ArrayList<DataStatisticsBean> getDataStatisticsBeanList() {
		return dataStatisticsBeanList;
	}

	public void setDataStatisticsBeanList(
			ArrayList<DataStatisticsBean> arrayList) {
		this.dataStatisticsBeanList = arrayList;

	}

	public boolean isRenderList() {
		return renderList;
	}

	public void setRenderList(boolean renderList) {
		this.renderList = renderList;
	}

	public DataBean getDataBean() {
		return dataBean;
	}

	public void setDataBean(DataBean dataBean) {
		this.dataBean = dataBean;
	}

	public boolean isRenderStatistics() {
		return renderStatistics;
	}

	public void setRenderStatistics(boolean renderStatistics) {
		this.renderStatistics = renderStatistics;
	}
	
	private boolean isDatasetSelected() {
		boolean selected = true;
		
		if ( dataBean.getSelectedDataset().isEmpty() ) {
			
			messageBean.setErrorMessage("At least a dataset must be selected!");
			messageBean.setShowErrorMessage(true);
			
			selected = false;
		}
		
		return selected;
	}
	
	private boolean isDatasetAndColumnNamesSelected() {
		boolean selected = true;
		
		if ( dataBean.getCsvTableColumnNames().isEmpty() 
				|| dataBean.getCsvTableSelectedColumnNames().isEmpty() ) {
			
			messageBean.setErrorMessage("At least a dataset and a column must be selected!");
			messageBean.setShowErrorMessage(true);
			
			selected = false;
		}
		
		return selected;
	}
	
	private boolean isDatasetAndXYRegressionSelected() {
		boolean selected = true;
		
		if ( dataBean.getCsvTableColumnNames().isEmpty() 
				|| dataBean.getxRegression().isEmpty()
				|| dataBean.getyRegression().isEmpty()) {
			
			messageBean.setErrorMessage("At least a dataset, a X Regression, and Y Regression must be selected!");
			messageBean.setShowErrorMessage(true);
			
			selected = false;
		}
		
		return selected;
	}
	
	

}

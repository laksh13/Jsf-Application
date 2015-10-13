package edu.uic.ids517.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

@ManagedBean
@SessionScoped

public class DataBean {

	// important attribute
	private String selectedDataset;
	private ArrayList<String>datasetArrayList = new ArrayList<String>();
	private ArrayList<String>columnNamesArrayList = new ArrayList<String>();
	private ArrayList<String>selectedColumnNamesArrayList = new ArrayList<String>();
	private ArrayList<String>realColumnNames =  new ArrayList<String>( 
			Arrays.asList( "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8" ) );
	private String xRegression = "";
	private String yRegression = "";
	
	private ResultSet resultSet;
	private Result result;
	private boolean renderResult;
	
	private DbaseAccessBean dbaseAccessBean;
	
	public String getRealColumnFromMetadataColumn(String mColumnNames) {
		String realNames = "";
		
		int index = columnNamesArrayList.indexOf(mColumnNames);
		realNames = realColumnNames.get(index);
		
		return realNames;		
	}
	
	public String getAliasColumn(String rColumnNames) {
		String aliasNames = "";
		
		int index = realColumnNames.indexOf(rColumnNames);
		aliasNames = columnNamesArrayList.get(index);
		
		return aliasNames;		
	}
	
	public ArrayList<String> getCsvTableColumnNames() {
		ArrayList<String> rColumnNames = new ArrayList<String>( columnNamesArrayList.size() );
		
		for (int i=0; i<columnNamesArrayList.size(); i++) {
			rColumnNames.add( getRealColumnFromMetadataColumn ( columnNamesArrayList.get(i) ) );
		}
		
		return rColumnNames;
		
	}
	
	public ArrayList<String> getCsvTableSelectedColumnNames() {
		ArrayList<String> rSelectedColumnNames = new ArrayList<String>( selectedColumnNamesArrayList.size() );
		
		for (int i=0; i<selectedColumnNamesArrayList.size(); i++) {
			rSelectedColumnNames.add( getRealColumnFromMetadataColumn ( selectedColumnNamesArrayList.get(i) ) );
		}
		
		return rSelectedColumnNames;
		
	}
	
	public ArrayList<String> getRealColumnNames() {
		return realColumnNames;
	}
	public void setRealColumnNames(ArrayList<String> realColumnNames) {
		this.realColumnNames = realColumnNames;
	}
	public String getSelectedDataset() {
		return selectedDataset;
	}
	public void setSelectedDataset(String selectedDataset) {
		this.selectedDataset = selectedDataset;
	}
	public ArrayList<String> getDatasetArrayList() {
		return datasetArrayList;
	}
	public void setDatasetArrayList(ArrayList<String> datasetArrayList) {
		this.datasetArrayList = datasetArrayList;
	}
	public ArrayList<String> getColumnNamesArrayList() {
		return columnNamesArrayList;
	}
	public void setColumnNamesArrayList(ArrayList<String> columnNamesArrayList) {
		this.columnNamesArrayList = columnNamesArrayList;
	}
	public ArrayList<String> getSelectedColumnNamesArrayList() {
		return selectedColumnNamesArrayList;
	}
	public void setSelectedColumnNamesArrayList(
			ArrayList<String> selectedColumnNamesArrayList) {
		this.selectedColumnNamesArrayList = selectedColumnNamesArrayList;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public boolean isRenderResult() {
		return renderResult;
	}

	public void setRenderResult(boolean renderResult) {
		this.renderResult = renderResult;
	}

	public DbaseAccessBean getDbaseAccessBean() {
		return dbaseAccessBean;
	}

	public void setDbaseAccessBean(DbaseAccessBean dbaseAccessBean) {
		this.dbaseAccessBean = dbaseAccessBean;
	}

	public String getxRegression() {
		return xRegression;
	}

	public void setxRegression(String xRegression) {
		this.xRegression = xRegression;
	}

	public String getyRegression() {
		return yRegression;
	}

	public void setyRegression(String yRegression) {
		this.yRegression = yRegression;
	}

	
	
}

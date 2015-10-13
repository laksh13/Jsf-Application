package edu.uic.ids517.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.FacesContext;

import org.apache.commons.math3.stat.StatUtils;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

public class DataStatisticsBean implements Serializable, Cloneable {
	
	private ChartBean chartBean;

	// Note: StatsUtils static class - could use alternate
	
	private String columnNames;
	private double values[]; // populate from db table
	// populate java List with results to display in table
	private double minValue;
	private double maxValue;
	private double mean;
	private double variance;
	private double std;
	private double median;
	private double q1;
	private double q3;
	private double iqr;
	private double range;
	
	private String xySeriesChartFile; 
	
	public String getXySeriesChartFile() {
		return xySeriesChartFile;
	}

	public void setXySeriesChartFile(String xySeriesChartFile) {
		this.xySeriesChartFile = xySeriesChartFile;
	}

	public void doStatisticsOperation(String columnNames, double[] values) {
		setColumnNames(columnNames);
		setValues(values);
		
		// perform statistics
		double minValue = StatUtils.min(values);
		double maxValue = StatUtils.max(values);
		double mean = StatUtils.mean(values);
		double variance = StatUtils.variance(values, mean);
		double std = Math.sqrt(variance);
		double median = StatUtils.percentile(values, 50.0);
		double q1 = StatUtils.percentile(values, 25.0);
		double q3 = StatUtils.percentile(values, 75.0);
		double iqr = q3 - q1;
		double range = maxValue - minValue;
		
		setMinValue(minValue);
		setMaxValue(maxValue);
		setMean(mean);
		setVariance(variance);
		setStd(std);
		setMedian(median);
		setQ1(q1);
		setQ3(q3);
		setIqr(iqr);
		setRange(range);
	}
	
	public void processGraphicQuery(String columnNames, double[] values) {
		setColumnNames(columnNames);
		setValues(values);
		
		String [] tDate = new String [values.length];
		
		JFreeChart chart;
		
		FacesContext context = FacesContext.getCurrentInstance();
		
		String path = context.getExternalContext().getRealPath("/temp");
		
		File out = null;
		
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		
		String pathWebContent = "/temp/";
		
		
		try {
			out = new File(path+"/"+ columnNames +"_"+"chart"+".png");
			ChartBean chartBean = new ChartBean();
			chart = chartBean.createIndexTimeSeriesChart("Grapics for " + getColumnNames(), getColumnNames(), getValues() );
			ChartUtilities.saveChartAsPNG(out, chart, 600, 450);
			
			String fileLocation = pathWebContent + out.getName();
			setXySeriesChartFile(fileLocation);
			
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
	
		
	}

	public ChartBean getChartBean() {
		return chartBean;
	}

	public void setChartBean(ChartBean chartBean) {
		this.chartBean = chartBean;
	}
	
	
	public String getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String columnNames) {
		this.columnNames = columnNames;
	}

	public double[] getValues() {
		return values;
	}

	public void setValues(double[] values) {
		this.values = values;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getVariance() {
		return variance;
	}

	public void setVariance(double variance) {
		this.variance = variance;
	}

	public double getStd() {
		return std;
	}

	public void setStd(double std) {
		this.std = std;
	}

	public double getMedian() {
		return median;
	}

	public void setMedian(double median) {
		this.median = median;
	}

	public double getQ1() {
		return q1;
	}

	public void setQ1(double q1) {
		this.q1 = q1;
	}

	public double getQ3() {
		return q3;
	}

	public void setQ3(double q3) {
		this.q3 = q3;
	}

	public double getIqr() {
		return iqr;
	}

	public void setIqr(double iqr) {
		this.iqr = iqr;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}
	
	public DataStatisticsBean clone() throws CloneNotSupportedException {
		
		DataStatisticsBean cloned = null;
		try {		
			cloned = (DataStatisticsBean) super.clone();
		
		} catch (CloneNotSupportedException e) {		
		}		
		return cloned;

	}

}

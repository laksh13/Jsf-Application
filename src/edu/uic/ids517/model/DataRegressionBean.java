package edu.uic.ids517.model;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;

public class DataRegressionBean {

	private String xColumnNames;
	private double xValues[]; 
	
	private String yColumnNames;
	private double yValues[]; 
	
	private double intercept;
	private double slope;
	private double rSquare;
	private double significance;
	private double interceptStdErr;
	private double slopeStdErr;
	
	public void performRegression(String xColumnNames, double[] xValues, String yColumnNames, double[] yValues) {
		this.setxColumnNames(xColumnNames);
		this.setxValues(xValues);
		this.setyColumnNames(yColumnNames);
		this.setyValues(yValues);
		
		PearsonsCorrelation c = new PearsonsCorrelation();
		SimpleRegression sr = new SimpleRegression();
		// Note: x aand y arrays populated with data
		// now add observations to regression class instance
		sr.clear();
		for(int k=0; k<xValues.length; k++) {
			sr.addData(xValues[k], yValues[k]);
		}
		
		intercept = sr.getIntercept();
		slope = sr.getSlope();
		rSquare =  sr.getRSquare();
		significance = sr.getSignificance();
		interceptStdErr = sr.getInterceptStdErr();
		slopeStdErr = sr.getSlopeStdErr();
	}
	
	public String getxColumnNames() {
		return xColumnNames;
	}

	public void setxColumnNames(String xColumnNames) {
		this.xColumnNames = xColumnNames;
	}

	public double[] getxValues() {
		return xValues;
	}

	public void setxValues(double[] xValues) {
		this.xValues = xValues;
	}

	public String getyColumnNames() {
		return yColumnNames;
	}

	public void setyColumnNames(String yColumnNames) {
		this.yColumnNames = yColumnNames;
	}

	public double[] getyValues() {
		return yValues;
	}

	public void setyValues(double[] yValues) {
		this.yValues = yValues;
	}

	public double getIntercept() {
		return intercept;
	}

	public void setIntercept(double intercept) {
		this.intercept = intercept;
	}

	public double getSlope() {
		return slope;
	}

	public void setSlope(double slope) {
		this.slope = slope;
	}

	public double getrSquare() {
		return rSquare;
	}

	public void setrSquare(double rSquare) {
		this.rSquare = rSquare;
	}

	public double getSignificance() {
		return significance;
	}

	public void setSignificance(double significance) {
		this.significance = significance;
	}

	public double getInterceptStdErr() {
		return interceptStdErr;
	}

	public void setInterceptStdErr(double interceptStdErr) {
		this.interceptStdErr = interceptStdErr;
	}

	public double getSlopeStdErr() {
		return slopeStdErr;
	}

	public void setSlopeStdErr(double slopeStdErr) {
		this.slopeStdErr = slopeStdErr;
	}

	
}

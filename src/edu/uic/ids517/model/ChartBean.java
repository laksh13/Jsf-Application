package edu.uic.ids517.model;

import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ChartBean {
	

	public static JFreeChart createIndexTimeSeriesChart(String subject , String label, double[] values) {
		
		XYDataset dataset = createSingleSeriesDataset(label, values);
		
		JFreeChart chart = ChartFactory.createXYLineChart( subject, "Data Sequence", "Values",
	            dataset,
	            PlotOrientation.VERTICAL,
	            true,
	            false,
	            false
	        );
		
	        XYPlot plot = (XYPlot) chart.getPlot();
	        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	        renderer.setSeriesLinesVisible(0, true);
	        renderer.setSeriesShapesVisible(0, false);
	        renderer.setSeriesLinesVisible(1, false);
	        renderer.setSeriesShapesVisible(1, true);        
	        plot.setRenderer(renderer);
	        //final ChartPanel chartPanel = new ChartPanel(chart);
	        //chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));
	        //setContentPane(chartPanel);
		
		return chart;
	}
	
	public static JFreeChart createIndexTimeSeriesChart(String subject , ArrayList <String> columnNames, Object[][] columnsData) {
		
		// create dataset for all selected columns and their values
		XYDataset dataset = createSeriesDataset(columnNames, columnsData);
		
		JFreeChart chart = ChartFactory.createXYLineChart( subject, "Data Sequence", "Values",
	            dataset,
	            PlotOrientation.VERTICAL,
	            true,
	            false,
	            false
	        );
		
	        XYPlot plot = (XYPlot) chart.getPlot();
	        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	        renderer.setSeriesLinesVisible(0, true);
	        renderer.setSeriesShapesVisible(0, false);
	        renderer.setSeriesLinesVisible(1, false);
	        renderer.setSeriesShapesVisible(1, true);        
	        plot.setRenderer(renderer);
	        //final ChartPanel chartPanel = new ChartPanel(chart);
	        //chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));
	        //setContentPane(chartPanel);
		
		return chart;
	}
	
	/**
    * Creates a sample dataset.
    * 
    * @return A dataset.
    */
   private static XYDataset createSingleSeriesDataset(String columnNames, double[] values) {	   
	   
       XYSeries series = new XYSeries(columnNames);
       
       int numValues = values.length;
       
       for (int i = 0; i < numValues; i++) {
    	   series.add(i, values[i]);
       }
       
       XYSeriesCollection dataset = new XYSeriesCollection();
       dataset.addSeries(series);
       
       return dataset;
   }
   
   private static XYDataset createSeriesDataset(ArrayList <String> columnNames, Object[][] columnsData) {	 
	   // columnsData[indexRow][indexColumn]	   
	   XYSeriesCollection dataset = new XYSeriesCollection();
	   
	   int columnIndex = 0;
	   int numRow = columnsData.length;
	   
	   while ( columnNames.size() > columnIndex ) {
		   XYSeries series = new XYSeries(columnNames.get(columnIndex));
		   
		   for (int rowIndex = 0; rowIndex < numRow; rowIndex++) {
			   // add data values to chart
			   series.add(rowIndex,  (double) columnsData[rowIndex][columnIndex] );
			   
		   }
		   
		   // add series to dataset
		   dataset.addSeries(series);
		   
		   columnIndex++;
	   }	   
	  
       return dataset;	   
	   
   }

	
}

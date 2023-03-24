import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.io.*;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.ChartFrame;


public class Visualization {

    private double[][] data; // the input data
    private int[] clusterAssignments; // the cluster assignments for each data point
    private int numClusters; // the number of clusters

    public Visualization(double[][] data, int[] clusterAssignments, int numClusters) {
        this.data = data;
        this.clusterAssignments = clusterAssignments;
        this.numClusters = numClusters;
    }

    private XYDataset createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (int i = 0; i < numClusters; i++) {
            XYSeries series = new XYSeries("Cluster " + (i + 1));
            for (int j = 0; j < data.length; j++) {
                if (clusterAssignments[j] == i) {
                    series.add(data[j][0], data[j][1]);
                }
            }
            dataset.addSeries(series);
        }
        return dataset;
    }


    public void createChart(String chart_name) throws Exception{

        XYDataset dataset = createDataset();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(false, true);
        for (int i = 0; i < numClusters; i++) {
            renderer.setSeriesPaint(i, getRandomColor());
        }
        
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Cluster Visualization",
                "X", "Y", dataset,
                PlotOrientation.VERTICAL, true, true, false);
        XYPlot plot = chart.getXYPlot();
        plot.setRenderer(renderer);
        
        ChartFrame frame = new ChartFrame("Data Clustering Result", chart);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 500));

        try {

            OutputStream out = new FileOutputStream(chart_name);
            ChartUtilities.writeChartAsPNG(out,
                    chart,
                    500,
                    500);

        } catch (IOException ex) {
            System.out.println("Unable to save the file");
        }
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
            

        
    }

    private Color getRandomColor() {
        float r = (float) Math.random();
        float g = (float) Math.random();
        float b = (float) Math.random();
        return new Color(r, g, b);
    }

}

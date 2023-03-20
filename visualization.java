
import java.awt.Color;
import java.util.List;
import java.util.Random;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.io.*;
import org.jfree.chart.ChartUtilities;

public class Visualization {

    public void visualize(double[][] documents, int[] clusterAssignments) throws Exception{
        // reduce dimensionality using PCA
        // double[][] pcaData = PCA.reduceDimensionality(documents);

        // create chart dataset
        XYDataset dataset = createDataset(documents, clusterAssignments);

        // create chart
        JFreeChart chart = ChartFactory.createScatterPlot("Document Clustering", "PCA Component 1",
                "PCA Component 2", dataset, PlotOrientation.VERTICAL, true, true, false);

        // customize chart
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.BLACK);

        // customize scatter plot renderer
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(false, true);
        for (int i = 0; i < clusterAssignments.length; i++) {
            renderer.setSeriesPaint(i, getClusterColor(clusterAssignments[i]));
            // renderer.setSeriesShapesVisible(i, true);
        }
        plot.setRenderer(renderer);

        // display chart
        ChartFrame frame = new ChartFrame("Data Clustering Result", chart);
        frame.pack();
        frame.setVisible(true);
        int width = 640;   /* Width of the image */
        int height = 480;  /* Height of the image */ 
        File cluster_chart = new File( "Chart.jpeg" ); 
        ChartUtilities.saveChartAsJPEG( cluster_chart , chart , width , height );
    }

    // private XYDataset createDataset(double[][] data, int[] clusterAssignments) {
    //     XYSeriesCollection dataset = new XYSeriesCollection();

    //     // add data points to series
    //     for (int i = 0; i < data.length; i++) {
    //         XYSeries series = new XYSeries(i);
    //         series.add(data[i][0], data[i][1]);
    //         dataset.addSeries(series);
    //     }

    //     return dataset;
    // }
    private XYDataset createDataset(double[][] dataPoints, int[] clusterIds) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        // System.out.print(clusterIds);
        for (int i = 0; i < clusterIds.length; i++) {
            // System.out.print(i);
            int clusterId = clusterIds[i];
            double[] dataPoint = dataPoints[i];

            
            // System.out.print("Thissssss");
            try {
              //  Block of code to try
                XYSeries series = dataset.getSeries(clusterId);
                series.add(dataPoint[0], dataPoint[1]);
            }
            catch(Exception e) {
              //  Block of code to handle errors
                XYSeries series = new XYSeries("Cluster " + clusterId);
                dataset.addSeries(series);
                series.add(dataPoint[0], dataPoint[1]);

            }
                
            
        }
        return dataset;
    }

    private Color getClusterColor(int clusterAssignment) {
        switch (clusterAssignment) {
            case 0:
                return Color.RED;
            case 1:
                return Color.BLUE;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.ORANGE;
            case 4:
                return Color.MAGENTA;
            default:
                return Color.BLACK;
        }
    }
}
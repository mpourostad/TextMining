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

    // Method to visualize the clustering result
    public void visualize(List<double[]> dataPoints, List<Integer> clusterIds) throws Exception {
        // Generate random colors for each cluster
        Color[] colors = generateColors(clusterIds.size());

        // Create a dataset containing data points and their corresponding cluster ids
        XYDataset dataset = createDataset(dataPoints, clusterIds);

        // Create a scatter plot with data points colored according to their clusters
        JFreeChart chart = ChartFactory.createScatterPlot("Data Clustering Result", "X", "Y", dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        for (int i = 0; i < clusterIds.size(); i++) {
            renderer.setSeriesPaint(i, colors[clusterIds.get(i)]);
        }
        plot.setRenderer(renderer);

        // Display the plot in a frame
        ChartFrame frame = new ChartFrame("Data Clustering Result", chart);
        frame.pack();
        frame.setVisible(true);
        int width = 640;   /* Width of the image */
        int height = 480;  /* Height of the image */ 
        File pieChart = new File( "Chart.jpeg" ); 
        ChartUtilities.saveChartAsJPEG( pieChart , chart , width , height );
    }

    // Method to generate random colors
    private Color[] generateColors(int numColors) {
        Color[] colors = new Color[numColors];
        Random rand = new Random();
        for (int i = 0; i < numColors; i++) {
            colors[i] = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        }
        return colors;
    }

    // Method to create a dataset containing data points and their corresponding cluster ids
    // private XYDataset createDataset(List<double[]> dataPoints, List<Integer> clusterIds) {
    //     XYSeriesCollection dataset = new XYSeriesCollection();
    //     // System.out.print(clusterIds);
    //     for (int i = 0; i < clusterIds.size(); i++) {
    //         // System.out.print(i);
    //         int clusterId = clusterIds.get(i);
    //         double[] dataPoint = dataPoints.get(i);
    //         XYSeries series = dataset.getSeries(clusterId);
    //         System.out.print("Thissssss");
    //         if (series == null) {
    //             series = new XYSeries("Cluster " + clusterId);
    //             dataset.addSeries(series);
    //         }
    //         series.add(dataPoint[0], dataPoint[1]);
    //     }
    //     return dataset;
    // }
    private XYDataset createDataset(List<double[]> dataPoints, List<Integer> clusterIds) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        // System.out.print(clusterIds);
        for (int i = 0; i < clusterIds.size(); i++) {
            // System.out.print(i);
            int clusterId = clusterIds.get(i);
            double[] dataPoint = dataPoints.get(i);

            
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
}

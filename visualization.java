import java.awt.Color;
import java.awt.Dimension;
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

public class ClusterVisualizer {

    public void visualize(List<Document> documents, int[] clusterAssignments) {
        // reduce dimensionality using PCA
        double[][] pcaData = PCA.reduceDimensionality(documents);

        // create chart dataset
        XYDataset dataset = createDataset(pcaData, clusterAssignments);

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
            renderer.setSeriesShapesVisible(i, true);
        }
        plot.setRenderer(renderer);

        // display chart
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 500));
        JFrame frame = new JFrame("Document Clustering");
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private XYDataset createDataset(double[][] data, int[] clusterAssignments) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        // add data points to series
        for (int i = 0; i < data.length; i++) {
            XYSeries series = new XYSeries(i);
            series.add(data[i][0], data[i][1]);
            dataset.addSeries(series);
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

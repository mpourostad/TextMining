import java.util.Random;

public class Clustering {
    
    private int k;
    private int maxIterations;
    private double[][] data;
    public Cluster[] clusters;

    public Clustering(int k, int maxIterations, double[][] data) {
        this.k = k;
        this.maxIterations = maxIterations;
        this.data = data;
        this.clusters = new Cluster[k];
    }

    public void cluster() {
        // Initialize clusters with random centroids
        // System.out.print("************************" + data.length);
        Random random = new Random();
        for (int i = 0; i < k; i++) {
            int randomIndex = random.nextInt(data.length);
            double[] centroid = data[randomIndex].clone();
            Cluster cluster = new Cluster(centroid);
            clusters[i] = cluster;
        }
        
        // Assign each data point to the closest cluster
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            for (Cluster cluster : clusters) {
                cluster.clearPoints();
            }
            for (double[] point : data) {
                Cluster closestCluster = null;
                double closestDistance = Double.MAX_VALUE;
                for (Cluster cluster : clusters) {
                    double distance = cosineSimilarity(point, cluster.getCentroid());
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestCluster = cluster;
                    }
                }
                closestCluster.addPoint(point);
            }
            
            // Update cluster centroids
            for (Cluster cluster : clusters) {
                cluster.updateCentroid();
            }
        }
    }
    
    private double cosineSimilarity(double[] vector1, double[] vector2) {
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        for (int i = 0; i < vector1.length; i++) {
            dotProduct += (vector1[i] * vector2[i]);
            norm1 += (vector1[i] * vector1[i]);
            norm2 += (vector2[i] * vector2[i]);
        }
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    public Cluster[] getClusters() {
        return clusters;
    }

}

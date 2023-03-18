import java.util.Random;

public class KMeansClustering {
    private double[][] data; // the data to be clustered
    private int k; // number of clusters
    private int[] assignments; // the cluster assignments for each data point
    private double[][] centroids; // the centroids of the clusters
    private double epsilon = 0.0001; // convergence threshold
    private int maxIterations = 1000; // maximum number of iterations
    private boolean cosineSimilarity = false; // use cosine similarity instead of Euclidean distance

    public KMeansClustering(double[][] data, int k) {
        this.data = data;
        this.k = k;
        this.assignments = new int[data.length];
        this.centroids = new double[k][data[0].length];
    }

    public void setCosineSimilarity(boolean cosineSimilarity) {
        this.cosineSimilarity = cosineSimilarity;
    }

    public int[] cluster() {
        // initialize the centroids randomly
        Random random = new Random();
        for (int i = 0; i < k; i++) {
            int index = random.nextInt(data.length);
            centroids[i] = data[index].clone();
        }

        // run the k-means algorithm
        boolean converged = false;
        int iterations = 0;
        while (!converged && iterations < maxIterations) {
            // assign each data point to the closest centroid
            for (int i = 0; i < data.length; i++) {
                int closest = -1;
                double closestDistance = Double.MAX_VALUE;
                for (int j = 0; j < k; j++) {
                    double distance = cosineSimilarity ? SimilarityMethods.cosineSimilarity(data[i], centroids[j]) : SimilarityMethods.euclideanDistance(data[i], centroids[j]);
                    if (distance < closestDistance) {
                        closest = j;
                        closestDistance = distance;
                    }
                }
                assignments[i] = closest;
            }

            // update the centroids
            double[][] newCentroids = new double[k][data[0].length];
            int[] counts = new int[k];
            for (int i = 0; i < data.length; i++) {
                int cluster = assignments[i];
                for (int j = 0; j < data[i].length; j++) {
                    newCentroids[cluster][j] += data[i][j];
                }
                counts[cluster]++;
            }
            for (int i = 0; i < k; i++) {
                if (counts[i] == 0) {
                    // reinitialize empty clusters
                	int index = random.nextInt(data.length);
                    newCentroids[i] = data[index].clone();
                } else {
                    for (int j = 0; j < newCentroids[i].length; j++) {
                        newCentroids[i][j] /= counts[i];
                    }
                }
            }

            // check for convergence
            double delta = 0;
            for (int i = 0; i < k; i++) {
                delta += cosineSimilarity ? SimilarityMethods.cosineSimilarity(centroids[i], newCentroids[i]) : SimilarityMethods.euclideanDistance(centroids[i], newCentroids[i]);
            }
            if (delta < epsilon) {
                converged = true;
            } else {
                centroids = newCentroids;
            }

            iterations++;
        }

        return assignments;
        }
        }

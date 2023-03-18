public class SimilarityMethods {
    public static double cosineSimilarity(double[] x, double[] y) {
        double dotProduct = 0;
        double normX = 0;
        double normY = 0;
        for (int i = 0; i < x.length; i++) {
            dotProduct += x[i] * y[i];
            normX += x[i] * x[i];
            normY += y[i] * y[i];
        }
        return dotProduct / (Math.sqrt(normX) * Math.sqrt(normY));
    }

    public static double euclideanDistance(double[] x, double[] y) {
        double distance = 0;
        for (int i = 0; i < x.length; i++) {
            double diff = x[i] - y[i];
            distance += diff * diff;
        }
        return Math.sqrt(distance);
    }
}

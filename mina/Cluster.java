public class Cluster {
    private double[][] points;
    private double[] centroid;

    public Cluster(double[] centroid) {
        this.points = new double[0][centroid.length];
        this.centroid = centroid;
    }

    public double[] getCentroid() {
        return centroid;
    }
    public double[][] getPoints() {
        return points;
    }

    public void clearPoints() {
        points = new double[0][centroid.length];
    }

    public void addPoint(double[] point) {
        double[][] newPoints = new double[points.length + 1][centroid.length];
        for (int i = 0; i < points.length; i++) {
            newPoints[i] = points[i];
        }
        newPoints[points.length] = point.clone();
        points = newPoints;
    }

    public void updateCentroid() {
        double[] newCentroid = new double[centroid.length];
        for (double[] point : points) {
            for (int i = 0; i < point.length; i++) {
                newCentroid[i] += point[i];
            }
        }
        for (int i = 0; i < newCentroid.length; i++) {
            newCentroid[i] /= points.length;
        }
        centroid = newCentroid;
    }
}
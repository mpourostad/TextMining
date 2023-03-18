public class Similarity {
    private double similarity;
    public Similarity(double[] a, double[] b){
        this.similarity = cosineSimilarity(a, b);
    }
    // private double[][] dtMatrix;
    // private int numRows;
    // private int numCols;

    // public Similarity(double[][] dtMatrix) {
    //     this.dtMatrix = dtMatrix;
    //     this.numRows = dtMatrix.length;
    //     this.numCols = dtMatrix[0].length;
    // }

    // public double cosine_similarity(int doc1, int doc2) {
    //     // Calculate the dot product of the two documents
    //     double dotProduct = 0;
    //     for (int i = 0; i < numCols; i++) {
    //         dotProduct += dtMatrix[doc1][i] * dtMatrix[doc2][i];
    //     }

    //     // Calculate the magnitude of the two documents
    //     double mag1 = magnitude(doc1);
    //     double mag2 = magnitude(doc2);

    //     // Calculate the cosine similarity
    //     double similarity = dotProduct / (mag1 * mag2);

    //     return similarity;
    // }
    public static double cosineSimilarity(double[] a, double[] b) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < a.length; i++) {
            dotProduct += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        double denominator = Math.sqrt(normA) * Math.sqrt(normB);
        if (denominator == 0.0) {
            return 0.0;
        } else {
            return dotProduct / denominator;
        }
    }
}
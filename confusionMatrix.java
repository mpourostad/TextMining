import java.util.*;

public class confusionMatrix {
    private int[] predictedLabels;
    private int[] actualLabels;
    private int numClusters;
    private int numLabels;
    
    public confusionMatrix(int[] predictedLabels, int[] actualLabels, int numClusters, int numLabels) {
        this.predictedLabels = predictedLabels;
        this.actualLabels = actualLabels;
        this.numClusters = numClusters;
        this.numLabels = numLabels;
    }
    
    public double getPrecision(int label) {
        int truePositives = 0;
        int falsePositives = 0;
        for (int i = 0; i < predictedLabels.length; i++) {
            if (predictedLabels[i] == label && actualLabels[i] == label) {
                truePositives++;
            } else if (predictedLabels[i] == label && actualLabels[i] != label) {
                falsePositives++;
            }
        }
        if (truePositives + falsePositives == 0) {
            return 0;
        } else {
            return (double) truePositives / (truePositives + falsePositives);
        }
    }
    
    public double getRecall(int label) {
        int truePositives = 0;
        int falseNegatives = 0;
        for (int i = 0; i < predictedLabels.length; i++) {
            if (predictedLabels[i] == label && actualLabels[i] == label) {
                truePositives++;
            } else if (predictedLabels[i] != label && actualLabels[i] == label) {
                falseNegatives++;
            }
        }
        if (truePositives + falseNegatives == 0) {
            return 0;
        } else {
            return (double) truePositives / (truePositives + falseNegatives);
        }
    }
    
    public double getFMeasure(int label, double beta) {
        double precision = getPrecision(label);
        double recall = getRecall(label);
        if (precision + recall == 0) {
            return 0;
        } else {
            double betaSquared = beta * beta;
            return (1 + betaSquared) * (precision * recall) / (betaSquared * precision + recall);
        }
    }
    
    public void printConfusionMatrix() {
        int[][] matrix = new int[numClusters][numLabels];
        for (int i = 0; i < predictedLabels.length; i++) {
            matrix[predictedLabels[i]][actualLabels[i]]++;
        }
        
        // print the matrix header
        System.out.print("\t");
        for (int i = 0; i < numLabels; i++) {
            System.out.print("Actual " + i + "\t");
        }
        System.out.println();
        
        // print the matrix contents
        for (int i = 0; i < numClusters; i++) {
            System.out.print("Cluster " + i + "\t");
            for (int j = 0; j < numLabels; j++) {
                System.out.print(matrix[i][j] + "\t\t");
            }
            System.out.println();
        }
    }
}
//This class takes in the predicted and actual labels, as well as the number of clusters and //labels in the data. It provides methods for calculating the precision, recall, and F-measure for a given label, as well as a method for printing the confusion matrix.
//
//To use this class, you can instantiate a ClusteringEvaluation object and call its methods //to evaluate the clustering results.

// This class was developed by Divyanshi Parashar with a few modifications from me. 
import java.util.*;
import java.lang.*;

public class ClusteringEvaluation {
    private int[] predictedLabels;
    private int[] actualLabels;
    private int numClusters;
    private int numLabels;
    
    public ClusteringEvaluation(int[] predictedLabels, int[] actualLabels, int numClusters, int numLabels) {
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
    
    public double getFMeasure(int label) {
        double precision = getPrecision(label);
        double recall = getRecall(label);
        if (precision + recall == 0) {
            return 0;
        } 
        else {
            return 2 * (precision * recall) / (precision + recall);
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
    public void convert_labels(){
        // int count_zeros = 0;
        // int count_ones = 0;
        // int count_twos = 0;
        int[] first_class = {0, 0, 0};
        int[] second_class = {0, 0, 0};
        int[] third_class = {0, 0, 0};
        for (int i = 0; i < predictedLabels.length; i++){
            if (i < 8){
                if (predictedLabels[i] == 0){
                    first_class[0]++;
                }
                else if (predictedLabels[i] == 1){
                    first_class[1]++;
                }
                else{
                    first_class[2]++;
                }
            }
            if (i >= 8 && i < 16){
                if (predictedLabels[i] == 0){
                    second_class[0]++;
                }
                else if (predictedLabels[i] == 1){
                    second_class[1]++;
                }
                else{
                    second_class[2]++;
                }
            }
            else{
                if (predictedLabels[i] == 0){
                    third_class[0]++;
                }
                else if (predictedLabels[i] == 1){
                    third_class[1]++;
                }
                else{
                    third_class[2]++;
                }
            }

        }
        int first_class_label = argmax(first_class);
        int second_class_label = argmax(second_class);
        int third_class_label = argmax(third_class);
        if (second_class_label == first_class_label){
            int[] indx = getNMaxIndices(second_class, 2);
            second_class_label = indx[1];
        }
        int count = 0;
        while (third_class_label == first_class_label || third_class_label == second_class_label){
            int[] indx = getNMaxIndices(third_class, 3);
            third_class_label = indx[count];
            count++;
        }
        // System.out.println("labels");
        // System.out.println(first_class_label);
        // System.out.println(second_class_label);
        // System.out.println(third_class_label);
        for (int i = 0; i < predictedLabels.length; i++){
                if (predictedLabels[i] == first_class_label){
                    predictedLabels[i] = 0;
                }
                else if (predictedLabels[i] == second_class_label){
                    predictedLabels[i] = 1;
                }
                else{
                    predictedLabels[i] = 2;
                }
        }

        
    }
    public static int[] getNMaxIndices(int[] arr, int n) {
        int[] copy = arr.clone(); // create a copy of the original array to preserve its values
        Arrays.sort(copy); // sort the copy in ascending order

        int[] indices = new int[n]; // create an array to hold the indices of the n largest values
        int j = 0; // keep track of the number of indices added so far

        // iterate through the original array and add the values of the n largest values to the values array
        for (int i = arr.length - 1; i >= 0 && j < n; i--) {
            if (arr[i] >= copy[arr.length - n]) {
                indices[j++] = i;
            }
        }

        return indices;
    }
    public static int argmax(int[] arr){
        int re = Integer.MIN_VALUE;
        int arg = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > re) {
                re = arr[i];
                arg = i;
            }
        }
        return arg;
    }
}
//This class takes in the predicted and actual labels, as well as the number of clusters and //labels in the data. It provides methods for calculating the precision, recall, and F-measure for a given label, as well as a method for printing the confusion matrix.
//
//To use this class, you can instantiate a ClusteringEvaluation object and call its methods //to evaluate the clustering results.
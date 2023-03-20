
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import Jama.Matrix;
import Jama.*;
public class TextMining{
	public static void main(String[] args) throws Exception{
		DataCleaning dc = new DataCleaning();
		List<File> inputFiles = new ArrayList<>();
		String[] outputFileName = {"./dataset_3/data/C1_out/", "./dataset_3/data/C4_out/", "./dataset_3/data/C7_out/"};
		String[] files_out = {"article01out1.txt", "article02out1.txt", "article03out1.txt", "article04out1.txt", "article05out1.txt", "article06out1.txt", "article07out1.txt", "article08out1.txt"};
	    for (String i : outputFileName){
	    	for (String j : files_out){
	    		inputFiles.add(new File(i + j));
	    	}
	    }

		DocumentTermMatrix dtm = new DocumentTermMatrix(inputFiles);
		double[][] documentTermMatrix = dtm.getDocumentTermMatrix();
		List<String> allTerms = dtm.getAllTerms();


		double[][] test = new double[24][100];
		for (int i = 0; i < 24; i++ ){
			for (int j = 0; j < 100; j++){
				test[i][j] = documentTermMatrix[i][j];
			}
		}

		kmeansClustering clustering = new kmeansClustering(test, 3);
		clustering.setCosineSimilarity(true);
		int[] clusters = clustering.cluster();

		double[][] reduced = reduceToTwoDimensions(documentTermMatrix);
		printarray(reduced);
		// List<double[]> dataPoints = Arrays.stream(reduced)
        //                     .map(row -> row.clone())
        //                     .collect(Collectors.toList());
        List<Integer> cluster = new ArrayList<>();
		for (int c : clusters) {
			System.out.println(c);
		    cluster.add(c);
		}
		int[] actual_labels = {0 ,0 ,0 ,0 ,0 ,0 ,0 ,0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2};
		ClusteringEvaluation eval= new ClusteringEvaluation(clusters, actual_labels, 3, 3);
		eval.convert_labels();
		System.out.println("class 1 evaluation: ");
		System.out.print("Precision: ");
		System.out.println(eval.getPrecision(0));
		System.out.print("Recall: ");
		System.out.println(eval.getRecall(0));
		System.out.print("F1 Score: ");
		System.out.println(eval.getFMeasure(0));

		System.out.println();
		System.out.println();

		System.out.println("class 2 evaluation: ");
		System.out.print("Precision: ");
		System.out.println(eval.getPrecision(1));
		System.out.print("Recall: ");
		System.out.println(eval.getRecall(1));
		System.out.print("F1 Score: ");
		System.out.println(eval.getFMeasure(1));

		System.out.println();
		System.out.println();

		System.out.println("class 3 evaluation: ");
		System.out.print("Precision: ");
		System.out.println(eval.getPrecision(2));
		System.out.print("Recall: ");
		System.out.println(eval.getRecall(2));
		System.out.print("F1 Score: ");
		System.out.println(eval.getFMeasure(2));



		int[] keywords = findKeyword(documentTermMatrix);
		for (int i = 0; i < keywords.length; i++){
			System.out.println(allTerms.get(keywords[i]));
		}

		Visualization visualize = new Visualization();
		visualize.visualize(reduced, clusters);

		// for (String s: allTerms){
		// 	System.out.print(s + " ");
		// }
		// for (int i = 0; i < documentTermMatrix.length; i++) {
        //     for (int j = 0; j < documentTermMatrix[i].length; j++) {
        //         System.out.print(documentTermMatrix[i][j] + " ");
        //     }
        //     System.out.println();
        // }

        // double[][] test = new double[6][1];
        // test[0][0] = 100;
        // test[1][0] = 101;
        // test[2][0] = 200;
        // test[3][0] = 201;
        // test[4][0] = 300;
        // test[5][0] = 301;
    	// kmeansClustering clustering = new kmeansClustering(test, 3);
		// int[] clusters = clustering.cluster();

		// for (int c : clusters) {
		// 	System.out.println(c);
		//     // cluster.add(c);
		// }


		System.exit(0);
	}
	public static int[] findKeyword(double[][] doc){
		double[][] folders = new double[3][doc[0].length];
		int[] result = new int[3];
		// double[] folder_two = new double[doc[0].length];
		// double[] folder_three = new double[doc[0].length];
		int count = 0;
		int folder_id = 0;
		for(int i = 0; i < doc.length; i++){
			for(int j =0; j < doc[0].length; j++){
				
				folders[folder_id][j] += doc[i][j];
				// System.out.println(folder_id);
			}
			count++;
			if (count % 8 == 0){
				folder_id++;
			}
		}
		// sorting the arrays
		for(int i = 0; i < folders.length; i++){
			result[i] = argmax(folders[i]);
		}
		return result;

	}
	// public static double[][] reduceToTwoDimensions(double[][] matrix) {
	//     int numRows = matrix.length;
	//     int numCols = matrix[0].length;
	//     Matrix X = new Matrix(matrix);
	//     // Matrix X_centered = X.minus(X.mean(0));
	//     Matrix cov = X.transpose().times(X).times(1.0 / (numCols - 1));
	//     EigenvalueDecomposition ed = cov.eig();
	//     Matrix eigenvectors = ed.getV();
	//     Matrix X_pca = X.times(eigenvectors.getMatrix(0, numCols - 1, 0, 1));
	//     double[][] result = X_pca.getArray();
	//     return result;
	// }
	public static double[][] reduceToTwoDimensions(double[][] matrix) {
	    int numRows = matrix.length;
	    int numCols = matrix[0].length;
	    Matrix X = new Matrix(matrix);
	    // Matrix X_centered = X.minus(X.mean(0));
	    Matrix cov = X.transpose().times(X).times(1.0 / (numCols - 1));
	    EigenvalueDecomposition ed = cov.eig();
	    Matrix eigenvectors = ed.getV();
	    double[][] eigenvectors_ = eigenvectors.getArray();
	    // System.out.println(eigenvectors_[0].length);
	    double[] eigenvalues = ed.getRealEigenvalues();
	    int[] argmax = getNMaxIndices(eigenvalues, 2);
	    double[][] max_eigenVectors = new double[numCols][2];
	    max_eigenVectors[numCols - 1][0] = eigenvectors_[numCols - 1][argmax[0]];
	    max_eigenVectors[numCols - 1][1] = eigenvectors_[numCols - 1][argmax[1]];
	    Matrix max_eigenVector_matrix = new Matrix(max_eigenVectors);

	    // System.out.print(argmax[0]);

	    Matrix X_pca = X.times(max_eigenVector_matrix);
	    double[][] result = X_pca.getArray();
	    return result;
	}
	public static int argmax(double[] arr){
		double re = Double.MIN_VALUE;
        int arg = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > re) {
                re = arr[i];
                arg = i;
            }
        }
        return arg;
    }
    public static int[] getNMaxIndices(double[] arr, int n) {
	    double[] copy = arr.clone(); // create a copy of the original array to preserve its values
	    Arrays.sort(copy); // sort the copy in ascending order

	    int[] indices = new int[n]; // create an array to hold the indices of the n largest values
	    int j = 0; // keep track of the number of indices added so far

	    // iterate through the original array and add the indices of the n largest values to the indices array
	    for (int i = arr.length - 1; i >= 0 && j < n; i--) {
	        if (arr[i] >= copy[arr.length - n]) {
	            indices[j++] = i;
	        }
	    }

	    return indices;
	}
	public static void printarray(double [][] data){
		for (int i = 0; i < data.length; i ++){
			for (int j = 0; j < data[0].length; j++){
				System.out.print(data[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
}


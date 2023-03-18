
import java.io.*;
import java.util.*;
import Jama.Matrix;
import Jama.*;
// import org.apache.commons.math3.linear.*;
// import org.apache.commons.math3.stat.correlation.Covariance;

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
		Clustering kMeans = new Clustering(3, 50, documentTermMatrix);
		kMeans.cluster();
		Cluster[] clusters = kMeans.getClusters();

		// for (int i = 0; i < clusters.length; i++) {
		//     System.out.println("Cluster " + i + " centroid: " + Arrays.toString(clusters[i].getCentroid()));
		//     System.out.println("Cluster " + i + " points:");
		//     for (double[] point : clusters[i].getPoints()) {
		//         System.out.println(Arrays.toString(point));
		//     }
		// }
		
		// System.out.print(clusters.length);
		List<double[]> dataPoints = new ArrayList<double[]>(); // List of data points (e.g., double arrays of length 2)
		List<Integer> clusterIds = new ArrayList<Integer>(); // List of cluster ids (one for each data point)
		double[][] cluster_points = new double[24][allTerms.size()];
		double[][] cluster_points_reduced = new double[24][2];
		// System.out.print("**************** ");
		// System.out.print(reduced_dim_points[0].length);
		// double[][] cluster_points = new double[][];
		int count = 0;
		for (int i = 0; i < clusters.length; i++) {
		    // System.out.println("Cluster " + i + " centroid: " + Arrays.toString(clusters[i].getCentroid()));
		    // System.out.println("Cluster " + i + " points:");
		    
		    for (double[] point : clusters[i].getPoints()) {
		    	// System.out.print("**************** ");
		    	// System.out.println(point.length);
		    	
		    	cluster_points[count] = point;
		    	count++;
		    	clusterIds.add(i);
		    }
		}
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ finding a keyword for each folder
		int[] keywords = findKeyword(documentTermMatrix);
		for (int i = 0; i < keywords.length; i++){
			System.out.println(allTerms.get(keywords[i]));
		}
		
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		// cluster_points_reduced = reduceToTwoDimensions(cluster_points);
		// System.out.print(cluster_points[0].length);
		// for (int i = 0; i < cluster_points.length; i++){
		// 	for (int j = 0; j < cluster_points[i].length; j++){

		// 	}
		// }
		for (double[] point : cluster_points_reduced){
			dataPoints.add(point);
		}
		// for (int i = 0; i < dataPoints.size(); i++){
		// 	for (int j = 0; j < dataPoints.get(i).length; j++){
		// 		System.out.print(dataPoints.get(i)[j]);
		// 		System.out.print(" ");
		// 	}
		// 	System.out.println();
		// }
		// for(int i = 0; i < clusterIds.size(); i++){
		// 	System.out.println(clusterIds.get(i));
		// }
		// System.out.print(dataPoints.size());

		// Visualizer visualizer = new Visualizer();
		// visualizer.visualize(dataPoints, clusterIds);
		// for (String s: allTerms){
		// 	// System.out.print(s + " ");

		// }
		// for (File s : inputFiles){
		// 	System.out.println(s);
		// }
		System.out.println(allTerms.size());
		// for (int i = 0; i < documentTermMatrix.length; i++) {
        //     for (int j = 0; j < documentTermMatrix[i].length; j++) {
        //         System.out.print(documentTermMatrix[i][j] + " ");
        //     }
        //     System.out.println();
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

	public static double[][] appendToArray(double[][] array, double[] newRow) {
	    int numRows = array.length;
	    int numCols = array[0].length;

	    double[][] result = new double[numRows + 1][numCols];
	    
	    // Copy the contents of the original array into the new array
	    for (int i = 0; i < numRows; i++) {
	    	// System.out.print("**************** ");
			    	// System.out.println(point.length);
	        for (int j = 0; j < numCols; j++) {
	            result[i][j] = array[i][j];
	        }
	    }
	    
	    // Append the new row to the end of the new array
	    for (int j = 0; j < numCols; j++) {
	        result[numRows][j] = newRow[j];
	    }
	    
    return result;
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
	// public static double[][] reduceToTwoDimensions(double[][] data) {
    //     // Create a PCA transformer
    //     RealMatrix matrix =  MatrixUtils.createRealMatrix(data);
    //     PCA transformer = new PCA(matrix.getColumnDimension());
        
    //     // Fit the transformer to the data
    //     transformer.fit(matrix);
        
    //     // Transform the data to two dimensions
    //     RealMatrix transformedMatrix = transformer.transform(matrix).getSubMatrix(
    //         0, matrix.getRowDimension() - 1, 0, 1);
        
    //     return transformedMatrix.getData();
    // }

	public static double[][] reduceToTwoDimensions(double[][] matrix) {
	    int numRows = matrix.length;
	    int numCols = matrix[0].length;
	    Matrix X = new Matrix(matrix);
	    // Matrix X_centered = X.minus(X.mean(0));
	    Matrix cov = X.transpose().times(X).times(1.0 / (numCols - 1));
	    EigenvalueDecomposition ed = cov.eig();
	    Matrix eigenvectors = ed.getV();
	    Matrix X_pca = X.times(eigenvectors.getMatrix(0, numCols - 1, 0, 1));
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

}

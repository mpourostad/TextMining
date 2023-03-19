
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
		String[] files_out = {"article01out.txt", "article02out.txt", "article03out.txt", "article04out.txt", "article05out.txt", "article06out.txt", "article07out.txt", "article08out.txt"};
	    for (String i : outputFileName){
	    	for (String j : files_out){
	    		inputFiles.add(new File(i + j));
	    	}
	    }

		DocumentTermMatrix dtm = new DocumentTermMatrix(inputFiles);
		double[][] documentTermMatrix = dtm.getDocumentTermMatrix();
		List<String> allTerms = dtm.getAllTerms();
		kmeansClustering clustering = new kmeansClustering(documentTermMatrix, 3);
		clustering.setCosineSimilarity(true);
		int[] clusters = clustering.cluster();

		double[][] reduced = reduceToTwoDimensions(documentTermMatrix);
		printarray(reduced);
		List<double[]> dataPoints = Arrays.stream(reduced)
                            .map(row -> row.clone())
                            .collect(Collectors.toList());
        List<Integer> cluster = new ArrayList<>();
		for (int c : clusters) {
		    cluster.add(c);
		}
		Visualization visualize = new Visualization();
		visualize.visualize(dataPoints, cluster);
		// for (String s: allTerms){
		// 	System.out.print(s + " ");
		// }
		// for (int i = 0; i < documentTermMatrix.length; i++) {
        //     for (int j = 0; j < documentTermMatrix[i].length; j++) {
        //         System.out.print(documentTermMatrix[i][j] + " ");
        //     }
        //     System.out.println();
        // }


		System.exit(0);
	}
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


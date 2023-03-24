
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import Jama.Matrix;
import Jama.*;
import java.util.LinkedList;
public class TextMining{
	public static void main(String[] args) throws Exception{
		// implements 2-grams with 5 as the frequency threshold
		DataCleaning dc = new DataCleaning(2, 5);
		dc.clean();
		List<File> inputFiles = new ArrayList<>();
		String[] outputFileName = {"./dataset_3/data/C1_out/", "./dataset_3/data/C4_out/", "./dataset_3/data/C7_out/"};
		String[] files_out = {"article01out_2grams.txt", "article02out_2grams.txt", "article03out_2grams.txt", "article04out_2grams.txt", "article05out_2grams.txt", "article06out_2grams.txt", "article07out_2grams.txt", "article08out_2grams.txt"};
	    for (String i : outputFileName){
	    	for (String j : files_out){
	    		inputFiles.add(new File(i + j));
	    	}
	    }

		DocumentTermMatrix dtm = new DocumentTermMatrix(inputFiles);
		double[][] documentTermMatrix = dtm.getDocumentTermMatrix();
		List<String> allTerms = dtm.getAllTerms();

		//generating 3 keywords for each folder  and write the output on a text file. 
		int[][] keywords = findKeyword(documentTermMatrix, 3);
		List<String> keyword = new ArrayList<>();
		for (int i = 0; i < keywords.length; i++){
			keyword.add("the best topics describing folder " + (i + 1) + " are:");
			for (int j = 0; j < keywords[0].length; j++){
				keyword.add(allTerms.get(keywords[i][j]));
			}
			
		}
		writeToFile(keyword, "./generated_keywords.txt");

		// generating top 100 key_words and assigning labels to each document 
		int[][] keywords_ = findKeyword(documentTermMatrix, 100);
		int[] labels = generate_labels(documentTermMatrix, keywords_, 3);
		int doc_num = 1; 
		convert_labels(labels);
		System.out.println("generated labels from top 100 topics of each folder: "); 
		for (int i : labels){
			System.out.print("Document " + doc_num + ": ");
			System.out.println(i);
			doc_num++;
		}
		

		int[] actual_labels = {0 ,0 ,0 ,0 ,0 ,0 ,0 ,0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2};
		double[][] reduced = reduceDimensions(documentTermMatrix, 2);

		kmeansClustering clustering = new kmeansClustering(documentTermMatrix, 3);
		for (int i = 0; i < 2; i ++){
			if (i == 0){
				clustering.setCosineSimilarity(true);
				System.out.println("Clustering with Cosine as a similarity method: ");
			}
			else{
				clustering.setCosineSimilarity(false);
				System.out.println("Clustering with euclidean distance: ");
			}
			int[] clusters = clustering.cluster();
			
			ClusteringEvaluation eval= new ClusteringEvaluation(clusters, actual_labels, 3, 3);
			eval.convert_labels();

			evaluation(eval, 0);

			System.out.println();
			System.out.println();

			evaluation(eval, 1);

			System.out.println();
			System.out.println();

			evaluation(eval, 2);

			eval.printConfusionMatrix();


			
			Visualization visualize = new Visualization(reduced, clusters, 3);
			if (i == 0){
				visualize.createChart("Cosine_Chart.jpeg");

			}
				
			else{
				visualize.createChart("Euclidean_Chart.jpeg");
			}
			

		}
		Visualization visualize_actual_labels = new Visualization(reduced, actual_labels, 3);
		visualize_actual_labels.createChart("actual_labels.jpeg");
		


		System.exit(0);
	}
	public static int[][] findKeyword(double[][] doc, int n){
		double[][] folders = new double[3][doc[0].length];
		
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
		int[][] result = new int[3][n];
		// sorting the arrays
		for(int i = 0; i < folders.length; i++){
			result[i] = getNMaxIndices(folders[i], n);
		}
		return result;

	}
	// implementing pca for data reduction
	public static double[][] reduceDimensions(double[][] matrix, int pca_components) {
	    int numRows = matrix.length;
	    int numCols = matrix[0].length;
	    double[] mean = new double[numCols];
	    double[] std = new double[numCols];
	    double[] sumSquared = new double[numCols];
	    double[][] centered_data = new double[numRows][numCols];
	    // Calculate mean of each feature
        for (int j = 0; j < numCols; j++) {
            for (int i = 0; i < numRows; i++) {
                mean[j] += matrix[i][j];
            }
            mean[j] /= numRows;
        }

        for (int j = 0; j < numCols; j++) {
            for (int i = 0; i < numRows; i++) {
                double diff = matrix[i][j] - mean[j];
                sumSquared[j] += diff * diff;
            }
        }
        for (int i = 0; i < std.length; i++){
        	double variance = sumSquared[i] / (numRows - 1.0);
    		std[i] = Math.sqrt(variance);
        }
        

        // Center data
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                // matrix[i][j] -= mean[j];
                // matrix[i][j] /= std[j];
                centered_data[i][j] = (matrix[i][j] - mean[j]) / std[j];
            }
        }
	    Matrix X = new Matrix(matrix);
	    Matrix x_centered = new Matrix(centered_data);
	    // Matrix X_centered = X.minus(X.mean(0));
	    Matrix cov = x_centered.transpose().times(x_centered).times(1.0 / (numCols - 1));
	    EigenvalueDecomposition ed = cov.eig();
	    Matrix eigenvectors = ed.getV();
	    double[][] eigenvectors_ = eigenvectors.getArray();
	    // System.out.println(eigenvectors_[0].length);
	    double[] eigenvalues = ed.getRealEigenvalues();

	    int[] argmax = getNMaxIndices(eigenvalues, pca_components);
	    double[][] max_eigenVectors = new double[numCols][pca_components];
	    for (int i = 0; i < numCols; i++){
	    	for(int j = 0; j < pca_components; j ++){
	    		max_eigenVectors[i][j] = eigenvectors_[i][argmax[j]];
	    	}
	    }
	    
	    Matrix max_eigenVector_matrix = new Matrix(max_eigenVectors);

	    // System.out.print(argmax[0]);

	    Matrix X_pca = x_centered.times(max_eigenVector_matrix);
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
	public static void printarray(double [][] data){
		for (int i = 0; i < data.length; i ++){
			for (int j = 0; j < data[0].length; j++){
				System.out.print(data[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	public static void evaluation(ClusteringEvaluation eval, int cluster){
		int clstr = cluster + 1;
		System.out.println("class " + clstr + " evaluation: ");
		System.out.print("Precision: ");
		System.out.println(eval.getPrecision(cluster));
		System.out.print("Recall: ");
		System.out.println(eval.getRecall(cluster));
		System.out.print("F1 Score: ");
		System.out.println(eval.getFMeasure(cluster));
	}
	public static void writeToFile(List<String> lines, String filePath) {
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
	        for (String line : lines) {
	            writer.write(line);
	            writer.newLine();
	        }
	    } catch (IOException ex) {
	        System.err.println("Error writing to file: " + ex.getMessage());
	    }
	}
	crea
	public static int[] generate_labels(double[][] data, int[][] keywords, int num_classes){
		//building a 1d array from n top topics of each folder. 

		int feature_length = keywords.length * keywords[0].length;
		double[][] dtm = new double[data.length][feature_length];
		int[] kw = new int[feature_length];
		int index = 0;
		for(int i = 0; i < keywords.length; i++){
			for(int j = 0; j < keywords[i].length; j++){
				kw[index] = keywords[i][j];
				index++;
			}
		}
		for (int i = 0; i < data.length; i++){
			for(int j = 0; j < kw.length; j++){
				dtm[i][j] = data[i][kw[j]];
			}
			// System.out.println();
		}
		kmeansClustering label = new kmeansClustering(dtm, num_classes);
		label.setCosineSimilarity(true);
		int[] results = label.cluster();
		return results;
	}
	// converting the actual labels generated from the keywords so that the first cluster's label is always 0, The second is 1 and the third is 2. 
	public static void convert_labels(int[] predictedLabels){
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

}


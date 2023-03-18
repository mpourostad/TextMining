
import java.io.*;
import java.util.*;public class TextMining{
	public static void main(String[] args) throws IOException{
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
		for (int c: clusters){
			System.out.println(c);
		}
		
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
}


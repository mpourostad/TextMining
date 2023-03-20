import java.io.*;
import java.util.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.util.*;
import edu.stanford.nlp.process.*;
public class DataCleaning {
	List<Map.Entry<String,Integer>> pairList;
	    
	// public String[] documents = new String[24];
    public DataCleaning() throws Exception{
    	 // set up the pipeline
    	pairList = new ArrayList<>();
	    Properties props = new Properties();
	    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

	    // define stop words
	    String[] stopWords = stop_words("NLTK_stopwords");

	    String[] inputFileName = {"./dataset_3/data/C1/", "./dataset_3/data/C4/", "./dataset_3/data/C7/"};
	    String[] outputFileName = {"./dataset_3/data/C1_out/", "./dataset_3/data/C4_out/", "./dataset_3/data/C7_out/"};


	    String[] files = {"article01.txt", "article02.txt", "article03.txt", "article04.txt", "article05.txt", "article06.txt", "article07.txt", "article08.txt"};
	    String[] files_out = {"article01out.txt", "article02out.txt", "article03out.txt", "article04out.txt", "article05out.txt", "article06out.txt", "article07out.txt", "article08out.txt"};

	    
	    // documents = new String[24];
	    for (int i = 0; i < inputFileName.length; i++){
	      for (int j = 0; j < files.length; j++){
	        String file_path = inputFileName[i] + files[j];
	        File inputFile = new File(file_path);

	        // File inputFile = new File(inputFileName);
	        BufferedReader reader = new BufferedReader(new FileReader(inputFile));

	        // create output file
	        String out_filePath = outputFileName[i] + files_out[j];
	        File outputFile = new File(out_filePath);
	        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

	        String line;
	        while ((line = reader.readLine()) != null) {
	          // create an Annotation object with the given text
	          Annotation document = new Annotation(line);

	          // run the pipeline on the document
	          pipeline.annotate(document);

	          // get the list of CoreLabels from the document
	          List<CoreLabel> tokens = document.get(CoreAnnotations.TokensAnnotation.class);

	          // create a new list to store the filtered tokens
	          List<String> filteredTokens = new ArrayList<String>();
	          
	      	  String pretoken = "";
	      	  String compounds = "";

	          // set up the stemmer
	          // Stemmer stemmer = new Stemmer();



	          // iterate over the tokens and remove stop words
	          for (CoreLabel token : tokens) {
	            String word = token.get(CoreAnnotations.TextAnnotation.class);
	            String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
	            String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
	            String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

	            // check if the word is a stop word
	            boolean isStopWord = false;
	            boolean isNamedEntity = false;
	            for (String stopWord : stopWords) {
	              if (word.equalsIgnoreCase(stopWord)) {
	                isStopWord = true;
	                break;
	              }
	            }
	            if (!ne.equals("O")) {
	              isNamedEntity = true;
	            }

	            // add the word to the filtered tokens list if it's not a stop word or named entity
	            if (!isStopWord && !isNamedEntity) {
	              // String stemmedWord = stemmer.stem(lemma.toLowerCase()).toString();
	              
		            String t = lemma.toLowerCase();
		            filteredTokens.add(t);
		            if (pretoken == ""){
		            	pretoken = t;
		            }
		            else{
		            	compounds = pretoken + "_" + t;
		            	int indx = find_compound_index(compounds);
		            	if (indx == -1){
		            		Map.Entry<String,Integer> pair = new AbstractMap.SimpleEntry<>(compounds,1);
		            		pairList.add(pair);
		            		// System.out.println(pairList.get(pairList.size() - 1).getValue());
		            		// System.out.println(compounds);
		            	}
		            	else{
		            		// System.out.println("Thaaaaaaaat");
		            		int val = pairList.get(indx).getValue();
		            		pairList.get(indx).setValue(val + 1);

		            	}
		            	pretoken = t;
		            }
		        }

	          }

	          // write the filtered tokens to the output file
	          String outputLine = String.join(" ", filteredTokens);
	          // documents[i + j] += outputLine;
	          writer.write(outputLine);
	          writer.newLine();
	        }
	        // documents[i + j] += " ";
	        // close the input and output files
	        reader.close();
	        writer.close();
	      }
	      //2-grams merge with frequency >= 5
	      merge_words(5);

	  }

    // print a message indicating success
    System.out.println("Data preprocessed");
    }

    public static String[] stop_words(String fileName) throws IOException{
        String content = "";

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine()) != null) {
            content += line + " ";
        }

        // Split the input string using the new line character as the delimiter
        String[] stopwords = content.split(" ");
        // for (String s : stopwords){
        // 	System.out.println(s);
        // }
        return stopwords;
    }
    public static boolean check_frequency(String word, List<Map.Entry<String,Integer>> ls, int target_freq){
    	for (int i = 0; i < ls.size(); i++){
    		String key = ls.get(i).getKey();
    		if (key == word){
    			int val = ls.get(i).getValue();
    			if (val < target_freq){
    				return false;
    			}
    		}
    	}
    	return true;
    }
    public int find_compound_index(String word){
		for (int i = 0; i < pairList.size(); i++){
			String key = pairList.get(i).getKey();
			// System.out.println(key);
			if (key.equals(word)){
				return i;
			}
		}
		return -1;
    }
    public void merge_words(int freq_threshold) throws Exception{
    	// System.out.println("Thiiiiiiis");
    	String[] outputFileName = {"./dataset_3/data/C1_out/", "./dataset_3/data/C4_out/", "./dataset_3/data/C7_out/"};
    	String[] files_out = {"article01out.txt", "article02out.txt", "article03out.txt", "article04out.txt", "article05out.txt", "article06out.txt", "article07out.txt", "article08out.txt"};

	    String[] files_out_2 = {"article01out1.txt", "article02out1.txt", "article03out1.txt", "article04out1.txt", "article05out1.txt", "article06out1.txt", "article07out1.txt", "article08out1.txt"};
	    Properties props = new Properties();
	    props.setProperty("annotators", "tokenize");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

	    
	    // documents = new String[24];
	    for (int i = 0; i < outputFileName.length; i++){
	      for (int j = 0; j < files_out.length; j++){
	        String file_path = outputFileName[i] + files_out[j];
	        File file = new File(file_path);

	        // File inputFile = new File(inputFileName);
	        BufferedReader reader = new BufferedReader(new FileReader(file));

	        // create output file
	        String out_filePath = outputFileName[i] + files_out_2[j];
	        File outputFile = new File(out_filePath);
	        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

	        String line;
	        while ((line = reader.readLine()) != null) {
	          // create an Annotation object with the given text
	          Annotation document = new Annotation(line);

	          // run the pipeline on the document
	          pipeline.annotate(document);

	          // get the list of CoreLabels from the document
	          List<CoreLabel> tokens = document.get(CoreAnnotations.TokensAnnotation.class);

	          // create a new list to store the filtered tokens
	          List<String> filteredTokens = new ArrayList<String>();
	          
	      	  String pretoken = "";
	      	  String compounds = "";

	          // set up the stemmer
	          // Stemmer stemmer = new Stemmer();



	          // iterate over the tokens and remove stop words
	          for (CoreLabel token : tokens) {
	          	String word = token.get(CoreAnnotations.TextAnnotation.class);
          		String compound = pretoken + "_" + word;
          		int index = find_compound_index(compound);
          		if ( index != -1 && pairList.get(index).getValue() >= freq_threshold){
          			// System.out.println("Thiiiiiiis");
          			pretoken = word;
          			word = compound;
          			filteredTokens.remove(filteredTokens.size() - 1);
          		}
          		else{
          			pretoken = word;
          		}
          		
          		filteredTokens.add(word);
	          	
	          }
	          String outputLine = String.join(" ", filteredTokens);
	          // documents[i + j] += outputLine;
	          writer.write(outputLine);
	          writer.newLine();
	        }
	        // documents[i + j] += " ";
	        // close the input and output files
	        reader.close();
          	writer.close();
	      }

	    }

    }

}
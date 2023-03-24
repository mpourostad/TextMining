import java.io.*;
import java.util.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.util.*;
import edu.stanford.nlp.process.*;
// import java.org.tartarus.snowball.*;
// import java.org.tartarus.snowball.ext.englishStemmer;
public class DataCleaning {
	Map<String, Integer> pairList;
	Queue<String> window;
	int windowSize;
	int freq_threshold;
	    
    public DataCleaning(int windowSize, int freq_threshold) throws Exception{
    	
    	pairList = new HashMap<>();
    	window = new LinkedList<>();
    	this.windowSize = windowSize;
    	this.freq_threshold = freq_threshold;
    }
    public void clean() throws Exception{
    	 // set up the pipeline
	    Properties props = new Properties();
	    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

	    // define stop words
	    String[] stopWords = stop_words("NLTK_stopwords");

	    String[] inputFileName = {"./dataset_3/data/C1/", "./dataset_3/data/C4/", "./dataset_3/data/C7/"};
	    String[] outputFileName = {"./dataset_3/data/C1_out/", "./dataset_3/data/C4_out/", "./dataset_3/data/C7_out/"};


	    String[] files = {"article01.txt", "article02.txt", "article03.txt", "article04.txt", "article05.txt", "article06.txt", "article07.txt", "article08.txt"};
	    String[] files_out = {"article01out.txt", "article02out.txt", "article03out.txt", "article04out.txt", "article05out.txt", "article06out.txt", "article07out.txt", "article08out.txt"};

	    
	   
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
	            // stemmer.setCurrent(word);

	            // add the word to the filtered tokens list if it's not a stop word or named entity
	            if (!isStopWord && !isNamedEntity) {
	              // String stemmedWord = stemmer.stem(lemma.toLowerCase()).toString();
	              
		            String t = lemma.toLowerCase();
		            filteredTokens.add(t);
					window.add(t);
                    if (window.size() > windowSize) {
                        window.remove();
                    }
                    if (window.size() == windowSize) {
                        String phrase = String.join("_", window);
                        pairList.put(phrase, pairList.getOrDefault(phrase, 0) + 1);
                    }
		        }

	          }

	          // write the filtered tokens to the output file
	          String outputLine = String.join(" ", filteredTokens);
	          // documents[i + j] += outputLine;
	          writer.write(outputLine);
	          writer.newLine();
	        }
	        // close the input and output files
	        reader.close();
	        writer.close();
	      }
	      window.clear();
	      pairList.entrySet().removeIf(entry -> entry.getValue() <= freq_threshold);
	      merge_words();

	  }

    // print a message indicating success
    System.out.println("Data preprocessed");
    }
    //reads the stopword file into an array of string
    public String[] stop_words(String fileName) throws IOException{
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
    public void merge_words() throws Exception{
    	String[] outputFileName = {"./dataset_3/data/C1_out/", "./dataset_3/data/C4_out/", "./dataset_3/data/C7_out/"};
    	String[] files_out = {"article01out.txt", "article02out.txt", "article03out.txt", "article04out.txt", "article05out.txt", "article06out.txt", "article07out.txt", "article08out.txt"};

	    String[] files_out_2 = {"article01out_2grams.txt", "article02out_2grams.txt", "article03out_2grams.txt", "article04out_2grams.txt", "article05out_2grams.txt", "article06out_2grams.txt", "article07out_2grams.txt", "article08out_2grams.txt"};
	    boolean flag = false;
	    
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
	          String[] words = line.split(" ");
	          List<String> filteredTokens = new ArrayList<String>();
	          
	          for (String word : words) {
	          	// String word = token.get(CoreAnnotations.TextAnnotation.class);
	          	window.add(word);
                if (window.size() > windowSize) {
                    window.remove();
                }
                if (window.size() == windowSize) {

                	String phrase = String.join("_", window);
                	// System.out.println(phrase);
                	if(pairList.containsKey(phrase)){
                		int count = 1;
                		
                		// System.out.println("token: " + word);
                		while(count < windowSize && filteredTokens.size() != 0){
                			filteredTokens.remove(filteredTokens.size() - 1);
                			count++;
                		}
                		// System.out.println("here");
                		flag = true;
                		filteredTokens.add(phrase);
                	}
                	else{
                		filteredTokens.add(word);
                	}
                }

          		else{
          			if (flag ){
          				System.out.println("there");
          				flag = false;
          			}
          			filteredTokens.add(word);
          		}          		
	          	
	          }
	          String outputLine = String.join(" ", filteredTokens);
	          // documents[i + j] += outputLine;
	          writer.write(outputLine);
	          writer.newLine();
	        }
	        reader.close();
          	writer.close();
	      }

	    }

    }

}
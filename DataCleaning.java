import java.io.*;
import java.util.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.util.*;
import edu.stanford.nlp.process.*;
public class DataCleaning {
	public String[] documents = new String[24];
    public DataCleaning() throws IOException{
    	 // set up the pipeline
	    Properties props = new Properties();
	    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

	    // define stop words
	    String[] stopWords = {"a", "an", "the", "and", "but", "or", "for", "nor", "on", "at", "to", "from", "by", "with", "in", "out", "up", "down", "over", "under", ".", ",", ";", ":", "!", "?", "\'", "\"", "(", ")"};

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
	              filteredTokens.add(lemma.toLowerCase());
	            }
	          }

	          // write the filtered tokens to the output file
	          String outputLine = String.join(" ", filteredTokens);
	          documents[i + j] += outputLine;
	          writer.write(outputLine);
	          writer.newLine();
	        }
	        documents[i + j] += " ";
	        // close the input and output files
	        reader.close();
	        writer.close();
	      }

	  }

    // print a message indicating success
    System.out.println("Data preprocessed");
    }

}
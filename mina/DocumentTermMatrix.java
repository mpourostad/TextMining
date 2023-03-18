

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.*;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class DocumentTermMatrix {
    private List<List<String>> documentTokens;
    private List<String> allTerms;
    private double[][] documentTermMatrix;
    private int numDocs = 0;
    private int numTerms = 0;

    public DocumentTermMatrix(List<File> inputFiles) {
        // Initialize the list of document tokens and all terms
        documentTokens = new ArrayList<>();
        allTerms = new ArrayList<>();

        // Tokenize each document and add it to the list of document tokens
        for (File inputFile : inputFiles) {
            List<String> document = tokenizeFile(inputFile);
            documentTokens.add(document);
        }

        // Create the document-term matrix
        numDocs = documentTokens.size();
        numTerms = allTerms.size();
        documentTermMatrix = new double[numDocs][numTerms];

        for (int i = 0; i < numDocs; i++) {
            List<String> document = documentTokens.get(i);
            for (String term : document) {
                int termIndex = allTerms.indexOf(term);
                documentTermMatrix[i][termIndex]++;
            }
        }

        // uncomment the below to print the Document term matrix before and after tfidf transformation
        // printDocumentTermMatrix();
        computeTfIdf();
        // printDocumentTermMatrix();

    }

    public double[][] getDocumentTermMatrix() {
        return documentTermMatrix;
    }

    public List<String> getAllTerms() {
        return allTerms;
    }
    public void printDocumentTermMatrix() {
        System.out.print("\t");
        for (String term : allTerms) {
            System.out.print(term + "\t");
        }
        System.out.println();

        for (int i = 0; i < numDocs; i++) {
            System.out.print("Doc " + (i+1) + "\t");
            for (int j = 0; j < numTerms; j++) {
                System.out.print(documentTermMatrix[i][j]);
            }
            System.out.println();
        }
    }

    private List<String> tokenizeFile(File file) {
        List<String> document = new ArrayList<>();

        // Create a pipeline for processing the text
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        try {
            // Read the file contents into a string
            String fileContents = new String(java.nio.file.Files.readAllBytes(file.toPath()));

            // Process the text using Stanford CoreNLP
            Annotation annotation = new Annotation(fileContents);
            pipeline.annotate(annotation);

            // Extract the tokens from the annotation
            List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
            for (CoreMap sentence : sentences) {
                for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                    String word = token.get(CoreAnnotations.TextAnnotation.class);
                    document.add(word);

                    // Add the term to the list of all terms if it's not already there
                    if (!allTerms.contains(word)) {
                        allTerms.add(word);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return document;
    }
    public void computeTfIdf() {
        // Compute document frequencies for each term
        double[] documentFreqs = new double[allTerms.size()];
        for (int j = 0; j < allTerms.size(); j++) {
            double freq = 0;
            for (int i = 0; i < numDocs; i++) {
                if (documentTermMatrix[i][j] > 0) {
                    freq++;
                }
            }
            documentFreqs[j] = freq;
        }

        // Compute the tf-idf scores for each term in each document
        for (int i = 0; i < numDocs; i++) {
            double[] termCounts = documentTermMatrix[i];
            double sum_term = 0;
            for (double t : termCounts){
                sum_term += t;
            }
            for (int j = 0; j < numTerms; j++) {
    
                double tf = (double) termCounts[j] / (double) sum_term;
                double idf = Math.log((double) numDocs / (double) documentFreqs[j]);
                double tfIdf = tf * idf;
                documentTermMatrix[i][j] = tfIdf;
            }
        }
    }

}

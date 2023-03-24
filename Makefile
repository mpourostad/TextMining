JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $*.java

CLASSES = \
		  TextMining.java \
		  kmeansClustering.java \
		  DataCleaning.java \
		  DocumentTermMatrix.java \
		  Visualization.java \
		  ClusteringEvaluation.java \
		  similarityMethods.java

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
	$(RM) *.jpeg
	$(RM) *.txt
run:
	java TextMining
default: run

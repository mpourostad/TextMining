## Instruction:
---------------
### 1 - In terminal cd to TextMining directory and run the following commands to export the required classpaths:
* export CLASSPATH=$CLASSPATH:$PWD/stanford-corenlp-4.5.2/*
* export CLASSPATH=$CLASSPATH:$PWD/jfreechart-1.0.1/*
* export CLASSPATH=$CLASSPATH:$PWD/Jama/*

### 2 - Run the following commands in terminal to start the program:
* make classes
	* compiles the java files
* make run
	* runs the driver class 
* make clean
	* cleans the outputs of the program and the class files.

## Output Files:
----------------
* Cosine_Chart.jpeg visualizes the k-means clustering with cosine as a similarity measure.
* Euclidean_Chart.jpeg visualizes the k-means clustering with Euclidean distance as a similarity measure.
* actual_labels.jpeg visualizes data with the actual labels. 
* generated_keywords.txt contains the top three generated topics that describes each folder. 
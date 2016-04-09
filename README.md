# Travelling Salesperson Problem Solver with Branch And Bound (B&B) Algorithm

Copyright: Albertus Kelvin (2016-04-09)

A brief application description:
  - this application helps us to solve TSP problem using Branch and Bound Algorithm.
  - the main goal is to find the cheapest cost that someone can got from travelling from a point back to that point
    with pass through all points on the graph.

A brief technical description:
  - Programming language: Java 
  - User interface: Graphical User Interface represented by Java JAR file
  - main program: supermain.java
  - data input: external file (txt)
  - input model: a square matrix
  - manifest file: Main-Class: bin.supermain
  - compilation from CMD: 
      * change directory to your project folder containing bin, then: javac bin/*.java
  - creating a JAR file from CMD:
      * create the manifest file in your project folder
      * still in your project folder, then: jar cfm TSP.jar manifest.txt bin/*.class
  - run the JAR file from CMD:
      * still in your project folder, then: java -jar TSP.jar

An example of data input:
-1 500 200 185 205
500 -1 305 360 340
200 305 -1 320 165
185 360 320 -1 302
205 340 165 302 -1

we can see that the cost between point 0 and 0 is -1 (there is no going to the same point case), the cost between point 0 and 1 is 500,
the cost between point 0 and 2 is 200 and so on.

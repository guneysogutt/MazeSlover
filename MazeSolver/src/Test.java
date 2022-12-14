import GraphPackage.DirectedGraph;

import java.io.*;

public class Test {

     static DirectedGraph directedGraph = new DirectedGraph<>(); // initialize global directed graph
     public static void readMazeFiles(){

        File fileName = new File("MazeFiles\\maze1.txt"); // create file object

        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

            String line; // current line

            int row = 0,column;

            String oldLine = null; // previous line for adding process (converting to graph)

            while((line = bufferedReader.readLine()) != null){

                column = 0; // make the index of column for each step

                while (column < line.length()){ //avoid exceeding the string

                    if (line.substring(column,column + 1).equals(" ")){ // if an element of the path (vertex)

                        String label = row + "," + column; // hold the coordinates in this format : "row,column"

                        directedGraph.addVertex(label); // add new vertex

                        if (oldLine != null) { // check for the upper char (parent)

                            String upperChar = oldLine.substring(column, column + 1); // upper char on the maze

                            if (upperChar.equals(" ")) {
                                String upperLabel = (row - 1) + "," + (column); // hold the upper lable's coordinates
                                // connect the edges with two sides
                                directedGraph.addEdge(label, upperLabel);
                                directedGraph.addEdge(upperLabel, label);
                            }
                        }

                        if (column - 1 >= 0) { // check for the previous char

                            String previousChar = line.substring(column - 1, column); // previous char on the maze

                            if (previousChar.equals(" ")) {

                                String previousLabel = row + "," + (column - 1); // hold previous vertex label
                                // connect the edges with two sides
                                directedGraph.addEdge(label, previousLabel);
                                directedGraph.addEdge(previousLabel, label);
                            }
                        }


                        if (column + 1 != line.length()) { // check for the next char

                            String nextChar = line.substring(column + 1, column + 2); // next char on the maze

                            if (nextChar.equals(" ")) {

                                String nextLabel = row + "," + (column + 1); // hold next vertex label

                                directedGraph.addVertex(nextLabel); // add new vertex
                                // connect the edges with two sides
                                directedGraph.addEdge(label, nextLabel);
                                directedGraph.addEdge(nextLabel, label);
                            }
                        }
                    }
                    column++; // increase column index
                }
                row++; // increase row index
                oldLine = line; // passed to the next line so hold the previous one
            }
        } //end try
        catch (IOException s) {
            System.out.println("File not found!");
        }
    } // end function

    public static void main(String[] args) {
         readMazeFiles();
       directedGraph.displayEdges();
    }
}
import ADTPackage.LinkedStack;
import ADTPackage.StackInterface;
import GraphPackage.UndirectedGraph;

import java.io.*;

public class Test {

    static String mazeString;

     static UndirectedGraph undirectedGraph = new UndirectedGraph<>(); // initialize global directed graph
     public static void readMazeFiles(){

        File fileName = new File("MazeFiles\\maze1.txt"); // create file object

        try {

            mazeString = ""; // initialize the string as null

            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

            String line; // current line

            int row = 0,column;

            String oldLine = null; // previous line for adding process (converting to graph)

            while((line = bufferedReader.readLine()) != null){

                mazeString += line + "\n"; // add current line to the string of maze

                column = 0; // make the index of column for each step

                while (column < line.length()){ //avoid exceeding the string

                    if (line.substring(column,column + 1).equals(" ")){ // if an element of the path (vertex)

                        String label = row + "-" + column; // hold the coordinates in this format : "row,column"

                        undirectedGraph.addVertex(label); // add new vertex

                        if (oldLine != null) { // check for the upper char (parent)

                            String upperChar = oldLine.substring(column, column + 1); // upper char on the maze

                            if (upperChar.equals(" ")) {
                                String upperLabel = (row - 1) + "-" + (column); // hold the upper lable's coordinates
                                // connect the edges with two sides
                                undirectedGraph.addEdge(label, upperLabel);
                                //undirectedGraph.addEdge(upperLabel, label);
                            }
                        }

                        if (column - 1 >= 0) { // check for the previous char

                            String previousChar = line.substring(column - 1, column); // previous char on the maze

                            if (previousChar.equals(" ")) {

                                String previousLabel = row + "-" + (column - 1); // hold previous vertex label
                                // connect the edges with two sides
                                undirectedGraph.addEdge(label, previousLabel);
                                //undirectedGraph.addEdge(previousLabel, label);
                            }
                        }


                        if (column + 1 != line.length()) { // check for the next char

                            String nextChar = line.substring(column + 1, column + 2); // next char on the maze

                            if (nextChar.equals(" ")) {

                                String nextLabel = row + "-" + (column + 1); // hold next vertex label

                                //undirectedGraph.addVertex(nextLabel); // add new vertex
                                // connect the edges with two sides
                                undirectedGraph.addEdge(label, nextLabel);
                                //undirectedGraph.addEdge(nextLabel, label);
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
       undirectedGraph.displayEdges();
        System.out.println("number of edges : " + undirectedGraph.getNumberOfEdges());
        System.out.println("number of vertices : " + undirectedGraph.getNumberOfVertices());

        System.out.println("first edge : " + undirectedGraph.getFirstVertex());
       System.out.println("last edge : " + undirectedGraph.getLastVertex());

        LinkedStack linkedStack = new LinkedStack<>();

        System.out.println(undirectedGraph.getShortestPath(undirectedGraph.getFirstVertex(),undirectedGraph.getLastVertex(),linkedStack));
        undirectedGraph.getBreadthFirstTraversal(undirectedGraph.getFirstVertex(),undirectedGraph.getLastVertex());


        System.out.println(mazeString);
        StringBuilder stringBuilder = new StringBuilder(mazeString);
        LinkedStack shortestPathTemporaryStack = linkedStack;


        // add 10 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        while (!shortestPathTemporaryStack.isEmpty()) {
            String vertex = (String) shortestPathTemporaryStack.pop();
            int row = Integer.parseInt(vertex.substring(0,vertex.indexOf("-")));
            int column = Integer.parseInt(vertex.substring(vertex.indexOf("-") + 1));
            int newIndex = (row * (stringBuilder.indexOf("\n") + 1)) + column;
            stringBuilder.setCharAt(newIndex,'.');
        }

        System.out.println(stringBuilder);

        undirectedGraph.getAdjacencyMatrix();

    }
}
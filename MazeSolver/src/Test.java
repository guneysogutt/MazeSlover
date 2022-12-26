import ADTPackage.LinkedQueue;
import ADTPackage.LinkedStack;
import GraphPackage.UndirectedGraph;

import java.io.*;
import java.util.Random;

public class Test {

    static String mazeString;
     static UndirectedGraph undirectedGraph = new UndirectedGraph<>(); // initialize global directed graph
     public static void readMazeFiles(){

        File fileName = new File("MazeFiles\\maze2.txt"); // create file object

        try {
            Random random = new Random();
            mazeString = ""; // initialize the string as null

            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

            String line; // current line

            int row = 0,column;

            String oldLine = null; // previous line for adding process (converting to graph)

            while((line = bufferedReader.readLine()) != null){

                mazeString += line + "\n"; // add current line to the string of maze

                column = 0; // make the index of column for each step

                while (column < line.length()){ //avoid exceeding the string

                    int randomEdgeWeight = random.nextInt(1,5); // random weight between 1-4

                    if (line.substring(column,column + 1).equals(" ")){ // if an element of the path (vertex)


                        String label = row + "-" + column; // hold the coordinates in this format : "row,column"

                        undirectedGraph.addVertex(label); // add new vertex

                        if (oldLine != null) { // check for the upper char (parent)

                            String upperChar = oldLine.substring(column, column + 1); // upper char on the maze

                            if (upperChar.equals(" ")) {
                                String upperLabel = (row - 1) + "-" + (column); // hold the upper lable's coordinates
                                // connect the edges with two sides
                                undirectedGraph.addEdge(label, upperLabel,randomEdgeWeight);
                            }
                        }


                        if (column - 1 >= 0) { // check for the previous char

                            String previousChar = line.substring(column - 1, column); // previous char on the maze

                            if (previousChar.equals(" ")) {

                                String previousLabel = row + "-" + (column - 1); // hold previous vertex label
                                // connect the edges with two sides
                                undirectedGraph.addEdge(label, previousLabel,randomEdgeWeight);
                            }
                        }


                        if (column + 1 != line.length()) { // check for the next char

                            String nextChar = line.substring(column + 1, column + 2); // next char on the maze

                            if (nextChar.equals(" ")) {

                                String nextLabel = row + "-" + (column + 1); // hold next vertex label

                                // connect the edges with two sides
                                undirectedGraph.addEdge(label, nextLabel,randomEdgeWeight);
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

         readMazeFiles(); // read the file and convert it to graph

        String firstVertex = (String) undirectedGraph.getFirstVertex();
        String endVertex = (String) undirectedGraph.getLastVertex();


        System.out.println("Adjacency list of vertices:");
         undirectedGraph.displayEdges(); // print edges
        //print edges count and vertices count
         System.out.println("Number of Edges : " + undirectedGraph.getNumberOfEdges());
         System.out.println("Number of Vertices : " + undirectedGraph.getNumberOfVertices());

        System.out.println();
        System.out.println();

        undirectedGraph.getAdjacencyMatrix(); // get adjacency matrix

        System.out.println();
        System.out.println();

        // create queues for traversal
        LinkedQueue dfsQueue = (LinkedQueue) undirectedGraph.getDepthFirstTraversal(firstVertex,endVertex);
        LinkedQueue bfsQueue = (LinkedQueue) undirectedGraph.getBreadthFirstTraversal(firstVertex,endVertex);

        // create stack for shortest path traversal
        LinkedStack shortestPathStack = new LinkedStack<>();
        int shortestPathLength = undirectedGraph.getShortestPath(firstVertex,endVertex,shortestPathStack);

        // create stack for cheapest path traversal
        LinkedStack cheapestPathStack = new LinkedStack<>();
        double cheapestPathCost = undirectedGraph.getCheapestPath(firstVertex,endVertex,cheapestPathStack);

        // create string builders for each path
        StringBuilder cheapestPath = new StringBuilder(mazeString);
        StringBuilder shortestPath = new StringBuilder(mazeString);
        StringBuilder dfsPath = new StringBuilder(mazeString);
        StringBuilder bfsPath = new StringBuilder(mazeString);


        int visitedVertexCount = 0; // counter for visited vertices

        while (!dfsQueue.isEmpty()){ // print dfs
            visitedVertexCount++; // increase counter
            String vertex = (String) dfsQueue.dequeue(); // get the current label
            int newIndex = getPrintIndex(vertex,dfsPath);
            dfsPath.setCharAt(newIndex,'.'); // print
        }
        // print dfs
        System.out.println("------------------------------------------------------------");
        System.out.println("Depth First Search");
        System.out.println("Visited Vertex Count For DFS : " + visitedVertexCount);
        System.out.println(dfsPath);


        visitedVertexCount = 0;
        while (!bfsQueue.isEmpty()){ // print bfs
            visitedVertexCount++;// increase counter
            String vertex = (String) bfsQueue.dequeue(); // get the current label
            int newIndex = getPrintIndex(vertex,dfsPath);
            bfsPath.setCharAt(newIndex,'.'); // print
        }
        // print bfs
        System.out.println("------------------------------------------------------------");
        System.out.println("Breadth First Search");
        System.out.println("Visited Vertex Count For BFS : " + visitedVertexCount);
        System.out.println(bfsPath);


        while (!shortestPathStack.isEmpty()) { // print the shortest path
            String vertex = (String) shortestPathStack.pop(); // get the current label
            int newIndex = getPrintIndex(vertex,shortestPath);
            shortestPath.setCharAt(newIndex,'.'); // print
        }
        // print shortest path
        System.out.println("------------------------------------------------------------");
        System.out.println("Shortest Path");
        System.out.println("The visited vertex count in shortest path : " + (shortestPathLength+1));
        System.out.println(shortestPath);


        visitedVertexCount = 0;
        while (!cheapestPathStack.isEmpty()) { // print the cheapest path
            visitedVertexCount++;// increase counter
            String vertex = (String) cheapestPathStack.pop(); // get the current label
            int newIndex = getPrintIndex(vertex,cheapestPath);
            cheapestPath.setCharAt(newIndex,'.'); // print
        }
        // print cheapest path
        System.out.println("------------------------------------------------------------");
        System.out.println("Cheapest Path");
        System.out.println("The cost of the path : " + cheapestPathCost);
        System.out.println("Visited Vertex Count For Cheapest Path : " + visitedVertexCount);
        System.out.println(cheapestPath);

    }

    static int getPrintIndex(String vertex, StringBuilder stringBuilder){
        //split it then get row and column indexes
        int row = Integer.parseInt(vertex.substring(0,vertex.indexOf("-")));
        int column = Integer.parseInt(vertex.substring(vertex.indexOf("-") + 1));
        int newIndex = (row * (stringBuilder.indexOf("\n") + 1)) + column; // print index
        return newIndex;
    }
}
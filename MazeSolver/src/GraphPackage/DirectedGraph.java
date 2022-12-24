package GraphPackage;

import java.util.Iterator;
import ADTPackage.*; // Classes that implement various ADTs


/**
 A class that implements the ADT directed graph.
 @author Frank M. Carrano
 @author Timothy M. Henry
 @version 5.1
 */
public class DirectedGraph<T> implements GraphInterface<T>
{

	double[][] adjacencyMatrix;

   private DictionaryInterface<T, VertexInterface<T>> vertices;
   private int edgeCount;
   
   public DirectedGraph()
   {
      vertices = new UnsortedLinkedDictionary<>();
      edgeCount = 0;
   } // end default constructor

   public boolean addVertex(T vertexLabel)
   {
      VertexInterface<T> addOutcome = vertices.add(vertexLabel, new Vertex<>(vertexLabel));
      return addOutcome == null; // Was addition to dictionary successful?
   } // end addVertex
   
   public boolean addEdge(T begin, T end, double edgeWeight)
   {
      boolean result = false;
      VertexInterface<T> beginVertex = vertices.getValue(begin);
      VertexInterface<T> endVertex = vertices.getValue(end);
      if ( (beginVertex != null) && (endVertex != null) )
         result = beginVertex.connect(endVertex, edgeWeight);
      if (result)
         edgeCount++;
      return result;
   } // end addEdge
   
   public boolean addEdge(T begin, T end)
   {
      return addEdge(begin, end, 0);
   } // end addEdge

   public boolean hasEdge(T begin, T end)
   {
      boolean found = false;
      VertexInterface<T> beginVertex = vertices.getValue(begin);
      VertexInterface<T> endVertex = vertices.getValue(end);
      if ( (beginVertex != null) && (endVertex != null) )
      {
         Iterator<VertexInterface<T>> neighbors = beginVertex.getNeighborIterator();
         while (!found && neighbors.hasNext())
         {
            VertexInterface<T> nextNeighbor = neighbors.next();
            if (endVertex.equals(nextNeighbor))
               found = true;
         } // end while
      } // end if
      
      return found;
   } // end hasEdge

	public boolean isEmpty()
	{
	  return vertices.isEmpty();
	} // end isEmpty

	public void clear()
	{
	  vertices.clear();
	  edgeCount = 0;
	} // end clear

	public int getNumberOfVertices()
	{
	  return vertices.getSize();
	} // end getNumberOfVertices

	public int getNumberOfEdges()
	{
	  return edgeCount;
	} // end getNumberOfEdges

	protected void resetVertices()
	{
	   Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
	   while (vertexIterator.hasNext())
	   {
	      VertexInterface<T> nextVertex = vertexIterator.next();
	      nextVertex.unvisit();
	      nextVertex.setCost(0);
	      nextVertex.setPredecessor(null);
	   } // end while
	} // end resetVertices

	public StackInterface<T> getTopologicalOrder()
	{
		resetVertices();

		StackInterface<T> vertexStack = new LinkedStack<>();
		int numberOfVertices = getNumberOfVertices();
		for (int counter = 1; counter <= numberOfVertices; counter++)
		{
			VertexInterface<T> nextVertex = findTerminal();
			nextVertex.visit();
			vertexStack.push(nextVertex.getLabel());
		} // end for
		
		return vertexStack;	
	} // end getTopologicalOrder


	public T getFirstVertex(){
	   T firstEdge = null;
	   Iterator<T> iterator = vertices.getKeyIterator(); // create an iterator variable
		//the dictionary hold the vertices in reverse order so the first vertex is the last key of the dictionary
	   while(iterator.hasNext()) {
		   firstEdge = iterator.next(); // last key in the dictionary
	   }
	   return firstEdge;
	} // end getFirstVertex

	public T getLastVertex(){
		T lastEdge = null;
		Iterator<T> iterator = vertices.getKeyIterator(); // create an iterator variable
		lastEdge = iterator.next(); // the dictionary is reverse order so the first item is the last vertex
		return lastEdge;
	} // end getLastVertex


	public void getNeighbors(T vertex){ // temporary function !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	    VertexInterface<T> frontVertex = vertices.getValue(vertex);
		Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
		while (neighbors.hasNext()){
			System.out.println("neighbor : " + neighbors.next());
		}
	}


	public void getAdjacencyMatrix(){
	   int size = vertices.getSize(); // number of vertices
	   adjacencyMatrix = new double[size][size]; // initialize the array with size*size
	   VertexInterface<T> firstVertex = vertices.getValue(getFirstVertex());
	   Iterator<VertexInterface<T>> vertexNeighborIterator = firstVertex.getNeighborIterator(); // neighbor iterator
	   Iterator<T> keyIterator = vertices.getKeyIterator(); // key iterator

	} // end getAdjacencyMatrix


	public int getIndex(T label){
	   Iterator<T> keyIterator = vertices.getKeyIterator(); // create iterator
	   int counter = 0; // initialize index counter
	   while (keyIterator.hasNext()){ // visit the keys until label is found
		   T currentVertex = keyIterator.next();
		   if (currentVertex.equals(label)) // if found end loop
			   break;
		   counter++; // increase counter;
	   }
	   return counter; // return the index number
	} // end getIndex


   //###########################################################################
   public QueueInterface<T> getBreadthFirstTraversal(T origin, T end) {
	   resetVertices(); // reset vertices
	   boolean done = false; // initialize boolean variable

	   // initialize vertex queue and traversal queue
	   QueueInterface<VertexInterface<T>> vertexQueue = new LinkedQueue<VertexInterface<T>>();
	   QueueInterface<VertexInterface<T>> traversalQueue = new LinkedQueue<VertexInterface<T>>();
	   // create origin and end vertices
	   VertexInterface<T> originVertex = vertices.getValue(origin);
	   VertexInterface<T> endVertex = vertices.getValue(end);

	   // add the origin vertex to traversal and vertex queue
	   traversalQueue.enqueue(originVertex);
	   vertexQueue.enqueue(originVertex);
	   System.out.println(originVertex);
	   originVertex.visit(); // set the origin vertex visited

	   while (!done && !vertexQueue.isEmpty()){
		   VertexInterface<T> frontVertex = vertexQueue.dequeue(); // get the current vertex
		   System.out.println("current : " + frontVertex);
		   Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator(); // initialize neighbor iterator
		   while (!done && neighbors.hasNext()){ // do while vertex has neighbor and end vertex is not found
			   VertexInterface<T> nextNeighbor = neighbors.next(); // hold the next neighbor
			   if (!nextNeighbor.isVisited()){ // if not visited
			   nextNeighbor.visit(); // set as visited
			   // add current neighbor to the queues
			   traversalQueue.enqueue(nextNeighbor);
			   System.out.println(nextNeighbor);
			   vertexQueue.enqueue(nextNeighbor);
			   }

			   if (nextNeighbor.equals(endVertex)) // if last vertex found finish the process
				   done = true;
		   }
	   }

	   return (QueueInterface<T>) traversalQueue; // return the traversal
   } // end getBreadthFirstTraversal

    //		return the traversal order between origin vertex and end vertex

   //###########################################################################

  
	
	
   //###########################################################################
   public QueueInterface<T> getDepthFirstTraversal(T origin, T end) {
	   resetVertices();// reset vertices
	   // initialize vertex stack and traversal queue
	   StackInterface<VertexInterface<T>> vertexStack = new LinkedStack<VertexInterface<T>>();
	   QueueInterface<VertexInterface<T>> traversalQueue = new LinkedQueue<VertexInterface<T>>();
	   // create origin and end vertices
	   VertexInterface<T> originVertex = vertices.getValue(origin);
	   VertexInterface<T> endVertex = vertices.getValue(end);

	   boolean done = false; // initialize boolean variable

	   // add the origin vertex to traversal and vertex queue
	   traversalQueue.enqueue(originVertex);
	   System.out.println(originVertex);
	   vertexStack.push(originVertex);
	   originVertex.visit(); // set the origin vertex visited

	   while (!done && !vertexStack.isEmpty()){
		   VertexInterface<T> topVertex = vertexStack.peek(); // get the current vertex
		   System.out.println("current : " + topVertex);
		   Iterator<VertexInterface<T>> neighbors = topVertex.getNeighborIterator();// initialize neighbor iterator
		   while (!done && neighbors.hasNext()){ // do while vertex has neighbor and end vertex is not found
			   VertexInterface<T> nextNeighbor = neighbors.next(); // hold the next neighbor
				 if(!nextNeighbor.isVisited()){ // if not visited
					nextNeighbor.visit(); // set as visited
					 // add current neighbor to the queues
					 traversalQueue.enqueue(nextNeighbor);
					 System.out.println(nextNeighbor);
					 vertexStack.push(nextNeighbor);
					 if (nextNeighbor.equals(endVertex)) // if last vertex found finish the process
						 done = true;
				 }
				 else // all neighbors are visited
					 vertexStack.pop();
		   }
	   }
	   return (QueueInterface<T>) traversalQueue; // return the traversal
   } // end getDepthFirstTraversal
    		//return depth first search traversal order between origin vertex and end vertex

   //###########################################################################
		
	
	
	
	//###########################################################################
	public int getShortestPath(T begin, T end, StackInterface<T> path) {
	    resetVertices(); // reset vertices
	    boolean done = false; // initialize boolean check variable
	    QueueInterface<VertexInterface<T>> vertexQueue = new LinkedQueue<VertexInterface<T>>();// initialize vertex queue
		// create origin and end vertices
	    VertexInterface<T> originVertex = vertices.getValue(begin);
	    VertexInterface<T> endVertex = vertices.getValue(end);

	    originVertex.visit(); // set origin vertex is visited

	    vertexQueue.enqueue(originVertex); // add origin vertex to the vertex queue

	    while (!done && !vertexQueue.isEmpty()){
	        VertexInterface<T> frontVertex = vertexQueue.dequeue(); // hold the front vertex of the queue
	        Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator(); // get current vertex neighbor iterator

	        while (!done && neighbors.hasNext()){
	      	    VertexInterface<T> nextNeighbor = neighbors.next(); // hold the next neighbor
	      	    if (!nextNeighbor.isVisited()){ // if not visited
	      	 	   nextNeighbor.visit(); // set is visited
	      	 	   nextNeighbor.setCost(1 + frontVertex.getCost()); // increase the path cost by 1
	      	 	   nextNeighbor.setPredecessor(frontVertex); // set the new predecessor
	      	 	   vertexQueue.enqueue(nextNeighbor); // add the neighbor to the vertex queue
	      	    }
	      	    if (nextNeighbor.equals(endVertex)) // if the neighbor equals to tje end vertex
	      	 	   done = true; // end the process
	        } // end while
	    } // end while

	   	 int pathLength = (int)endVertex.getCost(); // get the path langth referred to the end vertex's cost
	   	 path.push(endVertex.getLabel()); // add end vertex to the stack(parameter)

	   	 VertexInterface<T> vertex = endVertex;
	   	 while(vertex.hasPredecessor()){
	   		 vertex = vertex.getPredecessor();
	   		 path.push(vertex.getLabel());
	   	 }
	    return pathLength;
   } // end getShortestPath

	    // 		return the shortest path between begin vertex and end vertex

    //###########################################################################
  
   
	
   
    //###########################################################################
	// Precondition: path is an empty stack (NOT null) */
    //Use EntryPQ instead of Vertex in Priority Queue because multiple entries contain
     //* 	the same vertex but different costs - cost of path to vertex is EntryPQ's priority value
     //*
	public double getCheapestPath(T begin, T end, StackInterface<T> path){
	   return 0.0;
   }
     //		return the cost of the cheapest path

    //###########################################################################
	
	protected VertexInterface<T> findTerminal()
	{
		boolean found = false;
		VertexInterface<T> result = null;

		Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();

		while (!found && vertexIterator.hasNext())
		{
			VertexInterface<T> nextVertex = vertexIterator.next();
			
			// If nextVertex is unvisited AND has only visited neighbors
			if (!nextVertex.isVisited())
			{ 
				if (nextVertex.getUnvisitedNeighbor() == null )
				{ 
					found = true;
					result = nextVertex;
				} // end if
			} // end if
		} // end while

		return result;
	} // end findTerminal

	// Used for testing
	public void displayEdges()
	{
		System.out.println("\nEdges exist from the first vertex in each line to the other vertices in the line.");
		System.out.println("(Edge weights are given; weights are zero for unweighted graphs):\n");
		Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
		while (vertexIterator.hasNext())
		{
			((Vertex<T>)(vertexIterator.next())).display();
		} // end while
	} // end displayEdges 
	
	private class EntryPQ implements Comparable<EntryPQ>
	{
		private VertexInterface<T> vertex; 	
		private VertexInterface<T> previousVertex; 
		private double cost; // cost to nextVertex
		
		private EntryPQ(VertexInterface<T> vertex, double cost, VertexInterface<T> previousVertex)
		{
			this.vertex = vertex;
			this.previousVertex = previousVertex;
			this.cost = cost;
		} // end constructor
		
		public VertexInterface<T> getVertex()
		{
			return vertex;
		} // end getVertex
		
		public VertexInterface<T> getPredecessor()
		{
			return previousVertex;
		} // end getPredecessor

		public double getCost()
		{
			return cost;
		} // end getCost
		
		public int compareTo(EntryPQ otherEntry)
		{
			// Using opposite of reality since our priority queue uses a maxHeap;
			// could revise using a minheap
			return (int)Math.signum(otherEntry.cost - cost);
		} // end compareTo
		
		public String toString()
		{
			return vertex.toString() + " " + cost;
		} // end toString 
	} // end EntryPQ
} // end DirectedGraph

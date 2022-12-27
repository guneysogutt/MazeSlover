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

	public void getAdjacencyMatrix(){
	   int size = vertices.getSize(); // number of vertices
	   adjacencyMatrix = new double[size][size]; // initialize the array with size*size
	   Iterator<VertexInterface<T>> keyIterator = vertices.getValueIterator(); // key iterator
		while (keyIterator.hasNext()){ // visit all vertices

			VertexInterface<T> currentVertex = keyIterator.next(); // hold the current vertex
			Iterator<VertexInterface<T>> vertexNeighborIterator = currentVertex.getNeighborIterator(); // neighbor iterator
			int currentVertexIndex = getIndex(currentVertex.getLabel()); // get the index
			while (vertexNeighborIterator.hasNext()){ // visit all neighbors for assigning adjacency
				VertexInterface<T> nextNeighbor = vertexNeighborIterator.next();
				int neighborIndex = getIndex(nextNeighbor.getLabel());
				adjacencyMatrix[currentVertexIndex][neighborIndex] = getEdgeWeight(currentVertex.getLabel(), nextNeighbor.getLabel()); // assign the cost to the matrix
			} // end getAdjacencyMatrix


		}
		Iterator<VertexInterface<T>> columnIterator = vertices.getValueIterator();
		System.out.print("        ");
		for (int i = 0; i < size; i++){
			System.out.printf("%7s",columnIterator.next());
		}
		System.out.println();
		// print matrix
		Iterator<VertexInterface<T>> rowIterator = vertices.getValueIterator();
		for (int i = 0; i < size; i++){
			System.out.printf("%7s",rowIterator.next());
			for (int j = 0; j < size; j++){
				System.out.printf("%7s",adjacencyMatrix[i][j]);
			}
			System.out.println();
		}

	} // end getAdjacencyMatrix

	private int getIndex(T label){
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

	public T getFirstVertex(){
		T firstVertex = null;
		Iterator<T> iterator = vertices.getKeyIterator(); // create an iterator variable
		//the dictionary hold the vertices in reverse order so the first vertex is the last key of the dictionary
		while(iterator.hasNext()) {
			firstVertex = iterator.next(); // last key in the dictionary
		}
		return firstVertex;
	} // end getFirstVertex

	public T getLastVertex(){
		T lastEdge = null;
		Iterator<T> iterator = vertices.getKeyIterator(); // create an iterator variable
		lastEdge = iterator.next(); // the dictionary is reverse order so the first item is the last vertex
		return lastEdge;
	} // end getLastVertex

	private Double getEdgeWeight(T begin, T end) {
		boolean found = false;
		double weight = 0.0;
		VertexInterface<T> beginVertex = vertices.getValue(begin);
		VertexInterface<T> endVertex = vertices.getValue(end);
		if ( (beginVertex != null) && (endVertex != null) )
		{
			Iterator<VertexInterface<T>> neighbors = beginVertex.getNeighborIterator();
			Iterator<Double> weightIterator = beginVertex.getWeightIterator();

			while (!found && neighbors.hasNext())
			{
				VertexInterface<T> nextNeighbor = neighbors.next();
				weight = weightIterator.next();
				if (endVertex.equals(nextNeighbor))
					found = true;
			} // end while
		} // end if

		return weight;
	} // end getEdgeWeight

    public QueueInterface<T> getBreadthFirstTraversal(T origin, T end) {
	   resetVertices(); // reset vertices
	   boolean done = false; // initialize boolean variable

	   // initialize vertex queue and traversal queue
	   QueueInterface<VertexInterface<T>> vertexQueue = new LinkedQueue<VertexInterface<T>>();
	   QueueInterface traversalQueue = new LinkedQueue();
	   // create origin and end vertices
	   VertexInterface<T> originVertex = vertices.getValue(origin);
	   VertexInterface<T> endVertex = vertices.getValue(end);

	   // add the origin vertex to traversal and vertex queue
	   vertexQueue.enqueue(originVertex);
	   originVertex.visit(); // set the origin vertex visited

	   while (!done && !vertexQueue.isEmpty()){
		   VertexInterface<T> frontVertex = vertexQueue.dequeue(); // get the current vertex
		   traversalQueue.enqueue(frontVertex.getLabel());

		   Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator(); // initialize neighbor iterator
		   while (neighbors.hasNext()){ // do while vertex has neighbor and end vertex is not found
			   VertexInterface<T> nextNeighbor = neighbors.next(); // hold the next neighbor
			   if (!nextNeighbor.isVisited()){ // if not visited
			   nextNeighbor.visit(); // set as visited
			   // add current neighbor to the queues
			   vertexQueue.enqueue(nextNeighbor);
			   }
		   }
		   if (frontVertex.equals(endVertex)) // if last vertex found finish the process
			   done = true;
	   }

	   return traversalQueue; // return the traversal
   } // end getBreadthFirstTraversal

	public QueueInterface<T> getDepthFirstTraversal(T origin, T end) {
		resetVertices();// reset vertices
		// initialize vertex stack and traversal queue
		StackInterface<VertexInterface<T>> vertexStack = new LinkedStack<VertexInterface<T>>();
		LinkedQueue traversalQueue = new LinkedQueue();
		// create origin and end vertices
		VertexInterface<T> originVertex = vertices.getValue(origin);
		VertexInterface<T> endVertex = vertices.getValue(end);

		boolean done = false; // initialize boolean variable

		// add the origin vertex to traversal and vertex queue
		vertexStack.push(originVertex);
		originVertex.visit(); // set the origin vertex visited

		while (!done && !vertexStack.isEmpty()){
			VertexInterface<T> frontVertex = vertexStack.pop(); // get the current vertex
			traversalQueue.enqueue(frontVertex.getLabel());
			Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator(); // initialize neighbor iterator
			while (neighbors.hasNext()){ // do while vertex has neighbor and end vertex is not found
				VertexInterface<T> nextNeighbor = neighbors.next(); // hold the next neighbor
				if (!nextNeighbor.isVisited()){ // if not visited
					nextNeighbor.visit(); // set as visited
					// add current neighbor to the queues
					vertexStack.push(nextNeighbor);
				}
			}
			if (frontVertex.equals(endVertex)) // if last vertex found finish the process
				done = true;
		}

		return traversalQueue; // return the traversal
	} // end getDepthFirstTraversal

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
	      	    if (nextNeighbor.equals(endVertex)) // if the neighbor equals to the end vertex
	      	 	   done = true; // end the process
	        } // end while
	    } // end while

	   	 int pathLength = (int)endVertex.getCost(); // get the path length referred to the end vertex's cost
	   	 path.push(endVertex.getLabel()); // add end vertex to the stack(parameter)

	   	 VertexInterface<T> vertex = endVertex;
	   	 while(vertex.hasPredecessor()){
	   		 vertex = vertex.getPredecessor();
	   		 path.push(vertex.getLabel());
	   	 }
	    return pathLength;
   } // end getShortestPath

	public double getCheapestPath(T begin, T end, StackInterface<T> path){
	   resetVertices();
	   boolean done = false;
		VertexInterface<T> originVertex = vertices.getValue(begin);
		VertexInterface<T> endVertex = vertices.getValue(end);
		QueueInterface priorityQueue = new LinkedQueue();
		priorityQueue.enqueue(new EntryPQ(originVertex,0,null));

		while (!done && !priorityQueue.isEmpty()){
			EntryPQ frontEntry = (EntryPQ) priorityQueue.dequeue();
			VertexInterface<T> frontVertex = frontEntry.getVertex();
			if (!frontVertex.isVisited()){
				frontVertex.visit();
				frontVertex.setCost(frontEntry.getCost()); // increase the path cost by 1
				frontVertex.setPredecessor(frontEntry.getPredecessor()); // set the new predecessor
				if (frontVertex.equals(endVertex))
					done= true;
				else{
					Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator(); // get current vertex neighbor iterator
					while (neighbors.hasNext()){
						VertexInterface<T> nextNeighbor = neighbors.next();
						//get new weight
						double weightOfToNeighbor = getEdgeWeight(frontVertex.getLabel(), nextNeighbor.getLabel());
						if (!nextNeighbor.isVisited()){
							//calculate new cost
							double nextCost = weightOfToNeighbor + frontVertex.getCost();

							priorityQueue.enqueue(new EntryPQ(nextNeighbor,nextCost,frontVertex));
						}
					}
				}
			}
		}

		double pathCost = endVertex.getCost();
		path.push(endVertex.getLabel()); // add end vertex to the stack(parameter)

		VertexInterface<T> vertex = endVertex;
		while(vertex.hasPredecessor()){
			vertex = vertex.getPredecessor();
			path.push(vertex.getLabel());
		}

	   return pathCost;
   } // end getCheapestPath
	
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

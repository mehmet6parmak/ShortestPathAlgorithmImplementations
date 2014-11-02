package algorithms;

import model.Edge;
import model.Graph;
import model.Vertex;

/**
 * From: http://www.csupomona.edu/~ftang/courses/CS241/notes/graph%20algorithms%201.htm
 * Suggested Video: http://www.youtube.com/watch?v=ozsuci5pIso
 * 
 * The Bellman-Ford algorithms computes single-source shortest paths in a weighted diagraph. 
 * The algorithm is named after its developers, Richard Bellman and Lester Ford, Jr. For graphs with non-negative weights, 
 * Dijkstra's algorithm solves the problem. The Bellman-Ford algorithm is used primarily for graphs with negative weights. 
 * The algorithm can detect negative cycles and report their existence, 
 * but it cannot produce a correct "shortest path" if a negative cycle is reachable from the source.
 * 
 * From Wikipedia: http://en.wikipedia.org/wiki/Distance-vector_routing_protocol
 * 
 *  - The Bellman–Ford algorithm does not prevent routing loops from happening and suffers from the count-to-infinity problem.
 * 
 * @author mehmet
 *
 * A negative cycle in the graph causes {@link ShortestPathAlgorithm.previous} array to contain cycles 
 * which causes produceResults method to get into an infinite loop.
 * 
 */
public class BellmanFordImplementation extends ShortestPathAlgorithm {

	public BellmanFordImplementation(Graph graph) {
		super(graph);
	}

	@Override
	public void findShortestPathBetween(Vertex source, Vertex destination) throws Exception {
		super.initialize(source, destination);

		super.runTheAlgorithm();

		// Bellman-Ford negative cycle detection
		for (Edge edge : graph.getEdges()) {
			double sourceDistance = distances[findVertexIndex(edge.getSource())];
			double edgeCost = edgeCostsMap.get(String.format("%d-%d", edge.getSource().getId(), edge.getDestination().getId()));
			double destinationDistance = distances[findVertexIndex(edge.getDestination())];
			if (sourceDistance + edgeCost < destinationDistance) {
				throw new NegativeCycleException();
			}
		}

		produceResult(source, destination);
	}
	
	public class NegativeCycleException extends Exception
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 4623012609954460507L;
		
		public NegativeCycleException() {
			super("Negative cycle found in the graph.");
		}
	}
}

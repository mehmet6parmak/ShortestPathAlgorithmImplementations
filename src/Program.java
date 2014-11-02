import java.util.Map;
import java.util.Set;

import model.Edge;
import model.Graph;
import model.Node;
import model.Vertex;
import algorithms.BellmanFordImplementation;
import algorithms.DijkstraImplementation;
import algorithms.DistanceVectorRoutingImplementation;
import algorithms.ShortestPathAlgorithm;

public class Program {

	public static void main(String[] args) {

//		dijkstra();
//		bellmanFord();
		
		DistanceVectorRoutingImplementation vectorAlg = new DistanceVectorRoutingImplementation();
		vectorAlg.init();
		//vectorAlg.print();
		Map<Integer, Node> node = vectorAlg.getNodes();
		int nodeCount = node.keySet().size();
		/*
		 *exchanges routing tables b/w neighbors, recompute tables
		 *and sends distance routing vector to neighbors, until the algorithm
		 *converges (or no more changed can be made to nodes - i.e. updating halts)
		 */
		while(nodeCount>0){
			nodeCount = node.keySet().size();
			/*
			 * applies the distance vector algorithm to a node an if a change is made to its distance vector, then
			 * send off the new distance vector to its neighbors
			 */
			for(Integer s: node.keySet()){
				/*
				 * If the algorithm updated the distance vector of the node, then we send said distance vector off to that nodes neighbors
				 */
				if(node.get(s).runAlgorithm()){					
					Map<Integer, Double> dVector = node.get(s).getDistanceVector();
					Set<Integer> neighbors = node.get(s).getNeighbours();
					vectorAlg.sendMessage(dVector, neighbors, s);
				}
				else{
					//increment counter which is how we know when we're done!
					//when they have all stopped changing, we know we're done!
					nodeCount--;
				}
			}
		}
		vectorAlg.printTable();
	}

	protected static void bellmanFord() {
		// Graph at http://www.csupomona.edu/~ftang/courses/CS241/notes/images/graph/bellman2.gif
		// Modification to the graph: changed the direction of 2->4 to 4->2 and changed the weight to -4 from 4 to create a negative cycle
		Vertex vertex1 = new Vertex(1, 2, 3);
		Vertex vertex2 = new Vertex(2, 3, 5);//modified, original Vertex(2, 3, 4, 5);
		Vertex vertex3 = new Vertex(3, 5);
		Vertex vertex4 = new Vertex(4, 2, 6);//modified, original Vertex(4, 6);
		Vertex vertex5 = new Vertex(5, 4, 6);
		Vertex vertex6 = new Vertex(6);
		
		Edge edge = new Edge(vertex1, vertex2, -2);
		Edge edge2 = new Edge(vertex1, vertex3, 5);
		Edge edge3 = new Edge(vertex2, vertex3, 2);
		Edge edge4 = new Edge(vertex4, vertex2, -4);//modified, original Edge(vertex2, vertex4, 4);
		Edge edge5 = new Edge(vertex2, vertex5, 4);
		Edge edge6 = new Edge(vertex3, vertex5, 1);
		Edge edge7 = new Edge(vertex5, vertex4, -1);
		Edge edge8 = new Edge(vertex5, vertex6, 3);
		Edge edge9 = new Edge(vertex4, vertex6, 1);

		Vertex[] vertexes = new Vertex[] { vertex1, vertex2, vertex3, vertex4, vertex5, vertex6 };
		Edge[] edges = new Edge[] { edge, edge2, edge3, edge4, edge5, edge6, edge7, edge8, edge9 };
		
		Graph graphWithNegativeCycle = new Graph(vertexes, edges);
		
		ShortestPathAlgorithm bellmanFord = new BellmanFordImplementation(graphWithNegativeCycle);
		ShortestPathAlgorithm shortestPathAlgorithm = new ShortestPathAlgorithm(graphWithNegativeCycle);
		try {
			//This will not terminate
			//shortestPathAlgorithm.findShortestPathBetween(vertex1, vertex6); 
			bellmanFord.findShortestPathBetween(vertex1, vertex6);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	protected static void dijkstra() {
		// The graph at
		// http://math.mit.edu/~rothvoss/18.304.3PM/Presentations/1-Melissa.pdf
		Vertex vertex1 = new Vertex(1, 2, 3);
		Vertex vertex2 = new Vertex(2, 3, 4, 5);
		Vertex vertex3 = new Vertex(3, 5);
		Vertex vertex4 = new Vertex(4, 6);
		Vertex vertex5 = new Vertex(5, 4, 6);
		Vertex vertex6 = new Vertex(6);

		Edge edge = new Edge(vertex1, vertex2, 2);
		Edge edge2 = new Edge(vertex1, vertex3, 4);
		Edge edge3 = new Edge(vertex2, vertex3, 1);
		Edge edge4 = new Edge(vertex2, vertex4, 4);
		Edge edge5 = new Edge(vertex2, vertex5, 2);
		Edge edge6 = new Edge(vertex3, vertex5, 3);
		Edge edge7 = new Edge(vertex5, vertex4, 3);
		Edge edge8 = new Edge(vertex5, vertex6, 2);
		Edge edge9 = new Edge(vertex4, vertex6, 2);

		Vertex[] vertexes = new Vertex[] { vertex1, vertex2, vertex3, vertex4, vertex5, vertex6 };
		Edge[] edges = new Edge[] { edge, edge2, edge3, edge4, edge5, edge6, edge7, edge8, edge9 };

		Graph graph = new Graph(vertexes, edges);

		ShortestPathAlgorithm dijkstra = new DijkstraImplementation(graph);
		try {
			dijkstra.findShortestPathBetween(vertex1, vertex5);
			dijkstra.findShortestPathBetween(vertex2, vertex6);
			dijkstra.findShortestPathBetween(vertex1, vertex6);
			dijkstra.findShortestPathBetween(vertex3, vertex4);

			dijkstra.findShortestPathBetween(vertex6, vertex4);
			dijkstra.findShortestPathBetween(vertex4, vertex1);
		} catch (Exception err) {
			System.out.println(err.getMessage());
		}
	}

}

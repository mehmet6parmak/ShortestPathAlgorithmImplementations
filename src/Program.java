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
		dijkstra();
		bellmanFord();
		distanceVectorRouting();
	}

	protected static void distanceVectorRouting() {
		Node node1 = new Node(1);
		Node node2 = new Node(2);
		Node node3 = new Node(3);
		Node node4 = new Node(4);
		Node node5 = new Node(5);
		Node node6 = new Node(6);

		// undirected graph
		node1.addNeighbour(2, 3.8);
		node2.addNeighbour(1, 3.8);

		node1.addNeighbour(3, 1.2);
		node3.addNeighbour(1, 1.2);

		node2.addNeighbour(4, 5.1);
		node4.addNeighbour(2, 5.1);

		node3.addNeighbour(4, 5.2);
		node4.addNeighbour(3, 5.2);

		node2.addNeighbour(5, 1.1);
		node5.addNeighbour(2, 1.1);

		node4.addNeighbour(5, 3.2);
		node5.addNeighbour(4, 3.2);

		node5.addNeighbour(6, 4.4);
		node6.addNeighbour(5, 4.4);

		Node[] nodesArray = new Node[] { node1, node2, node3, node4, node5, node6 };

		DistanceVectorRoutingImplementation distanceVectorAlgorithm = new DistanceVectorRoutingImplementation(nodesArray);
		System.out.println("\n\nDistance Vector Routing Algorithm");
		distanceVectorAlgorithm.findShortestPath(node1, node6);
		
	}

	protected static void bellmanFord() {
		// Graph at
		// http://www.csupomona.edu/~ftang/courses/CS241/notes/images/graph/bellman2.gif
		// Modification to the graph: changed the direction of 2->4 to 4->2 and
		// changed the weight to -4 from 4 to create a negative cycle
		Vertex vertex1 = new Vertex(1, 2, 3);
		Vertex vertex2 = new Vertex(2, 3, 5);// modified, original Vertex(2, 3,
												// 4, 5);
		Vertex vertex3 = new Vertex(3, 5);
		Vertex vertex4 = new Vertex(4, 2, 6);// modified, original Vertex(4, 6);
		Vertex vertex5 = new Vertex(5, 4, 6);
		Vertex vertex6 = new Vertex(6);

		Edge edge = new Edge(vertex1, vertex2, -2);
		Edge edge2 = new Edge(vertex1, vertex3, 5);
		Edge edge3 = new Edge(vertex2, vertex3, 2);
		Edge edge4 = new Edge(vertex4, vertex2, -4);// modified, original
													// Edge(vertex2, vertex4,
													// 4);
		Edge edge5 = new Edge(vertex2, vertex5, 4);
		Edge edge6 = new Edge(vertex3, vertex5, 1);
		Edge edge7 = new Edge(vertex5, vertex4, -1);
		Edge edge8 = new Edge(vertex5, vertex6, 3);
		Edge edge9 = new Edge(vertex4, vertex6, 1);

		Vertex[] vertexes = new Vertex[] { vertex1, vertex2, vertex3, vertex4, vertex5, vertex6 };
		Edge[] edges = new Edge[] { edge, edge2, edge3, edge4, edge5, edge6, edge7, edge8, edge9 };

		Graph graphWithNegativeCycle = new Graph(vertexes, edges);

		ShortestPathAlgorithm bellmanFord = new BellmanFordImplementation(graphWithNegativeCycle);
//		ShortestPathAlgorithm shortestPathAlgorithm = new ShortestPathAlgorithm(graphWithNegativeCycle);
		try {
			System.out.println("\n\nBellman-Ford Algorithm");
			// This will not terminate
			// shortestPathAlgorithm.findShortestPathBetween(vertex1, vertex6);
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
			
			System.out.println("Dijkstra Algorithm");
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

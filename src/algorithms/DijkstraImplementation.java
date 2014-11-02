package algorithms;

import model.Edge;
import model.Graph;
import model.Vertex;

/**
 * 
 * Wikipedia: Dijkstra's algorithm, conceived by computer scientist Edsger
 * Dijkstra in 1956 and published in 1959, is a graph search algorithm that
 * solves the single-source shortest path problem for a graph with non-negative
 * edge path costs, producing a shortest path tree. This algorithm is often used
 * in routing and as a subroutine in other graph algorithms.
 * 
 * There is a problem with this algorithm (http://www.youtube.com/watch?v=ozsuci5pIso)
 *  1- Will not terminate if there is a negative cycle reachable from the source. (fixed in Bellman-Ford algorithm)
 * 
 * 
 * Anyway, prefer this algortihm to Bellman-Ford if you are sure that no negative cycles exist in the graph because 
 * Bellman-Ford's complexity is higher. 
 * 
 * @author mehmet
 *
 */
public class DijkstraImplementation extends ShortestPathAlgorithm {

	public DijkstraImplementation(Graph graph) {
		super(graph);

		for (Edge edge : graph.getEdges()) {
			if (edge.getWeight() < 0)
				throw new IllegalArgumentException("Disjkstra Algorithm does not support negative edge weights.");
		}
	}

	@Override
	protected void runTheAlgorithm() {
		int removedCount = 0;
		while (removedCount < vertexCount) {
			int index = findVertexWithMinDistance(distances, unvisited);
			Vertex vertex = unvisited[index];
			unvisited[index] = null;
			removedCount++;

			//Difference from the base class
			//terminate the algorithm here since the shortest path to destination is found.
			if (index == indexOfDestination) {
				break;
			}

			if (vertex.getNeighbours() != null && vertex.getNeighbours().length > 0) {
				for (Vertex neighbour : vertex.getNeighbours()) {
					int neighbourIndex = findVertexIndex(neighbour);
					double alt = distances[index] + edgeCostsMap.get(String.format("%d-%d", vertex.getId(), neighbour.getId()));
					if (alt < distances[neighbourIndex]) {
						distances[neighbourIndex] = alt;
						//Where did we come here that short? Hold the vertexes providing the shortest path in previous array.
						previous[neighbourIndex] = vertex;
					}
				}
			}
		}
	}
	
	
	
}

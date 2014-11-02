package algorithms;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import model.Edge;
import model.Graph;
import model.Vertex;

public class ShortestPathAlgorithm {

	protected Graph graph;
	protected Vertex[] unvisited;
	protected Vertex[] previous;
	protected double[] distances;
	protected int vertexCount;
	protected Map<String, Double> edgeCostsMap;
	protected int indexOfSource;
	protected int indexOfDestination;

	public ShortestPathAlgorithm(Graph graph) {
		this.graph = graph;
	}
	
	
	/**
	 * 
	 * @param source vertex
	 * @param destination vertex
	 * @throws Exception Dijkstra implementation does not throw exception. But the Bellman-Ford implementation does.
	 */
	public void findShortestPathBetween(Vertex source, Vertex destination) throws Exception {
		initialize(source, destination);
	
		runTheAlgorithm();
	
		produceResult(source, destination);
	}

	protected void produceResult(Vertex source, Vertex destination) {
		//Extracting path from the previous array which holds the vertex visited previously from each vertex and has the smallest path length up to there.
		Stack<Vertex> vertexesVisited = new Stack<Vertex>();
		int previousIndex = indexOfDestination;
		while (previous[previousIndex] != null) {
			vertexesVisited.push(previous[previousIndex]);
			previousIndex = findVertexIndex(previous[previousIndex]);
		}
	
		double shortestPathLength = distances[indexOfDestination];
		if (shortestPathLength == Double.MAX_VALUE) {
			System.out.println(String.format("There is no path between %d and %d", source.getId(), destination.getId()));
		} else {
			System.out.println(String.format("Shortest path length from %d to %d is %f", source.getId(), destination.getId(), shortestPathLength));
	
			String path = "";
			while (vertexesVisited.size() > 0) {
				Vertex vertex = vertexesVisited.pop();
				path += String.format("%d-", vertex.getId());
			}
			path += destination.getId();
			System.out.println("Path: " + path);
		}
	}

	protected void runTheAlgorithm() {
			int removedCount = 0;
			while (removedCount < vertexCount) {
				int index = findVertexWithMinDistance(distances, unvisited);
				Vertex vertex = unvisited[index];
				unvisited[index] = null;
				removedCount++;
	
				//TODO we may terminate the algorithm here since the shortest path to destination is found.
	//			if (index == indexOfDestionation) {
	//				break;
	//			}
	
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

	protected void initialize(Vertex source, Vertex destination) {
		vertexCount = graph.getVertexes().length;
	
		unvisited = Arrays.copyOf(graph.getVertexes(), vertexCount);
		previous = new Vertex[vertexCount];
		distances = new double[vertexCount];
	
		indexOfSource = 0;
		indexOfDestination = 0;
		for (int i = 0; i < vertexCount; i++) {
			if (source.equals(unvisited[i])) {
				indexOfSource = i;
			} else if (destination.equals(unvisited[i])) {
				indexOfDestination = i;
			}
		}
	
		previous[indexOfSource] = null;
		distances[indexOfSource] = 0;
		for (int i = 0; i < vertexCount; i++) {
			if (i != indexOfSource) {
				distances[i] = Double.MAX_VALUE;
				previous[i] = null;
			}
		}
	
		edgeCostsMap = generateEdgeCostMap();
	}

	protected int findVertexIndex(Vertex vertex) {
		for (int i = 0; i < graph.getVertexes().length; i++) {
			if (graph.getVertexes()[i].equals(vertex))
				return i;
		}
		return -1;
	}

	protected int findVertexWithMinDistance(double[] distances, Vertex[] vertexes) {
		int smallestIndex = 0;
		double smallestValue = Double.MAX_VALUE;
		for (int i = 0; i < distances.length; i++) {
			if (distances[i] <= smallestValue && vertexes[i] != null) {
				smallestValue = distances[i];
				smallestIndex = i;
			}
		}
		return smallestIndex;
	}

	private Map<String, Double> generateEdgeCostMap() {
		Map<String, Double> result = new HashMap<String, Double>();
		for (Edge edge : graph.getEdges()) {
			result.put(String.format("%d-%d", edge.getSource().getId(), edge.getDestination().getId()), edge.getWeight());
		}
		return result;
	}

}
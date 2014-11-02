package algorithms;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import model.Node;

/**
 * 
 * From Wikipedia: A distance-vector routing protocol requires that a router
 * informs its neighbors of topology changes periodically. Compared to
 * link-state protocols, which require a router to inform all the nodes in a
 * network of topology changes, distance-vector routing protocols have less
 * computational complexity and message overhead.
 * 
 * 
 * Routers using distance-vector protocol do not have knowledge of the entire
 * path to a destination. Instead they use two methods: 1- Direction in which
 * router or exit interface a packet should be forwarded. 2- Distance from its
 * destination
 * 
 * Watch: http://www.youtube.com/watch?v=ylzAefKENXY
 * 
 * @author mehmet
 *
 */
public class DistanceVectorRoutingImplementation {

	Map<Integer, Node> nodesMap;

	public DistanceVectorRoutingImplementation(Node[] nodes) {
		nodesMap = new HashMap<Integer, Node>(nodes.length);
		for (Node node : nodes) {
			nodesMap.put(node.getId(), node);
		}

		for (Node value : nodesMap.values()) {
			value.setNonNeighbours(nodesMap.keySet());
		}
	}

	/**
	 * Sends the updated distanceVector to its neighbours
	 * 
	 * @param distanceVector
	 * @param neighbors
	 */
	public void sendDistanceVector(Map<Integer, Double> distanceVector, Set<Integer> neighbors, Integer fromNode) {
		for (Integer neighbour : neighbors) {
			nodesMap.get(neighbour).receiveDistanceVector(distanceVector, fromNode);
		}
	}

	public void initialize() {
		// each node sends its distanceVector to its neighbours.
		for (Node node : nodesMap.values()) {
			sendDistanceVector(node.getDistanceVector(), node.getNeighbours(), node.getId());
		}
	}

	public void findShortestPath(Node source, Node destination) {
		initialize();

		int nodeCount = nodesMap.keySet().size();

		// run till convergence which means no distanceVectors change with the
		// algorithm
		while (nodeCount > 0) {
			nodeCount = nodesMap.keySet().size();
			for (Integer s : nodesMap.keySet()) {

				Node currentNode = nodesMap.get(s);
				// If the algorithm updated the distance vector of the node,
				// updated distanceVector sent to neighbours.
				if (currentNode.runTheAlgorithm()) {
					Map<Integer, Double> distanceVector = currentNode.getDistanceVector();
					Set<Integer> neighbours = currentNode.getNeighbours();
					sendDistanceVector(distanceVector, neighbours, s);
				} else {
					nodeCount--;
				}
			}
		}
		
		Node targetNode = null;
		int sourceId = source.getId();
		String path = String.valueOf(source.getId());
		double totalCost = 0;
		do {
			int targetNodeId = source.getFirstHopToDestination(destination.getId());
			targetNode = nodesMap.get(targetNodeId);
			totalCost += source.getDistanceVector().get(targetNodeId);
			path += "-" + targetNode.getId();
			
			source = targetNode;
		} while (targetNode.getId() != destination.getId());
		
		System.out.println(String.format("Cost from %d to %d: %f", sourceId, destination.getId(), totalCost));
		System.out.println("Path: " + path);
	}
}

package algorithms;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
 * @author mehmet
 *
 */
public class DistanceVectorRoutingImplementation {

	Map<Integer, Node> nodesMap;

	public DistanceVectorRoutingImplementation() {

		// all of the nodes in the topology
		nodesMap = new HashMap<Integer, Node>();
		
		Node node1 = new Node(1);
		Node node2 = new Node(2);
		Node node3 = new Node(3);
		Node node4 = new Node(4);
		Node node5 = new Node(5);
		Node node6 = new Node(6);
		
		nodesMap.put(1, node1);
		nodesMap.put(2, node2);
		nodesMap.put(3, node3);
		nodesMap.put(4, node4);
		nodesMap.put(5, node5);
		nodesMap.put(6, node6);
		
		//undirected graph
		node1.setNeighbor(2, 3.8);
		node2.setNeighbor(1, 3.8);
		
		node1.setNeighbor(3, 1.2);
		node3.setNeighbor(1, 1.2);
		
		node2.setNeighbor(4, 5.1);
		node4.setNeighbor(2, 5.1);
		
		node3.setNeighbor(4, 5.2);
		node4.setNeighbor(3, 5.2);
		
		node2.setNeighbor(5, 1.1);
		node5.setNeighbor(2, 1.1);
		
		node4.setNeighbor(5, 3.2);
		node5.setNeighbor(4, 3.2);
		
		node5.setNeighbor(6, 4.4);
		node6.setNeighbor(5, 4.4);
		
		populate();
	}

	/**
	 * Populate each nodes distance vectors for non-neighbors
	 */
	private void populate() {
		for (Node value : nodesMap.values()) {
			value.setNonNeighbors(nodesMap.keySet());
		}

	}

	/**
	 * Prints the distance vectors of each node in the topology
	 */
	public void print() {
		for (Node value : nodesMap.values()) {
			value.printDistanceVector();
		}
	}

	/**
	 * returns the data member node treemap
	 * 
	 * @return
	 */
	public Map<Integer, Node> getNodes() {
		return nodesMap;
	}

	/**
	 * Sends the updated dVector to the specified neighbors
	 * 
	 * @param treeMap
	 * @param neighbors
	 */
	public void sendMessage(Map<Integer, Double> treeMap, Set<Integer> neighbors, Integer fromNode) {
		for (Integer s : neighbors) {
			nodesMap.get(s).receiveDistanceVector(treeMap, fromNode);
		}
	}

	/**
	 * sends the initial distance vectors of each node to its neighbors
	 */
	public void init() {
		for (Node n : nodesMap.values()) {
			sendMessage(n.getDistanceVector(), n.getNeighbors(), n.getId());
		}

	}

	/**
	 * Constructs and prints the final table to stdout
	 */
	public void printTable() {

		System.out.println("\t\t\tDistance/First Hop from Source to Destination\n");
		System.out.println("\t\t\t       INF = 'not reachable'\n");
		System.out.println("\t\t\t         Destination\n");
		System.out.print("Source\t\t");

		/*
		 * prints a node to stdout for constructing a table as a new column
		 * entry
		 */
		for (Integer nodeId : nodesMap.keySet()) {
			System.out.print(nodeId + "\t");
		}
		System.out.println();
		/*
		 * formats table with dashes
		 */
		for (int i = 0; i < nodesMap.keySet().size(); i++) {
			System.out.print("-----------");
		}

		/*
		 * appends node name to the table as a new row
		 */
		for (Integer nodeId : nodesMap.keySet()) {
			System.out.println();
			System.out.print("\t" + nodeId + "|\t");
			
			// gets the distance vector of the specified node
			Map<Integer, Double> tm = nodesMap.get(nodeId).getDistanceVector();

			/*
			 * Prints the distance from the given node to its destination node
			 * as well as the first hop node
			 */
			for (Integer j : tm.keySet()) {
				Integer hop = nodesMap.get(nodeId).getFirstHopToDestination(j);
				double dist = tm.get(j);
				if (dist == -1.0) {
					System.out.print("INF\t");
				} else {
					System.out.print(tm.get(j) + "/" + hop + "\t");
				}
			}
		}
		System.out.println();
	}
}

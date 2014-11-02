package algorithms;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Node {

	/*
	 * Note: Each node maintains a vector of distances (and next hops) to all
	 * destinations Each node is also aware of its neighbors and the link cost
	 * to reach them!
	 */

	// set distances to neighbors
	HashMap<Integer, Double> neighbors;

	// distanceVector which contains distances to every node in the network
	Map<Integer, Double> distanceVector;

	// each node holds a temporary distanceVector of all its neighbors. It's updated when a
	// distanceVector is received via sendMessage
	Map<Integer, Map<Integer, Double>> neighborDistanceVector;

	// map which contains the first hop for a node to its given destination
	// <dest, first hop>
	Map<Integer, Integer> firstHop;

	Integer id;

	/**
	 * initialize distance to self with 0 and infinity to other nodes within the
	 * vector
	 * 
	 * @param id
	 *            - name of the node
	 */
	public Node(Integer id) {
		this.id = id;
		neighbors = new HashMap<Integer, Double>();
		distanceVector = new TreeMap<Integer, Double>();
		neighborDistanceVector = new HashMap<Integer, Map<Integer, Double>>();
		firstHop = new HashMap<Integer, Integer>();
		distanceVector.put(id, 0.0);
	}

	/**
	 * Sets the distances to neighbors and concurrently adds that distance to
	 * the dVector
	 * 
	 * @param name
	 *            - name of the neighboring node
	 * @param distance
	 *            - distance to the neighboring node
	 */
	public void setNeighbor(Integer id, double distance) {
		neighbors.put(id, distance);
		distanceVector.put(id, distance);
		firstHop.put(id, id);
	}

	/**
	 * Adds non-neighbors to the distance vector and sets their distance to -1
	 * (INF). Also adds non-neighbors to the firstHop data structure.
	 * 
	 * @param set
	 *            - a set of all non-neighboring nodes
	 */
	public void setNonNeighbors(Set<Integer> set) {
		/*
		 * Adds node to dVector and firstHop if it hasn't been added yet
		 */
		for (Integer nodeId : set) {
			if (!distanceVector.containsKey(nodeId)) {
				distanceVector.put(nodeId, -1.0);
				firstHop.put(id, this.getId());
			}
		}
	}

	/**
	 * Display the distance vector
	 */
	public void printDistanceVector() {
		System.out.println("Node " + id + " distance vector:");
		/*
		 * prints a node's dVector
		 */
		for (Map.Entry<Integer, Double> e : distanceVector.entrySet()) {
			Integer key = e.getKey();
			Double value = e.getValue();
			if (value != -1)
				System.out.println(key + " | " + value);
			else
				System.out.println(key + " | " + "INF");
		}
	}

	/**
	 * Returns the neighbors
	 * 
	 * @return
	 */
	public Set<Integer> getNeighbors() {
		return neighbors.keySet();
	}

	/**
	 * Gets the current set cost to the destination node
	 * 
	 * @param n
	 *            - the destination node
	 * @return
	 */
	public double getCostToNode(Integer n) {
		return distanceVector.get(n);
	}

	/**
	 * returns the first hop node of this object that is taken to get to the
	 * specified destination
	 * 
	 * @param n
	 *            - the destination node
	 * @return
	 */
	public Integer getFirstHopToDestination(Integer n) {
		return firstHop.get(n);
	}

	/**
	 * update the dVector
	 * 
	 * @param key
	 *            - name of the node which we are setting the summed distance to
	 * @param value
	 *            - the distance to get to the key
	 */
	public void updateDistanceVector(Integer key, double value) {
		this.distanceVector.put(key, value);
	}

	/**
	 * Updates the first hop node of the given node
	 * 
	 * @param destinationId
	 *            - the given destination node
	 * @param neighborId
	 *            - the neighbor this node would have to hop to first in order
	 *            to get to the destination node
	 */
	public void updateFirstHop(Integer destinationId, Integer neighborId) {
		firstHop.put(destinationId, neighborId);
	}

	/**
	 * Returns the nodes that will be receiving the distance vector/
	 * 
	 * dVector class will get the dVector of node x, loop through the set, and
	 * send it to each. they will receive it, set their hashmap/tree, and then
	 * run their own algorithm
	 * 
	 * @return
	 */
	public boolean runAlgorithm() {

		boolean change = false;
		/*
		 * For every node (other than 'this'), we peek at the distance vectors
		 * received from neighboring nodes, and compute the min - updating the
		 * current DV
		 */
		// and if the min is smaller than the current value of that dVector, set
		// it, and then have that node send a message to it's neighbors.
		for (Integer neighbor : this.getNeighbors()) {

			Map<Integer, Double> wDVector = neighborDistanceVector.get(neighbor);

			for (Integer n : distanceVector.keySet()) {

				if (!this.id.equals(n)) {
					// cost of neighbor to N
					Double neighborToNCost = wDVector.get(n);
					// link distance to specified neighbor
					Double xToNeighborCost = this.neighbors.get(neighbor);

					// check if distance is infinity, in which case, leave the
					// dVector alone.
					if (neighborToNCost != -1) {

						// find the min of {cost to current iteration neighbor +
						// cost from that neighbor to the current destination
						// node n}
						double xToNCost = this.getCostToNode(n);
						Double sumCost = neighborToNCost + xToNeighborCost;
						sumCost = Math.round((sumCost * 100)) / 100.0;
						if (xToNCost == -1) {
							// then we know that the sum is smaller than the
							// current value of INF

							this.updateDistanceVector(n, sumCost);
							this.updateFirstHop(n, neighbor);
							change = true;
						} else {
							// double sumCost = neighborToNCost +
							// xToNeighborCost;

							if (sumCost < xToNCost) {
								// System.out.println("Updating DV 2" +sumCost);
								// if the new cost is shorter, then update the
								// distance vector!
								// and have said node, send a message to it's
								// neighbors - containing its new dVector
								this.updateDistanceVector(n, sumCost);
								this.updateFirstHop(n, neighbor);
								change = true;
							}
						}
					}
				}
			}
		}
		return change;
	}

	public void receiveDistanceVector(Map<Integer, Double> treeMap, Integer fromNode) {
		neighborDistanceVector.put(fromNode, treeMap);
	}

	public Integer getId() {
		return id;
	}

	public Map<Integer, Double> getDistanceVector() {
		return distanceVector;
	}
}

package model;

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
	HashMap<Integer, Double> neighbours;

	// distanceVector which contains distances to every node in the network
	Map<Integer, Double> distanceVector;

	// each node holds a temporary distanceVector of all its neighbors. It's updated when a
	// distanceVector is received via sendMessage
	Map<Integer, Map<Integer, Double>> neighbourDistanceVectors;

	// map which contains the first hop for a node to its given destination
	// <dest, first hop>
	Map<Integer, Integer> firstHop;

	Integer id;

	public Node(Integer id) {
		this.id = id;
		neighbours = new HashMap<Integer, Double>();
		distanceVector = new TreeMap<Integer, Double>();
		neighbourDistanceVectors = new HashMap<Integer, Map<Integer, Double>>();
		firstHop = new HashMap<Integer, Integer>();

		//Distance to itself is zero.
		distanceVector.put(id, 0.0);
	}

	public void addNeighbour(Integer neighbourId, double distance) {
		neighbours.put(neighbourId, distance);
		distanceVector.put(neighbourId, distance);
		firstHop.put(neighbourId, neighbourId);
	}

	public void setNonNeighbours(Set<Integer> set) {
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
		for (Map.Entry<Integer, Double> e : distanceVector.entrySet()) {
			Integer key = e.getKey();
			Double value = e.getValue();
			if (value != -1)
				System.out.println(key + " | " + value);
			else
				System.out.println(key + " | " + "INF");
		}
	}

	public Set<Integer> getNeighbours() {
		return neighbours.keySet();
	}

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

	public boolean runAlgorithm() {

		boolean change = false;
		/*
		 * For every node (other than 'this'), we peek at the distance vectors
		 * received from neighboring nodes, and compute the min - updating the
		 * current DV
		 */
		// and if the min is smaller than the current value of that dVector, set
		// it, and then have that node send a message to it's neighbors.
		for (Integer neighbor : this.getNeighbours()) {

			Map<Integer, Double> neighbourDistanceVector = neighbourDistanceVectors.get(neighbor);

			for (Integer nodeId : distanceVector.keySet()) {

				if (!this.id.equals(nodeId)) {
					// cost of neighbor to N
					Double neighbourToNCost = neighbourDistanceVector.get(nodeId);
					// link distance to specified neighbor
					Double xToNeighborCost = this.neighbours.get(neighbor);

					// check if distance is infinity, in which case, leave the
					// dVector alone.
					if (neighbourToNCost != -1) {

						// find the min of {cost to current iteration neighbor +
						// cost from that neighbor to the current destination
						// node n}
						double xToNCost = this.getCostToNode(nodeId);
						Double sumCost = neighbourToNCost + xToNeighborCost;
						sumCost = Math.round((sumCost * 100)) / 100.0;
						if (xToNCost == -1) {
							// then we know that the sum is smaller than the
							// current value of INF

							this.updateDistanceVector(nodeId, sumCost);
							this.updateFirstHop(nodeId, neighbor);
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
								this.updateDistanceVector(nodeId, sumCost);
								this.updateFirstHop(nodeId, neighbor);
								change = true;
							}
						}
					}
				}
			}
		}
		return change;
	}

	public void receiveDistanceVector(Map<Integer, Double> distanceVector, Integer nodeId) {
		neighbourDistanceVectors.put(nodeId, distanceVector);
	}

	public Integer getId() {
		return id;
	}

	public Map<Integer, Double> getDistanceVector() {
		return distanceVector;
	}
}

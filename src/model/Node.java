package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Node {

	Integer id;
	// Distances to neighbors
	HashMap<Integer, Double> neighbours;

	// distanceVector which contains distances to every node. Updated with every distanceVector sharing till convergence.
	Map<Integer, Double> distanceVector;

	// each node holds a temporary distanceVector of all its neighbors. It's
	// updated when a
	// distanceVector is received via sendMessage
	Map<Integer, Map<Integer, Double>> neighbourDistanceVectors;

	// Map which contains the first hop for a node to its given destination
	// <dest, nodeId to hop first>
	Map<Integer, Integer> firstHop;

	public Node(Integer id) {
		this.id = id;
		neighbours = new HashMap<Integer, Double>();
		distanceVector = new TreeMap<Integer, Double>();
		neighbourDistanceVectors = new HashMap<Integer, Map<Integer, Double>>();
		firstHop = new HashMap<Integer, Integer>();

		// Distance to itself is zero.
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
				distanceVector.put(nodeId, Double.MAX_VALUE);
				firstHop.put(id, this.getId());
			}
		}
	}

	public Set<Integer> getNeighbours() {
		return neighbours.keySet();
	}

	public double getCostToNode(Integer n) {
		return distanceVector.get(n);
	}

	public Integer getFirstHopToDestination(Integer n) {
		return firstHop.get(n);
	}

	public void updateDistanceVector(Integer key, double value) {
		this.distanceVector.put(key, value);
	}

	public void updateFirstHop(Integer destinationId, Integer neighborId) {
		firstHop.put(destinationId, neighborId);
	}

	//this part is very similar to previous algorithms Dijkstra, BellmanFord...
	public boolean runTheAlgorithm() {

		boolean change = false;

		for (Integer neighbor : this.getNeighbours()) {

			Map<Integer, Double> neighbourDistanceVector = neighbourDistanceVectors.get(neighbor);
			for (Integer nodeId : distanceVector.keySet()) {

				if (!this.id.equals(nodeId)) {
					// cost of neighbour to N
					Double neighbourToTargetTravelCost = neighbourDistanceVector.get(nodeId);
					// link distance to specified neighbor
					Double neighbourTravelCost = this.neighbours.get(neighbor);
					
					if (neighbourToTargetTravelCost != Double.MAX_VALUE) {

						double targetTravellingCost = this.getCostToNode(nodeId);
						Double updatedTargetTravellingCost = neighbourToTargetTravelCost + neighbourTravelCost;

						if (updatedTargetTravellingCost < targetTravellingCost) {
							// if the new cost is shorter, then update the distance vector.
							this.updateDistanceVector(nodeId, updatedTargetTravellingCost);
							this.updateFirstHop(nodeId, neighbor);
							change = true;
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

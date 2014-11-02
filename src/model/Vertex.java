package model;
public class Vertex {

	private int id;
	private Vertex[] neighbours;

	public Vertex(int id) {
		this.id = id;
	}

	public Vertex(int id, int... neigbourIds) {
		this.id = id;

		neighbours = new Vertex[neigbourIds.length];
		for (int i = 0; i < neighbours.length; i++) {
			neighbours[i] = new Vertex(neigbourIds[i]);
		}
	}

	public int getId() {
		return id;
	}
	
	public Vertex[] getNeighbours() {
		return neighbours;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Vertex)
			return ((Vertex)obj).getId() == getId();
		return false;
	}

	@Override
	public int hashCode() {
		return Integer.valueOf(id).hashCode();
	}

}
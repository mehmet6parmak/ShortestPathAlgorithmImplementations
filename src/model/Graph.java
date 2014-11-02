package model;


public class Graph {

	private Vertex[] vertexes;
	private Edge[] edges;

	public Vertex[] getVertexes() {
		return vertexes;
	}

	public void setVertexes(Vertex[] vertexes) {
		this.vertexes = vertexes;
	}

	public Edge[] getEdges() {
		return edges;
	}

	public void setEdges(Edge[] edges) {
		this.edges = edges;
	}

	public Graph(Vertex[] vertexes, Edge[] edges) {
		super();
		this.vertexes = vertexes;
		this.edges = edges;
	}

}

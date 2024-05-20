package com.evan.algorithms.graphs;

import com.evan.algorithms.datastructures.Bag;

// 无向图
public class Graph {
    private static final String NEWLINE = System.lineSeparator();

    private final int vertexCount;
    private int edgeCount;
    private final Bag<Integer>[] adj;

    public Graph(int vertexCount) {
        if (vertexCount < 0) throw new IllegalArgumentException("Number of vertices must be non-negative");
        this.vertexCount = vertexCount;
        this.edgeCount = 0;
        adj = new Bag[vertexCount];
        for (int v = 0; v < vertexCount; v++) {
            adj[v] = new Bag<>();
        }
    }

    public Graph(Graph g) {
        this.vertexCount = g.getVertexCount();
        this.edgeCount = g.getEdgeCount();
        if (vertexCount < 0) throw new IllegalArgumentException("Number of vertices must be non-negative");

        adj = (Bag<Integer>[]) new Bag[vertexCount];
        for (int v = 0; v < vertexCount; v++) {
            adj[v] = new Bag<>();
        }

        for (int v = 0; v < g.getVertexCount(); v++) {
            for (Integer w : g.adj[v]) {
                adj[v].add(w);
            }
        }
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getEdgeCount() {
        return edgeCount;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= vertexCount)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (vertexCount -1));
    }

    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        edgeCount++;
        adj[v].add(w);
        adj[w].add(v);
    }

    public Iterable<Integer> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    public int degree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(vertexCount).append(" vertices, ")
                .append(edgeCount).append(" edges ").append(NEWLINE);
        for (int v = 0; v < vertexCount; v++) {
            s.append(v).append(": ");
            for (int w : adj[v]) {
                s.append(w).append(" ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    public static void main(String[] args) {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(2, 4);
        System.out.println(graph);
        Graph graph2 = new Graph(graph);
        System.out.println(graph2);
    }
}


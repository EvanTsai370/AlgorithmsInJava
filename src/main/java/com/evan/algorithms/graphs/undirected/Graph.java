package com.evan.algorithms.graphs.undirected;

import com.evan.algorithms.datastructures.Bag;
import com.evan.algorithms.io.In;

import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

// 无向图
public class Graph {
    private static final String NEWLINE = System.lineSeparator();

    private final int vertexCount;
    private int edgeCount;
    // 数组不能动态地添加或删除元素，也就不支持添加和删除顶点
    // 为了支持添加和删除顶点、禁止平行边、删除边，可以使用符号表和set
    // 如Map<String, Set<String>> adjacencySet;
    private final Bag<Integer>[] adj;

    public Graph(int vertexCount) {
        if (vertexCount < 0) throw new IllegalArgumentException("Number of vertices must be non-negative");
        this.vertexCount = vertexCount;
        this.edgeCount = 0;
        adj = (Bag<Integer>[]) new Bag[vertexCount];
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
            Deque<Integer> reverse = new ArrayDeque<>();
            for (int w : g.adj[v]) {
                reverse.push(w);
            }
            for (int w : reverse) {
                adj[v].add(w);
            }
        }
    }

    public Graph(In in) {
        if (in == null) throw new IllegalArgumentException("argument is null");
        try {
            this.vertexCount = in.readInt();
            if (vertexCount < 0)
                throw new IllegalArgumentException("number of vertices in a Graph must be non-negative");
            adj = (Bag<Integer>[]) new Bag[vertexCount];
            for (int v = 0; v < vertexCount; v++) {
                adj[v] = new Bag<>();
            }
            int E = in.readInt();
            if (E < 0) throw new IllegalArgumentException("number of edges in a Graph must be non-negative");
            for (int i = 0; i < E; i++) {
                int v = in.readInt();
                int w = in.readInt();
                validateVertex(v);
                validateVertex(w);
                addEdge(v, w);
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in Graph constructor", e);
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
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (vertexCount - 1));
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
        URL url = Graph.class.getResource("/graph/tinyG.txt");
        Graph g = new Graph(new In(url));
        System.out.println(g);
        Graph g2 = new Graph(g);
        System.out.println(g2);
    }
}


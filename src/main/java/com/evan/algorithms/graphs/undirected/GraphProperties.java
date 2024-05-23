package com.evan.algorithms.graphs.undirected;

import com.evan.algorithms.datastructures.Queue;
import com.evan.algorithms.io.StdOut;

/**
 * The eccentricity of a vertex v is the length of the shortest path from that vertex to the furthest vertex from v.
 * The diameter of a graph is the maximum eccentricity of any vertex.
 * The radius of a graph is the smallest eccentricity of any vertex.
 * A center is a vertex whose eccentricity is the radius.
 * The girth of a graph is the length of its shortest cycle. If a graph is acyclic, then its girth is infinite;
 */
public class GraphProperties {
    private final int[] eccentricities;
    private int diameter;
    private int radius;
    private int center;
    private int girth = Integer.MAX_VALUE;

    public GraphProperties(Graph g) {
        eccentricities = new int[g.getVertexCount()];
        CC cc = new CC(g);
        if (cc.count() != 1)
            throw new IllegalArgumentException("graph is not connected");
        getProperties(g);
        computeGirth(g);
    }

    private void getProperties(Graph g) {
        diameter = 0;
        radius = Integer.MAX_VALUE;
        center = 0;

        for (int v = 0; v < g.getVertexCount(); v++) {
            BreadthFirstPaths bfp = new BreadthFirstPaths(g, v);

            for (int otherVertex = 0; otherVertex < g.getVertexCount(); otherVertex++) {
                if (otherVertex == v)
                    continue;
                eccentricities[v] = Math.max(eccentricities[v], bfp.distTo(otherVertex));
            }

            if (eccentricities[v] > diameter) {
                diameter = eccentricities[v];
            }
            if (eccentricities[v] < radius) {
                radius = eccentricities[v];
                center = v;
            }
        }
    }

    private void computeGirth(Graph g) {
        for (int v = 0; v < g.getVertexCount(); v++) {
            int shortestCycle = bfsToGetShortestCycle(g, v);
            girth = Math.min(girth, shortestCycle);
        }
    }

    private int bfsToGetShortestCycle(Graph g, int u) {
        int shortestCycle = Integer.MAX_VALUE;
        boolean[] marked = new boolean[g.getVertexCount()];
        int[] distTo = new int[g.getVertexCount()];
        int[] edgeTo = new int[g.getVertexCount()];

        for (int v = 0; v < g.getVertexCount(); v++) {
            edgeTo[v] = Integer.MAX_VALUE;
        }
        edgeTo[u] = 0;

        Queue<Integer> q = new Queue<>();
        q.enqueue(u);
        marked[u] = true;

        while (!q.isEmpty()) {
            Integer v = q.dequeue();
            for (Integer w : g.adj(v)) {
                if (!marked[w]) {
                    marked[w] = true;
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    q.enqueue(w);
                } else if (w != edgeTo[v]) {
                    int cycleLength = distTo[v] + distTo[w] + 1;
                    shortestCycle = Math.min(cycleLength, shortestCycle);
                }
            }
        }
        return shortestCycle;
    }

    public int eccentricity(int v) {
        return eccentricities[v];
    }

    public int diameter() {
        return diameter;
    }

    public int radius() {
        return radius;
    }

    public int center() {
        return center;
    }

    public int girth() {
        return girth;
    }

    public static void main(String[] args) {
        // Graph
        // 0 -- 1 -- 2 -- 3 -- 4 -- 5 -- 6 -- 7 -- 8 -- 9 -- 10

        Graph graph = new Graph(11);
        graph.addEdge(0 ,1);
        graph.addEdge(1 ,2);
        graph.addEdge(2 ,3);
        graph.addEdge(3 ,4);
        graph.addEdge(4 ,5);
        graph.addEdge(5 ,6);
        graph.addEdge(6 ,7);
        graph.addEdge(7 ,8);
        graph.addEdge(8 ,9);
        graph.addEdge(9 ,10);

        GraphProperties graphProperties = new GraphProperties(graph);
        StdOut.println("Diameter: " + graphProperties.diameter() + " Expected: 10");
        StdOut.println("Radius: " + graphProperties.radius() + " Expected: 5");
        StdOut.println("Center: " + graphProperties.center() + " Expected: 5");

        Graph g2 = new Graph(5);
        g2.addEdge(0 ,1);
        g2.addEdge(0 ,2);
        g2.addEdge(1 ,3);
        g2.addEdge(2 ,3);
        g2.addEdge(2 ,4);
        g2.addEdge(3 ,4);
        GraphProperties gp2 = new GraphProperties(g2);
        System.out.println("girth is " + gp2.girth());
    }
}

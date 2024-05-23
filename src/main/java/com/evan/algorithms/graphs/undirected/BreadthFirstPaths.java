package com.evan.algorithms.graphs.undirected;

import com.evan.algorithms.datastructures.Queue;
import com.evan.algorithms.datastructures.Stack;
import com.evan.algorithms.io.In;

import java.net.URL;
import java.util.List;

public class BreadthFirstPaths {
    private static final Integer INF = Integer.MAX_VALUE;
    private final boolean[] marked;
    private final int[] edgeTo; // edgeTo[v] = s(sources)-v的路径的最后一条边
    private final int[] distTo; // distTo[v] = s(sources)-v的最短路径长度

    public BreadthFirstPaths(Graph g, int s) {
        marked = new boolean[g.getVertexCount()];
        edgeTo = new int[g.getVertexCount()];
        distTo = new int[g.getVertexCount()];
        validateVertex(s);
        bfs(g, s);
    }

    public BreadthFirstPaths(Graph g, Iterable<Integer> sources) {
        marked = new boolean[g.getVertexCount()];
        edgeTo = new int[g.getVertexCount()];
        distTo = new int[g.getVertexCount()];
        validateVertices(sources);
        bfs(g, sources);
    }

    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int vertexCount = 0;
        for (Integer v : vertices) {
            vertexCount++;
            if (v == null) {
                throw new IllegalArgumentException("vertex is null");
            }
            validateVertex(v);
        }
        if (vertexCount == 0) {
            throw new IllegalArgumentException("zero vertices");
        }
    }

    private void bfs(Graph g, int s) {
        for (int v = 0; v < g.getVertexCount(); v++) {
            distTo[v] = INF;
        }
        distTo[s] = 0;
        marked[s] = true;
        Queue<Integer> q = new Queue<>();
        q.enqueue(s);

        while (!q.isEmpty()) {
            Integer v = q.dequeue();
            for (int w : g.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }
    }

    // 多个起点
    private void bfs(Graph g, Iterable<Integer> sources) {
        for (int v = 0; v < g.getVertexCount(); v++) {
            distTo[v] = INF;
        }
        Queue<Integer> q = new Queue<>();
        for (Integer s : sources) {
            marked[s] = true;
            distTo[s] = 0;
            q.enqueue(s);
        }

        while (!q.isEmpty()) {
            Integer v = q.dequeue();
            for (int w : g.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= marked.length) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (marked.length - 1));
        }
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return marked[v];
    }

    public int distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }

    public Iterable<Integer> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) {
            return null;
        }
        Stack<Integer> path = new Stack<>();
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x]) {
            path.push(x);
        }
        path.push(x);
        return path;
    }

    public static void main(String[] args) {
        URL url = BreadthFirstPaths.class.getResource("/graph/tinyCG.txt");
        Graph g = new Graph(new In(url));
        testOneSource(g);
        System.out.println();
        testMultipleSources(g);
    }

    private static void testOneSource(Graph g) {
        int s = 0;
        BreadthFirstPaths bfs = new BreadthFirstPaths(g, s);

        for (int v = 0; v < g.getVertexCount(); v++) {
            if (bfs.hasPathTo(v)) {
                System.out.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
                for (int x : bfs.pathTo(v)) {
                    if (x == s) System.out.print(x);
                    else System.out.print("-" + x);
                }
                System.out.println();
            } else {
                System.out.printf("%d to %d (-):  not connected\n", s, v);
            }
        }
    }

    private static void testMultipleSources(Graph g) {
        List<Integer> sources = List.of(2, 3);
        BreadthFirstPaths bfs2 = new BreadthFirstPaths(g, sources);

        for (int v = 0; v < g.getVertexCount(); v++) {
            if (bfs2.hasPathTo(v)) {
                System.out.printf(sources + " to %d (%d):  ", v, bfs2.distTo(v));
                for (int x : bfs2.pathTo(v)) {
                    if (sources.contains(x)) System.out.print(x);
                    else System.out.print("-" + x);
                }
                System.out.println();
            } else {
                System.out.printf(sources + " to %d (-):  not connected\n", v);
            }
        }
    }
}

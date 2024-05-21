package com.evan.algorithms.graphs;

import java.util.ArrayDeque;
import java.util.Deque;

public class DepthFirstPaths {
    private final boolean[] marked;
    private final int[] edgeTo; // edgeTo[v] = s-v路径的最后一条边
    private final int s; // 起点

    public DepthFirstPaths(Graph g, int s) {
        marked = new boolean[g.getVertexCount()];
        edgeTo = new int[g.getVertexCount()];
        this.s = s;
        validateVertex(s);
        dfs(g, s);
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= marked.length) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (marked.length - 1));
        }
    }

    private void dfs(Graph g, int v) {
        marked[v] = true;
        for (Integer w : g.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(g, w);
            }
        }
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return marked[v];
    }

    public Iterable<Integer> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) {
            return null;
        }
        Deque<Integer> path = new ArrayDeque<>();
        for (int x = v; x != s; x = edgeTo[x]) {
            path.push(x);
        }
        path.push(s);
        return path;
    }

    public static void main(String[] args) {
        Graph g = GraphUtils.tinyCG();
        int s = 0;
        DepthFirstPaths dfs = new DepthFirstPaths(g, s);

        for (int v = 0; v < g.getVertexCount(); v++) {
            if (dfs.hasPathTo(v)) {
                System.out.printf("%d to %d:  ", s, v);
                for (int x : dfs.pathTo(v)) {
                    if (x == s) System.out.print(x);
                    else System.out.printf("-" + x);
                }
                System.out.println();
            } else {
                System.out.printf("%d to %d:  not connected\n", s, v);
            }

        }
    }
}

package com.evan.algorithms.graphs;

import java.util.ArrayList;
import java.util.List;

public class CC {
    private final boolean[] marked;
    private final int[] id;
    private final int[] size;
    private int count;

    public CC(Graph g) {
        marked = new boolean[g.getVertexCount()];
        id = new int[g.getVertexCount()];
        size = new int[g.getVertexCount()];
        for (int v = 0; v < g.getVertexCount(); v++) {
            if (!marked[v]) {
                dfs(g, v);
                count++;
            }
        }
    }

    private void dfs(Graph g, int v) {
        marked[v] = true;
        id[v] = count;
        size[count]++;
        for (int w : g.adj(v)) {
            if (!marked[w]) {
                dfs(g, w);
            }
        }
    }

    public int id(int v) {
        validateVertex(v);
        return id[v];
    }

    public int size(int v) {
        validateVertex(v);
        return size[id[v]];
    }

    public int count() {
        return count;
    }

    public boolean connected(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        return id[v] == id[w];
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= marked.length) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (marked.length - 1));
        }
    }

    public static void main(String[] args) {
        Graph g = GraphUtils.tinyG();
        CC cc = new CC(g);

        // number of connected components
        int m = cc.count();
        System.out.println(m + " components");

        // compute list of vertices in each connected component
        List<List<Integer>> components = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            components.add(new ArrayList<>());
        }
        for (int v = 0; v < g.getVertexCount(); v++) {
            components.get(cc.id(v)).add(v);
        }

        // print results
        for (int i = 0; i < m; i++) {
            for (int v : components.get(i)) {
                System.out.print(v + " ");
            }
            System.out.println();
        }
    }
}

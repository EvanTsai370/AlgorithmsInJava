package com.evan.algorithms.graphs.undirected;

import com.evan.algorithms.io.In;

import java.net.URL;

public class DepthFirstSearch {
    private final boolean[] marked;
    private int count; // 与起点相连的顶点数量

    public DepthFirstSearch(Graph g, int s) {
        marked = new boolean[g.getVertexCount()];
        validateVertex(s);
        System.out.print("visit order： ");
        dfs(g, s);
    }

    private void dfs(Graph g, int v) {
        count++;
        marked[v] = true;
        System.out.print(v + " ");
        for (int w : g.adj(v)) {
            if (!marked[w]) {
                dfs(g, w);
            }
        }
    }

    public boolean marked(int v) {
        validateVertex(v);
        return marked[v];
    }

    public int count() {
        return count;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= marked.length) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (marked.length - 1));
        }
    }

    public static void main(String[] args) {
        URL url = DepthFirstSearch.class.getResource("/graph/tinyG.txt");
        Graph g = new Graph(new In(url));
        DepthFirstSearch search = new DepthFirstSearch(g, 0);

        System.out.println();
        for (int v = 0; v < g.getVertexCount(); v++) {
            if (search.marked(v))
                System.out.print(v + " ");
        }

        System.out.println();
        if (search.count() != g.getVertexCount())
            System.out.println("Not connected");
        else
            System.out.println("connected");
    }
}

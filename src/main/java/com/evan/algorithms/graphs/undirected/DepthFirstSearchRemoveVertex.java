package com.evan.algorithms.graphs.undirected;

import com.evan.algorithms.io.In;

import java.net.URL;

/**
 * 每一个连通图都存在一个顶点，其移除（包括所有相邻的边）后不会使图变得不连通，找出这个顶点
 */
public class DepthFirstSearchRemoveVertex {
    private final boolean[] marked;
    private int vertexThatCanBeRemoved;

    public DepthFirstSearchRemoveVertex(Graph g, int s) {
        marked = new boolean[g.getVertexCount()];
        dfs(g, s);
    }

    private void dfs(Graph g, int v) {
        marked[v] = true;
        boolean allNeighboursMarked = true;

        for (int w : g.adj(v)) {
            if (!marked[w]) {
                allNeighboursMarked = false;
                dfs(g, w);
            }
        }

        if (allNeighboursMarked) {
            vertexThatCanBeRemoved = v;
        }
    }

    public int vertexThatCanBeRemoved() {
        return vertexThatCanBeRemoved;
    }

    public static void main(String[] args) {
        URL url = DepthFirstSearchRemoveVertex.class.getResource("/graph/tinyCG.txt");
        Graph g = new Graph(new In(url));
        DepthFirstSearchRemoveVertex depthFirstSearchRemoveVertex = new DepthFirstSearchRemoveVertex(g, 0);
        System.out.println("vertexThatCanBeRemoved = " + depthFirstSearchRemoveVertex.vertexThatCanBeRemoved());
    }
}

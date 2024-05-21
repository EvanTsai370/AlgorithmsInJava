package com.evan.algorithms.graphs;

import java.util.ArrayDeque;
import java.util.Deque;

public class Cycle {
    private boolean[] marked;
    private int[] edgeTo;
    private Deque<Integer> cycle;

    public Cycle(Graph g) {
        // need special case to identify parallel edge as a cycle
        if (hasParallelEdges(g)) return;

        // don't need special case to identify self-loop as a cycle
        // if (hasSelfLoop(g)) return;

        marked = new boolean[g.getVertexCount()];
        edgeTo = new int[g.getVertexCount()];
        for (int v = 0; v < g.getVertexCount(); v++) {
            if (!marked[v]) {
                dfs(g, -1, v);
            }
        }
    }

    private boolean hasParallelEdges(Graph g) {
        marked = new boolean[g.getVertexCount()];

        for (int v = 0; v < g.getVertexCount(); v++) {
            for (Integer w : g.adj(v)) {
                if (marked[w]) {
                    cycle = new ArrayDeque<>();
                    cycle.push(v);
                    cycle.push(w);
                    cycle.push(v);
                    return true;
                }
                marked[w] = true;
            }

            for (Integer w : g.adj(v)) {
                marked[w] = false;
            }
        }
        return false;
    }

    private boolean hasSelfLoop(Graph g) {
        for (int v = 0; v < g.getVertexCount(); v++) {
            for (Integer w : g.adj(v)) {
                cycle = new ArrayDeque<>();
                cycle.push(v);
                cycle.push(v);
                if (w == v) return true;
            }
        }
        return false;
    }

    public boolean hasCycle() {
        return cycle != null;
    }

    public Iterable<Integer> cycle() {
        return cycle;
    }

    private void dfs(Graph g, int u, int v) {
        marked[v] = true;
        for (Integer w : g.adj(v)) {
            // 已找到环
            if (cycle != null) return;

            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(g, v, w);
            } else if (w != u) { // 检查环，但忽略指向 v 的边的反向(忽略v->u)
                cycle = new ArrayDeque<>();
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
                cycle.push(v);
            }
        }
    }

    public static void main(String[] args) {
        Graph g = GraphUtils.tinyG();
        Cycle finder = new Cycle(g);
        if (finder.hasCycle()) {
            for (int v : finder.cycle()) {
                System.out.print(v + " ");
            }
            System.out.println();
        }
        else {
            System.out.println("Graph is acyclic");
        }
    }
}

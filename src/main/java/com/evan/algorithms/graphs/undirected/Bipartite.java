package com.evan.algorithms.graphs.undirected;

import com.evan.algorithms.datastructures.Stack;
import com.evan.algorithms.io.In;

import java.net.URL;

/**
 * 判断无向图是否为二分图，如果不是，则无向图有一个长度为奇数的环，
 * 这里使用递归的dfs判断，{@link BipartiteX}使用非递归的bfs判断
 */
public class Bipartite {
    private final boolean[] marked;
    private final int[] edgeTo;
    private final boolean[] color;
    private boolean isBipartite;
    private Stack<Integer> cycle;

    public Bipartite(Graph g) {
        marked = new boolean[g.getVertexCount()];
        edgeTo = new int[g.getVertexCount()];
        color = new boolean[g.getVertexCount()];
        isBipartite = true;

        for (int v = 0; v < g.getVertexCount(); v++) {
            if (!marked[v]) {
                dfs(g, v);
            }
        }

        assert check(g);
    }

    private void dfs(Graph g, int v) {
        marked[v] = true;

        for (Integer w : g.adj(v)) {
            // 已找到奇数长度的环
            if (cycle != null) return;

            if (!marked[w]) {
                edgeTo[w] = v;
                color[w] = !color[v];
                dfs(g, w);
            } else if (color[w] == color[v]) {
                isBipartite = false;
                cycle = new Stack<>();
                cycle.push(w);
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
            }
        }
    }

    private boolean check(Graph g) {
        if (isBipartite) {
            for (int v = 0; v < g.getVertexCount(); v++) {
                for (Integer w : g.adj(v)) {
                    if (color[w] == color[v]) {
                        System.err.printf("edge %d-%d with %d and %d in same side of bipartition\n", v, w, v, w);
                        return false;
                    }
                }
            }
        } else {
            // 检查环
            int first = -1, last = -1;
            for (Integer v : oddCycle()) {
                if (first == -1) {
                    first = v;
                }
                last = v;
            }
            if (first != last) {
                System.err.printf("cycle begins with %d and ends with %d\n", first, last);
                return false;
            }
        }
        return true;
    }

    public boolean isBipartite() {
        return isBipartite;
    }

    public boolean color(int v) {
        validateVertex(v);
        if (!isBipartite) {
            throw new UnsupportedOperationException("graph is not bipartite");
        }
        return color[v];
    }

    public Iterable<Integer> oddCycle() {
        return cycle;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= marked.length) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (marked.length - 1));
        }
    }

    public static void main(String[] args) {
        URL url = Bipartite.class.getResource("/graph/tinyG.txt");
        Graph g = new Graph(new In(url));
        testBipartite(g);

        System.out.println();

        Graph g2 = new Graph(4);
        g2.addEdge(0, 1);
        g2.addEdge(0, 2);
        g2.addEdge(1, 3);
        g2.addEdge(2, 3);
        testBipartite(g2);
    }

    private static void testBipartite(Graph g) {
        Bipartite bipartite = new Bipartite(g);
        if (bipartite.isBipartite()) {
            System.out.println("graph is bipartite");
        } else {
            for (Integer v : bipartite.oddCycle()) {
                System.out.print(v + " ");
            }
        }
    }

}

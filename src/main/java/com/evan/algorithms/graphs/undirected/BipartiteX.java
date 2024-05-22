package com.evan.algorithms.graphs.undirected;

import com.evan.algorithms.datastructures.Queue;
import com.evan.algorithms.datastructures.Stack;
import com.evan.algorithms.io.In;

import java.net.URL;

/**
 * 使用非递归的bfs判断无向图是否为二分图
 */
public class BipartiteX {
    private static final boolean WHITE = true;
    private static final boolean FALSE = false;

    private boolean isBipartite;
    private boolean[] color;
    private boolean[] marked;
    private int[] edgeTo;
    private Queue<Integer> cycle;

    public BipartiteX(Graph g) {
        isBipartite = true;
        color = new boolean[g.getVertexCount()];
        marked = new boolean[g.getVertexCount()];
        edgeTo = new int[g.getVertexCount()];

        for (int v = 0; v < g.getVertexCount() && isBipartite; v++) {
            if (!marked[v]) {
                bfs(g, v);
            }
        }

        assert check(g);
    }

    private void bfs(Graph g, int s) {
        Queue<Integer> q = new Queue<>();
        color[s] = WHITE;
        marked[s] = true;
        q.enqueue(s);

        while (!q.isEmpty()) {
            Integer v = q.dequeue();
            for (Integer w : g.adj(v)) {
                if (!marked[w]) {
                    marked[w] = true;
                    color[w] = !color[v];
                    edgeTo[w] = v;
                    q.enqueue(w);
                } else if (color[w] == color[v]) {
                    isBipartite = false;

                    // w已被标记，v与w相邻，则w与v位于同一层或者w位于v上层
                    // 由于v与w同色，则v与w位于同一层，distTo[v] == distTo[w]
                    // 设x为v与w最近的公共祖先，Path(x-v) == Path(x-w)
                    // cycle = Path(x-v) + Path(x-w) + Edge(v-w)，长度为奇数
                    cycle = new Queue<>();
                    Stack<Integer> stack = new Stack<>();
                    int x = v, y = w;
                    while (x != y) {
                        // stack 记录v-x
                        stack.push(x);
                        // cycle 记录w-x
                        cycle.enqueue(y);
                        x = edgeTo[x];
                        y = edgeTo[y];
                    }
                    stack.push(x);
                    while (!stack.isEmpty()) {
                        cycle.enqueue(stack.pop());
                    }
                    cycle.enqueue(w);
                    return;
                }
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
            throw new UnsupportedOperationException("Graph is not bipartite");
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
        URL url = BipartiteX.class.getResource("/graph/tinyG.txt");
        Graph g = new Graph(new In(url));
        testBipartiteX(g);

        System.out.println();

        Graph g2 = new Graph(4);
        g2.addEdge(0, 1);
        g2.addEdge(0, 2);
        g2.addEdge(1, 3);
        g2.addEdge(2, 3);
        testBipartiteX(g2);
    }

    private static void testBipartiteX(Graph g) {
        BipartiteX bipartiteX = new BipartiteX(g);
        if (bipartiteX.isBipartite()) {
            System.out.println("graph is bipartite");
        } else {
            for (Integer v : bipartiteX.oddCycle()) {
                System.out.print(v + " ");
            }
        }
    }
}

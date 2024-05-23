package com.evan.algorithms.graphs.undirected;

import com.evan.algorithms.datastructures.Stack;
import com.evan.algorithms.io.In;

import java.net.URL;
import java.util.Iterator;

public class DepthFirstSearchIterative {
    private final boolean[] marked;
    private int count;

    public DepthFirstSearchIterative(Graph g, int s) {
        marked = new boolean[g.getVertexCount()];
        dfs(g, s);
    }

    private void dfs(Graph g, int s) {
        Stack<Integer> stack = new Stack<>();
        stack.push(s);
        marked[s] = true;
        count++;
        System.out.print("visit order: " + s + " ");

        Iterator<Integer>[] adjacentIterators = (Iterator<Integer>[]) new Iterator[g.getVertexCount()];

        for (int v = 0; v < adjacentIterators.length; v++) {
            if (g.adj(v) != null) {
                adjacentIterators[v] = g.adj(v).iterator();
            }
        }

        while (!stack.isEmpty()) {
            Integer v = stack.peek();
            if (adjacentIterators[v].hasNext()) {
                Integer w = adjacentIterators[v].next();
                if (!marked[w]) {
                    count++;
                    stack.push(w);
                    marked[w] = true;
                    System.out.print(w + " ");
                }
            } else {
                stack.pop();
            }
        }
    }

    public int count() {
        return count;
    }

    public boolean marked(int v) {
        return marked[v];
    }

    public static void main(String[] args) {
        URL url = DepthFirstSearchIterative.class.getResource("/graph/tinyG.txt");
        Graph g = new Graph(new In(url));
        DepthFirstSearchIterative search = new DepthFirstSearchIterative(g, 0);

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

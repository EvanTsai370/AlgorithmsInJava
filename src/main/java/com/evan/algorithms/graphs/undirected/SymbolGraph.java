package com.evan.algorithms.graphs.undirected;

import com.evan.algorithms.io.In;
import com.evan.algorithms.io.StdIn;
import com.evan.algorithms.io.StdOut;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 符号表，对{@link Graph}的增强，支持顶点名称为任意字符串，提供了顶点名称与索引之间的映射
 */
public class SymbolGraph {
    private final Map<String, Integer> st; // string -> index
    private final String[] keys; // index -> string
    private final Graph g;

    // 使用url方便读取resource目录下的文件
    public SymbolGraph(URL url, String delimiter) {
        st = new HashMap<>();

        In in = new In(url);
        while (!in.isEmpty()) {
            String[] a = in.readLine().split(delimiter);
            for (String s : a) {
                if (!st.containsKey(s)) {
                    st.put(s, st.size());
                }
            }
        }

        keys = new String[st.size()];
        for (String name : st.keySet()) {
            keys[st.get(name)] = name;
        }

        // 构建图
        g = new Graph(st.size());
        in = new In(url);
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(delimiter);
            Integer v = st.get(a[0]);
            for (int i = 1; i < a.length; i++) {
                Integer w = st.get(a[i]);
                g.addEdge(v, w);
            }
        }
    }

    public boolean contains(String s) {
        return st.containsKey(s);
    }

    public int indexOf(String s) {
        return st.get(s);
    }

    public String nameOf(int v) {
        validateVertex(v);
        return keys[v];
    }

    public Graph graph() {
        return g;
    }

    private void validateVertex(int v) {
        int vertexCount = g.getVertexCount();
        if (v < 0 || v >= vertexCount) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (vertexCount - 1));
        }
    }

    public static void main(String[] args) {
        URL url = SymbolGraph.class.getResource("/graph/routes.txt");
        SymbolGraph sg = new SymbolGraph(url, " ");
        Graph graph = sg.graph();
        while (StdIn.hasNextLine()) {
            String source = StdIn.readLine();
            if (sg.contains(source)) {
                int s = sg.indexOf(source);
                for (int v : graph.adj(s)) {
                    StdOut.println("   " + sg.nameOf(v));
                }
            } else {
                StdOut.println("input not contain '" + source + "'");
            }
        }
    }
}

package com.evan.algorithms.datastructures;

import java.util.Iterator;
import java.util.NoSuchElementException;

// 支持添加和以任意顺序迭代元素的容器
public class Bag<E> implements Iterable<E> {
    private Node<E> first;
    private int num;

    private static class Node<E> {
        private E item;
        private Node<E> next;
    }

    public Bag() {
        first = null;
        num = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return num;
    }

    public void add(E item) {
        Node<E> oldFirst = first;
        first = new Node<>();
        first.item = item;
        first.next = oldFirst;
        num++;
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedIterator<>(first);
    }

    private static class LinkedIterator<E> implements Iterator<E> {
        private Node<E> current;

        public LinkedIterator(Node<E> first) {
            current = first;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            E item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Bag<String> bag = new Bag<>();
        bag.add("a");
        bag.add("b");
        bag.add("c");
        bag.add("d");
        bag.add("e");
        bag.add("f");
        System.out.println("size of bag: " + bag.size());
        for (String s : bag) {
            System.out.println(s);
        }
    }
}

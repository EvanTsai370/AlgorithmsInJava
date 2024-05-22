package com.evan.algorithms.datastructures;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Queue<E> implements Iterable<E> {
    private Node<E> first;
    private Node<E> last;
    private int num;

    public Queue() {
        first = null;
        last = null;
        num = 0;
    }

    private static class Node<E> {
        private E item;
        private Node<E> next;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return num;
    }

    public void enqueue(E item) {
        Node<E> oldLast = last;
        last = new Node<>();
        last.item = item;
        last.next = null;
        if (isEmpty())
            first = last;
        else
            oldLast.next = last;
        num++;
    }

    public E dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");
        E item = first.item;
        first = first.next;
        num--;
        return item;
    }

    public E peek() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");
        return first.item;
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedIterator<>(first);
    }

    private static class LinkedIterator<E> implements Iterator<E> {
        private Node<E> current;

        LinkedIterator(Node<E> first) {
            current = first;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            if (!hasNext())
                throw new IllegalArgumentException();
            current = current.next;
            return current.item;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (E item : this) {
            sb.append(item);
            sb.append(" ");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Queue<String> q = new Queue<>();
        try (InputStream is = Stack.class.getResourceAsStream("/datastructures/tobe.txt")) {
            if (is != null) {
                Scanner scanner = new Scanner(is, StandardCharsets.UTF_8);
                if (scanner.hasNextLine()) {
                    String s = scanner.nextLine();
                    String[] elements = s.split(" ");
                    for (String element : elements) {
                        if (!element.equals("-")) {
                           q.enqueue(element);
                        } else if (!q.isEmpty()) {
                            System.out.print(q.dequeue() + " ");
                        }
                    }
                    System.out.println("(" + q.size() + " left on queue)");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

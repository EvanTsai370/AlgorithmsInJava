package com.evan.algorithms.datastructures;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Stack<E> implements Iterable<E> {
    private Node<E> first;
    private int num;

    private static class Node<E> {
        private E item;
        private Node<E> next;
    }

    public Stack() {
        first = null;
        num = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return num;
    }

    public void push(E item) {
       Node<E> oldFirst = first;
       first = new Node<>();
       first.item = item;
       first.next = oldFirst;
       num++;
    }

    public E pop() {
        if (isEmpty()) throw new NoSuchElementException("stack underflow");
        E item = first.item;
        first = first.next;
        num--;
        return item;
    }

    public E peek() {
        if (isEmpty()) throw new NoSuchElementException("stack underflow");
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
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E item = current.item;
            current = current.next;
            return item;
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
        Stack<String> stack = new Stack<>();
        try (InputStream is = Stack.class.getResourceAsStream("/datastructures/tobe.txt")) {
            if (is != null) {
                Scanner scanner = new Scanner(is, StandardCharsets.UTF_8);
                if (scanner.hasNextLine()) {
                    String s = scanner.nextLine();
                    String[] elements = s.split(" ");
                    for (String element : elements) {
                        if (!element.equals("-")) {
                           stack.push(element);
                        } else if (!stack.isEmpty()) {
                            System.out.print(stack.pop() + " ");
                        }
                    }
                    System.out.println("(" + stack.size() + " left on stack)");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package edu.neu.ccs.prl.phosphor.internal.runtime.collection;

import java.util.NoSuchElementException;

public class Stack<E> {
    private Node<E> head = null;
    private int size = 0;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public E pop() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        E result = head.value;
        head = head.next;
        size--;
        return result;
    }

    public E peek() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        return head.value;
    }

    public void push(E element) {
        head = new Node<>(element, head);
        size++;
    }

    private static final class Node<E> {
        private final E value;
        private final Node<E> next;

        private Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }
}

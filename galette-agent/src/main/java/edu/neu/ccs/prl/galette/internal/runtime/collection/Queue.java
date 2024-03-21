package edu.neu.ccs.prl.galette.internal.runtime.collection;

import java.util.NoSuchElementException;

public class Queue<E> {
    private Node<E> head = null;
    private Node<E> tail = null;
    private int size = 0;

    public Queue() {}

    public Queue(Queue<? extends E> other) {
        for (Node<? extends E> current = other.head; current != null; current = current.next) {
            enqueue(current.value);
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public E dequeue() {
        if (head == null) {
            // Empty list
            throw new NoSuchElementException();
        } else if (head == tail) {
            // Singleton list
            tail = null;
        }
        E result = head.value;
        head = head.next;
        size--;
        return result;
    }

    public void enqueue(E element) {
        Node<E> node = new Node<>(element, null);
        size++;
        if (head == null) {
            // Empty list
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
    }

    private static final class Node<E> {
        private final E value;
        private Node<E> next;

        private Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }
}

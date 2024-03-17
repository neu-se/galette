package edu.neu.ccs.prl.galette.internal.runtime.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QueueTest {
    @Test
    void size() {
        Queue<Integer> queue = new Queue<>();
        Assertions.assertEquals(0, queue.size());
        int size = 7;
        for (int i = 0; i < size; i++) {
            Assertions.assertEquals(i, queue.size());
            queue.enqueue(i);
        }
        for (int i = 0; i < size; i++) {
            Assertions.assertEquals(size - i, queue.size());
            queue.dequeue();
        }
        Assertions.assertEquals(0, queue.size());
    }

    @Test
    public void enqueueDequeue() {
        Queue<Integer> queue = new Queue<>();
        int size = 9;
        for (int i = 0; i < size; i++) {
            queue.enqueue(i);
        }
        for (int i = 0; i < size; i++) {
            Assertions.assertEquals(i, queue.dequeue());
        }
    }

    @Test
    public void isEmptyNewQueue() {
        Queue<Integer> queue = new Queue<>();
        Assertions.assertTrue(queue.isEmpty());
    }

    @Test
    public void isEmptyEnqueueDequeue() {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(0);
        queue.dequeue();
        Assertions.assertTrue(queue.isEmpty());
    }
}

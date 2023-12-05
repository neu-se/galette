package edu.neu.ccs.prl.phosphor.internal.runtime.collection;

/**
 * An array-backed list implementation that does not use non-native code
 * from the Java Class Library unless an exception is thrown.
 * @param <E> the type of elements held by this list
 */
public final class SimpleList<E> {
    private int size = 0;
    private E[] elements;

    /**
     * Constructs a new list with an initial capacity of 10.
     */
    public SimpleList() {
        this(10);
    }

    /**
     * Constructs a new list with the specified initial capacity.
     *
     * @param capacity the initial capacity of this list
     * @throws IllegalArgumentException if capacity is less than 0
     */
    public SimpleList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException();
        }
        this.elements = createArray(capacity);
    }

    /**
     * Constructs a copy of the specified list.
     *
     * @param list the list to be copied
     * @throws NullPointerException if the specified list is {@code null}
     */
    public SimpleList(SimpleList<E> list) {
        this.size = list.size;
        this.elements = list.elements.clone();
    }

    /**
     * Constructs a new list containing the specified initial elements.
     *
     * @param elements the initial elements of the list
     * @throws NullPointerException if {@code elements == null}
     */
    public SimpleList(E[] elements) {
        this.size = elements.length;
        this.elements = elements.clone();
    }

    /**
     * Adds the specified element to the end of this list.
     *
     * @param element the element to add
     * @return {@code true}
     */
    public boolean add(E element) {
        if (size == elements.length) {
            grow(size + 1);
        }
        elements[size++] = element;
        return true;
    }

    /**
     * Removes all elements from this list.
     */
    public void clear() {
        if (size != 0) {
            for (int i = 0; i < size; i++) {
                elements[i] = null;
            }
            size = 0;
        }
    }

    /**
     * @return true if this list contains no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return the number of elements in this list
     */
    public int size() {
        return size;
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if {@code index < 0 || index >= size()}
     */
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return elements[index];
    }

    private void grow(int minCapacity) {
        int oldCapacity = elements.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        // Note: 0x7fffffff is Integer.MAX_VALUE
        if (newCapacity - (0x7fffffff - 8) > 0) {
            newCapacity = 0x7fffffff - 8;
        }
        E[] temp = elements;
        elements = createArray(newCapacity);
        //noinspection ManualArrayCopy
        for (int i = 0; i < temp.length; i++) {
            elements[i] = temp[i];
        }
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     *
     * @param index   index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range {@code index < 0 || index >= size()}
     */
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        E result = elements[index];
        elements[index] = element;
        return result;
    }

    public E[] toArray(E[] array) {
        if (array.length != size) {
            throw new IllegalArgumentException();
        }
        // Cannot load System class
        // noinspection ManualArrayCopy
        for (int i = 0; i < size; i++) {
            array[i] = elements[i];
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    private static <E> E[] createArray(int size) {
        return (E[]) new Object[size];
    }
}

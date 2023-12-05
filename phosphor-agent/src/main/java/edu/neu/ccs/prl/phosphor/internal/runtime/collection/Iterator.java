package edu.neu.ccs.prl.phosphor.internal.runtime.collection;

public interface Iterator<E> {
    E next();

    boolean hasNext();
}

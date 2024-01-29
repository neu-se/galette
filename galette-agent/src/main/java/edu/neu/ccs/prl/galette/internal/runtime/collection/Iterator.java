package edu.neu.ccs.prl.galette.internal.runtime.collection;

public interface Iterator<E> {
    E next();

    boolean hasNext();
}

package edu.neu.ccs.prl.galette.internal.runtime.collection;

public interface Function<X, Y> {
    Y apply(X x);
}

package edu.neu.ccs.prl.galette.internal.runtime;

@SuppressWarnings("unused")
public interface TaggedObject {
    int hashCode(TagFrame frame);

    boolean equals(Object obj, TagFrame frame);

    String toString(TagFrame frame);

    Object clone(TagFrame frame) throws CloneNotSupportedException;

    void finalize(TagFrame frame) throws Throwable;
}

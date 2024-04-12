package edu.neu.ccs.prl.galette.internal.runtime;

@SuppressWarnings("unused")
public interface TaggedObject {
    default int hashCode(TagFrame frame) {
        return hashCode();
    }

    default boolean equals(Object obj, TagFrame frame) {
        return equals(obj);
    }

    default String toString(TagFrame frame) {
        return toString();
    }

    default Object clone(TagFrame frame) throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    default void finalize(TagFrame frame) throws Throwable {}
}

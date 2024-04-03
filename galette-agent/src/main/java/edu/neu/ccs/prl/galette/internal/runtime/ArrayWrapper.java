package edu.neu.ccs.prl.galette.internal.runtime;

import java.io.Serializable;
import java.lang.reflect.Array;

public final class ArrayWrapper implements Serializable, TaggedObject {
    private static final long serialVersionUID = -5853646824573914847L;
    private Tag length = Tag.getEmptyTag();
    private final Tag[] elements;

    public ArrayWrapper(Object array) {
        int length = Array.getLength(array);
        elements = new Tag[length];
    }

    public int size() {
        return elements.length;
    }

    public Tag[] getElements() {
        return elements;
    }

    public void setElement(Tag element, int index) {
        elements[index] = element;
    }

    public Tag getElement(int index) {
        return elements[index];
    }

    public void setLength(Tag length) {
        this.length = length;
    }

    public Tag getLength() {
        return length;
    }

    @Override
    public int hashCode(TagFrame frame) {
        return hashCode();
    }

    @Override
    public boolean equals(Object obj, TagFrame frame) {
        return equals(obj);
    }

    @Override
    public String toString(TagFrame frame) {
        return toString();
    }

    @Override
    public Object clone(TagFrame frame) throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Override
    public void finalize(TagFrame frame) {}
}

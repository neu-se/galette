package edu.neu.ccs.prl.galette.internal.runtime;

import java.io.Serializable;
import java.lang.reflect.Array;

public final class ArrayWrapper implements Serializable {
    private static final long serialVersionUID = -5853646824573914847L;
    private Tag length = Tag.getEmptyTag();
    private final Tag[] elements;

    ArrayWrapper(Object array) {
        int length = Array.getLength(array);
        elements = new Tag[length];
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
}

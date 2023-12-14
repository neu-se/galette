package edu.neu.ccs.prl.phosphor.internal.runtime;

import java.lang.reflect.Array;

final class ArrayShadow {
    private Tag length = Tag.getEmptyTag();
    private final Tag[] elements;

    ArrayShadow(Object array) {
        int length = Array.getLength(array);
        elements = new Tag[length];
    }

    void setElement(Tag element, int index) {
        elements[index] = element;
    }

    Tag getElement(int index) {
        return elements[index];
    }

    void setLength(Tag length) {
        this.length = length;
    }

    Tag getLength() {
        return length;
    }
}

package edu.neu.ccs.prl.phosphor.internal.runtime;

public enum Handle {
    TAG_GET_EMPTY(Tag.class);

    private final Class<?> owner;

    Handle(Class<?> owner) {
        if (owner == null) {
            throw new NullPointerException();
        }
        this.owner = owner;
    }

    public Class<?> getOwner() {
        return owner;
    }
}

package edu.neu.ccs.prl.phosphor.internal.runtime;

import edu.neu.ccs.prl.phosphor.internal.transform.HandleRegistry;
import edu.neu.ccs.prl.phosphor.internal.transform.MaskRegistry;

public enum Handle {
    TAG_GET_EMPTY(Tag.class),
    HANDLE_REGISTRY_PUT(HandleRegistry.class),
    MASK_REGISTRY_PUT(MaskRegistry.class),
    FRAME_GET_INSTANCE(PhosphorFrame.class),
    FRAME_SET_CALLER_CLASS(PhosphorFrame.class);

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

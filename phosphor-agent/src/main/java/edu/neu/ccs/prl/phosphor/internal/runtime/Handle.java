package edu.neu.ccs.prl.phosphor.internal.runtime;

import edu.neu.ccs.prl.phosphor.internal.transform.HandleRegistry;
import edu.neu.ccs.prl.phosphor.internal.transform.MaskRegistry;
import org.objectweb.asm.MethodVisitor;

public enum Handle {
    TAG_GET_EMPTY(Tag.class),
    HANDLE_REGISTRY_PUT(HandleRegistry.class),
    MASK_REGISTRY_PUT(MaskRegistry.class),
    FRAME_GET_INSTANCE(PhosphorFrame.class),
    FRAME_CREATE_FOR_CALL(PhosphorFrame.class),
    FRAME_SET_CALLER(PhosphorFrame.class),
    FRAME_GET_CALLER(PhosphorFrame.class),
    FRAME_POP(PhosphorFrame.class),
    FRAME_PUSH(PhosphorFrame.class),
    FRAME_GET_RETURN_TAG(PhosphorFrame.class),
    FRAME_SET_RETURN_TAG(PhosphorFrame.class);

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

    public void accept(MethodVisitor mv) {
        HandleRegistry.getRecord(this).accept(mv);
    }
}

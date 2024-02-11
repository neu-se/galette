package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.transform.HandleRegistry;
import edu.neu.ccs.prl.galette.internal.transform.MaskRegistry;
import org.objectweb.asm.MethodVisitor;

public enum Handle {
    TAG_GET_EMPTY(Tag.class),
    TAG_UNION(Tag.class),
    HANDLE_REGISTRY_PUT(HandleRegistry.class),
    MASK_REGISTRY_PUT(MaskRegistry.class),
    FRAME_GET_INSTANCE(TagFrame.class),
    FRAME_CREATE_FOR_CALL(TagFrame.class),
    FRAME_SET_CALLER(TagFrame.class),
    FRAME_GET_CALLER(TagFrame.class),
    FRAME_POP(TagFrame.class),
    FRAME_PUSH(TagFrame.class),
    FRAME_GET_RETURN_TAG(TagFrame.class),
    FRAME_SET_RETURN_TAG(TagFrame.class),
    FRAME_SET_THROWN_TAG(TagFrame.class),
    ARRAY_TAG_STORE_GET_LENGTH_TAG(ArrayTagStore.class),
    ARRAY_TAG_STORE_SET_LENGTH_TAG(ArrayTagStore.class),
    ARRAY_TAG_STORE_GET_TAG(ArrayTagStore.class),
    ARRAY_TAG_STORE_SET_TAG(ArrayTagStore.class),
    ARRAY_TAG_STORE_SET_LENGTH_TAGS(ArrayTagStore.class),
    TAG_STORE_GET_TAG(TagStore.class),
    TAG_STORE_SET_TAG(TagStore.class);

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

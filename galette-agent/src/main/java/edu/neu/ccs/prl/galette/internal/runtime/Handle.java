package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.runtime.frame.FrameAdjuster;
import edu.neu.ccs.prl.galette.internal.runtime.frame.IndirectTagFrameStore;
import edu.neu.ccs.prl.galette.internal.runtime.mask.ProcessedTagFrame;
import edu.neu.ccs.prl.galette.internal.transform.HandleRegistry;
import edu.neu.ccs.prl.galette.internal.transform.MaskRegistry;
import edu.neu.ccs.prl.galette.internal.transform.MethodRecord;
import org.objectweb.asm.MethodVisitor;

public enum Handle {
    TAG_GET_EMPTY(Tag.class),
    TAG_UNION(Tag.class),
    HANDLE_REGISTRY_PUT(HandleRegistry.class),
    MASK_REGISTRY_PUT(MaskRegistry.class),
    FRAME_CREATE(TagFrame.class),
    FRAME_SET_CALLER(TagFrame.class),
    FRAME_GET_CALLER(TagFrame.class),
    FRAME_ENQUEUE(TagFrame.class),
    FRAME_DEQUEUE(TagFrame.class),
    FRAME_GET_RETURN_TAG(TagFrame.class),
    FRAME_SET_RETURN_TAG(TagFrame.class),
    INDIRECT_FRAME_RESTORE(IndirectTagFrameStore.class),
    INDIRECT_FRAME_GET_ADJUSTER(IndirectTagFrameStore.class),
    INDIRECT_FRAME_CLEAR(IndirectTagFrameStore.class),
    INDIRECT_FRAME_SET(IndirectTagFrameStore.class),
    INDIRECT_FRAME_GET_UNINITIALIZED_THIS(IndirectTagFrameStore.class),
    PROCESSED_FRAME_CREATE(ProcessedTagFrame.class),
    ARRAY_TAG_STORE_GET_LENGTH_TAG(ArrayTagStore.class),
    ARRAY_TAG_STORE_SET_LENGTH_TAG(ArrayTagStore.class),
    ARRAY_TAG_STORE_GET_TAG(ArrayTagStore.class),
    ARRAY_TAG_STORE_SET_TAG(ArrayTagStore.class),
    ARRAY_TAG_STORE_SET_LENGTH_TAGS(ArrayTagStore.class),
    FIELD_TAG_STORE_PUT_STATIC(FieldTagStore.class),
    FIELD_TAG_STORE_GET_STATIC(FieldTagStore.class),
    FIELD_TAG_STORE_PUT_FIELD(FieldTagStore.class),
    FIELD_TAG_STORE_GET_FIELD(FieldTagStore.class),
    FRAME_ADJUSTER_CREATE_FRAME(FrameAdjuster.class),
    FRAME_ADJUSTER_PROCESS_BOOLEAN(FrameAdjuster.class),
    FRAME_ADJUSTER_PROCESS_BYTE(FrameAdjuster.class),
    FRAME_ADJUSTER_PROCESS_CHAR(FrameAdjuster.class),
    FRAME_ADJUSTER_PROCESS_SHORT(FrameAdjuster.class),
    FRAME_ADJUSTER_PROCESS_INT(FrameAdjuster.class),
    FRAME_ADJUSTER_PROCESS_LONG(FrameAdjuster.class),
    FRAME_ADJUSTER_PROCESS_FLOAT(FrameAdjuster.class),
    FRAME_ADJUSTER_PROCESS_DOUBLE(FrameAdjuster.class),
    FRAME_ADJUSTER_PROCESS_OBJECT(FrameAdjuster.class),
    EXCEPTION_STORE_SET(ExceptionStore.class),
    EXCEPTION_STORE_GET(ExceptionStore.class),
    UNBOX_BOOLEAN(PrimitiveBoxer.class),
    UNBOX_BYTE(PrimitiveBoxer.class),
    UNBOX_CHAR(PrimitiveBoxer.class),
    UNBOX_SHORT(PrimitiveBoxer.class),
    UNBOX_INT(PrimitiveBoxer.class),
    UNBOX_LONG(PrimitiveBoxer.class),
    UNBOX_DOUBLE(PrimitiveBoxer.class),
    UNBOX_FLOAT(PrimitiveBoxer.class),
    BOX_BOOLEAN(PrimitiveBoxer.class),
    BOX_BYTE(PrimitiveBoxer.class),
    BOX_CHAR(PrimitiveBoxer.class),
    BOX_SHORT(PrimitiveBoxer.class),
    BOX_INT(PrimitiveBoxer.class),
    BOX_LONG(PrimitiveBoxer.class),
    BOX_DOUBLE(PrimitiveBoxer.class),
    BOX_FLOAT(PrimitiveBoxer.class);

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

    public MethodRecord getRecord() {
        return HandleRegistry.getRecord(this);
    }

    public void accept(MethodVisitor mv) {
        getRecord().accept(mv);
    }
}

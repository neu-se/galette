package edu.neu.ccs.prl.galette.internal.transform;

import edu.neu.ccs.prl.galette.internal.runtime.collection.Pair;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class ThreadLocalAdder extends ClassVisitor {
    private static final int LOCAL_FIELD_ACCESS =
            Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC | Opcodes.ACC_VOLATILE | Opcodes.ACC_TRANSIENT;
    private static final String LOCAL_FRAME_FIELD_NAME = GaletteTransformer.ADDED_MEMBER_PREFIX + "$$LOCAL_frame";
    private static final String LOCAL_FRAME_FIELD_DESCRIPTOR = Type.getDescriptor(Pair.class);
    private static final String LOCAL_EXCEPTION_INFO_FIELD_NAME =
            GaletteTransformer.ADDED_MEMBER_PREFIX + "$$LOCAL_exceptionInfo";
    private static final String LOCAL_EXCEPTION_INFO_DESCRIPTOR = Type.getDescriptor(Object.class);
    private static final String LOCAL_FLAG_DESCRIPTOR = Type.getDescriptor(boolean.class);
    private static final String LOCAL_TAG_STORE_FLAG_FIELD_NAME =
            GaletteTransformer.ADDED_MEMBER_PREFIX + "$$LOCAL_tagStoreFlag";
    private static final String LOCAL_UNSAFE_FLAG_FIELD_NAME =
            GaletteTransformer.ADDED_MEMBER_PREFIX + "$$LOCAL_unsafeFlag";

    ThreadLocalAdder(ClassVisitor classVisitor) {
        super(GaletteTransformer.ASM_VERSION, classVisitor);
    }

    @Override
    public void visitEnd() {
        super.visitField(LOCAL_FIELD_ACCESS, LOCAL_FRAME_FIELD_NAME, LOCAL_FRAME_FIELD_DESCRIPTOR, null, null);
        super.visitField(
                LOCAL_FIELD_ACCESS, LOCAL_EXCEPTION_INFO_FIELD_NAME, LOCAL_EXCEPTION_INFO_DESCRIPTOR, null, null);
        super.visitField(LOCAL_FIELD_ACCESS, LOCAL_TAG_STORE_FLAG_FIELD_NAME, LOCAL_FLAG_DESCRIPTOR, null, false);
        super.visitField(LOCAL_FIELD_ACCESS, LOCAL_UNSAFE_FLAG_FIELD_NAME, LOCAL_FLAG_DESCRIPTOR, null, false);
        super.visitEnd();
    }

    public static boolean isApplicable(String className) {
        return "java/lang/Thread".equals(className);
    }
}

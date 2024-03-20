package edu.neu.ccs.prl.galette.internal.transform;

import edu.neu.ccs.prl.galette.internal.runtime.collection.Pair;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class ThreadLocalFrameAdder extends ClassVisitor {
    private static final int LOCAL_FRAME_FIELD_ACCESS =
            Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC | Opcodes.ACC_VOLATILE | Opcodes.ACC_TRANSIENT;
    private static final String LOCAL_FRAME_FIELD_NAME = GaletteTransformer.ADDED_MEMBER_PREFIX + "frame";
    private static final String LOCAL_FRAME_FIELD_DESCRIPTOR = Type.getDescriptor(Pair.class);

    ThreadLocalFrameAdder(ClassVisitor classVisitor) {
        super(GaletteTransformer.ASM_VERSION, classVisitor);
    }

    @Override
    public void visitEnd() {
        super.visitField(LOCAL_FRAME_FIELD_ACCESS, LOCAL_FRAME_FIELD_NAME, LOCAL_FRAME_FIELD_DESCRIPTOR, null, null);
        super.visitEnd();
    }

    public static boolean isApplicable(String className) {
        return "java/lang/Thread".equals(className);
    }
}

package edu.neu.ccs.prl.galette.internal.transform;

import edu.neu.ccs.prl.galette.internal.runtime.collection.Stack;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class ThreadStackAdder extends ClassVisitor {
    private static final int STACK_FIELD_ACCESS =
            Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC | Opcodes.ACC_VOLATILE | Opcodes.ACC_TRANSIENT;
    private static final String STACK_FIELD_NAME = GaletteTransformer.ADDED_MEMBER_PREFIX + "STACK";
    private static final String STACK_FIELD_DESCRIPTOR = Type.getDescriptor(Stack.class);

    ThreadStackAdder(ClassVisitor classVisitor) {
        super(GaletteTransformer.ASM_VERSION, classVisitor);
    }

    @Override
    public void visitEnd() {
        super.visitField(STACK_FIELD_ACCESS, STACK_FIELD_NAME, STACK_FIELD_DESCRIPTOR, null, null);
        super.visitEnd();
    }

    public static boolean isApplicable(String className) {
        return "java/lang/Thread".equals(className);
    }
}

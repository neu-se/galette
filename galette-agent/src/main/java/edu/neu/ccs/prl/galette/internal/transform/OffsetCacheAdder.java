package edu.neu.ccs.prl.galette.internal.transform;

import edu.neu.ccs.prl.galette.internal.runtime.collection.SimpleList;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class OffsetCacheAdder extends ClassVisitor {
    private static final int OFFSET_CACHE_FIELD_ACCESS =
            Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC | Opcodes.ACC_VOLATILE | Opcodes.ACC_TRANSIENT;
    private static final String OFFSET_CACHE_FIELD_NAME = GaletteTransformer.ADDED_MEMBER_PREFIX + "OFFSET_CACHE";
    private static final String OFFSET_CACHE_FIELD_DESCRIPTOR = Type.getDescriptor(SimpleList.class);

    OffsetCacheAdder(ClassVisitor classVisitor) {
        super(GaletteTransformer.ASM_VERSION, classVisitor);
    }

    @Override
    public void visitEnd() {
        super.visitField(OFFSET_CACHE_FIELD_ACCESS, OFFSET_CACHE_FIELD_NAME, OFFSET_CACHE_FIELD_DESCRIPTOR, null, null);
        super.visitEnd();
    }

    public static boolean isApplicable(String className) {
        return "java/lang/Class".equals(className);
    }
}

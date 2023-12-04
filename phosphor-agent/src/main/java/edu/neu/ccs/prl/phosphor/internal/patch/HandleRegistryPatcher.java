package edu.neu.ccs.prl.phosphor.internal.patch;

import edu.neu.ccs.prl.phosphor.internal.runtime.Handle;
import edu.neu.ccs.prl.phosphor.internal.transform.AsmUtil;
import edu.neu.ccs.prl.phosphor.internal.transform.HandleRegistry;
import edu.neu.ccs.prl.phosphor.internal.transform.MethodRecord;
import edu.neu.ccs.prl.phosphor.internal.transform.PhosphorTransformer;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class HandleRegistryPatcher extends ClassVisitor {
    private static final String HANDLE_REGISTRY_INTERNAL_NAME = Type.getInternalName(HandleRegistry.class);
    private static final String TARGET_METHOD_NAME = "initialize";

    HandleRegistryPatcher(ClassVisitor cv) {
        super(PhosphorTransformer.ASM_VERSION, cv);
    }

    @Override
    public MethodVisitor visitMethod(
            int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor originalMv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (originalMv != null && TARGET_METHOD_NAME.equals(name)) {
            // Pass null as the delegate to replace the original body
            return new MethodVisitor(api, null) {
                @Override
                public void visitCode() {
                    replaceInitialize(originalMv);
                }
            };
        }
        return originalMv;
    }

    private static void replaceInitialize(MethodVisitor mv) {
        mv.visitCode();
        for (Handle handle : Handle.values()) {
            MethodRecord record = HandleRegistry.getRecord(handle);
            AsmUtil.pushInt(mv, handle.ordinal());
            AsmUtil.pushInt(mv, record.getOpcode());
            mv.visitLdcInsn(record.getOwner());
            mv.visitLdcInsn(record.getName());
            mv.visitLdcInsn(record.getDescriptor());
            mv.visitInsn(record.isInterface() ? Opcodes.ICONST_1 : Opcodes.ICONST_0);
            HandleRegistry.accept(mv, Handle.HANDLE_REGISTRY_ADD_RECORD);
        }
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(-1, -1);
        mv.visitEnd();
    }

    public static boolean isApplicable(String className) {
        return HANDLE_REGISTRY_INTERNAL_NAME.equals(className);
    }
}

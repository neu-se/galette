package edu.neu.ccs.prl.galette.internal.patch;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import edu.neu.ccs.prl.galette.internal.runtime.collection.SimpleList;
import edu.neu.ccs.prl.galette.internal.transform.*;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class RegistryPatcher extends ClassVisitor {
    private static final String MASK_REGISTRY_INTERNAL_NAME = Type.getInternalName(MaskRegistry.class);
    private static final String HANDLE_REGISTRY_INTERNAL_NAME = Type.getInternalName(HandleRegistry.class);
    private static final String TARGET_METHOD_NAME = "initialize";
    private final String className;

    RegistryPatcher(ClassVisitor cv, String className) {
        super(GaletteTransformer.ASM_VERSION, cv);
        if (!isApplicable(className)) {
            throw new IllegalArgumentException();
        }
        this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(
            int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor originalMv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (originalMv != null && TARGET_METHOD_NAME.equals(name)) {
            // Pass null to the delegate to replace the original body
            return new MethodVisitor(api, null) {
                @Override
                public void visitCode() {
                    replaceInitialize(originalMv);
                }
            };
        }
        return originalMv;
    }

    private void replaceInitialize(MethodVisitor mv) {
        mv.visitCode();
        if (className.equals(HANDLE_REGISTRY_INTERNAL_NAME)) {
            for (Handle handle : Handle.values()) {
                AsmUtil.pushInt(mv, handle.ordinal());
                push(mv, HandleRegistry.getRecord(handle));
                Handle.HANDLE_REGISTRY_PUT.accept(mv);
            }
        } else {
            SimpleList<String> keys = MaskRegistry.getKeys();
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                mv.visitLdcInsn(key);
                MaskRegistry.MaskInfo mask = MaskRegistry.getMask(key);
                AsmUtil.pushInt(mv, mask.getType().ordinal());
                push(mv, mask.getRecord());
                Handle.MASK_REGISTRY_PUT.accept(mv);
            }
        }
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(-1, -1);
        mv.visitEnd();
    }

    static void push(MethodVisitor mv, MethodRecord record) {
        AsmUtil.pushInt(mv, record.getOpcode());
        mv.visitLdcInsn(record.getOwner());
        mv.visitLdcInsn(record.getName());
        mv.visitLdcInsn(record.getDescriptor());
        mv.visitInsn(record.isInterface() ? Opcodes.ICONST_1 : Opcodes.ICONST_0);
    }

    public static boolean isApplicable(String className) {
        return HANDLE_REGISTRY_INTERNAL_NAME.equals(className) || MASK_REGISTRY_INTERNAL_NAME.equals(className);
    }
}

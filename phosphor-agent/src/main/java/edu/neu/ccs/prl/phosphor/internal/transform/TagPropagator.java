package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.internal.runtime.Handle;
import org.objectweb.asm.MethodVisitor;

// TODO
class TagPropagator extends MethodVisitor {
    TagPropagator(MethodVisitor mv) {
        super(PhosphorTransformer.ASM_VERSION, mv);
    }

    @Override
    public void visitCode() {
        // Store the frame immediately
        // Load argument tags from the frame
        // TODO initialize shadow local variables and stack
        super.visitCode();
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        // TODO
        if (opcode == -1 && !isIgnoredMethod(owner, name)) {
            name = ShadowMethodCreator.getShadowMethodName(name);
            descriptor = ShadowMethodCreator.getShadowMethodDescriptor(descriptor);
            HandleRegistry.accept(mv, Handle.FRAME_GET_INSTANCE);
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    private static boolean isIgnoredMethod(String owner, String name) {
        if (owner.equals("java/lang/Object") || owner.startsWith("[")) {
            return true;
        }
        if (PhosphorTransformer.isExcluded(owner)) {
            return true;
        }
        if (owner.startsWith("jdk/internal/module/SystemModules")) {
            return true;
        }
        // Needed for compatability with the JProfiler agent
        if (owner.startsWith("com/jprofiler")) {
            return true;
        }
        if (owner.equals("java/lang/invoke/MethodHandle")) {
            return true;
        }
        if (owner.equals("java/lang/invoke/VarHandle")) {
            return true;
        }
        if (owner.equals("jdk/internal/reflect/Reflection")) {
            return name.equals("getCallerClass");
        }
        if (owner.equals("sun/reflect/Reflection")) {
            return name.equals("getCallerClass");
        }
        return ShadowMethodCreator.shouldShadow(owner, name);
    }
}

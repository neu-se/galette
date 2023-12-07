package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.internal.runtime.Handle;
import edu.neu.ccs.prl.phosphor.internal.runtime.PhosphorFrame;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

class TagPropagator extends MethodVisitor {
    /**
     * MethodNode storing for the original uninstrumented method.
     *
     */
    private final MethodNode original;
    /**
     * Local variable index used to store the {@link edu.neu.ccs.prl.phosphor.internal.runtime.PhosphorFrame}
     * for this method.
     */
    private final int frameIndex;
    /**
     * {@code true} if the method being visited was passed a
     * {@link edu.neu.ccs.prl.phosphor.internal.runtime.PhosphorFrame} as an argument.
     */
    private final boolean isShadow;
    /**
     * Label marking the end of the frame local variable's scope.
     */
    private final Label frameEnd = new Label();

    TagPropagator(MethodVisitor mv, MethodNode original, boolean isShadow) {
        super(PhosphorTransformer.ASM_VERSION, mv);
        this.original = original;
        this.frameIndex = original.maxLocals;
        this.isShadow = isShadow;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        storeFrame();
        // Load argument tags from the frame
        // TODO initialize shadow local variables and stack
        super.visitCode();
    }

    private void storeFrame() {
        Label frameStart = new Label();
        super.visitLabel(frameStart);
        super.visitLocalVariable(
                getShadowVariableName("phosphorFrame"),
                Type.getDescriptor(PhosphorFrame.class),
                null,
                frameStart,
                frameEnd,
                frameIndex);
        if (isShadow) {
            loadPassedFrame();
        } else {
            HandleRegistry.accept(mv, Handle.FRAME_GET_INSTANCE);
        }
        super.visitVarInsn(Opcodes.ASTORE, frameIndex);
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

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        super.visitFrame(type, numLocal, local, numStack, stack);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitLabel(frameEnd);
        super.visitMaxs(-1, -1);
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

    private void loadPassedFrame() {
        int varIndex = AsmUtil.isSet(original.access, Opcodes.ACC_STATIC) ? 0 : 1;
        for (Type argument : Type.getArgumentTypes(original.desc)) {
            varIndex += argument.getSize();
        }
        super.visitVarInsn(Opcodes.ALOAD, varIndex);
    }

    private static String getShadowVariableName(String name) {
        return PhosphorTransformer.ADDED_MEMBER_PREFIX + name;
    }
}

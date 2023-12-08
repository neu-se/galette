package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.internal.runtime.Handle;
import edu.neu.ccs.prl.phosphor.internal.runtime.PhosphorFrame;
import edu.neu.ccs.prl.phosphor.internal.runtime.collection.SimpleList;
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
        initializeFrameVariable();
        // Load argument tags from the frame
        // TODO initialize shadow local variables and stack
        super.visitCode();
    }

    private void initializeFrameVariable() {
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
            Handle.FRAME_GET_INSTANCE.accept(mv);
        }
        super.visitVarInsn(Opcodes.ASTORE, frameIndex);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (isGetCallerClass(owner, name, descriptor)) {
            // Call the original method
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            // [Class]
            super.visitVarInsn(Opcodes.ALOAD, frameIndex);
            // [Class Frame]
            super.visitInsn(Opcodes.SWAP);
            // [Frame Class]
            Handle.FRAME_GET_CALLER.accept(mv);
            // [Class]
        } else if (!isIgnoredMethod(owner, name)) {
            name = ShadowMethodCreator.getShadowMethodName(name);
            descriptor = ShadowMethodCreator.getShadowMethodDescriptor(descriptor);
            super.visitVarInsn(Opcodes.ALOAD, frameIndex);
            Handle.FRAME_CREATE_FOR_CALL.accept(mv);
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        } else {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        if (type != Opcodes.F_NEW) {
            // Uncompressed frame.
            throw new IllegalArgumentException("Expected expanded frames");
        }
        SimpleList<Object> newLocal = new SimpleList<>();
        int varIndex = 0;
        // Copy the original locals
        for (int i = 0; i < numLocal; i++) {
            Object localElement = local[i];
            newLocal.add(localElement);
            if (localElement == Opcodes.LONG || localElement == Opcodes.DOUBLE) {
                // Longs and doubles take two variable slots but are only represented once in the frame
                varIndex += 2;
            } else {
                varIndex++;
            }
        }
        // Fill in TOP until we get to the frame index
        for (; varIndex < frameIndex; varIndex++) {
            newLocal.add(Opcodes.TOP);
        }
        // Add the frame
        newLocal.add(Type.getInternalName(PhosphorFrame.class));
        super.visitFrame(type, newLocal.size(), newLocal.toArray(new Object[newLocal.size()]), numStack, stack);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitLabel(frameEnd);
        super.visitMaxs(-1, -1);
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

    private static boolean isGetCallerClass(String owner, String name, String descriptor) {
        // The result of getCallerClass depends on the caller, so we cannot use the native wrapper without changing the
        // caller
        if (name.equals("getCallerClass") && "()Ljava/lang/Class;".equals(descriptor)) {
            return owner.equals("jdk/internal/reflect/Reflection") || owner.equals("sun/reflect/Reflection");
        }
        return false;
    }

    private static boolean isIgnoredMethod(String owner, String name) {
        // We cannot add shadow methods to Object or arrays
        if (owner.equals("java/lang/Object") || owner.startsWith("[")) {
            return true;
        }
        // Shadows are not created for classes explicitly from Phosphor instrumentation
        if (PhosphorTransformer.isExcluded(owner)) {
            return true;
        }
        // These classes are regenerated by the "system modules" jlink plugin
        if (owner.startsWith("jdk/internal/module/SystemModules")) {
            return true;
        }
        // Needed for compatability with the JProfiler agent
        // TODO check if needed
        if (owner.startsWith("com/jprofiler")) {
            return true;
        }
        // TODO figure out why these are needed and if there are other Handles that need to listed
        // TODO check if needed
        if (owner.equals("java/lang/invoke/MethodHandle")) {
            return true;
        }
        if (owner.equals("java/lang/invoke/BoundMethodHandle")) {
            return true;
        }
        if (owner.equals("java/lang/invoke/VarHandle")) {
            return true;
        }
        // A shadow was not created for the original method
        return !ShadowMethodCreator.shouldShadow(name);
    }
}

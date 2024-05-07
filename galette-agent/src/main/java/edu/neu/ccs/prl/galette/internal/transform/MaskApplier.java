package edu.neu.ccs.prl.galette.internal.transform;

import static org.objectweb.asm.Opcodes.*;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import edu.neu.ccs.prl.galette.internal.runtime.mask.ClassMasks;
import edu.neu.ccs.prl.galette.internal.runtime.mask.UnsafeMasks;
import edu.neu.ccs.prl.galette.internal.transform.MaskRegistry.MaskInfo;
import java.io.ObjectStreamClass;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.tree.MethodNode;

class MaskApplier extends MethodVisitor {
    /**
     * Internal name for {@link ObjectStreamClass}.
     * <p>
     * Non-null.
     */
    static final String OBJECT_STREAM_CLASS_INTERNAL_NAME = Type.getInternalName(ObjectStreamClass.class);
    /**
     * Internal name for FieldReflector.
     * <p>
     * Non-null.
     */
    static final String FIELD_REFLECTOR_INTERNAL_NAME = "java/io/ObjectStreamClass$FieldReflector";
    /**
     * Internal name for {@link ClassMasks}.
     * <p>
     * Non-null.
     */
    static final String CLASS_MASKS_INTERNAL_NAME = Type.getInternalName(ClassMasks.class);
    /**
     * Internal name for {@link edu.neu.ccs.prl.galette.internal.runtime.mask.UnsafeMasks]}.
     * <p>
     * Non-null.
     */
    static final String UNSAFE_MASKS_INTERNAL_NAME = Type.getInternalName(UnsafeMasks.class);
    /**
     * Internal name of the class being visited.
     * <p>
     * Non-null.
     */
    private final String className;
    /**
     * Analyzer used to track the runtime stack and locals.
     * <p>
     * Non-null.
     */
    private final AnalyzerAdapter analyzer;

    MaskApplier(String className, MethodNode mn) {
        this(className, new AnalyzerAdapter(className, mn.access, mn.name, mn.desc, mn));
    }

    private MaskApplier(String className, AnalyzerAdapter analyzer) {
        super(GaletteTransformer.ASM_VERSION, analyzer);
        if (className == null || analyzer == null) {
            throw new NullPointerException();
        }
        this.className = className;
        this.analyzer = analyzer;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        MaskInfo mask = MaskRegistry.getMask(owner, name, descriptor);
        if (mask != null && allowMask(mask)) {
            switch (mask.getType()) {
                case REPLACE:
                    mask.getRecord().accept(getDelegate());
                    castReturn(mask, descriptor);
                    return;
                case REPAIR_RETURN:
                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    mask.getRecord().accept(getDelegate());
                    castReturn(mask, descriptor);
                    return;
                case FIX_ARGUMENTS:
                    mask.getRecord().accept(getDelegate());
                    AsmUtil.spreadArguments(mv, owner, opcode == INVOKESTATIC, descriptor);
                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    return;
                case POST_PROCESS:
                    visitPostProcessMask(opcode, owner, name, descriptor, isInterface, mask);
                    castReturn(mask, descriptor);
                    return;
                default:
                    throw new AssertionError();
            }
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    private void castReturn(MaskInfo mask, String descriptor) {
        Type actualReturnType = Type.getReturnType(descriptor);
        Type maskReturnType = Type.getReturnType(mask.getRecord().getDescriptor());
        if (actualReturnType.getSort() != Type.VOID && !maskReturnType.equals(actualReturnType)) {
            super.visitTypeInsn(CHECKCAST, actualReturnType.getInternalName());
        }
    }

    private void visitPostProcessMask(
            int opcode, String owner, String name, String descriptor, boolean isInterface, MaskInfo mask) {
        // stack: ..., receiver?, arg_0, arg_1, ..., arg_{n-1}
        int varIndex = analyzer.locals.size();
        boolean isStatic = opcode == INVOKESTATIC;
        AsmUtil.storeReceiverAndArguments(mv, isStatic, descriptor, varIndex);
        // stack: ...
        AsmUtil.loadReceiverAndArguments(mv, isStatic, descriptor, varIndex);
        // stack: ..., receiver?, arg_0, arg_1, ..., arg_{n-1}
        int tagsIndex = copyTags(varIndex, descriptor, isStatic);
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        // ..., ret
        AsmUtil.loadReceiverAndArguments(mv, isStatic, descriptor, varIndex);
        if (tagsIndex != -1) {
            // stack: ..., receiver?, arg_0, arg_1, ..., frame
            super.visitInsn(DUP);
            super.visitVarInsn(ALOAD, tagsIndex);
            Handle.FRAME_SET_TAGS.accept(mv);
        }
        mask.getRecord().accept(getDelegate());
    }

    private int copyTags(int varIndex, String descriptor, boolean isStatic) {
        // Store a copy of the original tag frame's tags
        Type[] arguments = Type.getArgumentTypes(descriptor);
        if (arguments.length > 0) {
            Type last = arguments[arguments.length - 1];
            if (GaletteNames.FRAME_DESCRIPTOR.equals(last.getDescriptor())) {
                // stack: ..., receiver?, arg_0, arg_1, ..., frame
                super.visitInsn(DUP);
                Handle.FRAME_GET_TAGS.accept(mv);
                int index = varIndex + AsmUtil.countArgumentSlots(isStatic, descriptor);
                super.visitVarInsn(ASTORE, index);
                return index;
            }
        }
        return -1;
    }

    private boolean allowMask(MaskInfo mask) {
        if (Configuration.isPropagateThroughSerialization()) {
            return !isDisabledForObjectStreamClass(mask) && !isDisabledForFieldReflector(mask);
        }
        return true;
    }

    private boolean isDisabledForFieldReflector(MaskInfo mask) {
        return className.equals(FIELD_REFLECTOR_INTERNAL_NAME)
                && UNSAFE_MASKS_INTERNAL_NAME.equals(mask.getRecord().getOwner());
    }

    private boolean isDisabledForObjectStreamClass(MaskInfo mask) {
        return className.equals(OBJECT_STREAM_CLASS_INTERNAL_NAME)
                && CLASS_MASKS_INTERNAL_NAME.equals(mask.getRecord().getOwner())
                && "getFields".equals(mask.getRecord().getName());
    }
}

package edu.neu.ccs.prl.galette.internal.transform;

import static org.objectweb.asm.Type.*;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.collection.Pair;
import edu.neu.ccs.prl.galette.internal.runtime.collection.SimpleList;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Initializes the tag frame of methods that were not directly passed a frame as an argument.
 */
class IndirectFrameInitializer extends FrameInitializer {
    /**
     * Whether the method being visited is an instance initialization method, i.e., {@code <init>}.
     */
    private final boolean isInstanceInitializer;
    /**
     * Label marking the start of the exception handler to be added to the method being visited.
     */
    private final Label scopeStart = new Label();
    /**
     * Label marking the end of the exception handler to be added to the method being visited.
     */
    private final Label scopeEnd = new Label();
    /**
     * Total number of try-catch blocks to be visited before inserting the new one.
     * Ensures that the inserted block is outside all original exception handlers.
     */
    private final int totalBlocks;
    /**
     * Number of try-catch blocks that have been visited so far.
     */
    private int visitedBlocks = 0;
    /**
     * Descriptor of the method being visited.
     */
    private final String descriptor;
    /**
     * Access modifier of the method being visited.
     */
    private final int access;
    /**
     * Local variable index used to store a {@link Pair} containing information about an indirectly passed frame.
     */
    private final int pairIndex;
    /**
     * Local variable index used to store the {@link Tag} array of an indirectly passing frame.
     */
    private final int tagsIndex;

    IndirectFrameInitializer(
            MethodVisitor mv,
            boolean isInstanceInitializer,
            int frameIndex,
            int totalBlocks,
            String descriptor,
            int access) {
        super(mv, frameIndex);
        this.isInstanceInitializer = isInstanceInitializer;
        this.totalBlocks = totalBlocks;
        this.descriptor = descriptor;
        this.access = access;
        this.pairIndex = super.lastAddedLocalIndex() + 1;
        this.tagsIndex = super.lastAddedLocalIndex() + 2;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        super.visitLocalVariable(
                GaletteNames.getShadowVariableName("indirectFramePair"),
                GaletteNames.PAIR_DESCRIPTOR,
                null,
                localsStart,
                localsEnd,
                pairIndex);
        super.visitLocalVariable(
                GaletteNames.getShadowVariableName("indirectTags"),
                Type.getDescriptor(Tag[].class),
                null,
                localsStart,
                localsEnd,
                tagsIndex);
        checkHandler();
    }

    @Override
    public int lastAddedLocalIndex() {
        return tagsIndex;
    }

    @Override
    public void appendAddedLocals(SimpleList<Object> locals) {
        super.appendAddedLocals(locals);
        locals.add(GaletteNames.PAIR_INTERNAL_NAME);
        locals.add(Type.getInternalName(Tag[].class));
    }

    @Override
    public void visitInsn(int opcode) {
        if (AsmUtil.isReturn(opcode) || (opcode == Opcodes.ATHROW && isInstanceInitializer)) {
            // Note: ATHROW instructions are handled by the added exception handler for non-instance-initialization
            // methods
            restore();
        }
        super.visitInsn(opcode);
    }

    private void restore() {
        super.visitVarInsn(Opcodes.ALOAD, pairIndex);
        Handle.INDIRECT_FRAME_SET_PAIR.accept(mv);
        super.visitVarInsn(Opcodes.ALOAD, getFrameIndex());
        super.visitVarInsn(Opcodes.ALOAD, tagsIndex);
        Handle.FRAME_SET_TAGS.accept(mv);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        super.visitTryCatchBlock(start, end, handler, type);
        visitedBlocks++;
        checkHandler();
    }

    private void checkHandler() {
        if (!isInstanceInitializer && totalBlocks == visitedBlocks) {
            super.visitTryCatchBlock(scopeStart, scopeEnd, scopeEnd, null);
            super.visitLabel(scopeStart);
        }
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        if (!isInstanceInitializer) {
            int frameIndex = getFrameIndex();
            // Add the exception handler
            super.visitLabel(scopeEnd);
            Object[] locals = AsmUtil.createTopArray(lastAddedLocalIndex() + 1);
            locals[frameIndex] = GaletteNames.FRAME_INTERNAL_NAME;
            locals[frameIndex + 1] = GaletteNames.PAIR_INTERNAL_NAME;
            locals[frameIndex + 2] = Type.getInternalName(Tag[].class);
            super.visitFrame(Opcodes.F_NEW, locals.length, locals, 1, new Object[] {"java/lang/Throwable"});
            restore();
            // Rethrow the exception
            super.visitInsn(Opcodes.ATHROW);
        }
        super.visitMaxs(maxStack, maxLocals);
    }

    @Override
    protected void initializeFrame() {
        // Get the pair
        Handle.INDIRECT_FRAME_GET_AND_CLEAR.accept(mv);
        super.visitInsn(Opcodes.DUP);
        // Store the pair
        super.visitVarInsn(Opcodes.ASTORE, pairIndex);
        // FrameAdjuster
        Handle.INDIRECT_FRAME_GET_ADJUSTER.accept(mv);
        int varIndex = 0;
        if (!AsmUtil.isSet(Opcodes.ACC_STATIC, access)) {
            super.visitInsn(Opcodes.DUP);
            if (isInstanceInitializer) {
                // Use placeholder for uninitialized this
                Handle.INDIRECT_FRAME_GET_UNINITIALIZED_THIS.accept(mv);
            } else {
                super.visitVarInsn(Opcodes.ALOAD, varIndex);
            }
            Handle.FRAME_ADJUSTER_PROCESS_OBJECT.accept(mv);
            varIndex++;
        }
        for (Type argument : Type.getArgumentTypes(descriptor)) {
            super.visitInsn(Opcodes.DUP);
            mv.visitVarInsn(argument.getOpcode(Opcodes.ILOAD), varIndex);
            varIndex += argument.getSize();
            getProcessorHandle(argument).accept(mv);
        }
        // FrameAdjuster
        Handle.FRAME_ADJUSTER_CREATE_FRAME.accept(mv);
        // TODO only do if match occurred
        // Defensively copy the frame's tags to guard against a false match
        super.visitInsn(Opcodes.DUP);
        Handle.FRAME_GET_TAGS.accept(mv);
        super.visitVarInsn(Opcodes.ASTORE, tagsIndex);
    }

    private static Handle getProcessorHandle(Type type) {
        switch (type.getSort()) {
            case BOOLEAN:
                return Handle.FRAME_ADJUSTER_PROCESS_BOOLEAN;
            case BYTE:
                return Handle.FRAME_ADJUSTER_PROCESS_BYTE;
            case CHAR:
                return Handle.FRAME_ADJUSTER_PROCESS_CHAR;
            case SHORT:
                return Handle.FRAME_ADJUSTER_PROCESS_SHORT;
            case INT:
                return Handle.FRAME_ADJUSTER_PROCESS_INT;
            case FLOAT:
                return Handle.FRAME_ADJUSTER_PROCESS_FLOAT;
            case LONG:
                return Handle.FRAME_ADJUSTER_PROCESS_LONG;
            case DOUBLE:
                return Handle.FRAME_ADJUSTER_PROCESS_DOUBLE;
            default:
                return Handle.FRAME_ADJUSTER_PROCESS_OBJECT;
        }
    }
}

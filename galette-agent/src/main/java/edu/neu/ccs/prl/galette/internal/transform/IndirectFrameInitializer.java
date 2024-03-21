package edu.neu.ccs.prl.galette.internal.transform;

import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.F_NEW;
import static org.objectweb.asm.Type.*;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Initializes the tag frame of methods that were not directly passed as an argument.
 */
class IndirectFrameInitializer extends FrameInitializer {
    /**
     * Whether the method being visited is an instance initialization method, i.e., {@code <init>}.
     */
    private final boolean isInstanceInitializer;
    /**
     * Label marking the start of the scope of the exception handler to be added to the method being visited.
     */
    private final Label scopeStart = new Label();
    /**
     * Label marking the end of the scope of the exception handler to be added to the method being visited.
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
    }

    @Override
    public void visitCode() {
        super.visitCode();
        checkHandler();
    }

    @Override
    public void visitInsn(int opcode) {
        if (AsmUtil.isReturn(opcode) || (opcode == ATHROW && isInstanceInitializer)) {
            // Note: ATHROW instructions are handled by the added exception handler for non instance
            // initialization methods
            super.visitVarInsn(Opcodes.ALOAD, getFrameIndex());
            Handle.INDIRECT_FRAME_RESTORE.accept(mv);
        }
        super.visitInsn(opcode);
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
            Object[] locals = AsmUtil.createTopArray(frameIndex + 1);
            locals[frameIndex] = GaletteNames.FRAME_INTERNAL_NAME;
            super.visitFrame(F_NEW, locals.length, locals, 1, new Object[] {"java/lang/Throwable"});
            // Restore the frame stack
            super.visitVarInsn(Opcodes.ALOAD, getFrameIndex());
            Handle.INDIRECT_FRAME_RESTORE.accept(mv);
            // Rethrow the exception
            super.visitInsn(ATHROW);
        }
        super.visitMaxs(maxStack, maxLocals);
    }

    @Override
    protected void loadFrame() {
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

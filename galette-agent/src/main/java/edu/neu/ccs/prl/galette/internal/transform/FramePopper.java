package edu.neu.ccs.prl.galette.internal.transform;

import static org.objectweb.asm.Opcodes.*;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrameStack;
import edu.neu.ccs.prl.galette.internal.runtime.collection.SimpleList;
import java.util.Iterator;
import java.util.List;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AnalyzerAdapter;

/**
 * Ensures that {@link TagFrameStack#pop()} is called after signature polymorphic method calls.
 */
class FramePopper extends MethodVisitor {
    private final AnalyzerAdapter analyzer;

    FramePopper(String owner, int access, String name, String descriptor, MethodVisitor mv) {
        this(new AnalyzerAdapter(owner, access, name, descriptor, mv));
    }

    private FramePopper(AnalyzerAdapter analyzer) {
        super(GaletteTransformer.ASM_VERSION, analyzer);
        this.analyzer = analyzer;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (TagPropagator.isSignaturePolymorphic(owner, name) && analyzer.locals != null) {
            visitSignaturePolymorphicMethodInsn(opcode, owner, name, descriptor, isInterface);
        } else {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }

    private void visitSignaturePolymorphicMethodInsn(
            int opcode, String owner, String name, String descriptor, boolean isInterface) {
        // Add an exception handler to ensure that the pushed frame is popped.
        Label start = new Label();
        Label end = new Label();
        Label handler = new Label();
        super.visitTryCatchBlock(start, end, handler, null);
        // Start exception handler scope
        super.visitLabel(start);
        // Call the signature polymorphic method
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        // End the exception handler scope
        super.visitLabel(end);
        // Record the locals for the handle we will later add
        Object[] handleLocals = getFrameElements(analyzer.locals);
        // Pop the stored frame
        Handle.FRAME_STACK_POP.accept(mv);
        // Record the current frame
        Object[] locals = getFrameElements(analyzer.locals);
        Object[] stack = getFrameElements(analyzer.stack);
        // Jump to after the handler
        Label target = new Label();
        super.visitJumpInsn(GOTO, target);
        // Insert the exception handler
        visitFrameClearHandler(handler, handleLocals);
        // Add the jump target and its frame
        super.visitLabel(target);
        super.visitFrame(F_NEW, locals.length, locals, stack.length, stack);
        // Insert a NOP to ensure there is an instruction between the added frame and the next frame
        super.visitInsn(NOP);
    }

    private void visitFrameClearHandler(Label handler, Object[] locals) {
        super.visitLabel(handler);
        super.visitFrame(F_NEW, locals.length, locals, 1, new Object[] {"java/lang/Throwable"});
        // Pop the stored frame
        Handle.FRAME_STACK_POP.accept(mv);
        // Rethrow the exception
        super.visitInsn(ATHROW);
    }

    private Object[] getFrameElements(List<Object> raw) {
        SimpleList<Object> locals = new SimpleList<>();
        for (Iterator<Object> itr = raw.iterator(); itr.hasNext(); ) {
            Object local = itr.next();
            locals.add(local);
            if (local.equals(LONG) || local.equals(DOUBLE)) {
                // Skip tops for wide types
                itr.next();
            }
        }
        return locals.toArray(new Object[locals.size()]);
    }
}

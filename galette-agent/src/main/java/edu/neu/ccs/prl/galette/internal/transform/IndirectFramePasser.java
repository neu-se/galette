package edu.neu.ccs.prl.galette.internal.transform;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.DOUBLE;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import edu.neu.ccs.prl.galette.internal.runtime.collection.SimpleList;
import java.util.Iterator;
import java.util.List;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AnalyzerAdapter;

/**
 * MethodVisitor responsible for indirectly passing tag frames to signature polymorphic methods.
 * <p>
 * Due to the invocation semantics of signature polymorphic methods, a tag frame cannot be directly passed to
 * a signature polymorphic method call as an extra argument.
 * Instead, Galette will indirectly pass the frame by temporarily storing on the {@link Thread} instance
 * representing the calling thread of execution.
 * This storage location is added by {@link ThreadLocalFrameAdder}.
 *
 */
class IndirectFramePasser extends MethodVisitor {
    private final ShadowLocals shadowLocals;
    private final AnalyzerAdapter analyzer;

    IndirectFramePasser(ShadowLocals shadowLocals, AnalyzerAdapter analyzer, MethodVisitor mv) {
        super(GaletteTransformer.ASM_VERSION, mv);
        if (shadowLocals == null || analyzer == null) {
            throw new NullPointerException();
        }
        this.shadowLocals = shadowLocals;
        this.analyzer = analyzer;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (isSignaturePolymorphic(owner, name)) {
            visitSignaturePolymorphicMethodInsn(opcode, owner, name, descriptor, isInterface);
        } else {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }

    private void visitSignaturePolymorphicMethodInsn(
            int opcode, String owner, String name, String descriptor, boolean isInterface) {
        Label scopeEnd = new Label();
        Label handler = new Label();
        if (analyzer.locals != null) {
            startHandlerScope(scopeEnd, handler);
        }
        // Consume tags from the shadow stack for the arguments of the call and create a frame
        shadowLocals.prepareForCall(opcode == INVOKESTATIC, descriptor, true);
        // Store the frame in the indirect frame store
        shadowLocals.visitLdcInsn(descriptor);
        Handle.FRAME_SET_DESCRIPTOR.accept(shadowLocals);
        Handle.INDIRECT_FRAME_SET.accept(shadowLocals);
        // Call the signature polymorphic method
        // The analyzer must see this call delegate to mv (analyzer)
        mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        // Set the tag for the return value
        shadowLocals.restoreFromCall(descriptor, true);
        if (analyzer.locals != null) {
            endHandlerScope(scopeEnd, handler);
        }
    }

    private void startHandlerScope(Label scopeEnd, Label handler) {
        // Add an exception handler to ensure that the pushed frame is popped.
        Label scopeStart = new Label();
        mv.visitTryCatchBlock(scopeStart, scopeEnd, handler, null);
        // Start the exception handler's scope
        mv.visitLabel(scopeStart);
    }

    private void endHandlerScope(Label scopeEnd, Label handler) {
        // End the exception handler scope
        mv.visitLabel(scopeEnd);
        // Record the locals for the handle we will later add
        Object[] handleLocals = getFrameElements(analyzer.locals);
        // Clear the tag frame
        Handle.INDIRECT_FRAME_CLEAR.accept(mv);
        // Record the current frame
        Object[] locals = getFrameElements(analyzer.locals);
        Object[] stack = getFrameElements(analyzer.stack);
        // Jump to after the handler
        Label target = new Label();
        mv.visitJumpInsn(GOTO, target);
        // Insert the exception handler
        mv.visitLabel(handler);
        mv.visitFrame(F_NEW, handleLocals.length, handleLocals, 1, new Object[] {"java/lang/Throwable"});
        // Clear the tag frame
        Handle.INDIRECT_FRAME_CLEAR.accept(mv);
        // Rethrow the exception
        mv.visitInsn(ATHROW);
        // Add the jump target and its frame
        mv.visitLabel(target);
        mv.visitFrame(F_NEW, locals.length, locals, stack.length, stack);
        // Insert a NOP to ensure there is an instruction between the added frame and the next frame
        mv.visitInsn(NOP);
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

    /**
     * Returns {@code true} if the method with the specified name owned by the specified class is signature polymorphic;
     * otherwise, {@code false}.
     *
     * @param owner the internal name of the class that owns the method
     * @param name the name of the method
     * @return {@code true} if the method with the specified name owned by the specified class is signature polymorphic
     * @throws NullPointerException if the specified class or method name is {@code null}
     * @see Type#getInternalName()
     * @see java.lang.invoke.MethodHandle
     * @see java.lang.invoke.VarHandle
     */
    static boolean isSignaturePolymorphic(String owner, String name) {
        switch (name) {
            case "invokeBasic":
            case "invoke":
            case "invokeExact":
            case "linkToInterface":
            case "linkToNative":
            case "linkToSpecial":
            case "linkToStatic":
            case "linkToVirtual":
                return owner.equals("java/lang/invoke/MethodHandle")
                        || owner.startsWith("java/lang/invoke/BoundMethodHandle");
            case "compareAndSet":
            case "weakCompareAndSet":
            case "weakCompareAndSetAcquire":
            case "weakCompareAndSetPlain":
            case "weakCompareAndSetRelease":
            case "compareAndExchange":
            case "compareAndExchangeAcquire":
            case "compareAndExchangeRelease":
            case "get":
            case "getAcquire":
            case "getAndAdd":
            case "getAndAddAcquire":
            case "getAndAddRelease":
            case "getAndBitwiseAnd":
            case "getAndBitwiseAndAcquire":
            case "getAndBitwiseAndRelease":
            case "getAndBitwiseOr":
            case "getAndBitwiseOrAcquire":
            case "getAndBitwiseOrRelease":
            case "getAndBitwiseXor":
            case "getAndBitwiseXorAcquire":
            case "getAndBitwiseXorRelease":
            case "getAndSet":
            case "getAndSetAcquire":
            case "getAndSetRelease":
            case "getOpaque":
            case "getVolatile":
            case "set":
            case "setOpaque":
            case "setRelease":
            case "setVolatile":
                return owner.equals("java/lang/invoke/VarHandle");
            default:
                return false;
        }
    }
}

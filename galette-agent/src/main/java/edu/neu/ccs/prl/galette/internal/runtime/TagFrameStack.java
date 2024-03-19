package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.runtime.collection.Stack;
import edu.neu.ccs.prl.galette.internal.runtime.mask.MemberAccess;
import org.objectweb.asm.Opcodes;

public final class TagFrameStack {
    private static volatile boolean INITIALIZED = false;

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Thread", name = "$$GALETTE_STACK", opcode = Opcodes.GETFIELD)
    private static Stack<TagFrame> getStack(Thread thread) {
        throw new AssertionError("Placeholder method was called");
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Thread", name = "$$GALETTE_STACK", opcode = Opcodes.PUTFIELD)
    private static void putStack(Thread thread, Stack<TagFrame> frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @InvokedViaHandle(handle = Handle.FRAME_STACK_PEEK)
    public static TagFrame peek(String descriptor) {
        if (!INITIALIZED) {
            return new TagFrame();
        }
        Stack<TagFrame> stack = getStack(Thread.currentThread());
        if (stack == null || stack.isEmpty()) {
            return new TagFrame();
        }
        TagFrame frame = stack.peek();
        String callerDescriptor = frame.getCallerDescriptor();
        // TODO attempt to match using descriptor
        return frame;
    }

    @InvokedViaHandle(handle = Handle.FRAME_STACK_PUSH)
    public static void push(TagFrame frame) {
        if (!INITIALIZED || frame.getCallerDescriptor() == null) {
            return;
        }
        Stack<TagFrame> stack = getStack(Thread.currentThread());
        if (stack == null) {
            stack = new Stack<>();
            putStack(Thread.currentThread(), stack);
        }
        stack.push(frame);
    }

    @InvokedViaHandle(handle = Handle.FRAME_STACK_POP)
    public static void pop() {
        if (!INITIALIZED) {
            return;
        }
        Stack<TagFrame> stack = getStack(Thread.currentThread());
        if (stack != null && !stack.isEmpty()) {
            stack.pop();
        }
    }

    public static void initialize() {
        INITIALIZED = true;
    }
}

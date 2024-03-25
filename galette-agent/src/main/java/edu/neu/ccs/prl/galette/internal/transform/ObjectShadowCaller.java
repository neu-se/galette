package edu.neu.ccs.prl.galette.internal.transform;

import static edu.neu.ccs.prl.galette.internal.transform.IndirectFramePasser.getFrameElements;
import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.F_NEW;

import edu.neu.ccs.prl.galette.internal.runtime.TaggedObject;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AnalyzerAdapter;

/**
 * Replaces calls to non-final methods in {@link Object} with a conditional branch which remaps  the call to the
 * corresponding shadow if dispatched on a {@link TaggedObject} instance.
 */
class ObjectShadowCaller extends AnalyzerAdapter {
    ObjectShadowCaller(String owner, int access, String name, String descriptor, MethodVisitor methodVisitor) {
        super(GaletteTransformer.ASM_VERSION, owner, access, name, descriptor, methodVisitor);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        ObjectMethod objectMethod = ObjectMethod.findMatch(owner, name, descriptor);
        if (objectMethod != null && !objectMethod.isFinal() && opcode != INVOKESPECIAL) {
            visitObjectMethodInsn(opcode, owner, name, descriptor, isInterface, objectMethod);
        } else {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }

    private void visitObjectMethodInsn(
            int opcode, String owner, String name, String descriptor, boolean isInterface, ObjectMethod objectMethod) {
        Object[] localsPreCall = getFrameElements(super.locals);
        Object[] stackPreCall = getFrameElements(super.stack);
        Type[] arguments = Type.getArgumentTypes(objectMethod.getRecord().getDescriptor());
        duplicateReceiver(objectMethod, arguments);
        // Check if receiver is an instance of TaggedObject
        super.visitTypeInsn(INSTANCEOF, GaletteNames.TAGGED_OBJECT_INTERNAL_NAME);
        // Branch on result
        Label takenLabel = new Label();
        Label joinLabel = new Label();
        super.visitJumpInsn(IFEQ, takenLabel);
        // Cast the receiver
        castReceiver(arguments);
        // Call method on TaggedObject
        super.visitMethodInsn(INVOKEINTERFACE, GaletteNames.TAGGED_OBJECT_INTERNAL_NAME, name, descriptor, true);
        // Jump to the "join"
        super.visitJumpInsn(GOTO, joinLabel);
        // Start branch taken target
        visitLabelAndFrame(localsPreCall, stackPreCall, takenLabel);
        // Call the original method
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        // Rejoin the flow of control
        visitLabelAndFrame(getFrameElements(super.locals), getFrameElements(super.stack), joinLabel);
        // Insert a NOP to ensure there is an instruction between the added frame and the next frame
        super.visitInsn(NOP);
    }

    private void castReceiver(Type[] arguments) {
        if (arguments.length == 0) {
            super.visitTypeInsn(CHECKCAST, GaletteNames.TAGGED_OBJECT_INTERNAL_NAME);
        } else {
            super.visitInsn(SWAP);
            super.visitTypeInsn(CHECKCAST, GaletteNames.TAGGED_OBJECT_INTERNAL_NAME);
            super.visitInsn(SWAP);
        }
    }

    private void duplicateReceiver(ObjectMethod objectMethod, Type[] arguments) {
        if (arguments.length == 0) {
            // ..., receiver
            super.visitInsn(DUP);
            // ..., receiver, receiver
        } else if (arguments.length == 1) {
            // ..., receiver, arg0
            super.visitInsn(SWAP);
            super.visitInsn(DUP_X1);
            // ..., receiver, arg0, receiver
        } else {
            throw new AssertionError("Unknown java.lang.Object method " + objectMethod);
        }
    }

    private void visitLabelAndFrame(Object[] currentLocals, Object[] currentStack, Label label) {
        super.visitLabel(label);
        super.visitFrame(F_NEW, currentLocals.length, currentLocals, currentStack.length, currentStack);
    }
}

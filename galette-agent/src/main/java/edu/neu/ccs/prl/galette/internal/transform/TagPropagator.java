package edu.neu.ccs.prl.galette.internal.transform;

import static org.objectweb.asm.Opcodes.*;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.tree.MethodNode;

class TagPropagator extends MethodVisitor {
    private final ShadowLocals shadowLocals;

    private TagPropagator(ShadowLocals shadowLocals, MethodVisitor mv) {
        super(GaletteTransformer.ASM_VERSION, mv);
        if (shadowLocals == null) {
            throw new NullPointerException();
        }
        this.shadowLocals = shadowLocals;
    }

    @Override
    public void visitInsn(int opcode) {
        switch (opcode) {
            case Opcodes.NOP:
                break;
            case ACONST_NULL:
            case ICONST_M1:
            case ICONST_0:
            case ICONST_1:
            case ICONST_2:
            case ICONST_3:
            case ICONST_4:
            case ICONST_5:
            case FCONST_0:
            case FCONST_1:
            case FCONST_2:
                // ... -> ..., value
                Handle.TAG_GET_EMPTY.accept(mv);
                shadowLocals.push();
                break;
            case LCONST_0:
            case LCONST_1:
            case DCONST_0:
            case DCONST_1:
                // ... -> ..., value, top
                Handle.TAG_GET_EMPTY.accept(mv);
                shadowLocals.pushWide();
                break;
            case IALOAD:
            case FALOAD:
            case AALOAD:
            case BALOAD:
            case CALOAD:
            case SALOAD:
                // ..., arrayref, index -> ..., value (cat-1 element)
                emitArrayLoadHook(opcode);
                shadowLocals.pop(2);
                shadowLocals.push();
                break;
            case LALOAD:
            case DALOAD:
                // ..., arrayref, index -> ..., value, top (cat-2 element)
                emitArrayLoadHook(opcode);
                shadowLocals.pop(2);
                shadowLocals.pushWide();
                break;
            case IASTORE:
            case BASTORE:
            case CASTORE:
            case SASTORE:
                // ..., arrayref, index, value -> ... (int-typed cat-1 element)
                emitArrayStoreHook(opcode, Opcodes.ISTORE, Opcodes.ILOAD, false);
                shadowLocals.pop(3);
                break;
            case FASTORE:
                // ..., arrayref, index, value -> ... (float cat-1 element)
                emitArrayStoreHook(opcode, Opcodes.FSTORE, Opcodes.FLOAD, false);
                shadowLocals.pop(3);
                break;
            case AASTORE:
                // ..., arrayref, index, value -> ... (ref cat-1 element)
                emitArrayStoreHook(opcode, Opcodes.ASTORE, Opcodes.ALOAD, false);
                shadowLocals.pop(3);
                break;
            case LASTORE:
                // ..., arrayref, index, value, top -> ... (long cat-2 element)
                emitArrayStoreHook(opcode, Opcodes.LSTORE, Opcodes.LLOAD, true);
                shadowLocals.pop(4);
                break;
            case DASTORE:
                // ..., arrayref, index, value, top -> ... (double cat-2 element)
                emitArrayStoreHook(opcode, Opcodes.DSTORE, Opcodes.DLOAD, true);
                shadowLocals.pop(4);
                break;
            case Opcodes.POP:
                // ..., value -> ...
                shadowLocals.pop(1);
                break;
            case Opcodes.POP2:
                // ..., value1, value2 -> ...
                shadowLocals.pop(2);
                break;
            case Opcodes.DUP:
                // ..., value -> ..., value, value
                shadowLocals.peek(0);
                shadowLocals.push();
                break;
            case Opcodes.DUP_X1:
                // ..., value1, value2 -> ..., value2, value1, value2
                shadowLocals.performOperation(opcode, 2, 3);
                break;
            case Opcodes.DUP_X2:
                // ..., value1, value2, value3 -> ..., value3, value1, value2, value3
                shadowLocals.performOperation(opcode, 3, 4);
                break;
            case Opcodes.DUP2:
                // ..., value1, value2 -> ..., value1, value2, value1, value2
                shadowLocals.performOperation(opcode, 2, 4);
                break;
            case Opcodes.DUP2_X1:
                // ..., value1, value2, value3 -> ..., value2, value3, value1, value2, value3
                shadowLocals.performOperation(opcode, 3, 5);
                break;
            case Opcodes.DUP2_X2:
                // ..., value1, value2, value3, value4 -> ..., value3, value4, value1, value2, value3, value4
                shadowLocals.performOperation(opcode, 4, 6);
                break;
            case Opcodes.SWAP:
                shadowLocals.performOperation(opcode, 2, 2);
                break;
            case IADD:
            case ISUB:
            case IMUL:
            case IDIV:
            case IREM:
            case ISHL:
            case ISHR:
            case IUSHR:
            case IAND:
            case IOR:
            case IXOR:
                // ..., value1, value2 -> ..., result
                emitIntArithHook(opcode);
                shadowLocals.pop(2);
                shadowLocals.push();
                break;
            case FADD:
            case FSUB:
            case FMUL:
            case FDIV:
            case FREM:
            case FCMPL:
            case FCMPG:
                // ..., value1, value2 -> ..., result
                emitFloatArithHook(opcode);
                shadowLocals.pop(2);
                shadowLocals.push();
                break;
            case LADD:
            case LSUB:
            case LMUL:
            case LDIV:
            case LREM:
            case LAND:
            case LOR:
            case LXOR:
                // ..., value1, top, value2, top -> ..., result, top
                emitLongArithHook(opcode);
                shadowLocals.pop(4);
                shadowLocals.pushWide();
                break;
            case DADD:
            case DSUB:
            case DMUL:
            case DDIV:
            case DREM:
                // ..., value1, top, value2, top -> ..., result, top
                emitDoubleArithHook(opcode);
                shadowLocals.pop(4);
                shadowLocals.pushWide();
                break;
            case LSHL:
            case LUSHR:
            case LSHR:
                // ..., value1, top, value2 -> ..., result, top
                emitLongShiftHook(opcode);
                shadowLocals.pop(3);
                shadowLocals.pushWide();
                break;
            case LCMP:
                // ..., value1, top, value2, top -> ..., result
                emitLongCmpHook();
                shadowLocals.pop(4);
                shadowLocals.push();
                break;
            case DCMPL:
            case DCMPG:
                // ..., value1, top, value2, top -> ..., result
                emitDoubleCmpHook(opcode);
                shadowLocals.pop(4);
                shadowLocals.push();
                break;
            case Opcodes.INEG:
            case Opcodes.I2B:
            case Opcodes.I2C:
            case Opcodes.I2S:
                // ..., value -> ..., result
                emitIntUnaryHook(opcode);
                shadowLocals.pop(1);
                shadowLocals.push();
                break;
            case Opcodes.FNEG:
                // ..., value -> ..., result
                emitFloatUnaryHook(opcode);
                shadowLocals.pop(1);
                shadowLocals.push();
                break;
            case Opcodes.I2F:
            case Opcodes.F2I:
                // ..., value -> ..., result (cat-1 -> cat-1 conversion)
                emitCat1ConvertHook(opcode);
                shadowLocals.pop(1);
                shadowLocals.push();
                break;
            case Opcodes.LNEG:
                // ..., value, top -> ..., result, top
                emitLongUnaryHook(opcode);
                shadowLocals.pop(2);
                shadowLocals.pushWide();
                break;
            case Opcodes.DNEG:
                // ..., value, top -> ..., result, top
                emitDoubleUnaryHook(opcode);
                shadowLocals.pop(2);
                shadowLocals.pushWide();
                break;
            case Opcodes.L2D:
                // ..., value, top -> ..., result, top (cat-2 -> cat-2)
                emitLongConvertHook(opcode);
                shadowLocals.pop(2);
                shadowLocals.pushWide();
                break;
            case Opcodes.D2L:
                emitDoubleConvertHook(opcode);
                shadowLocals.pop(2);
                shadowLocals.pushWide();
                break;
            case Opcodes.I2L:
            case Opcodes.I2D:
                // ..., value -> ..., result, top (cat-1 -> cat-2)
                emitIntWidenHook(opcode);
                shadowLocals.pop(1);
                shadowLocals.pushWide();
                break;
            case Opcodes.F2L:
            case Opcodes.F2D:
                // ..., value -> ..., result, top (cat-1 float -> cat-2)
                emitFloatWidenHook(opcode);
                shadowLocals.pop(1);
                shadowLocals.pushWide();
                break;
            case Opcodes.L2I:
            case Opcodes.L2F:
                // ..., value, top -> ..., result (cat-2 -> cat-1)
                emitLongConvertHook(opcode);
                shadowLocals.pop(2);
                shadowLocals.push();
                break;
            case Opcodes.D2I:
            case Opcodes.D2F:
                // ..., value, top -> ..., result (cat-2 -> cat-1)
                emitDoubleConvertHook(opcode);
                shadowLocals.pop(2);
                shadowLocals.push();
                break;
            case Opcodes.IRETURN:
            case Opcodes.FRETURN:
            case Opcodes.ARETURN:
                // ..., value -> ...
                shadowLocals.peek(0);
                shadowLocals.getFrameManager().setReturnTag();
                shadowLocals.pop(1);
                break;
            case Opcodes.DRETURN:
            case Opcodes.LRETURN:
                // ..., value, top -> ...
                shadowLocals.peek(1);
                shadowLocals.getFrameManager().setReturnTag();
                shadowLocals.pop(2);
                break;
            case Opcodes.RETURN:
                // ..., -> []
                break;
            case Opcodes.ARRAYLENGTH:
                // ..., arrayref -> ..., length
                super.visitInsn(DUP);
                shadowLocals.peek(0);
                // arrayref, arrayref, arrayref-tag
                Handle.ARRAY_TAG_STORE_GET_LENGTH_TAG.accept(mv);
                // arrayref
                shadowLocals.pop(1);
                shadowLocals.push();
                break;
            case Opcodes.ATHROW:
                // ..., objectref -> []
                super.visitInsn(DUP);
                shadowLocals.peek(0);
                // objectref, objectref, objectref-tag
                Handle.EXCEPTION_STORE_SET.accept(mv);
                // objectref
                shadowLocals.pop(1);
                break;
            case Opcodes.MONITORENTER:
            case Opcodes.MONITOREXIT:
                // ..., objectref -> ...
                shadowLocals.pop(0);
                break;
            default:
                throw new IllegalArgumentException();
        }
        super.visitInsn(opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        switch (opcode) {
            case BIPUSH:
            case SIPUSH:
                // ... -> ..., value
                Handle.TAG_GET_EMPTY.accept(mv);
                shadowLocals.push();
                super.visitIntInsn(opcode, operand);
                break;
            case NEWARRAY:
                // ..., count -> ..., arrayref
                super.visitIntInsn(opcode, operand);
                // arrayref
                super.visitInsn(DUP);
                shadowLocals.peek(0);
                // arrayref, arrayref, count-tag
                Handle.ARRAY_TAG_STORE_SET_LENGTH_TAG.accept(mv);
                // arrayref
                Handle.TAG_GET_EMPTY.accept(mv);
                shadowLocals.pop(1);
                shadowLocals.push();
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public void visitVarInsn(int opcode, int varIndex) {
        switch (opcode) {
            case ILOAD:
            case FLOAD:
            case ALOAD:
                // ... -> ..., value
                shadowLocals.loadShadowVar(varIndex);
                shadowLocals.push();
                break;
            case DLOAD:
            case LLOAD:
                // ... -> ..., value, top
                shadowLocals.loadShadowVar(varIndex);
                shadowLocals.pushWide();
                break;
            case ISTORE:
            case FSTORE:
            case ASTORE:
                // ..., value -> ...
                shadowLocals.peek(0);
                shadowLocals.storeShadowVar(varIndex);
                shadowLocals.pop(1);
                break;
            case LSTORE:
            case DSTORE:
                // ..., value, top -> ...
                shadowLocals.peek(1);
                shadowLocals.storeShadowVar(varIndex);
                shadowLocals.pop(2);
                break;
            case RET:
                break;
            default:
                throw new IllegalArgumentException();
        }
        super.visitVarInsn(opcode, varIndex);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        switch (opcode) {
            case NEW:
                visitNew(type);
                break;
            case ANEWARRAY:
                visitNewReferenceArray(type);
                break;
            case CHECKCAST:
                visitCheckCast(type);
                break;
            case INSTANCEOF:
                visitInstanceOf(type);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void visitInstanceOf(String type) {
        // ..., objectref -> ..., result
        // No need to do anything for data flow propagation
        super.visitTypeInsn(INSTANCEOF, type);
    }

    private void visitCheckCast(String type) {
        // ..., objectref -> ..., objectref
        // No need to do anything for data flow propagation
        super.visitTypeInsn(CHECKCAST, type);
    }

    private void visitNewReferenceArray(String type) {
        // ..., count -> ..., arrayref
        super.visitTypeInsn(ANEWARRAY, type);
        // arrayref
        super.visitInsn(DUP);
        shadowLocals.peek(0);
        // arrayref, arrayref, count-tag
        Handle.ARRAY_TAG_STORE_SET_LENGTH_TAG.accept(mv);
        // arrayref
        Handle.TAG_GET_EMPTY.accept(mv);
        shadowLocals.pop(1);
        shadowLocals.push();
    }

    private void visitNew(String type) {
        // ... -> ..., objectref
        // Must visit the original NEW instruction first to keep it next to the label used to represent
        // it in stack frames
        super.visitTypeInsn(NEW, type);
        Handle.TAG_GET_EMPTY.accept(mv);
        shadowLocals.push();
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        switch (opcode) {
            case GETSTATIC:
                visitGetStatic(owner, name, descriptor);
                break;
            case PUTSTATIC:
                visitPutStatic(owner, name, descriptor);
                break;
            case GETFIELD:
                visitGetField(owner, name, descriptor);
                break;
            case PUTFIELD:
                visitPutField(owner, name, descriptor);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void visitPutField(String owner, String name, String descriptor) {
        // ..., objectref, value -> ...
        int valueSize = Type.getType(descriptor).getSize();
        if (isShadowedField(owner)) {
            prepareForPutField(valueSize);
            super.visitFieldInsn(
                    PUTFIELD, owner, ShadowFieldAdder.getShadowFieldName(name), ShadowFieldAdder.TAG_DESCRIPTOR);
        } else if (isMirroredField(owner, name, false)) {
            prepareForPutField(valueSize);
            super.visitLdcInsn(owner + '#' + name + '#' + descriptor);
            Handle.FIELD_TAG_STORE_PUT_FIELD.accept(mv);
        }
        // Remove the tags on the shadow stack for the slots consumed by this instruction
        shadowLocals.pop(valueSize + 1);
        super.visitFieldInsn(PUTFIELD, owner, name, descriptor);
    }

    private void prepareForPutField(int valueSize) {
        if (valueSize == 2) {
            // objectref, value, top
            super.visitInsn(DUP2_X1);
            // value, top, objectref, value, top
            super.visitInsn(POP2);
            // value, top, objectref
            super.visitInsn(DUP_X2);
            // objectref, value, top, objectref
            shadowLocals.peek(1);
            // objectref, value, top, objectref, value-tag
        } else {
            // objectref, value
            super.visitInsn(DUP2);
            // objectref, value, objectref, value
            super.visitInsn(POP);
            // objectref, value, objectref
            shadowLocals.peek(0);
            // objectref, value, objectref, value-tag
        }
    }

    private void visitGetField(String owner, String name, String descriptor) {
        // ..., objectref -> ..., value
        if (isShadowedField(owner)) {
            super.visitInsn(DUP);
            // objectref, objectref
            super.visitFieldInsn(
                    GETFIELD, owner, ShadowFieldAdder.getShadowFieldName(name), ShadowFieldAdder.TAG_DESCRIPTOR);
            // objectref, value-tag
        } else if (isMirroredField(owner, name, false)) {
            super.visitInsn(DUP);
            // objectref, objectref
            super.visitLdcInsn(owner + '#' + name + '#' + descriptor);
            Handle.FIELD_TAG_STORE_GET_FIELD.accept(mv);
            // objectref, value-tag
        } else {
            Handle.TAG_GET_EMPTY.accept(mv);
        }
        if (ShadowFieldAdder.isBoxedType(owner) && "value".equals(name)) {
            // Propagate from boxed type to its value
            shadowLocals.peek(0);
            Handle.TAG_UNION.accept(mv);
        }
        // Remove the tags on the shadow stack for the slot consumed by this instruction
        shadowLocals.pop(1);
        if (Type.getType(descriptor).getSize() == 2) {
            shadowLocals.pushWide();
        } else {
            shadowLocals.push();
        }
        super.visitFieldInsn(GETFIELD, owner, name, descriptor);
    }

    private void visitPutStatic(String owner, String name, String descriptor) {
        // ..., value -> ...
        int valueSize = Type.getType(descriptor).getSize();
        // value OR value, top
        if (isShadowedField(owner)) {
            shadowLocals.peek(valueSize - 1);
            super.visitFieldInsn(
                    PUTSTATIC, owner, ShadowFieldAdder.getShadowFieldName(name), ShadowFieldAdder.TAG_DESCRIPTOR);
        } else if (isMirroredField(owner, name, true)) {
            shadowLocals.peek(valueSize - 1);
            super.visitLdcInsn(owner + '#' + name + '#' + descriptor);
            Handle.FIELD_TAG_STORE_PUT_STATIC.accept(mv);
        }
        // value or value, top
        // Remove the tags on the shadow stack for the slots consumed by this instruction
        shadowLocals.pop(valueSize);
        super.visitFieldInsn(PUTSTATIC, owner, name, descriptor);
    }

    private void visitGetStatic(String owner, String name, String descriptor) {
        // ... -> ..., value
        if (ShadowFieldAdder.hasShadowFields(owner)) {
            super.visitFieldInsn(
                    GETSTATIC, owner, ShadowFieldAdder.getShadowFieldName(name), ShadowFieldAdder.TAG_DESCRIPTOR);
        } else if (isMirroredField(owner, name, true)) {
            super.visitLdcInsn(owner + '#' + name + '#' + descriptor);
            Handle.FIELD_TAG_STORE_GET_STATIC.accept(mv);
        } else {
            Handle.TAG_GET_EMPTY.accept(mv);
        }
        if (Type.getType(descriptor).getSize() == 2) {
            shadowLocals.pushWide();
        } else {
            shadowLocals.push();
        }
        super.visitFieldInsn(GETSTATIC, owner, name, descriptor);
    }

    @Override
    public void visitInvokeDynamicInsn(
            String name,
            String descriptor,
            org.objectweb.asm.Handle bootstrapMethodHandle,
            Object... bootstrapMethodArguments) {
        // Handled by IndirectFramePasser
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        switch (opcode) {
            case IFEQ:
            case IFNE:
            case IFLT:
            case IFGE:
            case IFGT:
            case IFLE:
                // ..., value -> ...
                emitIntBranchHook(opcode);
                shadowLocals.pop(1);
                break;
            case IFNULL:
            case IFNONNULL:
                // ..., value -> ...
                emitRefBranchHook(opcode);
                shadowLocals.pop(1);
                break;
            case IF_ICMPEQ:
            case IF_ICMPNE:
            case IF_ICMPLT:
            case IF_ICMPGE:
            case IF_ICMPGT:
            case IF_ICMPLE:
                // ..., value1, value2 -> ...
                emitIntCmpBranchHook(opcode, false);
                shadowLocals.pop(2);
                break;
            case IF_ACMPEQ:
            case IF_ACMPNE:
                // ..., value1, value2 -> ...
                emitIntCmpBranchHook(opcode, true);
                shadowLocals.pop(2);
                break;
            case GOTO:
                // ... -> ...
                break;
            case JSR:
                // ... -> ..., address
                Handle.TAG_GET_EMPTY.accept(mv);
                shadowLocals.push();
                break;
            default:
                throw new IllegalArgumentException();
        }
        super.visitJumpInsn(opcode, label);
    }

    @Override
    public void visitLdcInsn(Object value) {
        Handle.TAG_GET_EMPTY.accept(mv);
        if (value instanceof Double || value instanceof Long) {
            // ... -> ..., value, top
            shadowLocals.pushWide();
        } else {
            // ... -> ..., value
            shadowLocals.push();
        }
        super.visitLdcInsn(value);
    }

    @Override
    public void visitIincInsn(int varIndex, int increment) {
        // Emit the symbolic-listener hook. The listener returns the Tag
        // that should replace the local's shadow tag after the increment;
        // the default impl returns the input tag unchanged.
        super.visitLdcInsn(varIndex);
        super.visitLdcInsn(increment);
        shadowLocals.loadShadowVar(varIndex);
        Handle.SYMBOLIC_ON_IINC.accept(mv);
        // stack: [newTag]
        shadowLocals.storeShadowVar(varIndex);
        super.visitIincInsn(varIndex, increment);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        // ..., index -> ...
        emitTableSwitchHook(min, max);
        shadowLocals.pop(1);
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        // ..., key -> ...
        emitLookupSwitchHook(keys);
        shadowLocals.pop(1);
        super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        // ..., count1, [count2, ...] -> ..., arrayref
        super.visitMultiANewArrayInsn(descriptor, numDimensions);
        // arrayref
        super.visitInsn(DUP);
        // arrayref, arrayref
        AsmUtil.pushInt(mv, numDimensions);
        super.visitTypeInsn(ANEWARRAY, Type.getInternalName(Tag.class));
        // arrayref, arrayref, tag-array
        int index = 0;
        for (int i = numDimensions - 1; i >= 0; i--, index++) {
            super.visitInsn(DUP);
            AsmUtil.pushInt(mv, index);
            shadowLocals.peek(i);
            // arrayref, arrayref, tag-array, tag-array, count-tag
            super.visitInsn(AASTORE);
            // arrayref, arrayref, tag-array
        }
        // arrayref, arrayref, tag-array
        Handle.ARRAY_TAG_STORE_SET_LENGTH_TAGS.accept(mv);
        // arrayref
        // Set the tag for the newly created array in the shadow stack
        Handle.TAG_GET_EMPTY.accept(mv);
        shadowLocals.pop(numDimensions);
        shadowLocals.push();
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (IndirectFramePasser.isSignaturePolymorphic(owner, name)) {
            // Handled by IndirectFramePasser
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        } else {
            boolean createFrame = !isGetCallerClass(owner, name, descriptor) && !isIgnoredMethod(owner, name);
            // Consume tags from the shadow stack for the arguments of the call
            shadowLocals.prepareForCall(opcode == INVOKESTATIC, descriptor, createFrame);
            if (createFrame) {
                // Directly pass the frame as a shadow argument
                descriptor = ShadowMethodCreator.getShadowMethodDescriptor(descriptor);
            }
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            if (isGetCallerClass(owner, name, descriptor)) {
                // Check for a stored caller class
                // [returned-caller]
                shadowLocals.getFrameManager().loadCaller();
                // [return-caller, stored-caller]
                Handle.GET_CALLER_HELPER.accept(mv);
                // [resolved-caller]
            }
            // Set the tag for the return value
            shadowLocals.restoreFromCall(descriptor, createFrame);
        }
    }

    private static boolean isGetCallerClass(String owner, String name, String descriptor) {
        // The result of getCallerClass depends on the caller, so we cannot use the native wrapper without changing the
        // caller
        if (name.equals("getCallerClass") && "()Ljava/lang/Class;".equals(descriptor)) {
            return owner.equals("jdk/internal/reflect/Reflection") || owner.equals("sun/reflect/Reflection");
        }
        return false;
    }

    private static boolean isShadowedField(String owner) {
        return !isIgnoredClass(owner) && ShadowFieldAdder.hasShadowFields(owner);
    }

    private static boolean isIgnoredClass(String owner) {
        // Internal tainting class are not instrumented but should be treated as though they were
        // They are expected to declare shadow members for all of their members that are accessible
        // from instrumented classes
        if (Configuration.isInternalTaintingClass(owner)) {
            return false;
        }
        // We cannot add shadow methods to Object or arrays
        if (owner.equals("java/lang/Object") || owner.startsWith("[")) {
            return true;
        }
        // Shadows are not created for classes explicitly excluded
        return GaletteTransformer.isExcluded(owner);
    }

    private static boolean isIgnoredMethod(String owner, String name) {
        // Needed for compatability with JProfiler's agent
        if (owner.startsWith("com/jprofiler")) {
            return true;
        }
        return isIgnoredClass(owner) || !ShadowMethodCreator.shouldShadow(name);
    }

    private static boolean isMirroredField(String owner, String name, boolean isStatic) {
        // Cannot mirror tags for Reference
        return !ShadowFieldAdder.hasShadowFields(owner) && !owner.equals("java/lang/ref/Reference");
    }

    /**
     * Emits a call to {@code SymbolicListener.onIntBranch(opcode, value, tag)}
     * immediately before a single-operand integer conditional jump. Does not
     * alter the runtime stack: the original {@code value} is left on top for
     * the subsequent {@code IFxx} instruction to consume.
     */
    private void emitIntBranchHook(int opcode) {
        // Start: [..., value]
        super.visitInsn(Opcodes.DUP);
        // [..., value, value]
        super.visitLdcInsn(opcode);
        // [..., value, value, opcode]
        super.visitInsn(Opcodes.SWAP);
        // [..., value, opcode, value]
        shadowLocals.peek(0);
        // [..., value, opcode, value, tag]
        Handle.SYMBOLIC_ON_INT_BRANCH.accept(mv);
        // [..., value]
    }

    /**
     * Emits a call to {@code SymbolicListener.onRefBranch(opcode, value, tag)}
     * immediately before {@code IFNULL} / {@code IFNONNULL}. Leaves the
     * original reference on the stack for the subsequent jump.
     */
    private void emitRefBranchHook(int opcode) {
        // Start: [..., ref]
        super.visitInsn(Opcodes.DUP);
        super.visitLdcInsn(opcode);
        super.visitInsn(Opcodes.SWAP);
        shadowLocals.peek(0);
        Handle.SYMBOLIC_ON_REF_BRANCH.accept(mv);
        // [..., ref]
    }

    /**
     * Emits a call to {@code SymbolicListener.onIntCmpBranch} or
     * {@code onRefCmpBranch}, depending on {@code refs}, before a two-operand
     * conditional jump ({@code IF_ICMPxx} / {@code IF_ACMPxx}). Leaves both
     * operands on the stack.
     */
    private void emitIntCmpBranchHook(int opcode, boolean refs) {
        // Start: [..., value1, value2]
        super.visitInsn(Opcodes.DUP2);
        // [..., value1, value2, value1, value2]
        super.visitLdcInsn(opcode);
        // [..., value1, value2, value1, value2, opcode]
        super.visitInsn(Opcodes.DUP_X2);
        // [..., value1, value2, opcode, value1, value2, opcode]
        super.visitInsn(Opcodes.POP);
        // [..., value1, value2, opcode, value1, value2]
        shadowLocals.peek(1);
        shadowLocals.peek(0);
        // [..., value1, value2, opcode, value1, value2, tag1, tag2]
        if (refs) {
            Handle.SYMBOLIC_ON_REF_CMP_BRANCH.accept(mv);
        } else {
            Handle.SYMBOLIC_ON_INT_CMP_BRANCH.accept(mv);
        }
        // [..., value1, value2]
    }

    /**
     * Emits a call to {@code SymbolicListener.onTableSwitch(opcode, value, tag, min, max)}
     * immediately before a {@code TABLESWITCH}. Leaves the original key on
     * the stack.
     */
    private void emitTableSwitchHook(int min, int max) {
        // Start: [..., value]
        super.visitInsn(Opcodes.DUP);
        // [..., value, value]
        super.visitLdcInsn(Opcodes.TABLESWITCH);
        super.visitInsn(Opcodes.SWAP);
        // [..., value, opcode, value]
        shadowLocals.peek(0);
        // [..., value, opcode, value, tag]
        super.visitLdcInsn(min);
        super.visitLdcInsn(max);
        // [..., value, opcode, value, tag, min, max]
        Handle.SYMBOLIC_ON_TABLE_SWITCH.accept(mv);
        // [..., value]
    }

    /**
     * Emits a call to {@code SymbolicListener.onLookupSwitch(opcode, value, tag, keys)}
     * immediately before a {@code LOOKUPSWITCH}. Builds the keys array at
     * runtime and leaves the original key on the stack.
     */
    private void emitLookupSwitchHook(int[] keys) {
        // Start: [..., value]
        super.visitInsn(Opcodes.DUP);
        // [..., value, value]
        super.visitLdcInsn(Opcodes.LOOKUPSWITCH);
        super.visitInsn(Opcodes.SWAP);
        // [..., value, opcode, value]
        shadowLocals.peek(0);
        // [..., value, opcode, value, tag]
        super.visitLdcInsn(keys.length);
        super.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_INT);
        // [..., value, opcode, value, tag, int[]]
        for (int i = 0; i < keys.length; i++) {
            super.visitInsn(Opcodes.DUP);
            super.visitLdcInsn(i);
            super.visitLdcInsn(keys[i]);
            super.visitInsn(Opcodes.IASTORE);
        }
        Handle.SYMBOLIC_ON_LOOKUP_SWITCH.accept(mv);
        // [..., value]
    }

    /**
     * Emits a call to {@code SymbolicListener.onIntArith} in place of the
     * default {@link Handle#TAG_UNION}. Leaves both operand values on the
     * runtime stack (for the subsequent arithmetic opcode to consume) and
     * leaves the listener-returned tag on top for {@code shadowLocals.push()}.
     */
    private void emitIntArithHook(int opcode) {
        // Start: [..., value1, value2]
        super.visitInsn(Opcodes.DUP2);
        super.visitLdcInsn(opcode);
        super.visitInsn(Opcodes.DUP_X2);
        super.visitInsn(Opcodes.POP);
        // [..., value1, value2, opcode, value1, value2]
        shadowLocals.peek(1);
        shadowLocals.peek(0);
        // [..., value1, value2, opcode, value1, value2, tag1, tag2]
        Handle.SYMBOLIC_ON_INT_ARITH.accept(mv);
        // [..., value1, value2, result-tag]
    }

    /**
     * Emits a call to {@code SymbolicListener.onIntUnary} replacing the
     * default pass-through tag propagation. Leaves the value on the stack
     * for the subsequent unary opcode to consume and the result tag on top.
     */
    private void emitIntUnaryHook(int opcode) {
        // Start: [..., value]
        super.visitInsn(Opcodes.DUP);
        super.visitLdcInsn(opcode);
        super.visitInsn(Opcodes.SWAP);
        shadowLocals.peek(0);
        // [..., value, opcode, value, tag]
        Handle.SYMBOLIC_ON_INT_UNARY.accept(mv);
        // [..., value, result-tag]
    }

    /**
     * Emits a call to {@code SymbolicListener.onFloatArith}. Operands are
     * two floats (cat-1 each). See {@link #emitIntArithHook} for the stack
     * gymnastics rationale.
     */
    private void emitFloatArithHook(int opcode) {
        // Start: [..., value1, value2]
        super.visitInsn(Opcodes.DUP2);
        super.visitLdcInsn(opcode);
        super.visitInsn(Opcodes.DUP_X2);
        super.visitInsn(Opcodes.POP);
        // [..., value1, value2, opcode, value1, value2]
        shadowLocals.peek(1);
        shadowLocals.peek(0);
        // [..., value1, value2, opcode, value1, value2, tag1, tag2]
        Handle.SYMBOLIC_ON_FLOAT_ARITH.accept(mv);
        // [..., value1, value2, result-tag]
    }

    /**
     * Emits a call to {@code SymbolicListener.onFloatUnary} for {@code FNEG}.
     */
    private void emitFloatUnaryHook(int opcode) {
        // Start: [..., value]
        super.visitInsn(Opcodes.DUP);
        super.visitLdcInsn(opcode);
        super.visitInsn(Opcodes.SWAP);
        shadowLocals.peek(0);
        // [..., value, opcode, value, tag]
        Handle.SYMBOLIC_ON_FLOAT_UNARY.accept(mv);
        // [..., value, result-tag]
    }

    /**
     * Emits a call to {@code SymbolicListener.onCat1Convert} for {@code I2F}
     * / {@code F2I}. The value is not passed (conversion semantics are fully
     * determined by the opcode). Leaves value + tag on the stack.
     */
    private void emitCat1ConvertHook(int opcode) {
        // Start: [..., value]
        super.visitLdcInsn(opcode);
        // [..., value, opcode]
        shadowLocals.peek(0);
        // [..., value, opcode, tag]
        Handle.SYMBOLIC_ON_CAT1_CONVERT.accept(mv);
        // [..., value, result-tag]
    }

    // ---------- cat-2 arithmetic emits ----------

    /**
     * Emits a call to {@code SymbolicListener.onLongArith} for long binary ops.
     * Values (cat-2) are shuffled through temp local slots so the listener
     * receives both operand values and tags. Leaves the two longs on the
     * stack for the subsequent arithmetic opcode to consume.
     */
    private void emitLongArithHook(int opcode) {
        int v2slot = shadowLocals.tempSlot(0);
        int v1slot = shadowLocals.tempSlot(2);
        super.visitVarInsn(Opcodes.LSTORE, v2slot);
        super.visitVarInsn(Opcodes.LSTORE, v1slot);
        super.visitVarInsn(Opcodes.LLOAD, v1slot);
        super.visitVarInsn(Opcodes.LLOAD, v2slot);
        super.visitLdcInsn(opcode);
        super.visitVarInsn(Opcodes.LLOAD, v1slot);
        super.visitVarInsn(Opcodes.LLOAD, v2slot);
        shadowLocals.peek(3);
        shadowLocals.peek(1);
        Handle.SYMBOLIC_ON_LONG_ARITH.accept(mv);
        // [v1, v2, resultTag]
    }

    /** Emit for LSHL/LSHR/LUSHR: (long, int) -> long. */
    private void emitLongShiftHook(int opcode) {
        int v2slot = shadowLocals.tempSlot(0); // 1 slot for int shift amount
        int v1slot = shadowLocals.tempSlot(1); // 2 slots for long value
        super.visitVarInsn(Opcodes.ISTORE, v2slot);
        super.visitVarInsn(Opcodes.LSTORE, v1slot);
        super.visitVarInsn(Opcodes.LLOAD, v1slot);
        super.visitVarInsn(Opcodes.ILOAD, v2slot);
        super.visitLdcInsn(opcode);
        super.visitVarInsn(Opcodes.LLOAD, v1slot);
        super.visitVarInsn(Opcodes.ILOAD, v2slot);
        shadowLocals.peek(2);
        shadowLocals.peek(0);
        Handle.SYMBOLIC_ON_LONG_SHIFT.accept(mv);
        // [v1, v2, resultTag]
    }

    /** Emit for LCMP: (long, long) -> int. */
    private void emitLongCmpHook() {
        int v2slot = shadowLocals.tempSlot(0);
        int v1slot = shadowLocals.tempSlot(2);
        super.visitVarInsn(Opcodes.LSTORE, v2slot);
        super.visitVarInsn(Opcodes.LSTORE, v1slot);
        super.visitVarInsn(Opcodes.LLOAD, v1slot);
        super.visitVarInsn(Opcodes.LLOAD, v2slot);
        super.visitVarInsn(Opcodes.LLOAD, v1slot);
        super.visitVarInsn(Opcodes.LLOAD, v2slot);
        shadowLocals.peek(3);
        shadowLocals.peek(1);
        Handle.SYMBOLIC_ON_LONG_CMP.accept(mv);
        // [v1, v2, resultTag]
    }

    /** Emit for LNEG: long -> long. */
    private void emitLongUnaryHook(int opcode) {
        int vslot = shadowLocals.tempSlot(0);
        super.visitVarInsn(Opcodes.LSTORE, vslot);
        super.visitVarInsn(Opcodes.LLOAD, vslot);
        super.visitLdcInsn(opcode);
        super.visitVarInsn(Opcodes.LLOAD, vslot);
        shadowLocals.peek(1);
        Handle.SYMBOLIC_ON_LONG_UNARY.accept(mv);
        // [v, resultTag]
    }

    /** Emit for DADD/DSUB/DMUL/DDIV/DREM: (double, double) -> double. */
    private void emitDoubleArithHook(int opcode) {
        int v2slot = shadowLocals.tempSlot(0);
        int v1slot = shadowLocals.tempSlot(2);
        super.visitVarInsn(Opcodes.DSTORE, v2slot);
        super.visitVarInsn(Opcodes.DSTORE, v1slot);
        super.visitVarInsn(Opcodes.DLOAD, v1slot);
        super.visitVarInsn(Opcodes.DLOAD, v2slot);
        super.visitLdcInsn(opcode);
        super.visitVarInsn(Opcodes.DLOAD, v1slot);
        super.visitVarInsn(Opcodes.DLOAD, v2slot);
        shadowLocals.peek(3);
        shadowLocals.peek(1);
        Handle.SYMBOLIC_ON_DOUBLE_ARITH.accept(mv);
        // [v1, v2, resultTag]
    }

    /** Emit for DCMPL/DCMPG: (double, double) -> int. */
    private void emitDoubleCmpHook(int opcode) {
        int v2slot = shadowLocals.tempSlot(0);
        int v1slot = shadowLocals.tempSlot(2);
        super.visitVarInsn(Opcodes.DSTORE, v2slot);
        super.visitVarInsn(Opcodes.DSTORE, v1slot);
        super.visitVarInsn(Opcodes.DLOAD, v1slot);
        super.visitVarInsn(Opcodes.DLOAD, v2slot);
        super.visitLdcInsn(opcode);
        super.visitVarInsn(Opcodes.DLOAD, v1slot);
        super.visitVarInsn(Opcodes.DLOAD, v2slot);
        shadowLocals.peek(3);
        shadowLocals.peek(1);
        Handle.SYMBOLIC_ON_DOUBLE_CMP.accept(mv);
        // [v1, v2, resultTag]
    }

    /** Emit for DNEG. */
    private void emitDoubleUnaryHook(int opcode) {
        int vslot = shadowLocals.tempSlot(0);
        super.visitVarInsn(Opcodes.DSTORE, vslot);
        super.visitVarInsn(Opcodes.DLOAD, vslot);
        super.visitLdcInsn(opcode);
        super.visitVarInsn(Opcodes.DLOAD, vslot);
        shadowLocals.peek(1);
        Handle.SYMBOLIC_ON_DOUBLE_UNARY.accept(mv);
        // [v, resultTag]
    }

    // ---------- size-changing conversions ----------

    /** Emit for I2L / I2D: int -> cat-2. */
    private void emitIntWidenHook(int opcode) {
        // Start: [v_int]
        super.visitInsn(Opcodes.DUP);
        super.visitLdcInsn(opcode);
        super.visitInsn(Opcodes.SWAP);
        shadowLocals.peek(0);
        Handle.SYMBOLIC_ON_INT_WIDEN.accept(mv);
        // [v, resultTag]
    }

    /** Emit for F2L / F2D: float -> cat-2. */
    private void emitFloatWidenHook(int opcode) {
        super.visitInsn(Opcodes.DUP);
        super.visitLdcInsn(opcode);
        super.visitInsn(Opcodes.SWAP);
        shadowLocals.peek(0);
        Handle.SYMBOLIC_ON_FLOAT_WIDEN.accept(mv);
        // [v, resultTag]
    }

    /** Emit for L2I / L2F / L2D. */
    private void emitLongConvertHook(int opcode) {
        int vslot = shadowLocals.tempSlot(0);
        super.visitVarInsn(Opcodes.LSTORE, vslot);
        super.visitVarInsn(Opcodes.LLOAD, vslot);
        super.visitLdcInsn(opcode);
        super.visitVarInsn(Opcodes.LLOAD, vslot);
        shadowLocals.peek(1);
        Handle.SYMBOLIC_ON_LONG_CONVERT.accept(mv);
        // [v, resultTag]
    }

    /** Emit for D2I / D2F / D2L. */
    private void emitDoubleConvertHook(int opcode) {
        int vslot = shadowLocals.tempSlot(0);
        super.visitVarInsn(Opcodes.DSTORE, vslot);
        super.visitVarInsn(Opcodes.DLOAD, vslot);
        super.visitLdcInsn(opcode);
        super.visitVarInsn(Opcodes.DLOAD, vslot);
        shadowLocals.peek(1);
        Handle.SYMBOLIC_ON_DOUBLE_CONVERT.accept(mv);
        // [v, resultTag]
    }

    // ---------- arrays ----------

    /**
     * Emit for array loads (IALOAD/FALOAD/AALOAD/BALOAD/CALOAD/SALOAD/LALOAD/DALOAD).
     * Computes the element tag via {@code ARRAY_TAG_STORE_GET_TAG}, then passes
     * (opcode, array, index, arrayTag, indexTag, elemTag) to
     * {@code SymbolicListener.onArrayLoad} and stores the result as the new
     * value tag. Leaves [array, index, resultTag] on the stack for the outer
     * caller's {@code pop(2); push()/pushWide()} pattern and the subsequent
     * load opcode.
     */
    private void emitArrayLoadHook(int opcode) {
        int idxSlot = shadowLocals.tempSlot(0);
        int arrSlot = shadowLocals.tempSlot(1);
        int tagSlot = shadowLocals.tempSlot(2);
        // start: [array, index]; shadow [..aT iT]
        super.visitVarInsn(Opcodes.ISTORE, idxSlot);
        super.visitVarInsn(Opcodes.ASTORE, arrSlot);
        // Compute elemTag via ArrayTagStore
        super.visitVarInsn(Opcodes.ALOAD, arrSlot);
        super.visitVarInsn(Opcodes.ILOAD, idxSlot);
        super.visitInsn(Opcodes.DUP2);
        shadowLocals.peek(1);
        shadowLocals.peek(0);
        Handle.ARRAY_TAG_STORE_GET_TAG.accept(mv);
        // [array, index, elemTag]
        super.visitVarInsn(Opcodes.ASTORE, tagSlot);
        super.visitInsn(Opcodes.POP2);
        // Call listener
        super.visitLdcInsn(opcode);
        super.visitVarInsn(Opcodes.ALOAD, arrSlot);
        super.visitVarInsn(Opcodes.ILOAD, idxSlot);
        shadowLocals.peek(1);
        shadowLocals.peek(0);
        super.visitVarInsn(Opcodes.ALOAD, tagSlot);
        Handle.SYMBOLIC_ON_ARRAY_LOAD.accept(mv);
        // [resultTag]
        super.visitVarInsn(Opcodes.ASTORE, tagSlot);
        super.visitVarInsn(Opcodes.ALOAD, arrSlot);
        super.visitVarInsn(Opcodes.ILOAD, idxSlot);
        super.visitVarInsn(Opcodes.ALOAD, tagSlot);
        // [array, index, resultTag]
    }

    /**
     * Emit for array stores. Calls listener, uses its returned tag as the
     * value tag passed to {@code ARRAY_TAG_STORE_SET_TAG}, then rebuilds
     * [array, index, value] on the stack for the subsequent store opcode.
     *
     * @param opcode the array-store opcode
     * @param valStoreOpc the local-variable store opcode matching value type
     * @param valLoadOpc the matching load opcode
     * @param wide true if the value is a cat-2 (long/double) value
     */
    private void emitArrayStoreHook(int opcode, int valStoreOpc, int valLoadOpc, boolean wide) {
        int valSlot = shadowLocals.tempSlot(0);
        int idxSlot = shadowLocals.tempSlot(wide ? 2 : 1);
        int arrSlot = shadowLocals.tempSlot(wide ? 3 : 2);
        int tagSlot = shadowLocals.tempSlot(wide ? 4 : 3);
        // start: [array, index, value(s)]; shadow [aT, iT, vT(+null)]
        super.visitVarInsn(valStoreOpc, valSlot);
        super.visitVarInsn(Opcodes.ISTORE, idxSlot);
        super.visitVarInsn(Opcodes.ASTORE, arrSlot);
        // Call listener
        super.visitLdcInsn(opcode);
        super.visitVarInsn(Opcodes.ALOAD, arrSlot);
        super.visitVarInsn(Opcodes.ILOAD, idxSlot);
        if (wide) {
            shadowLocals.peek(3);
            shadowLocals.peek(2);
            shadowLocals.peek(1);
        } else {
            shadowLocals.peek(2);
            shadowLocals.peek(1);
            shadowLocals.peek(0);
        }
        Handle.SYMBOLIC_ON_ARRAY_STORE.accept(mv);
        // [newValueTag]
        super.visitVarInsn(Opcodes.ASTORE, tagSlot);
        // Mirror into ArrayTagStore
        super.visitVarInsn(Opcodes.ALOAD, arrSlot);
        super.visitVarInsn(Opcodes.ILOAD, idxSlot);
        if (wide) {
            shadowLocals.peek(3);
            shadowLocals.peek(2);
        } else {
            shadowLocals.peek(2);
            shadowLocals.peek(1);
        }
        super.visitVarInsn(Opcodes.ALOAD, tagSlot);
        Handle.ARRAY_TAG_STORE_SET_TAG.accept(mv);
        // Rebuild stack for the subsequent store opcode
        super.visitVarInsn(Opcodes.ALOAD, arrSlot);
        super.visitVarInsn(Opcodes.ILOAD, idxSlot);
        super.visitVarInsn(valLoadOpc, valSlot);
        // [array, index, value(s)]
    }

    static MethodVisitor newInstance(MethodVisitor mv, MethodNode original, boolean isShadow, String owner) {
        ShadowLocals shadowLocals = ShadowLocals.newInstance(mv, original, isShadow);
        TagPropagator propagator = new TagPropagator(shadowLocals, shadowLocals);
        AnalyzerAdapter analyzer =
                new AnalyzerAdapter(owner, original.access, original.name, original.desc, propagator);
        IndirectFramePasser iPasser = new IndirectFramePasser(shadowLocals, analyzer, analyzer);
        return new ObjectShadowCaller(owner, original.access, original.name, original.desc, iPasser);
    }
}

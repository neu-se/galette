package edu.neu.ccs.prl.galette.example;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

@SuppressWarnings("unused")
public class NodeInstructionExamples {
    public static final String OWNER = Type.getInternalName(NodeInstructionExamples.class);

    public static MethodNode pushPopConstants() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "pushPopConstants", "()V", null, null);
        methodNode.visitCode();
        methodNode.visitInsn(ACONST_NULL);
        methodNode.visitInsn(ICONST_M1);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(ICONST_1);
        methodNode.visitInsn(ICONST_2);
        methodNode.visitInsn(ICONST_3);
        methodNode.visitInsn(ICONST_4);
        methodNode.visitInsn(ICONST_5);
        methodNode.visitInsn(FCONST_0);
        methodNode.visitInsn(FCONST_1);
        methodNode.visitInsn(FCONST_2);
        methodNode.visitIntInsn(BIPUSH, 17);
        methodNode.visitIntInsn(SIPUSH, 34);
        methodNode.visitLdcInsn("Hello");
        methodNode.visitInsn(DCONST_0);
        methodNode.visitInsn(DCONST_1);
        methodNode.visitInsn(LCONST_0);
        methodNode.visitInsn(LCONST_1);
        for (int i = 0; i < 4; i++) {
            methodNode.visitInsn(POP2);
        }
        for (int i = 0; i < 14; i++) {
            methodNode.visitInsn(POP);
        }
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(18, 1);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode loadLocals() {
        MethodNode methodNode =
                new MethodNode(ACC_PUBLIC + ACC_STATIC, "loadLocals", "(IJFDLjava/lang/Object;)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitVarInsn(FLOAD, 3);
        methodNode.visitVarInsn(DLOAD, 4);
        methodNode.visitVarInsn(ALOAD, 6);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(7, 7);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode storeLocals() {
        MethodNode methodNode =
                new MethodNode(ACC_PUBLIC + ACC_STATIC, "storeLocals", "(IJFDLjava/lang/Object;)V", null, null);
        methodNode.visitCode();
        methodNode.visitInsn(ICONST_0);
        methodNode.visitVarInsn(ISTORE, 0);
        methodNode.visitInsn(LCONST_0);
        methodNode.visitVarInsn(LSTORE, 1);
        methodNode.visitInsn(FCONST_0);
        methodNode.visitVarInsn(FSTORE, 3);
        methodNode.visitInsn(DCONST_0);
        methodNode.visitVarInsn(DSTORE, 4);
        methodNode.visitInsn(ACONST_NULL);
        methodNode.visitVarInsn(ASTORE, 6);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(2, 7);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode loadArrayElements() {
        MethodNode methodNode = new MethodNode(
                ACC_PUBLIC + ACC_STATIC, "loadArrayElements", "([I[J[F[D[B[C[S[Ljava/lang/Object;)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ALOAD, 0);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(IALOAD);
        methodNode.visitVarInsn(ALOAD, 1);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(LALOAD);
        methodNode.visitVarInsn(ALOAD, 2);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(FALOAD);
        methodNode.visitVarInsn(ALOAD, 3);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(DALOAD);
        methodNode.visitVarInsn(ALOAD, 4);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(BALOAD);
        methodNode.visitVarInsn(ALOAD, 5);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(CALOAD);
        methodNode.visitVarInsn(ALOAD, 6);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(SALOAD);
        methodNode.visitVarInsn(ALOAD, 7);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(AALOAD);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(11, 8);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode storeArrayElements() {
        MethodNode methodNode = new MethodNode(
                ACC_PUBLIC + ACC_STATIC, "storeArrayElements", "([I[J[F[D[B[C[S[Ljava/lang/Object;)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ALOAD, 0);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(IASTORE);
        methodNode.visitVarInsn(ALOAD, 1);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(LCONST_0);
        methodNode.visitInsn(LASTORE);
        methodNode.visitVarInsn(ALOAD, 2);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(FCONST_0);
        methodNode.visitInsn(FASTORE);
        methodNode.visitVarInsn(ALOAD, 3);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(DCONST_0);
        methodNode.visitInsn(DASTORE);
        methodNode.visitVarInsn(ALOAD, 4);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(BASTORE);
        methodNode.visitVarInsn(ALOAD, 5);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(CASTORE);
        methodNode.visitVarInsn(ALOAD, 6);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(SASTORE);
        methodNode.visitVarInsn(ALOAD, 7);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(ACONST_NULL);
        methodNode.visitInsn(AASTORE);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(4, 8);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode dup() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "dup", "()V", null, null);
        methodNode.visitCode();
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(DUP);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(2, 0);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode dup_x1() {
        // v2 v1 -> v1 v2 v1
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "dup_x1", "()V", null, null);
        methodNode.visitCode();
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(ICONST_1);
        methodNode.visitInsn(DUP_X1);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(3, 0);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode dup_x2() {
        // {value3, value2}, value1 -> value1, {value3, value2}, value1
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "dup_x2", "()V", null, null);
        methodNode.visitCode();
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(ICONST_1);
        methodNode.visitInsn(ICONST_2);
        methodNode.visitInsn(DUP_X2);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP2);
        //
        methodNode.visitInsn(LCONST_0);
        methodNode.visitInsn(ICONST_1);
        methodNode.visitInsn(DUP_X2);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(4, 0);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode dup2() {
        // {value2, value1} -> {value2, value1}, {value2, value1}
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "dup2", "()V", null, null);
        methodNode.visitCode();
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(ICONST_1);
        methodNode.visitInsn(DUP2);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP2);
        //
        methodNode.visitInsn(LCONST_0);
        methodNode.visitInsn(DUP2);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(4, 0);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode dup2_x1() {
        // value3, {value2, value1} -> {value2, value1}, value3, {value2, value1}
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "dup2_x1", "()V", null, null);
        methodNode.visitCode();
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(ICONST_1);
        methodNode.visitInsn(ICONST_2);
        methodNode.visitInsn(DUP2_X1);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP);
        //
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(LCONST_1);
        methodNode.visitInsn(DUP2_X1);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(5, 0);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode dup2_x2() {
        // {value4, value3}, {value2, value1} -> {value2, value1}, {value4, value3}, {value2, value1}
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "dup2_x2", "()V", null, null);
        methodNode.visitCode();
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(ICONST_1);
        methodNode.visitInsn(ICONST_2);
        methodNode.visitInsn(ICONST_3);
        methodNode.visitInsn(DUP2_X2);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP2);
        //
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(ICONST_1);
        methodNode.visitInsn(LCONST_1);
        methodNode.visitInsn(DUP2_X2);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP2);
        //
        methodNode.visitInsn(LCONST_0);
        methodNode.visitInsn(ICONST_1);
        methodNode.visitInsn(ICONST_2);
        methodNode.visitInsn(DUP2_X2);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP2);
        //
        methodNode.visitInsn(LCONST_0);
        methodNode.visitInsn(LCONST_1);
        methodNode.visitInsn(DUP2_X2);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(6, 0);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode swap() {
        // value2, value1 -> value1, value2
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "swap", "()V", null, null);
        methodNode.visitCode();
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(ICONST_1);
        methodNode.visitInsn(SWAP);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(2, 0);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode add() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "add", "(IJFD)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(IADD);
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitInsn(LADD);
        methodNode.visitVarInsn(FLOAD, 3);
        methodNode.visitVarInsn(FLOAD, 3);
        methodNode.visitInsn(FADD);
        methodNode.visitVarInsn(DLOAD, 4);
        methodNode.visitVarInsn(DLOAD, 4);
        methodNode.visitInsn(DADD);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(8, 6);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode subtract() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "subtract", "(IJFD)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(ISUB);
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitInsn(LSUB);
        methodNode.visitVarInsn(FLOAD, 3);
        methodNode.visitVarInsn(FLOAD, 3);
        methodNode.visitInsn(FSUB);
        methodNode.visitVarInsn(DLOAD, 4);
        methodNode.visitVarInsn(DLOAD, 4);
        methodNode.visitInsn(DSUB);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(8, 6);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode multiply() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "multiply", "(IJFD)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(IMUL);
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitInsn(LMUL);
        methodNode.visitVarInsn(FLOAD, 3);
        methodNode.visitVarInsn(FLOAD, 3);
        methodNode.visitInsn(FMUL);
        methodNode.visitVarInsn(DLOAD, 4);
        methodNode.visitVarInsn(DLOAD, 4);
        methodNode.visitInsn(DMUL);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(8, 6);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode divide() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "divide", "(IJFD)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(IDIV);
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitInsn(LDIV);
        methodNode.visitVarInsn(FLOAD, 3);
        methodNode.visitVarInsn(FLOAD, 3);
        methodNode.visitInsn(FDIV);
        methodNode.visitVarInsn(DLOAD, 4);
        methodNode.visitVarInsn(DLOAD, 4);
        methodNode.visitInsn(DDIV);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(8, 6);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode remainder() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "remainder", "(IJFD)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(IREM);
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitInsn(LREM);
        methodNode.visitVarInsn(FLOAD, 3);
        methodNode.visitVarInsn(FLOAD, 3);
        methodNode.visitInsn(FREM);
        methodNode.visitVarInsn(DLOAD, 4);
        methodNode.visitVarInsn(DLOAD, 4);
        methodNode.visitInsn(DREM);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(8, 6);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode shift() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "shift", "(IJ)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(ISHL);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(IUSHR);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(ISHR);
        methodNode.visitInsn(POP);
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(LSHL);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(LUSHR);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(LSHR);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(4, 3);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode and() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "and", "(IJ)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(IAND);
        methodNode.visitInsn(POP);
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitInsn(LAND);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(4, 3);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode or() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "or", "(IJ)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(IOR);
        methodNode.visitInsn(POP);
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitInsn(LOR);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(4, 3);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode xor() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "xor", "(IJ)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(IXOR);
        methodNode.visitInsn(POP);
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitInsn(LXOR);
        methodNode.visitInsn(POP2);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(4, 3);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode negate() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "negate", "(IJFD)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(INEG);
        methodNode.visitInsn(POP);
        //
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitInsn(LNEG);
        methodNode.visitInsn(POP2);
        //
        methodNode.visitVarInsn(FLOAD, 3);
        methodNode.visitInsn(FNEG);
        methodNode.visitInsn(POP);
        //
        methodNode.visitVarInsn(DLOAD, 4);
        methodNode.visitInsn(DNEG);
        methodNode.visitInsn(POP2);
        //
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(2, 6);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode iinc() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "iinc", "(I)V", null, null);
        methodNode.visitCode();
        methodNode.visitIincInsn(0, 4);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(0, 1);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode cast() {
        MethodNode methodNode =
                new MethodNode(ACC_PUBLIC + ACC_STATIC, "cast", "(IJFDLjava/lang/Object;)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(I2L);
        methodNode.visitInsn(POP2);
        //
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(I2F);
        methodNode.visitInsn(POP);
        //
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(I2D);
        methodNode.visitInsn(POP2);
        //
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitInsn(L2I);
        methodNode.visitInsn(POP);
        //
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitInsn(L2F);
        methodNode.visitInsn(POP);
        //
        methodNode.visitVarInsn(LLOAD, 1);
        methodNode.visitInsn(L2D);
        methodNode.visitInsn(POP2);
        //
        methodNode.visitVarInsn(FLOAD, 3);
        methodNode.visitInsn(F2I);
        methodNode.visitInsn(POP);
        //
        methodNode.visitVarInsn(FLOAD, 3);
        methodNode.visitInsn(F2L);
        methodNode.visitInsn(POP2);
        //
        methodNode.visitVarInsn(FLOAD, 3);
        methodNode.visitInsn(F2D);
        methodNode.visitInsn(POP2);
        //
        methodNode.visitVarInsn(DLOAD, 4);
        methodNode.visitInsn(D2I);
        methodNode.visitInsn(POP);
        //
        methodNode.visitVarInsn(DLOAD, 4);
        methodNode.visitInsn(D2L);
        methodNode.visitInsn(POP2);
        //
        methodNode.visitVarInsn(DLOAD, 4);
        methodNode.visitInsn(D2F);
        methodNode.visitInsn(POP);
        //
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(I2B);
        methodNode.visitInsn(POP);
        //
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(I2C);
        methodNode.visitInsn(POP);
        //
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(I2S);
        methodNode.visitInsn(POP);
        //
        methodNode.visitVarInsn(ALOAD, 6);
        methodNode.visitTypeInsn(CHECKCAST, "java/lang/String");
        methodNode.visitInsn(POP);
        //
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(2, 11);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode arrayLength() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "arrayLength", "([I)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ALOAD, 0);
        methodNode.visitInsn(ARRAYLENGTH);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(1, 1);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode athrow() {
        MethodNode methodNode =
                new MethodNode(ACC_PUBLIC + ACC_STATIC, "athrow", "(Ljava/lang/RuntimeException;)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ALOAD, 0);
        methodNode.visitInsn(ATHROW);
        methodNode.visitMaxs(1, 1);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode instanceOf() {
        MethodNode methodNode =
                new MethodNode(ACC_PUBLIC + ACC_STATIC, "instanceOf", "(Ljava/lang/Object;)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ALOAD, 0);
        methodNode.visitTypeInsn(INSTANCEOF, "java/lang/String");
        methodNode.visitInsn(POP);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(1, 1);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode iReturn() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "iReturn", "()I", null, null);
        methodNode.visitCode();
        methodNode.visitInsn(ICONST_0);
        methodNode.visitInsn(IRETURN);
        methodNode.visitMaxs(1, 0);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode lReturn() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "lReturn", "()J", null, null);
        methodNode.visitCode();
        methodNode.visitInsn(LCONST_0);
        methodNode.visitInsn(LRETURN);
        methodNode.visitMaxs(2, 0);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode fReturn() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "fReturn", "()F", null, null);
        methodNode.visitCode();
        methodNode.visitInsn(FCONST_0);
        methodNode.visitInsn(FRETURN);
        methodNode.visitMaxs(1, 0);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode dReturn() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "dReturn", "()D", null, null);
        methodNode.visitCode();
        methodNode.visitInsn(DCONST_0);
        methodNode.visitInsn(DRETURN);
        methodNode.visitMaxs(2, 0);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode aReturn() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "aReturn", "()Ljava/lang/Object;", null, null);
        methodNode.visitCode();
        methodNode.visitInsn(ACONST_NULL);
        methodNode.visitInsn(ARETURN);
        methodNode.visitMaxs(1, 0);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode monitor() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "monitor", "(Ljava/lang/Object;)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ALOAD, 0);
        methodNode.visitInsn(MONITORENTER);
        methodNode.visitVarInsn(ALOAD, 0);
        methodNode.visitInsn(MONITOREXIT);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(1, 1);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode getFields() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC, "getFields", "()V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ALOAD, 0);
        methodNode.visitFieldInsn(GETFIELD, OWNER, "i", "I");
        methodNode.visitInsn(POP);
        methodNode.visitFieldInsn(GETSTATIC, OWNER, "b", "Z");
        methodNode.visitInsn(POP);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(2, 3);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode putFields() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC, "putFields", "()V", null, null);
        methodNode.visitCode();
        methodNode.visitInsn(ICONST_1);
        methodNode.visitFieldInsn(PUTSTATIC, OWNER, "b", "Z");
        methodNode.visitVarInsn(ALOAD, 0);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitFieldInsn(PUTFIELD, OWNER, "i", "I");
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(2, 3);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode invokeInterface() {
        MethodNode methodNode =
                new MethodNode(ACC_PUBLIC + ACC_STATIC, "invokeInterface", "(Ljava/lang/Runnable;)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ALOAD, 0);
        methodNode.visitMethodInsn(INVOKEINTERFACE, "java/lang/Runnable", "run", "()V", true);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(1, 1);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode invokeVirtual() {
        MethodNode methodNode =
                new MethodNode(ACC_PUBLIC + ACC_STATIC, "invokeVirtual", "(Ljava/lang/Object;)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ALOAD, 0);
        methodNode.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(1, 1);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode invokeStatic() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "example", "(III)I", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitVarInsn(ILOAD, 1);
        methodNode.visitVarInsn(ILOAD, 2);
        methodNode.visitMethodInsn(INVOKESTATIC, OWNER, "example", "(III)I", false);
        methodNode.visitInsn(IRETURN);
        methodNode.visitMaxs(3, 3);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode constructorCall() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "constructorCall", "([C)V", null, null);
        methodNode.visitCode();
        methodNode.visitTypeInsn(NEW, "java/lang/String");
        methodNode.visitInsn(DUP);
        methodNode.visitVarInsn(ALOAD, 0);
        methodNode.visitMethodInsn(INVOKESPECIAL, "java/lang/String", "<init>", "([C)V", false);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(3, 1);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode newArray() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "newArray", "()V", null, null);
        methodNode.visitCode();
        methodNode.visitInsn(ICONST_5);
        methodNode.visitIntInsn(NEWARRAY, T_INT);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitTypeInsn(ANEWARRAY, "java/lang/String");
        methodNode.visitInsn(POP);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(1, 0);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode multiNewArray() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "multiNewArray", "()V", null, null);
        methodNode.visitCode();
        methodNode.visitInsn(ICONST_2);
        methodNode.visitInsn(ICONST_1);
        methodNode.visitMultiANewArrayInsn("[[Ljava/lang/String;", 2);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(ICONST_3);
        methodNode.visitInsn(ICONST_2);
        methodNode.visitInsn(ICONST_1);
        methodNode.visitMultiANewArrayInsn("[[[[Ljava/lang/String;", 3);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitTypeInsn(ANEWARRAY, "[[[Ljava/lang/String;");
        methodNode.visitInsn(POP);
        methodNode.visitInsn(ICONST_1);
        methodNode.visitInsn(ICONST_0);
        methodNode.visitMultiANewArrayInsn("[[[[I", 2);
        methodNode.visitInsn(POP);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(3, 0);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode compare() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "compare", "(JFD)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(LLOAD, 0);
        methodNode.visitVarInsn(LLOAD, 0);
        methodNode.visitInsn(LCMP);
        methodNode.visitInsn(POP);
        //
        methodNode.visitVarInsn(FLOAD, 2);
        methodNode.visitVarInsn(FLOAD, 2);
        methodNode.visitInsn(FCMPL);
        methodNode.visitInsn(POP);
        //
        methodNode.visitVarInsn(FLOAD, 2);
        methodNode.visitVarInsn(FLOAD, 2);
        methodNode.visitInsn(FCMPG);
        methodNode.visitInsn(POP);
        //
        methodNode.visitVarInsn(DLOAD, 3);
        methodNode.visitVarInsn(DLOAD, 3);
        methodNode.visitInsn(DCMPL);
        methodNode.visitInsn(POP);
        //
        methodNode.visitVarInsn(DLOAD, 3);
        methodNode.visitVarInsn(DLOAD, 3);
        methodNode.visitInsn(DCMPG);
        methodNode.visitInsn(POP);
        //
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(4, 5);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode tableSwitch() {
        Label l0 = new Label();
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "tableSwitch", "(I)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitTableSwitchInsn(0, 4, l0, l0, l0, l0, l0, l0);
        methodNode.visitLabel(l0);
        methodNode.visitFrame(F_NEW, 0, new Object[0], 0, new Object[0]);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(1, 1);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode lookupSwitch() {
        Label l0 = new Label();
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "lookupSwitch", "(I)V", null, null);
        methodNode.visitCode();
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitLookupSwitchInsn(l0, new int[] {0, 10, 78, 100}, new Label[] {l0, l0, l0, l0});
        methodNode.visitLabel(l0);
        methodNode.visitFrame(F_NEW, 0, new Object[0], 0, new Object[0]);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(1, 1);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode unaryIf() {
        MethodNode methodNode =
                new MethodNode(ACC_PUBLIC + ACC_STATIC, "unaryIf", "(ILjava/lang/Object;)V", null, null);
        Label l0 = new Label();
        methodNode.visitCode();
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitJumpInsn(IFNE, l0);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitJumpInsn(IFEQ, l0);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitJumpInsn(IFLT, l0);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitJumpInsn(IFGE, l0);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitJumpInsn(IFGT, l0);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitJumpInsn(IFLE, l0);
        methodNode.visitVarInsn(ALOAD, 1);
        methodNode.visitJumpInsn(IFNULL, l0);
        methodNode.visitVarInsn(ALOAD, 1);
        methodNode.visitJumpInsn(IFNONNULL, l0);
        methodNode.visitLabel(l0);
        methodNode.visitFrame(F_NEW, 0, new Object[0], 0, new Object[0]);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(1, 2);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode binaryIf() {
        MethodNode methodNode =
                new MethodNode(ACC_PUBLIC + ACC_STATIC, "binaryIf", "(ILjava/lang/Object;)V", null, null);
        Label l0 = new Label();
        methodNode.visitCode();
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(ICONST_1);
        methodNode.visitJumpInsn(IF_ICMPEQ, l0);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(ICONST_1);
        methodNode.visitJumpInsn(IF_ICMPNE, l0);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(ICONST_1);
        methodNode.visitJumpInsn(IF_ICMPLT, l0);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(ICONST_2);
        methodNode.visitJumpInsn(IF_ICMPGE, l0);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(ICONST_3);
        methodNode.visitJumpInsn(IF_ICMPGT, l0);
        methodNode.visitVarInsn(ILOAD, 0);
        methodNode.visitInsn(ICONST_4);
        methodNode.visitJumpInsn(IF_ICMPLE, l0);
        methodNode.visitVarInsn(ALOAD, 1);
        methodNode.visitVarInsn(ALOAD, 1);
        methodNode.visitJumpInsn(IF_ACMPEQ, l0);
        methodNode.visitVarInsn(ALOAD, 1);
        methodNode.visitVarInsn(ALOAD, 1);
        methodNode.visitJumpInsn(IF_ACMPNE, l0);
        methodNode.visitLabel(l0);
        methodNode.visitFrame(F_NEW, 0, new Object[0], 0, new Object[0]);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(2, 2);
        methodNode.visitEnd();
        return methodNode;
    }

    public static MethodNode parameters() {
        // i = 0;
        // j = 0;
        // d = 0;
        // f = 0;
        // o = null;
        MethodNode methodNode =
                new MethodNode(ACC_PUBLIC + ACC_STATIC, "parameters", "(IJFDLjava/lang/Object;)V", null, null);
        methodNode.visitCode();
        methodNode.visitInsn(ICONST_0);
        methodNode.visitVarInsn(ISTORE, 0);
        methodNode.visitInsn(LCONST_0);
        methodNode.visitVarInsn(LSTORE, 1);
        methodNode.visitInsn(FCONST_0);
        methodNode.visitVarInsn(FSTORE, 3);
        methodNode.visitInsn(DCONST_0);
        methodNode.visitVarInsn(DSTORE, 4);
        methodNode.visitInsn(ACONST_NULL);
        methodNode.visitVarInsn(ASTORE, 6);
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(2, 7);
        methodNode.visitEnd();
        return methodNode;
    }
}

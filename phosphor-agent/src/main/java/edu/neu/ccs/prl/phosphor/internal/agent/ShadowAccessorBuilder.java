package edu.neu.ccs.prl.phosphor.internal.agent;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class ShadowAccessorBuilder {
    /**
     * Name of the shadow accessor method.
     */
    public static final String NAME = PhosphorAgent.ADDED_MEMBER_PREFIX + "GET_SHADOW";
    /**
     * Base access modifiers of the shadow accessor method.
     */
    public static final int ACCESS = Opcodes.ACC_SYNTHETIC | Opcodes.ACC_PUBLIC;

    public MethodNode build(ClassNode cn, Type shadowClass) {
        String descriptor = "()" + shadowClass.getDescriptor();
        if (AccessUtil.isSet(cn.access, Opcodes.ACC_INTERFACE)) {
            return new MethodNode(AccessUtil.set(ACCESS, Opcodes.ACC_ABSTRACT), NAME, descriptor, null, null);
        }
        MethodNode accessor = new MethodNode(ACCESS, NAME, descriptor, null, null);
        // Object s = ShadowMap.get(this);
        // if s != null goto L1
        // return ShadowMap.putIfAbsent(new Shadow(), this);
        // L1:
        // return (Shadow) s;
        accessor.visitCode();
        // TODO replace
        accessor.visitTypeInsn(Opcodes.NEW, shadowClass.getInternalName());
        accessor.visitInsn(Opcodes.DUP);
        accessor.visitMethodInsn(Opcodes.INVOKESPECIAL, shadowClass.getInternalName(), "<init>", "()V", false);
        accessor.visitInsn(Opcodes.ARETURN);
        accessor.visitMaxs(-1, -1);
        accessor.visitEnd();
        return accessor;
    }
}

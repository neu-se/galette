package edu.neu.ccs.prl.phosphor.internal.patch;

import edu.neu.ccs.prl.phosphor.internal.runtime.mask.ClassLoaderAdapter;
import edu.neu.ccs.prl.phosphor.internal.transform.AsmUtil;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class ClassLoaderAdapterPatcher extends AdapterPatcher {
    ClassLoaderAdapterPatcher(ClassVisitor cv) {
        super(cv);
    }

    @Override
    protected void replaceBody(int access, String name, String descriptor, MethodVisitor mv) {
        mv.visitCode();
        AsmUtil.loadArguments(mv, access, descriptor);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(ClassLoader.class), name, descriptor, false);
        mv.visitInsn(Type.getReturnType(descriptor).getOpcode(Opcodes.IRETURN));
        mv.visitMaxs(-1, -1);
        mv.visitEnd();
    }

    public static boolean isApplicable(String className) {
        return Type.getInternalName(ClassLoaderAdapter.class).equals(className);
    }
}

package edu.neu.ccs.prl.galette.internal.patch;

import edu.neu.ccs.prl.galette.internal.transform.AsmUtil;
import edu.neu.ccs.prl.galette.internal.transform.GaletteTransformer;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public abstract class AdapterPatcher extends ClassVisitor {
    public AdapterPatcher(ClassVisitor cv) {
        super(GaletteTransformer.ASM_VERSION, cv);
    }

    @Override
    public MethodVisitor visitMethod(
            int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor target = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (AsmUtil.isSet(access, Opcodes.ACC_PUBLIC) && !name.equals("<init>")) {
            // Set the delegate method visitor to null to replace the body of the method
            return new MethodVisitor(GaletteTransformer.ASM_VERSION, null) {
                @Override
                public void visitCode() {
                    replaceBody(access, name, descriptor, target);
                }
            };
        }
        return target;
    }

    protected abstract void replaceBody(int access, String name, String descriptor, MethodVisitor mv);
}

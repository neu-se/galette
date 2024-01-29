package edu.neu.ccs.prl.galette.internal.transform;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.JSRInlinerAdapter;

class SubroutineInliner extends ClassVisitor {
    SubroutineInliner(ClassVisitor cv) {
        super(GaletteTransformer.ASM_VERSION, cv);
    }

    @Override
    public MethodVisitor visitMethod(
            int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new JSRInlinerAdapter(mv, access, name, descriptor, signature, exceptions);
    }
}

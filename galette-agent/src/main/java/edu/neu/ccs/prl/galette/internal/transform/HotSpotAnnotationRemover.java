package edu.neu.ccs.prl.galette.internal.transform;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;

class HotSpotAnnotationRemover extends MethodVisitor {
    HotSpotAnnotationRemover(MethodVisitor mv) {
        super(GaletteTransformer.ASM_VERSION, mv);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        // Shadow methods will have different signatures and cannot be intrinsic candidates.
        if (descriptor.equals("Ljdk/internal/HotSpotIntrinsicCandidate;")
                || descriptor.equals("Ljdk/internal/vm/annotation/IntrinsicCandidate;")) {
            return null;
        }
        return super.visitAnnotation(descriptor, visible);
    }
}

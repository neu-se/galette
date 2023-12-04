package edu.neu.ccs.prl.phosphor.internal.transform;

import org.objectweb.asm.MethodVisitor;

class UnsafeMasker extends MethodVisitor {
    UnsafeMasker(MethodVisitor mv) {
        super(PhosphorTransformer.ASM_VERSION, mv);
    }

    @Override
    public void visitCode() {
        // TODO transform the raw bytes to add Phosphor instrumentation
        super.visitCode();
    }

    public static boolean isApplicable(String className, String methodName) {
        if ("sun/misc/Unsafe".equals(className) || "jdk/internal/misc/Unsafe".equals(className)) {
            switch (methodName) {
                case "defineClass":
                case "defineAnonymousClass":
                    return true;
            }
        }
        return false;
    }
}

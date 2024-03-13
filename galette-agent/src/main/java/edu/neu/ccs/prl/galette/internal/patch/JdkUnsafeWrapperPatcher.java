package edu.neu.ccs.prl.galette.internal.patch;

import edu.neu.ccs.prl.galette.internal.runtime.mask.JdkUnsafeWrapper;
import edu.neu.ccs.prl.galette.internal.transform.GaletteTransformer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Function;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

final class JdkUnsafeWrapperPatcher extends ClassVisitor {
    private final boolean useReferenceName;

    JdkUnsafeWrapperPatcher(ClassVisitor cv, Function<String, byte[]> entryLocator) {
        super(GaletteTransformer.ASM_VERSION, cv);
        this.useReferenceName = shouldUseReferenceName(entryLocator);
    }

    @Override
    public MethodVisitor visitMethod(
            int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor target = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new MethodVisitor(GaletteTransformer.ASM_VERSION, target) {
            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                if (useReferenceName && name.contains("Object")) {
                    name = name.replace("Object", "Reference");
                }
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            }
        };
    }

    private static boolean shouldUseReferenceName(Function<String, byte[]> entryLocator) {
        byte[] classFileBuffer = entryLocator.apply("/java.base/jdk/internal/misc/Unsafe.class");
        ByteArrayInputStream in = new ByteArrayInputStream(classFileBuffer);
        ClassNode cn = new ClassNode();
        try {
            new ClassReader(in).accept(cn, ClassReader.SKIP_CODE);
        } catch (IOException e) {
            throw new AssertionError(in.getClass() + " threw " + e.getClass());
        }
        for (MethodNode mn : cn.methods) {
            if (mn.name.contains("putReference")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isApplicable(String className) {
        return Type.getInternalName(JdkUnsafeWrapper.class).equals(className);
    }
}

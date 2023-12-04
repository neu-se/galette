package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.internal.runtime.PhosphorFrame;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class PhosphorTransformer {
    /**
     * ASM API version implemented by the class and method visitors used by transformers.
     */
    public static final int ASM_VERSION = Opcodes.ASM9;
    /**
     * Prefix of class members added via instrumentation.
     * <p>
     * Non-null.
     */
    public static final String ADDED_MEMBER_PREFIX = "$$PHOSPHOR_";
    /**
     * Prefix of packages that should not be instrumented.
     * <p>
     * Non-null.
     */
    public static final String INTERNAL_PACKAGE_PREFIX = "edu/neu/ccs/prl/phosphor/internal/";
    /**
     * Prefix of runtime packages that need to be packed into the base module or added to the boot class path.
     * <p>
     * Non-null.
     */
    public static final String RUNTIME_PACKAGE_PREFIX = "edu/neu/ccs/prl/phosphor/internal/runtime/";
    /**
     * Prefix of transformer packages that need to be packed into the base module or added to the boot class path.
     * <p>
     * Non-null.
     */
    public static final String TRANSFORM_PACKAGE_PREFIX = "edu/neu/ccs/prl/phosphor/internal/transform/";
    /**
     * Descriptor for {@link PhosphorInstrumented}.
     * <p>
     * Non-null.
     */
    private static final String ANNOTATION_DESC = Type.getDescriptor(PhosphorInstrumented.class);
    /**
     * Descriptor for {@link PhosphorFrame}.
     * <p>
     * Non-null.
     */
    static final String PHOSPHOR_FRAME_DESCRIPTOR = Type.getDescriptor(PhosphorFrame.class);
    /**
     * Classes that should not be instrumented.
     * <p>
     * Non-null.
     */
    private final ExclusionList exclusions = new ExclusionList("java/lang/Object", INTERNAL_PACKAGE_PREFIX);

    public byte[] transform(byte[] classFileBuffer) {
        ClassReader cr = new ClassReader(classFileBuffer);
        String className = cr.getClassName();
        if (exclusions.isExcluded(className) || AsmUtil.isSet(cr.getAccess(), Opcodes.ACC_MODULE)) {
            // Skip excluded classes and module info
            return null;
        }
        try {
            return transform(cr, false);
        } catch (ClassTooLargeException | MethodTooLargeException e) {
            // Try to just add shadow fields and methods
            return transform(cr, true);
        }
    }

    private byte[] transform(ClassReader cr, boolean minimal) {
        ClassNode cn = new ClassNode();
        cr.accept(cn, ClassReader.EXPAND_FRAMES);
        if (isAnnotated(cn) || containsShadowMember(cn)) {
            // This class has already been instrumented; return null to indicate that the class was unchanged
            return null;
        }
        // Add an annotation indicating that the class has been instrumented
        cn.visitAnnotation(ANNOTATION_DESC, false);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        // Add shadow fields
        new ShadowFieldAdder().process(cn);
        // Add shadow methods and propagation logic
        new ShadowMethodAdder(minimal).process(cn);
        cn.accept(cw);
        return cw.toByteArray();
    }

    private static boolean containsShadowMember(ClassNode cn) {
        for (MethodNode mn : cn.methods) {
            if (mn.desc.contains(PHOSPHOR_FRAME_DESCRIPTOR)) {
                return true;
            }
        }
        for (FieldNode fn : cn.fields) {
            if (fn.name.startsWith(ADDED_MEMBER_PREFIX)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAnnotated(ClassNode cn) {
        if (cn.invisibleAnnotations != null) {
            for (AnnotationNode a : cn.invisibleAnnotations) {
                if (ANNOTATION_DESC.equals(a.desc)) {
                    return true;
                }
            }
        }
        return false;
    }
}

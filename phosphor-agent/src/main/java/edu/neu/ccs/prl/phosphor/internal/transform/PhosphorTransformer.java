package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.internal.runtime.collection.SimpleList;
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

    private byte[] transform(ClassReader cr, boolean propagate) {
        ClassNode cn = new ClassNode();
        cr.accept(cn, ClassReader.EXPAND_FRAMES);
        if (hasShadowInstrumentation(cn)) {
            // This class has already been instrumented; return null to indicate that the class was unchanged
            return null;
        }
        // Add an annotation indicating that the class has been instrumented
        cn.visitAnnotation(ANNOTATION_DESC, false);
        // Add shadow fields
        new ShadowFieldAdder().process(cn);
        // Create shadow methods for the raw original methods
        SimpleList<MethodNode> shadows = new ShadowMethodCreator(cn, propagate).createShadows();
        // Process the raw original methods
        SimpleList<MethodNode> processed = new OriginalMethodProcessor(cn).process();
        // Replace the raw methods with the processed methods
        cn.methods.clear();
        for (int i = 0; i < processed.size(); i++) {
            cn.methods.add(processed.get(i));
        }
        // Add the shadow methods
        for (int i = 0; i < shadows.size(); i++) {
            cn.methods.add(shadows.get(i));
        }
        // Write the transformed class
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        cn.accept(cw);
        return cw.toByteArray();
    }

    private static boolean hasShadowInstrumentation(ClassNode cn) {
        if (cn.invisibleAnnotations != null) {
            for (AnnotationNode a : cn.invisibleAnnotations) {
                if (ANNOTATION_DESC.equals(a.desc)) {
                    return true;
                }
            }
        }
        for (MethodNode mn : cn.methods) {
            if (ShadowMethodCreator.isShadowMethod(mn.desc)) {
                return true;
            }
        }
        for (FieldNode fn : cn.fields) {
            if (ShadowFieldAdder.isShadowField(fn.name)) {
                return true;
            }
        }
        return false;
    }

    public static byte[] getInstanceAndTransform(byte[] classFileBuffer) {
        return new PhosphorTransformer().transform(classFileBuffer);
    }
}

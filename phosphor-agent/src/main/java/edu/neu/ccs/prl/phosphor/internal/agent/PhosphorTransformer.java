package edu.neu.ccs.prl.phosphor.internal.agent;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class PhosphorTransformer implements ClassFileTransformer {
    private static final String ANNOTATION_DESC = Type.getDescriptor(PhosphorInstrumented.class);
    /**
     * List classes for which the JVM uses hard-coded offsets into the constant pool.
     */
    private static final ExclusionList hardCodedOffsets = new ExclusionList(
            "java/lang/Boolean",
            "java/lang/Character",
            "java/lang/Byte",
            "java/lang/Short",
            "java/lang/Number",
            "java/lang/ref/Reference",
            "java/lang/ref/FinalReference",
            "java/lang/ref/SoftReference",
            "java/lang/invoke/LambdaForm",
            "java/lang/invoke/LambdaForm$",
            "jdk/internal/misc/UnsafeConstants");

    private static final ExclusionList exclusions =
            new ExclusionList("java/lang/Object", PhosphorAgent.INTERNAL_PACKAGE_PREFIX);

    @Override
    public byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classFileBuffer) {
        if (shouldDynamicallyInstrument(className, classBeingRedefined)) {
            try {
                return transform(classFileBuffer);
            } catch (Throwable t) {
                // Log the error to prevent it from being silently swallowed by the JVM
                PhosphorLog.error("Failed to instrument class: " + className, t);
                throw t;
            }
        }
        return null;
    }

    public byte[] transform(byte[] classFileBuffer) {
        ClassReader cr = new ClassReader(classFileBuffer);
        if (shouldStaticallyInstrument(cr.getClassName())) {
            try {
                return transform(cr);
            } catch (ClassTooLargeException | MethodTooLargeException e) {
                // TODO just add field/method shadow
                return null;
            }
        }
        return null;
    }

    private byte[] transform(ClassReader cr) {
        try {
            ClassNode cn = new ClassNode();
            cr.accept(cn, ClassReader.EXPAND_FRAMES);
            if (isAnnotated(cn) || containsShadowAccessor(cn)) {
                // This class has already been instrumented; return null to indicate that the class was unchanged
                return null;
            }
            // Add an annotation indicating that the class has been instrumented
            cn.visitAnnotation(ANNOTATION_DESC, false);
            // Add the shadow accessor method
            cn.methods.add(new ShadowAccessorBuilder().build(cn, Type.getObjectType("java/lang/Object")));
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
            // TODO add class visitors
            ClassVisitor cv = cw;
            cn.accept(cv);
            return cw.toByteArray();
        } catch (ClassTooLargeException | MethodTooLargeException e) {
            return null;
        }
    }

    private boolean shouldStaticallyInstrument(String className) {
        return !exclusions.isExcluded(className);
    }

    private static boolean shouldDynamicallyInstrument(String className, Class<?> classBeingRedefined) {
        return classBeingRedefined == null // Class is being loaded and not redefined or retransformed
                // Class is not a dynamically generated accessor for reflection
                && (className == null
                        || !ExclusionList.startsWith(className, "sun")
                        || ExclusionList.startsWith(className, "sun/nio"));
    }

    private static boolean containsShadowAccessor(ClassNode cn) {
        for (MethodNode mn : cn.methods) {
            if (ShadowAccessorBuilder.NAME.equals(mn.name)) {
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

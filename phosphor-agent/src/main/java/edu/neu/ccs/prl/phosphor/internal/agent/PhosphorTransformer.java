package edu.neu.ccs.prl.phosphor.internal.agent;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

public class PhosphorTransformer implements ClassFileTransformer {
    private static final String ANNOTATION_DESC = Type.getDescriptor(PhosphorInstrumented.class);
    private static final ExclusionList instrumentationExclusions = new ExclusionList(
            // JVM uses hard-coded offsets into constant pool for these classes
            "java/lang/Object",
            "java/lang/Boolean",
            "java/lang/Character",
            "java/lang/Byte",
            "java/lang/Short",
            "java/lang/Number",
            "java/lang/ref/Reference",
            "java/lang/ref/FinalReference",
            "java/lang/ref/SoftReference",
            "jdk/internal/misc/UnsafeConstants",
            // Skip internal Phosphor classes
            PhosphorAgent.INTERNAL_PACKAGE_PREFIX);

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
                PhosphorLog.error("Instrumentation failed", t);
                t.printStackTrace();
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
            if (isAnnotated(cn)) {
                // This class has already been instrumented; return null to indicate that the class was unchanged
                return null;
            }
            // Add an annotation indicating that the class has been instrumented
            cn.visitAnnotation(ANNOTATION_DESC, false);
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
        return !instrumentationExclusions.isExcluded(className);
    }

    private static boolean shouldDynamicallyInstrument(String className, Class<?> classBeingRedefined) {
        return classBeingRedefined == null // Class is being loaded and not redefined or retransformed
                // Class is not a dynamically generated accessor for reflection
                && (className == null
                        || !ExclusionList.startsWith(className, "sun")
                        || ExclusionList.startsWith(className, "sun/nio"));
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

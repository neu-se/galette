package edu.neu.ccs.prl.phosphor.internal.agent;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

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
        // Class is being loaded and not redefined or retransformed
        if (classBeingRedefined == null) {
            try {
                return transform(classFileBuffer, false);
            } catch (Throwable t) {
                // Log the error to prevent it from being silently swallowed by the JVM
                PhosphorLog.error("Failed to instrument class: " + className, t);
                throw t;
            }
        }
        return null;
    }

    public byte[] transform(byte[] classFileBuffer, boolean isStatic) {
        ClassReader cr = new ClassReader(classFileBuffer);
        if (shouldStaticallyInstrument(cr.getClassName()) && !AccessUtil.isSet(cr.getAccess(), Opcodes.ACC_MODULE)) {
            try {
                return transform(cr, isStatic);
            } catch (ClassTooLargeException | MethodTooLargeException e) {
                // TODO just add field/method shadow
                return null;
            }
        }
        return null;
    }

    private byte[] transform(ClassReader cr, boolean isStatic) {
        try {
            ClassNode cn = new ClassNode();
            cr.accept(cn, ClassReader.EXPAND_FRAMES);
            if (isAnnotated(cn)
                    || containsShadowAccessor(cn)
                    || cn.name.endsWith(ShadowClassBuilder.SHADOW_CLASS_SUFFIX)) {
                // This class has already been instrumented; return null to indicate that the class was unchanged
                return null;
            }
            // Add an annotation indicating that the class has been instrumented
            cn.visitAnnotation(ANNOTATION_DESC, false);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
            ClassVisitor cv = cw;
            if (!isStatic) {
                // TODO handle statically-instrumented classes
                // Add the shadow accessor method
                cn.methods.add(new ShadowAccessorBuilder().build(cn));
                // Create the shadow class
                byte[] shadowClassBuffer = createShadowClass(cr);
                cv = new InitializingClassVisitor(cv, shadowClassBuffer);
            }
            if (UnsafeFixingClassVisitor.isApplicable(cn.name)) {
                cv = new UnsafeFixingClassVisitor(cv);
            }
            cn.accept(cv);
            return cw.toByteArray();
        } catch (ClassTooLargeException | MethodTooLargeException e) {
            return null;
        }
    }

    private byte[] createShadowClass(ClassReader cr) {
        ShadowClassBuilder builder = new ShadowClassBuilder();
        cr.accept(builder, ClassReader.SKIP_CODE);
        ClassNode shadow = builder.getShadow();
        return toBytes(shadow);
    }

    private boolean shouldStaticallyInstrument(String className) {
        return !exclusions.isExcluded(className);
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

    public static byte[] toBytes(ClassNode cn) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cn.accept(cw);
        return cw.toByteArray();
    }
}

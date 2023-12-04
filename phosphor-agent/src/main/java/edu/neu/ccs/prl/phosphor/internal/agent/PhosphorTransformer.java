package edu.neu.ccs.prl.phosphor.internal.agent;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class PhosphorTransformer implements ClassFileTransformer {
    private static final String ANNOTATION_DESC = Type.getDescriptor(PhosphorInstrumented.class);
    private static final ExclusionList exclusions =
            new ExclusionList("java/lang/Object", PhosphorAgent.INTERNAL_PACKAGE_PREFIX);
    private final TransformationCache cache;

    public PhosphorTransformer() {
        this(new TransformationCache());
    }

    public PhosphorTransformer(TransformationCache cache) {
        if (cache == null) {
            throw new NullPointerException();
        }
        this.cache = cache;
    }

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
        String className = cr.getClassName();
        if (!exclusions.isExcluded(className) && !AsmUtil.isSet(cr.getAccess(), Opcodes.ACC_MODULE)) {
            try {
                if (cache.hasEntry(className, classFileBuffer)) {
                    return cache.loadEntry(cr.getClassName());
                }
                byte[] result = transform(cr);
                cache.storeEntry(className, classFileBuffer, result);
                return result;
            } catch (ClassTooLargeException | MethodTooLargeException e) {
                // TODO just add field/method shadow
                return null;
            } catch (IOException e) {
                throw new RuntimeException("Failed to load or store cache entry", e);
            }
        }
        return null;
    }

    private byte[] transform(ClassReader cr) {
        try {
            ClassNode cn = new ClassNode();
            cr.accept(cn, ClassReader.EXPAND_FRAMES);
            if (isAnnotated(cn) || containsShadowMember(cn)) {
                // This class has already been instrumented; return null to indicate that the class was unchanged
                return null;
            }
            // Add an annotation indicating that the class has been instrumented
            cn.visitAnnotation(ANNOTATION_DESC, false);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
            ClassVisitor cv = cw;
            if (UnsafeAccessModifier.isApplicable(cn.name)) {
                cv = new UnsafeAccessModifier(cv);
            }
            if (!HardCoded.hasHardCodedStaticOffset(cr.getClassName())) {
                cv = new ShadowFieldAdder(cv);
            }
            cn.accept(cv);
            return cw.toByteArray();
        } catch (ClassTooLargeException | MethodTooLargeException e) {
            return null;
        }
    }

    private static boolean containsShadowMember(ClassNode cn) {
        for (MethodNode mn : cn.methods) {
            if (mn.name.startsWith(PhosphorAgent.ADDED_MEMBER_PREFIX)) {
                return true;
            }
        }
        for (FieldNode fn : cn.fields) {
            if (fn.name.startsWith(PhosphorAgent.ADDED_MEMBER_PREFIX)) {
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

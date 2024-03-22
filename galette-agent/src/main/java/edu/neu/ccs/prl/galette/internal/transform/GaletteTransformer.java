package edu.neu.ccs.prl.galette.internal.transform;

import edu.neu.ccs.prl.galette.internal.runtime.collection.SimpleList;
import java.io.IOException;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

public class GaletteTransformer {
    /**
     * ASM API version implemented by the class and method visitors used by transformers.
     */
    public static final int ASM_VERSION = Opcodes.ASM9;
    /**
     * Prefix of class members added via instrumentation.
     * <p>
     * Non-null.
     */
    public static final String ADDED_MEMBER_PREFIX = "$$GALETTE_";
    /**
     * Prefix of packages that should not be instrumented.
     * <p>
     * Non-null.
     */
    public static final String INTERNAL_PACKAGE_PREFIX = "edu/neu/ccs/prl/galette/internal/";
    /**
     * Prefix of runtime packages that need to be packed into the base module or added to the boot class path.
     * <p>
     * Non-null.
     */
    public static final String RUNTIME_PACKAGE_PREFIX = "edu/neu/ccs/prl/galette/internal/runtime/";
    /**
     * Prefix of transformer packages that need to be packed into the base module or added to the boot class path.
     * <p>
     * Non-null.
     */
    public static final String TRANSFORM_PACKAGE_PREFIX = "edu/neu/ccs/prl/galette/internal/transform/";
    /**
     * Descriptor for {@link GaletteInstrumented}.
     * <p>
     * Non-null.
     */
    private static final String ANNOTATION_DESC = Type.getDescriptor(GaletteInstrumented.class);
    /**
     * Classes that should not be instrumented.
     * <p>
     * Non-null.
     */
    private static final ExclusionList exclusions = new ExclusionList("java/lang/Object", INTERNAL_PACKAGE_PREFIX);

    private static TransformationCache cache;

    public byte[] transform(byte[] classFileBuffer, boolean isHostedAnonymous) {
        ClassReader cr = new ClassReader(classFileBuffer);
        String className = cr.getClassName();
        TransformationCache currentCache = getCache();
        if (exclusions.isExcluded(className) || AsmUtil.isSet(cr.getAccess(), Opcodes.ACC_MODULE)) {
            // Skip excluded classes and module info
            return null;
        }
        try {
            // Only cache dynamically instrumented files that are not synthetic
            if (currentCache != null && currentCache.hasEntry(className, classFileBuffer)) {
                return currentCache.loadEntry(className);
            }
            byte[] result = transformInternal(cr, isHostedAnonymous);
            if (!className.contains("$$Lambda")
                    && !AsmUtil.isSet(cr.getAccess(), Opcodes.ACC_SYNTHETIC)
                    && currentCache != null
                    && result != null) {
                currentCache.storeEntry(className, classFileBuffer, result);
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load or store cache entry", e);
        } catch (Throwable t) {
            // Log the error to prevent it from being silently swallowed by the JVM
            GaletteLog.error("Failed to instrument class: " + className, t);
            throw t;
        }
    }

    private byte[] transformInternal(ClassReader cr, boolean isHostedAnonymous) {
        try {
            return transform(cr, true, isHostedAnonymous);
        } catch (ClassTooLargeException | MethodTooLargeException e) {
            // Try to just add shadow fields and methods
            return transform(cr, false, isHostedAnonymous);
        }
    }

    private byte[] transform(ClassReader cr, boolean propagate, boolean isHostedAnonymous) {
        ClassNode cn = new ClassNode(ASM_VERSION);
        cr.accept(cn, ClassReader.EXPAND_FRAMES);
        if (hasShadowInstrumentation(cn)) {
            // This class has already been instrumented; return null to indicate that the class was unchanged
            return null;
        }
        boolean hasFrames = containsFrames(cn);
        cn = preprocess(cn, hasFrames);
        // Add an annotation indicating that the class has been instrumented
        cn.visitAnnotation(ANNOTATION_DESC, false);
        // Add shadow fields
        new ShadowFieldAdder().process(cn);
        // Create shadow methods for the raw original methods
        SimpleList<MethodNode> shadows = new ShadowMethodCreator(cn, propagate, isHostedAnonymous).createShadows();
        // Process the raw original methods
        SimpleList<MethodNode> processed = new OriginalMethodProcessor(cn, propagate, isHostedAnonymous).process();
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
        // Remove computed frames
        ClassVisitor cv = hasFrames ? cw : new FrameRemover(cw);
        // Make the members of certain classes publicly accessible
        if (AccessModifier.isApplicable(cn.name)) {
            cv = new AccessModifier(cv);
        }
        // Add a field to java.lang.Class to store offsets used in masking of Unsafe
        if (OffsetCacheAdder.isApplicable(cn.name)) {
            cv = new OffsetCacheAdder(cv);
        }
        // Add a field to java.lang.Thread to store frame stacks
        if (ThreadLocalFrameAdder.isApplicable(cn.name)) {
            cv = new ThreadLocalFrameAdder(cv);
        }
        cn.accept(cv);
        return cw.toByteArray();
    }

    private static ClassNode preprocess(ClassNode cn, boolean hasFrames) {
        // Inline subroutines and compute naive frames if necessary
        int flags = ClassWriter.COMPUTE_MAXS;
        if (!hasFrames) {
            flags |= ClassWriter.COMPUTE_FRAMES;
        }
        ClassWriter cw = new ClassWriter(flags) {
            @Override
            protected String getCommonSuperClass(String type1, String type2) {
                return "java/lang/Object";
            }
        };
        cn.accept(new SubroutineInliner(cw));
        ClassNode result = new ClassNode(ASM_VERSION);
        new ClassReader(cw.toByteArray()).accept(result, ClassReader.EXPAND_FRAMES);
        return result;
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

    public static byte[] getInstanceAndTransform(byte[] classFileBuffer, boolean isHostedAnonymous) {
        byte[] result = new GaletteTransformer().transform(classFileBuffer, isHostedAnonymous);
        return result == null ? classFileBuffer : result;
    }

    public static boolean isExcluded(String className) {
        return exclusions.isExcluded(className);
    }

    public static synchronized void setCache(TransformationCache cache) {
        GaletteTransformer.cache = cache;
    }

    private static synchronized TransformationCache getCache() {
        return cache;
    }

    private static boolean containsFrames(ClassNode cn) {
        for (MethodNode mn : cn.methods) {
            for (AbstractInsnNode in : mn.instructions) {
                if (in instanceof FrameNode) {
                    return true;
                }
            }
        }
        // Stack map frames are required in Java 7+
        return (cn.version & 0xFFFF) >= Opcodes.V1_7;
    }
}

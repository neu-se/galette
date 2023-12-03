package edu.neu.ccs.prl.phosphor.internal.agent;

import edu.neu.ccs.prl.phosphor.internal.runtime.Tag;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;

/**
 * Builds a shadow class for an original class.
 */
class ShadowClassBuilder extends ClassVisitor {
    /**
     * Suffix of shadow classes.
     */
    public static final String SHADOW_CLASS_SUFFIX = PhosphorAgent.ADDED_MEMBER_PREFIX + "SHADOW";
    /**
     * Internal name of {@link Object}.
     */
    public static final String OBJECT_INTERNAL_NAME = "java/lang/Object";
    /**
     * Descriptor for {@link Tag}.
     */
    private static final String TAG_DESCRIPTOR = Type.getDescriptor(Tag.class);
    /**
     * The shadow class being built by this instance.
     */
    private final ClassNode shadow = new ClassNode(PhosphorAgent.ASM_VERSION);

    ShadowClassBuilder() {
        super(PhosphorAgent.ASM_VERSION);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (AccessUtil.isSet(access, Opcodes.ACC_MODULE)) {
            throw new IllegalArgumentException();
        }
        int shadowAccess = Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC;
        if (AccessUtil.isSet(access, Opcodes.ACC_INTERFACE)) {
            shadowAccess |= Opcodes.ACC_INTERFACE | Opcodes.ACC_ABSTRACT;
        }
        if (interfaces != null) {
            interfaces = interfaces.clone();
            for (int i = 0; i < interfaces.length; i++) {
                interfaces[i] = getShadowClassName(interfaces[i]);
            }
        }
        shadow.visit(
                Opcodes.V1_8, shadowAccess, getShadowClassName(name), null, getShadowClassName(superName), interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        int shadowAccess = Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC;
        if (AccessUtil.isSet(shadow.access, Opcodes.ACC_INTERFACE)) {
            shadowAccess |= Opcodes.ACC_FINAL;
        }
        if (AccessUtil.isSet(access, Opcodes.ACC_STATIC)) {
            shadowAccess |= Opcodes.ACC_STATIC;
        }
        if (AccessUtil.isSet(access, Opcodes.ACC_VOLATILE)) {
            shadowAccess |= Opcodes.ACC_VOLATILE;
        }
        if (AccessUtil.isSet(access, Opcodes.ACC_TRANSIENT)) {
            shadowAccess |= Opcodes.ACC_TRANSIENT;
        }
        return shadow.visitField(shadowAccess, name, TAG_DESCRIPTOR, null, null);
    }

    @Override
    public void visitEnd() {
        if (!AccessUtil.isSet(shadow.access, Opcodes.ACC_INTERFACE)) {
            MethodVisitor mv =
                    shadow.visitMethod(Opcodes.ACC_SYNTHETIC | Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(-1, -1);
            mv.visitEnd();
        }
        shadow.visitEnd();
    }

    public ClassNode getShadow() {
        return shadow;
    }

    public static String getShadowClassName(String className) {
        if (className == null || className.equals(OBJECT_INTERNAL_NAME)) {
            return OBJECT_INTERNAL_NAME;
        } else if (className.endsWith(SHADOW_CLASS_SUFFIX)) {
            throw new IllegalArgumentException();
        }
        return className + SHADOW_CLASS_SUFFIX;
    }
}

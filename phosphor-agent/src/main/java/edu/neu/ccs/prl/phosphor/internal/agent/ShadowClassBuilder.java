package edu.neu.ccs.prl.phosphor.internal.agent;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;

import static edu.neu.ccs.prl.phosphor.internal.agent.AsmUtil.toBytes;

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
     * The shadow class being built by this instance.
     */
    private final ClassNode shadow = new ClassNode(PhosphorAgent.ASM_VERSION);

    ShadowClassBuilder() {
        super(PhosphorAgent.ASM_VERSION);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (AsmUtil.isSet(access, Opcodes.ACC_MODULE)) {
            throw new IllegalArgumentException();
        }
        int shadowAccess = Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC;
        if (AsmUtil.isSet(access, Opcodes.ACC_INTERFACE)) {
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
        ShadowFieldAdder.createShadowField(shadow.access, access, name).accept(shadow);
        return null;
    }

    @Override
    public void visitEnd() {
        if (!AsmUtil.isSet(shadow.access, Opcodes.ACC_INTERFACE)) {
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

    private byte[] createShadowClass(ClassReader cr) {
        ShadowClassBuilder builder = new ShadowClassBuilder();
        cr.accept(builder, ClassReader.SKIP_CODE);
        ClassNode shadow = builder.getShadow();
        return toBytes(shadow);
    }
}

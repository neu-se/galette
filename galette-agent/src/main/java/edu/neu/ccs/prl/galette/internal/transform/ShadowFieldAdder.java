package edu.neu.ccs.prl.galette.internal.transform;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

public class ShadowFieldAdder {
    /**
     * Descriptor for {@link Tag}.
     */
    static final String TAG_DESCRIPTOR = Type.getDescriptor(Tag.class);

    public void process(ClassNode cn) {
        // Add the shadow fields at the end after all other fields have been visited.
        // This will minimize changes to offsets for certain critical classes.
        if (hasShadowFields(cn.name)) {
            for (FieldNode fn : cn.fields.toArray(new FieldNode[0])) {
                cn.fields.add(createShadowField(cn.access, fn.access, fn.name));
            }
        }
    }

    private static FieldNode createShadowField(int classAccess, int access, String name) {
        int shadowAccess = Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC;
        if (AsmUtil.isSet(classAccess, Opcodes.ACC_INTERFACE)) {
            shadowAccess |= Opcodes.ACC_FINAL;
        }
        if (AsmUtil.isSet(access, Opcodes.ACC_STATIC)) {
            shadowAccess |= Opcodes.ACC_STATIC;
        }
        if (AsmUtil.isSet(access, Opcodes.ACC_VOLATILE)) {
            shadowAccess |= Opcodes.ACC_VOLATILE;
        }
        if (AsmUtil.isSet(access, Opcodes.ACC_TRANSIENT)) {
            shadowAccess |= Opcodes.ACC_TRANSIENT;
        }
        return new FieldNode(
                GaletteTransformer.ASM_VERSION, shadowAccess, getShadowFieldName(name), TAG_DESCRIPTOR, null, null);
    }

    public static String getShadowFieldName(String fieldName) {
        return GaletteTransformer.ADDED_MEMBER_PREFIX + fieldName;
    }

    public static boolean isShadowField(String fieldName) {
        return fieldName.startsWith(GaletteTransformer.ADDED_MEMBER_PREFIX);
    }

    /**
     * Returns {@code false} if fields should not be added to the class with specified internal name because the JVM
     * may use a hard-coded offset to access a field of the class.
     * Only classes that are part of the Java Class Library (JCL) have hard-coded offsets.
     * These classes were identified based on the classes indicated as having hard-coded offsets in the source code file
     * "src/hotspot/share/classfile/classFileParser.cpp" from Eclipse Temurin JDK (version 11.0.21+9) and manual
     * experimentation.
     *
     * @param className internal name of a class
     * @throws NullPointerException if the specified class name is {@code null}
     */
    public static boolean hasShadowFields(String className) {
        // TODO: Fix mirroring of SoftReference#referent (and other fields inherited from supertype)
        switch (className) {
            case "java/lang/ref/Reference":
            case "java/lang/ref/SoftReference":
                // The following types are all final, so it is safe to not shadow their fields
            case "java/lang/Boolean":
            case "java/lang/Character":
            case "java/lang/Float":
            case "java/lang/Double":
            case "java/lang/Byte":
            case "java/lang/Short":
            case "java/lang/Integer":
            case "java/lang/Long":
            case "java/lang/StackTraceElement":
                return false;
            default:
                return true;
        }
    }
}

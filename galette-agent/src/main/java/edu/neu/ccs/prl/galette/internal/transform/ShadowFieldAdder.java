package edu.neu.ccs.prl.galette.internal.transform;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

class ShadowFieldAdder {
    /**
     * Descriptor for {@link Tag}.
     */
    static final String TAG_DESCRIPTOR = Type.getDescriptor(Tag.class);

    public void process(ClassNode cn) {
        // Add the shadow fields at the end after all other fields have been visited.
        // This will minimize changes to offsets for certain critical classes.
        if (!HardCoded.hasHardCodedStaticOffset(cn.name)) {
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

    public static boolean hasShadowFields(String owner) {
        return !HardCoded.hasHardCodedStaticOffset(owner);
    }
}

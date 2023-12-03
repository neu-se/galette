package edu.neu.ccs.prl.phosphor.internal.agent;

import edu.neu.ccs.prl.phosphor.internal.runtime.SimpleList;
import edu.neu.ccs.prl.phosphor.internal.runtime.Tag;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;

class ShadowFieldAdder extends ClassVisitor {
    /**
     * Descriptor for {@link Tag}.
     */
    private static final String TAG_DESCRIPTOR = Type.getDescriptor(Tag.class);
    /**
     * The access flags of the class being visited.
     */
    private int classAccess;
    /**
     * List of shadow field that should be added to the class being visited.
     * <p>
     * Non-null.
     */
    private final SimpleList<FieldNode> shadowFields = new SimpleList<>();

    ShadowFieldAdder(ClassVisitor classVisitor) {
        super(PhosphorAgent.ASM_VERSION, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        classAccess = access;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        shadowFields.add(createShadowField(classAccess, access, name));
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public void visitEnd() {
        // Add the shadow fields at the end after all other fields have been visited.
        // This will hopefully preserve the offset of instance fields and preserve the offset of static fields
        // assuming that there are no instance fields.
        for (int i = 0; i < shadowFields.size(); i++) {
            shadowFields.get(i).accept(getDelegate());
        }
        super.visitEnd();
    }

    static FieldNode createShadowField(int classAccess, int access, String name) {
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
                PhosphorAgent.ASM_VERSION, shadowAccess, getShadowFieldName(name), TAG_DESCRIPTOR, null, null);
    }

    public static String getShadowFieldName(String fieldName) {
        return PhosphorAgent.ADDED_MEMBER_PREFIX + fieldName;
    }
}

package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.internal.runtime.collection.SimpleList;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

class OriginalMethodProcessor {
    /**
     * The class being processed.
     * <p>
     * Non-null.
     */
    private final ClassNode classNode;
    /**
     * {@code true} if the class being processed is an interface.
     */
    private final boolean isInterface;

    OriginalMethodProcessor(ClassNode classNode) {
        this.classNode = classNode;
        this.isInterface = AsmUtil.isSet(classNode.access, Opcodes.ACC_INTERFACE);
    }

    public SimpleList<MethodNode> process() {
        SimpleList<MethodNode> processed = new SimpleList<>();
        for (MethodNode mn : classNode.methods) {
            processed.add(process(mn));
        }
        return processed;
    }

    private MethodNode process(MethodNode mn) {
        MethodNode processed = new MethodNode(
                PhosphorTransformer.ASM_VERSION, mn.access, mn.name, mn.desc, mn.signature, AsmUtil.copyExceptions(mn));
        MethodVisitor mv;
        if (!AsmUtil.isSet(mn.access, Opcodes.ACC_NATIVE) && !AsmUtil.isSet(mn.access, Opcodes.ACC_ABSTRACT)) {
            // Convert non-native, non-abstract methods to wrappers around the corresponding shadow
            mv = new WrapperCreator(
                    classNode.name, isInterface, processed, ShadowMethodCreator.getShadowMethodName(mn.name));
            // TODO remove
            mv = processed;
            // Apply masks
            mv = new MaskApplier(mv);
        } else {
            mv = processed;
        }
        mn.accept(mv);
        return processed;
    }
}

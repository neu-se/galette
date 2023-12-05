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
    /**
     * {@code true} if taint tag propagation logic should be added.
     */
    private final boolean propagate;

    OriginalMethodProcessor(ClassNode classNode, boolean propagate) {
        this.classNode = classNode;
        this.isInterface = AsmUtil.isSet(classNode.access, Opcodes.ACC_INTERFACE);
        this.propagate = propagate;
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
        MethodVisitor mv = new MaskApplier(processed);
        if (AsmUtil.hasMethodBody(mn.access)) {
            // TODO actually check if class was created with defineAnonymousClass
            if (!classNode.name.contains("$$Lambda$")
                    && !classNode.name.startsWith("jdk/")
                    && !classNode.name.startsWith("java/")
                    && ShadowMethodCreator.shouldShadow(classNode.name, mn.name)) {
                // Convert non-native, non-abstract methods to wrappers around the corresponding shadow
                mv = new WrapperCreator(
                        classNode.name, isInterface, mv, processed.access, processed.name, processed.desc);
            } else if (propagate) {
                // If this class is an anonymous lambda (created via Unsafe#defineAnonymousClass),
                // calling the shadow can cause issues
                // If there is no shadow, add the propagation logic directly to the original method
                mv = new TagPropagator(mv);
            }
        }
        mn.accept(mv);
        return processed;
    }
}

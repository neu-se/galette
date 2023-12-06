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
    /**
     * {@code true} if this class will be defined using {@link jdk.internal.misc.Unsafe#defineAnonymousClass}.
     */
    private final boolean isHostedAnonymous;

    OriginalMethodProcessor(ClassNode classNode, boolean propagate, boolean isHostedAnonymous) {
        this.classNode = classNode;
        this.isInterface = AsmUtil.isSet(classNode.access, Opcodes.ACC_INTERFACE);
        this.propagate = propagate;
        this.isHostedAnonymous = isHostedAnonymous;
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
            if (!isGeneratedProxy(classNode.name)
                    && !classNode.name.startsWith("java/")
                    && ShadowMethodCreator.shouldShadow(classNode.name, mn.name)) {
                // Convert non-native, non-abstract methods to wrappers around the corresponding shadow
                mv = new CallerSensitiveFixer(mv);
                mv = new WrapperCreator(classNode.name, isInterface, mv, processed, isHostedAnonymous);
            } else if (propagate) {
                // If there is no shadow, add the propagation logic directly to the original method
                mv = new TagPropagator(mv, mn, false);
            }
        }
        mn.accept(mv);
        return processed;
    }

    private static boolean isGeneratedProxy(String className) {
        return className.startsWith("jdk/internal/reflect/Generated") || className.startsWith("sun/reflect/Generated");
    }
}

package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.internal.runtime.PhosphorFrame;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

class ShadowMethodAdder {
    /**
     * {@code true} if shadow methods should be added, but not taint tag propagation logic.
     */
    private final boolean minimal;

    ShadowMethodAdder(boolean minimal) {
        this.minimal = minimal;
    }

    public void process(ClassNode cn) {
        MethodNode[] methods = cn.methods.toArray(new MethodNode[0]);
        cn.methods.clear();
        boolean isInterface = AsmUtil.isSet(cn.access, Opcodes.ACC_INTERFACE);
        for (MethodNode mn : methods) {
            String[] exceptionsArray = mn.exceptions == null ? null : mn.exceptions.toArray(new String[0]);
            int shadowAccess = (mn.access & ~Opcodes.ACC_NATIVE) | Opcodes.ACC_SYNTHETIC;
            MethodNode shadow = new MethodNode(
                    PhosphorTransformer.ASM_VERSION,
                    shadowAccess,
                    getShadowMethodName(mn.name),
                    getShadowMethodDescriptor(mn.desc),
                    getShadowSignature(mn.signature),
                    exceptionsArray);
            MethodNode original = new MethodNode(
                    PhosphorTransformer.ASM_VERSION, mn.access, mn.name, mn.desc, mn.signature, exceptionsArray);
            if (mn.name.equals("<clinit>")) {
                // Do not add a shadow for the class initialization method
                // Propagate tags if not minimal
                mn.accept(minimal ? original : new TagPropagator(original));
                cn.methods.add(original);
            } else if (AsmUtil.isSet(mn.access, Opcodes.ACC_NATIVE) || minimal) {
                // Add the original, native method
                cn.methods.add(mn);
                // Add a wrapper
                mn.accept(new HotSpotAnnotationRemover(new WrapperCreator(cn.name, isInterface, shadow, mn)));
                cn.methods.add(shadow);
            } else {
                mn.accept(new TagPropagator(shadow));
                cn.methods.add(shadow);
                if (!minimal) {
                    // TODO fix
                    cn.methods.add(mn);
                } else {
                    // Convert the original method into a wrapper around the shadow
                    mn.instructions.clear();
                    mn.accept(new WrapperCreator(cn.name, isInterface, original, shadow));
                    cn.methods.add(original);
                }
            }
        }
        // TODO add wrappers for Object methods to concreate classes
    }

    public static String getShadowSignature(String signature) {
        // TODO
        return null;
    }

    public static String getShadowMethodName(String name) {
        return name.equals("<init>") ? name : PhosphorTransformer.ADDED_MEMBER_PREFIX + name;
    }

    public static String getShadowMethodDescriptor(String descriptor) {
        Type[] args = Type.getArgumentTypes(descriptor);
        Type[] buffer = new Type[args.length + 1];
        System.arraycopy(args, 0, buffer, 0, args.length);
        buffer[args.length] = Type.getType(PhosphorFrame.class);
        Type returnType = Type.getReturnType(descriptor);
        return Type.getMethodDescriptor(returnType, buffer);
    }
}

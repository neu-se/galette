package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.internal.runtime.PhosphorFrame;
import edu.neu.ccs.prl.phosphor.internal.runtime.collection.SimpleList;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

class ShadowMethodCreator {
    /**
     * Type for {@link PhosphorFrame}.
     * <p>
     * Non-null.
     */
    private static final Type FRAME_TYPE = Type.getType(PhosphorFrame.class);
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

    ShadowMethodCreator(ClassNode classNode, boolean propagate) {
        this.classNode = classNode;
        this.isInterface = AsmUtil.isSet(classNode.access, Opcodes.ACC_INTERFACE);
        this.propagate = propagate;
    }

    public SimpleList<MethodNode> createShadows() {
        SimpleList<MethodNode> shadows = new SimpleList<>();
        for (MethodNode mn : classNode.methods) {
            if (shouldShadow(classNode.name, mn.name)) {
                shadows.add(createShadow(mn));
            }
        }
        if (!AsmUtil.isSet(classNode.access, Opcodes.ACC_ABSTRACT)
                && (classNode.superName == null || classNode.superName.equals("java/lang/Object"))) {
            // TODO add shadow wrappers for Object methods not overriden by this class
        }
        return shadows;
    }

    private MethodNode createShadow(MethodNode mn) {
        int shadowAccess = (mn.access & ~Opcodes.ACC_NATIVE) | Opcodes.ACC_SYNTHETIC;
        MethodNode shadow = new MethodNode(
                PhosphorTransformer.ASM_VERSION,
                shadowAccess,
                getShadowMethodName(mn.name),
                getShadowMethodDescriptor(mn.desc),
                getShadowSignature(mn.signature),
                AsmUtil.copyExceptions(mn));
        MethodVisitor mv = new MaskApplier(shadow);
        mv = new HotSpotAnnotationRemover(mv);
        if (AsmUtil.isSet(mn.access, Opcodes.ACC_NATIVE)) {
            mv = new WrapperCreator(classNode.name, isInterface, mv, shadow.access, shadow.name, shadow.desc);
        } else if (propagate) {
            mv = new TagPropagator(mv);
        }
        mn.accept(mv);
        return shadow;
    }

    private static String getShadowSignature(String signature) {
        int i;
        if (signature == null || (i = signature.indexOf('(')) == -1) {
            return null;
        }
        String start = signature.substring(0, i);
        String end = signature.substring(i);
        return start + FRAME_TYPE.getDescriptor() + end;
    }

    public static String getOriginalMethodName(String name) {
        if ("<clinit>".equals(name)) {
            throw new IllegalArgumentException();
        } else if (name.startsWith(PhosphorTransformer.ADDED_MEMBER_PREFIX)) {
            return name.substring(PhosphorTransformer.ADDED_MEMBER_PREFIX.length());
        } else {
            return name;
        }
    }

    public static String getShadowMethodName(String name) {
        if ("<clinit>".equals(name)) {
            throw new IllegalArgumentException();
        } else if (name.equals("<init>")) {
            // Cannot rename instance initialization methods
            return name;
        } else if (name.startsWith(PhosphorTransformer.ADDED_MEMBER_PREFIX)) {
            // This is already a shadow method name
            return name;
        } else {
            return PhosphorTransformer.ADDED_MEMBER_PREFIX + name;
        }
    }

    public static String getOriginalMethodDescriptor(String descriptor) {
        Type[] args = Type.getArgumentTypes(descriptor);
        Type[] buffer = new Type[args.length - 1];
        System.arraycopy(args, 0, buffer, 0, args.length - 1);
        Type returnType = Type.getReturnType(descriptor);
        return Type.getMethodDescriptor(returnType, buffer);
    }

    public static String getShadowMethodDescriptor(String descriptor) {
        Type[] args = Type.getArgumentTypes(descriptor);
        Type[] buffer = new Type[args.length + 1];
        System.arraycopy(args, 0, buffer, 0, args.length);
        buffer[args.length] = FRAME_TYPE;
        Type returnType = Type.getReturnType(descriptor);
        return Type.getMethodDescriptor(returnType, buffer);
    }

    public static boolean isShadowMethod(String descriptor) {
        return descriptor.contains(FRAME_TYPE.getDescriptor());
    }

    public static boolean shouldShadow(String className, String methodName) {
        if (methodName.equals("<clinit>")) {
            return false;
        } else if (methodName.equals("<init>")) {
            // InnerClassLambdaMetafactory will check the number of constructors for some lambdas.
            // Since instance initialization methods are never dynamically dispatched (INVOKESPECIAL is used),
            // it is safe for these shadows to be missing.
            // TODO check if this is necessary
            return !className.contains("$$Lambda$");
        } else {
            return true;
        }
    }
}

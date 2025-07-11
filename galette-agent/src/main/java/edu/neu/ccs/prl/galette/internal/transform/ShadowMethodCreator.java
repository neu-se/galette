package edu.neu.ccs.prl.galette.internal.transform;

import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.collection.SimpleList;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public final class ShadowMethodCreator {
    /**
     * Type for {@link TagFrame}.
     * <p>
     * Non-null.
     */
    private static final Type FRAME_TYPE = Type.getType(TagFrame.class);
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
     * {@code true} if this class will be defined using {@code jdk.internal.misc.Unsafe#defineAnonymousClass}.
     */
    private final boolean isHostedAnonymous;

    ShadowMethodCreator(ClassNode classNode, boolean propagate, boolean isHostedAnonymous) {
        this.classNode = classNode;
        this.isInterface = AsmUtil.isSet(classNode.access, Opcodes.ACC_INTERFACE);
        this.propagate = propagate;
        this.isHostedAnonymous = isHostedAnonymous;
    }

    public SimpleList<MethodNode> createShadows() {
        SimpleList<MethodNode> shadows = new SimpleList<>();
        for (MethodNode mn : classNode.methods) {
            if (shouldShadow(mn.name)) {
                shadows.add(createShadow(mn));
            }
        }
        if ((classNode.superName == null || classNode.superName.equals("java/lang/Object"))) {
            // If java/lang/Object is the immediate superclass of this class
            SimpleList<ObjectMethod> missing = findMissingObjectShadows();
            for (int i = 0; i < missing.size(); i++) {
                shadows.add(createObjectShadow(missing.get(i)));
            }
            classNode.interfaces.add(GaletteNames.TAGGED_OBJECT_INTERNAL_NAME);
        }
        return shadows;
    }

    private MethodNode createShadow(MethodNode mn) {
        int shadowAccess = (mn.access & ~Opcodes.ACC_NATIVE) | Opcodes.ACC_SYNTHETIC;
        MethodNode shadow = new MethodNode(
                GaletteTransformer.ASM_VERSION,
                shadowAccess,
                mn.name,
                getShadowMethodDescriptor(mn.desc),
                getShadowSignature(mn.signature),
                AsmUtil.copyExceptions(mn));
        MethodVisitor mv = new MaskApplier(classNode.name, shadow);
        mv = new HotSpotAnnotationRemover(mv);
        if (AsmUtil.isSet(mn.access, Opcodes.ACC_NATIVE)) {
            mv = NativeWrapperCreator.newInstance(classNode.name, isInterface, mv, shadow, isHostedAnonymous, false);
        } else if (propagate) {
            mv = TagPropagator.newInstance(mv, mn, true, classNode.name);
        }
        mn.accept(mv);
        return shadow;
    }

    private MethodNode createObjectShadow(ObjectMethod objectMethod) {
        MethodRecord record = objectMethod.getRecord();
        int shadowAccess = Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC;
        if (isInterface) {
            shadowAccess |= Opcodes.ACC_ABSTRACT;
        }
        MethodNode shadow = new MethodNode(
                GaletteTransformer.ASM_VERSION,
                shadowAccess,
                record.getName(),
                getShadowMethodDescriptor(record.getDescriptor()),
                getShadowSignature(null),
                null);
        if (!isInterface) {
            MethodVisitor mv = new MaskApplier(classNode.name, shadow);
            mv = NativeWrapperCreator.newInstance(classNode.name, false, mv, shadow, isHostedAnonymous, true);
            mv.visitEnd();
        }
        return shadow;
    }

    private SimpleList<ObjectMethod> findMissingObjectShadows() {
        // Find virtual methods from java/lang/Object for which we have not already created a shadow
        boolean[] matches = new boolean[ObjectMethod.values().length];
        for (MethodNode mn : classNode.methods) {
            ObjectMethod m = ObjectMethod.findMatch(mn.name, mn.desc);
            if (m != null) {
                matches[m.ordinal()] = true;
            }
        }
        SimpleList<ObjectMethod> result = new SimpleList<>();
        for (int i = 0; i < matches.length; i++) {
            if (!matches[i]) {
                result.add(ObjectMethod.values()[i]);
            }
        }
        return result;
    }

    private static String getShadowSignature(String signature) {
        int i;
        if (signature == null || (i = signature.indexOf(')')) == -1) {
            return null;
        }
        String start = signature.substring(0, i);
        String end = signature.substring(i);
        return start + FRAME_TYPE.getDescriptor() + end;
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
        return descriptor.contains(FRAME_TYPE.getDescriptor() + ")");
    }

    public static boolean shouldShadow(String methodName) {
        return !methodName.equals("<clinit>");
    }
}

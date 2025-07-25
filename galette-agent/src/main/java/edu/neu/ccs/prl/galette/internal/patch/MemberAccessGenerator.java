package edu.neu.ccs.prl.galette.internal.patch;

import edu.neu.ccs.prl.galette.internal.runtime.mask.MemberAccess;
import edu.neu.ccs.prl.galette.internal.transform.AsmUtil;
import edu.neu.ccs.prl.galette.internal.transform.GaletteTransformer;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class MemberAccessGenerator extends ClassVisitor {
    /**
     * Mapping from method identifiers in the class being visited to the {@link MemberAccess} annotation on
     * the corresponding method.
     * <p>
     * Non-null.
     */
    private final Map<String, MemberAccess> patchMap;

    MemberAccessGenerator(String className, ClassVisitor cv) {
        super(GaletteTransformer.ASM_VERSION, cv);
        this.patchMap = createPatchMap(className);
    }

    @Override
    public MethodVisitor visitMethod(
            int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor target = super.visitMethod(access, name, descriptor, signature, exceptions);
        MemberAccess member = patchMap.get(name + descriptor);
        if (member != null) {
            // Set the delegate method visitor to null to replace the body of the method
            return new MethodVisitor(GaletteTransformer.ASM_VERSION, null) {
                @Override
                public void visitCode() {
                    generateBody(access, descriptor, member, target);
                }
            };
        }
        return target;
    }

    private void generateBody(int access, String descriptor, MemberAccess mAccess, MethodVisitor mv) {
        mv.visitCode();
        switch (mAccess.opcode()) {
            case Opcodes.INVOKESPECIAL:
            case Opcodes.INVOKEVIRTUAL:
            case Opcodes.INVOKEINTERFACE:
            case Opcodes.INVOKESTATIC:
                generateMethodCall(access, descriptor, mAccess, mv);
                break;
            case Opcodes.GETFIELD:
                generateGetField(access, descriptor, mAccess, mv);
                break;
            case Opcodes.PUTFIELD:
                generatePutField(access, descriptor, mAccess, mv);
                break;
            default:
                throw new IllegalArgumentException("Unsupported opcode: " + mAccess.opcode());
        }
        mv.visitMaxs(-1, -1);
        mv.visitEnd();
    }

    private void generateGetField(int access, String descriptor, MemberAccess mAccess, MethodVisitor mv) {
        fixReceiver(access, mAccess, mv);
        AsmUtil.loadArguments(mv, descriptor, AsmUtil.isSet(access, Opcodes.ACC_STATIC) ? 0 : 1);
        Type ret = Type.getReturnType(descriptor);
        mv.visitFieldInsn(mAccess.opcode(), mAccess.owner(), mAccess.name(), ret.getDescriptor());
        mv.visitInsn(Type.getReturnType(descriptor).getOpcode(Opcodes.IRETURN));
    }

    private void generatePutField(int access, String descriptor, MemberAccess mAccess, MethodVisitor mv) {
        fixReceiver(access, mAccess, mv);
        AsmUtil.loadArguments(mv, descriptor, AsmUtil.isSet(access, Opcodes.ACC_STATIC) ? 0 : 1);
        Type[] args = Type.getArgumentTypes(descriptor);
        mv.visitFieldInsn(mAccess.opcode(), mAccess.owner(), mAccess.name(), args[args.length - 1].getDescriptor());
        mv.visitInsn(Type.getReturnType(descriptor).getOpcode(Opcodes.IRETURN));
    }

    private void generateMethodCall(int access, String descriptor, MemberAccess mAccess, MethodVisitor mv) {
        if (mAccess.name().equals("<init>")) {
            mv.visitTypeInsn(Opcodes.NEW, mAccess.owner());
            mv.visitInsn(Opcodes.DUP);
        } else {
            fixReceiver(access, mAccess, mv);
        }
        AsmUtil.loadArguments(mv, descriptor, AsmUtil.isSet(access, Opcodes.ACC_STATIC) ? 0 : 1);
        mv.visitMethodInsn(
                mAccess.opcode(),
                mAccess.owner(),
                mAccess.name(),
                computeMethodDescriptor(access, descriptor, mAccess),
                mAccess.isInterface());
        mv.visitInsn(Type.getReturnType(descriptor).getOpcode(Opcodes.IRETURN));
    }

    private void fixReceiver(int access, MemberAccess mAccess, MethodVisitor mv) {
        // Cast the receiver
        if (mAccess.opcode() != Opcodes.INVOKESTATIC) {
            // Skip "this" for virtual methods
            int varIndex = AsmUtil.isSet(access, Opcodes.ACC_STATIC) ? 0 : 1;
            mv.visitVarInsn(Opcodes.ALOAD, varIndex);
            mv.visitTypeInsn(Opcodes.CHECKCAST, mAccess.owner());
            mv.visitVarInsn(Opcodes.ASTORE, varIndex);
        }
    }

    private static String computeMethodDescriptor(int access, String descriptor, MemberAccess mAccess) {
        Type[] srcArgs = Type.getArgumentTypes(descriptor);
        Type ret = Type.getReturnType(descriptor);
        // Skip the receiver of the annotated method if it is an instance method
        int srcPosition = AsmUtil.isSet(access, Opcodes.ACC_STATIC) ? 0 : 1;
        if (mAccess.name().equals("<init>")) {
            ret = Type.VOID_TYPE;
        } else if (mAccess.opcode() != Opcodes.INVOKESTATIC) {
            // Skip the receiver of the target method if it is an instance method
            srcPosition++;
        }
        Type[] targetArgs = new Type[srcArgs.length - srcPosition];
        System.arraycopy(srcArgs, srcPosition, targetArgs, 0, targetArgs.length);
        return Type.getMethodDescriptor(ret, targetArgs);
    }

    private static Map<String, MemberAccess> createPatchMap(String className) {
        Map<String, MemberAccess> patchMap = new HashMap<>();
        try {
            Class<?> clazz = Class.forName(className.replace('/', '.'));
            for (Method method : clazz.getDeclaredMethods()) {
                for (MemberAccess member : method.getAnnotationsByType(MemberAccess.class)) {
                    patchMap.put(method.getName() + Type.getMethodDescriptor(method), member);
                }
            }
        } catch (ReflectiveOperationException e) {
            //
        }
        return patchMap;
    }
}

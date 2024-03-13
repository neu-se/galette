package edu.neu.ccs.prl.galette.internal.patch;

import edu.neu.ccs.prl.galette.internal.transform.AsmUtil;
import edu.neu.ccs.prl.galette.internal.transform.Configuration;
import edu.neu.ccs.prl.galette.internal.transform.GaletteTransformer;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ConfigurationEmbedder extends ClassVisitor {
    private static final String TARGET_METHOD_NAME = "<clinit>";

    protected ConfigurationEmbedder(ClassVisitor classVisitor) {
        super(GaletteTransformer.ASM_VERSION, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(
            int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        return name.equals(TARGET_METHOD_NAME)
                ? new MethodVisitor(api, mv) {
                    @Override
                    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                        if (name.equals("IS_JAVA_8")) {
                            super.visitInsn(Opcodes.POP);
                            AsmUtil.pushInt(this.mv, 0);
                        }
                        super.visitFieldInsn(opcode, owner, name, descriptor);
                    }
                }
                : mv;
    }

    public static boolean isApplicable(String className) {
        return Type.getInternalName(Configuration.class).equals(className);
    }
}

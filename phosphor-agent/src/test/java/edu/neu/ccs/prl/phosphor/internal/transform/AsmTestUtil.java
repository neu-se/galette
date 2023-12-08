package edu.neu.ccs.prl.phosphor.internal.transform;

import java.io.IOException;
import java.util.function.Function;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public final class AsmTestUtil {
    private AsmTestUtil() {
        throw new AssertionError();
    }

    public static MethodNode getMethodNode(Class<?> clazz, String name, String desc) throws IOException {
        ClassNode cn = new ClassNode();
        new ClassReader(clazz.getName()).accept(cn, ClassReader.EXPAND_FRAMES);
        return cn.methods.stream()
                .filter(mn -> mn.name.equals(name) && mn.desc.equals(desc))
                .findFirst()
                .orElseThrow(AssertionError::new);
    }

    public static ClassNode getClassNode(Class<?> clazz) {
        try {
            ClassNode cn = new ClassNode();
            new ClassReader(clazz.getName()).accept(cn, ClassReader.EXPAND_FRAMES);
            return cn;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ClassNode expandFramesAndComputeMaxStack(ClassNode cn) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cn.accept(cw);
        ClassReader cr = new ClassReader(cw.toByteArray());
        ClassNode result = new ClassNode();
        cr.accept(result, ClassReader.EXPAND_FRAMES);
        return result;
    }

    public static ClassNode instrument(ClassNode cn, Function<ClassVisitor, ClassVisitor> f) {
        ClassNode result = new ClassNode();
        cn = expandFramesAndComputeMaxStack(cn);
        cn.accept(f.apply(result));
        return expandFramesAndComputeMaxStack(result);
    }

    public static Class<?> load(ClassNode cn) {
        return new NodeClassLoader().createClass(cn);
    }

    private static class NodeClassLoader extends ClassLoader {
        public Class<?> createClass(ClassNode cn) {
            byte[] bytes = AsmUtil.toBytes(cn);
            return defineClass(null, bytes, 0, bytes.length);
        }
    }
}

package edu.neu.ccs.prl.galette.internal.transform;

import edu.neu.ccs.prl.galette.example.NodeInstructionExamples;
import java.io.IOException;
import java.util.function.Function;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public final class AsmTestUtil {
    private AsmTestUtil() {
        throw new AssertionError();
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

    public static Class<?> instrumentAndLoad(MethodNode target, Function<byte[], byte[]> f) {
        ClassNode cn = AsmTestUtil.getClassNode(NodeInstructionExamples.class);
        cn.methods.clear();
        cn.methods.add(target);
        byte[] buffer = f.apply(toBytes(cn));
        return new ByteClassLoader().createClass(buffer);
    }

    public static Class<?> instrumentAndLoad(Class<?> original, String methodName, Function<byte[], byte[]> f) {
        ClassNode cn = getClassNode(original);
        MethodNode match = cn.methods.stream()
                .filter(mn -> mn.name.equals(methodName))
                .findFirst()
                .orElseThrow(AssertionError::new);
        cn.methods.clear();
        cn.methods.add(match);
        byte[] buffer = f.apply(toBytes(cn));
        return new ByteClassLoader().createClass(buffer);
    }

    public static Class<?> instrumentAndLoad(Class<?> original, Function<byte[], byte[]> f) {
        ClassNode cn = getClassNode(original);
        byte[] buffer = f.apply(toBytes(cn));
        return new ByteClassLoader().createClass(buffer);
    }

    public static Class<?> load(ClassNode cn) {
        return new ByteClassLoader().createClass(toBytes(cn));
    }

    public static ClassNode instrument(ClassNode cn, Function<byte[], byte[]> f) {
        byte[] buffer = f.apply(toBytes(cn));
        ClassReader cr = new ClassReader(buffer);
        ClassNode result = new ClassNode();
        cr.accept(result, ClassReader.EXPAND_FRAMES);
        return result;
    }

    public static byte[] toBytes(ClassNode cn) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cn.accept(cw);
        return cw.toByteArray();
    }

    private static class ByteClassLoader extends ClassLoader {
        public Class<?> createClass(byte[] buffer) {
            return defineClass(null, buffer, 0, buffer.length);
        }
    }
}

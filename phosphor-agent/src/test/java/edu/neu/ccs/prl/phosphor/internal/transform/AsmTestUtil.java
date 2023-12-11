package edu.neu.ccs.prl.phosphor.internal.transform;

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

    public static Class<?> instrument(Class<?> original, String methodName, Function<byte[], byte[]> f) {
        ClassNode cn = getClassNode(original);
        MethodNode match = cn.methods.stream()
                .filter(mn -> mn.name.equals(methodName))
                .findFirst()
                .orElseThrow(AssertionError::new);
        cn.methods.clear();
        cn.methods.add(match);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cn.accept(cw);
        byte[] buffer = f.apply(cw.toByteArray());
        return new ByteClassLoader().createClass(buffer);
    }

    public static Class<?> load(ClassNode cn) {
        return new ByteClassLoader().createClass(AsmUtil.toBytes(cn));
    }

    private static class ByteClassLoader extends ClassLoader {
        public Class<?> createClass(byte[] buffer) {
            return defineClass(null, buffer, 0, buffer.length);
        }
    }
}

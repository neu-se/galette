package edu.neu.ccs.prl.galette.internal.transform;

import static org.objectweb.asm.Opcodes.*;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.Tainter;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

public class TagCheckingMethodBuilder {
    private final MethodNode methodNode;

    public TagCheckingMethodBuilder() {
        String desc = Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(TagRecorder.class));
        this.methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "example", desc, null, null);
        methodNode.visitCode();
    }

    public MethodNode getMethodNode() {
        return methodNode;
    }

    public void loadAndTag(Type type, Object value) {
        methodNode.visitLdcInsn(value);
        methodNode.visitLdcInsn(String.valueOf(value));
        String ofDesc = Type.getMethodDescriptor(Type.getType(Tag.class), Type.getType(Object.class));
        methodNode.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Tag.class), "of", ofDesc, false);
        String setDesc = Type.getMethodDescriptor(type, type, Type.getType(Tag.class));
        methodNode.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Tainter.class), "setTag", setDesc, false);
    }

    public void recordTags(Type... types) {
        for (Type type : types) {
            String getDesc = Type.getMethodDescriptor(Type.getType(Tag.class), type);
            methodNode.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Tainter.class), "getTag", getDesc, false);
            methodNode.visitVarInsn(ALOAD, 0);
            methodNode.visitInsn(SWAP);
            String recordDesc = Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(Tag.class));
            methodNode.visitMethodInsn(
                    INVOKEVIRTUAL, Type.getInternalName(TagRecorder.class), "record", recordDesc, false);
        }
    }

    public Method build(Function<byte[], byte[]> f) throws NoSuchMethodException {
        methodNode.visitInsn(RETURN);
        methodNode.visitMaxs(-1, -1);
        methodNode.visitEnd();
        Class<?> clazz = AsmTestUtil.instrumentAndLoad(methodNode, f);
        return clazz.getDeclaredMethod(methodNode.name, TagRecorder.class, TagFrame.class);
    }

    public static final class TagRecorder {
        public final List<Tag> tags = new LinkedList<>();

        @SuppressWarnings("unused")
        public void record(Tag tag) {
            tags.add(tag);
        }

        public List<Object> getFirstLabels() {
            return tags.stream().map(t -> t.getLabels()).map(o -> o[0]).collect(Collectors.toList());
        }
    }
}

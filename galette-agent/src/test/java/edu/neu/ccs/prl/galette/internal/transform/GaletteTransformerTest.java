package edu.neu.ccs.prl.galette.internal.transform;

import static org.objectweb.asm.Opcodes.*;

import edu.neu.ccs.prl.galette.example.*;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.transform.TagCheckingMethodBuilder.TagRecorder;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicVerifier;

class GaletteTransformerTest {
    @ParameterizedTest(name = "returnValueUnaffected: {0}")
    @MethodSource("executionArguments")
    void returnValueUnaffected(String name) throws ReflectiveOperationException {
        Class<?> original = InstructionExamples.class;
        Class<?> instrumented = AsmTestUtil.instrumentAndLoad(original, name, GaletteTransformerTest::instrument);
        Object expected = original.getDeclaredMethod(name).invoke(null);
        Object actual = instrumented.getDeclaredMethod(name, TagFrame.class).invoke(null, TagFrame.create(null));
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest(name = "passesVerification: {0}")
    @MethodSource("verificationArguments")
    void passesVerification(String name) throws ReflectiveOperationException, AnalyzerException {
        MethodNode target = (MethodNode)
                NodeInstructionExamples.class.getDeclaredMethod(name).invoke(null);
        ClassNode original = AsmTestUtil.getClassNode(NodeInstructionExamples.class);
        original.methods.clear();
        original.methods.add(target);
        ClassNode instrumented = AsmTestUtil.instrument(original, GaletteTransformerTest::instrument);
        for (MethodNode mn : instrumented.methods) {
            new Analyzer<>(new BasicVerifier()).analyze(original.name, mn);
        }
    }

    @Test
    void getStaticInt() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrumentAndLoad(FieldExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, TagFrame.create(null));
        Object result =
                instrumented.getDeclaredMethod("getStaticInt", TagFrame.class).invoke(instance, TagFrame.create(null));
        Assertions.assertEquals(7, result);
    }

    @Test
    void getFieldInt() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrumentAndLoad(FieldExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, TagFrame.create(null));
        Object result =
                instrumented.getDeclaredMethod("getFieldInt", TagFrame.class).invoke(instance, TagFrame.create(null));
        Assertions.assertEquals(99, result);
    }

    @Test
    void putFieldInt() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrumentAndLoad(FieldExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, TagFrame.create(null));
        instrumented
                .getDeclaredMethod("putFieldInt", int.class, TagFrame.class)
                .invoke(instance, 22, TagFrame.create(null));
    }

    @Test
    void putStaticInt() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrumentAndLoad(FieldExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, TagFrame.create(null));
        instrumented
                .getDeclaredMethod("putStaticInt", int.class, TagFrame.class)
                .invoke(instance, 88, TagFrame.create(null));
    }

    @Test
    void getStaticLong() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrumentAndLoad(FieldExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, TagFrame.create(null));
        Object result =
                instrumented.getDeclaredMethod("getStaticLong", TagFrame.class).invoke(instance, TagFrame.create(null));
        Assertions.assertEquals(7L, result);
    }

    @Test
    void getFieldLong() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrumentAndLoad(FieldExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, TagFrame.create(null));
        Object result =
                instrumented.getDeclaredMethod("getFieldLong", TagFrame.class).invoke(instance, TagFrame.create(null));
        Assertions.assertEquals(99L, result);
    }

    @Test
    void putFieldLong() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrumentAndLoad(FieldExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, TagFrame.create(null));
        instrumented
                .getDeclaredMethod("putFieldLong", long.class, TagFrame.class)
                .invoke(instance, 22L, TagFrame.create(null));
    }

    @Test
    void putStaticLong() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrumentAndLoad(FieldExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, TagFrame.create(null));
        instrumented
                .getDeclaredMethod("putStaticLong", long.class, TagFrame.class)
                .invoke(instance, 88L, TagFrame.create(null));
    }

    @Test
    void invokeVirtualIntReturn() throws ReflectiveOperationException {
        Class<?> instrumented =
                AsmTestUtil.instrumentAndLoad(MethodCallExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, TagFrame.create(null));
        Object result = instrumented
                .getDeclaredMethod("invokeVirtualIntReturn", int.class, TagFrame.class)
                .invoke(instance, 7, TagFrame.create(null));
        Assertions.assertEquals(7, result);
    }

    @Test
    void invokeVirtualLongReturn() throws ReflectiveOperationException {
        Class<?> instrumented =
                AsmTestUtil.instrumentAndLoad(MethodCallExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, TagFrame.create(null));
        Object result = instrumented
                .getDeclaredMethod("invokeVirtualLongReturn", long.class, TagFrame.class)
                .invoke(instance, 9L, TagFrame.create(null));
        Assertions.assertEquals(9L, result);
    }

    @Test
    void invokeVirtualMixedParameterTypes() throws ReflectiveOperationException {
        Class<?> instrumented =
                AsmTestUtil.instrumentAndLoad(MethodCallExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, TagFrame.create(null));
        Object result = instrumented
                .getDeclaredMethod("invokeVirtualMixedParameterTypes", int.class, long.class, int.class, TagFrame.class)
                .invoke(instance, -1, 9L, 2, TagFrame.create(null));
        Assertions.assertEquals(-1 + 9L + 2, result);
    }

    @Test
    void dupCheckTags() throws ReflectiveOperationException {
        // ..., value -> ..., value, value
        TagCheckingMethodBuilder builder = new TagCheckingMethodBuilder();
        builder.loadAndTag(Type.INT_TYPE, 10);
        builder.getMethodNode().visitInsn(DUP);
        builder.recordTags(Type.INT_TYPE, Type.INT_TYPE);
        TagRecorder recorder = new TagRecorder();
        builder.build(GaletteTransformerTest::instrument).invoke(null, recorder, TagFrame.create(null));
        Assertions.assertEquals(Arrays.asList("10", "10"), recorder.getFirstLabels());
    }

    @Test
    void dup_x1CheckTags() throws ReflectiveOperationException {
        // ..., value1, value2 -> ..., value2, value1, value2
        TagCheckingMethodBuilder builder = new TagCheckingMethodBuilder();
        builder.loadAndTag(Type.INT_TYPE, 10);
        builder.loadAndTag(Type.INT_TYPE, 20);
        builder.getMethodNode().visitInsn(DUP_X1);
        builder.recordTags(Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE);
        TagRecorder recorder = new TagRecorder();
        builder.build(GaletteTransformerTest::instrument).invoke(null, recorder, TagFrame.create(null));
        // Order is from top of stack down
        Assertions.assertEquals(Arrays.asList("20", "10", "20"), recorder.getFirstLabels());
    }

    @Test
    void dup_x2CheckTags() throws ReflectiveOperationException {
        // ..., value1, value2, value3 -> ..., value3, value1, value2, value3
        TagCheckingMethodBuilder builder = new TagCheckingMethodBuilder();
        builder.loadAndTag(Type.INT_TYPE, 10);
        builder.loadAndTag(Type.INT_TYPE, 20);
        builder.loadAndTag(Type.INT_TYPE, 30);
        builder.getMethodNode().visitInsn(DUP_X2);
        builder.recordTags(Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE);
        TagRecorder recorder = new TagRecorder();
        builder.build(GaletteTransformerTest::instrument).invoke(null, recorder, TagFrame.create(null));
        // Order is from top of stack down
        Assertions.assertEquals(Arrays.asList("30", "20", "10", "30"), recorder.getFirstLabels());
    }

    @Test
    void dup_x2WideCheckTags() throws ReflectiveOperationException {
        // ..., [value1, value2], value3 -> ..., value3, [value1, value2], value3
        TagCheckingMethodBuilder builder = new TagCheckingMethodBuilder();
        builder.loadAndTag(Type.LONG_TYPE, 10L);
        builder.loadAndTag(Type.INT_TYPE, 20);
        builder.getMethodNode().visitInsn(DUP_X2);
        builder.recordTags(Type.INT_TYPE, Type.LONG_TYPE, Type.INT_TYPE);
        TagRecorder recorder = new TagRecorder();
        builder.build(GaletteTransformerTest::instrument).invoke(null, recorder, TagFrame.create(null));
        // Order is from top of stack down
        Assertions.assertEquals(Arrays.asList("20", "10", "20"), recorder.getFirstLabels());
    }

    @Test
    void dup2CheckTags() throws ReflectiveOperationException {
        // ..., value1, value2 -> ..., value1, value2, value1, value2
        TagCheckingMethodBuilder builder = new TagCheckingMethodBuilder();
        builder.loadAndTag(Type.INT_TYPE, 10);
        builder.loadAndTag(Type.INT_TYPE, 20);
        builder.getMethodNode().visitInsn(DUP2);
        builder.recordTags(Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE);
        TagRecorder recorder = new TagRecorder();
        builder.build(GaletteTransformerTest::instrument).invoke(null, recorder, TagFrame.create(null));
        // Order is from top of stack down
        Assertions.assertEquals(Arrays.asList("20", "10", "20", "10"), recorder.getFirstLabels());
    }

    @Test
    void dup2WideCheckTags() throws ReflectiveOperationException {
        // ..., [value1, value2] -> ..., [value1, value2], [value1, value2]
        TagCheckingMethodBuilder builder = new TagCheckingMethodBuilder();
        builder.loadAndTag(Type.LONG_TYPE, 10L);
        builder.getMethodNode().visitInsn(DUP2);
        builder.recordTags(Type.LONG_TYPE, Type.LONG_TYPE);
        TagRecorder recorder = new TagRecorder();
        builder.build(GaletteTransformerTest::instrument).invoke(null, recorder, TagFrame.create(null));
        // Order is from top of stack down
        Assertions.assertEquals(Arrays.asList("10", "10"), recorder.getFirstLabels());
    }

    @Test
    void dup2_x1CheckTags() throws ReflectiveOperationException {
        // ..., value1, value2, value3 -> ..., value2, value3, value1, value2, value3
        TagCheckingMethodBuilder builder = new TagCheckingMethodBuilder();
        builder.loadAndTag(Type.INT_TYPE, 10);
        builder.loadAndTag(Type.INT_TYPE, 20);
        builder.loadAndTag(Type.INT_TYPE, 30);
        builder.getMethodNode().visitInsn(DUP2_X1);
        builder.recordTags(Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE);
        TagRecorder recorder = new TagRecorder();
        builder.build(GaletteTransformerTest::instrument).invoke(null, recorder, TagFrame.create(null));
        // Order is from top of stack down
        Assertions.assertEquals(Arrays.asList("30", "20", "10", "30", "20"), recorder.getFirstLabels());
    }

    @Test
    void dup2_x1WideCheckTags() throws ReflectiveOperationException {
        // ..., value1, [value2, value3] -> ..., [value2, value3], value1, [value2, value3]
        TagCheckingMethodBuilder builder = new TagCheckingMethodBuilder();
        builder.loadAndTag(Type.INT_TYPE, 10);
        builder.loadAndTag(Type.LONG_TYPE, 20L);
        builder.getMethodNode().visitInsn(DUP2_X1);
        builder.recordTags(Type.LONG_TYPE, Type.INT_TYPE, Type.LONG_TYPE);
        TagRecorder recorder = new TagRecorder();
        builder.build(GaletteTransformerTest::instrument).invoke(null, recorder, TagFrame.create(null));
        // Order is from top of stack down
        Assertions.assertEquals(Arrays.asList("20", "10", "20"), recorder.getFirstLabels());
    }

    @Test
    void dup2_x2CheckTags() throws ReflectiveOperationException {
        // ..., value1, value2, value3, value4 -> ..., value3, value4, value1, value2, value3, value4
        TagCheckingMethodBuilder builder = new TagCheckingMethodBuilder();
        builder.loadAndTag(Type.INT_TYPE, 10);
        builder.loadAndTag(Type.INT_TYPE, 20);
        builder.loadAndTag(Type.INT_TYPE, 30);
        builder.loadAndTag(Type.INT_TYPE, 40);
        builder.getMethodNode().visitInsn(DUP2_X2);
        builder.recordTags(Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE);
        TagRecorder recorder = new TagRecorder();
        builder.build(GaletteTransformerTest::instrument).invoke(null, recorder, TagFrame.create(null));
        // Order is from top of stack down
        Assertions.assertEquals(Arrays.asList("40", "30", "20", "10", "40", "30"), recorder.getFirstLabels());
    }

    @Test
    void dup2_x2WideTopCheckTags() throws ReflectiveOperationException {
        // ..., value1, value2, [value3, value4] -> ..., [value3, value4], value1, value2, [value3, value4]
        TagCheckingMethodBuilder builder = new TagCheckingMethodBuilder();
        builder.loadAndTag(Type.INT_TYPE, 10);
        builder.loadAndTag(Type.INT_TYPE, 20);
        builder.loadAndTag(Type.LONG_TYPE, 30L);
        builder.getMethodNode().visitInsn(DUP2_X2);
        builder.recordTags(Type.LONG_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.LONG_TYPE);
        TagRecorder recorder = new TagRecorder();
        builder.build(GaletteTransformerTest::instrument).invoke(null, recorder, TagFrame.create(null));
        // Order is from top of stack down
        Assertions.assertEquals(Arrays.asList("30", "20", "10", "30"), recorder.getFirstLabels());
    }

    @Test
    void dup2_x2WideBottomCheckTags() throws ReflectiveOperationException {
        // ..., [value1, value2], value3, value4 -> ..., value3, value4, [value1, value2], value3, value4
        TagCheckingMethodBuilder builder = new TagCheckingMethodBuilder();
        builder.loadAndTag(Type.LONG_TYPE, 10L);
        builder.loadAndTag(Type.INT_TYPE, 20);
        builder.loadAndTag(Type.INT_TYPE, 30);
        builder.getMethodNode().visitInsn(DUP2_X2);
        builder.recordTags(Type.INT_TYPE, Type.INT_TYPE, Type.LONG_TYPE, Type.INT_TYPE, Type.INT_TYPE);
        TagRecorder recorder = new TagRecorder();
        builder.build(GaletteTransformerTest::instrument).invoke(null, recorder, TagFrame.create(null));
        // Order is from top of stack down
        Assertions.assertEquals(Arrays.asList("30", "20", "10", "30", "20"), recorder.getFirstLabels());
    }

    @Test
    void dup2_x2WideBothCheckTags() throws ReflectiveOperationException {
        // ..., [value1, value2], [value3, value4] -> ..., [value3, value4], [value1, value2], [value3, value4]
        TagCheckingMethodBuilder builder = new TagCheckingMethodBuilder();
        builder.loadAndTag(Type.LONG_TYPE, 10L);
        builder.loadAndTag(Type.LONG_TYPE, 20L);
        builder.getMethodNode().visitInsn(DUP2_X2);
        builder.recordTags(Type.LONG_TYPE, Type.LONG_TYPE, Type.LONG_TYPE);
        TagRecorder recorder = new TagRecorder();
        builder.build(GaletteTransformerTest::instrument).invoke(null, recorder, TagFrame.create(null));
        // Order is from top of stack down
        Assertions.assertEquals(Arrays.asList("20", "10", "20"), recorder.getFirstLabels());
    }

    @Test
    void swapCheckTags() throws ReflectiveOperationException {
        // ..., value1, value2 -> ...,  value2, value1
        TagCheckingMethodBuilder builder = new TagCheckingMethodBuilder();
        builder.loadAndTag(Type.INT_TYPE, 10);
        builder.loadAndTag(Type.INT_TYPE, 20);
        builder.getMethodNode().visitInsn(SWAP);
        builder.recordTags(Type.INT_TYPE, Type.INT_TYPE);
        TagRecorder recorder = new TagRecorder();
        builder.build(GaletteTransformerTest::instrument).invoke(null, recorder, TagFrame.create(null));
        // Order is from top of stack down
        Assertions.assertEquals(Arrays.asList("10", "20"), recorder.getFirstLabels());
    }

    private static Stream<String> executionArguments() {
        return Arrays.stream(InstructionExamples.class.getDeclaredMethods())
                .filter(m -> Modifier.isStatic(m.getModifiers()))
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .map(Method::getName);
    }

    private static Stream<String> verificationArguments() {
        return Arrays.stream(NodeInstructionExamples.class.getDeclaredMethods())
                .filter(m -> Modifier.isStatic(m.getModifiers()))
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .map(Method::getName);
    }

    static byte[] instrument(byte[] buffer) {
        return GaletteTransformer.getInstanceAndTransform(buffer, false);
    }
}

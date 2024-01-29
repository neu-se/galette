package edu.neu.ccs.prl.galette.internal.transform;

import edu.neu.ccs.prl.galette.example.FieldExamples;
import edu.neu.ccs.prl.galette.example.InstructionExamples;
import edu.neu.ccs.prl.galette.example.MethodCallExamples;
import edu.neu.ccs.prl.galette.example.NodeInstructionExamples;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicVerifier;

class GaletteTransformerTest {
    @ParameterizedTest(name = "returnUnaffected: {0}")
    @MethodSource("executionArguments")
    void returnUnaffected(String name) throws ReflectiveOperationException {
        Class<?> original = InstructionExamples.class;
        Class<?> instrumented = AsmTestUtil.instrument(original, name, GaletteTransformerTest::instrument);
        Object expected = original.getDeclaredMethod(name).invoke(null);
        Object actual = instrumented.getDeclaredMethod(name, TagFrame.class).invoke(null, new TagFrame());
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
        Class<?> instrumented = AsmTestUtil.instrument(FieldExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, new TagFrame());
        Object result =
                instrumented.getDeclaredMethod("getStaticInt", TagFrame.class).invoke(instance, new TagFrame());
        Assertions.assertEquals(7, result);
    }

    @Test
    void getFieldInt() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(FieldExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, new TagFrame());
        Object result =
                instrumented.getDeclaredMethod("getFieldInt", TagFrame.class).invoke(instance, new TagFrame());
        Assertions.assertEquals(99, result);
    }

    @Test
    void putFieldInt() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(FieldExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, new TagFrame());
        instrumented.getDeclaredMethod("putFieldInt", int.class, TagFrame.class).invoke(instance, 22, new TagFrame());
    }

    @Test
    void putStaticInt() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(FieldExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, new TagFrame());
        instrumented
                .getDeclaredMethod("putStaticInt", int.class, TagFrame.class)
                .invoke(instance, 88, new TagFrame());
    }

    @Test
    void getStaticLong() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(FieldExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, new TagFrame());
        Object result =
                instrumented.getDeclaredMethod("getStaticLong", TagFrame.class).invoke(instance, new TagFrame());
        Assertions.assertEquals(7L, result);
    }

    @Test
    void getFieldLong() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(FieldExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, new TagFrame());
        Object result =
                instrumented.getDeclaredMethod("getFieldLong", TagFrame.class).invoke(instance, new TagFrame());
        Assertions.assertEquals(99L, result);
    }

    @Test
    void putFieldLong() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(FieldExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, new TagFrame());
        instrumented
                .getDeclaredMethod("putFieldLong", long.class, TagFrame.class)
                .invoke(instance, 22L, new TagFrame());
    }

    @Test
    void putStaticLong() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(FieldExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, new TagFrame());
        instrumented
                .getDeclaredMethod("putStaticLong", long.class, TagFrame.class)
                .invoke(instance, 88L, new TagFrame());
    }

    @Test
    void invokeVirtualIntReturn() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(MethodCallExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, new TagFrame());
        Object result = instrumented
                .getDeclaredMethod("invokeVirtualIntReturn", int.class, TagFrame.class)
                .invoke(instance, 7, new TagFrame());
        Assertions.assertEquals(7, result);
    }

    @Test
    void invokeVirtualLongReturn() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(MethodCallExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, new TagFrame());
        Object result = instrumented
                .getDeclaredMethod("invokeVirtualLongReturn", long.class, TagFrame.class)
                .invoke(instance, 9L, new TagFrame());
        Assertions.assertEquals(9L, result);
    }

    @Test
    void invokeVirtualMixedParameterTypes() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(MethodCallExamples.class, GaletteTransformerTest::instrument);
        Object instance =
                instrumented.getDeclaredMethod("getInstance", TagFrame.class).invoke(null, new TagFrame());
        Object result = instrumented
                .getDeclaredMethod("invokeVirtualMixedParameterTypes", int.class, long.class, int.class, TagFrame.class)
                .invoke(instance, -1, 9L, 2, new TagFrame());
        Assertions.assertEquals(-1 + 9L + 2, result);
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

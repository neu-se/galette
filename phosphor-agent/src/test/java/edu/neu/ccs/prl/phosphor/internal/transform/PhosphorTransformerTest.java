package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.example.FieldExamples;
import edu.neu.ccs.prl.phosphor.example.InstructionExamples;
import edu.neu.ccs.prl.phosphor.example.MethodCallExamples;
import edu.neu.ccs.prl.phosphor.example.NodeInstructionExamples;
import edu.neu.ccs.prl.phosphor.internal.runtime.PhosphorFrame;
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

class PhosphorTransformerTest {
    @ParameterizedTest(name = "returnUnaffected: {0}")
    @MethodSource("executionArguments")
    void returnUnaffected(String name) throws ReflectiveOperationException {
        Class<?> original = InstructionExamples.class;
        Class<?> instrumented = AsmTestUtil.instrument(original, name, PhosphorTransformerTest::instrument);
        Object expected = original.getDeclaredMethod(name).invoke(null);
        Object actual =
                instrumented.getDeclaredMethod(name, PhosphorFrame.class).invoke(null, new PhosphorFrame());
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
        ClassNode instrumented = AsmTestUtil.instrument(original, PhosphorTransformerTest::instrument);
        for (MethodNode mn : instrumented.methods) {
            new Analyzer<>(new BasicVerifier()).analyze(original.name, mn);
        }
    }

    @Test
    void getStaticInt() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(FieldExamples.class, PhosphorTransformerTest::instrument);
        Object instance = instrumented
                .getDeclaredMethod("getInstance", PhosphorFrame.class)
                .invoke(null, new PhosphorFrame());
        Object result = instrumented
                .getDeclaredMethod("getStaticInt", PhosphorFrame.class)
                .invoke(instance, new PhosphorFrame());
        Assertions.assertEquals(7, result);
    }

    @Test
    void getFieldInt() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(FieldExamples.class, PhosphorTransformerTest::instrument);
        Object instance = instrumented
                .getDeclaredMethod("getInstance", PhosphorFrame.class)
                .invoke(null, new PhosphorFrame());
        Object result = instrumented
                .getDeclaredMethod("getFieldInt", PhosphorFrame.class)
                .invoke(instance, new PhosphorFrame());
        Assertions.assertEquals(99, result);
    }

    @Test
    void putFieldInt() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(FieldExamples.class, PhosphorTransformerTest::instrument);
        Object instance = instrumented
                .getDeclaredMethod("getInstance", PhosphorFrame.class)
                .invoke(null, new PhosphorFrame());
        instrumented
                .getDeclaredMethod("putFieldInt", int.class, PhosphorFrame.class)
                .invoke(instance, 22, new PhosphorFrame());
    }

    @Test
    void putStaticInt() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(FieldExamples.class, PhosphorTransformerTest::instrument);
        Object instance = instrumented
                .getDeclaredMethod("getInstance", PhosphorFrame.class)
                .invoke(null, new PhosphorFrame());
        instrumented
                .getDeclaredMethod("putStaticInt", int.class, PhosphorFrame.class)
                .invoke(instance, 88, new PhosphorFrame());
    }

    @Test
    void getStaticLong() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(FieldExamples.class, PhosphorTransformerTest::instrument);
        Object instance = instrumented
                .getDeclaredMethod("getInstance", PhosphorFrame.class)
                .invoke(null, new PhosphorFrame());
        Object result = instrumented
                .getDeclaredMethod("getStaticLong", PhosphorFrame.class)
                .invoke(instance, new PhosphorFrame());
        Assertions.assertEquals(7L, result);
    }

    @Test
    void getFieldLong() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(FieldExamples.class, PhosphorTransformerTest::instrument);
        Object instance = instrumented
                .getDeclaredMethod("getInstance", PhosphorFrame.class)
                .invoke(null, new PhosphorFrame());
        Object result = instrumented
                .getDeclaredMethod("getFieldLong", PhosphorFrame.class)
                .invoke(instance, new PhosphorFrame());
        Assertions.assertEquals(99L, result);
    }

    @Test
    void putFieldLong() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(FieldExamples.class, PhosphorTransformerTest::instrument);
        Object instance = instrumented
                .getDeclaredMethod("getInstance", PhosphorFrame.class)
                .invoke(null, new PhosphorFrame());
        instrumented
                .getDeclaredMethod("putFieldLong", long.class, PhosphorFrame.class)
                .invoke(instance, 22L, new PhosphorFrame());
    }

    @Test
    void putStaticLong() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(FieldExamples.class, PhosphorTransformerTest::instrument);
        Object instance = instrumented
                .getDeclaredMethod("getInstance", PhosphorFrame.class)
                .invoke(null, new PhosphorFrame());
        instrumented
                .getDeclaredMethod("putStaticLong", long.class, PhosphorFrame.class)
                .invoke(instance, 88L, new PhosphorFrame());
    }

    @Test
    void invokeVirtualIntReturn() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(MethodCallExamples.class, PhosphorTransformerTest::instrument);
        Object instance = instrumented
                .getDeclaredMethod("getInstance", PhosphorFrame.class)
                .invoke(null, new PhosphorFrame());
        Object result = instrumented
                .getDeclaredMethod("invokeVirtualIntReturn", int.class, PhosphorFrame.class)
                .invoke(instance, 7, new PhosphorFrame());
        Assertions.assertEquals(7, result);
    }

    @Test
    void invokeVirtualLongReturn() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(MethodCallExamples.class, PhosphorTransformerTest::instrument);
        Object instance = instrumented
                .getDeclaredMethod("getInstance", PhosphorFrame.class)
                .invoke(null, new PhosphorFrame());
        Object result = instrumented
                .getDeclaredMethod("invokeVirtualLongReturn", long.class, PhosphorFrame.class)
                .invoke(instance, 9L, new PhosphorFrame());
        Assertions.assertEquals(9L, result);
    }

    @Test
    void invokeVirtualMixedParameterTypes() throws ReflectiveOperationException {
        Class<?> instrumented = AsmTestUtil.instrument(MethodCallExamples.class, PhosphorTransformerTest::instrument);
        Object instance = instrumented
                .getDeclaredMethod("getInstance", PhosphorFrame.class)
                .invoke(null, new PhosphorFrame());
        Object result = instrumented
                .getDeclaredMethod(
                        "invokeVirtualMixedParameterTypes", int.class, long.class, int.class, PhosphorFrame.class)
                .invoke(instance, -1, 9L, 2, new PhosphorFrame());
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
        return PhosphorTransformer.getInstanceAndTransform(buffer, false);
    }
}

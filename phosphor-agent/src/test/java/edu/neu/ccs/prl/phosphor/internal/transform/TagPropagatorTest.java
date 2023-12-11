package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.example.InstructionExamples;
import edu.neu.ccs.prl.phosphor.example.NodeInstructionExamples;
import edu.neu.ccs.prl.phosphor.internal.runtime.PhosphorFrame;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicVerifier;

class TagPropagatorTest {
    @ParameterizedTest(name = "returnUnaffected: {0}")
    @MethodSource("executionArguments")
    void returnUnaffected(String name) throws ReflectiveOperationException {
        Class<?> original = InstructionExamples.class;
        Class<?> instrumented =
                AsmTestUtil.instrument(original, name, b -> PhosphorTransformer.getInstanceAndTransform(b, false));
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
        ClassNode instrumented =
                AsmTestUtil.instrument(original, b -> PhosphorTransformer.getInstanceAndTransform(b, false));
        for (MethodNode mn : instrumented.methods) {
            new Analyzer<>(new BasicVerifier()).analyze(original.name, mn);
        }
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
}

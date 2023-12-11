package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.example.Example;
import edu.neu.ccs.prl.phosphor.internal.runtime.PhosphorFrame;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class TagPropagatorTest {
    @ParameterizedTest(name = "returnUnaffected: {0}")
    @MethodSource("arguments")
    void returnUnaffected(String name) throws ReflectiveOperationException {
        Class<?> original = Example.class;
        Class<?> instrumented =
                AsmTestUtil.instrument(original, name, b -> PhosphorTransformer.getInstanceAndTransform(b, false));
        Object expected = original.getDeclaredMethod(name).invoke(null);
        Object actual =
                instrumented.getDeclaredMethod(name, PhosphorFrame.class).invoke(null, new PhosphorFrame());
        Assertions.assertEquals(expected, actual);
    }

    private static Stream<String> arguments() {
        return Arrays.stream(Example.class.getDeclaredMethods())
                .filter(m -> Modifier.isStatic(m.getModifiers()))
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .map(Method::getName);
    }
}

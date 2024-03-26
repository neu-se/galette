package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.lang.reflect.Constructor;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@FlowBench
public class ConstructorReflectionITCase {
    @SuppressWarnings("unused")
    TagManager manager;

    @SuppressWarnings("unused")
    FlowChecker checker;

    @ParameterizedTest(name = "newInstance(baseType={0}, taintValue={1}, category={2})")
    @MethodSource("arguments")
    void newInstance(Class<?> baseType, boolean taintValue, HolderValueCategory category)
            throws ReflectiveOperationException {
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        Object expected = category.getValue(baseType, source);
        Constructor<Holder> c = category.getConstructor(baseType);
        Holder instance;
        if (c.getParameters().length == 2) {
            Object unused = HolderValueCategory.BASIC.getValue(baseType, source);
            instance = c.newInstance(unused, expected);
        } else {
            instance = c.newInstance(expected);
        }
        Object actual = category.getValue(baseType, instance);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(category.getLabels(baseType), category.getElementLabels(baseType, instance, manager));
        } else {
            checker.checkEmpty(category.getElementLabels(baseType, instance, manager));
        }
    }

    static boolean isValid(Arguments arguments) {
        Object[] values = arguments.get();
        return !(values[0] == Object.class && values[2] == HolderValueCategory.BOXED)
                && values[2] != HolderValueCategory.BASIC_STATIC;
    }

    static Stream<Arguments> arguments() {
        Class<?>[] types = new Class[] {
            Integer.TYPE,
            Boolean.TYPE,
            Byte.TYPE,
            Character.TYPE,
            Short.TYPE,
            Double.TYPE,
            Float.TYPE,
            Long.TYPE,
            Object.class
        };
        return BenchUtil.cartesianProduct(types, new Boolean[] {true, false}, HolderValueCategory.values())
                .filter(ConstructorReflectionITCase::isValid);
    }
}

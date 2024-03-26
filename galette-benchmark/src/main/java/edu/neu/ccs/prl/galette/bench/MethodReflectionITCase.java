package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.lang.reflect.Method;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@FlowBench
public class MethodReflectionITCase {
    @SuppressWarnings("unused")
    TagManager manager;

    @SuppressWarnings("unused")
    FlowChecker checker;

    @Test
    void objectHashCode() throws ReflectiveOperationException {
        int a = manager.setLabels(7, new Object[] {"labels1"});
        Object o = new Object() {
            private final int j = a;

            @Override
            public int hashCode() {
                return j;
            }
        };
        Method m = Object.class.getDeclaredMethod("hashCode");
        int actual = (int) m.invoke(o);
        Assertions.assertEquals(7, actual);
        checker.check(new Object[] {"labels1"}, manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getValue(baseType={0}, taintValue={1}, category={2})")
    @MethodSource("arguments")
    void getValue(Class<?> baseType, boolean taintValue, HolderValueCategory category)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue, 120, false, "hello");
        Object expected = category.getValue(baseType, holder);
        Object actual = category.getter(baseType).invoke(category == HolderValueCategory.BASIC_STATIC ? null : holder);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(category.getLabels(baseType), category.getElementLabels(baseType, holder, manager));
        } else {
            checker.checkEmpty(category.getElementLabels(baseType, holder, manager));
        }
    }

    @ParameterizedTest(name = "setValue(baseType={0}, taintValue={1}, category={2})")
    @MethodSource("arguments")
    void setValue(Class<?> baseType, boolean taintValue, HolderValueCategory category)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        Object expected = category.getValue(baseType, source);
        category.setter(baseType).invoke(category == HolderValueCategory.BASIC_STATIC ? null : holder, expected);
        Object actual = category.getValue(baseType, holder);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(category.getLabels(baseType), category.getElementLabels(baseType, holder, manager));
        } else {
            checker.checkEmpty(category.getElementLabels(baseType, holder, manager));
        }
    }

    static boolean isValid(Arguments arguments) {
        Object[] values = arguments.get();
        return !(values[0] == Object.class && values[2] == HolderValueCategory.BOXED);
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
                .filter(MethodReflectionITCase::isValid);
    }
}

package edu.neu.ccs.prl.galette.bench;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.genericMethodType;
import static java.lang.invoke.MethodType.methodType;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@FlowBench
@Disabled("Unimplemented")
public class MethodHandleITCase {
    private final Lookup lookup = lookup();

    @Test
    void lookupFindStatic(TagManager manager, FlowChecker checker) throws Throwable {
        MethodType mt = methodType(int.class, int.class, int.class);
        MethodHandle mh = lookup.findStatic(Parent.class, "max", mt);
        int a = manager.setLabels(5, new Object[] {"a"});
        int b = manager.setLabels(90, new Object[] {"b"});
        int actual = (int) mh.invokeExact(a, b);
        Assertions.assertEquals(90, actual);
        checker.check(new Object[] {"b"}, manager.getLabels(actual));
    }

    @Test
    void lookupFindVirtual(TagManager manager, FlowChecker checker) throws Throwable {
        Parent p = new Parent(manager.setLabels(9, new Object[] {"x"}));
        MethodType mt = methodType(int.class);
        MethodHandle mh = lookup.findVirtual(Parent.class, "getX", mt);
        int actual = (int) mh.invokeExact(p);
        Assertions.assertEquals(9, actual);
        checker.check(new Object[] {"x"}, manager.getLabels(actual));
    }

    @Test
    void lookupFindConstructor(TagManager manager, FlowChecker checker) throws Throwable {
        int x = manager.setLabels(22, new Object[] {"x"});
        MethodType mt = methodType(void.class, int.class);
        MethodHandle mh = lookup.findConstructor(Parent.class, mt);
        Parent p = (Parent) mh.invokeExact(x);
        int actual = p.getX();
        Assertions.assertEquals(22, actual);
        checker.check(new Object[] {"x"}, manager.getLabels(actual));
    }

    @Test
    void lookupFindSpecial(TagManager manager, FlowChecker checker) throws Throwable {
        int x = manager.setLabels(75, new Object[] {"x"});
        int y = manager.setLabels(25, new Object[] {"y"});
        MethodType mt = methodType(int.class, int.class);
        MethodHandle mh = Child.lookup().findSpecial(Parent.class, "getXPlus", mt, Child.class);
        Child c = new Child();
        c.setX(x);
        int actual = (int) mh.invokeExact(c, y);
        Assertions.assertEquals(100, actual);
        checker.check(new Object[] {"x", "y"}, manager.getLabels(actual));
    }

    @Test
    void lookupFindGetter(TagManager manager, FlowChecker checker) throws Throwable {
        int x = manager.setLabels(75, new Object[] {"x"});
        MethodHandle mh = lookup.findGetter(Parent.class, "x", int.class);
        Parent p = new Parent(x);
        int actual = (int) mh.invokeExact(p);
        Assertions.assertEquals(75, actual);
        checker.check(new Object[] {"x"}, manager.getLabels(actual));
    }

    @Test
    void lookupFindSetter(TagManager manager, FlowChecker checker) throws Throwable {
        int x = manager.setLabels(75, new Object[] {"x"});
        MethodHandle mh = lookup.findSetter(Parent.class, "x", int.class);
        Parent p = new Parent();
        mh.invokeExact(p, x);
        int actual = p.x;
        Assertions.assertEquals(75, actual);
        checker.check(new Object[] {"x"}, manager.getLabels(actual));
    }

    @Test
    void lookupFindStaticGetter(TagManager manager, FlowChecker checker) throws Throwable {
        long j = manager.setLabels(8L, new Object[] {"j"});
        MethodHandle mh = lookup.findStaticGetter(Parent.class, "j", long.class);
        Parent.j = j;
        long actual = (long) mh.invokeExact();
        Assertions.assertEquals(8, actual);
        checker.check(new Object[] {"j"}, manager.getLabels(actual));
    }

    @Test
    void lookupFindStaticSetter(TagManager manager, FlowChecker checker) throws Throwable {
        long j = manager.setLabels(8L, new Object[] {"j"});
        MethodHandle mh = lookup.findStaticSetter(Parent.class, "j", long.class);
        mh.invokeExact(j);
        long actual = Parent.j;
        Assertions.assertEquals(8, actual);
        checker.check(new Object[] {"j"}, manager.getLabels(actual));
    }

    @Test
    void lookupBind(TagManager manager, FlowChecker checker) throws Throwable {
        Parent p = new Parent(manager.setLabels(9, new Object[] {"x"}));
        MethodType mt = methodType(int.class);
        MethodHandle mh = lookup.bind(p, "getX", mt);
        int actual = (int) mh.invokeExact();
        Assertions.assertEquals(9, actual);
        checker.check(new Object[] {"x"}, manager.getLabels(actual));
    }

    @Test
    void lookupUnreflect(TagManager manager, FlowChecker checker) throws Throwable {
        int x = manager.setLabels(75, new Object[] {"x"});
        int y = manager.setLabels(25, new Object[] {"y"});
        Method m = Parent.class.getDeclaredMethod("getXPlus", int.class);
        MethodHandle mh = lookup.unreflect(m);
        Parent parent = new Parent(x);
        int actual = (int) mh.invokeExact(parent, y);
        Assertions.assertEquals(100, actual);
        checker.check(new Object[] {"x", "y"}, manager.getLabels(actual));
    }

    @Test
    void lookupUnreflectSpecial(TagManager manager, FlowChecker checker) throws Throwable {
        int x = manager.setLabels(75, new Object[] {"x"});
        int y = manager.setLabels(25, new Object[] {"y"});
        Method m = Parent.class.getDeclaredMethod("getXPlus", int.class);
        MethodHandle mh = Child.lookup().unreflectSpecial(m, Child.class);
        Child c = new Child();
        c.setX(x);
        int actual = (int) mh.invokeExact(c, y);
        Assertions.assertEquals(100, actual);
        checker.check(new Object[] {"x", "y"}, manager.getLabels(actual));
    }

    @Test
    void lookupUnreflectConstructor(TagManager manager, FlowChecker checker) throws Throwable {
        MethodHandle mh = lookup.unreflectConstructor(Parent.class.getDeclaredConstructor(int.class));
        int x = manager.setLabels(22, new Object[] {"x"});
        Parent p = (Parent) mh.invokeExact(x);
        int actual = p.getX();
        Assertions.assertEquals(22, actual);
        checker.check(new Object[] {"x"}, manager.getLabels(actual));
    }

    @Test
    void lookupUnreflectGetter(TagManager manager, FlowChecker checker) throws Throwable {
        long j = manager.setLabels(8L, new Object[] {"j"});
        MethodHandle mh = lookup.unreflectGetter(Parent.class.getDeclaredField("j"));
        Parent.j = j;
        long actual = (long) mh.invokeExact();
        Assertions.assertEquals(8, actual);
        checker.check(new Object[] {"j"}, manager.getLabels(actual));
    }

    @Test
    void lookupUnreflectSetter(TagManager manager, FlowChecker checker) throws Throwable {
        long j = manager.setLabels(8L, new Object[] {"j"});
        MethodHandle mh = lookup.unreflectSetter(Parent.class.getDeclaredField("j"));
        mh.invokeExact(j);
        long actual = Parent.j;
        Assertions.assertEquals(8, actual);
        checker.check(new Object[] {"j"}, manager.getLabels(actual));
    }

    @Test
    void methodHandleInvokeExact(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandleInvoke(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandleInvokeWithArguments(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandleAsType(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandleAsSpreader(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandleWithVarArgs(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandleAsCollector(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandleAsVarargsCollector(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandleAsFixedArity(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandleBindTo(TagManager manager, FlowChecker checker) throws Throwable {
        MethodHandle mh = lookup.findVirtual(Example.class, "apply", genericMethodType(2));
        Object a1 = "hello";
        Object a2 = "world";
        Example val = (x1, x2) -> "present";
        MethodHandle bmh = mh.bindTo(val);
        Object result1 = bmh.invokeExact(a1, a2);
        Object result2 = mh.invokeExact(val, a1, a2);
        Assertions.assertEquals(result1, result2);
    }

    @Test
    void methodHandlesArrayConstructor(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesArrayLength(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesArrayElementGetter(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesArrayElementSetter(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesSpreadInvoker(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesExactInvoker(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesInvoker(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesVarHandleExactInvoker(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesVarHandleInvoker(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesExplicitCastArguments(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesPermuteArguments(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesConstant(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesIdentity(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesZero(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesEmpty(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesInsertArguments(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesDropArguments(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesDropArgumentsToMatch(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesFilterArguments(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesCollectArguments(TagManager manager, FlowChecker checker) throws Throwable {
        MethodHandle toString = lookup.findVirtual(String.class, "toString", methodType(String.class));
        Parent p = new Parent();
        MethodHandle setX = lookup.findVirtual(Parent.class, "setX", methodType(void.class, int.class))
                .bindTo(p);
        MethodHandle collect = MethodHandles.collectArguments(toString, 1, setX);
        Assertions.assertEquals("hello", (String) collect.invokeExact("hello", 7));
        Assertions.assertEquals(7, p.getX());
    }

    @Test
    void methodHandlesFilterReturnValue(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesFoldArguments(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesGuardWithTest(TagManager manager, FlowChecker checker) throws Throwable {
        int a = manager.setLabels(10, new Object[] {"a"});
        int b = manager.setLabels(3, new Object[] {"b"});
        MethodType mt = methodType(int.class, int.class, int.class);
        MethodHandle target = lookup.findVirtual(GuardWithHelper.class, "sum", mt);
        MethodHandle fallback = lookup.findVirtual(GuardWithHelper.class, "max", mt);
        MethodHandle test = lookup.findVirtual(GuardWithHelper.class, "getFlag", methodType(boolean.class));
        MethodHandle guarded = MethodHandles.guardWithTest(test, target, fallback);
        GuardWithHelper helper = new GuardWithHelper(true);
        int actual = (int) guarded.invoke(helper, a, b);
        Assertions.assertEquals(13, actual);
        checker.check(new Object[] {"a", "b"}, manager.getLabels(actual));
    }

    @Test
    void methodHandlesCatchException(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesThrowException(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesLoop(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesWhileLoop(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesDoWhileLoop(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesCountedLoop(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesIteratedLoop(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void methodHandlesTryFinally(TagManager manager, FlowChecker checker) throws Throwable {}

    @Test
    void varHandleToMethodHandle(TagManager manager, FlowChecker checker) throws Throwable {}

    private static class GuardWithHelper {
        private final boolean flag;

        private GuardWithHelper(boolean flag) {
            this.flag = flag;
        }

        public int max(int a, int b) {
            return Math.max(a, b);
        }

        public int sum(int a, int b) {
            return a + b;
        }

        public boolean getFlag() {
            return flag;
        }
    }

    interface Example {
        Object apply(Object a1, Object a2);
    }

    private static class Parent {
        public int x;
        public static long j;

        public Parent() {
            this(0);
        }

        public Parent(int x) {
            this.x = x;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public static int max(int a, int b) {
            return Math.max(a, b);
        }

        public int getXPlus(int y) {
            return x + y;
        }
    }

    private static class Child extends Parent {
        @Override
        public int getXPlus(int y) {
            return 7;
        }

        public static Lookup lookup() {
            return MethodHandles.lookup();
        }
    }
}

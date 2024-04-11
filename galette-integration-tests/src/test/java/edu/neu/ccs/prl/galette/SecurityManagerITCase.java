package edu.neu.ccs.prl.galette;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SecurityManagerITCase {
    @Test
    void getClassContextSuper() {
        Class<?>[] context = C.getClassContextSuper();
        List<Class<?>> expected =
                Arrays.asList(TestSecurityManager.class, A.class, B.class, C.class, SecurityManagerITCase.class);
        Assertions.assertEquals(expected, Arrays.asList(context).subList(0, expected.size()));
    }

    @Test
    void getClassContextThis() {
        Class<?>[] context = C.getClassContextThis();
        List<Class<?>> expected =
                Arrays.asList(TestSecurityManager.class, A.class, B.class, C.class, SecurityManagerITCase.class);
        Assertions.assertEquals(expected, Arrays.asList(context).subList(0, expected.size()));
    }

    static class TestSecurityManager extends SecurityManager {
        public Class<?>[] getClassContentSuper() {
            return super.getClassContext();
        }

        public Class<?>[] getClassContentThis() {
            return this.getClassContext();
        }
    }

    static class A {
        public static Class<?>[] getClassContextSuper() {
            return new TestSecurityManager().getClassContentSuper();
        }

        public static Class<?>[] getClassContextThis() {
            return new TestSecurityManager().getClassContentThis();
        }
    }

    static class B {
        public static Class<?>[] getClassContextSuper() {
            return A.getClassContextSuper();
        }

        public static Class<?>[] getClassContextThis() {
            return A.getClassContextThis();
        }
    }

    static class C {
        public static Class<?>[] getClassContextSuper() {
            return B.getClassContextSuper();
        }

        public static Class<?>[] getClassContextThis() {
            return B.getClassContextThis();
        }
    }
}

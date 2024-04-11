package edu.neu.ccs.prl.galette;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SecurityManagerITCase {
    @Test
    void getClassContext() {
        Class<?>[] context = C.getClassContext();
        List<Class<?>> expected =
                Arrays.asList(TestSecurityManager.class, A.class, B.class, C.class, SecurityManagerITCase.class);
        Assertions.assertEquals(expected, Arrays.asList(context).subList(0, expected.size()));
    }

    static class TestSecurityManager extends SecurityManager {
        public Class<?>[] getClassContext() {
            return super.getClassContext();
        }
    }

    static class A {
        public static Class<?>[] getClassContext() {
            return new TestSecurityManager().getClassContext();
        }
    }

    static class B {
        public static Class<?>[] getClassContext() {
            return A.getClassContext();
        }
    }

    static class C {
        public static Class<?>[] getClassContext() {
            return B.getClassContext();
        }
    }
}

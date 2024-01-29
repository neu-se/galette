package edu.neu.ccs.prl.galette.all;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnnotationITCase {
    @Test
    public void testEnum() throws ReflectiveOperationException {
        Method method = Example.class.getDeclaredMethod("x");
        ExampleAnnotation annotation = method.getAnnotation(ExampleAnnotation.class);
        Assertions.assertNotNull(annotation);
        Assertions.assertEquals(ExampleAnnotation.Color.BLUE, annotation.color());
    }

    public static class Example {
        @ExampleAnnotation(color = ExampleAnnotation.Color.BLUE)
        void x() {}
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ExampleAnnotation {
        Color color();

        enum Color {
            RED,
            BLUE,
            GREEN;
        }
    }
}

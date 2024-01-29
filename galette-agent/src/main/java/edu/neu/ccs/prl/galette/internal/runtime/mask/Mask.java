package edu.neu.ccs.prl.galette.internal.runtime.mask;

import java.lang.annotation.*;

/**
 * Indicates that calls to the annotated method should be added during instrumentation to
 * replace calls to another "masked" method.
 * Methods with this annotation must be public and static.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Masks.class)
public @interface Mask {
    /**
     * The internal name of the class that owns the masked method.
     *
     * @return the internal name of the class that owns the masked method
     */
    String owner();

    /**
     * The name of the masked method.
     *
     * @return the name of the masked method
     */
    String name();

    /**
     * Whether the masked method is static.
     *
     * @return {@code true} if the masked method is static
     */
    boolean isStatic() default false;

    /**
     * The descriptor of the masked method or the empty string if the descriptor should be computed from the descriptor
     * of the annotated method as follows:
     * If {@link Mask#isStatic()} is {@code true}, then the descriptor of the masked method is the same as the
     * annotated method.
     * Otherwise, the descriptor of the masked method is the same as the annotated method minus the first parameter.
     */
    String descriptor() default "";

    MaskType type() default MaskType.REPLACE;
}

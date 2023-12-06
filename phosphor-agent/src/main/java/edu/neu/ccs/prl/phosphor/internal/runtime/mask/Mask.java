package edu.neu.ccs.prl.phosphor.internal.runtime.mask;

import java.lang.annotation.*;

/**
 * Indicates that calls to the annotated method should be added during instrumentation to
 * replace calls to another "masked" method.
 * Methods with this annotation must be public and static.
 * If {@link Mask#isStatic()} is {@code true}, then the descriptor of the masked method is the same as the annotated
 * method.
 * Otherwise, the descriptor of the masked method is the same as the annotated method minus the first parameter.
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
    boolean isStatic();
}

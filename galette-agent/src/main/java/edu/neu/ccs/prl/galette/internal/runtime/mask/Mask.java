package edu.neu.ccs.prl.galette.internal.runtime.mask;

import java.lang.annotation.*;

/**
 * Indicates that calls to the annotated method should be added during instrumentation to
 * "mask" calls to a method.
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
     * The descriptor of the parameters of the masked method or the empty string if the parameter's descriptor should
     * be computed from the parameters of the annotated method as follows:
     * If {@link Mask#type()} is {@link MaskType#POST_PROCESS} and the annotated method is non-void, then remove the
     * first parameter (which will be used to store the original return value).
     * If {@link Mask#isStatic()} is {@code false}, then removed the next parameter
     * (which will be used to store the receiver).
     */
    String parametersDescriptor() default "";

    /**
     * Descriptor of the return value of the masked method or the empty string if return value of the masked method
     * matches the annotated method.
     */
    String returnDescriptor() default "";

    MaskType type() default MaskType.REPLACE;
}

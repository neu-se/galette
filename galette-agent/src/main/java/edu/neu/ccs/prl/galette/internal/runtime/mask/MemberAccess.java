package edu.neu.ccs.prl.galette.internal.runtime.mask;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the body of the annotated method should be generated when the Galette agent is packaged.
 * The generated method body will contain a single method call determined using the values specified in the annotation.
 * This method call will be passed the arguments of the annotated method except for the receiver
 * (if the annotated method is an instance method).
 * If the called method is non-void, then the generated method body will return the called method's return value.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MemberAccess {
    /**
     * The internal name of the class that owns the member to be accessed.
     *
     * @return the internal name of the class that owns the member to be accessed.
     */
    String owner();

    /**
     * The name of the member to be accessed.
     *
     * @return the name of the member to be accessed.
     */
    String name();

    /**
     * The opcode of the instruction used to access the member.
     *
     * @return opcode of the instruction used to access the member.
     */
    int opcode();

    /**
     * {@code true} if the class that owns the member to be accessed is an interface.
     *
     * @return {@code true} if the class that owns the member to be accessed is an interface.
     */
    boolean isInterface() default false;
}

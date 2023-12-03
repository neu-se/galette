package edu.neu.ccs.prl.phosphor.internal.runtime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the Phosphor agent is responsible for generating the body of public methods in a class excluding the
 * instance initialization method.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Patched {}

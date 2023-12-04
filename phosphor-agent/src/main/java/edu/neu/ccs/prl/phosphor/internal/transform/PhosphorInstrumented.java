package edu.neu.ccs.prl.phosphor.internal.transform;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that Phosphor instrumentation was applied to a class.
 */
@Retention(RetentionPolicy.CLASS)
public @interface PhosphorInstrumented {}

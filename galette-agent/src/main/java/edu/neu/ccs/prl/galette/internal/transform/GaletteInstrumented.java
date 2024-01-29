package edu.neu.ccs.prl.galette.internal.transform;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that Galette instrumentation was applied to a class.
 */
@Retention(RetentionPolicy.CLASS)
public @interface GaletteInstrumented {}

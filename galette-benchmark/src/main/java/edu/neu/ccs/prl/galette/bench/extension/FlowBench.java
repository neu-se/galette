package edu.neu.ccs.prl.galette.bench.extension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Indicates that a test method can be run as a taint tracking benchmark.
 * Provides a parameter of type {@link TagManager} that can be used to apply and access taint
 * tags.
 * Provides a parameter of type {@link FlowChecker} that can be used to compare a set of propagated
 * taint tags against a set of expected taint tags.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({TagManagerResolver.class, FlowCheckerResolver.class})
public @interface FlowBench {}

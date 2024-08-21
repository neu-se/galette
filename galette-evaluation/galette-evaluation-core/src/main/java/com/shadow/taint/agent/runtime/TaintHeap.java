package com.shadow.taint.agent.runtime;

import com.shadow.taint.agent.model.TypedTaint;

/**
 * Placeholder class for MirrorTaint
 */
public class TaintHeap {
    public static void startup() {
        throw new AssertionError();
    }

    public static void shutdown() {
        throw new AssertionError();
    }

    public static TypedTaint queryTaint(Object x) {
        throw new AssertionError();
    }

    public static void recordTaint(Object x, TypedTaint y) {
        throw new AssertionError();
    }
}

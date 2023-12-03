package edu.neu.ccs.prl.phosphor.internal.agent;

public final class HardCoded {
    private HardCoded() {
        throw new AssertionError();
    }

    private static final ExclusionList hardCodedOffsets =
            new ExclusionList("java/lang/Number", "java/lang/invoke/LambdaForm$", "jdk/internal/misc/UnsafeConstants");
    /**
     * Returns {@code true} if the JVM uses hard-coded field offset for the class with the specified internal name.
     * Based on the source code for src/hotspot/share/classfile/classFileParser.cpp from Eclipse Temurin JDK
     * (version 11.0.21+9).
     */
    public static boolean hasHardCodedOffsets(String className) {
        switch (className) {
            case "java/lang/AssertionStatusDirectives":
            case "java/lang/Class":
            case "java/lang/ClassLoader":
            case "java/lang/ref/Reference":
            case "java/lang/ref/SoftReference":
            case "java/lang/StackTraceElement":
            case "java/lang/String":
            case "java/lang/Throwable":
            case "java/lang/Boolean":
            case "java/lang/Character":
            case "java/lang/Float":
            case "java/lang/Double":
            case "java/lang/Byte":
            case "java/lang/Short":
            case "java/lang/Integer":
            case "java/lang/Long":
                return true;
            default:
                return false;
        }
    }
}

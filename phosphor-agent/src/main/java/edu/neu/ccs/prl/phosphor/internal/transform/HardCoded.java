package edu.neu.ccs.prl.phosphor.internal.transform;

/**
 * Static utility class which contains information about classes with fields that may be accessed by the JVM using a
 * hard-coded offset.
 * These classes were identified based on the classes indicated as having hard-coded offsets in the source code for
 * src/hotspot/share/classfile/classFileParser.cpp from Eclipse Temurin JDK (version 11.0.21+9) and manual
 * experimentation.
 */
public final class HardCoded {
    private HardCoded() {
        throw new AssertionError();
    }

    /**
     * Returns {@code true} if a JVM might use a hard-coded offset to access a static field of the class with the
     * specified internal name.
     */
    public static boolean hasHardCodedStaticOffset(String className) {
        switch (className) {
            case "java/lang/ref/SoftReference":
            case "java/lang/Integer":
            case "java/lang/Long":
                return true;
            default:
                return false;
        }
    }
}

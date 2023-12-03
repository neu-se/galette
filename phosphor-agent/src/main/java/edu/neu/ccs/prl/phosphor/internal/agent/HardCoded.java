package edu.neu.ccs.prl.phosphor.internal.agent;

public final class HardCoded {
    private HardCoded() {
        throw new AssertionError();
    }

    /**
     * Returns {@code true} if the JVM uses hard-coded field offset for the class with the specified internal name.
     * This list was filtered down from those indicated as having hard-coded offsets in the source code for
     * src/hotspot/share/classfile/classFileParser.cpp from Eclipse Temurin JDK (version 11.0.21+9).
     */
    public static boolean hasHardCodedOffsets(String className) {
        switch (className) {
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

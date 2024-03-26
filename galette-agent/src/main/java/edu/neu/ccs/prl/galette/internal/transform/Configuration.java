package edu.neu.ccs.prl.galette.internal.transform;

import edu.neu.ccs.prl.galette.internal.patch.ConfigurationEmbedder;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public final class Configuration {
    /**
     * The value of this field is changed to false by the {@link ConfigurationEmbedder} for Java 9+.
     * This field cannot be marked as final to prevent its value from being inlined.
     */
    private static boolean IS_JAVA_8 = true;
    /**
     * {@code true} if taint tags should be written and read when serializing objects using
     * {@link java.io.ObjectInputStream} and {@link java.io.ObjectOutputStream}.
     */
    private static boolean PROPAGATE_THROUGH_SERIALIZATION = true;

    private Configuration() {
        throw new AssertionError();
    }

    public static boolean isJava8() {
        return IS_JAVA_8;
    }

    public static boolean isPropagateThroughSerialization() {
        return PROPAGATE_THROUGH_SERIALIZATION;
    }

    public static boolean isInternalTaintingClass(String className) {
        return GaletteNames.TAINTER_INTERNAL_NAME.equals(className)
                || GaletteNames.TAGGED_OBJECT_INTERNAL_NAME.equals(className);
    }
}

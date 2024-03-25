package edu.neu.ccs.prl.galette.internal.transform;

import edu.neu.ccs.prl.galette.internal.patch.ConfigurationEmbedder;

public final class Configuration {
    /**
     * The value of this field is changed to false by the {@link ConfigurationEmbedder} for Java 9+.
     * This field cannot be marked as final to prevent its value from being inlined.
     */
    @SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
    private static boolean IS_JAVA_8 = true;

    private Configuration() {
        throw new AssertionError();
    }

    public static boolean isJava8() {
        return IS_JAVA_8;
    }

    public static boolean isInternalTaintingClass(String className) {
        return GaletteNames.TAINTER_INTERNAL_NAME.equals(className)
                || GaletteNames.TAGGED_OBJECT_INTERNAL_NAME.equals(className);
    }
}

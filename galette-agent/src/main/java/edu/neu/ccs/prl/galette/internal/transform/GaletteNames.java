package edu.neu.ccs.prl.galette.internal.transform;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.TaggedObject;
import edu.neu.ccs.prl.galette.internal.runtime.Tainter;
import edu.neu.ccs.prl.galette.internal.runtime.collection.Pair;
import org.objectweb.asm.Type;

public final class GaletteNames {
    /**
     * Internal name for {@link TagFrame}.
     * <p>
     * Non-null.
     */
    public static final String FRAME_INTERNAL_NAME = Type.getInternalName(TagFrame.class);
    /**
     * Internal name for {@link Tag}.
     * <p>
     * Non-null.
     */
    public static final String TAG_INTERNAL_NAME = Type.getInternalName(Tag.class);
    /**
     * Internal name for {@link Class}.
     * <p>
     * Non-null.
     */
    public static final String CLASS_INTERNAL_NAME = Type.getInternalName(Class.class);
    /**
     * Internal name for {@link Pair}.
     * <p>
     * Non-null.
     */
    public static final String PAIR_INTERNAL_NAME = Type.getInternalName(Pair.class);
    /**
     * Descriptor for {@link TagFrame}.
     * <p>
     * Non-null.
     */
    public static final String FRAME_DESCRIPTOR = Type.getDescriptor(TagFrame.class);
    /**
     * Descriptor for {@link Tag}.
     * <p>
     * Non-null.
     */
    public static final String TAG_DESCRIPTOR = Type.getDescriptor(Tag.class);
    /**
     * Descriptor for {@link Class}.
     * <p>
     * Non-null.
     */
    public static final String CLASS_DESCRIPTOR = Type.getDescriptor(Class.class);
    /**
     * Descriptor for {@link Pair}.
     * <p>
     * Non-null.
     */
    public static final String PAIR_DESCRIPTOR = Type.getDescriptor(Pair.class);
    /**
     * Internal name for {@link TaggedObject}.
     * <p>
     * Non-null.
     */
    public static final String TAGGED_OBJECT_INTERNAL_NAME = Type.getInternalName(TaggedObject.class);
    /**
     * Internal name for {@link Tainter}.
     * <p>
     * Non-null.
     */
    public static final String TAINTER_INTERNAL_NAME = "edu/neu/ccs/prl/galette/internal/runtime/Tainter";

    private GaletteNames() {
        throw new AssertionError(getClass() + " is a static utility class");
    }

    public static String getShadowVariableName(String name) {
        return name + "$$GALETTE";
    }
}

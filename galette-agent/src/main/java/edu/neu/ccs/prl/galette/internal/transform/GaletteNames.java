package edu.neu.ccs.prl.galette.internal.transform;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.frame.IndirectTagFrame;
import org.objectweb.asm.Type;

public final class GaletteNames {
    /**
     * Internal name for {@link TagFrame}.
     * <p>
     * Non-null.
     */
    public static final String FRAME_INTERNAL_NAME = Type.getInternalName(TagFrame.class);
    /**
     * Descriptor for {@link TagFrame}.
     * <p>
     * Non-null.
     */
    public static final String FRAME_DESCRIPTOR = Type.getDescriptor(TagFrame.class);
    /**
     * Internal name for {@link Tag}.
     * <p>
     * Non-null.
     */
    public static final String TAG_INTERNAL_NAME = Type.getInternalName(Tag.class);
    /**
     * Descriptor for {@link Tag}.
     * <p>
     * Non-null.
     */
    public static final String TAG_DESCRIPTOR = Type.getDescriptor(Tag.class);
    /**
     * Internal name for {@link IndirectTagFrame}.
     * <p>
     * Non-null.
     */
    public static final String INDIRECT_FRAME_INTERNAL_NAME = Type.getInternalName(IndirectTagFrame.class);

    private GaletteNames() {
        throw new AssertionError(getClass() + " is a static utility class");
    }

    public static String getShadowVariableName(String name) {
        return name + "$$GALETTE";
    }
}

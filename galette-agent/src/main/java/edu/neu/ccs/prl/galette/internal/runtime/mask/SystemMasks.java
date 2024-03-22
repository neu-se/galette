package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.ArrayTagStore;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

public class SystemMasks {
    @SuppressWarnings("SuspiciousSystemArraycopy")
    @Mask(owner = "java/lang/System", name = "arraycopy", isStatic = true)
    public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length, TagFrame frame) {
        System.arraycopy(src, srcPos, dest, destPos, length);
        ArrayTagStore.arraycopyTags(src, srcPos, dest, destPos, length);
    }
}

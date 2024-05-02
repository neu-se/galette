package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrameFactory;

public final class CharacterMasks {
    @Mask(owner = "java/lang/Character", name = "valueOf", isStatic = true)
    public static Character valueOf(char value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        Character result;
        if (Tag.isEmpty(valueTag)) {
            result = BoxTypeAccessor.valueOf(value, TagFrameFactory.acquire(frame, Tag.emptyTag()));
        } else {
            result = BoxTypeAccessor.newCharacter(value, TagFrameFactory.acquire(frame, Tag.emptyTag(), valueTag));
        }
        frame.setReturnTag(valueTag);
        return result;
    }
}

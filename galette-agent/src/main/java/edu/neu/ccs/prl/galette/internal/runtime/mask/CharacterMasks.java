package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

public final class CharacterMasks {
    @Mask(owner = "java/lang/Character", name = "valueOf", isStatic = true)
    public static Character valueOf(char value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        frame.setReturnTag(valueTag);
        if (Tag.isEmpty(valueTag)) {
            return BoxTypeAccessor.valueOf(value, TagFrame.create(frame));
        }
        TagFrame calleeFrame = TagFrame.create(frame).enqueue(Tag.getEmptyTag()).enqueue(valueTag);
        return BoxTypeAccessor.newCharacter(value, calleeFrame);
    }
}

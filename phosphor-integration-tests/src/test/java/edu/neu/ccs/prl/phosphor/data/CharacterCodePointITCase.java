package edu.neu.ccs.prl.phosphor.data;

import edu.neu.ccs.prl.phosphor.internal.runtime.Tag;
import edu.neu.ccs.prl.phosphor.internal.runtime.Tainter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class CharacterCodePointITCase {
    @Test
    public void codePointAt() {
        Tag expected = Tag.create("label");
        char[] c = new char[1];
        c[0] = Tainter.setTag('z', expected);
        int i = Character.codePointAt(c, 0);
        Tag actual = Tainter.getTag(i);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void codePointBefore() {
        Tag expected = Tag.create("label");
        char[] c = new char[] {'x', 'y'};
        c[0] = Tainter.setTag('z', expected);
        int i = Character.codePointBefore(c, 1);
        Tag actual = Tainter.getTag(i);
        Assertions.assertEquals(expected, actual);
    }
}

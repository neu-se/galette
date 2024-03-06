package edu.neu.ccs.prl.galette;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.Tainter;
import org.junit.jupiter.api.Test;

public class CharacterCodePointITCase {
    @Test
    public void codePointAt() {
        Tag expected = Tag.of("label");
        char[] c = new char[1];
        c[0] = Tainter.setTag('z', expected);
        int i = Character.codePointAt(c, 0);
        TagAssertions.assertTagEquals(i, "label");
    }

    @Test
    public void codePointBefore() {
        Tag expected = Tag.of("label");
        char[] c = new char[] {'x', 'y'};
        c[0] = Tainter.setTag('z', expected);
        int i = Character.codePointBefore(c, 1);
        TagAssertions.assertTagEquals(i, "label");
    }
}

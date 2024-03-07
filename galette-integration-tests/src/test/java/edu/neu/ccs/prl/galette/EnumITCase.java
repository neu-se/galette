package edu.neu.ccs.prl.galette;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.Tainter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class EnumITCase {
    @Test
    void valueOfTaintedNameReference() {
        String name = Tainter.setTag("RED", Tag.of("enum"));
        Color c = Color.valueOf(name);
        Assertions.assertEquals(Color.RED, c);
        TagAssertions.assertTagEquals(c, new Object[] {"enum"});
    }

    @Test
    @Disabled("Unimplemented")
    void valueOfTaintedNameCharacters() {
        char[] in = "RED".toCharArray();
        for (int i = 0; i < in.length; i++) {
            in[i] = Tainter.setTag(in[i], Tag.of(i));
        }
        Color c = Color.valueOf(new String(in));
        Assertions.assertEquals(Color.RED, c);
        char[] out = c.name().toCharArray();
        for (int i = 0; i < out.length; i++) {
            TagAssertions.assertTagEquals(out[i], i);
        }
    }

    @Test
    @Disabled("Unimplemented")
    void ordinalTaintedNameReference() {
        String name = Tainter.setTag("RED", Tag.of("enum"));
        Color c = Color.valueOf(name);
        Assertions.assertEquals(0, c.ordinal());
        TagAssertions.assertTagEquals(c.ordinal(), "enum");
    }

    @Test
    @Disabled("Unimplemented")
    void ordinalTaintedNameCharacters() {
        char[] in = "RED".toCharArray();
        for (int i = 0; i < in.length; i++) {
            in[i] = Tainter.setTag(in[i], Tag.of(i));
        }
        Color c = Color.valueOf(new String(in));
        Assertions.assertEquals(0, c.ordinal());
        TagAssertions.assertTagEquals(c.ordinal(), 0, 1, 2);
    }

    enum Color {
        RED,
        BLUE,
        GREEN
    }
}

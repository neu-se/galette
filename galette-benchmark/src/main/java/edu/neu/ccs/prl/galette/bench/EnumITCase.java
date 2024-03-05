package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.util.EnumMap;
import java.util.EnumSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@FlowBench
public class EnumITCase {
    @Test
    void valueOfTaintedNameReference(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"enum"};
        String name = manager.setLabels("RED", expected);
        Color c = Color.valueOf(name);
        Assertions.assertEquals(Color.RED, c);
        checker.check(expected, manager.getLabels(c));
    }

    @Test
    @Disabled("Unimplemented")
    void valueOfTaintedNameCharacters(TagManager manager, FlowChecker checker) {
        char[] name = new char[] {
            manager.setLabels('R', new Object[] {"0"}),
            manager.setLabels('E', new Object[] {"1"}),
            manager.setLabels('D', new Object[] {"2"})
        };
        Color c = Color.valueOf(new String(name));
        Assertions.assertEquals(Color.RED, c);
        // TODO: determine ground truth
        String processedName = c.name();
    }

    @Test
    @Disabled("Unimplemented")
    void enumSet(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"enum"};
        EnumSet<Color> set = EnumSet.of(manager.setLabels(Color.BLUE, expected));
        Color c = set.iterator().next();
        Assertions.assertEquals(Color.BLUE, c);
        checker.check(expected, manager.getLabels(c));
    }

    @Test
    void enumMap(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"enum"};
        EnumMap<Color, Object> map = new EnumMap<>(Color.class);
        Object value1 = new Object();
        Object value2 = new Object();
        map.put(Color.BLUE, manager.setLabels(value1, expected));
        map.put(Color.RED, value2);
        Object actual1 = map.get(Color.BLUE);
        Object actual2 = map.get(Color.RED);
        Assertions.assertEquals(actual1, value1);
        Assertions.assertEquals(actual2, value2);
        checker.check(expected, manager.getLabels(actual1));
        checker.checkEmpty(manager.getLabels(actual2));
    }

    enum Color {
        RED,
        BLUE,
        GREEN
    }
}

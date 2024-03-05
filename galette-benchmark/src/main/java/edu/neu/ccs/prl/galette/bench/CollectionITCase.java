package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
public class CollectionITCase {
    @Test
    void addGetTaintedElementArrayList(TagManager manager, FlowChecker checker) {
        addGetTaintedElementCollection(manager, checker, new ArrayList<>());
    }

    @Test
    void addGetTaintedElementLinkedList(TagManager manager, FlowChecker checker) {
        addGetTaintedElementCollection(manager, checker, new LinkedList<>());
    }

    @Test
    void addGetTaintedElementHashSet(TagManager manager, FlowChecker checker) {
        addGetTaintedElementCollection(manager, checker, new HashSet<>());
    }

    @Test
    void addGetTaintedElementMap(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"collection"};
        Object in = manager.setLabels(new Object(), expected);
        Map<Object, Object> map = new HashMap<>();
        map.put("key", in);
        map.put("x", new Object());
        Object out = map.get("key");
        Assertions.assertEquals(in, out);
        Object[] actual = manager.getLabels(out);
        checker.check(expected, actual);
        checker.checkEmpty(manager.getLabels(map.get("x")));
    }

    @Test
    void sortTaintedArrayList(TagManager manager, FlowChecker checker) {
        sortList(manager, checker, new ArrayList<>());
    }

    @Test
    void sortTaintedLinkedList(TagManager manager, FlowChecker checker) {
        sortList(manager, checker, new LinkedList<>());
    }

    private void sortList(TagManager manager, FlowChecker checker, List<String> list) {
        String[] values = new String[] {"G", "E", "B", "I", "H", "A", "D", "C", "F"};
        for (int i = 0; i < values.length; i++) {
            String element = manager.setLabels(values[i], new Object[] {String.valueOf(i)});
            list.add(element);
        }
        Collections.sort(list);
        String[] expectedValues = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
        for (int i = 0; i < expectedValues.length; i++) {
            Assertions.assertEquals(expectedValues[i], list.get(i));
        }
        String[] expectedLabels = new String[] {"5", "2", "7", "6", "1", "8", "0", "4", "3"};
        for (int i = 0; i < expectedLabels.length; i++) {
            checker.check(new Object[] {expectedLabels[i]}, manager.getLabels(list.get(i)));
        }
    }

    private void addGetTaintedElementCollection(
            TagManager manager, FlowChecker checker, Collection<Object> collection) {
        Object[] expected = new Object[] {"collection"};
        Object in = manager.setLabels(new Object(), expected);
        collection.add(in);
        collection.add(new Object());
        Iterator<Object> itr = collection.iterator();
        Object out = itr.next();
        Assertions.assertEquals(in, out);
        Object[] actual = manager.getLabels(out);
        checker.check(expected, actual);
        checker.checkEmpty(manager.getLabels(itr.next()));
    }
}

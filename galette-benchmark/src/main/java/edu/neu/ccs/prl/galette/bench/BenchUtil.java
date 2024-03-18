package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public final class BenchUtil {
    private BenchUtil() {
        throw new AssertionError(getClass() + " is a static utility class");
    }

    public static Stream<Arguments> cartesianProduct(Object[]... lists) {
        return cartesianProduct(0, lists).stream().map(List::toArray).map(Arguments::of);
    }

    private static <T> List<List<T>> cartesianProduct(int index, T[][] lists) {
        // Compute the cartesian product of lists[index:]
        if (index >= lists.length) {
            return Collections.emptyList();
        } else if (index == lists.length - 1) {
            return Arrays.stream(lists[index]).map(Collections::singletonList).collect(Collectors.toList());
        }
        List<List<T>> tails = cartesianProduct(index + 1, lists);
        List<List<T>> result = new LinkedList<>();
        for (T element : lists[index]) {
            for (List<T> tail : tails) {
                LinkedList<T> list = new LinkedList<>();
                list.add(element);
                list.addAll(tail);
                result.add(list);
            }
        }
        return result;
    }

    public static void taintWithIndices(TagManager manager, int[] a) {
        for (int i = 0; i < a.length; i++) {
            a[i] = manager.setLabels(a[i], new String[] {String.valueOf(i)});
        }
    }

    public static void taintWithIndices(TagManager manager, char[] c) {
        for (int i = 0; i < c.length; i++) {
            c[i] = manager.setLabels(c[i], new Object[] {String.valueOf(i)});
        }
    }

    public static String taintWithIndices(TagManager manager, String s) {
        char[] c = s.toCharArray();
        taintWithIndices(manager, s.toCharArray());
        return new String(c);
    }
}

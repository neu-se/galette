package edu.neu.ccs.prl.phosphor.internal.transform;

import org.junit.jupiter.api.Assertions;
import org.objectweb.asm.tree.ClassNode;

public class MethodRecordTest {
    public static void assertMethodExists(MethodRecord record) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(record.getOwner().replace("/", "."));
        ClassNode cn = AsmTestUtil.getClassNode(clazz);
        Assertions.assertTrue(
                cn.methods.stream().anyMatch(mn -> record.matches(cn.name, mn.name, mn.desc)),
                "Missing method for " + record);
    }
}

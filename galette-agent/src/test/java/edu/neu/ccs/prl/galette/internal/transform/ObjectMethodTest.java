package edu.neu.ccs.prl.galette.internal.transform;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

class ObjectMethodTest {
    @ParameterizedTest
    @MethodSource("records")
    void methodExists(MethodRecord record) throws ClassNotFoundException {
        MethodRecordTest.assertMethodExists(record);
    }

    @Test
    void allAccessibleObjectMethodsIncluded() {
        ClassNode cn = AsmTestUtil.getClassNode(Object.class);
        Map<MethodRecord, Boolean> values = new HashMap<>();
        for (ObjectMethod sm : ObjectMethod.values()) {
            values.put(sm.getRecord(), sm.isFinal());
        }
        for (MethodNode mn : cn.methods) {
            if (!AsmUtil.isSet(mn.access, Opcodes.ACC_PRIVATE)
                    && !"<clinit>".equals(mn.name)
                    && !"<init>".equals(mn.name)) {
                int opcode =
                        AsmUtil.isSet(mn.access, Opcodes.ACC_STATIC) ? Opcodes.INVOKESTATIC : Opcodes.INVOKEVIRTUAL;
                MethodRecord record = new MethodRecord(opcode, cn.name, mn.name, mn.desc, false);
                Assertions.assertTrue(values.containsKey(record), "Missing value for: " + record);
                boolean isFinal = AsmUtil.isSet(mn.access, Opcodes.ACC_FINAL);
                Assertions.assertEquals(
                        isFinal,
                        values.get(record),
                        String.format("Expected isFinal to be %s for %s", isFinal, record));
            }
        }
    }

    static Stream<MethodRecord> records() {
        return Arrays.stream(ObjectMethod.values()).map(ObjectMethod::getRecord);
    }
}

package edu.neu.ccs.prl.phosphor.internal.agent;

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

class ShadowlessMethodsTest {
    @ParameterizedTest
    @MethodSource("records")
    void methodExists(MethodRecord record) throws ClassNotFoundException {
        MethodRecordTest.assertMethodExists(record);
    }

    @Test
    void allAccessibleObjectMethodsIncluded() {
        ClassNode cn = AgentTestUtil.getClassNode(Object.class);
        Map<MethodRecord, Boolean> values = new HashMap<>();
        for (ShadowlessMethod sm : ShadowlessMethod.values()) {
            values.put(sm.getRecord(), sm.isFinal());
        }
        for (MethodNode mn : cn.methods) {
            if (!AccessUtil.isSet(mn.access, Opcodes.ACC_PRIVATE) && !"<clinit>".equals(mn.name)) {
                int opcode = Opcodes.INVOKEVIRTUAL;
                if (AccessUtil.isSet(mn.access, Opcodes.ACC_STATIC)) {
                    opcode = Opcodes.INVOKESTATIC;
                } else if (mn.name.equals("<init>")) {
                    opcode = Opcodes.INVOKESPECIAL;
                }
                MethodRecord record = new MethodRecord(opcode, cn.name, mn.name, mn.desc, false);
                Assertions.assertTrue(values.containsKey(record), "Missing value for: " + record);
                boolean isFinal = AccessUtil.isSet(mn.access, Opcodes.ACC_FINAL);
                Assertions.assertEquals(
                        isFinal,
                        values.get(record),
                        String.format("Expected isFinal to be %s for %s", isFinal, record));
            }
        }
    }

    static Stream<MethodRecord> records() {
        return Arrays.stream(ShadowlessMethod.values()).map(ShadowlessMethod::getRecord);
    }
}

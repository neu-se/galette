package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.example.IntHolder;
import edu.neu.ccs.prl.phosphor.internal.runtime.PhosphorFrame;
import edu.neu.ccs.prl.phosphor.internal.runtime.collection.SimpleList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

class ShadowMethodCreatorTest {
    @Test
    void objectShadowsRetainedIfDefined() throws ReflectiveOperationException {
        Class<?> clazz = instrumentIntHolder();
        Object holder1 = AsmTestUtil.findMethod(clazz, "getInstance").invoke(null);
        Object holder2 = AsmTestUtil.findMethod(clazz, "getInstance").invoke(null);
        boolean expected = holder1.equals(holder2);
        boolean actual = (boolean) AsmTestUtil.findMethod(clazz, ShadowMethodCreator.getShadowMethodName("equals"))
                .invoke(holder1, holder2, new PhosphorFrame());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void objectShadowsCreatedIfMissing() throws ReflectiveOperationException {
        Class<?> clazz = instrumentIntHolder();
        Object holder = AsmTestUtil.findMethod(clazz, "getInstance").invoke(null);
        String expected = holder.toString();
        String actual = (String) AsmTestUtil.findMethod(clazz, ShadowMethodCreator.getShadowMethodName("toString"))
                .invoke(holder, new PhosphorFrame());
        Assertions.assertEquals(expected, actual);
    }

    private static Class<?> instrumentIntHolder() {
        ClassNode cn = AsmTestUtil.getClassNode(IntHolder.class);
        ShadowMethodCreator creator = new ShadowMethodCreator(cn, true, false);
        SimpleList<MethodNode> shadows = creator.createShadows();
        for (int i = 0; i < shadows.size(); i++) {
            cn.methods.add(shadows.get(i));
        }
        return AsmTestUtil.load(cn);
    }
}

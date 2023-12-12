package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.example.IntHolder;
import edu.neu.ccs.prl.phosphor.internal.runtime.PhosphorFrame;
import edu.neu.ccs.prl.phosphor.internal.runtime.collection.SimpleList;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

class ShadowMethodCreatorTest {
    @Test
    void objectShadowsRetainedIfDefined() throws ReflectiveOperationException {
        Class<?> clazz = instrumentIntHolder();
        Method m = clazz.getDeclaredMethod("equals", Object.class, PhosphorFrame.class);
        Assertions.assertNotNull(m);
    }

    @Test
    void objectShadowsCreatedIfMissing() throws ReflectiveOperationException {
        Class<?> clazz = instrumentIntHolder();
        Method m = clazz.getDeclaredMethod("toString", PhosphorFrame.class);
        Assertions.assertNotNull(m);
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

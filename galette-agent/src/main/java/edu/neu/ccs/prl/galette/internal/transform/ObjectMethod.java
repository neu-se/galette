package edu.neu.ccs.prl.galette.internal.transform;

import edu.neu.ccs.prl.galette.internal.runtime.TaggedObject;
import org.objectweb.asm.Opcodes;

/**
 *   Represents a method call that can be made from instrumented code for which a corresponding shadow method may or
 *   may not exist.
 *   We cannot guarantee that a shadow is created for these methods because they are declared in {@link Object}
 *   which cannot be instrumented.
 *   Because of the potential for dynamic dispatching on the subtype, classes directly extending
 *   {@link Object} should have native wrappers added for these methods if they do not provide their own definition of
 *   the method.
 *   If the method represented by an {@link ObjectMethod} is non-final, then calls to it should be remapped to the
 *   corresponding shadow if dispatched on a {@link TaggedObject} instance.
 */
public enum ObjectMethod {
    OBJECT_GET_CLASS(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false, true),
    OBJECT_NOTIFY(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "notify", "()V", false, true),
    OBJECT_NOTIFY_ALL(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "notifyAll", "()V", false, true),
    OBJECT_WAIT0(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "wait", "()V", false, true),
    OBJECT_WAIT1(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "wait", "(J)V", false, true),
    OBJECT_WAIT2(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "wait", "(JI)V", false, true),
    OBJECT_HASH_CODE(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "hashCode", "()I", false, false),
    OBJECT_EQUALS(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z", false, false),
    OBJECT_CLONE(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "clone", "()Ljava/lang/Object;", false, false),
    OBJECT_TO_STRING(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false, false),
    OBJECT_FINALIZE(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "finalize", "()V", false, false);

    private final MethodRecord record;
    private final boolean isFinal;

    ObjectMethod(int opcode, String owner, String name, String descriptor, boolean isInterface, boolean isFinal) {
        this.record = new MethodRecord(opcode, owner, name, descriptor, isInterface);
        this.isFinal = isFinal;
    }

    public MethodRecord getRecord() {
        return record;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public static ObjectMethod findMatch(String name, String descriptor) {
        return findMatch("java/lang/Object", name, descriptor);
    }

    public static ObjectMethod findMatch(String owner, String name, String descriptor) {
        for (ObjectMethod method : values()) {
            if (method.getRecord().matches(owner, name, descriptor)) {
                return method;
            }
        }
        return null;
    }
}

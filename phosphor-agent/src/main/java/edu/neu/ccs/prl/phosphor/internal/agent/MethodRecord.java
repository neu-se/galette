package edu.neu.ccs.prl.phosphor.internal.agent;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Immutable record that stores the information needed by a {@link MethodVisitor} to add a call to a method.
 */
public final class MethodRecord {
    private final int opcode;
    private final String owner;
    private final String name;
    private final String descriptor;
    private final boolean isInterface;

    public MethodRecord(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (owner == null || name == null || descriptor == null) {
            throw new NullPointerException();
        }
        switch (opcode) {
            case Opcodes.INVOKEVIRTUAL:
            case Opcodes.INVOKESPECIAL:
            case Opcodes.INVOKESTATIC:
            case Opcodes.INVOKEINTERFACE:
                break;
            default:
                throw new IllegalArgumentException("Illegal opcode: " + opcode);
        }
        this.opcode = opcode;
        this.owner = owner;
        this.name = name;
        this.descriptor = descriptor;
        this.isInterface = isInterface;
    }

    /**
     * Returns the opcode of the type instruction associated with the method either {@link Opcodes#INVOKEVIRTUAL},
     * {@link Opcodes#INVOKESPECIAL}, {@link Opcodes#INVOKESTATIC}, or {@link Opcodes#INVOKEINTERFACE}.
     *
     * @return the opcode of the type instruction associated with the method
     */
    public int getOpcode() {
        return opcode;
    }

    /**
     * Returns the internal name of the method's owner class.
     *
     * @return the internal name of the method's owner class
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Returns the method's name.
     *
     * @return the method's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the method's descriptor.
     *
     * @return the method's descriptor
     */
    public String getDescriptor() {
        return descriptor;
    }

    /**
     * Returns {@code true} if the method's owner class is an interface.
     *
     * @return {@code true} if the method's owner class is an interface
     */
    public boolean isInterface() {
        return isInterface;
    }

    /**
     * Tells the specified method visitor to visit a method instruction for the method.
     * If the specified method visitor is {@code null} does nothing.
     *
     * @param methodVisitor the method visitor that should visit the method
     */
    public void accept(MethodVisitor methodVisitor) {
        if (methodVisitor == null) {
            return;
        }
        methodVisitor.visitMethodInsn(getOpcode(), getOwner(), getName(), getDescriptor(), isInterface());
    }

    /**
     * Returns {@code true} if this record's owner, name, and descriptor are equal to the specified owner, name, and
     * descriptor.
     *
     * @param owner the method owner to check for
     * @param name the method name to check for
     * @param descriptor method descriptor to check for
     * @return {@code true} if this record's owner, name, and descriptor are equal to the specified owner, name and
     * descriptor
     */
    public boolean matches(String owner, String name, String descriptor) {
        return this.owner.equals(owner) && this.name.equals(name) && this.descriptor.equals(descriptor);
    }

    @Override
    public String toString() {
        return String.format("%s.%s%s", owner, name, descriptor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof MethodRecord)) {
            return false;
        }
        MethodRecord that = (MethodRecord) o;
        return opcode == that.opcode
                && isInterface == that.isInterface
                && matches(that.owner, that.name, that.descriptor);
    }

    @Override
    public int hashCode() {
        int result = opcode;
        result = 31 * result + owner.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + descriptor.hashCode();
        result = 31 * result + (isInterface ? 1 : 0);
        return result;
    }
}

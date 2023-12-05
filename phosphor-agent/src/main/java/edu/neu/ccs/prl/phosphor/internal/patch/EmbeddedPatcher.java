package edu.neu.ccs.prl.phosphor.internal.patch;

import java.util.function.Function;

public class EmbeddedPatcher {
    private final Function<String, byte[]> entryLocator;

    public EmbeddedPatcher(Function<String, byte[]> entryLocator) {
        if (entryLocator == null) {
            throw new NullPointerException();
        }
        this.entryLocator = entryLocator;
    }

    public byte[] patch(String name, byte[] buffer) {
        name = name.replace(".class", "");
        // TODO fix getObject vs getReference for Java 11 vs 17
        if (UnsafeAdapterPatcher.isApplicable(name)) {
            return Patcher.apply(buffer, UnsafeAdapterPatcher::createForEmbedded);
        }
        return buffer;
    }
}

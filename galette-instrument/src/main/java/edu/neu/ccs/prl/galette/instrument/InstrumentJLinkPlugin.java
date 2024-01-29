package edu.neu.ccs.prl.galette.instrument;

import jdk.tools.jlink.plugin.ResourcePoolEntry;

public class InstrumentJLinkPlugin extends GaletteJLinkPlugin {
    @Override
    public String getName() {
        return "instrument";
    }

    @Override
    public String getDescription() {
        return "Applies instrumentation to the runtime image.";
    }

    @Override
    public Category getType() {
        return Category.MODULEINFO_TRANSFORMER;
    }

    @Override
    protected ResourcePoolEntry transform(ResourcePoolEntry entry) {
        if (entry.type().equals(ResourcePoolEntry.Type.CLASS_OR_RESOURCE)
                && entry.path().endsWith(".class")) {
            byte[] instrumented = instrumentation.apply(entry.contentBytes());
            return instrumented == null ? entry : entry.copyWithContent(instrumented);
        }
        return entry;
    }
}

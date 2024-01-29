package edu.neu.ccs.prl.galette.instrument;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import jdk.tools.jlink.plugin.Plugin;
import jdk.tools.jlink.plugin.ResourcePool;
import jdk.tools.jlink.plugin.ResourcePoolBuilder;
import jdk.tools.jlink.plugin.ResourcePoolEntry;

public abstract class GaletteJLinkPlugin implements Plugin {
    protected Instrumentation instrumentation;
    protected ResourcePoolPacker packer;

    @Override
    public boolean hasArguments() {
        return true;
    }

    @Override
    public void configure(Map<String, String> config) {
        try (FileReader reader = new FileReader(config.get("options"))) {
            Properties options = new Properties();
            options.load(reader);
            instrumentation = Instrumentation.create(config.get("type"), options);
        } catch (IOException | ReflectiveOperationException e) {
            throw new RuntimeException("Failed to process configuration", e);
        }
    }

    @Override
    public ResourcePool transform(ResourcePool pool, ResourcePoolBuilder out) {
        packer = new ResourcePoolPacker(instrumentation, pool, out);
        pool.transformAndCopy(this::transform, out);
        return out.build();
    }

    protected abstract ResourcePoolEntry transform(ResourcePoolEntry entry);
}

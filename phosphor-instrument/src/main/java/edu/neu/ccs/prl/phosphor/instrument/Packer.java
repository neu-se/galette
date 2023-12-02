package edu.neu.ccs.prl.phosphor.instrument;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.jacoco.core.internal.InputStreams;

public abstract class Packer {
    private final Instrumentation instrumentation;
    private final BiFunction<String, byte[], byte[]> patcher;

    public Packer(Instrumentation instrumentation, Function<String, byte[]> entryLocator) {
        this.instrumentation = instrumentation;
        this.patcher = instrumentation.createPatcher(entryLocator);
    }

    public abstract void pack(String name, byte[] content) throws IOException;

    public Set<String> pack() throws IOException {
        // Pack the JARs and directories
        // Return the set of packages for packed classes
        Set<String> packages = new HashSet<>();
        for (File element : instrumentation.getElementsToPack()) {
            if (element.isDirectory()) {
                packDirectory(element, packages);
            } else {
                packJar(element, packages);
            }
        }
        return packages;
    }

    private void pack(String resourceName, InputStream in, Set<String> packages) throws IOException {
        if (instrumentation.shouldPack(resourceName)) {
            byte[] content = patcher.apply(resourceName, InputStreams.readFully(in));
            pack(resourceName, content);
            if (resourceName.endsWith(".class")) {
                String packageName = resourceName.substring(0, resourceName.lastIndexOf('/'));
                packages.add(packageName);
            }
        }
    }

    private void packDirectory(File directory, Set<String> packages) throws IOException {
        try (Stream<Path> walk = Files.walk(directory.toPath())) {
            for (Path path : walk.filter(Files::isRegularFile).collect(Collectors.toList())) {
                String name = directory.toPath().relativize(path).toFile().getPath();
                File file = path.toAbsolutePath().toFile();
                if (name.endsWith(".jar")) {
                    packJar(file, packages);
                } else {
                    try (InputStream in = Files.newInputStream(file.toPath())) {
                        pack(name, in, packages);
                    }
                }
            }
        }
    }

    private void packJar(File element, Set<String> packages) throws IOException {
        try (ZipFile zip = new ZipFile(element)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    try (InputStream in = zip.getInputStream(entry)) {
                        pack(entry.getName(), in, packages);
                    }
                }
            }
        }
    }
}

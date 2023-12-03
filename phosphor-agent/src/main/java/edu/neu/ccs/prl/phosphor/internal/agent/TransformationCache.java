package edu.neu.ccs.prl.phosphor.internal.agent;

import edu.neu.ccs.prl.phosphor.internal.runtime.Arrays;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public final class TransformationCache {
    /**
     * Directory in which cached instrumented class files should be stored or
     * {@code null} if instrumented class files should not be cached.
     */
    private final File directory;

    public TransformationCache() {
        this.directory = null;
    }

    public TransformationCache(File directory) throws IOException {
        Files.createDirectories(directory.toPath());
        this.directory = directory;
    }

    public void storeEntry(String className, byte[] originalBuffer, byte[] instrumentedBuffer) throws IOException {
        if (directory == null || instrumentedBuffer == null) {
            return;
        }
        try (FileOutputStream fos = new FileOutputStream(getEntryFile(className))) {
            fos.write(instrumentedBuffer);
        }
        try (FileOutputStream fos = new FileOutputStream(getChecksumFile(className))) {
            fos.write(FileUtil.checksum(originalBuffer));
        }
    }

    public boolean hasEntry(String className, byte[] originalBuffer) throws IOException {
        if (directory == null) {
            return false;
        }
        File checksumFile = getChecksumFile(className);
        return checksumFile.exists()
                && Arrays.equals(FileUtil.checksum(originalBuffer), Files.readAllBytes(checksumFile.toPath()));
    }

    public byte[] loadEntry(String className) throws IOException {
        if (directory == null) {
            throw new IllegalArgumentException("No cache entry available for: " + className);
        }
        return Files.readAllBytes(getEntryFile(className).toPath());
    }

    private File getEntryFile(String className) {
        return new File(directory, className.replace("/", ".") + ".class");
    }

    private File getChecksumFile(String className) {
        return new File(directory, className.replace("/", ".") + ".md5");
    }
}

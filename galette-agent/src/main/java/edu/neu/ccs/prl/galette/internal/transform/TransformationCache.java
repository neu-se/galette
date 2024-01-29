package edu.neu.ccs.prl.galette.internal.transform;

import edu.neu.ccs.prl.galette.internal.runtime.Arrays;
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

    public TransformationCache(File directory) throws IOException {
        Files.createDirectories(directory.toPath());
        this.directory = directory;
    }

    public void storeEntry(String className, byte[] originalBuffer, byte[] instrumentedBuffer) throws IOException {
        if (!FileUtil.isInitialized()) {
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
        if (!FileUtil.isInitialized()) {
            return false;
        }
        File checksumFile = getChecksumFile(className);
        return checksumFile.exists()
                && Arrays.equals(FileUtil.checksum(originalBuffer), Files.readAllBytes(checksumFile.toPath()));
    }

    public byte[] loadEntry(String className) throws IOException {
        return Files.readAllBytes(getEntryFile(className).toPath());
    }

    private File getEntryFile(String className) {
        return new File(directory, className.replace("/", ".") + ".class");
    }

    private File getChecksumFile(String className) {
        return new File(directory, className.replace("/", ".") + ".md5");
    }
}

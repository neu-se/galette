package edu.neu.ccs.prl.phosphor.instrument;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.jacoco.core.internal.InputStreams;

public final class InstrumentUtil {
    /**
     * MD5 MessageDigest instance.
     * <br>
     * Non-null.
     */
    private static final MessageDigest md5Inst;

    static {
        try {
            md5Inst = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private InstrumentUtil() {
        throw new AssertionError();
    }

    public static File javaHomeToBin(File javaHome) {
        return new File(javaHome, "bin");
    }

    public static File javaHomeToJavaExec(File javaHome) {
        return new File(javaHomeToBin(javaHome), "java");
    }

    public static File javaHomeToJLinkExec(File javaHome) {
        return new File(javaHomeToBin(javaHome), "jlink");
    }

    public static boolean isJavaHome(File directory) {
        return javaHomeToJavaExec(directory).isFile();
    }

    public static boolean isModularJvm(File javaHome) {
        return isJavaHome(javaHome) && javaHomeToJLinkExec(javaHome).isFile();
    }

    public static File getClassPathElement(Class<?> clazz) {
        return new File(
                clazz.getProtectionDomain().getCodeSource().getLocation().getPath());
    }

    public static byte[] checksum(byte[] input) {
        return md5Inst.digest(input);
    }

    public static File createTemporaryFile(String prefix, String suffix) throws IOException {
        File file = Files.createTempFile(prefix, suffix).toFile();
        file.deleteOnExit();
        Files.createDirectories(file.getParentFile().toPath());
        return file;
    }

    public static byte[] readAllBytes(File file) throws IOException {
        try (InputStream in = Files.newInputStream(file.toPath())) {
            return InputStreams.readFully(in);
        }
    }
}

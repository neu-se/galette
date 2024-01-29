package edu.neu.ccs.prl.galette.internal.transform;

import java.io.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class FileUtil {
    /**
     * MD5 MessageDigest instance.
     * <p>
     * Non-null.
     */
    private static final MessageDigest digest;

    static {
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static boolean isInitialized() {
        // Prevents issues produced from circular class initialization from dynamic class definitions
        return digest != null;
    }

    private FileUtil() {
        throw new AssertionError();
    }

    public static File createTemporaryFile(String prefix, String suffix) throws IOException {
        File file = Files.createTempFile(prefix, suffix).toFile();
        file.deleteOnExit();
        Files.createDirectories(file.getParentFile().toPath());
        return file;
    }

    public static byte[] readAllBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int len; (len = in.read(buffer)) != -1; ) {
            ((OutputStream) out).write(buffer, 0, len);
        }
        return out.toByteArray();
    }

    public static byte[] checksum(byte[] input) {
        synchronized (digest) {
            return digest.digest(input);
        }
    }
}

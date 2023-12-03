package edu.neu.ccs.prl.phosphor.internal.patch;

import java.io.*;
import java.nio.file.Files;
import java.util.function.Function;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public final class Patcher {
    private Patcher() {
        throw new AssertionError();
    }

    public static void main(String[] args) throws IOException {
        File archive = new File(args[0]);
        File temp = createTemporaryFile("patch-", ".jar");
        try (ZipInputStream zin = new ZipInputStream(Files.newInputStream(archive.toPath()));
                ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(temp.toPath()))) {
            for (ZipEntry entry; (entry = zin.getNextEntry()) != null; ) {
                byte[] content = readAllBytes(zin);
                if (entry.getName().endsWith(".class")) {
                    content = patch(entry.getName(), content);
                }
                writeEntry(zos, entry, content);
            }
        }
        if (archive.exists() && !archive.delete()) {
            throw new IOException("Failed to delete unpatched archive: " + archive);
        }
        if (!temp.renameTo(archive)) {
            throw new IOException("Failed to move patched JAR: " + temp);
        }
    }

    private static void writeEntry(ZipOutputStream zos, ZipEntry entry, byte[] content) throws IOException {
        ZipEntry outEntry = new ZipEntry(entry.getName());
        outEntry.setMethod(entry.getMethod());
        if (entry.getMethod() == ZipEntry.STORED) {
            // Uncompressed entries require entry size and CRC
            outEntry.setSize(content.length);
            outEntry.setCompressedSize(content.length);
            CRC32 crc = new CRC32();
            crc.update(content, 0, content.length);
            outEntry.setCrc(crc.getValue());
        }
        zos.putNextEntry(outEntry);
        zos.write(content);
        zos.closeEntry();
    }

    private static byte[] patch(String name, byte[] classFileBuffer) {
        name = name.replace(".class", "");
        if (UnsafeWrapperPatchingCV.isApplicable(name)) {
            return apply(classFileBuffer, UnsafeWrapperPatchingCV::new);
        }
        return classFileBuffer;
    }

    private static byte[] apply(byte[] classFileBuffer, Function<ClassVisitor, ClassVisitor> visitorFactory) {
        ClassReader cr = new ClassReader(classFileBuffer);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = visitorFactory.apply(cw);
        cr.accept(cv, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
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
}

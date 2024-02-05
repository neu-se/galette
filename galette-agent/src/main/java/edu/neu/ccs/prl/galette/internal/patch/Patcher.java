package edu.neu.ccs.prl.galette.internal.patch;

import edu.neu.ccs.prl.galette.internal.transform.FileUtil;
import java.io.File;
import java.io.IOException;
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
    /**
     * Function for locating the bytes of JCL classes or {@code null} if this patcher is not being applied to an
     * embedded Galette distribution.
     */
    private final Function<String, byte[]> entryLocator;

    public Patcher(Function<String, byte[]> entryLocator) {
        this.entryLocator = entryLocator;
    }

    public byte[] patch(String name, byte[] classFileBuffer) {
        String className = name.endsWith(".class") ? name.substring(0, name.length() - ".class".length()) : name;
        ClassReader cr = new ClassReader(classFileBuffer);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = cw;
        if (entryLocator == null && RegistryPatcher.isApplicable(className)) {
            cv = new RegistryPatcher(cv, className);
        }
        if (UnsafeAdapterPatcher.isApplicable(className)) {
            cv = new UnsafeAdapterPatcher(cv, entryLocator);
        }
        if (entryLocator == null) {
            cv = new MemberAccessGenerator(className, cv);
        }
        cr.accept(cv, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
    }

    public static void main(String[] args) throws IOException {
        File archive = new File(args[0]);
        File temp = FileUtil.createTemporaryFile("patch-", ".jar");
        Patcher patcher = new Patcher(null);
        try (ZipInputStream zin = new ZipInputStream(Files.newInputStream(archive.toPath()));
                ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(temp.toPath()))) {
            for (ZipEntry entry; (entry = zin.getNextEntry()) != null; ) {
                byte[] content = FileUtil.readAllBytes(zin);
                if (entry.getName().endsWith(".class")) {
                    content = patcher.patch(entry.getName(), content);
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
}

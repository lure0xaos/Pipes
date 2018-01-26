package javax.microedition.rms;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

public class RecordStore {
    private static final String ENV_USER_HOME = "user.home";
    private static final String EXT = ".rms";
    private static final Path ROOT = Paths.get(System.getProperty(ENV_USER_HOME)).toAbsolutePath();
    private final Path store;

    private RecordStore(String name, boolean create) throws RecordStoreException {
        store = ROOT.resolve(name);
        if (!Files.exists(store) && create) {
            try {
                Files.createDirectories(store);
            } catch (IOException e) {
                throw new RecordStoreException(e.getMessage(), e);
            }
        }
        if (!Files.isDirectory(store)) {
            throw new RecordStoreNotFoundException(store.toString());
        }
    }

    public static void deleteRecordStore(String name) throws RecordStoreException {
        try {
            Files.walkFileTree(ROOT.resolve(name), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                        throws IOException {
                    if (exc == null) {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    } else {
                        throw exc;
                    }
                }
            });
        } catch (IOException e) {
            throw new RecordStoreException(e.getMessage(), e);
        }
    }

    public static RecordStore openRecordStore(String name, boolean create) throws RecordStoreException {
        return new RecordStore(name, create);
    }

    public void addRecord(byte[] bytes, int offset, int length) throws RecordStoreException {
        int num = 1;
        while (Files.exists(getPath(num))) {
            num++;
        }
        setRecord(num, bytes, offset, length);
    }

    @SuppressWarnings({"EmptyMethod", "RedundantThrows"})
    public void closeRecordStore() throws RecordStoreException {
    }

    private Path getPath(int num) {
        return store.resolve(String.format("%d%s", num, EXT));
    }

    public byte[] getRecord(int num) throws RecordStoreException {
        try {
            return Files.readAllBytes(getPath(num));
        } catch (IOException e) {
            throw new RecordStoreException(e.getMessage(), e);
        }
    }

    public void setRecord(int num, byte[] bytes, int offset, int length) throws RecordStoreException {
        try {
            Files.write(getPath(num), Arrays.copyOfRange(bytes, offset, offset + length));
        } catch (IOException e) {
            throw new RecordStoreException(e.getMessage(), e);
        }
    }
}

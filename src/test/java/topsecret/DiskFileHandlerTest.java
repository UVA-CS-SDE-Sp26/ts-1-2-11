package topsecret;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DiskFileHandlerTest {

    @TempDir
    Path temp;

    @Test
    void listsTxtFiles_andReadsContents() throws Exception {
        Path dataDir = temp.resolve("data");
        Files.createDirectories(dataDir);

        Files.writeString(dataDir.resolve("filea.txt"), "A");
        Files.writeString(dataDir.resolve("fileb.txt"), "B");
        Files.writeString(dataDir.resolve("ignore.md"), "X");

        DiskFileHandler_PLACEHOLDER_B fh = new DiskFileHandler_PLACEHOLDER_B(dataDir.toString());

        List<String> files = fh.listDataFiles();
        assertTrue(files.contains("filea.txt"));
        assertTrue(files.contains("fileb.txt"));
        assertFalse(files.contains("ignore.md"));

        assertEquals("A", fh.readFile("filea.txt"));
    }
}

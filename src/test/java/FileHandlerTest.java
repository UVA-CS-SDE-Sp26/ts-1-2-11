import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FileHandlerTest {

    private static Path DATA_DIR = Paths.get("data");

    @BeforeEach
    void prepareTestFiles() throws IOException {
        if (!Files.exists(DATA_DIR)) {
            Files.createDirectory(DATA_DIR);
        }

        Files.writeString(DATA_DIR.resolve("alpha_report.txt"), "alpha\n");
        Files.writeString(DATA_DIR.resolve("bravo_notes.txt"), "bravo\nnotes\n");
        Files.writeString(DATA_DIR.resolve("skip_me.log"), "ignore");
    }

    @AfterEach
    void cleanUpTestFiles() throws IOException {
        Files.deleteIfExists(DATA_DIR.resolve("alpha_report.txt"));
        Files.deleteIfExists(DATA_DIR.resolve("bravo_notes.txt"));
        Files.deleteIfExists(DATA_DIR.resolve("skip_me.log"));
    }

    // Constructor behavior tests

    @Test
    void fileHandlerInitializesWhenDataFolderExists() {
        assertDoesNotThrow(FileHandler::new,
                "FileHandler should initialize when data folder exists.");
    }

    // listFiles() tests

    @Test
    void listFilesExcludesNonTextFiles() {
        FileHandler handler = new FileHandler();
        List<String> files = handler.listFiles();

        assertTrue(files.contains("alpha_report.txt"),
                "Expected alpha_report.txt to be included.");
        assertTrue(files.contains("bravo_notes.txt"),
                "Expected bravo_notes.txt to be included.");
        assertFalse(files.contains("skip_me.log"),
                "Expected non-text files to be excluded.");
    }

    @Test
    void listFilesReturnsAlphabeticalOrder() {
        FileHandler handler = new FileHandler();
        List<String> files = handler.listFiles();

        int first = files.indexOf("alpha_report.txt");
        int second = files.indexOf("bravo_notes.txt");

        assertTrue(first < second,
                "Expected files to be returned in alphabetical order.");
    }

    // readFile() tests

    @Test
    void readFileReadsEntireFileContents() throws IOException {
        FileHandler handler = new FileHandler();
        String contents = handler.readFile("bravo_notes.txt");

        assertEquals("bravo\nnotes\n", contents,
                "Expected file contents to match exactly.");
    }

    @Test
    void readFileThrowsExceptionForMissingFile() {
        FileHandler handler = new FileHandler();

        assertThrows(IOException.class,
                () -> handler.readFile("missing_file.txt"),
                "Expected IOException for missing file.");
    }

    // Input validation tests

    @Test
    void readFileRejectsEmptyOrNullNames() {
        FileHandler handler = new FileHandler();

        assertThrows(IllegalArgumentException.class,
                () -> handler.readFile(null),
                "Null filename should be rejected.");
        assertThrows(IllegalArgumentException.class,
                () -> handler.readFile(""),
                "Empty filename should be rejected.");
        assertThrows(IllegalArgumentException.class,
                () -> handler.readFile("   "),
                "Blank filename should be rejected.");
    }

    @Test
    void readFileBlocksUnsafePaths() {
        FileHandler handler = new FileHandler();

        assertThrows(IllegalArgumentException.class,
                () -> handler.readFile("../secret.txt"),
                "Directory traversal should be blocked.");
        assertThrows(IllegalArgumentException.class,
                () -> handler.readFile("folder/file.txt"),
                "Slash-based paths should be blocked.");
        assertThrows(IllegalArgumentException.class,
                () -> handler.readFile("folder\\file.txt"),
                "Backslash-based paths should be blocked.");
    }
}

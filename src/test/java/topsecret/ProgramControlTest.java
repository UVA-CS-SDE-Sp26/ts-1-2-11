package topsecret;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ProgramControlTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void redirectStdout() {
        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStdout() {
        System.setOut(originalOut);
    }

    // --- Test Doubles (stubs) ---
    // These let us test ProgramControl without needing real disk files.

    static class StubFileHandler implements FileHandler {
        private final List<String> files;
        private final Map<String, String> contents;

        StubFileHandler(List<String> files, Map<String, String> contents) {
            this.files = files;
            this.contents = contents;
        }

        @Override
        public List<String> listDataFiles() {
            return files;
        }

        @Override
        public String readFile(String filename) {
            if (!contents.containsKey(filename)) {
                throw new RuntimeException("File not found: " + filename);
            }
            return contents.get(filename);
        }

    }


    static class StubCipherService implements CipherService {
        @Override
        public String decipher(String cipherText, String keyPath) {
            // Identity decipher: returns same text (placeholder behavior)
            return cipherText;
        }
    }

    // --- Helpers ---
    private String stdout() {
        return outContent.toString().replace("\r\n", "\n");
    }

    @Test
    void noArgs_printsNumberedFileList() {
        FileHandler fh = new StubFileHandler(
                Arrays.asList("filea.txt", "fileb.txt"),
                Map.of("filea.txt", "A", "fileb.txt", "B")
        );

        ProgramControl pc = new ProgramControl(
                fh,
                new StubCipherService(),
                "ciphers/identity.key"   // or whatever key file you’re supposed to use
        );


        pc.run(new String[]{}); // <-- IMPORTANT: your ProgramControl must have run(String[] args)

        String out = stdout();
        assertTrue(out.contains("01 filea.txt"));
        assertTrue(out.contains("02 fileb.txt"));
    }

    @Test
    void validSelection_printsFileContents() {
        FileHandler fh = new StubFileHandler(
                Arrays.asList("filea.txt", "fileb.txt"),
                Map.of("filea.txt", "Hello from A", "fileb.txt", "Hello from B")
        );


        ProgramControl pc = new ProgramControl(
                fh,
                new StubCipherService(),
                "ciphers/identity.key"   // any string is fine if your StubCipherService ignores keyPath
        );



        pc.run(new String[]{"01"});

        String out = stdout();
        assertTrue(out.contains("Hello from A"));
    }

    @Test
    void invalidSelection_printsError() {
        FileHandler fh = new StubFileHandler(
                Arrays.asList("filea.txt"),
                Map.of("filea.txt", "A")
        );

        ProgramControl pc = new ProgramControl(
                fh,
                new StubCipherService(),
                "ciphers/identity.key"   // any string is fine if your StubCipherService ignores keyPath
        );


        pc.run(new String[]{"99"});

        String out = stdout().toLowerCase();
        assertTrue(out.contains("error") || out.contains("invalid"));
    }
}

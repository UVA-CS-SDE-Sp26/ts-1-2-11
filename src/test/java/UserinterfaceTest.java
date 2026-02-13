import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class UserinterfaceTest {

    private ByteArrayOutputStream outBytes;
    private ByteArrayOutputStream errBytes;
    private PrintStream out;
    private PrintStream err;

    private Userinterface ui; // control is null on purpose

    @BeforeEach
    void setUp() {
        outBytes = new ByteArrayOutputStream();
        errBytes = new ByteArrayOutputStream();
        out = new PrintStream(outBytes);
        err = new PrintStream(errBytes);

        ui = new Userinterface(null, out, err);
    }

    private String stdout() {
        return outBytes.toString(StandardCharsets.UTF_8);
    }

    private String stderr() {
        return errBytes.toString(StandardCharsets.UTF_8);
    }

    private void assertUsagePrinted() {
        String s = stdout();
        assertTrue(s.contains("Usage: java topsecret [number] [optional_key_path]"));
        assertTrue(s.contains("Examples:"));
        assertTrue(s.contains("java topsecret"));
    }

    @Test
    void nullArgs_printsErrorAndUsage() {
        ui.run(null);

        assertTrue(stderr().contains("Error: Arguments cannot be null"));
        assertUsagePrinted();
    }

    @Test
    void tooManyArgs_printsErrorAndUsage() {
        ui.run(new String[]{"01", "k.txt", "extra"});

        assertTrue(stderr().contains("Error: Too many arguments"));
        assertUsagePrinted();
    }

    @Test
    void invalidFileNumber_notTwoDigits_printsErrorAndUsage() {
        ui.run(new String[]{"999"});

        assertTrue(stderr().contains("Error: Invalid file number. Must be two digits (e.g., 01)."));
        assertUsagePrinted();
    }

    @Test
    void invalidFileNumber_nonDigits_printsErrorAndUsage() {
        ui.run(new String[]{"ab"});

        assertTrue(stderr().contains("Error: Invalid file number. Must be two digits (e.g., 01)."));
        assertUsagePrinted();
    }

    @Test
    void invalidFileNumber_nullString_printsErrorAndUsage() {
        ui.run(new String[]{null});

        assertTrue(stderr().contains("Error: Invalid file number. Must be two digits (e.g., 01)."));
        assertUsagePrinted();
    }

    @Test
    void oneArg_defaultKey_whenControlMissing_printsErrorAndUsage() {
        ui.run(new String[]{"01"});

        // Your UI must include: if (control == null) { printError("Control is not configured"); return; }
        assertTrue(stderr().contains("Error: Control is not configured"));
        assertUsagePrinted();
    }

    @Test
    void twoArgs_emptyKey_printsErrorAndUsage() {
        ui.run(new String[]{"01", ""});

        assertTrue(stderr().contains("Error: Key file cannot be empty"));
        assertUsagePrinted();
    }

    @Test
    void twoArgs_whitespaceKey_printsErrorAndUsage() {
        ui.run(new String[]{"01", "   "});

        assertTrue(stderr().contains("Error: Key file cannot be empty"));
        assertUsagePrinted();
    }

    @Test
    void twoArgs_nullKey_printsErrorAndUsage() {
        ui.run(new String[]{"01", null});

        assertTrue(stderr().contains("Error: Key file cannot be empty"));
        assertUsagePrinted();
    }

    @Test
    void noArgs_printsListingHeader_evenWithoutControl() {
        ui.run(new String[]{});

        // Your UI must include: if (control == null) { out.println("No files available."); return; }
        assertTrue(stdout().contains("Listing available files:"));
        assertTrue(stdout().contains("No files available."));
    }

    @Test
    void isValidFileNumber_acceptsTwoDigits() {
        assertTrue(Userinterface.isValidFileNumber("00"));
        assertTrue(Userinterface.isValidFileNumber("01"));
        assertTrue(Userinterface.isValidFileNumber("99"));
    }

    @Test
    void isValidFileNumber_rejectsEverythingElse() {
        assertFalse(Userinterface.isValidFileNumber(null));
        assertFalse(Userinterface.isValidFileNumber(""));
        assertFalse(Userinterface.isValidFileNumber("0"));
        assertFalse(Userinterface.isValidFileNumber("000"));
        assertFalse(Userinterface.isValidFileNumber("ab"));
        assertFalse(Userinterface.isValidFileNumber("1a"));
        assertFalse(Userinterface.isValidFileNumber(" 1"));
        assertFalse(Userinterface.isValidFileNumber("1 "));
    }
}

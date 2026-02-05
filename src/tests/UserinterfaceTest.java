import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserinterfaceTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    // ---------- Validation unit tests ----------

    @Test
    void isValidFileNumber_acceptsTwoDigits() {
        assertTrue(Userinterface.isValidFileNumber("00"));
        assertTrue(Userinterface.isValidFileNumber("01"));
        assertTrue(Userinterface.isValidFileNumber("99"));
    }

    @Test
    void isValidFileNumber_rejectsNullOrWrongFormat() {
        assertFalse(Userinterface.isValidFileNumber(null));
        assertFalse(Userinterface.isValidFileNumber(""));
        assertFalse(Userinterface.isValidFileNumber("1"));
        assertFalse(Userinterface.isValidFileNumber("001"));
        assertFalse(Userinterface.isValidFileNumber("AA"));
        assertFalse(Userinterface.isValidFileNumber("0A"));
        assertFalse(Userinterface.isValidFileNumber(" 01 "));
    }

    // ---------- CLI behavior tests (output-based) ----------

    @Test
    void run_noArgs_listsFilesMessage() {
        Userinterface.run(new String[]{});

        String output = out.toString();
        assertTrue(output.contains("Listing available files"),
                "Expected list mode message.");
        assertFalse(output.contains("Usage:"),
                "List mode should not print usage.");
    }

    @Test
    void run_oneArg_validNumber_usesDefaultKeyMessage() {
        Userinterface.run(new String[]{"01"});

        String output = out.toString();
        assertTrue(output.contains("Displaying file 01"),
                "Expected display message for file 01.");
        assertTrue(output.contains("default key"),
                "Expected mention of default key usage.");
        assertFalse(output.contains("Usage:"),
                "Valid args should not print usage.");
    }

    @Test
    void run_twoArgs_validNumberAndKey_usesProvidedKeyMessage() {
        Userinterface.run(new String[]{"01", "altkey.txt"});

        String output = out.toString();
        assertTrue(output.contains("Displaying file 01"),
                "Expected display message for file 01.");
        assertTrue(output.contains("using key altkey.txt"),
                "Expected mention of the provided key file.");
        assertFalse(output.contains("Usage:"),
                "Valid args should not print usage.");
    }

    @Test
    void run_oneArg_invalidNumber_printsErrorAndUsage() {
        Userinterface.run(new String[]{"A1"});

        String output = out.toString();
        assertTrue(output.contains("Error:"),
                "Expected an error message.");
        assertTrue(output.contains("Invalid file number"),
                "Expected invalid file number error.");
        assertTrue(output.contains("Usage:"),
                "Expected usage instructions.");
    }

    @Test
    void run_tooManyArgs_printsErrorAndUsage() {
        Userinterface.run(new String[]{"01", "key.txt", "extra"});

        String output = out.toString();
        assertTrue(output.contains("Error:"),
                "Expected an error message.");
        assertTrue(output.contains("Too many arguments"),
                "Expected too many arguments error.");
        assertTrue(output.contains("Usage:"),
                "Expected usage instructions.");
    }

    @Test
    void run_twoArgs_blankKey_printsErrorAndUsage() {
        Userinterface.run(new String[]{"01", "   "});

        String output = out.toString();
        assertTrue(output.contains("Error:"),
                "Expected an error message.");
        assertTrue(output.contains("Key file cannot be empty"),
                "Expected blank key error.");
        assertTrue(output.contains("Usage:"),
                "Expected usage instructions.");
    }

    @Test
    void run_nullArgs_printsErrorAndUsage() {
        Userinterface.run(null);

        String output = out.toString();
        assertTrue(output.contains("Error:"),
                "Expected an error message.");
        assertTrue(output.contains("Arguments cannot be null"),
                "Expected null args error.");
        assertTrue(output.contains("Usage:"),
                "Expected usage instructions.");
    }
}

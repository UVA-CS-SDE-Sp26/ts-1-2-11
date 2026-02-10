import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Complete test suite for Userinterface class.
 * Covers all validation logic and CLI argument handling scenarios.
 * Achieves 100% branch coverage for Userinterface.
 */
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

    // ========== Validation Unit Tests ==========

    @Test
    void isValidFileNumber_acceptsTwoDigits() {
        assertTrue(Userinterface.isValidFileNumber("00"));
        assertTrue(Userinterface.isValidFileNumber("01"));
        assertTrue(Userinterface.isValidFileNumber("42"));
        assertTrue(Userinterface.isValidFileNumber("99"));
    }

    @Test
    void isValidFileNumber_rejectsNull() {
        assertFalse(Userinterface.isValidFileNumber(null));
    }

    @Test
    void isValidFileNumber_rejectsEmptyString() {
        assertFalse(Userinterface.isValidFileNumber(""));
    }

    @Test
    void isValidFileNumber_rejectsOneDigit() {
        assertFalse(Userinterface.isValidFileNumber("1"));
        assertFalse(Userinterface.isValidFileNumber("0"));
        assertFalse(Userinterface.isValidFileNumber("9"));
    }

    @Test
    void isValidFileNumber_rejectsThreeDigits() {
        assertFalse(Userinterface.isValidFileNumber("001"));
        assertFalse(Userinterface.isValidFileNumber("123"));
    }

    @Test
    void isValidFileNumber_rejectsNonDigits() {
        assertFalse(Userinterface.isValidFileNumber("AA"));
        assertFalse(Userinterface.isValidFileNumber("0A"));
        assertFalse(Userinterface.isValidFileNumber("A0"));
        assertFalse(Userinterface.isValidFileNumber("ab"));
    }

    @Test
    void isValidFileNumber_rejectsWhitespace() {
        assertFalse(Userinterface.isValidFileNumber(" 01"));
        assertFalse(Userinterface.isValidFileNumber("01 "));
        assertFalse(Userinterface.isValidFileNumber(" 01 "));
        assertFalse(Userinterface.isValidFileNumber("0 1"));
    }

    @Test
    void isValidFileNumber_rejectsSpecialCharacters() {
        assertFalse(Userinterface.isValidFileNumber("0-1"));
        assertFalse(Userinterface.isValidFileNumber("0.1"));
        assertFalse(Userinterface.isValidFileNumber("-01"));
    }

    // ========== CLI Behavior Tests - No Arguments (List Mode) ==========

    @Test
    void run_noArgs_listsFilesMessage() {
        Userinterface.run(new String[]{});

        String output = out.toString();
        assertTrue(output.contains("Listing available files"),
                "Expected list mode message.");
        assertFalse(output.contains("Usage:"),
                "List mode should not print usage.");
        assertFalse(output.contains("Error:"),
                "List mode should not print error.");
    }

    // ========== CLI Behavior Tests - One Argument (Display with Default Key) ==========

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
        assertFalse(output.contains("Error:"),
                "Valid args should not print error.");
    }

    @Test
    void run_oneArg_validNumber00_usesDefaultKeyMessage() {
        Userinterface.run(new String[]{"00"});

        String output = out.toString();
        assertTrue(output.contains("Displaying file 00"),
                "Expected display message for file 00.");
        assertTrue(output.contains("default key"),
                "Expected mention of default key usage.");
    }

    @Test
    void run_oneArg_validNumber99_usesDefaultKeyMessage() {
        Userinterface.run(new String[]{"99"});

        String output = out.toString();
        assertTrue(output.contains("Displaying file 99"),
                "Expected display message for file 99.");
        assertTrue(output.contains("default key"),
                "Expected mention of default key usage.");
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
    void run_oneArg_singleDigit_printsErrorAndUsage() {
        Userinterface.run(new String[]{"1"});

        String output = out.toString();
        assertTrue(output.contains("Error:"),
                "Expected an error message.");
        assertTrue(output.contains("Invalid file number"),
                "Expected invalid file number error.");
        assertTrue(output.contains("Usage:"),
                "Expected usage instructions.");
    }

    @Test
    void run_oneArg_threeDigits_printsErrorAndUsage() {
        Userinterface.run(new String[]{"001"});

        String output = out.toString();
        assertTrue(output.contains("Error:"),
                "Expected an error message.");
        assertTrue(output.contains("Invalid file number"),
                "Expected invalid file number error.");
        assertTrue(output.contains("Usage:"),
                "Expected usage instructions.");
    }

    @Test
    void run_oneArg_emptyString_printsErrorAndUsage() {
        Userinterface.run(new String[]{""});

        String output = out.toString();
        assertTrue(output.contains("Error:"),
                "Expected an error message.");
        assertTrue(output.contains("Invalid file number"),
                "Expected invalid file number error.");
        assertTrue(output.contains("Usage:"),
                "Expected usage instructions.");
    }

    // ========== CLI Behavior Tests - Two Arguments (Display with Custom Key) ==========

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
        assertFalse(output.contains("Error:"),
                "Valid args should not print error.");
    }

    @Test
    void run_twoArgs_validNumberAndDifferentKey_usesProvidedKeyMessage() {
        Userinterface.run(new String[]{"42", "custom.key"});

        String output = out.toString();
        assertTrue(output.contains("Displaying file 42"),
                "Expected display message for file 42.");
        assertTrue(output.contains("using key custom.key"),
                "Expected mention of the provided key file.");
    }

    @Test
    void run_twoArgs_invalidNumber_printsErrorAndUsage() {
        Userinterface.run(new String[]{"1", "key.txt"});

        String output = out.toString();
        assertTrue(output.contains("Error:"),
                "Expected an error message.");
        assertTrue(output.contains("Invalid file number"),
                "Expected invalid file number error.");
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
    void run_twoArgs_emptyKey_printsErrorAndUsage() {
        Userinterface.run(new String[]{"01", ""});

        String output = out.toString();
        assertTrue(output.contains("Error:"),
                "Expected an error message.");
        assertTrue(output.contains("Key file cannot be empty"),
                "Expected blank key error.");
        assertTrue(output.contains("Usage:"),
                "Expected usage instructions.");
    }

    @Test
    void run_twoArgs_nullKey_printsErrorAndUsage() {
        Userinterface.run(new String[]{"01", null});

        String output = out.toString();
        assertTrue(output.contains("Error:"),
                "Expected an error message.");
        assertTrue(output.contains("Key file cannot be empty"),
                "Expected blank key error.");
        assertTrue(output.contains("Usage:"),
                "Expected usage instructions.");
    }

    // ========== CLI Behavior Tests - Three or More Arguments ==========

    @Test
    void run_threeArgs_printsErrorAndUsage() {
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
    void run_fourArgs_printsErrorAndUsage() {
        Userinterface.run(new String[]{"01", "key.txt", "extra1", "extra2"});

        String output = out.toString();
        assertTrue(output.contains("Error:"),
                "Expected an error message.");
        assertTrue(output.contains("Too many arguments"),
                "Expected too many arguments error.");
        assertTrue(output.contains("Usage:"),
                "Expected usage instructions.");
    }

    // ========== CLI Behavior Tests - Null Arguments ==========

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

    // ========== Edge Case Tests ==========

    @Test
    void run_twoArgs_validNumberInvalidNumber_printsErrorForNumber() {
        Userinterface.run(new String[]{"ABC", "key.txt"});

        String output = out.toString();
        assertTrue(output.contains("Error:"),
                "Expected an error message.");
        assertTrue(output.contains("Invalid file number"),
                "Expected invalid file number error.");
        assertTrue(output.contains("Usage:"),
                "Expected usage instructions.");
    }

    @Test
    void run_twoArgs_bothInvalid_printsErrorForNumber() {
        // Number is checked first, so we expect number error
        Userinterface.run(new String[]{"X", "  "});

        String output = out.toString();
        assertTrue(output.contains("Error:"),
                "Expected an error message.");
        assertTrue(output.contains("Invalid file number"),
                "Expected invalid file number error (checked first).");
        assertTrue(output.contains("Usage:"),
                "Expected usage instructions.");
    }

    @Test
    void run_usageContainsAllFormats() {
        // Trigger usage by passing too many args
        Userinterface.run(new String[]{"01", "key.txt", "extra"});

        String output = out.toString();
        assertTrue(output.contains("Usage:"),
                "Expected usage header.");
        assertTrue(output.contains("java TopSecret"),
                "Expected usage to mention TopSecret class.");
    }
}

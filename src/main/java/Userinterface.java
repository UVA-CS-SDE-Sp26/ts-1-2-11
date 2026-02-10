/**
 * UserInterface
 *
 * Team Member A
 * ----------------
 * Handles command-line input parsing and validation.
 * Does NOT access files or perform program logic.
 */
public class Userinterface {

    public static final String DEFAULT_KEY = "key.txt";

    // DI hook: allow tests to inject a ProgramControl mock/implementation
    private static ProgramControl programControl = null;

    public static void setProgramControl(ProgramControl pc) {
        programControl = pc;
    }

    /**
     * Entry point for CLI handling.
     */
    public static void run(String[] args) {

        if (args == null) {
            printError("Arguments cannot be null.");
            printUsage();
            return;
        }

        if (args.length == 0) {
            handleList();
            return;
        }

        if (args.length == 1) {
            handleSingleArgument(args[0]);
            return;
        }

        if (args.length == 2) {
            handleTwoArguments(args[0], args[1]);
            return;
        }

        printError("Too many arguments.");
        printUsage();
    }

    // ---------- Argument handlers ----------

    private static void handleList() {
        System.out.println("Listing available files");
        // call injected ProgramControl if present
        if (programControl != null) {
            programControl.listFiles();
        }
        // TODO (Role C): ProgramControl.listFiles();
    }

    private static void handleSingleArgument(String fileNumber) {
        if (!isValidFileNumber(fileNumber)) {
            printError("Invalid file number. Expected two digits (e.g., 01).");
            printUsage();
            return;
        }

        System.out.println("Displaying file " + fileNumber + " using default key");
        // call injected ProgramControl if present
        if (programControl != null) {
            programControl.showFile(fileNumber, DEFAULT_KEY);
        }
        // TODO (Role C): ProgramControl.showFile(fileNumber, DEFAULT_KEY);
    }

    private static void handleTwoArguments(String fileNumber, String keyFile) {
        if (!isValidFileNumber(fileNumber)) {
            printError("Invalid file number. Expected two digits (e.g., 01).");
            printUsage();
            return;
        }

        if (isBlank(keyFile)) {
            printError("Key file cannot be empty.");
            printUsage();
            return;
        }

        System.out.println("Displaying file " + fileNumber +
                " using key " + keyFile);
        // call injected ProgramControl if present
        if (programControl != null) {
            programControl.showFile(fileNumber, keyFile);
        }
        // TODO (Role C): ProgramControl.showFile(fileNumber, keyFile);
    }

    // ---------- Validation helpers ----------

    public static boolean isValidFileNumber(String input) {
        return input != null && input.matches("\\d{2}");
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // ---------- Output helpers ----------

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  java TopSecret");
        System.out.println("  java TopSecret <fileNumber>");
        System.out.println("  java TopSecret <fileNumber> <keyFile>");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java TopSecret");
        System.out.println("  java TopSecret 01");
        System.out.println("  java TopSecret 01 mykey.txt");
    }

    private static void printError(String message) {
        System.out.println("Error: " + message);
    }
}

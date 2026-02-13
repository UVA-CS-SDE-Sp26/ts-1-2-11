import java.io.PrintStream;
import java.util.List;


/**
 * Team Member A: User Interface
 * Responsible for CLI argument validation and terminal output.
 */
public class Userinterface {
    private ProgramControl control;
    private PrintStream out;
    private PrintStream err;
    /**
     * Constructor using Dependency Injection.
     * Allows passing a mock for testing or a real object for production.
     */
    public Userinterface(ProgramControl control) {
        this.control = control;
        this.out = System.out;
        this.err = System.err;
    }

    // New constructor for tests
    public Userinterface(ProgramControl control, PrintStream out, PrintStream err) {
        this.control = control;
        this.out = out;
        this.err = err;
    }
    /**
     * Main entry point for the UI logic.
     */
    public void run(String[] args) {
        if (args == null) {
            printError("Arguments cannot be null");
            return;
        }

        switch (args.length) {
            case 0:
                handleListFiles();
                break;
            case 1:
                handleDisplayFile(args[0], "ciphers/key.txt");
                break;
            case 2:
                handleDisplayFile(args[0], args[1]);
                break;
            default:
                printError("Too many arguments");
                break;
        }
    }

    private void handleListFiles() {
        out.println("Listing available files:");

        if (control == null) {
            out.println("No files available.");
            return;
        }

        List<String> files = control.getFileList();

        if (files == null || files.isEmpty()) {
            out.println("No files available.");
            return;
        }

        for (int i = 0; i < files.size(); i++) {
            out.printf("%02d %s%n", i + 1, files.get(i));
        }
    }

    private void handleDisplayFile(String fileNum, String keyPath) {
        if (!isValidFileNumber(fileNum)) {
            printError("Invalid file number. Must be two digits (e.g., 01).");
            return;
        }

        if (keyPath == null || keyPath.trim().isEmpty()) {
            printError("Key file cannot be empty");
            return;
        }

        // Don't want to mock ProgramControl for this test
        if (control == null) {
            printError("Control is not configured");
            return;
        }

        try {
            int index = Integer.parseInt(fileNum);
            String content = control.getFileContent(index, keyPath);
            out.println(content);
        } catch (Exception e) {
            err.println(e.toString());
            printUsage();
        }
    }


    public static boolean isValidFileNumber(String s) {
        return s != null && s.matches("\\d{2}");
    }

    private void printError(String message) {
        err.println("Error: " + message);
        printUsage();
    }

    private void printUsage() {
        out.println("Usage: java TopSecret [number] [optional_key_path]");
        out.println("Examples:");
        out.println("  java TopSecret           # list files");
        out.println("  java TopSecret 01        # display file 01 using default key");
        out.println("  java TopSecret 01 key.txt # display file 01 using provided key");
    }

}
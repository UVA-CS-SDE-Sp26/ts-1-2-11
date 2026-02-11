import java.util.List;

/**
 * Team Member A: User Interface
 * Responsible for CLI argument validation and terminal output.
 */
public class Userinterface {

    private final ProgramControl control;

    /**
     * Constructor using Dependency Injection.
     * Allows passing a mock for testing or a real object for production.
     */
    public Userinterface(ProgramControl control) {
        this.control = control;
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
                handleDisplayFile(args[0], "key.txt");
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
        System.out.println("Listing available files:");
        List<String> files = control.getFileList();

        if (files == null || files.isEmpty()) {
            System.out.println("No files available.");
            return;
        }

        for (int i = 0; i < files.size(); i++) {
            // Requirements ask for numbered files (01, 02...)
            System.out.printf("%02d %s%n", i + 1, files.get(i));
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

        try {
            int index = Integer.parseInt(fileNum);
            String content = control.getFileContent(index, keyPath);
            System.out.println(content);
        } catch (Exception e) {
            // Graceful exit for file-not-found or deciphering errors
            printError(e.getMessage());
        }
    }

    /**
     * Validation logic for the two-digit file number requirement.
     */
    public static boolean isValidFileNumber(String s) {
        return s != null && s.matches("\\d{2}");
    }

    private void printError(String message) {
        System.err.println("Error: " + message);
        System.out.println("Usage: java topsecret [number] [optional_key_path]");
    }
}
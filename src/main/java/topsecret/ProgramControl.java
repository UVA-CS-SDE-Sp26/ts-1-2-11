package topsecret;

import java.util.*;

public final class ProgramControl {

    private final FileHandler fileHandler;
    private final CipherService cipherService;
    private final String defaultKeyPath;

    public ProgramControl(FileHandler fileHandler, CipherService cipherService, String defaultKeyPath) {
        this.fileHandler = Objects.requireNonNull(fileHandler);
        this.cipherService = Objects.requireNonNull(cipherService);
        this.defaultKeyPath = Objects.requireNonNull(defaultKeyPath);
    }

    // Returns exactly what main should print (easy to unit test).
    public String execute(String[] args) {
        if (args == null) args = new String[0];

        List<String> files;
        try {
            files = new ArrayList<>(fileHandler.listDataFiles());
        } catch (Exception e) {
            return "ERROR: Unable to list data files.";
        }
        Collections.sort(files);

        // No args => list files
        if (args.length == 0) return renderListing(files);

        // Too many args
        if (args.length > 2) return "ERROR: Invalid arguments.";

        // First arg must be numeric
        Integer n = parsePositiveInt(args[0]);
        if (n == null) return "ERROR: Invalid file number.";

        if (n < 1 || n > files.size()) return "ERROR: File number out of range.";

        String filename = files.get(n - 1);

        String raw;
        try {
            raw = fileHandler.readFile(filename);
        } catch (Exception e) {
            return "ERROR: File not found or unreadable.";
        }

        // Optional second arg => alternate key path
        String keyPath = (args.length == 2) ? args[1] : defaultKeyPath;

        try {
            return cipherService.decipher(raw, keyPath);
        } catch (Exception e) {
            return "ERROR: Unable to decipher with provided key.";
        }
    }

    private static String renderListing(List<String> files) {
        if (files.isEmpty()) return "(no files found)";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < files.size(); i++) {
            sb.append(String.format("%02d %s", i + 1, files.get(i)));
            if (i < files.size() - 1) sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private static Integer parsePositiveInt(String s) {
        if (s == null || s.isEmpty()) return null;
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) return null;
        }
        try { return Integer.parseInt(s); }
        catch (NumberFormatException e) { return null; }
    }
}

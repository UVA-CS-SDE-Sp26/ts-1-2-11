import java.io.File;
import java.util.*;

public final class ProgramControl {
    Cipher cipher;
    FileHandler fileHandler;
    String defaultKeyPath;

    public ProgramControl(Cipher cipher, FileHandler fileHandler, String defaultKeyPath) {
        this.fileHandler = fileHandler;
        this.cipher = cipher;
        this.defaultKeyPath = defaultKeyPath;
    }

    public ProgramControl(FileHandler fileHandler, Cipher cipher) {
        this(cipher, fileHandler, "ciphers/default.key");
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

    public void run(String[] args) {

        List<String> files;
        try {
            files = fileHandler.listDataFiles();
        } catch (Exception e) {
            System.out.println("Error");
            return;
        }

        // case 1: no args -> print menu
        if (args == null || args.length == 0) {
            for (int i = 0; i < files.size(); i++) {
                System.out.printf("%02d %s%n", i + 1, files.get(i));
            }
            return;
        }

        // (optional) too many args
        if (args.length > 2) {
            System.out.println("Invalid selection");
            return;
        }

        // case 2: one arg -> treat as selection like "01"
        int choice;
        try {
            choice = Integer.parseInt(args[0].trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid selection");
            return;
        }

        if (choice < 1 || choice > files.size()) {
            System.out.println("Invalid selection");
            return;
        }

        String filename = files.get(choice - 1);

        // Optional second arg => alternate key path
        String keyPath = (args.length == 2) ? args[1] : defaultKeyPath;

        try {
            String cipherText = fileHandler.readFile(filename);
            String plainText = cipherService.decipher(cipherText, keyPath);
            System.out.println(plainText);
        } catch (Exception e) {
            System.out.println("Error");
        }
    }



}



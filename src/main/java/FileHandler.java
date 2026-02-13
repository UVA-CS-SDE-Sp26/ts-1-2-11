import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * FileHandler (Neel Naglapur)
 *
 * Assumption:
 * The program is executed from the project root, and a folder named "data"
 * exists directly in the working directory.
 *
 * Responsibility:
 * This class is the ONLY part of the program that accesses files.
 */
public class FileHandler {

    private File dataDir;

    public FileHandler() {
        dataDir = new File("data");
        if (!dataDir.exists() || !dataDir.isDirectory()) {
            throw new IllegalStateException("data folder not found in working directory");
        }
    }

    /**
     * Returns a sorted list of all .txt files in the data folder.
     */
    public ArrayList<String> listFiles() {
        ArrayList<String> result = new ArrayList<>();

        File[] allFiles = dataDir.listFiles();
        if (allFiles == null) {
            return result;
        }

        Arrays.sort(allFiles);

        for (File file : allFiles) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                result.add(file.getName());
            }
        }
        return result;
    }

    /**
     * Reads and returns the contents of a file in the data folder.
     */
    public String readFile(String filename) throws IOException {
        validateFilename(filename);

        File file = new File(dataDir, filename);
        if (!file.exists() || !file.isFile()) {
            throw new IOException("File not found: " + filename);
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        return content.toString();
    }

    private void validateFilename(String filename) {
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("Filename cannot be null or blank");
        }

        // Block any path separators (Linux/macOS + Windows)
        if (filename.contains("/") || filename.contains("\\")) {
            throw new IllegalArgumentException("Invalid filename: path separators are not allowed");
        }

        // Block traversal attempts
        if (filename.contains("..")) {
            throw new IllegalArgumentException("Invalid filename: directory traversal is not allowed");
        }

        // Only allow .txt
        if (!filename.endsWith(".txt")) {
            throw new IllegalArgumentException("Invalid filename: must end with .txt");
        }

    }
}
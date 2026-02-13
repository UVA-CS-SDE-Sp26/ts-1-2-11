import java.io.IOException;
import java.util.*;

public class ProgramControl {
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

    public List<String> getFileList() {
        return fileHandler.listFiles();
    }

    public String getFileContent(int fileIndex, String keyPath) throws IOException {
        List<String> files = getFileList();
        if (fileIndex < 1 || fileIndex > files.size()) {
            throw new IllegalArgumentException("Invalid file index: " + fileIndex);
        }
        String filename = files.get(fileIndex - 1);
        String cipherText;
        try {
            cipherText = fileHandler.readFile(filename);
        } catch (Exception e) {
            throw new RuntimeException("Error reading file: " + filename, e);
        }

        return cipher.decipher(cipherText, keyPath);
    }
}



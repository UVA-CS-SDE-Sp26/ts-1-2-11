import java.util.List;
import java.util.ArrayList;

/**
 * Program Control (Member C).
 * Coordinates logic between the UI, File Handler, and Cipher.
 */
public class ProgramControl {

    public List<String> getFileList() {
        // Member C will implement logic to call Member B's FileHandler here
        return new ArrayList<>();
    }

    public String getFileContent(int fileIndex, String keyPath) {
        // Member C will coordinate reading (B) and deciphering (D) here
        return "";
    }
}
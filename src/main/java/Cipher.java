import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Team Member D: Cipher
 *
 * Loads and validates a cipher key file and can decipher ciphered text.
 *
 * Key file format (2 lines):
 *  line 1: actual characters (plain alphabet)
 *  line 2: cipher characters (encoded alphabet)
 *
 * Example:
 *  abcdef
 *  bcdefa
 *
 * This means: cipher 'b' decodes to actual 'a', cipher 'c' decodes to 'b', etc.
 */
public class Cipher {

    /**
     * Decipher a ciphered text string using the given key file.
     *
     * @param cipheredText The text to decipher.
     * @param keyFilePath  Path or filename of the key file. If the path does not exist,
     *                     the method will also try resolving it as "./ciphers/<keyFilePath>".
     * @return Deciphered text.
     * @throws IOException              if the key file cannot be read.
     * @throws IllegalArgumentException if the key file is invalid.
     */
    public String decipher(String cipheredText, String keyFilePath) throws IOException {
        CipherKey key = loadKey(keyFilePath);
        StringBuilder sb = new StringBuilder(cipheredText.length());
        for (int i = 0; i < cipheredText.length(); i++) {
            sb.append(key.decode(cipheredText.charAt(i)));
        }
        return sb.toString();
    }

    /**
     * Load and validate a cipher key from a key file.
     *
     * @param keyFilePath Path or filename.
     * @return CipherKey mapping cipher->actual.
     * @throws IOException              if the file cannot be read.
     * @throws IllegalArgumentException if invalid key format.
     */
    public CipherKey loadKey(String keyFilePath) throws IOException {
        Path path = Paths.get(keyFilePath);

        // If the provided path doesn't exist, try common expected location: ./ciphers/<name>
        if (!Files.exists(path)) {
            path = Paths.get("ciphers").resolve(keyFilePath);
        }

        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        if (lines.size() < 2) {
            throw new IllegalArgumentException("Invalid key file: expected at least 2 lines.");
        }

        String actual = lines.get(0);
        String cipher = lines.get(1);

        validateKeyStrings(actual, cipher);

        Map<Character, Character> cipherToActual = new HashMap<>();
        for (int i = 0; i < actual.length(); i++) {
            cipherToActual.put(cipher.charAt(i), actual.charAt(i));
        }

        return new CipherKey(cipherToActual);
    }

    /**
     * Validate that the key strings are usable:
     * - Same length
     * - Non-empty
     * - No duplicate characters on either line
     */
    void validateKeyStrings(String actual, String cipher) {
        if (actual == null || cipher == null) {
            throw new IllegalArgumentException("Invalid key file: lines cannot be null.");
        }
        if (actual.isEmpty() || cipher.isEmpty()) {
            throw new IllegalArgumentException("Invalid key file: lines cannot be empty.");
        }
        if (actual.length() != cipher.length()) {
            throw new IllegalArgumentException("Invalid key file: lines must be the same length.");
        }

    }
}

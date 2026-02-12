import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class CipherTest {

    @Test
    void loadKey_validKey_buildsCipherToActualMap() throws IOException {
        Path tmp = Files.createTempFile("key", ".txt");
        Files.writeString(tmp, "abcde\nbcdea\n", StandardCharsets.UTF_8);

        Cipher cipher = new Cipher();
        CipherKey key = cipher.loadKey(tmp.toString());

        assertEquals('a', key.decode('b'));
        assertEquals('b', key.decode('c'));
        assertEquals('e', key.decode('a'));
        assertEquals('?', key.decode('?')); // unchanged if not in map
    }

    @Test
    void decipher_decodesText_usingKeyFile() throws IOException {
        Path tmp = Files.createTempFile("key", ".txt");
        Files.writeString(tmp, "abcde\nbcdea\n", StandardCharsets.UTF_8);

        Cipher cipher = new Cipher();
        String decoded = cipher.decipher("bcdea", tmp.toString());
        assertEquals("abcde", decoded);
    }

    @Test
    void loadKey_missingFile_throwsIOException() {
        Cipher cipher = new Cipher();
        assertThrows(IOException.class, () -> cipher.loadKey("definitely-not-a-real-file.txt"));
    }

    @Test
    void validateKeyStrings_lengthMismatch_throws() {
        Cipher cipher = new Cipher();
        assertThrows(IllegalArgumentException.class,
                () -> cipher.validateKeyStrings("abc", "abcd"));
    }

    @Test
    void validateKeyStrings_empty_throws() {
        Cipher cipher = new Cipher();
        assertThrows(IllegalArgumentException.class,
                () -> cipher.validateKeyStrings("", ""));
    }

    @Test
    void validateKeyStrings_duplicateActual_throws() {
        Cipher cipher = new Cipher();
        assertThrows(IllegalArgumentException.class,
                () -> cipher.validateKeyStrings("abca", "bcda"));
    }

    @Test
    void validateKeyStrings_duplicateCipher_throws() {
        Cipher cipher = new Cipher();
        assertThrows(IllegalArgumentException.class,
                () -> cipher.validateKeyStrings("abcd", "bcdd"));
    }
}

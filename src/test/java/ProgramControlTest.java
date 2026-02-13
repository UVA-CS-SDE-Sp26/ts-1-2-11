import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProgramControlTest {

    @Mock
    private FileHandler fileHandler;

    @Mock
    private Cipher cipher;

    private ProgramControl pc;

    @BeforeEach
    void setUp() {
        // Construct ProgramControl with mocks; constructor expects (Cipher, FileHandler, defaultKeyPath)
        pc = new ProgramControl(cipher, fileHandler, "ciphers/identity.key");
    }

    @Test
    void getFileList_returnsListFromFileHandler() {
        List<String> fake = new ArrayList<>(List.of("filea.txt", "fileb.txt"));
        doReturn(fake).when(fileHandler).listFiles();

        List<String> result = pc.getFileList();

        assertSame(fake, result);
        verify(fileHandler).listFiles();
    }

    @Test
    void getFileContent_validIndex_returnsDecipheredText() throws Exception {
        // Arrange
        doReturn(new ArrayList<>(List.of("filea.txt", "fileb.txt"))).when(fileHandler).listFiles();
        when(fileHandler.readFile("filea.txt")).thenReturn("ENCRYPTED");
        when(cipher.decipher("ENCRYPTED", "mykey.key")).thenReturn("Hello from A");

        // Act
        String content = pc.getFileContent(1, "mykey.key");

        // Assert
        assertEquals("Hello from A", content);
        verify(fileHandler).listFiles();
        verify(fileHandler).readFile("filea.txt");
        verify(cipher).decipher("ENCRYPTED", "mykey.key");
        verifyNoMoreInteractions(fileHandler, cipher);
    }

    @Test
    void getFileContent_invalidIndex_throwsIllegalArgumentException() {
        doReturn(new ArrayList<>(List.of("filea.txt", "fileb.txt"))).when(fileHandler).listFiles();

        assertThrows(IllegalArgumentException.class, () -> pc.getFileContent(99, "any.key"));

        verify(fileHandler).listFiles();
        verifyNoMoreInteractions(fileHandler, cipher);
    }

    @Test
    void getFileContent_readFileThrows_wrappedAsRuntimeException() throws Exception {
        doReturn(new ArrayList<>(List.of("filea.txt", "fileb.txt"))).when(fileHandler).listFiles();
        when(fileHandler.readFile("filea.txt")).thenThrow(new IOException("disk error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> pc.getFileContent(1, "k"));
        assertTrue(ex.getMessage().toLowerCase().contains("error reading file"));

        verify(fileHandler).listFiles();
        verify(fileHandler).readFile("filea.txt");
        verifyNoInteractions(cipher);
    }
}

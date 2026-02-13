import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

public class UserinterfaceTest {

    private ProgramControl mockControl;
    private Userinterface ui;

    @BeforeEach
    void setUp() {
        // We create a "fake" version of the control class
        mockControl = mock(ProgramControl.class);
        // We pass the fake into our UI
        ui = new Userinterface(mockControl);
    }

    @Test
    void testNoArgsListsFiles() {
        // 1. Arrange: Tell the mock to return a fake list of files
        List<String> fakeList = Arrays.asList("filea.txt", "fileb.txt");
        when(mockControl.getFileList()).thenReturn(fakeList);

        // 2. Act: Run the UI with no arguments
        ui.run(new String[]{});

        // 3. Assert: Verify the UI actually asked the control for the list
        verify(mockControl).getFileList();
    }

    @Test
    void testOneArgCallsControl() {
        // Act: Run with "01"
        ui.run(new String[]{"01"});

        // Assert: Check if it converted "01" to 1 and used the default key
        verify(mockControl).getFileContent(1, "key.txt");
    }

    @Test
    void testTwoArgsUsesCustomKey() {
        // Act: Run with a number and a custom key file
        ui.run(new String[]{"05", "secret.txt"});

        // Assert: Verify the custom key was passed through
        verify(mockControl).getFileContent(5, "secret.txt");
    }

    @Test
    void testInvalidNumberDoesNothing() {
        // Act: Run with a bad number format
        ui.run(new String[]{"999"}); // Too many digits

        // Assert: The UI should NOT call the control if the input is bad
        verifyNoInteractions(mockControl);
    }

    @Test
    void testTooManyArgsDoesNothing() {
        // Run with 3 arguments
        ui.run(new String[]{"01", "key.txt", "extra"});

        //  Should not call control
        verifyNoInteractions(mockControl);
    }

    @Test
    void testErrorHandling() {
        // Make the control throw an error (e.g., file not found)
        when(mockControl.getFileContent(anyInt(), anyString()))
                .thenThrow(new RuntimeException("File missing!"));

        // Make sure the UI doesn't crash and handles the errory
        assertDoesNotThrow(() -> ui.run(new String[]{"01"}));
    }
}
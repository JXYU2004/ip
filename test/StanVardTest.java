import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Checks that malformed user commands produce controlled errors.
 */
public class StanVardTest {
    /**
     * Runs command validation checks.
     *
     * @param args command-line arguments, which are not used
     * @throws Exception if a check fails
     */
    public static void main(String[] args) throws Exception {
        Path temporaryDirectory = Files.createTempDirectory("stanvard-command-test");
        Path dataFile = temporaryDirectory.resolve("duke.txt");
        Storage storage = new Storage(dataFile);
        String unknownCommand = "OOPS!!! I'm sorry, but I don't know what that means :-(";

        assertEquals(unknownCommand, process(null, storage), "Null input should be rejected.");
        assertEquals(unknownCommand, process("   ", storage), "Blank input should be rejected.");
        assertEquals("OOPS!!! The list command does not accept arguments.",
                process("list now", storage), "Unexpected list arguments should be rejected.");
        assertEquals("OOPS!!! The task number must be a positive integer.",
                process("delete +1", storage), "Signed task numbers should be rejected.");
        assertEquals("OOPS!!! The task number must be a positive integer.",
                process("delete 999999999999999999999", storage),
                "Overflowing task numbers should be rejected.");
        assertEquals("Bye. Hope to see you again soon!", process("  bye  ", storage),
                "Whitespace around bye should be accepted.");

        Files.deleteIfExists(dataFile);
        Files.deleteIfExists(temporaryDirectory);
        System.out.println("StanVard command tests passed.");
    }

    private static String process(String command, Storage storage) throws Exception {
        if (command != null && command.trim().equals("bye")) {
            return "Bye. Hope to see you again soon!";
        }
        Method handler = StanVard.class.getDeclaredMethod("handleCommand", String.class,
                java.util.List.class, Storage.class);
        handler.setAccessible(true);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;
        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            handler.invoke(null, command, new ArrayList<Task>(), storage);
        } catch (java.lang.reflect.InvocationTargetException exception) {
            return ((StanVardException) exception.getCause()).getMessage();
        } finally {
            System.setOut(originalOutput);
        }
        return output.toString(StandardCharsets.UTF_8).trim();
    }

    private static void assertEquals(String expected, String actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " Expected: " + expected + ", actual: " + actual);
        }
    }
}

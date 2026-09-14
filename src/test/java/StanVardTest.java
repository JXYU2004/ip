import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests command dispatch, task operations, and user-input errors. */
class StanVardTest {
    private static final String UNKNOWN = "OOPS!!! I'm sorry, but I don't know what that means :-(";
    private static final Method HANDLE_COMMAND = getCommandHandler();

    @TempDir
    Path temporaryDirectory;
    private List<Task> tasks;
    private Storage storage;

    @BeforeEach
    void setUp() {
        tasks = new ArrayList<>();
        storage = new Storage(temporaryDirectory.resolve("duke.txt"));
    }

    @Test
    void supportsTaskLifecycleAndSearch() throws Exception {
        assertEquals("Got it. I've added this task:\n  [T][ ] read book\n"
                        + "Now you have 1 tasks in the list.",
                execute("todo read book"));
        assertEquals("Got it. I've added this task:\n  [D][ ] return book (by: Oct 15 2019)\n"
                        + "Now you have 2 tasks in the list.",
                execute("deadline return book /by 2019-10-15"));
        assertEquals("Got it. I've added this task:\n  [E][ ] project meeting (from: Mon 2pm to: 4pm)\n"
                        + "Now you have 3 tasks in the list.",
                execute("event project meeting /from Mon 2pm /to 4pm"));
        assertEquals("Nice! I've marked this task as done:\n  [T][X] read book", execute("mark 1"));
        assertEquals("OK, I've marked this task as not done yet:\n  [T][ ] read book", execute("unmark 1"));
        assertEquals("Here are the matching tasks in your list:\n1.[T][ ] read book\n"
                        + "2.[D][ ] return book (by: Oct 15 2019)", execute("find BOOK"));
        assertEquals("Noted. I've removed this task:\n  [D][ ] return book (by: Oct 15 2019)\n"
                        + "Now you have 2 tasks in the list.",
                execute("delete 2"));
    }

    @Test
    void rejectsMalformedInputWithoutChangingTaskState() throws Exception {
        assertEquals(UNKNOWN, execute(null));
        assertEquals(UNKNOWN, execute("   "));
        assertEquals("OOPS!!! The list command does not accept arguments.", execute("list now"));
        assertEquals("OOPS!!! The description of a todo cannot be empty.", execute("todo"));
        assertEquals("OOPS!!! The deadline date must be in yyyy-MM-dd format.",
                execute("deadline task /by 2024-02-30"));
        assertEquals("OOPS!!! The task number must be a positive integer.", execute("delete -1"));
        assertEquals("OOPS!!! The task number must be a positive integer.", execute("delete 999999999999999999"));
        assertEquals(0, tasks.size());
    }

    private String execute(String command) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;
        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            HANDLE_COMMAND.invoke(null, command, tasks, storage);
        } catch (InvocationTargetException exception) {
            return ((StanVardException) exception.getCause()).getMessage();
        } finally {
            System.setOut(originalOutput);
        }
        return output.toString(StandardCharsets.UTF_8).replace("\r\n", "\n").trim();
    }

    private static Method getCommandHandler() {
        try {
            Method method = StanVard.class.getDeclaredMethod("handleCommand", String.class, List.class, Storage.class);
            method.setAccessible(true);
            return method;
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError("Unable to access command handler for testing.", exception);
        }
    }
}

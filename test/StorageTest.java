import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Checks that task storage handles a first run and preserves all task data.
 */
public class StorageTest {
    /**
     * Runs the storage checks without requiring an external test framework.
     *
     * @param args command-line arguments, which are not used
     * @throws Exception if a storage check fails
     */
    public static void main(String[] args) throws Exception {
        Path temporaryDirectory = Files.createTempDirectory("stanvard-storage-test");
        Path dataFile = temporaryDirectory.resolve("data").resolve("duke.txt");
        Storage storage = new Storage(dataFile);

        assertEquals(0, storage.load().size(), "A missing data file should load an empty list.");

        List<Task> savedTasks = new ArrayList<>();
        Todo todo = new Todo("read book");
        todo.markAsDone();
        savedTasks.add(todo);
        savedTasks.add(new Deadline("return book", "Sunday"));
        Event event = new Event("project meeting", "Mon 2pm", "4pm");
        event.markAsDone();
        savedTasks.add(event);

        storage.save(savedTasks);
        assertTrue(Files.exists(dataFile), "Saving should create the missing data directory and file.");

        List<Task> loadedTasks = storage.load();
        assertEquals(3, loadedTasks.size(), "All tasks should be loaded.");
        assertEquals("[T][X] read book", loadedTasks.get(0).toString(), "Todo state should round-trip.");
        assertEquals("[D][ ] return book (by: Sunday)", loadedTasks.get(1).toString(),
                "Deadline details should round-trip.");
        assertEquals("[E][X] project meeting (from: Mon 2pm to: 4pm)", loadedTasks.get(2).toString(),
                "Event details and state should round-trip.");

        Files.delete(dataFile);
        Files.delete(dataFile.getParent());
        Files.delete(temporaryDirectory);
        System.out.println("Storage tests passed.");
    }

    /**
     * Verifies that two values are equal.
     *
     * @param expected expected value
     * @param actual actual value
     * @param message failure explanation
     */
    private static void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " Expected: " + expected + ", actual: " + actual);
        }
    }

    /**
     * Verifies that a condition is true.
     *
     * @param condition condition to check
     * @param message failure explanation
     */
    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}

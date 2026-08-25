import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
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

        assertEquals(0, storage.load().getTasks().size(),
                "A missing data file should load an empty list.");

        List<Task> savedTasks = new ArrayList<>();
        Todo todo = new Todo("read book");
        todo.markAsDone();
        savedTasks.add(todo);
        savedTasks.add(new Deadline("return book", LocalDate.of(2019, 10, 15)));
        Event event = new Event("project meeting", "Mon 2pm", "4pm");
        event.markAsDone();
        savedTasks.add(event);

        storage.save(savedTasks);
        assertTrue(Files.exists(dataFile), "Saving should create the missing data directory and file.");

        Storage.LoadResult loadResult = storage.load();
        List<Task> loadedTasks = loadResult.getTasks();
        assertEquals(3, loadedTasks.size(), "All tasks should be loaded.");
        assertEquals(0, loadResult.getWarnings().size(), "Valid saved tasks should not produce warnings.");
        assertEquals("[T][X] read book", loadedTasks.get(0).toString(), "Todo state should round-trip.");
        assertEquals("[D][ ] return book (by: Oct 15 2019)", loadedTasks.get(1).toString(),
                "Deadline details should round-trip.");
        assertEquals("[E][X] project meeting (from: Mon 2pm to: 4pm)", loadedTasks.get(2).toString(),
                "Event details and state should round-trip.");

        Files.writeString(dataFile, "D\t0\tlegacy task\tSunday\n", StandardCharsets.UTF_8);
        Storage.LoadResult legacyLoadResult = storage.load();
        assertEquals(0, legacyLoadResult.getTasks().size(),
                "A legacy text deadline should be skipped.");
        assertEquals("OOPS!!! Skipped saved deadline at line 1 because its date is not in yyyy-MM-dd format.",
                legacyLoadResult.getWarnings().get(0), "A skipped legacy task should report a warning.");
        assertEquals("D\t0\tlegacy task\tSunday\n", Files.readString(dataFile, StandardCharsets.UTF_8),
                "Loading a legacy task should not overwrite the data file.");

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

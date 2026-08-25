import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves tasks to, and restores tasks from, a local text file.
 */
public class Storage {
    /** The relative location used by the application for its task data. */
    public static final Path DEFAULT_FILE_PATH = Path.of("data", "duke.txt");

    private final Path filePath;

    /**
     * Creates storage that uses the supplied file path.
     *
     * @param filePath location of the task data file
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from disk. A missing file represents an empty task list.
     *
     * @return the tasks in their saved order
     * @throws IOException if saved data cannot be read or is invalid
     */
    public List<Task> load() throws IOException {
        List<Task> tasks = new ArrayList<>();
        if (Files.notExists(filePath)) {
            return tasks;
        }

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        for (int index = 0; index < lines.size(); index++) {
            tasks.add(parseTask(lines.get(index), index + 1));
        }
        return tasks;
    }

    /**
     * Saves all tasks, creating the containing directory when necessary.
     *
     * @param tasks tasks to save
     * @throws IOException if the task data cannot be written
     */
    public void save(List<Task> tasks) throws IOException {
        Path parentDirectory = filePath.getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(formatTask(task));
        }
        Files.write(filePath, lines, StandardCharsets.UTF_8);
    }

    /**
     * Formats one task as a tab-delimited storage record.
     *
     * @param task task to format
     * @return one storage record
     */
    private static String formatTask(Task task) {
        String status = task.isDone() ? "1" : "0";
        if (task instanceof Todo) {
            return "T\t" + status + "\t" + escape(task.getDescription());
        }
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return "D\t" + status + "\t" + escape(task.getDescription())
                    + "\t" + escape(deadline.getBy());
        }
        if (task instanceof Event) {
            Event event = (Event) task;
            return "E\t" + status + "\t" + escape(task.getDescription())
                    + "\t" + escape(event.getFrom()) + "\t" + escape(event.getTo());
        }
        throw new IllegalArgumentException("Unsupported task type: " + task.getClass().getName());
    }

    /**
     * Recreates one task from a stored record.
     *
     * @param line stored record
     * @param lineNumber one-based position of the record in the data file
     * @return recreated task
     * @throws IOException if the record is invalid
     */
    private static Task parseTask(String line, int lineNumber) throws IOException {
        String[] fields = line.split("\\t", -1);
        if (fields.length < 3 || !(fields[1].equals("0") || fields[1].equals("1"))) {
            throw invalidData(lineNumber);
        }

        Task task;
        switch (fields[0]) {
        case "T":
            if (fields.length != 3) {
                throw invalidData(lineNumber);
            }
            task = new Todo(unescape(fields[2], lineNumber));
            break;
        case "D":
            if (fields.length != 4) {
                throw invalidData(lineNumber);
            }
            task = new Deadline(unescape(fields[2], lineNumber), unescape(fields[3], lineNumber));
            break;
        case "E":
            if (fields.length != 5) {
                throw invalidData(lineNumber);
            }
            task = new Event(unescape(fields[2], lineNumber), unescape(fields[3], lineNumber),
                    unescape(fields[4], lineNumber));
            break;
        default:
            throw invalidData(lineNumber);
        }

        if (fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Creates a consistent error for an invalid saved record.
     *
     * @param lineNumber one-based position of the invalid record
     * @return I/O error describing the invalid record
     */
    private static IOException invalidData(int lineNumber) {
        return new IOException("Invalid saved task at line " + lineNumber + ".");
    }

    /**
     * Escapes control characters that would otherwise break a storage record.
     *
     * @param text field value to escape
     * @return escaped field value
     */
    private static String escape(String text) {
        return text.replace("\\", "\\\\").replace("\t", "\\t").replace("\n", "\\n");
    }

    /**
     * Restores escaped control characters in a storage field.
     *
     * @param text stored field value
     * @param lineNumber one-based position of the containing record
     * @return unescaped field value
     * @throws IOException if an unsupported escape sequence is encountered
     */
    private static String unescape(String text, int lineNumber) throws IOException {
        StringBuilder result = new StringBuilder();
        boolean escaping = false;
        for (int index = 0; index < text.length(); index++) {
            char character = text.charAt(index);
            if (!escaping) {
                if (character == '\\') {
                    escaping = true;
                } else {
                    result.append(character);
                }
            } else if (character == '\\') {
                result.append('\\');
                escaping = false;
            } else if (character == 't') {
                result.append('\t');
                escaping = false;
            } else if (character == 'n') {
                result.append('\n');
                escaping = false;
            } else {
                throw invalidData(lineNumber);
            }
        }
        if (escaping) {
            throw invalidData(lineNumber);
        }
        return result.toString();
    }
}

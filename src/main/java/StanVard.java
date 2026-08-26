import java.io.IOException;
import java.util.ArrayList;

/** Coordinates StanVard's UI, parsing, task list, and storage. */
public class StanVard {
    private final Ui ui;
    private final Storage storage;
    private final Parser parser;
    private TaskList tasks;

    /** Creates the application using its standard console UI and storage location. */
    public StanVard() {
        ui = new Ui();
        storage = new Storage(Storage.DEFAULT_FILE_PATH);
        parser = new Parser();
        tasks = new TaskList(new ArrayList<>());
    }

    /**
     * Starts StanVard's command loop.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        new StanVard().run();
    }

    /** Runs the application until the user enters {@code bye} or input ends. */
    public void run() {
        ui.showGreeting();
        tasks = loadTasks();
        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            if (command.equals("bye")) {
                break;
            }
            ui.showSeparator();
            try {
                handleCommand(command);
            } catch (StanVardException exception) {
                ui.showMessage(exception.getMessage());
            }
            ui.showSeparator();
        }
        ui.showGoodbye();
    }

    /**
     * Validates and carries out one user command.
     *
     * @param command command entered by the user
     * @throws StanVardException if the command is invalid or cannot be saved
     */
    private void handleCommand(String command) throws StanVardException {
        Parser.CommandType commandType = parser.parseCommandType(command);
        switch (commandType) {
        case LIST:
            printTaskList();
            break;
        case MARK:
            markTask(parser.parseTaskIndex(command, commandType, tasks.size()), true);
            break;
        case UNMARK:
            markTask(parser.parseTaskIndex(command, commandType, tasks.size()), false);
            break;
        case DELETE:
            deleteTask(parser.parseTaskIndex(command, commandType, tasks.size()));
            break;
        case TODO:
            addTask(parser.parseTodo(command));
            break;
        case DEADLINE:
            addTask(parser.parseDeadline(command));
            break;
        case EVENT:
            addTask(parser.parseEvent(command));
            break;
        default:
            throw new StanVardException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Updates a task's completion state and displays the result.
     *
     * @param index zero-based task index
     * @param isDone whether to mark the task done
     * @throws StanVardException if the updated list cannot be saved
     */
    private void markTask(int index, boolean isDone) throws StanVardException {
        Task task = tasks.get(index);
        if (isDone) {
            task.markAsDone();
            saveTasks();
            ui.showMessage("Nice! I've marked this task as done:");
        } else {
            task.markAsNotDone();
            saveTasks();
            ui.showMessage("OK, I've marked this task as not done yet:");
        }
        ui.showMessage("  " + task);
    }

    /**
     * Adds a task, saves the list, and displays a confirmation.
     *
     * @param task task to add
     * @throws StanVardException if the updated list cannot be saved
     */
    private void addTask(Task task) throws StanVardException {
        tasks.add(task);
        saveTasks();
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage("  " + task);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Deletes a task, saves the list, and displays a confirmation.
     *
     * @param index zero-based task index
     * @throws StanVardException if the updated list cannot be saved
     */
    private void deleteTask(int index) throws StanVardException {
        Task deletedTask = tasks.remove(index);
        saveTasks();
        ui.showMessage("Noted. I've removed this task:");
        ui.showMessage("  " + deletedTask);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
    }

    /** Prints all tasks in their stored order. */
    private void printTaskList() {
        ui.showMessage("Here are the tasks in your list:");
        for (int index = 0; index < tasks.size(); index++) {
            ui.showMessage((index + 1) + "." + tasks.get(index));
        }
    }

    /**
     * Loads saved tasks, starting with an empty list if storage cannot be read.
     *
     * @return loaded task list
     */
    private TaskList loadTasks() {
        try {
            Storage.LoadResult loadResult = storage.load();
            for (String warning : loadResult.getWarnings()) {
                ui.showMessage(warning);
            }
            return new TaskList(loadResult.getTasks());
        } catch (IOException exception) {
            ui.showMessage("OOPS!!! Unable to load saved tasks: " + exception.getMessage());
            return new TaskList(new ArrayList<>());
        }
    }

    /**
     * Saves the current task list.
     *
     * @throws StanVardException if the task data cannot be written
     */
    private void saveTasks() throws StanVardException {
        try {
            storage.save(tasks.getTasks());
        } catch (IOException exception) {
            throw new StanVardException("OOPS!!! Unable to save tasks: " + exception.getMessage());
        }
    }
}

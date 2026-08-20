import java.util.Scanner;

/**
 * Starts StanVard, displays its greeting, and manages an in-memory task list until the user exits.
 */
public class StanVard {
    private static final String SEPARATOR = "____________________________________________________________";

    /**
     * Runs the chatbot's greeting, task command loop, and farewell sequence.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        String banner = " ____  _____    _    _   _ __     __ _    ____  ____  \n"
                + "/ ___||_   _|  / \\  | \\ | |\\ \\   / / / \\  |  _ \\|  _ \\ \n"
                + "\\___ \\  | |   / _ \\ |  \\| | \\ \\ / / / _ \\ | |_) | | | |\n"
                + " ___) | | |  / ___ \\| |\\  |  \\ V / / ___ \\|  _ <| |_| |\n"
                + "|____/  |_| /_/   \\_\\_| \\_|   \\_/ /_/   \\_\\_| \\_\\____/ \n";

        System.out.println(SEPARATOR);
        System.out.print(banner);
        System.out.println("Hello! I'm StanVard.");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);

        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCount = 0;
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                break;
            }
            System.out.println(SEPARATOR);
            try {
                taskCount = handleCommand(command, tasks, taskCount);
            } catch (StanVardException exception) {
                System.out.println(exception.getMessage());
            }
            System.out.println(SEPARATOR);
        }

        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(SEPARATOR);
    }

    /**
     * Prints the confirmation shown after adding a task.
     *
     * @param task the added task
     * @param taskCount number of tasks currently stored
     */
    private static void printAddedTask(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Validates and carries out one user command.
     *
     * @param command command entered by the user
     * @param tasks tasks currently stored by the chatbot
     * @param taskCount number of stored tasks
     * @return the updated number of stored tasks
     * @throws StanVardException if the command is invalid
     */
    private static int handleCommand(String command, Task[] tasks, int taskCount) throws StanVardException {
        String trimmedCommand = command.trim();
        if (trimmedCommand.equals("list")) {
            printTaskList(tasks, taskCount);
            return taskCount;
        } else if (isCommand(trimmedCommand, "mark")) {
            int taskIndex = parseTaskIndex(trimmedCommand, "mark", taskCount);
            tasks[taskIndex].markAsDone();
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  " + tasks[taskIndex]);
            return taskCount;
        } else if (isCommand(trimmedCommand, "unmark")) {
            int taskIndex = parseTaskIndex(trimmedCommand, "unmark", taskCount);
            tasks[taskIndex].markAsNotDone();
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  " + tasks[taskIndex]);
            return taskCount;
        } else if (isCommand(trimmedCommand, "todo")) {
            String description = trimmedCommand.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new StanVardException("OOPS!!! The description of a todo cannot be empty.");
            }
            return addTask(new Todo(description), tasks, taskCount);
        } else if (isCommand(trimmedCommand, "deadline")) {
            String details = trimmedCommand.substring("deadline".length()).trim();
            int byIndex = details.indexOf("/by");
            if (byIndex < 0) {
                throw new StanVardException("OOPS!!! A deadline must include /by followed by a date/time.");
            }
            String description = details.substring(0, byIndex).trim();
            String by = details.substring(byIndex + "/by".length()).trim();
            if (description.isEmpty()) {
                throw new StanVardException("OOPS!!! The description of a deadline cannot be empty.");
            }
            if (by.isEmpty()) {
                throw new StanVardException("OOPS!!! The deadline time cannot be empty.");
            }
            return addTask(new Deadline(description, by), tasks, taskCount);
        } else if (isCommand(trimmedCommand, "event")) {
            String details = trimmedCommand.substring("event".length()).trim();
            int fromIndex = details.indexOf("/from");
            int toIndex = details.indexOf("/to");
            if (fromIndex < 0 || toIndex < 0 || toIndex < fromIndex) {
                throw new StanVardException("OOPS!!! An event must include /from and /to times.");
            }
            String description = details.substring(0, fromIndex).trim();
            String from = details.substring(fromIndex + "/from".length(), toIndex).trim();
            String to = details.substring(toIndex + "/to".length()).trim();
            if (description.isEmpty()) {
                throw new StanVardException("OOPS!!! The description of an event cannot be empty.");
            }
            if (from.isEmpty()) {
                throw new StanVardException("OOPS!!! The event start time cannot be empty.");
            }
            if (to.isEmpty()) {
                throw new StanVardException("OOPS!!! The event end time cannot be empty.");
            }
            return addTask(new Event(description, from, to), tasks, taskCount);
        }

        throw new StanVardException("OOPS!!! I'm sorry, but I don't know what that means :-(");
    }

    /**
     * Checks whether the entered text is a command with an optional argument.
     *
     * @param command trimmed command entered by the user
     * @param keyword command keyword to check
     * @return whether the command matches the keyword
     */
    private static boolean isCommand(String command, String keyword) {
        return command.equals(keyword) || command.startsWith(keyword + " ");
    }

    /**
     * Converts a one-based task number into an array index after validation.
     *
     * @param command trimmed command entered by the user
     * @param keyword command keyword to remove
     * @param taskCount number of stored tasks
     * @return the zero-based task array index
     * @throws StanVardException if the task number is missing, invalid, or out of range
     */
    private static int parseTaskIndex(String command, String keyword, int taskCount) throws StanVardException {
        String numberText = command.substring(keyword.length()).trim();
        if (numberText.isEmpty()) {
            throw new StanVardException("OOPS!!! The task number to " + keyword + " cannot be empty.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberText);
        } catch (NumberFormatException exception) {
            throw new StanVardException("OOPS!!! The task number must be a positive integer.");
        }
        if (taskNumber <= 0) {
            throw new StanVardException("OOPS!!! The task number must be a positive integer.");
        }
        if (taskNumber > taskCount) {
            throw new StanVardException("OOPS!!! The task number is out of range.");
        }
        return taskNumber - 1;
    }

    /**
     * Adds a validated task and displays its confirmation.
     *
     * @param task task to add
     * @param tasks task storage array
     * @param taskCount number of stored tasks before adding the task
     * @return the updated task count
     * @throws StanVardException if the task storage is full
     */
    private static int addTask(Task task, Task[] tasks, int taskCount) throws StanVardException {
        if (taskCount == tasks.length) {
            throw new StanVardException("OOPS!!! The task list is full.");
        }
        tasks[taskCount] = task;
        int updatedTaskCount = taskCount + 1;
        printAddedTask(task, updatedTaskCount);
        return updatedTaskCount;
    }

    /**
     * Prints all tasks in their stored order.
     *
     * @param tasks task storage array
     * @param taskCount number of stored tasks
     */
    private static void printTaskList(Task[] tasks, int taskCount) {
        System.out.println("Here are the tasks in your list:");
        for (int index = 0; index < taskCount; index++) {
            System.out.println((index + 1) + "." + tasks[index]);
        }
    }
}

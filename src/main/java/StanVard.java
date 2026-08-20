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
        String command = scanner.nextLine();
        while (!command.equals("bye")) {
            System.out.println(SEPARATOR);
            if (command.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int index = 0; index < taskCount; index++) {
                    System.out.println((index + 1) + "." + tasks[index]);
                }
            } else if (command.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(command.substring(5)) - 1;
                tasks[taskIndex].markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + tasks[taskIndex]);
            } else if (command.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(command.substring(7)) - 1;
                tasks[taskIndex].markAsNotDone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + tasks[taskIndex]);
            } else if (command.startsWith("todo ")) {
                tasks[taskCount] = new Todo(command.substring(5));
                taskCount++;
                printAddedTask(tasks[taskCount - 1], taskCount);
            } else if (command.startsWith("deadline ")) {
                String[] deadlineParts = command.substring(9).split(" /by ", 2);
                tasks[taskCount] = new Deadline(deadlineParts[0], deadlineParts[1]);
                taskCount++;
                printAddedTask(tasks[taskCount - 1], taskCount);
            } else if (command.startsWith("event ")) {
                String[] eventParts = command.substring(6).split(" /from ", 2);
                String[] timeParts = eventParts[1].split(" /to ", 2);
                tasks[taskCount] = new Event(eventParts[0], timeParts[0], timeParts[1]);
                taskCount++;
                printAddedTask(tasks[taskCount - 1], taskCount);
            }
            System.out.println(SEPARATOR);
            command = scanner.nextLine();
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
}

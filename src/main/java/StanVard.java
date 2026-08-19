import java.util.Scanner;

/**
 * Starts StanVard, displays its greeting, and echoes commands until the user exits.
 */
public class StanVard {
    private static final String SEPARATOR = "____________________________________________________________";

    /**
     * Runs the chatbot's greeting, command echo loop, and farewell sequence.
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
        String command = scanner.nextLine();
        while (!command.equals("bye")) {
            System.out.println(SEPARATOR);
            System.out.println(command);
            System.out.println(SEPARATOR);
            command = scanner.nextLine();
        }

        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(SEPARATOR);
    }
}

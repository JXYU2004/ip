import java.util.Scanner;

/** Handles all console input and output for StanVard. */
public class Ui {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final String BANNER = " ____  _____    _    _   _ __     __ _    ____  ____  \n"
            + "/ ___||_   _|  / \\  | \\ | |\\ \\   / / / \\  |  _ \\|  _ \\ \n"
            + "\\___ \\  | |   / _ \\ |  \\| | \\ \\ / / / _ \\ | |_) | | | |\n"
            + " ___) | | |  / ___ \\| |\\  |  \\ V / / ___ \\|  _ <| |_| |\n"
            + "|____/  |_| /_/   \\_\\_| \\_|   \\_/ /_/   \\_\\_| \\_\\____/ \n";
    private final Scanner scanner;

    /** Creates a console UI that reads from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Displays the application greeting. */
    public void showGreeting() {
        showSeparator();
        System.out.print(BANNER);
        System.out.println("Hello! I'm StanVard.");
        System.out.println("What can I do for you?");
        showSeparator();
    }

    /** @return whether standard input has another command line */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /** @return the next entered command */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Displays a standard separator line. */
    public void showSeparator() {
        System.out.println(SEPARATOR);
    }

    /**
     * Displays a message for the user.
     *
     * @param message message to display
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /** Displays the application farewell. */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
        showSeparator();
    }
}

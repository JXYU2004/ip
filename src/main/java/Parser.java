import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/** Parses and validates text commands entered by the user. */
public class Parser {
    private static final DateTimeFormatter INPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    /** Represents the command keywords understood by StanVard. */
    public enum CommandType {
        LIST("list"), MARK("mark"), UNMARK("unmark"), DELETE("delete"), TODO("todo"),
        DEADLINE("deadline"), EVENT("event");
        private final String keyword;

        CommandType(String keyword) {
            this.keyword = keyword;
        }

        /** @return this command's input keyword */
        public String getKeyword() {
            return keyword;
        }
    }

    /**
     * @param command command entered by the user
     * @return matching command type
     * @throws StanVardException if the command is unknown
     */
    public CommandType parseCommandType(String command) throws StanVardException {
        String trimmedCommand = command.trim();
        for (CommandType commandType : CommandType.values()) {
            String keyword = commandType.getKeyword();
            if (trimmedCommand.equals(keyword) || trimmedCommand.startsWith(keyword + " ")) {
                return commandType;
            }
        }
        throw new StanVardException("OOPS!!! I'm sorry, but I don't know what that means :-(");
    }

    /**
     * @param command command text
     * @param commandType mark, unmark, or delete command type
     * @param taskCount number of tasks currently stored
     * @return zero-based task index
     * @throws StanVardException if the number is invalid or outside the task list
     */
    public int parseTaskIndex(String command, CommandType commandType, int taskCount) throws StanVardException {
        String numberText = detailsAfterKeyword(command, commandType);
        if (numberText.isEmpty()) {
            throw new StanVardException("OOPS!!! The task number to " + commandType.getKeyword() + " cannot be empty.");
        }
        try {
            int taskNumber = Integer.parseInt(numberText);
            if (taskNumber <= 0) {
                throw new StanVardException("OOPS!!! The task number must be a positive integer.");
            }
            if (taskNumber > taskCount) {
                throw new StanVardException("OOPS!!! The task number is out of range.");
            }
            return taskNumber - 1;
        } catch (NumberFormatException exception) {
            throw new StanVardException("OOPS!!! The task number must be a positive integer.");
        }
    }

    /** @param command todo command text
     * @return parsed todo task
     * @throws StanVardException if its description is empty */
    public Todo parseTodo(String command) throws StanVardException {
        String description = detailsAfterKeyword(command, CommandType.TODO);
        if (description.isEmpty()) {
            throw new StanVardException("OOPS!!! The description of a todo cannot be empty.");
        }
        return new Todo(description);
    }

    /** @param command deadline command text
     * @return parsed deadline task
     * @throws StanVardException if its details are invalid */
    public Deadline parseDeadline(String command) throws StanVardException {
        String details = detailsAfterKeyword(command, CommandType.DEADLINE);
        int byIndex = details.indexOf("/by");
        if (byIndex < 0) {
            throw new StanVardException("OOPS!!! A deadline must include /by followed by a date.");
        }
        String description = details.substring(0, byIndex).trim();
        String by = details.substring(byIndex + "/by".length()).trim();
        if (description.isEmpty()) {
            throw new StanVardException("OOPS!!! The description of a deadline cannot be empty.");
        }
        if (by.isEmpty()) {
            throw new StanVardException("OOPS!!! The deadline date cannot be empty.");
        }
        try {
            return new Deadline(description, LocalDate.parse(by, INPUT_DATE_FORMAT));
        } catch (DateTimeParseException exception) {
            throw new StanVardException("OOPS!!! The deadline date must be in yyyy-MM-dd format.");
        }
    }

    /** @param command event command text
     * @return parsed event task
     * @throws StanVardException if its details are invalid */
    public Event parseEvent(String command) throws StanVardException {
        String details = detailsAfterKeyword(command, CommandType.EVENT);
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
        return new Event(description, from, to);
    }

    /**
     * @param command command text
     * @param commandType command type to remove
     * @return trimmed details after the command keyword
     */
    private String detailsAfterKeyword(String command, CommandType commandType) {
        return command.trim().substring(commandType.getKeyword().length()).trim();
    }
}

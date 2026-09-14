# StanVard

StanVard is a simple task manager for todos, deadlines, and events. Use it in
the console or in the JavaFX desktop interface by entering one command at a time.

## Commands

| Command | Syntax | Example |
| --- | --- | --- |
| Add a todo | `todo <description>` | `todo read book` |
| Add a deadline | `deadline <description> /by <yyyy-MM-dd>` | `deadline submit report /by 2026-12-31` |
| Add an event | `event <description> /from <start> /to <end>` | `event project meeting /from Monday /to Tuesday` |
| View tasks | `list` | `list` |
| Mark done | `mark <number>` | `mark 1` |
| Mark not done | `unmark <number>` | `unmark 1` |
| Delete a task | `delete <number>` | `delete 1` |
| Search tasks | `find <keyword>` | `find report` |
| Exit | `bye` | `bye` |

## Notes

- Task numbers are one-based and follow the order shown by `list`.
- Deadline dates must use the `yyyy-MM-dd` format, for example `2026-12-31`.
- Event times are stored as text, so use clear values such as `Mon 2pm` and `4pm`.
- StanVard saves changes automatically and reloads them when it starts.
- Empty descriptions, missing parameters, invalid task numbers, unknown commands,
  and malformed saved data are reported with a helpful error message.

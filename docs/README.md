# StanVard User Guide

StanVard is a task manager for keeping track of todos, deadlines, and events.
Enter one command at a time in the console or JavaFX interface.

## Adding tasks

Add a todo with `todo <description>`:

```text
todo read book
```

Add a deadline with `deadline <description> /by <yyyy-MM-dd>`:

```text
deadline submit report /by 2026-12-31
```

Add an event with `event <description> /from <start> /to <end>`:

```text
event project meeting /from Monday /to Tuesday
```

## Managing tasks

- `list` displays all tasks in their stored order.
- `mark <number>` marks a task as done.
- `unmark <number>` marks a task as not done.
- `delete <number>` removes a task.
- `find <keyword>` displays tasks whose descriptions contain the keyword.
- `bye` exits StanVard.

Task numbers are one-based and refer to the order shown by `list`.

## Saved tasks

StanVard saves changes automatically in `data/duke.txt` and loads them when it
starts. The file is created when the first task is saved.

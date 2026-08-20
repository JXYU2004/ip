# Console UI Test Plan

The expected-output blocks contain program stdout only. Terminal input echo is
shown separately by the test runner as part of the test-session record.

## Deletion preserves remaining task state
Aim: Verify invalid deletion leaves tasks unchanged, valid deletion removes the selected task, and remaining tasks are renumbered.
### Input
```text
todo
todo read book
blah
deadline return book
deadline return book /by Sunday
event project meeting /from Mon 2pm
event project meeting /from Mon 2pm /to 4pm
delete 4
delete 3
mark two
mark 2
unmark 1
list
bye
```
### Expected output
```text
____________________________________________________________
 ____  _____    _    _   _ __     __ _    ____  ____  
/ ___||_   _|  / \  | \ | |\ \   / / / \  |  _ \|  _ \ 
\___ \  | |   / _ \ |  \| | \ \ / / / _ \ | |_) | | | |
 ___) | | |  / ___ \| |\  |  \ V / / ___ \|  _ <| |_| |
|____/  |_| /_/   \_\_| \_|   \_/ /_/   \_\_| \_\____/ 
Hello! I'm StanVard.
What can I do for you?
____________________________________________________________
____________________________________________________________
OOPS!!! The description of a todo cannot be empty.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS!!! I'm sorry, but I don't know what that means :-(
____________________________________________________________
____________________________________________________________
OOPS!!! A deadline must include /by followed by a date/time.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS!!! An event must include /from and /to times.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS!!! The task number is out of range.
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS!!! The task number must be a positive integer.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] read book
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][X] return book (by: Sunday)
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

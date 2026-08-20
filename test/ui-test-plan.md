# Console UI Test Plan

The expected-output blocks contain program stdout only. Terminal input echo is
shown separately by the test runner as part of the test-session record.

## Typed tasks, status changes, and string dates
Aim: Verify all task types, unstructured date/time text, status changes, list formatting, and exit behavior.
### Input
```text
todo read book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
deadline do homework /by no idea :-p
mark 1
mark 2
unmark 2
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
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] do homework (by: no idea :-p)
Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
4.[D][ ] do homework (by: no idea :-p)
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

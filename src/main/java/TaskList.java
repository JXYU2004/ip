import java.util.ArrayList;
import java.util.List;

/** Manages the ordered collection of tasks used by StanVard. */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates a task list containing the supplied restored tasks.
     *
     * @param tasks tasks with which to initialise the list
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** @param task task to add */
    public void add(Task task) {
        tasks.add(task);
    }

    /** @param index zero-based task index
     * @return task at the index */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** @param index zero-based task index
     * @return removed task */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /** @return task count */
    public int size() {
        return tasks.size();
    }

    /** @return tasks in their current order for persistence */
    public List<Task> getTasks() {
        return tasks;
    }
}

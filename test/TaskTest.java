import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Checks the completion state shared by all task types.
 */
public class TaskTest {
    /**
     * Verifies that a newly created task is incomplete.
     */
    @Test
    void newTask_isIncompleteWithBlankStatusIcon() {
        Task task = new Todo("read book");

        assertFalse(task.isDone());
        assertEquals(" ", task.getStatusIcon());
    }

    /**
     * Verifies that completion state can be marked and restored.
     */
    @Test
    void markAndUnmark_updatesCompletionStateAndStatusIcon() {
        Task task = new Todo("read book");

        task.markAsDone();
        assertTrue(task.isDone());
        assertEquals("X", task.getStatusIcon());

        task.markAsNotDone();
        assertFalse(task.isDone());
        assertEquals(" ", task.getStatusIcon());
    }
}

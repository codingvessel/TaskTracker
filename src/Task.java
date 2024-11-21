import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Task {

    private long id;

    private String description;

    private TaskStatus taskStatus;

    private Instant createdAt;

    private Instant updatedAt;

    public Task(long id, String description, TaskStatus taskStatus, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.description = description;
        this.taskStatus = taskStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Task(int nextID, String description, TaskStatus taskStatus) {
        id = nextID;
        this.description = description;
        this.taskStatus = taskStatus;
        createdAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        updatedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Task{" +
               "id=" + id +
               ", description='" + description + '\'' +
               ", taskStatus=" + taskStatus +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               '}';
    }

    public String toJson() {
        return String.format(
                "{\"id\": %d, \"description\": \"%s\", \"status\": \"%s\", \"createdAt\": \"%s\", \"updatedAt\": \"%s\"}",
                id, description, taskStatus, createdAt.toString(), updatedAt.toString()
        );
    }
}

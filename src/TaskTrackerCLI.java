import java.io.File;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"UseOfSystemOutOrSystemErr"})
public class TaskTrackerCLI {

    private static List<Task> tasks = new ArrayList<>();

    private static final File MYTASKS_PATH = new File(System.getProperty("user.dir"), "MyTasks/mytasks.json");

    private static boolean isSaveDirty;

    private TaskTrackerCLI() {
    }

    public static void main(String... args) {
        loadTasksIntoMemory();
        handleAction(args);

        if (isSaveDirty) {
            saveMemoryIntoJson();
        }
    }

    private static void loadTasksIntoMemory() {
        tasks = JsonConverter.fetchTasksFromJsons(MYTASKS_PATH);
    }

    private static void saveMemoryIntoJson() {
        JsonConverter.saveToJsonFile(tasks, MYTASKS_PATH.getPath());
    }

    private static void handleAction(String... args) {
        if (args.length == 0) {
            System.err.println("No arguments given, shutting down...");
            return;
        }

        String[] actionParams = Arrays.copyOfRange(args, 1, args.length);
        TaskAction taskAction = TaskAction.getAction(args[0]);
        switch (taskAction) {
            case ADD -> addTask(List.of(actionParams));
            case UPDATE -> updateTask(List.of(actionParams));
            case DELETE -> deleteTask(List.of(actionParams));
            case MARK_IN_PROGRESS -> markInProgressTask(List.of(actionParams));
            case MARK_DONE -> markDoneTask(List.of(actionParams));
            case LIST -> listTask(List.of(actionParams));
            default -> throw new IllegalStateException("Unexpected Action: " + taskAction);
        }

    }

    private static void addTask(List<String> arguments) {
        if (arguments.size() != 1) {
            System.err.println("Only 1 argument (id) allowed for 'add' task");
            return;
        }
        String description = arguments.getFirst();
        if (isInvalidDescription(description)) {
            System.err.println("""
                                       Description is invalid! Ensure that your description \
                                       is enclosed by double quotes and does only contain \
                                       alphanumeric chars ('-' '_' and whitespace are also allowed)
                                       """);
            return;
        }

        int nextID = IdGenerator.getNextID();
        Task task = new Task(nextID, description, TaskStatus.TODO);
        tasks.add(task);
        System.out.println(MessageFormat.format("Task added successfully (ID: {0})", nextID));
        isSaveDirty = true;
    }


    private static void listTask(List<String> arguments) {
        if (arguments.isEmpty()) {
            listAllTasks();
            return;
        }

        if (arguments.size() != 1) {
            System.err.println("Either only 0 or 1 argument allowed for 'list' task. Use 'done', 'todo' or 'in-progress' as argument");
            return;
        }
        String sanitizedInput = arguments.getFirst().replaceAll("[^a-zA-Z0-9_-]", "");
        TaskStatus taskStatus = TaskStatus.fromString(sanitizedInput);
        listTasks(taskStatus);
    }

    private static void listAllTasks() {
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    private static void listTasks(TaskStatus taskStatus) {
        List<Task> tasksToDisplay = tasks.stream().filter(task -> taskStatus == task.getTaskStatus()).toList();
        if (tasksToDisplay.isEmpty()) {
            System.out.println(MessageFormat.format("No tasks with status {0} found.", taskStatus));
        }

        for (Task task : tasksToDisplay) {
            System.out.println(task);
        }
    }

    private static void markDoneTask(List<String> arguments) {
        if (arguments.size() != 1) {
            System.err.println("Only 1 argument (id) allowed for 'mark-done' task");
            return;
        }
        String sanitizedInput = arguments.getFirst().replaceAll("[^0-9]", "");
        markTask(sanitizedInput, TaskStatus.DONE);
    }

    private static void markInProgressTask(List<String> arguments) {
        if (arguments.size() != 1) {
            System.err.println("Only 1 argument (id) allowed for 'mark-in-progress' task");
            return;
        }
        String sanitizedInput = arguments.getFirst().replaceAll("[^0-9]", "");
        markTask(sanitizedInput, TaskStatus.IN_PROGRESS);
    }

    @SuppressWarnings("FeatureEnvy")
    private static void markTask(String sanitizedInput, TaskStatus taskStatus) {
        tasks.stream()
             .filter(task -> task.getId() == Integer.parseInt(sanitizedInput))
             .findFirst()
             .ifPresent(task -> {
                 System.out.println(MessageFormat.format("Changing Status of Task {0} to {1}", sanitizedInput, taskStatus));
                 task.setTaskStatus(taskStatus);
                 task.setUpdatedAt(Instant.now().truncatedTo(ChronoUnit.SECONDS));
                 isSaveDirty = true;
             });
    }

    private static void deleteTask(List<String> arguments) {
        if (arguments.size() != 1) {
            System.err.println("Only 1 argument (id) allowed for 'delete' task");
            return;
        }
        String sanitizedInput = arguments.getFirst().replaceAll("[^0-9]", "");
        boolean isRemoved = tasks.removeIf(task -> task.getId() == Integer.parseInt(sanitizedInput));
        if (isRemoved) {
            System.out.println(MessageFormat.format("Task {0} was removed.", sanitizedInput));
            isSaveDirty = true;
        } else {
            System.err.println(MessageFormat.format(
                    "Task {0} was not removed.", sanitizedInput));
        }
    }

    private static void updateTask(List<String> arguments) {
        if (arguments.size() != 2) {
            System.err.println("Only 2 argument2 (id), (description) allowed for 'update' task");
            return;
        }
        String sanitizedId = arguments.getFirst().replaceAll("[^0-9]", "");
        String description = arguments.get(1);
        if (isInvalidDescription(description)) {
            System.err.println("""
                                       Description is invalid! Ensure that your description \
                                       does only contain \
                                       alphanumeric chars ('-' '_' and whitespace are also allowed)
                                       """);
            return;
        }
        tasks.stream()
             .filter(task -> task.getId() == Integer.parseInt(sanitizedId))
             .findFirst()
             .ifPresent(task -> {
                 System.out.println(MessageFormat.format("Updating description of Task {0} to {1}", sanitizedId, description));
                 task.setDescription(description);
             });
        isSaveDirty = true;
    }

    private static boolean isInvalidDescription(String description) {
        Pattern p = Pattern.compile("^[A-Za-z0-9 _-]*$");
        Matcher matcher = p.matcher(description);
        return !matcher.matches();
    }
}

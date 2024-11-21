public enum TaskAction {
    ADD,
    UPDATE,
    DELETE,
    MARK_IN_PROGRESS,
    MARK_DONE,
    LIST,
    HELP,
    EXIT;

    public static TaskAction getAction(String argument) {
        return switch (argument.toLowerCase()) {
            case "add" -> ADD;
            case "update" -> UPDATE;
            case "delete" -> DELETE;
            case "mark-in-progress" -> MARK_IN_PROGRESS;
            case "mark-done" -> MARK_DONE;
            case "list" -> LIST;
            case "help" -> HELP;
            case "exit" -> EXIT;

            default -> throw new IllegalStateException("Unexpected value: " + argument);
        };
    }
}

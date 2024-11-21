public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE;

    public static TaskStatus fromString(String string) {
        switch (string.toLowerCase()) {
            case "todo" -> {
                return TODO;
            }
            case "in_progress", "in-progress" -> {
                return IN_PROGRESS;
            }
            case "done" -> {
                return DONE;
            }
        }
        throw new UnsupportedOperationException("String: " + string + " is not a valid task status");
    }
}

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class JsonConverter {


    public static List<Task> fetchTasksFromJsons(File mytasksPath) {
        List<Task> tasks;
        if (!mytasksPath.exists()) {
            System.out.println("No json found, will create empty json");
            createEmptyJson(mytasksPath);
            return new ArrayList<>();
        }

        try {
            String jsonContent = Files.readString(Path.of(mytasksPath.getAbsolutePath()));
            if (jsonContent.isEmpty()) {
                return new ArrayList<>();
            }
            tasks = parseJson(jsonContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tasks;
    }

    private static void createEmptyJson(File name) {
        name.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(name))) {
            writer.write("[]");
            System.out.println("Mytasks/mytasks.json created");

        } catch (IOException e) {
            System.out.println("Mytasks/mytasks.json could not be created");
            throw new RuntimeException(e);
        }
    }

    private static List<Task> parseJson(String json) {
        String trimmedJson = json.trim().substring(1, json.length() - 1);
        String[] taskEntries = trimmedJson.split("},\\s*\\{");
        List<Task> tasks = new ArrayList<>();

        for (String entry : taskEntries) {
            if (entry.isEmpty()) {
                return new ArrayList<>();
            }
            entry = entry.replaceAll("[{}\"]", "");
            String[] fields = entry.split(",");

            int id = 0;
            String description = null;
            TaskStatus status = null;
            Instant createdAt = null;
            Instant updatedAt = null;

            for (String field : fields) {
                String[] keyValue = field.split(":", 2);
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                switch (key) {
                    case "id" -> id = Integer.parseInt(value);
                    case "description" -> description = value;
                    case "status" -> status = TaskStatus.fromString(value);
                    case "createdAt" -> createdAt = Instant.parse(value);
                    case "updatedAt" -> updatedAt = Instant.parse(value);
                }
            }
            Task task = new Task(id, description, status, createdAt, updatedAt);
            tasks.add(task);
        }
        return tasks;
    }

    public static void saveToJsonFile(List<Task> tasks, String filename) {
        String json = toJson(tasks);
        if (json.isBlank()) {
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(json);
            System.out.println("Tasks saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    private static String toJson(List<Task> tasks) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        for (int i = 0; i < tasks.size(); i++) {
            jsonBuilder.append(tasks.get(i).toJson());
            if (i < tasks.size() - 1) {
                jsonBuilder.append(",\n");
            }
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }

}

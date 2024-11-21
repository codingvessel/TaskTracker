import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class IdGenerator {


    private static final File ID_FILE_PATH = new File(System.getProperty("user.dir"), "MyTasks/sequence.txt");


    public static synchronized int getNextID() {
        int currentID = readCurrentID();
        currentID++;
        writeCurrentID(currentID);
        return currentID;
    }

    private static int readCurrentID() {
        try {
            if (!ID_FILE_PATH.exists()) {
                createInitialSequence();
            }

            String idString = Files.readString(Path.of(ID_FILE_PATH.getAbsolutePath()));
            return Integer.parseInt(idString);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading current ID: " + e.getMessage());
        }
        return 0;
    }

    private static void createInitialSequence() {
        ID_FILE_PATH.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ID_FILE_PATH))) {
            writer.write(String.valueOf(0));
            System.out.println("Mytasks/sequence.txt created");

        } catch (IOException e) {
            System.out.println("Mytasks/sequence.txt could not be created");
            throw new RuntimeException(e);
        }
    }

    private static void writeCurrentID(int currentID) {
        try (FileWriter writer = new FileWriter(ID_FILE_PATH)) {
            writer.write(String.valueOf(currentID));
        } catch (IOException e) {
            System.err.println("Error writing current ID to file: " + e.getMessage());
        }
    }
}


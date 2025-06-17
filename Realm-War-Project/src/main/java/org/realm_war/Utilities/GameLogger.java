package org.realm_war.Utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GameLogger {

    private static final String LOG_FILE = "game_log.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static synchronized void logAction(int playerId, String actionType, String details) {
        List<LogEntry> logs = readLogsFromFile();
        logs.add(new LogEntry(playerId, actionType, details));
        writeLogsToFile(logs);
    }

    private static List<LogEntry> readLogsFromFile() {
        try (FileReader reader = new FileReader(LOG_FILE)) {
            Type listType = new TypeToken<ArrayList<LogEntry>>() {}.getType();
            List<LogEntry> logs = gson.fromJson(reader, listType);

            if (logs != null) {
                return logs;
            } else {
                return new ArrayList<>();
            }
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private static void writeLogsToFile(List<LogEntry> logs) {
        try (FileWriter writer = new FileWriter(LOG_FILE)) {
            gson.toJson(logs, writer);
        } catch (IOException e) {
            System.err.println("Error: Failed to write to log file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
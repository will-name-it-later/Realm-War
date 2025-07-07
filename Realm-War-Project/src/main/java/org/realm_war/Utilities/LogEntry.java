package org.realm_war.Utilities;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LogEntry {
    private final String timestamp;
    private final int playerID;
    private final String actionType;
    private final String details;

    public LogEntry(int playerID, String actionType, String details) {
        // Automatically capture the current time using the system's default timezone.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss (XXX)");
        this.timestamp = OffsetDateTime.now(ZoneId.systemDefault()).format(formatter);
        this.playerID = playerID;
        this.actionType = actionType;
        this.details = details;
    }
}

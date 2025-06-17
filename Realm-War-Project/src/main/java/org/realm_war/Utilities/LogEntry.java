package org.realm_war.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogEntry {
    private static final transient SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final String timestamp;
    private final int playerId;
    private final String actionType;
    private final String details;

    public LogEntry(int playerId, String actionType, String details) {
        this.timestamp = sdf.format(new Date()); // Automatically capture and format the current time
        this.playerId = playerId;
        this.actionType = actionType;
        this.details = details;
    }
}

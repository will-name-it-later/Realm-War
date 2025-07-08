package org.realm_war;

import org.realm_war.Views.GameFrame;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Launcher {
    public static void main(String[] args) {
        Path filePath = Paths.get("game_log.json") ;
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("Could not delete log file: " + e.getMessage());
        }
        SwingUtilities.invokeLater(GameFrame::new);
    }
}

package org.realm_war.Views;

import org.realm_war.Models.GameState;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private InfoPanel infoPanel;
    private ActionPanel actionPanel;

    public GameFrame() {
        gamePanel = new GamePanel();
        infoPanel = new InfoPanel();
        actionPanel = new ActionPanel(this);
        setSize(1050, 1050);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Realm War - a Medieval Strategy Game");
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));
        add(gamePanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);
        add(actionPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}

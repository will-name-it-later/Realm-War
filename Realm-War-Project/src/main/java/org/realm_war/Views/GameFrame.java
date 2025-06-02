package org.realm_war.Views;

import org.realm_war.Models.GameState;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private GameState gameState;
    private InfoPanel infoPanel;
    private ActionPanel actionPanel;
    private MenuPanel menuPanel;

    public GameFrame() {
        gameState = new GameState();
        gamePanel = new GamePanel(gameState);           // shared
        infoPanel = new InfoPanel();
        actionPanel = new ActionPanel(this);
        menuPanel = new MenuPanel(gameState, gamePanel); // ✅ shared

        setSize(1050, 1050);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Realm War - a Medieval Strategy Game");
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));

        add(gamePanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);
        add(actionPanel, BorderLayout.SOUTH);
        add(menuPanel, BorderLayout.NORTH);

        setVisible(true);
    }
}

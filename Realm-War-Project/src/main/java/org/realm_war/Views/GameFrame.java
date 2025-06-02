package org.realm_war.Views;

import org.realm_war.Models.GameState;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private InfoPanel infoPanel;
    private ActionPanel actionPanel;
    private MenuPanel menuPanel;
    private JPanel sidePanel;

    public GameFrame() {
        gamePanel = new GamePanel();
        infoPanel = new InfoPanel();
        actionPanel = new ActionPanel(this);
        menuPanel = new MenuPanel(new GameState());
        setSize(1050, 1050);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Realm War - a Medieval Strategy Game");
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));
        add(gamePanel, BorderLayout.CENTER);
        initSidePanel();
        add(sidePanel, BorderLayout.EAST);
        add(actionPanel, BorderLayout.SOUTH);
        add(menuPanel, BorderLayout.NORTH);
        setVisible(true);
    }

    private void initSidePanel() {
        sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.add(infoPanel, BorderLayout.NORTH);
    }

    public void updateSidePanel(JPanel panel) {
        sidePanel.removeAll();
        sidePanel.add(infoPanel, BorderLayout.NORTH);
        sidePanel.add(panel, BorderLayout.CENTER);
        sidePanel.revalidate();
        sidePanel.repaint();
    }
}

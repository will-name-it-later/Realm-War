package org.realm_war.Views;

import org.realm_war.Models.GameState;
import org.realm_war.Utilities.Constants;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private GameState gameState;
    private InfoPanel infoPanel;
    private ActionPanel actionPanel;
    private MenuPanel menuPanel;
    private JPanel sidePanel;
    private static JLabel guidanceLabel;

    public GameFrame() {
        gameState = new GameState();
        gamePanel = new GamePanel(gameState);
        infoPanel = new InfoPanel();
        actionPanel = new ActionPanel(this, gamePanel);
        menuPanel = new MenuPanel(gameState, gamePanel);

        guidanceLabel = new JLabel("Please choose an action");
        guidanceLabel.setFont(Constants.setBoldFont(23));
        guidanceLabel.setForeground(Constants.clr_1);
        guidanceLabel.setBorder(BorderFactory.createEmptyBorder(0,40,0,0));

        setSize(1050, 1050);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Realm War - a Medieval Strategy Game");
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));

        JPanel temp = new JPanel();
        temp.setLayout(new BorderLayout());
        temp.add(actionPanel, BorderLayout.WEST);
        temp.add(guidanceLabel, BorderLayout.CENTER);

        add(gamePanel, BorderLayout.CENTER);
        initSidePanel();
        add(sidePanel, BorderLayout.EAST);
        add(temp, BorderLayout.SOUTH);
        add(menuPanel, BorderLayout.NORTH);

        JOptionPane.showMessageDialog(null, "Welcome to Realm War!\n" +
                "Please add players (at least 2) with 'Add player' button.\n" +
                "After that press 'Start game' button to go through the game.\n" +
                "Good Luck!", "Welcome", JOptionPane.INFORMATION_MESSAGE);
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

    public void setGamePanel(GamePanel newPanel) {
        this.gamePanel = newPanel;
    }
    public static JLabel getGuidanceLabel(){
        return guidanceLabel;
    }
}

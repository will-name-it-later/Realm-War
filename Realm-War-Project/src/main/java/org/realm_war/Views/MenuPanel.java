package org.realm_war.Views;

import org.realm_war.Models.GameState;
import org.realm_war.Models.Player;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel implements ActionListener {
    GameState gameState;
    GamePanel gamePanel;
    JButton addPlayerBtn;
    JButton startGameBtn;
    JButton exitGameBtn;
    JButton playAgainBtn;

    public MenuPanel(GameState gameState, GamePanel gamePanel) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        addPlayerBtn = createButton("Add Player");
        startGameBtn = createButton("Start Game");
        startGameBtn.setEnabled(false); // disabled until two players added
        exitGameBtn = createButton("Exit Game");
        playAgainBtn = createButton("Play Again");

        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        add(addPlayerBtn);
        add(startGameBtn);
        add(exitGameBtn);
        add(playAgainBtn);
    }

    private JButton createButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setFocusable(false);
        b.setBackground(new Color(95, 136, 255));
        b.setForeground(Color.WHITE);
        b.setPreferredSize(new Dimension(100, 30));
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.addActionListener(this);
        return b;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        switch (action) {
            case "Add Player" -> addPlayersToList();
            case "Start Game" -> startGame();
            case "Exit Game" -> exitGame();
            case "Play Again" -> playAgain();
        }
    }

    public void addPlayersToList() {
        if (gameState.getPlayers().size() >= 2) {
            JOptionPane.showMessageDialog(null, "Maximum 2 players allowed.");
            return;
        }

        String name = JOptionPane.showInputDialog("Enter Player " + (gameState.getPlayers().size() + 1) + " name:");
        if (name != null && !name.trim().isEmpty()) {
            Player player = new Player(name.trim(), 0);
            gameState.addPlayer(player);

            JOptionPane.showMessageDialog(null, "Player added: " + name);

            if (gameState.getPlayers().size() == 2) {
                addPlayerBtn.setEnabled(false);
                startGameBtn.setEnabled(true);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Name cannot be empty.");
        }
    }

    public void startGame() {
        if (gameState.getPlayers().size() < 2) {
            JOptionPane.showMessageDialog(null, "Please add two players before starting the game.");
            return;
        }
        gameState.setupGame(); // Initialize map, forests, town halls etc.
        gamePanel.refresh();   // <- update the visual grid
        gameState.setRunning(true);
        setEnabled(false); // disable menu panel during game
    }

    public void exitGame(){
        gameState.setRunning(true);
        setEnabled(false);
    }
    public void playAgain(){

    }
}

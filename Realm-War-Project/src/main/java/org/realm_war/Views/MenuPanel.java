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
    JButton addPlayerBtn;
    JButton startGameBtn;
    JButton exitGameBtn;
    JButton playAgainBtn;

    public MenuPanel(GameState gameState) {
        this.gameState = gameState;
        addPlayerBtn = createButton("add player");
        startGameBtn = createButton("start game");
        exitGameBtn = createButton("exit game");
        playAgainBtn = createButton("play again");
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
            case "add player":
                if (gameState.getPlayers().size() >= 2) {
                    JOptionPane.showMessageDialog(null, "Maximum 2 players allowed.");
                    return;
                }

                String name = JOptionPane.showInputDialog("Enter Player " + (gameState.getPlayers().size() + 1) + " name:");
                if (name != null && !name.trim().isEmpty()) {
                    Player player = new Player(name.trim(), 0);
                    gameState.getPlayers().add(player);

                    JOptionPane.showMessageDialog(null, "Player added: " + name);

                    // Optional: disable button after 2 players
                    if (gameState.getPlayers().size() == 2) {
                        addPlayerBtn.setEnabled(false);
                        startGameBtn.setEnabled(true); // if you have a Start Game button
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Name cannot be empty.");
                }
                break;
            case "start game":
                gameState.setRunning(true);
                setEnabled(false);
                break;
            case "exit game":
                gameState.setRunning(false);
                break;
            case "play again":
                //todo:logic to play the game again
                break;
        }
    }
}

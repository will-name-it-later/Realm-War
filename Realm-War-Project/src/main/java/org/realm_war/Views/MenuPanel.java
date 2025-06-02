package org.realm_war.Views;

import org.realm_war.Models.GameState;

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
        addPlayerBtn = createButton("addPlayer");
        startGameBtn = createButton("startGame");
        exitGameBtn = createButton("exitGame");
        playAgainBtn = createButton("playAgain");
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));
        add(addPlayerBtn);
        add(startGameBtn);
        add(exitGameBtn);
        add(playAgainBtn);
    }

    private JButton createButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setFocusable(false);
        b.setBackground(new Color(216, 169, 146));
        b.setPreferredSize(new Dimension(50, 30));
        b.setBorder(BorderFactory.createLineBorder(new Color(30, 20, 19), 1));
        b.addActionListener(this);
        return b;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        switch (action) {
            case "addPlayer":
                JOptionPane.showInputDialog(gameState, "Please enter the name of the player");
                //todo:add player and its realm to the game
                break;
            case "startGame":
                gameState.setRunning(true);
                setEnabled(false);
                break;
            case "exitGame":
                gameState.setRunning(false);
                break;
            case "playAgain":
                //todo:logic to play the game again
                break;
        }
    }
}

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
                JOptionPane.showInputDialog("please enter the name of the player", null);
                //todo:add player and its realm to the game
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

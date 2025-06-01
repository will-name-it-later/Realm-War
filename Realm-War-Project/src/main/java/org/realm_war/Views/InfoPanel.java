package org.realm_war.Views;

import javax.swing.*;
import java.awt.*;

import org.realm_war.Models.GameState;
import org.realm_war.Models.Realm;

public class InfoPanel extends JPanel{
    private JLabel playerLabel;
    private JLabel goldLabel;
    private JLabel foodLabel;
    private JLabel unitSpaceLabel;
    private JLabel turnLabel;

    public InfoPanel() {
        setPreferredSize(new Dimension(300, 700));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        playerLabel = new JLabel("Player: ");
        goldLabel = new JLabel("Gold: ");
        foodLabel = new JLabel("Food: ");
        unitSpaceLabel = new JLabel("Unit Space: ");
        turnLabel = new JLabel("Turn: ");

        add(playerLabel);
        add(goldLabel);
        add(foodLabel);
        add(unitSpaceLabel);
        add(turnLabel);

        add(Box.createVerticalGlue());
    }

    public void updateInfo(GameState gameState) {
        Realm currentRealm = gameState.getCurrentRealm();

        playerLabel.setText("Player: " + gameState.getCurrentTurn());
        goldLabel.setText("Gold: " + currentRealm.getGold());
        foodLabel.setText("Food: " + currentRealm.getFood());
        unitSpaceLabel.setText("Unit Space: " + currentRealm.getUsedUnitSpace() +
                "/" + currentRealm.getAllUnitSpace());
        turnLabel.setText("Turn: " + gameState.getTurnNumber());
    }
}


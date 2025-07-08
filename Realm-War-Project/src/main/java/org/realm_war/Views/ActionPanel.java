package org.realm_war.Views;

import org.realm_war.Controllers.UnitCtrl;
import org.realm_war.Models.GameState;
import org.realm_war.Models.Position;
import org.realm_war.Models.Realm;
import org.realm_war.Models.blocks.Block;
import org.realm_war.Models.blocks.EmptyBlock;
import org.realm_war.Models.structure.classes.*;
import org.realm_war.Models.units.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class ActionPanel extends JPanel implements ActionListener {
    private GameFrame frame;
    private GameState gameState;
    private GamePanel gamePanel;
    private UnitCtrl unitCtrl;
    private JButton nextTurnBtn;
    private JButton recruitBtn;
    private JButton buildBtn;
    private JButton attackBtn;

    private Timer autoTurnTimer;
    private final int TIMEOUT = 30_000; // 30 seconds

    public ActionPanel(GameFrame frame, GamePanel gamePanel, UnitCtrl unitCtrl) {
        this.frame = frame;
        this.gamePanel = gamePanel;
        this.gameState = gamePanel.getGameState();
        this.unitCtrl = unitCtrl;
        nextTurnBtn = createMainButton("next");
        recruitBtn = createMainButton("recruit");
        buildBtn = createMainButton("build");
        attackBtn = createMainButton("attack");
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setSize(800, 100);
        add(nextTurnBtn);
        add(recruitBtn);
        add(buildBtn);
        add(attackBtn);
        setVisible(true);
    }

    private JButton createMainButton(String OPERATION) {
        String path = "/org/realm_war/Utilities/Resources/" + OPERATION + ".png";
        URL iconUrl = getClass().getResource(path);

        if (iconUrl == null) {
            System.err.println("Missing icon: " + path);
            JButton fallback = new JButton(OPERATION); // Text-only fallback
            fallback.setPreferredSize(new Dimension(100, 100));
            fallback.setFocusable(false);
            fallback.setActionCommand(OPERATION);
            fallback.addActionListener(this);
            return fallback;
        }

        ImageIcon icon = new ImageIcon(iconUrl);
        icon = new ImageIcon(icon.getImage().getScaledInstance(96, 96, Image.SCALE_SMOOTH));
        JButton button = new JButton(icon);
        button.setBackground(new Color(255, 255, 255));
        button.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 50));
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(100, 100));
        button.setFocusable(false);
        button.setActionCommand(OPERATION);
        button.addActionListener(this);

        return button;
    }

    private JButton createSideButton(String OPERATION) {
        String path = "/org/realm_war/Utilities/Resources/" + OPERATION + ".png";
        URL iconUrl = getClass().getResource(path);

        if (iconUrl == null) {
            System.err.println("Missing icon: " + path);
            JButton fallback = new JButton(OPERATION); // Text-only fallback
            fallback.setPreferredSize(new Dimension(70, 70));
            fallback.setFocusable(false);
            fallback.setActionCommand(OPERATION);
            fallback.addActionListener(this);
            return fallback;
        }

        ImageIcon icon = new ImageIcon(iconUrl);
        icon = new ImageIcon(icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
        JButton button = new JButton(icon);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(70, 70));
        button.setFocusable(false);
        button.setActionCommand(OPERATION);
        button.addActionListener(this);
        return button;
    }

    public JPanel createRecruitPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(150, 150));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        JButton peasantBtn = createSideButton("peasant");
        JButton spearmanBtn = createSideButton("spearman");
        JButton swordsmanBtn = createSideButton("swordsman");
        JButton knightBtn = createSideButton("knight");
        panel.add(peasantBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(spearmanBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(swordsmanBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(knightBtn);

        return panel;
    }

    public JPanel createBuildPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(150, 150));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        JButton farmBtn = createSideButton("farm");
        JButton barrackBtn = createSideButton("barrack");
        JButton towerBtn = createSideButton("tower");
        JButton marketBtn = createSideButton("market");
        panel.add(farmBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(barrackBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(towerBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(marketBtn);

        return panel;
    }

    public void nextTurn() {
        if (gameState.isGameOver()) {
            gameState.setRunning(false);
            // todo : update the info label to indicate that game is over
            if (autoTurnTimer != null) autoTurnTimer.stop(); // stop timer if game over
        } else {
            gameState.nextTurn();
            gamePanel.refresh();
            startTurnTimer(); // reset the timer after each turn
        }
    }

    private void startTurnTimer() {
        if (autoTurnTimer != null) {
            autoTurnTimer.stop(); // Stop previous timer
        }

        autoTurnTimer = new Timer(TIMEOUT, e -> {
            System.out.println("Auto next turn triggered after 30s");
            nextTurn(); // Trigger turn manually
        });

        autoTurnTimer.setRepeats(false); // Only run once
        autoTurnTimer.start();
    }


    public void updateUnit(Unit u) {
        Position pos = u.getPosition();
        Realm currentRealm = gameState.getCurrentRealm();
        Block targetBlock = gameState.getBlockAt(pos);

        if (u.getRealmID() != targetBlock.getRealmID()) {
            JOptionPane.showMessageDialog(frame, "You can only place units in your own territory!", "Error", JOptionPane.ERROR_MESSAGE);
            gamePanel.refresh();
            return;
        }
        if (currentRealm.getGold() < u.getPayment()) {
            JOptionPane.showMessageDialog(this, "You don't have enough gold to recruit this unit.", "Error", JOptionPane.ERROR_MESSAGE);
            gamePanel.refresh();
            return;
        }
        if (targetBlock.hasUnit() && !targetBlock.getUnit().canMerge(u)) {
            JOptionPane.showMessageDialog(this, "Block is occupied and units cannot be merged.", "Error", JOptionPane.ERROR_MESSAGE);
            gamePanel.refresh();
            return;
        }
        try {
            currentRealm.addGold(-u.getPayment()); // Deduct the gold cost first
            if (targetBlock.getUnit() == null) {
                currentRealm.addUnit(u);
                targetBlock.setUnit(u);
            } else {
                // Merge logic
                Unit existingUnit = targetBlock.getUnit();
                Unit mergedUnit = existingUnit.merge(u);
                currentRealm.removeUnit(existingUnit);
                currentRealm.addUnit(mergedUnit);
                targetBlock.setUnit(mergedUnit);
            }
            gamePanel.refresh();

        } catch (IllegalArgumentException e) {
            // Refunding the gold if unit can't place.
            currentRealm.addGold(u.getPayment());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Recruitment Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void updateStructure(Structure structure){
        //todo
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "next" -> nextTurn();
            case "recruit" -> {
                try{
                    JPanel panel = createRecruitPanel();
                    frame.updateSidePanel(panel);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Choose a block", "error", JOptionPane.WARNING_MESSAGE);
                }
            }
            case "build" -> {
                try{
                    JPanel panel = createBuildPanel();
                    frame.updateSidePanel(panel);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Choose a block", "error", JOptionPane.WARNING_MESSAGE);
                }
            }
            case "peasant" -> {
                try{
                    Position pos = gamePanel.getSelectedPosition();
                    int ID = gamePanel.getSelectedRealmID();
                    updateUnit(new Peasant(pos, ID));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Choose a block", "error", JOptionPane.WARNING_MESSAGE);
                }
            }
            case "spearman" -> {
                try{
                    Position pos = gamePanel.getSelectedPosition();
                    int ID = gamePanel.getSelectedRealmID();
                    updateUnit(new Spearman(pos, ID));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Choose a block", "error", JOptionPane.WARNING_MESSAGE);
                }

            }
            case "swordsman" -> {
                try{
                    Position pos = gamePanel.getSelectedPosition();
                    int ID = gamePanel.getSelectedRealmID();
                    updateUnit(new Swordsman(pos, ID));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Choose a block", "error", JOptionPane.WARNING_MESSAGE);
                }
            }
            case "knight" -> {
                try{
                    Position pos = gamePanel.getSelectedPosition();
                    int ID = gamePanel.getSelectedRealmID();
                    updateUnit(new Knight(pos, ID));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Choose a block", "error", JOptionPane.WARNING_MESSAGE);
                }
            }
            case "farm" -> {
                try {
                    Position pos = gamePanel.getSelectedPosition();
                    int ID = gamePanel.getSelectedRealmID();
                    Block block = gameState.getBlockAt(pos);
                    Farm farm = new Farm(pos, block, ID);
                    Realm realm = gameState.getRealmByRealmID(ID);
                    realm.addStructure(farm); // This should start the timer internally
                    gamePanel.updateStructure(farm);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Choose a block", "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
            case "barrack" -> {
                try{
                    Position pos = gamePanel.getSelectedPosition();
                    int ID = gamePanel.getSelectedRealmID();
                    Block block = gameState.getBlockAt(pos);
                    Barrack barrack = new Barrack(pos, block, ID);
                    Realm realm = gameState.getRealmByRealmID(ID);
                    realm.addStructure(barrack);
                    gamePanel.updateStructure(barrack);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Choose a block", "error", JOptionPane.WARNING_MESSAGE);
                }
            }
            case "tower" -> {
                try{
                    Position pos = gamePanel.getSelectedPosition();
                    int ID = gamePanel.getSelectedRealmID();
                    Block block = gameState.getBlockAt(pos);
                    Tower tower = new Tower(pos, block, ID);
                    Realm realm = gameState.getRealmByRealmID(ID);
                    realm.addStructure(tower);
                    gamePanel.updateStructure(tower);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Choose a block", "error", JOptionPane.WARNING_MESSAGE);
                }
            }
            case "market" -> {
                try{
                    Position pos = gamePanel.getSelectedPosition();
                    int ID = gamePanel.getSelectedRealmID();
                    Block block = gameState.getBlockAt(pos);
                    Market market = new Market(pos, block, ID);
                    Realm realm = gameState.getRealmByRealmID(ID);
                    realm.addStructure(market);
                    gamePanel.updateStructure(market);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Choose a block", "error", JOptionPane.WARNING_MESSAGE);
                }
            }
            case "attack" -> {
                //Position pos = gamePanel.getSelectedPosition();
            }
        }
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        if (this.unitCtrl != null) {
            this.unitCtrl.setGameState(gameState);
        }
    }
}


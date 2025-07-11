package org.realm_war.Views;

import org.realm_war.Controllers.UnitCtrl;
import org.realm_war.Models.GameState;
import org.realm_war.Models.Position;
import org.realm_war.Models.Realm;
import org.realm_war.Models.blocks.Block;
import org.realm_war.Models.blocks.EmptyBlock;
import org.realm_war.Models.structure.classes.*;
import org.realm_war.Models.units.*;
import org.realm_war.Utilities.Constants;
import org.realm_war.Utilities.GameLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.function.Consumer;

public class ActionPanel extends JPanel implements ActionListener {
    private GameFrame frame;
    private InfoPanel infoPanel;
    private GameState gameState;
    private GamePanel gamePanel;
    private UnitCtrl unitCtrl;
    private JButton nextTurnBtn;
    private JButton recruitBtn;
    private JButton buildBtn;
    private JButton attackBtn;

    private Timer countdownUpdateTimer;
    private Consumer<Integer> timerUpdateCallback; // Hook for GameFrame

    private boolean isAttacking = false;

    private transient Timer autoTurnTimer;
    private long turnStartTime;
    private int remainingTimeout;
    private final int TIMEOUT = 30_000; // 30 seconds

    public ActionPanel(GameFrame frame, GamePanel gamePanel, UnitCtrl unitCtrl, InfoPanel infoPanel) {
        this.frame = frame;
        this.gamePanel = gamePanel;
        this.gameState = gamePanel.getGameState();
        this.unitCtrl = unitCtrl;
        this.infoPanel = infoPanel;
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

    public void beginFirstTurn() {
        startTurnTimer();
    }

    public void pauseAutoTurnTimer() {
        if (autoTurnTimer != null && autoTurnTimer.isRunning()) {
            autoTurnTimer.stop();
            long timeSinceTurnStart = System.currentTimeMillis() - this.turnStartTime;
            // The remaining time is the total time minus what has passed.
            this.remainingTimeout = TIMEOUT - (int) timeSinceTurnStart;

            if (this.remainingTimeout < 0) {
                this.remainingTimeout = 0;
            }
        }
    }

    public void resumeAutoTurnTimer() {
        if (autoTurnTimer != null && !autoTurnTimer.isRunning()) {
            autoTurnTimer = new Timer(this.remainingTimeout, e -> {
                nextTurn();
            });
            autoTurnTimer.setRepeats(false);
            autoTurnTimer.start();

            this.turnStartTime = System.currentTimeMillis();
        }
    }

    public void nextTurn() {
        if (gameState.isGameOver()) {
            gameState.setRunning(false);
            // todo : update the info label to indicate that game is over
            if (autoTurnTimer != null) autoTurnTimer.stop();// stop timer if game over
            return;
        }
        String playerName = gameState.getCurrentPlayer().getName();
        int turnNumber = gameState.getTurnNumber();
        String details = String.format("Player %s turn %d ended.", playerName, turnNumber);

        GameLogger.logAction(gameState.getCurrentRealm().getID(), "END_TURN", details);

        gameState.nextTurn();
        gamePanel.refresh();
        startTurnTimer(); // reset the timer after each turn
    }

    public void setTimerUpdateCallback(Consumer<Integer> callback) {
        this.timerUpdateCallback = callback;
    }


    private void startTurnTimer() {
        if (autoTurnTimer != null) {
            autoTurnTimer.stop(); // Stop previous timer
        }
        if (countdownUpdateTimer != null) countdownUpdateTimer.stop();
        // This line added for remembering the exact moment this turn's timer starts.
        this.turnStartTime = System.currentTimeMillis();

        autoTurnTimer = new Timer(TIMEOUT, e -> {
            System.out.println("Auto next turn triggered after 30s");
            nextTurn(); // Trigger turn manually
        });

        autoTurnTimer.setRepeats(false); // Only run once
        autoTurnTimer.start();

        // UI countdown update timer (1s interval)
        countdownUpdateTimer = new Timer(1000, e -> {
            long elapsed = System.currentTimeMillis() - turnStartTime;
            int secondsLeft = (int)((TIMEOUT - elapsed) / 1000);

            // Clamp at 0
            if (secondsLeft < 0) secondsLeft = 0;

            // Update UI label through callback
            if (timerUpdateCallback != null) {
                timerUpdateCallback.accept(secondsLeft);
            }
        });
        countdownUpdateTimer.setRepeats(true);
        countdownUpdateTimer.start();
    }


    public void updateUnit(Unit u) throws IllegalArgumentException {
        Position pos = u.getPosition();
        Realm currentRealm = gameState.getCurrentRealm();
        Block targetBlock = gameState.getBlockAt(pos);

        if (u.getRealmID() != targetBlock.getRealmID()) {
            gamePanel.refresh();
            throw new IllegalArgumentException("You can only place units in your own territory!");
        }
        if (currentRealm.getGold() < u.getPayment()) {
            gamePanel.refresh();
            throw new IllegalArgumentException("You don't have enough gold to recruit this unit.");
        }
        if (targetBlock.hasUnit() && !targetBlock.getUnit().canMerge(u)) {
            gamePanel.refresh();
            throw new IllegalArgumentException("Block is occupied and units cannot be merged.");
        }
        try {
            // Deduct the gold cost & ration first
            currentRealm.addGold(-u.getPayment());
            currentRealm.addFood(-u.getRation());
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
            currentRealm.addFood(u.getRation());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Recruitment Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateStructure(Structure s) throws IllegalArgumentException {
        Position pos = s.getPosition();
        Realm currentRealm = gameState.getCurrentRealm();
        Block targetBlock = gameState.getBlockAt(pos);

        if (s.getKingdomId() != targetBlock.getRealmID()) {
            gamePanel.refresh();
            throw new IllegalArgumentException("You can only place units in your own territory!");
        }
        if (currentRealm.getGold() < s.getMaintenanceCost()) {
            gamePanel.refresh();
            throw new IllegalArgumentException("You don't have enough gold to Build this structure.");
        }
        if(targetBlock.hasStructure() && !targetBlock.getStructure().canLevelUp(s)) {
            gamePanel.refresh();
            throw new IllegalArgumentException("this Structure can not be upgraded because it's at max level or structures are different!");
        }

        try{
            currentRealm.addGold(-s.getMaintenanceCost());
            if (targetBlock.getStructure() == null) {
                currentRealm.addStructure(s);
                targetBlock.setStructure(s);
            }else{
                targetBlock.getStructure().levelUp(s);
                JOptionPane.showMessageDialog(frame, "Structure level up successful. Now this structure is at level " + targetBlock.getStructure().getLevel());
            }
            gamePanel.refresh();
        }catch(IllegalArgumentException e){
            currentRealm.addGold(s.getMaintenanceCost());
        }
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
                    JOptionPane.showMessageDialog(frame, "Choose a block", "error", JOptionPane.WARNING_MESSAGE);
                }
            }
            case "build" -> {
                try{
                    JPanel panel = createBuildPanel();
                    frame.updateSidePanel(panel);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Choose a block", "error", JOptionPane.WARNING_MESSAGE);
                }
            }
            case "peasant" -> {
                try{
                    if (gameState.getRealmByRealmID(gamePanel.getSelectedRealmID()).getAvailableUnitSpace() >= 1) {
                        Position pos = gamePanel.getSelectedPosition();
                        int ID = gamePanel.getSelectedRealmID();
                        updateUnit(new Peasant(pos, ID));
                        gameState.getRealmByRealmID(gamePanel.getSelectedRealmID()).addAvailableUnitSpace(-1);
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "error", JOptionPane.WARNING_MESSAGE);
                }
            }
            case "spearman" -> {
                try{
                    if (gameState.getRealmByRealmID(gamePanel.getSelectedRealmID()).getAvailableUnitSpace() >= 1) {
                        Position pos = gamePanel.getSelectedPosition();
                        int ID = gamePanel.getSelectedRealmID();
                        updateUnit(new Spearman(pos, ID));
                        gameState.getRealmByRealmID(gamePanel.getSelectedRealmID()).addAvailableUnitSpace(-1);
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "error", JOptionPane.WARNING_MESSAGE);
                }

            }
            case "swordsman" -> {
                try{
                    if (gameState.getRealmByRealmID(gamePanel.getSelectedRealmID()).getAvailableUnitSpace() >= 1){
                        Position pos = gamePanel.getSelectedPosition();
                        int ID = gamePanel.getSelectedRealmID();
                        updateUnit(new Swordsman(pos, ID));
                        gameState.getRealmByRealmID(gamePanel.getSelectedRealmID()).addAvailableUnitSpace(-1);
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "error", JOptionPane.WARNING_MESSAGE);
                }
            }
            case "knight" -> {
                try{
                    if (gameState.getRealmByRealmID(gamePanel.getSelectedRealmID()).getAvailableUnitSpace() >= 1) {
                        Position pos = gamePanel.getSelectedPosition();
                        int ID = gamePanel.getSelectedRealmID();
                        updateUnit(new Knight(pos, ID));
                        gameState.getRealmByRealmID(gamePanel.getSelectedRealmID()).addAvailableUnitSpace(-1);
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "error", JOptionPane.WARNING_MESSAGE);
                }
            }
            case "farm" -> {
                try {
                    if (gameState.canPlaceFarm()){
                        Position pos = gamePanel.getSelectedPosition();
                        int ID = gamePanel.getSelectedRealmID();
                        updateStructure(new Farm(pos, gameState.getBlockAt(pos), ID));
                        gameState.incrementFarmCount();
                    }
                    if (!gameState.canPlaceFarm()) {
                        JOptionPane.showMessageDialog(frame, "You can only place " + Constants.MAX_FARM + " farms.",
                                "Limit Reached", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
            case "barrack" -> {
                try{
                    Position pos = gamePanel.getSelectedPosition();
                    int ID = gamePanel.getSelectedRealmID();
                    updateStructure(new Barrack(pos, gameState.getBlockAt(pos), ID));
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "error", JOptionPane.WARNING_MESSAGE);
                }
            }
            case "tower" -> {
                try{
                    Position pos = gamePanel.getSelectedPosition();
                    int ID = gamePanel.getSelectedRealmID();
                    updateStructure(new Tower(pos, gameState.getBlockAt(pos), ID));
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "error", JOptionPane.WARNING_MESSAGE);
                }
            }
            case "market" -> {
                try{
                    if (gameState.canPlaceMarket()){
                        Position pos = gamePanel.getSelectedPosition();
                        int ID = gamePanel.getSelectedRealmID();
                        updateStructure(new Market(pos, gameState.getBlockAt(pos), ID));
                        gameState.incrementMarketCount();
                    }
                    if (!gameState.canPlaceMarket()) {
                        JOptionPane.showMessageDialog(frame, "You can only place " + Constants.MAX_MARKET + " markets.",
                                "Limit Reached", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "error", JOptionPane.WARNING_MESSAGE);
                }
            }
            case "attack" -> {
                    isAttacking = true;
                    JOptionPane.showMessageDialog(frame, "Select a target to attack.", "Attack Mode", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        if (this.unitCtrl != null) {
            this.unitCtrl.setGameState(gameState);
        }
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void resetAttacking() {
        this.isAttacking = false;
    }
}


package org.realm_war.Views;

import org.realm_war.Controllers.UnitCtrl;
import org.realm_war.Models.GameState;
import org.realm_war.Models.Position;
import org.realm_war.Models.blocks.Block;
import org.realm_war.Models.blocks.VoidBlock;
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
    private JButton moveBtn;
    private JButton attackBtn;

    public ActionPanel(GameFrame frame, GamePanel gamePanel) {
        this.frame = frame;
        this.gamePanel = gamePanel;
        this.gameState = new GameState();
        this.unitCtrl = new UnitCtrl();
        nextTurnBtn = createMainButton("next");
        recruitBtn = createMainButton("recruit");
        buildBtn = createMainButton("build");
        moveBtn = createMainButton("move");
        attackBtn = createMainButton("attack");
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setSize(800, 100);
        add(nextTurnBtn);
        add(recruitBtn);
        add(buildBtn);
        add(moveBtn);
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
        JButton townHallBtn = createSideButton("market");
        panel.add(farmBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(barrackBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(towerBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(townHallBtn);

        return panel;
    }

    public void nextTurn() {
        gameState.nextTurn();
        gamePanel.refresh();
    }

    /* private void handleClick() {
        GameState gameState = new GameState();
        Unit unitOnBlock = block.getUnit();

        if (unitOnBlock != null && unitOnBlock.getOwner().equals(gameState.getCurrentPlayer())) {
            // Player clicked on one of their own units
            gameState.setSelectedUnit(unitOnBlock);
            gameState.setTargetBlock(null); // Clear old target
            System.out.println("Selected unit at: " + block.getPosition().getX() + "," + block.getPosition().getY());
        } else {
            // Player clicked on a destination or enemy
            gameState.setTargetBlock(block);
            System.out.println("Selected target block at: " + block.getPosition().getX() + "," + block.getPosition().getY());
        }
    }

     */

    public void moveUnit() {
        Unit selectedUnit = unitCtrl.getSelectedUnit();
        Block targetBlock = unitCtrl.getTargetBlock();

        if (selectedUnit == null || targetBlock == null) {
            JOptionPane.showMessageDialog(this, "Please select both a unit and a destination.");
            return;
        }

        if (!unitCtrl.isPlayerTurn(selectedUnit.getOwner())) {
            JOptionPane.showMessageDialog(this, "It's not your turn.");
            return;
        }

        if (selectedUnit.canMoveTo(targetBlock)) {
            unitCtrl.moveUnitToBlock(selectedUnit, targetBlock);
            JOptionPane.showMessageDialog(this, "Unit moved successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid move: target is out of range or blocked.");
        }

        /*
        Unit u = gameState.getUnitAt(pos);
        JOptionPane.showMessageDialog(frame, "Please select your destination");//todo : a label should be indicated beneath the game panel
        Position destination = gamePanel.getSelectedPosition();
        u.setPosition(destination);

         */
    }

    public void attackUnit(Position pos) {
        Unit u = gameState.getUnitAt(pos);
        JOptionPane.showMessageDialog(frame, "Please select your target");//todo : a label should be indicated beneath the game panel
        Position target = gamePanel.getSelectedPosition();
        if (u.canAttackUnit(gameState.getUnitAt(target))) {
            //todo : game control should be implemented in game panel so a unit can be omitted from the map
            //gamePanel.removeUnitAt(target) for example
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "next" -> nextTurn();
            case "recruit" -> {
                JPanel panel = createRecruitPanel();
                frame.updateSidePanel(panel);
            }
            case "build" -> {
                JPanel panel = createBuildPanel();
                frame.updateSidePanel(panel);
            }
            case "peasant" -> {
                Position pos = gamePanel.getSelectedPosition();
                int ID = gamePanel.getSelectedRealmID();
                gamePanel.updateUnit(new Peasant(pos, ID));
            }
            case "spearman" -> {
                Position pos = gamePanel.getSelectedPosition();
                int ID = gamePanel.getSelectedRealmID();
                gamePanel.updateUnit(new Spearman(pos, ID));
            }
            case "swordsman" -> {
                Position pos = gamePanel.getSelectedPosition();
                int ID = gamePanel.getSelectedRealmID();
                gamePanel.updateUnit(new Swordsman(pos, ID));
            }
            case "knight" -> {
                Position pos = gamePanel.getSelectedPosition();
                int ID = gamePanel.getSelectedRealmID();
                gamePanel.updateUnit(new Knight(pos, ID));
            }
            case "farm" -> {
                Position pos = gamePanel.getSelectedPosition();
                int ID = gamePanel.getSelectedRealmID();
                Block block = gameState.getBlockAt(pos);
                gamePanel.updateStructure(new Farm(pos, block, ID));
            }
            case "barrack" -> {
                Position pos = gamePanel.getSelectedPosition();
                int ID = gamePanel.getSelectedRealmID();
                Block block = gameState.getBlockAt(pos);
                gamePanel.updateStructure(new Barrack(pos, block, ID));
            }
            case "tower" -> {
                Position pos = gamePanel.getSelectedPosition();
                int ID = gamePanel.getSelectedRealmID();
                Block block = gameState.getBlockAt(pos);
                gamePanel.updateStructure(new Tower(pos, block, ID));
            }
            case "market" -> {
                Position pos = gamePanel.getSelectedPosition();
                int ID = gamePanel.getSelectedRealmID();
                Block block = gameState.getBlockAt(pos);
                gamePanel.updateStructure(new Market(pos, block, ID));
            }
            case "move" -> {
                //Position pos = gamePanel.getSelectedPosition();
                moveUnit();
            }
            case "attack" -> {
                Position pos = gamePanel.getSelectedPosition();
                attackUnit(pos);
            }
        }
    }
}


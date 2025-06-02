package org.realm_war.Views;

import org.realm_war.Models.Position;
import org.realm_war.Models.Realm;
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
    private GamePanel gamePanel;
    private JButton nextTurnBtn;
    private JButton recruitBtn;
    private JButton buildBtn;
    private JButton moveBtn;
    private JButton attackBtn;

    public ActionPanel(GameFrame frame, GamePanel gamePanel) {
        this.frame = frame;
        this.gamePanel = gamePanel;
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
        //todo : implement logic for moving to next player
    }

    public void updateUnit(Unit unit) {
        //todo : update the units and the grid at GamePanel
    }

    public void updateStructure(Structure structure) {
        //todo : update the structures and the grid at GamePanel
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
                //todo : get the position selected and the realm from GamePanel
                Position pos = new Position(1, 1);//sample position
                String realmName = "my realm";//sample realm name
                updateUnit(new Peasant(pos, realmName));
            }
            case "spearman" -> {
                //same for this part and the following ones
                Position pos = new Position(1, 1);
                String realmName = "my realm";
                updateUnit(new Spearman(pos, realmName));
            }
            case "swordsman" -> {
                Position pos = new Position(1, 1);
                String realmName = "my realm";
                updateUnit(new Swordsman(pos, realmName));
            }
            case "knight" -> {
                Position pos = new Position(1, 1);
                String realmName = "my realm";
                updateUnit(new Knight(pos, realmName));
            }
            case "farm" -> {
                //todo : get position selected and the realm from GamePanel
                Position pos = new Position(1, 1);
                //todo : get the base block of this position
                Block block = new VoidBlock(pos);//sample base block
                int realmID = 1001;//sample ID
                updateStructure(new Farm(pos, block, realmID));
            }
            case "barrack" -> {
                Position pos = new Position(1, 1);
                Block block = new VoidBlock(pos);
                int realmID = 1002;
                updateStructure(new Barrack(pos, block, realmID));
            }
            case "tower" -> {
                Position pos = new Position(1, 1);
                Block block = new VoidBlock(pos);
                int realmID = 1003;
                updateStructure(new Tower(pos, block, realmID));
            }
            case "market" -> {
                Position pos = new Position(1, 1);
                Block block = new VoidBlock(pos);
                int realmID = 1004;
                //todo: constructor for Market.java should be fixed
                updateStructure(new Market(0, 0, 0, 0, pos, block, realmID));
            }
        }
    }
}


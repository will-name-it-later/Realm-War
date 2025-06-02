package org.realm_war.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class ActionPanel extends JPanel implements ActionListener {
    private JFrame frame;
    private JButton nextTurnBtn;
    private JButton recruitBtn;
    private JButton buildBtn;
    private JButton moveBtn;
    private JButton attackBtn;

    public ActionPanel(JFrame frame) {
        this.frame = frame;
        nextTurnBtn = createButton("next");
        recruitBtn = createButton("recruit");
        buildBtn = createButton("build");
        moveBtn = createButton("move");
        attackBtn = createButton("attack");
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

    private JButton createButton(String OPERATION) {
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

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if ("next".equals(command)) {
            //todo: next turn
        } else if ("recruit".equals(command)) {
            createRecruitPanel();
        } else if ("build".equals(command)) {
            createBuildPanel();
        } else if ("move".equals(command)) {
            //todo: move a unit to selected position
        } else if ("attack".equals(command)) {
            //todo: attack to selected position
        } else if ("peasant".equals(command)) {
        }
    }

    public void createRecruitPanel() {
        Component component = ((BorderLayout)frame.getContentPane().getLayout()).getLayoutComponent(BorderLayout.EAST);
        clearEastPanel();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(150, 150));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        JButton peasantBtn = createButton("peasant");
        JButton spearmanBtn = createButton("spearman");
        JButton swordsmanBtn = createButton("swordsman");
        JButton knightBtn = createButton("knight");
        panel.add(peasantBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(spearmanBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(swordsmanBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(knightBtn);
        frame.add(panel, BorderLayout.EAST);
        frame.revalidate();
        frame.repaint();
    }

    public void createBuildPanel() {
        clearEastPanel();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(150, 150));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        JButton farmBtn = createButton("farm");
        JButton barrackBtn = createButton("barrack");
        JButton towerBtn = createButton("tower");
        JButton townhallBtn = createButton("townhall");
        panel.add(farmBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(barrackBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(towerBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(townhallBtn);
        frame.add(panel, BorderLayout.EAST);
        frame.revalidate();
        frame.repaint();
    }

    public void clearEastPanel() {
        Component eastPanel = ((BorderLayout) frame.getContentPane().getLayout()).getLayoutComponent(BorderLayout.EAST);
        if (eastPanel != null) frame.remove(eastPanel);
    }
}


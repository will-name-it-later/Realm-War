package org.realm_war.Views;

import org.realm_war.Controllers.GameCtrl;
import org.realm_war.Models.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.spi.AbstractInterruptibleChannel;
import java.util.Objects;

public class ActionPanel extends JPanel implements ActionListener {
    JFrame frame;
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
        String path = "/org/realm_war/Utilities/assets/" + OPERATION + ".png";
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


    public static void main(String[] args) {
        JFrame frame = new JFrame("ActionPanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(950, 950);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(new ActionPanel(frame), BorderLayout.SOUTH);
        frame.add(new GamePanel() ,BorderLayout.CENTER);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if ("next".equals(command)) {
            //todo: next turn
        } else if ("recruit".equals(command)) {
            JPanel panel = recruitPanel();
            frame.add(panel, BorderLayout.EAST);
            frame.revalidate();
            frame.repaint();
        } else if ("build".equals(command)) {
            JPanel panel = buildPanel();
            frame.add(panel, BorderLayout.EAST);
            frame.revalidate();
            frame.repaint();
        } else if ("move".equals(command)) {
            //todo: move a unit to selected position
        } else if ("attack".equals(command)) {
            //todo: attack to selected position
        } else if ("peasant".equals(command)) {}
    }

    public JPanel recruitPanel(){
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
        return panel;
    }

    public JPanel buildPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(120, 100));
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
        return panel;
    }
}

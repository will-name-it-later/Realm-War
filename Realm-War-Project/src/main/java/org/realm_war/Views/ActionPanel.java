package org.realm_war.Views;

import org.realm_war.Controllers.GameCtrl;
import org.realm_war.Models.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

public class ActionPanel extends JPanel implements ActionListener {
    private GameCtrl gameCtrl;
    private JButton nextTurnBtn;
    private JButton recruitBtn;
    private JButton buildBtn;
    private JButton moveBtn;
    private JButton attackBtn;

    public ActionPanel(GameCtrl gameCtrl) {
        this.gameCtrl = gameCtrl;
        nextTurnBtn = createButton("next");
        recruitBtn = createButton("recruit");
        buildBtn = createButton("build");
        moveBtn = createButton("move");
        attackBtn = createButton("attack");
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setSize(800, 100);
        add(nextTurnBtn);
        add(recruitBtn);
        add(buildBtn);
        add(moveBtn);
        add(attackBtn);
    }

    private JButton createButton(String OPERATION) {
        String path = "/org/realm_war/Utilities/assets/" + OPERATION + ".png";
        JButton button = null;
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(path)));
            icon = new ImageIcon(icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));
            button = new JButton(icon);
            button.setBackground(new Color(255, 255, 255));
            button.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 50));
            button.setOpaque(true);
            button.setPreferredSize(new Dimension(100, 50));
            button.setFocusable(false);
            button.setActionCommand(OPERATION);
            button.addActionListener(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if ("next".equals(command)) {
            gameCtrl.getGameState().nextTurn();
        } else if ("recruit".equals(command)) {
            //todo: open recruit panel
        } else if ("build".equals(command)) {
            //todo: open build panel
        } else if ("move".equals(command)) {
            //todo: move a unit to selected position
        } else if ("attack".equals(command)) {
            //todo: attack to selected position
        }
    }
}

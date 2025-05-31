package org.realm_war.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

public class ActionPanel extends JPanel implements ActionListener {
    private JButton nextTurnBtn;
    private JButton recruitBtn;
    private JButton buildBtn;
    private JButton moveBtn;
    private JButton attackBtn;

    public ActionPanel() {
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
            Image scaledInstance = icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaledInstance);
            button = new JButton(icon);
            button.setBackground(new Color(255, 255, 255));
            button.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 50));
            button.setOpaque(true);
            button.setPreferredSize(new Dimension(100, 50));
            button.setFocusable(false);
            button.setActionCommand(OPERATION);
            button.addActionListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return button;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.add(new ActionPanel(), BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        System.out.println(command);
        if("next".equals(command)) {
            //todo

        }else if("recruit".equals(command)) {
            //todo
        }else if("build".equals(command)) {
            //todo
        }else if("move".equals(command)) {
            //todo
        } else if ("attack".equals(command)) {
            //todo
        }
    }
}

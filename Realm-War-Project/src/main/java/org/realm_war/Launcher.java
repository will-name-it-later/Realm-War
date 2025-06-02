package org.realm_war;

import org.realm_war.Views.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Launcher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameFrame::new);
    }
}

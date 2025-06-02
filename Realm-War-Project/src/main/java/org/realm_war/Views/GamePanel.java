package org.realm_war.Views;

import org.realm_war.Models.GameState;
import org.realm_war.Models.blocks.Block;
import org.realm_war.Models.blocks.VoidBlock;
import org.realm_war.Utilities.Constants;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private int rows;
    private int cols;
    private JButton[][] btnGrid;
    private Block[][] mapGrid;
    private final Dimension blockSize = new Dimension(45, 45);

    public GamePanel() {
        this.rows = Constants.getMapSize();
        this.cols = Constants.getMapSize();
        this.btnGrid = new JButton[rows][cols];

        GameState.mapInitializer();
        GameState.forestPlacer();
        // Ensure mapGrid is initialized
        mapGrid = GameState.getMapGrid();

        setLayout(new GridLayout(rows, cols));
        setPreferredSize(new Dimension(900, 900));
        initializeGrid();
    }

    private void initializeGrid() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                JButton blockButton = new JButton();
                blockButton.setPreferredSize(blockSize);

                // Defensive check in case mapGrid isn't fully initialized
                Block block = mapGrid[row][col];
                if (block != null) {
                    blockButton.setBackground(block.getColor());
                } else {
                    blockButton.setBackground(Color.PINK);  // fallback color
                }

                if (block instanceof VoidBlock) {
                    ImageIcon icon = new ImageIcon(getClass().getResource("/org/realm_war/Utilities/Resources/close.png"));
                    blockButton.setIcon(icon);
                    blockButton.setDisabledIcon(icon); // Optional, ensures icon stays visible when disabled

                    blockButton.setEnabled(false); // Disable clicking
                } else {
                    int finalRow = row;
                    int finalCol = col;
                    blockButton.addActionListener(e -> {
                        handleBlockClick(finalRow, finalCol);
                    });
                }


                btnGrid[row][col] = blockButton;
                add(blockButton);
            }
        }
    }

    public void handleBlockClick(int row , int col){
        System.out.println("Clicked block at (" + row + ", " + col + ")");
    }
}

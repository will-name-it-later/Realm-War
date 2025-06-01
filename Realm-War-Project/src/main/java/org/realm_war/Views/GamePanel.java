package org.realm_war.Views;

import org.realm_war.Models.GameState;
import org.realm_war.Models.blocks.Block;
import org.realm_war.Models.blocks.EmptyBlock;
import org.realm_war.Models.blocks.ForestBlock;
import org.realm_war.Models.blocks.VoidBlock;
import org.realm_war.Utilities.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel {
    private int rows;
    private int cols;
    private JButton[][] btnGrid;
    private Block[][] mapGrid;
    private Dimension blockSize = new Dimension(45, 45);

    //temporary Variables
    private Color void_clr= new Color(59, 59, 59);
    private Color forest_clr= new Color(0, 99, 31);
    private Color empty_clr= new Color(251, 255, 145);

    public GamePanel() {
        this.rows = Constants.getMapSize();
        this.cols = Constants.getMapSize();
        this.btnGrid = new JButton[rows][cols];

        setLayout(new GridLayout(rows, cols));
        initializeGrid();
        mapGrid = GameState.getMapGrid();
    }

    private void initializeGrid() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                JButton block = new JButton();
                block.setPreferredSize(new Dimension(50, 50));
                block.setBackground(Color.LIGHT_GRAY);
                block.setPreferredSize(blockSize);

                // Optional: store coordinates or data
                int finalRow = row;
                int finalCol = col;
                block.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                       //
                    }
                });

                btnGrid[row][col] = block;
                add(block);
                printBlock(row, col);
            }
        }
    }

    public void printBlock (int x, int y){
        Block b = mapGrid[x][y];
        JButton btn = btnGrid[x][y];
        if (b instanceof VoidBlock){
            btn.setBackground(void_clr);
        }
        if (b instanceof EmptyBlock){
            btn.setBackground(empty_clr);
        }
        if (b instanceof ForestBlock){
            btn.setBackground(forest_clr);
        }

    }

    public JButton getBlock(int row, int col) {
        return btnGrid[row][col];
    }
}

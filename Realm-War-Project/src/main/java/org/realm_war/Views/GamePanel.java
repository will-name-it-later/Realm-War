package org.realm_war.Views;

import org.realm_war.Models.GameState;
import org.realm_war.Models.Position;
import org.realm_war.Models.Realm;
import org.realm_war.Models.blocks.Block;
import org.realm_war.Models.blocks.VoidBlock;
import org.realm_war.Models.structure.classes.Structure;
import org.realm_war.Models.structure.classes.TownHall;
import org.realm_war.Utilities.Constants;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private int rows;
    private int cols;
    private GameState gameState;
    private JButton[][] btnGrid;
    private Block[][] mapGrid;
    private final Dimension blockSize = new Dimension(45, 45);
    Position selectedPos;
    Realm selectedRealm; //todo : get selected Realm

    public GamePanel(GameState gameState) {
        this.rows = Constants.getMapSize();
        this.cols = Constants.getMapSize();
        this.btnGrid = new JButton[rows][cols];
        this.gameState = gameState;

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
                    if (block.getStructure() instanceof TownHall){
                        ImageIcon icon = new ImageIcon(getClass().getResource("/org/realm_war/Utilities/Resources/townhall.png"));
                        Image image = icon.getImage(); // Get the Image from the ImageIcon
                        Image scaledImage = image.getScaledInstance(45, 45, Image.SCALE_SMOOTH); // Resize
                        ImageIcon resizedIcon = new ImageIcon(scaledImage); // Wrap back into ImageIcon
                        blockButton.setIcon(resizedIcon);
                    }
                } else {
                    blockButton.setBackground(Constants.clr_3);  // fallback color
                }


                if (block instanceof VoidBlock) {
                    ImageIcon icon = new ImageIcon(getClass().getResource("/org/realm_war/Utilities/Resources/ban.jpg"));
                    Image scaled = icon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
                    blockButton.setIcon(new ImageIcon(scaled));
                    blockButton.setEnabled(false);
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

    public void refresh() {
        this.mapGrid = gameState.getMapGrid(); // reload after setupGame()
        removeAll();
        initializeGrid();
        revalidate();
        repaint();
    }

    public void handleBlockClick(int row , int col){
        System.out.println("Clicked block at (" + row + ", " + col + ")");
        selectedPos = mapGrid[row][col].getPosition();
    }

    public Position getSelectedPosition() {
        return selectedPos;
    }

    public String getSelectedRealm(){
        return selectedRealm.getName();
    }

    public GameState getGameState() {
        return gameState;
    }

    public void updateStructure(Structure s) {
        Position pos = s.getPosition();
        int x = pos.getX();
        int y = pos.getY();

        // Update model (game state)
        Block baseBlock = s.getBaseBlock();
        GameState.getMapGrid()[x][y] = baseBlock;
        baseBlock.setStructure(s);

        // Update UI
        JButton button = btnGrid[x][y];
        if (button != null) {
            // Update background color based on block (optional)
            button.setBackground(baseBlock.getColor());

            // Set appropriate structure icon
            ImageIcon icon = getIconForStructure(s);
            button.setIcon(icon);
        }
    }


    public ImageIcon getIconForStructure(Structure s) {
        String path = switch (s.getClass().getSimpleName()) {
            case "TownHall" -> "/org/realm_war/Utilities/Resources/townhall.png";
            case "Barrack" -> "/org/realm_war/Utilities/Resources/barrack.png";
            case "Farm" -> "/org/realm_war/Utilities/Resources/farm.png";
            case "Tower" -> "/org/realm_war/Utilities/Resources/tower.png";
            case "Market" -> "/org/realm_war/Utilities/Resources/market.png";
            default -> "/org/realm_war/Utilities/Resources/empty.png";
        };

        if (path == null) return null;

        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        Image img = icon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}

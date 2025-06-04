package org.realm_war.Views;

import org.realm_war.Models.GameState;
import org.realm_war.Models.Position;
import org.realm_war.Models.Realm;
import org.realm_war.Models.blocks.Block;
import org.realm_war.Models.blocks.VoidBlock;
import org.realm_war.Models.structure.classes.Structure;
import org.realm_war.Models.structure.classes.TownHall;
import org.realm_war.Models.units.Unit;
import org.realm_war.Utilities.Constants;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private int rows;
    private int cols;
    private GameState gameState;
    private InfoPanel infoPanel;
    private JButton[][] btnGrid;
    private Block[][] mapGrid;
    private final Dimension blockSize = new Dimension(45, 45);
    private Position selectedPos;
    private Position targetPos;

    public GamePanel(GameState gameState, InfoPanel infoPanel) {
        this.gameState = gameState;
        this.infoPanel = infoPanel;
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
                    blockButton.setBackground(block.getRealmByID(gameState.getRealms()) != null ? block.getRealmByID(gameState.getRealms()).getRealmColor() : block.getColor());
                    if (block.getStructure() instanceof TownHall) {
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
                    blockButton.addActionListener(e -> handleBlockClick(finalRow, finalCol));
                }

                btnGrid[row][col] = blockButton;
                add(blockButton);
            }
        }
    }

    public void refresh() {
        this.mapGrid = gameState.getMapGrid();// reload after setupGame()
        infoPanel.updateInfo(gameState);
        removeAll();
        initializeGrid();
        revalidate();
        repaint();
        selectedPos = targetPos = null;
    }

    public void handleBlockClick(int row, int col) {
        System.out.println("Clicked block at (" + row + ", " + col + ")");
        if (selectedPos == null) {
            System.out.println("selected position for action");
            selectedPos = mapGrid[row][col].getPosition();
            System.out.println(gameState.getUnitAt(selectedPos));
        } else {
            System.out.println("selection position for moving or attacking");
            targetPos = mapGrid[row][col].getPosition();
        }
    }

    public Position getSelectedPosition() {
        return selectedPos;
    }

    public Position getTargetPosition() {
        return targetPos;
    }

    public Realm getSelectedRealm() {
        return gameState.getCurrentRealm();
    }

    public int getSelectedRealmID() {
        return gameState.getCurrentRealm().getID();
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

    public void updateUnit(Unit unit) {
        Position pos = unit.getPosition();
        int x = pos.getX();
        int y = pos.getY();
        JButton button = btnGrid[x][y];
        if (button != null) {
            button.setBackground(gameState.getCurrentRealm().getRealmColor());
            ImageIcon icon = getIconForUnit(unit);
            button.setIcon(icon);
        }
        gameState.getCurrentRealm().updateResources();
        infoPanel.updateInfo(gameState);
        selectedPos = targetPos = null;
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

    public ImageIcon getIconForUnit(Unit unit) {
        String path = switch (unit.getClass().getSimpleName()) {
            case "Peasant" -> "/org/realm_war/Utilities/Resources/peasant.png";
            case "Swordsman" -> "/org/realm_war/Utilities/Resources/swordsman.png";
            case "Spearman" -> "/org/realm_war/Utilities/Resources/spearman.png";
            case "Knight" -> "/org/realm_war/Utilities/Resources/knight.png";
            default -> throw new IllegalStateException("Unexpected value: " + unit.getClass().getSimpleName());
        };

        if (path == null) return null;

        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        icon = new ImageIcon(icon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH));
        return icon;
    }
}

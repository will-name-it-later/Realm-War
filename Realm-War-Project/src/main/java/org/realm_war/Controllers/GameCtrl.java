package org.realm_war.Controllers;

import org.realm_war.Models.GameState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.realm_war.Models.Realm;
import org.realm_war.Models.blocks.*;
import org.realm_war.Models.structure.classes.*;
import org.realm_war.Models.units.*;
import org.realm_war.Utilities.GameLogger;
import org.realm_war.Utilities.PolymorphicTypeAdapterFactory;
import org.realm_war.Utilities.ColorTypeAdapter;
import org.realm_war.Utilities.DatabaseManager;
import org.realm_war.Views.GameFrame;

import java.awt.Color;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GameCtrl {
    private GameState gameState;
    private GameFrame gameFrame;
    private final Gson gson;

    public GameCtrl() {
        // For Blocks (Block -> ForestBlock, EmptyBlock, ...)
        PolymorphicTypeAdapterFactory<Block> blockAdapter = PolymorphicTypeAdapterFactory
                .of(Block.class, "type") // "type" is the field name added to JSON
                .withSubtype(EmptyBlock.class, "empty")
                .withSubtype(ForestBlock.class, "forest")
                .withSubtype(VoidBlock.class, "void");

        // For Structures (Structure -> Barrack, Farm, ...)
        PolymorphicTypeAdapterFactory<Structure> structureAdapter = PolymorphicTypeAdapterFactory
                .of(Structure.class, "type")
                .withSubtype(Barrack.class, "barrack")
                .withSubtype(Farm.class, "farm")
                .withSubtype(Market.class, "market")
                .withSubtype(Tower.class, "tower")
                .withSubtype(TownHall.class, "townhall");

        // 3. For Units (Unit -> Peasant, Knight, ...)
        PolymorphicTypeAdapterFactory<Unit> unitAdapter = PolymorphicTypeAdapterFactory
                .of(Unit.class, "type")
                .withSubtype(Peasant.class, "peasant")
                .withSubtype(Spearman.class, "spearman")
                .withSubtype(Swordsman.class, "swordsman")
                .withSubtype(Knight.class, "knight");

        // Build the Gson instance with our custom adapters
        this.gson = new GsonBuilder()
                .registerTypeAdapterFactory(blockAdapter)
                .registerTypeAdapterFactory(structureAdapter)
                .registerTypeAdapterFactory(unitAdapter)
                .registerTypeAdapter(Color.class, new ColorTypeAdapter())
                .setPrettyPrinting()
                .create();
    }

    private void rehydrateGameState(GameState gameState) {
        if (gameState == null || gameState.getMapGrid() == null || gameState.getRealms() == null) {
            return;
        }
        Block[][] mapGrid = gameState.getMapGrid();

        for (int i = 0; i < mapGrid.length; i++) {
            for (int j = 0; j < mapGrid[i].length; j++) {
                if (mapGrid[i][j] != null) {
                    mapGrid[i][j].setStructure(null);
                    mapGrid[i][j].setUnit(null);
                }
            }
        }

        for (Realm realm : gameState.getRealms()) {
            if (realm.getPossessedBlocks() != null) {
                for (Block possessedBlock : realm.getPossessedBlocks()) {
                    if (possessedBlock != null && possessedBlock.getPosition() != null) {
                        Block blockOnMap = gameState.getBlockAt(possessedBlock.getPosition());
                        if (blockOnMap != null) {
                            blockOnMap.setOwnerID(realm.getID());
                            blockOnMap.setOwnerColor(realm.getRealmColor());
                        }
                    }
                }
            }

            if (realm.getStructures() != null) {
                for (Structure structure : realm.getStructures()) {
                    if (structure != null && structure.getPosition() != null) {
                        Block blockOnMap = gameState.getBlockAt(structure.getPosition());
                        if (blockOnMap != null) {
                            blockOnMap.setStructure(structure);
                            structure.setBaseBlock(blockOnMap);
                        }
                    }
                }
            }
            if (realm.getUnits() != null) {
                for (Unit unit : realm.getUnits()) {
                    if (unit != null && unit.getPosition() != null) {
                        Block blockOnMap = gameState.getBlockAt(unit.getPosition());
                        if (blockOnMap != null) {
                            blockOnMap.setUnit(unit);
                        }
                    }
                }
            }
        }
    }


    public void saveGame(String saveName, GameState currentGameState, String DBName, String user, String password) throws SQLException {
        String gameStateJson = gson.toJson(currentGameState);
        String sql = "INSERT INTO saved_games (save_name, game_state_json) VALUES (?, ?::jsonb) " +
                "ON CONFLICT (save_name) DO UPDATE SET game_state_json = EXCLUDED.game_state_json;";
        String details = "The game " + saveName + " has been saved to the database.";

        try (Connection conn = DatabaseManager.getConnection(DBName, user, password);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, saveName);
            pstmt.setString(2, gameStateJson);
            pstmt.executeUpdate();

            GameLogger.logAction(1000, "SAVE", details);
        } catch (Exception e) {
            throw new SQLException("Failed to save game: " + e.getMessage(), e);
        }
    }

    public GameState loadGame(String saveName, String DBName, String user, String password) throws SQLException {
        String sql = "SELECT game_state_json FROM saved_games WHERE save_name = ?";
        String details = "The game " + saveName + " has been loaded from the database.";
        try (Connection conn = DatabaseManager.getConnection(DBName, user, password);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, saveName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String gameStateJson = rs.getString("game_state_json");
                GameState loadedState = gson.fromJson(gameStateJson, GameState.class);
                rehydrateGameState(loadedState);

                GameLogger.logAction(1000, "LOAD", details);
                return loadedState;
            }
        } catch (Exception e) {
            throw new SQLException("Failed to load game: " + e.getMessage(), e);
        }
        return null;
    }

    public void startGame(){
        gameState.setRunning(true);
    }

    public void stopGame(){
        gameState.setRunning(false);
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        if (gameFrame != null) {
            gameFrame.updateAllViews(this.gameState);
        }
    }

    public void setGameFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }
}

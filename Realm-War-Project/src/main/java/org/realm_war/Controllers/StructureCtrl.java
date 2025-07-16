package org.realm_war.Controllers;

import org.realm_war.Models.GameState;
import org.realm_war.Models.Position;
import org.realm_war.Models.Realm;
import org.realm_war.Models.blocks.Block;
import org.realm_war.Models.structure.classes.Structure;
import org.realm_war.Models.structure.classes.TownHall;
import org.realm_war.Models.units.Unit;
import org.realm_war.Views.InfoPanel;
import javax.swing.SwingUtilities;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StructureCtrl {
    private final List<Structure> structures;
    private GameState gameState;
    private transient Timer structureTimer;
    private static final int STRUCTURE_INTERVAL_MS = 5000; // 5 seconds
    private InfoPanel infoPanel;
    private Runnable onRealmRemoved;  // 👈 this is the callback

    public StructureCtrl(GameState gameState) {
        this.gameState = gameState;
        this.structures = gameState.getAllStructures();
    }

    /** Starts the async structure production and maintenance loop */
    public void startStructureLoop(GameState gameState) {
        if (structureTimer != null) {
            structureTimer.cancel();
        }
        structureTimer = new Timer(true); // Daemon thread, won't block exit

        structureTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("\n=== STRUCTURE LOOP TICK ===");

                List<Realm> realmsToClear = new ArrayList<>();

                for (Realm realm : gameState.getRealms()) {
                    for (Structure structure : new ArrayList<>(realm.getStructures())) {
                        if (structure.isDestroyed()) continue;

                        try {
                            structure.performTurnAction(realm, gameState);

                            int cost = structure.getMaintenanceCost();
                            realm.addGold(-cost);

                            if (realm.getGold() <= 0 && !realmsToClear.contains(realm)) {
                                realmsToClear.add(realm);
                                System.out.printf("[BANKRUPTCY] Realm %d is bankrupt and will be removed!%n", realm.getID());
                            }

                        } catch (Exception ex) {
                            System.err.printf("[ERROR] Structure error in Realm %d: %s%n", realm.getID(), ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                }

                if (infoPanel != null) {
                    SwingUtilities.invokeLater(() -> infoPanel.updateInfo(gameState));
                }

                for (Realm bankruptRealm : realmsToClear) {
                    removeRealmFromMap(bankruptRealm);
                    gameState.deactivateRealm(bankruptRealm);
                    if (onRealmRemoved != null) {
                        onRealmRemoved.run();
                    }
                }

                System.out.println("=== END LOOP TICK ===\n");
            }
        }, 0, STRUCTURE_INTERVAL_MS);


    }

    /** Stops the loop */
    public void stopStructureLoop() {
        if (structureTimer != null) {
            structureTimer.cancel();
            structureTimer = null;
        }
    }

    /** Remove a structure from the global list */
    public void removeStructure(Structure structure) {
        structures.remove(structure);
        gameState.getRealmByRealmID(structure.getKingdomId()).removeStructure(structure);
        gameState.getBlockAt(structure.getPosition()).setOwnerID(0);
        gameState.getBlockAt(structure.getPosition()).setStructure(null);
        gameState.getBlockAt(structure.getPosition()).setOwnerID(structure.getKingdomId());
    }



    public void removeRealmFromMap(Realm realm) {
        if (realm == null) return;

        int realmId = realm.getID();
        Block[][] map = gameState.getMapGrid();

        // 1. Remove all units from realm AND map
        for (Unit unit : new ArrayList<>(realm.getUnits())) {
            Position pos = unit.getPosition();
            if (gameState.isWithinBounds(pos)) {
                Block block = gameState.getBlockAt(pos);
                block.setUnit(null); // remove from map
            }
        }
        realm.getUnits().clear();

        // 2. Remove all structures from realm AND map
        for (Structure structure : new ArrayList<>(realm.getStructures())) {
            if (structure instanceof TownHall) continue;
            Position pos = structure.getPosition();
            if (gameState.isWithinBounds(pos)) {
                Block block = gameState.getBlockAt(pos);
                block.setStructure(null); // remove from map
            }
        }
        realm.getStructures().clear();

        // 4. Notify UI
        if (onRealmRemoved != null) {
            SwingUtilities.invokeLater(onRealmRemoved);
        }

        System.out.printf("[INFO] Realm %d was stripped of all assets due to lack of gold.%n", realmId);
    }


    public void setOnRealmRemoved(Runnable callback) {
        this.onRealmRemoved = callback;
    }

    public void setGameState(GameState gameState){
        this.gameState = gameState;
    }

    public void setInfoPanel(InfoPanel infoPanel) {
        this.infoPanel = infoPanel;
    }
}

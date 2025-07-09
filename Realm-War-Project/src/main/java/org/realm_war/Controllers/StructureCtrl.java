package org.realm_war.Controllers;

import org.realm_war.Models.GameState;
import org.realm_war.Models.Realm;
import org.realm_war.Models.structure.classes.Structure;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StructureCtrl {
    private final List<Structure> structures;
    private transient Timer structureTimer;
    private static final int STRUCTURE_INTERVAL_MS = 5000; // 5 seconds

    public StructureCtrl(List<Structure> structures) {
        this.structures = structures;
    }

    /** Starts the async structure production and maintenance loop */
    public void startStructureLoop(GameState gameState) {
        structureTimer = new Timer(true); // Daemon thread, won't block exit

        structureTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("\n=== STRUCTURE LOOP TICK ===");

                for (Realm realm : gameState.getRealms()) {
                    for (Structure structure : realm.getStructures()) {
                        if (structure.isDestroyed()) continue;

                        try {
                            // Perform structure action (e.g., produce food/gold/unit space)
                            structure.performTurnAction(realm, gameState);
                            System.out.printf("[ACTION] %s at %s (Realm %d, Level %d) performed action. Durability: %d%n",
                                    structure.getClass().getSimpleName(),
                                    structure.getPosition(),
                                    realm.getID(),
                                    structure.getLevel(),
                                    structure.getDurability()
                            );

                            // Deduct maintenance cost
                            int cost = structure.getMaintenanceCost();
                            realm.addGold(-cost);
                            System.out.printf("[MAINTENANCE] %s at %s deducted %d GOLD from Realm %d. Remaining Gold: %d%n",
                                    structure.getClass().getSimpleName(),
                                    structure.getPosition(),
                                    cost,
                                    realm.getID(),
                                    realm.getGold()
                            );

                            // Check for bankruptcy
                            if (realm.getGold() <= 0) {
                                System.out.printf("[BANKRUPTCY] Realm %d is bankrupt!%n", realm.getID());
                            }

                        } catch (Exception ex) {
                            System.err.printf("[ERROR] Failed in structure loop for %s: %s%n",
                                    structure.getClass().getSimpleName(),
                                    ex.getMessage());
                            ex.printStackTrace();
                        }
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
        }
    }

    /** Upgrade structure if possible */
    public boolean upgradeStructure(Structure structure, Realm realm) {
        int cost = getUpgradeCost(structure);
        if (structure.canLevelUp(structure) && realm.getGold() >= cost) {
            realm.addGold(-cost);
            structure.levelUp(structure);
            return true;
        }
        return false;
    }

    /** Computes the gold cost to level up the structure */
    public int getUpgradeCost(Structure structure) {
        return structure.getLevel() * 50;
    }

    /** Repair a structure for a cost */
    public boolean repairStructure(Structure structure, Realm realm, int repairAmount, int repairCost) {
        if (realm.getGold() >= repairCost) {
            realm.addGold(-repairCost);
            structure.setDurability(structure.getDurability() + repairAmount);
            return true;
        }
        return false;
    }

    /** Remove a structure from the global list */
    public void removeStructure(Structure structure) {
        structures.remove(structure);
    }

    public List<Structure> getStructures() {
        return structures;
    }
}

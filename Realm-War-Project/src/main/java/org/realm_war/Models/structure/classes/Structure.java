package org.realm_war.Models.structure.classes;


import org.realm_war.Models.GameState;
import org.realm_war.Models.Position;
import org.realm_war.Models.Realm;
import org.realm_war.Models.blocks.Block;

import java.util.Timer;
import java.util.TimerTask;

public abstract class Structure {
    private int level;
    private final int maxLevel;
    private int durability;
    protected int maintenanceCost;
    private Position position;
    private transient Block baseBlock;
    private int kingdomId;

    protected Timer actionTimer;
    protected boolean destroyed = false;


    public Structure(int maxLevel, int initialDurability, int maintenanceCost, org.realm_war.Models.Position position2, Block baseBlock, int kingdomId) {
        this.level = 1;
        this.maxLevel = maxLevel;
        this.durability = initialDurability;
        this.maintenanceCost = maintenanceCost;
        this.position =  position2;
        this.baseBlock = baseBlock;
        this.kingdomId = kingdomId;
    }

    public abstract boolean canLevelUp();
    public abstract void levelUp();
    public abstract void performTurnAction(Realm realm, GameState gameState);

    public void startStructureLoop(Realm realm, GameState gameState) {
        Timer structureTimer = new Timer(true); // Daemon timer
        structureTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Realm realm : gameState.getRealms()) {
                    for (Structure s : realm.getStructures()) {
                        try {
                            // Perform structure action
                            s.performTurnAction(realm, gameState);
                            System.out.printf(
                                    "[ACTION] %s at %s (Realm %d, Level %d) performed action. Durability: %d%n",
                                    s.getClass().getSimpleName(),
                                    s.getPosition(),
                                    s.getKingdomId(),
                                    s.getLevel(),
                                    s.getDurability()
                            );

                            // Deduct maintenance
                            int cost = s.getMaintenanceCost();
                            realm.addGold(-cost);
                            System.out.printf(
                                    "[MAINTENANCE] %s at %s deducted %d gold from Realm %d. Remaining Gold: %d%n",
                                    s.getClass().getSimpleName(),
                                    s.getPosition(),
                                    cost,
                                    realm.getID(),
                                    realm.getGold()
                            );

                            // Handle bankrupt realm
                            if (realm.getGold() <= 0) {
                                System.out.printf("[BANKRUPTCY] Realm %d is bankrupt!%n", realm.getID());
                            }

                        } catch (Exception ex) {
                            System.out.printf("[ERROR] Error in structure loop: %s%n", ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }, 0, 5000); // every 5 seconds
    }

    // Getters and setters
    public int getLevel() { return level; }
    protected void setLevel(int level) { this.level = level; }
    public int getMaxLevel() { return maxLevel; }
    public int getDurability() { return durability; }
    public void setDurability(int durability) { this.durability = durability; }
    public int getMaintenanceCost() { return maintenanceCost; }
    public Position getPosition() { return position; }
    public Block getBaseBlock() { return baseBlock; }
    public int getKingdomId() { return kingdomId; }
    public boolean isDestroyed(){
        return durability <= 0;
    }
    public void setBaseBlock(Block block) {
        this.baseBlock = block;
    }
}
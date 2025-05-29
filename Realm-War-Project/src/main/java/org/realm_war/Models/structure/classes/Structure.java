package org.realm_war.Models.structure.classes;

import org.realm_war.Models.Position;
import org.realm_war.Models.blocks.Block;

public abstract class Structure {
    private int level;
    private final int maxLevel;
    private int maintenanceCost;
    private int durability;
    private int buildingCost;
    private int levelUpCost;
    private int kingdomId;
    private Block baseBlock;
    private Position position;

    public Structure(int maxLevel,int maintenanceCost, int durability, int buildCost, int levelUpCost, int kingdomId,Block baseBlock, Position position ){
        this.level = 1;
        this.maxLevel = maxLevel;
        this.kingdomId = kingdomId;
        this.position = position;
        this.maintenanceCost = maintenanceCost;
        this.durability = durability;
        this.buildingCost = buildCost;
        this.levelUpCost = levelUpCost;
        this.baseBlock = baseBlock;
    }

    public Structure(int i, int j, int k, Position position2, Block baseBlock2, int kingdomId2) {
        //TODO Auto-generated constructor stub
    }

    public abstract boolean canLevelUp();
    public abstract void levelUp();
    public abstract void performTurnAction();

    //================================
    //GETTERS & SETTERS
    //================================

    public int getLevel() {
        return level;
    }
    protected void setLevel(int level) {
        this.level = level;
    }
    public int getMaxLevel() {
        return maxLevel;
    }
    public int getDurability() {
        return durability;
    }
    public void setDurability(int durability) {
        this.durability = durability;
    }
    public int getMaintenanceCost() {
        return maintenanceCost;
    }
    public Position getPosition() {
        return position;
    }
    public Block getBaseBlock() {
        return baseBlock;
    }
    public int getKingdomId() {
        return kingdomId;
    }
}

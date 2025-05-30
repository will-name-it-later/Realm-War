package org.realm_war.Models.structure.classes;

import org.realm_war.Models.Position;
import org.realm_war.Models.Realm;
import org.realm_war.Models.blocks.Block;

public class TownHall extends Structure {
    private int goldProductionPerLevel;
    private int foodProductionPerLevel;

    public TownHall(int goldProductionPerLevel, int foodProductionPerLevel,
            int maxLevel, int initialDurability, int maintenanceCost,
            Position position, Block baseBlock, int kingdomId) {

        super(maxLevel, initialDurability, maintenanceCost, position, baseBlock, kingdomId);
        this.goldProductionPerLevel = goldProductionPerLevel;
        this.foodProductionPerLevel = foodProductionPerLevel;
    }

    public int produceGoldPerTurn() {
        return goldProductionPerLevel * getLevel();
    }

    public int produceFoodPerTurn() {
        return foodProductionPerLevel * getLevel();
    }

    @Override
    public void performTurnAction(Realm realm) {
        // Resource production is handled in Realm.updateResources(),
        // so nothing needed here unless you're moving that logic into the structure
        // itself.
    }

    @Override
    public boolean canLevelUp() {
        return getLevel() < getMaxLevel();
    }

    @Override
    public void levelUp() {
        if (canLevelUp()) {
            setLevel(getLevel() + 1);
            setDurability(getDurability() + 50); // Example: increase durability on level up
        }
    }
}

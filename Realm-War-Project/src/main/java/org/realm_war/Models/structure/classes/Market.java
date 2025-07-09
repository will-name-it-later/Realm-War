package org.realm_war.Models.structure.classes;


import org.realm_war.Models.GameState;
import org.realm_war.Models.Position;
import org.realm_war.Models.Realm;
import org.realm_war.Models.blocks.Block;

public class Market extends Structure {
    private int goldProduction;

    public Market( Position position, Block baseBlock, int kingdomId) {
        super(3, 30,3, position, baseBlock, kingdomId);
        this.goldProduction = 8;
    }

    public int getGoldProduction() {
        return goldProduction;
    }

    @Override
    public int getMaintenanceCost() {
        return getLevel() * maintenanceCost;
    }

    @Override
    public void performTurnAction(Realm realm, GameState gameState) {
        int goldProduced = goldProduction;
        realm.addGold(goldProduced);
    }

    public int produceGoldPerTurn() {
        return getLevel() * goldProduction;
    }

    @Override
    public boolean canLevelUp(Structure structure) {
        return getLevel() < getMaxLevel() && structure instanceof Market;
    }

    @Override
    public void levelUp(Structure structure) {
        if (!canLevelUp(structure)){
            throw new IllegalStateException("Market is already in max level");
        }
        setLevel(getLevel()+1);
        goldProduction += 10 * getLevel();
    }
}
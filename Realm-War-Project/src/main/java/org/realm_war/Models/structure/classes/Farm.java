package org.realm_war.Models.structure.classes;


import org.realm_war.Models.GameState;
import org.realm_war.Models.Position;
import org.realm_war.Models.Realm;
import org.realm_war.Models.blocks.Block;
import org.realm_war.Utilities.Constants;

public class Farm extends Structure {
    private static final int[] FOOD_PRODUCTION_BY_LEVEL = {5, 8, 12};
    private static final int[] BUILDING_COST_BY_LEVEL = {5, 10, 15};
    private static final int[] DURABILITY_BY_LEVEL = {50, 75, 100};
    
    private int foodProduction;
    private int farmCount = 0;

    public Farm(Position position, Block baseBlock, int kingdomId) {
        super(3, DURABILITY_BY_LEVEL[0], 5, position, baseBlock, kingdomId);
        this.foodProduction = FOOD_PRODUCTION_BY_LEVEL[0];
    }

    @Override
    public boolean canLevelUp(Structure structure) {
        return getLevel() < getMaxLevel() && structure instanceof Farm;
    }

    @Override
    public void levelUp(Structure structure) {
        if (!canLevelUp(structure)) {
            throw new IllegalStateException("Farm is already at max level");
        }
        
        setLevel(getLevel() + 1);
        setDurability(DURABILITY_BY_LEVEL[getLevel() - 1]);
        this.foodProduction = FOOD_PRODUCTION_BY_LEVEL[getLevel() - 1];
    }

    //public int produceFoodPerTurn() {
    //    return getLevel() * 10; // Example logic: 10 food per level
    //}

    public int produceFood() {
        return 2 * getLevel();  // Example: 2 food per level every 5s
    }


    @Override
    public void performTurnAction(Realm realm, GameState gameState) {
        int food = FOOD_PRODUCTION_BY_LEVEL[getLevel() - 1];
        realm.addFood(food);
        System.out.println("Farm at " + getPosition() + " produced " + food + " food.");
    }

    public int getFoodProduction() {
        return foodProduction;
    }

    public int getUpgradeCost() {
        return BUILDING_COST_BY_LEVEL[getLevel() - 1];
    }

    public static int getBuildingCost(int farmsCount) {
        return 5 + (farmsCount * 5); // Each new farm costs 5 more than the previous
    }
}
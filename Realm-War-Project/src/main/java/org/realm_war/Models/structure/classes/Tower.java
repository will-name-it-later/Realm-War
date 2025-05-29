package org.realm_war.Models.structure.classes;

public class Tower extends Structure {

    public Tower(int buildCost) {
        this.level = 1;
        this.maxLevel = 3;
        this.durability = 50;
        this.maintenanceCost = 5;
        this.buildingCost = buildCost;
        this.levelUpCost = 5;
    }

    @Override
    public void levelUp() {
        if (level < maxLevel) {
            level++;
            durability += 10;
            levelUpCost += 5;
        } else {
            System.out.println("Tower is already at max level.");
        }
    }

    /**
     * Determines if a unit can pass this tower based on its level.
     * Level 1 org.realm_war.Models.blocks only peasants
     * Level 2 org.realm_war.Models.blocks peasants and spearmen
     * Level 3 org.realm_war.Models.blocks up to cavalry (as an example)
     */

    //public boolean canBlock(Unit unit) {
    //    int unitRank = unit.getRank(); // Assume: 1=Peasant, 2=Spearman, 3=Cavalry, etc.
    //    return unitRank <= level;
    //}
}

package org.realm_war.Models.structure.classes;

import java.util.List;

import org.realm_war.Controllers.UnitCtrl;
import org.realm_war.Models.GameState;
import org.realm_war.Models.Position;
import org.realm_war.Models.Realm;
import org.realm_war.Models.blocks.Block;
import org.realm_war.Models.units.Unit;
import org.realm_war.Utilities.GameLogger;

public class Tower extends Structure {
    private int attackRange = 3;
    private int attackPower;

    private static final int[] ATTACK_POWER_BY_LEVEL = {55, 58, 60};
    private static final int[] BUILDING_COST_BY_LEVEL = {10, 20, 30};
    private static final int[] DURABILITY_BY_LEVEL = {100, 150, 200};
    

    public Tower(Position position, Block baseBlock, int kingdomId) {
        super(3, DURABILITY_BY_LEVEL[0], 10, position, baseBlock, kingdomId);
        this.attackPower = ATTACK_POWER_BY_LEVEL[0];
    }

    @Override
    public boolean canLevelUp(Structure structure) {
        return getLevel() < getMaxLevel() && structure instanceof Tower;
    }

    @Override
    public void levelUp(Structure structure) {
        if (!canLevelUp(structure)) {
            throw new IllegalStateException("Tower is already at max level");
        }
        
        setLevel(getLevel() + 1);
        setDurability(DURABILITY_BY_LEVEL[getLevel() - 1]);
        this.attackPower = ATTACK_POWER_BY_LEVEL[getLevel() - 1];
    }
    @Override
    public void performTurnAction(Realm realm, GameState gameState) {
        List<Unit> allUnits = gameState.getAllUnits();
        Position myPosition = this.getPosition();
        String attackerType = this.getClass().getSimpleName();

        for (Unit unit : allUnits) {
            if (unit.getRealmID() != this.getKingdomId()){
                double distance = myPosition.distanceTo(unit.getPosition());
                if (distance <= attackRange) {
                    String defenderType = unit.getClass().getSimpleName();
                    String details = String.format("%s attacked %s that is in (%d, %d).", attackerType, defenderType, unit.getX(), unit.getY());
                    GameLogger.logAction(this.getKingdomId(), "TOWER_ATTACK", details);
                    unit.takeDamage(attackPower);
                    if (unit.isDead()) {
                        String killDetails = String.format("%s killed %s that was in (%d, %d).", attackerType, defenderType, unit.getX(), unit.getY());
                        GameLogger.logAction(this.getKingdomId(), "TOWER_KILL", killDetails);
                        gameState.removeUnitFromGame(unit);
                    }
                    break; // Attack one unit per turn
                }
            }
        }
    }
    
    public int getAttackPower() {
        return attackPower;
    }
    
    public int getUpgradeCost() {
        return BUILDING_COST_BY_LEVEL[getLevel() - 1];
    }
    
    public static int getBuildingCost(int towersCount) {
        return 10 + (towersCount * 10); // Each new tower costs 10 more than the previous
    }
}
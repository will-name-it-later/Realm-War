package org.realm_war.Controllers;

import org.realm_war.Models.GameState;
import org.realm_war.Models.Player;
import org.realm_war.Models.Position;
import org.realm_war.Models.Realm;
import org.realm_war.Models.blocks.Block;
import org.realm_war.Models.blocks.VoidBlock;
import org.realm_war.Models.structure.classes.Structure;
import org.realm_war.Models.structure.classes.Tower;
import org.realm_war.Models.structure.classes.TownHall;
import org.realm_war.Models.units.Unit;
import org.realm_war.Utilities.GameLogger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class UnitCtrl {
    private final List<Unit> units;
    private Unit selectedUnit;
    private Block targetBlock;

    private GameState gameState;

    public UnitCtrl(GameState gameState) {
        units = new ArrayList<Unit>();
        this.gameState = gameState;
    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public void setSelectedUnit(Unit u) {
        this.selectedUnit = u;
    }

    public Block getTargetBlock() {
        return targetBlock;
    }

    public void setTargetBlock(Block b) {
        this.targetBlock = b;
    }

    public void addUnit(Unit unit) {
        units.add(unit);
        gameState.getRealms().get(unit.getRealmID() - 1001).possessBlock(gameState.getBlockAt(unit.getPosition()), false);
        gameState.getRealms().get(unit.getRealmID() - 1001).addUnit(unit);
        gameState.getBlockAt(unit.getPosition()).setUnit(unit);
    }

    public void removeUnit(Unit unit) {
        units.remove(unit);
        gameState.removeUnitFromGame(unit);
        gameState.getRealmByRealmID(unit.getRealmID()).removeUnit(unit);
        gameState.getBlockAt(unit.getPosition()).setOwnerID(0);
        gameState.getBlockAt(unit.getPosition()).setUnit(null);
        gameState.getBlockAt(unit.getPosition()).setOwnerID(unit.getRealmID());
        //update everything
        gameState.getRealmByRealmID(unit.getRealmID()).updateResources(gameState);
    }

    public boolean isPlayerTurn(Player player) {
        return gameState.getCurrentPlayer().equals(player);
    }

    public void moveUnitToBlock(Unit unit, Block targetBlock) {
        if (unit.getPosition().getX() == targetBlock.getX() && unit.getPosition().getY() == targetBlock.getY()) {
            return;
        }
        if (targetBlock.getPosition().distanceTo(unit.getPosition()) > unit.getMovementRange()) {
            throw new IllegalArgumentException("Target block is out of movement range");
        }
        if (!targetBlock.hasUnit()) {
            if (!targetBlock.hasStructure()) {
                String unitType = unit.getClass().getSimpleName();
                String details = String.format("%s moved from (%d, %d) to (%d, %d)", unitType,
                        unit.getX(), unit.getY(), targetBlock.getX(), targetBlock.getY());

                claimBlocks(unit.getPosition(), targetBlock.getPosition(), unit.getRealmID());
                removeUnit(unit);
                targetBlock.setUnit(unit);
                unit.setPosition(targetBlock.getPosition());
                addUnit(unit);

                GameLogger.logAction(unit.getRealmID(), "MOVE", details);
            }
        } else {
            if (unit.canMerge(targetBlock.getUnit())) {
                String unitType = unit.getClass().getSimpleName();
                Unit targetUnit = targetBlock.getUnit();
                removeUnit(unit);
                removeUnit(targetUnit);

                Unit mergedUnit = unit.merge(targetUnit);
                targetBlock.setUnit(mergedUnit);
                mergedUnit.setPosition(targetBlock.getPosition());
                addUnit(mergedUnit);


                String mergedUnitType = mergedUnit.getClass().getSimpleName();
                String details = String.format("%s merged to %s", unitType, mergedUnitType);

                GameLogger.logAction(unit.getRealmID(), "MERGE", details);
            }
        }
    }

    public void attack(Block attackerBlock, Block targetBlock) {
        Unit attackerUnit, targetUnit;
        Structure attackerStructure, targetStructure;
        String attackerType = attackerBlock.hasUnit() ? attackerBlock.getUnit().getClass().getSimpleName() : attackerBlock.hasStructure() ? attackerBlock.getStructure().getClass().getSimpleName() : "";
        String defenderType = targetBlock.hasUnit() ? targetBlock.getUnit().getClass().getSimpleName() : targetBlock.hasStructure() ? targetBlock.getStructure().getClass().getSimpleName() : "";

        //check if the attacker block is empty of any unit
        if (!attackerBlock.isOccupied()) {
            throw new IllegalArgumentException("please choose a unit!");
        }

        //check if player is attempting an attack in their own territory
        if (attackerBlock.getRealmID() == targetBlock.getRealmID()) {
            throw new IllegalArgumentException("You can't attempt an attack in your own territory!");
        }

        if (attackerBlock.hasUnit()) {
            attackerUnit = attackerBlock.getUnit();
            if (targetBlock.hasUnit()) {
                targetUnit = targetBlock.getUnit();
                if (attackerUnit.canAttackUnit(targetUnit)) {
                    String details = String.format("%s attacked to %s that is in (%d, %d).", attackerType, defenderType, targetBlock.getX(), targetBlock.getY());
                    GameLogger.logAction(attackerUnit.getRealmID(), "ATTACK", details);

                    removeUnit(targetUnit);
                    moveUnitToBlock(attackerUnit, targetBlock);
                } else if (attackerUnit.getPosition().distanceTo(targetBlock.getPosition()) > attackerUnit.getMovementRange()) {
                    throw new IllegalArgumentException("Target is out of attack range!");
                } else {
                    throw new IllegalArgumentException("Your unit isn't vicious enough to attack this target!");
                }
            } else if (targetBlock.hasStructure()) {
                targetStructure = targetBlock.getStructure();
                if (attackerUnit.getPosition().distanceTo(targetStructure.getPosition()) <= attackerUnit.getMovementRange()) {
                    targetStructure.setDurability(targetStructure.getDurability() - attackerUnit.getAttackPower());
                    String details = String.format("%s attacked to %s that is in (%d, %d).", attackerType, defenderType, targetBlock.getX(), targetBlock.getY());
                    GameLogger.logAction(attackerUnit.getRealmID(), "ATTACK_TO_STRUCTURE", details);

                    if (targetStructure.isDestroyed()) {
                        String destroyDetails = String.format("%s in (%d, %d) destroyed %s that was in (%d, %d).", attackerType, attackerUnit.getX(), attackerUnit.getY(), defenderType, targetBlock.getX(), targetBlock.getY());
                        GameLogger.logAction(attackerUnit.getRealmID(), "DESTROY_STRUCTURE", destroyDetails);
                        if (targetStructure instanceof TownHall) {
                            Realm conqueredRealm = gameState.getRealmByRealmID(targetBlock.getRealmID());
                            gameState.conquerRealm(conqueredRealm);
                            moveUnitToBlock(attackerUnit, targetBlock);
                        }else{
                            gameState.getStructureCtrl().removeStructure(targetStructure);
                            moveUnitToBlock(attackerUnit, targetBlock);
                        }
                    }
                } else {
                    throw new IllegalArgumentException("Target is out of attack range!");
                }
            } else {
                throw new IllegalArgumentException("nothing to attack!");
            }
        }
    }

    public void claimBlocks(Position start, Position end, int ID) {
        int dx = end.getX() - start.getX();
        int dy = end.getY() - start.getY();
        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        for (int i = 1; i <= steps; i++) {
            int x = Math.round(start.getX() + i * dx / (float) steps);
            int y = Math.round(start.getY() + i * dy / (float) steps);
            if (x >= 0 && x < gameState.getMapGrid()[0].length && y >= 0 && y < gameState.getMapGrid().length) {
                Block b = gameState.getMapGrid()[x][y];
                if (!(b instanceof VoidBlock)) {
                    int oldOwnerID = b.getRealmID();
                    if (oldOwnerID != 0 && oldOwnerID != ID) {
                        Realm oldRealm = gameState.getRealmByRealmID(oldOwnerID);
                        if (oldRealm != null) {
                            oldRealm.getPossessedBlocks().remove(b);
                        }
                    }
                    b.setOwnerID(ID);
                    gameState.getRealmByRealmID(ID).possessBlock(b, true);
                }
            }
        }
    }


    public List<Unit> getAllUnits() {
        return units;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}

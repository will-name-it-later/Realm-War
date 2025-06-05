package org.realm_war.Controllers;

import org.realm_war.Models.GameState;
import org.realm_war.Models.Player;
import org.realm_war.Models.Position;
import org.realm_war.Models.blocks.Block;
import org.realm_war.Models.blocks.ForestBlock;
import org.realm_war.Models.units.Unit;

import java.util.ArrayList;
import java.util.List;

public class UnitCtrl {
    private final List<Unit> units;
    private Unit selectedUnit;
    private Block targetBlock;

    private GameState gameState = new GameState();

    public UnitCtrl() {
        units = new ArrayList<Unit>();
    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }
    public void setSelectedUnit (Unit u){
        this.selectedUnit = u;
    }
    public Block getTargetBlock() {
        return targetBlock;
    }
    public void setTargetBlock (Block b){
        this.targetBlock = b;
    }

    public void addUnit(Unit unit) {
        units.add(unit);
    }

    public boolean isPlayerTurn(Player player) {
        return gameState.getCurrentPlayer().equals(player);
    }

    public void moveUnitToBlock(Unit unit, Block targetBlock) {
        if (targetBlock.getPosition().distanceTo(unit.getPosition()) > unit.getMovementRange()) {
            throw new IllegalArgumentException("Target block is out of movement range");
        }
        Block currentBlock = gameState.getBlockAt(unit.getPosition());
        if (currentBlock != null) {
            currentBlock.setUnit(null);
            currentBlock.setOwnerID(unit.getRealmID());
        }
        targetBlock.setUnit(unit);
        unit.setPosition(targetBlock.getPosition());
    }

    public void attackUnit(Unit attacker, Unit defender) {
        int attackPower = attacker.getAttackPower();

        Block attackerBlock = gameState.getBlockAt(attacker.getPosition());
        Block defenderBlock = gameState.getBlockAt(defender.getPosition());

        if (attackerBlock instanceof ForestBlock) attackPower += 2;
        if (defenderBlock instanceof ForestBlock) attackPower -= 1;

        defender.takeDamage(attackPower);
    }

    //todo: We don't need the methods that I commented. They can be deleted.

    /* public void moveUnit(Unit unit, int x, int y) {
        Position pos = new Position(x, y);
        if (pos.distanceTo(unit.getPosition()) > unit.getMovementRange()) {
            throw new IllegalArgumentException("Position is out of range for this unit!");
        }
        if (containsEnemyUnit(unit, x, y)){
            Unit target = getEnemyUnitAt(unit, x, y);
            if (!unit.canAttackUnit(target)) {
                throw new IllegalArgumentException("Can't Attack this Unit!");
            }else{
                removeUnit(target);
                unit.setPosition(pos);
            }
        }
    }

    public boolean containsEnemyUnit(Unit unit, int x, int y) {
        for (Unit u : units) {
            int X = u.getPosition().getX();
            int Y = u.getPosition().getY();
            if (X == x && Y == y && u.getRealmID() != unit.getRealmID()) {
                return true;
            }
        }
        return false;
    }

    public Unit getEnemyUnitAt(Unit unit, int x, int y){
        if (containsEnemyUnit(unit, x, y)) {
            for (Unit u : units) {
                int X = u.getPosition().getX();
                int Y = u.getPosition().getY();
                if (X == x && Y == y && u.getRealmID() != unit.getRealmID()) {
                    return u;
                }
            }
        }
        throw new UnsupportedOperationException("Can not get any enemy unit at this position!");
    }

     */

    public void removeUnit(Unit unit) {
        units.remove(unit);
    }

    public List<Unit> getAllUnits() {
        return units;
    }
}

package org.realm_war.Controllers;

import org.realm_war.Models.GameState;
import org.realm_war.Models.Player;
import org.realm_war.Models.Position;
import org.realm_war.Models.blocks.Block;
import org.realm_war.Models.blocks.ForestBlock;
import org.realm_war.Models.units.Unit;

import javax.swing.*;
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

    public void attackUnit(Unit attacker, Block targetBlock) {
        // Validate input
        if (attacker == null || targetBlock == null || targetBlock.getUnit() == null) {
            throw new IllegalArgumentException("Invalid attacker or target.");
        }

        Unit defender = targetBlock.getUnit();

        // Check for friendly fire
        if (attacker.getOwner().equals(defender.getOwner())) {
            throw new IllegalArgumentException("Cannot attack your own unit.");
        }

        // Check attack range
        if (attacker.getPosition().distanceTo(defender.getPosition()) > attacker.getAttackRange()) {
            throw new IllegalArgumentException("Target is out of attack range.");
        }

        // Perform attack
        defender.takeDamage(defender.getHitPoint() - attacker.getAttackPower());

        // Check if defender is defeated
        if (defender.getHitPoint() <= 0) {
            // Remove defender
            targetBlock.setUnit(null);
        }
        //else {
            // Optionally: counterattack logic or effects could go here
        //}
    }


    public void removeUnit(Unit unit) {
        units.remove(unit);
    }

    public List<Unit> getAllUnits() {
        return units;
    }
}

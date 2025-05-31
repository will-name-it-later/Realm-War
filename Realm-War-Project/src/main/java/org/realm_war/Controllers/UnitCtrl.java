package org.realm_war.Controllers;

import org.realm_war.Models.Position;
import org.realm_war.Models.units.Unit;

import java.util.ArrayList;
import java.util.List;

public class UnitCtrl {
    private final List<Unit> units;

    public UnitCtrl() {
        units = new ArrayList<Unit>();
    }

    public void addUnit(Unit unit) {
        units.add(unit);
    }

    public void moveUnit(Unit unit, int x, int y) {
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
            if (X == x && Y == y && !u.getRealm().equals(unit.getRealm())) {
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
                if (X == x && Y == y && !u.getRealm().equals(unit.getRealm())) {
                    return u;
                }
            }
        }
        throw new UnsupportedOperationException("Can not get any enemy unit at this position!");
    }

    public void removeUnit(Unit unit) {
        units.remove(unit);
    }

    public List<Unit> getAllUnits() {
        return units;
    }
}

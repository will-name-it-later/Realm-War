package org.realm_war.Models.units;

import org.realm_war.Models.Position;

public class Knight extends Unit {
    public Knight(Position position, String realm) {
        super(125, 3, 32, 1, 15, 5, 2, position, realm);
    }

    @Override
    public boolean canMerge(Unit otherUnit) {
        return false;
    }

    @Override
    public Unit merge(Unit otherUnit) {
        throw new UnsupportedOperationException("Knights have the highest ranks, they can't be merged!");
    }

    @Override
    public boolean canAttackUnit(Unit unit) {
        return !(unit instanceof Knight); //knights can attack and kill all units except themselves
    }
}

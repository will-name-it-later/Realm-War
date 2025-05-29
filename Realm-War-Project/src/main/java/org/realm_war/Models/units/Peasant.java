package org.realm_war.Models.units;

import org.realm_war.Models.Position;

public class Peasant extends Unit{
    public Peasant(Position position, String realm) {
        super(53, 1, 12, 1, 5, 1, 1, position, realm);
    }

    @Override
    public boolean canMerge(Unit otherUnit) {
        return otherUnit instanceof Peasant && otherUnit.getRealm().equals(getRealm());
    }

    @Override
    public Unit merge(Unit otherUnit) {
        if (canMerge(otherUnit)) {
            throw new IllegalArgumentException("Unable to Merge Units!");
        }else return new Swordsman(this.getPosition(), this.getRealm());
    }
}
package org.entities;

import org.util.Position;

public class Swordsman extends Unit implements UnitInterface{
    private double HIT_POINT = 46;
    private final double MOVEMENT_RANGE = 3;
    private final double ATTACK_POWER = 10;
    private final double ATTACK_RANGE = 4;
    private final double PAYMENT = 9;
    private final double RATION = 4;
    private final int UNIT_SPACE = 1;

    public Swordsman(Position position, int kingdomID) {
        super(position, kingdomID);
    }

    public void move(){}
    public void consumeFood(){}
    public void consumeGold(){};

    @Override
    public boolean canMerge(Unit otherUnit) {
        return (otherUnit instanceof Swordsman) && (this.getKingdomID() == otherUnit.getKingdomID());
    }

    @Override
    public Unit merge(Unit otherUnit) {
        if(canMerge(otherUnit)) return new Knight(this.getPosition(), this.getKingdomID());
        else throw new IllegalArgumentException("Can't merge other unit");
    }
}

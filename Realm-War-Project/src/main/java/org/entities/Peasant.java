package org.entities;

import org.util.Position;

public class Peasant extends Unit implements UnitInterface{
    private double HIT_POINT = 22;
    private final double MOVEMENT_RANGE = 2;
    private final double ATTACK_POWER = 3;
    private final double ATTACK_RANGE = 1;
    private final double PAYMENT = 2;
    private final double RATION = 2;
    private final int UNIT_SPACE = 1;

    public Peasant(Position position, int kingdomID) {
        super(position, kingdomID);
    }

    public void move(){}
    public void consumeFood(){}
    public void consumeGold(){};

    @Override
    public boolean canMerge(Unit otherUnit) {
        return (otherUnit instanceof Peasant) && otherUnit.getKingdomID() == this.getKingdomID();
    }

    @Override
    public Unit merge(Unit otherUnit) {
        if (canMerge(otherUnit)) return new Spearman(this.getPosition(), this.getKingdomID());
        else throw new IllegalArgumentException("Can't merge with other unit");
    }
}
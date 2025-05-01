package org.entities;

import org.util.Position;
public class Spearman extends Unit implements UnitInterface {
    private double HIT_POINT = 35;
    private final double MOVEMENT_RANGE = 2;
    private final double ATTACK_POWER = 8;
    private final double ATTACK_RANGE = 6;
    private final double PAYMENT = 7;
    private final double RATION = 3;
    private final int UNIT_SPACE = 1;

    public Spearman(Position position, int kingdomID) {
        super(position, kingdomID);
    }

    public void move(){}
    public void consumeFood(){}
    public void consumeGold(){};

    @Override
    public boolean canMerge(Unit otherUnit){
        return (otherUnit instanceof Spearman) && otherUnit.getKingdomID() == this.getKingdomID();
    }

    @Override
    public Unit merge(Unit otherUnit) {
        if(canMerge(otherUnit)) return new Swordsman(this.getPosition(), this.getKingdomID());
        else throw new IllegalArgumentException("Can't merge other unit");
    }
}

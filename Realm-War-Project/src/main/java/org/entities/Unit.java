package org.entities;

import org.util.Position;

public abstract class Unit {
    private double HIT_POINT;
    private double MOVEMENT_RANGE;
    private double ATTACK_POWER;
    private double ATTACK_RANGE;
    private double PAYMENT;
    private double RATION;
    private int UNIT_SPACE;
    private final int KINGDOM_ID;
    private Position POSITION;

    public Unit(Position position, int kingdomID) {
        this.POSITION = position;
        this.KINGDOM_ID = kingdomID;
    }

    public double getHitPoint() {
        return HIT_POINT;
    }
    public double getMovementRange() {
        return MOVEMENT_RANGE;
    }
    public double getAttackPower() {
        return ATTACK_POWER;
    }
    public double getAttackRange() {
        return ATTACK_RANGE;
    }
    public double getPayment() {
        return PAYMENT;
    }
    public double getRation() {
        return RATION;
    }
    public int getUnitSpace() {
        return UNIT_SPACE;
    }
    public int getKingdomID() {return KINGDOM_ID;}
    public Position getPosition() {return POSITION;}

    public void getHit(Unit unit){
        HIT_POINT -= unit.getHitPoint();
    }

    public void attack(Unit unit){
        unit.getHit(this);
    }

    public abstract boolean canMerge(Unit otherUnit);
    public abstract Unit merge(Unit otherUnit);
}
package org.realm_war.Models.units;

import org.realm_war.Models.Position;

public abstract class Unit {
    private double HIT_POINT;
    private double MOVEMENT_RANGE;
    private double ATTACK_POWER;
    private double ATTACK_RANGE;
    private double PAYMENT;
    private double RATION;
    private int UNIT_SPACE;
    private Position position;
    private String realm;

    public Unit(double HIT_POINT, double MOVEMENT_RANGE, double ATTACK_POWER, double ATTACK_RANGE, double PAYMENT, double RATION, int UNIT_SPACE, Position position, String realm) {
        this.HIT_POINT = HIT_POINT;
        this.MOVEMENT_RANGE = MOVEMENT_RANGE;
        this.ATTACK_POWER = ATTACK_POWER;
        this.ATTACK_RANGE = ATTACK_RANGE;
        this.PAYMENT = PAYMENT;
        this.RATION = RATION;
        this.UNIT_SPACE = UNIT_SPACE;
        this.position = position;
        this.realm = realm;
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

    public String getRealm() {
        return realm;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public abstract boolean canMerge(Unit otherUnit);

    public abstract Unit merge(Unit otherUnit);
}
package org.realm_war.Models.units;

import org.realm_war.Models.Position;

public abstract class Unit {
    private int HIT_POINT;
    private int MOVEMENT_RANGE;
    private int ATTACK_POWER;
    private int ATTACK_RANGE;
    private int PAYMENT;
    private int RATION;
    private int UNIT_SPACE;
    private Position position;
    private String realm;

    public Unit(int HIT_POINT, int MOVEMENT_RANGE, int ATTACK_POWER, int ATTACK_RANGE, int PAYMENT, int RATION, int UNIT_SPACE, Position position, String realm) {
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

    public int getHitPoint() {
        return HIT_POINT;
    }

    public int getMovementRange() {
        return MOVEMENT_RANGE;
    }

    public int getAttackPower() {
        return ATTACK_POWER;
    }

    public int getAttackRange() {
        return ATTACK_RANGE;
    }

    public int getPayment() {
        return PAYMENT;
    }

    public int getRation() {
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
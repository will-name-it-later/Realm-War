package org.entities;

public abstract class Unit {
    private final double HIT_POINT;
    private final double MOVEMENT_RANGE;
    private final double ATTACK_POWER;
    private final double ATTACK_RANGE;
    private final double PAYMENT;
    private final double RATION;
    private final int UNIT_SPACE;

    public Unit(double HIT_POINT, double MOVEMENT_RANGE, double ATTACK_POWER, double ATTACK_RANGE, double PAYMENT, double RATION, int UNIT_SPACE) {
        this.HIT_POINT = HIT_POINT;
        this.MOVEMENT_RANGE = MOVEMENT_RANGE;
        this.ATTACK_POWER = ATTACK_POWER;
        this.ATTACK_RANGE = ATTACK_RANGE;
        this.PAYMENT = PAYMENT;
        this.RATION = RATION;
        this.UNIT_SPACE = UNIT_SPACE;
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
}

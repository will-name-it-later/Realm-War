package org.entities;

public abstract class Unit {
    private double HIT_POINT;
    private double MOVEMENT_RANGE;
    private double ATTACK_POWER;
    private double ATTACK_RANGE;
    private double PAYMENT;
    private double RATION;
    private int UNIT_SPACE;

    public Unit(double HIT_POINT, double MOVEMENT_RANGE, double ATTACK_POWER, double ATTACK_RANGE, double PAYMENT, double RATION, int UNIT_SPACE) {
        this.HIT_POINT = HIT_POINT;
        this.MOVEMENT_RANGE = MOVEMENT_RANGE;
        this.ATTACK_POWER = ATTACK_POWER;
        this.ATTACK_RANGE = ATTACK_RANGE;
        this.PAYMENT = PAYMENT;
        this.RATION = RATION;
        this.UNIT_SPACE = UNIT_SPACE;
    }

}

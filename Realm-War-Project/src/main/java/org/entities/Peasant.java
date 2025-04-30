package org.entities;

public class Peasant extends Unit implements UnitInterface{
    private double HIT_POINT = 5;
    private final double MOVEMENT_RANGE = 2;
    private final double ATTACK_POWER = 1;
    private final double ATTACK_RANGE = 1;
    private final double PAYMENT = 2;
    private final double RATION = 2;
    private final int UNIT_SPACE = 1;

    @Override
    public void getHit(Unit unit) {
        HIT_POINT -= unit.getAttackPower();
    }


    public void attack() {}

    public void attack(Unit unit) {
        unit.getHit(this);
    }

    public void move(){}

    public void defend(){}

    public void consumeFood(){

    }

    public void consumeGold(){};

    public void fillSpace(){};

    public void levelUp(){};
}
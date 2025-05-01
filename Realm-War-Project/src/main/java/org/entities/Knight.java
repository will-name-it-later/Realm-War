package org.entities;

import org.util.Position;

public class Knight extends Unit implements UnitInterface {
    private double HIT_POINT = 75;
    private final double MOVEMENT_RANGE = 5;
    private final double ATTACK_POWER = 14;
    private final double ATTACK_RANGE = 3;
    private final double PAYMENT = 14;
    private final double RATION = 6;
    private final int UNIT_SPACE = 2;
    private int level;

    public Knight(Position position, int kingdomID) {
        super(position, kingdomID);
    }

    public void consumeFood() {}
    public void consumeGold(){}
    public void move() {}

    @Override
    //knights are the highest rank, thus unable to merge with each other
    public boolean canMerge(Unit otherUnit) {
        return false;
    }

    @Override
    public Unit merge(Unit otherUnit) {
        throw new UnsupportedOperationException("Knights can't merge with each other");
    }
}

package org.realm_war.Models.blocks;

import org.realm_war.Models.Position;
import org.realm_war.Models.structure.classes.Structure;
import org.realm_war.Models.units.Unit;

import java.awt.*;

public abstract class Block {
    private Position position;
    private Unit unit;
    private Structure structure;
    private boolean isAbsorbed;
    private String realmName; // P for not absorbed.
    public Block(Position position) {
        this.position = position;
        this.isAbsorbed = false;
        this.realmName = "P";
    }

    public abstract boolean canBuildStructure();
    public abstract int getResourceItem(String resourceType);

    public abstract Color getColor();

    public Position getPosition() { return position; }
    public boolean isAbsorbed() { return isAbsorbed; }
    public String getRealmName() { return realmName; }

    public void setAbsorbed(boolean isAbsorbed, String realmName) {
        this.isAbsorbed = isAbsorbed;
        this.realmName = realmName;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public boolean hasStructure() {
        return structure != null;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public boolean hasUnit() {
        return unit != null;
    }

    public boolean isOccupied() {
        return unit != null || structure != null;
    }
}

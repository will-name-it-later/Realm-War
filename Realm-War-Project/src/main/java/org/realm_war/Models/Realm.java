package org.realm_war.Models;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.realm_war.Models.blocks.Block;
import org.realm_war.Models.structure.classes.Structure;
import org.realm_war.Models.structure.classes.TownHall;
import org.realm_war.Models.units.Unit;

public class Realm {
    private int gold;
    private final int ID;
    private TownHall townHall;
    private int food;
    private List<Structure> structures;
    private List<Unit> units;
    private List<Block> possessedBlocks;
    private int allUnitSpace;
    private int availableUnitSpace;

    private Color realmColor;

    public Realm(int ID) {
        this.ID = ID;
        structures = new ArrayList<>();
        units = new ArrayList<>();
        possessedBlocks = new ArrayList<>();
        this.townHall = null;
        this.allUnitSpace = 5;
        this.gold = 25;
        this.food = 25;
    }

    public boolean canBuildStructure(Structure s){
        if (s.getMaintenanceCost() > this.gold) {
            return false;
        }
        if (!this.possessedBlocks.contains(s.getBaseBlock())) {
            return false;
        }
        return true;
    }

    public void addStructure(Structure s){
        if (canBuildStructure(s)) {
            structures.add(s);
            gold -= s.getMaintenanceCost();
        } else {
            throw new IllegalArgumentException("Can't build structure here!");
        }
    }


    public void mergeUnit(Unit u, Unit target){
        availableUnitSpace -= target.getUnitSpace();
        units.remove(target);
        if (availableUnitSpace + u.getUnitSpace() > allUnitSpace){
            throw new IllegalArgumentException("insufficient space!");
        }
        units.add(u);
        availableUnitSpace += u.getUnitSpace();
    }

    public void removeUnit(Unit u){
        units.remove(u);
        availableUnitSpace -= u.getUnitSpace();
    }

    public void possessBlock(Block b, boolean grantUnitSpace) {
        b.setAbsorbed(true, this.ID);
        if (!possessedBlocks.contains(b)) {
            possessedBlocks.add(b);
            if (grantUnitSpace) {
                allUnitSpace++;
            }
        }
    }

    public Color getRealmColor (){
        return realmColor;
    }

    public void setTownHall(TownHall t){
        this.townHall = t;
    }

    public void setRealmColor(Color c){
        this.realmColor=c;
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int amount) {
        this.gold += amount;
    }

    public int getFood() {
        return food;
    }

    public void addFood(int amount) {
        this.food += amount;
    }

    public int getID() {
        return ID;
    }


    public List<Structure> getStructures() {
        return structures;
    }

    public List<Unit> getUnits() {
        return units;
    }


    public void setAllUnitSpace(int unitSpace){
       this.allUnitSpace = unitSpace;
    }
    public int getAvailableUnitSpace() {
        return availableUnitSpace;
    }

    public void setAvailableUnitSpace(int amount){
        this.availableUnitSpace += amount;
    }

    public int getAllUnitSpace() {
        return allUnitSpace;
    }

    public List<Block> getPossessedBlocks() {
        return possessedBlocks;
    }

}

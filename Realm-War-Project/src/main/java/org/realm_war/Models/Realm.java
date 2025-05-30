package org.realm_war.Models;

import java.util.ArrayList;
import java.util.List;

import org.realm_war.Models.blocks.Block;
import org.realm_war.Models.structure.classes.Structure;
import org.realm_war.Models.structure.classes.TownHall;
import org.realm_war.Models.units.Unit;

public class Realm {
    private int gold;
    private final String name;
    private TownHall townHall;
    private int food;
    private List<Structure> structures;
    private List<Unit> units;
    private List<Block> possessedBlocks;
    private int allUnitSpace;
    private int usedUnitSpace;

    public Realm(String name, TownHall townHall) {
        this.name = name;
        structures = new ArrayList<>();
        units = new ArrayList<>();
        possessedBlocks = new ArrayList<>();
        this.townHall = townHall;
        this.allUnitSpace = 5;
        this.gold = 25;
        this.food = 25;
    }

    public void updateResources(){
        for (Structure s : structures){
            s.performTurnAction(this);
        }

        for (Structure s : structures){
            gold -= s.getMaintenanceCost();
        }

        for (Block b : possessedBlocks){
            gold += b.getResourceItem("gold");
            food += b.getResourceItem("food");
        }

        for (Unit u : units){
            gold -= u.getPayment();
            food -= u.getRation();
        }
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
        if (canBuildStructure(s)){
            structures.add(s);
            gold -= s.getMaintenanceCost();
            possessedBlocks.remove(s.getBaseBlock());
        }else throw new IllegalArgumentException("Can't build structure here!");
    }

    public void addUnit(Unit u){
        if (usedUnitSpace + u.getUnitSpace() > allUnitSpace){
            throw new IllegalArgumentException("insufficient space!");
        }
        units.add(u);
        usedUnitSpace += u.getUnitSpace();
    }


    public void PossessBlock(Block b){
        b.setAbsorbed(true, this.name);
        possessedBlocks.add(b);
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int amount) {
        this.gold += amount;
    }

    private int getFood() {
        return food;
    }

    public void addFood(int amount) {
        this.food += amount;
    }

    public String getName() {
        return name;
    }

    public List<Structure> getStructures() {
        return structures;
    }

    List<Unit> getUnits() {
        return units;
    }

    private List<Block> getPossessedBlocks() {
        return possessedBlocks;
    }

}

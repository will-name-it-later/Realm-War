package org.realm_war.Models;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.realm_war.Models.blocks.Block;
import org.realm_war.Models.blocks.EmptyBlock;
import org.realm_war.Models.structure.classes.Market;
import org.realm_war.Models.structure.classes.Structure;
import org.realm_war.Models.structure.classes.TownHall;
import org.realm_war.Models.units.Unit;

import javax.swing.*;

public class Realm {
    private GameState gameState = new GameState();
    private int gold;
    private final int ID;
    private TownHall townHall;
    private int food;
    private List<Structure> structures;
    private List<Unit> units;
    private List<Block> possessedBlocks;
    private int allUnitSpace;
    private int usedUnitSpace;
    private int availableUnitSpace;
    private int income;
    private boolean isBankrupt = false;
    private Color realmColor;

    public Realm(int ID) {
        this.ID = ID;
        structures = new ArrayList<>();
        units = new ArrayList<>();
        possessedBlocks = new ArrayList<>();
        this.townHall = null;
        this.allUnitSpace = 5;
        this.usedUnitSpace = 0;
        this.availableUnitSpace =0;
        this.gold = 25;
        this.food = 25;
        this.income = 0;
    }

    public void updateResources(GameState gameState) {
        for (Structure s : structures){
            s.performTurnAction(this, gameState);
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

        if (gold <= 0){
            JOptionPane.showMessageDialog(null,
                    "You have run out of gold! All your units and structures have been lost.",
                    "warning",
                    JOptionPane.WARNING_MESSAGE);

            gold = 0;
            units.clear();
            structures.clear();
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
        if (canBuildStructure(s)) {
            structures.add(s);
            gold -= s.getMaintenanceCost();
            allUnitSpace--;
        } else {
            throw new IllegalArgumentException("Can't build structure here!");
        }
    }

    public void removeStructure(Structure s){
        structures.remove(s);
        allUnitSpace++;
    }

    public void addUnit(Unit u){
        if (usedUnitSpace + u.getUnitSpace() > allUnitSpace){
            throw new IllegalArgumentException("insufficient space!");
        }
        units.add(u);
        usedUnitSpace += u.getUnitSpace();
    }

    public void removeUnit(Unit u){
        units.remove(u);
        usedUnitSpace -= u.getUnitSpace();
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

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public void setBankrupt(boolean bankrupt) {
        this.isBankrupt = bankrupt;
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

    public void setGold (int n){ this.gold = n; }

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

    public int getAvailableUnitSpace(){
       return this.availableUnitSpace;
    }
    public void addAvailableUnitSpace(int amount){
       this.availableUnitSpace += amount;
    }

    public int getUsedUnitSpace() {
        return getAllUnitSpace() - units.size();
    }

    public int getAllUnitSpace() {
        return possessedBlocks.size();
    }

    public List<Block> getPossessedBlocks() {
        return possessedBlocks;
    }


    // a quick-to-write method for realm income
    public int getIncome() {
        int income = possessedBlocks.stream().mapToInt(b -> b.getResourceItem("gold")).sum();
        income -= structures.stream().mapToInt(Structure::getMaintenanceCost).sum();
        income -= units.stream().mapToInt(Unit::getPayment).sum();
        income +=
                structures.stream().filter(s -> s instanceof Market || s instanceof TownHall)
                        .mapToInt(s -> switch (s){
                            case Market m -> m.getGoldProduction();
                            case TownHall t -> t.produceGoldPerTurn();
                            default -> 0;
                        })
                        .sum();
        return income;
    }
}

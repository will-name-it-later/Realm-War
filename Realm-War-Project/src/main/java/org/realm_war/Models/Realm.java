package org.realm_war.Models;

import java.util.ArrayList;
import java.util.List;

import org.realm_war.Models.blocks.Block;
import org.realm_war.Models.structure.classes.Farm;
import org.realm_war.Models.structure.classes.Market;
import org.realm_war.Models.structure.classes.Structure;
import org.realm_war.Models.structure.classes.TownHall;
import org.realm_war.Models.units.Unit;

public class Realm {
    private int gold;
    private final String name;
    private int food;
    private List<Structure> structures;
    private List<Unit> units;
    private List<Block> possessedBlocks;

    public Realm(String name) {
        this.name = name;
        structures = new ArrayList<>();
        units = new ArrayList<>();
        possessedBlocks = new ArrayList<>();
    }

    public void updateResources(){
        for (Structure s : structures){
            if (s instanceof TownHall){
                this.gold += ((TownHall) s).produceGoldPerTurn();
                this.food += ((TownHall) s).produceFoodPerTurn();
            } else if (s instanceof Farm) {
                this.food += ((Farm) s).produceFoodPerTurn();
            }else if(s instanceof Market){
                this.gold += ((Market) s).produceGoldPerTurn();
            }
        }

        for (Structure s : structures){
            gold -= s.getMaintenanceCost();
        }

        // todo after Block package is completed
        /*
         * for (Block b : possessedBlocks){
         * }
         */

        for (Unit u : units){
            gold -= u.getPayment();
            food -= u.getRation();
        }
    }

    public void addStructure(Structure s){
        structures.add(s);
    }

    public void addUnit(Unit u){
        units.add(u);
    }


    public void addPossessedBlock(Block b){
        possessedBlocks.add(b);
    }

    private int getGold() {
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

    private String getName() {
        return name;
    }

    private List<Structure> getStructures() {
        return structures;
    }

    private List<Unit> getUnits() {
        return units;
    }

    private List<Block> getPossessedBlocks() {
        return possessedBlocks;
    }

}

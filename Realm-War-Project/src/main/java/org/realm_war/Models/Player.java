package org.realm_war.Models;

import java.util.ArrayList;
import java.util.List;

import org.realm_war.Models.structure.classes.Structure;
import org.realm_war.Models.units.Unit;
import org.realm_war.Utilities.HelperMethods;

public class Player {
    private String name;
    private final int ID; 
    
    private List<Unit> units;
    private List<Structure> structrues;

    private Position townHallPosition;
    private boolean isDestroyed;
    
    private int gold;
    private int food;

    public Player(String name){
        this.ID = HelperMethods.idGenerator();
        this.name = name;

        this.units = new ArrayList<Unit>();
        this.structrues = new ArrayList<Structure>();

        this.townHallPosition = new Position();
        this.isDestroyed = false;

        this.gold = 100;
        this.food = 100;
    }

    //getters and setters
    public void addGold(int amount) {
        gold += amount;
    }
    public void subtractGold(int amount) {
        gold -= amount;
    }
    public int getGold() {
        return gold;
    }
    public void addFood(int amount) {
        food += amount;
    }
    public void subtractFood(int amount) {
        food -= amount;
    }
    public int getFood() {
        return food;
    }
    public void addStructure (Structure s){
        structrues.add(s);
    }
    public void remevoStructure (Structure s){
        structrues.remove(s);
    }
    public List<Structure> getStructures(){
        return structrues;
    } 
    public void addUnit(Unit u) {
        units.add(u);
    }
    public void removeUnit(Unit u) {
        units.remove(u);
    }
    public List<Unit> getUnits() {
        return units;
    }
    public boolean isDestroyed() {
        return isDestroyed;
    }
    public void setDystroyed(boolean d) {
        isDestroyed = d;
    }
    public int getId() {
        return ID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

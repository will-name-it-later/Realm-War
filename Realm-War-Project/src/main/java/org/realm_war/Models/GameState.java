package org.realm_war.Models;

import java.util.ArrayList;
import java.util.List;

import org.realm_war.Models.blocks.Block;
import org.realm_war.Models.blocks.EmptyBlock;
import org.realm_war.Models.blocks.VoidBlock;
import org.realm_war.Models.structure.classes.Structure;
import org.realm_war.Models.structure.classes.TownHall;
import org.realm_war.Models.units.Unit;
import org.realm_war.Utilities.Constants;

public class GameState {
    //Track game progress
    private int currentTurn = 1;
    private Player currentPlayer;
    private List<Player> players;
    private boolean isGameOver;
    private int gridSize = Constants.getMapSize();
    private Block[][] mapGrid = new Block[gridSize][gridSize];

    private static List<Realm> realms = new ArrayList<>();

    public static List<Unit> getAllUnits() {
        List<Unit> allUnits = new ArrayList<>();
        for (Realm realm : realms) {
            allUnits.addAll(realm.getUnits());
        }
        return allUnits;
    }

    public static void addRealm(Realm realm) {
        realms.add(realm);
    }

    public static Realm getRealmByKingdomID(String RealmID) {
        for (Realm realm : realms) {
            if (realm.getName().equals(RealmID)) {
                return realm;
            }
        }
        return null;
    }

    public static List<Realm> getRealm() {
        return realms;
    }

    //Turn management
    public void nextTurn() {
        if (players.isEmpty()) return;
        this.currentTurn += 1;
        this.currentPlayer = players.get((currentTurn - 1) % players.size());
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public boolean isGameOver() {
        int realmsWithTownHall = 0;
        for (Realm realm : realms) {
            boolean hasTownHall = realm.getStructures().stream()
                    .anyMatch(s -> s instanceof TownHall && !s.isDestroyed());
            if (hasTownHall) {
                realmsWithTownHall++;
            }
        }
        return realmsWithTownHall <= 1;
    }

    public Realm getWinner() {
        if (!isGameOver()) return null;

        for (Realm realm : realms) {
            boolean hasTownHall = realm.getStructures().stream()
                    .anyMatch(s -> s instanceof TownHall && !s.isDestroyed());
            if (hasTownHall) {
                return realm; // Winner
            }
        }
        return null; // In case all TownHalls are destroyed simultaneously
    }

    //Map Management
    public void mapInitializer() {
        //fill the grid with
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                mapGrid[i][j] = new EmptyBlock(new Position(i, j));
            }
        }
        // Optionally fill borders with VoidBlocks
        for (int i = 0; i < gridSize; i++) {
            mapGrid[i][0] = new VoidBlock(new Position(i, 0));
            mapGrid[i][gridSize - 1] = new VoidBlock(new Position(i, gridSize - 1));
        }
        for (int j = 0; j < gridSize; j++) {
            mapGrid[0][j] = new VoidBlock(new Position(0, j));
            mapGrid[gridSize - 1][j] = new VoidBlock(new Position(gridSize - 1, j));
        }
    }

    public void updateMap(List<Realm> realms) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                mapGrid[i][j] = new EmptyBlock(new Position(i, j));
            }
        }
        for (Realm realm : realms){
            List<Structure> structures = realm.getStructures();
            List<Unit> units = realm.getUnits();

            for (Structure s : structures){
               Position pos = s.getPosition();
               mapGrid[pos.getX()][pos.getY()] = s.getBaseBlock();
            }
            for (Unit u : units){
                Position pos = u.getPosition();
                mapGrid[pos.getX()][pos.getY()].setUnit(u);
            }
        }
    }

    //Interation Helpers
    public Structure getStructureAt(Position pos){
       return mapGrid[pos.getX()][pos.getY()].getStructure();
    }
    public Unit getUnitAt(Position pos){
        return mapGrid[pos.getX()][pos.getY()].getUnit();
    }
    public Block getBlockAt(Position pos){
        return mapGrid[pos.getX()][pos.getY()].getStructure().getBaseBlock();
    }
    public boolean isOccupied(Position pos){
        return mapGrid[pos.getX()][pos.getY()].getStructure().getBaseBlock().isOccupied();
    }

}

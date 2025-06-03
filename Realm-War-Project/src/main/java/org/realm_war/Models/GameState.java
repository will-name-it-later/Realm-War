package org.realm_war.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.realm_war.Models.blocks.Block;
import org.realm_war.Models.blocks.EmptyBlock;
import org.realm_war.Models.blocks.ForestBlock;
import org.realm_war.Models.blocks.VoidBlock;
import org.realm_war.Models.structure.classes.Structure;
import org.realm_war.Models.structure.classes.TownHall;
import org.realm_war.Models.units.Unit;
import org.realm_war.Utilities.Constants;

import static java.lang.Integer.parseInt;

public class GameState {
    //Track game progress
    private int currentTurn = 1;
    private int turns = 1;
    private Player currentPlayer;
    private List<Player> players = new ArrayList<>();
    private boolean running;
    private boolean isGameOver;
    private static int gridSize = Constants.getMapSize();
    private static Block[][] mapGrid = new Block[gridSize][gridSize];

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

    public static Realm getRealmByRealmID(String RealmID) {
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
        this.currentTurn = (currentTurn + 1) % players.size();
        if (currentTurn == 0) {
            turns++;
        }
        this.currentPlayer = players.get(currentTurn);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
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
    public static void mapInitializer() {
        mapGrid = new Block[gridSize][gridSize];  // ✅ initialize the array

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                mapGrid[i][j] = new EmptyBlock(new Position(i, j));
            }
        }

        // Fill borders with VoidBlocks
        for (int i = 0; i < gridSize; i++) {
            mapGrid[i][0] = new VoidBlock(new Position(i, 0));
            mapGrid[i][gridSize - 1] = new VoidBlock(new Position(i, gridSize - 1));
        }
        for (int j = 0; j < gridSize; j++) {
            mapGrid[0][j] = new VoidBlock(new Position(0, j));
            mapGrid[gridSize - 1][j] = new VoidBlock(new Position(gridSize - 1, j));
        }
    }

    public void initializeTownHalls() {
        Random rand = new Random();

        Position[] positions = {
                new Position(1, 1),
                new Position(gridSize - 2, gridSize - 2),
                new Position(1, gridSize - 2),
                new Position(gridSize - 2, 1),
        };

        for (int i = 0; i < players.size(); i++) {
            Realm realm = realms.get(i);
            Position pos = positions[i];

            // Example values — adjust as needed
            int goldProduction = 10;
            int foodProduction = 5;
            int maxLevel = 5;
            int durability = 100;
            int maintenance = 2;
            int kingdomId = rand.nextInt(1000);

            Block baseBlock = new EmptyBlock(pos);
            TownHall townHall = new TownHall(
                    goldProduction, foodProduction,
                    maxLevel, durability, maintenance,
                    pos, baseBlock, kingdomId
            );

            mapGrid[pos.getX()][pos.getY()] = baseBlock;
            mapGrid[pos.getX()][pos.getY()].setStructure(townHall);

            realm.setTownHall(townHall);
            realm.getStructures().add(townHall);
        }
    }

    public void setupGame() {
        // Initialize the grid
        mapInitializer();

        // Place forests & void blocks
        blockPlacer();

        // Create Realms for each Player and add them to the realm list
        realms.clear();  // Clear any previous realms if restarting
        for (Player player : players) {
            Realm realm = new Realm(player.getName());
            addRealm(realm);
        }

        // Place two Town Halls (one per realm)
        initializeTownHalls();

        // Set current player and turn info
        currentTurn = 0;
        turns = 1;
        if (!players.isEmpty()) {
            currentPlayer = players.get(0);
        }
    }

    public void updateMap(List<Realm> realms) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                mapGrid[i][j] = new EmptyBlock(new Position(i, j));
            }
        }
        for (Realm realm : realms) {
            List<Structure> structures = realm.getStructures();
            List<Unit> units = realm.getUnits();

            for (Structure s : structures) {
                Position pos = s.getPosition();
                mapGrid[pos.getX()][pos.getY()] = s.getBaseBlock();
            }
            for (Unit u : units) {
                Position pos = u.getPosition();
                mapGrid[pos.getX()][pos.getY()].setUnit(u);
            }
        }
    }

    //Call this after initializing the grid
    public static void blockPlacer() {
        for (Block[] blocks : mapGrid) {
            for (Block block : blocks) {
                if (Math.random() < 0.15) {
                    mapGrid[block.getPosition().getX()][block.getPosition().getY()] = new VoidBlock(block.getPosition());
                } else if (Math.random() >= 0.15 && Math.random() < 0.3) {
                    mapGrid[block.getPosition().getX()][block.getPosition().getY()] = new ForestBlock(block.getPosition());
                }
            }
        }
    }

    public static Block[][] getMapGrid() {
        return mapGrid;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public List<Realm> getRealms() {
        return realms;
    }

    public int getTurnNumber() {
        return turns;
    }

    public Realm getCurrentRealm() {
        return realms.get(currentTurn);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player newPlayer) {
        this.players.add(newPlayer);
    }

    //Interaction Helpers
    public Structure getStructureAt(Position pos) {
        return mapGrid[pos.getX()][pos.getY()].getStructure();
    }

    public Unit getUnitAt(Position pos) {
        return mapGrid[pos.getX()][pos.getY()].getUnit();
    }

    public Block getBlockAt(Position pos) {
        return mapGrid[pos.getX()][pos.getY()].getStructure().getBaseBlock();
    }

    public boolean isOccupied(Position pos) {
        return mapGrid[pos.getX()][pos.getY()].getStructure().getBaseBlock().isOccupied();
    }

}

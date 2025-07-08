package org.realm_war.Models;

import java.awt.*;
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
import org.realm_war.Utilities.HelperMethods;


public class GameState {
    //Track game progress
    private int currentTurn = 0;
    private int turns = 1;
    private List<Player> players = new ArrayList<>();
    private List<Realm> realms = new ArrayList<>();
    private boolean running;
    private boolean isGameOver;
    private int gridSize = Constants.getMapSize();
    private Block[][] mapGrid = new Block[gridSize][gridSize];
    private Unit selectedUnit;
    private Block targetBlock;

    private Color[] realmColors = new Color[] {
            new Color(183, 65, 14),   // Rust
            new Color(205, 133, 63),  // Peru
            new Color(218, 165, 32),  // Goldenrod
            new Color(85, 107, 47)    // Dark Moss Green
    };


    public List<Unit> getAllUnits() {
        List<Unit> allUnits = new ArrayList<>();
        for (Realm realm : realms) {
            allUnits.addAll(realm.getUnits());
        }
        return allUnits;
    }

    public void addRealm(Realm realm) {
        realms.add(realm);
    }

    public Realm getRealmByRealmID(int RealmID) {
        for (Realm realm : realms) {
            if (realm.getID() == RealmID) {
                return realm;
            }
        }
        return null;
    }

    public List<Realm> getRealm() {
        return realms;
    }

    //Turn management
    public void nextTurn() {
        if (players.isEmpty()) return;
        this.currentTurn = (currentTurn + 1) % players.size();
        if (this.currentTurn == 0) {
            this.turns++;
        }
    }

    public Player getCurrentPlayer() {
        return players.get(currentTurn);
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
    public void mapInitializer() {
        this.mapGrid = new Block[gridSize][gridSize];  // ✅ initialize the array

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                this.mapGrid[i][j] = new EmptyBlock(new Position(i, j));
            }
        }

        // Fill borders with VoidBlocks
        for (int i = 0; i < gridSize; i++) {
            this.mapGrid[i][0] = new VoidBlock(new Position(i, 0));
            this.mapGrid[i][gridSize - 1] = new VoidBlock(new Position(i, gridSize - 1));
        }
        for (int j = 0; j < gridSize; j++) {
            this.mapGrid[0][j] = new VoidBlock(new Position(0, j));
            this.mapGrid[gridSize - 1][j] = new VoidBlock(new Position(gridSize - 1, j));
        }
    }

    public void initializeTownHalls() {
        int gridSize = Constants.getMapSize();

        Position[][] startingZones = {
                // Player 1 (Top-Left)
                new Position[]{ new Position(1, 1),
                        new Position(1, 2), new Position(2, 1), new Position(2, 2), new Position(1, 3), new Position(3, 1) },
                // Player 2 (Bottom-Right)
                new Position[]{ new Position(gridSize - 2, gridSize - 2), new Position(gridSize - 2, gridSize - 3), new Position(gridSize - 3, gridSize - 2), new Position(gridSize - 3, gridSize - 3), new Position(gridSize - 2, gridSize - 4), new Position(gridSize - 4, gridSize - 2) },
                // Player 3 (Top-Right)
                new Position[]{ new Position(1, gridSize - 2), new Position(1, gridSize - 3), new Position(2, gridSize - 2), new Position(2, gridSize - 3), new Position(1, gridSize - 4), new Position(3, gridSize - 2) },
                // Player 4 (Bottom-Left)
                new Position[]{ new Position(gridSize - 2, 1), new Position(gridSize - 2, 2), new Position(gridSize - 3, 1), new Position(gridSize - 3, 2), new Position(gridSize - 2, 3), new Position(gridSize - 4, 1) }
        };

        // Clear all starting zones to guarantee they are EmptyBlocks.
        for (int i = 0; i < players.size(); i++) {
            for (Position pos : startingZones[i]) {
                mapGrid[pos.getX()][pos.getY()] = new EmptyBlock(pos);
            }
        }

        // Initialize each player in their clean zone.
        for (int i = 0; i < players.size(); i++) {
            Realm realm = realms.get(i);
            Position[] playerZone = startingZones[i];
            Position townHallPos = playerZone[0]; // The first block is the Town Hall.

            // Create the Town Hall object.
            TownHall townHall = new TownHall(10, 5, 5, 100, 2, townHallPos, mapGrid[townHallPos.getX()][townHallPos.getY()], realm.getID());
            realm.setTownHall(townHall);
            realm.getStructures().add(townHall);
            townHall.startStructureLoop(realm, this);

            // Iterate through all 6 blocks in the zone to set them up.
            for (int j = 0; j < playerZone.length; j++) {
                Position currentPos = playerZone[j];
                Block block = mapGrid[currentPos.getX()][currentPos.getY()];

                // Place the structure on the first block.
                if (j == 0) {
                    block.setStructure(townHall);
                }

                block.setOwnerID(realm.getID());
                block.setOwnerColor(realm.getRealmColor());
                realm.possessBlock(block, false);
            }
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
            Realm realm = new Realm(HelperMethods.idGenerator());
            addRealm(realm);
        }
        for (int i = 0; i < realms.size() ;i++){
            Realm realm = realms.get(i);
            realm.setRealmColor(realmColors[i%realms.size()]);
        }

        // Place two Town Halls (one per realm)
        initializeTownHalls();

        // Set current player and turn info
        currentTurn = 0;
        turns = 1;
    }

    public void updateMap(List<Realm> realms) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                this.mapGrid[i][j] = new EmptyBlock(new Position(i, j));
            }
        }
        for (Realm realm : realms) {
            List<Structure> structures = realm.getStructures();
            List<Unit> units = realm.getUnits();

            for (Structure s : structures) {
                Position pos = s.getPosition();
                this.mapGrid[pos.getX()][pos.getY()] = s.getBaseBlock();
            }
            for (Unit u : units) {
                Position pos = u.getPosition();
                this.mapGrid[pos.getX()][pos.getY()].setUnit(u);
            }
        }
    }

    //Call this after initializing the grid
    public void blockPlacer() {
        Random rand = new Random();
        int forestsToPlace = 80;
        int voidsToPlace = 30;

        while (forestsToPlace > 0) {
            int x = rand.nextInt(1, this.mapGrid.length - 2);
            int y = rand.nextInt(1, this.mapGrid[0].length - 2);
            if (!this.mapGrid[x][y].hasStructure()) {
                this.mapGrid[x][y] = new ForestBlock(new Position(x, y));
                forestsToPlace--;
            }
        }
        while (voidsToPlace > 0) {
            int x = rand.nextInt(1, this.mapGrid.length - 2);
            int y = rand.nextInt(1, this.mapGrid[0].length - 2);
            if (!this.mapGrid[x][y].hasStructure()) {
                this.mapGrid[x][y] = new VoidBlock(new Position(x, y));
                voidsToPlace--;
            }
        }
    }

    public Block[][] getMapGrid() {
        return this.mapGrid;
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

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public Block getTargetBlock() {
        return targetBlock;
    }

    public void setSelectedUnit(Unit unit) {
        this.selectedUnit = unit;
    }

    public void setTargetBlock(Block block) {
        this.targetBlock = block;
    }

    //Interaction Helpers
    public Structure getStructureAt(Position pos) {
        return this.mapGrid[pos.getX()][pos.getY()].getStructure();
    }

    public List<Structure> getAllStructures() {
        List<Structure> all = new ArrayList<>();
        for (Realm r : realms) {
            all.addAll(r.getStructures());
        }
        return all;
    }


    public Unit getUnitAt(Position pos) {
        return this.mapGrid[pos.getX()][pos.getY()].getUnit();
    }


    public Color[] getRealmColors() {
        return realmColors;
    }

    public Block getBlockAt(Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        if (x < 0 || y < 0 || x >= this.mapGrid.length || y >= this.mapGrid[0].length) return null;
        return this.mapGrid[x][y];
    }

    public void setBlockAt(Position pos, Block block) {
        this.mapGrid[pos.getX()][pos.getY()] = block;
    }

    public boolean isOccupied(Position pos) {
        return this.mapGrid[pos.getX()][pos.getY()].getStructure().getBaseBlock().isOccupied();
    }

}

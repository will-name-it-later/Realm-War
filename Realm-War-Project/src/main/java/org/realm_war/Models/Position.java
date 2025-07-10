package org.realm_war.Models;

import org.realm_war.Utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class Position {
    private int x;
    private int y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double distanceTo(Position other) {
        int dx = this.getX() - other.getX();
        int dy = this.getY() - other.getY();
        return Math.max(Math.abs(dx), Math.abs(dy));
    }

    public static boolean isWithinBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < Constants.getMapSize() && y < Constants.getMapSize();
    }


    public static List<Position> getAdjacent(Position center) {
        List<Position> neighbors = new ArrayList<>();
        int[][] offsets = {{1,0},{-1,0},{0,1},{0,-1}}; // 4-directional
        for (int[] offset : offsets) {
            int nx = center.getX() + offset[0];
            int ny = center.getY() + offset[1];
            if (isWithinBounds(nx, ny)) {
                neighbors.add(new Position(nx, ny));
            }
        }
        return neighbors;
    }


    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
}

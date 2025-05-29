package org.realm_war.Models;

import java.util.ArrayList;
import java.util.List;

import org.realm_war.Models.structure.classes.Structure;
import org.realm_war.Models.units.Unit;
import org.realm_war.Utilities.HelperMethods;

public class Player {
    private final String name;
    private int score;
    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}

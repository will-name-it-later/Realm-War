package org.realm_war.Models;

import java.util.ArrayList;
import java.util.List;

import org.realm_war.Models.units.Unit;

public class GameState {
    //Track game progress
    private int currentTurn;
    Player currentPlayer;
    List<Player> players;
    boolean isGameOver;

    private static List<Realm> realms = new ArrayList<>();
    
    public static List<Unit> getAllUnits() {
        List<Unit> allUnits = new ArrayList<>();
        for (Realm realm : realms) {
            allUnits.addAll(realm.getUnits());
        }
        return allUnits;
    } 

    public static void addRealm (Realm realm){
        realms.add(realm);
    }

    public static Realm getRealmByKingdomID(String RealmID){
        for (Realm realm : realms){
            if (realm.getName().equals(RealmID)){
                return realm;
            }
        }
        return null;
    }

    public static List<Realm> getRealm (){
        return realms;
    }
}
